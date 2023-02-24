import torch
from torch import nn

import torch.nn.functional as F


class Mapping(nn.Module):
    def __init__(self, configs):
        super(Mapping, self).__init__()

        # dropout
        self.dropout = configs['dropout']

        # 文本填充字段，填充在首位
        self.start_word_vec = nn.Parameter(torch.zeros(configs['word_dim']).float(), requires_grad=True)

        # 视频填充字段，表示预测结果
        self.end_frame_vec = nn.Parameter(torch.zeros(configs['frame_dim']).float(), requires_grad=True)

        # 线性转换层
        self.frame_fc = nn.Linear(configs['frame_dim'], configs['hidden_size'])
        self.word_fc = nn.Linear(configs['word_dim'], configs['hidden_size'])

    # TODO 改进点，我感觉这里通过增加一个维度用于生成预测结果不太靠谱？
    def forward(self, frames_feat, frames_len, words_feat, words_len):
        """
        将文本和视频映射到同一个空间；其中，文本添加一个token在开头，训练后用于后续语义补全
        视频添加一个token在结尾，训练后，作为产出用于预测跨模态融合后的信息如何预测center/width
        :param frames_len:
        :param words_len:
        :param words_feat: 文本特征
        :param frames_feat: 视频特征
        :return:
        """
        # 视频处理
        bsz, n_frames, _ = frames_feat.shape
        # TODO 这里是深拷贝还是浅拷贝，复制到指定batch_size大小？？？浅拷贝；后期时候会保持一致？
        pred_vec = self.end_frame_vec.view(1, 1, -1).expand(bsz, 1, -1)
        frames_feat = torch.cat([frames_feat, pred_vec], dim=1)
        frames_feat = F.dropout(frames_feat, self.dropout, self.training)
        frames_feat = self.frame_fc(frames_feat)

        # 生成mask
        self._generate_mask()

        # 文本处理
        # TODO 这里是深拷贝还是浅拷贝，复制到指定batch_size大小？？？
        word_vec = self.start_word_vec.view(1, 1, -1).expand(bsz, 1, -1)
        words_feat = torch.cat([words_feat, word_vec], dim=1)
        words_feat = F.dropout(words_feat, self.dropout, self.training)
        words_feat = self.word_fc(words_feat)

        return frames_feat, frames_mask, words_feat, words_mask

    def _generate_mask(self, x, x_len):
        mask = []
        for x_l in x_len:
            mask.append(torch.zeros([x.size(1)]).byte().cuda())
            mask[-1][:x_l] = 1
        mask = torch.stack(mask, 0)
        return mask

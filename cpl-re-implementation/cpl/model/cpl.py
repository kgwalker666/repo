from torch import nn
from cpl.model.modules.interaction import DualTransformer
from cpl.model.modules.mapping import Mapping
from cpl.model.modules.utils import SinusoidalPositionalEmbedding
from cpl.model.modules.prediction import Prediction


class CPL(nn.Module):
    def __init__(self, configs):
        super(CPL, self).__init__()

        # 工具
        # self.word_pos_encoder = SinusoidalPositionalEmbedding(configs['hidden_size'])

        # # 映射层
        # self.mapping = Mapping(configs['mapping'])
        #
        # # 交互层
        # self.trans = DualTransformer(configs['interaction'])
        #
        # # 预测层
        # self.prediction = Prediction(configs['prediction'])

    def forward(self, frames_feat, frames_len, words_feat, words_len, words_weight, epoch):
        """
        前向传播获取网络执行结果
        :param frames_feat: 视频特征
        :param frames_len: 视频有效特征长度
        :param words_feat: 文本特征
        :param words_len: 文本有效特征长度
        :param words_weight: 单词权重，根据词性划分权重
        :param epoch: 当前的epoch次数
        :return: 模型执行结果
        """
        pass

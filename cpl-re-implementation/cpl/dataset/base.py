import nltk
import numpy as np
import torch
from torch.utils.data import Dataset
from cpl.utils.file_utils import load_json


class BaseDataset(Dataset):
    def __init__(self, data_path, vocab, configs):
        """
        cls(data_path=args['train_data'], vocab=vocab, args=args, is_training=True, split='train')
        """
        # word->feature
        self.vocab = vocab

        # 保存配置
        self.configs = configs

        # 加载注解文件
        self.ori_data = load_json(data_path)

        self.max_num_frames = configs['max_num_frames']

        self.max_num_words = configs['max_num_words']

        # 已经读取的单词向量文件内容
        """
        {
            "w2id": {       // 12825
                "<PAD>": 0
            },
            "id2vec": [     // 12825
                (300, ),
                (300, )
                ...
            ],
            "counter": {    // 12824        // 出现的次数
                "woman": 7196
            }
        }
        """
        self.keep_vocab = dict()
        for w, _ in vocab['counter'].most_common(configs['vocab_size']):  # 选择出现最多的前k个
            self.keep_vocab[w] = self.vocab_size  # 从1开始编号

    def _load_frame_features(self, vid):
        raise NotImplementedError

    @property
    def vocab_size(self):
        return len(self.keep_vocab) + 1

    def __len__(self):
        return len(self.ori_data)

    def __getitem__(self, index):  # 给定index，获取其对应的数据
        vid, duration, timestamps, sentence = self.ori_data[index]
        duration = float(duration)

        weights = []  # Probabilities to be masked
        words = []
        # TODO 不在keep_vocab的词会被直接丢弃
        for word, tag in nltk.pos_tag(nltk.tokenize.word_tokenize(sentence)):
            word = word.lower()
            if word in self.keep_vocab:
                if 'NN' in tag:
                    weights.append(2)
                elif 'VB' in tag:
                    weights.append(2)
                elif 'JJ' in tag or 'RB' in tag:
                    weights.append(2)
                else:
                    weights.append(1)
                words.append(word)
        words_id = [self.keep_vocab[w] for w in words]
        words_feat = [self.vocab['id2vec'][self.vocab['w2id'][w]].astype(np.float32) for w in words]
        frames_feat = self._sample_frame_features(self._load_frame_features(vid))

        return {
            'frames_feat': frames_feat,
            'words_feat': words_feat,
            'words_id': words_id,
            'weights': weights,
            'raw': [vid, duration, timestamps, sentence]
        }

    def _sample_frame_features(self, frames_feat):  # (889, 500) -> (200, 500)
        num_clips = self.configs['max_num_frames']
        keep_idx = np.arange(0, num_clips + 1) / num_clips * len(frames_feat)
        keep_idx = np.round(keep_idx).astype(np.int64)
        keep_idx[keep_idx >= len(frames_feat)] = len(frames_feat) - 1
        frames_feat1 = []
        for j in range(num_clips):
            s, e = keep_idx[j], keep_idx[j + 1]
            assert s <= e
            if s == e:
                frames_feat1.append(frames_feat[s])
            else:
                frames_feat1.append(frames_feat[s:e].mean(axis=0))
        return np.stack(frames_feat1, 0)


def build_collate_data(max_num_frames, max_num_words, frame_dim, word_dim):
    """
    max_num_frames：
    max_num_words：
    frame_dim：
    word_dim：
    """

    def collate_data(samples):  # 样本list整合成一个batch，自定义
        bsz = len(samples)
        batch = {
            'raw': [sample['raw'] for sample in samples],
        }

        frames_len = []
        words_len = []

        for i, sample in enumerate(samples):
            frames_len.append(min(len(sample['frames_feat']), max_num_frames))  # 限制最大长度
            words_len.append(min(len(sample['words_id']), max_num_words))

        frames_feat = np.zeros([bsz, max_num_frames, frame_dim]).astype(np.float32)
        words_feat = np.zeros([bsz, max(words_len), word_dim]).astype(np.float32)  # max + 1
        words_id = np.zeros([bsz, max(words_len)]).astype(np.int64)
        weights = np.zeros([bsz, max(words_len)]).astype(np.float32)
        for i, sample in enumerate(samples):
            frames_feat[i, :len(sample['frames_feat'])] = sample['frames_feat']
            keep = min(len(sample['words_feat']), words_feat.shape[1])
            words_feat[i, :keep] = sample['words_feat'][:keep]
            keep = min(len(sample['words_id']), words_id.shape[1])
            words_id[i, :keep] = sample['words_id'][:keep]
            keep = min(len(sample['weights']), weights.shape[1])
            tmp = np.exp(sample['weights'][:keep])
            weights[i, :keep] = tmp / np.sum(tmp)  # 划分成概率，相加=1

        batch.update({
            'net_input': {
                'frames_feat': torch.from_numpy(frames_feat),
                'frames_len': torch.from_numpy(np.asarray(frames_len)),
                'words_feat': torch.from_numpy(words_feat),
                'words_id': torch.from_numpy(words_id),
                'words_weight': torch.from_numpy(weights),
                'words_len': torch.from_numpy(np.asarray(words_len)),
            }
        })
        return batch

    return collate_data

import h5py
import numpy as np
from cpl.dataset.base import BaseDataset, build_collate_data


class ActivityNet(BaseDataset):
    def _load_frame_features(self, vid):
        with h5py.File(self.args['feature_path'], 'r') as fr:
            return np.asarray(fr[vid]['c3d_features']).astype(np.float32)

    def __init__(self, data_path, vocab, configs):
        """
        cls(data_path=args['train_data'], vocab=vocab, args=args, is_training=True, split='train')
        """
        super().__init__(data_path, vocab, configs)

        # 闭包，返回一个函数
        self.collate_fn = build_collate_data(configs['max_num_frames'], configs['max_num_words'],
                                             configs['frame_dim'], configs['word_dim'])

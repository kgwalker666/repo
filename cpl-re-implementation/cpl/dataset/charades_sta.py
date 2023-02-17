import h5py
import numpy as np
from cpl.dataset.base import BaseDataset, build_collate_data


class CharadesSTA(BaseDataset):
    def __init__(self, data_path, vocab, configs):
        super().__init__(data_path, vocab, configs)
        self.collate_fn = build_collate_data(configs['max_num_frames'], configs['max_num_words'],
                                             configs['frame_dim'], configs['word_dim'])

    def _load_frame_features(self, vid):
        with h5py.File(self.args['feature_path'], 'r') as fr:
            return np.asarray(fr[vid]).astype(np.float32)

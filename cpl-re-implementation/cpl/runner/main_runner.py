import logging
import pickle
from torch.utils.data import DataLoader
import cpl.dataset as da
import cpl.model as model
from cpl.utils.cuda_utils import move_to_cuda


class MainRunner(object):
    def __init__(self, global_configs):
        self.global_configs = global_configs

        # 构建数据集
        self._build_dataset()

        # 构建模型
        self.global_configs['model']['config']['prediction'][
            'vocab_size'] = self.train_set.vocab_size  # 高频词数量8000+1（未知），之后的语义推断从中选一个
        self.global_configs['model']['config']['prediction']['max_epoch'] = self.global_configs['train']['max_epoch']
        self._build_model()

    def _build_dataset(self):
        """
        构建数据集
        :return:
        """
        # 数据集相关参数
        dataset_configs = self.global_configs['dataset']

        # 加载对应的配置数据集的类
        cls = getattr(da, dataset_configs['dataset'], None)

        # 读取单词的特征向量
        with open(dataset_configs['vocab_path'], 'rb') as fp:
            vocab = pickle.load(fp)

        # 创建数据集类
        self.train_set = cls(data_path=dataset_configs['train_data'], vocab=vocab, configs=dataset_configs)
        self.test_set = cls(data_path=dataset_configs['test_data'], vocab=vocab, configs=dataset_configs)
        logging.debug('train: {} samples, test: {} samples'.format(len(self.train_set), len(self.test_set)))
        batch_size = self.global_configs['train']['batch_size']

        # TODO 只有训练才需要？
        def worker_init_fn(worker_id):
            def set_seed(seed):
                import random
                import numpy as np
                import torch

                random.seed(seed)
                np.random.seed(seed + 1)
                torch.manual_seed(seed + 3)
                torch.cuda.manual_seed(seed + 4)
                torch.cuda.manual_seed_all(seed + 4)

            set_seed(8 + worker_id)

        # 数据加载器
        self.train_loader = DataLoader(self.train_set, batch_size=batch_size, shuffle=True,
                                       # TODO 默认是2；这里设置为0使用同步加载方便调试
                                       collate_fn=self.train_set.collate_fn, num_workers=0,
                                       worker_init_fn=worker_init_fn)
        self.test_loader = DataLoader(self.test_set, batch_size=batch_size, shuffle=False,
                                      collate_fn=self.test_set.collate_fn,
                                      num_workers=0)

    def load_model(self, param):
        pass

    def train(self):
        best_results = None  # 最好结果
        for epoch in range(1, self.global_configs['train']['max_epoch'] + 1):
            logging.info('Start Epoch {}'.format(epoch))
            self._train_one_epoch(epoch)
            self._save_model(epoch)
            self._eval()
            logging.info('=' * 60)
        logging.info("最好结果\n{}".format(best_results))

    def eval(self):
        pass

    def _build_model(self):
        # 模型相关参数
        model_configs = self.global_configs['model']

        # 加载对应的配置模型的类
        model_cls = getattr(model, model_configs['name'])

        # 构建模型
        self.model = (model_cls(model_configs['config'])).cuda()

        logging.debug(self.model)

    def _train_one_epoch(self, epoch):
        self.model.train()

        for bid, batch in enumerate(self.train_loader, 1):
            # self.optimizer.zero_grad()  # 清空一轮梯度信息
            net_input = move_to_cuda(batch['net_input'])

    def _save_model(self, epoch):
        pass

    def _eval(self):
        pass

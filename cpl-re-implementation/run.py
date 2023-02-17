import argparse
import logging
import os
import sys

import yaml

from cpl.utils.file_utils import load_config, create_folders_if_exists_delete, save_json
from cpl.utils.zip_utils import do_zip_compress
from cpl.utils.logging_utils import logging_config
from cpl.runner.main_runner import MainRunner
from cpl.utils.seed_utils import fix_seed


def parse_args():
    """
    参数解析：
    config-path：配置文件路径
    resume：加载已训练的checkpoint位置
    eval：仅测试
    tag：本次实验的标记，会为其生成对应的目录存放所有的代码，日志，配置信息
    :return: 命令行参数
    """
    parser = argparse.ArgumentParser()

    parser.add_argument('--config-path', type=str, required=True, help='config file path')
    parser.add_argument('--resume', type=str, help='checkpoint path to resume')
    parser.add_argument('--eval', action='store_true', help='only evaluate')
    parser.add_argument('--tag', type=str, help='experiment tag')
    parser.add_argument('--handlers', type=list, default=['console'], help='experiment tag')

    return vars(parser.parse_args())


def main(args):
    """
    入口函数
    :return:
    """
    # 加载配置文件
    global_configs = load_config(args['config_path'])

    # 固定随机种子
    fix_seed(global_configs['train']['seed'])

    # 保存信息设置
    if args['tag']:
        # 当前保存目录
        tag_path = os.path.join(global_configs['train']['model_saved_path'], args['tag'])
        # 创建保存目录，如果存在则全部删除！！！（为了不干扰本次实验）
        create_folders_if_exists_delete(tag_path)
        # 压缩并保存代码
        do_zip_compress(os.path.dirname(__file__), os.path.join(tag_path, 'code_{}.zip'.format(args['tag'])))
        # 记录日志到文件
        args['handlers'].append('file#{}'.format(os.path.join(tag_path, 'log_{}.log'.format(args['tag']))))
        # 模型保存位置
        global_configs['train']['model_saved_path'] = os.path.join(
            global_configs['train']['model_saved_path'], args['tag'])
        # 保存命令行参数
        save_json(args, os.path.join(tag_path, 'args_{}.json'.format(args['tag'])))

    # 日志配置
    logging_config(args['handlers'])

    # 展示运行信息
    logging.debug("本次训练参数\n{}".format(yaml.safe_dump(global_configs).strip()))

    # 创建启动类
    runner = MainRunner(global_configs)

    # 加载原本模型
    if args['resume']:
        runner.load_model(args['resume'])

    # 测试or训练
    runner.train() if not args['eval'] else runner.eval()


if __name__ == '__main__':
    main(parse_args())
 
import json
import shutil

import yaml
import os


def load_config(config_path):
    """
    加载yaml文件
    :param config_path:文件位置
    :return: 文件内容
    """
    with open(config_path, 'r', encoding='utf-8') as fp:
        return yaml.safe_load(fp)


def create_folders_if_exists_delete(folder_path):
    """
    创建目录结构，如果已存在，则删除对应目录及其目录下所有内容
    :param folder_path: 目录地址
    :return:
    """
    if os.path.exists(folder_path):
        shutil.rmtree(folder_path)
    os.makedirs(folder_path)


def save_json(content, filepath):
    """
    将给定内容保存成json文件
    :param content: 内容
    :param filepath: 文件路径
    :return:
    """
    with open(filepath, mode='w', encoding='utf-8') as fp:
        json.dump(content, fp, indent=2)


def load_json(filepath):
    with open(filepath, encoding='utf8') as fr:
        return json.load(fr)

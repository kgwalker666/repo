"""
针对判别器的预训练方法
模型输入
    batch->（句子，剪辑后的视频片段）
"""
import argparse


def parse_args():
    parser = argparse.ArgumentParser("talgan")
    parser.add_argument('--config-path', '-f')
    return vars(parser.parse_args())


def main():
    args = parse_args()

    # pass


if __name__ == '__main__':
    main()

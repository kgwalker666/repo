import argparse
import os
import random
from os import mkdir
from torch import optim
import torch
from mmn.config import cfg
from mmn.utils.logger import setup_logger
from mmn.utils.miscellaneous import save_config
from mmn.modeling import build_model
from mmn.data import make_data_loader
from mmn.utils.checkpoint import MmnCheckpointer


def train(cfg):
    model = build_model(cfg)
    device = torch.device(cfg.MODEL.DEVICE)
    model.to(device)

    learning_rate = cfg.SOLVER.LR * 1.0
    data_loader = make_data_loader(cfg, is_train=True)

    bert_params = []
    base_params = []
    for name, param in model.named_parameters():  # TODO 参数分为两种，bert和基本参数
        if "bert" in name:
            bert_params.append(param)
        else:
            base_params.append(param)

    param_dict = {'bert': bert_params, 'base': base_params}
    optimizer = optim.AdamW([{'params': base_params},
                             {'params': bert_params, 'lr': learning_rate * 0.1}], lr=learning_rate, betas=(0.9, 0.99),
                            weight_decay=1e-5)
    scheduler = optim.lr_scheduler.MultiStepLR(optimizer, milestones=cfg.SOLVER.MILESTONES, gamma=0.1)
    output_dir = cfg.OUTPUT_DIR
    save_to_disk = True
    checkpointer = MmnCheckpointer(cfg, model, optimizer, scheduler, output_dir, save_to_disk)
    arguments = {"epoch": 1}


def main():
    parser = argparse.ArgumentParser(description="Mutual Matching Network")
    parser.add_argument(
        "--config-file",
        default="configs/pool_tacos_128x128_k5l8.yaml",
        metavar="FILE",
        help="path to config file",
        type=str,
    )
    parser.add_argument("--local_rank", type=int, default=0)
    parser.add_argument(
        "--skip-test",
        dest="skip_test",
        help="Do not test the final model",
        action="store_true",
    )
    parser.add_argument(
        "opts",
        help="Modify config options using the command-line",
        default=None,
        nargs=argparse.REMAINDER,
    )

    args = parser.parse_args()
    seed = 25285
    random.seed(seed)
    torch.manual_seed(seed)
    torch.cuda.manual_seed_all(seed)
    torch.backends.cudnn.deterministic = True

    cfg.merge_from_file(args.config_file)
    cfg.merge_from_list(args.opts)
    cfg.freeze()

    output_dir = cfg.OUTPUT_DIR
    if output_dir:
        os.makedirs(output_dir, exist_ok=True)

    logger = setup_logger("mmn", output_dir)
    logger.info(args)

    logger.info("Loaded configuration file {}".format(args.config_file))
    with open(args.config_file, "r") as cf:
        config_str = "\n" + cf.read()
        logger.info(config_str)
    logger.info("Running with config:\n{}".format(cfg))

    output_config_path = os.path.join(cfg.OUTPUT_DIR, 'config.yml')
    logger.info("Saving config into: {}".format(output_config_path))
    # save overloaded model config in the output directory
    save_config(cfg, output_config_path)

    model = train(cfg)


if __name__ == "__main__":
    main()

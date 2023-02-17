import logging
import sys


def logging_config(handlers):
    level = logging.DEBUG
    fmt = '[%(levelname)s] %(asctime)s - %(message)s'
    # 打印到终端
    class_handlers = []
    for handler in handlers:
        if 'console' in handler:
            class_handlers.append(logging.StreamHandler(sys.stdout))
        elif 'file' in handler:
            class_handlers.append(logging.FileHandler(handler.split("#")[-1]))
    logging.basicConfig(level=level, format=fmt, handlers=class_handlers)

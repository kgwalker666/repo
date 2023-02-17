from torch import nn


class Prediction(nn.Module):
    def __init__(self, configs):
        super(Prediction, self).__init__()

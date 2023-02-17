from torch import nn


class SinusoidalPositionalEmbedding(nn.Module):
    def __init__(self, configs):
        super(SinusoidalPositionalEmbedding, self).__init__()

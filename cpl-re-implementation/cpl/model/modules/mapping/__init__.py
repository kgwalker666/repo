from torch import nn


class Mapping(nn.Module):
    def __init__(self, configs):
        super(Mapping, self).__init__()
        self.frame_fc = nn.Linear(configs['frames_input_size'], configs['hidden_size'])
        self.word_fc = nn.Linear(configs['words_input_size'], configs['hidden_size'])

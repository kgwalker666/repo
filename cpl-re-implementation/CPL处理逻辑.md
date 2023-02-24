# CPL处理逻辑

### 原始输入

```python
frames_feat		# torch.Size([32, 200, 500])，视频原始输入
frames_len		# 32
words_id		# torch.Size([32, 20])，单词与idx的映射，每一个idx表示一个单词
words_feat		# torch.Size([32, 21, 300])，文本原始输入【多一个(32,300)在最后，当前填充为0】
words_len		# torch.Size(32)，句子原始长度
weights			# torch.Size([32, 20])，根据单词词性划分的权重
epoch			# 当前迭代次数
```

### 1. 视频填充工作-最后一列

```python
self.pred_vec	# torch.Size(500)->torch.Size([32, 1, 500])，注意这里是浅拷贝，所有的batch共用的一个torch.Size(500)

frames_feat		# torch.Size([32, 200, 500])->torch.Size([32, 201, 500])，最后一个torch.Size(32, 500)由self.pred_vec填充

dropout，随机设置0.1为0，在frames_feat
```

### 2. 维度映射

```python
self.frame_fc	# frames_feat: torch.Size([32, 201, 500])->torch.Size([32, 201, 256])

# 为frames_feat生成有效性mask，只有最后一列是0，因为这是无效列，是pred_vec填充
frames_mask
```

### 3. 文本填充工作-第一列

```python
words_feat		# 填充第一列

dropout，随机设置0.1为0，在words_feat
```

### 4. 维度映射

```python
self.word_fc	

torch.Size([32, 21, 300]) -> torch.Size([32, 21, 256])

生成mask，其中填充的第一维算是【有效的】

生成位置信息：words_pos
```

### 5. 模态交融

```python
# 第一次，decoding=1；所有的输入前两个是视频，后两个是文本
1. 视频为空，文本处理【decoder2】
2. 文本处理（融入上下文信息），视频处理【decoder1】

1.1. mask语义反转，有效的为0，无效的变成1
1.2. 交换维度； torch.Size([32, 21, 256])->torch.Size([21, 32, 256])
1.3. 经过layer，三个相同结构
	x, 文本；torch.Size([21, 32, 256])
    non_pad_tgt_mask,文本mask；torch.Size([32, 21])
    src, 视频；None
    non_pad_src_mask,视频mask；None
    self.buffered_future_mask(x),单词与单词之间的关联性矩阵torch.Size([21, 21])，有效部分左下角+主对角线
    src_gauss_weight, [TODO]
    tgt_gauss_weight [TODO]
1.4. 单一layer分析
	1. 保存原始文本信息res
    2. MultiheadAttention，多头自注意力
		x, 
        x, 
        x, 文本；torch.Size([21, 32, 256])
        mask,文本mask；torch.Size([32, 21])
        attn_mask=self_attn_mask,单词与单词之间的关联性矩阵torch.Size([21, 21])，有效部分左下角+主对角线
        gauss_weight=tgt_gauss_weight None[TODO]
    3. dropout一些文本x
    4. 与原始文本信息相加 res+x
    5. layer_norm归一化
    6. 保存x成res
    7. linear->relu->linear
    8. dropout
    9. x = res+x增加原始信息
    10. layer_norm 归一化
    11. 返回：x，weight；这里的x是文本处理的结果（融入上下文信息）
1.5. multihead分析
	1. 数据有效性判断
    2. 子注意力走qkv相同路线
    	weight: torch.Size([768, 256])； torch.Size([768])
        query与weight第一部分相乘+bias(linear操作)
        	torch.Size([21, 32, 256]) * torch.Size([768, 256]) + torch.Size([768]) => torch.Size([21, 32, 768])
            chunk操作：将其分为三份，每一份是torch.Size([21, 32, 256])，分别是q,k,v
    3. q = q * scaling [TODO]
   	4. qkv相同处理，维度变换【多头注意力的体现，将信息划分成4份，样本量扩大了四分】
    	torch.Size([21, 32, 256]) -> torch.Size([21, 128, 64]) -> torch.Size([128, 21, 64])
    5. qk融合，torch.Size([128, 21, 64]) * torch.Size([128, 64, 21]) -> torch.Size([128, 21, 21])
    	生成的就是单词与单词之间的相关性，21*21是棋盘，128就是有效的信息，如果不多头，则只有样本数量的信息（每一个样本一个，现在由四个）
    6. 我们自己传入的attn_mask=self_attn_mask,单词与单词之间的关联性矩阵torch.Size([21, 21])，有效部分左下角+主对角线；将其右上角变成0，这部分是无效的 attn_weights += attn_mask 任何数+负无穷都是负无穷
    7. 我们传入的mask,文本mask；torch.Size([32, 21])
	    torch.Size([32, 4, 21, 21])
    	torch.Size([32, 1, 1, 21])，根据这个矩阵选择出有效的单词与单词之间的权重信息
    8. 对attn_weights进行归一化，所有样本对我的关注度之和为1
    9. dropout
    10. 根据关注度，与原始的数据v相乘; torch.Size([128, 21, 21]) * torch.Size([128, 21, 64])
    	torch.Size([128, 21, 64])，获取对齐权重的在分配，已经融入了上下问信息
    11. 转回原本维度；torch.Size([21, 32, 256]) -> linear -> torch.Size([21, 32, 256])
    12. 输出这个融入上下文信息后的attn注意力句子矩阵torch.Size([21, 32, 256])；及其词与词之间的权重信息；torch.Size([32, 21, 21])
最终的输出信息是：原始的温度，只不过融入了上下文信息


【decoder=1第二部分】
1. 视频融入上下文信息
2. 生成视频帧和单词之间一一对应的权重矩阵attn_weights
3. 将文本信息通过权重矩阵融入到视频中，生成跨模态信息

最终DualTransformer
enc_out：融入上下文信息的文本矩阵；out：融入上下文信息及文本信息的视频矩阵
```

### 6. 预测

```python
1. 获取之前填充的那一维度的视频信息，分别是32个样本分别的结果
h[:, -1](32,) torch.Size([32, 256]) -> linear -> torch.Size([32, 16]) -> sigmoid -> torch.Size([256, 2])
# 样本数量变成了256，是8组样本，每一组代表一个样本的预测结果
第一维度作为 gauss_center = torch.Size([256])
gauss_width = torch.Size([256])
```

### 7. 下采样视频/4

```python
frames_feat = torch.Size([32, 50, 256]) -> 将其复制8分，每一个预测一份 torch.Size([256, 50, 256])
frames_mask = torch.Size([32, 50]) -> torch.Size([256, 50])

# 根据预测结果，分别对原始的下采样视频进行自定义处理；获取每一个帧的权重信息
计算出一个权重信息，torch.Size([256, 50])

文本也变成8分，用于语义补全
```

### 8. 语义补全，复用之前的网络模块

```python
self.trans(
    props_feat, 		torch.Size([256, 50, 256])	视频特征，256是样本数量
    props_mask, 		视频mask
    words_feat1, 		文本特征torch.Size([256, 20, 256])
    words_mask1, 		文本mask
    decoding=2, 		标志
    gauss_weight=pos_weight, 	每一个视频特征的权重
    need_weight=True	需要weight
)

子注意力
根据生成的pos权重信息，影响其自注意力上下文信息，将其与原本计算的帧与帧之间的权重矩阵attn_weights，两者相乘，导致其最后生成的融入上下文信息的视频更加突出我想要的帧，使其视频信息有差异

交互注意力
通过gauss_weight影响文本融入视频信息时各个视频帧的权重

最终生成个性化的，补全语义的文本信息，torch.Size([256, 20, 256])

直接将其映射到词空间，看起选择了那个词；torch.Size([256, 20, 8001])

negative_proposal_mining没看懂？？torch.Size([256, 50])
根据这个权重，生成补全结果neg_h_1->neg_words_logit_1
neg_words_logit_2

原始的视频和文本映射计算出一个结果【正样本】ref_words_logit

'neg_words_logit_1': neg_words_logit_1,		负样本
'neg_words_logit_2': neg_words_logit_2,		负样本
'ref_words_logit': ref_words_logit,			正样本
'words_logit': words_logit,					预测结果
'words_id': words_id,
'words_mask': words_mask,
'width': gauss_width,
'center': gauss_center,
'gauss_weight': gauss_weight,
```

### 9. Loss计算-rec_loss

```python
计算预测结果离我们单词id的距离，正样本和我们的样本越小越好

```






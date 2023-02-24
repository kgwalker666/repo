package page.controller;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CounterController {
    StringRedisTemplate redisTemplate;

    public CounterController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/")
    public Map<String, Object> counter() {
        Long count = this.redisTemplate.opsForValue().increment("count");
        return Map.of("访问量", ObjectUtil.defaultIfNull(count, "获取失败"));
    }
}

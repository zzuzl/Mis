package cn.zzuzl.dao;

import cn.zzuzl.model.TermScore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class RedisDao {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void updateScore(String schoolNum, Object scores) throws JsonProcessingException {
        String json = new ObjectMapper().writeValueAsString(scores);
        redisTemplate.boundValueOps(schoolNum).set(json);
    }

    public List<TermScore> getScores(String schoolNum) throws IOException {
        String json = getJsonScores(schoolNum);
        List<TermScore> list = null;
        if(json != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            TermScore[] arr = objectMapper.readValue(json, TermScore[].class);
            list = Arrays.asList(arr);
        }

        return list;
    }

    public String getJsonScores(String schoolNum) {
        return (String) redisTemplate.boundValueOps(schoolNum).get();
    }
}

package cn.itcast.hotel;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
class TestIndex {

    @Autowired
    private RestHighLevelClient client;

    @Test
    void testCreateIndex() throws IOException {

        // mapping结构
        String content = """
                {
                    "mappings": {
                        "properties": {
                            "id": {
                                "type": "keyword"
                            },
                            "name": {
                                "type": "text",
                                "analyzer": "ik_max_word"
                            },
                            "address": {
                                "type": "keyword",
                                "index": false
                            },
                            "price": {
                                "type": "double"
                            },
                            "brand": {
                                "type": "keyword"
                            },
                            "city": {
                                "type": "keyword"
                            },
                            "starName": {
                                "type": "keyword"
                            },
                            "business": {
                                "type": "keyword"
                            },
                            "location": {
                                "type": "geo_point"
                            },
                            "pic": {
                                "type": "keyword",
                                "index": false
                            }
                        }
                    }
                }
                """;

        // 准备请求
        var createIndexRequest = new CreateIndexRequest("hotel");
        createIndexRequest.source(content, XContentType.JSON);
        // 发送请求
        var createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        // 解析响应
        System.out.println(createIndexResponse);
    }

    @Test
    void testDeleteIndex() throws IOException {
        var deleteRequest = new DeleteIndexRequest("hotel");
        var response = client.indices().delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(response.isAcknowledged());
    }

    @Test
    void testExistIndex() throws IOException {
        var getIndexRequest = new GetIndexRequest("hotel");
        var resp = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        System.out.println("是否存在索引库：" + resp);
    }
}

package cn.itcast.hotel;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.service.impl.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class TestDocument {
    @Autowired
    private RestHighLevelClient client;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private HotelService hotelService;

    @Test
    void testAddDocument() throws IOException {
        Hotel hotel = this.hotelService.getById(36934L);
        HotelDoc hotelDoc = new HotelDoc(hotel);
        IndexRequest indexRequest = new IndexRequest("hotel").id(String.valueOf(hotel.getId()));
        indexRequest.source(BeanUtil.beanToMap(hotelDoc), XContentType.JSON);
        var indexResp = client.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(indexResp.getId());
    }

    @Test
    void testQueryDocument() throws IOException {
        GetRequest getRequest = new GetRequest("hotel");
        getRequest.id(String.valueOf(36934L));
        GetResponse response = this.client.get(getRequest, RequestOptions.DEFAULT);
        HotelDoc hotelDoc = this.objectMapper.readValue(response.getSourceAsString(), HotelDoc.class);
        Hotel hotel = BeanUtil.copyProperties(hotelDoc, Hotel.class);
        String[] split = hotelDoc.getLocation().split(",");
        assert split.length == 2;
        hotel.setLatitude(split[0].strip());
        hotel.setLongitude(split[1].strip());
        System.out.println(hotel);
    }

    // 局部更新
    @Test
    void testUpdateDocument() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("hotel", String.valueOf(36934L));
        updateRequest.doc("name", "8天酒店", "price", 444);
        UpdateResponse updateResponse = this.client.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse.getResult());
    }

    @Test
    void testDeleteDocument() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("hotel", String.valueOf(36934L));
        DeleteResponse deleteResponse = this.client.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(deleteResponse.getResult());
    }

    @Test
    void testBulk() throws IOException {
        List<Hotel> hotels = this.hotelService.list();
        List<HotelDoc> hotelDocs = hotels.stream().map(HotelDoc::new).collect(Collectors.toList());

        BulkRequest bulkRequest = new BulkRequest();
        for (HotelDoc hotelDoc : hotelDocs) {
            String content = this.objectMapper.writeValueAsString(hotelDoc);
            System.out.println(content);
            bulkRequest.add(new IndexRequest("hotel").id(hotelDoc.getId().toString()).source(content, XContentType.JSON));
        }
        BulkResponse bulkResponse = this.client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulkResponse.status());
    }
}

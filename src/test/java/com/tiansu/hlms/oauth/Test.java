package com.tiansu.hlms.oauth;

import com.alibaba.druid.support.json.JSONUtils;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 王瑞
 * @date 2018-03-14 13:57
 * @description 接口调用示例 以Spring提供的web.client RestTemplate类提供的
 */
public class Test {

    public final static String ipPort = "http://192.168.40.42:8888/";

    /*
     * main方法中为调用示例，接口对真正对接接口为准
     */
    public static void main(String[] args) {

        /*
         * 用于获取第三方应用凭证（suite_access_token）示例接口
         * 接口以真正对接接口为准
         *
         * 该示例是请求POST接口 参数以body包体的方式传入
         * uri接口访问地址，bodyParam包体参数，headers如果是空默认数据采用json格式进行传输，
         * Map.class接收请求返回结果对象，可自行定义
         *
         */
        String uri = ipPort + "cgi-bin/service/get_suite_token";
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("suite_id", "882123");
        bodyParam.put("suite_secret", "BD4RJK7PhTQt");
        bodyParam.put("suite_ticket", "o0wSGZhckBSiepk5");
        Map<String, Object> map3 = post(uri, bodyParam, null, Map.class);
        System.out.println(JSONUtils.toJSONString(map3));


        /*
         * 服务商根据suite_access_token获取预授权码
         * 接口以真正对接接口为准
         *
         * 该示例是请求GET接口 参数以uri参数方式传入，参数和值以uri方式
         * Map.class接收请求返回结果对象，可自行定义
         *
         */
        String uri1 = ipPort + "cgi-bin/service/get_pre_auth_code?suite_access_token="+map3.get("suite_access_token").toString();
        Map<String, Object> map1 = get(uri1, Map.class);
        System.out.println(JSONUtils.toJSONString(map1));

         /*
         * 服务商根据suite_access_token获取预授权码
         * 接口以真正对接接口为准
         *
         * 该示例是请求GET接口 参数以uri参数方式传入，参数和值直接传入
         * Map.class接收请求返回结果对象，可自行定义
         *
         */
        String uri2 = ipPort + "cgi-bin/service/get_pre_auth_code?suite_access_token={suite_access_token}";
        Map<String, Object> param1 = new HashMap<>();
        param1.put("suite_access_token", map3.get("suite_access_token").toString());
        Map<String, Object> map2 = get(uri2, param1, Map.class);
        System.out.println(JSONUtils.toJSONString(map2));

        /*
         * 使用临时授权码换取授权方的永久授权码
         * 接口以真正对接接口为准
         *
         * 该示例是请求POST接口 参数以body包体和uri参数的方式传入
         * uri接口访问地址，bodyParam1，uriParam1， headers如果是空默认数据采用json格式进行传输，
         * Map.class接收请求返回结果对象，可自行定义
         *
         */
        String uri4 = ipPort + "cgi-bin/service/get_permanent_code?suite_access_token={suite_access_token}";
        Map<String, Object> bodyParam1 = new HashMap<>();
        bodyParam1.put("auth_code", map2.get("auth_code").toString());
        Map<String, Object> uriParam1 = new HashMap<>();
        uriParam1.put("suite_access_token", map2.get("suite_access_token").toString());
        Map<String, Object> map5 = post(uri4, bodyParam1, uriParam1, null, Map.class);
        System.out.println(JSONUtils.toJSONString(map5));

         /*
         * 服务提供商鉴权token接收接口
         * 接口以真正对接接口为准
         *
         * 该示例是请求POST接口 参数以uri参数的方式传入
         * uri接口访问地址，bodyParam包体参数没有传null，uriParam2参数， headers如果是空默认数据采用json格式进行传输，
         * Map.class接收请求返回结果对象，可自行定义
         *
         */
        String uri5 = ipPort + "cgi-bin/service/provider_auth_token?suite_id={suite_id}&token={token}";
        Map<String, Object> uriParam2 = new HashMap<>();
        uriParam2.put("suite_id", "882123");
        uriParam2.put("token", "BD4RJK7PhTQt");
        Map<String, Object> map4 = post(uri5, null, uriParam2, null, Map.class);
        System.out.println(JSONUtils.toJSONString(map4));


    }

    /**
     * 调用查询接口
     *
     * @param uri          uri接口
     * @param responseType 返回值Class
     * @param <T>          返回泛型
     * @return
     */
    public static <T> T get(String uri, Class<T> responseType) {
        RestTemplate template = getDefaultTemplate();
        ResponseEntity<T> responseEntity = template.getForEntity(uri, responseType);
        if (HttpStatus.OK == responseEntity.getStatusCode()) {
            return responseEntity.getBody();
        } else {
            throw new RuntimeException(uri + " get failed , response code is " + responseEntity.getStatusCode());
        }

    }

    /**
     * 调用查询接口
     *
     * @param uri           uri接口
     * @param uriParameters uri参数
     * @param responseType  返回值Class
     * @param <T>           返回泛型
     * @return
     */
    public static <T> T get(String uri, Map<String, Object> uriParameters, Class<T> responseType) {
        RestTemplate template = getDefaultTemplate();
        ResponseEntity<T> responseEntity = template.getForEntity(uri, responseType, uriParameters);
        if (HttpStatus.OK == responseEntity.getStatusCode()) {
            return responseEntity.getBody();
        } else {
            throw new RuntimeException(uri + " get failed , response code is " + responseEntity.getStatusCode());
        }

    }

    /**
     * @param uri            uri接口
     * @param bodyParameters 请求包体参数
     * @param headers        请求头，null为默认json
     * @param responseType   返回值Class
     * @param <T>            返回泛型
     * @return
     */
    public static <T> T post(String uri, Map<String, Object> bodyParameters, HttpHeaders headers, Class<T> responseType) {
        RestTemplate template = getDefaultTemplate();
        if (headers == null) {
            //默认数据采用json格式进行传输
            headers = getDefaultHeaders();
        }
        HttpEntity<?> requestEntity = new HttpEntity(bodyParameters, headers);
        ResponseEntity<T> responseEntity = template.postForEntity(uri, requestEntity, responseType);
        if (HttpStatus.OK == responseEntity.getStatusCode()) {
            return responseEntity.getBody();
        } else {
            throw new RuntimeException(uri + " get failed , response code is " + responseEntity.getStatusCode());
        }

    }

    /**
     * @param uri            uri接口
     * @param bodyParameters 请求体参数
     * @param uriParameters  uri参数
     * @param headers        请求头，null为默认json
     * @param responseType   返回值Class
     * @param <T>            返回泛型
     * @return
     */
    public static <T> T post(String uri, Map<String, Object> bodyParameters, Map<String, Object> uriParameters, HttpHeaders headers, Class<T> responseType) {
        RestTemplate template = getDefaultTemplate();
        if (headers == null) {
            //默认数据采用json格式进行传输
            headers = getDefaultHeaders();
        }
        HttpEntity<?> requestEntity = new HttpEntity(bodyParameters, headers);
        ResponseEntity<T> responseEntity = template.postForEntity(uri, requestEntity, responseType, uriParameters);
        if (HttpStatus.OK == responseEntity.getStatusCode()) {
            return responseEntity.getBody();
        } else {
            throw new RuntimeException(uri + " get failed , response code is " + responseEntity.getStatusCode());
        }

    }


    /**
     * 返回默认json的post请求头
     *
     * @return
     */
    private static HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.set("Connection", "Close");
        headers.add("X-Auth-Token", UUID.randomUUID().toString());
        return headers;
    }


    /**
     * 构造默认的restTemplate
     *
     * @return
     */
    public static RestTemplate getDefaultTemplate() {
        RestTemplate template = new RestTemplate();
        template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        return template;
    }


}

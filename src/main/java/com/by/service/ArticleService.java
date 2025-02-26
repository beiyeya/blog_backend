package com.by.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class ArticleService {

	@Autowired
    private RestTemplate restTemplate;
	
    public String formatDate(Object date) {
        if (date == null) {
            return "";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            // 如果 date 是 Date 类型
            if (date instanceof Date) {
                return ((Date) date).toInstant()
                                     .atZone(ZoneId.systemDefault())
                                     .format(formatter);
            }

            // 如果 date 是 String 类型
            if (date instanceof String) {
                return LocalDateTime.parse((String) date, formatter).format(formatter);
            }
        } catch (Exception e) {
            System.out.println("日期格式化失败: " + e.getMessage());
        }

        return "";
    }
    
    public void updateArticle(String id, String remoteBaseUrl, Map<String, Object> values) throws IOException {
        // 提取文章的标题、内容等信息
        String title = (String) values.get("title");
        String content = (String) values.get("content");
        String categories = (String) values.get("categories");
        String tags = (String) values.get("tags");

        // 构建新的Markdown内容
        StringBuilder markdownContent = new StringBuilder();
        markdownContent.append("---\n");
        markdownContent.append("title: ").append(title).append("\n");
        markdownContent.append("categories: ").append(String.join(", ", categories)).append("\n");
        markdownContent.append("tags: ").append(String.join(", ", tags)).append("\n");
        markdownContent.append("---\n\n");
        markdownContent.append(content);

        // 构建宝塔API请求
        String btApiUrl = "http://120.26.36.42/files?action=SaveFileBody"; // 宝塔API地址
        String filePath = remoteBaseUrl + id; // 文件的完整路径

        // 构建请求体
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("path", filePath); // 文件路径
        requestBody.put("data", markdownContent.toString()); // 文件内容
        requestBody.put("encoding", "utf-8"); // 文件编码
        requestBody.put("st_mtime", "1723158504"); // 示例时间戳，实际需要获取文件的最新时间戳
        requestBody.put("force", "0"); // 强制更新标志

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 发送POST请求
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        String response = restTemplate.postForObject(btApiUrl, requestEntity, String.class);

        System.out.println("API响应: " + response);
        System.out.println("文章已更新: " + filePath);
    }
}

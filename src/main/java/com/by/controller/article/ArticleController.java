package com.by.controller.article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.by.service.ArticleService;
import com.by.utils.MarkdownParserUtil;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ArticleController {

	private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
    @Autowired
    private RestTemplate restTemplate;
    
    // 使用构造器注入
    @Autowired
    private ArticleService articleService;

    private final String remoteBaseUrl = "http://120.26.36.42/ArticleList/";

    @GetMapping("/article/query")
    public List<Map<String, Object>> getArticle(@RequestParam Map<String, String> query) {
        System.out.println("获取文章列表:查询参数：" + query);

        try {
        	logger.error("准备从远程服务器获取文件列表-");
            // 从远程服务器获取文件列表
            String response = restTemplate.getForObject(remoteBaseUrl, String.class);
            System.out.println("Response from server: " + response);

            // 使用正则表达式提取文件名
            List<String> files = new ArrayList<>();
            String[] parts = response.split("href=\"");
            for (String part : parts) {
                if (part.contains("\"")) {
                    String file = part.split("\"")[0];
                    if (!file.isEmpty()) {
                        files.add(file);
                    }
                }
            }
            logger.error("准备排序获取每个文件的内容");
            // 获取每个文件的内容
            List<Map<String, Object>> posts = new ArrayList<>();
            for (String file : files) {
            	if("../".equals(file)) {
            		continue;
            	}
                String encodedFile = URLDecoder.decode(file, "UTF-8");
                // 使用 ResponseEntity 获取完整的响应
                ResponseEntity<String> responseEntity = restTemplate.getForEntity(remoteBaseUrl + encodedFile, String.class);
                // 修正乱码问题
                String fileResponse = responseEntity.getBody();
                String correctedResponse = new String(fileResponse.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                // 假设文件内容是Markdown格式，使用gray-matter解析
                Map<String, Object> data = MarkdownParserUtil.parseMarkdown(correctedResponse);
                String formattedDate = articleService.formatDate(data.get("date"));

                // 使用Map.put方法构建每个文章的元数据
                Map<String, Object> post = new HashMap<>();
                post.put("id", encodedFile);
                post.put("title", data.getOrDefault("title", ""));
                post.put("date", formattedDate);
                post.put("categories", data.getOrDefault("categories", Collections.emptyList()));
                post.put("tags", data.getOrDefault("tags", Collections.emptyList()));
                post.put("content", data.getOrDefault("body", ""));

                posts.add(post);
            }
            logger.error("准备排序posts："+posts);
            // 根据日期降序排序
            posts.sort((a, b) -> {
                try {
                    // 定义日期格式化模式
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    // 解析日期字符串
                    LocalDateTime dateA = LocalDateTime.parse((String) a.get("date"), formatter);
                    LocalDateTime dateB = LocalDateTime.parse((String) b.get("date"), formatter);

                    // 比较日期
                    return dateB.compareTo(dateA);
                } catch (Exception e) {
                    System.err.println("日期解析失败: " + e.getMessage());
                    return 0; // 如果解析失败，返回0（即不改变顺序）
                }
            });
            logger.error("成功返回posts："+posts);
            return posts;
        } catch (Exception e) {
            System.out.println("获取文章列表失败: " + e.getMessage());
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "获取文章列表失败 ");
            return Collections.singletonList(errorMap);
        }
    }

    @PutMapping("/article/updateArticle/{id}")
    public ResponseEntity<String> updateArticle(@PathVariable String id, @RequestBody Map<String, Object> values) {
        try {
            // 打印接收到的文章数据
            System.out.println("获取修改的文章数据: 查询参数：" + values);

            // 调用服务层方法更新文章
            articleService.updateArticle(id, remoteBaseUrl, values);

            // 返回成功响应
            return ResponseEntity.ok("编辑成功");
        } catch (Exception e) {
            // 打印错误信息
            System.out.println("编辑文章失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("编辑失败，请稍后再试。");
        }
    }
    
}
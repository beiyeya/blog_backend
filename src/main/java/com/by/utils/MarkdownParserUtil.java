package com.by.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownParserUtil {

    /**
     * 解析Markdown内容，提取Front Matter和正文
     * @param markdown Markdown内容
     * @return 包含元数据和正文的Map
     */
    public static Map<String, Object> parseMarkdown(String markdown) {
        Map<String, Object> result = new HashMap<>();

        // 正则表达式匹配Front Matter
        Pattern pattern = Pattern.compile("^---\\s*\\n(.+?)\\n---\\s*(.*)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(markdown);

        if (matcher.find()) {
            // 提取Front Matter并解析为Map
            String frontMatter = matcher.group(1);
            result.putAll(parseFrontMatter(frontMatter));

            // 提取正文内容
            String body = matcher.group(2).trim();
            result.put("body", body);
        } else {
            // 如果没有Front Matter，直接将整个内容作为正文
            result.put("body", markdown.trim());
        }

        return result;
    }

    /**
     * 解析Front Matter部分，提取键值对
     * @param frontMatter Front Matter内容
     * @return 包含元数据的Map
     */
    private static Map<String, String> parseFrontMatter(String frontMatter) {
        Map<String, String> data = new HashMap<>();
        String[] lines = frontMatter.split("\n");

        for (String line : lines) {
            String[] parts = line.split(":", 2);
            if (parts.length == 2) {
                data.put(parts[0].trim(), parts[1].trim());
            }
        }

        return data;
    }
}
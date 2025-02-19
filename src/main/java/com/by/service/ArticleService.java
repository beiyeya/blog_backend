package com.by.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.stereotype.Service;
@Service
public class ArticleService {

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
    
}

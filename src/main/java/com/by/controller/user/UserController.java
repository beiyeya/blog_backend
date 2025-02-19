 package com.by.controller.user;

import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.PostMapping;
 import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 import java.util.Collections;
 import java.util.HashMap;
 import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

 @RestController
 @RequestMapping("/api")
 public class UserController {

     @PostMapping("/login/account")
     public Map<String, String> login(@RequestBody Map<String, String> body) {
         String username = body.get("username");
         String password = body.get("password");
         if ("admin".equals(username) && "ant.design".equals(password)) {
             return Collections.unmodifiableMap(new HashMap<String, String>() {{
                 put("status", "ok");
                 put("currentAuthority", "admin");
             }});
         } else {
             return Collections.unmodifiableMap(new HashMap<String, String>() {{
                 put("status", "error");
                 put("message", "用户名或密码错误");
             }});
         }
     }
     

     @GetMapping("/currentUser")
     public Map<String, Object> currentUser() {
         return Collections.unmodifiableMap(new HashMap<String, Object>() {{
             put("success", true);
             put("data", Collections.unmodifiableMap(new HashMap<String, Object>() {{
                 put("name", "Serati Ma");
                 put("avatar", "https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png");
                 put("userid", "00000001");
                 put("email", "antdesign@alipay.com");
                 put("signature", "海纳百川，有容乃大");
                 put("title", "交互专家");
                 put("group", "蚂蚁金服－某某某事业群－某某平台部－某某技术部－UED");
                 put("tags", new Object[]{
                     Collections.unmodifiableMap(new HashMap<String, String>() {{
                         put("key", "0");
                         put("label", "很有想法的");
                     }}),
                     Collections.unmodifiableMap(new HashMap<String, String>() {{
                         put("key", "1");
                         put("label", "专注设计");
                     }}),
                     Collections.unmodifiableMap(new HashMap<String, String>() {{
                         put("key", "2");
                         put("label", "辣~");
                     }}),
                     Collections.unmodifiableMap(new HashMap<String, String>() {{
                         put("key", "3");
                         put("label", "大长腿");
                     }}),
                     Collections.unmodifiableMap(new HashMap<String, String>() {{
                         put("key", "4");
                         put("label", "川妹子");
                     }}),
                     Collections.unmodifiableMap(new HashMap<String, String>() {{
                         put("key", "5");
                         put("label", "海纳百川");
                     }})
                 });
                 put("notifyCount", 12);
                 put("unreadCount", 11);
                 put("country", "China");
                 put("access", "admin");
                 put("geographic", Collections.unmodifiableMap(new HashMap<String, Object>() {{
                     put("province", Collections.unmodifiableMap(new HashMap<String, String>() {{
                         put("label", "浙江省");
                         put("key", "330000");
                     }}));
                     put("city", Collections.unmodifiableMap(new HashMap<String, String>() {{
                         put("label", "杭州市");
                         put("key", "330100");
                     }}));
                 }}));
                 put("address", "西湖区工专路 77 号");
                 put("phone", "0752-268888888");
             }}));
         }});
     }
     @GetMapping("/ai")
     public Map<String, String> ai() {
    	 return Collections.unmodifiableMap(new HashMap<String, String>() {{
             put("status", "ok");
         }});
     }
     @PostMapping("/login/outLogin")
     public String outLogin(HttpServletRequest request) {
         // 获取当前请求的会话
         HttpSession session = request.getSession(false);
         
         // 如果会话存在，则使会话失效，从而实现退出登录
         if (session != null) {
             session.invalidate();
         }
         // 返回退出登录成功的消息
         return "登出成功";
     }
 }
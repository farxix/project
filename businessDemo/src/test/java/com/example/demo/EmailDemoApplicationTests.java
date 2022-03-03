package com.example.demo;

import cn.hutool.core.util.RandomUtil;
import com.example.demo.message.MailServiceImpl;
import com.example.demo.redis.RedisUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailDemoApplicationTests {

    @Autowired
    private MailServiceImpl mailServiceImpl;

    @Resource
    RedisUtils redisUtils;

    private static final String TO = "xxx@qq.com";
    private static final String SUBJECT = "测试邮件";
    private static final String CONTENT = "test content";

    /**
     * 测试发送普通邮件
     */
    @Test
    public void sendSimpleMailMessage() {
        mailServiceImpl.sendSimpleMailMessage(TO, SUBJECT, CONTENT);
    }

    /**
     * 注册邮箱，校验邮箱是否注册过
     * 发送邮件验证码，3分钟内有效
     */
    @Test
    public void sendSimpleTextEmail() {
        String key = "test0301";
        String random = RandomUtil.randomNumbers(4);
        System.out.println(random);
        sendSimpleMailMessage();

        redisUtils.set(key,random,3, TimeUnit.MINUTES);
    }

    /**
     * 查看验证码是否有效
     */
    @Test
    public void getRedisValue() {
        String key = "test0301";
        System.out.println(redisUtils.isExist(key));
        System.out.println(redisUtils.get(key));
    }

    /**
     * 测试发送html邮件
     */
    @Test
    public void sendHtmlMessage() {
        String htmlStr = "<h1>Test</h1>";
        mailServiceImpl.sendMimeMessage(TO, SUBJECT, htmlStr);
    }

    /**
     * 测试发送带附件的邮件
     * @throws FileNotFoundException
     */
    @Test
    public void sendAttachmentMessage() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:test.txt");
        String filePath = file.getAbsolutePath();
        mailServiceImpl.sendMimeMessage(TO, SUBJECT, CONTENT, filePath);
    }

    /**
     * 测试发送带附件的邮件
     * @throws FileNotFoundException
     */
    @Test
    public void sendPicMessage() throws FileNotFoundException {
        String htmlStr = "<html><body>测试：图片1 <br> <img src=\'cid:pic1\'/> <br>图片2 <br> <img src=\'cid:pic2\'/></body></html>";
        Map<String, String> rscIdMap = new HashMap<>(2);
        rscIdMap.put("pic1", ResourceUtils.getFile("classpath:pic01.jpg").getAbsolutePath());
        rscIdMap.put("pic2", ResourceUtils.getFile("classpath:pic02.jpg").getAbsolutePath());
        mailServiceImpl.sendMimeMessage(TO, SUBJECT, htmlStr, rscIdMap);
    }
}

package com.example.demo.message;

import java.util.Map;

public interface MailService {
    void sendSimpleMailMessage(String to, String subject, String content);

    void sendMimeMessage(String to, String subject, String content);

    void sendMimeMessage(String to, String subject, String content, String filePath);

    void sendMimeMessage(String to, String subject, String content, Map<String, String> rscIdMap);
}

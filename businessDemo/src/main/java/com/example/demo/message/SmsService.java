//package com.example.demo.sms;
//
//import com.aliyuncs.DefaultAcsClient;
//import com.aliyuncs.IAcsClient;
//import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
//import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
//import com.aliyuncs.exceptions.ClientException;
//import com.aliyuncs.profile.DefaultProfile;
//import com.aliyuncs.profile.IClientProfile;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
///**
// * 短信服务
// *
// * @author cc
// * @date 2021-12-07 9:50
// */
//@Service
//public class SmsService {
//    @Value("${sms.accessKeyId}")
//    private String accessKeyId;
//
//    @Value("${sms.accessSecret}")
//    private String accessSecret;
//
//    @Value("${sms.signName}")
//    private String signName;
//
//    @Value("${sms.templateCode}")
//    private String templateCode;
//
//    public void send(String phone, String code) throws ClientException {
//        if (StringUtils.isEmpty(phone)) {
//            throw new RuntimeException("手机号码不能为空");
//        }
//        if (StringUtils.isEmpty(code)) {
//            throw new RuntimeException("验证码不能为空");
//        }
//
//        // 初始化acsClient,暂不支持region化
//        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou",
//                accessKeyId, accessSecret);
//        IAcsClient acsClient = new DefaultAcsClient(profile);
//        // 组装请求对象-具体描述见控制台-文档部分内容
//        SendSmsRequest request = new SendSmsRequest();
//        // 必填:待发送手机号
//        request.setPhoneNumbers(phone);
//        // 必填:短信签名-可在短信控制台中找到
//        request.setSignName(signName);
//        // 必填:短信模板-可在短信控制台中找到
//        request.setTemplateCode(templateCode);
//        request.setTemplateParam("{\"code\":\"" + code + "\"}");
//
//        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
//        if(sendSmsResponse.getCode()!= null && "OK".equals(sendSmsResponse.getCode())){
//            System.out.println("短信发送成功！");
//        }else {
//            throw new RuntimeException("短信发送失败：" + sendSmsResponse.getMessage());
//        }
//    }
//}

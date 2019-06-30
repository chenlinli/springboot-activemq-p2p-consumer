package com.cl.consumer;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class P2PConsumer {

    @Autowired
    JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String toEmail;

    @JmsListener(destination = "${myQueueSB}")
    public void recieve(String msg) throws Exception {
        if(StringUtils.isEmpty(msg)){
            return ;
        }

        JSONObject jsonObject = new JSONObject().parseObject(msg);
        String userName = jsonObject.getString("userName");
        String email = jsonObject.getString("email");
        sendSimpleMail(email, userName);
        System.out.println("p2p模式：消费消息:"+msg);
    }

    public void sendSimpleMail(String eamil, String userName) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(eamil);
        message.setTo(toEmail);
        //邮件标题
        message.setSubject("蚂蚁课堂|每特教育 新学员提醒");
        //内容
        message.setText("祝贺您,成为了我们" + userName + ",学员!");
        javaMailSender.send(message);
        System.out.println("邮件发送完成," + JSONObject.toJSONString(message));
    }
}

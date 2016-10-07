package cn.zzuzl.service;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

/**
 * Created by Administrator on 2016/10/7.
 */
@Service
public class MailService {
    @Resource
    private SpringTemplateEngine templateEngine;
    @Resource
    private JavaMailSenderImpl mailSender;

    public void sendAdviceEmail(String advice) throws MessagingException {
        // 准备数据
        final Context ctx = new Context();
        ctx.setVariable("advice", advice);

        // 准备消息
        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        message.setSubject("意见反馈");
        message.setFrom("m15617536860@163.com");
        message.setTo("672399171@qq.com");

        // html消息内容
        String htmlContent = templateEngine.process("email/advice", ctx);
        message.setText(htmlContent, true);

        // 发送
        mailSender.send(mimeMessage);
    }
}

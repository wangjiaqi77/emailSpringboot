package qg.fangrui.boot.controller;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Map;

/**
 * Created by wangjiaqi on 2018/1/10.
 */
@RestController
@RequestMapping("/xdf/email")
public class EmailSend {


    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String Sender;


    @PostMapping("/send_ord")
    public String sendSimpleMail(Map map) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(Sender);
        message.setTo(Sender);
        message.setSubject("主题：简单邮件");
        message.setText("测试邮件内容");
        mailSender.send(message);
        return "发送完毕---" + new Date();
    }

    @PostMapping("/send_html")
    public String sendHtmlMail() {
        MimeMessage message = null;
        try {
            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(Sender);
            helper.setTo(Sender);
            helper.setSubject("主题：HTML邮件");

            StringBuffer sb = new StringBuffer();
            sb.append("<h1>恭喜你中奖了-h1</h1>")
                    .append("<p style='color:#F00'>恭喜你获得 爸爸去哪了 第三季特别关注奖</p>")
                    .append("<p style='text-align:right'>右对齐</p>");
            helper.setText(sb.toString(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mailSender.send(message);
        return "发送完毕---" + new Date();
    }

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @PostMapping("/send_ftl")
    public String sendTemplateMail() {
        MimeMessage message = null;
        try {
            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(Sender);
            helper.setTo(Sender);
            helper.setSubject("主题：颤抖吧凡人啊啊啊哈哈哈哈！！！！");

            Map<String, Object> model = new HashedMap();
            model.put("username", "凡人啊");
            model.put("text", "这波操作很666！！！！");

            Configuration cfg= new Configuration(Configuration.VERSION_2_3_23);

            // 设定去哪里读取相应的ftl模板

            cfg.setClassForTemplateLoading(this.getClass(), "/templates/ftl");

            // 在模板文件目录中寻找名称为name的模板文件

            Template template   = cfg.getTemplate("template.ftl");

            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            helper.setText(html,true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        mailSender.send(message);
        return "发送成功 ==="+new Date();
    }

    }


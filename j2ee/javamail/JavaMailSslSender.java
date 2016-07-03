package com.edu.servlet;

import java.security.Security; 
import java.util.Date; 
import java.util.Properties; 

import javax.mail.Authenticator; 
import javax.mail.Message; 
import javax.mail.MessagingException; 
import javax.mail.PasswordAuthentication; 
import javax.mail.Session; 
import javax.mail.Transport; 
import javax.mail.internet.AddressException; 
import javax.mail.internet.InternetAddress; 
import javax.mail.internet.MimeMessage; 


/** 
* 发送邮件模块 
* @author dan.shan 
* 
*/ 
public class JavaMailSslSender { 

    private  Properties serverProps; 
    
    private   String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory"; 
    private   String SMTP_HOST = "smtp.exmail.qq.com"; // smtp 服务器地址 
    private   int SMTP_PORT = 465; // smtp 服务器端口 
    
    private   String FROM_ADDR = "test@cygnia.ch"; // 邮件的发送者地址 
    private   String USERNAME = "test@cygnia.ch"; // 用户名 
    private   String PASSWORD = "cygnia123456"; // 密码 
    
    public JavaMailSslSender(String from, String username,String password){
    	  FROM_ADDR = from;
    	  USERNAME= username;
    	  PASSWORD = password;
    	  serverProps = new Properties(); 
          serverProps.setProperty("mail.smtp.host", SMTP_HOST); 
          serverProps.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
          serverProps.setProperty("mail.smtp.socketFactory.fallback", "false"); 
          serverProps.setProperty("mail.smtp.port", String.valueOf(SMTP_PORT)); 
          serverProps.setProperty("mail.smtp.socketFactory.port", String.valueOf(SMTP_PORT)); 
          serverProps.put("mail.smtp.auth", "true"); 
    }
      
    
	/**
	 * 
	 * @param receiver  收件人
	 * @param subject	标题
	 * @param content	内容
	 * @throws AddressException
	 * @throws MessagingException
	 */
    public  void send(String receiver, String subject, String content) throws AddressException, MessagingException { 
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider()); 
        Session session = Session.getDefaultInstance(serverProps, new Authenticator() { 

                @Override 
                protected PasswordAuthentication getPasswordAuthentication() { 
                    return new PasswordAuthentication(USERNAME, PASSWORD); 
                } 
        }); 
        
        Message msg = new MimeMessage(session); 
        msg.setFrom(new InternetAddress(FROM_ADDR)); 
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver, false)); 
        msg.setSubject(subject); 
        msg.setText(content); 
        msg.setSentDate(new Date()); 
        Transport.send(msg); 

        System.out.println("Message sent."); 
    } 
    
   
    
} 
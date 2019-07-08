package cn.cnnic.domainstat.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
	@Value("${mail.host}")
	private static String host;
	@Value("${mail.user}")
	private static String user;
	@Value("${mail.password}")
	private static String password;

	public static void sendEmail(String emailAddr, String subject, String content) throws Exception{
		sendEmail(new String[] {emailAddr}, subject, content);
	}
	public static void sendEmail(String emailAddr[], String subject, String content) throws Exception {
		Properties prop = new Properties();
		prop.setProperty("mail.host", host);
		prop.setProperty("mail.transport.protocol", "smtp");
		prop.setProperty("mail.smtp.auth", "true");
		// 使用JavaMail发送邮件的5个步骤
		// 1、创建session
		Session session = Session.getInstance(prop);
		// 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
		session.setDebug(true);
		// 2、通过session得到transport对象
		Transport ts = session.getTransport();
		// 3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
		ts.connect(host, user, password);
		// 4、创建邮件
		Message message = createSimpleMail(session, emailAddr, subject, content);
		// 5、发送邮件
		ts.sendMessage(message, message.getAllRecipients());
		ts.close();
	}

	/**
	 * @Method: createSimpleMail
	 * @Description: 创建一封只包含文本的邮件
	 * @Anthor:zhangtao
	 *
	 * @param session
	 * @param content
	 * @param subject
	 * @param emailAddr
	 * @return
	 * @throws Exception
	 */
	public static MimeMessage createSimpleMail(Session session, String emailAddr[], String subject, String content)
			throws Exception {
		// 创建邮件对象
		MimeMessage message = new MimeMessage(session);
		// 指明邮件的发件人
		message.setFrom(new InternetAddress(user));
		// 指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
		for (String addr : emailAddr) {
			System.out.println(addr);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(addr));
		}
		// 邮件的标题
		message.setSubject(subject);
		// 邮件的文本内容
		message.setContent(content, "text/html;charset=UTF-8");
		// 返回创建好的邮件对象
		return message;
	}
}

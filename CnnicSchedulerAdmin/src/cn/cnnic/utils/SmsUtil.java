package cn.cnnic.utils;

import cn.cnnic.api.ApiException;
import cn.cnnic.api.DefaultCnnicOpClient;
import cn.cnnic.api.request.SmsSendRequest;

public class SmsUtil {

	public static void sendSms(String cellPhone,String content) {
		DefaultCnnicOpClient client=new DefaultCnnicOpClient();
		SmsSendRequest request=new SmsSendRequest();
		request.setContent(content);
		request.setPhone_nums(cellPhone);
		try {
			client.execute(request);
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}

}

package cn.cnnic.porterrecord.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Test {

	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
		Date startDate=sdfDate.parse("2011-01-01 08:00:00");
		Date endDate=sdfDate.parse("2011-01-07 07:00:00");
		Map<String,String> backupMap=new HashMap<String,String>();
		
		Date loopDate=(Date) startDate.clone();
		Calendar cal = Calendar.getInstance();
		while(!sdfDate.format(loopDate).equals(sdfDate.format(endDate))){
			backupMap.put(sdfDate.format(loopDate), "00:00:00");
			cal.setTime(loopDate);
			cal.add(cal.DATE, 1);
			loopDate=cal.getTime();
		}
		backupMap.put("2011-01-01", "20:00:00");
		System.out.println(backupMap);
		
	}

}

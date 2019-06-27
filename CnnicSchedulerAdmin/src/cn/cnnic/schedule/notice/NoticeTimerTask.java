package cn.cnnic.schedule.notice;

import cn.cnnic.common.QueryFilter;
import cn.cnnic.model.Person;
import cn.cnnic.model.Scheduler;
import cn.cnnic.utils.CalendarUtil;
import cn.cnnic.utils.EmailUtil;
import cn.cnnic.utils.SmsUtil;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

@Component
public class NoticeTimerTask
{
  private static final Logger LOGGER = LoggerFactory.getLogger(NoticeTimerTask.class);
  
  @Scheduled(cron="0 10 8 ? * FRI")
  public void leaderNotie()
    throws Exception
  {
    LOGGER.info("--------------------[NoticeTimerTask->leaderNotie]\t\tnotice begin");
    String groupLeader ="";
    String today=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    Record record=Db.findFirst("select * from gc_schedule_secondline where startDate=?",CalendarUtil.getIntervalDate(today,-6));
    if(null!=record) {
    	groupLeader=record.getStr("groupLeader");
    }else {
    	return;
    }
    QueryFilter queryFilter = new QueryFilter();
    queryFilter.setSelectFields("cellphone,email");
    queryFilter.setWhereString("name='" + groupLeader + "'");
    queryFilter.setOrderString("pid desc");
    List<Person> dictList = Person.me.find(queryFilter);
    
    String cellphone = "";String email = "";
    String emailSubject = "带班组长周报提醒";
    String content = "";
    if (!dictList.isEmpty())
    {
      cellphone = ((Person)dictList.get(0)).getStr("cellphone");
      email = ((Person)dictList.get(0)).getStr("email");
    }
    content = groupLeader + "您好，您这周作为带班组长，请记得按时提交周报，谢谢。";
    LOGGER.info("--------------------[NoticeTimerTask->leaderNotie]\t\tbegin to send email to " + email);
    EmailUtil.sendEmail(new String[] { email }, emailSubject, content);
    LOGGER.info("--------------------[NoticeTimerTask->leaderNotie]\t\tbegin to send sms to " + cellphone);
    SmsUtil.sendSms(cellphone, content);
    LOGGER.info("--------------------[NoticeTimerTask->leaderNotie]\t\tnotice end");
  }
  
  @Scheduled(cron="0 0 17 * * ?")
  public void dayNoticeWithSms()
    throws Exception
  {
    LOGGER.info("--------------------[NoticeTimerTask->dayNoticeWithSms]\tnotice begin");
    
    QueryFilter qf1 = new QueryFilter();
    qf1.setWhereString("day='" + CalendarUtil.getTomorrowDate() + "' and events = '白班'");
    qf1.setSelectFields("*");
    qf1.setOrderString("id ASC");
    List<Scheduler> list1 = Scheduler.me.getEntityList(qf1);
    for (Scheduler sch1 : list1)
    {
      int pid = sch1.getInt("pid").intValue();
      
      Person person = (Person)Person.me.findById(Integer.valueOf(pid));
      String name = person.getStr("name");
      String cellPhone = person.getStr("cellphone");
      String smsContent = "温馨提示：" + name + "，你好。请记得明天早上来值白班哦！";
      Record record=Db.findFirst("select * from gc_schedule_scheduler where day=? and pid<>? and events='白班'",CalendarUtil.getTomorrowDate(),pid);
      if(null!=record) {
    	  String theOtherName=record.getStr("name");
    	  smsContent=smsContent+"明天和你一起值班的同事是"+theOtherName;
      }
      QueryFilter qf2 = new QueryFilter();
      qf2.setWhereString("pid='" + pid + "' and day>'" + CalendarUtil.getTomorrowDate() + "' and day<='" + CalendarUtil.get7DayLaterDate() + "' and events = '白班'");
      qf2.setSelectFields("*");
      qf2.setOrderString("id ASC");
      List<Scheduler> list2 = Scheduler.me.getEntityList(qf2);
      int dayNum = list2.size();
      if (dayNum == 0)
      {
        smsContent = smsContent + "	PS.明天下班之后，你接下来的一周都没有白班啦哦！";
      }
      else
      {
        smsContent = smsContent + " PS.明天下班之后接下来的七天你还有" + dayNum + "个白班，日期为：";
        int i = 1;
        for (Scheduler sch2 : list2)
        {
          smsContent = smsContent + sch2.getStr("day");
          if (i != dayNum) {
            smsContent = smsContent + ",";
          }
          i++;
        }
      }
      LOGGER.info("--------------------[NoticeTimerTask->dayNoticeWithSms] begin to send sms to " + cellPhone);
      LOGGER.info("--------------------[NoticeTimerTask->dayNoticeWithSms] sms content is [" + smsContent + "]");
      SmsUtil.sendSms(cellPhone, smsContent);
    }
    LOGGER.info("--------------------[NoticeTimerTask->dayNoticeWithSms]\tnotice end");
  }
  
  @Scheduled(cron="0 0 12 * * ?")
  public void dayNoticeWithEmail()
    throws Exception
  {
    LOGGER.info("--------------------[NoticeTimerTask->dayNoticeWithEmail]\tnotice begin");
    
    QueryFilter qf1 = new QueryFilter();
    qf1.setWhereString("day='" + CalendarUtil.getTodayDate() + "' and events in ('白班','常白','夜班','白连夜')");
    qf1.setSelectFields("*");
    qf1.setOrderString("id ASC");
    List<Scheduler> list1 = Scheduler.me.getEntityList(qf1);
    for (Scheduler sch1 : list1)
    {
      int pid = sch1.getInt("pid").intValue();
      
      String todayEvents = sch1.getStr("events");
      
      Person person = (Person)Person.me.findById(Integer.valueOf(pid));
      String name = person.getStr("name");
      String email = person.getStr("email");
      
      QueryFilter qf2 = new QueryFilter();
      qf2.setWhereString("pid='" + pid + "' and day>'" + CalendarUtil.getTodayDate() + "' and day<='" + CalendarUtil.get7DayLaterDate() + "'");
      qf2.setSelectFields("*");
      qf2.setOrderString("day ASC");
      List<Scheduler> list2 = Scheduler.me.getEntityList(qf2);
      String emailSubject = "白班提醒通知";
      for (Scheduler sh2 : list2)
      {
        String events = sh2.getStr("events");
        if (("".equals(events)) || ("常白".equals(events)) || ("夜班".equals(events)) || ("白连夜".equals(events))) {
          break;
        }
        if ((!"白班换休".equals(events)) && (!"夜班换休".equals(events)) && (!"周末休息".equals(events))) {
          if ("白班".equals(events))
          {
            String emailContent = "";
            if (("夜班".equals(todayEvents)) || ("白连夜".equals(todayEvents))) {
              emailContent = "温馨提示：" + name + ",你好。明天早上下夜班之后，你接下来最近一个白班的日期为：";
            } else {
              emailContent = "温馨提示：" + name + ",你好。今天下班之后，你接下来最近一个白班的日期为：";
            }
            emailContent = emailContent + sh2.getStr("day") + "。<br/><br/>";
            
            QueryFilter qf3 = new QueryFilter();
            qf3.setWhereString("pid='" + pid + "' and day>'" + CalendarUtil.getTodayDate() + "' and day<'" + CalendarUtil.get7DayLaterDate() + "' and events = '白班'");
            qf3.setSelectFields("*");
            List<Scheduler> list3 = Scheduler.me.getEntityList(qf3);
            int dayNum = list3.size();
            emailContent = emailContent + "PS.你接下来七天一共有" + dayNum + "个白班，日期为：";
            int i = 1;
            for (Scheduler sch3 : list3)
            {
              emailContent = emailContent + sch3.getStr("day");
              if (i != dayNum) {
                emailContent = emailContent + ",";
              }
              i++;
            }
            LOGGER.info("--------------------[NoticeTimerTask->dayNoticeWithEmail] begin to send email to " + email);
            LOGGER.info("--------------------[NoticeTimerTask->dayNoticeWithEmail] email content is [" + emailContent + "]");
            EmailUtil.sendEmail(new String[] { email }, emailSubject, emailContent);
            break;
          }
        }
      }
    }
    LOGGER.info("--------------------[NoticeTimerTask->dayNoticeWithEmail]\tnotice end");
  }
  
  @Scheduled(cron="0 0 8 * * ?")
  public void nightNoticeWithSms()
    throws Exception
  {
    LOGGER.info("--------------------[NoticeTimerTask->nightNoticeWithSms]\tnotice begin");
    
    QueryFilter qf1 = new QueryFilter();
    qf1.setWhereString("day='" + CalendarUtil.getTodayDate() + "' and events in ('夜班','白连夜')");
    qf1.setSelectFields("*");
    qf1.setOrderString("id ASC");
    List<Scheduler> list1 = Scheduler.me.getEntityList(qf1);
    for (Scheduler sch1 : list1)
    {
      int pid = sch1.getInt("pid").intValue();
      
      Person person = (Person)Person.me.findById(Integer.valueOf(pid));
      String name = person.getStr("name");
      String cellPhone = person.getStr("cellphone");
      
      String smsContent = "温馨提示：" + name + "，你好。请记得今天晚上来值夜班哦！";
      Record record=Db.findFirst("select * from gc_schedule_scheduler where day=? and pid<>? and events in ('夜班','白连夜')",CalendarUtil.getTodayDate(),pid);
      if(null!=record) {
    	  String theOtherName=record.getStr("name");
    	  smsContent=smsContent+"今天和你一起值班的同事是"+theOtherName;
      }
      QueryFilter qf2 = new QueryFilter();
      qf2.setWhereString("pid='" + pid + "' and day>='" + CalendarUtil.getTomorrowDate() + "' and day<='" + CalendarUtil.get7DayLaterDate() + "' and events in ('夜班','白连夜')");
      qf2.setSelectFields("*");
      qf2.setOrderString("id ASC");
      List<Scheduler> list2 = Scheduler.me.getEntityList(qf2);
      int dayNum = list2.size();
      if (dayNum == 0)
      {
        smsContent = smsContent + "\tPS.明天下夜班之后，你接下来的一周都没有夜班啦哦！";
      }
      else
      {
        smsContent = smsContent + "\tPS.明天下夜班之后接下来七天你还有" + dayNum + "个夜班，日期为：";
        int i = 1;
        for (Scheduler sch2 : list2)
        {
          smsContent = smsContent + sch2.getStr("day");
          if (i != dayNum) {
            smsContent = smsContent + ",";
          }
          i++;
        }
      }
      LOGGER.info("--------------------[NoticeTimerTask->nightNoticeWithSms] begin to send sms to " + cellPhone);
      LOGGER.info("--------------------[NoticeTimerTask->nightNoticeWithSms] sms content is [" + smsContent + "]");
      SmsUtil.sendSms(cellPhone, smsContent);
    }
    LOGGER.info("--------------------[NoticeTimerTask->nightNoticeWithSms]\tnotice end");
  }
  
  @Scheduled(cron="0 0 12 * * ?")
  public void nightNoticeWithEmail()
    throws Exception
  {
    LOGGER.info("--------------------[NoticeTimerTask->nightNoticeWithEmail]\tnotice begin");
    
    QueryFilter qf1 = new QueryFilter();
    qf1.setWhereString("day='" + CalendarUtil.getTodayDate() + "' and events in ('白班','常白','夜班','白连夜')");
    qf1.setSelectFields("*");
    qf1.setOrderString("id ASC");
    List<Scheduler> list1 = Scheduler.me.getEntityList(qf1);
    for (Scheduler sch1 : list1)
    {
      int pid = sch1.getInt("pid").intValue();
      
      String todayEvents = sch1.getStr("events");
      
      Person person = (Person)Person.me.findById(Integer.valueOf(pid));
      String name = person.getStr("name");
      String email = person.getStr("email");
      
      QueryFilter qf2 = new QueryFilter();
      qf2.setWhereString("pid='" + pid + "' and day>'" + CalendarUtil.getTodayDate() + "' and day<='" + CalendarUtil.get7DayLaterDate() + "'");
      qf2.setSelectFields("*");
      qf2.setOrderString("day ASC");
      List<Scheduler> list2 = Scheduler.me.getEntityList(qf2);
      String emailSubject = "夜班提醒通知";
      for (Scheduler sh2 : list2)
      {
        String events = sh2.getStr("events");
        if (("".equals(events)) || ("常白".equals(events)) || ("白班".equals(events))) {
          break;
        }
        if ((!"白班换休".equals(events)) && (!"夜班换休".equals(events)) && (!"周末休息".equals(events))) {
          if (("夜班".equals(events)) || ("白连夜".equals(events)))
          {
            String emailContent = "";
            if (("夜班".equals(todayEvents)) || ("白连夜".equals(todayEvents))) {
              emailContent = "温馨提示：" + name + ",你好。明天早上下夜班之后，你接下来最近一个夜班的日期为：";
            } else {
              emailContent = "温馨提示：" + name + ",你好。今天下班之后，你接下来最近一个夜班的日期为：";
            }
            emailContent = emailContent + sh2.getStr("day") + "。<br/><br/>";
            
            QueryFilter qf3 = new QueryFilter();
            qf3.setWhereString("pid='" + pid + "' and day>'" + CalendarUtil.getTodayDate() + "' and day<'" + CalendarUtil.get7DayLaterDate() + "' and events in ('夜班','白连夜')");
            qf3.setSelectFields("*");
            List<Scheduler> list3 = Scheduler.me.getEntityList(qf3);
            int dayNum = list3.size();
            emailContent = emailContent + "PS.你接下来七天一共有" + dayNum + "个夜班，日期为：";
            int i = 1;
            for (Scheduler sch3 : list3)
            {
              emailContent = emailContent + sch3.getStr("day");
              if (i != dayNum) {
                emailContent = emailContent + ",";
              }
              i++;
            }
            LOGGER.info("--------------------[NoticeTimerTask->nightNoticeWithEmail] begin to send email to " + email);
            LOGGER.info("--------------------[NoticeTimerTask->nightNoticeWithEmail] email content is [" + emailContent + "]");
            EmailUtil.sendEmail(new String[] { email }, emailSubject, emailContent);
            break;
          }
        }
      }
    }
    LOGGER.info("--------------------[NoticeTimerTask->nightNoticeWithEmail]\tnotice end");
  }
}

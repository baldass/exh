package com.txts.task;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;  
  
/** 
 * Created by 赵亚辉 on 2017/12/18.  
 */  
@SuppressWarnings("all")
@Component  
public class SchedulerAllJob {  
  

    private StdSchedulerFactory  schedulerFactoryBean;  
    public SchedulerAllJob(){
    	 Properties props = new Properties();
    	 schedulerFactoryBean= new StdSchedulerFactory();
         try {
			schedulerFactoryBean.initialize(props);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
    }

    //日期格式转换器
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");
    public void restartScheduleJobs() throws Exception {  
    	try {
    		this.stop();
    		this.scheduleJobs();
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    }
    /* 
     * 此处可以注入数据库操作，查询出所有的任务配置 
     */  
  
    /** 
     * 该方法用来启动所有的定时任务 
     * @throws Exception 
     */  
    public void scheduleJobs() throws Exception {  
    	//定时器对象
    	Scheduler scheduler = schedulerFactoryBean.getScheduler();  
    	//秒数执行
    	this.scheduleJobSimple(scheduler, 6, 1);
    	this.scheduleJobSimple(scheduler, 11, 2);
    	//cron表达式
    	this.scheduleJob(scheduler, "0 * * * * ?", 4);
    	this.scheduleJob(scheduler, "30 * * * * ?", 4);
    	scheduler.start();
    }
    
    public void stop() throws SchedulerException {  
        Scheduler scheduler = schedulerFactoryBean.getScheduler();  
        scheduler.clear();  //清楚全部的任务
    }  
  
  
  
    private void scheduleJob(Scheduler scheduler,String cron,int num) throws SchedulerException{  
        try {
        	/* 
             *  此处可以先通过任务名查询数据库，如果数据库中存在该任务，则按照ScheduleRefreshDatabase类中的方法，更新任务的配置以及触发器 
             *  如果此时数据库中没有查询到该任务，则按照下面的步骤新建一个任务，并配置初始化的参数，并将配置存到数据库中 
             */  
            JobDetail jobDetail = JobBuilder.newJob(ScheduledJob.class) .withIdentity("job"+num, "group"+num).build(); 
            JobDataMap jobDataMap = jobDetail.getJobDataMap();
            //放入执行时需要的参数
            jobDataMap.put("test", "cron");
            // 根据cron表达式 执行  
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);  
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("trigger"+num, "group"+num) .withSchedule(scheduleBuilder).build();  
            scheduler.scheduleJob(jobDetail,cronTrigger);  
		} catch (Exception e) {
			e.printStackTrace();
		}
    }  
    private void scheduleJobSimple(Scheduler scheduler,int seconds,int num) throws SchedulerException{  
        /* 
         *  此处可以先通过任务名查询数据库，如果数据库中存在该任务，则按照ScheduleRefreshDatabase类中的方法，更新任务的配置以及触发器 
         *  如果此时数据库中没有查询到该任务，则按照下面的步骤新建一个任务，并配置初始化的参数，并将配置存到数据库中 
         */  
       try {
    	   	JobDetail jobDetail = JobBuilder.newJob(ScheduledJob.class) .withIdentity("job"+num, "group"+num).build(); 
    	   	JobDataMap jobDataMap = jobDetail.getJobDataMap();
    	    //放入执行时需要的参数
            jobDataMap.put("test", "simple");
    	   	//每x秒执行一次  
    	   	SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.repeatSecondlyForever(seconds);
    	   	SimpleTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("trigger"+num, "group"+num).withSchedule(scheduleBuilder).build();  
    	   	scheduler.scheduleJob(jobDetail,cronTrigger);  
		} catch (Exception e) {
			e.printStackTrace();
		}
    }  
  
	
}  
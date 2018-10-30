package com.txts.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;  
  
/** 
 * Created by 赵亚辉 on 2017/12/18. 
 */  

@RestController  
@RequestMapping("/quartz")  
public class QuartzController {  
	
	public static void main(String[] args) throws Exception {
		SchedulerAllJob aj = new SchedulerAllJob();
		aj.restartScheduleJobs();
	}

    @Autowired  
    public SchedulerAllJob myScheduler;  
    
    @RequestMapping("/start")  
    public String schedule() throws Exception {
        myScheduler.scheduleJobs();
        return "success";
    }
    
    @RequestMapping("/stop")  
    public String stop() throws Exception {
        myScheduler.stop();
        return "success";
    }
    
}
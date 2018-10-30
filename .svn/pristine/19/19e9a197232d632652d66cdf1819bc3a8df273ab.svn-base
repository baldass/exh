package com.txts.task;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.txts.exh.console.service.ConsoleService;
@SuppressWarnings("all")
public class ScheduledJob implements Job {

	private static final Logger logger = Logger.getLogger(ScheduledJob.class);

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		JobDetail jobDetail = jobExecutionContext.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		//执行的时候 可以从jobDataMap 获取之前存入的数据或者对象
		System.out.println("execing job...");
		ConsoleService service = (ConsoleService) jobDataMap.get("ConsoleService");
		List<Map<String,Object>> dispatchList = service.getDispatchList(null);
		Map<String, Object> dispatch = dispatchList.get(0);
		String detailStr = (String) dispatch.get("dispatch_detail");
		JSONArray detailArr = JSON.parseArray(detailStr);
		int size = detailArr.size();
		for (int i = 0; i < size; i++) {
			JSONObject item = detailArr.getJSONObject(i);
			//操作硬件
			//取出硬件ID 判断执行哪个方法 
			
		}
		
		
		
		
	}

}
$(function() {
	//初始化
	qualityChart = echarts.init(document.getElementById('quality_chart_main'), 'dark');
	/*deviceChart = echarts.init(document.getElementById('device_chart_main'), 'dark');*/
	beatChart = echarts.init(document.getElementById('beat_chart_main'), 'dark');
	start_boder_interval();
})
var qualityChart;//质量图表
var deviceChart;//设备图表
var beatChart;//节拍图表
var closeId;
function open_order_picker(){
	closeId = layer.open({
		id:"dispathc_pool",
		title : '订单选择',
		maxmin: true,
		type: 2,
		shade : 0,
		content : '/console/orderpicker',
		//scrollbar: false,
		//shade : [ 0.3, '#fff' ],
		//content : '<div style="padding: 0 10px;"><iframe src="" name="iframe_a"></iframe></div>',
		area : [ '900px', '550px' ]
		
	})
}
function open_dispathc_pool(){
	closeId = layer.open({
		id:"dispathc_pool",
		title : '任务池',
		maxmin: true,
		type: 2,
		shade : 0,
		content : '/console/dispatch',
		//scrollbar: false,
		//shade : [ 0.3, '#fff' ],
		//content : '<div style="padding: 0 10px;"><iframe src="" name="iframe_a"></iframe></div>',
		area : [ '700px', '550px' ]
		
	})
	//layer.full(closeId);
}
function close_layer_win(){
	layer.close(closeId);
}
function start_boder_interval() {
	//开启定时器
	window.setInterval(function() {
		var hour = new Date().getHours();
		$("#today_ele").html(hour * 97);
		$.ajax({
			url : '/console/border/blue/data',
			data : {},
			type : "post",
			dataType : "json",
			//async : true,
			success : function(data) {
				if(data.result){
					//提取数据 调用各个图表对应的方法
					getDataAndReviewQualityChart(data.data.quality); 
					getDataAndReviewDeviceChart(data.data.device); 
					getDataAndReviewOrderChart(data.data.order); 
					getDataAndReviewActivationChart(data.data.activation);
					getDataAndReviewBeatChart(data.data.beat);
					
				}
			},
			error :function(){
				base_alert_info('获取状态失败');
			}
		})
	}, 5000);
}

//稼动率
function getDataAndReviewActivationChart(data){
	$("#border_activation_list tr:gt(0)").remove();
	for (var i = 0; i < data.length; i++) {
		if(!data[i].plan_time)
		{
			data[i].plan_time=1;
		}
		if(!data[i].ok_rate)
		{
			data[i].ok_rate=0;
		}
		var str=
		'<tr>'+
		'	<td>'+
		'		<div class="border_order_circle">&nbsp;'+(i+1)+'</div>'+
		'	</td>'+
		'	<td>'+data[i].device_name+'</td>'+
		'	<td>'+data[i].plan_time+'</td>'+
		'	<td>'+data[i].real_time+'</td>'+
		'	<td>'+(data[i].real_time/data[i].plan_time*1.0*100).toFixed(2)+'%</td>'+
		'	<td>'+(data[i].ok_rate*100).toFixed(2)+'%</td>'+
		'	<td>'+(data[i].real_time/data[i].plan_time*1.0*data[i].ok_rate*100).toFixed(2)+'%</td>'+
		'	<td>'+(data[i].real_time/data[i].plan_time*1.0*data[i].ok_rate*data[i].ok_rate*100).toFixed(2)+'%</td>'+
		'</tr>';
		$("#border_activation_list").append(str);
	}
}
//订单
function getDataAndReviewOrderChart(data){
	$("#border_order_list tr:gt(0)").remove();
	for (var i = 0; i < data.length; i++) {
		var str=
		'<tr>'+
		'	<td>'+
		'		<div class="border_order_circle">&nbsp;'+(i+1)+'</div>'+
		'	</td>'+
		'	<td>'+data[i].emd_date+'</td>'+
		'	<td>'+data[i].emd_order+'</td>'+
		'	<td>'+data[i].emd_desc+'</td>'+
		'	<td>'+data[i].emd_sl+'</td>'+
		'	<td>'+data[i].emd_endsl+'</td>'+
		
		'	<td>'+data[i].emd_out+'</td>'+
		'	<td>'+data[i].emd_process1+'</td>'+
		'	<td>'+data[i].emd_process2+'</td>'+
		'	<td>'+data[i].emd_process3+'</td>'+
		'	<td>'+data[i].emd_in+'</td>'+
		'	<td>'+data[i].emd_state+'</td>'+
		'</tr>';
		$("#border_order_list").append(str);
	}
} 
//设备
function getDataAndReviewDeviceChart(datas) {
	
	var s0 = 0;
	var s1 = 0;
	var s2 = 0;
	$("#border_device_list tr:gt(0)").remove();
	for (var i = 0; i < datas.length; i++) {
		var s = datas[i].device_status;
		if(s == '0'){
			s0++;
		}
		else if(s == '1'){
			s1++;
		}
		else if(s == '2'){
			s2++;
		}
		var str=
			'<tr>'+
			'	<td>'+	datas[i].device_name+
			'	</td>'+
			'	<td class="device_item_stop">'+(datas[i].device_status=='2'?'<div style="height: 15px;width:15px;" class="device_item_div"></div>':'')+'</td>'+
			'	<td class="device_item_run">'+(datas[i].device_status=='1'?'<div style="height: 15px;width:15px;" class="device_item_div"></div>':'')+'</td>'+
			'	<td class="device_item_err">'+(datas[i].device_status=='0'?'<div style="height: 15px;width:15px;" class="device_item_div"></div>':'')+'</td>'+
			'</tr>';
		$("#border_device_list").append(str);
	}
	var data = [];
	data.push({
		name:"异常",
		value:s0
	});
	data.push({
		name:"开启",
		value:s1
	});
	data.push({
		name:"关闭",
		value:s2
	});
	// 指定图表的配置项和数据
	option = { 
			color:['red', 'green','blue'],
		    series: [{
		        data: data,
		        type: 'pie',
		        itemStyle:{ 
		            normal:{ 
		                  label:{ 
		                    show: true, 
		                    formatter: '{b} : {c}' 
		                  }, 
		                  labelLine :{show:true} 
		                } 
		            } 
		    },
		    
		    ]
		};
	// 使用刚指定的配置项和数据显示图表。
	//deviceChart.setOption(option);
	//deviceChart.hideLoading();
}
//质量
function getDataAndReviewQualityChart(data) {
	qualityChart.showLoading({
		text : "加载数据中..."
	});
	var names =[];
	var datas = [];
	$("#border_quality_list tr:gt(0)").remove();
	for(var i=0;i<data.length-1;i++){//外层循环控制排序趟数
	　　　　　　for(var j=0;j<data.length-1-i;j++){//内层循环控制每一趟排序多少次
	　　　　　　　　if(data[j].sum>data[j+1].sum){
	　　　　　　　　　　var temp=data[j];
	　　　　　　　　　　data[j]=data[j+1];
	　　　　　　　　　　data[j+1]=temp;
	　　　　　　　　}
	　　　　　　}
	} 
	for (var i = 0; i < data.length; i++) {
		names.push(data[i].eq_name);
		datas.push(data[i].sum);
		
		
		var str=
		'<tr>'+
		'	<td>'+
		'		<div class="border_order_circle">&nbsp;'+(i+1)+'</div>'+
		'	</td>'+
		'	<td>'+data[i].eq_name+'</td>'+
		'	<td>'+data[i].sum+'</td>'+
		'</tr>';
		$("#border_quality_list").append(str);
	}
	
	option = {
			color : [ '#3398DB' ],
			grid : {
				left : '3%',
				right : '4%',
				bottom : '3%',
				containLabel : true
			},
			xAxis : [
				{
					splitLine:{show: false},//去除网格线
					type : 'category',
					data : names,
					axisTick : {
						alignWithLabel : true
					}
				}
			],
			yAxis : [
				{
					splitLine:{show: false},//去除网格线
					type : 'value'
				}
			],
			series : [
				{
					name : '直接访问',
					type : 'bar',
					barWidth : '60%',
					data : datas
				}
			]
		};
	
	// 使用刚指定的配置项和数据显示图表。
	qualityChart.setOption(option);
	qualityChart.hideLoading();
	$('#quality_chart_main canvas').css("top","-50px");
}
//节拍
function getDataAndReviewBeatChart(data) {
	beatChart.showLoading({
		text : "加载数据中..."
	});
	var names =[];
	var datas = [];
	for (var i = 0; i < data.length; i++) {
		names.push(data[i].beat_name);
		datas.push(data[i].beat_num);
	}
	
	option = {
			color : [ '#3398DB' ],
			grid : {
				left : '3%',
				right : '4%',
				bottom : '3%',
				containLabel : true
			},
			xAxis : [
				{
					splitLine:{show: false},//去除网格线
					type : 'category',
					data : names,
					axisTick : {
						alignWithLabel : true
					}
				}
			],
			yAxis : [
				{
					splitLine:{show: false},//去除网格线
					type : 'value'
				}
			],
			series : [
				{
					name : '直接访问',
					type : 'bar',
					barWidth : '60%',
					data : datas
				}
			]
		};
	
	// 使用刚指定的配置项和数据显示图表。
	beatChart.setOption(option);
	beatChart.hideLoading();
	$('#beat_chart_main canvas').css("top","-50px");
}
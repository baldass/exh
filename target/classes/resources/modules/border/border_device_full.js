$(function() {
	orderChart = echarts.init(document.getElementById('order_chart_main'), 'dark');
	qualChart = echarts.init(document.getElementById('qual_chart_main'), 'dark');
	//初始化
	start_boder_interval();
	setInterval("setTimer(tg)",1000); 
})
var tg ;//启动时间时间戳
var realProduce;
var shouleProduce;
var orderChart; //订单柱图
var qualChart; //质量饼图
//订单
function getDataAndReviewOrderChart(names,datas) {
	// 指定图表的配置项和数据
	/*var names =[];
	var datas = [];
	for (var i = 0; i < data.length; i++) {
		names.push(data[i].name);
		datas.push(data[i].value);
	}*/
	
	var option = {
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
					
					type : 'bar',
					barWidth : '60%',
					data : datas
				}
			]
		};
	// 使用刚指定的配置项和数据显示图表。
	orderChart.setOption(option);
	orderChart.hideLoading();
	$('#order_chart_main canvas').css("top","-25px");
}
//质量
function getDataAndReviewQualChart(dt) {
	var data =[];
	for (var i = 0; i < dt.length; i++) {
		var node = dt[i];
		data.push({
			name:node.eq_name,
			value:node.sum
		})
}
	var option = {
			color : [ 'yellow', 'green' ],
			series : [ {
				data : data,
				type : 'pie',
				itemStyle : {
					normal : {
						label : {
							show : true,
							formatter : '{b}'
						},
						labelLine : {
							show : true
						}
					}
				}
			},
			]
		};
	// 使用刚指定的配置项和数据显示图表。
	qualChart.setOption(option);
	qualChart.hideLoading();
	$('#heap_chart_main canvas').css("top","-50px");
}

function start_boder_interval() {
	window.setInterval(function() {
		$.ajax({
			url : '/console/border/blue/device/data',
			data : {device_name:device_name},
			type : "post",
			dataType : "json",
			//async : true,
			success : function(data) {
				if (data.result) {
					//订单柱形图数据
					var names = [];
					var datas = [];
					names.push('计划');
					names.push('实际');
					names.push('应产');
					//提取数据 调用各个图表对应的方法
					tg=data.data.startStamp;//启动时间
					
					$("#device_item_start_time").html(formatDateTimeHHmmss(new Date(tg)));//启动时
					if(data.data.moDetail[0]){
						$("#device_item_order").html(data.data.moDetail[0].emd_order);//单号
						$("#device_item_plan").html(data.data.moDetail[0].emd_sl);//计划
						datas.push(data.data.moDetail[0].emd_sl);//计划
						$("#device_item_real_num").html(data.data.moDetail[0].emd_endsl);//实际
						datas.push(data.data.moDetail[0].emd_endsl);//实际
						$("#device_item_ng").html(data.data.moDetail[0].emd_ng);//不良
						var should = data.data.moDetail[0].emd_endsl;
						if(should<=0)
							should = 0;
						$("#device_item_should_num").html(should);//应该
						datas.push(should);//应该
					}else{
						$("#device_item_order").html("...");//单号
						$("#device_item_plan").html(0);//计划
						$("#device_item_real_num").html(0);//实际
						$("#device_item_ng").html(0);//不良
						$("#device_item_should_num").html(0);//应该
					}
					
					$("#device_item_status").html(data.data.deviceStatus);//状态
					
					//质量
					getDataAndReviewQualChart(data.data.quality); 
					//订单柱图
					getDataAndReviewOrderChart(names,datas);
				}
			},
			error : function() {
				base_alert_info('获取状态失败');
			}
		})
	}, 5000);
	
	
	return ;
	
}

function setTimer(timestamp){
	$("#during_time").html(timeago(timestamp));

}

function timeago(timestamp){
	   var leftTime = (new Date())- (timestamp); //计算已用的毫秒数   
	   var days = parseInt(leftTime / 1000 / 60 / 60 / 24 , 10); //计算剩余的天数   
	   var hours = parseInt(leftTime / 1000 / 60 / 60 % 24 , 10); //计算剩余的小时   
	   var minutes = parseInt(leftTime / 1000 / 60 % 60, 10);//计算剩余的分钟   
	   var seconds = parseInt(leftTime / 1000 % 60, 10);//计算剩余的秒数   
	   days = checkTime(days);   
	   hours = checkTime(hours);   
	   minutes = checkTime(minutes);   
	   seconds = checkTime(seconds);   
		var timehtml= hours+":" + minutes+":"+seconds; 
		return timehtml;
}

function checkTime(i){ 
	   //将0-9的数字前面加上0，例1变为01   
	   if(i<10)   { 
	   i = "0" + i;  
	   }   
	   return i; 
}
function format_datetime(Date){
	var Y = Date.getFullYear();
	var M = Date.getMonth() + 1;
		M = M < 10 ? '0' + M : M;// 不够两位补充0
	var D = Date.getDate();
		D = D < 10 ? '0' + D : D;
	var H = Date.getHours();
		H = H < 10 ? '0' + H : H;
	var Mi = Date.getMinutes();
		Mi = Mi < 10 ? '0' + Mi : Mi;
	var S = Date.getSeconds();
		S = S < 10 ? '0' + S : S;
		return Y + '-' + M + '-' + D + ' ' + H + ':' + Mi + ':' + S;
}
function formatDateTimeHHmmss(inputTime) {  
    var date = new Date(inputTime);
    var y = date.getFullYear();  
    var m = date.getMonth() + 1;  
    m = m < 10 ? ('0' + m) : m;  
    var d = date.getDate();  
    d = d < 10 ? ('0' + d) : d;  
    var h = date.getHours();
    h = h < 10 ? ('0' + h) : h;
    var minute = date.getMinutes();
    var second = date.getSeconds();
    minute = minute < 10 ? ('0' + minute) : minute;  
    second = second < 10 ? ('0' + second) : second; 
    return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second;  
};
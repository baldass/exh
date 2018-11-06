$(function() {
	completionChart = echarts.init(document.getElementById('completion_chart_main'), 'dark');
	heapChart = echarts.init(document.getElementById('heap_chart_main'), 'dark');
	//初始化
	start_boder_interval();
})
var completionChart; //完成度饼图
var heapChart; //累积数量柱状图
var heapDate=[];
//完成度
function getDataAndReviewCompletionChart(data) {
	// 指定图表的配置项和数据
	var option = {
		color : [ 'yellow', 'green' ],
		series : [ {
			data : data,
			type : 'pie',
			itemStyle : {
				normal : {
					label : {
						show : true,
						formatter : '{b} : {c}'
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
	completionChart.setOption(option);
	completionChart.hideLoading();
}
//累积数量
function getDataAndReviewHeapChart() {
	var names =[];
	var datas = [];
	for (var i = 0; i < heapDate.length; i++) {
		names.push(heapDate[i].heap_name);
		datas.push(heapDate[i].heap_num);
	}
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
	heapChart.setOption(option);
	heapChart.hideLoading();
	$('#heap_chart_main canvas').css("top","-50px");
}
function start_boder_interval() {
	//开启定时器
	window.setInterval(function() {
		var val = "";
		if(this.dev_id){//存在变量的时候
			val = dev_id;
		}
		$.ajax({
			url : '/console/border/blue/device/data',
			data : {dev_id:val},
			type : "post",
			dataType : "json",
			//async : true,
			success : function(data) {
				if (data.result) {
					//提取数据 调用各个图表对应的方法
					var comp = [{
						name:"已完成",
						value:data.data.moDetail.emd_endsl
					},{
						name:"未完成",
						value:(data.data.moDetail.emd_sl-data.data.moDetail.emd_endsl)
					}];
					deviceStatus
					getDataAndReviewCompletionChart(comp);
					heapDate.push(data.data.moDetail.emd_endsl);
					if(heapDate.length>=10){
						heapDate.shift();
					}
					getDataAndReviewHeapChart();
					$("").html(data.data.moDetail);
					var st = data.data.deviceStatus;
					var sts = "";
					if(st =='1'){
						sts = '正常';
					}else if(st =='2'){
						sts='关闭';
					}else if(st =='0'){
						sts = '异常';
					}
					$("#border_device_name").html(data.data.moDetail);
					$("#border_device_name").html(sts);
				}
			},
			error : function() {
				base_alert_info('获取状态失败');
			}
		})
		/*getDataAndReviewHeapChart(
			[
				{heap_name:'2015-5-5',heap_num:1},
				{heap_name:'2015-5-6',heap_num:2},
				{heap_name:'2015-5-7',heap_num:3},
			]
		);
		getDataAndReviewCompletionChart([
			{name:'已完成',value:98},
			{name:'未完成',value:2}
		]);*/
	}, 1000); //10s一次 测试时候为1s
}

/**
 * 
 */
var $table_manage1; //表格对象
var $table_manage2; //表格对象
var $table_manage3; //表格对象
//初始化bootstrap-table的内容
function InitMainTable() {
	var queryUrl1 = '/console/order/list';
	var queryUrl2 = '/console/material/list';
	var queryUrl3 = '/console/ins/list';
	$table_manage1 = $('#manage_table1').bootstrapTable({
		url : queryUrl1, //请求后台的URL（*）
		method : 'GET', //请求方式（*）
		//toolbar: '#toolbar',              //工具按钮用哪个容器
		striped : true, //是否显示行间隔色
		cache : false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		//pagination : true, //是否显示分页（*）
		sortable : true, //是否启用排序
		sortOrder : "asc", //排序方式
		sidePagination : "server", //分页方式：client客户端分页，server服务端分页（*）
		pageNumber : 1, //初始化加载第一页，默认第一页,并记录
		pageSize : 10, //每页的记录行数（*）
		pageList : [ 10, 25, 50, 100 ], //可供选择的每页的行数（*）
		search : false, //是否显示表格搜索
		strictSearch : true,
		//showColumns : true, //是否显示所有的列（选择显示的列）
		//showRefresh : true, //是否显示刷新按钮
		//minimumCountColumns : 2, //最少允许的列数
		clickToSelect : true, //是否启用点击选中行
		//height: 500,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
		uniqueId : "ID", //每一行的唯一标识，一般为主键列
		//showToggle : true, //是否显示详细视图和列表视图的切换按钮
		//cardView : false, //是否显示详细视图
		//detailView : false, //是否显示父子表
		//得到查询的参数
		queryParams : function(params) {
			//这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			var temp = {
				rows : params.limit, //页面大小
				page : (params.offset / params.limit) + 1, //页码
				sort : params.sort, //排序列名  
				sortOrder : params.order, //排位命令（desc，asc） 

			};
			return temp;
		},
		columns : [

			{
				field : 'mo_no',
				title : '单号'
			},
			{
				field : 'MC_Name',
				title : '产品名'
			},{
				field : 'mo_sl',
				title : '订单数量'
			},{
				field : 'mo_end_sl',
				title : '订单完成数量'
			},{
				field : 'mo_pass_sl',
				title : '订单通过数量'
			},{
				field : 'mo_NG_sl',
				title : '订单未通过数量'
			},{
				field : 'plan_start',
				title : '计划开始时间'
			},{
				field : 'plan_start',
				title : '计划结束时间'
			}
		],
		onLoadSuccess : function() {},
		onLoadError : function(d) {},
		onDblClickRow : function(row, $element) {},
	});
	$table_manage2 = $('#manage_table2').bootstrapTable({
		url : queryUrl2, //请求后台的URL（*）
		method : 'GET', //请求方式（*）
		//toolbar: '#toolbar',              //工具按钮用哪个容器
		striped : true, //是否显示行间隔色
		cache : false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		//pagination : true, //是否显示分页（*）
		sortable : true, //是否启用排序
		sortOrder : "asc", //排序方式
		sidePagination : "server", //分页方式：client客户端分页，server服务端分页（*）
		pageNumber : 1, //初始化加载第一页，默认第一页,并记录
		pageSize : 10, //每页的记录行数（*）
		pageList : [ 10, 25, 50, 100 ], //可供选择的每页的行数（*）
		search : false, //是否显示表格搜索
		strictSearch : true,
		//showColumns : true, //是否显示所有的列（选择显示的列）
		//showRefresh : true, //是否显示刷新按钮
		//minimumCountColumns : 2, //最少允许的列数
		clickToSelect : true, //是否启用点击选中行
		//height: 500,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
		uniqueId : "ID", //每一行的唯一标识，一般为主键列
		//showToggle : true, //是否显示详细视图和列表视图的切换按钮
		//cardView : false, //是否显示详细视图
		//detailView : false, //是否显示父子表
		//得到查询的参数
		queryParams : function(params) {
			//这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			var temp = {
				rows : params.limit, //页面大小
				page : (params.offset / params.limit) + 1, //页码
				sort : params.sort, //排序列名  
				sortOrder : params.order, //排位命令（desc，asc） 

			};
			return temp;
		},
		columns : [

			{
				field : 'material_name',
				title : '物料名称'
			},
			{
				field : 'material_in_producing',
				title : '生产中物料数量'
			},
			{
				field : 'material_left',
				title : '物料剩余'
			},
		],
		onLoadSuccess : function() {},
		onLoadError : function(d) {},
		onDblClickRow : function(row, $element) {},
	});
	$table_manage3 = $('#manage_table3').bootstrapTable({
		url : queryUrl3, //请求后台的URL（*）
		method : 'GET', //请求方式（*）
		//toolbar: '#toolbar',              //工具按钮用哪个容器
		striped : true, //是否显示行间隔色
		cache : false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		//pagination : true, //是否显示分页（*）
		sortable : true, //是否启用排序
		sortOrder : "asc", //排序方式
		sidePagination : "server", //分页方式：client客户端分页，server服务端分页（*）
		pageNumber : 1, //初始化加载第一页，默认第一页,并记录
		pageSize : 10, //每页的记录行数（*）
		pageList : [ 10, 25, 50, 100 ], //可供选择的每页的行数（*）
		search : false, //是否显示表格搜索
		strictSearch : true,
		//showColumns : true, //是否显示所有的列（选择显示的列）
		//showRefresh : true, //是否显示刷新按钮
		//minimumCountColumns : 2, //最少允许的列数
		clickToSelect : true, //是否启用点击选中行
		//height: 500,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
		uniqueId : "ID", //每一行的唯一标识，一般为主键列
		//showToggle : true, //是否显示详细视图和列表视图的切换按钮
		//cardView : false, //是否显示详细视图
		//detailView : false, //是否显示父子表
		//得到查询的参数
		queryParams : function(params) {
			//这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			var temp = {
				rows : params.limit, //页面大小
				page : (params.offset / params.limit) + 1, //页码
				sort : params.sort, //排序列名  
				sortOrder : params.order, //排位命令（desc，asc） 

			};
			return temp;
		},
		columns : [

			{
				field : 'ins_pass',
				title : '通过数量'
			},
			{
				field : 'ins_NG',
				title : '未通过数量'
			},{
				field : 'ins_reason',
				title : '未通过原因'
			}
		],
		onLoadSuccess : function() {},
		onLoadError : function(d) {},
		onDblClickRow : function(row, $element) {},
	});
};
//刷新
function table_manage_refresh() {
	$table_manage1.bootstrapTable('refresh');
}
$(function() {
	load_device_infos();
	InitMainTable();
})
function add_device_item(obj){
	var str = 
	'<div class="device_item">'+
	'	<div class="device_item_img">'+
	'		<img alt="" src="'+obj.img_path+'"  >'+
	'	</div>'+
	'	<div class="device_item_detail">'+
	'		<div>设备名字:'+obj.device_name+'</div>'+
	'		<div>是否正常:'+obj.status+'</div>'+
	'		<div>是否启动:'+obj.is_start+'</div>'+
	'	</div>'+
	'</div>'
	
	$(".device_list").append(str);
}
function load_device_infos(){
	var url = "device/list";
	$.ajax({
		url : url,
		data : {},
		type : "post",
		dataType : "json",
		//async : true,
		success : function(data) {
			if(data.result){
				$(".device_list").html('');
				for (var i = 0; i < data.data.length; i++) {
					add_device_item(data.data[i]);
				}
				return;
			}
			alert(data.msg);
		},
		error :function(){
			alert('服务器繁忙');
		}
	})
}
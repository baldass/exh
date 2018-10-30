var $table_manage; //表格对象

$(function(){
	InitMainTable();
})
//初始化bootstrap-table的内容
function InitMainTable() {
	//记录页面bootstrap-table全局变量$table_manage，方便应用
	var queryUrl = '/console/orderpicker/data';
	$table_manage = $('#manage_table').bootstrapTable({
		url : queryUrl, //请求后台的URL（*）
		method : 'GET', //请求方式（*）
		//toolbar: '#toolbar',              //工具按钮用哪个容器
		striped : true, //是否显示行间隔色
		cache : false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		pagination : true, //是否显示分页（*）
		sortable : true, //是否启用排序
		sortOrder : "asc", //排序方式
		sidePagination : "server", //分页方式：client客户端分页，server服务端分页（*）
		pageNumber : 1, //初始化加载第一页，默认第一页,并记录
		pageSize : 10, //每页的记录行数（*）
		pageList : [ 10, 25, 50, 100 ], //可供选择的每页的行数（*）
		search : false, //是否显示表格搜索
		strictSearch : true,
		showColumns : true, //是否显示所有的列（选择显示的列）
		showRefresh : true, //是否显示刷新按钮
		minimumCountColumns : 2, //最少允许的列数
		clickToSelect : true, //是否启用点击选中行
		//height: 500,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
		uniqueId : "ID", //每一行的唯一标识，一般为主键列
		showToggle : true, //是否显示详细视图和列表视图的切换按钮
		cardView : false, //是否显示详细视图
		detailView : false, //是否显示父子表
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
				title : '订单',
			},
			{
				field : 'mc_name',
				title : '物品',
			},
			{
				field : 'mo_sl',
				title : '计划数量',
			},
			{
				field : 'input_time',
				title : '录入时间',
			},
			{
				field : 'mo_state',
				title : '状态',
			},
			{
				field : 'ID',
				title : '操作',
				width : 120,
				align : 'center',
				valign : 'middle',
				formatter : function(value, row, index) {
					var mo_no = row.mo_no;
					var mc_name = row.mc_name;
					var emd_sl = row.mo_sl;
					var result = "";
					result += '<a href="#" class="btn blue mini" onclick="order_start(\'' + mo_no + '\',\'' + mc_name + '\',\'' + emd_sl + '\')" ><i class="icon-edit"></i>开工</a>';
					return result;
				}
			}, ],
		onLoadSuccess : function() {},
		onLoadError : function(d) {},
		onDblClickRow : function(row, $element) {}
	});
}
function order_start(mo_no,mc_name,emd_sl){
	$.ajax({
		url : '/console/orderpicker/start',
		data : {
			mo_no:mo_no,
			mc_name:mc_name,
			emd_sl:emd_sl
		},
		type : "post",
		dataType : "json",
		//async : true,
		success : function(data) {
			if(data.result){
				$table_manage.bootstrapTable('refresh');
				base_alert_ok("操作成功");
				//调用父级总窗口的关闭方法
				//window.parent.close_layer_win();
			}
		},
		err:function(e){
			
		}
	})
	
	
}
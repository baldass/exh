var $table_manage; //表格对象
var $pool_manage; //表格对象 任务池
var $device_manage;//设备表格

var $pool_data; //表格数据
var $device_data; //设备数据

var selectDispatchId;

var editHtml,editHtml2; //编辑框 html内容

var closeId,closeId2;

//初始化bootstrap-table的内容
function InitMainTable() {
	//记录页面bootstrap-table全局变量$table_manage，方便应用
	var queryUrl = '/console/dispatch/list';
	$table_manage = $('#manage_table').bootstrapTable({
		url : queryUrl, //请求后台的URL（*）
		method : 'GET', //请求方式（*）
		//toolbar: '#toolbar',              //工具按钮用哪个容器
		striped : true, //是否显示行间隔色
		cache : false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		//pagination : true, //是否显示分页（*）
		//sortable : true, //是否启用排序
		//sortOrder : "asc", //排序方式
		//sidePagination : "server", //分页方式：client客户端分页，server服务端分页（*）
		//pageNumber : 1, //初始化加载第一页，默认第一页,并记录
		//pageSize : 10, //每页的记录行数（*）
		//pageList : [ 10, 25, 50, 100 ], //可供选择的每页的行数（*）
		//search : false, //是否显示表格搜索
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
				field : 'dispatch_order',
				title : '订单',
			},
			{
				field : 'dispatch_name',
				title : '调度描述',
			},

			{
				field : 'ID',
				title : '操作',
				width : 120,
				align : 'center',
				valign : 'middle',
				formatter : function(value, row, index) {
					var id = row.card_id;
					var result = "";
					result += '<a href="#" class="btn blue mini" onclick="table_manage_edit(' + id + ')" ><i class="icon-edit"></i>任务池</a>';
					return result;
				}
			}, ],
		onLoadSuccess : function() {},
		onLoadError : function(d) {},
		onDblClickRow : function(row, $element) {},
	});
};
//初始化bootstrap-table的内容
function InitPoolTable() {
	//记录页面bootstrap-table全局变量$table_manage，方便应用
	$pool_manage = $('#pool_manage').bootstrapTable({
		data : $pool_data,
		//toolbar: '#toolbar',              //工具按钮用哪个容器
		striped : true, //是否显示行间隔色
		cache : false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		//pagination : true, //是否显示分页（*）
		//sortable : true, //是否启用排序
		//sortOrder : "asc", //排序方式
		//sidePagination : "server", //分页方式：client客户端分页，server服务端分页（*）
		//pageNumber : 1, //初始化加载第一页，默认第一页,并记录
		//pageSize : 10, //每页的记录行数（*）
		//pageList : [ 10, 25, 50, 100 ], //可供选择的每页的行数（*）
		//search : false, //是否显示表格搜索
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
		singleSelect :true,
		columns : [
			 {
                 checkbox: true  
              },
              {  
            	    title: '序号',  
            	    field: '',  
            	    formatter: function (value, row, index) {  
            	        return index+1;  
            	    }  
            	} ,
			{
				field : 'device',
				title : '设备',
			},
			{
				field : 'handle',
				title : '动作描述',
			},
			{
				field : 'status_name',
				title : '状态',
			}
			],
		onLoadSuccess : function() {},
		onLoadError : function(d) {},
		onDblClickRow : function(row, $element) {},
	});
}
function InitDeviceTable() {
	//记录页面bootstrap-table全局变量$table_manage，方便应用
	$device_manage = $('#device_manage').bootstrapTable({
		data:$device_data ,
		//toolbar: '#toolbar',              //工具按钮用哪个容器
		striped : true, //是否显示行间隔色
		cache : false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		//pagination : true, //是否显示分页（*）
		//sortable : true, //是否启用排序
		//sortOrder : "asc", //排序方式
		//sidePagination : "server", //分页方式：client客户端分页，server服务端分页（*）
		//pageNumber : 1, //初始化加载第一页，默认第一页,并记录
		//pageSize : 10, //每页的记录行数（*）
		//pageList : [ 10, 25, 50, 100 ], //可供选择的每页的行数（*）
		//search : false, //是否显示表格搜索
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
		singleSelect :true,
		columns : [
			{
                checkbox: true  
            },
			{
				field : 'device',
				title : '设备',
			},
			{
				field : 'handle',
				title : '动作描述',
			},
			{
				field : 'status_name',
				title : '状态',
			}
			],
		onLoadSuccess : function() {},
		onLoadError : function(d) {},
		onDblClickRow : function(row, $element) {},
	});
}
$(function() {
	editHtml = $("#edit_content_div").html();
	$("#edit_content_div").remove();
	editHtml2 = $("#edit_content_div2").html();
	$("#edit_content_div2").remove();
	InitMainTable();
})

//编辑任务池事件
function table_manage_edit(id) {
	var allTableData = $table_manage.bootstrapTable('getData');
	var selectObj;
	for (var i = 0; i < allTableData.length; i++) {
		var obj = allTableData[i];
		if (obj.card_id == id) {
			selectObj = obj;
			break;
		}
	}
	
	var id = selectObj.dispatch_id;
	selectDispatchId = id;
	$.ajax({
		url : "/console/dispatch/device",
		data : {
		},
		type : "post",
		dataType : "json",
		//async : true,
		success : function(data) {
			if (data.result) {
				$device_data =data.data;
				return;
			}
			base_alert_info(data.msg);
		},
		error : function() {
			base_alert_info('服务器繁忙');
		}
	})
	$.ajax({
		url : "/console/dispatch/detail",
		data : {
			dispatch_id : id
		},
		type : "post",
		dataType : "json",
		//async : true,
		success : function(data) {
			if (data.result) {
				$pool_data = JSON.parse(data.data);
				//填充数据
				closeId = layer.open({
					type : 1,
					id:"detail",
					shade : [ 0.3, '#fff' ],
					title : false,
					//scrollbar: false,         
					shadeClose : false,
					content : editHtml,
					area : [ '600px', '480px' ],
					btn : [ '保存', '新增', '上移', '下移', '删除', '关闭' ],
					success : function(layero, index) {
						InitPoolTable();
					},
					yes : function(index, layero) {
						//保存
						pool_manage_save();
					},
					btn2 : function(index, layero) {
						//新增
						pool_manage_device()
						return false;
					},
					btn3 : function(index, layero) {
						//上移
						pool_manage_up();
						return false;
					},
					btn4 : function(index, layero) {
						//下移
						pool_manage_down();
						return false;
					},
					btn5 : function(index, layero) {
						//删除
						pool_manage_del();
						return false;
					},
					btn6 : function(index, layero) {
						//关闭
					}
				});
				return;
			}
			base_alert_fail(data.msg);
		},
		error : function() {
			base_alert_info('服务器繁忙');
		}
	})

}



//刷新
function table_manage_refresh() {
	$table_manage.bootstrapTable('refresh');
}

//刷新详细
function pool_manage_add(row) {
	if(!$pool_data)
		$pool_data=[];
	var newRow= {};//浅复制一个对象
	for (var it in row) {
		newRow[it]=row[it];
	}
	$pool_data.push(newRow);
	$pool_manage.bootstrapTable('load', $pool_data);
	
}
//删除详细
function pool_manage_del() {
	//删除
	var ss = $pool_manage.bootstrapTable("getSelections");
	if(!ss.length){
		base_alert_fail('请先选择!')
		return ;
	}
	var s=ss[0];
	var del = null;
	for (var i = 0; i < $pool_data.length; i++) {
		if(s == $pool_data[i]){
			del = i;
		}
	}
	if(del != null){
		$pool_data.splice(del, 1);
		$pool_manage.bootstrapTable('load', $pool_data);
	}
	
}
//上移
function pool_manage_up() {
	//删除
	var ss = $pool_manage.bootstrapTable("getSelections");
	if(!ss.length){
		base_alert_fail('请先选择!')
		return ;
	}
	var s=ss[0];
	var idx = null;
	for (var i = 0; i < $pool_data.length; i++) {
		if(s == $pool_data[i]){
			idx = i;
		}
	}
	if( idx ){
		var temp =null;
		temp =$pool_data[idx];
		$pool_data[idx] =$pool_data[idx-1];
		$pool_data[idx-1] =temp;
		$pool_manage.bootstrapTable('load', $pool_data);
	}
}
//下移
function pool_manage_down() {
	//删除
	var ss = $pool_manage.bootstrapTable("getSelections");
	if(!ss.length){
		base_alert_fail('请先选择!')
		return ;
	}
	var s=ss[0];
	var idx = null;
	var pdlen=$pool_data.length;
	for (var i = 0; i < pdlen; i++) {
		if(s == $pool_data[i]){
			idx = i;
		}
	}
	if( idx!=null && idx < (pdlen-1) ){
		var temp =null;
		temp =$pool_data[idx];
		$pool_data[idx] =$pool_data[idx+1];
		$pool_data[idx+1] =temp;
		$pool_manage.bootstrapTable('load', $pool_data);
	}
}
//显示设备列表
function pool_manage_device() {
	closeId2 = layer.open({
		type : 1,
		shade : [ 0.3, '#fff' ],
		title : false,
		id:"device",
		//scrollbar: false,         
		shadeClose : true,
		content : editHtml2,
		area : [ '600px', '480px' ],
		btn : [ '选择',  '关闭' ],
		success : function(layero, index) {
			InitDeviceTable();
		},
		yes : function(index, layero) {
			//保存
			var s = $device_manage.bootstrapTable("getSelections");
			if(!s.length){
				base_alert_fail('请先选择!')
				return ;
			}
			$device_manage.bootstrapTable("uncheckAll");
			
			pool_manage_add(s[0]);
			pool_manage_device();
			base_alert_ok('添加成功!')
			//layer.close(closeId2);
		},
	
	});

}
function pool_manage_save(){
	$.ajax({
		url : "/console/dispatch/update",
		data : {
			"dispatch_id":selectDispatchId,
			"dispatch_detail":JSON.stringify($pool_data)
		},
		type : "post",
		dataType : "json",
		//async : true,
		success : function(data) {
			if (data.result) {
				base_alert_ok(data.msg);
				layer.close(closeId);
				return;
			}
			base_alert_info(data.msg);
		},
		error : function() {
			base_alert_info('服务器繁忙');
		}
	})
	
}
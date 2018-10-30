//////////////////////
//////////////////////
////全局自定义方法
//////////////////////
//////////////////////

//快速调用的遮罩层 start_loading()开启 close_loading()关闭
var loading_load_index;
function start_loading(title) {
	if (!title) {
		title = '加载中...';
	}
	loading_load_index = layer.load(1, {
		shade : [ 0.3, '#fff' ]
	})
}
//快速调用的遮罩层 start_loading()开启 close_loading()关闭
function close_loading() {
	layer.close(loading_load_index);
}
function toStr(o) {
	if (!o) {
		return '';
	}
	return o.toString();
} 
//弹窗成功
function base_alert_ok(msg) {
	msg = "<span style='color:black;'>"+msg+"</span>";
	layer.msg(msg, {
		icon : 1
	});
}
//弹窗失败
function base_alert_fail(msg) {
	msg = "<span style='color:black;'>"+msg+"</span>";
	layer.msg(msg, {
		icon : 2
	});
}
//弹窗
function base_alert_info(msg) {
	msg = "<span style='color:black;'>"+msg+"</span>";
	layer.msg(msg, {
		icon : 7
	});
}
//弹窗
function base_alert_promt(msg, callback) {
	//示范一个公告层
	layer.open({
		type : 1,
		title : false, //不显示标题栏
		closeBtn : false,
		area : '300px;',
		shade : 0.2,
		id : 'base_alert_promt', //设定一个id，防止重复弹出
		resize : false,
		btn : [ '确定', '取消' ],
		btnAlign : 'c',
		moveType : 1, //拖拽模式，0或者1
		content : '<div style="padding: 50px; line-height: 22px; ' +
			'background-color: #555555; color: #fff; font-weight: 300;">' +
			msg + '</div>',
		yes : function(r) {
			layer.close(r); //关闭
			if (callback) {
				callback();
			}
		}
	});

}
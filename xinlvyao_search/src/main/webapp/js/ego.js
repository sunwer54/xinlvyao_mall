
var TT = EGOU = {
	checkLogin : function(){

		$.ajax({
			url : "http://localhost:8084/user/token",
			// dataType : "jsonp",
			type : "GET",
			crossDomain:true, //设置允许ajax跨域请求为true
			xhrFields: {
				withCredentials: true //默认情况下，标准的跨域请求是不会发送cookie的
			},
			success : function(data){
				var username = data.data.username;
				 $("#loginbar").html(username+"，欢迎来到京东！<a id='my_logout_id' href=\"http://localhost:8084/user/logout\">[退出]</a>");
			}
		});
	}
}




$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	TT.checkLogin();
	$(".link-logout").live("click",function(){
		var href=$(this).attr("href");
		$.ajax({
			url:href,
			type:'post',
			crossDomain:true, //设置跨域为true
			xhrFields: {
				withCredentials: true //默认情况下，标准的跨域请求是不会发送cookie的
			},
			// jsonp:'callback',
			// dataType:'jsonp',
			// jsonpCallback:'abc',
			success:function(data){
				if(data.status==200){
					$("#loginbar").html('您好！欢迎来到京东！<a href="javascript:login()">[登录]</a>&nbsp;<a href="javascript:regist()">[免费注册]</a>');
				}
			}
		});
		return false;
	})
});
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--[if lt IE 8]> <html class="lt-ie8"><![endif]-->
<!--[if gt IE 8]><!-->
<html>
<!--<![endif]-->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title></title>
<link rel="stylesheet" href="<c:url value='/styles/global.css'/>" />
<link rel="stylesheet" href="<c:url value='/styles/common.css'/>" />
<link rel="stylesheet" href="<c:url value='/styles/home.css'/>" />
<link rel="stylesheet" href="<c:url value='/styles/style1.css'/>" />
<link rel="stylesheet" href="<c:url value='/styles/style2.css'/>" />
<link rel="stylesheet" href="<c:url value='/styles/login.css'/>" />
<link rel="stylesheet" href="<c:url value='/styles/register.css'/>" />
<link rel="stylesheet" href="<c:url value='/styles/jscroll.css'/>" />

<script src="<c:url value='/js/jquery-1.11.2.js'/>"></script>
<script src="<c:url value='/js/jquery.caroufredsel.min.js'/>"></script>

 <script src="http://devstatic.zhongchouban.com.cn/pc/skin/js/custom.js"></script>  

</head>
<body>
	<input id="msg_hidden" type="hidden" value="${msg}" />

	<div class="pagewrapper">
		<div class="top_bar">
			<div class="main">
				<p class="left">您好 欢迎来到普惠众筹网！目前网站内测阶段仅对内部员工开放，敬请提出宝贵意见,谢谢！</p>
				<p class="right">
					<span class="login"> <a href="#"> [登录] </a> | <a href="#">
							[免费注册] </a>
					</span> <span class="infor"> <a href="#"> 退出 </a> | <a href="#">
							用户中心 </a>
					</span> <span class="email"> 服务邮箱：pub_zc@pingan.com.cn </span>
				</p>
			</div>
		</div>
		<div class="header">
			<div class="main">
				<img src="<c:url value='/images/logo.png' />" class="logo">
				<ul>
					<li><a href="#" class=""> 首页 </a></li>
					<li><a href="#" class=""> 众筹项目 </a></li>
					<li><a href="#" class=""> 发起众筹 </a></li>
					<li><a href="#" class=""> 帮助中心 </a></li>
					<li><a href="#" class=""> 筹吧 </a> <em> new </em></li>
				</ul>
			</div>
		</div>
		<div class="bodyer">
			<div class="user_page">
				<div class="main clearfix">
					<div class="left">
						<div class="user_nav">
							<div class="user_infor">
								<div class="img_part">
									<img src="<c:url value='/images/head.jpg' />">
									<div class="cover"></div>
								</div>
								<h3>Seagoo 22</h3>
							</div>
							<ul>
								<li class="item1 "><a href="user_page1.html" class=" ">
										我的众筹 </a> <span></span></li>
								<li class="item2 "><a href="user_page2.html" class=" ">
										我的认证 </a> <span></span></li>
								<li class="item3 "><a href="user_page3.html" class=" ">
										我的银行卡 </a> <span></span></li>
								<li class="item4 "><a href="user_page4.html" class=" ">
										充值提现 </a> <span></span></li>
								<li class="item5 "><a href="user_page5.html" class=" ">
										我的资料 </a> <span></span></li>
								<li class="item6 "><a href="user_page6.html" class=" ">
										收货地址 </a> <span></span></li>
								<li class="item7 "><a href="user_page7.html" class=" ">
										我的吐槽 </a> <span></span></li>
								<li class="item8 active"><a href="user_page8.html"
									class=" active"> 修改密码 </a> <span></span></li>
							</ul>
						</div>
					</div>
					<div class="right">
						<div class="user_page_inner  user_page8-1">

							<ul class="user_top cha_pass">
								<li class="first"><a href="javascript:void(0);"
									id="tab_login" class="active" onclick="switchTab(1);"> 登录密码
								</a></li>
								<li><a id="tab_trans" href="javascript:void(0);"
									onclick="switchTab(2);"> 交易密码 </a></li>

							</ul>
							<form class="cha_pass_f" id="form_modifyLoginPwd"
								action="modifyLoginPwd.do">
								<div class="input_row ">
									<label> <i>*</i>原密码
									</label>
									<div class="input_part">
										<input id="orgLoginPwd" name="orgLoginPwd" class="f_pass"
											type="password">

									</div>
									<a href="#" class="normal"> 忘记密码？ </a>
								</div>

								<div class="input_row">
									<label> <i>*</i>新密码
									</label>
									<div class="input_part ">
										<input id="newLoginPwd" name="newLoginPwd" class="f_pass"
											type="password"> <span></span>
									</div>

								</div>

								<div class="input_row">
									<label> <i>*</i>确认新密码
									</label>
									<div class="input_part ">
										<input id="newLoginPwd2" name="newLoginPwd2" class="f_pass"
											type="password"> <span></span>
									</div>

								</div>

								<div class="password_check">
									<p>密码长度为6~20长度，必须包含数字和字母</p>
									<ul>
										<li class="password_item1 active"></li>
										<li></li>
										<li class="password_item3"></li>
									</ul>
								</div>


								<div class="input_row">
									<label> </label> <a class="submit" href="#"> 保存 </a>
								</div>

							</form>

							<form class="cha_pass_f" id="form_modifyTransPwd"
								action="modifyTransPwd.do" style="display: none">
								<div class="input_row">
									<label> <i>*</i>原密码
									</label>
									<div class="input_part">
										<input class="f_pass" type="password" value="1234546">

									</div>

								</div>

								<div class="input_row">
									<label> <i>*</i>新密码
									</label>
									<div class="input_part ">
										<input type="text"> <span></span>
									</div>

								</div>

								<div class="input_row">
									<label> <i>*</i>确认新密码
									</label>
									<div class="input_part ">
										<input type="text"> <span></span>
									</div>

								</div>

								<div class="input_row">
									<label> </label> <a class="submit" href="#"> 保存 </a>
								</div>

							</form>

						</div>
					</div>
				</div>
			</div>

			<!--[if IE 6]>
<script src="skin/js/png_fix.js"></script>
<script>
    DD_belatedPNG.fix('.user_nav .user_infor .cover');
</script>
<![endif]-->
		</div>
		<div class="footer">
			<div class="main">
				<div class="top">
					<p class="bottom_menu">
						<a href="#"> 关于我们 </a> | <a href="#"> 人才招聘 </a> | <a href="#">
							联系我们 </a> <br> <a href="#"> 用户协议 </a> | <a href="#"> 友情链接 </a> |
						<a href="#"> 网站地图 </a>
					</p>
					<ul class="bottom_infor">
						<li class="item1">服务邮箱<br> pub_zc@pingan.com.cn
						</li>
						<li class="item2">服务邮箱<br> pub_zc@pingan.com.cn
						</li>
					</ul>
				</div>
				<div class="bottom">
					<div class="bottom_links">
						<a href="#" class="open"> 平安集团旗下网站 </a>
						<ul>
							<li><a href="http://www.pingan.com" target="_blank">中国平安官网</a></li>
							<li><a href="http://one.pingan.com" target="_blank">平安一账通</a></li>
							<li><a href="http://www.lufax.com" target="_blank">陆金所</a></li>
							<li><a href="http://www.xsme.cn" target="_blank">平安交易所</a></li>
						</ul>
					</div>
				</div>
				<p>版权所有 ©2015 深圳前海普惠众筹交易股份有限公司 粤ICP备15042952号</p>
			</div>
		</div>
	</div>
	<div class="fix_function">
		<a href="#"> 意见<br> 反馈
		</a> <a class="last" href="#"> 返回 </a>
	</div>
	<script src="<c:url value='/js/custom.js' />"></script>

</body>
<script>

	function switchTab(str) {
		var form_login = $("#form_modifyLoginPwd");
		var form_trans = $("#form_modifyTransPwd");
		var tab_login = $("#tab_login");
		var tab_trans = $("#tab_trans");
		if (str == 1) {
			tab_login.addClass("active");
			tab_trans.removeClass("active");
			form_login.css("display", "block");
			form_trans.css("display", "none");
		} else {
			tab_login.removeClass("active");
			tab_trans.addClass("active");
			form_login.css("display", "none");
			form_trans.css("display", "block");
		}

	}

	function alertMsg() {
		var msg = $("#msg_hidden").val();
		console.log(msg);
		if (msg !== null && msg !== "" && msg !== undefined) {
			alert(msg);
		}
	}

	/*form_modifyLoginPwd*/
	$(function() {
		var form_login = $("#form_modifyLoginPwd");
		form_login.find("a.submit").click(function(e) {
			var mainobj = $(this).parents("form");
			e.preventDefault();
			e.stopPropagation();
			var action = mainobj.attr("action");

			var orgLoginPwd = mainobj.find("[name='orgLoginPwd']").val();
			var newLoginPwd = mainobj.find("[name='newLoginPwd']").val();

			//alert(orgLoginPwd);

			var data = {
				"orgLoginPwd" : md5(orgLoginPwd),
				"newLoginPwd" : md5(newLoginPwd)
			};

			$.ajax({
				type : 'POST',
				url : action,
				data : data,
				success : function(data) {
					console.log("ret: ", data);
					if (data.success === true) {
						alert("修改成功");
					}
				},
				error : function(error) {
					alert("修改失败");
					console.log("error data: ", error);
				},
				dataType : "json"
			});

		});

	});

	/*form_modifyTransPwd*/
	$(function() {
		var form_trans = $("#form_modifyTransPwd");
		form_trans.find("a.submit").click(function(e) {
			var mainobj = $(this).parents("form");
			e.preventDefault();
			e.stopPropagation();
			var action = mainobj.attr("action");

			var orgLoginPwd = mainobj.find("[name='orgLoginPwd']").val();
			var newLoginPwd = mainobj.find("[name='newLoginPwd']").val();

			var data = {
				"orgLoginPwd" : md5(orgLoginPwd),
				"newLoginPwd" : md5(newLoginPwd)
			};

			$.ajax({
				type : 'POST',
				url : action,
				data : data,
				success : function(data) {
					console.log("ret: ", data);
					if (data.success === true) {
						alert("修改成功");
					}
				},
				error : function(error) {
					console.log("error data: ", error);
				},
				dataType : "json"
			});

		});

	});

	/*用window.onload调用myfun()*/
	window.onload = alertMsg;
</script>
</html>
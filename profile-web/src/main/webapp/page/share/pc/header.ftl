<!DOCTYPE html>
<!--[if lt IE 8]> <html class="lt-ie8"><![endif]-->
<!--[if gt IE 8]><!-->
<html>
<!--<![endif]-->
<head>
	<#if titleName?exists>
		<title>用户中心-平安众+网（内测） - ${titleName} </title>
	<#else>
		<title>用户中心-平安众+网（内测）</title>
	</#if>
	<meta name="author" content="用户中心-平安众+网（内测）"/>
	<meta name="copyright" content="用户中心-平安众+网（内测）"/>
	<meta name="keywords" content="用户中心-平安众+网（内测）"/>
	<meta name="description" content="用户中心-平安众+网（内测）"/>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="icon" href="${config.getDomainPcstatic()}/skin/images/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="${config.getDomainPcstatic()}/skin/css/global.css?${config.getDomainPcstaticVersion()}"/>
    <link rel="stylesheet" href="${config.getDomainPcstatic()}/skin/css/common.css?${config.getDomainPcstaticVersion()}"/>
    <link rel="stylesheet" href="${config.getDomainPcstatic()}/skin/css/home.css?${config.getDomainPcstaticVersion()}"/>
    <link rel="stylesheet" href="${config.getDomainPcstatic()}/skin/css/login.css?${config.getDomainPcstaticVersion()}"/>
    <link rel="stylesheet" href="${config.getDomainPcstatic()}/skin/css/register.css?${config.getDomainPcstaticVersion()}"/>
    <link rel="stylesheet" href="${config.getDomainPcstatic()}/skin/css/style1.css?${config.getDomainPcstaticVersion()}"/>
    <link rel="stylesheet" href="${config.getDomainPcstatic()}/skin/css/style2.css?${config.getDomainPcstaticVersion()}"/>
    <link rel="stylesheet" href="${config.getDomainPcstatic()}/skin/css/jscroll.css?${config.getDomainPcstaticVersion()}"/>
    <link rel="stylesheet" href="${config.getDomainPcstatic()}/skin/css/calendar.css?${config.getDomainPcstaticVersion()}"/>
    <script src="${config.getDomainPcstatic()}/skin/js/jquery-1.11.2.js?${config.getDomainPcstaticVersion()}"></script>
    <script src="${config.getDomainPcstatic()}/skin/js/jquery.caroufredsel.min.js?${config.getDomainPcstaticVersion()}"></script>
    <script src="${config.getDomainPcstatic()}/skin/js/manhuaDate.1.0.js?${config.getDomainPcstaticVersion()}"></script>
    <script>
    	document.domain="${config.getDomainHost()}".substring(1);
    	if (!window.console || typeof(window.console) == "undefined") {
			window.console = {};
			window.console['log'] = function() {};
		}
    </script>
</head>
<body>
	<div class="pagewrapper">
		<div class="top_bar">
			<div class="main">
				
				<p class="left">
					您好 欢迎来到平安众<u>+</u>
					<span class="login" style="display:none" id="loginpanel"> 
						<a href="${config.getDomainPassport()}/siteLogin/loginForm.do" class="first" >请登录</a>|
						<a href="${config.getDomainPassport()}/siteReg/registerForm.do">免费注册</a>
					</span>
					<span class="infor hide" id="logoutpanel">
			        	<a href="${config.getDomainProfile()}/orders.do" id="username"></a>|
			            <a href="${config.getDomainPassport()}/logout.do">退出</a>
		        	</span>
		        	<script type="text/javascript">
			        function getcookie(name) {
						var cookie_start = document.cookie.indexOf(name);
						var cookie_end = document.cookie.indexOf(";", cookie_start);
						return cookie_start == -1 ? '' : unescape(document.cookie.substring(cookie_start + name.length + 1, (cookie_end > cookie_start ? cookie_end : document.cookie.length)));
						} 
			         var COOKIE_NAME = 'userName';  
					 if(getcookie(COOKIE_NAME)){  
					   $("#username").html( getcookie(COOKIE_NAME) );
					   $("#logoutpanel").show();
					 }else{
					 	$("#loginpanel").show();
					 }
		        </script>
				</p>
				
				<p class="right">
					<a href="${config.getDomainProfile()}/orders.do"  class="active" target="_blank"> 用户中心 </a> | 
					<a href="${config.getDomainWww()}/static/help1-8.html"  target="_blank"> 帮助中心 </a> | 
					<a href="#" class="last"> 
						<span class="email"> 服务邮箱：pub_zc@pingan.com.cn</span> 
        			</a>
				</p>
			</div>
		</div>
		<div class="header">
			<div class="main">
				<a href="${config.getDomainWww()}/"><img src="${config.getDomainPcstatic()}/skin/images/logo.png" class="logo"></a>
				<ul>
					<li><a href="${config.getDomainWww()}/" > 众筹首页 </a></li>
					<li><a href="${config.getDomainItem()}/item/list.do" class="" target="_blank"> 众筹项目 </a> <em> new </em></li>
				</ul>
			</div>
		</div>
		<div class="bodyer">
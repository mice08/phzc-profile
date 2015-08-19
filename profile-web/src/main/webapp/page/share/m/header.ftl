<!DOCTYPE html>
<!--[if lt IE 8]> <html class="lt-ie8"><![endif]-->
<!--[if gt IE 8]><!-->
<html>
<!--<![endif]-->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width,maximum-scale=1, minimum-scale=1, initial-scale=1,user-scalable=no">
<title>用户中心-平安众+网（内测）</title>
<link rel="stylesheet"
	href="${config.getDomainMstatic()}/skin/css/global.css?${config.getDomainMstaticVersion()}" />
<link rel="stylesheet"
	href="${config.getDomainMstatic()}/skin/css/style.css?${config.getDomainMstaticVersion()}" />
<script
	src="${config.getDomainMstatic()}/skin/js/zepto.min.js?${config.getDomainMstaticVersion()}"></script>
<script>
	(function($) {
		var html_obj = $("html");
		var window_obj = $(window);
		//size
		function sizeConfirm() {
			var window_width = window_obj.width();
			if (window_width >= 640) {
				html_obj.css({
					"font-size" : 20 + "px"
				});
			} else {
				html_obj.css({
					"font-size" : 20 * window_width / 640 + "px"
				});
			}
		}
		sizeConfirm();
		window_obj.bind("resize", function() {
			sizeConfirm();
		});
		$(function() {
			window_obj.trigger("resize");
		})
	})(Zepto);
</script>
</head>
<body>
	<#if params?exists>
		<#if params.hStyle=="header_w">
		<div class="pagewrapper">
				<div class="header">
					<a href="#"> <img src="${config.getDomainMstatic()}/skin/images/head.jpg">
					</a>
				</div>
		<#elseif params.hStyle=="header_p">
			<#if params.backUrl?exists>
			<div class="pagewrapper">
				<div class="header_page custom_header_page">
					<span> ${params.parentName} </span> <a class="back" href="${params.backUrl}"> back
					</a>
				</div>
			<#else>
			<div class="pagewrapper">
				<div class="header_page custom_header_page">
					<span>${params.parentName} </span> <a class="back" href="javascript:void(0)" onclick="history.go(-1);"> back
					</a>
				</div>
			</#if>
		</#if>		
	</#if>
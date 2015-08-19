<#import "../../share/m/layout.ftl" as page> 
<#assign params={"hStyle":"header_p","parentName":"安全设置"} in page>
<@page.pc>
	<section class="user_change_password no_banner">
		<form class="form_order">
			<ul class="clearfix">
				<li><label> 修改登录密码 </label> <a href="${config.getDomainProfile()}/m/modifyPwd/modifyPwd.do?modifyType=login" class="normal_link "> </a></li>
				
				<#if isTransPwdSet == "1">
					<li><label> 修改交易密码 </label> <a href="${config.getDomainProfile()}/m/modifyPwd/modifyPwd.do?modifyType=trans" class="normal_link "> </a></li>
				<#else>
					<li><label> 设置交易密码 </label> <a href="${config.getDomainProfile()}/m/openAccount/setTransPassWordInit.do" class="normal_link "> </a></li>
				</#if>
				
				
			</ul>
		</form>
	</section>
</div>

<div class="logo"></div>
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
</@page.pc>

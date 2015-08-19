<#import "../../share/m/layout.ftl" as page> 
<#assign params={"hStyle":"header_p","backUrl":"${config.getDomainProfile()}/m/userInfo.do","parentName":"修改成功"} in page>
<@page.pc>
	<section class="register_detail register_success">
		<div class="result">
			<h1>
			<#if modifyType == "login">
			您的登录密码修改成功！
			<#else>
			您的交易密码修改成功！
			</#if>
			</h1>
			<h2></h2>
			<a href="${config.getDomainProfile()}/m/userInfo.do" class="submit"> 继续 </a>
		</div>
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

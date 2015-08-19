<#import "../share/pc/layout.ftl" as page> 
<@page.pc>

<div class="user_page">
	<div class="main clearfix">
	
<#include "../leftNav.ftl">
<@leftNav activeMenu="item3">
</@leftNav>

<div class="right">
	<div class="user_page_inner  user_page3">
		<h1>更换银行卡</h1>
		<div class="submit_success">
			<h2>提交成功</h2>
			<p class="type1">
				10分钟内即可收到广州银联02096585来电。<br>
				来电后，请按照语音提示输入绑定银行卡的交易密码，确认您是卡主。<br> 绑定成功或失败我们都会通过短信方式通知您。
			</p>
			<p class="type2">
				成功绑定银行卡后，您可以去<a href="${config.getDomainProfile()}/account/account.do">充值</a>、
				继续<a href="${config.getDomainProfile()}/orders.do">支付</a>订单、或去首页逛逛<br>
				未成功绑定银行卡？<a href="bindForm.do">重新绑定银行卡</a>
			</p>
			<p class="type3">
				贴心提醒<br> 1、请仔细核对来电号码，防止被骗哦<br>
				2、如认证信息未通过，您将不会收到广州银联的来电
			</p>
			<div class="img"></div>
		</div>
	</div>
</div>
</div>

</@page.pc>
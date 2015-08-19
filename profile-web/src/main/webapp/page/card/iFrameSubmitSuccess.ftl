<#import "../share/pc/iFrameLayout.ftl" as page> <@page.pc>

<div class="ext ext_iframe">
	<div class="noframe ext_state1">
		<div class="ext_hint ext_hint_success">
			<div class="img"></div>
			<div class="content">
				<h2>恭喜您，添加成功</h2>
				<p>
					成功绑定银行卡后，您可以去
					<a href="${config.getDomainProfile()}/account/account.do">充值</a>、继续
					<a href="${config.getDomainProfile()}/orders.do">支付</a>订单、或去首页逛逛<br>
					未成功绑定银行卡？
					<a href="bindForm.do">重新绑定银行卡</a>
				</p>
			</div>
		</div>
	</div>
</div>

</@page.pc>
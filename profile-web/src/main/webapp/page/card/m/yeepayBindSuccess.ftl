<#import "../../share/m/layout.ftl" as page>
<#assign params={"hStyle":"header_p", "parentName":"银行卡"} in page>
<@page.pc>

	<section class="payment_addcard3 no_banner">
		<form class="form_order">
			<div class="warning_form">
				您已设置银行卡，可放心进行充值、提现、投资
			</div>
			<div class="infor_bank">
				<#if bankId != '' && bankName != ''>
					<img src="${config.getDomainMstatic()}/skin/images/banks/${bankId}.png">
				<#else>
					<img src="${config.getDomainMstatic()}/skin/images/banks/card.png">
				</#if>
				<p>
					${bankName}&nbsp;&nbsp;&nbsp;借记卡<br>
					尾号:  ${cardEndId}
				</p>
			</div>
			<ul>
				<li class="sp1">
					为了您的资金安全，不建议更换绑定银行卡，如遇特殊情况，请联系服务邮箱 <a href="mailto:pub_zc@pingan.com.cn">pub_zc@pingan.com.cn</a>
				</li>
				<li class="normal">
					<#if source?exists>
						<a class="submit" href="${source!''}">确定</a>
					<#else>
						<a class="submit" href="${config.getDomainM()}">马上投资</a>
					</#if>
				</li>
			</ul>
		</form>    
	</section>
</div>

</@page.pc>
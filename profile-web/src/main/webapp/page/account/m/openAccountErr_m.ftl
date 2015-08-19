<#import "../../share/m/layout.ftl" as page>
<#assign params={"hStyle":"header_p","backUrl":"${config.getDomainProfile()}/m/identity/identityCenter.do","parentName":"实名认证"} in page>
<@page.pc>
			<section class="register_detail register_failed">
				<div class="result">
				<#if cardForm?exists>
					<h1>您的账号实名认证失败！${cardForm.errorString!''}</h1>
				<#else>
					<h1>您的账号实名认证失败！</h1>
				</#if>
					<h2><a href="${config.getDomainProfile()}/m/openAccount/openAccountInit.do" class="submit">重新申请实名认证</a></h2>
				</div>
			</section>
		</div>
		<div class="logo"></div>
</@page.pc>
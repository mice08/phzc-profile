<#import "../../share/m/layout.ftl" as page>
<#assign params={"hStyle":"header_p","backUrl":"${config.getDomainProfile()}/m/identity/identityCenter.do","parentName":"特定合格投资人认证"} in page>
<@page.pc>
			<section class="register_detail register_failed">
				<div class="result">
					<h1>您的账号特定合格投资人认证失败！${identityForm.errorString!''}</h1>
					<h2><a href="${config.getDomainProfile()}/m/identity/queryIdentity.do" class="submit">重新申请特定合格投资人认证</a></h2>
				</div>
			</section>
		</div>
		<div class="logo"></div>
</@page.pc>
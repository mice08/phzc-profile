<#import "../../share/m/layout.ftl" as page>
<#assign params={"hStyle":"header_p","backUrl":"${config.getDomainProfile()}/m/identity/identityCenter.do","parentName":"实名认证"} in page>
<@page.pc>
			<section class="register_detail register_success">
				<div class="result">
					<h1>您的账号实名认证成功！</h1>
					<#if source?exists>
						<a href="${source}" class="submit"> 确定 </a>
					<#else>
						<a href="${config.getDomainM()}" class="submit"> 去首页看看 </a>
					</#if>
				</div>
			</section>
		</div>
		<div class="logo"></div>
</@page.pc>
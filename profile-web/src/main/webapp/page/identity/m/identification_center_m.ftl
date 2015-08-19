<#import "../../share/m/layout.ftl" as page> 
<#assign params={"hStyle":"header_p","parentName":"我的认证"} in page>
<@page.pc>
<div class="pagewrapper">
	
	<section class="user_identification no_banner">
		<form class="form_order" >
			<ul class="clearfix">
				<#if realFlag==true>
				<li><label> 实名认证 </label> <a href="${config.getDomainProfile()}/m/openAccount/openAccountInit.do" class="normal_link"></a></li>
				<li><label> 特定合格投资人认证 </label> <a href="${config.getDomainProfile()}/m/identity/queryIdentity.do" class="normal_link"> </a></li>
				<#else>
					<li><label> 实名认证 </label> <a href="${config.getDomainProfile()}/m/openAccount/openAccountInit.do" class="normal_link error">未认证 </a></li>
					<li><label class="denied"> 特定合格投资人认证 </label> <a href="${config.getDomainProfile()}/m/identity/queryIdentity.do" class="normal_link"> </a></li>
				</#if>
			</ul>
		</form>
	</section>
</div>
<div class="logo"></div>
</@page.pc>

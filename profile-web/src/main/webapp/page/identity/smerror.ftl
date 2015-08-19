<#import "../share/pc/layout.ftl" as page>
<#assign actives="active3" in page>
<@page.pc>
<#setting number_format="0"> 
<div class="user_page">
	<div class="main clearfix">
		<#include "../leftNav.ftl"> <@leftNav activeMenu="item2"> </@leftNav>
		<div class="right">
			<div class="user_page_inner  user_page2-2">
			<ul class="user_top cha_pass">
				<li class="first">
					<a href="${config.getDomainProfile()}/openAccount/openAccount.do" >	实名认证</a>
				</li>
				<li>
					<a href="#" class="active">合格投资人认证	</a>
				</li>
			</ul>
				<div class="user_card">
				<#if identityForm?exists>
					<p class="user_note user_c_p">
						非常遗憾，您的身份认证未通过，${identityForm.errorString?default("请先检查实名认证信息")}</p>
					现在您可以重新申请合格投资人认证<a class="submit submit1" href="identityInit.do">
						申请合格投资人认证</a>
				<#else>
					<p class="user_note user_c_p">
						非常遗憾，您的身份认证未通过，请联系客服邮箱。</p>
					现在您可以重新申请合格投资人认证<a class="submit submit1" href="identityInit.do">
						申请合格投资人认证</a>
				</#if>
				</div>
			</div>
		</div>
	</div>
</div>
</@page.pc>
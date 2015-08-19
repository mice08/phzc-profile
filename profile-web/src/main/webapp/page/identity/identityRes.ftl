<#import "../share/pc/layout.ftl" as page> 
<#assign actives="active3" in page>
<@page.pc>
<div class="user_page">
	<div class="main clearfix">
		<#include "../leftNav.ftl"> <@leftNav activeMenu="item2"> </@leftNav>
		<div class="right">
			<div class="user_page_inner  user_page2-6">
				<ul class="user_top cha_pass">
					<li class="first"><a
						href="${config.getDomainProfile()}/openAccount/openAccount.do">
							实名认证 </a></li>
					<li><a href="#" class="active"> 合格投资人认证 </a></li>

				</ul>
				<div class="submit_success">
					<h2>恭喜您，申请认证成功</h2>
					<p class="type2">您的申请已受理，请到用户中心－我的认证查询审核状态<br>
					您的手机号码：${identityInfo.usrMp}<br>
					审核编号：${identityInfo.idSid}</p>
					<p class="type1">
						现在您可以：查看<a
							href="${config.getDomainProfile()}/identity/queryIdentity.do">认证列表</a>，<a
							href="${config.getDomainWww()}/">回到首页</a>
					</p>
					<div class="img"></div>
				</div>
			</div>
		</div>
	</div>
</div>

</@page.pc>

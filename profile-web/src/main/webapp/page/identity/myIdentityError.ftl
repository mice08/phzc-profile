<#import "../share/pc/layout.ftl" as page>
<#assign actives="active3" in page>
<@page.pc>
<#setting number_format="0">
<div class="user_page">
	<div class="main clearfix">
		<#include "../leftNav.ftl"> 
		<@leftNav activeMenu="item2"> </@leftNav>
		
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
					<p class="user_note user_c_p">
						为了方便您更好的参与众筹项目，<br>请申请项目所需的合格投资人认证。
					</p>
					<a class="submit submit1" href="identityInit.do"> 申请合格投资人认证 </a>
					<div class="img"></div>
				</div>
			</div>
		</div>
	</div>
</div>
</@page.pc>
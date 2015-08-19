 <#import "../share/pc/layout.ftl" as page> 
<#assign actives="active3" in page>
<@page.pc>
        <div class="user_page">
			<div class="main clearfix">
				<#include "../leftNav.ftl">
		        <@leftNav activeMenu="item2">
		        </@leftNav>
				<div class="right">
					<div class="user_page_inner  user_page2-4">
		                <ul class="user_top cha_pass">
							<li class="first">
								<a href="#" class="active">	实名认证</a>
							</li>
							<li>
								<a href="${config.getDomainProfile()}/identity/queryIdentity.do" >合格投资人认证	</a>
							</li>
						</ul>
						<h4>  您的实名认证	</h4>
						<form>
						    <div class="input_row">
						        <label>姓名</label>
						        <span>${cardForm.realName !''}</span>
						    </div>
						    <div class="input_row">
						        <label>	 证件类型</label>
						        <span> 身份证	</span>
						    </div>
						    <div class="input_row">
						        <label>	证件号码	</label>
						        <span>${cardForm.certId !''}</span>
						    </div>
						</form>
					</div>
				</div>
			</div>
		</div>
</@page.pc>
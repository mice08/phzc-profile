<#import "../../share/m/layout.ftl" as page>
<#assign params={"hStyle":"header_p","backUrl":"${config.getDomainProfile()}/m/identity/identityCenter.do","parentName":"实名认证"} in page>
<@page.pc>
		<section class="temp_page user_real no_banner">
			<form >
				<ul class="clearfix">
					<li><label> 真实姓名 </label><span>${cardForm.realName } </span></li>
					<li><label> 身份证号 </label> <span>${cardForm.certId } </span></li>
				</ul>
			</form>
		</section>
	</div>
	
	<div class="logo"></div>
</@page.pc>
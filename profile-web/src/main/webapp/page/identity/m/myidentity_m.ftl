<#import "../../share/m/layout.ftl" as page>
<#assign params={"hStyle":"header_p","backUrl":"${config.getDomainProfile()}/m/identity/identityCenter.do","parentName":"我的认证"} in page>
<@page.pc>
<section class="user_identification no_banner">
			<form >
				<ul>
					<li><label> 姓名 </label> <span> ${userIdentity.usrName} </span></li>
					<li><label> 身份证号 </label> <span> ${userIdentity.certId} </span></li>
					<#if userIdentity.identityStat?exists>
						<#if userIdentity.identityStat=="1" ||userIdentity.identityStat=="0">
							<li class="normal">
								<div class="li_infor">特定合格投资人，以下条件选择之一即可（审核中）</div>
							</li>
						<#elseif userIdentity.identityStat=="2">
							<li class="normal">
							<div class="li_infor">特定合格投资人，以下条件选择之一即可</div>
							</li>
						</#if>		
					</#if>
					
					<#if userIdentity.assetSal?exists>
						<#if "1"==userIdentity.assetSal >
							<li class="checkbox checkbox_checked" ><label> 金融资产不低于300万人民币 </label> <b></b></li>
						<#else>
							<li class="checkbox" ><label> 金融资产不低于300万人民币 </label> <b></b></li>
						</#if>
					<#else>
						<li class="checkbox" ><label> 金融资产不低于300万人民币 </label> <b></b></li>
					</#if>
					<#if userIdentity.assetTot?exists>
						<#if "2"==userIdentity.assetTot >
							<li class="checkbox checkbox_checked" ><label> 最近三年个人年均收入不低于50万 </label> <b></b></li>
						<#else>
							<li class="checkbox " ><label> 最近三年个人年均收入不低于50万 </label> <b></b></li>
						</#if>
					<#else>
						<li class="checkbox" ><label> 最近三年个人年均收入不低于50万 </label> <b></b></li>
					</#if>
					
					<li><a href="${config.getDomainProfile()}/m/identity/identityCenter.do" class="submit"> 返回 </a></li>
				</ul>
			</form>
		</section>
	</div>
	<div class="logo"></div>

 </@page.pc>
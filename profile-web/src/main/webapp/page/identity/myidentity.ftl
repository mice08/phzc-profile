 <#import "../share/pc/layout.ftl" as page> 
<#assign actives="active3" in page>
 <@page.pc> 
 <#setting number_format="0">
<div class="user_page">
	<div class="main clearfix">
		<#include "../leftNav.ftl"> <@leftNav activeMenu="item2"> </@leftNav>

		<div class="right">
			<div class="user_page_inner  user_page2-1">
				<ul class="user_top cha_pass">
					<li class="first"><a
						href="${config.getDomainProfile()}/openAccount/openAccount.do">
							实名认证</a></li>
					<li><a href="#" class="active">合格投资人认证 </a></li>
				</ul>
				<div class="user_form" id="identityForm">
					<p class="intro">
						普惠众筹为您提供银行级信息安全保障，认证需要您提供以下信息（下列标记<span>*</span>为必填项）
					</p>
					<form >
						<div class="ajax_part">
							<div class="input_row">
								<label> <i>*</i>姓名</label>
								<div class="input_part">
									<span>${userIdentity.usrName}</span>
								</div>
							</div>
							<div class="input_row">
								<label> <i>*</i>证件类型
								</label>
								<div class="select_part zindex1">
									<a class="select_part_main" id="credType" name="credType" href="#" data-value="99"> 身份证</a>

								</div>
							</div>
							<div class="input_row">
								<label> <i>*</i>证件号码</label>
								<span>${userIdentity.certId}</span>
								</div>
							</div>
							<div class="input_row">
								<label> 手机号码 </label> <span>${userIdentity.usrMp}</span> 
							</div>
							<div class="input_row">
								<label> 所在企业 </label>
								<div class="input_part">
									<input type="text" name="orgName" id="orgName" value="${userIdentity.orgName!''}"> 
								</div>
							</div>
							<div class="input_row input_row_sp">
								<label> <i>*</i>特定合格投资人</label><span>（以下条件至少勾选一项)</span>
							</div>
							<div class="input_row input_row_sp">
								<#if userIdentity.assetSal=="1">
									<label> </label> <input type="checkbox" id="assetSal" name="assetSal" value="${userIdentity.assetSal}"   checked > <span>个人金融资产不低于300万人民币 </span>
								<#else>
									<label> </label> <input type="checkbox" id="assetSal" name="assetSal" value="${userIdentity.assetSal}" > <span>个人金融资产不低于300万人民币 </span>
								</#if>
							</div>
							<div class="input_row input_row_sp">
								<#if userIdentity.assetTot=="2">
									<label> </label> <input type="checkbox"  id="assetTot" name="assetTot" value="${userIdentity.assetTot}"  checked> <span> 最近三年个人年均收入不低于50万人民币 </span>
								<#else>
									<label> </label> <input type="checkbox"  id="assetTot" name="assetTot" value="${userIdentity.assetTot}" > <span> 最近三年个人年均收入不低于50万人民币 </span>
								</#if>
							</div>
							
							<div class="input_row">
								<label> </label> 
								<span>
								以上信息仅供本平台进行后续合格投资者认证使用。
								</span>
							</div>
							<div class="input_row">
								<label> </label> <a class="submit" href="${config.getDomainProfile()}/identity/queryIdentity.do" > 返回列表 </a>
							</div>
					</form>
				</div>
				
			</div>
		</div>
	</div>
</div>
<div class="bgcover"></div>
</@page.pc>

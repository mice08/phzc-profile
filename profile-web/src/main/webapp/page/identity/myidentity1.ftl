<#import "../share/pc/layout.ftl" as page>
<#assign actives="active3" in page>
<@page.pc>
<#setting number_format="0"> 
<div class="user_page">
	<div class="main clearfix">
		<#include "../leftNav.ftl">
        <@leftNav activeMenu="item2">
        </@leftNav>
			<div class="right">
				<div class="user_page_inner  user_page2-3">
				<ul class="user_top cha_pass">
							<li class="first">
								<a href="${config.getDomainProfile()}/openAccount/openAccount.do" >	实名认证</a>
							</li>
							<li>
								<a href="#" class="active">合格投资人认证	</a>
							</li>
				</ul>
				<div class="u_approve_t" id="identityShow" style="display:none;">
						<p class="approve_p">为了方便您更好的参与众筹项目，请申请项目所需的合格投资人认证。</p>
						<a class="submit submit_top" href="${config.getDomainProfile()}/identity/identityInit.do"> 申请合格投资人认证
						</a>
				</div>
				<form action="identityInit.do" name="identityForm" id="identityForm"method="get">
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						class="approve_tab">
						<thead>
							<tr>
								<th>申请编号
								</td>
								<th class="">认证类型
								</td>
								<th class="">投资人类型
								</td>
								<th class="">申请时间
								</td>
								<th class="">审核时间
								</td>
								<th class="">审核状态
								</td>
								<th class="">姓名/企业
								</td>
								<th class="">备注
								</td>
								<th class="operation_w">操作
								</td>
							</tr>
						</thead>
						<tbody>
						<#if userIdentityList?exists >
							<#list userIdentityList as userIdentity>
								<#if userIdentity_index%2==0>
									<tr>
								<#else>
									<tr class="backgr">
								</#if>
								<td class="">
								<#if userIdentity.idSid?length gt 0 >
									${userIdentity.idSid}
								</#if>
								</td>
								<td>
										个人
								</td>
								<td>
									<#if userIdentity.idType =="2" > 
										特定合格投资人 
									<#else>
										普通认证 
									</#if> <br>
								</td>
								<td>${userIdentity.identityTime?string('yyyy-MM-dd HH:mm:ss')}</td>
								<td>${userIdentity.auditTime?default("")}</td>
								<td><a href="#" class="n_pass">
									<#if userIdentity.identityStat=="0"||userIdentity.identityStat=="1">
										待审核
									<#elseif userIdentity.identityStat=="2">
										已通过
									<#elseif userIdentity.identityStat=="3">
										未通过
									<#elseif userIdentity.identityStat=="4">
										审核中
									<#elseif userIdentity.identityStat=="9">
										已撤销
									</#if>
								</a></td>
								<td>${userIdentity.usrName?default("")}</td>
								<td>${userIdentity.remark?default("")}</td>
								<input type="hidden" name="idType" value="${userIdentity.idType}">
								<#if userIdentity.identityStat=="1"||userIdentity.identityStat=="0">
									<td><a class="operate" href="myIdentity.do?idSid=${userIdentity.idSid}">查看 </a></td> 
								<#elseif userIdentity.identityStat=="3" && userIdentity.idType =="2">
									<td><a class="operate" href="identityInit.do?idSid=${userIdentity.idSid}">编辑 </a></td> 
								<#else>
									<td>&nbsp;</td> 
								</#if>
								</tr>
							</#list>
						</#if>
						
						</tbody>
					</table>
		</form>
	</div>
</div>
</div>
</div>
<script type="text/javascript">
		var counter=0; 
		$("input[name='idType']").each(  
			function(){  
				counter+=parseInt($(this).val());
			}  
		)
		if(counter<2){
			$("#identityShow").show();
		}

</script >
</@page.pc>

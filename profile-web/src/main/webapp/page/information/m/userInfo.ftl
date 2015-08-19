<#import "../../share/m/layout.ftl" as page>
<#assign params={"hStyle":"header_p","backUrl":"${config.getDomainProfile()}/m/orders.do","parentName":"个人中心"} in page>
<@page.pc>

	<section class="user_infor no_banner">
		<form class="form_order">
			<ul class="clearfix">
				<li class="sp2">
					<label>头像</label>
					<#if userInfo.headImgUrl?exists>
						<img src="${userInfo.headImgUrl}" class="head">
					<#else>
						<#if userInfo.usrSex == 'M'>
							<img src="${config.getDomainMstatic()}/skin/images/temp_head3.jpg" class="head">
						<#elseif userInfo.usrSex == 'F'>
							<img src="${config.getDomainMstatic()}/skin/images/temp_head2.jpg" class="head">
						<#elseif userInfo.usrSex == 'B'>
							<img src="${config.getDomainMstatic()}/skin/images/temp_head1.jpg" class="head">
						<#else>
							<img src="${config.getDomainMstatic()}/skin/images/head.jpg" class="head">
						</#if>
					</#if>
				</li>
				<li>
					<label>手机</label>
					<em>${userInfo.usrMp!''}</em>
				</li>
				<li class="sp3">
					<label>我的认证</label>
					<#if userInfo.idChkStat != 'S'>
						<a href="/m/identity/identityCenter.do" class="normal_link error">未认证</a>
					<#else>
						<#if userInfo.isInvStat != 'S'>
							<a href="/m/identity/identityCenter.do" class="normal_link error">未认证</a>
						<#else>
							<a href="/m/identity/identityCenter.do" class="normal_link"></a>
						</#if>
					</#if>
				</li>
				<li>
					<label>邮箱</label>
					<em>${userInfo.usrEmail!''}</em>
				</li>
				<li>
					<label>收货地址</label>
					<a href="/m/address/queryAddress.do" class="normal_link">
					</a>
				</li>
				<li class="sp3">
					<label>我的银行卡</label>
					<#if userInfo.hasCard == 1>
						<a href="/m/yeepay/myCards.do" class="normal_link"></a>
					<#elseif userInfo.hasCard == 0>
						<a href="/m/yeepay/bindStepOneForm.do" class="normal_link error">未绑卡</a>
					</#if>
				</li>
				<li>
					<label>安全设置</label>
					<a href="/m/modifyPwd/modifyPwdInit.do" class="normal_link"></a>
				</li>
				<li class="sp3">
					<a href="${config.getDomainPassport()}/m/logout.do" class="login_out">
						退出登录
					</a>
				</li>
			</ul>
		</form>
	</section>
</div>
<div class="logo"></div>


</@page.pc>
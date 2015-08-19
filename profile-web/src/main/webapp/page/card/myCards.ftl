<#import "../share/pc/layout.ftl" as page> 
<@page.pc>

<div class="user_page">
	<div class="main clearfix">
	
<#include "../leftNav.ftl">
	<@leftNav activeMenu="item3">
</@leftNav>

<input id="msg_hidden" type="hidden" value="${msg!''}" />
<div class="right">
	<div class="user_page_inner  user_page3">
		<h1>我的银行卡</h1>
		<#if cards?? && (cards?size > 0)>
			<ul class="card_list clearfix">
				<#list cards as card>
					<li>
						<div class="card_list_inner">
							<img src="${config.getDomainPcstatic()}/skin/images/banks/${card.bankId}.png">
							<h2>${card.cardId}</h2>
							<p>
								<span> 默认银行卡 </span> <!-- <a href="javascript:void(0);" onclick="changeCard('${card.cardSeqId}');"> 更换 </a> -->
							</p>
						</div>
					</li>
				</#list>
			</ul>
		<#else>
			<p class="user_page3-4_p">
				暂未绑定任何储蓄卡，现在您可以去<a href="bindForm.do">绑定银行卡</a>
			</p>
		</#if>
		
		<div class="user_note">
			<span> 说明： </span><br> 1、仅支持绑定储蓄卡<br> 2、支持的银行包括：<br>
			<img src="${config.getDomainPcstatic()}/skin/images/banks.jpg">
		</div>
	</div>
</div>
</div>

<script>

function alertMsg() {
	var msg = $("#msg_hidden").val();
	if (msg !== null && msg !== "" && msg !== undefined) {
		alert(decodeURI(msg));
	}
}

function changeCard(cardSeqId) {
	window.location.href = "/changeCard/changeCardForm.do?cardSeqId=" + cardSeqId;
}

window.onload = alertMsg;

</script>

</@page.pc>
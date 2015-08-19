<#import "../../share/m/layout.ftl" as page>
<@page.pc>

<div class="pagewrapper">
	<section class="user_funds no_banner">
	<#if accountLogList?? && (accountLogList?size > 0)>
		<ul class="fund_list">
		<#list accountLogList as accountLog>
			<li><a href="#">
					<h1>${accountLog.name}</h1>
					<p>${accountLog.acctTime}</p> <label> ${accountLog.acctType} </label> <span> ${accountLog.transAmt}<em>元</em>
				</span>
			</a></li>
		</#list>
		</ul>
	</#if>
	</section>
</div>

</@page.pc>
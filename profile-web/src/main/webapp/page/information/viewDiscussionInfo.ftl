<#import "../share/pc/layout.ftl" as page>
<@page.pc>
<div class="user_page">
    <div class="main clearfix">
        <#include "../leftNav.ftl">
        <@leftNav activeMenu="item7">
        </@leftNav>
        	 <div class="right">
            <div class="user_page_inner  user_page7-1">
                
<ul class="user_top cha_pass">
    <li class="first">
        <a href="#"  class="active">
            我的吐槽
        </a>
    </li>
    <li>
        <a href="/discussion/submitInit.do">
            我要吐槽
        </a>
    </li>
</ul>
<div class="page_tweet">
		 <#list disList as discussion>
			 <div class="tweet clearfix">
		        <div class="img_part">
		             <#if userInfo?? && userInfo.usrSex=="F">
                    	<img src="${config.getDomainPcstatic()}/skin/images/temp_head2.jpg">
                     <#else>
                   		<img src="${config.getDomainPcstatic()}/skin/images/temp_head3.jpg">
                    </#if>
		            <div class="cover">
		            </div>
		        </div>
		        <div class="tweet_r">
		            <div class="tweet_right clearfix">
		                <p class="tweet_time">${discussion.disCreatetime!}</p>
		                <p class="tweet_view">${discussion.disContent!}</p>
											<#if discussion.disDealstate == 1>
												<div class="tweet_reply clearfix">
								                    <span class='reply_sp'>普惠众筹回复：</span><p class='reply_p'>${discussion.disReply!}</p>
								                    <p class="reply_r">时间：${discussion.disUpdatetime!}</p>
								                    <i></i>
								                </div>
							                </#if>
		            </div>
		        </div>
		    </div>
    	</#list>

</div>            </div>
        </div>
    </div>
</div>
    </div>
 </div>    
</@page.pc>
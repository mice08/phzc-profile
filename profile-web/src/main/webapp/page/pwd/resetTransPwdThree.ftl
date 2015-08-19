<#import "../share/pc/layout.ftl" as page>
<@page.pc>

<div class="user_page">
    <div class="main clearfix">
        <div class="step_part">
            <ul class="step_top">
                <li class="item1 ">
        <span class="">
            <i>
                1
            </i>
            <b>
                验证短信
            </b>
        </span>
                </li>
                <li class="item2 ">
        <span class="">
            <i>
                2
            </i>
            <b>
                设置新交易密码
            </b>
        </span>
                </li>
                <li class="item3 active">
        <span class="active">
            <i>
                3
            </i>
            <b>
                完成
            </b>
        </span>
                </li>
            </ul>
            <div class="step_result">
                <div class="img"></div>
                <div class="content">
                    <h2>恭喜您，交易密码重置成功！</h2>

                    <p>
                    	现在您可以去<a href="${config.getDomainProfile()}/orders.do">个人信息</a>查看，去<a href="${config.getDomainWww()}/">首页</a>逛逛
                    </p>
                </div>
            </div>
        </div>
    </div>
</@page.pc>
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
                身份认证
            </b>
        </span>
                </li>
                <li class="item2 ">
        <span class="">
            <i>
                2
            </i>
            <b>
                设置新邮箱地址
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
                    <h2>恭喜您，邮箱验证成功！</h2>

                    <p>
                        现在您可以返回<a href="${config.getDomainProfile()}/information/showBaseInfo.do">我的资料</a>查看
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>
</@page.pc>
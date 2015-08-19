<#import "../share/pc/layout.ftl" as page>
<@page.pc>
<div class="user_page">
    <div class="main clearfix">
        <#include "../leftNav.ftl">
        <@leftNav activeMenu="item5">
        </@leftNav>
        <div class="right">
            <div class="user_page_inner  user_page5-1">
            <#--<ul class="user_top">
                <li class="first">
                    <a href="${config.getDomainProfile()}/information/showBaseInfo.do" class="active">
                        个人信息
                    </a>
                </li>
                <li>
                    <a href="${config.getDomainProfile()}/information/showUserImage.do">
                        头像照片
                    </a>
                </li>
                <li>
                    <a href="user_page5-3.html">
                        捐赠证书
                    </a>
                </li>
            </ul>-->
                <form class="user_page5-1_form">
                    <#--<div class="input_row">
                        <label>
                            昵称：
                        </label>
                        <span>
                        ${info.nickName!}
                        </span>
                    </div>-->
                    <div class="input_row">
                        <label>
                            姓名：
                        </label>
                            <span>
                            ${info.usrName!}
                            </span>
                    </div>
                    <div class="input_row">
                        <label>
                            证件号码：
                        </label>
                            <span>
                            ${info.certId!}
                            </span>
                    </div>
                    <div class="input_row">
                        <label>
                            手机号码：
                        </label>
                            <span>
                            ${info.usrMp!}
                            </span>
                        <a href="${config.getDomainProfile()}/information/changePhoneOne.do" class="normal">
                            修改
                        </a>
                        <#if info.usrMp??>
                            <em class="correct">
                                已验证
                            </em>
                        <#else>
                            <em class="error">
                                未验证
                            </em>
                        </#if>
                    </div>
                    <div class="input_row">
                        <label>
                            邮箱地址：
                        </label>
                            <span>
                            ${info.usrEmail!}
                            </span>
                        <a href="${config.getDomainProfile()}/information/changeEmailOne.do" class="normal">
                            修改
                        </a>
                        <#if info.usrEmail??>
                            <em class="correct">
                                已验证
                            </em>
                        <#else>
                            <em class="error">
                                未验证
                            </em>
                        </#if>
                    </div>
                    <div class="input_row">
                        <label>
                            性别：
                        </label>
                            <span>
                            ${info.usrSex!}
                            </span>
                    </div>
                    <div class="input_row">
                        <label>
                            生日：
                        </label>
                            <span>
                            ${info.birthday!}
                            </span>
                    </div>
                    <div class="input_row">
                        <label>
                            星座：
                        </label>
                        <span>
                        ${info.constellation!}
                        </span>
                    </div>
                </form>
            </div>
        </div>
    </div>
</@page.pc>
<#macro leftNav activeMenu>
<div class="left">
    <div class="user_nav">
        <ul>
            <li class="item1 <#if activeMenu = 'item1'> active </#if>">
                <a href="${config.getDomainProfile()}/orders.do" class=" <#if activeMenu = 'item1'> active </#if>">
                    我的众筹
                </a>
                <span></span>
            </li>
            <li class="item2 <#if activeMenu = 'item2'> active </#if>">
                <a href="${config.getDomainProfile()}/openAccount/openAccount.do" class=" <#if activeMenu = 'item2'> active </#if>">
                    我的认证
                </a>
                <span></span>
            </li>
            <li class="item3 <#if activeMenu = 'item3'> active </#if>">
                <a href="${config.getDomainProfile()}/card/myCards.do" class=" <#if activeMenu = 'item3'> active </#if>">
                    我的银行卡
                </a>
                <span></span>
            </li>
            <li class="item4 <#if activeMenu = 'item4'> active </#if>">
                <a href="${config.getDomainProfile()}/account/account.do" class=" <#if activeMenu = 'item4'> active </#if>">
                    充值提现
                </a>
                <span></span>
            </li>
            <li class="item5  <#if activeMenu = 'item5'> active </#if>">
                <a href="${config.getDomainProfile()}/information/showBaseInfo.do" class=" <#if activeMenu = 'item5'> active </#if>">
                    我的资料
                </a>
                <span></span>
            </li>
            <li class="item6 <#if activeMenu = 'item6'> active </#if>">
                <a href="${config.getDomainProfile()}/address/queryAddress.do" class=" <#if activeMenu = 'item6'> active </#if>">
                    收货地址
                </a>
                <span></span>
            </li>
            <li class="item7 <#if activeMenu = 'item7'> active </#if>">
                <a href="${config.getDomainProfile()}/discussion/query.do" class=" <#if activeMenu = 'item7'> active </#if>">
                    我的吐槽
                </a>
                <span></span>
            </li>
            <li class="item8 <#if activeMenu = 'item8'> active </#if>">
                <a href="${config.getDomainProfile()}/modifyPwd/modifyPwd.do" class=" <#if activeMenu = 'item8'> active </#if>">
                    修改密码
                </a>
                <span></span>
            </li>
        </ul>
    </div>
</div>
    <#nested>
</#macro>
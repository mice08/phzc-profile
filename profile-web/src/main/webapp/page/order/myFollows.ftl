<#import "../share/pc/layout.ftl" as page> <@page.pc>
<div class="user_page">
    <div class="main clearfix">
        <#include "../leftNav.ftl"> <@leftNav activeMenu="item1"> </@leftNav>

        <div class="right">
            <div class="user_page_inner  user_page1-3">
                <ul class="user_top cha_pass">
                    <li class="first"><a href="/orders.do"> 我支持的 </a></li>
                    <li><a href="/order/follows.do" class="active"> 我关注的 </a>
                        <#if pageSet?? && pageSet.rowCount gt 0>
                            <i>${pageSet.rowCount}</i>
                        </#if>
                    </li>
                </ul>

                <#if userAttentionList?? && (userAttentionList?size > 0)>
                    <table class="right_item">
                        <thead>
                        <tr>
                            <td class="right_item_thd page1_thd">项目信息</td>
                            <td class="right_item_thead">操作</td>
                        </tr>
                        </thead>

                        <tbody>
                            <#list 0..(userAttentionList?size)-1 as i>
                                <#if i%2==1>
                                <tr class="right_tbody">
                                <#else>
                                <tr>
                                </#if>
                                <td class="right_tab_tbody page1_1">
                                    <a><img src="${config.changeToSSL(userAttentionList[i].imageUrl)!}"></a>

                                    <div class="right_tab_td page1_p">
                                        <div class="kick">
                                            <a href="${config.domainItem}/item/detail.do?productId=${userAttentionList[i].productId}" target="_blank">${userAttentionList[i].productName!}</a>
                                                <#if userAttentionList[i].productStatus == 9 ><!-- 众筹中 -->
                                                <span class="catalog_state1">众筹中</span>
                                                <#elseif userAttentionList[i].productStatus == 7 ><!-- 预热中 -->
                                                <span class="catalog_state2">预热中</span>
                                                <#elseif userAttentionList[i].productStatus == 11 ><!-- 众筹成功 -->
                                                <span class="catalog_state3">众筹成功</span>
                                                <#elseif userAttentionList[i].productStatus == 10 ><!-- 众筹失败 -->
                                                <span class="catalog_state4">众筹失败</span>
                                                <#elseif userAttentionList[i].productStatus == 8 ><!-- 预约中 -->
                                                <span class="catalog_state5">预约中</span>
                                                <#elseif userAttentionList[i].productStatus == 12 ><!-- 项目成功 -->
                                                <span class="catalog_state6">项目成功</span>
                                                <#elseif userAttentionList[i].productStatus == 13 ><!-- 项目失败 -->
                                                <span class="catalog_state7">项目失败</span>
                                                <#elseif userAttentionList[i].productStatus == 15 ><!-- 成功结束 -->
                                                <span class="catalog_state8">成功结束</span>
                                                <#elseif userAttentionList[i].productStatus == 16 ><!-- 失败结束 -->
                                                <span class="catalog_state9">失败结束</span>
                                            </#if>
                                        </div>
                                        <div class="page1_tab">
                                            <table>
                                                <tr class='page1_tr1'>
                                                    <td class='page1_td_def'>已完成</td>
                                                    <td>已筹集</td>
                                                    <td>剩余时间</td>
                                                </tr>
                                                <tr class='page1_tr2'>
                                                    <td class='page1_td_def'>${userAttentionList[i].productRate!0}</td>
                                                    <td>${userAttentionList[i].productShare!0}元</td>
                                                    <td>${userAttentionList[i].remainDays!0}天</td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                </td>
                                <td class='right_money'><a href="" class='page1_a'
                                                           productId="${userAttentionList[i].productId}"
                                                           productName="${userAttentionList[i].productName!}">取消关注</a>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                <#else>
                    <div class="none"></div>
                </#if>

                <div class="pager">
                    <div class="pager_inner">
                        <#if pageSet.curPage == 1>
                            <a class="first disable"> |< </a>
                            <a class="prev disable"> < </a>
                            <a class="active"> ${pageSet.curPage } </a>
                            <#if pageSet.pageSize == 1 || pageSet.pageSize == 0>
                                <a class="next disable"> > </a>
                                <a class="last disable"> >| </a>
                            <#else>
                                <a href="/order/follows.do?curPage=${pageSet.curPage+1 }" onclick="javascript:location.href='/order/follows.do?curPage=${pageSet.curPage+1 }'"> ${pageSet.curPage+1 } </a>
                                <a href="/order/follows.do?curPage=${pageSet.curPage+1 }" onclick="javascript:location.href='/order/follows.do?curPage=${pageSet.curPage+1 }'" class="next"> > </a>
                                <a href="/order/follows.do?curPage=${pageSet.pageSize }" onclick="javascript:location.href='/order/follows.do?curPage=${pageSet.pageSize }'" class="last"> >| </a>
                            </#if>
                        <#elseif pageSet.curPage == pageSet.pageSize>
                            <a href="/order/follows.do?curPage=1" class="first" onclick="javascript:location.href='/order/follows.do?curPage=1'"> |< </a>
                            <a href="/order/follows.do?curPage=${pageSet.curPage-1 }" class="prev" onclick="javascript:location.href='/order/follows.do?curPage=${pageSet.curPage-1 }'"> < </a>
                            <a href="/order/follows.do?curPage=${pageSet.curPage-1 }" onclick="javascript:location.href='/order/follows.do?curPage=${pageSet.curPage-1 }'"> ${pageSet.curPage-1 } </a>
                            <a href="/order/follows.do?curPage=${pageSet.curPage }"
                               class="active" onclick="javascript:location.href='/order/follows.do?curPage=${pageSet.curPage }'"> ${pageSet.curPage } </a>
                            <a class="next disable"> > </a>
                            <a class="last disable"> >| </a>
                        <#else>
                            <a href="/order/follows.do?curPage=1" class="first" onclick="javascript:location.href='/order/follows.do?curPage=1'"> |< </a>
                            <a href="/order/follows.do?curPage=${pageSet.curPage-1 }" class="prev" onclick="javascript:location.href='/order/follows.do?curPage=${pageSet.curPage-1 }'"> < </a>
                            <a href="/order/follows.do?curPage=${pageSet.curPage-1 }" onclick="javascript:location.href='/order/follows.do?curPage=${pageSet.curPage-1 }'"> ${pageSet.curPage-1 } </a>
                            <a href="/order/follows.do?curPage=${pageSet.curPage }"
                               class="active" onclick="javascript:location.href='/order/follows.do?curPage=${pageSet.curPage }'"> ${pageSet.curPage } </a>
                            <a href="/order/follows.do?curPage=${pageSet.curPage+1 }" onclick="javascript:location.href='/order/follows.do?curPage=${pageSet.curPage+1 }'"> ${pageSet.curPage+1 } </a>
                            <a href="/order/follows.do?curPage=${pageSet.curPage+1 }" class="next" onclick="javascript:location.href='/order/follows.do?curPage=${pageSet.curPage+1 }'"> > </a>
                            <a href="/order/follows.do?curPage=${pageSet.pageSize }" class="last" onclick="javascript:location.href='/order/follows.do?curPage=${pageSet.pageSize }'"> >| </a>
                        </#if>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
<div class="bgcover"></div>
<div class="ext ext_cancel">
    <h1>
        取消关注
    </h1>

    <p>
        确定取消关注<span id="s_productName"></span>吗？
    </p>
    <input type="hidden" id="productId" name="productId">
    <input type="hidden" id="curPage" name="curPage" value="${pageSet.curPage}">
    <form>
        <div class="link_row">
            <a href="" class="cancel">
                取消
            </a>
            <a href="javascript:submitForm();" class="cofirm">
                确定
            </a>
        </div>
    </form>
    <a href="#" class="close">
        关闭
    </a>
    <script>
        $("a.cancel").click(function (e) {
            e.preventDefault();
            closeExt(".ext_cancel")
        })

        $("a.page1_a").click(function (e) {
            e.preventDefault();
            openExt(".ext_cancel")
            var productId = $(this).attr("productId");
            $("#productId").val(productId);
            var productName = $(this).attr("productName");
            $("#s_productName").text("[" + productName + "]");
        })

        /*提交*/
        function submitForm() {
            var pageUrl = "/order/cancelFollow.do";
            var data = {
                "productId": $("#productId").val()
            };
            $.ajax({
                type: 'POST',
                url: pageUrl,
                data: data,
                success: function (data) {
                    closeExt(".ext_cancel");
                  //  window.location.reload();
                    window.location.href="/order/follows.do?"+$("#curPage").val();
                },
                error: function (error) {
                }
            });

        }
    </script>
</div>

</@page.pc>
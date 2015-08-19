<div class="pager">
    <div class="pager_inner">
    <#if page.curPage == 1>
        <a href="#" class="first"> |< </a>
        <a href="#" class="prev"> < </a>
        <a href="#" class="active"> ${page.curPage } </a>
        <#if page.pageSize == 1 || page.pageSize == 0>
            <a href="#" class="next">
                > </a> <a href="#" class="last"> >| </a>
        <#else>
            <a href="#" onclick="goPage(${page.curPage+1 })"> ${page.curPage+1 } </a>
            <a href="#" onclick="goPage(${page.curPage+1 })" class="next">
                > </a> <a href="#" onclick="goPage(${page.pageSize })" class="last"> >| </a>
        </#if>
    <#elseif page.curPage == page.pageSize>
        <a href="#" onclick="goPage(1)" class="first"> |< </a>
        <a href="#" onclick="goPage(${page.curPage-1 })" class="prev"> < </a>
        <a href="#" onclick="goPage(${page.curPage-1 })" > ${page.curPage-1 } </a>
        <a href="#" onclick="goPage(${page.curPage })" class="active"> ${page.curPage } </a>
        <a href="#" class="next">
            > </a> <a href="#" class="last"> >| </a>
    <#else>
        <a href="#" onclick="goPage(1)" class="first"> |< </a>
        <a href="#" onclick="goPage(${page.curPage-1 })" class="prev"> < </a>
        <a href="#" onclick="goPage(${page.curPage-1 })" > ${page.curPage-1 } </a>
        <a href="#" onclick="goPage(${page.curPage })" class="active"> ${page.curPage } </a>
        <a href="#" onclick="goPage(${page.curPage+1 })" > ${page.curPage+1 } </a>
        <a href="#" onclick="goPage(${page.curPage+1 })" class="next">
            > </a> <a href="#" onclick="goPage(${page.pageSize })" class="last"> >| </a>
    </#if>
    </div>
</div>
<script>
    function goPage(curPage){
        location.href="/orders.do?curPage="+curPage;
    }
</script>
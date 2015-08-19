<html>
<head>
<script>
   	document.domain="${config.getDomainHost()}".substring(1);
</script>
<script>
<#if msg?exists>
	function call(){
		window.parent.uploadFailed();
	}
	call();
<#else>
	function call(){
		window.parent.queryAttachList();
	}
	call();
</#if>
</script>
</head>
<body>

</body>
</html>
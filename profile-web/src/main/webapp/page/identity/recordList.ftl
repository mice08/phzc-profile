	<#setting number_format="0">
	<#if identityRecord?exists>
		<#list identityRecord as iRecord>
			<tr>
				<td name="uploadFileList" id="${iRecord.fileType?default("")}" >${iRecord.fileDesc?default("")}</td>
				<td><img src="${config.getDomainFile()}${iRecord.filePath?default("")}">
					<a href="${config.getDomainFile()}${iRecord.filePath?default("")}" target="_blank">
					${iRecord.fileName?default("")} </a> 
					<a href="javascript:void(0);" onclick="updateAttach(${iRecord.attachId});" class="del"> 删除 </a>
				</td>
			</tr>
		</#list>
	</#if>
									
										
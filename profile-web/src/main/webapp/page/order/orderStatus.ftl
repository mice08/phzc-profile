${productDto.productName}
		        
		<#if productDto.productStatus == 9 ><!-- 众筹中 -->
			 <span class="catalog_state1 catalog_state">
		            		众筹中
		     </span>
		<#elseif productDto.productStatus == 7 ><!-- 预热中 -->
			<span class="catalog_state2 catalog_state">
		            		预热中
		     </span>
		<#elseif productDto.productStatus == 11 ><!-- 众筹成功 -->
			<span class="catalog_state3 catalog_state">
		            		众筹成功
		     </span>
		<#elseif productDto.productStatus == 10 ><!-- 众筹失败 -->
			<span class="catalog_state4 catalog_state">
		            		众筹失败
		     </span>
		<#elseif productDto.productStatus == 8 ><!-- 预约中 -->
			<span class="catalog_state5 catalog_state">
		            		预约中
		     </span>
		<#elseif productDto.productStatus == 12 ><!-- 项目成功 -->
			<span class="catalog_state6 catalog_state">
		            		项目成功
		     </span>
		<#elseif productDto.productStatus == 13 ><!-- 项目失败 -->
			<span class="catalog_state7 catalog_state">
		            		项目失败
		     </span>
		<#elseif productDto.productStatus == 15 ><!-- 成功结束 -->
			<span class="catalog_state8 catalog_state">
		            		成功结束 
		     </span>
		<#elseif productDto.productStatus == 16 ><!-- 失败结束 -->
			<span class="catalog_state9 catalog_state">
		            		失败结束
		     </span>
		</#if> 
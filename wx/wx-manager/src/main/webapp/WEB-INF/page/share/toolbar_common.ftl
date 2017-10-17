<#-- 重置，刷新 -->
<#macro refset sessionId SystemSn name_space privalue jsmodelname formId gridId gridType>
	<#if checkPrivileges(sessionId,SystemSn,name_space,privalue) == true>
		<a href="javascript:void(0)"  iconCls="icon-refresh" plain="true" onclick="${jsmodelname!''}.refresh('${gridId!''}','${gridType!''}')">刷新</a>
		<a>-</a>
	</#if>
	<#if checkPrivileges(sessionId,SystemSn,name_space,privalue) == true>
		<a href="javascript:void(0)"  iconCls="icon-remove" plain="true" onclick="${jsmodelname!''}.reset('${formId!''}')">重置</a>
		<a>-</a>
	</#if>
</#macro>

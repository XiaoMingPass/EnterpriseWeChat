<#-- 
id:
title:
iconCls:
action:
permission: 1-新增 2-修改 3-删除 4-查询 5-打印 6-导出       0代表不验证权限的按钮
验证： checkPower("${thisPowerV?number}",4)==true
checkPrivilege(sessionId,"${privilegeSystemSn}","tools",1)
-->
<#macro toolbar  id='cur_toolbar' sessionId='' moduleSn=''  listData='[]'  >
<div id="${id}"></div>
<script type="text/javascript">
$(function(){
  var toolbarList=[];
  <#list listData as item>
  	  <#assign flag=1>
      <#if (item.permission)?? >
	      <#if checkPrivileges(sessionId,privilegeSystemSn,moduleSn,item.permission) == false>
	      	<#assign flag=0>
	      </#if>
      </#if> 	  
      var this_bar={};
      this_bar.text='${item.title}';
      <#if flag==1>
      		this_bar.id='${item.id}';
      		this_bar.iconCls='${item.iconCls}';
      		this_bar.handler=function(){${item.action}};
      <#else>
      		this_bar.iconCls='${item.iconCls}-dis';
            this_bar.disabled=true;
      </#if>
       toolbarList.push(this_bar);
       toolbarList.push('-');
  </#list>
  toolbarList.pop();
  $('#${id}').toolbar({items:toolbarList});
  });
</script>
</#macro>

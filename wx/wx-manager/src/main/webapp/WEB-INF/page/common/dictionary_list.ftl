<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>数据字典-列表</title> 
<#include "/share/include.ftl" />
<#import "/share/toolbar_common.ftl" as tolbcom/>
<#assign name_space = "fran_dictionary" />
<script type="text/javascript" language="javascript" src="${waremanager}/resources/js/common/common.js"></script>
<script type="text/javascript" language="javascript" src="${waremanager}/resources/js/common/dictionary_list.js"></script>
<script>
	var _jsSessionId='${sessionId!''}'; //sessionId
</script>
</head>
<body class="easyui-layout">
	<div data-options="region:'center',border:false">
		<div id="subLayout" class="easyui-layout" data-options="fit:true,border:false">
			<div data-options="region:'north',border:false">
				<!-- 抬头  开始-->
				<div id="toolbar" class="easyui-toolbar">
					<#--重置,刷新-->
					<@tolbcom.refset sessionId,wxSystemSn,name_space,1,'common','searchForm','dictionaryTable','datagrid'/>
					<#if checkPrivileges(sessionId,wxSystemSn,name_space,0) == true>
			        	<a href="javascript:void(0)"  iconCls="icon-add" plain="true" id="jsAdd">添加</a>
			        	<a>-</a>
			        </#if>
			        <#if checkPrivileges(sessionId,wxSystemSn,name_space,2) == true>
			        	<a href="javascript:void(0)"  iconCls="icon-edit" plain="true" id="jsEdit">修改</a>
			        	<a>-</a>
			        </#if>
			        <#if checkPrivileges(sessionId,wxSystemSn,name_space,3) == true>
			        	<a href="javascript:void(0)"  iconCls="icon-del" plain="true" id="jsDel">删除</a>
			        	<a>-</a>
			        </#if>
			    </div>
			    <!-- 抬头  结束-->	
			    <#if checkPrivileges(sessionId,wxSystemSn,name_space,1) == true>
				<!--搜索start -->
				<div class="search-div">
					<form name="searchForm" id="searchForm" action="" method="post" onsubmit="return false">
						<table class="search-tb">
							<col width="50" />
							<col />
							<col width="50" />
							<col />
							<tbody>
								<tr>
									<th>类型：</th>
									<td>
										<select id="dictionaryTypeBox" name="typeCode" data-options="editable:false,width:160,onSelect:dictionary.onSelect">
											<option value="" selected>全部</option>
			                            	<#--<#if dictionaryTypes??>
			                            		<#list dictionaryTypes as item>
			                            		<option value="${item.typeCode}">${(item.typeName)!''}</option>
			                            		</#list>
			                            	</#if>
			                            	-->
		                            	</select>
									</td>
									<th>名称：</th>
									<td><input id="jsSearchKeyword" class="ipt" name="name" style="width: 150px;" />
										<a iconCls="icon-search" class="easyui-linkbutton ml10" id="SearchBtn" data-options="iconCls:'icon-search'">查询</a>
									</td>
								</tr>
							</tbody>
						</table>
					</form>
				</div>
				<!--搜索end-->
				</#if>
			</div>
			<!--列表start-->
			<div data-options="region:'center',border:false">
				<table id="dictionaryTable" class="easyui-datagrid" title="" style="width:700px;height:250px"
		            data-options="singleSelect:false,collapsible:true,pagination:true,pageSize:20,pageList:[20,50],
		            url:'${franmanager}/fran/com/dictionary/ajaxList.do?sessionId=${sessionId!''}',method:'post'" >
					<thead>
						<tr>
							<th data-options="field:'id',checkbox:true"></th>
							<th data-options="field:'name',width:80, align:'left'">名称</th>
							<th data-options="field:'code',width:80, align:'center'">编号</th>
							<th data-options="field:'typeName',width:80, align:'left'">类型</th>
							<th data-options="field:'remark',width:200, align:'left'">备注</th>
							<th data-options="field:'sortNo',width:40,align:'center'">排序编号</th>
							<th data-options="field:'opt',width:80,align:'center',formatter : optsFormatter">操作</th>
						</tr>
					</thead>
				</table>
				
			</div>
			<!--列表end-->
		</div>
	</div>
	
	<script>
		function parsePage() {
			dictionary.init();
		};
		function optsFormatter (value, row, index) {
			if (row != null) {
				var id=row.id;
				var del = "";
				var update = "";
				var status = parseInt(row.status);
				
				<#if checkPrivileges(sessionId,wxSystemSn,name_space,3) == true>
				del = '<a href="javascript:void(0)" class="easyui-linkbutton" style="color: blue;" iconCls="icon-edit" plain="true" onclick="delByIds(\''
						+ id + '\')">删除</a>&nbsp;&nbsp;';
				</#if>
					
				<#if checkPrivileges(sessionId,wxSystemSn,name_space,2) == true>update ='<a href="javascript:void(0)" class="easyui-linkbutton" style="color: blue;" iconCls="icon-edit" plain="true" onclick="dictionary.editUI(\''
						+ id + '\')">修改</a>&nbsp;&nbsp;';</#if>
				if (status == 0) {
					return update + del;
				} else {
					return update + del;
				}
			}
		}
	</script>
	
</body>
</html>

<form id="menuForm" method="POST" >
    <div data-options="region:'center',border:false">
        <div class="pd10">
            <div class="easyui-panel" data-options="plain:true,fitWidth:true,cls:'mt5'" >
                <div class="fl">
                    <table class="form-tb">
                        <tr>
                            <th>编号：</th>
                            <td><input type="text" 
                            <#if (dictionary.code)?? && (dictionary.code)!="">value="${(dictionary.code)!''}"<#else> value="${typeCode}"</#if>
                            name="code" readonly="true" class="ipt easyui-validatebox" data-options="required:true,missingMessage:'编码不能为空！'" 
                            validType="remote['${franmanager}/fran/com/dictionary/validCode.do?oldCode=${(dictionary.code)!''}&sessionId=${sessionId!''}', 'newCode']"  /></td>
                        </tr>
                        <tr>
                            <th>数据类型：</th>
                            <td>
                            	<input type="hidden" name="id" value="${(dictionary.id)!''}" />
                            	<input type="hidden" id="jsTypeCode" value="<#if (dictionary.typeCode)??>${(dictionary.typeCode)!''}<#else>${typeId}</#if>" />
                            	<select id="typeCodes" name="typeCode" data-options="editable:false,maxHeight:250,panelHeight:250,prompt:'请选择...',width:160,required:true,missingMessage:'请选择数据类型！'"></select>
                            	<a class="easyui-linkbutton ml10" id="addDictionaryType" data-options="iconCls:'icon-add'">添加类型</a>
                            </td>
                        </tr>
                        <#--
                        <tr>
                            <th>名称：</th>
                            <td><input id="jsDictionaryName" type="text" value="${(dictionary.name)!''}"  maxlength="20" 
                            name="name" class="ipt easyui-validatebox" data-options="required:true,missingMessage:'名称不能为空！'" 
                            validType="remote['${franmanager}/fran/com/dictionary/validName.do?oldName=${(dictionary.name)!''}&sessionId=${sessionId!''}', 'newName']" /></td>
                        </tr>
                        -->
                        <tr>
                            <th>名称：</th>
                            <td><input style="width:200px;" id="jsDictionaryName" type="text" value="${(dictionary.name)!''}"  maxlength="32" 
                            name="name" data-options="required:true,missingMessage:'名称不能为空！'"  /></td>
                        </tr>
                        
                        <tr>
                            <th>排序：</th>
                            <td>
                            	<input type="text" name="sortNo" value="${(dictionary.sortNo)!'1000'}" class="ipt easyui-validatebox" style="width:50px;" data-options="required:true, validType:['integer', 'length[1,6]'],missingMessage:'排序编号不能为空！'"  />
                            </td>
                        </tr>
                        
                        <tr>
                            <th style="vertical-align:top">描述：</th>
                            <td rowspan="2">
                            	<textarea id="remark" name="remark" style="width:480px;height:150px;">${(dictionary.remark)!''}</textarea>
                            </td>
                        </tr>
                        <tr>
                            <th>&nbsp;</th>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
</form>

<script>
	$(document).ready(function(){
		var _typeCode = '';
		$('#typeCodes').combobox({
			url:'${franmanager}/fran/com/dictionary/ajaxListTypeData.do?sessionId=${sessionId!''}',
			valueField:'typeCode',
			textField:'typeName', 
			onLoadSuccess:function(){
				var jsTypeCode = $("#jsTypeCode").val();
				_typeCode = jsTypeCode;
				if(jsTypeCode!= ''){
					$('#typeCodes').combobox('setValue', jsTypeCode);
				}
			},
			onSelect:function(obj){
				_typeCode = obj.typeCode;
				$("#jsDictionaryName").validatebox({
				    validType: "remote['${franmanager}/fran/com/dictionary/validName.do?sessionId=${sessionId!''}&typeCode="+_typeCode+"&oldName=${(dictionary.name)!''}', 'newName']"   
				});
			}
		});
		
		$("#jsDictionaryName").validatebox({
		    validType: "remote['${franmanager}/fran/com/dictionary/validName.do?sessionId=${sessionId!''}&typeCode=<#if (dictionary.typeCode)??>${(dictionary.typeCode)!''}<#else>${typeId}</#if>&oldName=${(dictionary.name)!''}', 'newName']"   
		});
	});
	
</script>

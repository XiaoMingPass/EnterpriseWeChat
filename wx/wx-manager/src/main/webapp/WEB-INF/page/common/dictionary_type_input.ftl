<form id="dictionaryTypeForm" method="POST" >
    <div data-options="region:'center',border:false">
        <div class="pd10">
            <div class="easyui-panel" data-options="plain:true,fitWidth:true,cls:'mt5'" >
                <div class="fl">
                    <table class="form-tb">
                        <tr>
                            <th>编号：</th>
                            <td>
                            	<input type="hidden" name="id" value="${(dictionaryType.id)!''}" />
                            	<input type="text" maxlength="20" <#if (dictionaryType.typeCode)?? && (dictionaryType.typeCode)!="">value="${(dictionaryType.typeCode)!''}"<#else> value="${typeCode}"</#if> 
                            	name="typeCode" class="ipt easyui-validatebox" readonly data-options="editable:false,required:true,missingMessage:'编号不能为空！'" 
                            	validType="remote['${franmanager}/fran/com/dictionaryType/validCode.do?oldCode=${(dictionaryType.typeCode)!''}&sessionId=${sessionId!''}', 'newCode']" />
                            </td>
                        </tr>
                        <tr>
                            <th>名称：</th>
                            <td><input type="text" value="${(dictionaryType.typeName)!''}"  maxlength="20" 
                            name="typeName" class="ipt easyui-validatebox" data-options="required:true,missingMessage:'名称不能为空！'" 
                            validType="remote['${franmanager}/fran/com/dictionaryType/validName.do?oldName=${(dictionaryType.typeName)!''}&sessionId=${sessionId!''}', 'newName']" /></td>
                        </tr>
                        <tr>
                            <th style="vertical-align:top">描述：</th>
                            <td rowspan="2">
                            	<textarea id="remark" name="remark" style="width:400px;height:100px;">${(dictionaryType.remark)!''}</textarea>
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

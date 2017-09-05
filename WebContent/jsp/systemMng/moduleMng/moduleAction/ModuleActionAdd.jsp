<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body onload="loadcalendar();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：系统管理&gt;权限管理&gt; 模块操作维护(新增)</div>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input type="hidden" id="ACTION_ID" name="ACTION_ID" value="" jset="maindata"/>
<table id="tb1" class="table_query">
    <tr>  
        <td width="10%">模块选择:</td>
        <td width="20%">
            <input name="MODULE_ID" id="MODULE_ID" value="" type="hidden" readonly jset="maindata"/>
            <input name="MODULE_NAME" id="MODULE_NAME" value="" type="text" size="20" jset="maindata"/>
            <input name='dlbtn2' id='dlbtn2' class='mini_btn' type='button' value='...' onclick="popSingleSelect('moduleSQL','showSelect',{})"/>
        </td>
        <td width="10%">操作编码:</td>
        <td width="20%">
            <input type='text' name="ACTION_CODE" id="ACTION_CODE" jset="maindata">
        </td>
        <td width="10%">操作名称:</td>
        <td width="20%">
            <input type='text' name="ACTION_NAME" id="ACTION_NAME" jset="maindata">
        </td>                                  
    </tr>
    <tr>
        <td width="10%">操作类型:</td>
        <td width="20%">
            <select name="ACTION_TYPE" id="ACTION_TYPE" jset="maindata"> 
                <option value="1">行操作</option> 
                <option value="2">非行操作</option>
            </select> 
        </td>   
    </tr>    
    <!-- 
    <tr>
        <td width="10%">下级模块选择:</td>
        <td width="20%" bgcolor="#FFFFFF">&nbsp;
            <input name="SUB_MODULE_ID" id="SUB_MODULE_ID" value="" type="hidden" readonly jset="maindata"/>
            <input name="SUB_MODULE_NAME" id="SUB_MODULE_NAME" value="" type="text" size="20" jset="maindata"/>
            <input name='dlbtn2' id='dlbtn2' class='mini_btn' type='button' value='...' onclick="popMultiSelect('subModuleSQL','showSubSelect',{MODULE_ID:fm.MODULE_ID.value},{})"/>
        </td>   
    </tr>
     -->
</table>
<TABLE align=center width="100%" class=csstable >
    <TR class="tblopt">
        <TD width="100%" class="tblopt">
        <div align="center">
            <input type="button" class="normal_btn" onClick="save();" value="保存">&nbsp;
            <input type="button" class="normal_btn" onclick="back();" value="返回" />
        </div>
        </TD>
    </TR>
</TABLE>
</form>
</div>

<script type=text/javascript>

//回调函数,popSingleSelect的第二个参数
function showSelect(data){
	fm.MODULE_ID.value=data.ID;
	fm.MODULE_NAME.value=data.FUNC_NAME;
}

/*回调函数,popMultiSelect的第二个参数
function showSubSelect(arrayJSON){
	var SUB_MODULE_ID="";
	var SUB_MODULE_NAME="";
	for(var i=0;i<arrayJSON.length;i++){
		SUB_MODULE_ID=SUB_MODULE_ID+arrayJSON[i].ID+",";
		SUB_MODULE_NAME=SUB_MODULE_NAME+arrayJSON[i].FUNC_NAME+",";
	}
	SUB_MODULE_ID=SUB_MODULE_ID.substr(0,SUB_MODULE_ID.length-1);
	SUB_MODULE_NAME=SUB_MODULE_NAME.substr(0,SUB_MODULE_NAME.length-1);
	fm.SUB_MODULE_ID.value=SUB_MODULE_ID;
	fm.SUB_MODULE_NAME.value=SUB_MODULE_NAME;	
}
*/
//保存
function save(){
    var json=getElementsByJSet("maindata");   
    var url= "<%=contextPath%>/sysmng/modulemng/ModuleAction/ActionSave.json?json="+json;
	sendAjax(url,saveResult,'fm');
}

//保存回调函数
function saveResult(json){
    if (json != null) {
        var success = json.success;
        var error = json.error;
        var exceptions = json.Exception;
        if (success) {
        	MyAlertForFun(success,back);
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}

//返回
function back(){
	fm.action="<%=contextPath%>/sysmng/modulemng/ModuleAction/ModuleActionInit.do";
	fm.submit();
}

</script>

</body>
</html>

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
<body >
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：基础信息管理 &gt; 配件基础信息维护 &gt; 包装材料(新增)</div>
    <form name="fm" id="fm" method="post" enctype="multipart/form-data">
        <input type="hidden" id="PACK_ID" name="PACK_ID" value=""/>
        <div class="form-panel">
            <h2>包装材料(新增)</h2>
            <div class="form-body">
                <table id="tb1" class="table_query">
                    <tr>
                        <td width="7%" class="table_query_2Col_label_6Letter right">规格:</td>
                        <td width="15%" align="left">
                            <input id="PACK_SPEC" class="middle_txt" name="PACK_SPEC" type="text" value=""/>
                        </td>
                        <td width="10%" class="table_query_2Col_label_6Letter right">包装类别:</td>
                        <td width="19%" align="left">
                            ${selectBox}<font color="red">*</font>
                        </td>    
                        <td width="15%" class="table_query_2Col_label_6Letter right">名称:</td>
                        <td width="15%" align="left">
                            <input id="PACK_NAME" class="middle_txt" name="PACK_NAME" type="text" value=""/>
                        </td>
                        <td width="15%" class="table_query_2Col_label_6Letter right">单位:</td>
                        <td width="15%" align="left">
                            <input id="PACK_UOM" class="middle_txt" name="PACK_UOM" type="text" value=""/>
                        </td>        
                    </tr>
                    <tr>
                        <td width="7%" class="table_query_2Col_label_6Letter right">库存:</td>
                        <td width="15%" align="left">
                            <input id="PACK_QTY" class="middle_txt" name="PACK_QTY" type="text" value=""/>
                        </td>                       
                    </tr>
                </table>
                <TABLE align=center width="100%" class=csstable >
                    <TR class="tblopt">
                        <TD width="100%" class="tblopt">
                            <div align="center">
                                <input type="button" class="u-button" onclick="save();" value="保存">&nbsp;
                                <input type="button" class="u-button u-cancel" onclick="back();" value="返回" />
                            </div>
                        </TD>
                    </TR>
                </TABLE>
            </div>
        </div>
    </form>
</div>

<script type=text/javascript>

function validate(value){
    var reg = new RegExp("^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$");
    if(!reg.test(value)){
        return false;
    }else{
    	return true;
    }
}

function isNull(value){
	if(value=="0"||value=="undefine"||value==""){
		return false;
	}else{
		return true;
	}
}

function save(){
	if(!validate(fm.PACK_QTY.value)){
		MyAlert("请录入正确库存!");
		return;
	}
	if(!isNull(fm.PACK_TYPE.value)){
		MyAlert("请选择包装类别!");
		return;
	}
	var json=getElementsByJSet("maindata");
	var url= "<%=contextPath%>/parts/baseManager/packageManager/packageManager/save.json?json="+json;
	sendAjax(url,saveResult,'fm');	
}

function saveResult(json) {
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
function back() {
	fm.action="<%=contextPath%>/parts/baseManager/packageManager/packageManager/packageMainInit.do";
	fm.submit();
}

$(function() {
    $('select[name="PACK_TYPE"]').addClass('u-select');
});
</script>

</body>
</html>

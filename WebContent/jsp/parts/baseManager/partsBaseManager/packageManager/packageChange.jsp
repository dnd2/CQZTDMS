<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	String packageId = request.getParameter("packageId");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>出入库</title>
<style>.u-select#FLAG{float:left;margin:0}</style>
</head>

<body>
<div class="wbox">
	<form name="fm" id="fm" method="post" enctype="multipart/form-data">
		<input type="hidden" id="PACK_ID" name="PACK_ID" value="<%=packageId%>" jset="data"/>
			<table class="table_query" >
				<tr>
					<td width="20%">出入库：</td>
					<td width="30%">
						<select id="FLAG" class="u-select" jset="data">
							<option value="">请选择</option>
							<option value="1">入库</option>
							<option value="2">出库</option>
						</select>
					</td>
					<td class="right" width="20%">数量：</td>
					<td width="30%"><input class="short_txt middle_txt" id="QTY" jset="data"></td>      	        
				</tr>
				<tr>
					<td width="20%">备注：</td>
					<td width="80%" colspan="3">
						<textarea rows="4" id="remark" class="form-control remark" jset="data"></textarea>
					</td>
				</tr>
				<tr>
					<td class="txt-center" align='center' colspan=2>
						<input id="saveBtn" type="button" class="u-button" onClick="saveQty();" value="保存"/>        
					</td>
				</tr>
			</table>
	</form>		
</div>	

</body>
<script type="text/javascript">

  /*  fm.FLAG.value=2;*/

    function validate(value){
        var reg = new RegExp("^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$");
        if(!reg.test(value)){
            return false;
        }else{
        	return true;
        }
   }
    
    function saveQty(){
		var json=getElementsByJSet("data");
		var qty=fm.QTY.value;
        if($("FLAG").value ==""){
            MyAlert("出入库类型必填!");
            return
        }
		if(!validate(qty)){
			MyAlert("请输入数字!");
			return;
		}else{
		    url="<%=contextPath%>/parts/baseManager/packageManager/packageManager/change.json?json="+json;
		    sendAjax(url,callBackSave,'fm');
		}
	}

    /*保存之后触发函数*/
	function callBackSave(json){
	    if (json != null) {
	        var success = json.success;	        
	        var error = json.error;
	        var exceptions = json.Exception;
	        if (success) {
	        	MyAlert(success);
	        	parentContainer.__extQuery__(1);
	        	_hide();
	        } else if (error) {
	        	MyAlert(error);
	        } else if (exceptions) {
	        	MyAlert(exceptions.message);
	        }
	    }		
	}
</script>
</html>

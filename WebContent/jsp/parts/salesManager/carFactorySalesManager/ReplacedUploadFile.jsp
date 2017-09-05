<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();

%>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>demo</title>
<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css"/>
<link href="<%=contextPath%>/style/calendar.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript">
jQuery.noConflict();
var partArr = [];
function uploadExcel(code){
    if(confirm("确定上传订单?，点击确定后请耐心等待.........!")){
    btnDisable()
	var importFileName = $("uploadFile1").value;
	if(importFileName==""){
	    MyAlert("请选择上传文件!");
        btnEable();
		return false;
	}
	var index = importFileName.lastIndexOf(".");
	var suffix = importFileName.substr(index+1,importFileName.length).toLowerCase();
	if(suffix != "xls" && suffix != "xlsx"){
	MyAlert("请选择Excel格式文件!");
       btnEable();
		return false;
	}
	$('SELLER_ID').value=parent.document.getElementById("SELLER_ID").value;
    $('dealerId').value=parent.document.getElementById("dealerId").value;
	$('ORDER_TYPE').value= parent.document.getElementById("ORDER_TYPE").value;
	$('produceFac').value=parent.document.getElementById("produceFac").value;
	
	if($('ORDER_TYPE').value==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>){
		$('brand').value=parent.document.getElementById("brand").value;
	}
	parent.disableAllBtn();
	if(code=="save"){
		$('saveFlag').value=code;
	}

	fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/uploadExcel.do?";
	fm.submit();
}
}
jQuery(
		function(){
			if($('flag').value){
				parent.document.getElementById("seq").value = $('seq').value;
				$('uploadDiv').style.display = "block";
				var error = "${partData.error}";
				if($('saveFlag').value=="save"){
			    	parent.saveOrder();
					partArr=[];
					return;
				}
			    parent.getUploadData(false);
                parent.doInit();
				partArr=[];
			}
		})
function exportExcelTemplate(){
	parent.exportExcelTemplate();
}
</script>
</head>

<body onload = "doInit();autoAlertException();" >
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input id="flag" name="flag" type="hidden" value="${flag}">
<input id="error" name="error" type="hidden" value="${error}">
<input id="SELLER_ID" name="SELLER_ID" type="hidden" value="">
<input id="dealerId" name="dealerId" type="hidden" value="">
<input id="ORDER_TYPE" name="ORDER_TYPE" type="hidden" value="">
<input id="produceFac" name="produceFac" type="hidden" value="">
<input id="brand" name="brand" type="hidden" value="">
<input id="partData" name="partData" type="hidden" value="${partData}">
<input id="saveFlag" name="saveFlag" type="hidden" value="${saveFlag}" />
<input id="seq" name="seq" type="hidden" value="${seq}" />
<div style="display:none; heigeht: 5px" id="uploadDiv">

    <tr>
        <td><font color="red">
            <input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate()"/>
            文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font>
            <input type="file" name="uploadFile1" id="uploadFile1" style="width: 250px" datatype="0,is_null,2000"
                   value=""/>
            &nbsp;
            <input type="button" id="upbtn" class="normal_btn" value="确定" onclick="uploadExcel('')"/></td>
            <input type="button" id="upbtn" class="normal_btn" value="直接生成订单" style="width:80px" onclick="uploadExcel('save')"/></td>
    </tr>

</div>
</form>
</body>
</html>

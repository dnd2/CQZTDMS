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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>demo</title>
<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css"/>
<link href="<%=contextPath%>/style/calendar.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript">
    MyAlert($('noDtl').value);
var partArr = [];
function uploadExcel(){
	$('wh_id').value=parent.document.getElementById("wh_id").value;
	$('beginTime2').value=parent.document.getElementById("beginTime2").value;
	$('PLAN_CYCLE').value=parent.document.getElementById("PLAN_CYCLE").value;
	$('COME_CYCLE').value=parent.document.getElementById("COME_CYCLE").value;
	$('VENDER_ID').value=parent.document.getElementById("VENDER_ID").value;
	$('MAKER_ID').value=parent.document.getElementById("MAKER_ID").value;
	$('SELECT_OP').value=parent.document.getElementById("SELECT_OP").value;
	$('PLANQTY').value=parent.document.getElementById("PLANQTY").value;
	fm.action = "<%=contextPath%>/parts/purchaseManager/purchasePlanSetting/PurchasePlanSetting/uploadPartPlanExcel.do?"
	fm.submit();
}
function doInit() {
	if($('flag').value){
		$('uploadDiv').style.display = "block";
		var error = "${partData.error}";

		parent.getUploadResult(partArr,error);
		partArr=[];
	}
   
}
function exportExcelTemplate(){
	parent.exportExcelTemplate();
}
</script>
</head>

<body onload = "autoAlertException();doInit();" >
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input id="wh_id" name="wh_id" type="hidden" value="">
<input id="beginTime2" name="beginTime2" type="hidden" value="">
<input id="PLAN_CYCLE" name="PLAN_CYCLE" type="hidden" value="">
<input id="COME_CYCLE" name="COME_CYCLE" type="hidden" value="">
<input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
<input id="MAKER_ID" name="MAKER_ID" type="hidden" value="">
<input id="SELECT_OP" name="SELECT_OP" type="hidden" value="">
<input id="PLANQTY" name="PLANQTY" type="hidden" value="">
<input id="flag" name="flag" type="hidden" value="${flag}">
<input id="noDtl" name="noDtl" type="hidden" value="${noDtl}">
<div style="display:none; heigeht: 5px" id="uploadDiv">

    <tr>
        <td><font color="red">
            <input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate()"/>
            文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font>
            <input type="file" name="uploadFile1" id="uploadFile1" style="width: 250px" datatype="0,is_null,2000"
                   value=""/>
            &nbsp;
            <input type="button" id="upbtn" class="normal_btn" value="确定" onclick="uploadExcel()"/></td>
    </tr>
<c:forEach items="${partData.dataList}" var="data" >
	<script type="text/javascript">
		var obj = new Object();
		obj.PART_ID = "${data.PART_ID}";
		obj.PART_CODE = "${data.PART_CODE}";
		obj.PART_OLDCODE= "${data.PART_OLDCODE}";
		obj.PART_CNAME = "${data.PART_CNAME}";
		obj.UNIT = "${data.UNIT}";
		obj.MIN_PACKAGE = "${data.MIN_PACKAGE}";
		obj.SALE_PRICE3 = "${data.SALE_PRICE3}";
		obj.BUY_PRICE = "${data.BUY_PRICE}";
		obj.planQty = "${data.planQty}";
		obj.ITEM_QTY = "${data.ITEM_QTY}";
		obj.BO_QTY = "${data.BO_QTY}";
		obj.AVG_QTY = "${data.AVG_QTY}";
		obj.ORDER_QTY = "${data.ORDER_QTY}";
		obj.SAFETY_STOCK = "${data.SAFETY_STOCK}";
		obj.PREARRIVEDATE = "${data.PREARRIVEDATE}";
		obj.REMARK = "${data.remark}";
		partArr.push(obj);
	</script>
</c:forEach>
</div>
</form>
</body>
</html>

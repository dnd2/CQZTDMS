<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件采购订单-修改</title>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);loadcalendar();">
<div class="wbox">
	<div class="navigation"> <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;采购计划管理&gt;配件采购订单&gt;修改 </div>
<form name="fm" id="fm" method="post" >
<input type="hidden" name="orderId" value="${mainInfo.ORDER_ID}"/>
<input type="hidden" name="polineIds" id="polineIds"/>
	<table class="table_query">
    <tr>
      <td bgcolor="#F3F4F8"   align="right">订单单号:</td>
      <td bgcolor="#FFFFFF" align="left" width="24%">&nbsp;<c:out value="${mainInfo.ORDER_CODE}" />
      <input type="hidden" name="orderCode" value="${mainInfo.ORDER_CODE}"/>
      </td>
<!--       <td   align="right" bgcolor="#F3F4F8">采购员:</td> -->
<%--       <td align="left" bgcolor="#FFFFFF" width="24%">&nbsp;<c:out value="${mainInfo.BUYER}" /><br /> --%>
<%--       <input type="hidden" name="BUYER_ID" value="${mainInfo.BUYER_ID}"/> --%>
<%--       <input type="hidden" name="BUYER" value="${mainInfo.BUYER}"/> --%>
<!--       </td> -->
      <td   align="right" bgcolor="#F3F4F8">制单日期:</td>
      <td align="left" bgcolor="#FFFFFF" width="24%">&nbsp;<c:out value="${mainInfo.CREATE_DATE}" /></td>
    </tr>
<!--     <tr> -->
<!--       <td   align="right" bgcolor="#F3F4F8">计划类型:</td> -->
<!--       <td align="left" bgcolor="#FFFFFF">&nbsp; -->
<!--       	<script type="text/javascript"> -->
<%--        			genSelBoxExp("PLAN_TYPE",<%=Constant.PART_PURCHASE_PLAN_TYPE%>,${mainInfo.PLAN_TYPE},true,"short_sel","disabled='disabled'","false",''); --%>
<!-- 		</script> -->
<%-- 		<input type="hidden" name="PLAN_TYPE1" value="${mainInfo.PLAN_TYPE}"/> --%>
<!--       </td> -->
<!--       <td   align="right" bgcolor="#F3F4F8">库房:</td> -->
<%--       <td align="left" bgcolor="#FFFFFF" width="21%">&nbsp;<c:out value="${mainInfo.WH_NAME}" /> --%>
<%--       <input type="hidden" name="WH_ID" value="${mainInfo.WH_ID}"/> --%>
<%--       <input type="hidden" name="WH_NAME" value="${mainInfo.WH_NAME}"/> --%>
<!--       <br /> -->
<!--       </td> -->
<!--     </tr> -->
    <tr>
      <td bgcolor="#F3F4F8"   align="right">总数量:</td>
      <td bgcolor="#FFFFFF" align="left">&nbsp;<input readonly class="phone_txt" type="text" style="border:0px;background-color:#F3F4F8;" value="${mainInfo.SUM_QTY}"  name="SUM_QTY" id="SUM_QTY" /></td>
      <td bgcolor="#F3F4F8"   align="right">总金额:</td>
      <td bgcolor="#FFFFFF" align="left">&nbsp;<input readonly class="phone_txt" type="text" style="border:0px;background-color:#F3F4F8;" value="${mainInfo.AMOUNT}"  name="AMOUNT" id="AMOUNT" /></td>
      <td bgcolor="#F3F4F8"   align="right"></td>
      <td bgcolor="#FFFFFF" align="left"></td>
    </tr>
<!--     <tr> -->
<!--       <td   align="right" bgcolor="#F3F4F8">备注:</td> -->
<%--       <td align="left" bgcolor="#FFFFFF" colspan="5">&nbsp;<c:out value="${mainInfo.REMARK}" /></td> --%>
<!--     </tr> -->
</table>
    <table class="table_query">
<th colspan="6" width="100%"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />配件信息</th>
    <tr>
       <td align="right">  配件编码： </td>
       <td align="left" >
           <input class="middle_txt" id="PART_OLDCODE"
                  datatype="1,is_noquotation,30" name="PART_OLDCODE"
                  type="text" value="${partOldCode }"/>
       </td>
       <td  align="right"> 配件名称： </td>
       <td align="left">
           <input class="middle_txt" id="PART_CNAME"
                  datatype="1,is_noquotation,30" name="PART_CNAME"
                  type="text" value="${partCname }"/>
       </td>
       <td  align="right"> 件号：</td>
       <td align="left">
           <input class="middle_txt" id="PART_CODE"
                  datatype="1,is_noquotation,30" name="PART_CODE"
                  type="text" value="${PART_CODE }"/>
       </td>
    </tr>
    <tr>
		<td align="center" colspan="6">
			<input class="normal_btn" type="button" name="BtnQuery"
				id="queryBtn" value="查 询" onclick="__extQuery__(1)" />
		</td>
	</tr>
    </table>
  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<table border="0" class="table_query">
  <tr align="center">
	  <td>
		  <input class="cssbutton" type="button" value="保 存" name="button1" onclick="confirmPlan();">&nbsp;
		  <input class="normal_btn" type="button" value="返 回" onclick="javascript:goback();"/>&nbsp;
	  </td>
  </tr>
  </table>
</form>
<script type="text/javascript" ><!--
var myPage;

var url = "<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/queryPurOrderDtl4Mod.json";

var title = null;

var columns = [
				{header: "序号", align:'center',renderer:getIndex},
				{header: "<input type='checkbox' id='selectAll' name='selectAll' onclick='checkselectAllBox(this)'  />", dataIndex: 'POLINE_ID', align: 'center', renderer: checkBoxLink},//2013-10-16 add zhumingwei 增加复选框保存数量金额
				{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
				{header: "配件名称", dataIndex: 'PART_CNAME',style: 'text-align:left'},
                {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
				{header: "单位", dataIndex: 'UNIT', align:'center'},
				{header: "供应商名称", dataIndex: 'VENDER_NAME', align:'center'},
				{header: "制造商名称", dataIndex: 'MAKER_NAME', align:'center'},
				{header: "库管员", dataIndex: 'WHMAN_NAME', align:'center'},
				{header: "计划数量", dataIndex: 'PLAN_QTY', align:'center'},
				{header: "采购数量", dataIndex: 'BUY_QTY', align:'center',renderer: getBuyQty},
				{header: "金额", dataIndex: 'BUY_AMOUNT', align:'center',renderer: getBuyAmount},
				{header: "已生成数量", dataIndex: 'CHECK_QTY', align:'center'},
				{header: "备注", dataIndex: 'REMARK', align:'center'},
				{id: 'action', header: "操作", sortable: false, dataIndex: 'POLINE_ID', renderer: myLink, align: 'center'}
		      ];

function myLink(value, meta, record) {
	var orderId = record.data.ORDER_ID;
    return String.format("<a href=\"#\" onclick='updatePurOrder(" + value + ","+orderId+")'>[保存]</a>"+"|"+"<a href=\"#\" onclick='delPurOrder(" + value + ","+orderId+")'>[删除]</a>");
}


//2013-10-16 add zhumingwei 增加复选框保存数量金额 begin
function checkselectAllBox(obj) {
    var boxs = document.getElementsByName('checkboxs');
    for (var i = 0; i < boxs.length; i++) {
        boxs[i].checked = obj.checked;
    }

}
function checkBoxLink(value, meta, record) {
    return "<input type='checkbox' id='checkboxs' name='checkboxs' value='" + value + "'  />";
}
//2013-10-16 add zhumingwei 增加复选框保存数量金额 end

function getBuyQty(value, meta, record){
	var pId = record.data.POLINE_ID;
	var output = '<input type="test" id="BUY_QTY' + pId + '" name="BUY_QTY' + pId + '" value="'+value+'"/><input type="hidden" id="BUY_QTY1' + pId + '" name="BUY_QTY1' + pId + '" value="'+value+'"/>';
    return output;
}

function getBuyAmount(value, meta, record){
	var pId = record.data.POLINE_ID;
	var output = '<input type="hidden" id="BUY_AMOUNT' + pId + '" name="BUY_AMOUNT' + pId + '" value="'+value+'"/>\n'+value;
    return output;
}

function delPurOrder(value,orderId){
	if (confirm("确定删除?")) {
        btnDisable();
        var url = '<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/deletePodtl.json?orderId=' + orderId +'&pId='+value+'&curPage=' + myPage.page;
        sendAjax(url, getResult, 'fm');
    }
}

function getResult(jsonObj) {
    btnEnable();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var backFlag = jsonObj.backFlag;
        var exceptions = jsonObj.Exception;
        if (success) {
        	MyAlert(success);
        	if(backFlag){
        		window.location.href = '<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/purchaseOrderQueryInit.do';
        	}else{
        		$("SUM_QTY").value = jsonObj.sumQty;
        		$("AMOUNT").value = jsonObj.amount;
        		$("PART_OLDCODE").value = jsonObj.partOldCode;
        		$("PART_CNAME").value = jsonObj.partCname;
        		$("PART_CODE").value = jsonObj.PART_CODE;
        		__extQuery__(jsonObj.curPage);
        	}
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}

function updatePurOrder(value,orderId){
	if (confirm("确定保存?")) {
        btnDisable();
        var url = '<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/updatePodtl.json?orderId=' + orderId +'&pId='+value+'&curPage=' + myPage.page;
        sendAjax(url, getResult1, 'fm');
    }
}

function getResult1(jsonObj) {
    btnEnable();
    if (jsonObj != null) {
   		$("#SUM_QTY")[0].value = jsonObj.sumQty;
   		$("#AMOUNT")[0].value = jsonObj.amount;
   		$("#PART_OLDCODE")[0].value = jsonObj.partOldCode;
   		$("#PART_CNAME")[0].value = jsonObj.partCname;
   		$("#PART_CODE")[0].value = jsonObj.PART_CODE;
   		__extQuery__(jsonObj.curPage); 
    }
}

//2013-10-16 add zhumingwei 增加复选框保存数量金额 begin
function confirmPlan() {
    var planId = "";
    var cb = document.getElementsByName("checkboxs");
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            planId += cb[i].value + ",";
        }
    }
    if (planId == "") {
        MyAlert("请选择保存项目!");
        return;
    }
    document.getElementById("polineIds").value = planId;
    MyConfirm("确定保存所选的项目?", confirmPlanCommit, []);
}
function confirmPlanCommit() {
	btnDisable();
	var url = '<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/partPlanCheckCommit.json?curPage=' + myPage.page;
    sendAjax(url, getResult2, 'fm');
    
}
function getResult2(jsonObj) {
    btnEnable();
    if (jsonObj != null) {
   		$("SUM_QTY").value = jsonObj.sumQty;
   		$("AMOUNT").value = jsonObj.amount;
   		$("PART_OLDCODE").value = jsonObj.partOldCode;
   		$("PART_CNAME").value = jsonObj.partCname;
   		$("PART_CODE").value = jsonObj.PART_CODE;
   		__extQuery__(jsonObj.curPage); 
    }
}
//2013-10-16 add zhumingwei 增加复选框保存数量金额 end

//返回查询页面
function goback(){
	window.location.href = '<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/purchaseOrderQueryInit.do';
}
-->
</script>
</div>
</body>
</html>

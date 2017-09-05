<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件BO明细关闭</title>
<style type="text/css">
.table_list_row0 td {
	background-color:#FFFFCC;
	border: 1px solid #DAE0EE;
	white-space:    nowrap;
}
.form-panel{margin-bottom: 10px}
</style>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理&gt;配件销售管理&gt;BO单处理&gt;关闭</div>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input type="hidden" name="boId" id="boId" value="${po['BO_ID'] }"/>
<input type="hidden" name="flag" id="flag" value="${flag}"/>
<div class="form-panel">
     <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>BO单信息</h2>
     <div class="form-body">
	  <table class="table_query">
	  <tr>
	      <td width="109" class="right" >BO单号：</td>
	      <td width="378" >${po['BO_CODE'] }</td>
	      <td width="109" class="right" >配件订单号：</td>
	      <td width="260" >${po['ORDER_CODE'] }</td>
	      <td width="98" class="right" >制单人：</td>
	      <td width="307" >${po['NAME'] }</td>
	    </tr>
	    <tr>
	      <td class="right">制单日期：</td>
	      <td>${po['CREATE_DATE'] }</td>
	      <td class="right"  >销售单位：</td>
	      <td >${po['SELLER_NAME'] }</td>
	      <td class="right"  >订货单位：</td>
	      <td >${po['DEALER_NAME'] }</td>
	    </tr>
	</table>
	</div>
	</div>
	<div class="form-panel">
	     <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>配件信息</h2>
	     <div class="form-body">
	    <table class="table_query">
	    <tr>
	            <td class="right">配件编码：</td>
	            <td align="left" >
	                <input class="middle_txt" id="PART_OLDCODE" datatype="1,is_noquotation,30" name="PART_OLDCODE" type="text"/>
	            </td>
	            <td  class="right"> 配件名称：</td>
	            <td align="left">
	                <input class="middle_txt" id="PART_CNAME" datatype="1,is_noquotation,30" name="PART_CNAME" type="text"/>
	            </td>
	    </tr>
	    <tr>
			<td class="center" colspan="4">
				<input class="normal_btn" type="button" name="BtnQuery" id="queryBtn" value="查 询" onclick="__extQuery__(1)" />
			</td>
		</tr>
	    </table>
	    </div>
	 </div>   
    <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
    <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<table width="100%" >
  <tr>
  <td  align="center">
  <input class="normal_btn" type="button" value="返 回" onclick="goBack()"/>
  </td>
  </tr>
  </table>
</form>
<script type=text/javascript>
 var myPage;
 var orderId = ${orderId};
 var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/queryPartBoDetail.json?orderId="+orderId;
 var title = null;
 var columns = [
				{header: "序号", align:'center', renderer:getIndex,width:'7%'},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
				{header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
				{header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
				{header: "单位", dataIndex: 'UNIT', align:'center'},
				{header: "订货单价", dataIndex: 'BUY_PRICE', style: 'text-align:right'},
				{header: "订货数量", dataIndex: 'BUY_QTY', align:'center'},
				{header: "满足数量", dataIndex: 'SALES_QTY', align:'center'},
				{header: "BO数量", dataIndex: 'BO_QTY', align:'center'},
				{header: "转销售数量", dataIndex: 'TOSAL_QTY', align:'center'},
				{header: "关闭数量", dataIndex: 'CLOSE_QTY', align:'center'},
				{header: "BO剩余数量", dataIndex: 'BO_ODDQTY', align:'center'},
				{header: "库存数量", dataIndex: 'NORMAL_QTY', align:'center'},
				{header: "生成日期", dataIndex: 'CREATE_DATE', align:'center',renderer:formatDate},
				{header: "处理状态", dataIndex: 'STATUS', align:'center'},
				{header: "备注", dataIndex: 'REMARK', align:'center',renderer:insertInput},
				{id:'action',header: "操作",sortable: false,dataIndex: 'BOLINE_ID',renderer:myLink ,align:'center'}
			  ];
    function myLink(value,meta,record){
		var boId = record.data.BO_ID;
		var stas = record.data.STATUS;
		if(stas=='未处理完成'){
			return String.format("<a href=\"#\" onclick='closeBoDtl(\""+value+"\","+boId+")'>[强制关闭]</a>");
		}
		return "";
	}
    function insertInput(value,meta,record){
        var boLineId = record.data.BOLINE_ID;
        var stas = record.data.STATUS;
        if(stas=='未处理完成'){
        	return "<input type='text' class='middle_txt' value='"+value+"' name='REMARK"+boLineId+"' id='REMARK"+boLineId+"' />";
        }
        return value;
    }
	function closeBoDtl(value,boId){
		MyConfirm("确定关闭?",confirmResult,[value,boId]);
	}
	
	function confirmResult(boLineId,boId){
		var url1 = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/closeBoDtl.json?boLineId="+boLineId+"&boId="+boId;	
		makeNomalFormCall(url1,getResult,'fm');
	}
	function getResult(jsonObj){
		if(jsonObj){
			var success = jsonObj.success;
			var isAllClose = jsonObj.isAllClose;
			var exceptions = jsonObj.Exception;
		    if(success){
		    	MyAlert(success);
		    	if(isAllClose==true){
		    		window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/boHandleQueryInit.do";
		    	}else{
		    		__extQuery__(1);
		    	}
		    }/* else if(exceptions){
		    	MyAlert(exceptions.message);
			} */
		}
	}
	function goBack(){
	  window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/boHandleQueryInit.do";
	}
	//格式化日期
	function formatDate(value,meta,record){
		var output = value.substr(0,10);
		return output;
	}
</script>
</div>
</body>
</html>

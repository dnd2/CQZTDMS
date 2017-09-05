<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>结算明细查询</title>
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/queryOrderBalanceDtlInfo.json";

	var title = null;

	var columns = [
                {header: "序号", align: 'center', renderer: getIndex},
				//{header: "验收单号", dataIndex: 'CHECK_CODE',  style: 'text-align:left'},
				{header: "入库单号", dataIndex: 'IN_CODE',  style: 'text-align:left'},
				{header: "入库日期", dataIndex: 'IN_DATE', align:'center',renderer:formatDate},
				//{header: "配件类型", dataIndex: 'PART_TYPE', align:'center',renderer:getItemValue},
				{header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align:left'},
				{header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align:left'},
				{header: "配件件号", dataIndex: 'PART_CODE',  style: 'text-align:left'},
				{header: "入库库房", dataIndex: 'WH_NAME',  style: 'text-align:left'},
				{header: "入库数量", dataIndex: 'IN_QTY', align:'center'},
				{header: "退货数量", dataIndex: 'RETURN_QTY', align:'center'},
				{header: "结算数量", dataIndex: 'BAL_QTY', align:'center'},
				{header: "供应商名称", dataIndex: 'VENDER_NAME',  style: 'text-align:left'},
				{header: "制造商名称", dataIndex: 'MAKER_NAME',  style: 'text-align:left'},
				{header: "计划价", dataIndex: 'PLAN_PRICE', align:'center'},
				{header: "计划金额", dataIndex: 'IN_AMOUNT', align:'center'},
				{header: "采购价", dataIndex: 'BUY_PRICE', align:'center'},
				{header: "采购金额", dataIndex: 'BAL_AMOUNT', align:'center'}
		      ];

	//格式化日期
	function formatDate(value,meta,record){
		var output = value.substr(0,10);
		return output;
	}

    function  myback(){
        var flag = $("#flag")[0].value;
        if(flag=="1"){
        	window.location.href="<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/purOrderBalanceConfirmQueryInit.do";
        }else{
        	window.location.href="<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/purOrderBalancePrintQueryInit.do";
        }
    }
    function clearInput() {
        //清空选定供应商
        document.getElementById("VENDER_ID").value = '';
        document.getElementById("VENDER_NAME").value = '';
    }
</script>
</head>
<body onload="__extQuery__(1);"> <!-- onunload='javascript:destoryPrototype()' -->
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：
		当前位置：配件管理&gt; 采购订单管理&gt; 采购订单结算明细
</div>
<form method="post" name ="fm" id="fm" enctype="multipart/form-data">
    <input type="hidden" name="curPage" id="curPage" />
    <input type="hidden" name="partId" id="partId"/>
    <input type="hidden" name="flag" id="flag" value="${flag }"/>
    <input type="hidden" name="BALANCE_CODE" id="BALANCE_CODE"  value="${BALANCE_CODE}" />
     <div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
	<table class="table_query">
	    <tr>

	      <td class="right">库房：</td>
	      <td >
	      <select id="WH_ID" name="WH_ID" class="u-select">
	      <option value="">-请选择-</option>
	      <c:forEach items="${wareHouses}" var="wareHouse">
            <option value="${wareHouse.whId }">${wareHouse.whName }</option>
          </c:forEach>
	      </select>
          </td>

          <td class="right">配件种类：</td>
      <td >
        <script type="text/javascript">
		       genSelBoxExp("PRODUCE_STATE",<%=Constant.PART_PRODUCE_STATE %>,"",true,"","","false",'');
		</script>
    </td>
      <td class="right">入库时间：</td>
           <td >
           		<input name="inBeginTime" id="t3" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="t3,t4" style="width: 80px;">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" />
           		至
           		<input name="inEndTime" id="t4" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="t3,t4" style="width: 80px;">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" />
          </td>
      </tr>
	<tr>
      <td class="right" >配件编码：</td>
      <td ><input name="PART_OLDCODE" type="text" class="middle_txt" id="PART_OLDCODE"/></td>
	  <td class="right">配件名称：</td>
	  <td ><input name="PART_CNAME" type="text" class="middle_txt" id="PART_CNAME"/></td>
	  <td class="right" >配件件号：</td>
      <td ><input name="PART_CODE" type="text" class="middle_txt" id="PART_CODE"/></td>
    </tr>

 <tr>
	      <td class="right">验收单号：</td>
	      <td ><input class="middle_txt" type="text"  name="CHECK_CODE"/></td>
	      <td class="right">入库单号：</td>
	      <td  ><input class="middle_txt" type="text"  name="IN_CODE"/></td>
        </tr>
    <tr>
     <td class="center" colspan="6"><input name="BtnQuery" id="queryBtn" type="button" class="u-button" onclick="__extQuery__(1);" value="查 询"/>
         &nbsp;<input name="button" type="button" class="u-button" onclick="myback();"
                      value="返回"/>
   </tr>
	</table>
</div>
</div>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div>
</body>
</html>
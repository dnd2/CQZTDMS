<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
    int flag1 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03;
    int flag2 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05;
    request.setAttribute("flag1",flag1);
    request.setAttribute("flag2",flag2);
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件调拨查看</title>
<style type="text/css">
.table_list_row0 td {
	background-color:#FFFFCC;
	border: 1px solid #DAE0EE;
	white-space:    nowrap;
}
</style>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理&gt;配件调拨管理&gt;配件调拨单&gt;查看</div>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input type="hidden" name="orderId" id="orderId" value="${po['ORDER_ID'] }"/>
  <table class="table_query">
    <tr>
    <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />调拨信息</th>
  </tr>
  <tr>
      <td width="109"   align="right" >调拨单号：</td>
      <td width="378" >
        ${po['ORDER_CODE'] }
      </td>
      <td width="109"   align="right" >调出单位：</td>
      <td width="260" >
        ${po['SELLER_NAME'] }
      </td>
      <td width="98"   align="right" >制单人：</td>
      <td width="307" >
      ${po['NAME'] }
      </td>
    </tr>
    <tr>
      <td   align="right">制单日期：</td>
      <td>
      ${po['CREATE_DATE'] }
      </td>
      <td   align="right"  >接收单位：</td>
      <td >
      	${po['RCV_ORG'] }
      	</td>
      <td   align="right"  >接收人：</td>
      <td >
      ${po['RECEIVER'] }
      </td>
    </tr>
    <tr>
      <td   align="right" >接收地址：</td>
      <td >
      	${po['ADDR'] }
        </td>
        <td   align="right">接收人电话：</td>
        <td>
         ${po['TEL'] }
       </td>
       <td   align="right" >邮政编码：</td>
       <td >
       ${po['POST_CODE'] }
       </td>
    </tr>
    <tr>
    <td   align="right"  >发运方式：</td>
      <td >
      <select id="TRANS_TYPE" name="TRANS_TYPE" disabled="disabled">
      <c:forEach items="${transList}" var="trans">
         <option value="${trans.fixValue }" ${po['TRANS_TYPE']==trans.fixValue?"selected":""}>${trans.fixName}</option>
      </c:forEach>
      </select>
	 </td>
	 <td width="98"   align="right" >付款方式：</td>
      <td width="307" >
      <script type="text/javascript">
			genSelBoxExp("PAY_TYPE",<%=Constant.CAR_FACTORY_SALES_PAY_TYPE%>,${po['PAY_TYPE'] },false,"short_sel","disabled='disabled'","false",'');
	  </script> 
      </td>
      <td   align="right">总金额：</td>
      <td>
      ${po['ORDER_AMOUNT'] }
      </td>
    </tr>
    <tr>
    <td   align="right"  >仓库：</td>
     <td >
      ${po['WH_CNAME'] }
      </td>
      <td   align="right" >到站名称：</td>
       <td >
       ${po['STATION'] }
       </td>
    </tr>
    <tr>
      <td   align="right" >备注：</td>
      <td colspan="6"><textarea name="remark1" cols="80" rows="4">${po['REMARK'] }</textarea></td>
    </tr>
    <c:if test="${(po['STATE'] eq flag1)||(po['STATE'] eq flag2)}">
     <tr>
      <td   align="right" >审核意见：</td>
      <td colspan="6"><textarea name="AUDIT_OPINION" cols="80" rows="4">${po['AUDIT_OPINION'] }</textarea></td>
    </tr>
    </c:if>
</table>
<table id="file" class="table_list" style="border-bottom:1px solid #DAE0EE">
    <tr>
      <th colspan="7" align="left"><img class="nav" src="<%=contextPath%>/img/nav.gif" />配件信息
	  </th>
    </tr>
    </table>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
    <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<table class="table_edit">
  <tr align="center">
  <td>
  <input class="normal_btn" type="button" value="返 回" onclick="goBack()"/>
  </td>
  </tr>
  </table>
</form>
<script type=text/javascript>

 var myPage;

 var url = "<%=contextPath%>/parts/partAllotManager/PartTransferManager/queryPartTransferDetail.json";
				
 var title = null;

 var columns = [
				{header: "序号", align:'center', renderer:getIndex,width:'7%'},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
				{header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
				{header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
				{header: "单位", dataIndex: 'UNIT', align:'center'},
				{header: "最小包装量", dataIndex: 'MIN_PACKAGE', align:'center'},
				{header: "调拨数量", dataIndex: 'BUY_QTY', align:'center'},
				{header: "调拨单价", dataIndex: 'BUY_PRICE', style: 'text-align:right'},
				{header: "金额", dataIndex: 'BUY_AMOUNT', style: 'text-align:right'},
				{header: "备注", dataIndex: 'REMARK', align:'center'}
			  ];

	function goBack(){
		window.location.href = "<%=contextPath%>/parts/partAllotManager/PartTransferManager/queryPartTransferInit.do";
	}
</script>
</div>
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- created by lishuai103@yahoo.com.cn 201006025 配件签收 -->
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件采购订单维护</title>
</head>

<script language="JavaScript">

	//初始化方法
	function doInit()
	{
		loadcalendar();//时间控件
		selOrderDonoCount();
	}

</script>
<body>
<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件管理 &gt; 配件采购 &gt;配件订单维护
<form id="fm" name="fm" method="post">
<input type="hidden" id="orderNo" name="orderNo" value="<c:out value="${orderNo}"/>" />
<input type="hidden" id="doNo" name="doNo" value="<c:out value="${doNo}"/>" />
<input type="hidden" id="orderId" name="orderId" value="<c:out value="${orderId}"/>"/>
<input type="hidden" name="flag" value="2"/>
    <table class="table_edit"  >
      <tr>
        <td class="table_query_2Col_label_6Letter">货运单号：</td>
        <td class="table_query_2Col_input">
        	<c:out value="${doNo}"/>
          	<!--<input name="truedoNo" type="text" id="truedoNo" datatype="1,is_digit_letter,18"  class="middle_txt"/>  -->
        </td>
        <td class="table_query_2Col_label_6Letter">采购单号：</td>
        <td class="table_query_2Col_input">
          	<c:out value="${orderNo}"/>
        </td>
      </tr>
      <tr>
  	<!-- 
        <td class="table_query_2Col_label_6Letter">签收时间：</td>
        <td class="table_query_2Col_input">
          	<input name="signDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
        </td>
        <td class="table_query_2Col_label_6Letter">签收人：</td>
        <td class="table_query_2Col_input">
          	<input name="signMan" type="text" id="signMan" datatype="1,is_null,10"  class="middle_txt"/>
        </td>
      -->
      </tr>
    </table>
    <div id="partInfo">
    </div>
<br>
	<table class="table_edit">
		<tr>
		  <td align="center">		    
		  	<input type="button" name="BtnNo" value="确认签收" class="normal_btn" onclick="checkSign()">
			<input type="button" name="BtnNo" value="返回" class="normal_btn" onclick="javascript:history.go(-1)">
          </td>
        </tr>
	</table>
</form>
<script type="text/javascript" >
	function addPartInfo(json) {
		var partInfo = document.getElementById("partInfo");
		var str = '';
		str += '<table class="table_list" id="pi">';
		str += '<tr><th colspan="38" align="left"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 基本信息<input type="button" name="BtnYes2" value="新增" class="normal_btn" onclick="addPart()" /></th><tr>' + 
		' <tr> ' + 
		' <th>序号</th> ' + 
		' <th>配件号</th> ' + 
		' <th>配件名称</th>' +
		' <th>单位</th>' +
		' <th>订货数量</th>' +
		' <th>货运数量</th>' +
		' <th>签收数量</th>' +
		'</tr>';
		
		for (var i = 0; i < json.partInfoSet.length; i++) {
			str += '<tr>'
			str += '<td>' + (i+1) + '</td>'
			str += '<td>' + json.partInfoSet[i].partCode + '</td>'
			str += '<td>' + json.partInfoSet[i].partName + '</td>'
			str += '<td>' + json.partInfoSet[i].unit + '</td>'
			str += '<td>'+ json.partInfoSet[i].orderCount + '</td>'
			str += '<td>'+ json.partInfoSet[i].doCount + '</td>'
			str += '<td><input name="signCount" type="text" onblur="getSignCount(this.value, '+json.partInfoSet[i].partId+ ')" value="' + json.partInfoSet[i].signCount + '"/></td>'
			str += '</tr>';
		}
		str += '</table>';
		partInfo.innerHTML = str;
		
	}
	
	function selOrderDonoCount()
	{	
		var doNo = '<c:out value="${doNo}"/>';
		makeNomalFormCall("<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/doNoOrdreList.json?doNo=" + doNo, addPartInfo,'fm','queryBtn');
	}
	
	function addPart(){
		var oid = document.getElementById("orderId").value;
		OpenHtmlWindow('<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/setPartInfo.do?flag=2&orderId='+oid,800,500);
	}
	
	//在内存中存新增签收个数
	function getSignCount(signCount, partId) {
		if (signCount.trim()) {
			//去后台改变备注
			makeNomalFormCall("<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/modifySignCount.json?partId=" + partId+'&signCount='+signCount,'','fm','queryBtn');
		} 
	}
	
	//确认签收
	function checkSign()
	{
		fm.action = "<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/saveSign.do";
		fm.submit();
	}
</script>
</body>
</html>
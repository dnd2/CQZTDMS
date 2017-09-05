<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>移库通知单</title>
</head>
<script type="text/javascript">
	function doInit()
	{
		doprint();  //初始化时间控件
	}
</script>
<%
	Map cmap = (Map)request.getAttribute("map"); 
%>
<body>
<br/>
<center><strong><font size="6">移库通知单</font></strong></center>
<br><br><br>
<div align="center">
<table width="600" border="0" cellSpacing= "0" cellPadding= "0">
	<tr>
		<td align="left" width="230">订单号：<c:out value="${map.ERP_ORDER_NO}"/></td>
		<td align="left" width="300">发车地：<c:out value="${map.WAREHOUSE_NAME}"/></td>
		<td align="left" width="200">
			提货方式：发运
		</td>
		<td width="270" align="left">时间：<c:out value="${map.STO_DATE}"/></td>
	</tr>
</table>
<table width="600" border="1" cellSpacing= "0" cellPadding= "0">
  <tr align="left">
    <td width="230" height="30">分销中心(省总公司)</td>
    <td width="100">默认</td>
    <td width="109">经理</td>
    <td width="170">销售代表</td>
    <td width="91">经理电话</td>
    <td width="199">&nbsp;</td>
  </tr>
  <tr>
    <td height="100%" colspan="6"><table width="100%" border="1" cellSpacing= "0" cellPadding= "0" frame="void">
      <tr align="left">
        <td width="100" height="30">启票单位</td>
        <td width="600"><c:out value="${map.DEALER_NAME}"/></td>
        <td width="90">启票类型</td>
        <td width="297">移库单</td>
      </tr>
      <tr align="left">
        <td height="30">收车单位</td>
        <td><c:out value="${map.DEALER_NAME}"/></td>
        <td>联系人</td>
        <td><c:out value=""/></td>
      </tr>
      <tr align="left">
        <td height="30">收车地址</td>
        <td><c:out value="${map.ADDRESS}"/></td>
        <td>电话</td>
        <td><c:out value="${map.TEL}"/></td>
      </tr>
      <tr align="left">
        <td height="30">税号</td>
        <td><c:out value="${map.TAXES_NO}"/></td>
        <td>付款方式</td>
        <td></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td height="100%" colspan="6"><table width="100%" border="1" cellSpacing= "0" cellPadding= "0" frame="void">
      <tr>
        <td width="31%" height="30">物料编码(车型-状态-颜色)</td>
        <td width="10%">车龄</td>
        <td width="7%">数量</td>
        <td width="12%">单价</td>
        <td width="10%">金额</td>
        <td width="8%">折扣率</td>
        <td width="13%">折扣后单价</td>
        <td width="9%">折扣额</td>
      </tr>
      
<%
		List list = (List) request.getAttribute("list");
		if(list!=null){
			int ss = list.size();
			int recou = 0;
			int tocou = 0;
			int dicou = 0;
			for(int i=0;i<list.size();i++){
				Map ab = (Map)list.get(i);
				recou+=Double.parseDouble(String.valueOf(ab.get("AMOUNT")));
				tocou+=Double.parseDouble(String.valueOf(ab.get("TOTAL_PRICE")));
	%>
	   <tr align="left">
		<td height="30"><%=ab.get("MATERIAL_CODE") %>.<%=ab.get("COLOR_NAME") %></td>
		<td><%=ab.get("BATCH_NO")==null?"":ab.get("BATCH_NO") %></td>
		<td><%=ab.get("AMOUNT") %></td>
		<td align="right">
      		<script type="text/javascript">
      			document.write(amountFormatNew(<%=ab.get("SINGLE_PRICE") %>));
      		</script>
		</td>
		<td align="right">
      		<script type="text/javascript">
      			document.write(amountFormatNew(<%=ab.get("TOTAL_PRICE") %>));
      		</script>	
		</td>
		<td></td>
		<td align="right">
      		<script type="text/javascript">
      			document.write(amountFormatNew(<%=ab.get("DISCOUNT_S_PRICE") %>));
      		</script>
		</td>
		<td align="right">
      		<script type="text/javascript">
      			document.write(amountFormatNew(<%=ab.get("DISCOUNT_PRICE") %>));
      		</script>
		</td>
	   </tr>	
	<%
			}
			if(list.size()==ss){
	%>
			 <tr align="left">
			      <td nowrap="nowrap" align="center" height="30"><strong>合计：</strong></td>
			      <td nowrap="nowrap" >&nbsp;</td>
			      <td nowrap="nowrap">
			      	<strong>
			      		<%=recou %>
			      	</strong>
			      </td>
			      <td nowrap="nowrap" ></td>
			      <td align="right" nowrap="nowrap" >
			      	<strong>
			      	<%
			      		if(tocou==0){
			      	%>
			      	0.00
			      	<%
			      		}else{
			      	%>
			      		<script type="text/javascript">
			      			document.write(amountFormatNew(<%=tocou %>));
			      		</script>	
			      	<% 
			      		}
			      	%>
			      	</strong>
			      </td>
			      <td nowrap="nowrap" >&nbsp;</td>
			      <td align="right" nowrap="nowrap" ></td>
			      <td align="right" nowrap="nowrap" >
			      	<strong>
			      	<%
			      		if(dicou==0){
			      	%>
			      	<%
			      		}else{
			      	%>
			      		<script type="text/javascript">
			      			document.write(amountFormatNew(<%=dicou %>));
			      		</script>	
			      	<% 
			      		}
			      	%>
			      	</strong>
			      </td>
    		</tr>			
	<% 
			}
		}
	%>	
      
    </table></td>
  </tr>
  <tr>
    <td height="100%" colspan="6"><table width="100%" height="100%" border="1" cellSpacing= "0" cellPadding= "0" frame="void">
      <tr>
        <td width="16%" align="center" height="80">备注：</td>
        <td width="84%" align="left" ><c:out value="${map.ORDER_REMARK}"/></td>
      </tr>
    </table></td>
  </tr>
</table>
<table width="600" border="0" cellSpacing= "0" cellPadding= "0">
	<tr>
		<td align="left" width="230">经办人：</td>
		<td align="left" width="300">审核：</td>
	</tr>
</table>
<br><br><br><br><br>
<table id="tt">
	<tr>
		<td>
			<input type="button" id="btn" value="打印" onclick="doprint()"/>
		</td>
	</tr>
</table>
</div>
<script type="text/javascript">
	function doprint()
	{
		document.getElementById("tt").style.display = "none";
		window.print();
	}
</script>
</body>
</html>

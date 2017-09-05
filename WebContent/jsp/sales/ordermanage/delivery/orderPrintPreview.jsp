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
<style media=print> 
.Noprint{display:none;} 
.PageNext{page-break-after: always;} 
</style>
<title>启票通知单</title>
</head>
<%
	Map cmap = (Map)request.getAttribute("map"); 
%>
<script type="text/javascript">
<!--
	function getType() {
		var type = getItemValue('${map.DEALER_TYPE}') ;
		document.getElementById('typeA').innerHTML = type ;
	}
//-->
</script>
<body onload="getType();">
<br/>
<center><strong><font size="6">启票通知单</font></strong></center>
<div align="center">
<table width="700" border="0" cellSpacing= "0" cellPadding= "0">
	<tr>
		<td align="left" width="230">订单号：<c:out value="${map.ERP_ORDER}"/></td>
		<td align="left" width="300">发车地：<c:out value="${map.WAREHOUSE_NAME}"/></td>
		<td align="left" width="200">
			提货方式：<script type="text/javascript">
    					writeItemValue(<c:out value="${map.DELIVERY_TYPE}"/>)
    				</script> 
		</td>
		<td width="370" align="left">时间：<c:out value="${map.SDATE}"/></td>
	</tr>
</table>
<table width="700" border="1" cellSpacing= "0" cellPadding= "0">
  <tr align="left">
    <td width="230" height="20"><strong>运营大区</strong></td>
    <td width="300"><c:out value="${map.ORG_NAME}"/></td>
    <td width="109"><strong>产品渠道</strong></td>
    <td width="170"><c:out value="${map.AREA_NAME}"/></td>
    <td width="91"><strong>经理电话</strong></td>
    <td width="299">&nbsp;</td>
  </tr>
  <tr>
    <td height="100%" colspan="6"><table width="100%" border="1" cellSpacing= "0" cellPadding= "0" frame="void">
      <tr align="left">
        <td width="100" height="20"><strong>启票单位</strong></td>
        <td width="377"><c:out value="${map.DEALER_NAME1}"/></td>
        <td width="55"><strong>启票类型</strong></td>
        <td width="168"><span id="typeA"></span></td>
      </tr>
      <tr align="left">
        <td height="20"><strong>收车单位</strong></td>
        <td><c:out value="${map.DEALER_NAME2}"/></td>
        <td><strong>联系人</strong></td>
        <td>
        	<c:if test="${map.DELIVERY_TYPE==10291002}">
        		<c:out value="${map.LINK_MAN}"/>
        	</c:if>
        </td>
      </tr>
      <tr align="left">
        <td height="20"><strong>收车地址</strong></td>
        <td>
	        <c:if test="${map.DELIVERY_TYPE==10291002}">
	        	<c:out value="${map.ADDRESS}"/>
	        </c:if>
        </td>
        <td><strong><strong>电话</strong></td>
        <td>
        	<c:if test="${map.DELIVERY_TYPE==10291002}">
        		<c:out value="${map.TEL}"/>
        	</c:if>
        </td>
      </tr>
      <tr align="left">
        <td height="20"><strong>税号</strong></td>
        <td><c:out value="${map.TAXES_NO}"/></td>
        <td><strong>付款方式</strong></td>
        <td><c:out value="${map.ACCOUNT_NAME}"/></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td height="100%" colspan="6"><table width="100%" border="1" cellSpacing= "0" cellPadding= "0" frame="void">
      <tr>
        <td width="29%" height="20"><strong>物料编码(车型-状态-颜色)</strong></td>
        <td width="8%"><strong>车龄</strong></td>
        <td width="7%"><strong>数量</strong></td>
        <td width="10%"><strong>单价</strong></td>
        <td width="14%"><strong>金额</strong></td>
        <td width="8%"><strong>折扣率</strong></td>
        <td width="13%"><strong>折扣后单价</strong></td>
        <td width="11%"><strong>折扣额</strong></td>
      </tr>
      
<%
int k=0;
int j=1;
		List list = (List) request.getAttribute("list");
		if(list!=null){
			int ss = list.size();
			
			int recou = 0;
			double tocou = 0;
			double dicou = 0;
			for(int i=0;i<list.size();i++){
				Map ab = (Map)list.get(i);
				double driveMoney=Double.parseDouble(String.valueOf(ab.get("DISCOUNT_S_PRICE")));
				double rateMoney=Double.parseDouble(String.valueOf(ab.get("DISCOUNT_RATE")));
				double discountMoney=Double.parseDouble(String.valueOf(ab.get("DISCOUNT_PRICE")));
				recou += Double.parseDouble(String.valueOf(ab.get("DELIVERY_AMOUNT")));
				tocou += Double.parseDouble(String.valueOf(ab.get("TOTAL_PRICE")));
				dicou += Double.parseDouble(String.valueOf(ab.get("DISCOUNT_PRICE")));
				if(Integer.parseInt(String.valueOf(ab.get("DELIVERY_AMOUNT")))!= 0){
					k++;
					if(k<=8||k==10){
					
	%>
	   <tr align="left">
		<td height="20"><%=ab.get("MATERIAL_CODE") %>.<%=ab.get("COLOR_NAME") %></td>
		<td>&nbsp;<%=ab.get("BATCH_NO")==null?"":ab.get("BATCH_NO") %></td>
		<td>&nbsp;<%=ab.get("DELIVERY_AMOUNT") %></td>
		<td align="center">
      		<script type="text/javascript">
      			document.write(amountFormatNew(<%=ab.get("SINGLE_PRICE") %>));
      		</script>
		</td>
		<td align="right">
      		<script type="text/javascript">
      			document.write(amountFormatNew(<%=ab.get("TOTAL_PRICE") %>));
      		</script>	
		</td>
		<td>
		<%if(!ab.get("DISCOUNT_RATE").toString().equals("0")){ %>
		<%=ab.get("DISCOUNT_RATE") %>
		<%} %>
		</td>
		<td align="center">
		<%  if(discountMoney!=0){  %>
      		<script type="text/javascript">
      			document.write(amountFormatNew(<%=ab.get("DISCOUNT_S_PRICE") %>));
      		</script>
      		<%} %>
		</td>
		<td align="center">
		<% if(discountMoney!=0){ %>
      		<script type="text/javascript">
      			document.write(amountFormatNew(<%=ab.get("DISCOUNT_PRICE") %>));
      		</script>
      		<%} %>
		</td>
	   </tr>	
	<%
				} if(k>8){ 
				if((k/10==1&&k%10==0)||(k-10)/16>=1){%>
				
				</table>
				</td>
				</tr>
	<tr   class="PageNext"><td colspan="6"></td></tr>
	<tr>
		<td colspan="6">
				<table width="100%" border="1" cellSpacing= "0" cellPadding= "0" frame="void">
      <tr>
        <td width="29%"></td>
        <td width="8%"></td>
        <td width="7%"></td>
        <td width="10%"></td>
        <td width="14%"></td>
        <td width="8%"></td>
        <td width="13%"></td>
        <td width="11%"></td>
      </tr>

				<%} %>
				<%if((k==9&&k==ss)){%>
				
				</table>
				</td>
				</tr>
				<tr   class="PageNext"><td colspan="6"></td></tr>
	<tr>
		<td colspan="6">
				<table width="100%" border="1" cellSpacing= "0" cellPadding= "0" frame="void">
      <tr>
        <td width="29%"></td>
        <td width="8%"></td>
        <td width="7%"></td>
        <td width="10%"></td>
        <td width="14%"></td>
        <td width="8%"></td>
        <td width="13%"></td>
        <td width="11%"></td>
      </tr>

				<%} if(k!=10){ %>
				<tr align="left">
		<td height="20"><%=ab.get("MATERIAL_CODE") %>.<%=ab.get("COLOR_NAME") %></td>
		<td>&nbsp;<%=ab.get("BATCH_NO")==null?"":ab.get("BATCH_NO") %></td>
		<td>&nbsp;<%=ab.get("DELIVERY_AMOUNT") %></td>
		<td align="center">
      		<script type="text/javascript">
      			document.write(amountFormatNew(<%=ab.get("SINGLE_PRICE") %>));
      		</script>
		</td>
		<td align="right">
      		<script type="text/javascript">
      			document.write(amountFormatNew(<%=ab.get("TOTAL_PRICE") %>));
      		</script>	
		</td>
		<td>
		<%if(!ab.get("DISCOUNT_RATE").toString().equals("0")){ %>
		<%=ab.get("DISCOUNT_RATE") %>
		<%} %>
		</td>
		<td align="center">
		<%  if(discountMoney!=0){  %>
      		<script type="text/javascript">
      			document.write(amountFormatNew(<%=ab.get("DISCOUNT_S_PRICE") %>));
      		</script>
      		<%} %>
		</td>
		<td align="center">
		<% if(discountMoney!=0){ %>
      		<script type="text/javascript">
      			document.write(amountFormatNew(<%=ab.get("DISCOUNT_PRICE") %>));
      		</script>
      		<%} %>
		</td>
	   </tr>	
				<%
					
				}
				}
					}
				j++;
			}
	%>
			 <tr align="left">
			      <td nowrap="nowrap" align="center" height="20"><strong>合计：</strong></td>
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
	%>	
      
    </table></td>
  </tr>
  <tr>
    <td height="100%" colspan="6"><table width="100%" height="100%" border="1" cellSpacing= "0" cellPadding= "0" frame="void">
    	<tr>
        <td width="16%" align="center" height="30"><strong>备注：</strong></td>
        <td width="84%" align="left" ><c:out value="${map.ORDER_REMARK}"/><c:out value="${map.REFIT_REMARK}"/></td>
      </tr> 
    </table></td>
  </tr>
</table>
<table width="700" border="0" cellSpacing= "0" cellPadding= "0">
<tr>&nbsp;</tr >

	<tr>
		<td align="left" width="230"><font size="2">经办人：</font></td>
		<td align="left" width="300"><font size="2">审核：</font></td>
	</tr>
</table>
<br><br>
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

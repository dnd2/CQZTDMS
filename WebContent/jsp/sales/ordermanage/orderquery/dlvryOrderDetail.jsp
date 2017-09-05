<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.math.BigDecimal" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html>
<head>
<%
	Map cmap = (Map)request.getAttribute("map"); 
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>发运订单明细</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<BODY>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;发运订单明细</div>
<form  name="fm" id="fm" method="post">
<input type="hidden" name="reqId" value="<c:out value="${map.REQ_ID}"/>"/>
<input type="hidden" name="orderType" value="<c:out value="${map.ORDER_TYPE}"/>"/>
<input type="hidden" name="ver" value="<c:out value="${map.VER}"/>"/>
  <table class="table_query">
    <tr class="cssTable" >
    <td width="15%" align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">业务范围：</td>
    <td width="35%" align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
    	<c:out value="${map.AREA_NAME}"/>
    </td>
    <td width="14%" align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">大区：</td>
    <td width="36%" align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
		<c:out value="${map.ORG_NAME}"/>
	</td>
  <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">&nbsp;</td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">&nbsp;</td>
    </tr>
    <tr class="cssTable" >
      <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">订单号：</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
      	<c:out value="${map.ORDER_NO}"/>
      </td>
      <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">订单类型：</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
      	   <script type="text/javascript">
      	   	     writeItemValue(<c:out value="${map.ORDER_TYPE}"/>)
      	   </script>
      </td>
      <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">提报时间：</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
      	 <c:out value="${map.RAISE_DATE}"/>
      </td>
    </tr>
  
  <tr class="cssTable" >
  <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">启票单位：</td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input"><c:out value="${map.DNAME1}"/></td>
    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">采购单位：</td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
    	<c:out value="${map.DNAME2}"/>
    </td>
    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">&nbsp;</td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">&nbsp;</td>
    </tr>
    <tr class="cssTable" >
  <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">库存组织：</td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
    	<c:out value="${map.WAREHOUSE_NAME}"/>
    </td>
    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">发运方式：</td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
    	<script type="text/javascript">
    		writeItemValue(<c:out value="${map.DELIVERY_TYPE}"/>)
    	</script>
    </td>
    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">&nbsp;</td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">&nbsp;</td>
    </tr>
</table>
<br>
<table class="table_list" id="pi">
    <tr align="center" class="cssTable">
      <th width="3%" nowrap="nowrap">行号</th>
      <th width="13%" nowrap="nowrap">物料编号</th>
      <th width="17%" nowrap="nowrap">物料名称</th>
      <th width="6%" nowrap="nowrap">保留数量</th>
      <th width="7%" nowrap="nowrap">对应批次</th>
    </tr>

	<%
		List list = (List) request.getAttribute("list");
		if(list!=null){
			int ss = list.size();
			Double recou = new Double(0);
			Double tocou = new Double(0);
			Double dicou = new Double(0);
			for(int i=0;i<list.size();i++){
				Map ab = (Map)list.get(i);
				recou += new Double(String.valueOf(ab.get("DELIVERY_AMOUNT"))).doubleValue();
				tocou += new Double(String.valueOf(ab.get("TOTAL_PRICE"))).doubleValue();
				dicou += new Double(String.valueOf(ab.get("DISCOUNT_PRICE"))).doubleValue();
	%>
	   <tr class="<%if(i%2 != 0){ %>table_list_row1<%}else{ %>table_list_row2<%} %>">
	    <td><%=(i+1) %></td>
		<td><%=ab.get("MATERIAL_CODE") %></td>
		<td><%=ab.get("MATERIAL_NAME") %></td>
		<td><%=ab.get("DELIVERY_AMOUNT") %></td>
		<td><%=ab.get("BATCH_NO")==null?"":ab.get("BATCH_NO") %></td>
	   </tr>	
	<%
			}
			if(list.size()==ss){
	%>
			 <tr align="left" class="header">
			  	  <td nowrap="nowrap" >&nbsp;</td>
			      <td nowrap="nowrap" >&nbsp;</td>
			      <td nowrap="nowrap"  ><strong>合计：</strong></td>
			      <td nowrap="nowrap" align="right">
			      	<strong>
			      		<%=recou.intValue()%>
			      	</strong>
			      </td>
			      <td nowrap="nowrap" ></td>
    		</tr>			
	<% 
			}
		}
	%>	

</table>
<br>
  <table class="table_query">
  <%
  	   String delivery = String.valueOf(cmap.get("DELIVERY_TYPE"));
  	   if(delivery.equals(String.valueOf(Constant.TRANSPORT_TYPE_02))){
  %>
    <tr class="cssTable" >
      <td align="right" valign="top" nowrap="nowrap">收货地址：</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">
      	  <c:out value="${map.ADDRESS}"/>
      </td>
      <td align="right" valign="top" nowrap="nowrap">收车单位：</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_2col_input"><c:out value="${map.RECEIVE_ORG}"/></td>
    </tr>
    <tr class="cssTable" >
      <td align="right" valign="top" nowrap="nowrap">收货方：</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">
      	 <c:out value="${map.DNAME3}"/>
      </td>
      <td align="right" valign="top" nowrap="nowrap">&nbsp;</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">&nbsp;</td>
    </tr>
    <tr class="cssTable" >
      <td align="right" valign="top" nowrap="nowrap">联系人：</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">
      	 <c:out value="${map.LINK_MAN}"/>
      </td>
      <td align="right" valign="top" nowrap="nowrap">联系电话：</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">
      	 <c:out value="${map.TEL}"/>
      </td>
    </tr>
    <%
   		}
    %>
    <tr class="cssTable" >
      <td align="right" valign="top" nowrap="nowrap">代交车：</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">
      	 <c:out value="${map.IS_FLEET}"/>
      </td>
      <td align="right" valign="top" nowrap="nowrap">&nbsp;</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">&nbsp;</td>
    </tr>
    <%
    	String isf = String.valueOf(cmap.get("IS_FLEET"));
    	if(isf.equals("是")){
    %>
    <tr class="cssTable" >
      <td align="right" valign="top" nowrap="nowrap">代交地址：</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">
      	 <c:out value="${map.FLEET_ADDRESS}"/>
      </td>
      <td align="right" valign="top" nowrap="nowrap">代交集团客户：</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">
      	 <c:out value="${map.FLEET_NAME}"/>
      </td>
    </tr>
    <%
    	}
    %>
    <%
    	String orderType = String.valueOf(cmap.get("ORDER_TYPE"));
    	if(orderType.equals(String.valueOf(Constant.ORDER_TYPE_03))){
    %>
    <tr class="cssTable" >
      <td align="right" valign="top" nowrap="nowrap">改装说明：</td>
      <td colspan="3" align="left" valign="top" nowrap="nowrap" class="table_info_2col_input" title="<%=cmap.get("REFIT_REMARK")==null?"":cmap.get("REFIT_REMARK")%>">&nbsp;
          		<%
          			if(cmap.get("REFIT_REMARK")!=null){
          				if(String.valueOf(cmap.get("REFIT_REMARK")).length()<=10){
          		%>
          			<%=cmap.get("REFIT_REMARK")%>
          		<%
          				}
          		%>
          		<%
          				if(String.valueOf(cmap.get("REFIT_REMARK")).length()>10){
          					String s = String.valueOf(cmap.get("REFIT_REMARK"));
          					//s = s.substring(0,9);
          		%>
          			<%=s%>
          		<%
          				}
          			}
          		%>
        </td>
    </tr>
    <%
    	}
    %>
    <tr class="cssTable" >
      <td width="15%" align="right" valign="top" nowrap="nowrap">备注说明：</td>
      	<td colspan="3" align="left" valign="top" nowrap="nowrap" class="table_info_2col_input" title="<%=cmap.get("ORDER_REMARK")==null?"":cmap.get("ORDER_REMARK")%>">&nbsp;
      		<%
      			if(cmap.get("ORDER_REMARK")!=null){
      				if(String.valueOf(cmap.get("ORDER_REMARK")).length()<=10){
      		%>
      			<%=cmap.get("ORDER_REMARK")%>
      		<%
      				}
      		%>
      		<%
      				if(String.valueOf(cmap.get("ORDER_REMARK")).length()>10){
      					String d = String.valueOf(cmap.get("ORDER_REMARK"));
      					//d = d.substring(0,9);
      		%>
      			<%=d%>
      		<%
      				}
      			}
      		%>
      	</td>
    </tr>
  </table>
  <p>&nbsp;</p>	
  <table class="table_query">
    <tr class="cssTable" align="center" >
      <td>
      	  <input class="normal_btn" name="add232" type="button" onclick="javasrcipt:history.go(-1)" value ="返回" />
      </td>
    </tr>
  </table>
  <br>
</form>
<script language="javascript">
	function printDO()
	{
		var reqId = document.getElementById("reqId").value;
		var orderType = document.getElementById("orderType").value;
		window.open("<%=request.getContextPath()%>/sales/ordermanage/delivery/OrderCommandQuery/commandQueryPrint.do?reqId="+reqId+"&orderType="+orderType);
	}
</script>
</body>
</html>

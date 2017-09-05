<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html>
<head>
<%
	Map cmap = (Map)request.getAttribute("map"); 
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单同步确认</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<BODY>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;订单同步确认</div>
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
    <td width="14%" align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">大区/事业部：</td>
    <td width="36%" align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
		<c:out value="${map.ORG_NAME}"/>
	</td>
    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">库存组织：</td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
    	<c:out value="${map.WAREHOUSE_NAME}"/>
    </td>
    </tr>
    <tr class="cssTable" >
      <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">销售订单号：</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
      	<c:out value="${map.ORDER_NO}"/>
      </td>
      <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">订单类型：</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
      	   <script type="text/javascript">
      	   	     writeItemValue(<c:out value="${map.ORDER_TYPE}"/>)
      	   </script>
      </td>
      <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">订单周度：</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
      	 <c:out value="${map.ORDER_WEEK}"/>
      </td>
    </tr>
  <tr class="cssTable" >
    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">发运方式：</td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
    	<script type="text/javascript">
    		writeItemValue(<c:out value="${map.DELIVERY_TYPE}"/>)
    	</script>
    </td>
    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">&nbsp;</td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">&nbsp;</td>
    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">&nbsp;</td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">&nbsp;</td>
    </tr>
  <tr class="cssTable" >
    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">启票单位：</td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
    	<c:out value="${map.DNAME1}"/>
    </td>
    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">采购单位：</td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
    	<c:out value="${map.DNAME2}"/>
    </td>
    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">&nbsp;</td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">&nbsp;</td>
  </tr>
</table>
<br>
<table class="table_list" id="table1" >
    <tr align="center" class="cssTable" >
      <th width="13%" nowrap="nowrap">物料编号</th>
      <th width="17%" nowrap="nowrap">物料名称</th>
      <th width="6%" nowrap="nowrap">申请数量</th>
      <th width="6%" nowrap="nowrap">保留数量</th>
      <th width="7%" nowrap="nowrap">对应批次</th>
    </tr>

	<%
		List list = (List) request.getAttribute("list");
		if(list!=null){
			int ss = list.size();
			int recou = 0;
			double tocou = 0;
			double dicou = 0;
			for(int i=0;i<list.size();i++){
				Map ab = (Map)list.get(i);
				recou += Double.parseDouble(String.valueOf(ab.get("RESERVE_AMOUNT")));
				tocou += Double.parseDouble(String.valueOf(ab.get("TOTAL_PRICE")));
				dicou += Double.parseDouble(String.valueOf(ab.get("DISCOUNT_PRICE")));
	%>
	   <tr class="<%if(i%2 != 0){ %>table_list_row1<%}else{ %>table_list_row2<%} %>">
		<td><%=ab.get("MATERIAL_CODE") %></td>
		<td><%=ab.get("MATERIAL_NAME") %></td>
		<td><%=ab.get("REQ_AMOUNT") %></td>
		<td><%=ab.get("RESERVE_AMOUNT") %></td>
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
			      	    <%=recou %>
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
      <td align="right" valign="top" nowrap="nowrap">收货单位：</td>
      <td width="35%" align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">
      	  <c:out value="${map.RECEIVE_ORG}"/>
      </td>
      <td width="14%" align="right" valign="top" nowrap="nowrap">&nbsp;</td>
      <td width="36%" align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">&nbsp;</td>
    </tr>
    <tr class="cssTable" >
      <td align="right" valign="top" nowrap="nowrap">收货地址：</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">
      	  <c:out value="${map.ADDRESS}"/>
      </td>
      <td align="right" valign="top" nowrap="nowrap">&nbsp;</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">&nbsp;</td>
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
          					s = s.substring(0,9);
          		%>
          			<%=s%>...
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
      <td width="15%" align="right" valign="top" nowrap="nowrap">付款信息备注：</td>
      	<td colspan="3" align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">${map.PAY_REMARK}</td>
    </tr>
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
      					d = d.substring(0,9);
      		%>
      			<%=d%>...
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
      <td >
      		<input class="normal_btn" id="add2"  name="add2" type="button" onclick="confir()" value ="同步" />
 			<input class="normal_btn" id="add"  name="add" type="button" onclick="confir2()" value ="作废" />
      		<input class="normal_btn"  name="add232" type="button" onclick="javasrcipt:history.go(-1)" value ="返回" />
      </td>
    </tr>
  </table>
  <br>
</form>
<script language="javascript">

	function confir()
	{
		var b = new Object();
		b = document.getElementById("add2");
		var a = new Object();
		a = document.getElementById("add");
		a.disabled=true;
		b.disabled=true;
		if(confirm("确认同意吗？")){
			checkOK();
		}else{
			a.disabled=false;
			b.disabled=false;
		}
	}
	
	function checkOK()
	{
		makeNomalFormCall("<%=request.getContextPath()%>/sales/ordermanage/delivery/OrderDeliveryCommand/dealerDeliverySubmit.json",checkBack,'fm','queryBtn');
	}
	
	function confir2()
    {
		var b = new Object();
		b = document.getElementById("add2");
		var a = new Object();
		a = document.getElementById("add");
		a.disabled=true;
		b.disabled=true;
		if(confirm("确认取消吗？")){
			checkNO();
		}else{
			a.disabled=false;
			b.disabled=false;
		}
	}
	
	function checkNO()
	{
		makeNomalFormCall("<%=request.getContextPath()%>/sales/ordermanage/delivery/OrderDeliveryCommand/dealerDeliverySubBack.json",checkBack,'fm','queryBtn');
	}
	
	function checkBack(json)
	{
		if(json.returnValue == '1')
		{
			window.parent.MyAlert("操作成功！");
			$('fm').action= "<%=request.getContextPath()%>/sales/ordermanage/delivery/OrderDeliveryCommand/dealerDeliveryInit.do"
			$('fm').submit();
		}
		esle
		{
			MyAlert("操作失败！请联系管理员！");
		}
	}

</script>
</body>
</html>

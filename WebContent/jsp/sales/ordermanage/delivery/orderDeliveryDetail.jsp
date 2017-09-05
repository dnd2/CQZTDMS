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
<title>发运指令下达</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<BODY>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;发运指令下达</div>
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
      <!--<td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">订单周度：</td>
      <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
      	 <c:out value="${map.ORDER_WEEK}"/>
      </td>
      --><td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">提报时间：</td>
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
    </tr><!--
  <tr class="cssTable" >
    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter"></td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
    </td>
    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">&nbsp;</td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">&nbsp;</td>
    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">&nbsp;</td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">&nbsp;</td>
  </tr>
  --><tr class="cssTable" >
    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">资金类型：</td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
    	<c:out value="${map.ACCOUNT_NAME}"/>
    </td>
    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">余额：</td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
    	<c:out value="${map.AVAILABLE_AMOUNT}"/>
    </td>
    <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">可用折让：</td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
    	<c:forEach items="${discountList}" var="po">
    		<script>document.write(amountFormatNew(${po.AVAILABLE_AMOUNT}));</script>
    	</c:forEach>
    </td>
  </tr>
    <tr class="cssTable" >
     <td align="right" valign="top" nowrap="nowrap" class="table_info_3col_label_6Letter">库存组织：</td>
    <td align="left" valign="top" nowrap="nowrap" class="table_info_3col_input">
    <input name="wareHouseId" id="wareHouseId" type="hidden" value="${map.WAREHOUSE_NAME}"></input>
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
      <th width="6%" nowrap="nowrap">单价</th>
      <th width="7%" nowrap="nowrap">金额</th>
      <th width="6%" nowrap="nowrap">折扣率 %</th>
      <th width="7%" nowrap="nowrap">折扣后单价</th>
      <th width="7%" nowrap="nowrap">折扣额</th>
      <th width="6%" nowrap="nowrap" title="可用库存 = 当前库存 - 未出库保留资源数">可用库存</th>
      <th width="6%" nowrap="nowrap" title="为满足常规订单 = 常规订单审核数 - 已保留资源数">未满足<br/>常规订单</th>
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
				recou += Double.parseDouble(String.valueOf(ab.get("DELIVERY_AMOUNT")));
				tocou += Double.parseDouble(String.valueOf(ab.get("TOTAL_PRICE")));
				dicou += Double.parseDouble(String.valueOf(ab.get("DISCOUNT_PRICE")));
	%>
	   <tr class="<%if(i%2 != 0){ %>table_list_row1<%}else{ %>table_list_row2<%} %>">
	   
	    <td><%=(i+1) %></td>
		<td><%=ab.get("MATERIAL_CODE") %></td>
		<td><%=ab.get("MATERIAL_NAME") %></td>
		<td>
		<%=ab.get("DELIVERY_AMOUNT") %>
		<a href="#" onclick="patchNoSelect('<%=ab.get("MATERIAL_ID")%>','<%=ab.get("DELIVERY_AMOUNT") %>','<%=ab.get("BATCH_NO")%>');">[查看]</a></td>
		<td><% if(ab.get("BATCH_NO")!=null){%>
		<%=ab.get("BATCH_NO")%>
		<%}%>
		<input type="hidden" name="reserveAmount" id="reserveAmount" value="<%=ab.get("DELIVERY_AMOUNT") %>"></input>
		<input type="hidden" name="REQ_AMOUNT" id="REQ_AMOUNT" value="<%=ab.get("REQ_AMOUNT")%>"></input>
		<input type="hidden" name="special_batch_no" id="special_batch_no" value="<%=ab.get("special_batch_no")%>"></input> 
		<input type="hidden" name="BATCH_NO" id="BATCH_NO" value="<%=ab.get("BATCH_NO")%>"></input> 
		</td>
		<td>
      		<script type="text/javascript">
      			document.write(amountFormatNew(<%=ab.get("SINGLE_PRICE") %>));
      		</script>
		</td>
		<td>
      		<script type="text/javascript">
      			document.write(amountFormatNew(<%=ab.get("TOTAL_PRICE") %>));
      		</script>	
		</td>
		<td><%=ab.get("DISCOUNT_RATE") %></td>
		<td>
      		<script type="text/javascript">
      			document.write(amountFormatNew(<%=ab.get("DISCOUNT_S_PRICE") %>));
      		</script>
		</td>
		<td>
      		<script type="text/javascript">
      			document.write(amountFormatNew(<%=ab.get("DISCOUNT_PRICE") %>));
      		</script>
		</td>
		<td><%=ab.get("AVA_STOCK") %></td>
		<td><%=ab.get("GENERAL_AMOUNT") %></td>
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
			      <td align="right" nowrap="nowrap" >&nbsp;</td>
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
			      	0.00
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
			      <td align="right" nowrap="nowrap" ></td>
			      <td align="right" nowrap="nowrap" ></td>
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
    <!--<tr class="cssTable" >
      <td align="right" valign="top" nowrap="nowrap">收货单位：</td>
      <td width="35%" align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">
      	  <c:out value="${map.RECEIVE_ORG}"/>
      </td>
      <td width="14%" align="right" valign="top" nowrap="nowrap">&nbsp;</td>
      <td width="36%" align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">&nbsp;</td>
    </tr>
    --><tr class="cssTable" >
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
      <td align="right" valign="top" nowrap="nowrap">价格类型：</td>
      <td colspan="3" align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">
      	 <c:out value="${map.PRICE_DESC}"/>
      </td>
    </tr>
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
      					//d = d.substring(0,9);
      		%>
      			<%=d%>
      		<%
      				}
      			}
      		%>
      	</td>
    </tr>
	<tr class="cssTable">
		<td align="right">审核描述：</td>
		<td align="left" colspan="3"><textarea name="checkRemark" id="checkRemark" cols="30" rows="3"></textarea></td>
	</tr>
  </table>
  <p>&nbsp;</p>	
  <table class="table_query">
    <tr class="cssTable" align="center" >
      <td>
      		<input class="normal_btn" id="id1" name="add2" type="button" onclick="confir()" value ="审核通过" />
			<input class="normal_btn" id="id2" name="add" type="button" onclick="confir2()" value ="审核驳回" />
      		<input class="normal_btn"  name="add232" type="button" onclick="javasrcipt:history.go(-1)" value ="返回" />
      </td>
    </tr>
  </table>
  <br>
</form>
<script language="javascript">

//批次号选择
function patchNoSelect(materalId,acount,specialMybatchNo){
	var wareHouseId = '${map.WAREHOUSE_ID}'//库存ID
	var batchNo = document.getElementById("BATCH_NO").value; //批次号
	var reserveAmount = document.getElementById("reserveAmount").value;//保留数量
	var orderType = '${map.ORDER_TYPE}';//订单类型
	var specialBatchNo = document.getElementById("special_batch_no").value;
	var initHouseId = '${map.WAREHOUSE_ID}'
	var initNo = (initHouseId != "" && initHouseId == wareHouseId) ? document.getElementById("BATCH_NO").value : "";
	var reqAmount = document.getElementById("REQ_AMOUNT").value;
	OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/delivery/OrderDeliveryCommand/patchNoSelect.do?wareHouseId='+wareHouseId+'&materalId='+materalId+'&batchNo='+specialMybatchNo+'&amount='+acount+'&orderType='+orderType+'&specialBatchNo='+specialBatchNo+'&initNo='+initNo+'&reqAmount='+reqAmount,700,500);
}
	function confir()
	{

		var tab = document.getElementById("pi");
		if(tab.rows.length>1)
		{
			var cc;
	 		for(var i=1;i<tab.rows.length-1;i++)
	 		{
	 			var aa = tab.rows[i].children(10).innerHTML;
	 			var bb = tab.rows[i].children(11).innerHTML;

	 			if(parseInt(aa)<parseInt(bb)){
	 				if(cc!=null)
	 				{
	 					cc = cc +  "," + tab.rows[i].children(0).innerHTML;
	 				}
	 				else
	 				{
	 					cc = tab.rows[i].children(0).innerHTML;
	 				}
	 				MyConfirm("通过后，第" + cc + "行 目前的可用库存已小于常规订单的为满足量，您要确认吗？", checkOK);
	 			}
	 			else
	 			{
	 				MyConfirm("确认通过？", checkOK);
	 			}
	 		}
		}
	}
	
	function checkOK()
	{
		makeNomalFormCall("<%=request.getContextPath()%>/sales/ordermanage/delivery/OrderDeliveryCommand/deliverySubmit.json",checkBack,'fm','queryBtn');
		document.getElementById("id1").disabled = "disabled";
		document.getElementById("id2").disabled = "disabled";
	}
	
	function confir2()
    {
		MyConfirm("确认驳回吗？", checkNO);
	}
	
	function checkNO()
	{
		makeNomalFormCall("<%=request.getContextPath()%>/sales/ordermanage/delivery/OrderDeliveryCommand/deliverySubBack.json",checkBack,'fm','queryBtn');
		document.getElementById("id1").disabled = "disabled";
		document.getElementById("id2").disabled = "disabled";
	}
	
	function checkBack(json)
	{
		if(json.returnValue == '1')
		{
			window.parent.MyAlert("操作成功！");
			$('fm').action= "<%=request.getContextPath()%>/sales/ordermanage/delivery/OrderDeliveryCommand/deliveryQueryInit.do"
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

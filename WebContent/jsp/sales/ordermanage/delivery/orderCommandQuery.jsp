<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>发运指令查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
</script>
</head>
<body onload="genLocSel('txt1','','','','','');">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单发运 &gt;发运指令查询</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td align="right">发运日期：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="t1" name="startDate" datatype="1,is_date,10" group="t1,t2"  value="${dateStr}" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't1', false);" value="&nbsp;" />&nbsp;至&nbsp;
				<input class="short_txt"  type="text" id="t2" name="endDate" datatype="1,is_date,10" group="t1,t2" value="${dateStr}" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't2', false);" value="&nbsp;" />
			</td>
			<td align="right">业务范围：</td>
			<td align="left">
				<select name="areaId" id="areaId" class="short_sel">
					<option value="">-请选择-</option>
					<c:forEach items="${areaBusList}" var="list">
						<option value="${list.AREA_ID}"><c:out value="${list.AREA_NAME}"/></option>
					</c:forEach>
				</select><input type="hidden" name="area" id="area"/>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">订单类型：</TD>
			<td align="left">
				<label>
					<script type="text/javascript">
						genSelBoxExp("orderType",<%=Constant.ORDER_TYPE%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
			<td align="right">运送方式：</TD>
			<td>
				<label>
					<script type="text/javascript">
						genSelBoxExp("transportType",<%=Constant.TRANSPORT_TYPE%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
			<td></td>
		</tr>
		<tr>
			<td width="13%" align="right">发运单号：</TD>
			<td width="35%"><input type="text" name="deliveryNo" class="middle_txt" value=""/></TD>
			<td align="right">发运状态：</td>
			<td align="left">
				<label>
					<script type="text/javascript">
						genSelBoxExp("deliveryStatus",<%=Constant.DELIVERY_STATUS%>,"",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
			<td></td>
		</tr>
		<tr>
			<td width="13%" align="right">开票经销商：</TD>
			<td width="35%">
				<input type="text" class="middle_txt" name="billingOrgCode" size="15" value="" readonly="readonly" id="billingOrgCode"/>
				<input name="button3" type="button" class="mini_btn" onclick="showOrgDealer('billingOrgCode','','true', '${orgId}')" value="..." />
           		<input class="cssbutton" type="button" value="清空" onclick="clrTxt('billingOrgCode');"/>
			</TD>
			<td align="right">采购经销商：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="orderOrgCode" size="15" value="" readonly="readonly" id="orderOrgCode"/>
				<input name="button3" type="button" class="mini_btn" onclick="showOrgDealer('orderOrgCode', '', 'true', '', 'true')" value="..." />
           		<input class="cssbutton" type="button" value="清空" onclick="clrTxt('orderOrgCode');"/>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right"  width="13%">仓库：</TD>
        <td class="table_query_2Col_input">
        <select name="endName" id="endName" class="short_sel">
          <option value="">-请选择-</option>
          <c:if test="${list_aim!=null}">
          <c:forEach items="${list_aim}" var="list">
            <option value="${list.WAREHOUSE_ID }">${list.WAREHOUSE_NAME }</option>
          </c:forEach>
        </c:if>
        </select>
        </td>
			<td  align="right">ERP订单号：
			</td>
			<TD align="left"><input type="text" value="" name="erpCode"  class="middle_txt"></input></TD>
			<td align="left">
			</td>
		</tr>
		<tr>
			<td  align="right">事业部：</td>
			<td align="left">
				<select id="orgId" name="orgId" class="short_sel">
				<option value="">--请选择--</option>
					<c:forEach items="${orgList}" var="orgList">
						<option value="${orgList.ORG_ID }">${orgList.ORG_NAME }</option>
					</c:forEach>
				</select>
			</td>
			<td align="right">省份：</td>
			<td align="left">
					<select class="short_sel" id="txt1" name="downtown"></select>
			</td>
		</tr>
		<tr>
			<td align="right"  width="13%">销售订单号：</TD>
	        <td class="table_query_2Col_input"><input type="text" value="" name="orderNo"  class="middle_txt"></td>
			<td align="right"></td>
			<TD>
				<input name="button2" id="queryBtn" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">
				<input name="button2" id="queryBtn" type=button class="cssbutton" onClick="modelLoad();" value="下载">
			</TD>
			<td align="left">
			</td>
		</tr>
	</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/ordermanage/delivery/OrderCommandQuery/commandQuery.json";
	var title = null;
	var columns = [
				{header: "开票方代码",dataIndex: 'CODE',align:'center'},
				{header: "开票方名称",dataIndex: 'TTNAME',align:'center'},
				{header: "开票方名称",dataIndex: 'TTNAME',align:'center'},
				{header: "开票方系统开通时间",dataIndex: 'CREATE_DATE',align:'center',renderer:formatDate},
				{header: "采购方代码",dataIndex: 'ORDER_ORG_CODE',align:'center'},
				{header: "采购方名称",dataIndex: 'ORDER_ORG_NAME',align:'center'},
				{header: "采购方系统开通时间",dataIndex: 'ORDER_CREATE_DATE',align:'center',renderer:formatDate},
				//{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center',renderer:getItemValue},
				{header: "销售订单号", dataIndex: 'ORDER_NO', align:'center'},
				//{header: "订单周度", dataIndex: 'ORDER_WEEK', align:'center',renderer:myText},
				{header: "发运单号", dataIndex: 'DELIVERY_NO', align:'center'},
				//{header: "订做车批次号", dataIndex: 'SPECIAL_BATCH_NO', align:'center'},
				//{header: "计划数量", dataIndex: 'DELIVERY_AMOUNT', align:'center'},
				{header: "发运数量", dataIndex: 'DELIVERY_AMOUNT', align:'center'},
				{header: "发票号", dataIndex: 'INVOICE_NO,', align:'center',renderer:myGetPiao},
				{header: "状态", dataIndex: 'DELIVERY_STATUS', align:'center',renderer:getItemValue},
				{header: "失败原因", dataIndex: 'LOSE_REASON', align:'center',renderer:myGetReson},
				{header: "DFS回复状态", dataIndex: 'RETURN_FLAG', align:'center',renderer:getItemValue},
				{header: "ERP订单号", dataIndex: 'ERP_ORDER', align:'center'},
				{header: "总价", dataIndex: 'TOTAL_PRICE', align:'center',renderer:amountFormat},
				{header: "运送方式", dataIndex: 'DELIVERY_TYPE', align:'center',renderer:getItemValue},
				{header: "运送地址", dataIndex: 'ADDRESS', align:'center'},
				{header: "发运指令下达时间", dataIndex: 'DELIVERY_DATE', align:'center'},
				{header: "操作",sortable: false, dataIndex: 'REQ_ID', align:'center',renderer:myMatchDetail}
		      ];

	function modelLoad(){
		$('fm').action= "<%=contextPath%>/sales/ordermanage/delivery/OrderCommandQuery/commandDown.json";
		$('fm').submit();
	}
	function formatDate(value,meta,record){
		if(value!=null && value!=""){
			return value.substring(0,7).replace('-','');
		}
		else{
			return "";
		}
		
	}

	//超链接设置
	function myText(value,meta,record){
		return String.format(record.data.ORDER_YEAR+"."+value);
	}
	//设置链接
	function myDetail(value,meta,record){
	    return String.format("<a href='#' onclick='orderDetailInfo(\""+ value +"\")'>"+value+"</a>");
	}
	//订单明细链接
	function orderDetailInfo(value){ 
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderdetail/OrderDetailInfoQuery/orderDetailInfoQuery.do?&orderNo='+value,700,500);
	}
	//设置链接
	function myMatchDetail(value,meta,record){
	    return String.format("<a href='#' onclick='infoquery("+ value +","+ record.data.ORDER_TYPE + ")'>[明细]</a>");
	}
	//配车明细链接
	function infoquery(val1,val2){ 
		$('fm').action= "<%=contextPath%>/sales/ordermanage/delivery/OrderCommandQuery/commandQueryInfo.do?reqId="+val1+"&orderType="+val2;
		$('fm').submit();
	}
	function myGetPiao(value,meta,record){
		if(record.data.INVOICE_NO!=null&&record.data.INVOICE_NO!=""){
	    	return String.format("<a href='#' onclick='gotoPiao(\""+ record.data.DELIVERY_NO +"\")'>"+record.data.INVOICE_NO+"</a>");
	        }
        return "";
	}
    function myGetReson(value,meta,record){
        if(record.data.LOSE_REASON!=null&&record.data.LOSE_REASON!=""){
    	return String.format("<a href='#' onclick='gotoreson(\""+ record.data.DELIVERY_NO +"\")'>"+record.data.LOSE_REASON+"</a>");
        }
        return "";
     }
    function gotoPiao(value){
    		OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/delivery/OrderCommandQuery/getMyPiao.do?deliverId='+value,700,500);
        }
    function gotoreson(value){
    		OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/delivery/OrderCommandQuery/getMyReson.do?deliverId='+value,700,500);
        }
	//设置超链接  begin
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
</script>
<!--页面列表 end -->
</body>
</html>
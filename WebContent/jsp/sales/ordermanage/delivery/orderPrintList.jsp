<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>发运指令查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit()
	{
   		loadcalendar();  //初始化时间控件
   		__extQuery__(1);
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单发运 &gt;发运指令查询</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
<div>
	<table  style="width:1000px;" class="table_query">
		<tr>
			<td width="13%" align="right">发运单号：</TD>
			<td width="35%"><input type="text" name="deliveryNo" id="deliveryNo" class="middle_txt" value=""/></TD>
			<td align="right">打印状态：</td>
			<td align="left">
				<select class="short_sel" id="printFlag" name="printFlag">
					<option value="<%=Constant.PRINT_FLAG_00 %>" selected="selected"><%=Constant.PRINT_FLAG_00_VALUE %></option>
					<option value="<%=Constant.PRINT_FLAG_01 %>"><%=Constant.PRINT_FLAG_01_VALUE %></option>
				</select>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">发运日期：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="t1" name="startDate" datatype="1,is_date,10" group="t1,t2"  value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't1', false);" value="&nbsp;" />&nbsp;至&nbsp;
				<input class="short_txt"  type="text" id="t2" name="endDate" datatype="1,is_date,10" group="t1,t2" value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't2', false);" value="&nbsp;" />
			</td>
			<td align="right">大区选择：</td>
			<td align="left">
				<select class="short_sel" id="orgId" name="orgId">
					<option value="">-请选择-</option>
				<c:forEach items="${list}" var="ls">	
					<option value="${ls.ORG_ID}">${ls.ORG_NAME}</option>
				</c:forEach>
				</select>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">业务范围：</td>
			<td align="left">
				<select class="short_sel" id="areaId" name="areaId">
					<option value="">-请选择-</option>
				<c:forEach items="${listA}" var="lsA">	
					<option value="${lsA.AREA_ID}">${lsA.AREA_NAME}</option>
				</c:forEach>
				</select>
			</td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td align="right"></td>
			<td>
			</td>
			<TD>&nbsp;</TD>
			<td align="left">
				<input name="button2" id="queryBtn" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">
			</td>
		</tr>
	</table>
	</div>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
</form>
  <form name="form1" style="display:none">
   <table style="width:1000px;" class="table_list" id="table1" >
  	  <tr>
  	  	<th align="center">
    		<input class="normal_btn" type="button" value="打印完成" onclick="printClose()">
       </th>
  	  </tr>
   </table>
  </form>
<!--页面列表 begin -->
<script type="text/javascript" >
	document.form1.style.display = "none";
	
	var HIDDEN_ARRAY_IDS=['form1'];

	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/ordermanage/delivery/OrderCommandQuery/commandQueryPrint.json";
	var title = null;
	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"deliveryIds\")' />",sortable: false,dataIndex: 'DELIVERY_ID',renderer:myCheckBox},
				{header: "操作",sortable: false, dataIndex: 'REQ_ID', align:'center',renderer:myMatchDetail},
				{header: "ERP订单号",dataIndex: 'ERP_ORDER',align:'center'},
				{header: "开票方代码",dataIndex: 'CODE',align:'center'},
				{header: "开票方名称",dataIndex: 'TTNAME',align:'center'},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center',renderer:getItemValue},
				{header: "销售订单号", dataIndex: 'ORDER_NO', align:'center'},
				//{header: "订单周度", dataIndex: 'ORDER_WEEK', align:'center',renderer:myText},
				{header: "发运单号", dataIndex: 'DELIVERY_NO', align:'center'},
				{header: "计划数量", dataIndex: 'DELIVERY_AMOUNT', align:'center'},
				{header: "总价", dataIndex: 'TOTAL_PRICE', align:'center'},
				{header: "运送方式", dataIndex: 'DELIVERY_TYPE', align:'center',renderer:getItemValue},
				{header: "运送地址", dataIndex: 'ADDRESS', align:'center'},
				{header: "发运时间", dataIndex: 'DELIVERY_DATE', align:'center'}
				
		      ];
      
	
	//全选checkbox
	function myCheckBox(value,metaDate,record)
	{
		var chI = record.data.REQ_ID ;
		return String.format("<input type='checkbox' name='deliveryIds' id='" + chI + "' value='" + value + "'/>");
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
	    return String.format("<a href='#' onclick='infoquery("+ value +","+ record.data.ORDER_TYPE + ")'>[打印]</a>");
	}
	//配车明细链接
	function infoquery(val1,val2){ 
		document.getElementById(val1).checked = 'checked' ;
		window.open("<%=contextPath%>/sales/ordermanage/delivery/OrderCommandQuery/commandPrintInfo.do?reqId="+val1+"&orderType="+val2);
	}
	//申报提醒
	function printClose()
	{
		var chk = document.getElementsByName("deliveryIds");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++)
		{        
			if(chk[i].checked)
			{            
				str = chk[i].value+","+str; 
				cnt++;
			}
		}
        if(cnt==0)
        {
             MyAlert("请选择要打印完成的工单！");
             return;
        }
		MyConfirm("确认提交？",putForword);
	}
	
	//提报申请
	function putForword()
	{
		makeNomalFormCall("<%=contextPath%>/sales/ordermanage/delivery/OrderCommandQuery/commandPrintOver.json",showForwordValue,'fm','queryBtn'); 
	}
	
	//提报回调函数
	function showForwordValue(json)
	{
		if(json.returnValue == '1')
		{
			MyAlert("操作成功！");
			__extQuery__(1);
		}else
		{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	
</script>
<!--页面列表 end -->
</body>
</html>
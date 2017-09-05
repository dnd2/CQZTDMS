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
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 车厂库存管理 &gt;移库单打印</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td width="13%" align="right">移库单号：</TD>
			<td width="35%"><input type="text" name="orderNo" id="orderNo" class="middle_txt" value=""/></TD>
			<td align="right">打印状态：</td>
			<td align="left">
				<select class="short_sel" id="printFlag" name="printFlag">
					<option value="<%=Constant.PRINT_FLAG_00 %>" selected="selected"><%=Constant.PRINT_FLAG_00_VALUE %></option>
					<option value="<%=Constant.PRINT_FLAG_01 %>"><%=Constant.PRINT_FLAG_01_VALUE %></option>
				</select>
			</td>
		</tr>
		<tr>
			<td align="right">移库日期：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="t1" name="startDate" datatype="1,is_date,10" group="t1,t2"  value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't1', false);" value="&nbsp;" />&nbsp;至&nbsp;
				<input class="short_txt"  type="text" id="t2" name="endDate" datatype="1,is_date,10" group="t1,t2" value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't2', false);" value="&nbsp;" />
			</td>
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
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
</form>
  <form name="form1" style="display:none">
   <table class="table_list" id="table1" >
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
	var url = "<%=contextPath%>/sales/oemstorage/OemStoragePrintList/commandQueryPrint.json";
	var title = null;
	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"deliveryIds\")' />", width:'8%',sortable: false,dataIndex: 'STO_ID',renderer:myCheckBox},
				{header: "ERP订单号",dataIndex: 'ERP_ORDER_NO',align:'center'},
				{header: "移库单号", dataIndex: 'ORDER_NO', align:'center'},
				{header: "目的仓库", dataIndex: 'TO_NAME', align:'center'},
				{header: "发运时间", dataIndex: 'STO_DATE', align:'center'},
				{header: "操作",sortable: false, dataIndex: 'STO_ID', align:'center',renderer:myMatchDetail}
		      ];
 
	
	//全选checkbox
	function myCheckBox(value,metaDate,record)
	{
		return String.format("<input type='checkbox' name='deliveryIds' value='" + value + "'/>");
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
	    return String.format("<a href='#' onclick='infoquery("+ value+")'>[打印]</a>");
	}
	//配车明细链接
	function infoquery(val1){ 
		window.open("<%=contextPath%>/sales/oemstorage/OemStoragePrintList/commandPrintInfo.do?reqId="+val1);
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
             MyAlert("请选择要打印完成的移库单！");
             return;
        }
		MyConfirm("确认提交？",putForword);
	}
	
	//提报申请
	function putForword()
	{
		makeNomalFormCall("<%=contextPath%>/sales/oemstorage/OemStoragePrintList/commandPrintOver.json",showForwordValue,'fm','queryBtn'); 
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
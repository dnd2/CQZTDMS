<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>生产商再索赔结算费用清单</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
//页面初始化加载日历控件
function doInit()
	{
	   loadcalendar();
	}
//导出.do 提交方式开始
 function exportExcel(){
       disableBtn($("commitBtn"));//点击按钮后，按钮变成灰色不可用;
	   var startDate=document.getElementById("startDate").value;
	   var endDate=document.getElementById("endDate").value;
	   var producerCode=document.getElementById("producerCode").value;
	   var producerName=document.getElementById("producerName").value;
       fm.action="<%=contextPath%>/claim/other/ManufacturerReClaim/exportExcel.do?startDate="+startDate+"&endDate="+endDate+"&producerCode="+producerCode+"&producerName="+producerName;
       fm.submit();
	 }
//导出.do 提交方式结束

</script>
</head>

<body>
  <div class="navigation">
  		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;其他功能&gt;生产商再索赔
  </div>
  <form method="post" name="fm" id="fm">
    <table class="table_query" >
            <tr>
              <td class="table_query_2Col_label_7Letter" >申请单结算日： </td>
	              <td align="left">
		             	<div align="left">
			            		<input name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
			            		&nbsp;至&nbsp;
			            		<input name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
	            	    </div>	
            	  </td>
            </tr>
            	<tr>
					<td class="table_query_2Col_label_6Letter">生产商代码：</td>
					<td align="left">
					 		<input name="producerCode" type="text" id="producerCode" class="middle_txt" datatype="1,is_digit_letter,10" />
					</td>
					<td class="table_query_2Col_label_6Letter">生产商名称：</td>
					<td align="left">
					        <input name="producerName" type="text" id="producerName" class="middle_txt" datatype="1,is_digit_letter_cn,10"/>
					</td>
				</tr>
            <tr>
				  <td colspan="11" align="center">
	                   <input type="button" name="Submit" value="查询" class="normal_btn" onclick="__extQuery__(1);" />
	                   <input type="button" name="Submit2" value="导出EXCEL" class="long_btn" onclick="exportExcel();" id="commitBtn"/>
	              </td>
		    </tr>
    </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
 <br/>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
//查询路径
	var url = "<%=contextPath%>/claim/other/ManufacturerReClaim/serviceActivityManageReclaimQuery.json";
				
	var title = null;

	var columns = [
				{header: "生产商代码 ", dataIndex: 'PRODUCER_CODE', align:'center'},
				{header: "生产商名称 ",dataIndex: 'PRODUCER_NAME' ,align:'center'},
				{header: "申请单号 ",dataIndex: 'RO_NO' ,align:'center'},
				{header: "配件代码 ",dataIndex: 'PART_CODE' ,align:'center'},
				{header: "配件名称",dataIndex: 'PART_NAME' ,align:'center'},
				{header: "VIN",dataIndex: 'VIN' ,align:'center'},
				{header: "车型",dataIndex: 'GROUP_NAME' ,align:'center'},
				{header: "购车日期",dataIndex: 'PURCHASED_DATE' ,align:'center'},
				{header: "行驶里程(公里)",dataIndex: 'IN_MILEAGE' ,align:'center'},
				{header: "索赔类型",dataIndex: 'CLAIM_TYPE' ,align:'center',renderer:getItemValue},
				{header: "工时费(元)",dataIndex: 'LABOUR_AMOUNT' ,align:'center',renderer:formatCurrency},
				{header: "配件金额(元)",dataIndex: 'PARTS_AMOUNT' ,align:'center',renderer:formatCurrency},
				{header: "其它金额(元)",dataIndex: 'OTHERITEM_AMOUNT' ,align:'center',renderer:formatCurrency},
				{header: "总金额(元)",dataIndex: 'GROSS_CREDIT' ,align:'center',renderer:formatCurrency}//,renderer:checkMoney
		      ];

</script>
<!--页面列表 end -->
</body>
</html>
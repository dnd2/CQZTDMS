<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>免费保养模板</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/OldReturnAction/returnAmountAuditList.json?query=true";
			
var title = null;

var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink,align:'center'},
  				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
  				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
  				{header: "旧件回运清单号", dataIndex: 'RETURN_NO', align:'center'},
  				//{header: "旧件回运清单状态", dataIndex: 'BACK_DESC', align:'center'},
  				{header: "基地", dataIndex: 'YIELDLY', align:'center',renderer:setYieldlyName},
  				{header: "提报日期", dataIndex: 'RETURN_DATE', align:'center',renderer:formatDate},
  				{header: "签收日期", dataIndex: 'CREATE_DATE', align:'center'},
  				{header: "签收人", dataIndex: 'IN_WARHOUSE_NAME', align:'center'},
  				{header: "签收流水号", dataIndex: 'SIGN_NO', align:'center'},
  				//{header: "回运类型", dataIndex: 'RETURN_TYPE', align:'center',renderer:getItemValue},
  				{header: "货运方式", dataIndex: 'RETURN_DESC', align:'center'},
  				{header: "旧件回运起止时间", dataIndex: 'WR_START_DATE', align:'center'},
  				{header: "装箱数", dataIndex: 'PARKAGE_AMOUNT', align:'center'},
  				{header: "发运单号", dataIndex: 'TRANSPORT_NO', align:'center'},
  				{header: "三包员电话", dataIndex: 'TEL', align:'center'}
	      ];

function setYieldlyName(value,meta,record){
  return String.format("长沙");//固定值 长沙
}

		//格式化时间为YYYY-MM-DD
		function formatDate(value,meta,record) {
			 if (value==""||value==null) {
				return "";
			 }else {
				return value.substr(0,16);
			 }
		}
	    function myLink(value,meta,record){
	    	var urlView="<%=contextPath%>/OldReturnAction/returnAmountAuditInit.do?id="+value;
	        return String.format("<a href='"+urlView+"'>[审核]</a>");
	    }
function doInit(){
  __extQuery__(1);
}
</script>
<!--页面列表 end -->
</head>
<body>
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;旧件运费审核</div>
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	   <tr>
         <td style="text-align:right" nowrap="nowrap"  >经销商代码：</td>            
         <td style="text-align:left" nowrap="nowrap" >
           <input id="dealerCode" name="dealerCode" value="${dealerCode }" type="text" class="middle_txt">
         </td>
         <td style="text-align:right" nowrap="nowrap" >经销商名称：</td>
         <td style="text-align:left" nowrap="nowrap"><input id="dealerName" name="dealerName" value="${dealerName }" type="text" class="middle_txt" datatype="1,is_name,15" callFunction="javascript:MyAlert();"></td>
       </tr>
       <tr>
         <td style="text-align:right" nowrap="nowrap" >旧件回运清单号：</td>
         <td style="text-align:left" nowrap="nowrap"><input id="back_order_no" name="back_order_no" value="${back_order_no }" type="text" class="middle_txt"></td>
         <td style="text-align:right" nowrap="nowrap" >货运方式：</td>
	     <td style="text-align:left" nowrap="nowrap">
	     <script type="text/javascript"> 
           genSelBoxExp("freight_type",<%=Constant.OLD_RETURN_STATUS%>,"","true","u-select","","false",'<%=Constant.OLD_RETURN_STATUS_02%>,<%=Constant.OLD_RETURN_STATUS_04%>');
         </script>
	     </td>
       </tr>
       <tr>
         <td style="text-align:right" nowrap="nowrap" >发运单号：</td>
         <td style="text-align:left" nowrap="nowrap">
           <input id="trans_no" name="transport_no" value="${transport_no }" type="text" class="middle_txt">
         </td>         
       </tr>
	   <tr>
         <td style="text-align:center" colspan="8">
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
         </td>
       </tr>
</table>
<!-- 查询条件 end -->

<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>售后报表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>

<script type="text/javascript">
	function expotData(){          
		fm.action="<%=contextPath%>/claim/application/ClaimBillStatusTrack/addTimeParameter.do";
		fm.submit();
	  
	}
	function doInit(){          
		__extQuery__(1);
	  
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔基础数据&gt;时效参数维护
</div>
<form name="fm" id="fm">
<input type="hidden" name="date" value="${date}">
<input type="hidden" name="N" value="${N}">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<tr>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true">参数号码：</td>
		<td width="10%">
				<input name="parameterCode" class="middle_txt" id="parameterCode">
		</td>
		<td width="10%"class="table_query_2Col_label_5Letter" nowrap="true">参数名称：</td>
		<td width="15%">
		<input class="middle_txt" id="parameterName" name="parameterName" />
		</td>
		<td width="55%"></td>
		
	</tr>
	<tr>
	    <td  class="table_add_2Col_label_6Letter">参数类别：</td>
        <td>
      		 <script type="text/javascript">
				 genSelBoxExp("PARAMETER_TYPE",<%=Constant.PARAMETER_TYPE%>,"${po.parameterType}",true,"short_sel",'',"false","");
			 </script>
        </td> 
		<td  class="table_add_2Col_label_6Letter">参数状态：</td>
        <td>	
		     <script type="text/javascript">
					genSelBoxExp("PARAMETER_STATUS",<%=Constant.STATUS%>,"${po.parameterStatus }",true,"short_sel",'',"false","");
			 </script>
		</td> 
	</tr>
    <tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="btnQuery"  value="查询"  class="normal_btn" onClick="checkSum(1)" >
    		<input type="button" name="btnQuery" id="btnQuery"  value="新增"  class="normal_btn" onclick="subFun();" >
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
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var modelChecked=false;
	var re = /^\+?[1-9][0-9]*$/;
	function checkSum(){
			__extQuery__(1);
	}
	
	
	 function subFun(){
	    location="<%=contextPath%>/claim/application/ClaimBillStatusTrack/addTimeParameter.do";   
	  }
	
	var url = "<%=contextPath%>/claim/application/ClaimBillStatusTrack/queryTimeParameter.json";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "参数号码", dataIndex: 'PARAMETER_CODE', align:'center'},
		{header: "参数名称", dataIndex: 'PARAMETER_NAME', align:'center'},
		{header: "金额", dataIndex: 'AMOUNT', align:'center'},
		{header: "超时时间", dataIndex: 'TIMEOUT', align:'center'},
		{header: "状态", dataIndex: 'PARAMETER_STATUS', align:'center'},
		{header: "参数类型", dataIndex: 'PARAMETER_TYPE', align:'center'},
		
		{header: "操作", dataIndex: 'ID', align:'center',renderer:operate}
	];
	
	function operate(value,meta,record){
		return String.format("<a href='#' onclick='updatePO("+value+")'>修改</a>");
// 		return String.format("<a href='#' onclick='productCar1("+record.data.VDATEYEAR+","+record.data.VDATEMONTH+" )'>"+value+"</a>");
	}
	
	
	function updatePO(id){
		 location="<%=contextPath%>/claim/application/ClaimBillStatusTrack/addTimeParameter.do?ID="+id;   
	}
	
	function getYear(value,meta,record){
		return value.substring(0,4);
	}
	function getMonth(value,meta,record){
		return value.substring(5,7);
	}

	function wrapOut(){
		document.getElementById("dealer_id").value="";
		document.getElementById("dealer_code").value="";
	}
	function formatDate1(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return String.format(value.substr(0,10));
		}
	}
</script>
<!--页面列表 end -->
</html>
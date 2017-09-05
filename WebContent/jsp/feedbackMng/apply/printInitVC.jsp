<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务车申请首页</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
		loadcalendar();
	}
</script>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 信息反馈管理&gt;信息反馈提报 &gt;服务车申请表打印</div>
<form id="fm" name="fm">
<table class="table_query">
	<tr>
		<td width="10%" align="right">申请单号：</td>
		<td width="20%">
			<input type="text" class="middle_txt" name="applyNo" />
		</td>
		<td width="10%" align="right">状态 ：</td>
		<td width="20%">
			<script type="text/javascript">
				genSelBoxExp("status",<%=Constant.SERVICE_CAR_APPLY%>,"",true,"short_sel","","true",'');
			</script>
		</td>
	</tr>
	<tr>
		<td width="10%" align="right">申请时间：</td>
		<td width="20%">
			<input type="text" class="short_txt" name="b_date" id="b_date" datatype="1,is_date,10" group="b_date,e_date" hasbtn="true" callFunction="showcalendar(event,'b_date',false);"/>
              至
  			<input type="text" class="short_txt" name="e_date" id="e_date" datatype="1,is_date,10" group="b_date,e_date" hasbtn="true" callFunction="showcalendar(event,'e_date',false);"/>
		</td>
		<td colspan="2" align="center">
			<input class="normal_btn" type="button" value="查询" id="qryButton" onClick="__extQuery__(1);">&nbsp;&nbsp;
   		</td> 
	</tr>
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script type="text/javascript">
	var url = "<%=contextPath%>/feedbackmng/apply/ServiceCarWCAction/queryByDealer.json";		
	var title = null;
	var columns = [
                  {header:'序号',renderer:getIndex,align:'center'},
                  {header:'申请单号',dataIndex:'APPLY_NO',align:'center'},
                  {header:'服务站代码',dataIndex:'DEALER_CODE',align:'center'},
                  {header:'服务站名称',dataIndex:'DEALER_NAME',align:'center'},
                  {header:'拟购车型',dataIndex:'MODEL_CODE',align:'center'},
                  {header:'拟购车型市场价',dataIndex:'STANDARD_PRICE',align:'center'},
                  {header:'状态',dataIndex:'STATUS',align:'center',renderer:getItemValue},
                  {header:'操作',align:'center',renderer:myHandler}
                  ];
	function myHandler(value,meta,rec){
		var id = rec.data.ID ; 
		var status = rec.data.STATUS ;
		if(status == <%=Constant.SERVICE_CAR_APPLY_08%> || status == <%=Constant.SERVICE_CAR_APPLY_10%>)
			return '<a href="#" onclick="goPrint('+id+')">[打印]</a>' ;
		else return '<a href="<%=contextPath%>/feedbackmng/apply/ServiceCarWCAction/showDetail.do?id='+id+'">[明细]</a>' ;
	}
	function addServicecar(){
		location = '<%=contextPath%>/feedbackmng/apply/ServiceCarWCAction/addServicecarInit.do' ;
	}
	function goPrint(val){
		var url = '<%=contextPath%>/feedbackmng/apply/ServiceCarWCAction/goPrint.do?id='+val ;
		window.open(url);
	}
</script>
</BODY>
</html>
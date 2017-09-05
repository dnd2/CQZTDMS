<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>服务站信封打印记录</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;服务站信封打印记录</div>
<form name='fm' id='fm' method="post">
<table class="table_edit" >
	<tr>
 		<td width="10%" align="right">经销商代码：</td>
 		<td width="20%"><input type="text" class="middle_txt" name="dealer_code"/></td>
		<td width="10%" align="right">经销商名称：</td>
		<td width="20%"><input type="text" class="middle_txt" name="dealer_name"/></td>
	</tr>
	<tr>
 		<td width="10%" align="right">打印时间：</td>
 		<td width="20%" colspan="3">
		<input name="bDate" type="text" class="short_time_txt" id="bDate" readonly="readonly"/> 
		<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'bDate', false);" />  	
  		&nbsp;至&nbsp; <input name="eDate" type="text" class="short_time_txt" id="eDate" readonly="readonly"/> 
		<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'eDate', false);" /> 
		</td>
	</tr>
	<tr>
 		<td colspan="4" align="center">
 			<input class="normal_btn" type="button" value="查询" onclick="__extQuery__(1);"/> 
 		</td>
	</tr>
</table>
<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />

</form>
<script type="text/javascript">
	var myPage;
	var url = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/mailPrintDetailQuery.json' ;
	
	var title = null ;

	var columns = [
	               {header:'序号',width:'8%',align:'center',renderer:getIndex},
	               {header:'经销商代码',width:'12%',align:'center',dataIndex:'DEALER_CODE'},
	               {header:'经销商名称',width:'14%',align:'center',dataIndex:'DEALER_NAME'},
	               {header:'打印时间',width:'12%',align:'center',dataIndex:'PRINT_TIME'},
	               {header:'打印人',width:'14%',align:'center',dataIndex:'NAME'},
	               {header:'打印类型',width:'12%',align:'center',dataIndex:'PRINT_TYPE'}
		           	];
		__extQuery__(1);
	
	function doInit(){
	loadcalendar();
	}
</script>
</body>
</html>

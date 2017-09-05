<%-- 
创建时间 : 2010.08.27
             创建人:lishuai
             功能描述：结算室审核，可对索赔单进行批量审核，和逐条审核
--%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>超2月未上报开票单</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;超2月未上报开票单
</div>
<form method="post" name="fm" id="fm">
<table align="center" class="table_query">
	<tr>
		<td align="right" nowrap="true">经销商名称：</td>
		<td align="left" nowrap="true">
		<input name="DEALER_NAME" value="" type="text" class="middle_txt"/>
		</td>
		<td align="right" nowrap="true">经销商代码：</td>
		<td align="left" nowrap="true">
		<input name="DEALER_CODE" value="" type="text" class="middle_txt"/>
		</td>
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	
	<script type="text/javascript">
	
	      var myPage;
		//查询路径
		var url = "<%=contextPath%>/claim/application/DealerNewKp/dealerOnKpQuery.json";
		//标题			
		var title = null;
	    //显示列表控制
		var columns = [
						{header: "序号",align:'center',renderer:getIndex},
						{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},		
						{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
						{header: "电话",dataIndex: 'PHONE',align:'center'},
						{header: "未上报开票单月份",dataIndex: 'START_DATE',align:'center'},
						{header: "未上报数量",dataIndex: 'STATUS',align:'center'}
			      ];
	
	
	</script>
</body>
</html>
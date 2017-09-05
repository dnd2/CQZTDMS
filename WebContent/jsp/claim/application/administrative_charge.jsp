<%-- 
创建时间 : 2010.08.27
             创建人:lishuai
             功能描述：索赔单审核，对一条结算单内的多条索赔单进行审核
--%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>行政扣款查询</title>
	<script type="text/javascript">
		
	</script>
</head>
<body onload="__extQuery__(1);">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;行政扣款单查询
</div>
<form method="post" name="fm" id="fm">
<div class="form-panel">
	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
	<div class="form-body">
<table align="center" class="table_query" border='0'>
	<tr>
		<td style="text-align: right;">结算编号：</td>
		<td>
			<input class="middle_txt" name="BALANCE_ODER" id="BALANCE_ODER" value="" type="text" />
		</td>
		<c:if test="${code == 1}">
			<td style="text-align: right">经销商代码：</td>
      		<td>
      	 	<input class="middle_txt" id="dealerCode" onclick="showOrgDealer('dealerCode','','true','',true,'','10771002');" readonly="readonly" name="dealerCode" type="text"/>       
            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput('dealerCode');" value="清除"/>
      	</td> 
		</c:if>
		<td style="text-align: right;">使用状态：</td>
		<td>
			<script type="text/javascript">
   				genSelBoxExp("status",<%=Constant.BANCENT_INVOICE%>,"",true,"","","fasle",'');
  		    </script>
		</td>
	</tr>
	<tr>
		<td colspan="6"  style="text-align: center;">
			<input name="queryBtn" type="button" class="u-button u-query" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> &nbsp; 
			<input type="reset" class="u-button u-cancel" value="重 置"/>
		</td>
	</tr>
	<tr>
	<td>
	</td>
	</tr>
</table>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	
<script type="text/javascript">
		var myPage;
		var url = "<%=contextPath%>/claim/application/ClaimManualAuditing/administrative_charge.json?COMMAND=1";
		var title = null;
		var columns = [
					{header: "序号",align:'center',renderer:getIndex},
					{header: "服务站代码",dataIndex: 'DEALER_CODE',align:'center'},
					{header: "服务站名称",dataIndex: 'DEALER_NAME',align:'center'},					
					{header: "结算编号",dataIndex: 'BALANCE_ODER',align:'center'},
					{header: "使用情况",dataIndex: 'STATUS',align:'center',renderer:getItemValue},
					{header: "行政扣款费用",dataIndex: 'LABOUR_SUM',align:'center'},
					{header: "创建时间",dataIndex: 'CREATE_DATE',align:'center'}
			      ];
</script>
</form>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<% String contextPath = request.getContextPath(); %>

<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.TtAsWrGamefeePO"%>
<%@page import="com.infodms.dms.bean.TtAsWrGameBean"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>三包策略对应保养费用维护</title>
	</head>
<body>
<% 
	TtAsWrGameBean gamePO = (TtAsWrGameBean)request.getAttribute("gamePO");
%>
	<form action="<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/saveStrategyAmount.do" method="post" name="fm" id="fm">
	<%
		List amountList = (List)request.getAttribute("amountList");
	    if(amountList!=null && amountList.size()>0){
	%>
			<table class="table_edit" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
	<%
	    	for(int i=0;i<amountList.size();i++){
	    		TtAsWrGamefeePO feePO = (TtAsWrGamefeePO)amountList.get(i);
	%>
				<tr>
       				<td align="right">第<%=(i+1)%>次保养费用(元)：</td>
       				<td align="left">
       					<input type="text" name="GUARANTEE_AMOUNT<%=i%>" id="GUARANTEE_AMOUNT<%=i%>"
       							value="<%=CommonUtils.checkNull(feePO.getManintainFee())%>" datatype="0,is_double,20" decimal="2"/>
       				</td>
			    </tr>
	<%      }%>
				<tr>
					<td align="center" colspan="2">
					    <!-- 保养费用次数 -->
		       			<input type="hidden" name="COUNT" value="<%=gamePO.getMaintainNum()%>"/>
		       			<!-- 三包策略ID -->
	       		        <input type="hidden" name="ID" value="<%=gamePO.getId()%>"/>
						<input type="submit" class="normal_btn" name="saveBtn" value="保存"/>
					</td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;</td>
				</tr>
		 	</table>
	<%  } %>
	</form>
</body>
</html>
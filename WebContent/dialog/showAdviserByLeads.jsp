<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ page import="java.util.List"%>
<%@ page import="com.infodms.dms.dao.crm.dealerleadsmanage.DlrLeadsManageDao"%>
<%@ page import="com.infoservice.po3.bean.DynaBean"%>
<%@taglib uri="/jstl/cout" prefix="c"%>

<!-- created by andy.ten@tom.com 20100526 通用选择经销商 -->
<html xmlns="http://www.w3.org/1999/xhtml">

<%
	String contextPath = request.getContextPath();
    String leadsCode=request.getParameter("leadsCode");
    String leadsGroup=request.getParameter("leadsGroup");
    String dealerId=request.getParameter("dealerId");
    DlrLeadsManageDao dao = new DlrLeadsManageDao();
    List<DynaBean> adviserList = dao.getAdviserBydealer(dealerId);
    System.out.println("```````4234234234```````````"+dealerId);
%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">


<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<title>通用选择框</title>

<script language="JavaScript">

function doConfirmThis(){
	var obj = document.getElementById("adviserId"); //定位id
	var index = obj.selectedIndex; // 选中索引
	var value = obj.options[index].value; // 选中值

	var leadsAllotId="<%=leadsCode%>";
	var leadsGroup="<%=leadsGroup%>";
	var adviserId=value;
	addResult(adviserId);
}
function addResult(adviserId){
	_hide();
	//MyAlert("parentDocument==="+parentContainer);
	//MyAlert("chulai==="+parent.document.getElementById ('inIframe'));
	//parentDocument.getElementById('telephone').value='13368277777';
	parentContainer.returnFunction(adviserId);
}
</script>
</head>
<body>
<div>
<form method="post" name ="fm" id="fm">
		<table class="table_query" width="95%" align="center" style="MARGIN-RIGHT: auto; MARGIN-LEFT: auto; margin-top: 65px ">
			<tr>
				<td align="right" width="40%">选择顾问：</td>
				<td><select id="adviserId">
						<c:forEach var="item" items="<%=adviserList %>" varStatus="status">
							<option id="${item.USER_ID }" value="${item.USER_ID }">${item.NAME}</option>
						</c:forEach>
				</select></td>
			</tr>
			<tr>
		      	<td colspan="2" align="center"><input name="chk" type="button" class="normal_btn" onclick="doConfirmThis();" value="确定" />
		    </tr>
		</table>
	</form>	
</div>
</body>
</html>
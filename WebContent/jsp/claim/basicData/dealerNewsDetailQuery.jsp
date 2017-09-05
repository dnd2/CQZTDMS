<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
	String newsId = request.getParameter("newsId") == null ? "" : request.getParameter("newsId").toString();
%>

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>首页新闻查询</title>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
   		__extQuery__(1);
	}
</script>
</head>
<body>
<form name='fm' id='fm'>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：个人信息管理&gt;首页新闻</div>
  <input type="hidden" id="newsId" name="newsId" value="<%=newsId %>"/>
  <input type="hidden" id="msgType" name="msgType" value="${msgType }"/>
   <input type="hidden" id="viewNewsType" name="viewNewsType" value="${viewNewsType }"/>
  <input type="hidden" id="ttt" name="ttt" value=""/>
  <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	   <tr class="csstr" align="center">
				<!--<td align="right">大区：</td>
				<td align="left">
					<select id="__large_org" name="__large_org" class="short_sel" onchange="changeOrg(this.value)">
						<option value="">--请选择--</option>
						<c:forEach var="org" items="${orglist}">
							<option value="${org.ORG_ID}" title="${org.ORG_NAME}">${org.ORG_NAME}</option>
						</c:forEach>
					</select>
				</td>
			<td align="right">省份：</td> 
		  	<td align="left" colspan="3">
				<select id="__province_org" name="__province_org"><option value="">==请选择==</option></select>
			</td> 
			-->
			<td align="right">代码 :</td>
			<td align="left"><input type='text' value='' name='code' id='code' ></td>
			<td align="right">名称:</td>
			<td align="left"><input type='text' value='' name='pName' id='pName' ></td>
			<td align="right">阅读状态：</td> 
		  	<td align="left" colspan="3">
				<select id="isRead" name="isRead">
					<option value="">==请选择==</option>
					<option value="1">未阅</option>
					<option value="2">已阅</option>
				</select>
			</td>
	  </tr> 
		<tr>
			<td align = "center" colspan="10">
   				<input class="cssbutton" type="button" value="查 询" name="button1" id="queryBtn" onclick="getDetail();">
   				<input class="cssbutton" type="button" value="导 出" id="downExcel" name="downExcel" onclick="downExcelQuery();">
   				<input class="cssbutton" type="button" value="关闭" id="downExcel" name="downExcel" onclick="_hide();">
   			</td>
		</tr>
		</table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
</form>
<script type="text/javascript" >
function downExcelQuery(){
	document.fm.action = '<%=contextPath%>/claim/basicData/HomePageNews/dealerNewsReadDetailQuery.do';
	$("ttt").value = 1;
	document.fm.submit();
}

var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/HomePageNews/dealerNewsReadDetailQuery.json";
	var title = null;
	
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "大区",sortable: false,dataIndex: 'ROOT_ORG_NAME',align:'center'},
				{header: "省份",sortable: false,dataIndex: 'ORG_NAME',align:'center'},
				{header: "代码",sortable: false,dataIndex: 'DEALER_CODE',align:'center'},				
				{header: "名称",sortable: false,dataIndex: 'DEALER_NAME',align:'center'},
				{header: "阅读回复",sortable: false,dataIndex: 'NEWSBACK',align:'center'},
				{header: "阅读时间",sortable: false,dataIndex: 'READ_DATE',align:'center'}
	];
	
	
function getDetail(){
	$("ttt").value = "";
	__extQuery__(1);
}
</script>
</body>
</html>
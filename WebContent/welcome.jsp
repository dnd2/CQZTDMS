<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.bean.AclUserBean,com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
	Long userId = (Long)request.getAttribute("logonUser");
	//Long newsNum = (Long)request.getSession().getAttribute("newsNum");
	AclUserBean user = (AclUserBean)request.getSession().getAttribute(Constant.LOGON_USER);
	String dealer = user.getDealerId() == null ? "" : user.getDealerId();
%>

<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>首页新闻</title>
<script type="text/javascript">
	var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/HomePageNews/HomePageNewsQuary.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "序号",sortable: false,renderer:getIndex},
				{header: "新闻类别",sortable: false,dataIndex: 'NEWS_TYPE',renderer:getItemValue},
				{header: "新闻编号",sortable: false,dataIndex: 'NEWS_CODE'},				
				{header: "发表人",sortable: false,dataIndex: 'VOICE_PERSON'},
				{header: "发表日期",sortable: false,dataIndex: 'NEWS_DATE',renderer:formatDate},
				{header: "新闻标题",sortable: false,dataIndex: 'NEWS_TITLE',renderer:hrefNews}
		      ];

	function receivePson(value,meta,record){
		return String.format(
				"<a href=\"#\" onclick='viewReceive(\""+record.data.NEWS_ID+"\")'>[接收人信息]</a>&nbsp;<a href='javascript:;' onclick=\"viewReadDetail('"+record.data.NEWS_ID+"','"+record.data.MSG_TYPE+"','"+record.data.DUTY_TYPE+"')\">[阅读明细]</a>"
		)
	}

	function viewReceive(value){
		OpenHtmlWindow("<%=contextPath%>/claim/basicData/HomePageNews/viewReceive.do?newsId="+value,800,600);
	}

	function viewReadDetail(newsId,msgType,viewNewsType) {
		OpenHtmlWindow('<%=contextPath%>/claim/basicData/HomePageNews/dealerNewsReadDetailInit.do?newsId=' + newsId+'&messageType='+msgType+'&viewNewsType='+viewNewsType ,800,500);
	}
	
	function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,16);
		}
	} 
	function viewNews(value){
		OpenHtmlWindow("<%=contextPath%>/claim/basicData/HomePageNews/viewNews.do?comman=2&newsId="+value,800,600);
	}
	function hrefNews(value,meta,record){
		
		if(record.data.IS_TOP=='0'){
			return String.format(
					"<div align='left'><a href=\"#\" onclick='viewNews(\""+record.data.NEWS_ID+"\")'>"+value+"</a></div>"
			)
		}else{
			return String.format(
					"<div align='left'><a href=\"#\" style=\"color:red;\" onclick='viewNews(\""+record.data.NEWS_ID+"\")'>"+value+"</a></div>"
			)
		}
		
	}
	//修改的超链接设置
	$(function(){
		__extQuery__(1);
	});
</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 首页新闻</div>
		<form name='fm' id='fm'>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
</body>
</html>
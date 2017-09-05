<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/globalVariable.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<%@ page import="com.infodms.dms.exception.BizException" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.common.ErrorCodeConstant" %>
<%@ page import="com.infodms.dms.common.RightMessageConstant " %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<%@taglib uri="/jstl/x" prefix="x" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<script language="javascript" event="onReceiveCTIEventChanged(sender,EventCode,EventData)" for="ut_atocx">
   ut_atocx_ATMsgEvent(sender,EventCode,EventData);
</script>
<link href="<%=request.getContextPath()%>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/style/calendar.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/style/page-info.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/regionData.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dict.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/framecommon/HashMap.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/dialog_new.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/atclient_ocx.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/DestoryPrototype.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/FormValidation.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/calendar.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/prototype.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/framecommon/default.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/framecommon/DialogManager.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/validate.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/InfoAjax.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/my-grid-pager.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dateFormatter.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/msgformat.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/textBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/kindeditor/kindeditor.js" /></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/html-to-json.js" /></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/json2.js" /></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/sales/storage/commonUtil.js" ></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/forbidBackSpace.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/xdate.js"></script>
<script type="text/javascript" >
    var g_webAppName = '<%=(request.getContextPath())%>';   //全局webcontent应用名，避免js使用"<%=request.getContextPath()%>"
    var g_webAppImagePath = "<%=(request.getContextPath())%>"+"/images";   //全局图片路径
	function autoAlertException(){
		var str = null;
		<%
		Throwable exception = ActionContext.getContext().getException();
			if(exception!=null&& (exception instanceof BizException)){
				BizException biz = (BizException)exception;
				if(biz.getType()!=ErrorCodeConstant.SELF_DEAL_WITH_CODE){
		%>
					if(<%=biz.getErrCode()!=null %>){
						str = strAppend(str,"错误代码"+'<%=biz.getErrCode()%>'+":</br>");
					}
					if(<%=biz.getMessage()!=null %>){
						str = strAppend(str,'<%=biz.getMessage() %>');
					}
					MyAlert(str);
					return false;
			<%
			}}
			%>
				return true;
			
	}
	function getIncomingAlertScreen(strCaller,strCallid)
   {
	  	location   =  "<%=request.getContextPath()%>/customerRelationships/clientManage/IncomingAlertScreen/incomingAlertScreenInit.do?strCaller="+strCaller+"&strCallid="+strCallid;
   } 
</script>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<%
	Calendar time=Calendar.getInstance();
	SimpleDateFormat formate = new SimpleDateFormat("yyyy,MM,dd,hh,mm,ss");
	String sys_date = formate.format(time.getTime());
%>	
<title>坐席业务看板</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body onload="__extQuery__(1)">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 班长管理 &gt;坐席业务看板</div>
	<form method="post" name = "fm" id="fm">
		
	 <!-- 查询条件 end -->
	 <!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	 <!--分页 end -->
	 	时间：
	 	<input type="text" id="times" name="times" value="${times}"> 
	 	(秒)
	</form>
	<script type="text/javascript">
	
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/classMonitorManage/SeatsInformation/querySeatsInformation.json";
				
	var title = null;

	var columns = [
				{header: "工号",dataIndex: 'ACNT',align:'center'},
				{header: "姓名", dataIndex: 'NAME', align:'center'},
				{header: "签到时间",dataIndex: 'LOGINDATE',align:'center'},
				{header: "性别", dataIndex: 'GENDER', align:'center'},
				{header: "工作频率",dataIndex: 'TIMES',align:'center'},
				{header: "通话时长",dataIndex: 'TALKTIME',align:'center'},
				{header: "状态", dataIndex: 'WORKSTATUS', align:'center'}
		      ];

	//定时器
	var t = document.getElementById("times").value;
	function myrefresh() 
	{ 
		var t = document.getElementById("times").value;
		fm.submit();
	}
	
	if(t != '' || t != null) {
		setTimeout('myrefresh()',t*1000);
	}
	
</script>


</body>
</html>
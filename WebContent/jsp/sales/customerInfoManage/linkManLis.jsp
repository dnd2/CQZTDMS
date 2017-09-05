<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		var ctmId_Old =parent.$('inIframe').contentWindow.$('ctmId_Old').value;
		if(ctmId_Old==0){
			document.getElementById("queryBtn").disabled =true;
			document.getElementById("linkManName").disabled =true;
			document.getElementById("linktd").disabled =true;
		}else{
			document.getElementById("ctmId_Old").value=ctmId_Old;
		}
	}
</script>

<title>实销信息上报</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 客户信息管理 &gt; 实销信息上报&gt; 其他联系人列表</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
				<td width="20%" class="tblopt" id="linktd"><div align="right">联系人名称：</div></td>
				<td width="39%" >
      				<input type="text" id="linkManName" name="linkManName" class="middle_txt" size="20"   />
    			</td>
				<td class="table_query_3Col_input" >
					<input type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> 
					<input type="button" class="normal_btn" onclick="toAddLinkMan();" value="新 增" id="queryBtn1" /> 
					<input type="hidden" id="ctmId_Old" name="ctmId_Old" />
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
	
</div>
<script type="text/javascript">

	var myPage;
	var url = "<%=contextPath%>/sales/customerInfoManage/SalesReport/queryLinkManList.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "姓名", dataIndex: 'CTM_NAME', align:'center'},
				{header: "主要联系电话", dataIndex: 'MAIN_PHONE', align:'center'},
				{header: "其他联系电话", dataIndex: 'OTHER_PHONE', align:'center'},
				{header: "联系目的", dataIndex: 'BIRTHDAY', align:'center'},
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'LM_ID',renderer:myLink}
		      ];

	function myLink(value,meta,rec){
		return "<input type='radio'  name='ctm_id' value='"+value+"' onclick=submit_('"+value+"'); />"
    }

	function submit_(value){
		_hide();
		parent.$('inIframe').contentWindow.showCustomerInfo(value);
	}

	function toAddLinkMan(){
		window.location.href = "<%=contextPath%>/sales/customerInfoManage/SalesReport/toAddLinkMan.do";
	}
</script>    
</body>
</html>
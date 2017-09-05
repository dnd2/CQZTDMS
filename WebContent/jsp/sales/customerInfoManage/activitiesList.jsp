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

</script>

<title>实销信息上报</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 客户信息管理 &gt; 实销信息上报&gt; 市场活动列表</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
				<td width="20%" class="tblopt"><div align="right">市场活动名称：</div></td>
				<td width="39%" >
      				<input type="text" id="campaignName" name="campaignName" class="middle_txt" size="20"   />
    			</td>
				<td class="table_query_3Col_input" >
					&nbsp;&nbsp;<input type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> 
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
	
</div>
<script type="text/javascript">

	var myPage;
	var url = "<%=contextPath%>/sales/customerInfoManage/SalesReport/getActivitiesList.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'CAMPAIGN_ID',renderer:myLink},
				{header: "活动名称", dataIndex: 'CAMPAIGN_NAME', align:'center'},
				{header: "活动编号", dataIndex: 'CAMPAIGN_NO', align:'center'},
				{header: "活动主题", dataIndex: 'CAMPAIGN_SUBJECT', align:'center'},
				{header: "活动时间", dataIndex: 'START_DATE', align:'center',renderer:myGetValue}
		      ];

    function myGetValue(value,meta,rec){
        return String.format(""+value+"~"+rec.data.END_DATE);
    }
	function myLink(value,meta,rec){
		var data = rec.data;
		return "<input type='radio'  name='campaing_id' value='"+value+"' onclick='submit_(\""+data.CAMPAIGN_ID+"\",\""+data.CAMPAIGN_NAME+"\");' />";
    }

	function submit_(campaing_id,campaign_name){
		if (parent.$('inIframe')) {
			_hide();
			parent.$('inIframe').contentWindow.showActivitiesInfo(campaing_id,campaign_name);
		}else {
			parent._hide();
			parent.showActivitiesInfo(campaing_id,campaign_name);
		}

	}

</script>    
</body>
</html>
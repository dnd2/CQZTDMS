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

<title>项目执行方</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  市场活动管理 &gt; 市场活动管理 &gt; 项目执行方列表</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
				<td width="20%" class="tblopt"><div align="right">执行方名称：</div></td>
				<td width="39%" >
      				<input type="text" id="makerName" name="makerName" class="middle_txt" size="20"   />
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
	var url = "<%=contextPath%>/sales/marketmanage/activity/ActivityApply/toActivityMakerQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'CAMPAIGN_ID',renderer:myLink},
				{header: "执行方代码", dataIndex: 'MAKER_CODE', align:'center'},
				{header: "执行方名称", dataIndex: 'MAKER_NAME', align:'center'},
				{header: "创建时间", dataIndex: 'CREATE_DATE', align:'center'}
		      ];

	function myLink(value,meta,rec){
		var data = rec.data;
		return "<input type='radio'  name='campaing_id' value='"+value+"' onclick='submit_(\""+data.MAKER_CODE+"\",\""+data.MAKER_ID+"\");' />";
    }

	function submit_(makeCode,make_id){
		if (parent.$('inIframe')) {
			_hide();
			parent.$('inIframe').contentWindow.showActivitiesInfo(makeCode,make_id);
		}else {
			parent._hide();
			parent.showActivitiesInfo(makeCode,make_id);
		}

	}

</script>    
</body>
</html>
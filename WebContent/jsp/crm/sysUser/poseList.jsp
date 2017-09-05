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
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  潜客管理 &gt; 团队管理 &gt; 经销商职位列表</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
				<td width="20%" class="tblopt"><div align="right">职位名称：</div></td>
				<td width="39%" >
      				<input type="text" id="poseName" name="poseName" class="middle_txt" size="20"   />
    			</td>
    			<td width="20%" class="tblopt"><div align="right">职位代码：</div></td>
				<td width="39%" >
      				<input type="text" id="poseCode" name="poseCode" class="middle_txt" size="20"   />
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
	var url = "<%=contextPath%>/crm/sysUser/DealerSysPose/getPoseList.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'POSE_ID',renderer:myLink},
				{header: "职位代码", dataIndex: 'POSE_CODE', align:'center'},
				{header: "职位名称", dataIndex: 'POSE_NAME', align:'center'},
				{header: "职位状态", dataIndex: 'POSE_STATUS', align:'center',renderer:getItemValue},
				{header: "职位级别", dataIndex: 'DEALER_RANK', align:'center'}
		      ];

	function myLink(value,meta,rec){
		var data = rec.data;
		return "<input type='radio'  name='pose_id' value='"+value+"' onclick='submit_(\""+data.POSE_ID+"\",\""+data.POSE_NAME+"\");' />";
    }

	function submit_(pose_id,pose_name){
		if (parent.$('inIframe')) {
			_hide();
			parent.$('inIframe').contentWindow.showSalesManInfo(pose_id,pose_name);
		}else {
			parent._hide();
			parent.showSalesManInfo(pose_id,pose_name);
		}

	}

</script>    
</body>
</html>
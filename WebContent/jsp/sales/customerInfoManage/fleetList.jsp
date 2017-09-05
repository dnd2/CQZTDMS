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
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 客户信息管理 &gt; 实销信息上报&gt; 集团客户列表</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
				<td width="20%" class="tblopt"><div align="right">集团客户名称：</div></td>
				<td width="39%" >
      				<input type="text" id="fleet_name" name="fleet_name" class="middle_txt" size="20"   />
    			</td>
				<td class="table_query_3Col_input" >
					<input type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> 
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
	
</div>
<script type="text/javascript">

	var myPage;
	var url = "<%=contextPath%>/sales/customerInfoManage/SalesReport/getFleetList.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'FLEET_ID',renderer:myLink},
				{header: "客户姓名", dataIndex: 'FLEET_NAME', align:'center'},
				{header: "大客户代码", dataIndex: 'PACT_NAME', align:'center'},
				{header: "客户类型", dataIndex: 'FLEET_TYPE', align:'center',renderer:getItemValue,renderer:getItemValue},
				//{header: "购车用途", dataIndex: 'PURPOSE', align:'center',renderer:getItemValue},
				//{header: "区域", dataIndex: 'REGION', align:'center'},
				{header: "主要联系人", dataIndex: 'MAIN_LINKMAN', align:'center'},
				{header: "主要联系人电话", dataIndex: 'MAIN_PHONE', align:'center'}
		      ];

	function myLink(value,meta,rec){
		var data = rec.data;
		return "<input type='radio'  name='ctm_id' value='"+value+"' onclick=submit_(\""+data.FLEET_ID+"\",\""+data.FLEET_CODE+"\",\""+data.TYPE+"\"); />";
    }

	function submit_(fleet_id,fleet_code,type){
		if (parent.$('inIframe')) {
			_hide();
			parent.$('inIframe').contentWindow.showFleetInfo(fleet_id,fleet_code,type);
		}else {
			parent._hide();
			parent.showFleetInfo(fleet_id,fleet_code,type);
		}

	}

</script>    
</body>
</html>
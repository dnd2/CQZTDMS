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

<title>意向车型查询</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="__extQuery__(1);"> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  潜客管理 &gt; 客户管理 &gt; 意向车型列表</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
				<td width="15%" class="tblopt" align="left"><div align="right">车型代码：</div></td>
				<td width="15%" >
      				<input type="text" id="seriesCode" name="seriesCode" class="middle_txt" size="20"   />
    			</td>
    			<td width="15%" class="tblopt" align="left"><div align="right">车型名称：</div></td>
				<td width="15%" >
      				<input type="text" id="seriesName" name="seriesName" class="middle_txt" size="20"   />
    			</td>
				<td class="table_query_3Col_input" >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
	var url = "<%=contextPath%>/crm/customer/CustomerManage/getIntentVechileList.json?COMMAND=1";
	var title = null;
	var columns = [
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'SERIES_ID',renderer:myLink},
				{header: "车型代码", dataIndex: 'SERIES_CODE', align:'center'},
				{header: "车型名称", dataIndex: 'SERIES_NAME', align:'center'},
				{header: "上级代码", dataIndex: 'UP_CODE', align:'center'},
				{header: "上级名称", dataIndex: 'UP_NAME', align:'center'},
				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue}
		      ];

	function myLink(value,meta,rec){
		var data = rec.data;
		return "<input type='radio'  name='pose_id' value='"+value+"' onclick='submit_(\""+data.SERIES_ID+"\",\""+data.SERIES_NAME+"\");' />";
    }

	function submit_(intent_id,series_name){
		var textId = "${textId}";
		var textName = "${textName}";
		MyAlert("textId=="+textId);
		if (parent.$('inIframe')) {
			parentContainer._hide();
			if(textId!=null&&textId!="") {
				parentContainer.showInfo(intent_id,series_name,textId,textName);
			} else {
				parentContainer.showInfo(intent_id,series_name);
			}
		}else {
			parentContainer._hide();
			parentContainer.showInfo(intent_id,series_name);
		}

	}

</script>    
</body>
</html>
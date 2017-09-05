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
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 客户信息管理 &gt; 实销信息上报&gt; 销售顾问列表</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
				<td width="20%" class="tblopt"><div align="right">销售顾问名称：</div></td>
				<td width="39%" >
      				<input type="text" id="name" name="name" class="middle_txt" size="20"   />
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
	var url = "<%=contextPath%>/sales/customerInfoManage/SalesReport/getSalesManList.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'PERSON_ID',renderer:myLink},
				{header: "销售顾问姓名", dataIndex: 'NAME', align:'center'},
				{header: "性别", dataIndex: 'GENDER', align:'center',renderer:getItemValue},
				{header: "联系电话", dataIndex: 'MOBILE', align:'center'},
				{header: "经销商", dataIndex: 'DEALER_NAME', align:'center'}
		      ];

	function myLink(value,meta,rec){
		var data = rec.data;
		return "<input type='radio'  name='ctm_id' value='"+value+"' onclick='submit_(\""+data.PERSON_ID+"\",\""+data.NAME+"\");' />";
    }

	function submit_(person_id,name){
		if (parent.$('inIframe')) {
			_hide();
			parent.$('inIframe').contentWindow.showSalesManInfo(person_id,name);
		}else {
			parent._hide();
			parent.showSalesManInfo(person_id,name);
		}

	}

</script>    
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>选择故障件</title>
</head>
<body onload="__extQuery__(1)">
	<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 选择故障件</div>
	<form method="post" name = "fm" id="fm">	
	<div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>选择故障件</h2>
				<div class="form-body">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			
			<tr>
				<td class="right">故障件代码：</td>
				<td class="left" nowrap="true">
					<input type="text" id="malcode" name="malcode"/>
				</td>
				
				<td class="right">故障件名称：</td>
				<td class="left" nowrap="true">
					<input type="text" id="malname" name="malname"/>
				</td>
			</tr>
	
			<tr>
				<td colspan="4" class="center">
					<input class="u-button u-query" type="button" value="查询" name="recommit" id="queryBtn" onclick="__extQuery__(1);" />
        		</td>
			</tr>
		</table>
	 <!-- 查询条件 end -->
	 <!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	 <!--分页 end -->
	</form>
<script type="text/javascript">
	
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/selectmalfunction.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择", width:'8%',sortable: false,dataIndex: 'CLAIM_NO',renderer:myCheckBox},
				{header: "故障件代码", dataIndex: 'PART_CODE', style: 'text-align:center'},
				{header: "故障件名称", dataIndex: 'PART_CNAME', style: 'text-align:center'},
				{header: "创建日期", dataIndex: 'CREATE_DATE', style: 'text-align:center'}
		      ];
	
	function myCheckBox(value,metaDate,record){
		return String.format("<input name='radio' type='radio' onclick='changeCheck1(this,\""+ record.data.PART_CODE +"\",\""+ record.data.PART_CNAME +"\")'/>");
	}
	
	function doRowClick(obj){
    	if(obj.cells[0].firstChild.checked != true){
    		obj.cells[0].firstChild.checked = true;
    		var str = obj.cells[0].firstChild.value;
    		
    		changeCheck1(obj.cells[0].firstChild,str.split(',')[0],str.split(',')[1]);
    	}
    }
	//返回的数据 更新页面数据
	function changeCheck1(checkBox,malcode,malname){
		if(checkBox.checked){
			if (parent.$('inIframe')) 
			{
				__parent().selectmalfunctionBack(malcode,malname);
			}
		   _hide();
		}
	}
	
</script>
</body>
</html>
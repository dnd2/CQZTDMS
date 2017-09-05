<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<link href="<%=request.getContextPath()%>/style/dtree_default.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/dtree1.js"></script>
</head>
	
<body onunload='javascript:destoryPrototype()' >
 <div style="float:right;">
	 <form id='fm' name='fm'>
		<table class="table_query" >
			<tr>
				<td class="table_query_label" align="right">集团客户名称：</td>
				<td class="table_query_input" align="left">
					<input datatype="1,is_noquotation,<%=Constant.Length_Check_Char_100 %>" id="fleetName" name="fleetName" class="middle_txt" type="text" />
				</td>
				<td width="30%">
					<input name="button2" type=button class="cssbutton" onclick="__extQuery__(1);" value="查询"/>
					<input class="cssbutton" type="reset" value="重置" />
					<input class="cssbutton" type="button" onclick="_hide();" value="关闭" />
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
 </div>
<script language="javascript">
	var url = "<%=contextPath%>/sales/ordermanage/orderreport/SpecialNeedConfirm/queryFleet.json";
	//设置表格标题
	var title= null;
	//设置列名属性
	var columns = [
					{id:'action',header: "", width:'8%',sortable: false,dataIndex: 'FLEET_ID',renderer:myRadio},
					{header: "集团客户名称",width:"50px", dataIndex: 'FLEET_NAME'}
			      ];
	
	function myRadio(value,meta,record){
		return String.format("<input type='radio' name='radio1' onClick='setFleet("+record.data.FLEET_ID+",\""+record.data.FLEET_NAME+"\");'/>");
	}
	function setFleet(value1,value2){
		var curWin = parent.$('inIframe').contentWindow;
		if(curWin.iframe!=null){
			curWin.iframe.window.$('fleetName').value = value2;
			curWin.iframe.window.$('fleetId').value = value1;
		}else{
			parent.$('inIframe').contentWindow.$('fleetId').value = value1;
			parent.$('inIframe').contentWindow.$('fleetName').value = value2;
		}
		_hide();
	}
	function doInit(){
		__extQuery__(1);
	}
</script>
</body>
</html>
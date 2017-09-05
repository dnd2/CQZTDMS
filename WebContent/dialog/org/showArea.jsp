<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<% 
	String type = request.getParameter("type");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>大区查询显示</title>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<script type="text/javascript">
	
	var parentContainer = parent || top;
	if( parentContainer.frames ['inIframe'] ){
		parentContainer = parentContainer.document.getElementById ('inIframe').contentWindow;
	}
	var parentDocument =parentContainer.document;
		var myPage;
		var url = "<%=request.getContextPath()%>/common/OrgShow/queryOrgArea.json?command=1";
		var title = null;
		
		var columns = [
       	  	{header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"groupIds\")'/>",dataIndex:'ORG_ID', align:'center', renderer: getCheckbox},
       	  	{header: "区域代码", dataIndex: 'ORG_CODE', align:'center'},
       	  	{header: "区域名称", dataIndex: 'ORG_NAME', align:'center'}
       	];
		
		function getCheckbox(value,meta,record) {
			return "<input type='checkbox' name='groupIds' id='"+value+"' value='"+record.data.ORG_NAME+"'/>";
		}
		
		function doInit() {
// 			var ids = parentContainer.getHtmlElement();
			var ids = parentContainer.getHtmlElement();
			for(var i=0;i<ids.length;i++) {
				document.getElementById('hcontent').innerHTML += "<input type='hidden' name='ids' value='"+ids[i].value+"'/>";
			}
			__extQuery__(1);
		}
		
		// 从弹出层获得选中的区域或经销商
		function getChoose() {
			var choosed = new Array();
			var groupIds = document.getElementsByName('groupIds');
			for(var i=0;i<groupIds.length;i++) {
				if(groupIds[i].checked == true) {
					choosed.push([groupIds[i].id, groupIds[i].value]);
				}
			}
			
			parentContainer.setHtmlValue(choosed);
// 			parentContainer.setHtmlValue(choosed);
		}
	</script>
</head>
<body>
	<form method="post" name="fm" id="fm">
		<span id="hcontent"></span>
		<input id="DUTY_TYPE" name="DUTY_TYPE" type="hidden" value="<%=type.equals("01") ? Constant.DUTY_TYPE_LARGEREGION : Constant.DUTY_TYPE_SMALLREGION %>"/>
		<table class="table_query" bordercolor="#DAE0EE">
			<colgroup>
				<col align="right"/><col align="left"/><col align="right"/><col align="left"/>
			</colgroup>
			<tr>
				<td>组织名称：</td>
				<td>
					<input id="ORG_NAME" name="ORG_NAME" type="text"/>
				</td>
				<td>组织代码：</td>
				<td>
					<input id="ORG_CODE" name="ORG_CODE" type="text"/>
				</td>
				<td>
					<input type="button" class="cssbutton" value="查询" onclick="__extQuery__(1)"/>
					&nbsp;
					<input type="reset" class="cssbutton" value="重置"/>
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		<center>
			<br/><input type="button" class="cssbutton" value="选 择" onclick="getChoose()"/>
		</center>
	</form>
</body>
</html>
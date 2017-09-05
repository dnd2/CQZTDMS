<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<title>数据字典查询</title>

	<script type=text/javascript>
		var myPage;
		var title = null;
		var url ="<%=contextPath%>/claim/preAuthorization/Authorization/getTcCodeListInitQuery.json";
		var columns = [
			{header: "序号",align:'center',renderer:getIndex},
			{header: "选择", align:'center',dataIndex: 'CODE_ID',renderer:setSelect},
			{header: "类型",dataIndex: 'TYPE_NAME',align:'center'},
			{header: "类型名称",dataIndex: 'CODE_ID',align:'center',renderer:getItemValue}
	      ];
		
		function setSelect(value, mata, record) {
			return "<input type='checkbox' name='codeId' value='"+record.data.CODE_ID+"_"+record.data.CODE_DESC+"' />";
		}
		
		function getPolicyType(value, mata, record) {
			if(value == '99951001'){
				return "常规商务政策";
			}else if(value =='99951002'){
				return "特殊商务政策";
			}else{
				return "常规商务政策";
			}
		}
		
		function choose() {
			var ids = '';
			var idName = '';
			var deployObjs = document.getElementsByName("codeId");
			if(deployObjs) {
				for(var i=0;i<deployObjs.length;i++) {
					if(deployObjs[i].checked == true) {
						var s = deployObjs[i].value.split("_");
						if (ids==""){
							ids = s[0];
							idName = s[1];
						}else{
							ids +=  ','+s[0];
							idName += ','+s[1];
						}
						
					}
				}
			}
			parentContainer.tcCodeConfirm('${idInput}', '${nameInput}', ids, idName);
		}
		
		function doInit() {
			loadcalendar();
			__extQuery__(1);
		}
	</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：&gt; 财务管理 &gt; 商务政策发布查询
		</div>
		<form method="post" name="fm" id="fm">
			<input type = "hidden" id = "CODE_TYPE" name ="CODE_TYPE" value = "${CODE_TYPE }" />
			<input type = "hidden" id = "NOT_CODE_ID" name ="NOT_CODE_ID" value = "${NOT_CODE_ID }" />
			<table class="table_query">
				<tr>
					<th width="100" align="left" colspan="4">
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 查询条件
					</th>
				</tr>
				<tr>
					<td align="right">类型名称：</td>
					<td>
						<input type="text" class="middle_txt" id="CODE_DESC" name="CODE_DESC"/>
					</td>
					<td align="right">类型代码：</td>
					<td>
						<input type="text" class="middle_txt" id="CODE_ID" name="CODE_ID"/>
					</td>
				</tr>
				<tr>
					<td align="center" colspan="4">
						<input class="cssbutton" type="button" value="查询" onclick="__extQuery__(1)" name="BtnQuery" id="queryBtn">
						&nbsp;&nbsp;
						<input class="cssbutton" type="button" value="确定选择" onclick="choose()">
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
</body>
</html>

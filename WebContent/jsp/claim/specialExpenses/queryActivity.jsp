<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>   
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>
<div class="navigation">
	<img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：服务活动选择
	</div>
	<input type="hidden" name="code" value="${code}"/>
	<form name = "fm" id="fm">
		<table align=center class=table_query>
			<tr>
				<td width="20%" align="right">活动代码：</td>
				<td><input type="text" name="activity_code" class="middle_txt"/></td>
				<td width="20%" align="right">活动名称：</td>
				<td><input type="text" name="activity_name" class="middle_txt"/></td>
				<td>
					<input type="button" name="btnQuery" value="查询" class="normal_btn" onclick="__extQuery__(1);"/>&nbsp;
					<input type="button" value="返回" class="normal_btn" onclick="_hide();"/>
				</td>
			</tr>
		</table>
		<input type="hidden" name="selPartIds" value="${selPartIds}"/>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>

<script language="JavaScript">
var code = "${code}" ;
var url = "<%=contextPath %>/claim/specialExpenses/SpecialExpensesManage/queryActivity.json?code="+code;
//设置表格标题
var title= null;
//设置列名属性
var columns= [
		{header: '序号', align:'center', renderer:getIndex,width:'7%'},
		{header: '选择',renderer:myCheckBox, align:'center', width:'10%'},
		{header: '活动代码', dataIndex:'ACTIVITY_CODE',align:'center',width:'10%'},
		{header: '活动名称', dataIndex:'ACTIVITY_NAME',align:'center',width:'10%'},
		{header: '活动类别', dataIndex:'ACTIVITY_TYPE',align:'center',renderer:getItemValue}
     ];
function myCheckBox(val,meta,rec){
	var ipt='';
		ipt+= '<input type="radio" onclick="MyWinClose('+rec.data.ACTIVITY_ID+')";/>' ;
	return String.format(ipt);
}
var pWin=parentContainer;
function MyWinClose(value){
	if(pWin.setActivity!=undefined){
		pWin.setActivity(value);
		_hide();
	}
	else
		MyAlert('调用父页面setActivity方法出现异常!');
}
__extQuery__(1);

</script>
</body>
</html>
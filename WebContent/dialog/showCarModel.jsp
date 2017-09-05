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
<body>
<div class="navigation">
	<img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：服务车型选择
	</div>
	<form name = "fm" id="fm">
		<table align=center class=table_query>
			<tr>
				<td width="10%" align="right">物料组代码：</td>
				<td><input type="text" name="code" class="middle_txt"/></td>
				<td width="10%" align="right">物料组名称：</td> 
				<td><input type="text" name="name" class="middle_btn"/></td>
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
var group_id = '<%=request.getParameter("id")%>' ;
var url = "<%=contextPath %>/common/SeriesShow/getCarModel.json?id="+group_id;
//设置表格标题
var title= null;
//设置列名属性
var columns= [
		{header: '序号', align:'center', renderer:getIndex,width:'7%'},
		{header: '选择',renderer:myCheckBox, align:'center', width:'10%'},
		{header: '物料组代码', dataIndex:'materialCode',align:'center',width:'10%'},
		{header: '物料组名称', dataIndex:'materialName',align:'center',width:'10%'},
		{header: '状态', dataIndex:'status',align:'center',width:'10%',renderer:getItemValue}
     ];
function myCheckBox(val,meta,rec){
	var ipt='';
		ipt+= '<input type="radio" name="partId" onclick="setModel(\'' ;
		ipt+=rec.data.materialId+"','" ;
		ipt+=rec.data.materialCode+"','" ;
		ipt+=rec.data.materialName+"','" ;
		ipt+=rec.data.status+"');\">" ;
	return String.format(ipt);
}
var pWin=parentContainer;
function setModel(id,code,name,status){
	if(pWin.setCarModel!=undefined)
		pWin.setCarModel(id,code,name,status);
	else
		MyAlert('调用父页面setCarModel方法出现异常!');
	_hide();
}
__extQuery__(1);

</script>
</body>
</html>
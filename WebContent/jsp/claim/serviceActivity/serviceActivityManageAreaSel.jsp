<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/dtree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/DestoryPrototype.js"></script>
<%
	String ctx = request.getContextPath();
%>
</head>
<body>	
<form id='fm' name='fm'>
	<table class="table_edit">
		<tr>
			<td width="10%" align="right" height="30">组织代码：</td>
			<td width="10%">
				<input type="text" class="middle_txt" name="code"/>
			</td>
			<td width="10%" align="right">组织名称 ：</td>
			<td width="20%">
				<input type="text" class="middle_txt" name="name"/>
			</td>
		</tr>
		<tr>
			<td colspan="4" align="center">
				<input type="button" class="normal_btn" value="查询" onclick="__extQuery__(1);"/>
				&nbsp;&nbsp;
				<input type="button" class="normal_btn" value="关闭" onclick="_hide();"/>
			</td>
		</tr>
	</table>
	<br />
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<table class="table_edit">
		<tr>
			<td align="center">
				<input type="button" class="normal_btn" value="全选" onclick="checkAll();"/>
				&nbsp;&nbsp;
				<input type="button" class="normal_btn" value="全不选" onclick="doDisCheckAll();"/>
				&nbsp;&nbsp;
				<input type="button" class="normal_btn" value="确定" onclick="winClose();"/>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
var url = "<%=ctx%>/claim/serviceActivity/ServiceActivityManageDealer/areaQuery.json";
//设置表格标题
var title= null;
//设置列名属性
var columns = [
                {header: "序号", align:'center', renderer:getIndex,width:"5px"},
				{header: "选择",   dataIndex: 'orgId', width:"10px",renderer:myLink},
				{header: "组织代码",dataIndex: 'orgCode',width:"40px"},
				{header: "组织名称",dataIndex: 'orgName',width:"50px"}
		      ];
function myLink(value,meta,rec){
	var ipt = '' ;
	ipt+='<input type="checkbox" name="org_id" value="'+rec.data.orgId+'">';
	ipt+='<input type="hidden" name="org_code" value="'+rec.data.orgCode+'">';
	ipt+='<input type="hidden" name="org_name" value="'+rec.data.orgName+'">';
	return String.format(ipt);
}
function winClose(){
	var ids = document.getElementsByName('org_id');
	var codes = document.getElementsByName('org_code');
	var names = document.getElementsByName('org_name');
	var i = [];
	var c = [];
	var n = [];
	var k = 0;
	for(var x=0;x<ids.length;x++){
		if(ids[x].checked==true){
			i[k] = ids[x];
			c[k] = codes[x];
			n[k++] = names[x];
		}
	}
	if(parentContainer.setArea==undefined){
		MyAlert('调用父页面setArea方法出现异常!');
	}else{
		parentContainer.setArea(i,c,n);
		_hide();
	}
}
function checkAll(){
	var groupCheckBoxs=document.getElementsByName("org_id");
	if(!groupCheckBoxs) return;
	for(var i=0;i<groupCheckBoxs.length;i++)
	{
		groupCheckBoxs[i].checked=true;
	}
}
function doDisCheckAll(){
	var groupCheckBoxs=document.getElementsByName("org_id");
	if(!groupCheckBoxs) return;
	for(var i=0;i<groupCheckBoxs.length;i++)
	{
		groupCheckBoxs[i].checked=false;
	}
}
__extQuery__(1);
</script>	
</body>
</html>
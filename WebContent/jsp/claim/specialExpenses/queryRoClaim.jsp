<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>  
<script type="text/javascript">
function doInit()
{
	loadcalendar();  //初始化时间控件
}
</script> 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>
<div class="navigation">
	<img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：工单选择
	</div>
	<form name = "fm" id="fm">
		<table align=center class=table_query>
			<tr>
				<td  align="right">VIN：</td>
				<td><input type="text" name="vin" class="middle_txt"/></td>
				<td  align="right">维修日期：</td>
				<td><input name="beginTime" id="t1" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            		&nbsp;至&nbsp;
            		<input name="endTime" id="t2" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            	</td>
				<td align="center">
					<input type="button" name="btnQuery" value="查询" class="normal_btn" onclick="__extQuery__(1);"/>&nbsp;
					<input type="button" value="返回" class="normal_btn" onclick="_hide();"/>
				</td>
			</tr>
		</table>
		<input type="hidden" name="selPartIds" value="${selPartIds}"/>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		<table class="table_edit">
			<tr>
				<td align="center">
					<input type="button" class="normal_btn" value="全选" onclick="selAll(true);"/>
					&nbsp;
					<input type="button" class="normal_btn" value="全不选" onclick="selAll(false);"/>
					&nbsp;
					<input type="button" class="normal_btn" value="确定" onclick="MyWinClose();"/>
				</td>
			</tr>
		</table>
	</form>

<script language="JavaScript">
var url = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/queryRoClaim.json';
//设置表格标题
var title= null;
//设置列名属性
var columns= [
		{header: '序号', align:'center', renderer:getIndex,width:'7%'},
		{header: '选择', dataIndex:'BALANCE_NO',renderer:myCheckBox, align:'center', width:'10%'},
		{header: '单号' ,dataIndex:'RO_NO',align:'center'},
		{header: 'VIN' ,dataIndex:'VIN',align:'center'},
		{header: '维修类型',dataIndex:'REPAIR_TYPE_CODE',renderer:getItemValue,align:'center'}
     ];
function myCheckBox(val,meta,rec){
	var ipt='';
		ipt+= '<input type="checkbox" name="cb"/>' ;
		ipt+= '<input type="hidden" name="id" value='+rec.data.ID+'>' ;
	return String.format(ipt);
}
var pWin=parentContainer;
function MyWinClose(){
	var arr = document.getElementsByName('cb');
	var bs = document.getElementsByName('id');
	var ids = '' ;
	for(var i=0;i<arr.length;i++){
		if(arr[i].checked == true ){
			ids+=arr[i].value+',';
		}
	}
	if(pWin.setRO!=undefined){
		pWin.setRO(ids.substr(0,ids.length-1));
		_hide();
	}
	else
		MyAlert('调用父页面setRO方法出现异常!');
}
function selAll(value){
	var arr = document.getElementsByName('cb');
	for(var i=0;i<arr.length;i++)
		arr[i].checked = value ;
}
__extQuery__(1);

</script>
</body>
</html>
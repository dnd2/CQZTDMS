<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%String contextPath = request.getContextPath();%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<title>辅料费维护新增</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	</head>
	<body>
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />
			&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;辅料费维护&gt;新增
		</div>
		<form name='fm' id='fm'>
			<table class="table_add">
				<tr>
				<td class="table_add_2Col_label_6Letter" nowrap="true">工时代码：</td>
				<td><input name="workhour_code" id="workhour_code" value=""
					type="text" class="middle_txt" /><font color="red"> *</font></td>
				<td class="table_add_2Col_label_6Letter" nowrap="true">工时名称：</td>
				<td><input name="workhour_name" id="workhour_name" value=""
					type="text" class="middle_txt" /><font color="red"> *</font></td>
				</tr>
				<tr>
				<td class="table_add_2Col_label_6Letter" nowrap="true">金额：</td>
				<td><input name="price" id="price" value=""
					datatype="0,is_double" decimal="2" class="middle_txt" /></td>
				<td class="table_add_2Col_label_6Letter" nowrap="true">状态：</td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("status",1001,"10011001",false,"short_sel","","true",'');
					</script>
				</td>
			</tr>
				
			</table>
			<table class="table_add">
				<tr>
					<td colspan="4" align=center>
						<input class="normal_btn" type="button" name="ok" value="保存" id="commitBtn" onclick="save();"/>
						<input class="normal_btn" name="back" id="back" type="button" onclick="javascript:history.go(-1)" value="返回"/>	
					</td>
				</tr>
			</table>
		</form>
<script type="text/javascript">


function save(){

	if(!submitForm('fm')) {
		MyAlert("111");
		return false;
	}
	
	$('commitBtn').disabled=true;
	if($('workhour_code').value==""){
		MyAlert("工时代码不能为空!");
		$('commitBtn').disabled=false;
		return;
	}
	
	if($('workhour_name').value==""){
		MyAlert("工时名称不能为空!");
		$('commitBtn').disabled=false;
		return;
	}

	var url = "<%=contextPath%>/claim/basicData/AccPriceMaintainMain/addDate.json";
	sendAjax(url,showDetail,"fm")
}

function doInit(){
	
}

function showDetail(json){
	var ok = json.ok;
	var workhourCodeExist = json.workhourCodeExist;
	if(ok=='ok'){
		MyAlert("新增成功!");
		back();
	}else if(workhourCodeExist!=""){
		MyAlert(workhourCodeExist);
		$('commitBtn').disabled=false
		return;
	}else{
		MyAlert("新增失败!");
		$('commitBtn').disabled=false
		return;
	}
}
function back(){
	location.href = "<%=contextPath%>/claim/basicData/AccPriceMaintainMain/accessoryPriceMaintainInit.do";
}

</script>
</body>
</html>

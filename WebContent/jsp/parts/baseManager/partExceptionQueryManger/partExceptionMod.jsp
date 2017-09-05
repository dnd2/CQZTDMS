<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
    String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>计划变更维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script language="javascript" type="text/javascript">

function goBack(){
	btnDisable();
	location = '<%=contextPath%>/parts/baseManager/partExceptionQueryManger/partExceptionQuery/partExceptionQueryInit.do' ;
}

// 替换配件选择
function sel2() {
  	var partId = $('#PART_ID')[0].value;
  	var url = '<%=contextPath%>/parts/baseManager/partExceptionQueryManger/partExceptionQuery/partExceptionQuerySelect.do';
	url += '?partOldId='+partId;
	url += '&reType=2';
    OpenHtmlWindow(url, 700, 560, '变更后编码');
}

//表单提交方法：
function checkForm(){
	btnDisable();
	makeNomalFormCall('<%=contextPath%>/parts/baseManager/partExceptionQueryManger/partExceptionQuery/partExceptionQueryUpdate.json',showResult,'fm');			
}

function showResult(json){
	btnEnable();
	if(json.errorExist != null && json.errorExist.length > 0){
		MyAlert("配件代码：【"+json.errorExist+"】系统已存在！");
	}else if(json.success != null && json.success == "true"){
		MyAlert('修改成功！', function(){
			window.location.href = "<%=contextPath%>/parts/baseManager/partExceptionQueryManger/partExceptionQuery/partExceptionQueryInit.do";
		});
	}else{
		MyAlert("修改失败，请联系管理员！");
	}
}


//表单提交前的验证：
function checkFormUpdate(){
	if (!submitForm('fm')) {
		return false;
	}
	var part_code1 = document.getElementById('PART_OLDCODE').value;
	var part_code2 = document.getElementById('PART_OLDCODE2').value;
	var type = document.getElementById('TYPE').value;
	if ("" == part_code1) {
		layer.msg("原配件编码不能为空！", {icon: 15});
		return false;
	}
	if ("" == part_code2) {
		layer.msg("变更后的配件编码空！", {icon: 15});
		return false;
	}
	if ("" == type) {
		layer.msg("请选择替换类型！", {icon: 15});
		return false;
	}
	/*if (part_code1 == part_code2) {
		MyAlert("设变后的代码不能和原代码相同！");
		document.getElementById('PART_OLDCODE2').value = "";
		return false;
	}*/
	MyConfirm("确认是否修改?",checkForm);
}

</script>
</head>
<body>
	<form name='fm' id='fm'>
		<div class="wbox">
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 设计变更维护 &gt; 修改
			</div>
			<div class="form-panel">
				<h2>
					<img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 变更信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td width="15%" class="right">配件编码：</td>
							<td width="35%">
								<input type="text" id="PART_OLDCODE" name="PART_OLDCODE" class="middle_txt" value="${partOldcode }" readonly="readonly" disabled="disabled" />
								<input type="hidden" name="PART_ID" id="PART_ID" value="${partId }" />
								<input type="hidden" name="REPLACE_ID" id="REPLACE_ID" value="${replaceId }" />
							</td>
							<td width="15%" class="right">变更后编码：</td>
							<td width="35%">
								<input type="text" class="middle_txt" id="PART_OLDCODE2" name="PART_OLDCODE2" value="${rePartOldcode }" readonly="readonly" />
								<input type="hidden" name="OLD_REP_PART_ID" id="OLD_REP_PART_ID" value="${rePartID }" />
								<input type="hidden" name="PART_DATA2" id="PART_DATA2" value="${rePartCname }" />
								<input type="hidden" name="PART_ID2" id="PART_ID2" value="${rePartID }" />
								<input name="BUTTON" type="button" class="mini_btn" onclick="sel2();" value="..." />
								<font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td class="right">替换类型：</td>
							<td colspan="3">
								<script type="text/javascript">
									genSelBoxExp("TYPE", <%=Constant.ZT_PB_PART_REPLACE_TYPE%>, "${type}", true, "short_sel u-select", "", "false", '');
								</script>
								<font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td class="right">备注：</td>
							<td colspan="3">
								<textarea rows="3" cols="2" id="REMARK" name="REMARK" class="form-control align" style="width: 80%;">${remark }</textarea>
							</td>
						</tr>
						<tr>
							<td class="center" colspan="4">
								<input type="button" name="saveBtn" id="saveBtn" value="保 存" onclick="checkFormUpdate()" class="u-button" />
								<input type="button" name="backBtn" id="backBtn" value="返 回" onclick="goBack()" class="u-button" />
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</form>
</body>
</html>

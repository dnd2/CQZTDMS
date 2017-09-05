<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
    String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件内部单位设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script language="javascript" type="text/javascript">
  //返回
function reBack() {
  	btnDisable();
  	location = '<%=contextPath%>/parts/baseManager/partInnerOrgManager/partInnerOrgAction/partInnerOrgInit.do';
}

//表单提交方法：
function checkForm() {
	btnDisable();
	var url = '<%=contextPath%>/parts/baseManager/partInnerOrgManager/partInnerOrgAction/insertPartInnerOrg.json';
	makeNomalFormCall(url, showResult, 'fm');
}
function showResult(json) {
	btnEnable();
	if (json.errorExist != null && json.errorExist.length > 0) {
		MyAlert("单位编码：【" + json.errorExist + "】已创建，不能重复创建!");
	} else if (json.success != null && json.success == "true") {
		MyAlert("新增成功!", reBack);
	} else {
		disableBtn($("saveBtn"));
		MyAlert("新增失败，请联系管理员!");
	}
}

//表单提交前的验证：
function checkFormUpdate() {
	var orgCode = document.getElementById("orgCode").value;
	var orgName = document.getElementById("orgName").value;
	if ("" == orgCode || null == orgCode) {
		MyAlert('内部单位编码不能为空!');
		return false;
	}

	if ("" == orgName || null == orgName) {
		MyAlert('内部单位名称不能为空!');
		return false;
	}
	MyConfirm("确认是否新增?", checkForm);
}
</script>
</head>
<body>
	<div class="wbox">
		<form name='fm' id='fm'>
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 配件内部单位维护 &gt; 新增
			</div>
			<div class="form-panel" style="min-width: 770px;">
				<h2>
					<img src="<%=request.getContextPath()%>/img/subNav.gif" /> 信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">单位编码：</td>
							<td>
								<input class="middle_txt" type="text" name="orgCode" id="orgCode" value="" />
								<font color="red">*</font>
							</td>
							<td class="right">单位名称：</td>
							<td>
								<input style="width: 200px;" class="middle_txt" type="text" name="orgName" id="orgName" value="" />
								<font color="red">*</font>
							</td>
							<td class="right">是否有效：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("STATE",<%=Constant.STATUS%>,<%=Constant.STATUS_ENABLE%>, true, "", "", "false", '');
								</script>
							</td>
						</tr>
						<tr>
							<td class="right">联系人：</td>
							<td>
								<input class="middle_txt" type="text" name="linkMan" id="linkMan" value="" />
							</td>
							<td class="right">联系电话：</td>
							<td colspan="2">
								<input class="middle_txt" type="text" name="linkPhone" id="linkPhone" value="" />
							</td>
						</tr>
						<tr>
							<td class="right">地址：</td>
							<td colspan="5">
								<input style="width: 51.8%;" class="middle_txt" type="text" name="address" id="address" value="" />
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input type="button" name="saveBtn" id="saveBtn" value="保 存" onclick="checkFormUpdate()" class="u-button" />
								<input type="button" name="backBtn" id="backBtn" value="返 回" onclick="reBack()" class="u-button" />
							</td>
						</tr>
					</table>
				</div>
			</div>
		</form>
	</div>
</body>
</html>

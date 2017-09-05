<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
    String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件内部单位设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">

//数据验证
function dataTypeCheck(obj)
{
	var value = obj.value;
    if (isNaN(value)) {
        MyAlert("请输入数字!");
        obj.value = "";
        return;
    }
    var re = /^[1-9]+[0-9]*]*$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入正整数!");
        obj.value = "";
        return;
    }
}

//保存设置
function saveData() {
	var orgCode = document.getElementById("orgCode").value;
   	var orgName = document.getElementById("orgName").value;
   	if("" == orgCode || null == orgCode)
   	{
   		MyAlert('内部单位编码不能为空!');
		return false;
   	}

   	if("" == orgName || null == orgName)
   	{
   		MyAlert('内部单位名称不能为空!');
		return false;
   	}
   	MyConfirm("确定保存设置?", function(){
		btnDisable();
     	var url = '<%=contextPath%>/parts/baseManager/partInnerOrgManager/partInnerOrgAction/updatePartInnerOrg.json';
     	makeNomalFormCall(url,showResult,'fm');
    });
   		
}

function showResult(json) {
	btnEnable();
    if (json.errorExist != null && json.errorExist.length > 0) {
        MyAlert(json.errorExist);
    } else if (json.success != null && json.success == "true") {
    	MyAlert("操作成功!", goBack);
    } else {
        MyAlert("操作失败，请联系管理员!");
    }
}

//返回
function goBack(){
	btnDisable();
	location = '<%=contextPath%>/parts/baseManager/partInnerOrgManager/partInnerOrgAction/partInnerOrgInit.do' ;
}

</script>
</head>
<body>
	<div class="wbox">
		<form name='fm' id='fm'>
			<input type="hidden" name="inOrgId" id="inOrgId" value="${map.IN_ORG_ID}" />
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 配件内部单位维护 &gt; 修改
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
								<input class="middle_txt" type="text" name="orgCodeTxt" id="orgCodeTxt" value="${map.IN_ORG_CODE}" disabled="disabled" />
								<input type="hidden" name="orgCode" id="orgCode" value="${map.IN_ORG_CODE}" />
								<font color="red">*</font>
							</td>
							<td class="right">单位名称：</td>
							<td>
								<input style="width: 200px;" class="middle_txt" type="text" name="orgName" id="orgName" value="${map.IN_ORG_NAME}" />
								<font color="red">*</font>
							</td>
							<td class="right">是否有效：</td>
							<td>
								<script type="text/javascript">
							   	 genSelBoxExp("STATE",<%=Constant.STATUS%>,"${map.STATE}",true,"","","false",'');
							  </script>
							</td>
						</tr>
						<tr>
							<td class="right">联系人：</td>
							<td>
								<input class="middle_txt" type="text" name="linkMan" id="linkMan" value="${map.LINK_MAN}" />
							</td>
							<td class="right">联系电话：</td>
							<td colspan="2">
								<input class="middle_txt" type="text" name="linkPhone" id="linkPhone" value="${map.LINK_PHONE}" />
							</td>
						</tr>
						<tr>
							<td class="right">地址：</td>
							<td colspan="5">
								<input style="width: 50%;" class="middle_txt" type="text" name="address" id="address" value="${map.ADDRESS}" />
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input type="button" name="saveBtn" id="saveBtn" value="保 存" onclick="saveData()" class="u-button" />
								<input type="button" name="backBtn" id="backBtn" value="返 回" onclick="goBack()" class="u-button" />
							</td>
						</tr>
					</table>
				</div>
			</div>
		</form>
	</div>

</body>
</html>

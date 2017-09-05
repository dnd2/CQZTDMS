<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%String contextPath = request.getContextPath();%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>货位维护</title>
</head>
<body>
<div class="wbox">
	<div class="navigation">
		<img src="<%=request.getContextPath()%>/img/nav.gif" />
		<span>&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 仓储相关信息维护 &gt; 货位维护&gt; 新增</span>
	</div>
	<form method="post" name ="fm" id="fm" method="post" enctype="multipart/form-data">
		<div class="form-panel">
			<h2><img src="/CQZTDMS/jmstyle/img/search-ico.png" class="panel-query-title nav" />查询条件</h2>
			<div class="form-body">
				<table class="table_query" border="0">
					<tr>
						<td class="right" align="right">库房：</td>
						<td align="left">
							<select name="WH_ID" id="WH_ID" class="short_sel u-select">
								<c:forEach items="${wareHouseList}" var="wareHouse">
									<option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
								</c:forEach>
							</select>
						</td>
						<td class="right" align="right">货位编码：</td>
						<td><input class="middle_txt" type="text" name="LOC_CODE" id="LOC_CODE"/></td>
					</tr>
					<tr>
						<td class="txt-center" align="center" colspan="4">
						<input class="u-button" type="button" value="保存" name="BtnQuery" id="queryBtn" onClick="save();"/>
						<input class="u-button u-cancel" type="button" value="返回" onclick="window.history.back();">
						</td>
					</tr>
				</table>
			</div>
		</div>
		
	</form>
</div>
</body>
<script type="text/javascript" >
function save(){
	var LOC_CODE = document.getElementById("LOC_CODE").value;
	if(LOC_CODE.trim() == ""){
		MyAlert("请输入货位编码再进行保存！");
		return;
	}
	var url = g_webAppName+"/parts/storageManager/partLocation/PartLocation/partLocationAddSave.json";
	makeNomalFormCall(url,saveCallBack,'fm','queryBtn');
}
function saveCallBack(jsonObj){
	if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (null != error && error.length > 0) {
            MyAlert(error);
        }else if (null != success && success.length > 0) {
            MyAlert(success);
            window.history.back();
        }else {
            MyAlert(exceptions.message);
        }
    }
}
</script>
</html>
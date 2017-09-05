<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<style>table.table_query{background-color: transparent}.bottom-button{padding-top: 10px}fieldset a.expand{text-decoration: none}</style>
<script type="text/javascript">
function controlTb(id) {
    if ($('#tb1_' + id)[0].style.display == "none") {
        $('#tb1_' + id)[0].style.display = "table"
    } else {
        $('#tb1_' + id)[0].style.display = "none";
    }

}
function ckAll(obj) {
    var ck = document.getElementsByName("ck");
    for (var i = 0; i < ck.length; i++) {
        ck[i].checked = obj.checked;
    }
}
function back() {
    window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstockExceptionLog/partDlrInstockExceptionLogInit.do?";
}
function sub() {
    MyConfirm('确定回复！', function(){
	    var ck = document.getElementsByName("ck");
	    for (var i = 0; i < ck.length; i++) {
	        ck[i].disabled = (!ck[i].checked);
	    }
	    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstockExceptionLog/reply.json";
	    sendAjax(url, getResult, 'fm');
    });
}

function getResult(jsonObj) {
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success, function(){
             window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstockExceptionLog/partDlrInstockExceptionLogInit.do';
            });
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}
</script>
</head>
<body enctype="multipart/form-data">
<div class="wbox">
	<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" alt="" />&nbsp;当前位置： 配件管理 &gt; 配件销售管理 &gt; 异常信息 &gt;异常明细
	</div>
	<form name="fm" id="fm" method="post">
		<div class="form-panel">
			<h2>异常明细</h2>
			<div class="form-body">
				<input type="checkbox" checked onclick=ckAll(this)>全选
				<c:forEach items="${list}" var="data">
					<FIELDSET class="form-fieldset">
						<LEGEND style="MozUserSelect: none; KhtmlUserSelect: none" unselectable="on">
								<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
								<input type="checkbox" name="ck" value="${data.EXCEPTION_ID}" checked />
								<a class="expand" href='javascript:void(0)' onclick="controlTb(${data.EXCEPTION_ID})"> 
									配件 <font color="a04020">${data.PART_CNAME}</font>的异常信息(点击可收起/展开)
								</a>
						</LEGEND>
						<table id="tb1_${data.EXCEPTION_ID}" name="tb" class="table_query">
							<tr>
								<td class="right" style="width: 13%;">配件编码： </td>
								<td style="width: 20%;">${data.PART_OLDCODE}</td>
								<td class="right" style="width: 13%;">配件名称：</td>
								<td style="width: 20%;"> ${data.PART_CNAME}</td>
								<td class="right" style="width: 13%;">缺件个数：</td>
								<td style="width: 20%;">
									<font color='red'>${data.EXCEPTION_NUM}</font>
								</td>
							</tr>
							<tr>
								<td class="right">缺坏件备注：</td>
								<td colspan="5">
									<textarea style="width: 80%;" class="form-control" cols="90" rows="2" readonly>${data.EXCEPTION_REMARK}</textarea>
								</td>
							</tr>
							<tr>
								<td class="right">回 复：</td>
								<td colspan="5">
									<textarea style="width: 80%;" class="form-control" rows="2" id="reply_${data.EXCEPTION_ID}" name="reply_${data.EXCEPTION_ID}" class="middle_txt">${data.OEM_REMARK}</textarea>
								</td>
							</tr>
						</table>
					</FIELDSET>
				</c:forEach>
				<table style="width: 100%;">
					<tr>
						<td class="bottom-button" align="center">
							<input name="BtnQuery" id="queryBtn" class="u-button u-submit" type="button" value="保 存" onclick="sub();" />
							&nbsp;&nbsp;&nbsp;&nbsp;
							<input name="BtnQuery" id="queryBtn" class="u-button u-cancel" type="button" value="返 回 " onclick="back();" />
						</td>
					</tr>
				</table>
			</div>
		</div>
	</form>
</div>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
			String curPage = (String) request.getAttribute("curPage");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 

"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件服务商开票信息修改</title>
<script type="text/javascript">
function confirmUpdate() {
	if(!submitForm('fm')){
		return;	
	}
	MyConfirm("确定修改?", function() {
		$("#saveBtn")[0].disabled = true;
		$("#saveBtn")[0].value = '请等待 ...';
	    //提交前把禁用的字段设置为可用
	    document.getElementById("DEALER_CODE").disabled="";
	    document.getElementById("DEALER_NAME").disabled="";
		
		  var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartBillManager/updatePartBillInfo.json";	
	    makeNomalFormCall(url,getResult,'fm');	
	});
}

function getResult(jsonObj) {
	btnEnable();
	if(jsonObj!=null){
			var success = jsonObj.success;
			var error = jsonObj.error;
			var exceptions = jsonObj.Exception;
			if(success){
				MyAlert(success, function(){
					window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartBillManager/partBillQueryInit.do?'+'curPage='+<%=curPage%>;
				});
			}else if (error) {
				layer.msg(error, {icon: 2});
				document.getElementById("DEALER_CODE").disabled="disabled";
					document.getElementById("DEALER_NAME").disabled="disabled";
			}else if(exceptions){
					layer.msg(exceptions.message, {icon: 2});
					document.getElementById("DEALER_CODE").disabled="disabled";
					document.getElementById("DEALER_NAME").disabled="disabled";
				}
	}	
}
	
//返回查询页面
function goback(){
	 window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartBillManager/partBillQueryInit.do';
}
	
</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 配件服务商开票信息维护 &gt; 修改
		</div>
		<form id="fm" name="fm" method="post">
			<input id="BILL_ID" name="BILL_ID" type="hidden" value="${billInfo.BILL_ID}">
			<input id="DEALER_ID" name="DEALER_ID" type="hidden" value="${billInfo.DEALER_ID}">
			<div class="form-panel">
				<h2>
					<img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif" />服务商开票信息修改
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">服务商编码:</td>
							<td>
								<input class="long_txt middle_txt" type="text" disabled="disabled" id="DEALER_CODE" name="DEALER_CODE" datatype="0,is_null" value="${billInfo.DEALER_CODE}" />
							</td>
							<td class="right">服务商名称:</td>
							<td>
								<input class="long_txt middle_txt" type="text" disabled="disabled" id="DEALER_NAME" name="DEALER_NAME" datatype="0,is_null" value="${billInfo.DEALER_NAME}" />
							</td>
							<td class="right">开票名称:</td>
							<td>
								<input type="text" id="TAX_NAME" name="TAX_NAME" class="long_txt middle_txt" datatype="0,is_null" value="${billInfo.TAX_NAME}" />
							</td>
						</tr>
						<tr>
							<td class="right">纳税人识别号:</td>
							<td>
								<input type="text" id="TAX_NO" name="TAX_NO" class="long_txt middle_txt" datatype="0,is_null" value="${billInfo.TAX_NO}" />
							</td>
							<td class="right">电话:</td>
							<td>
								<input type="text" id="TEL" name="TEL" class="long_txt middle_txt" datatype="0,is_phone" value="${billInfo.TEL}" />
							</td>
							<td class="right">开户行:</td>
							<td>
								<input type="text" id="BANK" name="BANK" class="long_txt middle_txt" datatype="0,is_null" value="${billInfo.BANK}" />
							</td>
						</tr>
						<tr>
							<td class="right">账号:</td>
							<td>
								<input type="text" id="ACCOUNT" name="ACCOUNT" class="long_txt middle_txt" datatype="0,is_digit" size="30" value="${billInfo.ACCOUNT}" />
							</td>
							<td class="right" nowrap="nowrap">开票类型:</td>
							<td colspan="3">
								<script type="text/javascript">
									genSelBoxExp("dlrInvTpe",<%=Constant.DLR_INVOICE_TYPE%>, '${billInfo.INV_TYPE}', true, "short_sel u-select", "", "false", "");
								</script>
							</td>
						<tr>
							<td class="right">地址:</td>
							<td colspan="5">
								<input class="middle_txt input-495" type="text" id="ADDR" name="ADDR" datatype="0,is_null" size="90" value="${billInfo.ADDR}" />
							</td>
						</tr>
						<tr>
							<td class="right">发票邮寄地址:</td>
							<td colspan="5">
								<input class="middle_txt input-495" type="text" id="MAIL_ADDR" name="MAIL_ADDR" datatype="1,is_null" size="90" value="${billInfo.MAIL_ADDR}" />
							</td>
						</tr>
						<tr>
							<td class="right">备注:</td>
							<td colspan="5">
								<textarea class="long_txt form-control remark align" rows="5" cols="100" id="REMARK" name="REMARK">${billInfo.REMARK}</textarea>
							</td>
						</tr>
					</table>
					<table class="table_edit" width="100%">
						<tr>
							<td align="center">
								<input type="button" name="saveBtn" id="saveBtn" value="保 存" onclick="confirmUpdate();" class="u-button" />
								<input type="button" name="saveBtn" id="saveBtn" value="返 回" onclick="javascript:goback();" class="u-button" />
							</td>
						</tr>
					</table>
				</div>
			</div>
		</form>
	</div>
</body>
</html>

<%@ page import="com.infodms.dms.common.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ taglib uri="/jstl/fmt" prefix="fmt"%>
<%
    String contextPath = request.getContextPath();
%>
<head>
<title>打款登记编辑</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<script type="text/javascript">
function saveCredit() {
    btnDisable();
    makeNomalFormCall('<%=contextPath%>/parts/financeManager/dealerAccImpRecordManager/partDealerAccImpRecAction/partDealerAccQuerySave.json', showResult, 'fm');
    btnEnable();
}
function showResult(json) {
    if (json.ACTION_RESULT == '1') {
        MyAlert("保存成功!", function(){
	        window.location.href = "<%=contextPath%>/parts/financeManager/dealerAccImpRecordManager/partDealerAccImpRecAction/partDealerAccPaymentinit.do";
        });
    } else {
    	MyAlert(json.ACTION_RESULT);
        btnEnable();
    }
}
function checkFormAddOrUpdate() {

    var amount = document.getElementById("amount").value;
    var pz_no = document.getElementById("pz_no").value;
    var checkDate = document.getElementById("checkSDate").value;
    var jp = document.getElementById("is_jp");
    var reg=/^[1-9]{1}\d*(\.\d{1,2})?$/;
    if(jp.checked){
        document.getElementById("jp").value=<%=Constant.PART_ACCOUNT_PURPOSE_TYPE_02%>
    }else{
        document.getElementById("jp").value=<%=Constant.PART_ACCOUNT_PURPOSE_TYPE_01%>
    }

    if(amount==""){
        MyAlert("打款金额不能为空！");
        return;
    }
    if(pz_no=="") {
        MyAlert("流水号不能为空！");
        return;
    }
    if(checkDate=="") {
        MyAlert("打款日期不能为空！");
        return;
    }
    if(!reg.test(amount)){
        MyAlert("打款金额录入不对！");
        return;
    }
    if (!submitForm('fm')) {
        return false;
    }
    MyConfirm("是否确认保存?", saveCredit);
}

function submitFormAddOrUpdate() {
    if (!submitForm('fm')) {
        return false;
    }
    MyConfirm("是否确认保存并提交，提交将后无法再次修改?", saveAndSubmitCredit);
}

function saveAndSubmitCredit() {
    btnDisable();
    document.getElementById("status").value = '${NORMAL_STATUS}';
    makeNomalFormCall('<%=contextPath%>/sales/threeCreditManagement/DoMoneyToRegister/doMoneyToRegisterSaveOrUpdate.json', showSubmitResult, 'fm');
}

function showSubmitResult(json) {
    if (json.ACTION_RESULT == '1') {
        MyAlert("保存并提交成功!");
        window.location.href = "<%=contextPath%>/sales/threeCreditManagement/DoMoneyToRegister/doMoneyToRegisterToQuery.do";
    } else {
        MyAlert(json.ACTION_RESULT);
        btnEnable();
    }
}

function extractCode(value) {
    var s = value.split("*#*");
    document.getElementById("bankCode").value = s[0];
    //MyAlert(11);
}
function gobackConfirm(){
    MyConfirm("确认返回?",goback);

}
function goback(){
    window.location.href = "<%=contextPath%>/parts/financeManager/dealerAccImpRecordManager/partDealerAccImpRecAction/partDealerAccPaymentinit.do";
}
</script>

</head>
<body>

	<div class="wbox">
		<div class=navigation>
			<img src="<%=request.getContextPath()%>/img/nav.gif">&nbsp;当前位置： 配件财务管理 &gt; 配件打款登记 &gt; 打款登记
		</div>
		<form id=fm method=post name=fm>
			<input type="hidden" name="dealerId" id="dealerId" value="${dealerId}" />
			<input type="hidden" name="hid" id="hid" value="${hid}" />
			<input type="hidden" name="jp" id="jp" />
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/img/subNav.gif"/> 信息
				</h2>
				<div class="form-body">
					<table class=table_query>
						<th colspan="4" />
						<tr>
							<td class="right">服务商代码：</td>
							<td>${dealerCode}</td>
							<td class="right">服务商名称：</td>
							<td>${dealerName}</td>
						</tr>
						<tr>
							<td class="right">打款金额：</td>
							<td>
								<input class="middle_txt" type="text" datatype="0,isMoney,10" maxlength="10" value="${amount}" id="amount" name="amount">
								<font style="color: red">(小数点后精度保留2位)</font>
							</td>
							<td class="right">打款日期：</td>
							<td>
								<input class="middle_txt" id="checkSDate" name="checkSDate" datatype="1,is_null,10" value="${dk_date}" readonly group="checkSDate,ECREATE_DATE" />
								<input class="time_ico" value=" " onclick="showcalendar(event, 'checkSDate', false);" type="button" />
						</tr>
						<tr>
							<td class="right">流水号：</td>
							<td>
								<input type="text" class="middle_txt" name="pz_no" id="pz_no" datatype="0,is_null,100" value="${pz_no}" />
								<font style="color: red">(请录入后8位数字)</font>
							</td>
							<td class="right">精品配件款：</td>
							<td>
								<input type="checkbox" style="border: none;" name="is_jp" id="is_jp" value="" />
								<font style="color: red">(款项为精品款时请勾选)</font>
								<script type="text/javascript">
				                    if('${jp}'=='<%=Constant.PART_ACCOUNT_PURPOSE_TYPE_02%>') {
										document.getElementById("is_jp").checked = true;
									} else {
										document.getElementById("is_jp").checked = false;
									}
								</script>
							</td>
						</tr>
						<tr>
							<td class="right">汇入银行：</td>
							<td>
								<select id="bankId" name="bankId" class="u-select">
									<c:forEach items="${bank }" var="list">
										<c:choose>
											<c:when test="${list.BANK_ID == bank_id}">
												<option value="${list.BANK_ID }" selected>${list.BANK_NAME }</option>
											</c:when>
											<c:otherwise>
												<option value="${list.BANK_ID }">${list.BANK_NAME }</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<td class="right">备注：</td>
							<td colspan="3">
								<textarea class="form-control" style="width: 80%; display: inline;" name="remark" id="remark" rows="3">${remark}</textarea>
								<font style="color: red">(字数不超过100)</font>
							</td>
						</tr>
					</table>
					<table class=table_query>
						<tr>
							<td class="center">
								<input id="saveBtn" onclick="checkFormAddOrUpdate();" class="u-button" value="保存" type="button">
								<input id="backBtn" onclick="gobackConfirm();" class="u-button" value="返回" type="button">
							</td>
						</tr>
					</table>
				</div>
			</div>
		</form>
</body>
</html>
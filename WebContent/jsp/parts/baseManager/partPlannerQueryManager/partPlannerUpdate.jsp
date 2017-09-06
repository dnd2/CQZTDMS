<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.common.Constant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">
function ON_OK(){
	var WAY = jQuery('#WAY').val();
	var ORG1 = jQuery('#ORG1').val();
	if(WAY==''){
		layer.msg('请选择采购方式!', {icon: 2});
		return;
	}
	if(ORG1==''){
		layer.msg('请选择上级单位!', {icon: 2});
		return;
	}
	MyConfirm('确认保存？', saveRecord, [], null, null, 2);
}

function saveRecord(){
   	var url = '<%=contextPath%>/parts/baseManager/partPlannerQueryManager/partPlannerQuery/UpdateProduceFac.json';
   	makeNomalFormCall(url,showResult,'fm');
}

function showResult(json) {
    if (json.errorExist != null && json.errorExist.length > 0) {
        MyAlert(json.errorExist);
    } else if (json.success != null && json.success == "true") {
        MyAlert("保存成功!", function(){
   __parent().__extQuery__(1);
         _hide();
        });
    } else {
        MyAlert("保存失败，请联系管理员!");
    }
}
   
  //返回申请页面
function goback(){
  	window.location.href = '<%=contextPath%>/parts/baseManager/partPlannerQueryManager/partPlannerQuery/partPlannerQueryInit.do';
}

function changeType(obj, num) {
	document.getElementById("WAY").value = obj;
	if (obj == 92811001) {
		// 92811001 客服采购     
		// 97141006  客服公司
		document.getElementById("ORG").value = 97141006;
		document.getElementById("ORG1").value = 97141006;
	} else if (obj == 92811002 || obj == 92811003 || obj == 92811004) {
		// 92811002 三工厂采购 
		// 92811003 四工厂采购 
		// 92811004 五工厂采购 
		// 97141003 股份公司
		document.getElementById("ORG").value = 97141003;
		document.getElementById("ORG1").value = 97141003;
	} else if (obj == 92811005) {
		// 92811005 北京采购 
		// 97141001 北京工厂
		document.getElementById("ORG").value = 97141001;
		document.getElementById("ORG1").value = 97141001;
	} else if (obj == 92811006) {
		// 92811006 合肥采购 
		// 97141004 合肥工厂
		document.getElementById("ORG").value = 97141004;
		document.getElementById("ORG1").value = 97141004;
	} else if (obj == 92811007) {
		// 92811007 铃木采购 
		// 97141007 铃木工厂
		document.getElementById("ORG").value = 97141007;
		document.getElementById("ORG1").value = 97141007;
	} else if (obj == 92811008) {
		// 92811008 河北采购 
		// 97141005 河北工厂
		document.getElementById("ORG").value = 97141005;
		document.getElementById("ORG1").value = 97141005;
	} else if (obj == 92811009) {
		// 92811009 福特采购 
		// 97141002 福特工厂
		document.getElementById("ORG").value = 97141002;
		document.getElementById("ORG1").value = 97141002;
	} else if (obj == 92811010) {
		// 92811010 东安动力 
		// 97141009 东安动力
		document.getElementById("ORG").value = 97141009;
		document.getElementById("ORG1").value = 97141009;
	} else if (obj == 92811011) {
		// 92811011 东安三菱 
		// 97141010 东安三菱
		document.getElementById("ORG").value = 97141010;
		document.getElementById("ORG1").value = 97141010;
	} else if (obj == 92811012) {
		// 92811012 南京采购 
		// 97141008 南京工厂
		document.getElementById("ORG").value = 97141008;
		document.getElementById("ORG1").value = 97141008;
	} else if (obj == 92811013) {
		// 92811013 新能源采购
		// 97141011 新能源公司
		document.getElementById("ORG").value = 97141011;
		document.getElementById("ORG1").value = 97141011;
	} else if (obj == 92811014) {
		// 92811014 江铃采购
		// 97141012 江铃工厂
		document.getElementById("ORG").value = 97141012;
		document.getElementById("ORG1").value = 97141012;
	}
	// document.all.sel.options[num].selected=true; 
}
</script>
</head>
<body>
	<div class="wbox">
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<input type="hidden" value="${PO.partId}" name="PART_ID">
			<input type="hidden" name="ORG" id="ORG" value="${PO.superiorPurchasing }">
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置: 基础信息管理 &gt;计划相关信息维护 &gt; 备件采购属性维护&gt;修改
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/subNav.gif" /> 备件信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">配件编码:</td>
							<td>${PO.partOldcode}</td>
							<td class="right">配件名称:</td>
							<td>${PO.partCname}</td>
						</tr>
						<tr>
							<td class="right">配件件号:</td>
							<td colspan="3">${PO.partCode}</td>
						</tr>
						<tr>
							<td class="right">采购方式:</td>
							<td>
								<select class="u-select" name="WAY" id="WAY" onchange="changeType(this.options[this.selectedIndex].value,this.selectedIndex);">
									<option value="">--请选择--</option>
									<c:forEach items="${TcList}" var="saler">
										<option <c:if test="${PO.produceFac==saler.codeId}">selected</c:if> value="${saler.codeId}">${saler.codeDesc}</option>
									</c:forEach>
								</select>
							</td>
							<td class="right">上级单位名称:</td>
							<td colspan="3">
								<select class="u-select" name="ORG1" id="ORG1" disabled>
									<option value="">--请选择--</option>
									<c:forEach items="${TcList1}" var="saler">
										<option <c:if test="${PO.superiorPurchasing==saler.codeId}">selected</c:if> value="${saler.codeId}">${saler.codeDesc}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
					</table>
				</div>
			</div>

			<table class="table_edit tb-button-set">
				<tr align="center">
					<td>
						<input class="u-button" type="button" value="保存" onclick="ON_OK();" />
						<input class="u-button" type="button" value="关闭" onclick="_hide();" />
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>


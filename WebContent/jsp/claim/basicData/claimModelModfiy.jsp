<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrModelGroupPO"%>
<%
String contextPath = request.getContextPath();
TtAsWrModelGroupPO po = (TtAsWrModelGroupPO)request.getAttribute("MODELGROUP"); //取车型组po
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车型组维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="wbox">
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;车型组维护</div>
<form name='fm' id='fm'>
<div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
<input type="hidden" name="WRGROUP_ID" id="WRGROUP_ID" value="<%=po.getWrgroupId()==null?"":po.getWrgroupId()%>"/>
<table class="table_query">
 <tr>
 	<td class="table_add_2Col_label_5Letter" style="text-align:right">车型组类型：</td>
 	<td>
 	    <script type="text/javascript">
	              genSelBoxExp("WRGROUP_TYPE",<%=Constant.WR_MODEL_GROUP_TYPE%>,"<%=po.getWrgroupType()%>",false,"","","false",'<%=Constant.WR_MODEL_GROUP_TYPE_02%>');
	    </script>
    </td>
    <td class="table_add_2Col_label_5Letter" style="text-align:right">车型组代码：</td>
 	<td><%=po.getWrgroupCode()==null?"":po.getWrgroupCode()%></td>
 </tr>
 <tr>
 	<td class="table_add_2Col_label_5Letter" style="text-align:right">车型组名称：</td>
 	<td>
 		 <input type="text" name="WRGROUP_NAME" id="WRGROUP_NAME" class="middle_txt"  value="<%=po.getWrgroupName()==null?"":po.getWrgroupName()%>"/>&nbsp;<font color="red">*</font>
 	</td>
 	<td class="table_add_2Col_label_5Letter" style="text-align:right">保养费用：</td>
 	<td>
 		 <input type="text" name="FREE" id="FREE" class="middle_txt" datatype="1,is_null,20" value="<%=po.getFree()==null?"0":po.getFree()%>"/>&nbsp;<font color="red">*</font>
 		 <input type="hidden" id="LABOUR_PRICE" name="LABOUR_PRICE" value="<%=po.getLabourPrice()==null?"0":po.getLabourPrice()%>" />
 		 <input type="hidden" id="PART_PRICE" name="PART_PRICE" value="<%=po.getPartPrice()==null?"0":po.getPartPrice()%>" />
 	</td>
 </tr>
   <tr>
 	<td class="table_add_2Col_label_5Letter" style="text-align:right">新车整备费：</td>
 	<td>
 		 <input type="text" name="textNewCarFee" id="textNewCarFee" class="middle_txt" blurback="true" value="<%=po.getNewCarFee()==null?"0":po.getNewCarFee()%>"/>&nbsp;<font color="red">*</font>
 	</td>
 	<td class="table_add_2Col_label_5Letter" style="text-align:right">首保条件：</td>
 	<td>
 		 <select name="selFirstMainCondition" id="selFirstMainCondition" class="u-select">
			<option value="">-请选择-</option>
			<c:forEach items="${list }" var="firstMainConditionMap">
				<c:if test="${firstMainConditionMap.ID == QamaintainId}">
					<option value="${firstMainConditionMap.ID }" selected>首保里程：${firstMainConditionMap.END_MILEAGE }&nbsp;-&nbsp;首保时间：${firstMainConditionMap.MAX_DAYS }</option>
				</c:if>
				<c:if test="${firstMainConditionMap.ID != QamaintainId}">
					<option value="${firstMainConditionMap.ID }">首保里程：${firstMainConditionMap.END_MILEAGE }&nbsp;-&nbsp;首保时间：${firstMainConditionMap.MAX_DAYS }</option>
				</c:if>
			</c:forEach>
		</select>&nbsp;<font color="red">*</font>
 </tr>
</table>

<table class="table_query" >
 <tr>
 	<td colspan="4" style="text-align:center">
 		<input class="normal_btn" type="button" name="ok" id="commitBtn" value="修改" onclick="checkFormUpdate();"/> 
 		<input class="normal_btn" name="back" type="button" onclick="_hide();" value="返回"/>
 	</td>
 </tr>
</table>
</div>
</div>
</form>
</div>
<script>
function chkCon() {
	if(chkNull("WRGROUP_NAME", "请填写车型组名称！")) return false ;
	if(chkNull("FREE", "请填写保养费用名称！")) return false ;
	if(chkNull("textNewCarFee", "请填写新车整备费！")) return false ;
	if(chkNull("selFirstMainCondition", "请选择首保条件！")) return false ;
	if(!chkPositiveDouble("FREE", "保养费用必须填写数字！")) return false ;
	if(!chkPositiveDouble("textNewCarFee", "新车整备费必须填写数字！")) return false ;
	
	return true ;
}
function chkNull(value, str) {
	if(!document.getElementById(value).value) {
		MyAlert(str) ;
		return true ;
	} 
	return false ;
}
function chkPositiveDouble(value, str) {
	var strChk = /^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/ ;
	
	if(!strChk.test(document.getElementById(value).value)) {
		document.getElementById(value).value = "" ;
		MyAlert(str) ;
		return false ;
	}
	
	return true ;
}

function blurBack(){

	var reg = /^(\d+\.\d{1,2}|\d+)$/;
	var total = 0;
	var labour = document.getElementById("LABOUR_PRICE").value;
	var part = document.getElementById("PART_PRICE").value;
	if(reg.test(labour)){
	total = total+labour*1.0;
	}
	if(reg.test(part)){
	total = total+part*1.0;
	}
	document.getElementById("FREE").value = total;//你一定会问，为什么*1， 因为是为了转化为Double
}
	function checkForm(){
			document.getElementById("commitBtn").disabled = true ;
			makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimModelMain/claimModelUpdate.json',showResult,'fm');			
	}
	
	function showResult(json){
		if(json.ACTION_RESULT == '1'){
		MyAlert("修改成功!");
		
		__parent().__extQuery__(1);
		_hide();
		}
	}
	function checkFormUpdate(){
		if(!chkCon()) return false ;
		
		document.getElementById("PART_PRICE").value = document.getElementById("FREE").value ;
		if(!submitForm('fm')) {
			return false;
		}			
		MyConfirm("是否确认修改?",checkForm);
	}
</script>
</body>
</html>
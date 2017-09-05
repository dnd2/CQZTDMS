<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>配件主信息维护</title>
<script type="text/javascript" >
// 返回
function goback(){
	btnDisable();
	window.location.href = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartVenderManager/partVenderRelationInit.do";
}

// 检查比例数字
function checkNum(obj) {
	obj.value = obj.value.replace(/\D/g,'');
	var value = obj.value;
	// 数字不能大于两位数
	if(value.length > 3){
		obj.value = value.substring(0, 3);
	}
}

// 保存
function save(){
	var avg = 0;
	var cofeeNumInp = document.getElementsByName("COEEF_NUM");
	var priceIds = "";
	var coeffNums = ""
	for(var i = 0; i < cofeeNumInp.length; i++){
		var value = document.getElementById("COEEF_NUM_"+i).value;
    	if(value != ''){
    		value = parseInt(value);
    	}else{
    		value = 0;
    	}
    	avg += value;
    	coeffNums += value + ",";
    	priceIds += document.getElementById("PRICE_ID_"+i).value + ",";
	}
	if(avg != 100){
		layer.msg('所有比例值的和必须等于100！', {icon: 2});
		return;
	}
	document.getElementById('PRICE_ID').value = priceIds.substring(0, priceIds.length - 1);
	document.getElementById('COEEF_NUMS').value = coeffNums.substring(0, coeffNums.length - 1);
	
    btnDisable();
    var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartVenderManager/savePartVenderRelation.json';
    makeNomalFormCall(url, saveSuccess, 'fm');
}

// 保存成功返回
function saveSuccess(json){
	btnEnable();
	if(json.returnCode == '1'){
		layer.msg('保存成功！', {icon: 1});
		goback();
	}else{
		MyAlert('比例设置失败！')
	}
}

</script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt;基础信息管理 &gt; 配件基础信息维护 &gt; 配件供应商供货比例维护</div>
<form method="post" name ="fm" id="fm" enctype="multipart/form-data">
	<input type="hidden" id="PART_ID" name="PART_ID" value="${partMap.PART_ID }">
	<input type="hidden" id="PRICE_ID" name="PRICE_ID">
	<input type="hidden" id="COEEF_NUMS" name="COEEF_NUMS">
	<div class="form-panel">
    <h2><img src="<%=request.getContextPath()%>/img/subNav.gif"/>&nbsp;配件信息</h2>
    <div class="form-body">
	<table class="table_query">
	    <tr>
	      <td class="right">配件编码：</td>
	      <td>
			<input class="middle_txt" type="text" name="PART_CODE" value="${partMap.PART_CODE }" disabled/>
	      </td>
	      <td class="right">配件名称：</td>
	      <td>
	      	<input class="middle_txt" type="text"  name="PART_NAME" value="${partMap.PART_CNAME }" disabled/></td>
	      </td>
      </tr>
      <tr>
		<td colspan="6" class="center" >
			<input type="button" name="saveBtn" id="saveBtn" value="保 存" onclick="save();" class="u-button"/>
			<input type="button" name="backBtn" id="backBtn" value="返 回" onclick="goback();" class="u-button"/>
         </td>
	  </tr>
	</table>
	</div>
	</div>
    <table id="file" class="table_list" style="border-bottom:1px solid #DAE0EE;">
		<caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/> 供应商比例设置</caption>
		<tr class="table_query">
			<th style="word-wrap: break-word;word-break: break-all;">序号</th>
			<th style="text-align: center">供应商编码</th>
			<th style="text-align: center">供应商名称</th>
			<th style="wtext-align: center">比例</th>
		</tr>
		<c:choose>
			<c:when test="${not empty relationList }">
				<c:forEach items="${relationList }" var="var" varStatus="vars">
					<tr class="table_list_row1">
						<td style="text-align: center">${vars.count }</td>
						<td style="text-align: center">${var.VENDER_CODE }</td>
						<td style="text-align: center">${var.VENDER_NAME }</td>
						<td style="text-align: center">
							<input type="hidden" id="PRICE_ID_${vars.index }" value="${var.PRICE_ID }">
							<input type="text" id="COEEF_NUM_${vars.index }" name="COEEF_NUM" onkeyup="checkNum(this)" value="${var.COEFF_NUM }" style="width: 50px;">&nbsp;%
						</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
			
			</c:otherwise>
		</c:choose>
    </table>
</form>
</div>
</body>
</html>
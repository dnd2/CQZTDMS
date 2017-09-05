<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
    String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<title>配件货位关系维护-新增</title>
<jsp:include page="${path}/common/jsp_head_new.jsp" />
<script type="text/javascript">

function checkData(){
	var partCode=document.getElementById("PART_CODE");/**配件编码*/
	var whCode=document.getElementById("WH_CODE");/**仓库编码*/
	var locCode=document.getElementById("LOC_CODE");/**货位编码*/
	var locName=document.getElementById("LOC_NAME");/**货位名称*/
	if(partCode.value==null || partCode.value==""){
		MyAlert("配件编码不能为空！");
		return  false;
	}
	if(whCode.value==null || whCode.value==""){
		MyAlert("仓库编码不能为空！");
		return  false;
	}
	if(locCode.value==null || locCode.value==""){
		MyAlert("货位编码不能为空！");
		return  false;
	}else{
		var reg = /^[A-Z]+\-([A-Z]{1}[0-9]{1})+\-([0-9]{3})+$/;
 		if (!reg.test(locCode.value)) {
		 	MyAlert("请输入正确的货位编码格式！正确格式：A-A1-001");
			return false;
 		}
	}
	if(locName.value==null || locName.value==""){
		MyAlert("货位名称不能为空！");
		return  false;
	}
	return true;
}

   //提交
function add(){
	if(checkData()==true){
		if(submitForm('fm')){
			MyConfirm("确定新增？",confirmResult);
		}
	}
}

function confirmResult(){
	btnDisable();
	makeNomalFormCall('<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/savePartLocation.json',showResult,'fm');
}

function showResult(json){
	btnEnable();
	if(json.error!=null){
		MyAlert(json.error);
	}else if(json.success =="success"){
		MyAlert("配件货位新增成功!", function(){
			location.href="<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/partLocationInit.do";
		});
	}else{
		MyAlert("未知错误!");
	}
}
//清空配件信心
function clrPartTxt(pCode, PId, pName){
   	document.getElementById(pCode).value = "";
   	document.getElementById(PId).value = "";
   	document.getElementById(pName).value = "";
}

//清空仓库信息
function clrWHTxt(whCode, whId){
   	document.getElementById(whCode).value = "";
   	document.getElementById(whId).value = "";
}

   //选择配件
function selPart(){
   	OpenHtmlWindow('<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/selPartInit.do',700,400);
}
function selWh(){
	OpenHtmlWindow('<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/selWhInit.do',700,400);
}
//回调方法 
function setPartList(id,code,name){
	$("#PART_ID")[0].value=id;
	$("#PART_CODE")[0].value=code;
	$("#PART_NAME")[0].value=name;
}
function setWhList(id,code){
	$("#WH_ID")[0].value=id;
	$("#WH_CODE")[0].value=code;
}

function goBack(){
	btnDisable();
    window.location.href = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/partLocationInit.do";
}
</script>

</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 基础信息管理 &gt; 配件基础信息维护 &gt; 配件货位关系维护 &gt; 新增
		</div>
		<form name="fm" id="fm" method="post">
			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/nav.gif" /> 配件货位关系
				</h2>
				<div class="form-body">
					<table class="table_query">
						<td class="right">配件编码：</td>
						<td nowrap="nowrap">
							<input type="text" class="middle_txt" value="" readonly="readonly" name="PART_CODE" id="PART_CODE" datatype="0,is_null,100" />
							<input name="button2" type="button" class="mini_btn" onclick="selPart()" value="..." />
							<input type="button" class="normal_btn" onclick="clrPartTxt('PART_CODE', 'PART_ID', 'PART_NAME');" value="清 空" id="clrPartBtn" />
							<input type="hidden" name="PART_ID" id="PART_ID" value="" name="PART_ID" />
						</td>
						<td class="right">配件名称：</td>
						<td>
							<input type="text" class="middle_txt" value="" readonly="readonly" id="PART_NAME" name="PART_NAME" />
						</td>
						<td class="right">仓库编码：</td>
						<td nowrap="nowrap">
							<input type="text" class="middle_txt" value="" readonly="readonly" name="WH_CODE" ID="WH_CODE" datatype="0,is_null,100" />
							<input name="button2" type="button" class="mini_btn" onclick="selWh();" value="..." />
							<input type="button" class="u-button" onclick="clrWHTxt('WH_CODE','WH_ID');" value="清 空" id="clrBtn" />
							<input type="hidden" id="WH_ID" value="" name="WH_ID" />
						</td>
						</tr>
						<tr>
							<td class="right">货位编码：</td>
							<td>
								<input type="text" class="middle_txt" value="" name="LOC_CODE" id="LOC_CODE" datatype="0,is_null,100" />
								<td class="right">货位名称：</td>
								<td>
									<input type="text" class="middle_txt" value="" name="LOC_NAME" id="LOC_NAME" datatype="0,is_null,100" />
								</td>
								<td class="right">附属货位：</td>
								<td>
									<input type="text" class="middle_txt" name="SUB_LOC" />
								</td>
						</tr>
					</table>
					<table class="table_edit" width="100%">
						<tr>
							<td align="center">
								<input class="u-button" name="saveBtn" id="saveBtn" value="保 存" onclick="add()" type="button" />
								&nbsp;&nbsp;
								<input class="u-button" name="saveBtn" id="saveBtn" value="返 回" onclick="goBack()" type="button" />
							</td>
						</tr>
					</table>
				</div>
		</form>
	</div>
</body>
</html>

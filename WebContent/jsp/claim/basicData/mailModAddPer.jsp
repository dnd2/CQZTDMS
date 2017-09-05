<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>单据里程修改</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;单据里程修改新增</div>
<form name='fm' id='fm' method="post">
<input type="hidden" id="dealerId" class="middle_txt" name="dealerId" value=""/>&nbsp;&nbsp;
<input type="hidden" id="mod_before"  class="middle_txt" name="mod_before"/>&nbsp;&nbsp;
<input type="hidden" id="mod_system"  class="middle_txt" name="mod_system"/>&nbsp;&nbsp;
<input type="hidden" id="vin"  class="middle_txt" name="vin"/>&nbsp;&nbsp;
<table class="table_edit" >
	<tr>
 		<td width="10%" align="right">错误单据号：</td>
 		<td width="20%"  >
 		<input type="text" id="roNo" class="middle_txt" onblur="getDetail(this.value);" name="roNo"/>&nbsp;&nbsp;
 		<span style="color:red">*</span></td>
 		<td width="10%" align="right">错误单据经销商：</td>
 		<td width="20%" id="dealerName">
 		</td>
	</tr>
	
	<tr>
 		<td width="10%" align="right">单据里程：</td>
 		<td width="20%" id="modBefore">
 		
 		</td>
 		<td width="10%" align="right">系统里程：</td>
 		<td width="20%" id="modSystem">
 		
 		</td>
	</tr>
		<tr>
 		<td width="10%" align="right">VIN：</td>
 		<td width="20%" id="vin2">
 		
 		</td>
 		<td width="10%" align="right">是否生成索赔结算单：</td>
 		<td width="20%" id="hasClaim">
 		
 		</td>
	</tr>
	<tr>
 		<td width="10%" align="right">变更后数据：</td>
 		<td width="20%" colspan="3">
 		<input type="text" id="mod_after" onblur="checkData();"  class="middle_txt" name="mod_after"/><span style="color: red">*</span>
 		</td>
	</tr>
	<tr>
 		<td colspan="4" align="center">
 			<input class="normal_btn" type="button" value="保存" id="saves" onclick="save();"/> 
 			<input class="normal_btn" type="button" value="返回" id="backs" onclick="history.back();"/> 
 		</td>
	</tr>
</table>
<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />

</form>
<script type="text/javascript">
function checkData(){
	var after = $('mod_after').value;
	var reg = /^\d+$/;
		if (after!=null&&after!=""){
			if(!reg.test(after)){
				MyAlert("修改后数据请输入正整数!");
				$('mod_after').value="";
				return false;
			}
		if(parseInt(after)<1){
			MyAlert("修改后数据必须大于0!");
			$('mod_after').value="";
			return false;
	}
}
}
function save(){

	var roNo = $('roNo').value;
	var before = $('mod_before').value;
	var sys = $('mod_system').value;
	var after = $('mod_after').value;
	if(roNo==""){
		MyAlert("请输入错误工单号!");
		return false;
	}
	if(after==""){
		MyAlert("请输入修改后的数据!");
		return false;
	}
	if(parseInt(after)>parseInt(sys)){
		MyAlert("修改后数据不能大于系统里程!");
		return false;
	}
	if(parseInt(before)!=parseInt(sys)){
		MyAlert("单据里程和系统里程存在差异,不能修改!");
		return false;
	}
	if(parseInt(after)<1){
		MyAlert("修改后数据必须大于0!");
		return false;
	}
	if(parseInt(sys)>50000){
		MyAlert("系统里程数已经大于50000,不能在此处修改,请走流程后台修改!");
		return false;
	}
	MyConfirm("确定修改？",confirmAdd0,[]);
}
function  confirmAdd0(){
$('saves').disabled=true;
$('backs').disabled=true;
	url= "<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/update.json";
		makeNomalFormCall(url,showResult,'fm');
}
function showResult(json){
$('saves').disabled=false;
$('backs').disabled=false;
	if(json.ok=="ok"){
		MyAlert("修改成功!");
		fm.action = "<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/mileageModify.do";
		fm.submit();
	}else{
		MyAlert("修改失败,请联系管理员!");
		return false;
	}
}
	function getDetail(roNo){
	if(roNo==""){
	MyAlert("请输入完整错误单据号(工单号)");
	return false;
	}else{
		url= "<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/getDetail.json?roNo="+roNo;
		makeNomalFormCall(url,showResult2,'fm');
		}
	}
	function showResult2(json){
	if(!json.roInfo){
		$('dealerId').value="";
		$('mod_before').value="";
		$('mod_system').value="";
		$('vin').value="";
		$('dealerName').innerHTML="";
		$('modBefore').innerHTML="";
		$('modSystem').innerHTML="";
		$('vin2').innerHTML="";
		$('hasClaim').innerHTML="";
		MyAlert("没有找到相关工单信息!");
		return false;
	}else{
		var ro = json.roInfo;
		$('dealerId').value=ro.DEALER_ID;
		$('mod_before').value=ro.IN_MILEAGE;
		$('mod_system').value=ro.MILEAGE;
		$('vin').value=ro.VIN;
		$('dealerName').innerHTML=ro.DEALER_NAME;
		$('modBefore').innerHTML=ro.IN_MILEAGE;
		$('modSystem').innerHTML=ro.MILEAGE;
		$('vin2').innerHTML=ro.VIN;
		$('hasClaim').innerHTML=ro.HAS_CLAIM;
	}
	}
</script>
</body>
</html>

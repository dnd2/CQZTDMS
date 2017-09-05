<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
    String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<title>配件仓库修改</title>
<jsp:include page="${path}/common/jsp_head_new.jsp" />
<script type="text/javascript">
   //提交
function mod(){
	var linkMan = document.getElementById("linkMan").value;
    var telPhone = document.getElementById("telPhone").value;
    var partsWareHouseType = document.getElementById("partsWareHouseType").value;
    if("" != linkMan){
    	var recName = /^[a-zA-Z\u4E00-\u9FA5]{0,200}$/;
    	if(!recName.test(linkMan)){
    		MyAlert("联系人名称输入格式有误!" );
    		return false;
    	}
    }
    if("" != telPhone){
    	var pattern =/((^[0-9]{3,4}\-[0-9]{7,8})(-(\d{3,}))?$)|(^[0-9]{7,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^1[0-9]{10}$)/;
  			if(!pattern.exec(telPhone)){
  				MyAlert("请输入正确的联系电话!");
  				$("#telPhone")[0].value="";
  				$("#telPhone")[0].focus();
  				return;
           }
	}
    if(partsWareHouseType=="" || partsWareHouseType==null){
    	MyAlert("请选择仓库类型！");
		return  false;
    }
            
//         	var provinceId = document.getElementById("PROVINCE_ID");//出发地
//         	var cityId = document.getElementById("CITY_ID");
 	var counties = document.getElementById("COUNTIES");
 	if(counties.value==null || counties.value==""){
 		MyAlert("地址省市县不能为空，请选择！");
 		return  false;
 	}
	if(submitForm('fm')){
		btnDisable();
		makeNomalFormCall('<%=contextPath%>/parts/baseManager/partsBaseManager/PartWareHouse/updatePartWareHouse.json',showResult,'fm');
	}
}
function showResult(json){
 	btnEnable();
 	var exception = json.Exception;
 	if(json.error!=null){
 		MyAlert(json.error);
 	}else if(json.success =="success"){
  		MyAlert("修改成功!");
  	    location.href='<%=contextPath%>/parts/baseManager/partsBaseManager/PartWareHouse/partWareHouseInit.do';
   }else if(exception){
   		MyAlert(exception.message);
	}
}
//清空
function clrTxt(txtId) {
    document.getElementById(txtId).value = "";
}
 function back(){
	 btnDisable();
     window.location.href='<%=contextPath%>/parts/baseManager/partsBaseManager/PartWareHouse/partWareHouseInit.do';
}

function setItemValue(selectName, objItemValue) {
    var objSelect = document.getElementById(selectName);
    if(!objSelect) {return;}
    if(!objItemValue || objItemValue == '-1' || objItemValue == '') {return;}
    for (var i = 0; i < objSelect.options.length; i++) {
        if (objSelect.options[i].value == objItemValue) {
            objSelect.options[i].selected = true;
            break;
        }
    }
}
$(document).ready(function(){
	//地址
	genLocSel('PROVINCE_ID','CITY_ID','COUNTIES');
	var p = document.getElementById("PROVINCE_ID");
	setItemValue('PROVINCE_ID', '${po.provinceId}');
	_genCity(p,'CITY_ID');
	var c = document.getElementById("CITY_ID");
	setItemValue('CITY_ID', '${po.cityId}');
	_genCity(c,'COUNTIES');
	var t = document.getElementById("COUNTIES");
	setItemValue('COUNTIES', '${po.counties}');		
});

</script>
</head>
<body>
<div class="wbox"> 	
	<form name="fm" id="fm" method="post">
		<input type="hidden" id="orgId" name="orgId" value="${orgId }" />
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 配件仓库维护 &gt; 修改
		</div>
		<div class="form-panel">
			<h2>
				<img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 仓库信息
			</h2>
			<div class="form-body">
				<table class="table_query">
					<tr>
						<td class="right">仓库编码：</td>
						<td width="25%">
							<input class="middle_txt" type="text" name="WH_CODE" id="WH_CODE" value="${po.whCode}" datatype="0,is_null,200" />
							<td class="right">仓库名称：</td>
							<td width="25%">
								<input class="middle_txt" type="text" name="WH_NAME" id="WH_NAME" value="${po.whName}" datatype="0,is_null,200" />
								<td class="right">所属机构：</td>
								<td width="25%">
									<input type="text" class="middle_txt" name="dealerCode" id="dealerCode" value="${orgName}" datatype="0,is_null,200" disabled="disabled" />
									<input type="hidden" name="WH_ID" id="WH_ID" value="${po.whId}" />
					</tr>
					<tr>
						<td class="right">仓库类型：</td>
						<td>
							<script type="text/javascript">
								genSelBoxExp("partsWareHouseType", <%=Constant.PARTS_WAREHOUSE_TYPE%>, "${po.whType}", true, "u-select", "", "false", '');
							</script>
						</td>
						<td class="right">联系人：</td>
						<td>
							<input class="middle_txt" type="text" id="linkMan" name="LINKMAN" value="${po.linkman }" />
						</td>
						<td class="right">联系电话：</td>
						<td>
							<input class="middle_txt" type="text" id="telPhone" name="TEL" value="${po.tel }" />
						</td>
						<td class="right">&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td class="right">地址省份：</td>
						<td>
							<select class="u-select" id="PROVINCE_ID" name="PROVINCE_ID" onchange="_genCity(this,'CITY_ID')"></select>
						</td>
						<td class="right">城市：</td>
						<td>
							<select class="u-select" id="CITY_ID" name="CITY_ID" onchange="_genCity(this,'COUNTIES')"></select>
						</td>
						<td class="right">区县：</td>
						<td>
							<select class="u-select" id="COUNTIES" name="COUNTIES" datatype="0,is_null,200"></select>
						</td>
					</tr>
					<tr>
						<td class="right">仓库地址：</td>
						<td colspan="5">
							<input class="middle_txt" type="text" name="addr" value="${po.addr}" style="width: 462px;" />
						</td>
					</tr>
					<tr>
						<td class="center" colspan="6">
							<input type="button" name="saveBtn" id="saveBtn" value="保 存" onclick="mod();" class="u-button" />
							&nbsp;&nbsp;
							<input type="button" name="backBtn" id="backBtn" value="返 回" onclick="back();" class="u-button" />
						</td>
					</tr>
				</table>
			</div>
		</div>
	</form>
	</div>
</body>
</html>

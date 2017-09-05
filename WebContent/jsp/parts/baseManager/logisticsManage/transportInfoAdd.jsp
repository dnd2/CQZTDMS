<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
    String contextPath = request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>运输方式维护-新增</title>
<script type="text/javascript" >
function addTransport(){ 	
	if(checkData()==true){
		MyConfirm("确认添加该信息！",addTransportInfo);
	}
}
function addTransportInfo()
{ 
	disabledButton(["saveButton","goBack"],true);
	makeNomalFormCall("<%=contextPath%>/parts/baseManager/logisticsManage/TransportInfoAction/transportInfoAdd.json",addTransportBack,'fm','queryBtn'); 
}
function addTransportBack(json){
	if(json.returnValue == 1){
		parent.MyAlert("操作成功！");
		fm.action = "<%=contextPath%>/parts/baseManager/logisticsManage/TransportInfoAction/transportInfoInit.do";
		fm.submit();
	}else if(json.returnValue == 2){//添加失败 
		disabledButton(["saveButton","goBack"],false);
		MyAlert("操作失败！该运输信息已存在");
	}else{
		disabledButton(["saveButton","goBack"],false);
		MyAlert("操作失败！请联系系统管理员！");
	}
}

function checkData(){
	var logiCode=document.getElementById("logiCode");/**承运商*/
	var tvCode=document.getElementById("tvCode");/**运输计划类型*/
	var price = document.getElementById("price");/**价格*/
	var isStatus = document.getElementById("isStatus");/**状态*/
	
	
	var provinceId = document.getElementById("PROVINCE_ID");//出发地
	var cityId = document.getElementById("CITY_ID");
	var counties = document.getElementById("COUNTIES");
	var provinceId1 = document.getElementById("PROVINCE_ID1");//目的地
	var cityId1 = document.getElementById("CITY_ID1");
	var counties1 = document.getElementById("COUNTIES1");
	
	if(logiCode.value==null || logiCode.value==""){
		MyAlert("承运商不能为空！");
		return  false;
	}
	if(tvCode.value==null || tvCode.value==""){
		MyAlert("运输计划类型！");
		return  false;
	}
	if(price.value==null || price.value==""){
		MyAlert("价格不能为空！");
		return  false;
	}else{
		var reg = /^\d+(\.\d{1,2})?$/;
 		if (!reg.test(price.value)) {
		 	MyAlert("请输入正确的价格格式！正确格式：整数或两位小数");
			return false;
 		}
	}
	if(isStatus.value==null || isStatus.value==""){
		MyAlert("状态不能为空！");
		return false;
	}
	if(counties.value==null || counties.value==""){
		MyAlert("出发地不能为空！");
		return  false;
	}
	if(counties1.value==null || counties1.value==""){
		MyAlert("目的地不能为空！");
		return  false;
	}
	return true;
}

function back(){
	fm.action="<%=contextPath%>/parts/baseManager/logisticsManage/TransportInfoAction/transportInfoInit.do";
	fm.submit();
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

// $(document).ready(function(){
// 	__extQuery__(1);
// });

$(function(){
	//出发地
	genLocSel('PROVINCE_ID','CITY_ID','COUNTIES');
	var p = document.getElementById("PROVINCE_ID");
	setItemValue('PROVINCE_ID', '${dMap.PROVINCE_ID}');
	_genCity(p,'CITY_ID');
	var c = document.getElementById("CITY_ID");
	setItemValue('CITY_ID', '${dMap.CITY_ID}');
	_genCity(c,'COUNTIES');
	var t = document.getElementById("COUNTIES");
	setItemValue('COUNTIES', '${dMap.COUNTIES}');
	//目的地
	genLocSel('PROVINCE_ID1','CITY_ID1','COUNTIES1');
	var p1 = document.getElementById("PROVINCE_ID1");
	setItemValue('PROVINCE_ID1', '${dMap.PROVINCE_ID}');
	_genCity(p1,'CITY_ID1');
	var c1 = document.getElementById("CITY_ID1");
	setItemValue('CITY_ID1', '${dMap.CITY_ID}');
	_genCity(c1,'COUNTIES1');
	var t1 = document.getElementById("COUNTIES1");
	setItemValue('COUNTIES1', '${dMap.COUNTIES}');
	
})

</script>

</head>

<body>
	<div class="wbox"  style="min-width: 568px;">
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理 &gt;基础信息管理 &gt; 配件基础信息维护 &gt; 运输方式维护(新增)
	</div>
	<form name="fm" method="post" id="fm">
		<div class="form-panel">
			<h2>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 承运商
			</h2>
			<div class="form-body">
				<table class="table_query" id="subtab">
					<tr>
						<td class="right">承运商：</td>
						<td>
							<select name="logiCode" id="logiCode" class="u-select">
								<option value="">--请选择--</option>
								<c:forEach items="${logisticsList}" var="logi">
									<option value="${logi.LOGI_CODE}">${logi.LOGI_FULL_NAME}</option>
								</c:forEach>
							</select>
						</td>
						<td class="right">运输计划类型：</td>
						<td>
							<select name="tvCode" id="tvCode" class="u-select">
								<option value="">--请选择--</option>
								<c:forEach items="${modeList}" var="mode">
									<option value="${mode.TV_ID}">${mode.TV_NAME}</option>
								</c:forEach>
							</select>
						</td>
						<td class="right">价格：</td>
						<td>
							<input type="text" id="price" name="price" class="middle_txt" />
						</td>
					</tr>
					<tr>
						<td class="right">出发地 省份：</td>
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
						<td class="right">目的地 省份：</td>
						<td>
							<select class="u-select" id="PROVINCE_ID1" name="PROVINCE_ID1" onchange="_genCity(this,'CITY_ID1')"></select>
						</td>
						<td class="right">城市：</td>
						<td>
							<select class="u-select" id="CITY_ID1" name="CITY_ID1" onchange="_genCity(this,'COUNTIES1')"></select>
						</td>
						<td class="right">区县：</td>
						<td>
							<select class="u-select" id="COUNTIES1" name="COUNTIES1" datatype="0,is_null,200"></select>
						</td>
					</tr>

					<tr>
						<td class="right">是否有效：</td>
						<td colspan="4">
							<script type="text/javascript">
								genSelBoxExp("isStatus",
							<%=Constant.STATUS%>
								, "${transInfoMap.STATUS}", true, "u-select", "", "false", '');
							</script>
						</td>
					</tr>
					<tr>
						<td colspan="6" class="center">
							<input type="button" name="button1" id="saveButton" class="u-button" onclick="addTransport();" value="保存" />
							<input type="button" name="button2" id="goBack" class="u-button" onclick="back();" value="返回" />
						</td>
					</tr>
				</table>
			</div>
		</div>
		<!-- 查询条件 end -->
		<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		<!--分页 end -->
		<!--页面列表 begin -->
	</form>
	</div>
</body>
</html>

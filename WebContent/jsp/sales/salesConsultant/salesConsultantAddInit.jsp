<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
	String isSpecialCar = String.valueOf(request.getAttribute("isSpecialCar"));
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>销售顾问申请</title>
<script type="text/javascript">
<!--
function doInit()
{  
	loadcalendar();   //初始化时间控件
}

function checkNowDate(value) {
	var aDate = document.getElementById("sys_date__").value.split(",") ;
	var nowDate = aDate[0] + "-" + aDate[1] + "-" + aDate[2] ;
	
	if(value > nowDate) {
		return false ;
	} else {
		return true ;
	}
}

function addSubmit() {
	if(submitForm("fm")) {
		if(!checkNowDate(document.getElementById("bornYear").value)) {
			MyAlert("出生日期不能大于当前日期!") ;
			
			return false ;
		}
		
		if(!checkNowDate(document.getElementById("tradeYear").value)) {
			MyAlert("从事汽车行业日期不能大于当前日期!") ;
			
			return false ;
		}
		
		if(!checkNowDate(document.getElementById("chanaTradeYear").value)) {
			MyAlert("从事长安汽车行业日期不能大于当前日期!") ;
			
			return false ;
		}
		
		var textStr = document.getElementById("reason").value ;
		
		if(textStr.length == 0) {
			MyAlert("申请原因不能为空！") ;
			
			return false ;
		}
		//验证身份证号码
		if(!isIdentityNumber()) {
			return false;
		};
		MyConfirm("确认提交?", submitSure) ;
	}
}

function submitSure() {
	document.getElementById("addSub").disabled = true ;
	var url = "<%=contextPath%>/sales/salesConsultant/SalesConsultant/submitAdd.json";
	makeFormCall(url, SubmitTip, "fm") ;
}

function SubmitTip(json) {
	var subFlag = json.subFlag ;
	
	if(subFlag == 'success') {
		MyAlert("申请成功!") ;
		
		$('fm').action= "<%=contextPath%>/sales/salesConsultant/SalesConsultant/submitInit.do";
		$('fm').submit();
	} else {
		document.getElementById("addSub").disabled = false ;
		MyAlert("申请失败!") ;
	}
}

//添加验证身份证号码 2012-09-04 韩晓宇
function isIdentityNumber() {
	var identityNumber = document.getElementById("identityNumber").value
	var sexOnPage = document.getElementById('sex').value;
	identityNumber = identityNumber.toUpperCase();  
    //身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X。   
    if(!(/(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test(identityNumber))) { 
    	MyAlert('输入的身份证号长度不对，或者号码不符合规定！15位号码应全为数字，18位号码末位可以为数字或X。'); 
        return false; 
    };
    
    var len = identityNumber.length;
    var year;
    var month;
    var day;
    var sex;
    var bornYear = document.getElementById("bornYear").value;
    if(len == 15) {
    	year = identityNumber.substr(6,2);
    	year += '19';
        month = identityNumber.substr(8,2);     
        day = identityNumber.substr(10,2); 
        sex = identityNumber.substr(14);
    } else if(len == 18) {
    	year = identityNumber.substr(6,4);     
        month = identityNumber.substr(10,2);     
        day = identityNumber.substr(12,2); 
        sex = identityNumber.substr(16,1);
    }
    if(bornYear != (year+'-'+month+'-'+day)) {
    	MyAlert("输入的身份证号的出生日期和用户选择的出生日期不匹配!");
    	return false;
    }
    if(sex % 2 == 0) {
    	if(sexOnPage == "10031001") {
    		MyAlert("输入的身份证号的性别和用户选择的性别不匹配!");
    		return false;
    	} 
    } else {
    	if(sexOnPage == "10031002") {
    		MyAlert("输入的身份证号的性别和用户选择的性别不匹配!");
    		return false;
    	} 
    }
    return true;
}

//-->
</script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 销售顾问管理 &gt; 销售顾问申请</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="curPage" id="curPage" value="1" />
	<table class="table_query" border="0">
		<tr>
			<td align="right">经销商选择：</td>
			<td align="left">
			<select name="dealerId" id="dealerId">
				<c:forEach var="dlrList" items="${dlrList }">
					<option value="${dlrList.DEALER_ID }">${dlrList.DEALER_NAME }-${dlrList.DEALER_CODE }
					</option>
				</c:forEach>
			</select>
			</td>
		</tr>
		<tr>
			<td align="right">姓名：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="salesConName" id="salesConName" value="" datatype="0,is_textarea,30" />
			</td>
		</tr>
		<tr>
			<td align="right">身份证号：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="identityNumber" id="identityNumber" value="" datatype="0,is_textarea,18" />
			</td>
		</tr>
		<tr>
			<td align="right">性别：</td>
			<td align="left">
				<script type="text/javascript">
	                genSelBoxExp("sex",<%=Constant.GENDER_TYPE%>,"",false,"mini_sel","","false",'');
	            </script>
			</td>
		</tr>
		<tr>
			<td align="right">出生日期：</td>
			<td align="left">
				<input class="short_txt" readonly="readonly" type="text" id="bornYear" name="bornYear" datatype="0,is_date,10"/>
				<input class="time_ico" type="button" onclick="showcalendar(event, 'bornYear', false);" value="&nbsp;" />
			</td>
		</tr>
		<tr>
			<td align="right">学历：</td>
			<td align="left">
				<script type="text/javascript">
	                genSelBoxExp("academicRecords",<%=Constant.SALES_CONSULTANT_RECORDS%>,"",false,"mini_sel","","false",'');
	            </script>
			</td>
		</tr>
		<tr>
			<td align="right">从事汽车行业日期：</td>
			<td align="left">
				<input class="short_txt" readonly="readonly" type="text" id="tradeYear" name="tradeYear" datatype="0,is_date,10"/>
				<input class="time_ico" type="button" onclick="showcalendar(event, 'tradeYear', false);" value="&nbsp;" />
			</td>
		</tr>
		<tr>
			<td align="right">从事长安汽车行业日期：</td>
			<td align="left">
				<input class="short_txt" readonly="readonly" type="text" id="chanaTradeYear" name="chanaTradeYear" datatype="0,is_date,10"/>
				<input class="time_ico" type="button" onclick="showcalendar(event, 'chanaTradeYear', false);" value="&nbsp;" />
			</td>
		</tr>
		<tr>
			<td align="right">联系电话：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="tel" id="tel" datatype="0,is_textarea,18" />
			</td>
		</tr>
		<tr>
			<td align="right">申请原因：</td>
			<td align="left">
				<textarea id="reason" name="reason" rows="3" cols="30"></textarea>&nbsp;<font color="red">*</font>
			</td>
		</tr>
	</table>
	<br />
	<table border="0" align="center">
	<tr>
		<td align="center" colspan="2">
			<input type="button" class="normal_btn" onclick="addSubmit();" value="提 交" id="addSub" />
			<input type="button" class="normal_btn" onclick="javascript:history.back() ;" value="返 回" id="retBtn" />
		</td>
	</tr>
</table>
</form>
</div>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件折扣率修改</title>
<style type="text/css">
.table_list_row0 td {
	background-color:#FFFFCC;
	border: 1px solid #DAE0EE;
	white-space:    nowrap;
}
</style>
</head>
<body onunload='javascript:destoryPrototype();' onload="__extQuery__(1);loadcalendar();showInfo();">
<div class="wbox">
	<div class="navigation"> <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
	  配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 配件折扣率维护 &gt; 修改
	</div>
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
    <input type="hidden" name="DISCOUNT_ID" id="DISCOUNT_ID" value="${po['DISCOUNT_ID'] }"/>
    <input type="hidden" name="dpIds" id="dpIds" value=""/>
    <input type="hidden" name="dpCodes" id="dpCodes" value=""/>
    <input type="hidden" name="dpNames" id="dpNames" value=""/>
    <input type="hidden" name="curPage" id="curPage" value="${curPage }"/>
	<table class="table_edit">
            <th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" class="nav" />信息</th>
	     <tr>
	      <td width="10%" class="table_query_right" align="right">折扣类型：</td>
	      <td width="20%">
	      <script type="text/javascript">
		       genSelBoxExp("DISCOUNT_TYPE",<%=Constant.PART_DISCOUNT_TYPE%>,${po['DISCOUNT_TYPE'] },false,"short_sel","disabled='disabled'","false",'');
	      </script>
	      </td>
	      <td width="10%" class="table_query_right" align="right">有效日期：</td>
	      <td width="30%">
	            <input name="startDate" id="t1" value=' <fmt:formatDate value="${po['VALID_FROM'] }"/>' type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2">
        		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't1', false);"/>
        		&nbsp;至&nbsp;
        		<input name="endDate" id="t2" value='<fmt:formatDate value="${po['VALID_TO'] }"/>' type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2">
        		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't2', false);"/>
	      </td>
	       <td  width="10%" class="table_query_right" align="right">折扣率：</td>
	      <td  width="20%">
	      <input class="short_txt" type="text" name="DISCOUNT_RATE" id="DISCOUNT_RATE" datatype="0,is_null" value="${po['DISCOUNT_RATE'] }"/>
	      </td>
	     
	    </tr>
	    
      <tr>
	        <td width="10%" class="table_query_right" align="right" >是否有效：</td>
      		<td width="20%">
      		<script type="text/javascript">
      		genSelBoxExp("STATE",<%=Constant.STATUS%>,${po['STATE'] },false,"short_sel","","false",'');
	       </script>
	       </td>
	       <td  width="10%"  align="right">金额：</td>
	      <td  width="20%">
	      <input class="middle_txt" type="text" name="ORDER_AMOUNT" id="ORDER_AMOUNT" value="${po['ORDER_AMOUNT'] }" datatype="0,is_null"/>
	      </td>
      </tr>
      <tr >
	        
      </tr>
        </table>
	<table class="table_edit">
            <tr>
    	<td colspan="6" align="center">
            <input type="button" name="saveBtn" id="saveBtn" value="保存" onclick="updateDiscount();"  class="normal_btn"/>
            <input type="button" name="returnBtn" id="returnBtn" value="返 回" 

onclick="javascript:goback();"  class="normal_btn"/>
        </td>
    </tr>
        </table>
</form>
<script type="text/javascript" >

function updateDiscount(){
	if (!submitForm('fm')) {
        return;
    }
	var rateObj = $("DISCOUNT_RATE");
	var flag = checkNumberLength(rateObj);
	if(!flag){
		return;
	}
	if(confirm("确定修改?")){
		btnDisable();
		$("DISCOUNT_TYPE").disabled="";
		var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartDiscountRateManager/updatePartDiscount.json?';
	    sendAjax(url, updateResult, 'fm');
	}
}

function updateResult(jsonObj){
	btnEable();
	if(jsonObj){
		var success = jsonObj.success;
	    var error = jsonObj.error;
	    var exceptions = jsonObj.Exception;
	    if(success){
		    MyAlert(success);
		    window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartDiscountRateManager/queryPartDiscountRateInit.do';
	    }else if(error){
	    	MyAlert(error);
	    }else if(exceptions){
	    	MyAlert(exceptions.message);
		}
	}
}

//验证精度不超过3的正小数
function checkNumberLength(inputObj) {
	if(inputObj.value==null||inputObj.value==""){
		MyAlert("折扣率不能为空!");
		inputObj.value="";
        inputObj.focus();
        return false;
	}
	if(isNaN(inputObj.value)){
		MyAlert("折扣率是小于等于1的正数，且小数保留精度最大为3位!");
        inputObj.value="";
        inputObj.focus();
        return false;
	}
	if(inputObj.value>1||inputObj.value==0){
		MyAlert("折扣率是小于等于1的正数，且小数保留精度最大为3位!");
        inputObj.value="";
        inputObj.focus();
        return false;
	}
    if (inputObj.value.indexOf("0.") >= 0) {
    	var pattern = /(^0\.(0{1,2}[1-9])$)|(^0\.([1-9]{1,3})$)/;
        if(!pattern.exec(inputObj.value)){
            MyAlert("折扣率是小于等于1的正数，且小数保留精度最大为3位!");
            inputObj.value="";
            inputObj.focus();
            return false;
        }
    }
    if (inputObj.value.indexOf("1.") >= 0) {
    	var pattern1 = /^1\.(0{1,3})$/;
        if(!pattern1.exec(inputObj.value)){
        	MyAlert("折扣率是小于等于1的正数，且小数保留精度最大为3位!");
            inputObj.value="";
            inputObj.focus();
            return false;
        }
    }
    return true;
}
//返回查询页面
function goback(){
	window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartDiscountRateManager/queryPartDiscountRateInit.do';
}
</script>
</div>
</body>
</html>

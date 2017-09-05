<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件运费加价设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script language="javascript" type="text/javascript">
	function doInit(){
		__extQuery__(1);
	}
</script>
</head>
<body onload="loadcalendar();">
<form name='fm' id='fm'>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：基础信息管理 &gt; 配件基础信息维护 &gt; 配件运费加价设置 &gt; 新增</div>
	<table class="table_query">
		<th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 设置条件</th>
	    <tr>
	      <td width="10%"   align="right">订单类型：</td>
		  <td width="20%">
		    <script type="text/javascript">
		   	 genSelBoxExp("orderType",<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE%>,"",true,"short_sel","","false",'');
		  </script>
		  <font color="red">*</font>
	      </td>
	      <td width="10%"   align="right">服务商类型：</td>
		  <td width="20%">
		    <select name="dealerType" id="dealerType" class="short_sel">
				<option value="">-请选择-</option>
				<c:if test="${vendList!=null}">
				<c:forEach items="${vendList}" var="list">
					<option value="${list.FIX_VALUE }">${list.FIX_NAME }</option>
				</c:forEach>
				</c:if>
			</select>
			<font color="red">*</font>
	     </td>
	      <td width="10%"   align="right">是否有效：</td>
		  <td width="20%">
		   <script type="text/javascript">
		   	 genSelBoxExp("STATE",<%=Constant.STATUS%>,"",true,"short_sel","","false",'');
		   </script>
	      </td>
        </tr>
	    <tr>
	      <td width="10%"   align="right"  >免运费次数：</td>
	      <td width="25%">
	        <input type="text" id="freeTimes"  name="freeTimes" class="middle_txt" value="" onchange="dataTypeCheck(this)" />
	        <font color="red">*(3位有效数)</font>
          </td>
          <td width="10%"   align="right" >免运费条件：</td>
	      <td width="25%" >
	        <input class="middle_txt" type="text" name="freeCondition" id="freeCondition" onchange="dataTypeCheck2(this)" value=""/>
	      <font color="red">*(8位有效数)</font>
	      </td>
	      <td width="10%"   align="right" >加收比例：</td>
	      <td width="20%" >
	      <input class="middle_txt" type="text" name="markupRatio" id="markupRatio" onchange="dataTypeCheck1(this)" value=""/>
	      <font color="red">*(<=1)</font>
	      </td>
      </tr>
      <tr>
    	<td  align="center" colspan="6" >
    	  <input type="button" name="saveBtn" id="saveBtn" value="保 存" onclick="saveData()"  class="normal_btn"/>
	      <input type="button" name="backBtn" id="backBtn" value="返 回" onclick="goBack()"  class="normal_btn"/>
    	</td>
	  </tr>
	</table>
</div>
</form>
<script type="text/javascript" >
	//数据验证
	function dataTypeCheck(obj)
	{
		var value = obj.value;
	    if (isNaN(value) || "" == value) {
	        MyAlert("请输入数字!");
	        obj.value = "";
	        return;
	    }
	    var re = /^((0)|([1-9]+[0-9]*]*))$/;
	    if (!re.test(obj.value)) {
	        MyAlert("请输入正整数!");
	        obj.value = "";
	        return;
	    }
	}

	function dataTypeCheck1(obj)
	{
		var value = obj.value;
	    if (isNaN(value) || "" == value) {
	        MyAlert("请输入数字!");
	        obj.value = "";
	        return;
	    }
	    if(1 < value)
	    {
	    	MyAlert("加收比例不能大于 1 !");
	        obj.value = "";
	        return;
	    }
	    obj.value = parseFloat(value).toFixed(2);
	}

	function dataTypeCheck2(obj)
	{
		var value = obj.value;
		if(0 == value)
		{
			obj.value = (0.00).toFixed(2);
	        return;
		}
		value = value + "";
		value = parseFloat(value.replace(new RegExp(",","g"),""));
	    if (isNaN(value) || "" == value) {
	        MyAlert("请输入数字!");
	        obj.value = (0.00).toFixed(2);
	        return;
	    }
	    if(0 > value)
	    {
		    MyAlert("免运费条件不能小于 0!");
		    obj.value = (0.00).toFixed(2);
		    return;
	    }
	    obj.value = addKannma(value.toFixed(2));
	}

	//千分格式
	function addKannma(number) {  
	    var num = number + "";  
	    num = num.replace(new RegExp(",","g"),"");   
	    // 正负号处理   
	    var symble = "";   
	    if(/^([-+]).*$/.test(num)) {   
	        symble = num.replace(/^([-+]).*$/,"$1");   
	        num = num.replace(/^([-+])(.*)$/,"$2");   
	    }   
	  
	    if(/^[0-9]+(\.[0-9]+)?$/.test(num)) {   
	        var num = num.replace(new RegExp("^[0]+","g"),"");   
	        if(/^\./.test(num)) {   
	        num = "0" + num;   
	        }   
	  
	        var decimal = num.replace(/^[0-9]+(\.[0-9]+)?$/,"$1");   
	        var integer= num.replace(/^([0-9]+)(\.[0-9]+)?$/,"$1");   
	  
	        var re=/(\d+)(\d{3})/;  
	  
	        while(re.test(integer)){   
	            integer = integer.replace(re,"$1,$2");  
	        }   
	        return symble + integer + decimal;   
	  
	    } else {   
	        return number;   
	    }   
	}

	//保存设置
	function saveData() {
		var orderType = document.getElementById("orderType").value;
    	var dealerType = document.getElementById("dealerType").value;
		var freeTimes = document.getElementById("freeTimes").value;
    	var freeCondition = document.getElementById("freeCondition").value;
    	freeCondition = freeCondition + "";
    	freeCondition = freeCondition .replace(new RegExp(",","g"),"");
    	var markupRatio = document.getElementById("markupRatio").value;

    	if("" == orderType || null == orderType)
    	{
    		MyAlert('请选择订单类型!');
			return false;
    	}

    	if("" == dealerType || null == dealerType)
    	{
    		MyAlert('请选择服务商类型!');
			return false;
    	}
    	
    	if("" == freeTimes || null == freeTimes)
    	{
    		MyAlert('请设置免运费次数!');
			return false;
    	}

    	if("" == freeCondition || null == freeCondition)
    	{
    		MyAlert('请设置免运费条件!');
			return false;
    	}

    	if("" == markupRatio || null == markupRatio)
    	{
    		MyAlert('请设置加收比例!');
			return false;
    	}
    	
		if(confirm("确定保存设置?")){
			btnDisable();
	     	var url = '<%=contextPath%>/parts/baseManager/partFreightageSet/partFreightageSetAction/insertPartFreightageSet.json';
	  		makeFormCall(url,showResult,'fm');
	    }
	}
	
	function showResult(json) {
		btnEnable();
	    if (json.errorExist != null && json.errorExist.length > 0) {
	        MyAlert(json.errorExist);
	    } else if (json.success != null && json.success == "true") {
	    	MyAlert("保存成功!");
	    	location = '<%=contextPath%>/parts/baseManager/partFreightageSet/partFreightageSetAction/partFreightageSetInit.do';          
	    } else {
	        MyAlert("保存失败，请联系管理员!");
	    }
	}

	//返回
	function goBack(){
		btnDisable();
		location = '<%=contextPath%>/parts/baseManager/partFreightageSet/partFreightageSetAction/partFreightageSetInit.do';
	}

	//失效按钮
	function btnDisable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });

	}

	//有效按钮
	function btnEnable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = "";
	    });

	}
</script>
</body>
</html>

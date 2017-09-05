<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
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
<input type="hidden" name="defineId" id="defineId" value="${defineId}" />
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：基础信息管理 &gt; 配件基础信息维护 &gt; 配件运费加价设置 &gt; 维护</div>
	<table class="table_query">
		<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 设置信息</th>
	    <tr>
	      <td width="10%"   align="right">订单类型：</td>
		  <td width="20%">
		    <input type="text" id="otName"  name="otName" class="middle_txt" value="${otName }" readonly="readonly" />
		    <input type="hidden" name="orderType" id="orderType" value="${orderType}" />
	      </td>
	      <td width="10%"   align="right">服务商类型：</td>
		  <td width="20%">
		    <input type="text" id="dtName"  name="dtName" class="middle_txt" value="${dtName }" readonly="readonly" />
		    <input type="hidden" name="dealerType" id="dealerType" value="${dealerType}" />
	      </td>
	      <td width="10%"   align="right">是否有效：</td>
		  <td width="20%">
		   <script type="text/javascript">
		   	 genSelBoxExp("STATE",<%=Constant.STATUS%>,${state},true,"short_sel","","false",'');
		   </script>
	      </td>
        </tr>
	    <tr>
	      <td width="10%"   align="right"  >免运费次数：</td>
	      <td width="20%">
	        <input type="text" id="freeTimes"  name="freeTimes" class="middle_txt" value="${freeTimes }" onchange="dataTypeCheck(this)" />
	        <font color="red">*</font>
          </td>
          <td width="10%"   align="right" >免运费条件：</td>
	      <td width="20%" >
	        <input class="middle_txt" type="text" name="freeCondition" id="freeCondition" onchange="dataTypeCheck1(this)" value="${freeCondition}"/>
	      <font color="red">*</font>
	      </td>
	      <td width="10%"   align="right" >加收比例：</td>
	      <td width="20%" >
	      <input class="middle_txt" type="text" name="markupRatio" id="markupRatio" onchange="dataTypeCheck1(this)" value="${markupRatio}"/>
	      <font color="red">*</font>
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
	    var re = /^[1-9]+[0-9]*]*$/;
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
	    obj.value = parseFloat(value).toFixed(2);
	}

	//保存设置
	function saveData() {
		var freeTimes = document.getElementById("freeTimes").value;
    	var freeCondition = document.getElementById("freeCondition").value;
    	var markupRatio = document.getElementById("markupRatio").value;
    	
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
	     	var url = '<%=contextPath%>/parts/baseManager/partFreightageSet/partFreightageSetAction/updatePartFreightageSet.json';
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

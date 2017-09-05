<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.TcTaskPlanPO" %>
<%
	String contextPath = request.getContextPath();
	TcTaskPlanPO showBean = (TcTaskPlanPO)request.getAttribute("SHOW_BEAN");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath%>/style/ext-all.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath%>/style/calendar.css" type="text/css" rel="stylesheet" />
<link href="<%=contextPath%>/style/page-info.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/ext-base.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/ext-all.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/InfoAjax.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/ext-grid-pager.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/validate/validate.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/calendar.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/dictDataList.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/dict.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/dialog.js"></script>
<title>定时任务</title>
</head>
<body onunload='javascript:destoryPrototype()' onload="ignore1();loadObj($('select2'));">
<div class="wbox">
	<div class="navigation">
	<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：系统管理 &gt;定时任务&gt;定时任务计划修改</div>
<form id="fm" method="post">
    <table class="table_info" >
      <tr>
		<td class="table_info_2col_label_5Letter" nowrap="nowrap">计划编号：</td>
        <td class="table_info_2col_input" nowrap="nowrap"><span class="table_query_3Col_input">
          <input name="text64"  type="text" class="middle_txt" value="<%=showBean.getPlanId()!=null? showBean.getPlanId():""%>"" disabled="disabled"/>
        </span></td>
	  </tr>
	  <tr>
        <td class="table_info_2col_label_5Letter" nowrap="nowrap">开始时间：</td>
        <td class="table_info_2col_input" nowrap="nowrap">
          <input id="startTime" name="PLAN_START"  type="text" class="middle_txt" value="<%=showBean.getPlanStart()!=null? showBean.getPlanStart():""%>" readonly="readonly"/>
		  <input class="time_ico" type="button" onclick="showTime(event, 'startTime', true);" value="&nbsp;" /><font color="red">   *</font>
        <td class="table_info_2col_label_6Letter" nowrap="nowrap">结束时间：</td>
        <td class="table_info_2col_input" nowrap="nowrap">
          <input id="endTime" name="PLAN_END" type="text" class="middle_txt" value="<%=showBean.getPlanEnd()!=null? showBean.getPlanEnd():""%>" readonly="readonly"/>
		  <input class="time_ico" type="button" onclick="showTime(event, 'endTime', true);" value="&nbsp;" /><font color="red">   *</font>
        </td>
      </tr>
      <tr>
        <td class="table_info_2col_label_5Letter" nowrap="nowrap">优先级：</td>
        <td class="table_info_2col_input" nowrap="nowrap"><span class="table_query_3Col_input">
          <input id="pri" name="PRIORITY"  type="text" class="middle_txt" value="<%=showBean.getPlanPriority()!=null? showBean.getPlanPriority().toString():""%>" />(0-9)<font color="red">   *</font>
        </span></td>
		<td class="table_info_2col_label_6Letter" nowrap="nowrap">状态：</td>
        <td class="table_info_2col_input" nowrap="nowrap"><span class="table_query_3Col_input">          
			<select class="short_sel" name="STATUS" id="select1"> 
	            <option value="1" <%if( showBean.getPlanStatus().intValue() == 1) {%>selected<%} %>>有效</option>
	            <option value="0" <%if( showBean.getPlanStatus().intValue() == 0) {%>selected<%} %>>失效</option>
          	</select>
        </span><font color="red">   *</font></td>
        
      </tr>
      <tr>
		<td class="table_info_2col_label_5Letter" nowrap="nowrap">类型：</td>
        <td class="table_info_2col_input" nowrap="nowrap"><span class="table_query_3Col_input">
          <select class="short_sel" name="REPEAT_TYPE" id="select2" onchange="selectObj(this);">
            <option value="YEAR" <%if( showBean.getPlanRepeatType().equals("YEAR")){%>selected<%} %>>年</option>
            <option value="MONTH" <%if( showBean.getPlanRepeatType().equals("MONTH")){%>selected<%} %>>月</option>
            <option value="WEEK" <%if( showBean.getPlanRepeatType().equals("WEEK")){%>selected<%} %>>周</option>
            <option value="DAY" <%if( showBean.getPlanRepeatType().equals("DAY")){%>selected<%} %>>天</option>
            <option value="HOUR" <%if( showBean.getPlanRepeatType().equals("HOUR")){%>selected<%} %>>小时</option>
            <option value="MINUTE" <%if( showBean.getPlanRepeatType().equals("MINUTE")){%>selected<%} %>>分钟</option>
            <option value="ONLYONE" <%if( showBean.getPlanRepeatType().equals("ONLYONE")){%>selected<%} %>>Onlyone</option>
                    </select>
        </span><font color="red">   *</font></td>
		</tr>
      <tr><td class="table_info_2col_label_5Letter" nowrap="nowrap">是否可跳过：</td>
        <td class="table_info_2col_input" nowrap="nowrap"><input id="ignore" value="1" type="checkbox" name="PLAN_IGNORE_FLAG"  onclick="ignore1()" <%if(showBean.getPlanIgnoreFlag().intValue() == 0){%>checked="checked"<%} %>/><font color="red"><span id="ignoreRequired">   *</span></font></td>
      </tr>
      <tr>
        <td class="table_info_2col_label_5Letter" nowrap="nowrap">执行方式：</td>
        <td class="table_info_2col_input" nowrap="nowrap">
		<select class="short_sel" name="PLAN_RUN_TYPE" id="select3" > 
            <option value="0" <%if( showBean.getPlanRunType().intValue() == 0) {%>selected<%} %>>立即执行</option>
            <option value="1" <%if( showBean.getPlanRunType().intValue() == 1) {%>selected<%} %>>按时间执行</option>
        </select><font color="red"><span id="runTypeRequired">   *</span></font>
        </td>
        <td class="table_info_2col_label_6Letter" nowrap="nowrap">启动超时时间：</td>
        <td class="table_info_2col_input" nowrap="nowrap">
          <input name="PLAN_START_TIMEOUT"  type="text" class="middle_txt" id="text4" disabled="disabled" value="<%=showBean.getPlanStartTimeout()!=null? showBean.getPlanStartTimeout().toString():""%>" /><span id="startTimeSpan"></span><font color="red"><span id="startTimeRequired"></span></font>
        </td>
      </tr>
      <tr>
		<td class="table_info_2col_label_5Letter" nowrap="nowrap">模式：</td>
        <td class="table_info_2col_input" nowrap="nowrap">
          <input name="PATTERN"  type="text" class="middle_txt"  id="pattern" value="<%=showBean.getPlanPattern()!=null? showBean.getPlanPattern():""%>"/><span id="tt"></span><font color="red"><span id="patternRequired">   *</span></font>
        </td>
        <td class="table_info_2col_label_6Letter" nowrap="nowrap">执行时间间隔：</td>
        <td class="table_info_2col_input" nowrap="nowrap"><span class="table_query_3Col_input">
          <input name="TASK_INTERVAL"  type="text" class="middle_txt"  id="interval" value="<%=showBean.getTaskInterval()!=null? showBean.getTaskInterval().toString():""%>"/><span id="na"></span><font color="red"><span id="intervalRequired">   *</span></font>
        </span></td>
      </tr>
    </table>
    <br/>
<table class="table_info_button" border="0">
	<tr>
		<td nowrap="nowrap" >
			<input id="saveBtn" name="button" type="button" class="normal_btn"  onclick="saveCutomer();" value="更新"/>
			<input class="normal_btn" type="button" value="返 回" onclick="goBack();"/>		
	  </td>
	</tr>
</table>
</form>
</div>
<script type="text/javascript" >
validateConfig.divCount = 10;
function  decide(){
	var flag = true;
	var regStr2 = new RegExp("^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$")//ONLYONE类型正则表达式
	var regStr3 = new RegExp("^(W[1-7])$")//周类型正则表达式
	var regStr5 = new RegExp("([1-12]-[1-31])")//年类型正则表达式
		if($("startTime").value == "" ){
			showErrMsg('startTime','开始时间为必填项！','30');
			flag = false;
		}
		if($("endTime").value == "" ){
			showErrMsg('endTime','结束时间为必填项！','30');
			flag = false;
		}
		var pattern = /^\d+$/; 
		if (!pattern.exec($("pri").value) || $("pri").value<0 || $("pri").value>9) {
		    showErrMsg('pri','优先级输入有误！','30');
			flag = false;
		}
		if($("select2").value == "" ){
			showErrMsg('select2','请选择类型！','30');
			flag = false;
		}
		if($("ignore").checked  != true ){
			if($("select3").value == "" ){
				showErrMsg('select3','请选择执行方式！','30');
				flag = false;
			}
		}
		if($("text4").disabled != true){
			if(isNaN($("text4").value) || $("text4").value<5 ||$("text4").value>=59){
				showErrMsg('text4','启动超时时间为大于或等于5小于60的数字！','43');
				flag = false;
			}
		}
		
		if($("select2").value == "ONLYONE"){
			if($("pattern").value == ""){
				showErrMsg('pattern','模式不能为空！','23');
			}else if(regStr2.exec($("pattern").value) == null){
				showErrMsg('pattern','模式输入有误！','23');
				flag = false;
			}
		}
		if($("select2").value == "WEEK"){
			if($("pattern").value == ""){
				showErrMsg('pattern','模式不能为空！','23');
			}else if(regStr3.exec($("pattern").value) == null ){
				showErrMsg('pattern','模式输入有误！','23');
				flag = false;
			}
		}
		if($("select2").value == "MONTH"){
			if($("pattern").value == ""){
				showErrMsg('pattern','模式不能为空！','23');
			}else if($("pattern").value != "M1" && $("pattern").value != "M30"){
				showErrMsg('pattern','模式输入有误！','23');
				flag = false;
			}
		}
		if($("select2").value == "YEAR"){
			if($("pattern").value == ""){
				showErrMsg('pattern','模式不能为空！','23');
			}else if(regStr2.exec($("pattern").value) == null){
				showErrMsg('pattern','模式输入有误！','23');
				flag = false;
			}
		}
		if($("select2").value == "HOUR"){
			if($("interval").value == ""){
				showErrMsg('interval','执行间隔不能为空！','26');
				flag = false;
			}else if(!pattern.exec($("interval").value) || $("interval").value<1 ||$("interval").value>24){
				showErrMsg('interval','执行间隔输入有误！','26');
				flag = false;
			}
		}
		if($("select2").value == "MINUTE"){
			if($("interval").value == ""){
				showErrMsg('interval','执行间隔不能为空！','26');
				flag = false;
			}else if(!pattern.exec($("interval").value) || $("interval").value<5 ||$("interval").value>60){
				showErrMsg('interval','执行间隔不能为空！','26');
				flag = false;
			}
		}
	return	flag;
	}
function saveCutomer(){
	var st = decide();
	if(st != true) {
		return false;
	}

		disableBtn($("saveBtn"));
		makeFormCall('<%=request.getContextPath()%>/sysmng/task/Task/taskPlanUpd.json?PLAN_IGNORE_FLAG='+$("ignore").checked+'&planId=<%=showBean.getPlanId()!=null? showBean.getPlanId():""%>',showResult,'fm');
}
function showResult(json){
	if(json.ACTION_RESULT == '1'){
		javascript:history.go(-1);
	}
}
function goBack(){
	javascript:history.go(-1);
}
function loadObj(obj){
	if(obj.value == "ONLYONE") {
		ignoreRequired.innerHTML ="";
		runTypeRequired.innerHTML ="";
		startTimeRequired.innerHTML ="";
		patternRequired.innerHTML ="  *";
		intervalRequired.innerHTML ="";
		$("startTimeSpan").innerHTML = "(5-60)";
		$("tt").innerHTML = "(yyyy-MM-dd)";
		$('interval').value = "";
		$('interval').disabled = true;
		$('ignore').disabled = false;
		$('pattern').disabled = false;
		ignore1();
	}
	if(obj.value == "HOUR" ) {
		ignoreRequired.innerHTML ="  *";
		runTypeRequired.innerHTML ="";
		startTimeRequired.innerHTML ="";
		patternRequired.innerHTML ="";
		intervalRequired.innerHTML ="  *";
		$("startTimeSpan").innerHTML = "(默认5)";
		$("tt").innerHTML = "";
		$('pattern').disabled = true;
		$('ignore').checked = true;
		$('ignore').disabled = true;
		$('interval').disabled = false;
		$('na').innerHTML = "(1-24)";
		ignore1();
	}
	if(obj.value == "MINUTE") {
		ignoreRequired.innerHTML ="  *";
		runTypeRequired.innerHTML ="";
		startTimeRequired.innerHTML ="";
		patternRequired.innerHTML ="";
		intervalRequired.innerHTML ="  *";
		$("startTimeSpan").innerHTML = "(默认5)";
		$("tt").innerHTML = "";
		$('pattern').disabled = true;
		$('ignore').checked = true;
		$('ignore').disabled = true;
		$('interval').disabled = false;
		$('na').innerHTML = "(5-60)";
		ignore1();
	}
	if(obj.value == "YEAR") {
		ignoreRequired.innerHTML ="";
		runTypeRequired.innerHTML ="";
		startTimeRequired.innerHTML ="";
		intervalRequired.innerHTML ="  *";
		patternRequired.innerHTML ="  *";
		$('na').innerHTML = "(默认1)";
		$("startTimeSpan").innerHTML = "(5-60)";
		$("text4").value = "";
		$('interval').value = "1";
		$("tt").innerHTML = "(yyyy-MM-dd)";
		$('ignore').disabled = false;
		$('interval').disabled = true;
		$('pattern').disabled = false;
	}
	if(obj.value == "MONTH") {
		ignoreRequired.innerHTML ="";
		runTypeRequired.innerHTML ="";
		startTimeRequired.innerHTML ="";
		patternRequired.innerHTML ="  *";
		intervalRequired.innerHTML ="  *";
		$('na').innerHTML = "(默认1)";
		$("startTimeSpan").innerHTML = "(5-60)";
		$("text4").value = "";
		$("tt").innerHTML = "(M1或M30)";
		$('ignore').disabled = false;
		$('interval').disabled = true;
		$('interval').value = "1";
		$('pattern').disabled = false;
	}
	if(obj.value == "WEEK") {
		ignoreRequired.innerHTML ="";
		runTypeRequired.innerHTML ="";
		startTimeRequired.innerHTML ="";
		patternRequired.innerHTML ="  *";
		intervalRequired.innerHTML ="  *";
		$('na').innerHTML = "(默认1)";
		$("startTimeSpan").innerHTML = "(5-60)";
		$("text4").value = "";
		$("tt").innerHTML = "(W1-W7)";
		$('interval').value = "1";
		$('ignore').disabled = false;
		$('interval').disabled = true;
		$('pattern').disabled = false;
	}
	if(obj.value == "DAY") {
		ignoreRequired.innerHTML ="";
		runTypeRequired.innerHTML ="";
		startTimeRequired.innerHTML ="";
		patternRequired.innerHTML ="";
		intervalRequired.innerHTML ="  *";
		$('na').innerHTML = "(默认1)";
		$("startTimeSpan").innerHTML = "(5-60)";
		$("tt").innerHTML = "";
		$('ignore').disabled = false;
		$('interval').value = "1";
		$('interval').disabled = true;
		$('pattern').disabled = true;
	}
}
function ignore1(){
	if($('ignore').checked == true) {
		startTimeRequired.innerHTML ="  *";
		runTypeRequired.innerHTML ="";
		$('select3').value = "";
		$('select3').disabled = true;
		$('text4').disabled = false;
	} else {
		startTimeRequired.innerHTML ="";
		runTypeRequired.innerHTML ="  *";
		$('select3').disabled = false;
		$('text4').value = "";
		$('text4').disabled = true;
	}
}
function loadRequired(){
	ignoreRequired.innerHTML ="";
	runTypeRequired.innerHTML ="  *";
	startTimeRequired.innerHTML ="";
	patternRequired.innerHTML ="";
	intervalRequired.innerHTML ="";
}
function selectObj(obj){
	if(obj.value == "") {
		ignoreRequired.innerHTML ="";
		runTypeRequired.innerHTML ="  *";
		startTimeRequired.innerHTML ="";
		patternRequired.innerHTML ="";
		intervalRequired.innerHTML ="";
	}
	if(obj.value == "ONLYONE") {
		ignoreRequired.innerHTML ="";
		runTypeRequired.innerHTML ="";
		startTimeRequired.innerHTML ="";
		patternRequired.innerHTML ="  *";
		intervalRequired.innerHTML ="";
		$("na").innerHTML = "";
		$("startTimeSpan").innerHTML = "(5-59)";
		$("text4").value = "";
		$("tt").innerHTML = "(yyyy-MM-dd)";
		$('interval').disabled = true;
		$('interval').value = "";
		$('ignore').checked = false;
		$('ignore').disabled = false;
		$('pattern').disabled = false;
		ignore1();
	}
	if(obj.value == "HOUR" ) {
		ignoreRequired.innerHTML ="  *";
		runTypeRequired.innerHTML ="";
		startTimeRequired.innerHTML ="";
		patternRequired.innerHTML ="";
		intervalRequired.innerHTML ="  *";
		$("startTimeSpan").innerHTML = "(默认5)";
		$("tt").innerHTML = "";
		$("text4").value = "5";
		$('pattern').value = "";
		$('pattern').disabled = true;
		$('ignore').checked = true;
		$('ignore').disabled = true;
		$('ignore').value = 0;
		$('interval').disabled = false;
		$("interval").value = "";
		$('na').innerHTML = "(1-24)";
		ignore1();
	}
	if(obj.value == "MINUTE") {
		ignoreRequired.innerHTML ="  *";
		runTypeRequired.innerHTML ="";
		startTimeRequired.innerHTML ="";
		patternRequired.innerHTML ="";
		intervalRequired.innerHTML ="  *";
		$("startTimeSpan").innerHTML = "(默认5)";
		$("tt").innerHTML = "";
		$("text4").value = "5";
		$('pattern').value = "";
		$('pattern').disabled = true;
		$('ignore').checked = true;
		$('ignore').disabled = true;
		$('ignore').value = 0;
		$('interval').disabled = false;
		$("interval").value = "";
		$('na').innerHTML = "(5-59)";
		ignore1();
	}
	if(obj.value == "YEAR") {
		ignoreRequired.innerHTML ="";
		runTypeRequired.innerHTML ="";
		startTimeRequired.innerHTML ="";
		intervalRequired.innerHTML ="  *";
		patternRequired.innerHTML ="  *";
		$('na').innerHTML = "(默认1)";
		$("startTimeSpan").innerHTML = "(5-59)";
		$("text4").value = "";
		$("tt").innerHTML = "(yyyy-MM-dd)";
		$('ignore').disabled = false;
		$('ignore').checked = false;
		$('interval').disabled = true;
		$('interval').value = "1";
		$('pattern').disabled = false;
		ignore1();
	}
	if(obj.value == "MONTH") {
		ignoreRequired.innerHTML ="";
		runTypeRequired.innerHTML ="";
		startTimeRequired.innerHTML ="";
		patternRequired.innerHTML ="  *";
		intervalRequired.innerHTML ="  *";
		$('na').innerHTML = "(默认1)";
		$("startTimeSpan").innerHTML = "(5-59)";
		$("text4").value = "";
		$("tt").innerHTML = "(M1或M30)";
		$('ignore').disabled = false;
		$('ignore').checked = false;
		$('interval').disabled = true;
		$('interval').value = "1";
		$('pattern').disabled = false;
		ignore1();
	}
	if(obj.value == "WEEK") {
		ignoreRequired.innerHTML ="";
		runTypeRequired.innerHTML ="";
		startTimeRequired.innerHTML ="";
		patternRequired.innerHTML ="  *";
		intervalRequired.innerHTML ="  *";
		$('na').innerHTML = "(默认1)";
		$("startTimeSpan").innerHTML = "(5-59)";
		$("text4").value = "";
		$("tt").innerHTML = "(W1-W7)";
		$('ignore').disabled = false;
		$('ignore').checked = false;
		$('interval').disabled = true;
		$('interval').value = "1";
		$('pattern').disabled = false;
		ignore1();
	}
	if(obj.value == "DAY") {
		ignoreRequired.innerHTML ="";
		runTypeRequired.innerHTML ="";
		startTimeRequired.innerHTML ="";
		patternRequired.innerHTML ="";
		intervalRequired.innerHTML ="  *";
		$('na').innerHTML = "(默认1)";
		$("startTimeSpan").innerHTML = "(5-59)";
		$("text4").value = "";
		$("tt").innerHTML = "";
		$('ignore').disabled = false;
		$('ignore').checked = false;
		$('interval').disabled = true;
		$('interval').value = "1";
		$('pattern').disabled = true;
		ignore1();
	}
}
</script>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>潜在客户新增(经销商端)</title>
<script type="text/javascript">
var globalContextPath ='<%=(request.getContextPath())%>';
var g_webAppName = '<%=(request.getContextPath())%>';   
var g_webAppImagePath = "<%=(request.getContextPath())%>"+"/images";

function doInit() {
   		genLocSel('txt1','txt2','txt3','','',''); // 加载省份城市和县
   		loadcalendar();//日期控件初始化

	}
//格式化时间为YYYY-MM-DD
 function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
</script>
<style>
.img {
	border: none
}
</style>
</head>

<body onload="doInit()">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：经销商实销管理&gt; 经销商库存管理 &gt; 潜在客户新增(DLR)</div>
<form id="fm" name="fm" method="post">
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden" value=""/>
<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
<table class="table_query" border="0">
	<tr>
	<td class="table_query_2Col_label_6Letter" nowrap="nowrap">客户类型：</td>
	    <td class="table_query_2Col_input" >
		 <script type="text/javascript">
 	          genSelBox("CUSTOMER_TYPE",<%=Constant.CUSTOMER_TYPE%>,"",false,"","");
	     </script>
		</td>
	  <td class="table_query_2Col_label_6Letter" nowrap="nowrap">客户名称：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
			<input class="middle_txt" type="text" datatype="0,is_letter_cn,10" maxlength="10"   id="customer_Name" name="customer_Name"/>
		</td>	 
	</tr>
	<tr>
	  	<td class="table_query_2Col_label_6Letter" nowrap="nowrap">性别：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
			<script type="text/javascript"> genSelBox("GENDER",<%=Constant.GENDER_TYPE%>,"",false,"","");</script>
		</td>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">联系手机：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
		  <input class="middle_txt"   maxlength="30" type="text" id="contactorMobile" name="contactorMobile" />
		</td>  
	</tr>
	<tr>   
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">证件类型：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
		     <script type="text/javascript"> genSelBox("DICT_CERTIFICATE",<%=Constant.DICT_CERTIFICATE_TYPE%>,"",false,"","");</script>
		</td>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">证件号码：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
		  	<input class="middle_txt" datatype="1,is_digit,30" maxlength="30" type="text" id="certificateNo" name="certificateNo"/>
		</td>
	</tr>
	<tr>
	    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">地址：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
		  <input class="middle_txt"   maxlength="30" type="text" id="address" name="address" />
		</td>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">联系电话：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
		  <input class="middle_txt"   maxlength="30" type="text" id="contactorPhone" name="contactorPhone" />
		</td>  
	</tr>
	<tr>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">传真：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
		<input class="middle_txt"  maxlength="30" type="text" id="fax" name="fax" />
		</td>  
	    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">E-MAIL：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
		  <input class="middle_txt"  maxlength="30" type="text" id="email" name="email" />
		</td>
	</tr>
	<tr>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">邮编：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
		  <input class="short_txt"  maxlength="10" type="text" id="zipCode" name="zipCode" />
		</td>    
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">行业大类：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <script type="text/javascript"> genSelBox("TRADE_TYPE",<%=Constant.TRADE_TYPE%>,"",false,"","");</script>
        </td> 
	</tr>
	<tr>
	    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">婚姻状况：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <script type="text/javascript"> genSelBox("MARRIAGE",<%=Constant.MARRIAGE_TYPE%>,"",false,"","");</script>
        </td>
        <td class="table_query_2Col_label_6Letter" nowrap="nowrap">教育水平：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <script type="text/javascript"> genSelBox("EDUCATION",<%=Constant.EDUCATION_TYPE%>,"",false,"","");</script>
        </td>
     </tr>
	<tr>
	 	<td class="table_query_2Col_label_6Letter" nowrap="nowrap">职业：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <script type="text/javascript"> genSelBox("PROFESSION",<%=Constant.PROFESSION_TYPE%>,"",false,"","");</script>
        </td>
        <td class="table_query_2Col_label_6Letter" nowrap="nowrap">职务名称：</td>
	    <td class="table_query_3Col_input" nowrap="nowrap">
	  		<input class="middle_txt"   maxlength="10" type="text" id="positionName" name="positionName" />
	    </td>
	</tr>
	<tr>
	    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">家庭月收入：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <script type="text/javascript"> genSelBox("familyIncome",<%=Constant.EARNING_MONTH%>,"",false,"","");</script>
        </td>  
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">购车目的：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <script type="text/javascript"> genSelBox("buyPurpose",<%=Constant.DICT_BUY_PURPOSE_TYPE%>,"",false,"","");</script>
        </td>  
	</tr>
	<tr>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">来店时间：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <input id="comeTime" name="comeTime" class="short_txt" name="startDate" style="width: 100px" group="startDate,endDate" />
              <input class="time_ico" onclick="showcalendar(event, 'comeTime', true);" value=" " type="button" />
        </td>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">离店时间：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
			<input id="leaveTime" class="short_txt" name="leaveTime" style="width: 100px" onchange="showStayTime()"/>
              <input class="time_ico" onclick="showcalendar(event, 'leaveTime', true);"  value=" " type="button" />
        </td>
	</tr>
		<tr>
	  	
	    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">在店时间：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <input name="stayMinute" id="stayMinute" type="text" class="short_time_txt" />分钟
        </td>
        	<td class="table_query_2Col_label_6Letter" nowrap="nowrap">来电人数：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <input name="customerNum" id="customerNum" type="text" datatype="0,isDigit,3" class="short_time_txt" />
        </td>
	</tr>
	<tr>
	    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">客户来源：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <script type="text/javascript"> genSelBox("cusSource",<%=Constant.DICT_CUS_SOURCE%>,"",false,"","");</script>
        </td> 
        <td class="table_query_2Col_label_6Letter" nowrap="nowrap">媒体类型：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <script type="text/javascript"> genSelBox("mediaType",<%=Constant.DICT_MEDIA_TYPE%>,"",false,"","");</script>
        </td> 
	</tr>
	<tr>
	    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">初始级别：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <script type="text/javascript"> genSelBox("initLevel",<%=Constant.DICT_INTENT_LEVEL%>,"",false,"true","");</script>
            <font color="red">*</font>
        </td>  
        <td class="table_query_2Col_label_6Letter" nowrap="nowrap">意向级别：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <script type="text/javascript"> genSelBox("intentLevel",<%=Constant.DICT_INTENT_LEVEL%>,"",false,"true","");</script>
            <font color="red">*</font>
        </td> 
	</tr>
	<tr>
	  	<td class="table_query_2Col_label_6Letter" nowrap="nowrap">是否首次购车：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <script type="text/javascript"> genSelBox("isFirstBuy",<%=Constant.IF_TYPE%>,"",false,"","");</script>
        </td>
	    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">是否有驾照：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <script type="text/javascript"> genSelBox("hasDriverLicense",<%=Constant.IF_TYPE%>,"",false,"","");</script>
        </td>
	</tr>
	<tr>
	  	<td class="table_query_2Col_label_6Letter" nowrap="nowrap">是否首次来店：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <script type="text/javascript"> genSelBox("isFirstCome",<%=Constant.IF_TYPE%>,"",false,"","");</script><font color="red">*</font>
        </td>
	    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">是否试驾：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <script type="text/javascript"> genSelBox("isTryDrive",<%=Constant.IF_TYPE%>,"",false,"","");</script>
        </td>
	</tr>
	<tr>
	  	<td class="table_query_2Col_label_6Letter" nowrap="nowrap">拟购车系：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
           <select id="gorupId" name="groupId" onchange="showColor();">
           		<option>--请选择--</option>
           		<c:forEach items="${series_list}" var="po">
           			<option value="${po.GROUP_ID }">${po.GROUP_NAME }</option>
           		</c:forEach>
           </select><font color="red">*</font>
        </td>
	    <td class="table_query_2Col_label_6Letter" nowrap="nowrap">选择颜色：</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
            <select id="colorName" name="colorName">
            	<option value="">--请选择--</option>
            </select>
        </td>
	</tr>
	<tr>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap" align="right">省份：</td>
		<td>
			<select class="min_sel" id="txt1" name="province" onchange="_genCity(this,'txt2')"></select>
			城市：
			<select class="min_sel" id="txt2" name="city" onchange="_genCity(this,'txt3')"></select>
			区/县：
			<select	class="min_sel" id="txt3" name="COUNTIES"></select>
		</td>
        <td class="table_query_2Col_label_6Letter" nowrap="nowrap">销售顾问：</td>
	    <td class="table_query_3Col_input" nowrap="nowrap">
	      <!-- <input class="middle_txt"   maxlength="10" type="text" id="soldBy" name="soldBy" /> -->
	      <select id ="soldBy" name="soldBy">
	      	<option value="">--请选择--</option>
	      	<c:forEach items="${list }" var="po">
	      		<option value="${po.ID }">${po.NAME }</option>
	      	</c:forEach>
	      </select><font color="red">*</font>
	  	</td> 
	</tr>
	
	<tr>
	  <td class="table_query_2Col_label_6Letter">备注：</td>
		<td colspan="3" class="tbwhite">
			<textarea name='remark' id='remark'	  rows='2' cols='28'></textarea>
		</td>
	</tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr >
	 <td align="center">
		<input name="button2" type="button" class="normal_btn" onclick="save()" value="保 存" id="saveBtn"/>&nbsp;
		<input name="button" type="button" style="width:60px;height:16px;line-height:10px;background-color:#EEF0FC;border:1px solid #5E7692;color:#1E3988;" class="normal_btn" onclick="toGoBack()" value="取 消"/>
	 </td>
	</tr>
</table>
</form>
</body>
</html>
<script>
function save(){
	var customer_Name = document.getElementById("customer_Name").value;
	var customerNum = document.getElementById("customerNum").value;
	var groupId = document.getElementById("groupId").value;
	var soldBy = document.getElementById("soldBy").value;
	if(customer_Name == ""){
		MyAlert("请填写客户名称!");
		return false;
	}
	if(customerNum == ""){
		MyAlert("请填写来店人数!");
		return false;
	}
	if(groupId == ""){
		MyAlert("请选择客户拟购车系!");
		return false;
	}
	if(soldBy == ""){
		MyAlert("请选择销售顾问!");
		return false;
	}
	MyConfirm("是否确认新增？",insert,'');
}
function insert(){
	document.getElementById("saveBtn").disabled = true;
  makeNomalFormCall('<%=contextPath%>/sysmng/usemng/PotentialCustomer/addPotentialCustomer.json',insertCall,'fm','');  
}
function insertCall(json){
  if(json.flag!= null && json.flag== true) {
	  	parent.MyAlert("新增成功！");
		toGoBack();
	} else {
		MyAlert("新增失败！请联系管理员！");
	}
}

function toGoBack() {
	window.location = "<%=contextPath%>/sysmng/usemng/PotentialCustomer/queryPotentialCustomerInit.do";
}

function showColor(){
	var groupId = document.getElementById("gorupId").value;
	sendAjax('<%=contextPath%>/sysmng/usemng/PotentialCustomer/searchColor.json?groupId='+groupId,showResult,'fm');
}
function showResult(json){
	var colorName = document.getElementById("colorName");
	colorName.options.length = 0;
	if(json.color != ""){
		var color = json.color;
		var attr = color.split(",");
		for(var i = 0 ; i < attr.length ; i++){
			colorName.options[colorName.options.length] = new Option(attr[i],attr[i]);
		}
	}else{
		colorName.options[colorName.options.length] = new Option("--请选择--","");
	}
}

function showStayTime(){
	var comeTime = document.getElementById("comeTime").value;
	var leaveTime = document.getElementById("leaveTime").value;
	sendAjax('<%=contextPath%>/sysmng/usemng/PotentialCustomer/showStayTime.json?comeTime='+comeTime+"&leaveTime"+leaveTime,showResult1,'fm');
}

function calendarCallBack(event)
{	
	showStayTime();
}
function showResult1(json){
	var stayMinute = document.getElementById("stayMinute");
	stayMinute.value = json.minute;
}

</script>
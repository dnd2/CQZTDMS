<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%
	String contextPath = request.getContextPath();
	List levellist = (List)request.getAttribute("LEVELLIST");
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>三包预警规则设置</title> 
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="wbox">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基本数据&gt;三包预警规则设置&gt;三包预警规则设置新增</div>
<form method="post" name='fm' id='fm'>
 <div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
		  <table class="table_query">
            <td style="text-align:right">预警类别：</td>
            <td>
                 <script type="text/javascript">
	              genSelBoxExp("WARNING_TYPE",<%=Constant.WANINGTIME_TYPE%>,"<%=Constant.WANINGTIME_TYPE_03%>",false,"","onchange=isCheck();","true",'');
	             </script>
            </td>
            <td id="a" style="text-align:right">总成：</td>
            <td  id="aa" >
                 <select name="ASSEMBLY" id="ASSEMBLY" class="u-select">
                 	<c:forEach var="dList" items="${assemblyDetail}" varStatus="status">
                 		<option value=${dList.PARTS_ASSEMBLY_ID}>${dList.PARTS_ASSEMBLY_NAME}</option>
                 	</c:forEach>
                 </select>
            </td>
            <td id="b" style="text-align:right">严重安全故障模式：</td>
            <td id="bb">
                 <select name="FAULT" id="FAULT" class="u-select">
                 	<c:forEach var="dList" items="${faultDetail}" varStatus="status" >
                 		<option value=${dList.FAULT_TYPE_ID}>${dList.FAULT_TYPE_NAME}</option>
                 	</c:forEach>
                 </select>
            </td>
            <td colspan="2" id="c"></td>
            <td colspan="2"></td>
          </tr>
          
         <%--  <tr id="a">
            <td id="a" style="text-align:right">总成：</td>
            <td>
                 <select name="ASSEMBLY" id="ASSEMBLY" class="u-select">
                 	<c:forEach var="dList" items="${assemblyDetail}" varStatus="status">
                 		<option value=${dList.PARTS_ASSEMBLY_ID}>${dList.PARTS_ASSEMBLY_NAME}</option>
                 	</c:forEach>
                 </select>
            </td>
            <td></td>
            <td></td>
          </tr> --%>
          <%-- <tr id="b">
            <td style="text-align:right">严重安全故障模式：</td>
            <td>
                 <select name="FAULT" id="FAULT" class="u-select">
                 	<c:forEach var="dList" items="${faultDetail}" varStatus="status" >
                 		<option value=${dList.FAULT_TYPE_ID}>${dList.FAULT_TYPE_NAME}</option>
                 	</c:forEach>
                 </select>
            </td>
            <td></td>
            <td></td>
          </tr> --%>
          <tr>
            <td style="text-align:right">预警等级：</td>
            <td>
				<script type="text/javascript">
	              genSelBoxExp("WAINING_LEVEL",<%=Constant.SWANINGTIME_LEVEL%>,"",true,"","","true",'');
	            </script>
            </td>
            <td style="text-align:right">是否累计：</td>
            <td>
				<script type="text/javascript">
	              genSelBoxExp("IS_ACCUMULATIVE",<%=Constant.IF_TYPE%>,"<%=Constant.IF_TYPE_NO%>",false,"","","true",'');
	            </script>
            </td>
            <td style="text-align:right">规则代码：</td>
            <td>
                 <input name="WARNING_CODE" id="WARNING_CODE" datatype="0,is_null,30"  type="text" class="middle_txt"/>
            </td>
          </tr>
          <tr>
            <td style="text-align:right">预警说明：</td>
            <td>
				<input name="WAINING_REMARK" id="WAINING_REMARK" type="text" class="middle_txt"/>
            </td>
            <td style="text-align:right">预警起次数：</td>
            <td>
            	<input name="WARNING_NUM_START" id="WARNING_NUM_START" datatype="0,is_null,30"  type="text" class="middle_txt"/>
            </td>
            <td style="text-align:right">预警止次数：</td>
            <td>
				<input name="WARNING_NUM_END" id="WARNING_NUM_END" datatype="0,is_null,30"  type="text" class="middle_txt"/>
            </td>
          </tr>
          <tr>
            <td style="text-align:right">有效起时限(月)：</td>
            <td>
            	<input name="VALID_START_DATE" id="VALID_START_DATE" datatype="0,is_null,30"  type="text" class="middle_txt"/>
            </td>
            <td style="text-align:right">有效止时限(月)：</td>
            <td>
            	<input name="VALID_DATE" id="VALID_DATE" datatype="0,is_null,30"  type="text" class="middle_txt"/>
            </td>
             <td style="text-align:right">有效起里程：</td>
            <td>
				<input name="VALID_START_MILEAGE" id="VALID_START_MILEAGE" datatype="0,is_null,30"  type="text" class="middle_txt"/>
            </td>
            </tr>
            <tr>
              <td style="text-align:right">有效止里程：</td>
            <td>
				<input name="VALID_MILEAGE" id="VALID_MILEAGE" datatype="0,is_null,30"  type="text" class="middle_txt"/>
            </td>
          </tr>
          <tr>
            <td style="text-align:right">法规条款：</td>
            <td colspan="5">
            	<textarea id="CLAUSE_STATUTE" name="CLAUSE_STATUTE" datatype="0,is_null,1000" rows="3" cols="80"></textarea>
            </td>
          </tr>
          </table>
          </div>
          </div>
          <div class="form-panel">
			<h2>授权级别</h2>
				<div class="form-body">
          <table class="table_list">
          <tr>
            <!-- <td style="text-align:right">授权级别：</td> -->
            
            	<% for(int i=0;i<levellist.size();i++){ 
			  		HashMap temp = (HashMap)levellist.get(i);
		  		%>
            	<td ><input type="checkbox"  name="<%=temp.get("APPROVAL_LEVEL_CODE")%>" value="<%=temp.get("APPROVAL_LEVEL_CODE")%>" />
				<%=temp.get("APPROVAL_LEVEL_NAME")%>
				</td>
				<%}%>
          </tr>
          </table>
          <table class="table_query">
			<tr> 
		     	 <td colspan="6" style="text-align:center">
		        <input name="ok" type="button" id="commitBtn" class="normal_btn"  value="确定"  onclick="checkForm();"/>
		        <input name="back" type="button" class="normal_btn" value="返回" onclick="_hide() ;"/>
		        </td>
		    </tr>
		  </table>
		  </div>
		  </div>
	</form>
<script>
function doInit(){
	document.getElementById("a").style.display = "none";
	document.getElementById("aa").style.display = "none";
	document.getElementById("b").style.display = "none";
	document.getElementById("bb").style.display = "none";
}

function isCheck(){
	var WARNING_TYPE = document.getElementById("WARNING_TYPE").value;
	if (WARNING_TYPE == '69991003') {
		document.getElementById("a").style.display = "none";
		document.getElementById("aa").style.display = "none";
		document.getElementById("b").style.display = "none";
		document.getElementById("bb").style.display = "none";
		document.getElementById("c").style.display = "";
	}
	if (WARNING_TYPE == '69991001') {
		document.getElementById("a").style.display = "";
		document.getElementById("aa").style.display = "";
		document.getElementById("b").style.display = "none";
		document.getElementById("bb").style.display = "none";
		document.getElementById("c").style.display = "none";
	}
	if (WARNING_TYPE == '69991002') {
		document.getElementById("a").style.display = "none";
		document.getElementById("aa").style.display = "none";
		document.getElementById("b").style.display = "";
		document.getElementById("bb").style.display = "";
		document.getElementById("c").style.display = "none";
	}
}

//表单提交前的验证：
function checkForm(){
	submitForm('fm') == true ? Add() : "";
}
//表单提交方法：
function Add(){
	if($('#CLAUSE_STATUTE').val() == '') {
		MyAlert('请填写法规条款!');
		return false ;
	}
	
	var selectvalue="";
	var itemlength=fm.elements.length;
		for(var k=0;k<parseInt(itemlength);k++){
			if(fm.elements[k].type=="checkbox"&&fm.elements[k].checked){
				selectvalue=selectvalue + fm.elements[k].value +"," ;		          
	        }
	    }
	if(selectvalue.length>1){
	  	selectvalue = selectvalue.substring(0,selectvalue.length-1);
	}else{
	  	MyAlert("您至少选择一种授权级别");
	  	return false;
	}	
	
	var WARNING_TYPE = document.getElementById("WARNING_TYPE").value;
	if(WARNING_TYPE == '') {
		MyAlert('预警类别不能为空!');
		return;
	}else{
		MyConfirm("是否确认新增?",addDownCode);
	}
}
//新增方法：
function addDownCode(){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/WarningRepairMain/WarningRepairAdd.json',addBack,'fm','');
}
//新增后的回调方法：
function addBack(json) {
	if(json.success != null && json.success == "true") {
	    if(json.returnValue != null && json.returnValue.length > 0){
			MyAlert("预警规则代码："+json.returnValue+"系统已存在!");
	    }else {
	    	document.getElementById("commitBtn").disabeld = true ;
	    	MyAlert("新增成功 !") ;
	    	__parent().__extQuery__(1) ;
	    	_hide() ;
	    }
	} else {
		MyAlert("新增失败！");
	}
}
//页面跳转
function sendPage(){
	window.location = '<%=contextPath%>/claim/basicData/WarningRepairMain/WarningRepairInit.do';
}
//返回
function onBack(){
    location="<%=contextPath%>/claim/basicData/WarningRepairMain/WarningRepairInit.do";   
}
</script>
</div>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.HashMap"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
String contextPath = request.getContextPath();
String id = (String) request.getAttribute("ID");
HashMap hm = (HashMap)request.getAttribute("RESULT"); //参数对应的值
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>旧件库区库位维护</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;旧件库区库位维护修改</div>
   
<form name="fm" id="fm">
<input type="hidden" name="ID" id="ID" value="<%=id%>"/>
 <table class="table_edit">
	   <tr>
	     <td class="table_edit_2Col_label_5Letter">配件代码：</td>
         <td><%=hm.get("PART_CODE")==null?"":hm.get("PART_CODE")%></td>
       </tr> 
	   <tr>
	     <td class="table_edit_2Col_label_5Letter">配件名称：</td>
         <td><%=hm.get("PART_NAME")==null?"":hm.get("PART_NAME")%></td>
       </tr>
	   <tr>
	     <td class="table_edit_2Col_label_5Letter">库区：</td>
         <td>
          <input type="text" value="<%=hm.get("LOCAL_WAR_HOUSE")==null?"":hm.get("LOCAL_WAR_HOUSE")%>" name="LOCAL_WAR_HOUSE"  id ="LOCAL_WAR_HOUSE" NOTNULL class="middle_txt" readonly="readonly"/>
         
           <span style="color: red">*</span>
            <input type="hidden" value="" name="SPY_NAME_ID"  id ="SPY_NAME_ID" class="middle_txt"/>
         	<input type="button" value="..." class="normal_btn" onclick="showReser1();"/>
         	
         </td>
       </tr>
	   <tr>
	     <td class="table_edit_2Col_label_5Letter">货架：</td>
         <td>
          	<input type="text" value="<%=hm.get("LOCAL_WAR_SHEL")==null?"":hm.get("LOCAL_WAR_SHEL")%>" name="LOCAL_WAR_SHEL"  id ="LOCAL_WAR_SHEL" NOTNULL class="middle_txt" readonly="readonly"/>
          	<span style="color: red">*</span>
            <input type="hidden" value="" name="SPY_NAME_ID"  id ="SPY_NAME_ID" class="middle_txt"/>
         	<input type="button" value="..." class="normal_btn" onclick="showReser2();"/>
         	
         </td>
       </tr>
	   <tr>
	     <td class="table_edit_2Col_label_5Letter">层数：</td>
         <td>
      		<input type="text" value="<%=hm.get("LOCAL_WAR_LAYER")==null?"":hm.get("LOCAL_WAR_LAYER")%>" name="LOCAL_WAR_LAYER"  id ="LOCAL_WAR_LAYER" NOTNULL class="middle_txt" readonly="readonly"/>
      		<span style="color: red">*</span>
            <input type="hidden" value="" name="SPY_NAME_ID"  id ="SPY_NAME_ID" class="middle_txt"/>
         	<input type="button" value="..." class="normal_btn" onclick="showReser3();"/>
         	
         </td>
       </tr>
	   <tr>
         <td colspan="2" align="center">
         <input class="normal_btn" type="button" name="ok" value="确  定" onclick="updateReser();"/>
         <input class="normal_btn" name="back" type="button" value="返  回"onclick="backTo();"/>
         </td>
       </tr>               
</table>
</form> 
<script type="text/javascript" >

function updateReser(){
	submitForm('fm') == true ? MyConfirm("是否确认修改？",modifyClaim) : "";
}
//修库区库位时：
function modifyClaim(){
	var partCode ="<%=hm.get("PART_CODE")%>";
	var warHouse = document.getElementById("LOCAL_WAR_HOUSE").value;
	var warShel = document.getElementById("LOCAL_WAR_SHEL").value;
	var warLayer = document.getElementById("LOCAL_WAR_LAYER").value;
	var url ="<%=contextPath%>/claim/basicData/ClaimVenderPrice/updateReser.json?PART_CODE="+partCode+"&LOCAL_WAR_HOUSE="+warHouse+"&LOCAL_WAR_SHEL="+warShel+"&LOCAL_WAR_LAYER="+warLayer;
	makeNomalFormCall(url,updateBack,'fm','');
}
//修改库区库位时回调方法：
function updateBack(json) {
	if(json.success != null && json.success == "true") {
		MyAlertForFun("修改成功！",function(){
			location="<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/oldPartReserInit.do";
		});
	} else {
		MyAlert("修改失败！请联系管理员！");
	}
}
function backTo(){
	location="<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/oldPartReserInit.do";
}
	function showReser1(){
		var url = '<%=contextPath%>/claim/basicData/ClaimVenderPrice/showReser.do?NAME_TYPE=1';
		OpenHtmlWindow(url,800,700);
	}
	function showReser2(){
		var url = '<%=contextPath%>/claim/basicData/ClaimVenderPrice/showReser.do?NAME_TYPE=2';
		OpenHtmlWindow(url,800,700);
	}
	function showReser3(){
		var url = '<%=contextPath%>/claim/basicData/ClaimVenderPrice/showReser.do?NAME_TYPE=3';
		OpenHtmlWindow(url,800,700);
	}
	
</script>
</body>
</html>

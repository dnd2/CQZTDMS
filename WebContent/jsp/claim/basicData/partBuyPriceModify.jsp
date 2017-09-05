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
<title>供应商索赔价格维护</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;供应商索赔价格维护修改</div>
   
<form name="fm" id="fm">
<input type="hidden" name="ID" id="ID" value="<%=id%>"/>
 <table class="table_edit">
	   <tr>
	     <td class="table_edit_2Col_label_7Letter">件号：</td>
         <td><%=hm.get("PART_CODE")==null?"":hm.get("PART_CODE")%></td>
       </tr>
	   <tr>
	     <td class="table_edit_2Col_label_7Letter">配件编码：</td>
         <td><%=hm.get("PART_OLDCODE")==null?"":hm.get("PART_OLDCODE")%></td>
       </tr> 
	   <tr>
	     <td class="table_edit_2Col_label_7Letter">配件名称：</td>
         <td><%=hm.get("PART_NAME")==null?"":hm.get("PART_NAME")%></td>
       </tr>
       <tr>
	     <td class="table_edit_2Col_label_7Letter">供应商编码：</td>
         <td><%=hm.get("VENDER_CODE")==null?"":hm.get("VENDER_CODE")%></td>
       </tr>
       <tr>
	     <td class="table_edit_2Col_label_7Letter">供应商名称：</td>
         <td><%=hm.get("VENDER_NAME")==null?"":hm.get("VENDER_NAME")%></td>
       </tr>
        <tr >
	     <td class="table_edit_2Col_label_7Letter">配件索赔系数：</td>
         <td><input type="text" name="PART_XS" id="PART_XS" datatype="0,isMoney,7"  
         value="<%=hm.get("PART_XS")==null?"":hm.get("PART_XS")%>" class="short_txt"/>
         </td>
       </tr>
        <tr >
	     <td class="table_edit_2Col_label_7Letter">工时索赔系数：</td>
         <td><input type="text" name="LABOUR_XS" id="LABOUR_XS" datatype="0,isMoney,7"  
         value="<%=hm.get("LABOUR_XS")==null?"":hm.get("LABOUR_XS")%>" class="short_txt"/>
         </td>
       </tr>
	  
	   <tr style="display: none">
	     <td class="table_edit_2Col_label_7Letter">索赔价格：</td>
         <td><input type="text" name="CLAIM_PRICE" id="CLAIM_PRICE" datatype="0,isMoney,7"  
         value="<%=hm.get("CLAIM_PRICE")==null?"":hm.get("CLAIM_PRICE")%>" class="short_txt"/>
         </td>
       </tr>
	   <tr style="display: none">
	     <td class="table_edit_2Col_label_7Letter">索赔员：</td>
         <td>
          <input type="text" value="<%=hm.get("SPY_NAME")==null?"":hm.get("SPY_NAME")%>" name="SPY_NAME"  id ="SPY_NAME" NOTNULL class="middle_txt" readonly="readonly"/>
           <span style="color: red">*</span>
            <input type="hidden" value="" name="SPY_NAME_ID"  id ="SPY_NAME_ID" class="middle_txt"/>
         	<input type="button" value="..." class="normal_btn" onclick="showUser();"/>
         	<input type="button" value="清空" class="normal_btn" onclick="cleanInput();"/>
         	
         </td>
       </tr>
	   <tr>
         <td colspan="2" align="center">
         <input class="normal_btn" type="button" name="ok" value="确  定" onclick="check();"/>
         <input class="normal_btn" name="back" type="button" value="返  回"onclick="backTo();"/>
         </td>
       </tr>               
</table>
</form> 
<script type="text/javascript" >

function check(){
	updateClaim();
}
function updateClaim(){
	submitForm('fm') == true ? MyConfirm("是否确认修改？",modifyClaim) : "";
}
//修改索赔工时：
function modifyClaim(){
	//var id = document.getElementById("ID").value;
	//var price = document.getElementById("CLAIM_PRICE").value;
	var url ="<%=contextPath%>/claim/basicData/ClaimVenderPrice/updatePrice.json";
	makeNomalFormCall(url,updateBack,'fm','');
}
//修改索赔工时回调方法：
function updateBack(json) {
	if(json.success != null && json.success == "true") {
		MyAlertForFun("修改成功！",function(){
			location="<%=contextPath%>/claim/basicData/ClaimVenderPrice/claimVenderPriceInit.do";
		});
	} else {
		MyAlert("修改失败！请联系管理员！");
	}
}
function backTo(){
	location="<%=contextPath%>/claim/basicData/ClaimVenderPrice/claimVenderPriceInit.do";
}
function cleanInput(){
		$('SPY_NAME').value="";
		$('SPY_NAME_ID').value="";
	}
	
	function showUser(){
		var url = '<%=contextPath%>/claim/basicData/ClaimVenderPrice/showUser.do' ;
		OpenHtmlWindow(url,800,700);
	}
	
function setUser(code,name){
	$('SPY_NAME_ID').value = code;
	$('SPY_NAME').value = name;
	}
</script>
</body>
</html>

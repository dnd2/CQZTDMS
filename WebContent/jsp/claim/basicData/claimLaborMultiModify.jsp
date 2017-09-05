<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.HashMap"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
String contextPath = request.getContextPath();
HashMap hm = (HashMap)request.getAttribute("SELMAP"); //参数对应的值
HashMap arrTwoId = (HashMap)request.getAttribute("arrTwoId");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔工时维护</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;索赔工时维护</div>
   
<form name="fm" id="fm">
<input type="hidden" name="WRGROUP_ID" id="WRGROUP_ID" value="<%=hm.get("WRGROUP_ID")==null?"":hm.get("WRGROUP_ID")%>"/>
<input type="hidden" name="ID" id="ID" value="<%=hm.get("ID")==null?"":hm.get("ID")%>"/>
<input type="hidden" name="P_ID" id="P_ID" value="<%=hm.get("PATER_ID")==null?"":hm.get("PATER_ID").toString()%>"/>
<input type="hidden" name="arrTwoId" id="arrTwoId" value="<%=arrTwoId%>"/>
 <table class="table_edit">
	   <tr>
	     <td class="table_edit_2Col_label_5Letter">索赔车型组：</td>
         <td><%=hm.get("WRGROUP_CODE")==null?"":hm.get("WRGROUP_CODE")%></td>
       </tr>
	   <tr>
	     <td class="table_edit_2Col_label_5Letter">工时代码：</td>
         <td><%=hm.get("LABOUR_CODE")==null?"":hm.get("LABOUR_CODE")%></td>
       </tr> 
	   <tr>
	     <td class="table_edit_2Col_label_5Letter">工时名称：</td>
         <td><input type="text" name="CN_DES" id="CN_DES" datatype="0,is_null,20"  value="<%=hm.get("CN_DES")==null?"":hm.get("CN_DES")%>" class="middle_txt"/></td>
       </tr>
	   <tr>
	     <td class="table_edit_2Col_label_5Letter">工时系数：</td>
         <td><input type="text" name="LABOUR_QUOTIETY" id="LABOUR_QUOTIETY" datatype="0,isMoney,7"  value="<%=hm.get("LABOUR_QUOTIETY")==null?"":hm.get("LABOUR_QUOTIETY")%>" class="short_txt"/></td>
       </tr> 
	   <tr>
	     <td class="table_edit_2Col_label_5Letter">索赔工时：</td>
         <td><input type="text" name="LABOUR_HOUR" id="LABOUR_HOUR" datatype="0,isMoney,7"  value="<%=hm.get("LABOUR_HOUR")==null?"":hm.get("LABOUR_HOUR")%>" class="short_txt"/></td>
       </tr>
	   <tr style="display:none">
	     <td class="table_edit_2Col_label_5Letter">工时大类：</td>
         <td>
 		<input type="text" name="P_LABOUR_CODE" id="P_LABOUR_CODE" value="<%=hm.get("P_LABOUR_CODE")==null?"":hm.get("P_LABOUR_CODE").toString()%>" readonly="readonly" class="middle_txt" datatype=0,is_null,20/>
 		<input type="button" onclick="selPaterClass();" class="mini_btn" value="..."/>
         </td>
       </tr>       
       <tr>
       <td>备注：</td>
       <td><textarea id="remark" name="remark" datatype="1,is_textarea,100"  maxlength="100" rows="5" cols="20"><%=hm.get("CON_REMARK")==null?"":hm.get("CON_REMARK") %></textarea></td>
       </tr>
	   <tr>
         <td colspan="2" align="center">
         <input class="normal_btn" type="button" name="ok" value="确定" onclick="updateClaim2();"/>
         <input class="normal_btn" name="back" type="button" value="返回" onclick="reBackPage();"/>
         </td>
       </tr>               
</table>
</form> 
<script type="text/javascript" >
//父类选择页面：
function selPaterClass(){
	OpenHtmlWindow('<%=contextPath%>/claim/basicData/ClaimLaborBigClassMain/laborPaterClassQueryInit.do',900,500);
}
//上级大类赋值：
function setPaterClass(paterid,paterCode){
	document.getElementById("P_LABOUR_CODE").value = paterCode;
	document.getElementById("P_ID").value = paterid;
}
//修改索赔工时(验证)：
function updateClaim2(){
	var a = confirm("是否确认修改？");
	if (a) {
		modifyClaim();
	}
}
//修改索赔工时：
function modifyClaim(){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimLaborMain/claimLaborUpdate.json',updateBack,'fm','');
}
//修改索赔工时回调方法：
function updateBack(json) {
	if(json.success != null && json.success == "true") {
		MyAlert("修改成功！");
		if (parent.$('inIframe')) {
 			parent.$('inIframe').contentWindow.backUpate();
 		} 
		_hide();
	} else {
		MyAlert("修改失败！请联系管理员！");
	}
}

//表单提交前的验证：
function checkForm(url){
	submitForm('fm') == true ? Add(url) : "";d
}
//表单提交方法：
function Add(url){
	fm.action = url;
	fm.submit();
}
  //新增
  function subFun(){
    fm.action ="<%=contextPath%>/claim/basicData/ClaimLaborMain/additionalLaborAddInit.do";   
    fm.submit();
  }
  //删除附加方法：
function sel(str1,str2){
	MyConfirm("是否确认删除？",del,[str1,str2]);
}
//删除故障代码关联：
function selBusiCode(str){
	MyConfirm("是否确认删除？",delBusicode,[str]);
}

function delBusicode(str){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimLaborMain/businessCodeDel.json?ID='+str,delBack,'fm','');
}  
//删除
function del(str1,str2){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimLaborMain/additionalLaborDel.json?ID='+str1+'&ADD_ID='+str2+'&W_ID=<%=hm.get("ID")==null?"":hm.get("ID")%>',delBack,'fm','');
}
//删除回调方法：
function delBack(json) {
	if(json.success != null && json.success == "true") {
		MyAlertForFun("删除成功！",sendPage);

	} else {
		MyAlert("删除失败！请联系管理员！");
	}
}
//页面跳转
function sendPage(){
	fm.action="<%=contextPath%>/claim/basicData/ClaimLaborMain/claimLaborUpdateInit.do";
	fm.submit();
}
//返回方法：
function reBackPage(){
	_hide();
} 
</script>
</body>
</html>

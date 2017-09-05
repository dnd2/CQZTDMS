<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.po.TtAsWrApplicationPO"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<TITLE>预授权监控其他项目维护</TITLE>
<script type="text/javascript">
		      
	//设置超链接  begin      
	function doInit(){
   		loadcalendar();
	}
	//修改的超链接设置
	function myLink(value,meta,record){
	    return String.format(
         "<a href=\"#\" onclick='sel(\""+value+"\")'>[删除]</a>");
	}
	//删除方法：
	function sel(str){
		MyConfirm("确认删除？",del,[str]);
	}  
	//删除
	function del(str){
		makeNomalFormCall('<%=contextPath%>/claim/preAuthorization/OtherMaintain/otherMaintainDel.json?ID='+str,delBack,'fm','');
	}
	//删除回调方法：
	function delBack(json) {
		if(json.success != null && json.success == true) {
			MyAlert("删除成功！");
			__extQuery__(1);
		} else {
			MyAlert("删除失败！请联系管理员！");
		}
	}	
	function getFeeDealerId(value,metadata,record) {
		return String.format(value+"<input  type='hidden' name='fee' value='" + value + "' /><input  type='hidden' name='dealerId' value='" + record.data.dealerId + "' />");;
	}
	//起始号渲染
	function roLine(value,metadata,record) {
		return value+"-"+record.data.endClaim;
	}
	//单位代码渲染函数
	function dezero(value,metadata,record) {
		if (value==0){
			return "";
		}else {
			return value;
		}
	}
	
	
	
	//取得已经选择的checkbox，拼接成字符串，各项目以,隔开
	function getCheckedToStr(name) {
		var str="";
		var chk = document.getElementsByName(name);
		if (chk==null){
			return "";
		}else {
		var l = chk.length;
		for(var i=0;i<l;i++){        
			if(chk[i].checked)
			{            
			str = chk[i].value+","+str; 
			}
		}
			return str;
		}
	}
	//上报
	function submitId(){
		MyConfirm("索赔申请单结算？",submitApply);
	}
	//上报操作
	function submitApply () {
		var fm = document.getElementById("fm");
		fm.action = '<%=request.getContextPath()%>/claim/application/ClaimBillCount/claimBillCount.json';
		fm.submit();
		//makeNomalFormCall('<%=request.getContextPath()%>/claim/application/ClaimBillCount/claimBillCount.json','fm','queryBtn');
	}
	//上报回调函数
	function returnBack0(json){
		var avl = json.returnValue;
		if(avl==1){
			__extQuery__(1);
			MyAlert("上报成功！");
		}else{
			MyAlert("上报失败！请联系管理员！");
		}
	}
	//删除
	function deleteId(){
	var str=getCheckedToStr("orderIds");
		if (str!=""){
		MyConfirm("确认删除？",deleteApply,[str]);
		}else {
			MyAlert("请选择至少一条要删除的申请单！");
		}
	}
	function deleteApply(str) {
		
			makeNomalFormCall('<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplyDelete.json?orderIds='+str,returnBack,'fm','queryBtn');
		
	}
	//删除回调函数
	function returnBack(json){
		var avl = json.returnValue;
		if(avl==1){
			__extQuery__(1);
			MyAlert("删除成功！");
		}else{
			MyAlert("删除失败！请联系管理员！");
		}
	}
	//回调函数
	function showResult(json){
		if(json.ACTION_RESULT == '1'){
			goBack();
			MyConfirm("新增成功！点击确认返回查询界面或者点击左边菜单进入其他功能！","window.location.href = '<%=request.getContextPath()%>/sysmng/orgmng/DlrInfoMng/queryAllDlrInfo.do'");
		}else if(json.ACTION_RESULT == '2'){
			MyAlert("新增失败！请重新载入或者联系系统管理员！");
		}
	}
	//表单提交方法：
	function checkForm(){
			makeFormCall('<%=contextPath%>/claim/preAuthorization/OtherMaintain/otherMaintainAdd.json',showResult,'fm');			
	}
	function goBack(){
		history.go(-1);
	}
	function showResult(json){
		if(json.success != null && json.success == true){
			if(json.existcode != null && json.existcode.length > 0){
				MyAlert("项目名称：【"+json.existcode+"】系统已存在！");
			}else{
				disableBtn($("commitBtn"));
				window.location.href = "<%=contextPath%>/claim/preAuthorization/OtherMaintain/otherMaintainForward.do";
			}
		}else{
			MyAlert("新增失败，请联系管理员！");
		}
	}
	//表单提交前的验证：
	function checkFormUpdate(){
		if(!submitForm('fm')) {
			return false;
		}
		  
		//FRM.APPROVAL_LEVEL.value=selectvalue;			
		MyConfirm("是否确认增加?",checkForm);
	}	
//设置超链接 end
</script>
</HEAD>
<BODY onload="doInit()">
<div class="navigation">
<img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔预授权&gt;预授权监控其他项目维护</div>
  
  <form method="post" name = "fm" id="fm">
<input type="hidden" name="ids" id="ids" value=""/>
<input type="hidden" name="OTHERFEE" id="OTHERFEE" value="<%=request.getAttribute("OTHERFEE")%>" />
  <TABLE align=center  class="table_edit" >
          <tr>
            <td  class="table_edit_2Col_label_5Letter">项目名称：</td>
            <td >
            <select  id="ITEM_CODE" name="ITEM_CODE">
			<script type="text/javascript">
			document.write(document.getElementById('OTHERFEE').value);
	        </script>
	        </select>
 		    </td> 
 		    <td>
 		    </td>
 		    </tr>
 		    <tr>
            <td class="table_edit_2Col_label_5Letter">项目描述：</td>
            <td colspan="3">
            	<input name="ITEM_DESC" type="text" id="ITEM_DESC"  class="middle_txt"  datatype="1,is_digit_letter_cn,100"   />
      		</td> 
      		<td></td>
			</tr>
         	<tr>
         	<td colspan="4" align="center">
         	<input class="normal_btn"  type="button" value="确定" name="commitBtn" id="commitBtn" onClick="checkFormUpdate();"/>
         	<input class="normal_btn"  type="button" value="返回" name="add" onClick="goBack();"/>
         	 </td>
        	</tr>
 	</table>
  	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
<form name="form1" style="display:none">
<table id='bt' class="table_list">
<tr><th align="center"><p>
    <input class="normal_btn" type=button value='结算' onclick='submitId()' name=modify />
  </p></th>
  </tr>
</table>
</form>
</BODY>
<SCRIPT LANGUAGE="JavaScript">
	document.form1.style.display = "none";
		
	var HIDDEN_ARRAY_IDS=['form1'];
	function goBack(){
		history.go(-1);
	}
</script>

</html>
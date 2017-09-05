<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
	List claimBasicList = (List)request.getAttribute("CLAIMBASIC");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>业务参数设定</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="wbox">
 <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;索赔配件加价率设定</div>
 <form name='fm' id='fm'>
 <div class="form-panel">
 <h2>基本信息</h2>
			<div class="form-body">
<table class="table_query">
        <tr>            
            <td class="table_add_2Col_label_5Letter" style="text-align:right">经销商代码：</td>          
        <td>
           <textarea rows="2" cols="53" id="dealerName" name="dealerName" readonly="readonly" onclick="showOrgDealer('dealerName','dealerId','true','','true','','10771002');"></textarea>
			<!--  <input class="middle_txt" id="dealerName" datatype="0,is_null,100"  name="dealerName" type="text"/>-->
            <!-- <input name="button2" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerName','dealerId','true','','true','','10771002');" value="..." />       -->  
            <input name="button" type="button" class="normal_btn" onclick="clr();" value="清除"/>
        </td>      
       </tr>
</table>

     <table class="table_list" >
     <% for(int i=0;i<claimBasicList.size();i++){ 
    	 HashMap temp = (HashMap)claimBasicList.get(i);
		  %>
		  <td><%=temp.get("CODE_DESC")%></td>
		  <%
   			 }	%>	
    <tr class="table_list_row1">
           <% for(int i=0;i<claimBasicList.size();i++){ 
    	 HashMap temp = (HashMap)claimBasicList.get(i);
    	 String  dataType = "0,isMoney,10";
    	 if(Constant.CLAIM_BASIC_PARAMETER_01.toString().equals(temp.get("CODE_ID")) || Constant.CLAIM_BASIC_PARAMETER_02.toString().equals(temp.get("CODE_ID"))){
    		 dataType = "0,isMoney,9";//一般工时单价和其他费用限定
    	 }else if(Constant.CLAIM_BASIC_PARAMETER_04.toString().equals(temp.get("CODE_ID"))){
    		 dataType = "0,isDigit,4";//索赔有效天数
    	 }else{
    		 dataType = "0,isMoney_4,9";//所有的税率
    	 }
		  %>
           <td>
              <input name="<%=temp.get("CODE_DESC")%>" type="hidden" value='18'/> 
              <input name="<%=temp.get("CODE_ID")%>" id="<%=temp.get("CODE_ID")%>"  datatype="<%=dataType%>" type="text" value='12' maxlength="9" class="middle_txt"/>             
           </td>
		  <%
   			 }	%>	
    </tr> 
</table>
<br/>
<table class="table_query" >
  <tr>
  <td style="text-align:center">
   <input type="button" name="button" value="确 定" id="commitBtn" class="normal_btn" onclick="checkForm();"/>
   <input type="button" name="button1" value="取 消" class="normal_btn" onclick="sendPage();"/>
  </td> 
  </tr>
</table>
</div>
</div>
</form>
</div>

<script type="text/javascript" >
//清除方法
 function clr() {
	document.getElementById('dealerName').value = "";
  }
//表单提交前的验证：
function checkForm(){
    var val = document.getElementById('dealerName').value;
   	if(val == null || val == ""){
   		MyAlert("经销商代码不能为空！");
   		return;
   	}else{
		submitForm('fm') == true ? otherfeeUpdate() : "";
   	}
}
//表单提交方法：
function otherfeeUpdate(){
	MyConfirm("是否确认新增？",add);
}  
//新增方法：
function add(){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimBasicParamsMain/claimBasicParmsAdd.json',addBack,'fm','');
}
//新增后的回调方法：
function addBack(json) {
	if(json.success != null && json.success == "true") {
<%--	    if(json.returnValue != null && json.returnValue.length > 0){--%>
<%--			MyAlertForFun("新增成功！但经销商："+json.returnValue+"原先已经维护了！",sendPage);--%>
<%--	    }else {--%>
	    	document.getElementById("commitBtn").disabled = true ;
	    	MyAlert("新增成功 !");
	    	sendPage() ;
<%--	    }--%>
	} else {
		MyAlert("新增失败！请联系管理员！");
	}
}
//页面跳转
function sendPage(){
	window.location = '<%=contextPath%>/claim/basicData/ClaimBasicParamsMain/claimBasicParmsInit.do';
}   
</script>
</body>
</html>
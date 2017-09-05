<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.TmDealerPO"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
	List claimBasicList = (List)request.getAttribute("CLAIMBASIC");//索赔基本参数类型集合 
	TmDealerPO dealerpo = (TmDealerPO)request.getAttribute("DEALERPO");//对应的经销商
	HashMap hm = (HashMap)request.getAttribute("DOWNPARAMETER"); //参数对应的值
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
     <table class="table_list" >
     <th>经销商代码</th>
     <th>经销商名称</th>
     <% for(int i=0;i<claimBasicList.size();i++){ 
    	 HashMap temp = (HashMap)claimBasicList.get(i);
		  %>
		  <th><%=temp.get("CODE_DESC")%></th>
		  <%
   			 }	%>	
    <tr class="table_list">
           <td>
           <input name="delearCode" id="delearCode" type="hidden" value='<%=dealerpo.getDealerCode()==null?"":dealerpo.getDealerCode()%>'/>
           <%=dealerpo.getDealerCode()==null?"":dealerpo.getDealerCode()%>        
           </td>
           <td>
           <%=dealerpo.getDealerShortname()==null?"":dealerpo.getDealerShortname()%>         
           </td>           
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
              <input name="<%=temp.get("CODE_ID")%>" id="<%=temp.get("CODE_ID")%>" datatype="<%=dataType%>" maxlength="9" type="text" value='<%=hm.get((Object)temp.get("CODE_ID"))==null?"":hm.get((Object)temp.get("CODE_ID"))%>'  class="middle_txt"/>             
           </td>
		  <%
   			 }	%>	
    </tr>
</table>
<br/>
 <table class="table_query" >
  <tr>
  <td style="text-align:center">
   <input type="button" name="modify"  value="修 改" id="commitBtn" class="normal_btn" onclick="checkForm();"/>
   <input type="button" name="button1" value="关 闭" class="normal_btn" onclick="_hide() ;"/>
  </td> 
  </tr>
</table>      
</div>
</div>
</form>
</div>

<script type="text/javascript" >
//表单提交前的验证：
function checkForm(){
	submitForm('fm') == true ? otherfeeUpdate() : "";
}
//表单提交方法：
function otherfeeUpdate(){
	MyConfirm("是否确认修改？",updateOtherfee);
}
function updateOtherfee(){
	document.getElementById("commitBtn").disabled = true ;
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimBasicParamsMain/claimBasicParmsUpdate.json',updateBack,'fm','');
}

//修改回调方法：
function updateBack(json) {
	if(json.success != null && json.success=='true'){
		MyAlert("修改成功",sendPage);
		__parent().__extQuery__(1);
		_hide() ;
	}else{
		document.getElementById("commitBtn").disabled = false ;
		MyAlert("修改失败！请联系管理员");
	}
}
//页面跳转
function sendPage(){
	window.location = '<%=contextPath%>/claim/basicData/ClaimBasicParamsMain/claimBasicParmsInit.do';
}		  
</script>
</body>
</html>
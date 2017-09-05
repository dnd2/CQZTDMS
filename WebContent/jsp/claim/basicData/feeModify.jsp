<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.TmVhclMaterialGroupPO"%>
<%@page import="java.util.HashMap"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
	List feeTypeList = (List)request.getAttribute("FEETYPE");//保养费用集合 
	TmVhclMaterialGroupPO grouppo = (TmVhclMaterialGroupPO)request.getAttribute("GROUPPO");//对应的车型
	HashMap hm = (HashMap)request.getAttribute("FEE"); //保养费用参数对应的值
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>保养费用维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
 <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;保养费用维护</div>
 <form name='fm' id='fm'>
     <table class="table_list" style="border-bottom:1px solid #DAE0EE">
     <th>车型代码</th>
     <th>车型名称</th>
     <% for(int i=0;i<feeTypeList.size();i++){ 
    	 HashMap temp = (HashMap)feeTypeList.get(i);
		  %>
		  <th><%=temp.get("CODE_DESC")%></th>
		  <%
   			 }	%>	
    <tr class="table_list_row1">
           <td>
           <input name="groupId" id="groupId" type="hidden" value='<%=grouppo.getGroupId()==null?"":grouppo.getGroupId()%>'/>
           <%=grouppo.getGroupCode()==null?"":grouppo.getGroupCode()%>        
           </td>
           <td>
           <%=grouppo.getGroupName()==null?"":grouppo.getGroupName()%>         
           </td>           
           <% for(int i=0;i<feeTypeList.size();i++){ 
    	 HashMap temp = (HashMap)feeTypeList.get(i);
		  %>
           <td>
              <input name="<%=temp.get("CODE_DESC")%>" type="hidden" value=''/> 
              <input name="<%=temp.get("CODE_ID")%>" id="<%=temp.get("CODE_ID")%>" datatype="0,isMoney,10" type="text" value='<%=hm.get((Object)temp.get("CODE_ID"))==null?"":hm.get((Object)temp.get("CODE_ID"))%>'  class="short_txt"/>             
           </td>
		  <%
   			 }	%>	
    </tr>
</table>
<br/>
 <table class="table_edit" >
  <tr>
  <td align="center">
   <input type="button" name="modify" id="commitBtn" value="修改" class="long_btn" onclick="checkForm();"/>
   <input type="button" name="button1" value="取消" class="normal_btn" onclick="history.back(-1);"/>
  </td> 
  </tr>
</table>      
</form>


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
	disableBtn($("commitBtn"));
	makeNomalFormCall('<%=contextPath%>/claim/basicData/FeeMain/feeUpdate.json',updateBack,'fm','');
}

//修改回调方法：
function updateBack(json) {
	if(json.success != null && json.success=='true'){
		MyAlertForFun("修改成功",sendPage);
	}else{
		MyAlert("修改失败！请联系管理员");
	}
}
//页面跳转
function sendPage(){
	window.location = '<%=contextPath%>/claim/basicData/FeeMain/feeInit.do';
}	  
</script>
</body>
</html>
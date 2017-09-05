<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔配件质保期维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
 <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;索赔配件质保期维护</div>
 <form name='fm' id='fm'>
     <input name="MODEL_ID" id="MODEL_ID" type="hidden" value='<%=(String)request.getAttribute("GROUP_ID") %>'/>
     <table class="table_list" style="border-bottom:1px solid #DAE0EE">
     <th>配件类型</th>
     <th>质保时间(月)</th>
     <th>质保里程(公里)</th>
    <c:forEach var="addlist" items="${MODELTYPE}">
       <tr class="table_list_row1">
        <input  name="GURN_ID" id="GURN_ID" type="hidden" value="${addlist.GURN_ID}"/>
        <input  name="CODE_ID" id="CODE_ID" type="hidden" value="${addlist.CODE_ID}"/>
          <td> 
			<c:out value="${addlist.CODE_DESC}"></c:out>
          </td>
          <td>
          	<input name="GURN_MONTH" id="GURN_MONTH" datatype="0,isDigit,3" type="text" value="${addlist.GURN_MONTH}"  class="short_txt"/>             
          </td>
          <td>
          	<input name="GURN_MILE" id="GURN_MILE" datatype="0,isMoney,10" type="text" value="${addlist.GURN_MILE}"  class="short_txt"/>             
          </td>          
        </tr>
    </c:forEach>
</table>
<br/>
 <table class="table_edit" >
  <tr>
  <td align="center">
   <input type="button" name="modify" value="修改" id="commitBtn" class="long_btn" onclick="checkForm();"/>
   <input type="button" name="button1" value="返回" class="normal_btn" onclick="history.back(-1);"/>
  </td> 
  </tr>
</table>      

</form>


<script type="text/javascript" >
//表单提交前的验证：
function checkForm(){
	submitForm('fm') == true ? update() : "";
}
//表单提交方法：
function update(){
	fm.action = '<%=contextPath%>/claim/basicData/QualityMain/qualityUpdate.do';
	fm.submit();
	}


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
	makeNomalFormCall('<%=contextPath%>/claim/basicData/QualityMain/qualityUpdate.json',updateBack,'fm','');
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
	window.location = '<%=contextPath%>/claim/basicData/QualityMain/qualityInit.do';
}	  
</script>
</body>
</html>
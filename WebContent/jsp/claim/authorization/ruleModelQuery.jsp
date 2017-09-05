<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.po.TtAsWrModelGroupPO"%>
<%
	String contextPath = request.getContextPath();
	TtAsWrModelGroupPO modelgroup = (TtAsWrModelGroupPO)request.getAttribute("MODELGROUP");  
	String rowNum = (String)request.getAttribute("ROWNUM");//行号
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔授权规则</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body onbeforeunload="returnBefore();">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔授权管理&gt;授权规则维护</div>
  <form name='fm' id='fm'>
  <input type="hidden" name="ID" id="ID" value="<%=request.getAttribute("MODELID")%>"/>
  <input id="groupid" name="groupid" type="hidden" />
  <table class="table_query">
       <tr>            
        <td class="table_query_2Col_label_4Letter">车型代码：</td>            
        <td>
			<input  class="middle_txt" id="groupcode"  name="groupcode" type="text" datatype="1,is_digit_letter,10"/>
        </td>
        <td class="table_query_2Col_label_4Letter">车型名称：</td>
        <td><input type="text" name="groupname" id="groupname" datatype="1,is_null,30" class="middle_txt" value=""/></td>   
       </tr>
       <tr>
        <td colspan="4" align="center">
        	    <input  id="queryBtn1" class="normal_btn" type="button" name="button1" value="查询"  onclick="query();"/>
				<input class="normal_btn" type="button" name="button1" value="关闭"  onclick="_hide();"/>
        </td>
       </tr>       
 	</table>
 	<br/>
  <table class="table_list" style="border-bottom:1px solid #DAE0EE" >
  	  <th>选择</th>
      <th>车型组</th>
      <th>车型代码</th>
      <th>车型名称</th>
       <c:forEach var="addlist" items="${ADDLIST}">
       <tr class="table_list_row1">
       	  <td>
			<input id="${addlist.GROUP_CODE}" name="${addlist.GROUP_CODE}" type="hidden" value="${addlist.GROUP_NAME}"/>
       	  	<input type="radio" name="${addlist.GROUP_CODE}" id="${addlist.GROUP_CODE}" value="${addlist.GROUP_CODE}" onclick="selbyid(this);"/>
       	  </td>
          <td> 
			<%=modelgroup.getWrgroupName()==null ? "" : modelgroup.getWrgroupName()%>
          </td>
          <td>
          <c:out value="${addlist.GROUP_CODE}"></c:out>
          </td>
          <td>
          <c:out value="${addlist.GROUP_NAME}"></c:out>
          </td>
        </tr>
    </c:forEach>
</table>
</form>
<script type="text/javascript" >
function returnBefore()
{   
	var ggg = 'ELEMENT_VALUE_'+<%=rowNum%>;
	var code = document.getElementById("groupid").value;
	if(code && code.length > 0){
		parentDocument.getElementById(ggg).value = document.getElementById("groupid").value;
	}
}
function selbyid(obj){
	  $('groupid').value = obj.value;
	_hide();
}
function query(){
	if(!submitForm('fm')) {
		return false;
	}
	var id = document.getElementById("ID").value;
	fm.action = '<%=contextPath%>/claim/authorization/RuleMain/ruleModelSelect.do?ID='+id;
	fm.submit();	
}
</script>  
  </body>
</html>


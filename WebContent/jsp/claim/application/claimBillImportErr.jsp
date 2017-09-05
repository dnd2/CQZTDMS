<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@page import="com.infodms.dms.bean.MessErr" %>

<%
	String contextPath = request.getContextPath();
	
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<title>索赔导入列表</title>

</head>
<body onload="autoAlertException();">
<div class=navigation><img src="<%=request.getContextPath()%>/img/nav.gif">&nbsp;索赔导入列表</div>
 <form name="fm" method="post"  enctype="multipart/form-data" id="fm">
<table class="table_list" >  
	  <tr  class=csstr>
	 	<td width="25%" align="center">序号</td>
	  	<td width="25%" align="center">行列</td>
	  	<td width="25%" align="center">内容</td>
	  	<td width="25%" align="center">错误信息</td>
	  </tr>
	  <%
	  List<MessErr> list = (List<MessErr>)request.getAttribute("listErr");
	  for(int i=0;i<list.size();i++){
		  MessErr err = list.get(i);
	  %>
	  	 <tr  class=csstr>
	  	 	<td align="center"><%=i+1%></td>
		  	<td align="center"><%=err.getHead() %></td>
		  	<td align="center"><%=err.getName() %></td>
		  	<td align="center" style="color: red"><%=err.getMess() %></td>
		  </tr>
	  	
	  <%
	  }
  %>
</table>
<table class="table_list" height="20px">
<tr class=csstr>
<td align="center" bgcolor="#FFD0FF" width="10%" >总条数：${successNum+faileNum}</td>
	
<c:choose>
	<c:when test="${successNum>0 }">
		<td align="center" bgcolor="#28FF28" width="${(successNum/(successNum+faileNum))*100-10}%" >正确条数：${successNum }</td>
	</c:when>
</c:choose>
  <c:choose>
	<c:when test="${faileNum>0 }">
			<td align="center" bgcolor="#FF0000" width="${(faileNum/(successNum+faileNum))*100+10 }%" >错误条数：${faileNum }</td>
	</c:when>
</c:choose>
  </tr>
</table>
<table width="95%"  align="center" class="table_query">
  <tr class=csstr>
    <td align="center">
    <c:choose>
    	<c:when test="${faileNum>0 }">
    	<input class="long_btn" type="button" value="确认导入" name="button2"  onClick="javascript:confirmImport();" disabled="disabled">
    	</c:when>
    	<c:when test="${successNum<1 }">
    	<input class="long_btn" type="button" value="确认导入" name="button2"  onClick="javascript:confirmImport();" disabled="disabled">
    	</c:when>
    	<c:otherwise>
    	<input class="long_btn" id="btn_sure" type="button" value="确认导入" name="button2"  onClick="javascript:confirmImport();">
    	</c:otherwise>
    </c:choose>
    	
        <input class="long_btn" type="button" id="back_id" value="返回" name="button1"  onClick="javascript:history.go(-1);">
    </td>
  </tr>
</table>
</form>
</div>
<script type="text/javascript">

	function confirmImport(){
		MyConfirm("确认新增！",importDo, []);
	}
	function importDo()
	{
		$('back_id').disabled=true;
		$('btn_sure').disabled=true;
		fm.action = '<%=contextPath%>/claim/application/ClaimBillOemImport/claimBillImport.do';
	    fm.submit();
	}
</script>
</body>
</html>

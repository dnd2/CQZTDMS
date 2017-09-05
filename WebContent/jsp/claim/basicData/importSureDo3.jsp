<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.Map"%>
<%@ page import="java.util.List"%>

<%
	String contextPath = request.getContextPath();
	
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<title>索赔工时维护导入确认</title>

</head>
<body onload="autoAlertException();">
<div class=navigation><img src="<%=request.getContextPath()%>/img/nav.gif">&nbsp;辅料费维护导入确认</div>
 <form name="fm" method="post"  enctype="multipart/form-data" id="fm">
<table class="table_list" >  
  <tr class="table_list_row1" style="background-color:#DAE0EE; ">
    <td width="12%" align="center">序号</td>
  	<td width="12%" align="center">车型组</td>
  	<td width="12%" align="center">工时代码</td>
  	<td width="12%" align="center">工时名称</td>
  	<td width="12%" align="center">工时大类代码</td>
  	<td width="12%" align="center">工时大类名称</td>
  	<td width="12%" align="center">工时系数</td>
  	<td width="12%" align="center">索赔工时</td>
  </tr>
 <c:set var="count" value="${1}"/>
  <%
  List<Map<String,String>> list = (List<Map<String,String>>)request.getAttribute("list");
  for(Map<String,String> map : list){
  %>
  	 <tr class="table_list_row1">
  	 	<td align="center">${count }</td>
	  	<td align="center"><%=map.get("1") %><input type="hidden" name="cxz${count }" value="<%=map.get("1") %>"/></td>
	  	<td align="center"><%=map.get("2") %><input type="hidden" name="gsdm${count }" value="<%=map.get("2") %>"/></td>
	  	<td align="center"><%=map.get("3") %></td>
	  	<td align="center"><%=map.get("4") %>
	  	<input type="hidden" name="gsmc${count }" value="<%=map.get("3") %>"/>
	  	<input type="hidden" name="gsdlCode${count }" value="<%=map.get("4") %>"/>
	  	<input type="hidden" name="gsdlName${count }" value="<%=map.get("5") %>"/>
	  	<input type="hidden" name="gsxs${count }" value="<%=map.get("6") %>"/>
	  	<input type="hidden" name="spgs${count }" value="<%=map.get("7") %>"/>
	  	</td>
	  	<td align="center"><%=map.get("5") %>
	  	</td>
	  	<td align="center"><%=map.get("6") %>
	  	</td>
	  	<td align="center"><%=map.get("7") %>
	  	</td>
	  	 <c:set var="count" value="${count+1}"/>
	  </tr>
  	
  <%
  }
  %>
</table>
<input type="hidden" name="count" value="${count }"/>
<table width="95%"  align="center" class="table_query">
  <tr class=csstr>
    <td align="center"><input class="normal_btn" type="button" value="确 定" name="button1"  onClick="add();">
        <input class="normal_btn" type="button" value="返 回" name="button1"  onClick="goBack()">
    </td>
  </tr>
</table>
</form>
<script type="text/javascript">

 //服务商资金确认导入：
	function add(parms) {
		if(confirm("确定导入?")){
			btnDisable();
		    var url = '<%=request.getContextPath()%>/claim/basicData/ClaimLaborMain/importAdd.json';
		  	makeFormCall(url,showResult,'fm');
		    }
	}

   function showResult(json) {
	   btnEnable();
		  if (json.success != null && json.success == "true") {
	       	   MyAlert("导入成功！");
	           window.location.href = "<%=request.getContextPath()%>/claim/basicData/ClaimLaborMain/claimLaborInit.do";
	       } else {
	           MyAlert("导入失败，请联系管理员！");
	       }
   }

   function goBack(){
		btnDisable();
		fm.action = "<%=request.getContextPath()%>/claim/basicData/ClaimLaborMain/inportPer.do";
		fm.submit();
	}

 	//失效按钮
	function btnDisable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });

	}

	//有效按钮
	function btnEnable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = "";
	    });

	}
</script>
</body>
</html>

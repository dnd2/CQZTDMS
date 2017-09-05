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
<title>索赔工时单价导入确认</title>

</head>
<body onload="autoAlertException();">
<div class=navigation><img src="<%=request.getContextPath()%>/img/nav.gif">&nbsp;索赔工时单价导入确认</div>
 <form name="fm" method="post"  enctype="multipart/form-data" id="fm">
<table class="table_list" >  
  <tr class="table_list_row1" style="background-color:#DAE0EE; ">
    <td width="5%" align="center">序号</td>
  	<td width="15%" align="center">导入类型</td>
  	<td width="15%" align="center">预警等级</td>
  	<td width="15%" align="center">配件代码或者关注部位代码</td>
  	<td width="15%" align="center">法定次数</td>
  	<td width="15%" align="center">预警</td>
  	<td  align="center">法规</td>
  </tr>
 <c:set var="count" value="${1}"/>
  <c:forEach var="list" items="${list}">
  	 <tr class="table_list_row1">
  	 	<td align="center">${count }</td>
	  	<td align="center"><c:if test="${list.partWrType == 94031002}">易损件</c:if> <c:if test="${list.partWrType == 94031003}">关注部位</c:if><input type="hidden" name="partWrType" value="${list.partWrType}"/></td>
	  	<td align="center">
		  	<script type='text/javascript'>
		       var activityType=getItemValue('${list.vrLevel}');
		       document.write(activityType) ;
		     </script>
		  	<input type="hidden" name="vrLevel" value="${list.vrLevel}"/>
	  	</td>
	  	<td align="center">${list.vrPartCode}<input type="hidden" name="vrPartCode" value="${list.vrPartCode}"/></td>
	  	<td align="center">${list.vrLaw}<input type="hidden" name="vrLaw" value="${list.vrLaw}"/></td>
	  	<td align="center">${list.vrWarranty}<input type="hidden" name="vrWarranty" value="${list.vrWarranty}"/></td>
	  	<td align="center">${list.vrLawStandard}<input type="hidden" name="vrLawStandard" value="${list.vrLawStandard}"/></td>
	  	 <c:set var="count" value="${count+1}"/>
	  </tr>
 </c:forEach>	  
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
		if(confirm("此次操作将会覆盖原有的数据,确定导入?")){
			btnDisable();
		    var url = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/labourImportRAdd.json';
		  	makeFormCall(url,showResult,'fm');
		    }
	}

   function showResult(json) {
	   btnEnable();
		  if (json.success != null && json.success == "true") {
	       	   MyAlert("导入成功！");
	           window.location.href = "<%=contextPath%>/claim/basicData/ClaimWrRule/claimWrRuleInit.do";
	       } else {
	           MyAlert("导入失败，请联系管理员！");
	       }
   }

   function goBack(){
		btnDisable();
		fm.action = "<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/labourPriceInit.do";
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

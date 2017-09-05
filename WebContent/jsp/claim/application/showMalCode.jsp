<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>

<!-- created by lishuai103@yahoo.com.cn 20100603 通用选择供应商 -->
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
    String inputCode = request.getParameter("INPUTCODE");
    String inputId = request.getParameter("INPUTID");
    String isMulti = request.getParameter("ISMULTI");
    
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>通用选择框</title>

<script language="JavaScript">

	function doInit()
	{
		__extQuery__(1);
	}

</script>
</head>
<body >
	<div class="navigation">
			<img src="../../../img/nav.gif" />
			&nbsp;当前位置：售后服务管理&gt;索赔申请审核&gt;故障代码选择
		</div>
<form method="post" name ="fm" id="fm">
	<table class="table_edit">
    <tr>
      <td class="table_query_3Col_label_6Letter" nowrap="nowrap">故障代码：</td>
      <td class="table_query_3Col_input" nowrap="nowrap">
      	<input class="middle_txt" id="malCode" name="malCode" value="" type="text"/>
      </td>
      <td class="table_query_3Col_label_6Letter" nowrap="nowrap">故障名称：</td>
      <td>
      	<input name="malName" type="text" id="malName"  class="middle_txt"/>
      </td>
      <td>&nbsp;</td>
      <td>
      	<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1);" >
      </td>
  	</tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script type="text/javascript" >
	var myPage;

	var url = "<%=contextPath%>/claim/application/ClaimManualAuditing/selectMalCodeQuery.json";
	var title = null;

	var columns = [
				{header: "选择", dataIndex: 'MAL_ID', align:'center',renderer:seled},
				{header: "故障代码", dataIndex: 'MAL_CODE', align:'center'},
				{header: "故障名称", dataIndex: 'MAL_NAME', align:'center'} 
		      ];
		      
	function seled(value,meta,record){
		var code=record.data.MAL_CODE;
		var name=record.data.MAL_NAME;
 		 return String.format("<input type='radio' name='rd' onclick='checkValues(\""+record.data.MAL_ID+"\",\""+record.data.MAL_CODE+"\",\""+record.data.MAL_NAME+"\")' />");
    }
    
    function checkValues(v1,v2,v3){
    	 if(v1==null||v1=="null"){
		 	v1 = "";
		 }
		  if(v2==null||v2=="null"){
		 	v2 = "";
		 }
		  if(v3==null||v3=="null"){
		 	v3 = "";
		 }
		 if (parent.$('inIframe')) {
 			parentContainer.setMalInfo(v1,v2,v3);
 		} else {
			parent.setMalInfo(v1,v2,v3);
		}
 			//关闭弹出页面
 			parent._hide();
 		 
    }
</script>
</body>
</html>
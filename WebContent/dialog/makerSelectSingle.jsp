<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>

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
<body onbeforeunload="doSupp();__extQuery__(1);">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 选择制造商</div>
<form method="post" name ="fm" id="fm">
	<input id="makerName" name="makerName" type="hidden" value="" />
	<input id="MAKER_ID" name="MAKER_ID" type="hidden" value=""/>	
	<table class="table_edit">
    <tr>
      <td class="table_query_3Col_label_6Letter" nowrap="nowrap">制造商编码：</td>
      <td class="table_query_3Col_input" nowrap="nowrap">
      	<input class="middle_txt" id="MAKER_CODE" name="MAKER_CODE" value="" type="text"/>
      </td>
      <td class="table_query_3Col_label_6Letter" nowrap="nowrap">制造商名称：</td>
      <td>
      	<input type="text"  id="MAKER_NAME" name="MAKER_NAME" class="middle_txt"/>
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

	var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartMakerManager/queryPartMakerInfo.json";
				
	var title = null;

	var columns = [
				{header: "选择", dataIndex: 'MAKER_ID', align:'center',renderer:seled},
				{header: "制造商编码", dataIndex: 'MAKER_CODE',style: 'text-align:left'},
				{header: "制造商名称", dataIndex: 'MAKER_NAME', style: 'text-align:left'}
		      ];
		      
	function seled(value,meta,record){
		var isMulti = "<%=isMulti%>";
 		if(isMulti == "false"){
 			return "<input type='radio' onclick='singleSelect(\""+value+"\",\""+record.data.MAKER_NAME+"\")'/>";
 		}else{
        	return "<input type='checkbox' name='checkCode' id='checkCode' value='"+ value +"' />";
        }
    }
    
    function singleSelect(val1,val2){
		var inputCode = "<%=inputCode%>";
		var inputId = "<%=inputId%>";
    	 $('MAKER_ID').value = val1;
    	 $('makerName').value = val2;
    	_hide();
		if(inputCode && inputCode.length > 0){
			parentDocument.getElementById(inputCode).value = val2;
		}
		if(inputId && inputId.length > 0){
			parentDocument.getElementById(inputId).value = val1;
		}
    	//parentContainer.changeDiv(inputCode,val2,inputId,val1);
    }
    
    function doSupp()//关闭弹出窗口的时候执行该方法
	{
		var inputCode = "<%=inputCode%>";
		var inputId = "<%=inputId%>";
		var venderName = document.getElementById("makerName").value;
		var venderId = document.getElementById("MAKER_ID").value;
		if(venderName && venderName.length > 0){
			parentDocument.getElementById(inputCode).value = venderName;
		}
		if(venderId && venderId.length > 0){
			parentDocument.getElementById(inputId).value = venderId;
		}
	}
	
</script>
</body>
</html>
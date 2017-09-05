<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
    
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
<body onbeforeunload="doSupp()">
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：供应商选择 </div>
</div>
<form method="post" name ="fm" id="fm">
	<table class="table_edit">
    <tr>
      <td class="table_query_3Col_label_6Letter" nowrap="nowrap">供应商代码：</td>
      <td class="table_query_3Col_input" nowrap="nowrap">
      	<input class="middle_txt" id="suppCode" name="suppCode" value="" type="text"/>
      </td>
      <td class="table_query_3Col_label_6Letter" nowrap="nowrap">供应商名称：</td>
      <td>
      	<input name="suppName" type="text" id="suppName"  class="middle_txt"/>
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
<form name="form1" style="display:none">
   <table class="table_list" id="table1" >
  	  <tr>
  	  	<th align="center">
	    	<input class="normal_btn" type="button" value="确认" onclick="doConfirm()"/>
	    	<input class="normal_btn" type="button" value="关闭" onclick="parent._hide()"/>
       </th>
  	  </tr>
   </table>
  </form>
<script type="text/javascript" >
	document.form1.style.display = "none";
	var HIDDEN_ARRAY_IDS=['form1'];
	
	var myPage;

	var url = "<%=contextPath%>/report/service/ClaimReport/showSupplyCode.json";
				
	var title = null;

	var columns = [
				{header: "选择", dataIndex: 'MAKER_CODE', align:'center',renderer:seled},
				{header: "供应商代码", dataIndex: 'MAKER_CODE', align:'center'},
				{header: "供应商名称", dataIndex: 'MAKER_NAME', align:'center'}
		      ];
		      
	function seled(value,meta,record){
        	return "<input type='checkbox' name='checkCode' id='checkCode' value='"+ value + "' />";
    }
 function doConfirm(){
	var code = document.getElementsByName("checkCode");
	var str="";
	var len = code.length;
	for(var i=0;i<len;i++){        
   		if(code[i].checked){            
   			str = str+code[i].value+","; 
   		}
   	}
	if(str!=""){
		str = str.substring(0,str.length-1);
	}
		
 		if (parent.$('inIframe')) {
 			parentContainer.setMainCode(str);
 		} else {
			parent.setMainCode(str);
		}
 		if(parentContainer.cloMainPart==1) {
 			//关闭弹出页面
 			parent._hide();
 		 }
	}    
</script>
</body>
</html>
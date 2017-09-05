<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	String sqlName = request.getParameter("sqlName");
	String para = request.getParameter("para");
	String callFunc = request.getParameter("callFunc");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查询</title>
</head>
<script type="text/javascript">
	var myPage;
	var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/selSales.json?dealerId="+dealerId+"&type="+type;
	var title = null;
	var columns = [
				{header: "选择",  align:'center',dataIndex:'PARENTORG_ID',renderer:getRadio},
				{header: "销售单位名称", dataIndex: 'PARENTORG_NAME', align:'center'},
				{header: "销售单位编码", dataIndex: 'PARENTORG_CODE', align:'center'}
				];

function selData(id,name,code,arg1,arg2,arg3,arg4){
	
	if(type=="3"){
		parentDocument.getElementById(inputId).value = id;
		parentDocument.getElementById(inputName).value = name;
		
		parentDocument.getElementById(inputLinkMan).value = arg1;
		
		parentDocument.getElementById(inputTel).value = arg2;
		
		parentDocument.getElementById(inputPostCode).value = arg3;
		
		parentDocument.getElementById(inputStation).value = arg4;
		
		_hide();
		return;
	}
	if(type=="1"){
		parentDocument.getElementById(inputId).value = id;
		parentDocument.getElementById(inputName).value = name;
		parentDocument.getElementById(inputCode).value = code;
		parentContainer.showAcount(arg1=='null'?'':arg1,arg2=='null'?'':arg2,arg3=='null'?'':arg3,arg4=='null'?'':arg4);
		parentContainer.initDivAndPartInfo()
		_hide();
		return;
	}
	if(type=="5"){
		parentDocument.getElementById(inputId).value = id;
		parentDocument.getElementById(inputName).value = name;
		parentDocument.getElementById(inputCode).value = code;
		parentContainer.initAccount(arg1,arg2,arg3,arg4);
		_hide();
		return;
	}
	parentDocument.getElementById(inputId).value = id;
	parentDocument.getElementById(inputName).value = name;
	parentDocument.getElementById(inputCode).value = code;
	_hide();
}
function doinit(){
	if(type=="3"){
		document.getElementById("code_name").style.display = "none";
		document.getElementById("code_text").style.display = "none";
	}
}
function conf(){
	var chks = document.getElementsByName("chks");
	var ids = "";
	var names ="";
	var codes = "";
	for(var i=0;i<chks.length;i++){
		if(chks[i].checked){
			for(var j=0;j<objArr.length;j++){
				if(chks[i].value==objArr[j].id){
					ids +=","+objArr[j].id;
					names +=","+objArr[j].name;
					codes +=","+objArr[j].code;
				}
			}
		}
	}
	parentDocument.getElementById(inputId).value = ids;
	parentDocument.getElementById(inputName).value = names;
	parentDocument.getElementById(inputCode).value = codes;
	_hide();
}
//生成对象
function createObjArr(id,code,name){
	var obj = new Object();
	obj.id = id;
	obj.code = code;
	obj.name = name;
	objArr.push(obj);
}
</script>
<body onload ="doinit();__extQuery__(1)">
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
<table border="0" class="table_query">
    <th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 查询条件</th>
    <tr>
      <td width="20%" class="table_query_2Col_label_6Letter">名 称：</td>
      <td width="30%"  ><input class="middle_txt" type="text" id="PARENTORG_NAME" name="PARENTORG_NAME" /></td>
      
      <td width="20%" class="table_query_2Col_label_6Letter"><div id="code_name" >编 码：</div></td>
      <td width="30%" ><div id="code_text" ><input class="middle_txt" type="text" id="PARENTORG_CODE" name="PARENTORG_CODE" /></div></td>
    </tr>
  <tr>
    <td colspan="4" align="center">
    <input class="normal_btn" type="button" value="查 询" onclick="__extQuery__(1);"/>
    
    <script type="text/javascript">
	    if(type=="4"){
	    	document.write('<input class="normal_btn" type="button" value="确 定" onclick="conf();"/>');
	    }else{
	    	document.write('<input class="normal_btn" type="button" value="关 闭" onclick="_hide();"/>');
	    }
    
    
    </script>
    </td>
  </tr>
</table>

<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />

</form>
</body>
</html>

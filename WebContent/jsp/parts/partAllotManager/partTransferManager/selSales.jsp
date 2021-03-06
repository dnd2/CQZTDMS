<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	String inputId = request.getParameter("inputId");
	String inputName = request.getParameter("inputName");
	String dealerId = request.getParameter("dealerId");
	String type = request.getParameter("type");
	String inputCode = request.getParameter("inputCode");
	String inputAddrId = request.getParameter("inputAddrId");
	String inputAddr = request.getParameter("inputAddr");
	String inputLinkMan = request.getParameter("inputLinkMan");
	String inputTel = request.getParameter("inputTel");
	String inputPostCode = request.getParameter("inputPostCode");
	String inputStation = request.getParameter("inputStation");
	String rcvOrgId = request.getParameter("rcvOrgId");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查询</title>
</head>
<script type="text/javascript">
	var dealerId = "<%=dealerId %>";
	var type = "<%=type %>";
	var inputId = "<%=inputId %>";
	var inputName = "<%=inputName %>";
	var inputCode = "<%=inputCode %>";
	var inputAddrId = "<%=inputAddrId %>";
	var inputAddr = "<%=inputAddr %>";
	var inputLinkMan = "<%=inputLinkMan %>";
	var inputTel = "<%=inputTel %>";
	var inputPostCode = "<%=inputPostCode %>";
	var inputStation = "<%=inputStation %>";
	var rcvOrgId = "<%=rcvOrgId %>";
	var myPage;
	var url = "<%=contextPath%>/jsp/parts/storageManager/partDlrStockMvManager/queryWhDealer.json";
	var title = null;
	var columns;
	if(type=="1"){
		columns = [
				{header: "选择",  align:'center',dataIndex:'PARENTORG_ID',renderer:getRadio},
				{header: "销售单位名称", dataIndex: 'PARENTORG_NAME', align:'center'},
				{header: "销售单位编码", dataIndex: 'PARENTORG_CODE', align:'center'}
				];
	}
	if(type=="2"){
		columns = [
				{header: "选择",  align:'center',dataIndex:'CHILDORG_ID',renderer:getRadio},
				{header: "接受单位名称", dataIndex: 'CHILDORG_NAME', align:'center'}
				
				];
	}
	if(type=="3"){
		columns = [
				{header: "选择",  align:'center',dataIndex:'ADDR_ID',renderer:getRadio},
				{header: "地址名称", dataIndex: 'ADDR', align:'center'},
				{header: "接收人", dataIndex: 'LINKMAN', align:'center'},
				{header: "电话", dataIndex: 'TEL', align:'center'},
				{header: "邮编", dataIndex: 'POST_CODE', align:'center'},
				{header: "到站名称", dataIndex: 'STATION', align:'center'}
				];
	}


function getRadio(value,meta,record){
	if(type=="1"){	
		return String.format("<input type='radio' value='"+value+"' onclick = 'selData("+'"'+value+'"'+","+'"'+record.data.PARENTORG_NAME+'"'+","+'"'+record.data.PARENTORG_CODE+'"'+","+'"'+record.data.ACCOUNT_ID+'"'+","+'"'+record.data.ACCOUNT_SUM+'"'+","+'"'+record.data.ACCOUNT_KY+'"'+","+'"'+record.data.ACCOUNT_DJ+'"'+")'/>");
	  }
	if(type=="2"){
	   return String.format("<input type='radio' value='"+value+"' onclick = 'selData("+'"'+value+'"'+","+'"'+record.data.CHILDORG_NAME+'"'+","+'"'+record.data.CHILDORG_CODE+'"'+",null,null,null,null)'/>");
	}
	if(type=="3"){
		 return String.format("<input type='radio' value='"+value+"' onclick = 'selData("+'"'+value+'"'+","+'"'+record.data.ADDR+'"'+",null,"+'"'+(record.data.LINKMAN==null?'':record.data.LINKMAN)+'"'+","+'"'+(record.data.TEL==null?'':record.data.TEL)+'"'+","+'"'+(record.data.POST_CODE==null?'':record.data.POST_CODE)+'"'+","+'"'+(record.data.STATION==null?'':record.data.STATION)+'"'+")'/>");
	}
}
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
		parentContainer.showAcount(arg1,arg2,arg3,arg4);
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
</script>
<body onload ="doinit();__extQuery__(1)">
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
<table border="0" class="table_query">
    <th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 查询条件</th>
    <tr>
      <td width="20%"   align="right">名 称：</td>
      <td width="30%"  ><input class="middle_txt" type="text" id="PARENTORG_NAME" name="PARENTORG_NAME" /></td>
      
      <td width="20%"   align="right"><div id="code_name" >编 码：</div></td>
      <td width="30%" ><div id="code_text" ><input class="middle_txt" type="text" id="PARENTORG_CODE" name="PARENTORG_CODE" /></div></td>
    </tr>
  <tr>
    <td   colspan="4" align="center"><input class="normal_btn" type="button" value="查 询" onclick="__extQuery__(1);"/>
    <input class="normal_btn" type="button" value="关 闭" onclick="_hide();"/>
    </td>
  </tr>
</table>

<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />

</form>
</body>
</html>
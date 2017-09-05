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
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查询</title>
</head>
<script type="text/javascript">
var objArr =[];
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
	var myPage;
	var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/selSales.json?dealerId="+dealerId+"&type="+type;
	var title = null;
	var columns;
	if(type=="1"){
		columns = [
				{header: "选择",  align:'center',dataIndex:'PARENTORG_ID',renderer:getRadio},
				{header: "销售单位名称", dataIndex: 'PARENTORG_NAME',  style:'text-align:left;'},
				{header: "销售单位编码", dataIndex: 'PARENTORG_CODE', align:'center'}
				];
	}
	if(type=="2"){
		columns = [
				{header: "选择",  align:'center',dataIndex:'CHILDORG_ID',renderer:getRadio},
				{header: "单位名称", dataIndex: 'CHILDORG_NAME',  style:'text-align:left;'}
				
				];
	}
	if(type=="4"||type=="5"||type=="6"){
		columns = [
				{header: "选择",  align:'center',dataIndex:'CHILDORG_ID',renderer:getRadio},
				{header: "单位名称", dataIndex: 'CHILDORG_NAME',  style:'text-align:left;'},
				{header: "单位编码", dataIndex: 'CHILDORG_CODE', align:'center'}
				];
	}
	if(type=="3"){
		columns = [
				{header: "选择",  align:'center',dataIndex:'ADDR_ID',renderer:getRadio},
				{header: "地址名称", dataIndex: 'ADDR',  style:'text-align:left;'},
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
	   return String.format("<input type='radio' value='"+value+"' onclick = 'selData("+'"'+value+'"'+","+'"'+record.data.CHILDORG_NAME+'"'+","+'"'+record.data.PARENTORG_CODE+'"'+",null,null,null,null)'/>");
	}
	if(type=="3"){
		 return String.format("<input type='radio' value='"+value+"' onclick = 'selData("+'"'+value+'"'+","+'"'+record.data.ADDR+'"'+",null,"+'"'+(record.data.LINKMAN==null?'':record.data.LINKMAN)+'"'+","+'"'+(record.data.TEL==null?'':record.data.TEL)+'"'+","+'"'+(record.data.POST_CODE==null?'':record.data.POST_CODE)+'"'+","+'"'+(record.data.STATION==null?'':record.data.STATION)+'"'+")'/>");
	}
	if(type=="4"){
		 createObjArr(value,record.data.CHILDORG_CODE,record.data.CHILDORG_NAME);
		 return String.format("<input name='chks'  type='checkbox' value='"+value+"' />");
	}
	if(type=="5"){
		 return String.format("<input type='radio' value='"+value+"' onclick = 'selData("+'"'+value+'"'+","+'"'+record.data.CHILDORG_NAME+'"'+","+'"'+record.data.CHILDORG_CODE+'"'+","+'"'+record.data.accountMap.ACCOUNT_ID+'"'+","+'"'+record.data.accountMap.ACCOUNT_SUM+'"'+","+'"'+record.data.accountMap.ACCOUNT_DJ+'"'+","+'"'+record.data.accountMap.ACCOUNT_KY+'"'+")'/>");
	}
    if(type=="6"){
        return String.format("<input type='radio' value='"+value+"' onclick = 'selData("+'"'+value+'"'+","+'"'+record.data.CHILDORG_NAME+'"'+","+'"'+record.data.CHILDORG_CODE+'"'+","+'"'+record.data.accountMap.ACCOUNT_ID+'"'+","+'"'+record.data.accountMap.ACCOUNT_SUM+'"'+","+'"'+record.data.accountMap.ACCOUNT_DJ+'"'+","+'"'+record.data.accountMap.ACCOUNT_KY+'"'+")'/>");
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
    if(type=="6"){
        parentDocument.getElementById(inputId).value = id;
        parentDocument.getElementById(inputName).value = name;
        parentDocument.getElementById(inputCode).value = code;
        parentContainer.initAccount(arg1,arg2,arg3,arg4);
        _hide();
        return;
    }
	if(type=="2"){
		parentDocument.getElementById(inputId).value = id;
		parentDocument.getElementById(inputName).value = name;
		parentDocument.getElementById(inputCode).value = code;
		var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/getDefault.json?id="+id;
	    sendAjax(url, getDefaultResult, 'fm');	
		return;
	}
	
	parentDocument.getElementById(inputId).value = id;
	parentDocument.getElementById(inputName).value = name;
	parentDocument.getElementById(inputCode).value = code;
    parentDocument.getElementById("ADDR_ID").value = "";
    parentDocument.getElementById("ADDR").value = "";
    parentDocument.getElementById("STATION").value = "";
    parentDocument.getElementById("RECEIVER").value = "";
    parentDocument.getElementById("TEL").value = "";
    parentDocument.getElementById("POST_CODE").value = "";
	_hide();
}

function getDefaultResult(jsonObj){
	if (jsonObj != null) {
		var exceptions = jsonObj.Exception;
        if (exceptions) {
            MyAlert(exceptions.message);
            return;
        }

        var defaultData = jsonObj.defaultData;
        
        parentDocument.getElementById("ADDR_ID").value= defaultData.ADDR_ID;
        parentDocument.getElementById("ADDR").value = defaultData.ADDR;
        parentDocument.getElementById("STATION").value= defaultData.STATION;
        parentDocument.getElementById("RECEIVER").value= defaultData.LINKMAN;
        parentDocument.getElementById("TEL").value = defaultData.TEL;
        parentDocument.getElementById("POST_CODE").value = defaultData.POST_CODE;
        _hide();
	}
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
<div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
		<table border="0" class="table_query">
		    <tr>
		      <td width="20%"   class="right">名 称：</td>
		      <td width="30%"  ><input class="middle_txt" type="text" id="PARENTORG_NAME" name="PARENTORG_NAME" /></td>
		      
		      <td width="20%"   class="right"><div id="code_name" >编 码：</div></td>
		      <td width="30%" ><div id="code_text" ><input class="middle_txt" type="text" id="PARENTORG_CODE" name="PARENTORG_CODE" /></div></td>
		    </tr>
		  <tr>
		    <td colspan="4" class="center">
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
	</div>
</div>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</body>
</html>

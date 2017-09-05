<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>服务商维护</title>
<script type="text/javascript">
var dealerLevel=<%=Constant.DEALER_LEVEL_01%>;
function doInit()
{
	__extQuery__(1);
	genLocSel('txt1','','','','',''); // 加载省份城市和县
	
}   

function downloadFunc() {
	var url = "<%=contextPath%>/parts/baseManager/partServicerManager/ServicerInfo/dealerInfoDownload.json" ;
	document.fm.action = url ;
	document.fm.submit();
}


</script>
</head>

<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 基础信息管理 &gt; 配件基础信息维护 &gt;服务商信息</div>
<form id="fm" name="fm" method="POST">
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>

<table class="table_query" border="0">
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">服务商代码：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="DEALER_CODE" maxlength="30" datatype="1,is_noquotation,30" id="DEALER_CODE" type="text" class="middle_txt" /></td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">服务商简称：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="DEALER_NAME" maxlength="30" datatype="1,is_noquotation,75" id="DEALER_NAME" type="text" class="middle_txt" /></td>
	</tr>
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">服务商状态：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
		<label>
				<script type="text/javascript">
					genSelBoxExp("DEALERSTATUS",<%=Constant.STATUS%>,"-1",true,"short_sel",'',"false",'');
				</script>
		</label>
		</td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">省份：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
			<select class="min_sel" id="txt1" name="province"></select>
		</td>
	</tr>
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">服务商类型：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"> 
		<label>
				<script type="text/javascript">
					genSelBoxExp("PDEALERTYPE",<%=Constant.PART_SALE_PRICE_DEALER_TYPE%>,"-1",true,"short_sel",'',"false",'');
				</script>
		</label>
		</td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">服务商公司：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
		<input class="middle_txt" id="COMPANY_NAME" style="cursor: pointer;" name="COMPANY_NAME" type="text" readonly="readonly"/>
		<input class="mark_btn" type="button" value="&hellip;" onclick="showCompany('<%=contextPath %>')"/>
		<input class="cssbutton" type="button" value="清空" onclick="clrTxt('COMPANY_NAME');clrTxt('COMPANY_ID');"/>
		</td>
	</tr>
	
	<tr align="center">
		<td colspan="4" class="table_query_4Col_input" style="text-align: center">
			<input name="queryBtn" type="button" class="normal_btn" onclick="setType()" value="设置类型" id="queryBtn"/> &nbsp; 
			<input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> &nbsp; 
			<input type="button" class="normal_btn" id="downloadIt" name="downloadIt" onclick="downloadFunc() ;" value="下 载" />&nbsp;
		</td>
	</tr>
</table>
</form>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />	
<script>
var myPage;
var url = "<%=contextPath%>/parts/baseManager/partServicerManager/ServicerInfo/queryServicerInfo.json";
//parts/baseManager/partServicerManager/ServicerInfo/
var title= null;
var columns = [
				{header: "设置类型", dataIndex: 'DEALER_ID', align:'center',width: '10%',renderer:typeCheckBox},
				{header: "服务商代码",width:'10%',   dataIndex: 'DEALER_CODE'},
				{header: "服务商简称", width:'20%', dataIndex: 'DEALER_SHORTNAME'},
				{header: "服务商类型", width:'10%', dataIndex: 'PDEALER_TYPE',renderer:getItemValue},
				{header: "服务商公司", width:'20%', dataIndex: 'COMPANY_SHORTNAME'},
				{header: "状态", width:'10%', dataIndex: 'STATUS',renderer:getItemValue},
				{header: "创建人", width:'10%', dataIndex: 'CREATEPER'},
				{header: "创建时间", width:'10%', dataIndex: 'CREATEDATE',renderer:formatDate},
				{header: "更改人", width:'10%', dataIndex: 'UPDATEPER'},
				{header: "更改时间", width:'10%', dataIndex: 'UPDATEDATE'},
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'DEALER_ID',renderer:myLink}
			  ];
			  
function myLink(dealer_id){ 
    return String.format(
    		 "<a href=\"<%=contextPath%>/parts/baseManager/partServicerManager/ServicerInfo/queryServicerInfoDetail.do?DEALER_ID="
            +dealer_id+"\">[查看]</a>");
} 

//设置类型
function setType(){
	var serviceType = document.getElementById('PDEALERTYPE').value;
	if(serviceType==""){
		MyAlert("请选择服务商类型");
		return;
	}
	
	var count=0;
	var s = "" ;
	var ss= document.getElementsByName('setType');
	 for(var i=0;i<ss.length;i++){
		if(ss[i].checked){
			count++;
			 s = s + ss[i].value +"@@" ;
		}
	 }
	 if(count==0){
		 MyAlert("请至少勾选一项");
		 return;
	 }
	 fm.action = '<%=contextPath%>/parts/baseManager/partServicerManager/ServicerInfo/resetServiceType.do?serviceType='+serviceType+'&dealerId='+s;
	 fm.submit();
}

//设置类型复选框
function typeCheckBox(value,meta,record)
{
 	var dealerId=record.data.DEALER_ID;
	 var s = String.format("<input type='checkbox' name = 'setType' id='setType' value='"+dealerId+"'/>");
	 return s;
}



function formatDate(value,meta,record){
	if(value!=null && value!=""){
		return value.substring(0,7).replace("-","");
	}
	else{
		return "";
	}
	
}

function clrTxt(txtId){
   	document.getElementById(txtId).value = "";
}
</script>
</body>
</html>

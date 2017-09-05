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
<title>形象店支持审核</title>
<script type="text/javascript">
var dealerLevel=<%=Constant.DEALER_LEVEL_01%>;
function doInit()
{
	__extQuery__(1);
}   
</script>
</head>

<body onload="genLocSel('txt1','txt2','');">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 财务管理 &gt; 财务管理 &gt;形象店支持审核</div>
<form method="post" id="fm" name="fm">
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>
<input id="dealer_id" name="dealer_id" type="hidden" value="${dealer_id }"/>
<input id="DEALER_TYPE" name="DEALER_TYPE" value="<%=Constant.DEALER_TYPE_DVS %>" type="hidden"/>
<input id="remark" name="remark" type="hidden" value=""/>
<table class="table_query" border="0">
	<tr align="center">
		<td colspan="4" class="table_query_4Col_input" style="text-align: center">
			<input  type="hidden"   value="${user_id}" id="user_id" name="user_id"/> &nbsp; 
			<input  type="hidden"   value="${size}" id="size" name="size"/> &nbsp;
			<input type='button' class='long_btn' onclick="checkDetail('${dealer_id }','2')" value='查看详细信息' />&nbsp;&nbsp;
			<input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> &nbsp; 
		</td>
	</tr>
</table>
</form>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />	
<script>
var myPage;
var index = 0;
var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/viConstructInfoQuery.json";
var title= null;
var arr = new Array();
var columns = [
				{header: "年份", dataIndex: 'YEAR_FLAG'},
				{header: "状态",     dataIndex: 'H_STATE'},
				{header: "车系",     dataIndex: 'GROUP_NAME'},
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'DEALER_ID',renderer:myLink},
				{header: "经销商代码", dataIndex: 'DEALER_CODE'},
				{header: "经销商简称", dataIndex: 'DEALER_SHORTNAME'},
				{header: "备注", dataIndex: 'REMARK',renderer:getText},
				{header: "建设开始日期", dataIndex: 'CONSTRUCT_SDATE',renderer:formatDate1},
				{header: "建设结束日期", dataIndex: 'CONSTRUCT_EDATE',renderer:formatDate1},
				{header: "实地验收日期", dataIndex: 'ACCEPT_DATE',renderer:formatDate1},
				{header: "验收形象店形式", dataIndex: 'CHECKED_TYPE',renderer:getItemValue},
				{header: "支持月度(开始)", dataIndex: 'SUPPORT_SDATE',renderer:formatDate1},
				{header: "支持月度(结束)", dataIndex: 'SUPPORT_EDATE',renderer:formatDate1},
				{header: "年租金", dataIndex: 'YEAR_RENT'},
				{header: "首次返还比例", dataIndex: 'SCALE'},
				{header: "提车支持款", dataIndex: 'AMOUNT'}

			  ];
function myLink(dealer_id,meta,record){
//	arr.push(record.data.H_STATE);
//	var checkCmd = "2";
	var link = "";
	
// 	index = document.getElementById("size").value;
// 	MyAlert(index);
// 	var user_id = document.getElementById("user_id").value;
// 	if(record.data.USER_ID!=user_id){
// 		link = "<input type='button' class='normal_btn' disabled onclick='addViConstruct(\"" + dealer_id + "\",\"" + record.data.YEAR_FLAG_NUM + "\")' value='通过' />";
// 		link += "&nbsp;&nbsp;<input type='button' disabled class='normal_btn' value='驳回' onclick='dialogAddressLink(\"" + dealer_id + "\",\"" + record.data.YEAR_FLAG_NUM + "\")' />";
// 	}else{
//		if(index>0){
//			if(arr[index-1]=='审核通过'||arr[index-1]=='终止'){
//				if(arr[index]=='审核中'){
//					link += "<input type='button' class='normal_btn'  onclick='addViConstruct(\"" + dealer_id + "\",\"" + record.data.YEAR_FLAG_NUM + "\")' value='通过' />";
//					link += "&nbsp;&nbsp;<input type='button' class='normal_btn'  value='驳回' onclick='dialogAddressLink(\"" + dealer_id + "\",\"" + record.data.YEAR_FLAG_NUM + "\")' />";
//				}else{
//					link += "<input type='button' class='normal_btn' disabled onclick='addViConstruct(\"" + dealer_id + "\",\"" + record.data.YEAR_FLAG_NUM + "\")' value='通过' />";
//					link += "&nbsp;&nbsp;<input type='button' disabled class='normal_btn'  value='驳回' onclick='dialogAddressLink(\"" + dealer_id + "\",\"" + record.data.YEAR_FLAG_NUM + "\")' />";
//				}
//			}else{
//				link += "<input type='button' class='normal_btn' disabled onclick='addViConstruct(\"" + dealer_id + "\",\"" + record.data.YEAR_FLAG_NUM + "\")' value='通过' />";
//				link += "&nbsp;&nbsp;<input type='button' disabled class='normal_btn' value='驳回' onclick='dialogAddressLink(\"" + dealer_id + "\",\"" + record.data.YEAR_FLAG_NUM + "\")' />";
//			}
//		}
//		else{
//			if(arr[index]=='审核通过'||arr[index]=='终止'){
//				link += "<input type='button' class='normal_btn' disabled onclick='addViConstruct(\"" + dealer_id + "\",\"" + record.data.YEAR_FLAG_NUM + "\")' value='通过' />";
//				link += "&nbsp;&nbsp;<input type='button' class='normal_btn' disabled value='驳回' onclick='dialogAddressLink(\"" + dealer_id + "\",\"" + record.data.YEAR_FLAG_NUM + "\")' />";
//			}else{
//				link += "<input type='button' class='normal_btn'  onclick='addViConstruct(\"" + dealer_id + "\",\"" + record.data.YEAR_FLAG_NUM + "\")' value='通过' />";
//				link += "&nbsp;&nbsp;<input type='button' class='normal_btn' value='驳回' onclick='dialogAddressLink(\"" + dealer_id + "\",\"" + record.data.YEAR_FLAG_NUM + "\")' />";
//			}
//		}
// 	}
//	index++;
	if (record.data.AUDIT_STATUS == 92861001) {
		link += "<input type='button' class='normal_btn' id='passBtn_"+record.data.YEAR_FLAG_NUM+record.data.DETAIL_ID+"'  onclick='addViConstruct(\"" + dealer_id + "\",\"" + record.data.YEAR_FLAG_NUM + "\",\"" + record.data.DETAIL_ID + "\")' value='通过' />";
		link += "&nbsp;&nbsp;<input type='button' class='normal_btn' id='nopassBtn_"+record.data.YEAR_FLAG_NUM+record.data.DETAIL_ID+"' value='驳回' onclick='dialogAddressLink(\"" + dealer_id + "\",\"" + record.data.YEAR_FLAG_NUM + "\",\"" + record.data.DETAIL_ID + "\")' />";
	}
    return String.format(link);
} 
function checkDetail(obj,cmd){
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/queryViConstructInfoById.do?DEALER_ID="+obj+"&cmd="+cmd+"&DEALER_TYPE=<%=Constant.DEALER_TYPE_DVS%>"+"&isDivShow=1";
// 	fm.action = url;
//  	fm.submit();
 	OpenHtmlWindow(url,850,650);
}
function getText(value,meta,record){
	var YEAR_FLAG = record.data.YEAR_FLAG_NUM;
	var DETAIL_ID = record.data.DETAIL_ID;
	if (record.data.AUDIT_STATUS == 92861001) {
		return String.format("<input name='remark_"+YEAR_FLAG+DETAIL_ID+"'  id='remark_"+YEAR_FLAG+DETAIL_ID+"' type='text' value='"+value+"'  class='long_txt'>");
	} else {
		return String.format("<input readonly='true' name='remark_"+YEAR_FLAG+DETAIL_ID+"'  id='remark_"+YEAR_FLAG+DETAIL_ID+"' type='text' value='"+value+"'  class='long_txt'>");
	}		
}
function formatDate1(value,meta,record) {
	if (value==""||value==null) {
		return "";
	}else {
		return String.format(value.substr(0,10));
	}
}
function formatDate(value,meta,record) {
	if (value==""||value==null) {
		return "";
	}else {
		return String.format(value.substr(0,8));
	}
}
function dialogAddressLink(dealer_id,YEAR_FALG,DETAIL_ID) {
	if (confirm("确认要驳回?")){
	 	document.getElementById("nopassBtn_"+YEAR_FALG+DETAIL_ID).disabled = "disabled";
		var remark = document.getElementById("remark_"+YEAR_FALG+DETAIL_ID).value;
		document.getElementById("remark").value = remark;
		var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/BOHUI.do?DEALER_ID="+dealer_id+"&YEAR_FALG="+YEAR_FALG+"&DETAIL_ID="+DETAIL_ID;
		fm.action = url;
	 	fm.submit();
	}
}

function addViConstruct(obj,YEAR_FALG,DETAIL_ID){
	 if (confirm("确认要通过?")){
		 	document.getElementById("passBtn_"+YEAR_FALG+DETAIL_ID).disabled = "disabled";
			var remark = document.getElementById("remark_"+YEAR_FALG+DETAIL_ID).value;
			document.getElementById("remark").value = remark;
			var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/SHENHETONGGUO.do?DEALER_ID="+obj+"&YEAR_FALG="+YEAR_FALG+"&DETAIL_ID="+DETAIL_ID;
			fm.action = url;
		 	fm.submit();
	 }
}

function formatDate(value,meta,record){
	if(value!=null && value!=""){
		return value.substring(0,7).replace("-","");
	}
	else{
		return "";
	}
	
}
function add()
{
	window.location.href='<%=contextPath%>/sysmng/dealer/DealerInfo/addNewDealer.do';
}

function clrTxt(txtId){
   	document.getElementById(txtId).value = "";
}
</script>
</body>
</html>

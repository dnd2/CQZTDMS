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
<title>经销商维护</title>
</head>

<body>	
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;经销商地址打印</div>
<form id="fm" name="fm">
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>
<table class="table_query" border="0">
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商代码：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="DEALER_CODE" maxlength="30" datatype="1,is_noquotation,30" id="DEALER_CODE" type="text" class="middle_txt" /></td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商名称：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="DEALER_NAME" maxlength="30" datatype="1,is_noquotation,75" id="DEALER_NAME" type="text" class="middle_txt" /></td>
	</tr>
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商类型：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
			<label>
				<script type="text/javascript">
					genSelBoxExp("DEALER_TYPE",<%=Constant.DEALER_TYPE%>,"-1",false,"short_sel","onchange='dispalyAddressType(this);'","true",'<%=Constant.DEALER_TYPE_JSZX%>, <%=Constant.DEALER_TYPE_QYZDL%>');
				</script>
			</label>
		</td>
		<td id = "add_type_XS" class="table_query_3Col_label_6Letter" nowrap="nowrap" >地址类型(销售)：</td>
		<td  id = "add_type_val_XS" class="table_query_4Col_input" nowrap="nowrap" >
			<select  id = "add_type_val_XS1" class="short_sel" name="ADDRESS_TYPE">
				<option value="20481002">发票收货地址</option>
				<option value="20481001">广宣收货地址</option>
				<option value="20481003">信函收货地址</option>
				<option value="20481004">整车收货地址</option>
				<option value="">所有地址</option>
			</select>
		</td>
		<td id = "add_type_SH" class="table_query_3Col_label_6Letter" nowrap="nowrap" style="display: none">地址类型(售后)：</td>
		<td  id = "add_type_val_SH" class="table_query_4Col_input" nowrap="nowrap" style="display: none">
			<select id = "add_type_val_SH1" class="short_sel" name="ADDRESS_TYPE_SH" >
				<option value="20491001">配件接收地址</option>
				<option value="20491002">发票邮寄地址</option>
				<option value="20491003">信件接收地址</option>
				<option value="">所有地址</option>
			</select>
		</td>
	</tr>
	<tr align="center">
		<td colspan="6" class="table_query_4Col_input" style="text-align: center">
			<input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> &nbsp; 
			<input name="button2" type="reset" class="normal_btn" value="重置" /> &nbsp;
			<input name="button3" type="reset" class="normal_btn" onclick="exp()" value="导出" /> &nbsp;
		</td>
	</tr>
</table>
</form>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />	
<script>
var myPage;
var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/dealerAddressQuery.json";
var title= null;
var columns = [
				{header: "经销商代码",width:'10%',   dataIndex: 'DEALER_CODE'},
				{header: "经销商名称", width:'20%', dataIndex: 'DEALER_NAME'},
				{header: "经销商类型", width:'20%', dataIndex: 'DEALER_TYPE',renderer:getItemValue},
				//{header: "经销商等级", width:'10%', dataIndex: 'DEALER_LEVEL',renderer:getItemValue},
				//{header: "上级经销商名称", width:'20%', dataIndex: 'PARENT_DEALER_NAME'},
				//{header: "组织名称", width:'20%', dataIndex: 'ORG_NAME'},
				{header: "所属公司", width:'20%', dataIndex: 'COMPANY_NAME'},
				{header: "详细地址",width:'30%',   dataIndex: 'ADDRESS'},
				{header: "地址类型", width:'10%', dataIndex: 'ADDRESS_TYPE',renderer:getDealerType},
				{header: "状态", width:'10%', dataIndex: 'STATUS',renderer:getItemValue},
				{header: "联系人姓名", width:'20%', dataIndex: 'LINK_MAN'},
				{header: "性别", width:'20%', dataIndex: 'SEX',renderer:getItemValue},
				{header: "手机", width:'10%', dataIndex: 'MOBILE_PHONE'},
				{header: "电话", width:'10%', dataIndex: 'TEL'},
				//{header: "经销商状态", width:'10%', dataIndex: 'SERVICE_STATUS',renderer:getItemValue},
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'ID',renderer:myLink}
			  ];
			  
function myLink(value){
	if(value == "") {
		return "";
	} else {
		return String.format("<a target=\"_blank\" href=\"<%=contextPath%>/sysmng/dealer/DealerInfo/queryDealerAddressById.do?ID="+ value +"\">[打印]</a>"+"<a target=\"_blank\" href=\"<%=contextPath%>/sysmng/dealer/DealerInfo/queryDealerAddressById.do?type=newPrint&ID="+ value +"\">[打印物流]</a>");
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
function exp(){
	var form = document.getElementById("fm");
	form.setAttribute("action", "<%=contextPath%>/sysmng/dealer/DealerInfo/exportPartCheckExcel.do");
	form.setAttribute("method", "post");
	form.submit();
}
function getDealerType(value) {
	if(value == "20481001") {
		return "广宣收货地址";
	} else if(value == "20481002") {
		return "发票收货地址";
	} else if(value == "20481003") {
		return "信函收货地址";
	} else if(value == "20481004") {
		return "整车收货地址";
	} else if(value == "20491001") {
		return "配件收货地址";
	} else if(value == "20491002"){
		return "发票邮寄地址";
	} else if(value == "20491003"){
		return "信件接收地址";
	} else{
		return "";
	}
}
function dispalyAddressType(_this) {
	if(_this.value==10771001){
		document.getElementById("add_type_XS").style.display = "";
		document.getElementById("add_type_val_XS").style.display = "";
		document.getElementById("add_type_val_XS1").disabled = false;
		document.getElementById("add_type_SH").style.display = "none";
		document.getElementById("add_type_val_SH").style.display = "none";
		document.getElementById("add_type_val_SH1").disabled = true;
	}else{
		document.getElementById("add_type_XS").style.display = "none";
		document.getElementById("add_type_val_XS").style.display = "none";
		document.getElementById("add_type_val_XS1").disabled = true;
		document.getElementById("add_type_SH").style.display = "";
		document.getElementById("add_type_val_SH").style.display = "";
		document.getElementById("add_type_val_SH1").disabled = false;
	}
}
</script>
</body>
</html>

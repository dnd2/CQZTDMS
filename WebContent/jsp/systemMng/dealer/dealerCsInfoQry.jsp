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
<title>经销商查询</title>
<script type="text/javascript">
var dealerLevel=<%=Constant.DEALER_LEVEL_01%>;
function doInit()
{
	/*var dl=document.getElementById("DEALERLEVEL").value;
	if(dl != ""){
		if(dealerLevel==dl)
		{
			document.getElementById("sJDealerCode").disabled="true";
			document.getElementById("dealerbu").disabled="true";
			document.getElementById("orgCode").disabled="";
			document.getElementById("orgbu").disabled="";
			
		}else
		{
			document.getElementById("sJDealerCode").disabled="";
			document.getElementById("dealerbu").disabled="";
			document.getElementById("orgCode").disabled="true";
			document.getElementById("orgbu").disabled="true";		
		}
	}*/
	genLocSel('txt1','','','','',''); // 加载省份城市和县
}   

function downloadFunc() {
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/dealerInfoDownload.json" ;
	document.fm.action = url ;
	document.fm.submit() ;
}

window.onload = function(){
	MyAlert($("from[name='fm']").serialize());
}
</script>
</head>

<body onload="genLocSel('txt1','txt2','');">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;经销商维护</div>
<form id="fm" name="fm">
<input id="DEALER_TYPE" name="DEALER_TYPE" value="<%=Constant.DEALER_TYPE_DWR %>" type="hidden"/>
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>
<table class="table_query" border="0">
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商代码：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="DEALER_CODE" maxlength="30" datatype="1,is_noquotation,30" id="DEALER_CODE" type="text" class="middle_txt" /></td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商简称：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="DEALER_NAME" maxlength="30" datatype="1,is_noquotation,75" id="DEALER_NAME" type="text" class="middle_txt" /></td>
	</tr>
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商等级：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"> 
		<label>
				<script type="text/javascript">
					genSelBoxExp("DEALERLEVEL",<%=Constant.DEALER_LEVEL%>,"-1",'true',"short_sel",'',"false",'');
				</script>
		</label>
		</td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商状态：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
		<label>
				<script type="text/javascript">
					genSelBoxExp("DEALERSTATUS",<%=Constant.STATUS%>,"-1",true,"short_sel",'',"false",'');
				</script>
		</label>
		</td>
	</tr>
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">上级组织：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
		<input type="text"  name="orgCode" size="15" value=""  id="orgCode" class="middle_txt" datatype="1,is_noquotation,75" />
		<input name="orgbu"  id="orgbu" type="button" class="mark_btn" onclick="showOrg('orgCode','','false')" value="&hellip;" />
		<input class="cssbutton" type="button" value="清空" onclick="clrTxt('orgCode');"/>
		</td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">上级经销商：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
		<input type="text"  name="sJDealerCode" size="15" value=""  id="sJDealerCode" class="middle_txt" datatype="1,is_noquotation,75"/>
		<input name="dealerbu"  id="dealerbu" type="button" class="mark_btn" onclick="showOrgDealer('sJDealerCode','','false','','true')" value="&hellip;" />
		<input class="cssbutton" type="button" value="清空" onclick="clrTxt('sJDealerCode');"/>
		</td>
	</tr>
	<tr>
		<td id="sh_xxzt" class="table_query_2Col_label_6Letter">信息状态：</td>
	    <td class="table_query_4Col_input">
		    <label>
				<script type="text/javascript">
					genSelBoxExp("SERVICE_STATUS",<%=Constant.DLR_SERVICE_STATUS%>,"",'true',"short_sel",'',"false",'');
				</script>
		    </label>
	    </td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商公司：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
		<input class="middle_txt" id="COMPANY_NAME" style="cursor: pointer;" name="COMPANY_NAME" type="text" readonly="readonly"/>
		<input class="mark_btn" type="button" value="&hellip;" onclick="showCompany('<%=contextPath %>')"/>
		<input class="cssbutton" type="button" value="清空" onclick="clrTxt('COMPANY_NAME');clrTxt('COMPANY_ID');"/>
		</td>
	</tr>
	<tr align="center">
		<td colspan="4" class="table_query_4Col_input" style="text-align: center">
			<input  type=reset class="normal_btn"  value="重置" id="queryBtn" /> &nbsp; 
			<input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> &nbsp; 
			<input type="button" class="normal_btn" id="downloadIt" name="downloadIt" onclick="exportFunc() ;" value="下 载" />&nbsp;
		</td>
	</tr>
</table>
</form>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />	
<script>
var myPage;
var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/queryDealerInfo.json";
var title= null;
var dl=document.getElementById("DEALERLEVEL").value;
var columns = [
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'DEALER_ID',renderer:myLink},
				{header: "经销商代码",width:'10%',   dataIndex: 'DEALER_CODE'},
				{header: "经销商名称",width:'10%', dataIndex: 'DEALER_NAME'},
				{header: "经销商简称", width:'20%', dataIndex: 'DEALER_SHORTNAME'},
				//{header: "服务电话", width:'10%', dataIndex: 'PHONE'},
				//{header: "地址", width:'20%', dataIndex: 'ZZADDRESS'},
				//{header: "24小时值班电话", width:'10%', dataIndex: 'DUTY_PHONE'},
				//{header: "站长手机", width:'10%', dataIndex: 'WEBMASTER_PHONE'},
				//{header: "销售经理电话", width:'10%', dataIndex: 'PHONE'},
				//{header: "销售地址", width:'20%', dataIndex: 'ADDRESS'},
				{header: "经销商等级", width:'10%', dataIndex: 'DEALER_LEVEL',renderer:getItemValue},
				//{header: "经销商类型", width:'10%', dataIndex: 'DEALER_TYPE',renderer:getItemValue},
				{header: "上级经销商", width:'20%', dataIndex: 'SHANGJINAME'},
				//{header: "上级组织", width:'20%', dataIndex: 'SHANGJIORGNAME'},
				{header: "组织名称", width:'20%', dataIndex: 'ORG_NAME'},
				{header: "所属公司", width:'20%', dataIndex: 'COMPANY_NAME'},
				{header: "状态", width:'10%', dataIndex: 'STATUS',renderer:getItemValue}
				//{header: "创建人", width:'10%', dataIndex: 'CREATEPER'},
				//{header: "创建时间", width:'10%', dataIndex: 'CREATEDATE',renderer:formatDate},
				//{header: "更改人", width:'10%', dataIndex: 'UPDATEPER'},
				//{header: "更改时间", width:'10%', dataIndex: 'UPDATEDATE'}		
			  ];
			  
function myLink(dealer_id){
	var link = "<input type='button' class='normal_btn' onclick='searchDetail("+dealer_id+")' value='查看信息'></a>";
		link += "&nbsp;&nbsp;<input type='button' class='normal_btn' onclick='dialogAddressLink("+dealer_id+")' value='查看地址'></a>";
	
    return String.format(link);
} 

function dialogAddressLink(dealer_id) {
	return OpenHtmlWindow("<%=contextPath%>/jsp/systemMng/dealer/dealerCsAddressQuery.jsp?DEALER_ID="+dealer_id,700,500);
}

function searchDetail(obj){
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfoQuery/queryDealerInfoDetail.do?DEALER_ID="+obj+"&DEALER_TYPE=<%=Constant.DEALER_TYPE_DWR%>";
	OpenHtmlWindow(url,800,600);
}

function formatDate(value,meta,record){
	if(value!=null && value!=""){
		return value.substring(0,7).replace("-","");
	}
	else{
		return "";
	}
	
}
function fwset(value){
	if(value!="" && value==<%=Constant.MSG_TYPE_2%>){//售后才放出这查询条件
		document.getElementById("sh_seach").style.display="";
		document.getElementById("sh_xxzt").style.display="none";
		
	}else{
		document.getElementById("sh_seach").style.display="none";
		document.getElementById("sh_xxzt").style.display="";
	}
}
function doCusChange(value)
{
	/*if(value == ""){
		document.getElementById("sJDealerCode").disabled="";
		document.getElementById("dealerbu").disabled="";
		document.getElementById("orgCode").disabled="";
		document.getElementById("orgbu").disabled="";
	}
	else{
		if(dealerLevel==value)
		{
			document.getElementById("sJDealerCode").disabled="true";
			document.getElementById("dealerbu").disabled="true";
			document.getElementById("orgCode").disabled="";
			document.getElementById("orgbu").disabled="";
			
		}else
		{
			document.getElementById("sJDealerCode").disabled="";
			document.getElementById("dealerbu").disabled="";
			document.getElementById("orgCode").disabled="true";
			document.getElementById("orgbu").disabled="true";		
		}
	}*/
}
function add()
{
	window.location.href='<%=contextPath%>/sysmng/dealer/DealerInfo/addNewDealer.do';
}

function clrTxt(txtId){
   	document.getElementById(txtId).value = "";
}

function downloadFunc() {
    var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/dealerInfoDownload.json" ;
    document.fm.action = url ;
    document.fm.submit() ;
}
function exportFunc() {
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfoQuery/dealerInfoDownloadNew.json?DEALER_TYPE=<%=Constant.DEALER_TYPE_DWR%>";
	document.fm.action = url ;
	document.fm.submit() ;
}
</script>
</body>
</html>

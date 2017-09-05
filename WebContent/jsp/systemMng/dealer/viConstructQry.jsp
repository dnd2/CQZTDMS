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
<title>形象店支持查询</title>
<script type="text/javascript">
var dealerLevel=<%=Constant.DEALER_LEVEL_01%>;
function doInit()
{
	__extQuery__(1);
}   
</script>
</head>

<body onload="genLocSel('txt1','txt2','');">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 财务管理 &gt; 财务管理 &gt;形象店支持查询</div>
<form method="post" id="fm" name="fm">
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>
<input id="DEALER_TYPE" name="DEALER_TYPE" value="<%=Constant.DEALER_TYPE_DVS %>" type="hidden"/>
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
   <td align=right>选择大区：</td>
		        <td align=left>
					<select id="orgId" name="orgId">
		        		<option value="">-- 请选择 --</option>
		        		<c:forEach items="${orglist}" var="list">
		        			<option value="${list.ORG_ID}">${list.ORG_NAME}</option>
		        		</c:forEach>
		        	</select>
	</td>	
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">选择省份：</td>
      		<td class="table_query_4Col_input" nowrap="nowrap">
				<input type="text"  readonly="readonly"  name="regionCode" size="15" value=""  id="regionCode" class="middle_txt" />
				<input name="regbu"  id="regbu" type="button" class="mark_btn" onclick="showRegion('regionCode','regionId','true')" value="..." />
				<input type="hidden"  name="regionId" size="15" value=""  id="regionId" />
				<input type="button"  class="cssbutton" value="清除" onclick="clrTxt('regionCode');clrTxt('regionId');"/>
			</td>
	</tr>
	<tr align="center">
		<td colspan="4" class="table_query_4Col_input" style="text-align: center">
			<input  type=reset class="normal_btn"  value="重置" id="clearBtn" /> &nbsp; 
			<input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> &nbsp; 
		</td>
	</tr>
</table>
</form>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />	
<script>
var myPage;
var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/queryViConstructInfo.json";
var title= null;
var columns = [
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'DEALER_ID',renderer:myLink},
				{header: "大区",width:'10%',   dataIndex: 'ROOT_ORG_NAME'},
				{header: "省份",width:'10%',   dataIndex: 'REGION_NAME'},
				{header: "经销商代码",width:'10%',   dataIndex: 'DEALER_CODE'},
				{header: "经销商名称",width:'10%', dataIndex: 'DEALER_NAME'},
				{header: "经销商简称", width:'20%', dataIndex: 'DEALER_SHORTNAME'},
				{header: "是否待审核", width:'20%', dataIndex: 'IS_HAVE_AUDIT'}
			  ];
function myLink(dealer_id,meta,record){
	var link;
	var MAIN_ID_SUV = record.data.MAIN_ID_SUV;
	var MAIN_ID_MPV = record.data.MAIN_ID_MPV;
	// cmd=1 表示维护  addCmd=0表示新增
	if(MAIN_ID_SUV || MAIN_ID_MPV){
		link = "<input type='button' class='normal_btn' onclick='addViConstruct(\"" + dealer_id + "\",2)' value='查看' />&nbsp;&nbsp;";
		if (MAIN_ID_SUV) {
			link += "<input type='button' class='normal_btn' onclick='addViConstruct(\"" + dealer_id + "\",1,\"2014032694231206\")' value='维护SUV' />";
		} else {
			link = "<input type='button' class='normal_btn' onclick='addViConstruct(\"" + dealer_id + "\",0,\"2014032694231206\")' value='新增SUV' />";
		}
		if (MAIN_ID_MPV) {
			link += "<input type='button' class='normal_btn' onclick='addViConstruct(\"" + dealer_id + "\",1,\"2015011508783002\")' value='维护MPV' />";
		} else {
			link += "<input type='button' class='normal_btn' onclick='addViConstruct(\"" + dealer_id + "\",0,\"2015011508783002\")' value='新增MPV' />";
		}
	}else{
		link = "<input type='button' class='normal_btn' onclick='addViConstruct(\"" + dealer_id + "\",0,\"2014032694231206\")' value='新增SUV' />";
		link += "<input type='button' class='normal_btn' onclick='addViConstruct(\"" + dealer_id + "\",0,\"2015011508783002\")' value='新增MPV' />";
	}
    return String.format(link);
} 

function dialogAddressLink(dealer_id) {
	return OpenHtmlWindow("<%=contextPath%>/jsp/systemMng/dealer/dealerAddressQuery.jsp?DEALER_ID="+dealer_id,700,500);
}

function addViConstruct(obj,cmd, suvOrMpv){
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/queryViConstructInfoById.do?DEALER_ID="+obj+"&cmd="+cmd+"&DEALER_TYPE=<%=Constant.DEALER_TYPE_DVS%>"+"&suvOrMpv="+suvOrMpv;
	fm.action = url;
 	fm.submit();
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
// 选择商务政策
function selectOther(id, otherid) {
	OpenHtmlWindow(g_webAppName + '/sales/financemanage/SalesPolicyManager/queryDeployedPolicyInit.do?id='+id+'&otherid='+otherid,700,500);
}
</script>
</body>
</html>

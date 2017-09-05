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
<title>服务站备案信息变更管理</title>
<script type="text/javascript">
var dealerLevel=<%=Constant.DEALER_LEVEL_01%>;
function doInit()
{
	__extQuery__(1);
}   
</script>
</head>

<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;服务站备案信息变更管理</div>
<form method="post" id="fm" name="fm">
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>
<table class="table_query" border="0">
	<tr align="center">
		<td colspan="4" class="table_query_4Col_input" style="text-align: center">
			<input name="queryBtn" type="button" class="normal_btn" onclick="doQuery()" value="查 询" id="queryBtn" /> &nbsp; 
			<input name="queryBtn" type="button" class="normal_btn" onclick="updateInfo()" value="修 改" id="queryBtn" /> &nbsp; 
		</td>
	</tr>
</table>
</form>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />	
<script>
var myPage;
var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/queryDealerInfoForDealer.json";
var title= null;
var columns = [
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'ID',renderer:myLink},		
				{header: "审核状态",width:'10%',   dataIndex: 'CHANGE_AUTID_STATUS',renderer:getItemValue},
               	{header: "大区",width:'10%',   dataIndex: 'ROOT_ORG_NAME'},
				{header: "省份",width:'10%',   dataIndex: 'REGION_NAME'},
				{header: "经销商代码",width:'10%',   dataIndex: 'DEALER_CODE'},
				{header: "经销商名称", width:'20%', dataIndex: 'DEALER_NAME'},
				{header: "经销商等级", width:'10%', dataIndex: 'DEALER_LEVEL',renderer:getItemValue},
// 				{header: "上级经销商名称", width:'20%', dataIndex: 'PAR_DEALER_NAME'},
				{header: "组织名称", width:'20%', dataIndex: 'ORG_NAME'},
				{header: "所属公司", width:'20%', dataIndex: 'COMPANY_NAME'},
				{header: "经销商状态", width:'10%', dataIndex: 'SERVICE_STATUS',renderer:getItemValue},
				{header: "授权类型", width:'10%', dataIndex: 'AUTHORIZATION_TYPE'},
				{header: "授权时间 ", width:'10%', dataIndex: 'AUTHORIZATION_DATE',renderer:formatDate},
				{header: "经营类型", width:'10%', dataIndex: 'WORK_TYPE'},
				{header: "验收形象等级", width:'10%', dataIndex: 'IMAGE_COMFIRM_LEVEL',renderer:getItemValue}
				
			  ];
function myLink(value,meta,record){
	var link = "";
		link += "<input type='button' class='normal_btn' onclick='searchDetail("+value+")' value='查看' />"; 
    return String.format(link);
} 
function updateInfo(value){
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/checkEx.json?";
	makeCall(url, showFunc_, "fm");
} 
function showFunc_(json){
	if(json.checkEx==true){
		MyAlert("有未审核通过的数据，需要先审核通过才能在修改");
	}else{
		var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/queryDealerCsInfoForDealer.do?cmd=2";
		 document.fm.action = url ;
		 document.fm.submit() ;
	}
	 
} 
function searchDetail(value){
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfoQuery/queryDealerChangeInfoDetail.do?ID="+value+"&DEALER_TYPE=<%=Constant.DEALER_TYPE_DWR%>";
	OpenHtmlWindow(url,800,600);
} 

function formatDate(value,meta,record){
	if(value!=null && value!=""){
		return value.substring(0,10);
	}
	else{
		return "";
	}
}

function doQuery() {
    __extQuery__(1);
}
</script>
</body>
</html>

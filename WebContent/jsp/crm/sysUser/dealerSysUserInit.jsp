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
	<!-- <link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
	<script type="text/javascript" src="<%=contextPath%>/js/jslib/mootools.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/my-grid-pager.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/dealer_tree.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/framecommon/HashMap.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/framecommon/default.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/validate/validate.js"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/web/dtree.js"></script>
	<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/dict.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/crm/sysUser/dealerSysUser.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/dialog_new.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/common.js"></script> -->
	
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	
<title>经销商用户维护</title>
<style>
.img {
	border: none
}
</style>
</head>

<body onload="__extQuery__(1);">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 潜客管理 &gt; 团队管理 &gt; 岗位人员管理</div>
<form id="fm" name="fm">
<input type="hidden" name="curPage" id="curPage" value="1" />
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden" value="${companyId}"/>
<input type="hidden" id="orderCol" name="orderCol" value="" />
<input type="hidden" id="order" name="order" value="" />
<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
<table class="table_query" border="0">
<tr>
		<td class="table_query_2Col_label_4Letter" nowrap="nowrap">用户账号：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
			<input class="middle_txt" type="text" maxlength="30" datatype="1,is_noquotation,30" id="ACNT" name="ACNT"/>
		</td>
		<td class="table_query_2Col_label_5Letter" nowrap="nowrap">职位级别：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
			<input class="middle_txt" type="text" maxlength="30" datatype="1,is_noquotation,30" id="POSE_RANK" name="POSE_RANK"/>
		</td>
		<td class="table_query_2Col_label_5Letter" nowrap="nowrap">姓名：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
			<input class="middle_txt" type="text" maxlength="30" datatype="1,is_noquotation,30" id="NAME" name="NAME" value=""/>
		</td>
		
	</tr>
	<tr>
	<td class="table_query_2Col_label_5Letter" nowrap="nowrap">是否在职：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
			<select name="userStatus" id="userStatus" style="width:136px;" >
		  		<option value="">--请选择--</option>
		  		<option value="10011001">是</option>
		  		<option value="10011002">否</option>
		  	</select>
		</td>
		<td class="table_query_2Col_label_5Letter" nowrap="nowrap">顾问小组：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
		  	<select name="groupId" id="groupId" style="width:136px;" >
		  		<option value="">--请选择--</option>
		  		<c:forEach items="${tgpList}" var="po" >
		  			<option value="${po.groupId}">${po.groupName}</option>
		  		</c:forEach>
		  	</select>
		</td>
		<td class="table_query_2Col_label_5Letter" nowrap="nowrap">是否锁定：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
			<select name="isLock" id="isLock" style="width:136px;" >
		  		<option value="">--请选择--</option>
		  		<option value="1">是</option>
		  		<option value="0">否</option>
		  	</select>
		</td>
	
	</tr>
	<tr>
		<td align="center" colspan="6">
			
			<input class="normal_btn" type="button" value="查 询" id="queryBtn" onclick="__extQuery__(1)" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input class="normal_btn" type="button" value="新 增" onclick="window.location.href='<%=contextPath%>/crm/sysUser/DealerSysUser/addSgmDealerSysUserInit.do'" value="新 增" />
		</td>
	</tr>
</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<div id="_page" style="margin-top:15px;display:none;"></div>
<div id="myGrid" ></div>
<div id="myPage" class="pages"></div>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>


</body>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar1", "topbar")</script>
<script type="text/javascript">
validateConfig.isOnBlur = false;
var  GOLB_SPLIT_HIDE_HREF = false;
var url = "<%=contextPath%>/crm/sysUser/DealerSysUser/sgmDealerSysUserQuery.json?COMMAND=1";
var pat = "<%=contextPath%>";



var myPage;
var title = null;

var columns = [
				{header: "序号", align:'center', renderer:getIndex,width:'7%'},
				{header: "用户账号", dataIndex: 'ACNT', align:'center'},
				{header: "经销商", dataIndex: 'COMPANY_SHORTNAME', align:'center'},
				{header: "职位级别",  dataIndex: 'POSE_RANK', align:'center'},
				{header: "组",  dataIndex: 'GROUP_NAME', align:'center'},
				{header: "姓名",  dataIndex: 'NAME', align:'center'},
				{header: "是否在职",  dataIndex: 'USER_STATUS', align:'center', renderer:getPosition},
				{header: "是否锁定",  dataIndex: 'IS_LOCK', align:'center', renderer:getLock},
				{header: "最后操作人", sortable: true, dataIndex: 'UPDATE_NAME', align:'center'},
				{header: "最后更新时间", sortable: true, dataIndex: 'UPDATE_DATE', align:'center'},
				{id:'action',header: "操作", width:70,sortable: false,dataIndex: 'USER_ID',renderer:myLink}
		      ];

function myLink(value,metadata){
   return String.format(
          "<a href=\""+pat+"/crm/sysUser/DealerSysUser/uealerUserModifyInit.do?userId="+ value + "\">[查看]</a>");
}


//是否在职
function getPosition(value,metadata){
	var str="";
	if(value=="10011001"){
		str="是";
	}else{
		str="否";
	}
	return String.format(str);
}
//是否锁定
function getLock(value,metadata){
	var str="";
	if(value=="1"){
		str="是";
	}else{
		str="否";
	}
return String.format(str);
}

function loadGroup(obj){
	$("groupId").value=obj.getAttribute("TREE_ID");
}
</script>
</html>

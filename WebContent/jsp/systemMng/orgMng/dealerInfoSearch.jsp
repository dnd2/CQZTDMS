<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/globalVariable.jsp" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<script type="text/javascript" src="<%=FileConstant.regionJsUrl%>"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
	<script type="text/javascript" src="<%=contextPath%>/js/jslib/mootools.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/my-grid-pager.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/dept_tree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/validate/validate.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/web/dtree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/dict.js"></script>
<title>经销商信息查询</title>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 部门管理 &gt; 经销商信息查询
		</div>
		<form id="fm">
			<input type="hidden" name="curPage" id="curPage" value="1" />
			<input type="hidden" id="orderCol" name="orderCol" value="" />
			<input type="hidden" id="order" name="order" value="" />
			<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
			<table class="table_query">
				<tr>
					<td class="table_query_2Col_label_5Letter" nowrap="nowrap">所属部门：</td>
					<td class="table_query_2Col_input"   nowrap="nowrap">
						<input class="middle_txt" id="DEPT_NAME" onblur="isCloseTreeDiv(event,this,'deptt')" onclick="showDEPT()"
style="cursor: pointer;" name="DEPT_NAME" type="text"/>
						<input  type="hidden" size="20" name="DEPT_ID" id="DEPT_ID" class="middle_txt" readonly/>
					</td>
					<td class="table_query_2Col_label_5Letter" nowrap="nowrap" >公司类型：</td>
					<td class="table_query_2Col_input" nowrap="nowrap">
						<select name="COMPANY_TYPE" class="short_sel">
							<option value="0">请选择</option>
							<option value="<%=Constant.COMPANY_TYPE_DEALER %>">经销商</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="table_query_2Col_label_5Letter" nowrap="nowrap">经销商代码：</td>
					<td class="table_query_2Col_input"   nowrap="nowrap">
						<input class="middle_txt"  type="text" name="DLR_CODE" id="DLR_CODE" datatype="1,is_digit_letter,<%=Constant.Length_Check_Char_10 %>"/>
					</td>
					<td class="table_query_2Col_label_5Letter" nowrap="nowrap" >经销商名称：</td>
					<td class="table_query_2Col_input" nowrap="nowrap">
						<input class="middle_txt"  type="text" name="DLR_NAME" id="DLR_NAME" datatype="1,is_null,<%=Constant.Length_Check_Char_30 %>"/>	
					</td>
				</tr>
				<tr class="table_query_last">
					<td colspan="4">
						<input id="queryBtn" class="normal_btn" type="button" value="查 询" onclick="__extQuery__(1)" />	
					</td>
				</tr>
			</table>
		</form>
		<div id="_page" style="margin-top:15px;display:none;"></div>
		<div id="myGrid" ></div>
		<div id="myPage" class="pages"></div>
		<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;'></div>
	</div>
<script type="text/javascript" >
	var dept_tree_url = "<%=contextPath%>/sysmng/usemng/SgmSysUser/initOrgTree.json";
	
	var myPage;
	function __extQuery__(page){
		$("queryBtn").disabled = "disabled";
		showMask();
		submitForm('fm') ? sendAjax(url+(url.lastIndexOf("?") == -1?"?":"&")+"curPage="+page,callBack,'fm') : ($("queryBtn").disabled = "",removeMask());
	}

	var url = "<%=contextPath%>/sysmng/orgmng/DlrInfoMng/dlrInfoSearch.json?command=1";
	
	var title= null;

	var columns = [
					{header: "序号",width:'5%',  renderer:getIndex},
					{header: "经销商代码", width:'10%', dataIndex: 'dlrCode',orderCol:"company_code"},
					{header: "经销商名称", width:'22%',orderType:"pingyin", dataIndex: 'dlrName',orderCol:"company_name"},
					{header: "经销商简称", width:'13%',orderType:"pingyin", dataIndex: 'dlrNameForShort',orderCol:"company_short_name"},
					{header: "状态", width:'7%', dataIndex: 'dlrStat', renderer:getItemValue,orderCol:"company_stat"},
					{header: "省份", width:'12%', dataIndex: 'province',renderer:getRegionName,orderCol:"province"},
					{header: "城市", width:'12%', dataIndex: 'city',renderer:getRegionName,orderCol:"city"},
					{header: "所属部门", width:'11%',orderType:"pingyin", dataIndex: 'orgName',orderCol:"dept_name"},
					{id:'action',header: "操作",width:'6%',dataIndex: 'dlrId',renderer:myLink}
				  ];   
	//设置超链接
	function myLink(value){
        return String.format(
               "<a href=\"<%=contextPath%>/sysmng/orgmng/DlrInfoMng/querySingleDlrInfo.do?dlrId="+value+"\">[明细]</a>");
    }
</script>
</body>
</html>

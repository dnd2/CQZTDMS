<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.bean.Dealer" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.*" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<!-- <script type="text/javascript" src="<%=FileConstant.regionJsUrl%>"></script> -->
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<!-- <link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
	<script type="text/javascript" src="<%=contextPath%>/js/jslib/mootools.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/framecommon/HashMap.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/my-grid-pager.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/dept_tree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/validate/validate.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/web/dtree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/dict.js"></script> -->
<title>车厂公司维护</title>
</head>
<body> <!-- onunload='javascript:destoryPrototype()' -->
 	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 组织管理 &gt; 车厂公司维护
		</div>
		<form id="fm">
			<input type="hidden" name="curPage" id="curPage" value="1" />
			<div class="form-panel">
				<h2><img src="/CQZTDMS/jmstyle/img/search-ico.png" class="panel-query-title">车厂公司维护</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="table_query_2Col_label_5Letter right" nowrap="nowrap">公司代码：</td>
							<td class="table_query_2Col_input"   nowrap="nowrap">
								<input class="middle_txt"  type="text" name="COMPANY_CODE" id="COMPANY_CODE" datatype="1,is_digit_letter,<%=Constant.Length_Check_Char_10 %>"/>
							</td>
							<td class="table_query_2Col_label_5Letter right" nowrap="nowrap" >公司名称：</td>
							<td class="table_query_2Col_input" nowrap="nowrap">
								<input class="middle_txt"  type="text" name="COMPANY_NAME" id="COMPANY_NAME" datatype="1,is_null,<%=Constant.Length_Check_Char_30 %>"/>	
							</td>
						</tr>
						<tr class="table_query_last" width="100%">
							<td class="center" colspan="4">
								<input id="queryBtn" class="u-button u-query" type="button" value="查 询" onclick="__extQuery__(1)" />&nbsp;
								<input class="u-button u-submit" type="button" value="新 增" onclick="addDlrInfo();" />		
							</td>
						</tr>
					</table>
				</div>
			</div>
		</form>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		<!-- <div id="_page" style="margin-top:15px;display:none;"></div>
		<div id="myGrid" ></div>
		<div id="myPage" class="pages"></div> -->
	</div>
<script type="text/javascript" >
    var url = "<%=contextPath%>/sysmng/orgmng/SgmOrgMng/queryOemCompanyInfo.json";
    var myPage;
	/* function __extQuery__(page){
		$("queryBtn").disabled = "disabled";
		showMask();
		sendAjax(url+(url.lastIndexOf("?") == -1?"?":"&")+"curPage="+page,callBack,'fm');
	} */
	//设置表格标题
	var title= null;

	var columns = [
					{header: "序号",width:'5%',  renderer:getIndex},
					{id:'action',header: "操作",width:'6%',dataIndex: 'COMPANY_ID',renderer:myLink},
					{header: "公司代码", width:'10%', dataIndex: 'COMPANY_CODE'},
					{header: "公司名称", width:'22%', dataIndex: 'COMPANY_NAME'},
					{header: "公司简称", width:'13%', dataIndex: 'COMPANY_SHORTNAME'},
					{header: "状态", width:'7%', dataIndex: 'STATUS', renderer:getItemValue}
				  ];  
	//设置超链接
	function myLink(value){
        return String.format(
               "<a href=\"<%=contextPath%>/sysmng/orgmng/SgmOrgMng/getOemCompanyInfo.do?companyId="+value+"\">[维护]</a>");
    }
	function addDlrInfo(){
		/* var form = $("#fm");
		form.action = '<%=request.getContextPath()%>/sysmng/orgmng/SgmOrgMng/addNewOemCompany.do';
		form.submit(); */
		document.location.href = '<%=request.getContextPath()%>/sysmng/orgmng/SgmOrgMng/addNewOemCompany.do';
	}

	$(function() {
		__extQuery__(1);
	});
</script>
</body>
</html>

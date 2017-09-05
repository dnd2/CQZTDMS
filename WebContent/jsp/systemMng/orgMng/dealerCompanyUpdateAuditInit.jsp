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
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!-- <script type="text/javascript" src="<%=FileConstant.regionJsUrl%>"></script> -->
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>公司维护</title>
</head>
<body onload="__extQuery__(1);"> <!-- onunload='javascript:destoryPrototype()' -->
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 组织管理 &gt; 经销商公司更名审核
		</div>
		<form id="fm">
			<div class="form-panel">
				<h2><img src="/CQZTDMS/jmstyle/img/search-ico.png" class="panel-query-title">经销商公司更名审核</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="table_query_2Col_label_5Letter right" nowrap="nowrap">公司代码：</td>
							<td class="table_query_2Col_input"   nowrap="nowrap">
								<input class="middle_txt"  type="text" name="COMPANY_CODE" id="COMPANY_CODE" datatype="1,is_digit_letter,<%=Constant.Length_Check_Char_10 %>"/>
							</td>
							<td class="table_query_2Col_label_5Letter right" nowrap="nowrap">公司名称：</td>
							<td class="table_query_2Col_input" nowrap="nowrap">
								<input class="middle_txt"  type="text" name="COMPANY_NAME" id="COMPANY_NAME" datatype="1,is_null,<%=Constant.Length_Check_Char_30 %>"/>	
							</td>
							<td class="table_query_2Col_label_5Letter right" nowrap="nowrap">状态：</td>
							<td class="table_query_2Col_input" nowrap="nowrap">
								<select name="STATUS" id="STATUS" class="short_sel u-select">
									<option value="1">待审核</option>
									<option value="2">审核通过</option>
									<option value="3">审核驳回</option>
								</select>
							</td>
						</tr>
						<tr class="table_query_last">
							<td class="center" colspan="6">
								<input id="queryBtn" class="u-button u-query" type="button" value="查 询" onclick="__extQuery__(1)" />&nbsp;
								<input class="u-button u-reset" type="reset" value="重置" />		
							</td>
						</tr>
					</table>
				</div>
			</div>
		</form>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />	
	</div>
	
<script type="text/javascript" >
    var url = "<%=contextPath%>/sysmng/orgmng/DlrInfoMng/queryCompanyLogInfo.json";
    var myPage;
	//设置表格标题
	var title= null;

	var columns = [
					{id:'action',header: "操作",width:'6%',dataIndex: 'COMPANY_ID',renderer:myLink},
					{header: "公司代码", width:'10%', dataIndex: 'COMPANY_CODE'},
					{header: "公司名称", width:'13%', dataIndex: 'OLD_COMPANY_NAME'},
					{header: "公司修改名称", width:'22%', dataIndex: 'NEW_COMPANY_NAME'},
// 					{header: "公司简称", width:'22%', dataIndex: 'OLD_COMPANY_SHORTNAME'},
// 					{id:'action',header: "操作",width:'6%',dataIndex: 'COMPANY_ID',renderer:myLink1},
					{header: "状态", dataIndex:'STATUS', renderer:getStatus}
				  ];  
	//设置超链接
	function myLink(value, meta, record) {
		if(record.data.STATUS == "1") {
			var str = "<INPUT onclick='auditProcess(\""+ value +"\",\"1\")' class='u-button' type='button' value='审核通过'>&nbsp;&nbsp;";
				str += "<INPUT onclick='auditProcess(\""+ value +"\",\"2\")' class='u-button' type='button' value='审核驳回'>&nbsp;&nbsp;";
				str += "<INPUT onclick='detail(\""+ value +"\")' class='long_btn' type='button' value='查看修改明细'>";
	        return String.format(str);
		} else {
			return "";
		}
    }
	function detail(value) {
		OpenHtmlWindow('<%=contextPath%>/sysmng/orgmng/DlrInfoMng/detailInit.do?COMPANY_ID='+value,800,550);
    }
	
	function getStatus(value) {
		if(value == "1") {
			return "待审核";
		} else if(value == "2") {
			return "审核通过";
		} else if(value == "3") {
			return "审核驳回";
		} else {
			return "未知状态";
		}
	}
	
	function auditProcess(COMPANY_ID, flag) {
		MyConfirm("审核之后不能修改，确定执行？", toAudit, [COMPANY_ID, flag]);
	}
	
	function toAudit(COMPANY_ID, flag){
		makeNomalFormCall('<%=contextPath%>/sysmng/orgmng/DlrInfoMng/companyLogAudit.json?COMPANY_ID='+COMPANY_ID+'&flag='+flag,showSubmitResult,'fm');
	}
	
	function showSubmitResult(json) {
		if(json.errCode == "0") {
			MyAlert(json.msg);
			__extQuery__("${curPage}");
		} else {
			MyAlert(json.msg);
		}
	}

	window.onload = function() {
		detail();
	}
</script>
</body>
</html>

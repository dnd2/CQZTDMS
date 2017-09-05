<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>系统可变参数表维护</title>
</head>
<body> <!-- onunload='javascript:destoryPrototype();' --> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  系统管理 &gt; 系统业务参数维护 &gt; 系统可变参数表维护</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<div class="form-panel">
			<h2><img src="/CQZTDMS/jmstyle/img/search-ico.png" class="panel-query-title">系统可变参数表维护</h2>
			<div class="form-body">
				<table class="table_query" border="0">
					<tr>
						<td width="20%" class="tblopt"><div align="right">类型名称：</div></td>
						<td width="20%" >
							<select class="u-select" name="typeName">
								<option value="">---请选择---</option>
								<c:forEach items="${typeNameList}" var="po" >
									<option value="${po.TYPE_NAME}">${po.TYPE_NAME}</option>
								</c:forEach>
							</select>
						</td>
						<td width="10%" class="tblopt"><div align="right">参数名称：</div></td>
						<td width="20%" >
							<input type="text" id="paraName" class="middle_txt" name="paraName" datatype="1,is_textarea,60" />
						</td>
						<td class="table_query_3Col_input" >
							<input type="button" class="u-button u-query" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> 
						</td>
					</tr>
				</table>
			</div>
		</div>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
<script type="text/javascript" >

	var myPage;
	
	var url = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/SysParameterManage/sysParameterList.json?COMMAND=1";
	
	var title = null;

	var columns = [
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'PARA_ID',renderer:myLink},
				{header: "类型名称", dataIndex: 'TYPE_NAME', align:'center'},
				{header: "参数名称", dataIndex: 'PARA_NAME', align:'center'},
				{header: "参数值", dataIndex: 'PARA_VALUE', align:'center'},
				{header: "备注", dataIndex: 'REMARK', align:'center'}
		      ];
		      
	function myLink(para_id){
        return String.format(
        		 "<a href=\"#\" onclick=toUpdateSysParm("+para_id+");>[修改]</a>");
    }

    function toUpdateSysParm(para_id){
        OpenHtmlWindow('<%=contextPath%>/sysbusinesparams/businesparamsmanage/SysParameterManage/sysParameterDetail.do?para_id='+para_id,700,480);
	}
	
	function reloadAction(json){
		turnQuery();
	}
	
	function turnQuery() {
		 __extQuery__(1);
		
	}
		
	$(function() {
		__extQuery__(1);
	});
 </script>    
</body>
</html>
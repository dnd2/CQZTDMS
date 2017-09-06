<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>运输方式与计价维护</title><script type="text/javascript" >
var myPage;
//查询路径           
var url = "<%=contextPath%>/parts/baseManager/logisticsManage/TransportAndValueAction/transportAndValueQuery.json";
var title = null;
var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "名称",dataIndex: 'TV_NAME',align:'center'},
				{header: "运输方式",dataIndex: 'TRANSPORT_CODE',align:'center',renderer:getItemValue},
				{header: "计价方式",dataIndex: 'VALUATION_CODE',align:'center',renderer:getItemValue},
				{header: "是否有效",dataIndex: 'STATUS',align:'center',renderer:getItemValue},
				{header: "操作",sortable: false, dataIndex: 'TV_ID', align:'center',renderer:myLink}
		      ];


//设置超链接
function myLink(value,meta,record){
 		return String.format("<a href=\"#\" onclick='updTransportInfo(\""+value+"\")'>[修改]</a>");
}

//修改页面
function updTransportInfo(value){
 window.location.href='<%=contextPath%>/parts/baseManager/logisticsManage/TransportAndValueAction/transportAndValueUpdInit.do?tvId='+value;  
}


//初始化    
function doInit(){
	__extQuery__(1);
}
//跳转新增页面
function addReservoir()
{
	fm.action = "<%=contextPath%>/parts/baseManager/logisticsManage/TransportAndValueAction/transportAndValueAddInit.do";
	fm.submit();
}

function doCusChange() {}
</script>
</head>

<body>
	<div class="wbox">
		<form name="fm" method="post" id="fm">
			<!-- 查询条件 begin -->
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 运输方式与计价维护
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query" id="subtab">
						<tr>
							<td class="right">运输方式：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("transportMode", <%=Constant.TRANSPORT_MODE%>, "", true, "u-select", "", "false", '');
								</script>
							</td>
							<td class="right">计价方式：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("valuationMode", <%=Constant.VALUATION_MODE%>, "", true, "u-select", "", "false", '');
								</script>
							</td>
						</tr>

						<tr>
							<td colspan="6" class="center">
								<input type="button" id="queryBtn" class="u-button" value="查询" onclick="__extQuery__(1);" />
								<input type="reset" class="u-button" id="resetButton" value="重置" />
								<input type="button" id="queryBtn" class="u-button" value="新增" onclick="addReservoir();" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />

		</form>
	</div>

</body>
</html>

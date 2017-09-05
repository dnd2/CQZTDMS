<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
    int yes = Constant.IF_TYPE_YES;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/common.js"></script>
<script type="text/javascript">
	function doInit(){
		loadcalendar();   //初始化时间控件
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
</script>

<title>顾问客流录入</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="loadcalendar();__extQuery__(1);">
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>潜客管理>经销商线索管理>顾问客流录入
		</div>
		<form id="fm" name="fm" method="post">
			<input type="hidden" name="curPage" id="curPage" value="1" /> <input
				type="hidden" id="dlrId" name="dlrId" value="" />

			<table class="table_query" width="95%" align="center">
				<c:if test="${returnValue==2}">
					<tr>
						<td colspan="6"></td>
					</tr>
				</c:if>
				<tr>
					<td align="right" width="10%">客户姓名：</td>
					<td width="20%"><input id="customer_name" name="customer_name" type="text"
						class="middle_txt" datatype="1,is_textarea,30" size="13"
						maxlength="60" /></td>
					<td align="right" width="7%">联系电话：</td>
					<td width="20%"><input id="telephone" name="telephone"
						type="text" class="middle_txt" datatype="1,is_textarea,30"
						size="20" maxlength="60" /></td>
					<td width="8%" align="right">来店时间：</td>
					<td width="25%">
						<div align="left">
							<input name="startDate" id="startDate" value="" type="text"
								class="short_txt" datatype="1,is_date,10"
								group="startDate,endDate"  style="width:80px;"/>
								<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value="&nbsp;" />
							离店时间： <input name="endDate" id="endDate" value=""
								type="text" class="short_txt" datatype="1,is_date,10"
								group="startDate,endDate"   style="width:80px;"/>
								<input class="time_ico" type="button"  onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
						</div></td>
				</tr>

				<tr>
					<td align="right" width="10%">分派状态：</td>
					<td><input type="hidden" id="allot_status" name="allot_status" value=""/>
		      		<div id="ddtopmenubar29" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:130px;" rel="ddsubmenu29" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6029', loadAllotStatus);" deftitle="--请选择--">
								--请选择--</a>
								<ul id="ddsubmenu29" class="ddsubmenustyle"></ul>
							</li>
						</ul></td>
					<td align="right" width="7%">客流状态：</td>
					<td><input type="hidden" id="customer_status" name="customer_status" value=""/>
		      		<div id="ddtopmenubar28" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:130px;" rel="ddsubmenu28" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6016', loadCustomerStatus);" deftitle="--请选择--">
								--请选择--</a>
								<ul id="ddsubmenu28" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</td>
					<td></td>
				</tr>
				<tr>
					<td colspan="3" align="right">
						<input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查询" />
						<input name="insertBtn" type="button" class="normal_btn" onclick="window.location.href='<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/adviserEnterInsert.do'" value="新增" />
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>

	</div>
	<script type="text/javascript"> 
	
	var myPage;
	var url = "<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/adviserEnterFindQuery.json";
	var title = null;
	var columns = [
				{header: "序号", renderer:getIndex, align:'center'},
				{header: "线索编码", dataIndex: 'LEADS_CODE', align:'center'},
				{header: "线索来源", dataIndex: 'LEADS_ORIGIN', align:'center'},
				{header: "客户描述", dataIndex: 'CUSTOMER_DESCRIBE', align:'center'},
				{header: "客户姓名", dataIndex: 'CUSTOMER_NAME', align:'center'},
				{header: "联系电话", dataIndex: 'TELEPHONE', align:'center'},
				{header: "意向车型", dataIndex: 'SERIES_NAME', align:'center'},
				{header: "分派顾问", dataIndex: 'ADVISER', align:'center'}
		      ];
	function myLink(value,meta,record){
		if(record.data.LEADS_STATUS_CODE == '60161004') {
			return String.format("<a href=\"#\" id=\"LEADS_ALLOT_ID\" name=\"LEADS_ALLOT_ID\" onclick='checkDetailUrl(\""+record.data.LEADS_CODE+"\",\""+record.data.LEADS_ALLOT_ID+"\",\""+record.data.ADVISERID+"\")'>[离店]</a>");
		} else {
			return '';
		}
	}   
	function checkDetailUrl(leadsCode,leadsAllotId,adviser){
		window.location.href="<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/dcrcLeaveDate.do?leadsCode="+leadsCode+"&leadsAllotId="+leadsAllotId+"&adviser="+adviser;
	} 
	</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar29", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar28", "topbar")</script>
</body>
</html>
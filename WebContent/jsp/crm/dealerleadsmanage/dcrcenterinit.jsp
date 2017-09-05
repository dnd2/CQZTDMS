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

<title>DCRC客流录入</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="loadcalendar();__extQuery__(1);">
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>潜客管理>经销商线索管理>DCRC客流录入
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
								group="startDate,endDate"  style="width: 80px;"/>
								<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value="&nbsp;" />
							   到： <input name="endDate" id="endDate" value=""
								type="text" class="short_txt" datatype="1,is_date,10"
								group="startDate,endDate"  style="width: 80px;"/>
								<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
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
					<td align="right" width="7%">分派顾问：</td>
					<td> 
				      <select id="adviser" name="adviser">
				      			<option value="">--请选择--</option>
				      		<c:forEach var="item" items="${adviserList }" varStatus="status">
				      			<option id="${item.USER_ID }" value="${item.USER_ID }">${item.NAME }</option>
				      		</c:forEach>
				      	</select>
				      </td>
				</tr>
				<tr>
					<td align="right" width="10%">分组：</td>
					<td> 
				      <select id="groupId" name="groupId">
				      	<option value="">--请选择--</option>
				      		<c:forEach var="item" items="${groupList }" varStatus="status">
				      			<option id="${item.GROUP_ID }" value="${item.GROUP_ID }">${item.GROUP_NAME }</option>
				      		</c:forEach>
				      </select>
				    </td>
				
					<td align="right" width="7%">集客方式：</td>
					<td><input type="hidden" id="collect_fashion" name="collect_fashion" value=""/>
		      		<div id="ddtopmenubar27" class="mattblackmenu">
						<ul> 
							<li>
								<a  style="width:170px;" rel="ddsubmenu27" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6002', loadCollectFashion);" deftitle="--请选择--">
								--请选择--</a>
								<ul id="ddsubmenu27" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</td>
				</tr>
				<tr>
					<td colspan="6" align="center"><input name="queryBtn"
						type="button" class="normal_btn" onclick="__extQuery__(1);"
						value="查询" />
						<input name="insertBtn"
						type="button" class="normal_btn" onclick="window.location.href='<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/dcrcEnterInsert.do'"
						value="新增" />
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>

	</div>
	<script type="text/javascript"> 
	
	var myPage;
	var url = "<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/dcrcEnterFindQuery.json";
	var title = null;
	var columns = [
				{header: "序号", renderer:getIndex, align:'center'},
				{header: "线索来源", dataIndex: 'LEADS_ORIGIN', align:'center'},
				{header: "来店(电)时间", dataIndex: 'COME_DATE', align:'center'},
				{header: "离店(电)时间", dataIndex: 'LEAVE_DATE', align:'center'},
				{header: "客户描述", dataIndex: 'CUSTOMER_DESCRIBE', align:'center'},
				{header: "客户姓名", dataIndex: 'CUSTOMER_NAME', align:'center'},
				{header: "联系电话", dataIndex: 'TELEPHONE', align:'center'},
				{header: "意向车型", dataIndex: 'SERIES_NAME', align:'center'},
				{header: "分派顾问", dataIndex: 'ADVISER', align:'center'},
				{header: "集客方式", dataIndex: 'JC_WAY', align:'center'},
				{header: "确认", sortable: false,dataIndex: 'LEADS_ALLOT_ID',renderer:myLink}
		      ];
	function myLink(value,meta,record){
		if(record.data.LEADS_STATUS_CODE == '60161004'&&record.data.IF_CONFIRM=='60321001') {
			if(record.data.LEADS_ORIGIN_CODE == '60151011') {
				return String.format("<a href=\"#\" id=\"LEADS_ALLOT_ID\" name=\"LEADS_ALLOT_ID\" onclick='checkDetailUrl(\""+record.data.LEADS_CODE+"\",\""+record.data.LEADS_ALLOT_ID+"\",\""+record.data.ADVISERID+"\")'>[离店]</a><a href=\"#\" id=\"LEADS_ALLOT_ID\" name=\"LEADS_ALLOT_ID\" onclick='checkDetailUrl2(\""+record.data.LEADS_CODE+"\",\""+record.data.LEADS_ALLOT_ID+"\",\""+record.data.ADVISERID+"\")'>[修改]</a>");
			} else {
				return String.format("<a href=\"#\" id=\"LEADS_ALLOT_ID\" name=\"LEADS_ALLOT_ID\" onclick='checkDetailUrl(\""+record.data.LEADS_CODE+"\",\""+record.data.LEADS_ALLOT_ID+"\",\""+record.data.ADVISERID+"\")'>[挂机]</a><a href=\"#\" id=\"LEADS_ALLOT_ID\" name=\"LEADS_ALLOT_ID\" onclick='checkDetailUrl2(\""+record.data.LEADS_CODE+"\",\""+record.data.LEADS_ALLOT_ID+"\",\""+record.data.ADVISERID+"\")'>[修改]</a>");
			}
			
		} else {
			return '';
		}
	}   
	function checkDetailUrl(leadsCode,leadsAllotId,adviser){
		// MyAlert("leadsCode=="+leadsCode+"   leadsAllotId=="+leadsAllotId+"  adviser==="+adviser);
		document.getElementById("LEADS_ALLOT_ID").disabled = true;
		window.location.href="<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/dcrcLeaveDate.do?leadsCode="+leadsCode+"&leadsAllotId="+leadsAllotId+"&adviserId="+adviser;
	} 
	function checkDetailUrl2(leadsCode,leadsAllotId,adviser){
		window.location.href="<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/dcrcEnterInsert.do?leadsCode="+leadsCode+"&leadsAllotId="+leadsAllotId+"&adviserId="+adviser+"&updateFlag=yes";
	} 
	</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar29", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar28", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar27", "topbar")</script>
</body>
</html>
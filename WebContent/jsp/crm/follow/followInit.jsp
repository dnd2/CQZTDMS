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

<title>跟进查询</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="__extQuery__(1);loadcalendar();">
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>潜客管理>经销商线索管理>跟进查询
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
					<td align="right" >客户姓名：</td>
					<td><input id="customer_name" name="customer_name" type="text"
						class="middle_txt" datatype="1,is_textarea,30" size="20"
						maxlength="60" /></td>
					<td align="right" >联系电话：</td>
					<td ><input id="telephone" name="telephone"
						type="text" class="middle_txt" datatype="1,is_textarea,30"
						size="20" maxlength="60" /></td>
					<td  align="right">跟进时间从：</td>
					<td >
						<div align="left">
							<input name="startDate" id="startDate" value="" type="text"
								class="short_txt" datatype="1,is_date,10"
								group="startDate,endDate" style="width: 80px;"/>
								<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value="&nbsp;" />
							 到 <input name="endDate" id="endDate" value=""
								type="text" class="short_txt" datatype="1,is_date,10"
								group="startDate,endDate" style="width: 80px;"/>
								<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
						</div></td>
					<td align="right" >是否完成：</td>
					<td>
						<select name="finishStatus" style="width:130px;">
		      			<option value="">--请选择--</option>
		      			<option value="60171002">是</option>
		      			<option value="60171001">否</option>
      					</select></td>
				</tr>
				<tr>
					<td  align="right">跟进方式：</td>
					<td>
					<input type="hidden" id="follow_type" name="follow_type" value=""/>
		      		<div id="ddtopmenubar29" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:130px;" rel="ddsubmenu29" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6046', loadFollowType);" deftitle="--请选择--">
								--请选择--</a>
								<ul id="ddsubmenu29" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</td>
			 <td  align="right"  >客户等级：</td>
	      <td align="left" nowrap >
	         <input type="hidden" id="ctmRank" name="ctmRank" value="" />
		      		<div id="ddtopmenubar28" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:130px;" rel="ddsubmenu28" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6010', loadCtmRank);" deftitle="--请选择--">
								--请选择--</a>
								<ul id="ddsubmenu28" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>	
	      </td>
					<c:if test="${adviserLogon=='yes' }">
						<td align="right" ></td>
						<td></td>
					</c:if>
					<c:if test="${adviserLogon=='no' }">
					<td align="right" >顾问：</td>
					<td>
						<select id="adviserId" name="adviserId" style="width:130px;">
				      			<option value="">--请选择--</option>
				      		<c:forEach var="item" items="${adviserList }" varStatus="status">
				      			<option id="${item.USER_ID }" value="${item.USER_ID }">${item.NAME }</option>
				      		</c:forEach>
				      	</select>
					</td>
					</c:if>
<!--				</tr>-->
<!--				<tr>-->
					<c:if test="${managerLogon=='yes' }">
						<td align="right" >分组：</td>
						<td  align="left">
							<select id="groupId" name="groupId" style="width:130px;">
				      			<option value="">--请选择--</option>
				      		<c:forEach var="item" items="${groupList }" varStatus="status">
				      			<option id="${item.GROUP_ID }" value="${item.GROUP_ID }">${item.GROUP_NAME }</option>
				      		</c:forEach>
				      	</select>
						</td>
					</c:if>
<!--					<c:if test="${managerLogon=='no' }">-->
<!--						<td align="right" width="13%"></td>-->
<!--						<td width="25%"></td>-->
<!--					</c:if>-->
				</tr>
				<tr>
					<td colspan="8" align="center"><input name="queryBtn"
						type="button" class="normal_btn" onclick="__extQuery__(1);"
						value="查询" />
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>

	</div>
	<script type="text/javascript"> 
	
	var myPage;
	var url = "<%=contextPath%>/crm/follow/FollowManage/followFindQuery.json";
	var title = null;
	var columns = [
				{header: "客户姓名", dataIndex: 'CUSTOMER_NAME', align:'center'},
				{header: "手机", dataIndex: 'TELEPHONE', align:'center'},
				{header: "客户等级", dataIndex: 'CTM_RANK', align:'center',renderer:getItemValue},
				{header: "跟进日期", dataIndex: 'FOLLOW_DATE', align:'center'},
				{header: "跟进方式", dataIndex: 'FOLLOW_TYPE', align:'center'},
				{header: "原意向等级", dataIndex: 'OLD_LEVEL', align:'center'},
				{header: "新意向等级", dataIndex: 'NEW_LEVEL', align:'center'},
				{header: "销售顾问", dataIndex: 'ADVISER', align:'center'},
				{header: "操作",sortable: false,dataIndex: 'FOLLOW_ID',renderer:myLink}
		      ];
	
	function myLink(value,meta,record){
		return String.format("<a href=\"#\" id=\"FOLLOW_ID\" name=\"FOLLOW_ID\" onclick='checkDetailUrl(\""+record.data.FOLLOW_ID+"\")'>[详情]</a>");
	}   
	function checkDetailUrl(followId){
		window.location.href="<%=contextPath%>/crm/follow/FollowManage/followDetailInit.do?followId="+followId;
	}
	</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar29", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar28", "topbar")</script>
</body>
</html>
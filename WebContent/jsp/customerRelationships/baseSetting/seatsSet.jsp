<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>坐席管理</title>
</head>
<body onload="__extQuery__(1)">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 基础设定 &gt;坐席管理</div>
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />坐席管理</th>
			
			<tr>
				<td align="right" nowrap="true">用户名：</td>
				<td align="left" nowrap="true">
					<input type="text" id="name" name="name"/>
				</td>
				<td align="right" nowrap="true">工号：</td>
				<td align="left" nowrap="true">
					<input type="text" id="account" name="account"/>
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">是否坐席：</td>
				<td align="left" nowrap="true">
				<script type="text/javascript">
		    			genSelBoxExp("isSeats",<%=Constant.IF_TYPE%>,"",true,"short_sel","","false",'');
		    	</script>
				</td>
				<td align="right" nowrap="true">坐席级别：</td>
				<td align="left" nowrap="true">
				<script type="text/javascript">
		    			genSelBoxExp("level",<%=Constant.SEATS_LEVEL%>,"",true,"short_sel","","false",'');
		    	</script>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">是否管理员：</td>
				<td align="left" nowrap="true">
				<script type="text/javascript">
		    			genSelBoxExp("isAdmin",<%=Constant.se_is_manamger%>,"",true,"short_sel","","false",'');
		    	</script>
				</td>
				<td align="right" nowrap="true">坐席组：</td>
				<td align="left" nowrap="true">
				<select id="stId" name="stId" class="short_sel">
						<option value=''>-请选择-</option>
						<c:forEach var="st" items="${stTeams}">
							<option value="${st.STID}" title="${st.STNAME}">${st.STNAME}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
	
			<tr>
				<td colspan="8" align="center">
					<input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="__extQuery__(1);" />
        		</td>
			</tr>
		</table>
		
	 <!-- 查询条件 end -->
	 <!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	 <!--分页 end -->
	</form>
<script type="text/javascript">

	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/baseSetting/seatsSet/querySeatsSet.json";
				
	var title = null;

	var columns = [
				{header: "序号", align: 'center', renderer:getIndex},
				{header: "用户名",dataIndex: 'TUSERNAME',align:'center'},
				{header: "帐号", dataIndex: 'TUSERACCOUNT', align:'center'},
				{header: "工号",dataIndex: 'SESEATSNUM',align:'center'},
				{header: "坐席级别", dataIndex: 'SELEVEL', align:'center',renderer:getItemValue},
				{header: "所属坐席组", dataIndex: 'STNAME', align:'center'},
				{header: "是否座席",dataIndex: 'SEISSEATS',align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'TUSERID',renderer:myLink}
		      ];

	function myLink(value,meta,record){
		if(record.data.STATUS == <%=Constant.STATUS_ENABLE%>){
			return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='viewDetail(\""+ value +"\")' value='修改'/><input name='detailBtn' type='button' class='normal_btn' onclick='destroy(\""+ record.data.SEID +"\")' value='注销'/>");
		}else{
			return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='viewDetail(\""+ value +"\")' value='修改'/>");
		}
	}
	function viewDetail(value){		
		window.location.href='<%=contextPath%>/customerRelationships/baseSetting/seatsSet/updateSeatsSet.do?id='+value ;
	}
	function destroy(seid){
		makeNomalFormCall('<%=contextPath%>/customerRelationships/baseSetting/seatsSet/destroySeatsSet.json?seid='+seid,destroyBack,'fm','');
	}
	
	function destroyBack(json) {
		if(json.success != null && json.success=='true'){
			MyAlertForFun("注销成功",sendPage);
		}else{
			MyAlert("注销失败！请联系管理员");
		}
	}
	//页面跳转：
	function sendPage(){
		window.location.href = "<%=contextPath%>/customerRelationships/baseSetting/seatsSet/seatsSetInit.do";
	}
</script>
</body>
</html>
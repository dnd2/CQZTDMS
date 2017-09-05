<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	Long userId = (Long)request.getAttribute("logonUser");
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>首页新闻查询</title>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  // 初始化时间控件
	}
	function orgChoose(obj) {
		var orgs = document.getElementsByName('org');
		if(obj.checked == true) {
			for(var i=0;i<orgs.length;i++) {
				if(orgs[i].lang == obj.value) {
					orgs[i].checked = true;
				}
			}
		}
		else
		{
			for(var i=0;i<orgs.length;i++) {
				if(orgs[i].lang == obj.value) {
					orgs[i].checked = false;
				}
			}
		}
		//__extQuery__(1);
	}
	
	function chooseConfim() {
		var userIds = document.getElementsByName('userIds');
		var dataArr = new Array();
		for(var i=0;i<userIds.length;i++) {
			if(userIds[i].checked == true) {
				var tempArr = userIds[i].value.split("_");
				dataArr.push(tempArr);
			}
		}
		
		parentContainer.initOemUserList(dataArr);
		_hide();
	}
</script>
</head>
<body>
<form name='fm' id='fm'>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 个人信息管理&gt;个人信息管理&gt;首页新闻</div>
  <input type="hidden" name="dealerType" id="dealerType" value="${dealerType }" />
   <table  class="table_query">
   		<tr>
   			<td width="80"><input type="checkbox" checked onclick="selectAll(this,'poseIds')"/>职位：</td>
   			<td>
   				<table>
	   				<tr>
		   				<c:forEach items="${poseList }" var="list" varStatus="status">
				       		<c:choose>
				 				<c:when test="${status.last == true }">
				 					<td width='24'>
				 						<input name="poseIds" type="checkbox" checked="checked" value="${list.POSE_ID }"/>
				 					</td>
				 					<td colspan='${6-status.index%6 }'>
				 						${list.POSE_NAME }
				 					</td>
				 					</tr>
				 				</c:when>
				 				<c:when test="${(status.index+1) % 6 == 0 && (status.index+1) > 5}">
				 					<td width='24'><input name="poseIds" type="checkbox" checked="checked" lang="${list.POSE_ID }" value="${list.POSE_ID }"/></td>
				 					<td width='120'>${list.POSE_NAME }</td>
				 					</tr><tr>
				 				</c:when>
				 				<c:otherwise>
				 					<td width='24'><input name="poseIds" type="checkbox" checked="checked" lang="${list.POSE_ID }" value="${list.POSE_ID }"/></td>
				 					<td width='120'>${list.POSE_NAME }</td>
				 				</c:otherwise>
				 			</c:choose>
				       </c:forEach>
			       </tr>
		       </table>
   			</td>
   		</tr>
   		<tr>
   			<td colspan='2' align='center'> <!-- 用户工号(姓名)：<input type='text' name='userName'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  -->
   				<input type="button" id="queryBtn" value="查 询" class="cssbutton" onclick="__extQuery__(1)"/>
   				&nbsp;&nbsp;
   				<input type="button" id="queryBtn" value="确 认" class="cssbutton" onclick="chooseConfim()"/>
   			</td>
   		</tr>
  </table>
</form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/HomePageNews/oemUserChooseQuery.json";
	var title = null;
	
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{
			header: "<input type='checkbox' onclick='selectAll(this,\"userIds\")'/>",dataIndex: 'USER_ID',align:'center',
			renderer:function(value,mata,record){
				return "<input type='checkbox' name='userIds' id='userIds' value='"+record.data.USER_ID+"_"+record.data.NAME+"_"+record.data.POSE_CODE+"_"+record.data.POSE_NAME+"_"+getItemValue(record.data.POSE_TYPE)+"'/>";
			}
		},
		{header: "用户名",sortable: false,dataIndex: 'NAME',align:'center',style:'text-align:left'},
		{header: "职位类型",sortable: false,dataIndex: 'POSE_TYPE',align:'center',renderer:getItemValue},
		{header: "职位名称",sortable: false,dataIndex: 'POSE_NAME',style:'text-align:left'}
      ];
</script>
</body>
</html>	
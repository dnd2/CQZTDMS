<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/invite/inviteCheck.js"></script>
<title>邀约查询</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="loadcalendar();__extQuery__(1);"> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
			&nbsp;当前位置：  整车销售 &gt;  潜客管理  &gt;邀约管理 &gt;邀约计划审核
	</div>
	<form id="fm" name="fm" method="post">
		<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
	<table class="table_query" border="0">
		<tr>
	     
	       <td  class="table_query_3Col_label_6Letter" >
	      	客户名称：
	      </td>
	      <td >
	      	<input type="text" name="name" id="name"/>
	      </td>
	      <td align="left" class="table_query_3Col_label_6Letter" nowrap >
	      	计划邀约时间：
	      </td>
	      <td align="left" nowrap >
	      	 起：<input  type="text" id="planInviteDate"  name="planInviteDate" class="short_txt" readonly    />
               <input class="time_ico" type="button" onClick="showcalendar(event, 'planInviteDate', false);" />
              止：<input  type="text" id="planInviteDateEnd"  name="planInviteDateEnd" class="short_txt" readonly    />
               <input class="time_ico" type="button" onClick="showcalendar(event, 'planInviteDateEnd', false);" />
	      </td>
	       <td align="right" >
	      	顾问:
	      </td>
	      <td>
					<select name="adviser" id="adviser" style="width:130px;">
		         	 	<option value="" >--请选择--</option>
		         	 	<c:forEach items="${userList}" var="po">
		         	 		<option value="${po.USER_ID}" >${po.NAME}</option>
		         	 	</c:forEach>
	         	 	</select>
	      </td>
		</tr>
		<tr>
	      <td align="left" class="table_query_3Col_label_6Letter"  >
	      		客户手机：
	      </td>
	      <td align="left" nowrap >
	      	<input type="text" name="telephone" id="telephone"/>
	      </td>
	      <td  class="table_query_3Col_label_6Letter">
	      	计划见面时间：
	      </td>
	      <td >
	     	起：<input  type="text" id="planMeetDate" name="planMeetDate"  class="short_txt" readonly   />
              <input class="time_ico" type="button" onClick="showcalendar(event, 'planMeetDate', false);" />
                                      止：<input  type="text" id="planMeetDateEnd" name="planMeetDateEnd"  class="short_txt" readonly   />
              <input class="time_ico" type="button" onClick="showcalendar(event, 'planMeetDateEnd', false);" />
      	</td>
     	    <c:if test="${managerLogon=='yes' }">
				<td align="right">分组：</td>
         		<td  align="left"  >
						 <select id="groupId" name="groupId">
			      			<option value="">--请选择--</option>
			      		<c:forEach var="item" items="${groupList }" varStatus="status">
			      			<option id="${item.GROUP_ID }" value="${item.GROUP_ID }">${item.GROUP_NAME }</option>
			      		</c:forEach>
		      	</select>
				</td>
        	 </c:if>
	      <td>
	      &nbsp;
	      </td>
		</tr>
	    <tr>
			<td  colspan="5" align="center" >
				<input type="button" class="normal_btn" onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" />
			</td>
	        <td align="right"  >
	                                    页面大小：<input name="pageSize" id="pageSize" type="text" class="mini_txt" value="10" datatype="0,isDigit,3"/>
	        </td>
		</tr>
	</table>
<!--分页部分  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />	
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div>   
</body>
</html>
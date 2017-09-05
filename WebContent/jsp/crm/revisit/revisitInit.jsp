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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/revisit/revisit.js"></script>

<title>回访查询</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="loadcalendar();doInit();"> 
<div class="wbox">
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt;  潜客管理  &gt;回访管理 &gt; 回访查询
	</div>
	<form id="fm" name="fm" method="post">
		<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
		      	<td align="left" class="table_query_2Col_label_4Letter" nowrap="nowrap" >
		      		客户名称：
		      	</td>
		      	<td align="left" >
		      		<input type="text" name="ctmName" id="ctmName"  style="width:130px;" class="long_txt"/>
		      	</td>
		    	<td align="left" class="table_query_2Col_label_4Letter" nowrap="nowrap" >
		    		手机：
		    	</td>
		     	<td align="left" >
		     		<input type="text" name="telephone" id="telephone" style="width:130px;" class="long_txt"/>
		     	</td>
	         	 <td align="left" class="table_query_2Col_label_5Letter" nowrap="nowrap"  >
	         	 	回访时间起：
	         	 </td>
		     	<td align="left" >
		     	       <input name="startTime" type="text" id="startTime"   class="short_txt" readonly    />
	          		   <input class="time_ico" type="button" onClick="showcalendar(event, 'startTime', false);" />
	          		止： <input name="endTime" type="text" id="endTime"  class="short_txt" readonly    />
	          		   <input class="time_ico" type="button" onClick="showcalendar(event, 'endTime', false);" />
	          </td>
	          <c:if test="${managerLogon=='yes' }">
	         		<td  align="left" width="17%" >
							分组：<select id="groupId" name="groupId">
				      			<option value="">--请选择--</option>
				      		<c:forEach var="item" items="${groupList }" varStatus="status">
				      			<option id="${item.GROUP_ID }" value="${item.GROUP_ID }">${item.GROUP_NAME }</option>
				      		</c:forEach>
				      	</select>
						</td>
	          </c:if>
			</tr>
			<tr>
				<td align="left" class="table_query_2Col_label_4Letter" nowrap="nowrap" >
					经销商：
				</td>
				<td align="left">
				  	<input name="dealerCode" type="text" id="dealerCode" style="width:130px;" class="middle_txt" value="${td.dealerShortName}" size="20" />
		                  <c:if test="${dutyType==10431001}">
		                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer('dealerCode','','true', '${orgId}')" value="..." />
		                  	 <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer6('dealerCode','','true', '${orgId}')" value="..." />
	                   		 <input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn" />
		                  </c:if>
		                  <c:if test="${dutyType==10431002}">
		                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer3('dealerCode','','true', '${orgId}')" value="..." />
		                 	 <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer6('dealerCode','','true', '${orgId}')" value="..." />
	                   	 <input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn" />
		                  </c:if>
		                  <c:if test="${dutyType==10431003}">
		                    <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer3('dealerCode','','true', '${orgId}')" value="..." />
		                  	 <input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="showOrgDealer6('dealerCode','','true', '${orgId}')" value="..." />
	                  		  <input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn" />
		                  </c:if>
	                   
				</td>
				<c:if test="${poseRank!=60281004}">
				<td class="table_query_2Col_label_4Letter" nowrap="nowrap" >顾问：</td>
				<td>
					<select name="adviser" id="adviser" style="width:130px;">
		         	 	<option value="" >--请选择--</option>
		         	 	<c:forEach items="${userList}" var="po">
		         	 		<option value="${po.USER_ID}" >${po.NAME}</option>
		         	 	</c:forEach>
	         	 	</select>
				</td>
				</c:if>
				<td class="table_query_2Col_label_5Letter" nowrap="nowrap" >回访类型：</td>
				<td>
				<input type="hidden" id="revisitType" name="revisitType" value="" />
		      		<div id="ddtopmenubar" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:130px;" rel="ddsubmenu40" href="#" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6043', loadType);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu40" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
				</td>
				<c:if test="${managerLogon=='yes' }">
				 <td>&nbsp;</td>
				</c:if>
			</tr>
		    <tr>
		    	<td> &nbsp;</td>
		    	 <c:if test="${managerLogon=='yes' }">
		          <td>&nbsp;</td>
		          </c:if>
				<td  colspan="4" align="center" >
						<input type="button" class="normal_btn" onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" />
				</td>
		        <td align="right" >
		             页面大小：<input name="pageSize" id="pageSize" type="text" class="mini_txt" value="10" datatype="0,isDigit,3"/>
		        </td>
			</tr>
		</table>
<!--分页部分  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />	
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div>   
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar", "topbar")</script>
</body>
</html>
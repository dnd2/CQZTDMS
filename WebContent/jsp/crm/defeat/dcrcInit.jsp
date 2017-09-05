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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/defeat/dcrc.js"></script>
<style type="text/css">
.long_select{
	width:135px;
}
</style>
<title>DCRC查询</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="loadcalendar();__extQuery__(1);"> 
<div class="wbox">
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt;  潜客管理  &gt;战败/失效管理 &gt;DCRC抽查
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
		      		<input type="text" name="ctmName" id="ctmName"  />
		      	</td>
		    	<td align="right" width="13%" nowrap="nowrap" >
		    		手机：
		    	</td>
		     	<td align="left" >
		     		<input type="text" name="telephone" id="telephone" />
		     	</td>
	         	 <td align="left" class="table_query_2Col_label_5Letter" nowrap="nowrap"  >
	         	 	顾问：
	         	 </td>
	         	 <td>
         	 		<select name="adviser" id="adviser">
	         	 	    <option value="" >--请选择--</option>
		         	 	<c:forEach items="${userList}" var="po">
		         	 		<option value="${po.USER_ID}" >${po.NAME}</option>
		         	 	</c:forEach>
	         	 	</select>
		        </td>
		     	<td align="right" >战败时间：</td>
		     	<td>
			     	起：<input name="defeatDate" type="text" id="defeatDate"  class="short_txt" readonly    />
		              <input class="time_ico" type="button" onClick="showcalendar(event, 'defeatDate', false);" />
		                                    止：<input name="defeatDateEnd" type="text" id="defeatDateEnd"  class="short_txt" readonly    />
		              <input class="time_ico" type="button" onClick="showcalendar(event, 'defeatDateEnd', false);" />
	          </td>
	          
			</tr>
			<tr>
				<td class="table_query_2Col_label_4Letter" nowrap="nowrap" >意向等级：</td>
				<td>
				<input type="hidden" id="ctmRank" name="ctmRank" value="" />
		      		<div id="ddtopmenubar" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:130px;" rel="ddsubmenu40" href="#" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6010', loadCtmRank);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu40" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
				</td>
				<td width="13%" align="right" >销售流程进度：</td>
				<td>
		            <input type="hidden" id="salesProgress" name="salesProgress" value="" />
		            <div id="ddtopmenubar1" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:130px;" rel="ddsubmenu1" href="#" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6037', loadProgress);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu1" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
				</td>
				<td  align="right">类型：</td>
				<td>
					 <input type="hidden" id="opType" name="opType" value="" />
		            <div id="ddtopmenubar2" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:110px;" rel="ddsubmenu2" href="#" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6039', loadType);" deftitle="--请选择--">--请选择--</a>
								<ul id="ddsubmenu2" class="ddsubmenustyle"></ul>
							</li>
						</ul>
					</div>
				</td>
	            <c:if test="${managerLogon=='yes' }">
						<td align="right" width="12%">分组：</td>
						<td width="30%">
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
		    	<td> &nbsp;</td>
				<td  colspan="6" align="center" >
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
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar1", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar2", "topbar")</script>
</body>
</html>
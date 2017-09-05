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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/data/dataManage.js"></script>
<title>经销商用户维护</title>
</head>
<body  onload="setTableBtn();"> 
<div class="wbox" style="height:800px;">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt;  潜客管理  &gt;字典管理 &gt;字典维护 </div>
	
	<form id="fm" name="fm" method="post">
		<input type="hidden" value="<%=contextPath%>" name="curPaths"/>
		<input type="hidden" value="${tc.codeLevel}" name="codeLevel"/>
	<table class="table_query" border="0">
	<tr>
		
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">父级字典代码：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
		${tc.type}
		</td>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap" >父级字典名称：</td>
		<td class="table_query_2Col_input" nowrap="nowrap" >
		${tc.typeName}
		</td>
		<td class="table_query_2Col_label_11Letter" nowrap="nowrap">状态：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
	             <input type="hidden" id="status" name="status" value="${tc.status}" />
	      		<div id="ddtopmenubar3" class="mattblackmenu">
					<ul> 
						<li>
							<a style="width:100px;" rel="ddsubmenu3" href="#" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1001', loadStatus);" deftitle="--请选择--">
							<c:if test="${status==null}">--请选择--</c:if><c:if test="${status!=null}">${status}</c:if>
							</a>
							<ul id="ddsubmenu3" class="ddsubmenustyle"></ul>
						</li>
					</ul>
				</div> 
        </td>
        
	</tr>
	<tr id="zz" style="display: inline;">
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">本级字典代码：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
		${tc.codeId}
		</td>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap" >本级字典名称：</td>
		<td class="table_query_2Col_input" nowrap="nowrap" >
		<input type="text" id="codeName" name="codeName" value="${tc.codeDesc}" onchange="onChangeData();"/>
		<input type="hidden" id="codeId" name="codeId" value="${tc.codeId}"/>
		</td>
		<td class="table_query_2Col_label_11Letter" nowrap="nowrap">是否经销商可以维护下级：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
	             <input type="hidden" id="ifDealer" name="ifDealer" value="${tc.ifDealer}"  />
	      		<div id="ddtopmenubar2" class="mattblackmenu">
					<ul> 
						<li>
							<a style="width:100px;" rel="ddsubmenu2" href="#" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1004', loadIfDealer);" deftitle="--请选择--">
							<c:if test="${ifDealer==null}">--请选择--</c:if><c:if test="${ifDealer!=null}">${ifDealer}</c:if>
							</a>
							<ul id="ddsubmenu2" class="ddsubmenustyle"></ul>
						</li>
					</ul>
				</div> 
        </td>
	</tr>
	<tr >
		<td class="table_query_2Col_label_11Letter" nowrap="nowrap">是否可以维护：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
	            <input type="hidden" id="ifVisible" name="ifVisible" value="${tc.ifVisible}"  />
	      		<div id="ddtopmenubar1" class="mattblackmenu">
					<ul> 
						<li>
							<a style="width:100px;" rel="ddsubmenu1" href="#" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1004', loadIfVisible);" deftitle="--请选择--">
							<c:if test="${ifVisible==null}">--请选择--</c:if><c:if test="${ifVisible!=null}">${ifVisible}</c:if>
							</a>
							<ul id="ddsubmenu1" class="ddsubmenustyle"></ul>
						</li>
					</ul>
				</div> 
        </td>
        	<td class="table_query_2Col_label_6Letter" nowrap="nowrap">级数：</td>
		<td class="table_query_2Col_input" nowrap="nowrap">
		${tc.codeLevel}
		</td>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap" >&nbsp;</td>
		<td class="table_query_2Col_input" nowrap="nowrap" >
			&nbsp;
		</td>
	</tr>
	<tr>
		<td colspan="6" class="table_query_4Col_input"
			style="text-align: center">
			<c:if test="${tc.codeLevel<3}"><input name="queryBtn" type="button" class="long_btn" onclick="tableBtn();" value="新增行" id="addBtn"/></c:if>
			<input name="queryBtn" type="button" class="normal_btn"  value="保存"  onclick="saveData();" id="queryBtn" />
			<input name="queryBtn" type="button" class="normal_btn"  value="返回"  onclick="history.back();" id="backBtn" />  
			
	</td>
	</tr>
</table>
<div height="200px;" id="curDiv" style="background-color: #F3F4F8;">
<table id="nextTable"    width="100%;" style="border-collapse:collapse;cellspacing:0;">
	<tr style="height:20px;">
		<td  style="border:1px solid black;" >机构代码</td>
		<td  style="border:1px solid black;">机构名称</td>
		<td  style="border:1px solid black;">子级代码</td>
		<td  style="border:1px solid black;">子级名称</td>
<!--		<td >操作</td>-->
	</tr>
	<c:forEach items="${tcList}" var="tc">
		<tr>
			<td  style="border:1px solid black;margin-right: 0px;">&nbsp;${tc.DEALER_CODE}</td>
			<td  style="border:1px solid black;margin-right: 0px;">&nbsp;${tc.DEALER_SHORTNAME}</td>
			<td  style="border:1px solid black;margin-right: 0px;">${tc.CODE_ID}<input type="hidden" name="nextCodeId" value="${tc.CODE_ID}"/></td>
			<td  style="border:1px solid black;"><input name='nextCodeDesc'  type='text' value='${tc.CODE_DESC}' style='width:100%'/></td>
<!--			<td ><a href='#' onclick='deleteThisRow(this);' align='center'>删&nbsp;&nbsp;除</a></td>-->
		</tr>
	</c:forEach>
</table>
</div>
</form>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar1", "topbar")</script> 
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar2", "topbar")</script> 
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar3", "topbar")</script> 
</div>   
</body>
</html>
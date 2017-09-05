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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/customer.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/common.js"></script>
<script type="text/javascript">
	function doInit(){
		loadcalendar();   //初始化时间控件
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
</script>
<title>客户管理</title>
</head>
<body onunload='javascript:destoryPrototype();'  > 
<div class="wbox">
	<input type="hidden" id="isMgr" value="${isMgr}"/>
	<div class="navigation" style="height: 19px;line-height: 19px;color: #e60012;margin-left: 24px;background-color: white;">
	<img src="<%=contextPath%>/img/crm/nav.gif" style="vertical-align: middle;" />&nbsp;当前位置：  整车销售 &gt;  潜客管理  &gt;客户管理 &gt; 客户查询</div>
	
		<form id="fm" name="fm" method="post">
		<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
	     	 	<td align="right">客户名称：</td>
		     	<td align="left" width="16%">
		      	<input type="text" name="customerName" />
		      	</td>
		    	<td align="right">电话：</td>
		     	<td ><input type="text" name="phone" /></td>
		     	<td align="right">意向车型：</td>
		     	<td ><input type="text" name="vechile" /></td>
		     	<td align="right">客户等级：</td>
		     	<td ><input type="hidden" id="ctmRank" name="ctmRank" value="" />
	      		<div id="ddtopmenubar1" class="mattblackmenu">
					<ul> 
						<li>
							<a style="width:130px;" rel="ddsubmenu41" href="#" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6010', loadCtmRank);" deftitle="--请选择--">--请选择--</a>
							<ul id="ddsubmenu41" class="ddsubmenustyle"></ul>
						</li>
					</ul>
				</div>
				</td>
			</tr>
			<tr>
	     	 	<td align="right">客户类型：</td>
		     	<td align="left">
		      	<input type="hidden" id="ctmType" name="ctmType" value="" />
	      		<div id="ddtopmenubar" class="mattblackmenu">
					<ul> 
						<li>
							<a style="width:130px;" rel="ddsubmenu40" href="#" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6034', loadCtmType);" deftitle="--请选择--">--请选择--</a>
							<ul id="ddsubmenu40" class="ddsubmenustyle"></ul>
						</li>
					</ul>
				</div>
		      	</td>
		      	<td align="right" width="8%">集客方式：</td>
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
		      	<c:if test="${adviserLogon=='no' }">
						<td align="right" width="8%">顾问：</td>
					<td>
						<select id="adviserId" name="adviserId">
				      			<option value="">--请选择--</option>
				      		<c:forEach var="item" items="${adviserList }" varStatus="status">
				      			<option id="${item.USER_ID }" value="${item.USER_ID }">${item.NAME }</option>
				      		</c:forEach>
				      	</select>
					</td>
				</c:if>
		    	<c:if test="${managerLogon=='yes' }">
						<td align="right" width="8%">分组：</td>
						<td width="16%">
							<select id="groupId" name="groupId">
				      			<option value="">--请选择--</option>
				      		<c:forEach var="item" items="${groupList }" varStatus="status">
				      			<option id="${item.GROUP_ID }" value="${item.GROUP_ID }">${item.GROUP_NAME }</option>
				      		</c:forEach>
				      	</select>
						</td>
				</c:if>
		     	<td >&nbsp;</td>
		     	<td >&nbsp;</td>
			</tr>
			<tr>
				<td width="10%" align="right">创建时间从：</td>
					<td width="22%">
						<div align="left">
							<input name="startDate" id="startDate" value="" type="text"
								class="middle_txt"  datatype="1,is_date,10" style="width: 80px;"
								group="startDate,endDate" />
								<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value="&nbsp;" />
							到<input name="endDate" id="endDate" value=""
								type="text" class="middle_txt" datatype="1,is_date,10" style="width: 80px;"
								group="startDate,endDate" />
								<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
						</div>
					</td>
			</tr>
	    	<tr>
				<td  colspan="8" align="center" >
						<input type="button" class="normal_btn" onclick="setCols();__extQuery__(1);" value=" 查  询  " id="queryBtn" />
						&nbsp;&nbsp;&nbsp;<c:if test="${isMgr==1}"><input type="button" class="normal_btn" onclick="changeAdviserAdd();" value="分配顾问 " id="addBtn" /></c:if>
				</td>
		</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />	
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
	
	
	
</div> 
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar", "topbar")</script>  
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar1", "topbar")</script>
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar27", "topbar")</script> 
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar28", "topbar")</script> 
</body>
</html>
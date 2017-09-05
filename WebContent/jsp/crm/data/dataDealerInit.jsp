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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/data/dataDealerManage.js"></script>
<title>字典查询（经销商）</title>
</head>
<body onunload='' onload="__extQuery__(1);"> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt;  潜客管理  &gt;字典管理 &gt;字典查询（经销商） </div>
	<form id="fm" name="fm" method="post">
		<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
	<table class="table_query" border="0">
		<tr>
	      <td align="right" width="25%">字典状态：</td>
	      <td align="left">
	      		<input type="hidden" id="status" name="status"  />
	      		<div id="ddtopmenubar" class="mattblackmenu">
					<ul> 
						<li>
							<a style="width:100px;" rel="ddsubmenu40" href="#" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1001', loadStatus);" deftitle="--请选择--">--请选择--</a>
							<ul id="ddsubmenu40" class="ddsubmenustyle"></ul>
						</li>
					</ul>
				</div>
	      </td>
	     <td align="right">字典名称：</td>
	     <td ><input type="text" name="typeName" id="typeName" 
	     	
	     	<c:if test="${codeName != null }">
	     	 	value="${codeName}"
	     	</c:if>
	      /></td>
	      <td style="display:none;">是否经销商可以维护：</td>
	      <td  style="display:none;">
	      		<input type="hidden" id="ifDealer" name="ifDealer" value="" />
	      		<div id="ifbar" class="mattblackmenu">
					<ul> 
						<li>
							<a style="width:100px;" rel="ifbar40" href="#" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1004', loadIf);" deftitle="--请选择--">--请选择--</a>
							<ul id="ifbar40" class="ddsubmenustyle"></ul>
						</li>
					</ul>
				</div>
	     </td>
		</tr>
	    <tr>
	    	<td> &nbsp;</td>
			<td  colspan="2" align="center" >
					<input type="button" class="normal_btn" onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" />
<!--					<c:if test="${fn:contains(funcStr, 1011090101)}">&nbsp;&nbsp;&nbsp;-->
<!--        			<input name="addBtn" type="button" class="normal_btn" onclick="addGroup()" value="新增" />-->
<!--        			</c:if>-->
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
<script type="text/javascript">ddlevelsmenu.setup("ifbar", "topbar")</script>
</body>
</html>
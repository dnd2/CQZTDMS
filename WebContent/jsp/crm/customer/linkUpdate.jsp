<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/common.js"></script>
<script type="text/javascript">

</script>
<style type="text/css" >
 .mix_type{width:100px;}
 .mm_type{width:176px;}
 .min_type{width:176px;}
 .mini_type{width:198px;}
 .long_type{width:545px;}
 .xlong_type{width:305px}
</style>
<title>实销信息上报</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="loadcalendar();"> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  潜客管理 &gt; 客户管理 &gt;联系人信息修改 </div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="ctmId" name="ctmId" value="${ctmId}"/>
		<input type="hidden" id="decorationId" name="linkId" value="${tp.linkId}"/>
		<table class="table_query" border="0">
		<tr>
				<td width="20%" class="tblopt"><div align="right">联系人姓名：</div></td>
				<td width="30%" >
      				<input type="text" id="linkman" name="linkman" class="min_type" size="20" value="${tp.linkMan}"/>
    			</td>
    			<td width="20%" class="tblopt"><div align="right">联系电话：</div></td>
				<td width="30%" >
      				 <input name="linktel" type="text" id="linktel"  class="min_type"  value="${tp.linkPhone}" />
    			</td>
			</tr>
			<tr>
				<td width="20%" class="tblopt"><div align="right">证件类型：</div></td>
				<td width="30%" >
		        <input type="hidden" id="cardType" name="cardType" value="${tp.cardType}" />
	      		<div id="ddtopmenubar1" class="mattblackmenu">
					<ul> 
						<li>
							<a style="width:173px;" rel="ddsubmenu1" href="###" isclick="true" 
							onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1093', loadCardType);" deftitle="--请选择--">
							<c:if test="${cardType==null}">--请选择--</c:if>	<c:if test="${cardType!=null}">${cardType}</c:if></a>
							<ul id="ddsubmenu1" class="ddsubmenustyle"></ul>
						</li>
					</ul>
				</div>
    			</td>
    			<td width="20%" class="tblopt"><div align="right">证件号码：</div></td>
				<td width="30%" >
      				<input type="text" id="cardno" name="cardno" class="min_type" size="20" value="${tp.cardCode}"/>
    			</td>
			</tr>
			<tr>
				<td width="20%" class="tblopt"><div align="right">与车主关系：</div></td>
				<td width="30%" >
      				<input type="text" id="relationship" name="relationship" class="min_type" size="20" value="${tp.relationship}"/>
    			</td>
				<td width="20%" class="tblopt"><div align="right">&nbsp;</div></td>
				<td width="30%"  >
      				&nbsp;
    			</td>
			</tr>
			<tr>
				<td class="table_query_3Col_input" colspan="4" align="center">
					<center>
					<input type="button" class="normal_btn" onclick="updateLink();" value="修 &nbsp; 改" id="queryBtn" /> 
					</center>
				</td>
			</tr>
		</table>
		
</form>
	
</div>
<script type="text/javascript">

	function updateLink(){
		makeFormCall('<%=contextPath%>/crm/customer/CustomerManage/linkUpdate.json', addResult, "fm") ;
	}
	//数据回写
	function addResult(json){
		_hide();
		parent.$('inIframe').contentWindow.linkTableAdd(json);
	}

</script>  
<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar1", "topbar")</script>  
</body>
</html>
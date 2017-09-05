<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@page import="com.infodms.dms.po.TtAsRepairOrderExtPO"%>
<%@page import="com.infodms.dms.po.TtAsRoLabourPO"%>
<%@page import="com.infodms.dms.po.TtAsRoRepairPartPO"%>
<%@page import="com.infodms.dms.po.TtAsRoAddItemPO"%>

<%@page import="com.infodms.dms.po.TtAsActivityPO"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="com.infodms.dms.po.TtAsWrGamefeePO"%>
<%@page import="com.infodms.dms.bean.ClaimListBean"%>
<%@page import="com.infodms.dms.common.Utility"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>

<%
	String contextPath = request.getContextPath();
%>

		
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<TITLE>索赔申请创建</TITLE>

	</HEAD>
	<BODY >
		<div class="navigation">
			<img src="../../img/nav.gif" />
			&nbsp;当前位置：售后服务管理&gt;维修登记&gt;维修登记查询
		</div>
	<form id='fm' name='fm'>
	<input type=hidden name='ID' value='${id }'/>
	<input type=hidden name='roNo' value='${roNo }'/>
	<input type=hidden name='type' value='${type }'/> 
		<table width=100% border="1" align="center" cellpadding="1" cellspacing="1" class="table_edit">
			<tr>
				<td>配件代码</td>
				<td>配件名称</td>
				<td>配件大类</td>
			</tr>
			<c:forEach items="${partCode }" var="part">
				<tr>
					<td>${part.PART_NO }</td>
					<td>${part.PART_NAME }</td>
					<td>${part.PART_TYPE_ID }</td>					
				</tr>
			</c:forEach>
			    	<td align='center' colspan='5'>
    		<c:if test="${flagStr==1}">
    			零件大类未维护，现在结算无法生成索赔单！
    		</c:if>
    	</td>
		</table>
		<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_edit">

    <tr>
    	<td align="center" colspan="5">
    		<input type="button" name="replyBtn" id="replyBtn"  value="立即结算"  class="normal_btn" onClick="pass()"/>
    		<input type="button" name="backBtn" id="backBtn"  value="取消结算"  class="normal_btn" onClick="back();" />
    	</td>

    </tr>
</table>
	</form>
	<SCRIPT LANGUAGE="JavaScript">
		function pass(){
			
			var roNo=$('roNo').value;
			var ID=$('ID').value;
			//var roNo = parentDocument.getElementById('myroNo').value;
			//var ID = parentDocument.getElementById('myID').value;
			window.location.href="<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail.do?type=2&roNo="+roNo+"&ID="+ID;
			
			//parentContainer.showRepairPartWin(roNo,ID,'');
			//_hide();
		}
		function back(){
			window.location.href="<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceForward.do";
		}
	</script>
	</BODY>
</html>

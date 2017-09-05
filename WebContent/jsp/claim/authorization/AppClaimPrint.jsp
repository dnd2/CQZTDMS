`<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
%>
<%@ page import="com.infodms.dms.common.Constant"%>
<!-- 日历类 -->
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/jmstyle/jslib/datepicker/WdatePicker.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/change" prefix="change" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>索赔明细查询</TITLE>

<SCRIPT LANGUAGE="JavaScript">
//设置超链接  begin      
function doInit()
{	
	__extQuery__(1);
	loadcalendar();
}

var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/application/DealerNewKp/Appprint.json?flat=t";
	
				
	var title = null;

	var columns = [
					{header: "序号", renderer:getIndex,align:'center'},
					{id:'action',header: "选择",sortable: false,dataIndex: 'ID',align:'center',renderer:myLink},
					{header: "索赔单号", align:'center', dataIndex: 'APP_CLAIM_NO'},
					{header: "维修类型",align:'center', dataIndex: 'REPAIR_TYPE',renderer:getItemValue},
					{header: "服务活动类型", align:'center', dataIndex: 'ACTIVITY_NAME'},
					{header: "工时申请费用", align:'center', dataIndex:'HOURS_APPLY_AMOUNT'},
					{header: "工时结算费用", align:'center', dataIndex:'HOURS_SETTLEMENT_AMOUNT'},
					{header: "配件申请费用", align:'center', dataIndex: 'PART_APPLY_AMOUNT'},
					{header: "配件结算费用", align:'center', dataIndex: 'PART_SETTLEMENT_AMOUNT'},
					{header: "外出结算费用", align:'center', dataIndex: 'OUTWARD_SETTLEMENT_AMOUNT'},
					{header: "PDI结算费用", align:'center', dataIndex: 'PDI_SETTLEMENT_AMOUNT'},
					{header: "保养结算费", align:'center', dataIndex: 'FIRST_SETTLEMENT_AMOUNT'},
					{header: "服务活动结算费用", align:'center', dataIndex: 'ACTIVITIE_SETTLEMENT_AMOUNT'},
					{header: "结算总计", align:'center', dataIndex: 'SETTLEMENT_TOTAL_AMOUNT'}
		      ];
	//超链接设置
	function myLink(value,meta,record){		
		return String.format("<a href=\"###\" onclick=\"fmFind("+value+"); \">[索赔明细查看]</a>");
	}
 function Back(){
	 __extQuery__(1);
	}
 function fmFind(value){
	 var form = document.getElementById("fm");
		form.action ='<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/auditApp.do?flag=se&id='+value;
		form.submit();	
} 
	function oemTxt(a,b){
			document.getElementById(a).value="";
			document.getElementById(b).value="";
	}
</SCRIPT>
</HEAD>
<BODY>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;索赔明细查询</div>
    <form method="post" name ="fm" id="fm">
    <div class="form-panel">
	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
	<div class="form-body">
	<input type="hidden" name="ids" id="ids" value="${ids }"/>
    <TABLE class="table_query" border="0">
    	<tr>
            <td style="text-align: right">索赔单号：</td>
            <td><input type="text" name="APP_CLAIM_NO" id="APP_CLAIM_NO" maxlength="20" class="middle_txt"/></td>	    
        </tr>               	    	
    	<tr>
            <td colspan="6" style="text-align: center">
            <input name="queryBtn" type="button" class="u-button u-query" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> &nbsp; 
			<input type="reset" class="u-button u-cancel" value="重 置"/> &nbsp; 
			</td>
        </tr>
  </table>
  </div>
  </div>
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
</BODY>
</html>
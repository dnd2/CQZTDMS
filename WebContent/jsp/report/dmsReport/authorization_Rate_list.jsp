<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>一般索赔单列表查询页面</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

var myPage;
//查询路径
	var url = "<%=contextPath%>/report/dmsReport/Application/authorizationRate.json?type=query";
			
var title = null;

var columns = [
				   {header: "序号",sortable: false,align:'center',renderer:getIndex},
                   {header: "大区", dataIndex: 'ROOT_ORG_NAME', align:'center'},
                   {header: "服务商代码", dataIndex: 'DEALER_CODE', align:'center'},
                   {header: "服务商简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
                   {header: "索赔预授权申报数量", dataIndex: 'REPORT_SUM', align:'center'},
                   {header: "通过数量", dataIndex: 'PASS_SUM', align:'center'},
                   {header: "一次通过数量", dataIndex: 'ONE_PASS_SUM', align:'center'},
                   {header: "驳回数量", dataIndex: 'REBUT_SUM', align:'center'},
                   {header: "拒赔数量", dataIndex: 'REFUSAL_SUM', align:'center'},
                   {header: "预授权索赔通过率", dataIndex: 'PERCENT_PASS', align:'center'},
                   {header: "一次通过率", dataIndex: 'PERCENT_ONE_PASS', align:'center'}
	      ];
   
     function wrapOut(){
 		$("dealer_id").value="";
 		$("dealer_code").value="";
 	}
	function exportToexcel(){
		var url = "<%=contextPath%>/report/dmsReport/Application/ExportauthorizationRate.do";
       fm.action=url;
       fm.submit();
	}
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;预授权通过率报表
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	
	
	<tr>
		<td width="12.5%"></td>
		<td width="10%"class="table_query_5Col_label_3Letter" nowrap="true">大区：
		          <select id="__large_org" name="__large_org" class="short_sel" >
						<option value="">--请选择--</option>
						<c:forEach var="org" items="${orglist}">
							<option value="${org.ORG_NAME}" title="${org.ORG_NAME}">${org.ORG_NAME}</option>
						</c:forEach>
					</select>
		</td>
		<td width="15%" class="table_query_5Col_label_7Letter" nowrap="true">申报开始日期：
      	<input name="beginTime" type="text" id="beginTime" readonly="readonly" onfocus="calendar();" class="short_txt"/>
		</td>
      	<td width="15%" class="table_query_5Col_label_7Letter" nowrap="true">
      	  申报结束日期：
      	<input name="endTime" type="text" id="endTime"  readonly="readonly" onfocus="calendar();" class="short_txt"/>
      	</td>
        <td width="10%"class="table_query_5Col_label_4Letter" nowrap="true">服务站：</td>
		<td width="30%" class="table_query_5Col_label_5Letter" >
		       <input class="short_txt" id="dealer_code"  name="dealerCode" type="text" onclick="showOrgDealer('dealer_code','dealer_id','true','','false','','10771002');" readonly="readonly"/>
				<input type="hidden" name="dealerId" id="dealer_id" value=""/>
 				<input type="button" value="清除" class="normal_btn" onclick="wrapOut();"/>
		</td>
		<td width="12.5%"></td>
	</tr>
	
	<tr>
    	<td align="center" colspan="8">
    		&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="exportBtn" value="导出" class="normal_btn" onClick="exportToexcel();"/>
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    	</td>
    </tr>
</table>
<!-- 查询条件 end -->

<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
</html>
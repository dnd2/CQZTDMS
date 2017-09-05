<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 



<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>三包审核统计费用明细</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit()
	{
   		loadcalendar();  //初始化时间控件
   		genLocSel('province','','','','',''); // 加载省份城市和县
	}
 function view(){
		fm.action = "<%=contextPath%>/report/jcafterservicereport/ThreeGuaranteesAuditReport/threeGuaranteesAuditReportView.do";
		fm.submit();
		
}
	function exportExcel()
	{
		fm.action = "<%=contextPath%>/report/jcafterservicereport/ServiceCenterMonthlyReport/exportToExce2.json";
		fm.submit();
	}
</script>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理&gt;轿车售后索赔报表&gt;三包审核统计费用明细（轿车）</div>

  <form method="post" name="fm" id="fm">
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
   <!-- 查询条件 begin -->
  <table class="table_query">
              
          <tr>
            <td align="right" nowrap="nowrap">维修起止时间： </td>
            <td colspan="6" nowrap="nowrap">
            	<div align="left">
            		<input name="beginTime" id="t1" value="${beginTime}" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            		&nbsp;至&nbsp;
            		<input name="endTime" id="t2" value="${endTime}" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            	</div>
   
            <td align="right" nowrap="nowrap">审核起止时间： </td>
            <td colspan="6" nowrap="nowrap">
            	<div align="left">
            		<input name="audit_beginTime" id="t3" value="${audit_beginTime}" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't3', false);">
            		&nbsp;至&nbsp;
            		<input name="audit_endTime" id="t4" value="${audit_endTime }" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't4', false);">
            	</div>
          </tr>
           		<tr>
            <td width="7%" align="right" nowrap="nowrap">所说大区：</td>
            <td colspan="6">
            	<select class="short_sel" name="areaName" value="${areaName}">
            		<option value="">-请选择-</option>
	            	<c:if test="${areaList!=null}">
						<c:forEach items="${areaList}" var="list1">
							<option value="${list1.ORG_ID}">${list1.ORG_NAME}</option>
						</c:forEach>
					</c:if>
				</select>
            </td>
            <td width="19%" align="right" nowrap="nowrap">服务商代码：</td>
            <td colspan="2" align="left" >
            	<input class="middle_txt" id="dealerCode" name="dealerCode" value="${dealerCode}" type="text"/>
            	<input class="mark_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCode','','true','','true')"/>   
            	<input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/> 
            </td>
          </tr> 
          <tr>
            <td align="right" nowrap="nowrap" >&nbsp;</td>
            <td colspan="6" nowrap="nowrap">&nbsp;</td>
            <td align="left" nowrap="nowrap">
            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询"  onclick="view()"/>&nbsp;
            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="下载"  onclick="exportExcel()" />
            </td>
            <td>&nbsp;</td>
            <td align="right" >&nbsp;</td>
          </tr>
  </table>
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>

<!--页面列表 begin 
<script type="text/javascript" >
	var myPage;
//查询路径
	var url = "<%=contextPath%>/report/jcafterservicereport/ThreeGuaranteesAuditReport/threeGuaranteesAuditReportView.json";
				
	var title = null;

	var columns = [
				{header: "序号", align:'center',renderer:getIndex},
				{header: "维修站代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "维修站名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "维修起时间", dataIndex: 'START_DATE', align:'center'},
				{header: "维修止时间", dataIndex: 'END_DATE', align:'center'},
				{header: "大区", dataIndex: 'ROOT_ORG_NAME', align:'center'},
				{header: "审核时间", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "生产厂家", dataIndex: 'REGION_NAME', align:'center'},
				{header: "（奔奔MINI）售后维修工时费", dataIndex: 'BBMI_SHGSF', align:'center'},
				{header: "（奔奔MINI）售后维修材料费", dataIndex: 'BBMI_SHCLF', align:'center'},
				{header: "（奔奔MINI）售后维修单据数", dataIndex: 'BBMI_SHSPDS', align:'center'},
				{header: "（奔奔MINI）售前维修工时费", dataIndex: 'BBMI_SQGSF', align:'center'},
				{header: "（奔奔MINI）售前维修材料费", dataIndex: 'BBMI_SQCLF', align:'center'},
				{header: "（奔奔MINI）售前维修单据数", dataIndex: 'BBMI_SQSPDS', align:'center'},
				{header: "（奔奔MINI）保养费", dataIndex: 'BBMI_MFBYZFY', align:'center'},
				{header: "（奔奔MINI）保养单数", dataIndex: 'BBMI_MFBYDS', align:'center'},
				{header: "（奔奔MINI）服务活动单数", dataIndex: 'BBMI_FWHDSPDS', align:'center'},
				//奔奔
			    {header: "（奔奔）售后维修工时费", dataIndex: 'BB_SHGSF', align:'center'},
				{header: "（奔奔）售后维修材料费", dataIndex: 'BB_SHCLF', align:'center'},
				{header: "（奔奔）售后维修单据数", dataIndex: 'BB_SHSPDS', align:'center'},
				{header: "（奔奔）售前维修工时费", dataIndex: 'BB_SQGSF', align:'center'},
				{header: "（奔奔）售前维修材料费", dataIndex: 'BB_SQCLF', align:'center'},
				{header: "（奔奔）售前维修单据数", dataIndex: 'BB_SQSPDS', align:'center'},
				{header: "（奔奔）保养费", dataIndex: 'BB_MFBYZFY', align:'center'},
				{header: "（奔奔）保养单数", dataIndex: 'BB_MFBYDS', align:'center'},
				{header: "（奔奔）服务活动单数", dataIndex: 'BB_FWHDSPDS', align:'center'},
				//悦翔
				{header: "（悦翔）售后维修工时费", dataIndex: 'YX_SHGSF', align:'center'},
				{header: "（悦翔）售后维修材料费", dataIndex: 'YX_SHCLF', align:'center'},
				{header: "（悦翔）售后维修单据数", dataIndex: 'YX_SHSPDS', align:'center'},
				{header: "（悦翔）售前维修工时费", dataIndex: 'YX_SQGSF', align:'center'},
				{header: "（悦翔）售前维修材料费", dataIndex: 'YX_SQCLF', align:'center'},
				{header: "（悦翔）售前维修单据数", dataIndex: 'YX_SQSPDS', align:'center'},
				{header: "（悦翔）保养费", dataIndex: 'YX_MFBYZFY', align:'center'},
				{header: "（悦翔）保养单数", dataIndex: 'YX_MFBYDS', align:'center'},
				{header: "（悦翔）服务活动单数", dataIndex: 'YX_FWHDSPDS', align:'center'},
				//杰勋
				{header: "（杰勋）售后维修工时费", dataIndex: 'JX_SHGSF', align:'center'},
				{header: "（杰勋）售后维修材料费", dataIndex: 'JX_SHCLF', align:'center'},
				{header: "（杰勋）售后维修单据数", dataIndex: 'JX_SHSPDS', align:'center'},
				{header: "（杰勋）售前维修工时费", dataIndex: 'JX_SQGSF', align:'center'},
				{header: "（杰勋）售前维修材料费", dataIndex: 'JX_SQCLF', align:'center'},
				{header: "（杰勋）售前维修单据数", dataIndex: 'JX_SQSPDS', align:'center'},
				{header: "（杰勋）保养费", dataIndex: 'JX_MFBYZFY', align:'center'},
				{header: "（杰勋）保养单数", dataIndex: 'JX_MFBYDS', align:'center'},
				{header: "（杰勋）服务活动单数", dataIndex: 'JX_FWHDSPDS', align:'center'},
				//志翔
				{header: "（志翔）售后维修工时费", dataIndex: 'ZX_SHGSF', align:'center'},
				{header: "（志翔）售后维修材料费", dataIndex: 'ZX_SHCLF', align:'center'},
				{header: "（志翔）售后维修单据数", dataIndex: 'ZX_SHSPDS', align:'center'},
				{header: "（志翔）售前维修工时费", dataIndex: 'ZX_SQGSF', align:'center'},
				{header: "（志翔）售前维修材料费", dataIndex: 'ZX_SQCLF', align:'center'},
				{header: "（志翔）售前维修单据数", dataIndex: 'ZX_SQSPDS', align:'center'},
				{header: "（志翔）保养费", dataIndex: 'ZX_MFBYZFY', align:'center'},
				{header: "（志翔）保养单数", dataIndex: 'ZX_MFBYDS', align:'center'},
				{header: "（志翔）服务活动单数", dataIndex: 'ZX_FWHDSPDS', align:'center'},
			    //CX30
				{header: "（CX30）售后维修工时费", dataIndex: 'CX30_SHGSF', align:'center'},
				{header: "（CX30）售后维修材料费", dataIndex: 'CX30_SHCLF', align:'center'},
				{header: "（CX30）售后维修单据数", dataIndex: 'CX30_SHSPDS', align:'center'},
				{header: "（CX30）售前维修工时费", dataIndex: 'CX30_SQGSF', align:'center'},
				{header: "（CX30）售前维修材料费", dataIndex: 'CX30_SQCLF', align:'center'},
				{header: "（CX30）售前维修单据数", dataIndex: 'CX30_SQSPDS', align:'center'},
				{header: "（CX30）保养费", dataIndex: 'CX30_MFBYZFY', align:'center'},
				{header: "（CX30）保养单数", dataIndex: 'CX30_MFBYDS', align:'center'},
				{header: "（CX30）服务活动单数", dataIndex: 'CX30_FWHDSPDS', align:'center'},
				//CX20
				{header: "（CX20）售后维修工时费", dataIndex: 'CX20_SHGSF', align:'center'},
				{header: "（CX20）售后维修材料费", dataIndex: 'CX20_SHCLF', align:'center'},
				{header: "（CX20）售后维修单据数", dataIndex: 'CX20_SHSPDS', align:'center'},
				{header: "（CX20）售前维修工时费", dataIndex: 'CX20_SQGSF', align:'center'},
				{header: "（CX20）售前维修材料费", dataIndex: 'CX20_SQCLF', align:'center'},
				{header: "（CX20）售前维修单据数", dataIndex: 'CX20_SQSPDS', align:'center'},
				{header: "（CX20）保养费", dataIndex: 'CX20_MFBYZFY', align:'center'},
				{header: "（CX20）保养单数", dataIndex: 'CX20_MFBYDS', align:'center'},
				{header: "（CX20）服务活动单数", dataIndex: 'CX20_FWHDSPDS', align:'center'},
				//CX30三厢
				{header: "（CX30三厢）售后维修工时费", dataIndex: 'CX30SX_SHGSF', align:'center'},
				{header: "（CX30三厢）售后维修材料费", dataIndex: 'CX30SX_SHCLF', align:'center'},
				{header: "（CX30三厢）售后维修单据数", dataIndex: 'CX30SX_SHSPDS', align:'center'},
				{header: "（CX30三厢）售前维修工时费", dataIndex: 'CX30SX_SQGSF', align:'center'},
				{header: "（CX30三厢）售前维修材料费", dataIndex: 'CX30SX_SQCLF', align:'center'},
				{header: "（CX30三厢）售前维修单据数", dataIndex: 'CX30SX_SQSPDS', align:'center'},
				{header: "（CX30三厢）保养费", dataIndex: 'CX30SX_MFBYZFY', align:'center'},
				{header: "（CX30三厢）保养单数", dataIndex: 'CX30SX_MFBYDS', align:'center'},
				{header: "（CX30三厢）服务活动单数", dataIndex: 'CX30SX_FWHDSPDS', align:'center'},
				//SC7133BR.CFA
				{header: "（SC7133BR.CFA）售后维修工时费", dataIndex: 'SC7133BR_SHGSF', align:'center'},
				{header: "（SC7133BR.CFA）售后维修材料费", dataIndex: 'SC7133BR_SHCLF', align:'center'},
				{header: "（SC7133BR.CFA）售后维修单据数", dataIndex: 'SC7133BR_SHSPDS', align:'center'},
				{header: "（SC7133BR.CFA）售前维修工时费", dataIndex: 'SC7133BR_SQGSF', align:'center'},
				{header: "（SC7133BR.CFA）售前维修材料费", dataIndex: 'SC7133BR_SQCLF', align:'center'},
				{header: "（SC7133BR.CFA）售前维修单据数", dataIndex: 'SC7133BR_SQSPDS', align:'center'},
				{header: "（SC7133BR.CFA）保养费", dataIndex: 'SC7133BR_MFBYZFY', align:'center'},
				{header: "（SC7133BR.CFA）保养单数", dataIndex: 'SC7133BR_MFBYDS', align:'center'},
				{header: "（SC7133BR.CFA）服务活动单数", dataIndex: 'SC7133BR_FWHDSPDS', align:'center'},
				///-------------------
				{header: "工时费", dataIndex: 'LABOUR_AMOUNT', align:'center'},
				{header: "材料费", dataIndex: 'PART_AMOUNT', align:'center'},
				{header: "走保费", dataIndex: 'FREE_AMOUNT', align:'center'},
				{header: "服务活动费", dataIndex: 'SERVICE_TOTAL_AMOUNT', align:'center'},
				{header: "救济费", dataIndex: 'OTHER_AMOUNT', align:'center'},
				{header: "运费", dataIndex: 'RETURN_AMOUNT', align:'center'},
				{header: "特殊费用", dataIndex: 'MARKET_AMOUNT_BAK', align:'center'},
				{header: "保养费扣款", dataIndex: 'FREE_DEDUCT', align:'center'},
				{header: "服务活动费扣款", dataIndex: 'SUM_SERVICE_DEDUCT', align:'center'},
				{header: "旧件扣款", dataIndex: 'OLD_DEDUCT', align:'center'},
				{header: "考核扣款", dataIndex: 'CHECK_DEDUCT', align:'center'},
				{header: "费用合计", dataIndex: 'Apply_Amount', align:'center'},
				{header: "扣款合计", dataIndex: 'SUM_KKZJ', align:'center'},
				{header: "总计合计", dataIndex: 'BALANCE_AMOUNT', align:'center'},
				{header: "审核扣款", dataIndex: 'SUM_KKZJ', align:'center'},
				{header: "结算员", dataIndex: 'AUTH_PERSON_NAME', align:'center'},
				{header: "开票单位名称", dataIndex: '', align:'center'}
		      ];
		        
	function exportExcel()
	{
		fm.action = "<%=contextPath%>/report/jcafterservicereport/ServiceCenterMonthlyReport/exportToExce2.json";
		fm.submit();
	}
	
    //清空经销商框
	function clearInput(){
		var target = document.getElementById('dealerCode');
		target.value = '';
	}

</script>
页面列表 end -->
<table width=100% border="0" align="center" class="table_list">
	<tr class="table_list_row2">
 			<td align="center"><strong>序号</strong></td>
			<td align="center"><strong>维修站代码</strong></td>
			<td align="center"><strong>维修站名称</strong></td>
			<td align="center"><strong>维修起时间</strong></td>
			<td align="center"><strong>维修止时间</strong></td>
			<td align="center"><strong>大区</strong></td>
			<td align="center"><strong>审核时间</strong></td>
			<td align="center"><strong>生产厂家</strong></td>
		    <td align="center"><strong>（奔奔MINI）售后维修工时费</strong></td>
			<td align="center"><strong>（奔奔MINI）售后维修材料</strong></td>
			<td align="center"><strong>（奔奔MINI）售后维修单据数</strong></td>
			<td align="center"><strong>（奔奔MINI）售前维修工时费</strong></td>
			<td align="center"><strong>（奔奔MINI）售前维修材料费</strong></td>
			<td align="center"><strong>（奔奔MINI）售前维修单据数</strong></td>
			<td align="center"><strong>（奔奔MINI）保养费</strong></td>
		    <td align="center"><strong>（奔奔MINI）保养单数</strong></td>
			<td align="center"><strong>（奔奔MINI）服务活动单数</strong></td>
			<!-- 奔奔 -->
		    <td align="center"><strong>（奔奔）售后维修工时费</strong></td>
			<td align="center"><strong>（奔奔）售后维修材料</strong></td>
			<td align="center"><strong>（奔奔）售后维修单据数</strong></td>
			<td align="center"><strong>（奔奔）售前维修工时费</strong></td>
			<td align="center"><strong>（奔奔）售前维修材料费</strong></td>
			<td align="center"><strong>（奔奔）售前维修单据数</strong></td>
			<td align="center"><strong>（奔奔）保养费</strong></td>
		    <td align="center"><strong>（奔奔）保养单数</strong></td>
			<td align="center"><strong>（奔奔）服务活动单数</strong></td>
			<!-- 悦翔 -->
		    <td align="center"><strong>（悦翔）售后维修工时费</strong></td>
			<td align="center"><strong>（悦翔）售后维修材料</strong></td>
			<td align="center"><strong>（悦翔）售后维修单据数</strong></td>
			<td align="center"><strong>（悦翔）售前维修工时费</strong></td>
			<td align="center"><strong>（悦翔）售前维修材料费</strong></td>
			<td align="center"><strong>（悦翔）售前维修单据数</strong></td>
			<td align="center"><strong>（悦翔）保养费</strong></td>
		    <td align="center"><strong>（悦翔）保养单数</strong></td>
			<td align="center"><strong>（悦翔）服务活动单数</strong></td>
			<!-- 杰勋 -->
		    <td align="center"><strong>（杰勋 ）售后维修工时费</strong></td>
			<td align="center"><strong>（杰勋 ）售后维修材料</strong></td>
			<td align="center"><strong>（杰勋 ）售后维修单据数</strong></td>
			<td align="center"><strong>（杰勋 ）售前维修工时费</strong></td>
			<td align="center"><strong>（杰勋 ）售前维修材料费</strong></td>
			<td align="center"><strong>（杰勋 ）售前维修单据数</strong></td>
			<td align="center"><strong>（杰勋 ）保养费</strong></td>
		    <td align="center"><strong>（杰勋 ）保养单数</strong></td>
			<td align="center"><strong>（杰勋 ）服务活动单数</strong></td>
			<!-- 志翔 -->
	        <td align="center"><strong>（志翔）售后维修工时费</strong></td>
			<td align="center"><strong>（志翔 ）售后维修材料</strong></td>
			<td align="center"><strong>（志翔 ）售后维修单据数</strong></td>
			<td align="center"><strong>（志翔 ）售前维修工时费</strong></td>
			<td align="center"><strong>（志翔 ）售前维修材料费</strong></td>
			<td align="center"><strong>（志翔 ）售前维修单据数</strong></td>
			<td align="center"><strong>（志翔 ）保养费</strong></td>
		    <td align="center"><strong>（志翔 ）保养单数</strong></td>
			<td align="center"><strong>（志翔 ）服务活动单数</strong></td>
			<!-- CX30 -->
	        <td align="center"><strong>（CX30两箱）售后维修工时费</strong></td>
			<td align="center"><strong>（CX30两箱）售后维修材料</strong></td>
			<td align="center"><strong>（CX30两箱）售后维修单据数</strong></td>
			<td align="center"><strong>（CX30两箱）售前维修工时费</strong></td>
			<td align="center"><strong>（CX30两箱）售前维修材料费</strong></td>
			<td align="center"><strong>（CX30两箱）售前维修单据数</strong></td>
			<td align="center"><strong>（CX30两箱）保养费</strong></td>
		    <td align="center"><strong>（CX30两箱）保养单数</strong></td>
			<td align="center"><strong>（CX30两箱 ）服务活动单数</strong></td>
			<!-- CX20 -->
	        <td align="center"><strong>（CX20）售后维修工时费</strong></td>
			<td align="center"><strong>（CX20）售后维修材料</strong></td>
			<td align="center"><strong>（CX20）售后维修单据数</strong></td>
			<td align="center"><strong>（CX20）售前维修工时费</strong></td>
			<td align="center"><strong>（CX20）售前维修材料费</strong></td>
			<td align="center"><strong>（CX20）售前维修单据数</strong></td>
			<td align="center"><strong>（CX20）保养费</strong></td>
		    <td align="center"><strong>（CX20）保养单数</strong></td>
			<td align="center"><strong>（CX20 ）服务活动单数</strong></td>
			<!-- CX30三厢 -->
		    <td align="center"><strong>（CX30三厢）售后维修工时费</strong></td>
			<td align="center"><strong>（CX30三厢）售后维修材料</strong></td>
			<td align="center"><strong>（CX30三厢）售后维修单据数</strong></td>
			<td align="center"><strong>（CX30三厢）售前维修工时费</strong></td>
			<td align="center"><strong>（CX30三厢）售前维修材料费</strong></td>
			<td align="center"><strong>（CX30三厢）售前维修单据数</strong></td>
			<td align="center"><strong>（CX30三厢）保养费</strong></td>
		    <td align="center"><strong>（CX30三厢）保养单数</strong></td>
			<td align="center"><strong>（CX30三厢）服务活动单数</strong></td>
			<!-- SC7133BR.CFA -->

			
			<td align="center"><strong>工时费</strong></td>
			<td align="center"><strong>材料费</strong></td>
			<td align="center"><strong>走保费</strong></td>
			<td align="center"><strong>服务活动费</strong></td>
			<td align="center"><strong>救济费</strong></td>
			<td align="center"><strong>运费</strong></td>
			<td align="center"><strong>特殊费用</strong></td>
		    <td align="center"><strong>保养费扣款</strong></td>
			<td align="center"><strong>服务活动费扣款</strong></td>
			
		    <td align="center"><strong>旧件扣款</strong></td>
			<td align="center"><strong>考核扣款</strong></td>
			<td align="center"><strong>费用合计</strong></td>
			<td align="center"><strong>扣款合计</strong></td>
			<td align="center"><strong>总计合计</strong></td>
		    <td align="center"><strong>审核扣款</strong></td>
			<td align="center"><strong>结算员</strong></td>
			<td align="center"><strong>开票单位名称</strong></td>
 	</tr>
 	<c:forEach var="freezeList" items="${list}" varStatus="status">
 			<tr class="table_list_row${status.index%2+1 }">
 			<td align="center">${status.index+1 }</td>
			<td align="center">${freezeList.DEALER_CODE}</td>
			<td align="center">${freezeList.DEALER_NAME}</td>
			<td align="center">
		<fmt:formatDate value="${freezeList.START_DATE}" pattern="yyyy-MM-dd"/> 
			</td>
			<td align="center">
			<fmt:formatDate value="${freezeList.END_DATE}" pattern="yyyy-MM-dd"/> 
			</td>
			<td align="center">${freezeList.ROOT_ORG_NAME}</td>
			<td align="center">
			<fmt:formatDate value="${freezeList.CREATE_DATE }" pattern="yyyy-MM-dd"/> 
			</td>
			<td align="center">重庆长安</td>
		    <td align="center">${freezeList.BBMI_SHGSF}</td>
			<td align="center">${freezeList.BBMI_SHCLF}</td>
			<td align="center">${freezeList.BBMI_SHSPDS}</td>
		    <td align="center">${freezeList.BBMI_SQGSF}</td>
			<td align="center">${freezeList.BBMI_SQCLF}</td>
			<td align="center">${freezeList.BBMI_SQSPDS}</td>
			<td align="center">${freezeList.BBMI_MFBYZFY}</td>
		    <td align="center">${freezeList.BBMI_MFBYDS}</td>
			<td align="center">${freezeList.BBMI_FWHDSPDS}</td>
			<!-- 奔奔 -->
		    <td align="center">${freezeList.BB_SHGSF}</td>
			<td align="center">${freezeList.BB_SHCLF}</td>
			<td align="center">${freezeList.BB_SHSPDS}</td>
		    <td align="center">${freezeList.BB_SQGSF}</td>
			<td align="center">${freezeList.BB_SQCLF}</td>
			<td align="center">${freezeList.BB_SQSPDS}</td>
			<td align="center">${freezeList.BB_MFBYZFY}</td>
		    <td align="center">${freezeList.BB_MFBYDS}</td>
			<td align="center">${freezeList.BB_FWHDSPDS}</td>
			<!-- 悦翔 -->
		    <td align="center">${freezeList.YX_SHGSF}</td>
			<td align="center">${freezeList.YX_SHCLF}</td>
			<td align="center">${freezeList.YX_SHSPDS}</td>
		    <td align="center">${freezeList.YX_SQGSF}</td>
			<td align="center">${freezeList.YX_SQCLF}</td>
			<td align="center">${freezeList.YX_SQSPDS}</td>
			<td align="center">${freezeList.YX_MFBYZFY}</td>
		    <td align="center">${freezeList.YX_MFBYDS}</td>
			<td align="center">${freezeList.YX_FWHDSPDS}</td>
			<!-- 杰勋 -->
		    <td align="center">${freezeList.JX_SHGSF}</td>
			<td align="center">${freezeList.JX_SHCLF}</td>
			<td align="center">${freezeList.JX_SHSPDS}</td>
		    <td align="center">${freezeList.JX_SQGSF}</td>
			<td align="center">${freezeList.JX_SQCLF}</td>
			<td align="center">${freezeList.JX_SQSPDS}</td>
			<td align="center">${freezeList.JX_MFBYZFY}</td>
		    <td align="center">${freezeList.JX_MFBYDS}</td>
			<td align="center">${freezeList.JX_FWHDSPDS}</td>
			<!-- 志翔 -->
		    <td align="center">${freezeList.ZX_SHGSF}</td>
			<td align="center">${freezeList.ZX_SHCLF}</td>
			<td align="center">${freezeList.ZX_SHSPDS}</td>
		    <td align="center">${freezeList.ZX_SQGSF}</td>
			<td align="center">${freezeList.ZX_SQCLF}</td>
			<td align="center">${freezeList.ZX_SQSPDS}</td>
			<td align="center">${freezeList.ZX_MFBYZFY}</td>
		    <td align="center">${freezeList.ZX_MFBYDS}</td>
			<td align="center">${freezeList.ZX_FWHDSPDS}</td>
			<!-- CX30 -->
		    <td align="center">${freezeList.CX30_SHGSF}</td>
			<td align="center">${freezeList.CX30_SHCLF}</td>
			<td align="center">${freezeList.CX30_SHSPDS}</td>
		    <td align="center">${freezeList.CX30_SQGSF}</td>
			<td align="center">${freezeList.CX30_SQCLF}</td>
			<td align="center">${freezeList.CX30_SQSPDS}</td>
			<td align="center">${freezeList.CX30_MFBYZFY}</td>
		    <td align="center">${freezeList.CX30_MFBYDS}</td>
			<td align="center">${freezeList.CX30_FWHDSPDS}</td>
			<!-- CX20 -->
	    	<td align="center">${freezeList.CX20_SHGSF}</td>
			<td align="center">${freezeList.CX20_SHCLF}</td>
			<td align="center">${freezeList.CX20_SHSPDS}</td>
		    <td align="center">${freezeList.CX20_SQGSF}</td>
			<td align="center">${freezeList.CX20_SQCLF}</td>
			<td align="center">${freezeList.CX20_SQSPDS}</td>
			<td align="center">${freezeList.CX20_MFBYZFY}</td>
		    <td align="center">${freezeList.CX20_MFBYDS}</td>
			<td align="center">${freezeList.CX20_FWHDSPDS}</td>
			<!-- CX30三厢 -->
	    	<td align="center">${freezeList.CX30SX_SHGSF}</td>
			<td align="center">${freezeList.CX30SX_SHCLF}</td>
			<td align="center">${freezeList.CX30SX_SHSPDS}</td>
		    <td align="center">${freezeList.CX30SX_SQGSF}</td>
			<td align="center">${freezeList.CX30SX_SQCLF}</td>
			<td align="center">${freezeList.CX30SX_SQSPDS}</td>
			<td align="center">${freezeList.CX30SX_MFBYZFY}</td>
		    <td align="center">${freezeList.CX30SX_MFBYDS}</td>
			<td align="center">${freezeList.CX30SX_FWHDSPDS}</td>
			<!-- SC7133BR.CFA -->
			
			<td align="center">${freezeList.LABOUR_AMOUNT}</td>
			<td align="center">${freezeList.PART_AMOUNT}</td>
			<td align="center">${freezeList.FREE_AMOUNT}</td>
			<td align="center">${freezeList.SERVICE_TOTAL_AMOUNT}</td>
			<td align="center">${freezeList.OTHER_AMOUNT}</td>
			<td align="center">${freezeList.RETURN_AMOUNT}</td>
			<td align="center">${freezeList.MARKET_AMOUNT_BAK}</td>
		    <td align="center">${freezeList.FREE_DEDUCT}</td>
			<td align="center">${freezeList.SUM_SERVICE_DEDUCT}</td>
			
		    <td align="center">${freezeList.OLD_DEDUCT}</td>
			<td align="center">${freezeList.CHECK_DEDUCT}</td>
			<td align="center">${freezeList.APPLY_AMOUNT}</td>
			<td align="center">${freezeList.SUM_KKZJ}</td>
			<td align="center">${freezeList.BALANCE_AMOUNT}</td>
		    <td align="center">${freezeList.SUM_KKZJ_SH}</td>
			<td align="center">${freezeList.AUTH_PERSON_NAME}</td>
			<td align="center">${freezeList.INVOICE_MAKER }</td>
 	</tr>
 	</c:forEach>
		<tr class="table_list_row2">
 			<td align="center">总计</td>
			<td align="center"></td>
			<td align="center"></td>
			<td align="center"></td>
			<td align="center"></td>
			<td align="center"></td>
			<td align="center"></td>
			<td align="center"></td>
		    <td align="center">${listTotal.BBMI_SHGSF}</td>
			<td align="center">${listTotal.BBMI_SHCLF}</td>
			<td align="center">${listTotal.BBMI_SHSPDS}</td>
		    <td align="center">${listTotal.BBMI_SQGSF}</td>
			<td align="center">${listTotal.BBMI_SQCLF}</td>
			<td align="center">${listTotal.BBMI_SQSPDS}</td>
			<td align="center">${listTotal.BBMI_MFBYZFY}</td>
		    <td align="center">${listTotal.BBMI_MFBYDS}</td>
			<td align="center">${listTotal.BBMI_FWHDSPDS}</td>
			<!-- 奔奔 -->
		    <td align="center">${listTotal.BB_SHGSF}</td>
			<td align="center">${listTotal.BB_SHCLF}</td>
			<td align="center">${listTotal.BB_SHSPDS}</td>
		    <td align="center">${listTotal.BB_SQGSF}</td>
			<td align="center">${listTotal.BB_SQCLF}</td>
			<td align="center">${listTotal.BB_SQSPDS}</td>
			<td align="center">${listTotal.BB_MFBYZFY}</td>
		    <td align="center">${listTotal.BB_MFBYDS}</td>
			<td align="center">${listTotal.BB_FWHDSPDS}</td>
			<!-- 悦翔 -->
		    <td align="center">${listTotal.YX_SHGSF}</td>
			<td align="center">${listTotal.YX_SHCLF}</td>
			<td align="center">${listTotal.YX_SHSPDS}</td>
		    <td align="center">${listTotal.YX_SQGSF}</td>
			<td align="center">${listTotal.YX_SQCLF}</td>
			<td align="center">${listTotal.YX_SQSPDS}</td>
			<td align="center">${listTotal.YX_MFBYZFY}</td>
		    <td align="center">${listTotal.YX_MFBYDS}</td>
			<td align="center">${listTotal.YX_FWHDSPDS}</td>
			<!-- 杰勋 -->
		    <td align="center">${listTotal.JX_SHGSF}</td>
			<td align="center">${listTotal.JX_SHCLF}</td>
			<td align="center">${listTotal.JX_SHSPDS}</td>
		    <td align="center">${listTotal.JX_SQGSF}</td>
			<td align="center">${listTotal.JX_SQCLF}</td>
			<td align="center">${listTotal.JX_SQSPDS}</td>
			<td align="center">${listTotal.JX_MFBYZFY}</td>
		    <td align="center">${listTotal.JX_MFBYDS}</td>
			<td align="center">${listTotal.JX_FWHDSPDS}</td>
			<!-- 志翔 -->
		    <td align="center">${listTotal.ZX_SHGSF}</td>
			<td align="center">${listTotal.ZX_SHCLF}</td>
			<td align="center">${listTotal.ZX_SHSPDS}</td>
		    <td align="center">${listTotal.ZX_SQGSF}</td>
			<td align="center">${listTotal.ZX_SQCLF}</td>
			<td align="center">${listTotal.ZX_SQSPDS}</td>
			<td align="center">${listTotal.ZX_MFBYZFY}</td>
		    <td align="center">${listTotal.ZX_MFBYDS}</td>
			<td align="center">${listTotal.ZX_FWHDSPDS}</td>
			<!-- CX30 -->
		    <td align="center">${listTotal.CX30_SHGSF}</td>
			<td align="center">${listTotal.CX30_SHCLF}</td>
			<td align="center">${listTotal.CX30_SHSPDS}</td>
		    <td align="center">${listTotal.CX30_SQGSF}</td>
			<td align="center">${listTotal.CX30_SQCLF}</td>
			<td align="center">${listTotal.CX30_SQSPDS}</td>
			<td align="center">${listTotal.CX30_MFBYZFY}</td>
		    <td align="center">${listTotal.CX30_MFBYDS}</td>
			<td align="center">${listTotal.CX30_FWHDSPDS}</td>
			<!-- CX20 -->
	    	<td align="center">${listTotal.CX20_SHGSF}</td>
			<td align="center">${listTotal.CX20_SHCLF}</td>
			<td align="center">${listTotal.CX20_SHSPDS}</td>
		    <td align="center">${listTotal.CX20_SQGSF}</td>
			<td align="center">${listTotal.CX20_SQCLF}</td>
			<td align="center">${listTotal.CX20_SQSPDS}</td>
			<td align="center">${listTotal.CX20_MFBYZFY}</td>
		    <td align="center">${listTotal.CX20_MFBYDS}</td>
			<td align="center">${listTotal.CX20_FWHDSPDS}</td>
			<!-- CX30三厢 -->
	    	<td align="center">${listTotal.CX30SX_SHGSF}</td>
			<td align="center">${listTotal.CX30SX_SHCLF}</td>
			<td align="center">${listTotal.CX30SX_SHSPDS}</td>
		    <td align="center">${listTotal.CX30SX_SQGSF}</td>
			<td align="center">${listTotal.CX30SX_SQCLF}</td>
			<td align="center">${listTotal.CX30SX_SQSPDS}</td>
			<td align="center">${listTotal.CX30SX_MFBYZFY}</td>
		    <td align="center">${listTotal.CX30SX_MFBYDS}</td>
			<td align="center">${listTotal.CX30SX_FWHDSPDS}</td>
			
			
			<td align="center">${listTotal.LABOUR_AMOUNT}</td>
			<td align="center">${listTotal.PART_AMOUNT}</td>
			<td align="center">${listTotal.FREE_AMOUNT}</td>
			<td align="center">${listTotal.SERVICE_TOTAL_AMOUNT}</td>
			<td align="center">${listTotal.OTHER_AMOUNT}</td>
			<td align="center">${listTotal.RETURN_AMOUNT}</td>
			<td align="center">${listTotal.MARKET_AMOUNT_BAK}</td>
		    <td align="center">${listTotal.FREE_DEDUCT}</td>
			<td align="center">${listTotal.SUM_SERVICE_DEDUCT}</td>
			
		    <td align="center">${listTotal.OLD_DEDUCT}</td>
			<td align="center">${listTotal.CHECK_DEDUCT}</td>
			<td align="center">${listTotal.APPLY_AMOUNT}</td>
			<td align="center">${listTotal.SUM_KKZJ}</td>
			<td align="center">${listTotal.BALANCE_AMOUNT}</td>
		    <td align="center">${listTotal.SUM_KKZJ_SH}</td>
			<td align="center"></td>
			<td align="center"></td>
			</tr>
</table>
</body>
</html>
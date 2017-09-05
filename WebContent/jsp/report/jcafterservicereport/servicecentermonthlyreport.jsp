<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>轿车公司服务中心月申报费用明细表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit()
	{
   		loadcalendar();  //初始化时间控件
   		genLocSel('province','','','','',''); // 加载省份城市和县
	}

</script>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理&gt;轿车售后索赔报表&gt;轿车公司服务中心月申报费用明细表</div>

  <form method="post" name="fm" id="fm">
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
   <!-- 查询条件 begin -->
  <table class="table_query">
 		<tr>
            <td width="7%" align="right" nowrap="nowrap">所说大区：</td>
            <td colspan="6">
            	<select class="short_sel" name="areaName">
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
            	<input class="middle_txt" id="dealerCode" name="dealerCode" value="" type="text"/>
            	<input class="mark_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCode','','true','','true')"/>   
            	<input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/> 
            </td>
          </tr>               
          <tr>
            <td align="right" nowrap="nowrap">起止时间： </td>
            <td colspan="6" nowrap="nowrap">
            	<div align="left">
            		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            		&nbsp;至&nbsp;
            		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            	</div>
			</td>
			<td align="right" nowrap="nowrap">省份：</td>
            <td colspan="2" align="left">
				<select class="short_sel" id="province" name="province"></select>
			</td>
          </tr>
          <tr>
            <td align="right" nowrap="nowrap">车系：</td>
            <td colspan="6" nowrap="nowrap">
            	<select class="short_sel" name="seriesName">
            		<option value="">-请选择-</option>
	            	<c:if test="${seriesList!=null}">
						<c:forEach items="${seriesList}" var="list3">
							<option value="${list3.GROUP_ID}">${list3.GROUP_NAME}</option>
						</c:forEach>
					</c:if>
				</select>   	
            </td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr> 
          <tr>
            <td align="right" nowrap="nowrap" >&nbsp;</td>
            <td colspan="6" nowrap="nowrap">&nbsp;</td>
            <td align="left" nowrap="nowrap">
            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询"  onclick="__extQuery__(1)"/>&nbsp;
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

<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
//查询路径
	var url = "<%=contextPath%>/report/jcafterservicereport/ServiceCenterMonthlyReport/queryServiceCenterMonthlyReport.json";
				
	var title = null;

	var columns = [
				{header: "序号", align:'center',renderer:getIndex},
				{header: "服务商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "服务商名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "车系", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "一级服务商", dataIndex: 'DEALERNAME', align:'center'},
				{header: "所属大区", dataIndex: 'ORG_NAME', align:'center'},
				{header: "省份", dataIndex: 'REGION_NAME', align:'center'},
				{header: "售后维修工时费", dataIndex: 'SHGS', align:'center'},
				{header: "售后维修材料费", dataIndex: 'SHCL', align:'center'},
				{header: "售后维修单据数", dataIndex: 'SHDS', align:'center'},
				{header: "售前维修工时费", dataIndex: 'SQGS', align:'center'},
				{header: "售前维修材料费", dataIndex: 'SQCL', align:'center'},
				{header: "售前维修单据数", dataIndex: 'SQDS', align:'center'},
				{header: "售后外出费用", dataIndex: 'SHWC', align:'center'},
				/***add xiongchuan 20110217 售前外出费用改为在特殊费用统计报表中展示***/
//				{header: "售前外出费用", dataIndex: 'SQWC', align:'center'},
				/***add xiongchuan 20110217 售前外出费用改为在特殊费用统计报表中展示***/
				{header: "保养费", dataIndex: 'BYF', align:'center'},
				{header: "保养单数", dataIndex: 'BYDS', align:'center'},
				{header: "服务活动费",sortable: false,dataIndex: 'FWHD' ,align:'center'},
				{header: "服务活动单数", dataIndex: 'FWHDDS', align:'center'}
		      ];
		        
	function exportExcel()
	{
		fm.action = "<%=contextPath%>/report/jcafterservicereport/ServiceCenterMonthlyReport/exportToExcel.json";
		fm.submit();
	}
	
    //清空经销商框
	function clearInput(){
		var target = document.getElementById('dealerCode');
		target.value = '';
	}

</script>
<!--页面列表 end -->


</body>
</html>
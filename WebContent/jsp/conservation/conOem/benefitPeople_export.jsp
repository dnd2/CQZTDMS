<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/activityfunc.js"></script>
<% String contextPath = request.getContextPath(); %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<TITLE>节能惠民报批数据导出</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="../../js/jslib/calendar.js"></script>
</HEAD>
<BODY onload="javascript:loadcalendar();">
<div class="navigation"><img src="../../img/nav.gif" />&nbsp;当前位置： 节能惠民管理

&nbsp;&gt;&nbsp;节能惠民报批数据导出</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align=center width="100%">
    <tr>
	  <td align="right" width="20%">节能惠民单号：</td>
		<td valign="top" align="left" width="15%"><input type="text" name="energyNo" id="energyNo" 

class="middle_txt" value="" /></td>
      <td align="right" nowrap width="20%">&nbsp;上报日期：</td>
      <td align="left" nowrap width="15%">
	  <input name="reportStartDate" type="text" id="reportStartDate"  class="short_txt" readonly  

value='' />
      <input class="time_ico" value=" " type="button" onClick="showcalendar(event, 'reportStartDate', false);" />
	  至
	  <input name="reportEndDate" type="text" id="reportEndDate"  class="short_txt" readonly  

value='' />
      <input class="time_ico" value=" " type="button" onClick="showcalendar(event, 'reportEndDate', false);" />
	  </td>
	  <td align="right" width="30%" nowrap></td>
    </tr>
	<tr>
	  <td align="right" width="20%">&nbsp;大区：</td>
	  <td align="left" width="15%">
		<select name="orgId">
					<option selelected="selected" value="">-请选择-</option>
					<c:forEach items="${orgIdList}" var="org">
						<option value="${org.ORG_ID}"><c:out value="${org.ORG_NAME}"/></option>
					</c:forEach>
				</select>
	  </td>
	  <td align="right" width="20%">&nbsp;所属业务范围：</td>
	  <td align="left" width="15%">
		<select name="areaId" class="min_sel">
				<option selected="selected" value="">--请选择--</option>
				<c:if test="${!empty areaBusList}">
					<c:forEach items="${areaBusList}" var="list">
						<option value="${list.AREA_ID}"><c:out value="${list.AREA_NAME}"/></option>
					</c:forEach>
				</c:if>
			</select>
	  </td>
	  <td align="right" nowrap></td>
	</tr>
	<tr>
		<td align="right" width="20%">&nbsp;省份：</td>
		<td align="left" width="15%">
			<select class="min_sel" id="dPro" name="dPro" onchange="_genCity(this,'dCity')"></select>
		</td>
		<td align="right" width="20%">城市：</td>
		<td align="left" width="15%"><select class="min_sel" id="dCity" name="dCity" ></select></td>
		<td align="right" nowrap></td>
	</tr>
	<tr>
		<td align="right" nowrap>&nbsp;经销商：</td>
		<td align="left" nowrap>
			<input type="text" name="dealerCode" id="dealerCode" class="middle_txt" 

readonly />
			<input type="button" name="selDlr" id="selDlr" class="mini_btn" onclick="showOrgDealer('dealerCode','','true');"

value="..." />
			<input type="button" name="clearDlr" id="clearDlr" class="normal_btn" onclick="txtClr('dealerCode');"

value="清 除" />
		</td>
		<td align="right" nowrap>&nbsp;车型：</td>
		<td align="left" nowrap>
			<input type="text" name="model" id="model" class="middle_txt" readonly />
			<input type="button" name="selModel" id="selModel" class="mini_btn" onclick="showMaterialGroup('model','','true','');"

value="..." />
			<input type="button" name="clearModel" id="clearModel" class="normal_btn" 

value="清 除" onclick="txtClr('model');"/>
		</td>
		<td align="right" nowrap></td>
	</tr>
    <tr>
		<td align="right" nowrap>&nbsp;VIN：</td>
		<td>
			<textarea name="vin" id="vin" rows="3" cols="18"></textarea>
		</td>
		<td valign="top" align="right" width="20%">是否导出：</td>
		<td valign="top" align="left" width="15%">
			<select name="isEx">
				<option value="0">否</option>
				<option value="1">是</option>
			</select>
		</td>
		<td align="center" nowrap></td>
    </tr>
	<tr>
		<td align="right" nowrap></td>
		<td align="left" nowrap></td>
		<td align="right" nowrap></td>
		<td align="left" nowrap></td>
		<td align="center" nowrap>
			<input type="button" name="queryBtn" class="normal_btn" 

onclick="__extQuery__(1);" value="查询" />&nbsp;
			<input type="button" name="exportBtn" class="normal_btn" onclick="getMyDownLoadConfirm();" value="导出" />
		</td>
    </tr>
  </TABLE>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

<script type="text/javascript" > 
	var myPage;
	var url = "<%=contextPath%>/conservation/EnergyConservationOem/vehicleEnergyConExportQuery.json?COMMAND=1";
				
	var title = null;

	var columns = [
				{header: "序号", renderer:getIndex, align:'center'},
				{header: "消费者名称", dataIndex: 'CTM_NAME', align:'center'},
				{header: "联系人", dataIndex: 'CTM_LINKMAN', align:'center'},
				{header: "联系电话", dataIndex: 'CTM_LINKTEL', align:'center'},
				{header: "销售日期", dataIndex: 'SALES_DATE', align:'center'},
				{header: "车型代码", dataIndex: 'MODELCODE', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "车系", dataIndex: 'SERIESNAME', align:'center'},
				//{header: "节能惠民单号", dataIndex: 'CONSERVATION_NO', align:'center'},
				{header: "经销商代码", dataIndex: 'DEALERCODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALERNAME', align:'center'},
				{header: "上报日期", dataIndex: 'CREATE_DATE', align:'center'}
				
				//{header: "节能惠民单号", dataIndex: 'CONSERVATION_NO', align:'center'},
				//{header: "上报日期", dataIndex: 'CREATE_DATE', align:'center'},
				//{header: "经销商代码", dataIndex: 'DEALERCODE', align:'center'},
				//{header: "经销商名称", dataIndex: 'DEALERNAME', align:'center'},
				//{header: "车型代码", dataIndex: 'MODELCODE', align:'center'},
				//{header: "车型", dataIndex: 'MODELNAME', align:'center'},
				//{header: "消费者名称", dataIndex: 'CTM_NAME', align:'center'},
				//{header: "联系人", dataIndex: 'CTM_LINKMAN', align:'center'},
				//{header: "联系电话", dataIndex: 'CTM_LINKTEL', align:'center'},
				//{header: "VIN", dataIndex: 'VIN', align:'center'},
				//{header: "销售日期", dataIndex: 'SALES_DATE', align:'center'}
		      ];
	function getMyDownLoadConfirm(){
		MyConfirm("导出后将进入外部审核环节，是否导出？",getMyDownLoad);
	}

	function getMyDownLoad(){
			
			//下载
		//var downLoadUrl = "<%=contextPath%>/conservation/EnergyConservationOem/vehicleEnergyConExportDoXLS.json";
		//var energyNo = document.getElementById('energyNo').value; 
		//var reportStartDate = document.getElementById('reportStartDate').value;
		//var reportEndDate = document.getElementById('reportEndDate').value;
		//var orgId = document.getElementById('orgId').value;
		//var areaId = document.getElementById('areaId').value;
		//var dPro = document.getElementById('dPro').value;
		//var dCity = document.getElementById('dCity').value;
		//var dealerCode = document.getElementById('dealerCode').value;
		//var model = document.getElementById('model').value;
		//var vin = document.getElementById('vin').value;
		//if(startTime!=''||endTime!=''){
		//	if(!checkTime('reportStartDate','reportEndDate')){
		//		return false;
		//	}
		//}
		
		//location.href = downLoadUrl+"?energyNo="+energyNo+"&reportStartDate="+reportStartDate+"&reportEndDate="+reportEndDate+
		//"&orgId="+orgId+"&areaId="+areaId+"&dPro="+dPro+"&dCity="+dCity+"&dealerCode="+dealerCode+"&model="+model+"&vin="+vin;
						
		fm.action = "<%=contextPath%>/conservation/EnergyConservationOem/vehicleEnergyConExportDoXLS.json";
		fm.submit();
	}
		//验证时间
	function checkTime(startTime,endTime){
		if(trim($(startTime).value)!=""&&trim($(endTime).value)!=""){
			if(trim($(startTime).value) > trim($(endTime).value))
			{
				MyAlert('创建开始时间不能晚于结束时间');
				return false;
		 	}
		}
		return true;
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
	genLocSel('dPro','dCity','','','',''); // 加载省份城市和县
	_setDate_("reportStartDate", "reportEndDate", "1", "0") ;
</SCRIPT>


</BODY>
</html>

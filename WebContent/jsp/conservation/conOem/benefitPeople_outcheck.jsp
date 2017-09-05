<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/activityfunc.js"></script>
<% String contextPath = request.getContextPath(); %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<TITLE>节能惠民信息审核</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</HEAD>
<BODY onload="javascript:loadcalendar();">
<div class="navigation"><img src="../../img/nav.gif" />&nbsp;当前位置： 节能惠民管理

&nbsp;&gt;&nbsp;节能惠民信息审核</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align=center width="100%">
    <tr>
      <td align="right" nowrap width="20%">&nbsp;销售日期：</td>
      <td align="left" nowrap width="15%">
	  <input name="saleStartDate" type="text" id="saleStartDate"  class="short_txt" readonly  

value='' />
      <input class="time_ico" value=" " type="button" onClick="showcalendar(event, 'saleStartDate', false);" />至
       <input name="saleEndDate" type="text" id="saleEndDate"  class="short_txt" readonly  

value='' />
      <input class="time_ico" value=" " type="button" onClick="showcalendar(event, 'saleEndDate', false);" />
	  </td>
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
			<input type="button" name="clearModel" id="clearModel" class="normal_btn" onclick="txtClr('model');"

value="清 除" />
		</td>
		<td align="right" nowrap></td>
	</tr>
	<tr>
		<td align="right" nowrap>&nbsp;大区：</td>
		<td>
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
		<td align="right" nowrap>&nbsp;VIN：</td>
		<td>
			<textarea name="vin" id="vin" rows="3" cols="18"></textarea>
		</td>
		<td align="right" width="20%">导出版本号：</td>
		<td align="left" width="">
			<input type="text" class="middle_txt" name="expVer" id="expVer" />	 <font color="gray">若导出日期为2011年11月22日，则可模糊查询20111122</font>
		</td>
		<!--<td align="left" width="15%">
			<select id="expVer" name='expVer'>
				<option selected="selected" value="">-请选择-</option>
				<c:if test="${!empty listExpVer}">
					<c:forEach items="${listExpVer}" var="expVer">
						<option value="${expVer.EXPORT_FLAG }">${expVer.EXPORT_FLAG }</option>
					</c:forEach>
				</c:if>
			</select>
		</td>
		--><td align="right" nowrap></td>
	</tr>
    <tr>
		<td align="right" width="20%">节能惠民单号：</td>
		<td align="left" width="15%"><input type="text" name="energyNo" id="energyNo" 

class="middle_txt" value="" /></td>
		<td align="right" nowrap></td>
		<td align="left" nowrap></td>
		<td align="center" nowrap>
			<input type="button" name="queryBtn" class="normal_btn" onclick="__extQuery__(1);" value="查询" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" name="queryBtn" class="normal_btn" onclick="all_Path()" value="全部通过" />
		</td>
    </tr>
    
  </TABLE>
  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<table class="table_query" align=center width="100%" border='0'>
    <tr>
    
	  <th align="right" nowrap width="10%">审批备注：</th>
	  <th align="right" nowrap width="90%">&nbsp;</th>
	<tr>
	<td align="left" nowrap colspan="2">
     	<textarea name="remark" id="remark" rows="3" cols="140"></textarea>
	  </td>
	</tr>
	<tr>
		<td colspan="2" align="center" nowrap>
			<input type="button" value="通  过" class="normal_btn" onclick="batchAuditingConfirm(1);"/>
			<input type="button" value="驳  回" class="normal_btn" onclick="batchAuditingConfirm(2);"/>
		</td>
	</tr>
<script type="text/javascript" > 
	var myPage;
	var url = "<%=contextPath%>/conservation/EnergyConservationOem/vehicleEnergyConOutCheckQuery.json?COMMAND=1";
				
	var title = null;
	var controlCols = "<input type=\"checkbox\" name=\"conAll\" onclick=\"selectAll(this,'conservationId')\"/>全选&nbsp;&nbsp;";

	var columns = [
				{header: "序号", renderer:getIndex, align:'center'},
				{header: controlCols,align:'center',renderer:createCheckbox},
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
				{header: "上报日期", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "复核人", dataIndex: 'NAME', align:'center'},
				{header: "复核时间", dataIndex: 'UPDATE_DATE', align:'center'}
		      ];
	/*function myLink(value,meta,record){
		
		return String.format("<a href=\"#\" onclick='checkDetailUrl(\""+record.data.CONSERVATION_ID+"\")'>[审核]</a>");
	} */  
	function checkDetailUrl(conservationId){
		window.location.href="<%=contextPath%>/conservation/EnergyConservationOem/vehicleEnergyConCheck.do?conservationId="+conservationId;
	}  
	//生成批量审核使用的选择框
	function createCheckbox(value,meta,record){
  	  		//var claimStatus = record.data.STATUS;
  	  		//var authcodeval = record.data.AUTH_CODE;
				resultStr = String.format("<input type=\"checkbox\" name=\"conservationId\" value=\""+record.data.CONSERVATION_ID+"\"/><input type=\"hidden\" name=\"allConservationId\" value=\""+record.data.CONSERVATION_ID+"\"/>");
		return resultStr;
	}
	 //批量审批
	function batchAuditingConfirm(flag){
		var selectArray = document.getElementsByName('conservationId');
		if(isSelectCheckBox(selectArray)){
			if(flag=='1'){
				MyConfirm("是否批量审核通过!",batchAuditing,[1]);
			}else{
				MyConfirm("是否批量审核驳回!",batchAuditing,[2]);
			}
			
		}else{
			MyAlert("请选择要审批的节能惠民申请单！");
		}
	}
	function batchAuditing(val)
	{
		makeNomalFormCall("<%=contextPath%>/conservation/EnergyConservationOem/vehicleEnergyConBatchFinalCheck.json?flag="+val,showAuditValue,'fm','queryBtn'); 
	}
	function all_Path() {
		MyConfirm("是否全部通过?",allPath,[]);
	}
	
	function allPath() {
		makeNomalFormCall("<%=contextPath%>/conservation/EnergyConservationOem/vehicleEnergyAllCheck.json?",showAuditValue,'fm','queryBtn'); 
	}
	
	function showAuditValue(json){
		if(json.msg=='01'){
			MyAlert("操作成功！");
			__extQuery__(1);
		}else{
			MyAlert("操作失败，请联系管理员！");
		}
		
	}
	//检测是否选择了特殊费用单
	function isSelectCheckBox(cbArray){
		if(cbArray!=null && cbArray.length>0){
			for(var i=0;i<cbArray.length;i++){
				if(cbArray[i].checked)
					return true;
			}
		}else{
			return false;
		}
		return false;
	}
		function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
		genLocSel('dPro','dCity','','','',''); // 加载省份城市和县
		_setDate_("reportStartDate", "reportEndDate", "1", "0") ;
</SCRIPT>
</BODY>
</html>

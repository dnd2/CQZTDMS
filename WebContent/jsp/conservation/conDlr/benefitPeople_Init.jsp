<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<TITLE>节能惠民信息上报</TITLE>
</HEAD>
<BODY onload="javascript:loadcalendar();">
<div class="navigation"><img src="../../img/nav.gif" />&nbsp;当前位置： 节能惠民管理&nbsp;&gt;&nbsp;节能惠民信息上报</div>
<form method="post" name="form1" id='fm' action="">
<pre><font color="red" face="宋体"><strong>
说明：
1、上报流程：经销商上报-销售部审核（通过或驳回）；
2、审核通过的不能再上报，驳回后才能重新上报；
<!-- 3、节能惠民车型：mini自动挡，其他车型不允许上报，请按照车型查询后进行上报 -->
</strong>
</font>
</pre>
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
	  <td align="right" width="30%" nowrap></td>
    </tr>
    <tr>
	<tr>
		<td valign="top" align="right" nowrap>&nbsp;车型：</td>
		<td valign="top" align="left" nowrap>
			<input type="text" name="model" id="model" class="middle_txt" readonly />
			<input type="button" name="selModel" id="selModel" class="mini_btn" onclick="showMaterialGroup('model','','true','');" value="..." />
			<input type="button" name="clearModel" id="clearModel" class="normal_btn" value="清 除" onclick="txtClr('model');"/>
		</td>
		<td align="right" nowrap>&nbsp;VIN：</td>
		<td>
			<textarea name="vin" id="vin" rows="3" cols="18"></textarea>
		</td>
		<td align="right" nowrap></td>
	</tr>
    <tr>
		<td align="right" nowrap></td>
		<td align="left" nowrap></td>
		<td align="right" nowrap></td>
		<td align="left" nowrap></td>
		<td align="center" nowrap>
			<input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="__extQuery__(1);" />
			
		</td>
    </tr>
  </TABLE>
   <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form><br>
<form id="form2" name="form2">
<table class="table_query" width="90%" border="0" align="center">
	<tr>
		<td align="center">
			<input name="button" type="button" class="normal_btn" onclick="checkSubmit() ;" value="批量申请" /> 
		</td>
	</tr>
</table>
</form>

<script type="text/javascript" > 
	var myPage;
	var url = "<%=contextPath%>/conservation/EnergyConservationDlr/vehicleEnergyConApply.json?COMMAND=1";
				
	var title = null;

	var columns = [
				{id:'check',header: "选择<input type='checkbox' name='checkAll' onclick='selectAll(this,\"vehicleIds\");' />", width:'6%',sortable: false,dataIndex: 'VEHICLE_ID',renderer:myCheckBox},
				{header: "车型代码", dataIndex: 'MODELCODE', align:'center'},
				{header: "车型", dataIndex: 'MODELNAME', align:'center'},
				{header: "消费者名称", dataIndex: 'CTM_NAME', align:'center'},
				{header: "联系电话", dataIndex: 'MAIN_PHONE', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "销售日期", dataIndex: 'PURCHASED_DATE', align:'center'},
				{header: "操作",sortable: false,dataIndex: 'VEHICLE_ID',renderer:myLink}
		      ];
	
	document.form2.style.display = "none";
	
	var HIDDEN_ARRAY_IDS=['form2'];
	
	function myLink(value,meta,record){
		return String.format("<a href=\"#\" onclick='reportUrl(\""+record.data.VEHICLE_ID+"\")'>[上报]</a>");
	}   
	
	function reportUrl(vehicleId){
		window.location.href="<%=contextPath%>/conservation/EnergyConservationDlr/vehicleEnergyConReport.do?vehicleId="+vehicleId;;
	}  
			function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
			
	function myCheckBox(value,metaDate,record){
		return String.format("<input type=\"checkbox\" id='vehicleIds' name='vehicleIds' value='" + value + "' />");
	}
	
	function checkSubmit() {
		var vehicleIds = document.getElementsByName("vehicleIds");
		var addFlag = false;
		
		for(var i=0; i<vehicleIds.length; i++){
			if(vehicleIds[i].checked){
				addFlag = true;
				break;
			}
		}
		
		if(addFlag){
			MyConfirm("是否提交?",submitAction);
		}else{
			MyAlert("请选择申请信息!");
		}
	} 
	
	function submitAction() {
		var url = "<%=contextPath%>/conservation/EnergyConservationDlr/vehicleMuchReport.json";
		
		makeNomalFormCall(url, showResult, 'fm') ;
	}
	
	function showResult(json) {
		var jsonFlag = json.flag ;
		
		if(jsonFlag == "success") {
			MyAlert("操作成功!") ;
		} else if(jsonFlag == "noData") {
			MyAlert("无可处理数据!") ;
		}
	}


</SCRIPT>


</BODY>
</html>

<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@ page import="com.infodms.dms.po.TtAsBarcodeApplyPO"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>市场质量信息申报审核</title>
<% String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
	//TtAsBarcodeApplyPO bean = (TtAsBarcodeApplyPO)request.getAttribute("bean");
	%>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
   		doCusChange();
	}

</script>
<body ">
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;质量信息申报
</div>
<div align="center">市场质量信息报表</div>
<form name="fm" id="fm">
	<input type="hidden" id="enginTypesH" name="enginTypesH">
	<input type="hidden" id="doorsH" name="doorsH">
	<input type="hidden" id="driveTypesH" name="driveTypesH">
	<input type="hidden" id="equipmentsH" name="equipmentsH">
	<input type="hidden" id="purposesH" name="purposesH">
	<input type="hidden" id="roadsH" name="roadsH">
	<input type="hidden" id="temperatureAndTimesH" name="temperatureAndTimesH">
	<input type="hidden" id="happendTimesH" name="happendTimesH">
	<input type="hidden" id="speedsH" name="speedsH">
	<input type="hidden" id="rainsH" name="rainsH">
	<input type="hidden" id="usedsH" name="usedsH">
	<input type="hidden" id="airConditionStatussH" name="airConditionStatussH">
	<input type="hidden" id="importantLevelsH" name="importantLevelsH">
	<input type="hidden" id="VIN" name="VIN" value="${ttSalesQualityInfoReportPO.vin}"/>
	<input type="hidden" id="modelId" name="modelId" value="${ttSalesQualityInfoReportPO.modelId}">
	<input type="hidden" id="qualiteReportId" name="qualiteReportId" value="${ttSalesQualityInfoReportPO.qualityReportId}">
	
	<table class="tab_edit" width="100%" align="center">
		<tr>
			<td width="12%" align="center">经销商代码</td>
			<td width="38%"><input name="dealerCode" id="dealerCode" value="${ttSalesQualityInfoReportPO.dealerCode}" type="text" class="long_txt" readonly="readonly"/></td>
			<td width="12%" align="center">经销商名称</td>
			<td width="38%"><input name="dealerName" id="dealerName" value="${ttSalesQualityInfoReportPO.dealerName}" type="text" class="long_txt" readonly="readonly"/></td>
		</tr>
		<tr>
			<td align="center">填报人</td>
			<td><input name="reportName" id="reportName" value="${ttSalesQualityInfoReportPO.reportName}" type="text" class="long_txt" readonly="readonly"/></td>
			<td align="center">联系电话</td>
			<td><input name="phone" id="phone" type="text" value="${ttSalesQualityInfoReportPO.phone}" class="long_txt" readonly="readonly"//></td>
		</tr>
		<tr>
			<td align="center">车型</td>
			<td><input name="modelName" id="modelName" value="${ttSalesQualityInfoReportPO.modelName}" type="text" class="long_txt" readonly="readonly"/></td>
			<td align="center">索赔单号</td>
			<td><input name="claimNo" id="claimNo" type="text" readonly="readonly" value="${ttSalesQualityInfoReportPO.claimNo}" class="long_txt" /><input type="button" value="..." class="mini_btn" onclick="selectPartFirst();"></td>
		</tr>
		<tr>
			<td align="center">行驶里程</td>
			<td><input name="mileage" id="mileage" value="${ttSalesQualityInfoReportPO.mileage}" type="text" class="long_txt" readonly="readonly"/>KM</td>
			<td align="center">发动机号</td>
			<td><input name="engineNo" id="engineNo" value="${ttSalesQualityInfoReportPO.engineNo}" type="text" class="long_txt" readonly="readonly"/></td>
		</tr>
		<tr>
			<td align="center">出厂日期</td>
			<td>
				<input name="productDate" id="productDate" value="<fmt:formatDate  value='${ttSalesQualityInfoReportPO.productDate}'  type="both" pattern="yyyy-MM-dd HH:mm:ss" />" type="text" class="long_txt" readonly="readonly"/>
		    </td>     
			<td align="center">购买日期</td>
			<td>
				<input name="purchasedDate" id="purchasedDate" value="<fmt:formatDate  value='${ttSalesQualityInfoReportPO.purchasedDate}'  type="both" pattern="yyyy-MM-dd HH:mm:ss" />" type="text" class="long_txt" readonly="readonly"/>
			</td>
		</tr>
		<tr>
			<td align="center">故障名称</td>
			<td>
				<input name="faultName" id="faultName" value="${ttSalesQualityInfoReportPO.faultName}" type="text" class="long_txt" readonly="readonly"/>
		    </td>     
			<td align="center">故障日期</td>
			<td >
			<input name="faultDate" id="faultDate" value="<fmt:formatDate  value='${ttSalesQualityInfoReportPO.faultDate}'  type="both" pattern="yyyy-MM-dd" />" type="text" class="long_txt" readonly="readonly"/>
			</td>
		</tr>
		<tr>
			<td align="center">用户姓名</td>
			<td><input name="ctmName" id="ctmName" value="${ttSalesQualityInfoReportPO.ctmName}" type="text" class="long_txt" readonly="readonly"/></td>
			<td align="center">联系电话</td>
			<td><input name="ctmPhone" id="ctmPhone" value="${ttSalesQualityInfoReportPO.ctmPhone}" type="text" class="long_txt" readonly="readonly"/></td>
		</tr>
		<tr>
			<td align="center">发动机类型</td>
			<td>
				<c:forEach var="engineType" items="${engineTypeList}">
					<c:choose>
						<c:when test="${engineType.isCheck}">
							<input type="checkbox" name="enginTypes" value="${engineType.value}" checked="checked" readonly="readonly" disabled="disabled">${engineType.key}
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="enginTypes" value="${engineType.value}" readonly="readonly" disabled="disabled">${engineType.key}
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</td>
			<td align="center">车身</td>
			<td>
				<c:forEach var="door" items="${doorList}">
					<c:choose>
						<c:when test="${door.isCheck}">
							<input type="checkbox" name="doors" value="${door.value}" checked="checked" readonly="readonly" disabled="disabled">${door.key}
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="doors" value="${door.value}" readonly="readonly" disabled="disabled">${door.key}
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</td>
		</tr>
		<tr>
			<td align="center">驱动方式</td>
			<td>
				<c:forEach var="driveType" items="${driveTypeList}">
					<c:choose>
						<c:when test="${driveType.isCheck}">
							<input type="checkbox" name="driveTypes" value="${driveType.value}" checked="checked" readonly="readonly" disabled="disabled">${driveType.key}
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="driveTypes" value="${driveType.value}" readonly="readonly" disabled="disabled">${driveType.key}
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</td>
			<td align="center">装备</td>
			<td>
				<c:forEach var="equipment" items="${equipmentList}" varStatus="equipmentStatus">
					<c:choose>
						<c:when test="${equipment.isCheck}">
							<input type="checkbox" name="equipments" value="${equipment.value}" checked="checked" readonly="readonly" disabled="disabled">${equipment.key}
							<c:if test="${equipmentStatus.count%4 == 0}"><br></c:if>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="equipments" value="${equipment.value}" readonly="readonly" disabled="disabled">${equipment.key}
							<c:if test="${equipmentStatus.count%4 == 0}"><br></c:if>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</td>
		</tr>					
	</table>
	<br/>
	<table class="tab_edit" width="100%" align="center">
		<tr>
			<td width="3%" align="center" rowspan="5">问<br>题<br>描<br>述<br>及<br>状<br>态</td>
			<td width="97%" colspan="6">
				<table width="100%" class="tab_edit" border="0">
					<tr>
						<td width="80%">条件及现象　（时间/地点/状况）</td>
						<td width="3%" rowspan="2" align="center">用途</td>
						<td width="17%" rowspan="2">
							<c:forEach var="purpose" items="${purposeList}" varStatus="purposeStatus">
								<c:choose>
									<c:when test="${purpose.isCheck}">
										<input type="checkbox" name="purposes" value="${purpose.value}" checked="checked" readonly="readonly" disabled="disabled">${purpose.key}
										<c:if test="${purposeStatus.count%2 == 0}"><br></c:if>
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="purposes" value="${purpose.value}" readonly="readonly" disabled="disabled">${purpose.key}
										<c:if test="${purposeStatus.count%2 == 0}"><br></c:if>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<input value="${otherValue}" style="width: 56px;
		                        border-top-style: none; border-right-style: none; border-left-style: none; height: 20px;
		                        background-color: transparent; border-bottom-style: groove" size="4" name="otherValue"
		                        id="otherValue" readonly="readonly"
		                        >
						</td>
					</tr>
					<tr>
						<td width="80%">
							<textarea rows="4" style="width: 98%" id="condition" name="condition" readonly="readonly" >${ttSalesQualityInfoReportPO.condition}</textarea>
						</td>
					</tr>
				</table>
			</td>

		</tr>
		<tr>
			<td width="16%" align="center">道路状况</td>
			<td width="12%" align="center">温度/时间</td>
			<td width="35%" colspan="2" align="center">发生时机/速度</td>
			<td width="16%" align="center">雨水状况</td>
			<td width="16%" align="center">平时使用状况</td>
		</tr>
		<tr>
			<td rowspan="3" align="left">
				<c:forEach var="road" items="${roadList}" varStatus="roadStatus">
					<c:choose>
						<c:when test="${road.isCheck}">
							<input type="checkbox" name="roads" value="${road.value}" checked="checked" readonly="readonly" disabled="disabled">${road.key}
							<c:if test="${roadStatus.count%2 == 0}"><br></c:if>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="roads" value="${road.value}" readonly="readonly" disabled="disabled">${road.key}
							<c:if test="${roadStatus.count%2 == 0}"><br></c:if>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</td>
			<td rowspan="3" align="left">
				<c:forEach var="temperatureAndTime" items="${temperatureAndTimeList}">
					<c:choose>
						<c:when test="${temperatureAndTime.isCheck}">
							<input type="checkbox" name="temperatureAndTimes" value="${temperatureAndTime.value}" checked="checked" readonly="readonly" disabled="disabled">${temperatureAndTime.key}
							<br>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="temperatureAndTimes" value="${temperatureAndTime.value}" readonly="readonly" disabled="disabled">${temperatureAndTime.key}
							<br>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</td>
			<td rowspan="3" align="left">
				<c:forEach var="happendTime" items="${happendTimeList}" varStatus="happendTimeStatus">
					<c:choose>
						<c:when test="${happendTime.isCheck}">
							<input type="checkbox" name="happendTimes" value="${happendTime.value}" checked="checked" readonly="readonly" disabled="disabled">${happendTime.key}
							<c:if test="${happendTimeStatus.count%2 == 0}"><br></c:if>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="happendTimes" value="${happendTime.value}" readonly="readonly" disabled="disabled">${happendTime.key}
							<c:if test="${happendTimeStatus.count%2 == 0}"><br></c:if>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</td>
			<td rowspan="3" align="left">
				<c:forEach var="speed" items="${speedList}">
					<c:choose>
						<c:when test="${speed.isCheck}">
							<input type="checkbox" name="speeds" value="${speed.value}" checked="checked" readonly="readonly" disabled="disabled">${speed.key}
							<br>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="speeds" value="${speed.value}" readonly="readonly" disabled="disabled">${speed.key}
							<br>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</td>
			<td align="left">
				<c:forEach var="rain" items="${rainList}">
					<c:choose>
						<c:when test="${rain.isCheck}">
							<input type="checkbox" name="rains" value="${rain.value}" checked="checked" readonly="readonly" disabled="disabled">${rain.key}
							<br>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="rains" value="${rain.value}" readonly="readonly" disabled="disabled">${rain.key}
							<br>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</td>
			<td rowspan="3" align="left">
				<c:forEach var="used" items="${usedList}">
					<c:choose>
						<c:when test="${used.isCheck}">
							<input type="checkbox" name="useds" value="${used.value}" checked="checked" readonly="readonly" disabled="disabled">${used.key}
							<br>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="useds" value="${used.value}" readonly="readonly" disabled="disabled">${used.key}
							<br>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</td>
		</tr>
		<tr>
			<td align="center">空调状态</td>
		</tr>
		<tr>
			<td align="center">
				<c:forEach var="airConditionStatus" items="${airConditionStatusList}">
					<c:choose>
						<c:when test="${airConditionStatus.isCheck}">
							<input type="checkbox" name="airConditionStatuss" value="${airConditionStatus.value}" checked="checked" readonly="readonly" disabled="disabled">${airConditionStatus.key}
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="airConditionStatuss" value="${airConditionStatus.value}" readonly="readonly" disabled="disabled">${airConditionStatus.key}
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</td>
		</tr>
		
		
		<tr>
			<td width="3%" align="center" rowspan="6">诊<br>断<br>结<br>果<br>及<br>处<br>理<br>方<br>法</td>
			<td colspan="6">状况 （检查结论）</td>
		</tr>
		<tr>
			<td colspan="6">
				<textarea rows="5" style="width: 98%" id="checkResult" name="checkResult" readonly="readonly">${ttSalesQualityInfoReportPO.checkResult}</textarea>
			</td>
		</tr>
		<tr><td colspan="6">处理方法（维修内容）</td></tr>
		<tr>
			<td colspan="6">
				<textarea rows="5" style="width: 98%" id="content" name="content" readonly="readonly">${ttSalesQualityInfoReportPO.content}</textarea>
			</td>
		</tr>
		<tr>
			<td rowspan="2" align="center">主因部件</td>
			<td align="center">配件名称</td>
			<td colspan="2" align="left">
				<input type="hidden" id="partId" name="partId" value="${ttSalesQualityInfoReportPO.partId}">
				<input type="text" id="partName" name="partName" value="${ttSalesQualityInfoReportPO.partName}" class="long_txt" readonly="readonly">
<!-- 				<input type="button" value="..." class="mini_btn" onclick="selectPart()"> -->
			</td>
			<!-- 艾春 9.12 修改 开始 -->
			<td rowspan="2" align="center">重要度判定</td>
			<td rowspan="2" align="left">
				<c:forEach var="importantLevel" items="${importantLevelList}">
					<c:choose>
						<c:when test="${importantLevel.isCheck}">
							<input type="checkbox" name="importantLevels" value="${importantLevel.value}" checked="checked">${importantLevel.key}
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="importantLevels" value="${importantLevel.value}">${importantLevel.key}
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</td>
			<!-- 艾春 9.12 修改 结束 -->
		</tr>
		<tr>
			<td align="center">责任单位</td>
			<td colspan="2" align="left">
				<select id="marker" name="marker" class="long_sel" disabled="disabled">
					<option value=''>-请选择-</option>
					<c:forEach var="mk" items="${markerList}">
						<c:choose>
							<c:when test="${mk.isCheck}">
								<option value='${mk.MAKER_ID}' selected="selected">${mk.MAKER_NAME}</option>
							</c:when>
							<c:otherwise>
								<option value='${mk.MAKER_ID}'>${mk.MAKER_NAME}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td colspan="7">补充说明</td>
		</tr>
		<tr>
			<td colspan="7">
				<textarea rows="5" style="width: 98%" id="remark" name="remark" readonly="readonly">${ttSalesQualityInfoReportPO.remark}</textarea>
			</td>
		</tr>
		<tr>
			<td colspan="7">审核意见</td>
		</tr>
		<tr>
			<td colspan="7">
				<textarea rows="5" style="width: 98%" id="qdApproval" name="qdApproval" >${ttSalesQualityInfoReportPO.qdApproval}</textarea>
			</td>
		</tr>
		<tr>
			<td colspan="7">驳回理由</td>
		</tr>
		<tr>
			<td colspan="7">
				<textarea rows="5" style="width: 98%" id="refuseReason" name="refuseReason" maxlength="150"></textarea>
			</td>
		</tr>
	</table>
	<br/>
	 <!-- 添加附件 开始  -->
        <table id="add_file"  width="100%" class="table_info" border="0" id="file">
				<input type="hidden" id="fjids" name="fjids"/>
	    		<tr>
	        		<th>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
						<font color="red">
							<span id="span1"></span>
						</font>
			     		<input type="button" class="normal_btn" align="right" id="addfile" onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
					</th>
				</tr>
				<tr>
    				<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  				</tr>
  					<%if(fileList!=null){for(int i=0;i<fileList.size();i++) { %>
	 					 <script type="text/javascript">
	 	 					 showUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>');
	 	 				</script>
					<%} }%>
			</table> 
  		<!-- 添加附件 结束 -->
		</br>

		<table width=100% border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="12" align=left width="33%">&nbsp;</td>
             	<td height="12" align=center width="33%">
                	<input type="button" class="normal_btn"   style="width=8%" value="通过 " onclick="verify('pass')"/>
                	<input type="button" class="normal_btn"   style="width=8%" value="驳回 " onclick="verify('refuse')"/>
                	&nbsp;
					<input type="button" onClick="history.back();" class="normal_btn"  style="width=8%" value="返回"/>
    			</td>
            	<td height="12" align=center width="33%">
      			</td>
			</tr>
		</table>
	</form>
</body>
</html>
<script type="text/javascript" >
	
	function verify(state){
		if(state == 'pass'){
			makeNomalFormCall('<%=contextPath%>/vehicleInfoManage/check/QualityInfoReportVerify/verifyQualityInfoReportBydeptSubmit.json?state='+state,verifyQualityInfoReportSubmitBack,'fm','');
		}else if(state == 'refuse' && check()){
			makeNomalFormCall('<%=contextPath%>/vehicleInfoManage/check/QualityInfoReportVerify/verifyQualityInfoReportBydeptSubmit.json?state='+state,verifyQualityInfoReportSubmitBack,'fm','');
		}
		
	}
	function check(){
		var msg ="";
		if(""==document.getElementById('refuseReason').value){
			msg+="驳回理由不能为空!</br>"
		}	
		
		if(msg!=""){
			MyAlert(msg);
			return false;
		}else{
			return true;
		}
	}
	
	function verifyQualityInfoReportSubmitBack(josn){
		if(josn.success != null && josn.success == 'true'){
			MyAlertForFun("审核成功!",sendPage);
		}else{
			MyAlert("审核失败,请联系管理员!");
		}
	}
	
	//页面跳转：
	function sendPage(){
		window.location.href = "<%=contextPath%>/vehicleInfoManage/check/QualityInfoReportVerify/qualityInfoReportVerifyByDeptinit.do";
	}
	


</script>

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
<title>市场质量信息申报</title>
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
			<td width="38%"><input name="dealerCode" id="dealerCode" value="${dealerdata.DEALER_CODE}" type="text" class="long_txt" readonly="readonly"/></td>
			<td width="12%" align="center">经销商名称</td>
			<td width="38%"><input name="dealerName" id="dealerName" value="${dealerdata.DEALER_NAME}" type="text" class="long_txt" readonly="readonly"/></td>
		</tr>
		<tr>
			<td align="center">填报人</td>
			<td><input name="reportName" id="reportName" value="${ttSalesQualityInfoReportPO.reportName}" maxlength="10" type="text" class="long_txt"/></td>
			<td align="center">联系电话</td>
			<td><input name="phone" id="phone" type="text" value="${ttSalesQualityInfoReportPO.phone}" maxlength="30" class="long_txt"/></td>
		</tr>
		<tr>
			<td align="center">车型</td>
			<td><input name="modelName" id="modelName" value="${ttSalesQualityInfoReportPO.modelName}" type="text" class="long_txt" readonly="readonly"/></td>
			<td align="center">索赔单号</td>
			<td><input name="claimNo" id="claimNo" type="text" readonly="readonly" value="${ttSalesQualityInfoReportPO.claimNo}" class="long_txt" /><input type="button" value="..." class="mini_btn" onclick="selectPartFirst();"></td>
			<!--<td align="center">VIN代码</td>onblur="queryDateFirst(this.value)"
			<td><input name="VIN" id="VIN" type="text" value="${ttSalesQualityInfoReportPO.vin}" class="long_txt" onblur="queryDate(this.value)"/></td>
			-->
		</tr>
		<tr>
			<td align="center">行驶里程</td>
			<td><input name="mileage" id="mileage" value='<fmt:parseNumber type="number">${ttSalesQualityInfoReportPO.mileage}</fmt:parseNumber>' datatype="1,is_double,8"  type="text" maxlength="8" class="long_txt"/>KM</td>
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
				<input name="faultName" id="faultName" value="${ttSalesQualityInfoReportPO.faultName}" type="text" class="long_txt"/>
		    </td>     
			<td align="center">故障日期</td>
			<td >
			<input type="text" name="faultDate" id="faultDate" readonly="readonly"
		             type="text" class="long_txt" 
		             datatype="1,is_date,10" 
		             value="<fmt:formatDate  value='${ttSalesQualityInfoReportPO.faultDate}'  type="both" pattern="yyyy-MM-dd" />"
		             hasbtn="true" callFunction="showcalendar(event, 'faultDate', false);" maxlength="10"/>	
			</td>
		</tr>
		<tr>
			<td align="center">用户姓名</td>
			<td><input name="ctmName" id="ctmName" value="${ttSalesQualityInfoReportPO.ctmName}" type="text" maxlength="30" class="long_txt"/></td>
			<td align="center">联系电话</td>
			<td><input name="ctmPhone" id="ctmPhone" value="${ttSalesQualityInfoReportPO.ctmPhone}" type="text" maxlength="30" class="long_txt"/></td>
		</tr>
		<tr>
			<td align="center">发动机类型</td>
			<td>
				<c:forEach var="engineType" items="${engineTypeList}">
					<c:choose>
						<c:when test="${engineType.isCheck}">
							<input type="checkbox" name="enginTypes" value="${engineType.value}" checked="checked">${engineType.key}
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="enginTypes" value="${engineType.value}">${engineType.key}
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</td>
			<td align="center">车身</td>
			<td>
				<c:forEach var="door" items="${doorList}">
					<c:choose>
						<c:when test="${door.isCheck}">
							<input type="checkbox" name="doors" value="${door.value}" checked="checked">${door.key}
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="doors" value="${door.value}">${door.key}
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
							<input type="checkbox" name="driveTypes" value="${driveType.value}" checked="checked">${driveType.key}
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="driveTypes" value="${driveType.value}">${driveType.key}
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</td>
			<td align="center">装备</td>
			<td>
				<c:forEach var="equipment" items="${equipmentList}" varStatus="equipmentStatus">
					<c:choose>
						<c:when test="${equipment.isCheck}">
							<input type="checkbox" name="equipments" value="${equipment.value}" checked="checked">${equipment.key}
							<c:if test="${equipmentStatus.count%4 == 0}"><br></c:if>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="equipments" value="${equipment.value}">${equipment.key}
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
										<input type="checkbox" name="purposes" value="${purpose.value}" checked="checked">${purpose.key}
										<c:if test="${purposeStatus.count%2 == 0}"><br></c:if>
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="purposes" value="${purpose.value}">${purpose.key}
										<c:if test="${purposeStatus.count%2 == 0}"><br></c:if>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<input value="${otherValue}" style="width: 56px;
		                        border-top-style: none; border-right-style: none; border-left-style: none; height: 20px;
		                        background-color: transparent; border-bottom-style: groove" size="4" name="otherValue"
		                        id="otherValue" maxlength="30"
		                        >
						</td>
					</tr>
					<tr>
						<td width="80%">
							<textarea rows="4" style="width: 98%" id="condition" name="condition" maxlength="150">${ttSalesQualityInfoReportPO.condition}</textarea>
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
							<input type="checkbox" name="roads" value="${road.value}" checked="checked">${road.key}
							<c:if test="${roadStatus.count%2 == 0}"><br></c:if>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="roads" value="${road.value}">${road.key}
							<c:if test="${roadStatus.count%2 == 0}"><br></c:if>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</td>
			<td rowspan="3" align="left">
				<c:forEach var="temperatureAndTime" items="${temperatureAndTimeList}">
					<c:choose>
						<c:when test="${temperatureAndTime.isCheck}">
							<input type="checkbox" name="temperatureAndTimes" value="${temperatureAndTime.value}" checked="checked">${temperatureAndTime.key}
							<br>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="temperatureAndTimes" value="${temperatureAndTime.value}">${temperatureAndTime.key}
							<br>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</td>
			<td rowspan="3" align="left">
				<c:forEach var="happendTime" items="${happendTimeList}" varStatus="happendTimeStatus">
					<c:choose>
						<c:when test="${happendTime.isCheck}">
							<input type="checkbox" name="happendTimes" value="${happendTime.value}" checked="checked">${happendTime.key}
							<c:if test="${happendTimeStatus.count%2 == 0}"><br></c:if>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="happendTimes" value="${happendTime.value}">${happendTime.key}
							<c:if test="${happendTimeStatus.count%2 == 0}"><br></c:if>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</td>
			<td rowspan="3" align="left">
				<c:forEach var="speed" items="${speedList}">
					<c:choose>
						<c:when test="${speed.isCheck}">
							<input type="checkbox" name="speeds" value="${speed.value}" checked="checked">${speed.key}
							<br>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="speeds" value="${speed.value}">${speed.key}
							<br>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</td>
			<td align="left">
				<c:forEach var="rain" items="${rainList}">
					<c:choose>
						<c:when test="${rain.isCheck}">
							<input type="checkbox" name="rains" value="${rain.value}" checked="checked">${rain.key}
							<br>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="rains" value="${rain.value}">${rain.key}
							<br>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</td>
			<td rowspan="3" align="left">
				<c:forEach var="used" items="${usedList}">
					<c:choose>
						<c:when test="${used.isCheck}">
							<input type="checkbox" name="useds" value="${used.value}" checked="checked">${used.key}
							<br>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="useds" value="${used.value}">${used.key}
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
							<input type="checkbox" name="airConditionStatuss" value="${airConditionStatus.value}" checked="checked">${airConditionStatus.key}
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="airConditionStatuss" value="${airConditionStatus.value}">${airConditionStatus.key}
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
				<textarea rows="5" style="width: 98%" id="checkResult" name="checkResult" maxlength="150">${ttSalesQualityInfoReportPO.checkResult}</textarea>
			</td>
		</tr>
		<tr><td colspan="6">处理方法（维修内容）</td></tr>
		<tr>
			<td colspan="6">
				<textarea rows="5" style="width: 98%" id="content" name="content" maxlength="150">${ttSalesQualityInfoReportPO.content}</textarea>
			</td>
		</tr>
		<tr>
			<td rowspan="2" align="center">主因部件</td>
			<td align="center">配件名称</td>
			<td colspan="2" align="left">
				<input type="hidden" id="partId" name="partId" value="${ttSalesQualityInfoReportPO.partId}">
				<input type="text" id="partName" name="partName" value="${ttSalesQualityInfoReportPO.partName}" readonly="readonly" class="long_txt">
				<input type="button" value="..." class="mini_btn" onclick="selectPart()">
			</td>
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
		</tr>
		<tr>
			<td align="center">责任单位</td>
			<td colspan="2" align="left">
				<select id="marker" name="marker" class="long_sel">
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
				<textarea rows="5" style="width: 98%" id="remark" name="remark" maxlength="150">${ttSalesQualityInfoReportPO.remark}</textarea>
			</td>
		</tr>
		<c:if test="${ttSalesQualityInfoReportPO.verifyStatus == 95531004}">
		<tr>
			<td colspan="7">驳回理由</td>
		</tr>
		<tr>
			<td colspan="7">
				<textarea rows="5" style="width: 98%" id="refuseReason" name="refuseReason" readonly="readonly">${ttSalesQualityInfoReportPO.refuseReason}</textarea>
			</td>
		</tr>
		</c:if>
		<c:if test="${ttSalesQualityInfoReportPO.verifyStatus == 95531005}">
		<tr>
			<td colspan="7">审核意见</td>
		</tr>
		<tr>
			<td colspan="7">
				<textarea rows="5" style="width: 98%" id="refuseReason" name="refuseReason" readonly="readonly">${ttSalesQualityInfoReportPO.qdApproval}</textarea>
			</td>
		</tr>
		</c:if>
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
			     		<input type="button" class="normal_btn" align="right" id="addfile" onclick="showUpload('<%=contextPath%>')" value ='添加附件'/><font color="red">*</font>
					</th>
				</tr>
				<tr>
    				<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  				</tr>
  					<%if(fileList!=null){for(int i=0;i<fileList.size();i++) { %>
	 					 <script type="text/javascript">
	 	 					 addUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>');
	 	 				</script>
					<%} }%>
			</table> 
  		<!-- 添加附件 结束 -->
		</br>
		<table width=100% border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="12" align=left width="33%">&nbsp;</td>
             	<td height="12" align=center width="33%">
                	<input type="button" class="normal_btn" id="saveBtn"  style="width=8%" value="保存 " onclick="save()"/>
                	&nbsp;
                	<c:if test="${empty ttSalesQualityInfoReportPO || ttSalesQualityInfoReportPO.verifyStatus == 95531001 || ttSalesQualityInfoReportPO.verifyStatus == 95531006}">
                		<input type="button" class="normal_btn" id="saveBtn"  style="width=8%" value="申报 " onclick="apply()"/>
                	</c:if>
                	
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
	function save(){
		if(check()){
			//document.getElementById("saveBtn").disabled = true;
			setHiddenCheckbox();

			makeNomalFormCall('<%=contextPath%>/vehicleInfoManage/apply/QualityInfoReport/updateQualityInfoReportSubmit.json',updateQualityInfoReportSubmitBack,'fm','');
		}
	}
	
	function apply(){
		if(check()){
			setHiddenCheckbox();
			makeNomalFormCall('<%=contextPath%>/vehicleInfoManage/apply/QualityInfoReport/applyQualityInfoReportSubmit.json',applyQualityInfoReportSubmitBack,'fm','');
		}		
	}
	
	function updateQualityInfoReportSubmitBack(josn){
		if(josn.success != null && josn.success == 'addtrue'){
			setTextData('qualiteReportId',josn.qualiteReportId,false);
			MyAlert("新增成功!");
		}else if(josn.success != null && josn.success == 'updatetrue'){
			MyAlert("修改成功!");
		}else{
			MyAlert("申报失败,请联系管理员!");
		}
	}
	
	function applyQualityInfoReportSubmitBack(josn){
		if(josn.success != null && josn.success == 'true'){
			MyAlertForFun("申报成功!",sendPage);
		}else{
			MyAlert("申报失败,请联系管理员!");
		}
	}
	
	//页面跳转：
	function sendPage(){
		window.location.href = "<%=contextPath%>/vehicleInfoManage/apply/QualityInfoReport/qualityInfoReportInit.do";
	}
	
	
	function setHiddenCheckbox(){
		setTextData('enginTypesH',addCheckBoxIds('enginTypes'),false);
		setTextData('doorsH',addCheckBoxIds('doors'),false);
		setTextData('driveTypesH',addCheckBoxIds('driveTypes'),false);
		setTextData('equipmentsH',addCheckBoxIds('equipments'),false);
		setTextData('purposesH',addSpecialCheckBoxIds('purposes','其他','otherValue'),false);
		setTextData('roadsH',addCheckBoxIds('roads'),false);
		setTextData('temperatureAndTimesH',addCheckBoxIds('temperatureAndTimes'),false);
		setTextData('happendTimesH',addCheckBoxIds('happendTimes'),false);
		setTextData('speedsH',addCheckBoxIds('speeds'),false);
		setTextData('rainsH',addCheckBoxIds('rains'),false);
		setTextData('usedsH',addCheckBoxIds('useds'),false);
		setTextData('airConditionStatussH',addCheckBoxIds('airConditionStatuss'),false);
		setTextData('importantLevelsH',addCheckBoxIds('importantLevels'),false);
	}

	function check(){
		var msg ="";
		if(""==document.getElementById('reportName').value){
			msg+="填报人不能为空!</br>"
		}
		if(""==document.getElementById('phone').value){
			msg+="联系电话不能为空!</br>"
		}
		if(""==document.getElementById('VIN').value){
			msg+="VIN代码不能为空!</br>"
		}
		if(""==document.getElementById('modelId').value){
			msg+="车型不能为空!</br>"
		}
		if(""==document.getElementById('dealerCode').value){
			msg+="经销商代码不能为空!</br>"
		}
		if(""==document.getElementById('faultName').value){
			msg+="故障名称不能为空!</br>"
		}
		if(""==document.getElementById('faultDate').value){
			msg+="故障日期不能为空!</br>"
		}
		if(""==document.getElementById('ctmName').value){
			msg+="用户姓名不能为空!</br>"
		}
		if(""==document.getElementById('ctmPhone').value){
			msg+="联系电话不能为空!</br>"
		}
		
		if(""==document.getElementById('condition').value){
			msg+="条件及现象（时间/地点/状况）不能为空!</br>"
		}
		if(""==document.getElementById('checkResult').value){
			msg+="状况 （检查结论）不能为空!</br>"
		}
		if(""==document.getElementById('content').value){
			msg+="处理方法（维修内容）不能为空!</br>"
		}
		if(""==document.getElementById('partName').value){
			msg+="配件名称不能为空!</br>"
		}
		if(""==document.getElementById('marker').value){
			msg+="责任单位不能为空!</br>"
		}
		

		if(!checkCheckbox('enginTypes',false)){
			msg+="发动机类型必须选一!</br>"
		}
		if(!checkCheckbox('doors',false)){
			msg+="车身必须选一!</br>"
		}
		if(!checkCheckbox('driveTypes',false)){
			msg+="驱动方式必须选一!</br>"
		}
		if(!checkCheckbox('equipments',false)){
			msg+="装备必须选一!</br>"
		}
		if(!checkCheckbox('purposes',false)){
			msg+="用途必须选一!</br>"
		}
		if(!checkSpecialCheckBox('purposes','其他','otherValue')){
			msg+="用途勾选其它时请输入其它内容!</br>"
		}
		
		if(!checkCheckbox('roads',false)){
			msg+="道路状况必须选一!</br>"
		}
		if(!checkCheckbox('temperatureAndTimes',false)){
			msg+="温度/时间必须选一!</br>"
		}
		if(!checkCheckbox('happendTimes',false)){
			msg+="发生时机必须选一!</br>"
		}
		if(!checkCheckbox('speeds',false)){
			msg+="发生速度必须选一!</br>"
		}
		if(!checkCheckbox('rains',false)){
			msg+="雨水状况必须选一!</br>"
		}
		if(!checkCheckbox('useds',false)){
			msg+="平时使用状况必须选一!</br>"
		}
		if(!checkCheckbox('importantLevels',true)){
			msg+="重要度判定必须且只能选一!</br>"
		}
		if(!checkCheckbox('airConditionStatuss',true)){
			msg+="空调状态必须且只能选一!</br>"
		}
		
		
		
		if(msg!=""){
			MyAlert(msg);
			return false;
		}else{
			return true;
		}
	}
	
	//添加多选框数据
	var checkBoxIds = new Array(); 
	/**
	 * 添加多选框数据 
	 * @param {Object} id checkBoxName
	 * @param {Object} otherId 如果判断值相同时所赋加的值的文本ID
	 * @param {Object} isValue 判断值
	 * @return {TypeName} 
	 */
	function addSpecialCheckBoxIds(id,isValue,otherId){
		var cnt = 0;
		var chk=document.getElementsByName(id);
		var l = chk.length;
		checkBoxIds.splice(0,checkBoxIds.length);
		for(var i=0;i<l;i++){
			if(chk[i].checked){
		       cnt++;
		       //特殊处理 拼接其它的值
		       if("" != otherId && chk[i].value == isValue){
		    	   var textVal=document.getElementById(otherId).value;
		    	   checkBoxIds.push(chk[i].value+'-'+textVal);
		       }else{
		    	   checkBoxIds.push(chk[i].value);
		       }
			}
		 }
		return checkBoxIds;
	}
	 
	function addCheckBoxIds(id){
		var cnt = 0;
		var chk=document.getElementsByName(id);
		var l = chk.length;
		checkBoxIds.splice(0,checkBoxIds.length);
		for(var i=0;i<l;i++){
			if(chk[i].checked){
		       cnt++;
		       checkBoxIds.push(chk[i].value);
			}
		 }
		return checkBoxIds;
	}
	
	function checkCheckbox(id,onlyOne){
		addCheckBoxIds(id)
		if(onlyOne){
			if(checkBoxIds.length==1) return true;
			else return false;
		}else{
			if(checkBoxIds.length>0) return true;
			else return false;
		}
	}
	
	function checkSpecialCheckBox(id,isValue,otherId){
		var chk=document.getElementsByName(id);
		var l = chk.length;
		for(var i=0;i<l;i++){
			if(chk[i].checked && "" != otherId && chk[i].value == isValue){
		       //特殊处理 拼接其它的值
	    	   if(trim(document.getElementById(otherId).value) != ""){
	    		   return true;
	    	   }else return false;
			}
		 }
		return true;
	}
	//填写索赔单号再取到VIN然后带出信息
	function queryDateFirst(){
		var vin=document.getElementById("VIN").value;
		queryDate(vin);
	}
	function queryDate(vin){
		makeNomalFormCall('<%=contextPath%>/vehicleInfoManage/apply/QualityInfoReport/queryDataByVin.json?vin='+vin,queryDateByVinBack,'fm','');
	}
	
	function queryDateByVinBack(json){
		//setTextData('dealerCode',json.data.DEALER_CODE,false);
		//setTextData('dealerName',json.data.DEALER_NAME,false);
		setTextData('modelName',json.data.MODEL_NAME,false);
		setTextData('modelId',json.data.MODEL_ID,false);
		setTextData('mileage',json.data.MILEAGE,false);
		setTextData('engineNo',json.data.ENGINE_NO,false);
		setTextData('productDate',json.data.PRODUCT_DATE,false);
		setTextData('purchasedDate',json.data.BUY_DATE,false);
		setTextData('ctmName',json.data.CTM_NAME,false);
		setTextData('ctmPhone',json.data.PHONE,false);
	}
	
	function selectPartFirst(){
		openWindowDialog('<%=contextPath%>/vehicleInfoManage/apply/QualityInfoReport/selectPartFirst.do');
	}
	
	function selectPart(){
		openWindowDialog('<%=contextPath%>/vehicleInfoManage/apply/QualityInfoReport/selectPart.do');
	}
	function setTextData(id,value,isdisabled){
		if(value == null) {
			value = ""; 
			return;
		}
		
		document.getElementById(id).value = value;
		document.getElementById(id).disabled = isdisabled;
	}
	
	function changeData(PART_ID,PART_CNAME){
		setTextData('partId',PART_ID,false);
		setTextData('partName',PART_CNAME,false);
		
		if(''!=PART_ID){
			makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changePartMarker.json?id='+PART_ID+'&defaultValue=""&isdisabled=false',changeMarkerBack,'fm','');
		}else{
			resetSelectOption('marker',null,null,null,null,null);
		}
	}
	function changeDataFrist(CLAIM_NO,VIN){
		setTextData('claimno',CLAIM_NO,false);
		setTextData('VIN',VIN,false);
		queryDateFirst();
	}
	
	
	function changeMarkerBack(json){
		resetSelectOption('marker',json.partMarkerList,'MAKER_NAME','MAKER_ID',json.defaultValue,json.isdisabled);
	}
	
	function openWindowDialog(targetUrl){
	    var height = 800;
	    var width = 1000;
	    var iTop = (window.screen.availHeight-30-height)/2;  //获得窗口的垂直位置      
	    var iLeft = (window.screen.availWidth-10-width)/2;   //获得窗口的水平位置
	    var params = "width="+width+",height="+height+",top="+iTop+",left="+iLeft+",scrollbars=yes,resizable=yes,status=no";
	    winHandle = window.open(targetUrl,null,params);
	    return winHandle;
 	}
	
		
	//重置下拉框数据
	function resetSelectOption(id,maps,dataName,dataValue,dataId,isdisabled){
		clearSelectNode(id);
		addSelectNode(id,maps,dataName,dataValue,dataId,isdisabled);
	}
	
	//动态删除下拉框节点
	function clearSelectNode(id) {			
		document.getElementById(id).options.length=0; 			
	}
	//动态添加下拉框节点
	function addSelectNode(id,maps,dataName,dataValue,dataId,isdisabled){
		document.getElementById(id).options.add(new Option('-请选择-',''));
		for(var i = 0; i<maps.length;i++){
			if((maps[i])['' +dataValue+''] == dataId){
				document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,true));
			}
			else{
				document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,false));
			}
		}
		if(isdisabled == 'true' || isdisabled == true){
			document.getElementById(id).disabled = "disabled";
		}else{
			document.getElementById(id).disabled = "";
		}
	}
	
	//修改提示框超出6行会超出框的BUG
	function MyAlert(info){
		 var owner = getTopWinRef();
		 try{
		  _dialogInit();  
		  owner.getElementById('dialog_content_div').innerHTML='\
		    <div style="font-size:12px;">\
		     <div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
		      <b>信息</b>\
		     </div>\
		     <div id="dialog_alert_info" style="padding:10px; line-height:2em"></div>\
		     <div style="padding:2px;text-align:center;background:#D0BFA1;">\
		      <input id="dialog_alert_button" type="button" value="确定" style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
		     </div>\
		    </div>';
		  owner.getElementById('dialog_alert_info').innerHTML=info;
		  owner.getElementById('dialog_alert_button').onclick=_hide;
		  var height=200 ;
		  
		  if(info.split('</br>').length>=6){
		 	height = height + (info.split('</br>').length-6)*27;
		  }
		   _setSize(300,height);
		    
		  _show();
		 }catch(e){
		  MyAlert('MyAlert : '+ e.name+'='+e.message);
		 }finally{
		  owner=null;
		 }
	}
	//删除左右两端的空格 
	function trim(str){ 
		return str.replace(/(^\s*)|(\s*$)/g, ""); 
	} 


</script>

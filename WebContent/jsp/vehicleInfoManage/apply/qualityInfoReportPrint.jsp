<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@ page import="com.infodms.dms.po.TtAsBarcodeApplyPO"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.text.DecimalFormat"%>
<style media=print>
/* 应用这个样式的在打印时隐藏 */
.Noprint {
	display: none;
}

/* 应用这个样式的，从那个标签结束开始另算一页，之后在遇到再起一页，以此类推 */
hr {
	page-break-after: always;
}
</style>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>索赔申请单打印</title>
<% String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
	%>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
   		doCusChange();
	}

</script>
</head>
<body onload="doInit()">
	<object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
	<form name="fm" id="fm">
<center>
	<div  style="font-size: 18px; font-weight: bold;">市场质量信息报表</div>
	<table class="tab_printsep" width="800px" >
		<tr>
			<td width="12%" >经销商代码</td>
			<td width="38%">${ttSalesQualityInfoReportPO.dealerCode}</td>
			<td width="12%" >经销商名称</td>
			<td width="38%">${ttSalesQualityInfoReportPO.dealerName}</td>
		</tr>
		<tr>
			<td >填报人</td>
			<td>${ttSalesQualityInfoReportPO.reportName}</td>
			<td >联系电话</td>
			<td>${ttSalesQualityInfoReportPO.phone}</td>
		</tr>
		<tr>
			<td >车型</td>
			<td>${ttSalesQualityInfoReportPO.modelName}</td>
			<td >VIN代码</td>
			<td>${ttSalesQualityInfoReportPO.vin}</td>
		</tr>
		<tr>
			<td >行驶里程</td>
			<td><fmt:parseNumber>${ttSalesQualityInfoReportPO.mileage}</fmt:parseNumber>KM</td>
			<td >发动机号</td>
			<td>${ttSalesQualityInfoReportPO.engineNo}</td>
		</tr>
		<tr>
			<td >出厂日期</td>
			<td><fmt:formatDate  value='${ttSalesQualityInfoReportPO.productDate}'  type="both" pattern="yyyy-MM-dd HH:mm:ss" /></td>     
			<td >购买日期</td>
			<td><fmt:formatDate  value='${ttSalesQualityInfoReportPO.purchasedDate}'  type="both" pattern="yyyy-MM-dd HH:mm:ss" /></td>
		</tr>
		<tr>
			<td >故障名称</td>
			<td>${ttSalesQualityInfoReportPO.faultName}</td>     
			<td >故障日期</td>
			<td ><fmt:formatDate  value='${ttSalesQualityInfoReportPO.faultDate}'  type="both" pattern="yyyy-MM-dd" /></td>
		</tr>
		<tr>
			<td >用户姓名</td>
			<td>${ttSalesQualityInfoReportPO.ctmName}</td>
			<td >联系电话</td>
			<td>${ttSalesQualityInfoReportPO.ctmPhone}</td>
		</tr>
		<tr>
			<td >发动机类型</td>
			<td><div align="left">
				<c:forEach var="engineType" items="${engineTypeList}">
					<c:choose>
						<c:when test="${engineType.isCheck}">
							<input type="checkbox" name="enginTypes" value="${engineType.value}" checked="checked" onclick="return false;">${engineType.key}
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="enginTypes" value="${engineType.value}" onclick="return false;">${engineType.key}
						</c:otherwise>
					</c:choose>
				</c:forEach>
				</div>
			</td>
			<td >车身</td>
			<td><div align="left">
				<c:forEach var="door" items="${doorList}">
					<c:choose>
						<c:when test="${door.isCheck}">
							<input type="checkbox" name="doors" value="${door.value}" checked="checked" onclick="return false;">${door.key}
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="doors" value="${door.value}" onclick="return false;">${door.key}
						</c:otherwise>
					</c:choose>
				</c:forEach>
				</div>
			</td>
		</tr>
		<tr>
			<td >驱动方式</td>
			<td><div align="left">
				<c:forEach var="driveType" items="${driveTypeList}">
					<c:choose>
						<c:when test="${driveType.isCheck}">
							<input type="checkbox" name="driveTypes" value="${driveType.value}" checked="checked" onclick="return false;">${driveType.key}
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="driveTypes" value="${driveType.value}" onclick="return false;">${driveType.key}
						</c:otherwise>
					</c:choose>
				</c:forEach>
				</div>
			</td>
			<td >装备</td>
			<td><div align="left">
				<c:forEach var="equipment" items="${equipmentList}" varStatus="equipmentStatus">
					<c:choose>
						<c:when test="${equipment.isCheck}">
							<input type="checkbox" name="equipments" value="${equipment.value}" checked="checked" onclick="return false;">${equipment.key}
							<c:if test="${equipmentStatus.count%4 == 0}"><br></c:if>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="equipments" value="${equipment.value}" onclick="return false;">${equipment.key}
							<c:if test="${equipmentStatus.count%4 == 0}"><br></c:if>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				</div>
			</td>
		</tr>					
	</table>
	<br/>
	<table class="tab_printsep" width="800px" >
		<tr>
			<td width="3%"  rowspan="5">问<br>题<br>描<br>述<br>及<br>状<br>态</td>
			<td width="97%" colspan="6">
				<table width="100%" class="tab_printvoid" border="0">
					<tr>
						<td width="75%">条件及现象　（时间/地点/状况）</td>
						<td width="3%" rowspan="2" >用途</td>
						<td width="25%" rowspan="2"><div align="left">
							<c:forEach var="purpose" items="${purposeList}" varStatus="purposeStatus">
								<c:choose>
									<c:when test="${purpose.isCheck}">
										<input type="checkbox" name="purposes" value="${purpose.value}" checked="checked" onclick="return false;">${purpose.key}
										<c:if test="${purposeStatus.count%2 == 0}"><br></c:if>
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="purposes" value="${purpose.value}" onclick="return false;">${purpose.key}
										<c:if test="${purposeStatus.count%2 == 0}"><br></c:if>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<input value="${otherValue}" style="width: 56px;
		                        border-top-style: none; border-right-style: none; border-left-style: none; height: 20px;
		                        background-color: transparent; border-bottom-style: groove" size="4" name="otherValue"
		                        id="otherValue" readonly="readonly"
		                        >
		                        </div>
						</td>
					</tr>
					<tr>
						<td width="75%">
							<textarea rows="4" style="width: 98%; border-color: white;" id="condition" name="condition">${ttSalesQualityInfoReportPO.condition}</textarea>
						</td>
					</tr>
				</table>
			</td>

		</tr>
		<tr>
			<td width="16%" >道路状况</td>
			<td width="12%" >温度/时间</td>
			<td width="35%" colspan="2" >发生时机/速度</td>
			<td width="16%" >雨水状况</td>
			<td width="16%" >平时使用状况</td>
		</tr>
		<tr>
			<td rowspan="3"><div align="left">
				<c:forEach var="road" items="${roadList}" varStatus="roadStatus">
					<c:choose>
						<c:when test="${road.isCheck}">
							<input type="checkbox" name="roads" value="${road.value}" checked="checked" onclick="return false;">${road.key}
							<c:if test="${roadStatus.count%2 == 0}"><br></c:if>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="roads" value="${road.value}" onclick="return false;">${road.key}
							<c:if test="${roadStatus.count%2 == 0}"><br></c:if>
						</c:otherwise>
					</c:choose>
				</c:forEach></div>
			</td>
			<td rowspan="3"><div align="left">
				<c:forEach var="temperatureAndTime" items="${temperatureAndTimeList}">
					<c:choose>
						<c:when test="${temperatureAndTime.isCheck}">
							<input type="checkbox" name="temperatureAndTimes" value="${temperatureAndTime.value}" checked="checked" onclick="return false;">${temperatureAndTime.key}
							<br>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="temperatureAndTimes" value="${temperatureAndTime.value}" onclick="return false;">${temperatureAndTime.key}
							<br>
						</c:otherwise>
					</c:choose>
				</c:forEach></div>
			</td>
			<td rowspan="3"><div align="left">
				<c:forEach var="happendTime" items="${happendTimeList}" varStatus="happendTimeStatus">
					<c:choose>
						<c:when test="${happendTime.isCheck}">
							<input type="checkbox" name="happendTimes" value="${happendTime.value}" checked="checked" onclick="return false;">${happendTime.key}
							<c:if test="${happendTimeStatus.count%2 == 0}"><br></c:if>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="happendTimes" value="${happendTime.value}" onclick="return false;">${happendTime.key}
							<c:if test="${happendTimeStatus.count%2 == 0}"><br></c:if>
						</c:otherwise>
					</c:choose>
				</c:forEach></div>
			</td>
			<td rowspan="3"><div align="left">
				<c:forEach var="speed" items="${speedList}">
					<c:choose>
						<c:when test="${speed.isCheck}">
							<input type="checkbox" name="speeds" value="${speed.value}" checked="checked" onclick="return false;">${speed.key}
							<br>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="speeds" value="${speed.value}" onclick="return false;">${speed.key}
							<br>
						</c:otherwise>
					</c:choose>
				</c:forEach></div>
			</td>
			<td align="left"><div align="left">
				<c:forEach var="rain" items="${rainList}">
					<c:choose>
						<c:when test="${rain.isCheck}">
							<input type="checkbox" name="rains" value="${rain.value}" checked="checked" onclick="return false;">${rain.key}
							<br>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="rains" value="${rain.value}" onclick="return false;">${rain.key}
							<br>
						</c:otherwise>
					</c:choose>
				</c:forEach></div>
			</td>
			<td rowspan="3"><div align="left">
				<c:forEach var="used" items="${usedList}">
					<c:choose>
						<c:when test="${used.isCheck}">
							<input type="checkbox" name="useds" value="${used.value}" checked="checked" onclick="return false;">${used.key}
							<br>
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="useds" value="${used.value}" onclick="return false;">${used.key}
							<br>
						</c:otherwise>
					</c:choose>
				</c:forEach></div>
			</td>
		</tr>
		<tr>
			<td >空调状态</td>
		</tr>
		<tr>
			<td ><div align="left">
				<c:forEach var="airConditionStatus" items="${airConditionStatusList}">
					<c:choose>
						<c:when test="${airConditionStatus.isCheck}">
							<input type="checkbox" name="airConditionStatuss" value="${airConditionStatus.value}" checked="checked" onclick="return false;">${airConditionStatus.key}
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="airConditionStatuss" value="${airConditionStatus.value}" onclick="return false;">${airConditionStatus.key}
						</c:otherwise>
					</c:choose>
				</c:forEach></div>
			</td>
		</tr>
		
		
		<tr>
			<td width="3%"  rowspan="6">诊<br>断<br>结<br>果<br>及<br>处<br>理<br>方<br>法</td>
			<td colspan="6">状况 （检查结论）</td>
		</tr>
		<tr>
			<td colspan="6"><div align="left"> ${ttSalesQualityInfoReportPO.checkResult}</div></td>
		</tr>
		<tr><td colspan="6">处理方法（维修内容）</td></tr>
		<tr>
			<td colspan="6"><div align="left"> ${ttSalesQualityInfoReportPO.content}</div></td>
		</tr>
		<tr>
			<td rowspan="2" >主因部件</td>
			<td >配件名称</td>
			<td colspan="2" align="left"><div align="left"> ${ttSalesQualityInfoReportPO.partName}</div></td>
			<td rowspan="2" >重要度判定</td>
			<td rowspan="2"><div align="left">
				<c:forEach var="importantLevel" items="${importantLevelList}">
					<c:choose>
						<c:when test="${importantLevel.isCheck}">
							<input type="checkbox" name="importantLevels" value="${importantLevel.value}" checked="checked" onclick="return false;">${importantLevel.key}
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="importantLevels" value="${importantLevel.value}" onclick="return false;">${importantLevel.key}
						</c:otherwise>
					</c:choose>
				</c:forEach></div>
			</td>
		</tr>
		<tr>
			<td >责任单位</td>
			<td colspan="2"><div align="left">
					<c:forEach var="mk" items="${markerList}">
						<c:choose>
							<c:when test="${mk.isCheck == 1}">${mk.MAKER_NAME}</c:when>
							<c:otherwise></c:otherwise>
						</c:choose>
					</c:forEach></div>
			</td>
		</tr>
		<tr>
			<td colspan="7">补充说明</td>
		</tr>
		<tr>
			<td colspan="7"><div align="left">${ttSalesQualityInfoReportPO.remark}</div></td>
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
	</table>
	
	<table width="100%" cellpadding="1"  class="Noprint">
		<tr>
			<td width="100%" height="25" colspan="3"><div id="kpr"
					align="center">
					<input class="ipt" type="button" value="打印"
						onclick="javascript:printit();" /> <input class="ipt"
						type="button" value="打印页面设置" onclick="javascript:printsetup();" />
					<input class="ipt" type="button" value="打印预览"
						onclick="javascript:printpreview();" />
			</td>
		</tr>
	</table>
	</center>
	</form>
	<script language="javascript">
		function printsetup() {
			wb.execwb(8, 1); // 打印页面设置 
		}
		function printpreview() {
			wb.execwb(7, 1); // 打印页面预览       
		}
		function printit() {
			if (confirm('确定打印吗？')) {
				wb.execwb(6, 6)
			}
		}
	</script>
</body>
</html>


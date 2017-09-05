<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>投诉咨询受理(任何人)</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 来电管理 &gt;来电登记</div>
	<form method="post" name = "fm" id="fm">
		<table width="100%" class="tab_edit">
			<th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />来电登记信息</th>
			<tr>
				<td align="right" >渠道类型：</td>
				<td align="left">
					<script type="text/javascript">
						genSelBoxExp("cpChanelType",<%=Constant.CHANEL_TYPE%>,"",true,"short_sel","","false",'');
					</script>
					<font color="red">*</font>
				</td>
				<td align="right" >业务类型：</td>
				<td align="left" >
					<select id="bizType" name="bizType" class="short_sel"  onchange="changeBizTypeEvent(this.value,'',false)">
						<option value=''>-请选择-</option>
						<c:forEach var="bt" items="${bizTypeList}">
							<option value="${bt.id}" title="${bt.name}">${bt.name}</option>
						</c:forEach>
					</select>
					<font color="red">*</font>
				</td>
			</tr>
			<tr id='tr_complaint_customer_type' style='display:none'>
				<td width="20%" align="right">咨询客户类型:</td>
				<td align="left" >
					<script type="text/javascript">
						genSelBoxExp("bizCustomerType",<%=Constant.BIZ_CUSTOMER_TYPE%>,"",true,"short_sel","onchange=customerTypeChange(this)","false",'');
					</script>
					<font color="red">*</font>
				</td>
				<td width="20%" align="right">了解渠道:</td>
				<td align="left" >
					<script type="text/javascript">
						genSelBoxExp("knownChanel",<%=Constant.BIZ_KNOWN_CHANEL%>,"",true,"short_sel","","false",'');
					</script>
					<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td align="right" >内容类型：</td>
				<td align="left">
					<select id="contType" name="contType" onchange="getDetail(this.value)" class="short_sel">
						<option value=''>-请选择-</option>
					</select>
					<font color="red" id="contTypeRed" style="display: none">*</font>
				</td>
				<td colspan="2" id="detailContent" align="center">&nbsp;</td>
			</tr>
			<tr>
				<td width="20%" align="right" >客户名称：</td>
				<td width="30%" align="left" >
					<input class="middle_txt" type="text" id="ctmname" name="ctmname" value='${queryCustomerInforData.CTMNAME}' datatype="1,is_null,25" maxlength="25"/><font color="red">*</font>
					<input class="normal_btn" type="button" value="客户选择" name="alertCusScreen" id="alertCusScreen" onclick="alertCusScreenEvent()"/>
					<input class="normal_btn" type="button" value="清除" name="clearCont" id="clearCont" onclick="clearDate()" />
				</td>
				<td width="20%" align="right" >联系电话：</td>
				<td width="30%" align="left">
					<input class="middle_txt" type="text" id="tele" name="tele" value='${queryCustomerInforData.PHONE}' maxlength="40"/>
<%-- 					<input class="middle_txt" type="text" id="teleStr" name="teleStr" value='${queryCustomerInforData.PHONE}' maxlength="40" disabled="disabled"/> --%>
				</td>
			</tr>
			<tr id='tr_vichle_info'>
				<td align="right" >VIN：</td>
				<td align="left" >
					<c:choose>
						<c:when test="${empty queryCustomerInforData}">
							<input class="middle_txt" type="text" id="vin" name="vin" onchange="setHiddenVIN(this.value,'vinStr');" disabled="disabled" value="${queryCustomerInforData.VIN}"/>
							<input type="hidden" id="vinStr" name="vinStr" value="${queryCustomerInforData.VIN}"/>
						</c:when>
						<c:otherwise>
							<input class="middle_txt" type="text" id="vin" name="vin" onchange="setHiddenVIN(this.value,'vinStr')" disabled="disabled" value="${queryCustomerInforData.VIN}"/>
							<input type="hidden" id="vinStr" name="vinStr" value="${queryCustomerInforData.VIN}"/>
						</c:otherwise>
					</c:choose>
				</td>
				<td align="right" >发动机号：</td>
				<td align="left">
					<input type="hidden" id="engineNo" name="engineNo"/>
					<input class="middle_txt" type="text" id="engineNoStr" name="engineNoStr" disabled="disabled"/>
				</td>
			</tr>
			<tr id='tr_vichle_info2'>
				<td align="right" >来电号码：</td>
				<td align="left" >
					<input class="middle_txt" type="text" id="incommingPhone" name="incommingPhone"/>
				</td>
				<td align="right" >行驶里程：</td>
				<td align="left">
					<c:choose>
						<c:when test="${empty queryCustomerInforData}">
							<input class="middle_txt" type="text" id="mileage" name="mileage" datatype="1,is_double,20" maxlength="20"/>
						</c:when>
						<c:otherwise>
							<input class="middle_txt" type="text" id="mileage" name="mileage" value="${queryCustomerInforData.MILEAGE}" datatype="1,is_double,20" maxlength="20"/>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr id='tr_complaint_bussness_type' style='display:none'>
				<td align="right" >商家类型：</td>
				<td align="left" >
					<script type="text/javascript">
						genSelBoxExp("bussnessType",<%=Constant.BUSSNESS_TYPE%>,"",true,"short_sel","","false",'');
					</script>
					<font color="red">*</font>
				</td>
				<td align="right" >现代理品牌：</td>
				<td align="left" >
					<input class="middle_txt" type='text' value='' id='proxy_type' name='proxy_type'/>
				</td>
			</tr>
			<tr id='tr_complaint_bussness_area' style='display:none'>
				<td align="right" >商家所在地区：</td>
				<td align="left" colspan='3'>
					<input type='text' value='' id='dealer_area' name='dealer_area' style="width:600px"/>
				</td>
			</tr>
			<tr>
				<td align="right" >车辆用途：</td>
				<td align="left">
					<select id="vinuse" name="vinuse" class="short_sel">
						<c:choose>
							<c:when test="${empty queryCustomerInforData}">
								<option value=''>-请选择-</option>
							</c:when>
							<c:otherwise>
								<option value=''>-请选择-</option>
							</c:otherwise>
						</c:choose>
						
						<c:forEach var="vinuse" items="${vinUseList}">
							<c:choose>
								<c:when test="${empty queryCustomerInforData}">
									<option value="${vinuse.CODE_ID}" title="${vinuse.CODE_DESC}">${vinuse.CODE_DESC}</option>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${queryCustomerInforData.SALESADDRESS == vinuse.CODE_ID}">
											<option value="${vinuse.CODE_ID}" title="${vinuse.CODE_DESC}" selected="selected">${vinuse.CODE_DESC}</option>
										</c:when>
										<c:otherwise>
											<option value="${vinuse.CODE_ID}" title="${vinuse.CODE_DESC}">${vinuse.CODE_DESC}</option>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr id="gzlc">
				<td align="right" >故障部件：</td>
				<td align="left" >
					<select id="faultPart" name="faultPart" class="short_sel">
							<option value=''>-请选择-</option>
						<c:forEach var="fp" items="${faultPartList}">
							<option value="${fp.CODE_ID}" title="${fp.CODE_DESC}">${fp.CODE_DESC}</option>
						</c:forEach>
					</select>
				</td>
				<td align="right" >里程范围：</td>
				<td align="left" >
					
					<select id="mileageRange" name="mileageRange" class="short_sel">
						<c:choose>
							<c:when test="${empty queryCustomerInforData}">
								<option value=''>-请选择-</option>
							</c:when>
							<c:otherwise>
								<option value=''>-请选择-</option>
							</c:otherwise>
						</c:choose>
						
						<c:forEach var="mr" items="${mileageRangeList}">
							<c:choose>
								<c:when test="${empty queryCustomerInforData}">
									<option value="${mr.CODE_ID}" title="${mr.CODE_DESC}">${mr.CODE_DESC}</option>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${queryCustomerInforData.SALESADDRESS == vinuse.CODE_ID}">
											<option value="${mr.CODE_ID}" title="${mr.CODE_DESC}" selected="selected" >${mr.CODE_DESC}</option>
										</c:when>
										<c:otherwise>
											<option value="${mr.CODE_ID}" title="${mr.CODE_DESC}">${mr.CODE_DESC}</option>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>					
			</tr>
			<tr>
				<td align="right" >省份：</td>
				<td align="left" >
					<select id="pro" name="pro" class="short_sel" onchange="changeCityEvent(this.value,'',false)">
						<c:choose>
							<c:when test="${empty queryCustomerInforData}">
								<option value=''>-请选择-</option>
							</c:when>
							<c:otherwise>
								<option value=''>-请选择-</option>
							</c:otherwise>
						</c:choose>
						<c:forEach var="pro" items="${proviceList}">
							<c:choose>
								<c:when test="${empty queryCustomerInforData}">
									<option value="${pro.REGION_CODE}" title="${pro.REGION_NAME}">${pro.REGION_NAME}</option>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${queryCustomerInforData.PROVINCE == pro.REGION_CODE}">
											<option value="${pro.REGION_CODE}" title="${pro.REGION_NAME}" selected="selected">${pro.REGION_NAME}</option>
										</c:when>
										<c:otherwise>
											<option value="${pro.REGION_CODE}" title="${pro.REGION_NAME}">${pro.REGION_NAME}</option>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<font color="red">*</font>
				</td>
				<td align="right" >城市：</td>
				<td align="left">
					<select id="citysel" name="citysel" class="short_sel">
						<c:choose>
							<c:when test="${empty queryCustomerInforData}">
								<option value=''>-请选择-</option>
							</c:when>
							<c:otherwise>
								<option value=''>-请选择-</option>
							</c:otherwise>
						</c:choose>
						<c:forEach var="city" items="${cityList}">
							<c:choose>
								<c:when test="${empty queryCustomerInforData}">
									<option value="${city.REGION_CODE}" title="${city.REGION_NAME}">${city.REGION_NAME}</option>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${queryCustomerInforData.CITY == city.REGION_CODE}">
											<option value="${city.REGION_CODE}" title="${city.REGION_NAME}" selected="selected">${city.REGION_NAME}</option>
										</c:when>
										<c:otherwise>
											<option value="${city.REGION_CODE}" title="${city.REGION_NAME}">${city.REGION_NAME}</option>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td align="right" >车种：</td>
				<td align="left" >
					<select id="ser" name="ser" class="short_sel" onchange="changeModelEvent(this.value,'',false)">
						<c:choose>
							<c:when test="${empty queryCustomerInforData}">
								<option value=''>-请选择-</option>
							</c:when>
							<c:otherwise>
								<option value=''>-请选择-</option>
								<script type="text/javascript">
									document.getElementById('ser').disabled = 'disabled';
								</script>
							</c:otherwise>
						</c:choose>
						<c:forEach var="seri" items="${seriesList}">
							<c:choose>
								<c:when test="${empty queryCustomerInforData}">
									<option value="${seri.groupId}" title="${seri.groupName}">${seri.groupName}</option>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${queryCustomerInforData.SERIESID == seri.groupId}">
											<option value="${seri.groupId}" title="${seri.groupName}" selected="selected">${seri.groupName}</option>
										</c:when>
										<c:otherwise>
											<option value="${seri.groupId}" title="${seri.groupName}">${seri.groupName}</option>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<font color="red" id="seriIdRed">*</font>
				</td>
				<td align="right" >车型(关注)：</td>
				<td align="left">
					<select id="model" name="model" class="short_sel">
						<c:choose>
							<c:when test="${empty queryCustomerInforData}">
								<option value=''>-请选择-</option>
							</c:when>
							<c:otherwise>
								<option value=''>-请选择-</option>
								<script type="text/javascript">
									document.getElementById('model').disabled = 'disabled';
								</script>
							</c:otherwise>
						</c:choose>
						<c:forEach var="mode" items="${modelList}">
							<c:choose>
								<c:when test="${empty queryCustomerInforData}">
									<option value="${mode.groupId}" title="${mode.groupName}">${mode.groupName}</option>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${queryCustomerInforData.MODELID == mode.groupId}">
											<option value="${mode.groupId}" title="${mode.groupName}" selected="selected">${mode.groupName}</option>
										</c:when>
										<c:otherwise>
											<option value="${mode.groupId}" title="${mode.groupName}">${mode.groupName}</option>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<font color="red" id="modelIdRed">*</font>
				</td>
			</tr>
			<tr id='tr_vichle_date'>
				<td align="right" >购车日期：</td>
				<td align="left">
					<c:choose>
						<c:when test="${empty queryCustomerInforData}">
				            <input  class="middle_txt" type="text" name="pudate" id="pudate" value="${queryCustomerInforData.PUDATE}" disabled="disabled"/>
				            <input class="time_ico" value=" " onclick="showcalendar(event, 'pudate', false);" type="button"/>   
						</c:when>
						<c:otherwise>
							<input class="middle_txt" type="text" name="pudate" id="pudate" value="${queryCustomerInforData.PUDATE}" disabled="disabled"/>
							<input class="time_ico" value=" " onclick="showcalendar(event, 'pudate', false);" type="button"/>	
						</c:otherwise>
					</c:choose>
				</td>
				<td align="right" >生产日期：</td>
				<td align="left" >
					<c:choose>
						<c:when test="${empty queryCustomerInforData}">
				             <input class="middle_txt" type="text" name="pdate" id="pdate" value="${queryCustomerInforData.PDATE}" disabled="disabled"/>
				             <input class="time_ico" value=" " onclick="showcalendar(event, 'pdate', false);" type="button"/>
						</c:when>
						<c:otherwise>
				             <input class="middle_txt" type="text" name="pdate" id="pdate" value="${queryCustomerInforData.PDATE}" disabled="disabled"/>
				             <input class="time_ico" value=" " onclick="showcalendar(event, 'pdate', false);" type="button"/>	
						</c:otherwise>
					</c:choose>								
				</td>
			</tr>			
			<tr id="complaintTrOne" style="display: none">
				<td align="right" >抱怨级别：</td>
				<td align="left" >
					<script type="text/javascript">
						genSelBoxExp("cplevel",<%=Constant.COMPLAINT_LEVEL%>,"",true,"short_sel","","false",'');
					</script>
	
				</td>
				<td align="right" >规定处理期限：</td>
				<td align="left" >
					<script type="text/javascript">
						genSelBoxExp("cplimit",<%=Constant.RULE_LIMIT%>,"",true,"short_sel","","false",'');
					</script>
				</td>
			</tr>
			<tr>
				<td align="right" >来电内容：</td>
				<td align="left"  colspan="3">
					<textarea id="complaintContent" name="complaintContent" rows="5" style="width: 95%" ></textarea>
					<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td align="right" >受理人反馈内容：</td>
				<td align="left"  colspan="3">
					<textarea id="CP_SEAT_COMMENT" name="CP_SEAT_COMMENT" rows="5" style="width: 95%" ></textarea>
					<font color="red">*</font>
				</td>
			</tr>
			
			<tr id='tr_consult_deal_type'>
				<td align="right" >处理方式：</td>
				<td align="left" colspan='3'>
					<select id="dealModel" name="dealModel" class="short_sel"  onchange="changeDealModelEvent(this.value)">
						<option value=''>-请选择-</option>
					</select>
					<font color="red">*</font>
				</td>
				<td align="right" id="td_deal_status_label" style="display:none">处理状态：</td>
				<td align="left" id="td_deal_status" style="display:none">
					<script type="text/javascript">
				  		genSelBoxExp("complaint_status",<%=Constant.COMPLAINT_STATUS%>,"",true,"short_sel",'',"false",'');
					</script>

					<font color="red">*</font>
				</td>
			</tr>
			<tr style="display: none" id="rcontTr">
				<td align="right" >回复内容：</td>
				<td align="left"  colspan="3">
					<textarea id="rcont" name="rcont" rows="5" style="width: 95%"></textarea>
					<font color="red">*</font>
				</td>
			</tr>
			<tr id="complaintTrTwo">
				<td align="right" >处理部门：</td>
				<td align="left"  colspan="3">
					<input type="radio" name="choise" value="1" onclick="choiseVm(1);" checked="true" />车厂
					<input type="radio" name="choise" value="2" onclick="choiseVm(2);" />经销商
					<select id="cpObject" name="cpObject" class="short_sel" onchange="changeDept(this.value)" >
						<option value=''>-请选择-</option>
						<c:forEach var="cpObj" items="${cpObjectList}">
							<option value="${cpObj.ORG_ID}" title="${cpObj.ORG_NAME}">${cpObj.ORG_NAME}</option>
						</c:forEach>
					</select>
					<!-- 大区 -->
					<select id="dept" name="dept" class="short_sel" onchange="changeArea(this.value);" style="display:none;">
						<option value=''>-请选择-</option>
						<c:forEach var="deptVal" items="${cpDeptList}">
							<option value="${deptVal.ORG_ID}" title="${deptVal.ORG_NAME}">${deptVal.ORG_NAME}</option>
						</c:forEach>
					</select>
					<select id="vcPro" name="vcPro" class="short_sel" onchange="changeMyCityEvent(this.value,'',false)" style="display:none;">
						<option value=''>-请选择-</option>
						<c:forEach var="os" items="${tmOrgSmallList}">
							<option value="${os.orgId}" title="${os.orgName}">${os.orgName}</option>
						</c:forEach>
					</select>
					<select id="myCity" name="myCity" class="short_sel" onchange="changeVcProEvent(this.value,'',false)"  style="display:none;">
						<option value=''>-请选择-</option>
					</select>
					<select id="vehicleCompany" name="vehicleCompany" onchange="changeDealer(this.value);" style="display:none;">
						<option value=''>-请选择-</option>
					</select>
					<select id="dealUser" name="dealUser">
						<option value=''>-请选择-</option>
					</select>
					<font color="red">*</font>
				</td>
			</tr>
			
			
			<tr>
				<td colspan="8" align="center">
					<input class="normal_btn" type="button" value="保存" name="saveButton" id="saveButton" onclick="save()" />
					&nbsp;
					<input class="normal_btn" type="reset" value="重置" name="resetBtn" id="resetBtn"/>
        		</td>
			</tr>
		</table>
		
	</form>
	<script type="text/javascript">
		function save(){
			//验证
			if(check()){
				document.getElementById("saveButton").disabled = true;
				makeNomalFormCall('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAcceptYx/saveComplaintAccept.json',saveBack,'fm','');
			}
		}
		
		function saveBack(json){
			if(json.isSuccess != null && json.isSuccess=='0'){
				parent.MyAlert("保存成功!");
				window.location.href="<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAcceptYx/complaintAcceptInit.do";
			}
		}
		
		//合法性验证
		function check(){ 
			var msg ="";
			if(""==document.getElementById('cpChanelType').value){
				msg+="渠道类型不能为空</br>";
			}
			if(""==document.getElementById('bizType').value){
				msg+="业务类型不能为空</br>";
			}
			if(""==document.getElementById('contType').value){
				msg+="内容类型不能为空</br>";
			}
			if("<%=Constant.TYPE_CONSULT%>"==document.getElementById('bizType').value){
				//为咨询
				if(""==document.getElementById('bizCustomerType').value){
					msg+="咨询客户类型不能为空</br>";
				}
				if(""==document.getElementById('knownChanel').value){
					msg+="了解渠道类型不能为空</br>";
				}
			}
			if(""==document.getElementById('ctmname').value){
				msg+="客户名称不能为空</br>";
			}
// 			if(""==document.getElementById('tele').value){
// 				msg+="联系电话不能为空</br>";
// 			}
			
			if("<%=Constant.TYPE_CONSULT%>"==document.getElementById('bizType').value){
				if("<%=Constant.BIZ_CUSTOMER_COMPANY_TYPE%>"==document.getElementById('bizCustomerType').value){
					//为潜在商家
					if(""==document.getElementById('bussnessType').value){
						msg+="商家类型不能为空</br>";
					}
				}
			}
			
			
			if(""==document.getElementById('pro').value){
				msg+="省份不能为空</br>";
			}
			if(""==document.getElementById('citysel').value){
				msg+="城市不能为空</br>";
			}
			
			if(""==document.getElementById('complaintContent').value){
				msg+="投诉或咨询内容不能为空</br>";
			}
			if("<%=Constant.TYPE_CONSULT%>"==document.getElementById('bizType').value){
				//为咨询
				if(""==document.getElementById('dealModel').value){
					msg+="处理方式不能为空</br>";
				}
// 				if(""==document.getElementById('rcont').value){
// 					msg+="回复内容不能为空</br>";
// 				}
			}
			if("<%=Constant.TYPE_COMPLAIN%>"==document.getElementById('bizType').value 
					|| (("<%=Constant.TYPE_CONSULT%>"==document.getElementById('bizType').value) && 
					"<%=Constant.CONSULT_PROCESS_WAIT%>"==document.getElementById('dealModel'))){
				var choise = "";
				var temp=document.getElementsByName("choise");
				 for (i=0;i<temp.length;i++){
					  if(temp[i].checked){
						  choise = temp[i].value;
						  break;
					  }
				 }
				if(choise == "1"){
					//为车厂
					if(""==document.getElementById('cpObject').value){
						msg+="处理部门不能为空</br>";
					}
				} else {
					//为经销商
					if(""==document.getElementById('dept').value){
						msg+="处理大区不能为空</br>";
					}
				}
				if(""==document.getElementById('dealUser').value){
					msg+="责任人不能为空</br>";
				}
			}
			
			if(msg!=""){
				MyAlert(msg);
				return false;
			}else{
				return true;
			}
		}
		
		//   判断长度是否合格 
		// 
		// 引数 s   传入的字符串 
		//   n   限制的长度n以下 
		function WidthCheck(s, n){   
			var w = 0;   
			for (var i=0; i<s.length; i++) {   
			   var c = s.charCodeAt(i);   
			   //单字节加1   
			   if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) {   
			    w++;   
			   }   
			   else {   
			    w+=2;   
			   }   
			}   
			if (w > n) {   
			   return false;   
			}   
			return true;   
		}
	
		function changeCityEvent(value,defaultValue,isdisabled){
			if(''!=value){
				makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeRegion.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeCityBack,'fm','');
			}else{
				resetSelectOption('citysel',null,null,null,null,null);
			}
		}

		//城市级联回调方法：
		function changeCityBack(json) {
			resetSelectOption('citysel',json.regionList,'REGION_NAME','REGION_CODE',json.defaultValue,json.isdisabled);
		}
		
		function changeModelEvent(value,defaultValue,isdisabled){
			if(''!=value){
				makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeVhclMaterialGroup.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeVhclMaterialGroupBack,'fm','');
			}else{
				resetSelectOption('model',null,null,null,null,null);
			}
		}

		//车型级联回调方法：
		function changeVhclMaterialGroupBack(json) {
			//json.defaultValue 修改了默认值,json.isdisabled 修改了置灰
			resetSelectOption('model',json.vhclList,'GROUPNAME','GROUPID',json.defaultValue,json.isdisabled);
		}
		
		function changeOrgSmallOrgEvent(value,defaultValue,isdisabled){
			if(''!=value){
				makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/cascadeOrgSmallOrg.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeOrgSmallOrgEventBack,'fm','');
			}else{
				resetSelectOption('vcPro',null,null,null,null,null);
				resetSelectOption('vehicleCompany',null,null,null,null,null);
			}
		}
		//大区级联小区回调方法：
		function changeOrgSmallOrgEventBack(json) {
			resetSelectOption('vcPro',json.orgProList,'ORG_NAME','ORG_ID',json.defaultValue,json.isdisabled);
			resetSelectOption('vehicleCompany',null,null,null,null,null);
		}
		
		// 艾春 9.13 修改城市回调方法
		function changeMyCityEvent(value,defaultValue,isdisabled){
			if(''!=value){
				makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeCity.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeMyCityBack,'fm','');
				resetSelectOption('vehicleCompany',null,null,null,null,null);
			}else{
				resetSelectOption('myCity',null,null,null,null,null);
				resetSelectOption('vehicleCompany',null,null,null,null,null);
			}
		}
		//经销商回调方法：
		function changeMyCityBack(json) {
			resetSelectOption('myCity',json.myCityList,'CITY_NAME','CITY_ID',json.defaultValue,json.isdisabled);
		}
		
		// 艾春 9.13 修改经销商回调方法
		function changeVcProEvent(value,defaultValue,isdisabled){
			if(''!=value){
				makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeOrgDealer1.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeVcProBack,'fm','');
			}else{
				resetSelectOption('vehicleCompany',null,null,null,null,null);
			}
		}
		//经销商回调方法：
		function changeVcProBack(json) {
			resetSelectOption('vehicleCompany',json.dealerList,'DEALER_NAME','DEALER_ID',json.defaultValue,json.isdisabled);
		}
		
		function changeBizTypeEvent(value,defaultValue,isdisabled){
			makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeBizContent.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeBizTypeBack,'fm','');
			//隐藏或显示
			//投诉显示
			if(value == <%=Constant.TYPE_COMPLAIN%>){
				isDisplay(true);
			//咨询隐藏
			}else {
				isDisplay(false);
			}
			document.getElementById('rcontTr').style.display = 'none';
			//document.getElementById('dealuserTr').style.display = 'none';
		}
		//内容类型回调方法：
		function changeBizTypeBack(json) {
			resetSelectOptionConsult('contType',json.bclist,'CODEDESCVIEW','CODEID',json.defaultValue,json.isdisabled);
			//删除已处理
			for (var i=0;i<json.processList.length;i++){
				if(json.processList[i].CODE_ID ==<%=Constant.CONSULT_PROCESS_FINISH%>){
					json.processList.splice(i,1);
				}
			}
			resetSelectOption('dealModel',json.processList,'CODE_DESC','CODE_ID',json.defaultValue,json.isdisabled);
		}
		function changeDealModelEvent(value){
			//现场处理
			if(value == <%=Constant.CONSULT_PROCESS_SPOT%>){
				//document.getElementById('rcontTr').style.display = '';
				//document.getElementById('dealuserTr').style.display = 'none';
				//隐藏处理部门
				document.getElementById('complaintTrTwo').style.display = 'none';
			}else if(value == <%=Constant.CONSULT_PROCESS_WAIT%>){
				//document.getElementById('rcontTr').style.display = '';
				//document.getElementById('dealuserTr').style.display = '';
				document.getElementById('complaintTrTwo').style.display = '';
			}else{
				//document.getElementById('rcontTr').style.display = 'none';
				//document.getElementById('dealuserTr').style.display = 'none';
				document.getElementById('complaintTrTwo').style.display = '';
			}
		}
		//重置下拉框数据 特殊内容类型处理
		function resetSelectOptionConsult(id,maps,dataName,dataValue,dataId,isdisabled){
			clearSelectNode(id);
			addSelectNodeConsult(id,maps,dataName,dataValue,dataId,isdisabled);
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
			if(maps != null){
				for(var i = 0; i<maps.length;i++){
					if((maps[i])['' +dataValue+''] == dataId){
						document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,true));
					}
					else{
						document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,false));
					}
				}
			}
			
			if(isdisabled == 'true' || isdisabled == true){
				document.getElementById(id).disabled = "disabled";
			}else{
				document.getElementById(id).disabled = "";
			}
			
		}
		
		//特殊内容类型处理
		function addSelectNodeConsult(id,maps,dataName,dataValue,dataId,isdisabled){
			document.getElementById(id).options.add(new Option('-请选择-',''));
			if(maps != null){
				for(var i = 0; i<maps.length;i++){
					if((maps[i])['' +dataValue+''] == dataId){
						var option = new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,true);
						if((maps[i])['MAXLEV'] != (maps[i])['LEV']){
							option.disabled = true;
						}
						document.getElementById(id).options.add(option);
					}
					else{
						var option = new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,false);
						//MyAlert((maps[i])['MAXLEV'] +"...."+(maps[i])['LEV']);
						if((maps[i])['MAXLEV'] != (maps[i])['LEV']){
							option.disabled = true;
						}
						document.getElementById(id).options.add(option);
					}
				}
			}
			
			if(isdisabled == 'true' || isdisabled == true){
				document.getElementById(id).disabled = "disabled";
			}else{
				document.getElementById(id).disabled = "";
			}
			
		}
		
		function checkedSelect(id,dataId,isdisabled){
			var obj = document.getElementById(id);
			for(var i=0;i<obj.options.length;i++){
				if(obj.options[i].value == dataId) obj.options[i].selected = 'selected';
			}
			obj.disabled = isdisabled;
		}
		
		function alertCusScreenEvent(){
			openWindowDialog('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAccept/showCustomList.do');
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
		
		function changeData(ctmName,phone,vin,salesaddress,seriesid,modelid,pudate,pdate,mileage,mileageRange,province,city,ctmId,engineNo){
			setTextData('ctmname',checkNull(ctmName),false);
			setTextData('tele',checkNull(phone),false);
// 			setTextData('teleStr',checkNull(phone),true);
			setTextData('incommingPhone',checkNull(phone),false);
			setTextData('vin',checkNull(vin),true);
			setTextData('vinStr',checkNull(vin),false);
			setTextData('pudate',checkNull(pudate),true);
			setTextData('pdate',checkNull(pdate),true);
			setTextData('mileage',checkNull(mileage),false);
			setTextData('engineNo',checkNull(engineNo),false);
			setTextData('engineNoStr',checkNull(engineNo),true);
			
			checkedSelect('mileageRange',checkNull(mileageRange),false);
			checkedSelect('vinuse',checkNull(salesaddress),false);
			checkedSelect('pro',checkNull(province),false);
			

			//级联相关省份的城市
			if(province!=null&&province!=''&&province!='null'){
				changeCityEvent(checkNull(province),checkNull(city),false);
			}
			//艾春9.25添加赋值车种
			checkedSelect('ser',seriesid,true);
			
			//级联相关车种的车型  // 2013-11-21 wangmiing 通过车种级联车型
			if(seriesid!=null&&seriesid!=''&&seriesid!='null'){
				changeModelEvent(checkNull(seriesid),checkNull(modelid),true);
			}
		}
		
		function checkNull(str){
			if(str == '' || str == 'null' || str == null) {
				str = '';
			}
			return str;
		}
		function setTextData(id,value,isdisabled){
			if(value == null) {
				value = ""; 
				retrun;
			}
			
			document.getElementById(id).value = value;
			document.getElementById(id).disabled = isdisabled;
		}
		
		function setHiddenVIN(value,id){
			document.getElementById(id).value = value;
		}
		
		//清空数据 及显灰
		function clearDate(){
			setTextData('ctmname','',false);
			setTextData('tele','',false);
			setTextData('vin','',false);
			setTextData('vinStr','',false);
			setTextData('mileage','',false);
			setTextData('pudate','',true);
			setTextData('pdate','',true);
			setTextData('complaintContent','',false);
			
			checkedSelect('mileageRange','',false);
			checkedSelect('faultPart','',false);
			checkedSelect('vinuse','',false);
			checkedSelect('pro','',false);
			checkedSelect('citysel','',false);
			checkedSelect('ser','',false);
			checkedSelect('model','',false);
			checkedSelect('bizType','',false);
			checkedSelect('dealModel','',false);
			checkedSelect('cplevel','',false);
			checkedSelect('cplimit','',false);
			checkedSelect('contType','',false);
			//checkedSelect('cpObject','',false);
			checkedSelect('vcPro','',false);
			checkedSelect('vehicleCompany','',false);
			checkedSelect('dealModel','',false);
			checkedSelect('myCity','',false);
			//checkedSelect('orgObj','',false);
			//checkedSelect('dealuser','',false);
			setTextData('rcont','',false);
			changeBizTypeEvent('','',false);
			resetSelectOption('model',null,null,null,null,null);
			resetSelectOption('citysel',null,null,null,null,null);
			resetSelectOption('dealModel',null,null,null,null,null);
		}
		//是否显示  isDisplay->true 显示 false 隐藏
		function isDisplay(isDisplay){
			document.getElementById('tr_vichle_info').style.display='inline';
			document.getElementById('tr_vichle_date').style.display='inline';
			document.getElementById('gzlc').style.display = 'inline';
			if(isDisplay){
				document.getElementById('complaintTrOne').style.display = '';
				//document.getElementById('complaintTrTwo').style.display = '';
				document.getElementById('contTypeRed').style.display = 'none';
				document.getElementById('seriIdRed').style.display = '';
				document.getElementById('modelIdRed').style.display = '';
				document.getElementById('td_deal_status_label').style.display = '';
				document.getElementById('td_deal_status').style.display = '';
				document.getElementById('tr_complaint_customer_type').style.display='none';//投诉的时候隐藏客户咨询类型、商家类型
				document.getElementById('tr_complaint_bussness_type').style.display='none';//投诉的时候隐藏商家类型
				document.getElementById('tr_complaint_bussness_area').style.display='none';//投诉的时候隐藏商家所在区域 
				
				document.getElementById('tr_complaint_customer_type').disabled='disabled';//投诉的时候隐藏客户咨询类型、商家类型
				document.getElementById('tr_complaint_bussness_type').disabled='disabled';//投诉的时候隐藏商家类型
				document.getElementById('tr_complaint_bussness_area').disabled='disabled';//投诉的时候隐藏商家所在区域 
				document.getElementById('tr_consult_deal_type').style.display='none';
				//document.getElementById('gzlc').style.display = '';
			}else{
				document.getElementById('complaintTrOne').style.display = 'none';
				//document.getElementById('complaintTrTwo').style.display = 'none';
				document.getElementById('contTypeRed').style.display = '';
				document.getElementById('seriIdRed').style.display = 'none';
				document.getElementById('modelIdRed').style.display = 'none';
				document.getElementById('td_deal_status_label').style.display = 'none';
				document.getElementById('td_deal_status').style.display = 'none';
				
				document.getElementById('tr_complaint_customer_type').disabled='';//投诉的时候隐藏客户咨询类型、商家类型
				//document.getElementById('tr_complaint_bussness_type').disabled='';//投诉的时候隐藏商家类型
				//document.getElementById('tr_complaint_bussness_area').disabled='';//投诉的时候隐藏商家所在区域 
				document.getElementById('tr_complaint_customer_type').style.display='';//投诉的时候隐藏客户咨询类型、商家类型
				//document.getElementById('tr_complaint_bussness_type').style.display='';//投诉的时候隐藏商家类型
				//document.getElementById('tr_complaint_bussness_area').style.display='';//投诉的时候隐藏商家所在区域 
				document.getElementById('tr_consult_deal_type').style.display='inline';
				//document.getElementById('gzlc').style.display = 'none';
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
	function customerTypeChange(obj){
		var incomingType= document.getElementById("bizType").value;
		if(incomingType=="9506"){//针对类型为咨询 
			if(obj.value==<%=Constant.BIZ_CUSTOMER_COMPANY_TYPE%>){//针对咨询类型如果是潜在 商家
				document.getElementById('tr_complaint_bussness_type').style.display='';//显示
				document.getElementById('tr_complaint_bussness_area').style.display='';//显示商家所在区域 
				
				document.getElementById('tr_complaint_bussness_type').disabled='';//投诉的时候隐藏商家类型
				document.getElementById('tr_complaint_bussness_area').disabled='';//投诉的时候隐藏商家所在区域 
	
				document.getElementById('alertCusScreen').style.display='none';
				document.getElementById('clearCont').style.display='none';
				document.getElementById('tr_vichle_info').style.display='none';
				document.getElementById('tr_vichle_date').style.display='none';
				document.getElementById('gzlc').style.display = 'none';
			}else if(obj.value==<%=Constant.BIZ_CUSTOMER_BUY_TYPE%>){//已购车客户从系统选择客户
				document.getElementById('alertCusScreen').style.display='inline';
				document.getElementById('clearCont').style.display='inline';
				document.getElementById('tr_vichle_info').style.display='inline';
				document.getElementById('tr_vichle_date').style.display='inline';
				document.getElementById('gzlc').style.display = 'inline';
			}else{
				document.getElementById('tr_complaint_bussness_type').style.display='none';//投诉的时候隐藏商家类型
				document.getElementById('tr_complaint_bussness_area').style.display='none';//投诉的时候隐藏商家所在区域 
				
				document.getElementById('tr_complaint_bussness_type').disabled='disabled';//投诉的时候隐藏商家类型
				document.getElementById('tr_complaint_bussness_area').disabled='disabled';//投诉的时候隐藏商家所在区域 
	
				document.getElementById('alertCusScreen').style.display='none';
				document.getElementById('clearCont').style.display='none';
				document.getElementById('tr_vichle_info').style.display='none';
				document.getElementById('tr_vichle_date').style.display='none';
				document.getElementById('gzlc').style.display = 'none';
			}
		}else{//投诉的时候
				document.getElementById('alertCusScreen').style.display='inline';
				document.getElementById('clearCont').style.display='inline';
				document.getElementById('tr_vichle_info').style.display='inline';
				document.getElementById('tr_vichle_date').style.display='inline';
				document.getElementById('gzlc').style.display = 'inline';
			}
	}
	
	//处理部门选择
	function choiseVm(showVar){
		//清空所有选择框
		document.getElementById("cpObject").options[0].selected = true;
		document.getElementById("dept").options[0].selected = true;
		reasetOption("vcPro");
		reasetOption("myCity");
		reasetOption("vehicleCompany");
		reasetOption("dealUser");
		if(showVar == "1"){
			//显示车厂vcPro myCity vehicleCompany
			document.getElementById("cpObject").style.display = "inline";
			document.getElementById("dept").style.display = "none";
			document.getElementById("vcPro").style.display = "none";
			document.getElementById("myCity").style.display = "none";
			document.getElementById("vehicleCompany").style.display = "none";
		}else{
			//显示经销商选项
			document.getElementById("cpObject").style.display = "none";
			document.getElementById("dept").style.display = "inline";
			document.getElementById("vcPro").style.display = "inline";
			document.getElementById("myCity").style.display = "inline";
			document.getElementById("vehicleCompany").style.display = "inline";
		}
	}
	
	function changeArea(dept){
		resetSelectOption('myCity',null,null,null,null,null);
		resetSelectOption('vehicleCompany',null,null,null,null,null);
		if(dept != null && dept != ""){
			makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/choiseOrg.json?orgId='+dept,changeOrg,'fm','');
		}
	}
	
	function changeOrg(json){
		resetSelectOption('vcPro',json.tmOrgs,'ORG_NAME','ORG_ID',json.defaultValue,json.isdisabled);
	}
	
	function changeDept(dept){
		if(dept != null && dept != ""){
			makeNomalFormCall('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAcceptYx/choiseDept.json?deptId='+dept,setDept,'fm','');
		}
	}
	
	//function addSelectNode(id,maps,dataName,dataValue,dataId,isdisabled){}
	function setDept(json){
		var maps = json.users;
		var dataId = json.defaultValue;
		var dataName = 'NAME';
		var dataValue = 'USERID';
		var id="dealUser";
		document.getElementById(id).options.length = 0;
		document.getElementById(id).options.add(new Option('-请选择-',''));
		if(maps != null){
			for(var i = 0; i<maps.length;i++){
				if((maps[i])['' +dataValue+''] == dataId){
					document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,true));
				}
				else{
					document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,false));
				}
			}
		}
	}
	
	function reasetOption(id){
		document.getElementById(id).options.length=0;
		document.getElementById(id).options.add(new Option('-请选择-',''));
	}
	
	function changeDealer(dealerId){
		if(dealerId != null && dealerId != ""){
			makeNomalFormCall('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAcceptYx/choiseDealer.json?dealerId='+dealerId,setDealer,'fm','');
		}
	}
	
	function setDealer(json){
		resetSelectOption('dealUser',json.dealers,'NAME','USER_ID',json.defaultValue,json.isdisabled);
	}
	
	function getDetail(id){
		if(id != null && id != ""){
			makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/getBizContentDetail.json?id='+id,getDetailBack,'fm','');
		}
	}
	
	function getDetailBack(json){
		document.getElementById("detailContent").innerHTML = json.detail;
	}
	</script>
</body>
</html>
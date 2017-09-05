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
<title>投诉受理处理（管理员）</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
	function watchComplaintSearch(cpid,ctmid){		
		window.open('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintConsult/complaintConsultWatch.do?openPage=1&cpid='+cpid+'&ctmid='+ctmid,"","toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=900") ;
	}
</script>
</head>
<body onload="changeBizTypeEvent('${complaintAcceptMap.BIZTYPE}','${complaintAcceptMap.BIZCONT}',false)">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 来电管理 &gt;来电受理处理</div>
	<form method="post" name = "fm" id="fm">
		<table width="100%" class="tab_edit">
			<th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />来电单信息</th>
			<tr>
				<td width="20%" align="right" >客户名称：</td>
				<td width="30%" align="left" >
					${complaintAcceptMap.CTMNAME} <input class="normal_btn" type="button" value = "查看" onclick="watchComplaintSearch('${cpid }','${ctmid}')"/>
				</td>
				<td width="20%" align="right" >联系电话：</td>
				<td width="30%" align="left">
					${complaintAcceptMap.PHONE}
				</td>
			</tr>
			<tr>
				<td align="right" >VIN：</td>
				<td align="left" >
					${complaintAcceptMap.VIN}
				</td>
				<td align="right" >行驶里程：</td>
				<td align="left">
					${complaintAcceptMap.MILEAGE}	
				</td>
			</tr>
			<tr>
				<td align="right" >里程范围：</td>
				<td align="left" >
					${complaintAcceptMap.MILEAGERANGE}
				</td>
				<td align="right" >车辆用途：</td>
				<td align="left">
					${complaintAcceptMap.VINUSER}
				</td>
			</tr>
			<tr>
				<td align="right" >省份：</td>
				<td align="left" >					
					<select id="pro" name="pro" class="short_sel" onchange="changeCityEvent(this.value,'',false)">
						<option value=''>-请选择-</option>
						<c:forEach var="pro" items="${proviceList}">
							<c:choose>
								<c:when test="${complaintAcceptMap.PROCODE == pro.REGION_CODE}">
									<option value="${pro.REGION_CODE}" title="${pro.REGION_NAME}" selected="selected">${pro.REGION_NAME}</option>
								</c:when>
								<c:otherwise>
									<option value="${pro.REGION_CODE}" title="${pro.REGION_NAME}">${pro.REGION_NAME}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<font color="red">*</font>
				</td>
				<td align="right" >城市：</td>
				<td align="left">
					<select id="citysel" name="citysel" class="short_sel">
						<option value=''>-请选择-</option>
						<c:forEach var="city" items="${cityList}">
							<c:choose>
								<c:when test="${complaintAcceptMap.CITYCODE == city.REGION_CODE}">
									<option value="${city.REGION_CODE}" title="${city.REGION_NAME}" selected="selected">${city.REGION_NAME}</option>
								</c:when>
								<c:otherwise>
									<option value="${city.REGION_CODE}" title="${city.REGION_NAME}">${city.REGION_NAME}</option>
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
					${complaintAcceptMap.SGROUP}
				</td>
				<td align="right" >购车日期：</td>
				<td align="left">
					${complaintAcceptMap.BUYDATE}
				</td>
			</tr>
			
			<tr>
				<td align="right" >生产日期：</td>
				<td align="left" >
					${complaintAcceptMap.PRDATE}
				</td>
				<td align="right" >车型：</td>
				<td align="left">
					${complaintAcceptMap.MGROUP}
				</td>
			</tr>


			<tr>
				<td align="right" >业务类型：</td>
				<td align="left" >
					<select id="bizType" name="bizType" class="short_sel"  onchange="changeBizTypeEvent(this.value,'${complaintAcceptMap.BIZCONT}',false)">
						<option value=''>-请选择-</option>
						<c:forEach var="bt" items="${bizTypeList}">
							<c:choose>
								<c:when test="${complaintAcceptMap.BIZTYPE == bt.id}">
									<option value="${bt.id}" title="${bt.name}" selected="selected">${bt.name}</option>
								</c:when>
								<c:otherwise>
									<option value="${bt.id}" title="${bt.name}">${bt.name}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<font color="red">*</font>
				</td>
				<td align="right" >内容类型：</td>
				<td align="left" >
					<select id="contType" name="contType" class="short_sel">
						<option value=''>-请选择-</option>
						<c:forEach var="bc" items="${bclist}">
							<c:choose>
								<c:when test="${complaintAcceptMap.BIZCONT == bc.CODE_ID}">
									<option value="${bc.CODE_ID}" title="${bc.CODE_DESC}" selected="selected">${bc.CODE_DESC}</option>
								</c:when>
								<c:otherwise>
									<option value="${bc.CODE_ID}" title="${bc.CODE_DESC}">${bc.CODE_DESC}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<font color="red">*</font>
				</td>
			</tr>
			
			<tr id="complaintTrOne" style="display: none">
				<td align="right" >抱怨级别：</td>
				<td align="left" >
					<c:choose>
						<c:when test="${empty complaintAcceptMap.CPLEVEL}">
							<script type="text/javascript">
								genSelBoxExp("cplevel",<%=Constant.COMPLAINT_LEVEL%>,'',true,"short_sel",'',"false",'');
							</script>
						</c:when>
						<c:otherwise>
							<script type="text/javascript">
								genSelBoxExp("cplevel",<%=Constant.COMPLAINT_LEVEL%>,${complaintAcceptMap.CPLEVEL},true,"short_sel",${complaintAcceptMap.CPLEVEL},"false",'');
							</script>
						</c:otherwise>
					</c:choose>
					<font color="red">*</font>
				</td>
				<td align="right" >规定处理期限：</td>
				<td align="left" >
					<c:choose>
						<c:when test="${empty complaintAcceptMap.CPLIMIT}">
							<script type="text/javascript">
								genSelBoxExp("cplimit",<%=Constant.RULE_LIMIT%>,'',true,"short_sel",'',"false",'');
							</script>
						</c:when>
						<c:otherwise>
							<script type="text/javascript">
								genSelBoxExp("cplimit",<%=Constant.RULE_LIMIT%>,${complaintAcceptMap.CPLIMIT},true,"short_sel",${complaintAcceptMap.CPLIMIT},"false",'');
							</script>
						</c:otherwise>
					</c:choose>
					<font color="red">*</font>
				</td>
			</tr>
			<tr id="complaintTrTwo" style="display: none">
				<td align="right" >抱怨对象：</td>
				<td align="left"  colspan="3">
					<select id="cpObject1" name="cpObject1" class="short_sel" >
						<option value=''>-请选择-</option>
						
						<c:forEach var="cpObj" items="${cpObjectList}">
							<c:choose>
								<c:when test="${complaintAcceptMap.VCOBJ == cpObj.ORG_ID}">
									<option value="${cpObj.ORG_ID}" title="${cpObj.ORG_NAME}" selected="selected">${cpObj.ORG_NAME}</option>
								</c:when>
								<c:otherwise>
									<option value="${cpObj.ORG_ID}" title="${cpObj.ORG_NAME}">${cpObj.ORG_NAME}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>	
					----	
					<select id="cpObject" name="cpObject" class="short_sel" onchange="changeOrgSmallOrgEvent(this.value,'',false)">
						<option value=''>-请选择-</option>

						<c:forEach var="cpObj" items="${cpObjectOrgList}">
							<c:choose>
								<c:when test="${complaintAcceptMap.COBJ == cpObj.orgId}">
									<option value="${cpObj.orgId}" title="${cpObj.orgName}" selected="selected">${cpObj.orgName}</option>
								</c:when>
								<c:otherwise>
									<option value="${cpObj.orgId}" title="${cpObj.orgName}">${cpObj.orgName}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<select id="vcPro" name="vcPro" class="short_sel" onchange="changeVcProEvent(this.value,'',false)">
						<option value=''>-请选择-</option>
						<c:forEach var="os" items="${cpObjectSmallOrgList}">
							<c:choose>
								<c:when test="${complaintAcceptMap.SOBJ == os.ORG_ID}">
									<option value="${os.ORG_ID}" title="${os.ORG_NAME}" selected="selected">${os.ORG_NAME}</option>
								</c:when>
								<c:otherwise>
									<option value="${os.ORG_ID}" title="${os.ORG_NAME}">${os.ORG_NAME}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<select id="vehicleCompany" name="vehicleCompany">
						<option value=''>-请选择-</option>
						<c:forEach var="vcObj" items="${vcObjList}">
							<c:choose>
								<c:when test="${complaintAcceptMap.VCOBJ == vcObj.DEALER_ID}">
									<option value="${vcObj.DEALER_ID}" title="${vcObj.DEALER_NAME}" selected="selected">${vcObj.DEALER_NAME}</option>
								</c:when>
								<c:otherwise>
									<option value="${vcObj.DEALER_ID}" title="${vcObj.DEALER_NAME}">${vcObj.DEALER_NAME}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right" >抱怨咨询内容：</td>
				<td align="left"  colspan="3">
					<textarea id="complaintContent" name="complaintContent" rows="5" style="width: 95%;">${complaintAcceptMap.CPCONT}</textarea>
					<font color="red">*</font>
				</td>
			</tr>
			
<%--			<tr id="verifyRTr">--%>
<%--				<td align="right" >审核：</td>--%>
<%--				<td align="left"  colspan="3">--%>
<%--					<input type="radio" id="verifyR1" name="verifyR"  value="95311001" onclick="selectVerifyRadio(this)"/>通过--%>
<%--              		<input type="radio" id="verifyR2" name="verifyR" value="95311002" onclick="selectVerifyRadio(this)"/>不通过--%>
<%--					<font color="red">*</font>--%>
<%--				</td>--%>
<%--			</tr>--%>
			
			<tr id="dealModelTr">
				<td align="right" >处理方式：</td>
				<td align="left" colspan="3">
					<select id="dealModel" name="dealModel" class="short_sel"  onchange="changeDealModelEvent(this.value)">
						<option value=''>-请选择-</option>
						<c:forEach var="dm" items="${dealModelList}">
							<c:choose>
								<c:when test="${complaintAcceptMap.DEALMODE == dm.CODE_ID}">
									<option value="${dm.CODE_ID}" title="${dm.CODE_DESC}" selected="selected">${dm.CODE_DESC}</option>
								</c:when>
								<c:otherwise>
									<option value="${dm.CODE_ID}" title="${dm.CODE_DESC}">${dm.CODE_DESC}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					<font color="red">*</font>
				</td>
			</tr>
			
			<tr style="display: none" id="rcontTr">
				<td align="right" >回复内容：</td>
				<td align="left"  colspan="3">
					<textarea id="ccont" name="ccont" rows="5" style="width: 95%"></textarea>
					<font color="red">*</font>
				</td>
			</tr>
			
			<tr style="display: none" id="dealuserTr">
				<td align="right" >处理部门：</td>
				<td align="left" colspan="3">
					<select id="cpObj" name="cpObj" class="short_sel" onchange="changeDealuserEvent(this.value,'',false)">
						<option value=''>-请选择-</option>
						<c:forEach var="tmOrg" items="${tmOrgList}">
							<option value="${tmOrg.ORG_ID}" title="${tmOrg.ORG_NAME}">${tmOrg.ORG_NAME}</option>
						</c:forEach>
					</select>
					<font color="red">*</font>
					<input type="hidden" value="${complaintAcceptMap.CPID}" id="cpid" name="cpid">
				</td>
			</tr>		
			
			<tr>
				<td colspan="8" align="center">
					<input class="normal_btn" type="button" value="保存" name="saveButton" id="saveButton" onclick="save()" />
					&nbsp;
					<input name="addBtn" type="button" class="normal_btn"  value="返回" onclick="history.back();" />
        		</td>
			</tr>
		</table>
		
	</form>
<script type="text/javascript">
		function check(){
			var msg ="";
			if(""==document.getElementById('pro').value){
				msg+="省份不能为空!</br>"
			}
			if(""==document.getElementById('citysel').value){
				msg+="城市不能为空!</br>"
			}	
			if(""==document.getElementById('bizType').value){
				msg+="业务类型不能为空!</br>"
			}
			if(""==document.getElementById('contType').value){
				msg+="内容类型不能为空!</br>"
			}	
			if(document.getElementById('complaintTrOne').style.display != "none" && ""==document.getElementById('cplevel').value){
				msg+="抱怨级别不能为空!</br>"
			}
			if(document.getElementById('complaintTrOne').style.display != "none" && ""==document.getElementById('cplimit').value){
				msg+="规定处理期限不能为空!</br>"
			}
			if(document.getElementById('complaintTrOne').style.display != "none"){
				if(""!=document.getElementById('vehicleCompany').value && ""!=document.getElementById('cpObject1').value){
					msg+="抱怨对象只能选其一!</br>"
				}
				if(""==document.getElementById('vehicleCompany').value && ""==document.getElementById('cpObject1').value){
					msg+="抱怨对象不能为空!</br>"
				}
			}
			if(""==document.getElementById('complaintContent').value){
				msg+="抱怨咨询内容不能为空!</br>"
			}else if(!WidthCheck(document.getElementById('complaintContent').value, 1000)){
				msg+="抱怨咨询内容太长!</br>"
			}
			if(""==document.getElementById('dealModel').value){
				msg+="处理方式不能为空!</br>"
			}
			if(document.getElementById('dealuserTr').style.display != "none"){
				if(""==document.getElementById('cpObj').value){
					msg+="处理部门不能为空!</br>"
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
	
		function save(){
			//验证
			if(check()){
				document.getElementById("saveButton").disabled = true;
				makeNomalFormCall('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintSearch/ComplaintSearchUpdateSubmit.json',saveBack,'fm','');
			}
		}
		
		function saveBack(json){
			if(json.success != null && json.success=='true'){
				MyAlertForFun("保存成功!",sendPage);
			}else{
				MyAlert("保存失败,请联系管理员!");
			}
			document.getElementById("saveButton").disabled = false;
		}
		
		
		//页面跳转：
		function sendPage(){
			window.location.href = "<%=contextPath%>/customerRelationships/complaintConsult/WaitComplaintSearch/waitComplaintSearchInit.do?flagInit=1";
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
			document.getElementById('dealuserTr').style.display = 'none';
		}
		//内容类型回调方法：
		function changeBizTypeBack(json) {
			resetSelectOption('contType',json.bclist,'CODEDESC','CODEID',json.defaultValue,json.isdisabled);
			var processList = new Array();
			for (var i=0;i<json.processList.length;i++){
				if(json.processList[i].CODE_ID ==<%=Constant.CONSULT_PROCESS_FINISH%> || json.processList[i].CODE_ID ==<%=Constant.COMPLAINT_PROCESS_TURN%>){
					processList.push(json.processList[i]);
				}
			}
			resetSelectOption('dealModel',processList,'CODE_DESC','CODE_ID',json.defaultValue,json.isdisabled);
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
		
		function changeVcProEvent(value,defaultValue,isdisabled){
			if(''!=value){
				makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeOrgDealer.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeVcProBack,'fm','');
			}else{
				resetSelectOption('vehicleCompany',null,null,null,null,null);
			}
		}
		//经销商回调方法：
		function changeVcProBack(json) {
			resetSelectOption('vehicleCompany',json.dealerList,'DEALER_NAME','DEALER_ID',json.defaultValue,json.isdisabled);
		}
		
				
		function changeDealuserEvent(value,defaultValue,isdisabled){
			if(''!=value){
				makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeOrg.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeDealuserEventBack,'fm','');
			}else{
				checkedSelect('dealuser','',false);
			}
		}
		
		function changeDealuserEventBack(json){
			resetSelectOption('dealuser',json.orgUserList,'NAME','USER_ID',json.defaultValue,json.isdisabled);
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
		function changeDealModelEvent(value){
			if(value == <%=Constant.COMPLAINT_PROCESS_TURN%>  || value == <%=Constant.CONSULT_PROCESS_WAIT%>){
				document.getElementById('dealuserTr').style.display = '';
			}else if(value == <%=Constant.CONSULT_PROCESS_FINISH%> ){
				document.getElementById('dealuserTr').style.display = 'none';
			}else{
				document.getElementById('dealuserTr').style.display = 'none';
			}
		}
		
		//是否显示  isDisplay->true 显示 false 隐藏
		function isDisplay(isDisplay){
			if(isDisplay){
				document.getElementById('complaintTrOne').style.display = '';
				document.getElementById('complaintTrTwo').style.display = '';
				document.getElementById('rcontTr').style.display = 'none';
			}else{
				document.getElementById('complaintTrOne').style.display = 'none';
				document.getElementById('complaintTrTwo').style.display = 'none';
				document.getElementById('rcontTr').style.display = '';
			}
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

	</script>
</body>
</html>
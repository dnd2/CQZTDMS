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
	function checkSet(){
		$("contType").disabled = true;
		$("vcPro").disabled = true;
		$("myCity").disabled = true;
		$("vehicleCompany").disabled = true;
		$("cpObject").disabled = true;
		$("cplevel").disabled = true;
		$("bizType").disabled = true;
	}
</script>
</head>
<body >
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系 &gt; 投诉咨询管理 &gt;咨询处理（管理员）</div>
	<form method="post" name = "fm" id="fm">
		<table width="100%" class="tab_edit">
		<input type="hidden" id="cpId" name="cpId" value="${complaintAcceptMap.CPID}">
		<input type="hidden" id="bizCont" name="bizCont" value="${complaintAcceptMap.CPBIZCONTENT}">
		<input type="hidden" id="biztype" name="biztype" value="${complaintAcceptMap.CPBIZTYPE}">
		<input type="hidden" id="id" name="id" value="0">
			<th colspan="5" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />投诉单信息</th>
			<tr>
				<td align="center" width="10%" rowspan="4">客户信息</td>
				<td width="20%" align="right" >投诉咨询单号：</td>
				<td align="left" colspan="3">
					${complaintAcceptMap.CPNO}
				</td>
			</tr>
			<tr>
				<td width="20%" align="right" >客户名称：</td>
				<td width="25%" align="left" >
					${complaintAcceptMap.CTMNAME} <%-- <input class="normal_btn" type="button" value = "查看" onclick="watchComplaintSearch('${cpid }','${ctmid}')"/> --%>
				</td>
				<td width="20%" align="right" >客户电话：</td>
				<td width="25%" align="left">
					${complaintAcceptMap.PHONE}
				</td>
			</tr>
			<tr>
				<td align="right">联系人：</td>
				<td>${complaintAcceptMap.PERSON}</td>
				<td align="right">联系电话：</td>
				<td>${complaintAcceptMap.CPPHONE}</td>
			</tr>
			<tr>
				<td align="right" >省份：</td>
				<td align="left" >
					${complaintAcceptMap.PRO}
				</td>
				<td align="right" >城市：</td>
				<td align="left">
					${complaintAcceptMap.CITY}
				</td>
			</tr>
			<tr>
				<td align="center" rowspan="4">车辆信息</td>
				<td align="right" >VIN：</td>
				<td align="left" >
					${complaintAcceptMap.VIN}
				</td>
				<td width="15%" align="right" >是否有车：</td>
				<td colspan="3" align="left" >
					<c:choose>
						<c:when test="${complaintAcceptMap.CUSTOMER eq '0'}">
						有车
						</c:when>
						<c:otherwise>
						无车
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td align="right" >车辆性质：</td>
				<td align="left">
					${complaintAcceptMap.NATURE}
				</td>
				<td align="right" >行驶里程：</td>
				<td align="left">
					${complaintAcceptMap.MILEAGE}	
				</td>
			</tr>
			<tr>
				<td align="right" >车系：</td>
				<td align="left" >
					${complaintAcceptMap.SERIESID}
				</td>
				<td align="right" >车型：</td>
				<td align="left">
					${complaintAcceptMap.MODELID}
				</td>
				
			</tr>
			<tr>
				<td align="right" >生产日期：</td>
				<td align="left" >
					${complaintAcceptMap.SDATE}
				</td>
				<td align="right" >购车日期：</td>
				<td align="left">
					${complaintAcceptMap.BDATE}
				</td>
			</tr>

			<tr>
				<td valign="middle" align="center" rowspan="2">业务分类</td>
				<td align="right" >业务类型：</td>
				<td align="left">
						${complaintAcceptMap.BIZTYPE}
				</td>
				<td align="right" >内容类型：</td>
				<td align="left" >
					${complaintAcceptMap.BIZCONT}
				</td>
			</tr>
			<tr id="complaintTrOne">
				<td align="right" >复杂度状态：</td>
				<td align="left" colspan="4">
					${complaintAcceptMap.CPLEVEL}
				</td>
			</tr>
			
			<tr id="complaintTrTwo">
				<td align="center" rowspan="1">投诉对象</td>
				<td align="left" colspan="4">
					${complaintAcceptMap.OBJECT}	
				</td>
			</tr>
			<tr>
				<td align="center" >投诉咨询内容</td>
				<td align="left"  colspan="4">
					<textarea id="complaintContent" name="complaintContent" rows="5" style="width: 95%;">${complaintAcceptMap.CPCONT}</textarea>
				</td>
			</tr>
			<tr >
				<td align="center">处理结果</td>
					<td valign="middle" colspan="4">
						<table width="100%" class="voidTab">
								<tr>
							      <th align="center" width="20%">处理时间</th>
							      <th align="center" width="60%">处理内容</th>
							      <th align="center" width="8%">当前处理人</th>
							      <th align="center" width="12%">处理状态</th>
						       </tr>
						            <c:forEach items="${dealRecordList}" var="dealR">
						              <tr>
						              	<td align="center">${dealR.CDDATE}</td>
						              	<td align="left">${dealR.CDCONT}</td>
						              	<td align="center">${dealR.USERNAME}</td>
						              	<td align="center">${dealR.STATUS}</td>
						              	</tr>
						             </c:forEach>
						</table>
					</td>
				</tr>
			<tr id="dealModelTr">
				<td align="center" >处理状态</td>
				<td align="left" colspan="4">
					<select id="dealStatus" name="dealStatus" class="short_sel"  onchange="changeDealModelEvent(this.value)">
						<option value=''>-请选择-</option>
						<option value='0'>处理中</option>
						<option value='1'>已处理</option>
					</select>
					<font color="red">*</font>
				</td>
			</tr>
			
			<tr >
				<td align="center">回复内容</td>
				<td align="left"  colspan="4">
					<textarea id="ccont" name="ccont" rows="5" style="width: 95%">${map.CP_CONTENT }</textarea>
					<font color="red">*</font>
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
			if(""==document.getElementById('bizType').value){
				msg+="业务类型不能为空!</br>"
			}
			if("9506"==document.getElementById('bizType').value && ""==document.getElementById('contType').value ){
				msg+="业务类型为咨询时,内容类型不能为空!</br>"
			}
			if(""==document.getElementById('dealStatus').value){
				msg+="处理状态不能为空!</br>"
			}
			if(""==document.getElementById('ccont').value){
				msg+="回复内容不能为空!</br>"
			}
			if(document.getElementById('complaintTrOne').style.display != "none"){
				if(""!=document.getElementById('vehicleCompany').value && ""!=document.getElementById('cpObject').value){
					msg+="投诉对象只能选其一!</br>"
				}
				if(""==document.getElementById('vehicleCompany').value && ""==document.getElementById('cpObject').value){
					msg+="投诉对象不能为空!</br>"
				}
			}
			if(""==document.getElementById('complaintContent').value){
				msg+="投诉咨询内容不能为空!</br>"
			}else if(!WidthCheck(document.getElementById('complaintContent').value,1000)){
				msg+="投诉咨询内容太长!</br>"
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
				makeNomalFormCall('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAcceptKh/adviceUpdateSubmit.json',saveBack,'fm','');
			}
		}
		
		function saveBack(json){
			if(json.success != null && json.success=='true'){
				MyAlertForFun("保存成功!",goBack);
			}else{
				MyAlert("保存失败,请联系管理员!");
			}
			document.getElementById("saveButton").disabled = false;
		}
		
		//页面跳转：
		function goBack(){
			window.location.href = "<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAcceptKh/getComplaintAcceptKhInit.do";
		}


		function changeCityEvent(value,defaultValue,isdisabled){
			if(''!=value){
				makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeRegion.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeCityBack,'fm','');
			}else{
				resetSelectOption('citysel',null,null,null,null,null);
			}
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
			if(value == <%=Constant.VOUCHER_TYPE_01%>){
				isDisplay(true);
			//咨询隐藏
			}else {
				isDisplay(false);
			}
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
			
			/* if(isdisabled == 'true' || isdisabled == true){
				document.getElementById(id).disabled = "disabled";
			}else{
				document.getElementById(id).disabled = "";
			} */
			
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
		
		//是否显示  isDisplay->true 显示 false 隐藏
		function isDisplay(isDisplay){
			if(isDisplay){
				document.getElementById('complaintTrOne').style.display = '';
				document.getElementById('complaintTrTwo').style.display = '';
				document.getElementById('complaintTrThree').style.display = '';
				document.getElementById('contTypeRed').style.display = 'none';
				document.getElementById('seriIdRed').style.display = '';
				document.getElementById('modelIdRed').style.display = '';
				document.getElementById('td_deal_status_label').style.display = '';
				document.getElementById('td_deal_status').style.display = '';
			}else{
				document.getElementById('complaintTrOne').style.display = 'none';
				document.getElementById('complaintTrTwo').style.display = 'none';
				document.getElementById('complaintTrThree').style.display = 'none';
				document.getElementById('contTypeRed').style.display = '';
				document.getElementById('seriIdRed').style.display = 'none';
				document.getElementById('modelIdRed').style.display = 'none';
				document.getElementById('td_deal_status_label').style.display = 'none';
				document.getElementById('td_deal_status').style.display = 'none';
			}
		}
	</script>
</body>
</html>
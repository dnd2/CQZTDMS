<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<% Map<String,Object> tempMap = (Map<String,Object>)request.getAttribute("complaintConsult"); 
String ctmid = (String)CommonUtils.getDataFromMap(tempMap,"CTMID");
if(ctmid == null || "".equals(ctmid)) ctmid="null";
String vin = (String)CommonUtils.getDataFromMap(tempMap,"VIN");
if(vin == null || "".equals(vin)) vin = "null";
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>来电处理界面 </title>
<link href="../../../style/content.css" rel="stylesheet" type="text/css" />
<link href="../../../style/calendar.css" type="text/css" rel="stylesheet" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<body onload="initPage();">
<div class="wbox">
<div class="navigation"><img src="../../../img/nav.gif" width="11" height="11" />&nbsp;当前位置：  来电管理 &gt;来电处理界面 </div>
</div>
<form method="post" name = "fm" id="fm">
<input type="hidden" id="logOnUserType" name="logOnUserType" value="${logOnUserType }" />
<input type="hidden" id="cpStatus" name="cpStatus" value="${cpStatus }" />
<input type="hidden" id="orgId" name="orgId" value="${orgId }" />
<input type="hidden" id="isEdit" name="isEdit" value="${isEdit }" />
<input type="hidden" id="flag" name="flag" />
<div id="res">
  <table id="complaintTable" width="100%">
    <tbody>
      <tr>
        <td><table id="Table2" width="100%" class="tab_edit">
          <tbody>
          	<tr>
		        <th align="center" style="width: 95%;font-size: 14px;" colspan="6">抱怨咨询详细信息 </th>
		      </tr>
            <tr>
              <td rowspan="8" width="2%" align="center">1 </td>
              <td rowspan="8" width="2%" align="center">受理来电基础信息 </td>
              <td width="19%" align="right">编号： </td>
              <td colspan="3"><%=CommonUtils.getDataFromMap(tempMap,"CPNO")%></td>
              
            </tr>
            <tr>
              <td height="140" width="17%" align="right">来电内容： </td>
              <td colspan="3"><%=CommonUtils.getDataFromMap(tempMap,"CPCONT")%> </td>
            </tr>
            <tr>
              <td height="140" width="17%" align="right">受理人反馈信息： </td>
              <td colspan="3"><%=CommonUtils.getDataFromMap(tempMap,"CPSEATCOMMENT")%> </td>
            </tr>
            <tr>
              <td align="right"> 客户姓名： </td>
              <td colspan="3"><a href="<%=contextPath%>/customerRelationships/clientManage/ClientInforManage/watchClientInfor.do?ctmid=<%=ctmid%>&vin=<%=vin%>&cpid=<%=CommonUtils.getDataFromMap(tempMap,"CPID")%>" target="blank"><%=CommonUtils.getDataFromMap(tempMap,"CTMNAME")%></a></td>
            </tr>
            <tr>
              <td align="right">联系电话： </td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"CPPHONE")%></td>
              <td align="right">VIN号/车牌号： </td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"VIN")%></td>
            </tr>
            <tr>
              <td align="right">省份： </td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"PRO")%></td>
              <td align="right">城市： </td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"CITY")%></td>
            </tr>
            <tr>
              <td align="right"> 行驶里程： </td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"MILEAGE")%></td>
              <td align="right"> 车辆用途： </td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"VINUSE")%></td>
            </tr>
            <tr>
              <td align="right">车型： </td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"MNAME")%></td>
              <td align="right">购车日期： </td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"BUYDATE")%></td>
            </tr>
            <tr>
              <td align="right">记录人： </td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"ACCISER")%></td>
              <td align="right">记录时间： </td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"ACCDATE")%></td>
            </tr>
            <tr>
              <td rowspan="2" align="center">2 </td>
              <td rowspan="2" align="center">抱怨受理</td>
              <td align="right">来电询问类型： </td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"BIZCONT")%> </td>
              <td align="right">故障部件： </td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"FAULTP")%></td>
            </tr>
            <tr>
              <td align="right">投诉级别：</td>
              <td  ><%=CommonUtils.getDataFromMap(tempMap,"CPLEV")%></td>
              <td align="right">处理部门：</td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"ORGNAME")%> </td>
            </tr>
            <tr style='display:none'>
              <td align="right">规定处理期限(天)： </td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"CPLIM")%></td>
              <td align="right">规定关闭时间：</td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"SETCLOSEDATE")%></td>
            </tr>
            <tr style='display:none'>
              <td align="right">转出人：</td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"TURNUSER")%></td>
              <td align="right">转出时间：</td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"TURNDATE")%></td>
            </tr>
            <tr id="fankui">
              <td align="center">3 </td>
              <td align="center">处理结果和反馈确认</td>
              <td valign="top" colspan="4">
              	<table width="100%" class="tab_edit">
              		<tr>
	              		<th align="center" width="20%">处理时间</th>
	              		<th align="center" width="40%">处理内容</th>
	              		<th align="center" width="10%">反馈人</th>
	              		<th align="center" width="10%">责任人</th>
	              		<th align="center" width="20%">下一处理部门</th>
              		</tr>
              		<c:forEach items="${dealRecordList}" var="dealR">
              			<tr>
              				<td align="center">${dealR.CDDATE}</td>
<%--               				<c:choose> --%>
<%-- 								<c:when test="${logonUserID == dealR.CDUSERID}"> --%>
<%-- 									<td align="center"><textarea name="${dealR.CDID}" style="width: 95%" >${dealR.CDCONT}</textarea></td> --%>
<%-- 								</c:when> --%>
<%-- 								<c:otherwise> --%>
									<td align="center">${dealR.CDCONT}</td>
<%-- 								</c:otherwise> --%>
<%-- 							</c:choose> --%>
              				<td align="center">${dealR.USERNAME}</td>
              				<td align="center">${dealR.RESPONSORNAME}</td>
              				<td align="center">${dealR.NEXTNAME}</td>
              			</tr>
              		</c:forEach>
              	</table>
              </td>
            </tr>
            <tr id="huifang">
              <td align="center">4 </td>
              <td align="center">用户回访 </td>
              <td valign="top" colspan="4">
              	<table width="100%" class="tab_edit">
              		<tr>
	              		<th width="20%">回访时间</th>
	              		<th width="50%">回访内容</th>
	              		<th width="10%">回访结果</th>
	              		<th width="20%">回访人</th>
              		</tr>
              		<c:forEach items="${returnRecordList}" var="returnR">
              			<tr>
              				<td align="center"><input type="hidden" name="crId" value="${returnR.CRID}"/> ${returnR.CRDATE}</td>
<%--               				<td align="center"><textarea name="content" style="width: 95%">${returnR.CRCONT}</textarea></td> --%>
							<td align="center">${returnR.CRCONT}</td>
              				<td align="center">${returnR.CONFIRMOPTION}</td>
              				<td align="center">${returnR.CRUSER}</td>
              			</tr>
              		</c:forEach>
              	</table>
              </td>
            </tr>
<!--             <tr id="result"> -->
<!--               <td align="center" colspan="2" rowspan="1">回访结果</td> -->
<!--               <td colspan="4"> -->
<!--               	<input type="radio" id="crResult1" name="crResult" onclick="setDeptDisplay('hide');" value="95111001"/>满意 -->
<!--               	<input type="radio" id="crResult2" name="crResult" onclick="setDeptDisplay('hide');" value="95111003"/>不满意 -->
<!--               	<input type="radio" id="crResult3" name="crResult" onclick="setDeptDisplay('show');" value="0"/>转其它部门处理 -->
<!--               </td> -->
<!--             </tr> -->
<!--             <tr id="disposeDept"> -->
<!-- 				<td align="center" colspan="2">转处理部门</td> -->
<!-- 				<td align="left"  colspan="4"> -->
<!-- 					<input type="radio" id="choise1" name="choise" value="1" onclick="choiseVm(1);" checked="true" />车厂 -->
<!-- 					<input type="radio" id="choise2" name="choise" value="2" onclick="choiseVm(2);" />经销商 -->
<!-- 					<select id="cpObject" name="cpObject" class="short_sel" onchange="changeDept(this.value)" > -->
<!-- 						<option value=''>-请选择-</option> -->
<%-- 						<c:forEach var="cpObj" items="${cpObjectList}"> --%>
<%-- 							<option value="${cpObj.ORG_ID}" title="${cpObj.ORG_NAME}">${cpObj.ORG_NAME}</option> --%>
<%-- 						</c:forEach> --%>
<!-- 					</select> -->
<!-- 					大区 -->
<!-- 					<select id="dept" name="dept" class="short_sel" onchange="changeArea(this.value);" style="display:none;"> -->
<!-- 						<option value=''>-请选择-</option> -->
<%-- 						<c:forEach var="deptVal" items="${cpDeptList}"> --%>
<%-- 							<option value="${deptVal.ORG_ID}" title="${deptVal.ORG_NAME}">${deptVal.ORG_NAME}</option> --%>
<%-- 						</c:forEach> --%>
<!-- 					</select> -->
<!-- 					<select id="vcPro" name="vcPro" class="short_sel" onchange="changeMyCityEvent(this.value,'',false)" style="display:none;"> -->
<!-- 						<option value=''>-请选择-</option> -->
<%-- 						<c:forEach var="os" items="${tmOrgSmallList}"> --%>
<%-- 							<option value="${os.orgId}" title="${os.orgName}">${os.orgName}</option> --%>
<%-- 						</c:forEach> --%>
<!-- 					</select> -->
<!-- 					<select id="myCity" name="myCity" class="short_sel" onchange="changeVcProEvent(this.value,'',false)"  style="display:none;"> -->
<!-- 						<option value=''>-请选择-</option> -->
<!-- 					</select> -->
<!-- 					<select id="vehicleCompany" name="vehicleCompany" onchange="changeDealer(this.value);" style="display:none;"> -->
<!-- 						<option value=''>-请选择-</option> -->
<!-- 					</select> -->
<!-- 					<select id="dealUser" name="dealUser"> -->
<!-- 						<option value=''>-请选择-</option> -->
<!-- 					</select> -->
<!-- 					<font color="red">*</font> -->
<!-- 				</td> -->
<!-- 			</tr> -->
			
			<tr id="ccontTr">
				<td align="right" colspan="2">备注</td>
				<td align="left" colspan="4">
					<textarea id="ccont" name="ccont" rows="5" style="width: 99.9%"></textarea>
				</td>
			</tr>
          </tbody>
        </table></td>
      </tr>
      <tr>
        <td align="center">
       		<c:if test="${(complaintConsult.CP_POINT_STATUS=='20231001'||complaintConsult.CP_POINT_STATUS=='20231000'||empty complaintConsult.CP_POINT_STATUS )&&(logOnUserType=='3')&&(complaintConsult.CP_BIZ_TYPE=='9505') }"><input name="is_huifang" type="checkBox" checked id="is_huifang" value="1">是否需要回访</input></c:if>
        	<c:if test="${(complaintConsult.CP_POINT_STATUS=='20231001'||complaintConsult.CP_POINT_STATUS=='20231000'||empty complaintConsult.CP_POINT_STATUS)||(ORGID=='2014050994697313')&&(!empty complaintConsult.CP_DEAL_DEALER )}"><input name="button" type="button" class="long_btn" id="addRecord" onclick="addRecordInfo(<%=CommonUtils.getDataFromMap(tempMap,"CPID")%>);" value="跟进" /></c:if>
        	<input name="button" type="button" class="long_btn"  onclick="history();" value="返回" />
        </td>
      </tr>
    </tbody>
  </table>
</div>
</FORM>
<script type="text/javascript">
	
	//初始化页面
	function initPage(){
		var cpStatus = document.getElementById("cpStatus").value;
		var logOnUserType = document.getElementById("logOnUserType").value;
		var isEdit = document.getElementById("isEdit").value;
		var flag = "";
		if(logOnUserType == "0" && (cpStatus == "<%=Constant.COMPLAINT_DEALER_DEPT_DOING %>" 
				|| cpStatus == "<%=Constant.COMPLAINT_STATUS_DOING %>"
				|| cpStatus == "<%=Constant.COMPLAINT_DEALER_STATUS_DOING %>"
				|| cpStatus == "<%=Constant.COMPLAINT_STATUS_WAIT_FEEDBACK %>"
				|| cpStatus == "<%=Constant.COMPLAINT_STATUS_DOING_ALREADY %>")){
			//为呼叫监控部
			document.getElementsByName("crResult")[0].checked = true;
			document.getElementById("disposeDept").style.display = "none";
			flag = "0";
		} else if(logOnUserType == "1" && isEdit == "1" && (cpStatus == "<%=Constant.COMPLAINT_DEALER_DEPT_DOING %>" 
				|| cpStatus == "<%=Constant.COMPLAINT_STATUS_DOING %>"
				|| cpStatus == "<%=Constant.COMPLAINT_DEALER_STATUS_DOING %>"
				|| cpStatus == "<%=Constant.COMPLAINT_STATUS_DOING_ALREADY %>")){
			//部门登录
			document.getElementById("huifang").style.display = "none";
			document.getElementById("result").style.display = "none";
// 			document.getElementById("disposeDept").style.display = "";
			flag = "1";
		}else if(logOnUserType == "2" && isEdit == "1" && (cpStatus == "<%=Constant.COMPLAINT_STATUS_DOING %>"
				|| cpStatus == "<%=Constant.COMPLAINT_DEALER_STATUS_DOING %>")){
			//大区登录
			document.getElementById("huifang").style.display = "none";
			document.getElementById("result").style.display = "none";
			var orgId = document.getElementById("orgId").value;
			if(orgId != null){
				var dept = document.getElementById("dept");
				for(var i = 0;i < dept.length;i++){
					if(dept[i].value == orgId){
						dept[i].selected = true;
						changeArea(orgId);
						break;
					}
				}
			}
			flag = "2";
		}else if(logOnUserType == "3" && isEdit == "1" && cpStatus == "<%=Constant.COMPLAINT_DEALER_STATUS_DOING %>"){
			//经销商登录并且经销商处理
			document.getElementById("huifang").style.display = "none";
			document.getElementById("result").style.display = "none";
			document.getElementById("choise2").disabled = "disabled";
			document.getElementById("disposeDept").style.display = "none";
			document.getElementById("three_package_set_btn").style.display = "none";
			
			flag = "3";
		} else if(cpStatus == "<%=Constant.COMPLAINT_STATUS_ALREADY_CLOSE %>"
				|| cpStatus == "<%=Constant.COMPLAINT_STATUS_WAIT_CLOSE %>"
				|| cpStatus == "<%=Constant.COMPLAINT_STATUS_CLOSE %>") {
			//查看
			document.getElementById("result").style.display = "none";
			document.getElementById("disposeDept").style.display = "none";
// 			document.getElementById("ccontTr").style.display = "none";
			document.getElementById("three_package_set_btn").style.display = "none";
// 			MyAlert("addRecord");
// 			document.getElementById("addRecord").style.display = "none";
		} else {
			//跟进
			document.getElementById("huifang").style.display = "none";
			document.getElementById("result").style.display = "none";
			document.getElementById("disposeDept").style.display = "none";
			document.getElementById("three_package_set_btn").style.display = "none";
			flag = "4";
		}
		document.getElementById("flag").value = flag;
	}
	
	function check(){
		var msg ="";
		var crResult = "";
		var temp=document.getElementsByName("crResult");
		for (i=0;i<temp.length;i++){
			if(temp[i].checked){
				crResult = temp[i].value;
				break;
			}
		}
		if(crResult == "0"){
			//转其他部门
			var choise = document.getElementsByName("choise");
			var bm = "";
			for (i=0;i<choise.length;i++){
				if(choise[i].checked){
					bm = choise[i].value;
					break;
				}
			}
			if(bm == "1"){
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
		} else {
			if(""==document.getElementById('ccont').value){
				msg+="备注信息不能为空</br>";
			}
		}

		if(msg!=""){
			MyAlert(msg);
			return false;
		}else{
			return true;
		}
	}
	
	function changeOrgUserEvent(value,defaultValue,isdisabled){
		if(''!=value){
			makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/getSeatUser.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeDealuserEventBack,'fm','');
		}else{
			resetSelectOption('userid',null,null,null,null,null);
		}
	}
	
		
	function changeDealuserEventBack(json){
		resetSelectOption('userid',json.seatList,'USERNANE','USERID',json.defaultValue,json.isdisabled);
	}
	function clearSelectNode(id) {			
		document.getElementById(id).options.length=0; 			
	}
	
	function resetSelectOption(id,maps,dataName,dataValue,dataId,isdisabled){
		clearSelectNode(id);
		addSelectNode(id,maps,dataName,dataValue,dataId,isdisabled);
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

	//保存
	function save(cpid){
		if(check()){
   			makeNomalFormCall('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintSearchYx/ComplaintSearchUpdateSubmit.json?cpid='+cpid,saveBack,'fm','');
		}
	}
	
	//跟进
	function addRecordInfo(cpid){
// 		if(document.getElementById("logOnUserType").value=="3"){
			if(document.getElementById("ccont").value==""){
				MyAlert("请输入备注");
				return false;
			}
// 		}
		document.getElementById("flag").value = "4";
		makeNomalFormCall('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintSearchYx/ComplaintSearchUpdateSubmit.json?cpid='+cpid,saveBack,'fm','');
	}
	
	function saveBack(json){
		if(json.success != null && json.success == '1'){
			MyAlertForFun("保存成功!",sendPage);
		}else if(json.success != null && json.success == '2'){
			sendPage();
		}else{
			MyAlert("保存失败,请联系管理员!");
		}
	}
	//页面跳转：
	function sendPage(){
		window.location.href = "<%=contextPath%>/customerRelationships/complaintConsult/WaitComplaintSearchYx/waitComplaintSearchInit.do?flagInit=1";
		//window.location.href = "<%=contextPath%>/customerRelationships/complaintConsult/ComplaintSearchYx/complaintSearchInit.do";
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
	
	function changeMyCityEvent(value,defaultValue,isdisabled){
		if(''!=value){
			makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeCity.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeMyCityBack,'fm','');
			resetSelectOption('vehicleCompany',null,null,null,null,null);
		}else{
			resetSelectOption('myCity',null,null,null,null,null);
			resetSelectOption('vehicleCompany',null,null,null,null,null);
		}
	}
	
	//重置下拉框数据
	function resetSelectOption(id,maps,dataName,dataValue,dataId,isdisabled){
		clearSelectNode(id);
		addSelectNode(id,maps,dataName,dataValue,dataId,isdisabled);
	}
	
	function changeMyCityBack(json) {
		resetSelectOption('myCity',json.myCityList,'CITY_NAME','CITY_ID',json.defaultValue,json.isdisabled);
	}
	
	function changeVcProEvent(value,defaultValue,isdisabled){
		if(''!=value){
			makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeOrgDealer1.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeVcProBack,'fm','');
		}else{
			resetSelectOption('vehicleCompany',null,null,null,null,null);
		}
	}
	
	function changeVcProBack(json) {
		resetSelectOption('vehicleCompany',json.dealerList,'DEALER_NAME','DEALER_ID',json.defaultValue,json.isdisabled);
	}
	
	function setDeptDisplay(isShow){
		if(isShow == "show"){
			document.getElementById("disposeDept").style.display = "inline";
		}else{
			document.getElementById("disposeDept").style.display = "none";
		}
	}
	
</script>
</body>
</html>
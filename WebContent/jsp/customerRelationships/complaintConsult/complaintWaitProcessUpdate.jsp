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
<title>待处理 </title>
<link href="../../../style/content.css" rel="stylesheet" type="text/css" />
<link href="../../../style/calendar.css" type="text/css" rel="stylesheet" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<body onload="loadcalendar();">
<div class="wbox">
<div class="navigation"><img src="../../../img/nav.gif" width="11" height="11" />&nbsp;当前位置：  客户信息 &gt;投诉咨询记录</div>
</div>
<form method="post" name = "fm" id="fm">
<div id="res">
  <table id="complaintTable" width="100%">
    <tbody>
      <tr>
        <td><table id="Table2" width="100%" class="tab_edit">
          <tbody>
          	<tr>
		        <th align="center" style="font-size: 14px;" colspan="6">抱怨咨询详细信息 </th>
		      </tr>
            <tr>
              <td rowspan="8" width="2%" align="center">1 </td>
              <td rowspan="8" width="2%" align="center">受理抱怨咨询 </td>
              <td width="19%" align="right">编号： </td>
              <td colspan="5"><%=CommonUtils.getDataFromMap(tempMap,"CPNO")%></td>
              
            </tr>
            <tr>
              <td height="140" width="17%" align="right">抱怨/咨询内容： </td>
              <td colspan="5"><%=CommonUtils.getDataFromMap(tempMap,"CPCONT")%> </td>
            </tr>
            <tr>
              <td align="right"> 客户姓名： </td>
              <td colspan="3"><a href="<%=contextPath%>/customerRelationships/clientManage/ClientInforManage/watchClientInfor.do?ctmid=<%=ctmid%>&vin=<%=vin%>&cpid=<%=CommonUtils.getDataFromMap(tempMap,"CPID")%>" target="blank"><%=CommonUtils.getDataFromMap(tempMap,"CTMNAME")%></a></td>
            </tr>
            <tr>
              <td align="right">联系电话： </td>
              <td colspan="2"><%=CommonUtils.getDataFromMap(tempMap,"CPPHONE")%></td>
              <td align="right">VIN号/车牌号： </td>
              <td colspan="2"><%=CommonUtils.getDataFromMap(tempMap,"VIN")%></td>
            </tr>
            <tr>
              <td align="right">省份： </td>
              <td colspan="2"><%=CommonUtils.getDataFromMap(tempMap,"PRO")%></td>
              <td align="right">城市： </td>
              <td colspan="2"><%=CommonUtils.getDataFromMap(tempMap,"CITY")%></td>
            </tr>
            <tr>
              <td align="right"> 行驶里程： </td>
              <td colspan="2"><%=CommonUtils.getDataFromMap(tempMap,"MILEAGE")%></td>
              <td align="right"> 车辆用途： </td>
              <td colspan="2"><%=CommonUtils.getDataFromMap(tempMap,"VINUSE")%></td>
            </tr>
            <tr>
              <td align="right">车型： </td>
              <td colspan="2"><%=CommonUtils.getDataFromMap(tempMap,"MNAME")%></td>
              <td align="right">购车日期： </td>
              <td colspan="2"><%=CommonUtils.getDataFromMap(tempMap,"BUYDATE")%></td>
            </tr>
            <tr>
              <td align="right">记录人： </td>
              <td colspan="2"><%=CommonUtils.getDataFromMap(tempMap,"ACCISER")%></td>
              <td align="right">记录时间： </td>
              <td colspan="2"><%=CommonUtils.getDataFromMap(tempMap,"ACCDATE")%></td>
            </tr>
            <tr>
              <td rowspan="6" align="center">2 </td>
              <td rowspan="6" align="center">抱怨受理</td>
              <td align="right">抱怨类型： </td>
              <td colspan="2"><%=CommonUtils.getDataFromMap(tempMap,"BIZCONT")%> </td>
              <td align="right">故障部件： </td>
              <td colspan="2"><%=CommonUtils.getDataFromMap(tempMap,"FAULTP")%></td>
            </tr>
            <tr>
              <td align="right">抱怨级别：</td>
              <td colspan="2" ><%=CommonUtils.getDataFromMap(tempMap,"CPLEV")%></td>
              <td align="right">规定处理期限(天)： </td>
              <td colspan="2"><%=CommonUtils.getDataFromMap(tempMap,"CPLIM")%></td>
            </tr>
            <tr>
              <td align="right">处理部门：</td>
              <td colspan="2"><%=CommonUtils.getDataFromMap(tempMap,"TUCOMP")%> </td>
              <td align="right">规定关闭时间：</td>
              <td colspan="2"><%=CommonUtils.getDataFromMap(tempMap,"SETCLOSEDATE")%></td>
            </tr>
            <tr>
              <td align="right">转出人：</td>
              <td colspan="2"><%=CommonUtils.getDataFromMap(tempMap,"TURNUSER")%></td>
              <td align="right">转出时间：</td>
              <td colspan="2"><%=CommonUtils.getDataFromMap(tempMap,"TURNDATE")%></td>
            </tr>
            <tr id="fankui">
              <td align="center">3 </td>
              <td align="center">处理结果和反馈确认</td>
              <td valign="top" colspan="6">
              	<table  border="1" rules="all" cellspacing="5" bordercolor="#808080" cellpadding="0" width="100%" frame="below" align="center">
              		<tr>
	              		<th align="center" width="20%">处理时间</th>
	              		<th align="center" width="50%">处理内容</th>
	              		<th align="center" width="10%">反馈人</th>
	              		<th align="center" width="20%">当前处理人</th>
              		</tr>
              		<c:forEach items="${dealRecordList}" var="dealR">
              			<tr>
              				<td align="center">${dealR.CDDATE}</td>
              				<td align="center">${dealR.CDCONT}</td>
              				<td align="center">${dealR.USERNAME}</td>
              				<td align="center">${dealR.NEXTNAME}</td>
              			</tr>
              		</c:forEach>
              	</table>
              </td>
            </tr>
            <tr id="huifang">
              <td align="center">4 </td>
              <td align="center">用户回访 </td>
              <td valign="top" colspan="6">
              	<table  border="1" rules="all" cellspacing="5" bordercolor="#808080" cellpadding="0" width="100%" frame="below" align="center">
              		<tr>
	              		<th width="20%">回访时间</th>
	              		<th width="50%">回访内容</th>
	              		<th width="10%">回访结果</th>
	              		<th width="20%">回访人</th>
              		</tr>
              		<c:forEach items="${returnRecordList}" var="returnR">
              			<tr>
              				<td align="center">${returnR.CRDATE}</td>
              				<td align="center">${returnR.CRCONT}</td>
              				<td align="center">${returnR.CONFIRMOPTION}</td>
              				<td align="center">${returnR.CRUSER}</td>
              			</tr>
              		</c:forEach>
              	</table>
              </td>
            </tr>
            <tr id="huifang">
              <td align="center">5 </td>
              <td align="center">申请延期记录 </td>
              <td valign="top" colspan="6">
              	<table width="100%" class="tab_edit">
              		<tr>
	              		<th width="10%">申请延期至时间</th>
	              		<th width="5%">申请天数</th>
	              		<th width="10%">申请类型</th>
	              		<th width="10%">申请内容</th>
	              		<th width="10%">申请人</th>
	              		<th width="10%">申请时间</th>
	              		<th width="10%">审核时间</th>
	              		<th width="10%">审核内容</th>
	              		<th width="10%">审核人</th>
	              		<th width="5%">审核状态</th>
	              		<th width="10%">延期当前处理人</th>
              		</tr>
              		<c:forEach items="${verifyRecordList}" var="verify">
              			<tr>
              				<td align="center">${verify.CLDATE}</td>
              				<td align="center">${verify.DAYS}</td>
              				<td align="center"><script type='text/javascript'>
				              var activityKind=getItemValue('${verify.CPDEFERTYPE}');
				              document.write(activityKind) ;
				             </script></td>
              				<td align="center">${verify.CLCONT}</td>
              				<td align="center">${verify.CLUSER}</td>
              				<td align="center">${verify.CREATEDATE}</td>
              				<td align="center">${verify.CLVERIFYDATE}</td>
              				<td align="center">${verify.CLVERIFYCONTENT}</td>
              				<td align="center">${verify.CLVERIFYUSER}</td>
              				<td align="center">${verify.CLVERIFYSTATUS}</td>
              				<td align="center">${verify.NEXTNAME}</td>
              			</tr>
              		</c:forEach>
              	</table>
              </td>
            </tr>  
            <tr>
           	  <td align="center" colspan="2" >确认意见 </td>
              <td valign="top" colspan="6">
              	<select id="confirmView" name="confirmView" onchange="changeConfirmViewEvent(this.value)">
              		<option value=''>-请选择-</option>
              		<c:forEach items="${confireMessage}" var="cm">
              			<option value='${cm.CODE_ID}' title="${cm.CODE_DESC}">${cm.CODE_DESC}</option>
              		</c:forEach>
              	</select>
              </td>
            </tr>
            
            <tr style="display: none" id="dealuserTr">
				<td align="right" colspan="2" >处理人</td>
				<td colspan="3">
					<select id="orgObj" name="orgObj" class="short_sel" onchange="changeDealuserEvent(this.value,'',false)">
						<option value=''>-请选择-</option>
						<c:forEach var="tmOrg" items="${tmOrgList}">
							<option value="${tmOrg.orgId}" title="${tmOrg.orgName}">${tmOrg.orgName}</option>
						</c:forEach>
					</select>
					<select id="dealuser" name="dealuser" class="short_sel">
						<option value=''>-请选择-</option>
					</select>
				</td>
				<td style="display: none" id="dealeruserid" >
					经销商
					<select id="vcPro" name="vcPro" class="short_sel" onchange="changeVcProEvent(this.value,'',false)">
						<option value=''>-请选择-</option>
						<c:forEach var="os" items="${tmOrgSmallList}">
							<option value="${os.orgId}" title="${os.orgName}">${os.orgName}</option>
						</c:forEach>
					</select>
					<select id="vehicleCompany" name="vehicleCompany" class="short_sel" onchange="changeVehicleCompanyEvent(this.value,'',false)">
						<option value=''>-请选择-</option>
					</select>
					<select id="dealeruser" name="dealeruser" class="short_sel">
						<option value=''>-请选择-</option>
					</select>
				</td>
			</tr>
			
			<tr style="display: none" id="ttCrmSeatsTr">
				<td align="right" colspan="2" >处理人</td>
				<td colspan="3">
					<select id="ttCrmSeatsId" name="ttCrmSeatsId" class="short_sel" >
						<option value=''>-请选择-</option>
						<c:forEach var="cs" items="${ttCrmSeatsList}">
							<option value="${cs.seUserId}" title="${cs.seName}">${cs.seName}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			
			<tr style="display: none" id="ccontTr">
				<td align="right" colspan="2">完结理由：</td>
				<td align="left" colspan="6">
					<textarea id="ccont" name="ccont" rows="5" style="width: 95%"></textarea>
					<font color="red">*</font>
				</td>
			</tr>

          </tbody>
        </table></td>
      </tr>
      <tr>
        <td align="center">
        	<input name="button" type="button" class="long_btn" id="three_package_set_btn" onclick="save(<%=CommonUtils.getDataFromMap(tempMap,"CPID")%>);" value="保存" />
        	<input name="button" type="button" class="long_btn"  onclick="history.back();"  value="返回" />
        </td>
      </tr>
    </tbody>
  </table>
</div>
</form>
<script type="text/javascript">
	function check(){
		var msg ="";
		if(""==document.getElementById('confirmView').value){
			msg+="确认意见不能为空!</br>"
		}
		if(document.getElementById('dealuserTr').style.display != "none"){
			if(document.getElementById('dealeruserid').style.display != "none"){
				if(""==document.getElementById('dealuser').value && ""==document.getElementById('dealeruser').value){
					msg+="处理人和经销商请选其一!</br>"
				}else if(""!=document.getElementById('dealuser').value && ""!=document.getElementById('dealeruser').value){
					msg+="处理人和经销商请选其一!</br>"
				}
			}else{
				if(""==document.getElementById('dealuser').value){
					msg+="处理人不能为空!</br>"
				}
			}		
		}else if(document.getElementById('ttCrmSeatsTr').style.display != "none"){
			if(""==document.getElementById('ttCrmSeatsId').value){
			msg+="处理人不能为空!</br>"
			}
		}
		if(document.getElementById('ccontTr').style.display != "none"){
			if(""==document.getElementById('ccont').value){
				msg+="完结理由不能为空!</br>"
			}else if(!WidthCheck(document.getElementById('ccont').value,1000)){
				msg+="完结理由太长!</br>"
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

	function save(cpid){
		if(check()){
			makeNomalFormCall('<%=contextPath%>/customerRelationships/complaintConsult/WaitComplaintSearch/waitComplaintSearchUpdateSubmit.json?id=waitProcess&cpid='+cpid,saveBack,'fm','');
		}
	}
	
	function saveBack(json){
		if(json.success != null && json.success == 'true'){
			MyAlertForFun("保存成功!",sendPage);
		}else{
			MyAlert("保存失败,请联系管理员!");
		}
	}
	//页面跳转：
	function sendPage(){
		window.location.href = "<%=contextPath%>/customerRelationships/complaintConsult/WaitComplaintSearch/waitComplaintSearchInit.do?flagInit=1";
	}
	
	function changeConfirmViewEvent(value){		
		if(value == <%=Constant.CONFIRE_WAIT_TYPE%>){
			document.getElementById('ccontTr').style.display = 'none';
			document.getElementById('dealeruserid').style.display = 'none';
			document.getElementById('dealuserTr').style.display = 'none';
			document.getElementById('ttCrmSeatsTr').style.display = '';
		}else if(value == <%=Constant.CONFIRE_TURN_TYPE%> ){
			document.getElementById('ccontTr').style.display = 'none';
			document.getElementById('dealeruserid').style.display = 'none';
			document.getElementById('ttCrmSeatsTr').style.display = 'none';
			document.getElementById('dealuserTr').style.display = '';				
		}else if(value == <%=Constant.CONFIRE_RETURN_TYPE%> || value == <%=Constant.CONFIRE_CONTINUE_RETURN_TYPE%>  ){
			document.getElementById('ccontTr').style.display = 'none';
			document.getElementById('dealeruserid').style.display = 'none';
			document.getElementById('dealuserTr').style.display = 'none';
			document.getElementById('ttCrmSeatsTr').style.display = '';
		}else if(value == <%=Constant.CONFIRE_CLOSE_TYPE%> ){
			document.getElementById('ccontTr').style.display = '';
			document.getElementById('dealeruserid').style.display = 'none';
			document.getElementById('dealuserTr').style.display = 'none';
			document.getElementById('ttCrmSeatsTr').style.display = 'none';
		}else{
			document.getElementById('ccontTr').style.display = 'none';
			document.getElementById('dealuserTr').style.display = 'none';
			document.getElementById('dealeruserid').style.display = 'none';
			document.getElementById('ttCrmSeatsTr').style.display = 'none';
		}
	}
	
	function changeDealuserEvent(value,defaultValue,isdisabled){
		if(''!=value){
			makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeOrg.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeDealuserEventBack,'fm','');
		}else{
			resetSelectOption('dealuser',null,null,null,null,null);
		}
	}
	
	function changeDealuserEventBack(json){
		resetSelectOption('dealuser',json.orgUserList,'NAME','USER_ID',json.defaultValue,json.isdisabled);
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
	
	function changeVehicleCompanyEvent(value,defaultValue,isdisabled){
		if(''!=value){
			makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeDealUser.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeVehicleCompanyEventBack,'fm','');
		}else{
			resetSelectOption('dealeruser',null,null,null,null,null);
		}
	}
	//经销商人员回调方法：
	function changeVehicleCompanyEventBack(json) {
		resetSelectOption('dealeruser',json.dealUserList,'NAME','USER_ID',json.defaultValue,json.isdisabled);
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
</script>
</body>
</html>
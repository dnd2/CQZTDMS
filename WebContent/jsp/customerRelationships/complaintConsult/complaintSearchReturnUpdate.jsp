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
<title>管理员回访处理界面 </title>
<link href="../../../style/content.css" rel="stylesheet" type="text/css" />
<link href="../../../style/calendar.css" type="text/css" rel="stylesheet" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<body onload="loadcalendar();">
<div class="wbox">
<div class="navigation"><img src="../../../img/nav.gif" width="11" height="11" />&nbsp;当前位置：  客户信息 &gt;管理员回访处理界面 </div>
</div>
<form method="post" name = "fm" id="fm">
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
              <td rowspan="8" width="2%" align="center">受理抱怨咨询 </td>
              <td width="19%" align="right">编号： </td>
              <td colspan="3"><%=CommonUtils.getDataFromMap(tempMap,"CPNO")%></td>
              
            </tr>
            <tr>
              <td height="140" width="17%" align="right">抱怨/咨询内容： </td>
              <td colspan="3"><%=CommonUtils.getDataFromMap(tempMap,"CPCONT")%> </td>
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
              <td rowspan="4" align="center">2 </td>
              <td rowspan="4" align="center">抱怨受理</td>
              <td align="right">抱怨类型： </td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"BIZCONT")%> </td>
              <td align="right">故障部件： </td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"FAULTP")%></td>
            </tr>
            <tr>
              <td align="right">抱怨级别：</td>
              <td  ><%=CommonUtils.getDataFromMap(tempMap,"CPLEV")%></td>
              <td align="right">规定处理期限(天)： </td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"CPLIM")%></td>
            </tr>
            <tr>
              <td align="right">处理部门：</td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"TUCOMP")%> </td>
              <td align="right">规定关闭时间：</td>
              <td ><%=CommonUtils.getDataFromMap(tempMap,"SETCLOSEDATE")%></td>
            </tr>
            <tr>
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
	              		<th align="center" width="50%">处理内容</th>
	              		<th align="center" width="10%">反馈人</th>
	              		<th align="center" width="20%">当前处理人</th>
              		</tr>
              		<c:forEach items="${dealRecordList}" var="dealR">
              			<tr>
              				<td align="center">${dealR.CDDATE}</td>
              				<c:choose>
								<c:when test="${logonUserID == dealR.USERID}">
									<td align="center"><textarea name="crm_deal_content" style="width: 95%" >${dealR.CDCONT}</textarea></td>
								</c:when>
								<c:otherwise>
									<td align="center">${dealR.CDCONT}</td>
								</c:otherwise>
							</c:choose>
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
              				<td align="center"><textarea name="content" style="width: 95%">${returnR.CRCONT}</textarea></td>
              				<td align="center">${returnR.CONFIRMOPTION}</td>
              				<td align="center">${returnR.CRUSER}</td>
              			</tr>
              		</c:forEach>
              	</table>
              </td>
            </tr>
           <!-- <tr id="huifang">
              <td align="center">5 </td>
              <td align="center">申请延期记录 </td>
              <td valign="top" colspan="4">
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
            </tr>  -->
            <tr>
              <td align="center" colspan="2" rowspan="1">回访结果</td>
              <td colspan="4">
              	<input type="radio" id="crResult1" name="crResult"  value="95111001"/>满意
              	<input type="radio" id="crResult2" name="crResult" value="95111003"/>不满意
              	<input type="radio" id="crResult3" name="crResult" value=""/>转部门
              </td>
            </tr>
            <tr >
			 <td align="left" colspan="6">处理部门：
				<select id="orgObj" name="orgObj" class="short_sel" onchange="changeOrgUserEvent(this.value,'',false)"  >
					<option value=''>-请选择-</option>
					<c:forEach var="tmOrg" items="${seatAdminDepartList}">
						<option value="${tmOrg.ORG_ID}" title="${tmOrg.ORG_NAME}">${tmOrg.ORG_NAME}</option>
					</c:forEach>
				</select>
				处理坐席人：
				<select id="userid" name="userid" >
					<option value=''>-请选择-</option>
				</select>
				<span id="spans"  style="color: red;display: none">不通过时,不需要选择部门和作坐席人!</span>
			</td>
			</tr>
<%--            <tr>--%>
<%--              <td align="center"  rowspan="1">审核</td>--%>
<%--              <td colspan="6">--%>
<%--              	<input type="radio" id="verifyRadio1" name="verifyRadio" value="95311001"/>通过--%>
<%--              	<input type="radio" id="verifyRadio2" name="verifyRadio" value="95311002"/>不通过           			--%>
<%--              </td>--%>
<%--            </tr>--%>
			
			<tr id="ccontTr">
				<td align="right" colspan="2">备注</td>
				<td align="left" colspan="4">
					<textarea id="ccont" name="ccont" rows="5" style="width: 95%"></textarea>
				</td>
			</tr>
          </tbody>
        </table></td>
      </tr>
      <tr>
        <td align="center">
        	<input name="button" type="button" class="long_btn" id="three_package_set_btn" onclick="save(<%=CommonUtils.getDataFromMap(tempMap,"CPID")%>);" value="保存" />
        	<input name="button" type="button" class="long_btn"  onclick="history.back();" ;" value="返回" />
        </td>
      </tr>
    </tbody>
  </table>
</div>

</FORM>
<script type="text/javascript">
	function check(){
		var msg ="";
		var  userid = document.getElementById('userid').value;
		if(!(document.getElementById('crResult1').checked || document.getElementById('crResult2').checked) && userid.length == 0 ){
			msg+="回访结果处理作息人不能同时为空!</br>"
		}
		if((document.getElementById('crResult1').checked || document.getElementById('crResult2').checked) && userid.length > 0 ){
			msg+="回访结果处理作息人不能同时选择!</br>"
		}

		if(document.getElementById('crResult2').checked){
			if(""==document.getElementById('ccont').value){
				msg+="备注不能为空!</br>"
			}else if(!WidthCheck(document.getElementById('ccont').value,1000)){
				msg+="备注太长!</br>"
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
   			makeNomalFormCall('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintSearch/ComplaintSearchUpdateSubmit.json?id=customerReturn&cpid='+cpid,saveBack,'fm','');
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
		//window.location.href = "<%=contextPath%>/customerRelationships/complaintConsult/ComplaintSearch/complaintSearchInit.do";
	}

</script>
</body>
</html>
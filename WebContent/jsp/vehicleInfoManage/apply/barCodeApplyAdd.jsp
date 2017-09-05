<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@ page import="com.infodms.dms.po.TtAsBarcodeApplyPO"%>
<%@ page import="com.infodms.dms.po.TmDealerPO"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@page import="java.text.SimpleDateFormat"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息变更申请</title>
<% String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> attachLs = request.getAttribute("lists") == null ? null : (LinkedList<FsFileuploadPO>)request.getAttribute("lists");
	TtAsBarcodeApplyPO bean = (TtAsBarcodeApplyPO)request.getAttribute("bean");
	Object o = request.getAttribute("tm");
	TmDealerPO dealer = null;
	if(o != null) {
		 dealer = (TmDealerPO) o;
	}
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	%>
<script type="text/javascript">
var arrvins = new Array();
	function doInit(){
   		loadcalendar();  //初始化时间控件
   		doCusChange();
   		var applyDate = document.getElementById("applyDate").value;
   		if(applyDate == null || applyDate == "")
   		{
   			var d = new Date();
			var vYear = d.getFullYear();
			var vMon = d.getMonth() + 1;
			var vDay = d.getDate();
			var currTime=(vYear)+"-"+(vMon<10 ? "0" + vMon : vMon)+"-"+(vDay<10 ?  "0"+ vDay : vDay) ;
   			document.getElementById("applyDate").value=currTime;
   		}
   		if(document.getElementById("expressMail").value == "0" || document.getElementById("chapterCode").value == "0")
   		{
   			document.getElementById("expressMail").value = "";
   			document.getElementById("chapterCode").value = "";
   		}
	}
function init(){
	var type = document.getElementById("type").value;
	if(type==1){
	queryVehicle();
		$('vin').setAttribute('disabled','true');
	}else if(type==2){
		queryVehicle();
		$('remark').setAttribute('disabled','true');
		$('vin').setAttribute('disabled','true');
		$('saveBtn').style.display="none";
		$("audit").style.display="";
		$('saveBtn4').style.display="none";
		$("dist").style.display="";
		$('auditRemark').setAttribute('disabled','true');
		$('checkPeople').setAttribute('disabled','true');
		$('applicantPeople').setAttribute('disabled','true');
		$('applyDate').setAttribute('disabled','true');
		$('auditDate').setAttribute('disabled','true');
		$('reviewRemark').setAttribute('disabled','true');
		$('distributionPeople').setAttribute('disabled','true');
		$('expressMail').setAttribute('disabled','true');
		$('distributionTime').setAttribute('disabled','true');
		$('chapterCode').setAttribute('disabled','true');
		$("addfile").style.display="none";
		removeFileDelete();
	}else if(type==4){
		queryVehicle();
		$('applicantPeople').setAttribute('disabled','true');
		$('applyDate').setAttribute('disabled','true');
		$('remark').setAttribute('disabled','true');
		$('vin').setAttribute('disabled','true');
		$("audit").style.display="";
		$("dist").style.display="";
		$('saveBtn').style.display="none";
		$('auditRemark').setAttribute('disabled','true');
		$('saveBtn4').style.display="";
		$("addfile").style.display="none";
		removeFileDelete();
	}else if(type==5){
		queryVehicle();
		$('applicantPeople').setAttribute('disabled','true');
		$('applyDate').setAttribute('disabled','true');
		$('remark').setAttribute('disabled','true');
		$('vin').setAttribute('disabled','true');
		$("audit").style.display="";
		$("dist").style.display="none";
		$('saveBtn').style.display="none";
		$('saveBtn2').style.display="";
		$('saveBtn3').style.display="";
		$('saveBtn4').style.display="none";
		$("addfile").style.display="none";
		removeFileDelete();
	}
}

function removeFileDelete() {
	var rows = $("fileUploadTab").rows;
	for(var i = 1; i < rows.length; i++) {
		rows[i].cells[1].innerHTML = "";
	}
}
</script>
<body onload="init();">
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;车辆信息变更申请
</div>
<form name="fm" id="fm">
<input type="hidden" name="type" id="type" value="${type }"/>
	<input type="hidden" name="ids" id="ids" value="<%=bean.getId()==null?"":bean.getId() %>"/>
	<input type="hidden" name="rand" id="rand" value="${rand}"/>
	<input type="hidden" name="randomId" id="randomId" value="${randomId}"/>
	<table class="table_edit" >
	<th colspan="6"><img class="nav"  src="../../../img/subNav.gif"/> 服务站信息</th>
		<tr>
			<td width="10%" align="right">服务站名称:</td>
			<td width="22%" align="left">
			<%=bean.getDealerName() == null ? (dealer != null ? dealer.getDealerName() : "" ) : bean.getDealerName()%>
			</td>
			<td width="10%" align="right">服务站编码:</td>
			<td width="22%" align="left">${tm.dealerCode}</td>
			<td width="12%" align="right">报告编号:</td>
			<td width="24%" align="left"><%=bean.getReportNumber() == null?"": bean.getReportNumber()%>${randomId}</td>
		</tr>
	</table>
	<table class="table_edit" id="vehicleInfo" width="">
		<th colspan="6"><img class="nav" src="../../../img/subNav.gif"/> 车辆信息</th>
		<tr>
			<td width="10%" align="right">VIN：</td>
			<td width="22%"><input name="vin" id="vin" type="text" class="middle_txt"   value="<%=bean.getVin()==null?"":bean.getVin() %>"
			 onblur="queryVehicle();"/><font color="red">*</font></td>
			<td width="10%" align="right">发动机号</td>
			<td width="22%" align="left"></td>
			<td width="12%" align="right">牌照号：</td>
			<td width="24%"></td>
		</tr>
		<tr>
			<td align="right">产地：</td>
			<td></td>
			<td width="10%" align="right">车系：</td>
			<td width="22%" align="left"></td>
			<td width="12%" align="right">车型：</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">购车日期：</td>
			<td align="left"></td>
			<td align="right">行驶里程：</td>
			<td></td>
			<td align="right">保养次数：</td>
			<td></td>
		</tr>
		<tr>
			<td width="10%" align="right">车主姓名：</td>
			<td></td>
			<td width="10%" align="right">车主电话：</td>
			<td width="22%" align="left"></td>
			<td width="12%" align="right">三包策略代码：</td>
			<td></td>
		</tr>
		<tr>
			<td width="12%" align="right" nowrap="nowrap">三包规则代码：<!-- 三包规则代码 --></td>
			<td></td>
			<td width="10%" align="right" >车主地址：<!-- 车主地址 --></td>
			<td colspan="2"></td>
			<td></td>
		</tr>
		</table>
		<table width="100%" class="table_info" border="0" >
			<th><img class="nav" src="../../../img/subNav.gif"/> 保养历史</th>
			<tr>
				<td>
					&nbsp;&nbsp;
					<input class="normal_btn" type="button" value="保养历史" onclick="aa();"/>
				</td>
			</tr>
		</table>
	    <table   width="100%" class="table_info" border="0" >
	        		<th colspan="6" nowrap="true"><img class="nav" src="../../../img/subNav.gif"/> 申请原因</th>
       		<tr>
          		<td align="right"  nowrap="true">申请原因：</td>
 				<td colspan="5" align="left">&nbsp;&nbsp;&nbsp;<textarea id="remark" onkeyup="check(this);" name="applyRemark" cols="110"   rows="5" ><%=bean.getApplyRemark()==null?"":bean.getApplyRemark() %></textarea><font color="red">*</font></td>
        	</tr>
        	<tr>
        		<td width="10%" align="right" nowrap="true">&nbsp;&nbsp;申请人：</td>
				<td width="22%" colspan="2">&nbsp;&nbsp;&nbsp;<input name="applicantPeople" id="applicantPeople" type="text" class="middle_txt" value="<%=bean.getApplicantPeople()== null?"":bean.getApplicantPeople() %>" /></td>
			 	<td width="10%" align="right" nowrap="true">申请时间：</td>
				<td width="22%" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;<input name="applyDate" id="applyDate" type="text" class="middle_txt" value="<%=bean.getApplyDate()== null? "":  sf.format(bean.getApplyDate()) %>" /></td>
        	</tr>
        	<tr>
        		<td colspan="6">
        			<jsp:include page="${contextPath}/uploadDiv.jsp" />
 					<input type="button" class="normal_btn" id="addfile"  onclick="showNewsUpload('<%=contextPath%>')" value ='添加附件'/>
        		</td>
        	</tr>
        	<%if(attachLs != null) { for(int i=0;i<attachLs.size();i++) { %>
			  	<script type="text/javascript">
			  		addUploadRowByDb('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
			  	</script>
			<%}} %>
			</table>
			<table id="audit"  width="100%" class="table_info" border="0"  style="display: none">
	        		<th colspan="6" nowrap="true"><img class="nav" src="../../../img/subNav.gif"/> 索赔室意见</th>
       		<tr>
          		<td align="right" nowrap="true">审核意见：</td>
 				<td colspan="5" align="left"><textarea id="auditRemark" onkeyup="check(this);" name="auditRemark" cols="110"   rows="5" ><%=bean.getAuditRemark()==null?"":bean.getAuditRemark() %></textarea></td>
        	</tr>
        	<tr style="display: none">
        		<td width="10%" align="right" nowrap="true">审核人：</td>
				<td width="22%" colspan="2"><input name="checkPeople" id="checkPeople" type="text" class="middle_txt" value="<%=bean.getCheckPeople() == null?"":bean.getCheckPeople() %>" onblur="formyDate(this.value);"	/></td>
			 	<td width="10%" align="right" nowrap="true">审核时间：</td>
				<td width="22%" colspan="2"><input name="auditDate" id="auditDate" type="text" class="middle_txt" value="<%=bean.getAuditDate() == null?"":sf.format(bean.getAuditDate()) %>" /></td>
        	</tr>
			</table> 
			<table id="dist"   width="100%" class="table_info" border="0" style="display: none">
	        <th colspan="6" nowrap="true"><img class="nav" src="../../../img/subNav.gif"/> 发放详情</th>
	        <tr>
          		<td align="right" nowrap="true">备注：</td>
 				<td colspan="5" align="left">&nbsp;&nbsp;<textarea id="reviewRemark" onkeyup="check(this);" name="reviewRemark" cols="110"   rows="5" ><%=bean.getReviewRemark()==null?"":bean.getReviewRemark() %></textarea></td>
        	</tr>
       		<tr>
        		<td width="10%" align="right" nowrap="true">发放人：</td>
				<td width="22%" colspan="2"><input name="distributionPeople" id="distributionPeople" type="text" class="middle_txt" value="<%=bean.getDistributionPeople() == null?"":bean.getDistributionPeople() %>" onblur="formyDate(this.value);" /></td>
			 	<td width="10%" align="right" nowrap="true">特快专递号：</td>
				<td width="22%" colspan="2"><input name="expressMail" id="expressMail" type="text" class="middle_txt" value="<%=bean.getExpressMail() == null?"":bean.getExpressMail() %>" /></td>
        	</tr>
       		
 			<tr>
        		<td width="10%" align="right" nowrap="true">发放日期：</td>
				<td width="22%" colspan="2"><input name="distributionTime" id="distributionTime" type="text" class="middle_txt" value="<%=bean.getDistributionTime() == null?"":sf.format(bean.getDistributionTime()) %>" /></td>
			 	<td width="10%" align="right" nowrap="true">编码章：</td>
				<td width="22%" colspan="2"><input name="chapterCode" id="chapterCode" type="text" class="middle_txt" value="<%=bean.getChapterCode() == null?"":bean.getChapterCode() %>"/></td>
        	</tr>
        	
			</table> 
		</br>
		<table width=100% border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="12" align=left width="33%">&nbsp;</td>
             	<td height="12" align=center width="33%">
                	<input type="button" onClick="doSave();" class="normal_btn" id="saveBtn"  style="width=8%" value="保存"/>
                	<input type="button" onClick="doSave2('agree');" class="normal_btn" id="saveBtn2"  style="width=8%;display: none" value="同意"/>
                	&nbsp;
                	<input type="button" onClick="doSave2('disagree');" class="normal_btn" id="saveBtn3"   style="width=8%;display: none" value="驳回"/>
                	&nbsp;
                	<input type="button" onClick="doSave2('save');" class="normal_btn" id="saveBtn4"   style="width=8%;display: none" value="确认"/>
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
function doSave2( obj ){
	if(obj=="agree"){
		MyConfirm("是否通过？",confirmAdd2,[obj]);	
	}else if(obj=="save"){
		var text="";
		if(document.getElementById("distributionPeople").value == null || document.getElementById("distributionPeople").value == "")
		{
			text+=" [发放人不能为空!] ";
			document.getElementById("distributionPeople").focus();
		}
		if(document.getElementById("expressMail").value == null || document.getElementById("expressMail").value == "")
		{
			text+=" [快递号不能为空!] ";
			document.getElementById("expressMail").focus();
		}
		if(document.getElementById("chapterCode").value == null || document.getElementById("chapterCode").value == "")
		{
			text+=" [编码章不能为空!] ";
			document.getElementById("chapterCode").focus();
		}
		if(text!=""){
			MyAlert("提示："+text);
			return false;
		}
		var reg = new RegExp("^[0-9]*$");
		if(!reg.test(document.getElementById("expressMail").value)){  
			MyAlert("提示：快递单号只能输入数字!");
			return false;
		}  
		var reg = new RegExp("^[0-9]*$");
		if(!reg.test(document.getElementById("chapterCode").value)){  
			MyAlert("提示：编码章只能输入数字!");
			return false;
		}  
		MyConfirm("提示：确认发运？",confirmAdd2,[obj]);	
	}else{
		var remark = document.getElementById("auditRemark").value;
		if(remark==null||remark==""){
			MyAlert("提示：请填写审核意见！");
			return false;
		}
		MyConfirm("是否驳回？",confirmAdd2,[obj]);
	}
}
function confirmAdd2(str){
		$('saveBtn2').disabled=true;
		$('saveBtn3').disabled=true;
		var id = $("ids").value;
		var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/barCodeApplySave.json?str='+str+'&id='+id;
		makeNomalFormCall(url,showResult,'fm');
}

function check(obj){
	if(obj.value.length>200){
	MyAlert("备注最大支持200字符!");
	obj.value = obj.value.substring(0,200);
		return false;
	}
}
function doSave(){
	var text = "";
	var vin = $('vin').value;
	if(vin ==null||vin==""){
		text+=" [请输入VIN码!] ";
	}
	var remark = $("remark").value;
	if(remark ==null||remark==""){
		text+=" [申请原因必须填写!] ";
	}
	var applicantPeople = $("applicantPeople").value;
	if(applicantPeople ==null||applicantPeople==""){
		text+=" [申请人必须填写!] ";
	}
	if(text!=""){
		MyAlert("提示：请填写必填字段"+text);
		return;
	}
	MyConfirm("是否保存？",confirmAdd0,[]);
}
function confirmAdd0(){
		$('saveBtn').disabled=true;
		var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/barCodeApplySave.json';
		makeNomalFormCall(url,showResult,'fm');
}
function showResult(json){
	if(json.result!=null&&json.result!=""){
		MyAlert("操作成功！");
		if(json.type==5){
			 window.location.href='<%=contextPath%>/jsp/vehicleInfoManage/apply/barCodeApplyPer2.jsp';
		}else if(json.type==4){
			window.location.href='<%=contextPath%>/jsp/vehicleInfoManage/apply/barCodeApplyPer3.jsp';
		}else{
			 window.location.href='<%=contextPath%>/jsp/vehicleInfoManage/apply/barCodeApplyPer.jsp';
		}
	}else{
		MyAlert("操作失败！");
	}
}
function queryVehicle() {
	var vin = document.getElementById("vin").value;
	 if(vin.length ==18){
       vin = vin.substring(0,vin.length-1);
        var strAscii = new Array();//用于接收ASCII码
		var res = "";
		for(var i = 0 ; i < vin.length ; i++ ){
		strAscii[i] = parseInt(vin.charCodeAt(i))-1;//只能把字符串中的字符一个一个的解码
		res+=String.fromCharCode(strAscii[i]);  //完成后，转化为为字母或者数字
		}
		document.getElementById("vin").value=res;
		vin = res;
        }
	var pattern=/^([A-Z]|[0-9]){17,17}$/;
	if(pattern.exec(vin)) {
		var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/queryVehicleInfo.json?vin='+vin;
		makeNomalFormCall(url,showVehicle,'fm');
	} else {
		MyAlert("不是有效的VIN格式");
	}
}
function showVehicle(json) {
	if (!json.vehicle) {
		MyAlert("没有找到对应的VIN");
		$('vin').value="";
	var vehicleInfo = document.getElementById("vehicleInfo");
	vehicleInfo.rows[1].cells[3].innerText = "";//发动机
	vehicleInfo.rows[1].cells[5].innerText = "";//牌照号
	vehicleInfo.rows[2].cells[1].innerText = "";//产地
	vehicleInfo.rows[2].cells[3].innerText = "";//车系
	vehicleInfo.rows[2].cells[5].innerText ="";//车型
	vehicleInfo.rows[3].cells[1].innerText = "";//购车日期
	vehicleInfo.rows[3].cells[3].innerText = "";//行驶里程
	vehicleInfo.rows[3].cells[5].innerText = "";//保养次数
	vehicleInfo.rows[4].cells[1].innerText = "";//车主姓名
	vehicleInfo.rows[4].cells[3].innerText = "";//车主电话
	vehicleInfo.rows[4].cells[5].innerText = "";//三包策略代码
	vehicleInfo.rows[5].cells[1].innerText = "";//三包规则代码
	vehicleInfo.rows[5].cells[3].innerText = "";//车主地址
		return;
	} 
	var vehicleInfo = document.getElementById("vehicleInfo");
	var vehicle = json.vehicle;
	vehicleInfo.rows[1].cells[3].innerText = vehicle.ENGINE_NO;//发动机
	vehicleInfo.rows[1].cells[5].innerText = checkNull(vehicle.VEHICLE_NO);//牌照号
	vehicleInfo.rows[2].cells[1].innerText = checkNull(vehicle.YIELDLY);//产地
	vehicleInfo.rows[2].cells[3].innerText = checkNull(vehicle.SERIES_CODE);//车系
	vehicleInfo.rows[2].cells[5].innerText = checkNull(vehicle.MODEL_CODE);//车型
	vehicleInfo.rows[3].cells[1].innerText = checkNull(vehicle.PURCHASED_DATE);//购车日期
	vehicleInfo.rows[3].cells[3].innerText = checkNull(vehicle.MILEAGE);//行驶里程
	vehicleInfo.rows[3].cells[5].innerText = checkNull(vehicle.FREE_TIMES);//保养次数
	vehicleInfo.rows[4].cells[1].innerText = checkNull(vehicle.CTM_NAME);//车主姓名
	vehicleInfo.rows[4].cells[3].innerText = checkNull(vehicle.MAIN_PHONE);//车主电话
	vehicleInfo.rows[4].cells[5].innerText = checkNull(vehicle.GAME_CODE);//三包策略代码
	vehicleInfo.rows[5].cells[1].innerText = checkNull(vehicle.RULE_CODE);//三包规则代码
	vehicleInfo.rows[5].cells[3].innerText = checkNull(vehicle.ADDRESS);//车主地址
	 
}
function checkNull(value) {
	if (value == null || value == 'null') {
		return '';
	} else {
		return value;
	}
}
function aa(){
	 var vin  = document.getElementById("vin").value;
	 if(vin.length == 0)
	 {
		MyAlert('请输入VIN'); 
	 }else
	 {
		 openWindowDialog('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/freeMaintainHistory.do?VIN='+vin);
	 }
	
}

//保养历史
function openWindowDialog(targetUrl){
		  var height = 500;
		  var width = 800;
		  var iTop = (window.screen.availHeight-30-height)/2;  //获得窗口的垂直位置      
		  var iLeft = (window.screen.availWidth-10-width)/2;   //获得窗口的水平位置
		  var params = "width="+width+",height="+height+",top="+iTop+",left="+iLeft+",scrollbars=yes,resizable=yes,status=no";
		  window.open(targetUrl,null,params);
	  	}

	function formyDate(val)
	{
		var auditDate = document.getElementById("auditDate").value;
   		var distributionTime = document.getElementById("distributionTime").value;
   		var d = new Date();
		var vYear = d.getFullYear();
		var vMon = d.getMonth() + 1;
		var vDay = d.getDate();
		var currTime=(vYear)+"-"+(vMon<10 ? "0" + vMon : vMon)+"-"+(vDay<10 ?  "0"+ vDay : vDay) ;
		if(val == document.getElementById("checkPeople").value)
		{
			if(auditDate == null || auditDate == "")
   			{
   				document.getElementById("auditDate").value=currTime;
   			}
		}
		else if(val == document.getElementById("distributionPeople").value)
		{
			if(distributionTime == null || distributionTime == "")
	   		{
	   			document.getElementById("distributionTime").value = currTime;
	   		}
		}
	}
</script>

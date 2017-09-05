<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@ page import="com.infodms.dms.po.TtAsPackgeChangeApplyPO "%>
<%@ page import="com.infodms.dms.bean.TtAsPackgeChangeDetailBean "%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息变更申请</title>
<% String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	List<TtAsPackgeChangeDetailBean> list = (LinkedList<TtAsPackgeChangeDetailBean>)request.getAttribute("list");
	TtAsPackgeChangeApplyPO  bean = (TtAsPackgeChangeApplyPO )request.getAttribute("bean");
	%>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
   		doCusChange();
	}
function init(){
	
		queryVehicle();
		$('vin').setAttribute('disabled','true');
		$('remark').setAttribute('disabled','true');
		$('vin').setAttribute('disabled','true');
		$('saveBtn').style.display="none";
		$("addfile").setAttribute('disabled','true');
	
}
	function tabDisplayControl(tableId){
			var tab = document.getElementById(tableId);
			if(tab.style.display=='none'){
				tab.style.display = '';
			}else{
				tab.style.display = 'none';
			}
		}
</script>
<body onload="init();">
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;三包凭证补办明细
</div>
<form name="fm" id="fm">
	<table class="table_edit" >
	<input type="hidden" name="type" id="type" value="${type }"/>
	<input type="hidden" name="ids" id="ids" value="<%=bean.getId()==null?"":bean.getId() %>"/>
		<th colspan="6" onclick="tabDisplayControl('vehicleInfo');" ><img class="nav" src="../../../img/subNav.gif" /> 车辆信息</th>
		</table>
		<table class="table_edit" id="vehicleInfo">
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
			<td ></td>
			<td width="10%" align="right" >车辆用途：<!-- 车主地址 --></td>
			<td ></td>
		</tr>
	</table>
	    <table   width="100%" class="table_info" border="0" >
	        		<th colspan="6" onclick="tabDisplayControl('applyReson');" ><img class="nav" src="../../../img/subNav.gif"/> 申请原因</th>
       		</table> 
 <table class="table_edit" id="applyReson" style="display: " >
       		<tr>
          		<td align="right">申请原因：</td>
 				<td colspan="5" align="left"><textarea id="remark" onkeyup="check(this);" name="remark" cols="120"   rows="5" ><%=bean.getApplyRemark()==null?"":bean.getApplyRemark() %></textarea><font color="red">*</font></td>
        	</tr>
			</table> 
 <table class="table_edit" >
		<th colspan="5" onclick="tabDisplayControl('auditDetail');" ><img class="nav" src="../../../img/subNav.gif"/> 审核记录</th>
		</table> 
 <table class="table_edit" id="auditDetail" style="display: " >
		<tr>
			<td width="10%" align="center" >审核人</td>
			<td width="22%" align="center">审核费用</td>
			<td width="10%" align="center">审核状态</td>
			<td width="22%" align="center">审核时间</td>
			<td width="12%" align="center">审核备注</td>
		</tr>
		<% if(list!=null&& list.size()>0){
			for(int i=0;i<list.size();i++){%>
				<tr>
			<td width="10%" align="center" ><%=list.get(i).getAuditName() %></td>
			<td width="22%" align="center"> <%=list.get(i).getAuditAcount() %></td>
			<td width="10%" align="center">
	 		<script type="text/javascript">
	 		document.write(getItemValue('<%=list.get(i).getAuditStatus() %>'));
	 	 	</script>
			</td>
			<td width="22%" align="center"><%=list.get(i).getAuditTime() %></td>
			<td width="12%" align="center"><%=list.get(i).getAuditRemark() %></td>
		</tr>
			<%} } else {%>
			<tr>
			<td width="10%" colspan="5" align="center" ><span style="color: red">没有审核记录</span></td>
		</tr>
			
			<%}%>
	</table>
	 <!-- 添加附件 开始  -->
        <table id="add_file"  width="100%" class="table_info" border="0" id="file">
				<input type="hidden" id="fjids" name="fjids"/>
	    		<tr>
	        		<th onclick="tabDisplayControl('fileIdss');">
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
						<font color="red">
							<span id="span1"></span>
						</font>
			     		<input type="button" class="normal_btn" align="right" id="addfile" onclick="showUpload('<%=contextPath%>')" value ='添加附件'/><font color="red">*</font>
					</th>
				</tr>
				</table> 
 <table class="table_edit" id="fileIdss" style="display: " >
				<tr>
    				<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  				</tr>
  					<%if(fileList!=null){
	  				for(int i=0;i<fileList.size();i++) { %>
					  <script type="text/javascript">
				    		addUploadRowByDbView('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
				    	</script>
					<%}}%>
			</table> 
  		<!-- 添加附件 结束 -->
		</br>
		<table width=100% border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="12" align=left width="33%">&nbsp;</td>
             	<td height="12" align=center width="33%">
                	<input type="button" onClick="doSave();" class="normal_btn" id="saveBtn"  style="width=8%" value="保存"/>
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
var flag = true;
	var remark = $("audit_remark").value;
	var str = $('auditAcount').value;
	if(str ==null||str==""){
		MyAlert("审核费用必填,若无费用请填0！");
		return false;
	}else if(parseFloat(str)==0&&(remark==null||remark=="")&&obj=="agree"){
		MyAlert("无费用时,必须填写原因！");
		return false
	}
	if(obj=="agree"){
	MyConfirm("是否通过？",confirmAdd2,[obj]);
	}else{
	MyConfirm("是否驳回？",confirmAdd2,[obj]);
	}
}
function confirmAdd2(str){
		$('saveBtn2').disabled=true;
		$('saveBtn3').disabled=true;
		var id = $("ids").value;
		var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/packgeAppAuditSave.json?str='+str+'&id='+id;
		makeNomalFormCall(url,showResult,'fm');
}
function checks(obj){

	var value=obj.value;
	var reg =/^\d+(?:\.\d{0,2})?$/;
	if(value==null||value==""){
		MyAlert("审核费用必填,若无费用请填0！");
		return false;
	}else if(!reg.test(value)){
		MyAlert("费用为最多2位小数的数字!");
		obj.value = "";
		return false;
	}
}
function check(obj){
	if(obj.value.length>200){
	MyAlert("备注最大支持200字符!");
	obj.value = obj.value.substring(0,200);
		return false;
	}
}
function doSave(){
var flag = true;
	var vin = $('vin').value;
	if(vin ==null||vin==""){
	MyAlert("请输入VIN码!");
	flag = false;
	return false;
	}
	var remark = $("remark").value;
	if(remark ==null||remark==""){
	MyAlert("申请原因必须填写!");
	flag = false;
	return false;
	}
	if($('fileUploadTab').rows.length<=1){
			MyAlert('请上传附件依据！!');
			flag = false;
			return false;
	}
	if(flag){
		MyConfirm("是否保存？",confirmAdd0,[]);
	}
}
function confirmAdd0(){
		$('saveBtn').disabled=true;
		var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/packgeApplySave.json';
		makeNomalFormCall(url,showResult,'fm');
}

function queryVehicle() {
	var vin = document.getElementById("vin").value;
	var pattern=/^([A-Z]|[0-9]){17,17}$/;
	if(pattern.exec(vin)) {
		var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/queryVehicleInfoPackge.json?vin='+vin;
		makeNomalFormCall(url,showVehicle,'fm');
	} else {
		MyAlert("不是有效的VIN格式");
	}
}
function showVehicle(json) {
	if (!json.vehicle) {
		MyAlert("没有找到对应的VIN");
		 $("vin").value="";
	var vehicleInfo = document.getElementById("vehicleInfo");
	vehicleInfo.rows[0].cells[3].innerText = "";//发动机
	vehicleInfo.rows[0].cells[5].innerText = "";//牌照号
	vehicleInfo.rows[1].cells[1].innerText = "";//产地
	vehicleInfo.rows[1].cells[3].innerText = "";//车系
	vehicleInfo.rows[1].cells[5].innerText ="";//车型
	vehicleInfo.rows[2].cells[1].innerText = "";//购车日期
	vehicleInfo.rows[2].cells[3].innerText = "";//行驶里程
	vehicleInfo.rows[2].cells[5].innerText = "";//保养次数
	vehicleInfo.rows[3].cells[1].innerText = "";//车主姓名
	vehicleInfo.rows[3].cells[3].innerText = "";//车主电话
	vehicleInfo.rows[3].cells[5].innerText = "";//三包策略代码
	vehicleInfo.rows[4].cells[1].innerText = "";//三包规则代码
	vehicleInfo.rows[4].cells[3].innerText = "";//车主地址
	vehicleInfo.rows[4].cells[5].innerText = "";//车主地址
		return;
	} 
	if(json.sale=="unSale"){
	 MyAlert("该车没有实销信息,不能做凭证补办!");
	 var vehicleInfo = document.getElementById("vehicleInfo");
	 $("vin").value="";
	vehicleInfo.rows[0].cells[3].innerText = "";//发动机
	vehicleInfo.rows[0].cells[5].innerText = "";//牌照号
	vehicleInfo.rows[1].cells[1].innerText = "";//产地
	vehicleInfo.rows[1].cells[3].innerText = "";//车系
	vehicleInfo.rows[1].cells[5].innerText ="";//车型
	vehicleInfo.rows[2].cells[1].innerText = "";//购车日期
	vehicleInfo.rows[2].cells[3].innerText = "";//行驶里程
	vehicleInfo.rows[2].cells[5].innerText = "";//保养次数
	vehicleInfo.rows[3].cells[1].innerText = "";//车主姓名
	vehicleInfo.rows[3].cells[3].innerText = "";//车主电话
	vehicleInfo.rows[3].cells[5].innerText = "";//三包策略代码
	vehicleInfo.rows[4].cells[1].innerText = "";//三包规则代码
	vehicleInfo.rows[4].cells[3].innerText = "";//车主地址
	vehicleInfo.rows[4].cells[5].innerText = "";//车主地址
	 return;
	}
	var vehicleInfo = document.getElementById("vehicleInfo");
	var vehicle = json.vehicle;
	vehicleInfo.rows[0].cells[3].innerText = vehicle.ENGINE_NO;//发动机
	vehicleInfo.rows[0].cells[5].innerText = checkNull(vehicle.VEHICLE_NO);//牌照号
	vehicleInfo.rows[1].cells[1].innerText = checkNull(vehicle.YIELDLY);//产地
	vehicleInfo.rows[1].cells[3].innerText = checkNull(vehicle.SERIES_CODE);//车系
	vehicleInfo.rows[1].cells[5].innerText = checkNull(vehicle.MODEL_CODE);//车型
	vehicleInfo.rows[2].cells[1].innerText = checkNull(vehicle.PURCHASED_DATE);//购车日期
	vehicleInfo.rows[2].cells[3].innerText = checkNull(vehicle.MILEAGE);//行驶里程
	vehicleInfo.rows[2].cells[5].innerText = checkNull(vehicle.FREE_TIMES);//保养次数
	vehicleInfo.rows[3].cells[1].innerText = checkNull(vehicle.CTM_NAME);//车主姓名
	vehicleInfo.rows[3].cells[3].innerText = checkNull(vehicle.MAIN_PHONE);//车主电话
	vehicleInfo.rows[3].cells[5].innerText = checkNull(vehicle.GAME_CODE);//三包策略代码
	vehicleInfo.rows[4].cells[1].innerText = checkNull(vehicle.RULE_CODE);//三包规则代码
	vehicleInfo.rows[4].cells[3].innerText = checkNull(vehicle.ADDRESS);//车主地址
	vehicleInfo.rows[4].cells[5].innerText = checkNull(vehicle.CODE_DESC);//车主地址
}
function showResult(json){
	if(json.result!=null&&json.result!=""){
		MyAlert("操作成功！");
		if(json.type=="dq"){
			fm.action = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/packgeChangeAudit.do';
		}else if(json.type=="cc"){
			fm.action = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/packgeChangeAudit2.do';
		}
		fm.method="post";
		fm.submit();
	}else{
		MyAlert("操作失败！");
	}
}
function checkNull(value) {
	if (value == null || value == 'null') {
		return '';
	} else {
		return value;
	}
}
</script>

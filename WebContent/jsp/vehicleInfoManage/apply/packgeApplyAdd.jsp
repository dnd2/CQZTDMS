<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.TtAsPackgeChangeApplyPO "%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息变更申请</title>
<% String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
	TtAsPackgeChangeApplyPO  bean = (TtAsPackgeChangeApplyPO )request.getAttribute("bean");
	int scan = (Integer)request.getAttribute("isScan");//取得后台传来的值得，用于判断在新增的时候,VIN输入框是否能够手动输入
	%>
<script type="text/javascript">
var arrvins = new Array();
function get(){
if(<%=scan%> ==0 ){
	var iKeyCode = window.event.keyCode;
	if(iKeyCode != 46 && iKeyCode != 8) {
	   var ddate = new Date();
	   arrvins[arrvins.length] = ddate.getTime();
	}
	}
	return true;
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
		$("addfile").setAttribute('disabled','true');
	}
}
</script>
<body onload="init();">
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;三包凭证补办申请
</div>
<form name="fm" id="fm" onsubmit="return false;">
	<table class="table_edit" id="vehicleInfo">
	<input type="hidden" name="type" id="type" value="${type }"/>
	<input type="hidden" name="ids" id="ids" value="<%=bean.getId()==null?"":bean.getId() %>"/>
		<th colspan="6"><img class="nav" src="../../../img/subNav.gif"/> 车辆信息</th>
		<tr>
			<td width="10%" align="right">VIN：</td>
			<td width="22%"><input name="vin" id="vin" type="text" class="middle_txt"   value="<%=bean.getVin()==null?"":bean.getVin() %>"
			 <% if(scan ==0){%> 
							 onkeydown="get()" onpaste="return false"
				<%  } %>
				onblur="queryVehicle();"/><font color="red">*</font></td>
			<td width="10%" align="right">发动机号:</td>
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
	        		<th colspan="6"><img class="nav" src="../../../img/subNav.gif"/> 申请原因</th>
       		<tr>
          		<td align="right">申请原因：</td>
 				<td colspan="5" align="left"><textarea id="remark" onkeyup="check(this);" name="remark" cols="120"   rows="5" ><%=bean.getApplyRemark()==null?"":bean.getApplyRemark() %></textarea><font color="red">*</font></td>
        	</tr>
			</table> 
	   
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
function showResult(json){
	if(json.result!=null&&json.result!=""){
		MyAlert("操作成功！");
		fm.action = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/packgeChangeApply.do';
		fm.method="post";
		fm.submit();
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
	  if(<%=scan%>==0){
	    	var time = arrvins[arrvins.length-1]-arrvins[0];
			arrvins = new Array();
			if(time > 500){
				document.getElementById("vin").value = "";
				MyAlert("你不能手动输入VIN!");
				return false;
			}
	    }
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
	vehicleInfo.rows[5].cells[5].innerText = "";//车主地址
		return;
	} 
	if(json.sale=="unSale"){
	 MyAlert("该车没有实销信息,不能做凭证补办!");
	 var vehicleInfo = document.getElementById("vehicleInfo");
	 $("vin").value="";
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
	vehicleInfo.rows[5].cells[5].innerText = "";//车主地址
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
	vehicleInfo.rows[5].cells[5].innerText = checkNull(vehicle.CODE_DESC);//车主地址
	 
	 
}
function checkNull(value) {
	if (value == null || value == 'null') {
		return '';
	} else {
		return value;
	}
}
</script>

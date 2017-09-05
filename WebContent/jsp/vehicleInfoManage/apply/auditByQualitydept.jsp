<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change"  prefix="change"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<head> 
<% String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<title>质量部门审批</title>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
	$(function(){
		var vin = $("#vin").val() ;
		if(vin=="" ||vin==null){
			$("#btn1").attr("disabled",true);
			$("#btn3").attr("disabled",true);
		}
		var productQuality=$("#productQuality").val();
		isradio(productQuality,"productQuality");
		var isKeepFit=$("#isKeepFit").val();
		isradio(isKeepFit,"isKeepFit");
		var isfit=$("#isfit").val();
		isradio(isfit,"isfit");
		ischecked("carStatusByProblem");
		ischecked("wayStatus");
		ischecked("weatherStatus");
		var oilLeak=$("#oilLeak").val();
		var problemProperties=$("#problemProperties").val();
		isradio(oilLeak,"oilLeak");
		isradio(problemProperties,"problemProperties");
		var problemTheSame=$("#problemTheSame").val();
		isradio(problemTheSame,"problemTheSame");
		var isAduit=$("#isAduit").val();
		isradio(isAduit,"isAduit");
		ischecked("problemCode");
		ischecked("partChangeStatus");
	});
	//公共的radio
	function isradio(val,className){
		$("."+className).each(function(){
			if(val==$(this).val()){
				$(this).attr("checked",true).attr("disabled",false);
			}
		});
	}
	
	//公共的checked
	function ischecked(className){
		var val=$("#"+className).val();
		if(val!=""){
			var str=val.split(",");
			$("."+className).each(function(){
				for(var i=0;i<str.length;i++){
					if(str[i]==$(this).val()){
						$(this).attr("checked",true).attr("disabled",false);
						$(this).bind("click",function(){
							$(this).attr("checked",true);
						});
					}
				}
			});
		}
	}
</script>
</head>
<body>
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;车辆信息管理&gt;质量部门审批
</div>
<form name="fm" id="fm" method="post">
<input type="hidden" id="qualityid" name="qualityid" value="${po.qualityId }"/>
<input type="hidden" id="addQualityScore" name="addQualityScore" value="${po.addQualityScore }"/>
<input type="hidden" id="inTimeScore" name="inTimeScore" value="${po.inTimeScore }"/>
<input type="hidden" id="countScore" name="countScore" value="${po.countScore}"/>
<input type="hidden" id="claimNo" name="claimNo" value="${po.claimNo }"/>
<input type="hidden" id="verifyStatus" name="verifyStatus" value="${po.verifyStatus }"/>
<input type="hidden" id="productQuality" value="${po.productQuality }"/>
<input type="hidden" id="carStatusByProblem"  value="${po.carStatusByProblem }"/>
<input type="hidden" id="isfit"  value="${po.isFit }"/>
<input type="hidden" id="isKeepFit"  value="${po.isKeepFit }"/>
<input type="hidden" id="wayStatus"  value="${po.wayStatus }"/>
<input type="hidden" id="weatherStatus"  value="${po.weatherStatus }"/>
<input type="hidden" id="oilLeak"  value="${po.oilLeak }"/>
<input type="hidden" id="problemProperties"  value="${po.problemProperties }"/>
<input type="hidden" id="problemTheSame"  value="${po.problemTheSame }"/>
<input type="hidden" id="isAduit"  value="${po.isAduit }"/>
<input type="hidden" id="problemCode"  value="${po.problemCode }"/>
<input type="hidden" id="partChangeStatus"  value="${po.partChangeStatus }"/>

<div style="text-align: center;">
	<h4>品质情报</h4>
	<br>
	<table border="1" cellpadding="1" cellspacing="1" class="tab_edit" width="100%" align="center">
		<tr>
			<td nowrap="true">
				服务商代码: <input type="text" name="dealerCode"  readonly="readonly" class="middle_txt"  value="${po.dealerCode }"/>  
				服务商名称: <input type="text" name="dealerName"  readonly="readonly" class="middle_txt" value="${po.dealerName }"/>  
				&nbsp;&nbsp;&nbsp;&nbsp;填报人:&nbsp;&nbsp;&nbsp;<input type="text" readonly="readonly" class="middle_txt" maxlength="15" name="reportName" id="reportName" value="${po.reportName}" />
				联系方式:<input type="text" class="middle_txt" name="contactType" readonly="readonly" id="contactType" maxlength="15" value="${po.contactType }" />
			</td>
		</tr>
		<tr>
			<td nowrap="true">
				&nbsp;&nbsp;&nbsp;&nbsp;编号：&nbsp; <input type="text"  class="middle_txt" id="randomId" readonly="readonly" name="randomId"  value="${po.randomId }"/>  
				及时性得分：<input type="text"  class="middle_txt" name="inTimeScore"  readonly="readonly" value="${po.inTimeScore }"/>  
				填报质量得分：<input type="text"  class="middle_txt" name="addQualityScore"  readonly="readonly" value="${po.addQualityScore }"/>  
				&nbsp;&nbsp;&nbsp;总分：<input type="text"  class="middle_txt"  name="countScore"  readonly="readonly" value="${po.countScore }"/>  
			</td>
		</tr>
		<tr>
			<td nowrap="true">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;是否维修：
    		<input type="radio" id="isfit" disabled="disabled" class="isfit" name="isFit" value="是"/>是
    		&nbsp;&nbsp;
    		<input type="radio" id="isnotfit" disabled="disabled" name="isFit" class="isfit" value="否"/>否
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;	填报日期:<input type="text" id="reportDate" name="reportDate"class="middle_txt"  readonly="readonly" value="${po.reportDate }"/> 
			</td>
		</tr>
	</table>
</div>
<table border="1"   class="tab_edit" width="100%" >
	<tr>
		<td nowrap="true" width="10%" style="border: none;">
			车辆信息：
    	</td>
    	<td nowrap="true" width="25%" style="border: none;">
    	<input type="radio"  class="productQuality" disabled="disabled" name="productQuality" value="售前车"/>售前车
    		&nbsp;&nbsp;
    		<input type="radio"  name="productQuality" disabled="disabled" class="productQuality" value="售后车"/>售后车
    	</td>
		<td nowrap="true" width="10%" style="border: none;">
    	</td>
    	<td nowrap="true" width="25%" style="border: none;">
    	</td>
		<td nowrap="true" width="10%" style="border: none;">
    	</td>
    	<td nowrap="true" width="20%" style="border: none;">
    	</td>
	</tr>
    <tr>
    	<td nowrap="true">
    		车系:
    	</td>
    	<td nowrap="true">
    		<input type="text" id="carClass" readonly="readonly" class="middle_txt" name="carClass" value="${po.carClass }"/>
    	</td>
    	<td nowrap="true">
    		车型:
    	</td>
    	<td >
    		<input type="text" id="carType"readonly="readonly" class="middle_txt" name="carType" value="${po.carType }"/>
    	</td>
    	<td nowrap="true">
    		车牌号:
    	</td>
    	<td >
    		<input type="text" id="carNo" readonly="readonly" class="middle_txt" name="carNo" value="${po.carNo }"/>
    	</td>
    </tr>
    
    <tr >
    	<td nowrap="true">
    		底盘号:
    	</td>
    	<td nowrap="true">
    		<input type="text" id="vin" readonly="readonly" class="middle_txt" name="vin" value="${po.vin}"/>
    	</td>
    	<td nowrap="true">
    		发动机号:
    	</td>
    	<td nowrap="true">
    		<input id="engineNo" readonly="readonly"  type="text"class="middle_txt" name="engineNo" value="${po.engineNo}"/>
    	</td>
    	<td nowrap="true">
    		变速器号:
    	</td>
    	<td >
    		<input type="text"  name="carSpend" readonly="readonly" class="middle_txt" value="${po.carSpend}"/>
    	</td>
    	
    </tr>
    
    <tr >
    	<td nowrap="true">
    		行驶里程:
    	</td>
    	<td nowrap="true">
    		<input type="text" id="mileage" readonly="readonly" class="middle_txt" name="mileage"  value="${po.mileage}"/>
    	</td>
    	<td nowrap="true">
    		生产日期:
    	</td>
    	<td nowrap="true">
    		<input type="text" id="productDate" readonly="readonly"  class="middle_txt" name="productDate" value="${po.productDate}"/>
    	</td>
    	<td nowrap="true">
    		购车日期:
    	</td>
    	<td >
    		<input type="text" id="buyCarDate" readonly="readonly" class="middle_txt" name="buyCarDate" value="${po.buyCarDate}"/>
    	</td>
    </tr>
    
    <tr >
    	<td nowrap="true">
    		故障日期:
    	</td>
    	<td nowrap="true">
    		<input type="text" id="faultDate" readonly="readonly" class="middle_txt" name="faultDate" value="${po.faultDate}"/>
    	</td>
    	<td nowrap="true">
    		维修日期:
    	</td>
    	<td nowrap="true">
    		<input type="text" id="serviceDate" readonly="readonly" class="middle_txt" name="serviceDate" value="${po.serviceDate}"/>
    	</td>
    	<td nowrap="true">
    		车辆用途:
    	</td>
    	<td nowrap="true">
    		<input type="text" id="carUseType" readonly="readonly" class="middle_txt" name="carUseType" value="${po.carUseType }"/>
    	</td>
    	
    </tr>
   
    <tr >
    	<td nowrap="true">
    		用户姓名:
    	</td>
    	<td nowrap="true">
    		<input type="text" id="userName" readonly="readonly" class="middle_txt" name="userName" value="${po.userName}"/>
    	</td>
    	<td nowrap="true">
    		地址:
    	</td>
    	<td nowrap="true">
    		<input type="text" id="userAddr" readonly="readonly" class="middle_txt"  name="userAddr" value="${po.userAddr}"/>
    	</td>
    	<td nowrap="true">
    		联系电话:
    	</td>
    	<td >
    		<input type="text" id="userPhone" readonly="readonly" class="middle_txt" name="userPhone"  value="${po.userPhone}"/>
    	</td>
    </tr>
    
    <tr >
    	<td nowrap="true">
    		保养信息
    	</td>
    	<td colspan="5">
    		&nbsp;是否按时保养:<input type="radio" name="isKeepFit" disabled="disabled"  class="isKeepFit" value="是"/>是
    		<input type="radio" name="isKeepFit"  disabled="disabled" class="isKeepFit" value="否"/>否
    	</td>
    </tr>
    
    <tr >
    	<td nowrap="true">
    		故障出现时
    	</td>
    	<td colspan="5">
    		<input type="checkbox" class="carStatusByProblem"  disabled="disabled" name="carStatusByProblem" value="启动"/>启动
    		<input type="checkbox" class="carStatusByProblem"  disabled="disabled" name="carStatusByProblem" value="制动"/>制动
    		<input type="checkbox" class="carStatusByProblem"  disabled="disabled" name="carStatusByProblem" value="怠速"/>怠速
    		<input type="checkbox" class="carStatusByProblem"  disabled="disabled" name="carStatusByProblem" value="加速"/>加速
    		<input type="checkbox" class="carStatusByProblem"  disabled="disabled"  name="carStatusByProblem" value="转向"/>转向
    		<input type="checkbox" class="carStatusByProblem"  disabled="disabled"  name="carStatusByProblem" value="冷车"/>冷车
    		<input type="checkbox" class="carStatusByProblem"  disabled="disabled"  name="carStatusByProblem" value="热车"/>热车
    		<input type="checkbox" class="carStatusByProblem"  disabled="disabled"  name="carStatusByProblem" value="满载"/>满载
    		<input type="checkbox" class="carStatusByProblem"  disabled="disabled"  name="carStatusByProblem" value="空载"/>空载
    	</td>
    </tr>
    <tr >
    	<td nowrap="true">
    		车辆状态:
    	</td>
    	<td colspan="5" nowrap="true">
    		&nbsp;行驶速度：<input id="runSpeed"  type="text" readonly="readonly" class="short_txt" name="runSpeed"  maxlength="4" value="${po.runSpeed}"/>Km/h;
    		&nbsp;发动机转速：<input type="text" id="engineSpeed" class="short_txt" readonly="readonly"  name="engineSpeed" maxlength="4" value="${po.engineSpeed}"/>rpm; 
    		&nbsp;其他：<input type="text" name="carStatusRemark" class="middle_txt" readonly="readonly" value="${po.carStatusRemark }" />
    	</td>
    <tr >
    	<td nowrap="true">
    		路面情况:
    	</td>
    	<td colspan="5">
    		<input type="checkbox" name="wayStatus" disabled="disabled" class="wayStatus"  value="平坦"/>平坦
    		<input type="checkbox" name="wayStatus" disabled="disabled"class="wayStatus"  value="凹凸"/>凹凸
    		<input type="checkbox" name="wayStatus" disabled="disabled"class="wayStatus"  value="砂石"/>砂石
    		<input type="checkbox" name="wayStatus" disabled="disabled"class="wayStatus"  value="结冰"/>结冰
    		<input type="checkbox" name="wayStatus" disabled="disabled"class="wayStatus"  value="积雪"/>积雪
    		<input type="checkbox" name="wayStatus" disabled="disabled"class="wayStatus"  value="积水"/>积水
    		<input type="checkbox" name="wayStatus" disabled="disabled"class="wayStatus"  value="上坡"/>上坡
    		<input type="checkbox" name="wayStatus" disabled="disabled"class="wayStatus"  value="下坡"/>下坡
    		<input type="checkbox" name="wayStatus" disabled="disabled"class="wayStatus"  value="其它"/>其它
    	</td>
    </tr>
    <tr >
    	<td nowrap="true">
    		天气情况:
    	</td>
    	<td colspan="2">
    		<input type="checkbox" name="weatherStatus" disabled="disabled" class="weatherStatus" value="晴天"/>晴天
    		<input type="checkbox" name="weatherStatus" disabled="disabled" class="weatherStatus"value="干燥"/>干燥
    		<input type="checkbox" name="weatherStatus" disabled="disabled" class="weatherStatus" value="雾天"/>雾天
    		<input type="checkbox" name="weatherStatus" disabled="disabled" class="weatherStatus"value="雨天"/>雨天
    		<input type="checkbox" name="weatherStatus" disabled="disabled" class="weatherStatus"value="雪天"/>雪天
    	</td>
    	<td colspan="3" nowrap="true">
    		&nbsp;&nbsp;&nbsp;油液泄漏:
    		<input type="radio" name="oilLeak"  class="oilLeak" disabled="disabled" value="无"/>无
    		<input type="radio" name="oilLeak"  class="oilLeak" disabled="disabled" value="有"/>有,
    		具体为：<input type="text" name="oilLeaRemark" class="middle_txt" readonly="readonly" value="${po.oilLeaRemark}"/>
    	</td>
    </tr>
    <tr >
    	<td rowspan="3">
    		故障属性:
    	</td>
    	<td colspan="5">
    		<input type="radio" name="problemProperties" disabled="disabled"  class="problemProperties" value="A类重大及危害性问题"/>A类重大及危害性问题
    		<input type="radio" name="problemProperties" disabled="disabled" class="problemProperties"value="B类主要质量问题"/>B类主要质量问题
    		<input type="radio" name="problemProperties" disabled="disabled" class="problemProperties"value="C类次要质量问题"/>C类次要质量问题
    		<input type="radio" name="problemProperties" disabled="disabled" class="problemProperties"value="D类持续改进问题"/>D类持续改进问题
    	</td>
    </tr>
    
    <tr >
    	<td colspan="3" nowrap="true">
    		&nbsp;同车同故障:
    		<input type="radio" name="problemTheSame" disabled="disabled" class="problemTheSame" value="否"/>否
    		<input type="radio" name="problemTheSame" disabled="disabled" class="problemTheSame" value="是"/>是,
    		第&nbsp;<input type="text" readonly="readonly" name="problemTheSameRemark" id="problemTheSameRemark" maxlength="2" class="mini_txt" value="${po.problemTheSameRemark==0?'':po.problemTheSameRemark}"/>次反馈；
    	</td>
    	<td colspan="2">
    		是否为批量问题：
    		<input type="radio" name="isAduit"  disabled="disabled" class="isAduit" value="否"/>否
    		<input type="radio" name="isAduit" disabled="disabled" class="isAduit" value="是"/>是
    	</td>
    	
    </tr>
    <tr>
    	<td colspan="5">
    		&nbsp;&nbsp;故障代码:&nbsp;
    		<input type="radio" name="problemCode" disabled="disabled" class="problemCode" value="无"/>无
    		<input type="radio" name="problemCode" disabled="disabled" class="problemCode" value="有"/>有,
    		具体为：<input type="text" id="problemCodeRemark" readonly="readonly" name="problemCodeRemark" class="middle_txt"  value="${po.problemCodeRemark}"/>
    	</td>
    </tr>
     
    <tr>
    	<td nowrap="true">主故障件</td>
    	<td colspan="5">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;故障件代码&nbsp;&nbsp;&nbsp;
    		<input type="text" id="firstProblemCode" readonly="readonly" name="firstProblemCode" maxlength="30" class="middle_txt" value="${po.firstProblemCode}"/>
    		&nbsp;故障件名称&nbsp;
    		<input type="text" id="firstProblemName" readonly="readonly" name="firstProblemName" maxlength="30" class="middle_txt" value="${po.firstProblemName}"/>
    		<br/>
    		&nbsp;主故障件供应商代码
    		<input type="text" id="firstProblemSupplierCode" readonly="readonly" maxlength="30"name="firstProblemSupplierCode" class="middle_txt" value="${po.firstProblemSupplierCode}"/>
    		&nbsp;&nbsp;&nbsp;目录分组&nbsp;
    		<input type="text" class="middle_txt" readonly="readonly" value="<change:change val="${po.listGroup}" type="<%=Constant.LISTGROUP%>"/>"/>
    	</td>
    </tr>
     <tr >
     	<td nowrap="true">
     		零件更换状态:
     	</td>
    	<td colspan="5">
    		<input type="checkbox" name="partChangeStatus" disabled="disabled" class="partChangeStatus" value="未涉及零件更换"/>未涉及零件更换
    		<input type="checkbox" name="partChangeStatus" disabled="disabled" class="partChangeStatus" value="零件已更换"/>零件已更换
    		<input type="checkbox" name="partChangeStatus" disabled="disabled" class="partChangeStatus" value="零件尚未更换"/>零件尚未更换
    	</td>
    </tr>
    
     <tr >
     	<td nowrap="true">
     		主题
    	</td>
    	<td colspan="5">
    		<textarea readonly="readonly"    style="width: 100%;height: 40px;" name="theme" >${po.theme }</textarea>
    	</td>
    </tr>
     <tr >
     	<td nowrap="true">
     		客户抱怨及需求
    	</td>
    	<td colspan="5">
    		<textarea style="width: 100%;height: 80px;"    name="customerComplainNeed" readonly="readonly">${po.customerComplainNeed }</textarea>
    	</td>
    </tr>
     <tr height="80px;">
     	<td class="problemReason" nowrap="true">
     		故障原因分析
    	</td>
    	<td colspan="5">
    		<textarea style="width: 100%;height: 80px;"    id="problemReason" readonly="readonly" name="problemReason">${po.problemReason }</textarea>
    	</td>
    </tr>
     <tr height="80px;">
     	<td class="checkStepFit" nowrap="true">
     		检查步骤及维修措施
    	</td>
    	<td colspan="5">
    		<textarea style="width: 100%;height:80px;"    id="checkStepFit" readonly="readonly" name="checkStepFit" >${po.checkStepFit }</textarea>
    	</td>
    </tr>
     <tr height="80px;">
     	<td class="dealAdvice" nowrap="true">
     		处理结果及建议
    	</td>
    	<td colspan="5" nowrap="true">
    		<textarea style="width: 100%;height: 80px;"  readonly="readonly" id="dealAdvice" name="dealAdvice">${po.dealAdvice}</textarea>
    	</td>
    </tr>
     <tr height="80px;">
     	<td  nowrap="true" class="technicalAuditAdvice">
     		技术支持部审批意见
    	</td>
    	<td colspan="5">
    		<textarea  style="width: 100%;height: 80px;" id="technicalAuditAdvice" readonly="readonly"  name="technicalAuditAdvice">${po.technicalAuditAdvice }</textarea>
    	</td>
    </tr>
     <tr height="80px;" >
     	<td nowrap="true" class="againAuditAdvice">
     		转市场质量部意见
    	</td>
    	<td colspan="5">
    		<textarea  style="width: 100%;height: 80px;" readonly="readonly"  id="againAuditAdvice" name="againAuditAdvice">${po.againAuditAdvice }</textarea>
    	</td>
    </tr>
     <tr height="80px;">
     	<td nowrap="true" class="applyNews">
     		市场质量部回复
    	</td>
    	<td colspan="5">
    		<textarea  style="width: 100%;height: 80px;" id="applyNews" name="applyNews">${po.applyNews }</textarea>
    	</td>
    </tr>
    <br/>
	 <!-- 添加附件 开始  -->
        <table id="add_file"  width="75%" class="table_info" border="0" id="file">
	    		<tr>
	        		<th>
						<input type="hidden" id="fjids" name="fjids"/>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
						<font color="red">
							<span id="span1"></span>
						</font>
					</th>
				</tr>
				<tr>
    				<td width="75%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
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
</table>
<table width=100% border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="12" align=left width="33%">&nbsp;</td>
				
             	<td height="12" align=center width="37%">
             	<input class="normal_btn" type="button" id="btn1" value="维修历史" onclick="maintaimHistory();"/>&nbsp;
             	&nbsp;
	            <input class="normal_btn" type="button" id="btn3" value="保养历史" onclick="freeMaintainHistory();"/>&nbsp;
				&nbsp;
				<input class="normal_btn" type="button" id="btn4" value="审核历史" onclick="auditRecord();"/>&nbsp;
             	&nbsp;
             	<input type="button" class="normal_btn" id="pass"  style="width=8%" value="通过 "/>
           		&nbsp;
               	<input type="button" class="normal_btn" id="refuse" style="width=8%" value="回复 "/>
               	&nbsp;
				<input type="button" onClick="history.back();" class="normal_btn"  style="width=8%" value="返回"/>
    			</td>
            	<td height="12" align=center width="30%">
      			</td>
			</tr>
		</table>
<br/>
</form>
<!--页面列表 begin -->
</body>
<script type="text/javascript">
//通过
$("#pass").live("click",function(){

	if($.trim($("#applyNews").val())==""){
		MyAlert("提示：市场质量部回复不能为空！");
		$("#applyNews").val("");
		return;
	}
	$("#verifyStatus").val("95531005");
	MyConfirm("是否确认通过并结束流程？",passMsg,"");
	
});
function passMsg(){
	var url="<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/auditPassByQualityDept.do";
	sendDo(url);
}
function refuseMsg(){
	var url="<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/auditRefuseByQualityDept.do";
	sendDo(url);
}
//驳回
$("#refuse").live("click",function(){
	if($.trim($("#applyNews").val())==""){
		MyAlert("提示：市场质量部回复不能为空！");
		$("#applyNews").val("");
		return;
	}
	$("#verifyStatus").val("95531006");
	MyConfirm("是否确认回复给技术部？",refuseMsg,"");
});
function sendDo(url){
	$("#fm").attr("action",url);
	$("#fm").submit();
}
//维修历史按钮方法
var vin = $("#vin").val() ;
function maintaimHistory(){
	window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/maintaimHistory.do?VIN='+vin);
}
//保养历史按钮方法
function freeMaintainHistory(){
	window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/freeMaintainHistory.do?VIN='+vin);
}
function auditRecord(){
	var qualityid= $("#qualityid").val();
	window.open('<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/auditRecord.do?qualityid='+qualityid);
}
</script>
</html>
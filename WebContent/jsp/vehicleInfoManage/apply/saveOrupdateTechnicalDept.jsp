<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
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
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<title>品质情报填写</title>
<script type="text/javascript">
	$(function(){
		var vin = $("#vin").val() ;
		if(vin=="" ||vin==null){
			$("#btn1").attr("disabled",true);
			$("#btn3").attr("disabled",true);
		}
		var d = new Date();
		var vYear = d.getFullYear();
		var vMon = d.getMonth() + 1;
		var vDay = d.getDate();
		var currTime=(vYear)+"-"+(vMon<10 ? "0" + vMon : vMon)+"-"+(vDay<10 ?  "0"+ vDay : vDay) ;
		$("#reportDate").val(currTime);	
		var productQuality=$("#productQuality").val();
		isradio(productQuality,"productQuality");
		//MyAlert($("#productQuality").val());
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
				$(this).attr("checked",true);
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
						$(this).attr("checked",true);
					}
				}
			});
		}
	}
</script>
</head>
<body>
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;车辆信息管理&gt;品质情报填写
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
	<table  border="1" cellpadding="1" cellspacing="1" class="tab_edit" width="100%" style="text-align: center;">
		<tr>
			<td nowrap="true">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;是否维修：
    		<input type="radio" id="isfit" class="isfit" name="isFit" value="是"/>是
    		&nbsp;&nbsp;
    		<input type="radio" id="isnotfit" name="isFit" class="isfit" value="否"/>否
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;填报日期:<input type="text" id="reportDate" name="reportDate"class="middle_txt"  readonly="readonly" value=""/> 
			</td>
		</tr>
		<tr>
			<td nowrap="true" >
				<div style="display: none">
				服务商代码: <input type="text" name="dealerCode" disabled="disabled" readonly="readonly" class="middle_txt"  value="${po.dealerCode }"/>  
				服务商名称: <input type="text" name="dealerName" disabled="disabled" readonly="readonly" class="middle_txt" value="${po.dealerName }"/>  
			</div>
					&nbsp;&nbsp;&nbsp;&nbsp;填报人:&nbsp;&nbsp;&nbsp;<input type="text" class="middle_txt" maxlength="15" name="reportName" id="reportName" value="${po.reportName}" />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;联系方式:<input type="text" class="middle_txt" name="contactType" id="contactType" maxlength="15" value="${po.contactType }" />
			</td>
		</tr>
		
	</table>
</div>
<table border="1"   class="tab_edit" width="100%" style="text-align: left;">
	<tr>
		<td nowrap="true" width="10%" style="border: none;">
			车辆信息：
    	</td>
    	<td nowrap="true" width="25%" style="border: none;">
    	<input type="radio"  class="productQuality"  name="productQuality" value="售前车"/>售前车
    		&nbsp;&nbsp;
    		<input type="radio"  name="productQuality"  class="productQuality" value="售后车"/>售后车
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
    	<td nowrap="true" class="carSpend">
    		变速器号:
    	</td>
    	<td >
    		<input type="text" id="carSpend" maxlength="25" name="carSpend"class="middle_txt" value="${po.carSpend}"/>
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
    		<input type="text" id="faultDate" readonly="readonly" class="middle_txt"  name="faultDate" value="${po.faultDate}"/>
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
    		&nbsp;是否按时保养:<input type="radio" name="isKeepFit"   class="isKeepFit" value="是"/>是
    		<input type="radio" name="isKeepFit"    class="isKeepFit" value="否"/>否
    	</td>
    </tr>
    
    <tr >
    	<td nowrap="true">
    		故障出现时
    	</td>
    	<td colspan="5">
    		<input type="checkbox" class="carStatusByProblem"    name="carStatusByProblem" value="启动"/>启动
    		<input type="checkbox" class="carStatusByProblem"   name="carStatusByProblem" value="制动"/>制动
    		<input type="checkbox" class="carStatusByProblem"   name="carStatusByProblem" value="怠速"/>怠速
    		<input type="checkbox" class="carStatusByProblem"   name="carStatusByProblem" value="加速"/>加速
    		<input type="checkbox" class="carStatusByProblem"    name="carStatusByProblem" value="转向"/>转向
    		<input type="checkbox" class="carStatusByProblem"    name="carStatusByProblem" value="冷车"/>冷车
    		<input type="checkbox" class="carStatusByProblem"    name="carStatusByProblem" value="热车"/>热车
    		<input type="checkbox" class="carStatusByProblem"    name="carStatusByProblem" value="满载"/>满载
    		<input type="checkbox" class="carStatusByProblem"    name="carStatusByProblem" value="空载"/>空载
    	</td>
    </tr>
    <tr >
    	<td nowrap="true">
    		车辆状态:
    	</td>
    	<td colspan="5" nowrap="true">
    		&nbsp;行驶速度：<input id="runSpeed"  type="text" onblur="checkNum(this);" class="short_txt" name="runSpeed"  maxlength="4" value="${po.runSpeed}"/>Km/h;
    		&nbsp;发动机转速：<input type="text" id="engineSpeed" onblur="checkNum(this);" class="short_txt"  name="engineSpeed" maxlength="4" value="${po.engineSpeed}"/>rpm; 
    		&nbsp;其他：<input type="text" name="carStatusRemark" class="middle_txt" value="${po.carStatusRemark }" />
    	</td>
    <tr >
    	<td nowrap="true">
    		路面情况:
    	</td>
    	<td colspan="5">
    		<input type="checkbox" name="wayStatus" class="wayStatus"  value="平坦"/>平坦
    		<input type="checkbox" name="wayStatus" class="wayStatus"  value="凹凸"/>凹凸
    		<input type="checkbox" name="wayStatus" class="wayStatus"  value="砂石"/>砂石
    		<input type="checkbox" name="wayStatus" class="wayStatus"  value="结冰"/>结冰
    		<input type="checkbox" name="wayStatus" class="wayStatus"  value="积雪"/>积雪
    		<input type="checkbox" name="wayStatus" class="wayStatus"  value="积水"/>积水
    		<input type="checkbox" name="wayStatus" class="wayStatus"  value="上坡"/>上坡
    		<input type="checkbox" name="wayStatus" class="wayStatus"  value="下坡"/>下坡
    		<input type="checkbox" name="wayStatus" class="wayStatus"  value="其它"/>其它
    	</td>
    </tr>
    <tr >
    	<td nowrap="true">
    		天气情况:
    	</td>
    	<td colspan="2">
    		<input type="checkbox" name="weatherStatus"  class="weatherStatus" value="晴天"/>晴天
    		<input type="checkbox" name="weatherStatus"  class="weatherStatus"value="干燥"/>干燥
    		<input type="checkbox" name="weatherStatus"  class="weatherStatus" value="雾天"/>雾天
    		<input type="checkbox" name="weatherStatus"  class="weatherStatus"value="雨天"/>雨天
    		<input type="checkbox" name="weatherStatus"  class="weatherStatus"value="雪天"/>雪天
    	</td>
    	<td colspan="3" nowrap="true">
    		&nbsp;&nbsp;&nbsp;油液泄漏:
    		<input type="radio" name="oilLeak"  class="oilLeak" value="无"/>无
    		<input type="radio" name="oilLeak"  class="oilLeak" value="有"/>有,
    		具体为：<input type="text" id="oilLeaRemark" name="oilLeaRemark" class="middle_txt"  value="${po.oilLeaRemark}"/>
    	</td>
    </tr>
    <tr >
    	<td rowspan="3">
    		故障属性:
    	</td>
    	<td colspan="5">
    		<input type="radio" name="problemProperties"  class="problemProperties" value="A类重大及危害性问题"/>A类重大及危害性问题
    		<input type="radio" name="problemProperties" class="problemProperties"value="B类主要质量问题"/>B类主要质量问题
    		<input type="radio" name="problemProperties" class="problemProperties"value="C类次要质量问题"/>C类次要质量问题
    		<input type="radio" name="problemProperties" class="problemProperties"value="D类持续改进问题"/>D类持续改进问题
    	</td>
    </tr>
    
    <tr >
    	<td colspan="3" nowrap="true">
    		&nbsp;同车同故障:
    		<input type="radio" name="problemTheSame" class="problemTheSame" value="否"/>否
    		<input type="radio" name="problemTheSame" class="problemTheSame" value="是"/>是,
    		第&nbsp;<input type="text" name="problemTheSameRemark" onblur="checkNum(this);" id="problemTheSameRemark" maxlength="2" class="mini_txt" value="${po.problemTheSameRemark==0?'':po.problemTheSameRemark}"/>次反馈；
    	</td>
    	<td colspan="2">
    		是否为批量问题：
    		<input type="radio" name="isAduit"  class="isAduit" value="否"/>否
    		<input type="radio" name="isAduit" class="isAduit" value="是"/>是
    	</td>
    	
    </tr>
    <tr>
    	<td colspan="5">
    		&nbsp;&nbsp;故障代码:&nbsp;
    		<input type="radio" name="problemCode" class="problemCode" value="无"/>无
    		<input type="radio" name="problemCode" class="problemCode" value="有"/>有,
    		具体为：<input type="text" id="problemCodeRemark" name="problemCodeRemark" class="middle_txt"  value="${po.problemCodeRemark}"/>
    	</td>
    </tr>
     
    <tr>
    	<td nowrap="true">主故障件</td>
    	<td colspan="5">
    		&nbsp;<input type="button" class="min_btn" id="choose" value="...." />
			&nbsp;&nbsp;故障件代码&nbsp;&nbsp;&nbsp;
    		<input type="text" id="firstProblemCode" readonly="readonly" name="firstProblemCode" maxlength="30" class="middle_txt" value="${po.firstProblemCode}"/>
    		&nbsp;故障件名称&nbsp;
    		<input type="text" id="firstProblemName" readonly="readonly" name="firstProblemName" maxlength="30" class="middle_txt" value="${po.firstProblemName}"/>
    		<br/>
    		&nbsp;主故障件供应商代码&nbsp;
    		<input type="text" id="firstProblemSupplierCode" maxlength="30"name="firstProblemSupplierCode" class="middle_txt" value="${po.firstProblemSupplierCode}"/>
    		&nbsp;&nbsp;&nbsp;目录分组&nbsp;
    		<script type="text/javascript">
    		genSelBoxExp("listGroup",<%=Constant.LISTGROUP%>,"${po.listGroup}",true,"short_sel","","false",'');
    		</script>
    	</td>
    </tr>
     <tr >
     	<td nowrap="true">
     		零件更换状态:
     	</td>
    	<td colspan="5">
    		<input type="checkbox" name="partChangeStatus" class="partChangeStatus" value="未涉及零件更换"/>未涉及零件更换
    		<input type="checkbox" name="partChangeStatus" class="partChangeStatus" value="零件已更换"/>零件已更换
    		<input type="checkbox" name="partChangeStatus"  class="partChangeStatus" value="零件尚未更换"/>零件尚未更换
    	</td>
    </tr>
    
     <tr height="40px;">
     	<td  class="theme" nowrap="true" >
     		主题
    	</td>
    	<td colspan="5">
    		<textarea style="width: 100%;height: 40px;" id="theme" name="theme" >${po.theme }</textarea>
    	</td>
    </tr>
     <tr height="40px;">
     	<td class="customerComplainNeed" nowrap="true" >
     		客户抱怨及需求
    	</td>
    	<td colspan="5">
    		<textarea style="width: 100%;height: 80px;" id="customerComplainNeed" name="customerComplainNeed" >${po.customerComplainNeed }</textarea>
    	</td>
    </tr>
     <tr height="80px;" style="display: none">
     	<td class="problemReason" nowrap="true">
     		检查步骤及故障原因分析
    	</td>
    	<td colspan="5">
    		<textarea style="width: 100%;height: 80px;" id="problemReason" name="problemReason">${po.problemReason }</textarea>
    	</td>
    </tr>
     <tr height="80px;" style="display: none">
     	<td class="checkStepFit"  nowrap="true">
     		维修措施及结果
    	</td>
    	<td colspan="5">
    		<textarea style="width: 100%;height:80px;" id="checkStepFit" name="checkStepFit" >${po.checkStepFit }</textarea>
    	</td>
    </tr>
     <tr height="40px;" >
     	<td class="dealAdvice" nowrap="true">
     		建议
    	</td>
    	<td colspan="5">
    		<textarea style="width: 100%;height: 80px;" id="dealAdvice" name="dealAdvice">${po.dealAdvice}</textarea>
    	</td>
    </tr>
     <tr height="80px;" style="display: none">
     	<td  nowrap="true" class="technicalAuditAdvice">
     		技术支持部审批意见
    	</td>
    	<td colspan="5">
    		<textarea  style="width: 100%;height: 80px;" id="technicalAuditAdvice" name="technicalAuditAdvice">${po.technicalAuditAdvice }</textarea>
    	</td>
    </tr>
     <tr height="80px;" style="display: none" >
     	<td nowrap="true" class="againAuditAdvice">
     		转市场质量部意见
    	</td>
    	<td colspan="5">
    		<textarea  style="width: 100%;height: 80px;" id="againAuditAdvice" name="againAuditAdvice">${po.againAuditAdvice }</textarea>
    	</td>
    </tr>
     <tr height="80px;"style="display: none">
     	<td nowrap="true">
     		检查步骤及故障原因分析
    	</td>
    	<td colspan="5">
    		<textarea  style="width: 100%;height: 80px;" name="problemReason">${po.problemReason }</textarea>
    	</td>
    </tr>
     <tr height="80px;" style="display: none">
     	<td nowrap="true">
     		市场质量部回复
    	</td>
    	<td colspan="5">
    		<textarea style="width: 100%;height: 80px;" name="applyNews" >${po.applyNews }</textarea>
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
			     		<input type="button" class="normal_btn" align="right" id="addfile" onclick="showUpload('<%=contextPath%>')" value ='添加附件'/><font color="red">*</font>
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
             	<td height="12" align=center width="33%" nowrap="true">
                	<input type="button" class="normal_btn" id="saveOrupdate"  style="width=8%" value="保存 " />
                	<input type="button" class="normal_btn" id="report"  style="width=8%" value="上报 "/>
					<input class="normal_btn" type="button" id="btn1" value="维修历史" onclick="maintaimHistory();"/>&nbsp;
	                <input class="normal_btn" type="button" id="btn3" value="保养历史" onclick="freeMaintainHistory();"/>&nbsp;
					<input type="button" onClick="history.back();" class="normal_btn"  style="width=8%" value="返回"/>
    			</td>
            	<td height="12" align=center width="33%">
      			</td>
			</tr>
		</table>
<br/>
</form>
<!--页面列表 begin -->
</body>
<script type="text/javascript">
	
	$("#saveOrupdate").live("click",function(){
		var strRadio="isFit,productQuality,isKeepFit,problemTheSame,oilLeak,isAduit,problemCode,problemProperties";
		var textStrRadio="是否维修,车辆信息,是否按时保养,同车同故障,油液泄漏,是否为批量问题,故障代码,故障属性";
		var strRadios=strRadio.split(",");
		var textStrRadios= textStrRadio.split(",");
		var alertStr="";
		for(var j=0;j<strRadios.length;j++){
			if($('input:radio[name="'+strRadios[j]+'"]:checked').val()==null){
				alertStr+= " ["+textStrRadios[j]+"]";
			}
		}
		var strCheck="partChangeStatus,weatherStatus,wayStatus,carStatusByProblem";
		var textCheck="零件更换状态,天气情况,路面情况,故障出现时";
		var strChecks=strCheck.split(",");
		var textChecks= textCheck.split(",");
		for(var k=0;k<strChecks.length;k++){
			if($('input[name="'+strChecks[k]+'"]').is(':checked')==false){
				alertStr+= " ["+textChecks[k]+"]";
			}
		} 
		
		var str="reportName,contactType,runSpeed,engineSpeed,firstProblemCode,firstProblemName,firstProblemSupplierCode,listGroup";
		var textStr="填报人,联系方式,行驶速度,发动机怠速,主故障件代码,主故障件名称,主故障件供应商代码,目录分组";
        if(str!=""){
			var strs= str.split(",");
			var textStrs= textStr.split(",");
			for(var i=0;i<strs.length;i++){
				if($("#"+strs[i]).val().length==0){
					alertStr+=" ["+textStrs[i]+"] ";
				}
			}
			if(alertStr!=""){
				MyAlert("提示：请填写必填字段或选择{"+alertStr+"}");
				return;
			}
		}
		if(checkPass()==true){
			var msg="";
			if($('input:radio[name="oilLeak"]:checked').val()=='有' && $("#oilLeaRemark").val().length==0){
				msg+="[提示：油液泄漏勾选有后请填写具体为字段！]";
			}
			if($('input:radio[name="problemTheSame"]:checked').val()=='是' && $("#problemTheSameRemark").val().length==0){
				msg+=" [提示：同车同故障勾选是后请填写第?次反馈字段！]";
			}
			if($('input:radio[name="problemCode"]:checked').val()=='有' && $("#problemCodeRemark").val().length==0 ){
				msg+=" [提示：故障代码勾选有后请填写具体为字段！]";
				
			}
			if(msg!=""){
				MyAlert(msg);
				return;
			}
        	$("#verifyStatus").val("95451001");
        	MyConfirm("是否确认保存？",saveOrUpdateMsg,"");
			
		}else{
			MyAlert("提示：未填写的字段有 "+textAlert);
		}
	});
	function saveOrUpdateMsg(){
		var url="<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/saveOrupdateInfo1.do";
		sendDo(url);
	}
	$("#report").live("click",function(){
		var strRadio="isFit,productQuality,isKeepFit,problemTheSame,oilLeak,isAduit,problemCode,problemProperties";
		var textStrRadio="是否维修,车辆信息,是否按时保养,同车同故障,油液泄漏,是否为批量问题,故障代码,故障属性";
		var strRadios=strRadio.split(",");
		var textStrRadios= textStrRadio.split(",");
		var alertStr="";
		for(var j=0;j<strRadios.length;j++){
			if($('input:radio[name="'+strRadios[j]+'"]:checked').val()==null){
				alertStr+= " ["+textStrRadios[j]+"]";
			}
		}
		var strCheck="partChangeStatus,weatherStatus,wayStatus,carStatusByProblem";
		var textCheck="零件更换状态,天气情况,路面情况,故障出现时";
		var strChecks=strCheck.split(",");
		var textChecks= textCheck.split(",");
		for(var k=0;k<strChecks.length;k++){
			if($('input[name="'+strChecks[k]+'"]').is(':checked')==false){
				alertStr+= " ["+textChecks[k]+"]";
			}
		} 
		
		var str="reportName,contactType,runSpeed,engineSpeed,firstProblemCode,firstProblemName,firstProblemSupplierCode,listGroup";
		var textStr="填报人,联系方式,行驶速度,发动机怠速,主故障件代码,主故障件名称,主故障件供应商代码,目录分组";
        if(str!=""){
			var strs= str.split(",");
			var textStrs= textStr.split(",");
			for(var i=0;i<strs.length;i++){
				if($("#"+strs[i]).val().length==0){
					alertStr+=" ["+textStrs[i]+"] ";
				}
			}
			if(alertStr!=""){
				MyAlert("提示：请填写必填字段或选择{"+alertStr+"}");
				return;
			}
		}
		if(checkPass()==true){
			var msg="";
			if($('input:radio[name="oilLeak"]:checked').val()=='有' && $("#oilLeaRemark").val().length==0){
				msg+="[提示：油液泄漏勾选有后请填写具体为字段！]";
			}
			if($('input:radio[name="problemTheSame"]:checked').val()=='是' && $("#problemTheSameRemark").val().length==0){
				msg+=" [提示：同车同故障勾选是后请填写第?次反馈字段！]";
			}
			if($('input:radio[name="problemCode"]:checked').val()=='是' && $("#problemCodeRemark").val().length==0 ){
				msg+=" [提示：故障代码勾选有后请填写具体为字段！]";
				
			}
			if(msg!=""){
				MyAlert(msg);
				return;
			}
			$("#verifyStatus").val("95451002");
			if($("input[name='fjid']").val()==undefined){
				MyAlert("提示：请上传附件！");
				return;
			}
			MyConfirm("是否确认上报？",reportMsg,"");
		}else{
			MyAlert("提示：未填写的字段有 "+textAlert);
		}
	});
	function reportMsg(){
		var url="<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/saveOrupdateInfo1.do";
		sendDo(url);
	}
	function sendDo(url){
		$("#fm").attr("action",url);
		$("#fm").submit();
	}

	function checkPass(){
		textAlert="";
		var fields="dealAdvice,theme,customerComplainNeed";
		checkNull(fields);
		if(textAlert!=""){
			return false;
		}
		return true;
	}
	function checkNull(fields){
		var str=fields.split(",");
		for(var i=0;i<str.length;i++){
			var tempstr=$("#"+str[i]).val();
			var val=$.trim(tempstr);
			if(val==""|| val==null){
				textAlert=textAlert+"["+$("."+str[i]).text()+"] ";
			}
		}
	}
	
	//填写索赔单号再取到VIN然后带出信息
	function changeDataFrist(claim_no,vin,create_date){
		$("#vin").val(vin);
		$("#claimNo").val(claim_no);
		$("#faultDate,#serviceDate").val(create_date);
		sendAjax('<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/queryDataByVin.json?vin='+vin,queryDateByVinBack,'fm');
	}
	
	function queryDateByVinBack(json){
		var t=json.queryDataByVin;
		$("#carUseType").val(t.GAME_NAME);//什么类型的车
		//$("#carType").val(t.MODEL_ID);//车型ID
		//$("#carClass").val(t.SERIES_ID);//车系ID
		$("#carClass").val(t.CXNAME);//车系ID
		$("#carType").val(t.MODEL_NAME);//车型ID
		$("#carNo").val(t.LICENSE_NO);//车牌号
		$("#engineNo").val(t.ENGINE_NO);//发动机号
		$("#mileage").val(t.MILEAGE);//里程数
		$("#productDate").val(t.PRODUCT_DATE);//生产日期
		$("#buyCarDate").val(t.BUY_DATE);//购车日期
		$("#userPhone").val(t.PHONE);//联系电话
		$("#userName").val(t.CTM_NAME);//用户姓名
		$("#userAddr").val(t.ADDRESS);//用户地址
	}
	$("#isfit").live("click",function(){
		OpenHtmlWindow('<%=contextPath%>/jsp/vehicleInfoManage/apply/selectPartFirst.jsp',800,500);
		$("#vin").attr("readonly","readonly");
		$("#faultDate").unbind("focus");
	});
	$("#isnotfit").live("click",function(){
		$("#carUseType").val("");//什么类型的车
		$("#carType").val("");//车型ID
		$("#carClass").val("");//车系ID
		$("#carNo").val("");//车牌号
		$("#engineNo").val("");//发动机号
		$("#mileage").val("");//里程数
		$("#productDate").val("");//生产日期
		$("#buyCarDate").val("");//购车日期
		$("#userPhone").val("");//联系电话
		$("#userName").val("");//用户姓名
		$("#userAddr").val("");//用户地址
		$("#faultDate").val("");//
		$("#serviceDate").val("");
		$("#vin").val("");
		$("#vin").removeAttr("readonly","");
		MyAlert("提示：请手动输入底盘号！");
		$("#vin").focus(); 
		$("#faultDate").bind("focus",function(){
			calendar();
		});
	});
	$("#vin").live("blur",function(){
		var vin=$("#vin").val();
		sendAjax('<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/queryDataByVin.json?vin='+vin,queryDateByVinBack2,'fm');
	});
	function queryDateByVinBack2(json){
		var t=json.queryDataByVin;
		if($("#vin").val()!=""&& t==null){
			MyAlert("提示：请输入正确的底盘号，再带出信息！");
			$("#vin").val("");
			return;
		}
		$("#carUseType").val(t.GAME_NAME);//什么类型的车
		//$("#carType").val(t.MODEL_ID);//车型ID
		//$("#carClass").val(t.SERIES_ID);//车系ID
		$("#carClass").val(t.CXNAME);//车系ID
		$("#carType").val(t.MODEL_NAME);//车型ID
		$("#carNo").val(t.LICENSE_NO);//车牌号
		$("#engineNo").val(t.ENGINE_NO);//发动机号
		$("#mileage").val(t.MILEAGE);//里程数
		$("#productDate").val(t.PRODUCT_DATE);//生产日期
		$("#buyCarDate").val(t.BUY_DATE);//购车日期
		$("#userPhone").val(t.PHONE);//联系电话
		$("#userName").val(t.CTM_NAME);//用户姓名
		$("#userAddr").val(t.ADDRESS);//用户地址
		$("#vin").attr("readonly","readonly");
	}
	function checkNum(obj){
		var reg=/(0|[1-9][0-9]*)$/;
		if(obj.value!=""&&!reg.test(obj.value)){
			MyAlert("提示：请输入正整数！");
			obj.value="";
		}
	}
	$(".oilLeak").live("click",function(){
		if($('input:radio[name="oilLeak"]:checked').val()=='无'){
			$("#oilLeaRemark").val("");
			$("#oilLeaRemark").attr("disabled",true);
		}else{
			$("#oilLeaRemark").attr("disabled",false);
		}
	});
	$(".problemCode").live("click",function(){
		if($('input:radio[name="problemCode"]:checked').val()=='无'){
			$("#problemCodeRemark").val("");
			$("#problemCodeRemark").attr("disabled",true);
		}else{
			$("#problemCodeRemark").attr("disabled",false);
		}
	});
	$(".problemTheSame").live("click",function(){
		if($('input:radio[name="problemTheSame"]:checked').val()=='否'){
			$("#problemTheSameRemark").val("");
			$("#problemTheSameRemark").attr("disabled",true);
		}else{
			$("#problemTheSameRemark").attr("disabled",false);
		}
	});
	function infotothers(obj){
		var code=$(obj).val();
		sendAjax('<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/infotothers.json?code='+code,queryDateByCode,'fm');
	}
	var vin = $("#vin").val() ;
	//维修历史按钮方法
	function maintaimHistory(){
		window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/maintaimHistory.do?VIN='+vin);
	}
	//保养历史按钮方法
	function freeMaintainHistory(){
		window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/freeMaintainHistory.do?VIN='+vin);
	}
	$("#choose").live("click",function(){
		OpenHtmlWindow('<%=contextPath%>/jsp/vehicleInfoManage/apply/selectmalfunction.jsp',800,500);
	});
	function selectmalfunctionBack(malcode,malname){
		$("#firstProblemCode").val(malcode);
		$("#firstProblemName").val(malname);
	}
</script>
</html>
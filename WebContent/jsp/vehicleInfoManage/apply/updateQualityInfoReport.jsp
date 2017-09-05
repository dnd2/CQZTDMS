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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<% String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<title>质量信息上报填写</title>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
	$(function(){
		$("input[type='text']").attr("class","middle_txt");
		var d = new Date();
		var vYear = d.getFullYear();
		var vMon = d.getMonth() + 1;
		var vDay = d.getDate();
		var currTime=(vYear)+"-"+(vMon<10 ? "0" + vMon : vMon)+"-"+(vDay<10 ?  "0"+ vDay : vDay) ;
		$("#reportDate").val(currTime);	
			
		if($("#verifyStatus").val()=="95531002"){
			$("#report").hide();
		}
		$("#runSpeed").attr("class","short_txt");
		$("#engineSpeed").attr("class","short_txt");
		$("#problemTheSameRemark").attr("class","short_txt");

		var productQuality=$("#productQuality").val();
		isradio(productQuality,"productQuality");
		var isfit=$("#isFit").val();
		isradio(isfit,"isfit");
		var isKeepFit=$("#isKeepFit").val();
		isradio(isKeepFit,"isKeepFit");
		ischecked("carStatusByProblem");
		ischecked("wayStatus");
		ischecked("weatherStatus");
		var oilLeak=$("#oilLeak").val();
		isradio(oilLeak,"oilLeak");
		ischecked("problemProperties");
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
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;车辆信息管理&gt;质量信息填写
</div>
<form name="fm" id="fm" method="post">
<input type="hidden" id="claimNo" name="claimNo" value="${po.claimNo }"/>
<input type="hidden" id="qualityId" name="qualityId" value="${po.qualityId }"/>
<input type="hidden" id="verifyStatus" name="verifyStatus" value="${po.verifyStatus }"/>
<input type="hidden" id="carStatusByProblem"  value="${po.carStatusByProblem }"/>
<input type="hidden" id="isFit"  value="${po.isFit }"/>
<input type="hidden" id="productQuality"  value="${po.productQuality }"/>
<input type="hidden" id="isKeepFit"  value="${po.isKeepFit }"/>
<input type="hidden" id="wayStatus"  value="${po.wayStatus }"/>
<input type="hidden" id="weatherStatus"  value="${po.weatherStatus }"/>
<input type="hidden" id="oilLeak"  value="${po.oilLeak }"/>
<input type="hidden" id="problemProperties"  value="${po.problemProperties }"/>
<input type="hidden" id="problemTheSame"  value="${po.problemTheSame }"/>
<input type="hidden" id="isAduit"  value="${po.isAduit }"/>
<input type="hidden" id="problemCode"  value="${po.problemCode }"/>
<input type="hidden" id="partChangeStatus"  value="${po.partChangeStatus }"/>
<table border="1" cellpadding="1" cellspacing="1"  class="tab_edit" width="100%" align="center">
	<tr>
		<td width="11%">
			填报人:
    	</td>
    	<td width="14%">
    		<input type="text" name="reportName" value="${po.reportName}"/>
    	</td>
		<td width="11%">
			联系方式：
    	</td>
    	<td width="14%">
    		<input type="text" name="contactType" value="${po.contactType }"/>
    	</td>
		<td width="11%">
			是否维修：
    	</td>
    	<td width="14%">
    		<input type="radio" id="isfit" class="isfit" name="isFit" value="是"/>是
    		<input type="radio" id="isnotfit" name="isFit" class="isfit" value="否"/>否
    	</td>
		<td width="11%">
			 填报日期:
    	</td>
    	<td width="14%">
    		<input type="text" id="reportDate" name="reportDate" value="${po.reportDate}" readonly="readonly"/>
    	</td>
	</tr>
    <tr >
    	<td colspan="2">
    		<h4>产品品质报告</h4>
    	</td>
    	<td colspan="6">
    	<input type="radio" name="productQuality" class="productQuality" value="质量信息反馈"/>质量信息反馈
    	<input type="radio" name="productQuality" class="productQuality" value="技术援助申请"/>技术援助申请
    	</td>
    </tr>
    <tr >
    	<td>
    		车系:
    	</td>
    	<td>
    		<input type="text" id="carClass" readonly="readonly" name="carClass" value="${po.carClass }"/>
    	</td>
    	<td>
    		车型:
    	</td>
    	<td >
    		<input type="text" id="carType" readonly="readonly" name="carType" value="${po.carType }"/>
    	</td>
    	<td>
    		车牌号:
    	</td>
    	<td colspan="3">
    		<input type="text" id="carNo" readonly="readonly" name="carNo" value="${po.carNo }"/>
    	</td>
    </tr>
    
    <tr >
    	<td>
    		底盘号:
    	</td>
    	<td>
    		<input type="text" id="vin" readonly="readonly" name="vin" value="${po.vin}"/>
    	</td>
    	<td>
    		发动机号:
    	</td>
    	<td>
    		<input id="engineNo" readonly="readonly" type="text" name="engineNo" value="${po.engineNo}"/>
    	</td>
    	<td>
    		变速器号:
    	</td>
    	<td colspan="3">
    		<input type="text" name="carSpend" value="${po.carSpend}"/>
    	</td>
    </tr>
    
    <tr >
    	<td>
    		行驶里程:
    	</td>
    	<td>
    		<input type="text" id="mileage" name="mileage" readonly="readonly" value="${po.mileage}"/>
    	</td>
    	<td>
    		生产日期:
    	</td>
    	<td>
    		<input type="text" id="productDate" readonly="readonly" name="productDate" value="${po.productDate}"/>
    	</td>
    	<td>
    		购车日期:
    	</td>
    	<td colspan="3">
    		<input type="text" id="buyCarDate" readonly="readonly" name="buyCarDate" value="${po.buyCarDate}"/>
    	</td>
    </tr>
    
    <tr >
    	<td>
    		故障日期:
    	</td>
    	<td>
    		<input type="text" id="faultDate" readonly="readonly" name="faultDate" value="${po.faultDate}"/>
    	</td>
    	<td>
    		维修日期:
    	</td>
    	<td>
    		<input type="text" id="serviceDate" readonly="readonly" name="serviceDate" value="${po.serviceDate}"/>
    	</td>
    	<td>
    		车辆用途:
    	</td>
    	<td colspan="3" >
    		<input type="text" id="carUseType" readonly="readonly" name="carUseType" value="${po.carUseType }"/>
    	</td>
    </tr>
   
    <tr >
    	<td>
    		用户姓名:
    	</td>
    	<td>
    		<input type="text" id="userName" readonly="readonly" name="userName" value="${po.userName}"/>
    	</td>
    	<td>
    		地址:
    	</td>
    	<td>
    		<input type="text" id="userAddr" readonly="readonly"  name="userAddr" value="${po.userAddr}"/>
    	</td>
    	<td>
    		联系电话:
    	</td>
    	<td colspan="3">
    		<input type="text" id="userPhone" name="userPhone" readonly="readonly" value="${po.userPhone}"/>
    	</td>
    </tr>
    
    <tr >
    	<td>
    		是否按时保养:
    	</td>
    	<td colspan="7">
    		<input type="radio" name="isKeepFit" class="isKeepFit" value="是"/>是
    		<input type="radio" name="isKeepFit" class="isKeepFit" value="否"/>否
    	</td>
    </tr>
    
    <tr >
    	<td>
    		故障出现时
    	</td>
    	<td colspan="7">
    		<input type="checkbox" class="carStatusByProblem" name="carStatusByProblem" value="启动"/>启动
    		<input type="checkbox" class="carStatusByProblem" name="carStatusByProblem" value="制动"/>制动
    		<input type="checkbox" class="carStatusByProblem" name="carStatusByProblem" value="怠速"/>怠速
    		<input type="checkbox" class="carStatusByProblem" name="carStatusByProblem" value="加速"/>加速
    		<input type="checkbox" class="carStatusByProblem"  name="carStatusByProblem" value="转向"/>转向
    		<input type="checkbox" class="carStatusByProblem"  name="carStatusByProblem" value="冷车"/>冷车
    		<input type="checkbox" class="carStatusByProblem"  name="carStatusByProblem" value="热车"/>热车
    		<input type="checkbox" class="carStatusByProblem"  name="carStatusByProblem" value="满载"/>满载
    		<input type="checkbox" class="carStatusByProblem"  name="carStatusByProblem" value="空载"/>空载
    	</td>
    </tr>
    <tr >
    	<td>
    		车辆状态:
    	</td>
    	<td >
    		行驶速度：<input id="runSpeed" type="text" name="runSpeed"  maxlength="4" value="${po.runSpeed}"/>Km/h;
    	</td>
    	<td>
    		发动机怠速：
    	</td>
    	<td>
    		<input type="text" id="engineSpeed" name="engineSpeed" maxlength="4" value="${po.engineSpeed}"/>rpm; 
    	</td>
    	<td>
    		其他：
    	</td>
    	<td colspan="3">
    		<textarea style="width: 100%" name="carStatusRemark">${po.carStatusRemark }</textarea>
    	</td>
    </tr>
     
    <tr >
    	<td>
    		路面情况:
    	</td>
    	<td colspan="7">
    		<input type="checkbox" name="wayStatus" class="wayStatus"value="平坦"/>平坦
    		<input type="checkbox" name="wayStatus" class="wayStatus"value="凹凸"/>凹凸
    		<input type="checkbox" name="wayStatus" class="wayStatus"value="砂石"/>砂石
    		<input type="checkbox" name="wayStatus" class="wayStatus"value="结冰"/>结冰
    		<input type="checkbox" name="wayStatus" class="wayStatus"value="积雪"/>积雪
    		<input type="checkbox" name="wayStatus" class="wayStatus"value="积水"/>积水
    		<input type="checkbox" name="wayStatus" class="wayStatus"value="上坡"/>上坡
    		<input type="checkbox" name="wayStatus" class="wayStatus"value="下坡"/>下坡
    		<input type="checkbox" name="wayStatus" class="wayStatus"value="其它"/>其它
    	</td>
    </tr>
    <tr >
    	<td>
    		天气情况:
    	</td>
    	<td colspan="4">
    		<input type="checkbox" name="weatherStatus" class="weatherStatus" value="晴天"/>晴天
    		<input type="checkbox" name="weatherStatus" class="weatherStatus"value="干燥"/>干燥
    		<input type="checkbox" name="weatherStatus" class="weatherStatus" value="雾天"/>雾天
    		<input type="checkbox" name="weatherStatus" class="weatherStatus"value="雨天"/>雨天
    		<input type="checkbox" name="weatherStatus" class="weatherStatus"value="雪天"/>雪天
    	</td>
    	<td colspan="3">
    		油液泄漏:
    		<input type="radio" name="oilLeak" class="oilLeak" value="无"/>无
    		<input type="radio" name="oilLeak" class="oilLeak" value="有"/>有,
    		具体为：<input type="text" name="oilLeaRemark" value="${po.oilLeaRemark}"/>
    	</td>
    </tr>
    <tr >
    	<td>
    	</td>
    	<td colspan="7">
    		<input type="checkbox" name="problemProperties" class="problemProperties" value="A类重大及危害性问题"/>A类重大及危害性问题
    		<input type="checkbox" name="problemProperties" class="problemProperties"value="B类主要质量问题"/>B类主要质量问题
    		<input type="checkbox" name="problemProperties" class="problemProperties"value="C类次要质量问题"/>C类次要质量问题
    		<input type="checkbox" name="problemProperties" class="problemProperties"value="D类持续改进问题"/>D类持续改进问题
    	</td>
    </tr>
    
    <tr >
    	<td>
    		故障属性:
    	</td>
    	<td colspan="4">
    		同车同故障:
    		<input type="radio" name="problemTheSame" class="problemTheSame" value="否"/>否
    		<input type="radio" name="problemTheSame" class="problemTheSame" value="是"/>是,
    		第<input type="text" name="problemTheSameRemark" id="problemTheSameRemark" value="${po.problemTheSameRemark}"/>次反馈；
    	</td>
    	<td colspan="3">
    		是否为批量问题：
    		<input type="radio" name="isAduit" class="isAduit" value="否"/>否
    		<input type="radio" name="isAduit" class="isAduit" value="是"/>是
    	</td>
    </tr>
    <tr>
    	<td>
    	</td>
    	<td colspan="7">
    		故障代码:
    		<input type="radio" name="problemCode" class="problemCode" value="无"/>无
    		<input type="radio" name="problemCode" class="problemCode" value="有"/>有,
    		具体为：<input type="text" name="problemCodeRemark"  value="${po.problemCodeRemark}"/>
    	</td>
    </tr>
     
    <tr>
    	<td>
    		主故障件代码
    	</td>
    	<td colspan="7">
    		<input type="text" name="firstProblemCode" value="${po.firstProblemCode}"/>
    		主故障件名称
    		<input type="text" name="firstProblemName" value="${po.firstProblemName}"/>
    		主故障件供应商代码
    		<input type="text" name="firstProblemSupplierCode" value="${po.firstProblemSupplierCode}"/>
    		目录分组
    		<input type="text" name="listGroup" value="${po.listGroup}"/>
    	</td>
    </tr>
     <tr >
     	<td>
     		零件更换状态
     	</td>
    	<td colspan="7">
    		<input type="checkbox" name="partChangeStatus" class="partChangeStatus" value="未涉及零件更换"/>未涉及零件更换
    		<input type="checkbox" name="partChangeStatus" class="partChangeStatus" value="零件已更换"/>零件已更换
    		<input type="checkbox" name="partChangeStatus" class="partChangeStatus" value="零件尚未更换"/>零件尚未更换
    	</td>
    </tr>
    
     <tr >
     	<td>
     		主题
    	</td>
    	<td colspan="7">
    		<textarea style="width: 100%" name="theme" >${po.theme }</textarea>
    	</td>
    </tr>
     <tr >
     	<td>
     		客户抱怨及需求
    	</td>
    	<td colspan="7">
    		<textarea style="width: 100%" name="customerComplainNeed">${po.customerComplainNeed }</textarea>
    	</td>
    </tr>
     <tr>
     	<td >
     		填报人具体情况描述及建议
    	</td>
    	<td colspan="7">
    		<textarea  style="width: 100%" name="reportNameAdvice">${po.reportNameAdvice }</textarea>
    	</td>
    </tr>
     <tr>
     	<td>
     		市场质量部回复
    	</td>
    	<td colspan="7">
    		<textarea style="width: 100%" name="applyNews">${po.applyNews }</textarea>
    	</td>
    </tr>
</table>
<br/>
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
  				<%if(fileList!=null){for(int i=0;i<fileList.size();i++) { %>
	 					 <script type="text/javascript">
	 	 					 addUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>');
	 	 				</script>
					<%} }%>
			</table> 
  		<!-- 添加附件 结束 -->
		</br>
		<table width=100% border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="12" align=left width="33%">&nbsp;</td>
             	<td height="12" align=center width="33%">
                	<input type="button" class="normal_btn" id="saveOrupdate"  style="width=8%" value="修改 " />
                	<input type="button" class="normal_btn" id="report"  style="width=8%" value="上报 "/>
					<input type="button" onClick="history.back();" class="normal_btn"  style="width=8%" value="返回"/>
    			</td>
            	<td height="12" align=center width="33%">
      			</td>
			</tr>
		</table>
</form>
<!--页面列表 begin -->
</body>
<script type="text/javascript" >
	$("#saveOrupdate").live("click",function(){
		var qualityId=$("#qualityId").val();
		$("#verifyStatus").val("95531001");
		var url="<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/saveOrupdateInfo.do?qualityId="+qualityId;
		sendSaveOrUpdate(url);
	});
	$("#report").live("click",function(){
		var qualityId=$("#qualityId").val();
		$("#verifyStatus").val("95531002");
		var url="<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/saveOrupdateInfoReport.do?qualityId="+qualityId;
		sendSaveOrUpdate(url);
	});
	function sendSaveOrUpdate(url){
		$("#fm").attr("action",url);
		$("#fm").submit();
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
		$("#carType").val(t.MODEL_ID);//车型ID
		$("#carClass").val(t.SERIES_ID);//车系ID
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
	});
	$("#vin").live("blur",function(){
		var vin=$("#vin").val();
		sendAjax('<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/queryDataByVin.json?vin='+vin,queryDateByVinBack2,'fm');
	});
	function queryDateByVinBack2(json){
		var t=json.queryDataByVin;
		$("#carUseType").val(t.GAME_NAME);//什么类型的车
		$("#carType").val(t.MODEL_ID);//车型ID
		$("#carClass").val(t.SERIES_ID);//车系ID
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
</script>
</html>
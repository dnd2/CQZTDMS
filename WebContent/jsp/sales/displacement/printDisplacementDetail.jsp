<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrGatherBalancePO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.po.TmDealerPO"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>经销商二手车置换打印信息</title>
<%  
    String contextPath = request.getContextPath();
    List  attachList   = (List)request.getAttribute("attachList");
%>
<script type="text/javascript">

	function myOnLoad(){
		loadcalendar();   //初始化时间控件
		var ctmType = ${vehMap.CTM_TYPE};
		var s_ctmType =<%=Constant.CUSTOMER_TYPE_02%>;
		if(ctmType == s_ctmType){
			document.getElementById("divcompany_table").style.display = "inline";
			document.getElementById("vehicleInfoid").style.display = "none";
		}else{
			document.getElementById("divcompany_table").style.display = "none";
		}
		document.getElementById("newZuofei").style.display= "none";
		document.getElementById("newCar").style.display="inline";
		
		setTypeShow() ;
	}
	
	function isSubmit(value) {
		if(document.getElementById("opinion").value.length == 0) {
			MyAlert("审核描述不允许为空！") ;
			
			return false ;
		}
		
		var title = "" ;
		if(value == 1) {
			title = "是否同意？" ;
			document.getElementById("chkStatus").value = <%=Constant.DisplancementCarrequ_cek_5%> ;
		} else if (value == 0) {
			title = "是否取消？" ;
			document.getElementById("chkStatus").value = <%=Constant.DisplancementCarrequ_cek_RETRUN%> ;
		}
		
		
		MyConfirm(title, Submit) ;
	}
	
	function Submit(){
		
		var url = "<%=contextPath%>/sales/displacement/DisplacementCarChk/toDealerCheck.json" ;
		
		makeFormCall(url, function() {history.back();}, "fm") ;
	}
	
	function setTypeShow() {
		if(${disMap.DISPLACEMENT_TYPE} == <%=Constant.DisplancementCarrequ_replace_2 %>) {
			document.getElementById("newZuofei").style.display= "inline";
		}
	}
	function goBack(){  
		window.location.href = "<%=contextPath%>/sales/displacement/DisplacementCarChk/displacementCarDealerSureInit.do";
	}
</script>
</head>
<body onload="myOnLoad();">
<div class="wbox">
<br/>
<table class="table_query" align=center>
	<tr class="tabletitle">
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />新车信息</th>
	</tr>
	<tr>
		<td width="15%" align=right>VIN：</td>
		<td width="35%" align=left>${vehMap.VIN }</td>
		<td align=right>发动机号：</td>
		<td width="35%" align=left>${vehMap.ENGINE_NO }</td>
	</tr>
	<tr>
		<td align=right>车系：</td>
		<td align=left>${vehMap.SERIES_NAME }</td>
		<td align=right>车型：</td>
		<td align=left>${vehMap.MODEL_NAME }</td>
	</tr>
	<tr>
		<td align=right>物料代码：</td>
		<td align=left>${vehMap.MATERIAL_CODE }</td>
		<td align=right>物料名称：</td>
		<td align=left>${vehMap.MATERIAL_NAME }</td>
	</tr>
	<tr>
		<td align=right>颜色：</td>
		<td align=left>${vehMap.COLOR }</td>
		<td align=right>品牌:</td>
		<td align=left>${vehMap.BRAND}</td>
	</tr>
	<tr>
		<td align=right>生产基地：</td>
		<td align=left>
			<script>document.write(getItemValue(${vehMap.PRODUCE_BASE}));</script>
		</td>
		<td align=right></td>
		<td align=left></td>
	</tr>
</table>
<div id="divcompany_table" style="">
<table class="table_query" align="center" id="company_table">
	<tr class="tabletitle">
		<th colspan="4" align="right"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />新车公司客户信息</th>
	</tr>

	<tr>
		<td width="15%" align="right">公司名称：</td>
		<td width="35%" align="left">${vehMap.COMPANY_NAME }</td>
		<td width="15%" align="right">公司简称：</td>
		<td width="35%" align="left">${vehicleInfo.COMPANY_S_NAME }</td>
	</tr>
	<tr>
		<td align="right">公司电话：</td>
		<td align="left">${vehMap.COMPANY_PHONE}</td>
		<td align="right">公司规模 ：</td>
		<td align="left">
		<script>document.write(getItemValue(${vehMap.LEVEL_ID}));</script>				
		</td>
	</tr>
	<tr>
		<td align="right">公司性质：</td>
		<td align="left">
		<script>document.write(getItemValue(${vehMap.KIND}));</script>		
		</td>
		<td align="right">目前车辆数：</td>
		<td align="left">${vehMap.VEHICLE_NUM}</td>
	</tr>
		<tr>
		<td align="right">客户来源：</td>
		<td align="left">
		<script>document.write(getItemValue(${vehMap.CTM_FORM}));</script>		
		</td>
	</tr>
	<tr>
		<td align="right">所在地：</td>
		<td colspan="3" align="left">
		省份：<script type='text/javascript'>
     			writeRegionName(${vehMap.PROVINCE });
 			</script>
	&nbsp;&nbsp;&nbsp;
		地级市：<script type='text/javascript'>
     			writeRegionName(${vehMap.CITY });
 			</script>&nbsp;&nbsp;&nbsp;
		区、县 :<script type='text/javascript'>
     			writeRegionName(${vehMap.TOWN });
 			</script>&nbsp;&nbsp;&nbsp;
		</td>
	</tr>
</table>
</div>
<div id="vehicleInfoid"  style="">
<table class="table_query" align="center" id="ctm_table_id">
	<tr>
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />新车个人客户信息</th>
	</tr>
	<tr>
		<td width="15%" align="right" id="tcmtd">客户姓名：</td>
		<td width="35%" align="left">
		${vehMap.CTM_NAME}
		</td>
		<td width="15%" align="right" id="sextd">性别：</td>
		<td align="left">
		<script>document.write(getItemValue(${vehMap.SEX}));</script>			
		</td>
	</tr>
</table>
<table class="table_query" align="center" id="ctm_table_id_2">
	<tr>
		<td width="15%" align="right">证件类别：</td>
		<td width="35%" align="left">
		<script>document.write(getItemValue(${vehMap.CARD_TYPE}));</script>				
		</td>
		<td width="15%" align="right">证件号码：</td>
		<td width="35%" align="left">${vehMap.CARD_NUM }</td>
	</tr>
	<tr>
		<td align="right">主要联系电话：</td>
		<td align="left">${vehMap.MAIN_PHONE}</td>
		<td align="right">其他联系电话：</td>
		<td align="left">${vehMap.OTHER_PHONE}</td>
	</tr>
	<tr>
		<td align="right">职业：</td>
		<td align="left">
		<script>document.write(getItemValue(${vehMap.PROFESSION}));</script>		
		</td>
		<td align="right">职务：</td>
		<td align="left">${vehMap.JOB}</td>
	</tr>
	<tr>
		<td align="right">所在地：</td>
		<td colspan="3" align="left">
		省份：<script type='text/javascript'>
     			writeRegionName(${vehMap.PROVINCE});
 			</script>
	&nbsp;&nbsp;&nbsp;
		地级市：<script type='text/javascript'>
     			writeRegionName(${vehMap.CITY});
 			</script>&nbsp;&nbsp;&nbsp;
		区、县 :<script type='text/javascript'>
     			writeRegionName(${vehMap.TOWN});
 			</script>&nbsp;&nbsp;&nbsp;
		</td>
	</tr>
	
	<tr>
		<td align="right">详细地址：</td>
		<td colspan="3" align="left">${vehMap.ADDRESS}</td>
	</tr>
</table>
</div>
<table class="table_query" align="center" id="displancement_table">
	<tr>
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />旧车信息</th>
	</tr>
	<tr>
		<td align="right" width="15%">二手车置换类型：</td>
		<td align="left">
		<script>document.write(getItemValue(${disMap.DISPLACEMENT_TYPE}));</script>	
		</td>
		<td align="right" width="15%"></td>
		<td align="left"></td>
	</tr>
	</table>
	<div id="newZuofei">
	<table class="table_query" align="center" id="newZuofei1">
	<tr>
		<td align="right" width="15%">报废证明编号：</td>
		<td align="left">${disMap.SCRAP_CERTIFY_NO }</td>
		<td align="right" width="15%">车辆报废时间：</td>
		<td align="left">${disMap.SCRAP_DATE }</td>
		<td></td>
		<td></td>
	</tr>
	</table>
	</div>
	<div id="newCar">
	<table class="table_query" align="center" id="newCar">
	<tr>
		<td align="right" width="15%">旧车品牌：</td>
		<td align="left" width="35%">${disMap.OLD_BRAND_NAME}</td>
		<td align="right" width="15%">旧车底盘号：</td>
		<td align="left" width="35%">${disMap.OLD_VIN}</td>
	</tr>
	<tr>
		<td align="right" width="15%">旧车型号：</td>
		<td align="left" width="35%">${disMap.OLD_MODEL_NAME}</td>
		<td align="right" width="15%">销售日期：</td>
		<td align="left" width="35%">${disMap.OLD_SLES_DATE}</td>
	</tr>
	<tr>
		<td align="right" width="15%">返利价格：</td>
		<td align="left" width="35%">${disMap.PRICE_AMOUNT}</td>
		<td align="right" width="15%">车主姓名：</td>
		<td align="left" width="35%">${disMap.HOST_NAME}</td>
	</tr>
	</table>
	</div>
	<table class="table_query" align="center">
	<tr>
		<td align="right" width="15%">备注：</td>
		<td align="left" width="85%" colspan="3">${disMap.REMARK}</td>
	</tr>
</table>
<br/>
<table width="100%" cellpadding="1" align="center" onmouseover="kpr.style.display='';" >   
	<tr>    
		<td width="100%" height="25" colspan="3">   
		<object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
			<div id="kpr" align="center">    
				<input class="ipt" type="button" value="打印" onclick="kpr.style.display='none';javascript:printit();"/>    
				<input class="ipt" type="button" value="打印页面设置" onclick="javascript:printsetup();" />    
				<input class="ipt" type="button" value="打印预览" onclick="kpr.style.display='none';javascript:printpreview();"/>    
			</div>
		</td>
	</tr>     
</table> 
<script language="javascript">    
  
	function printsetup()
	{       
		wb.execwb(8,1);    // 打印页面设置 
	}    
	
	function printpreview()
	{    
		wb.execwb(7,1);   // 打印页面预览       
	}  
	    
	function printit()    
	{    
		if(confirm('确定打印吗？'))
		{    
			wb.execwb(6,6)    
		}    
	}
</script> 
</div>
</body>
</html>
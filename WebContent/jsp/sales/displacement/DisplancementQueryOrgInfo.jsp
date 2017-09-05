<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="com.infodms.dms.po.TtDealerActualSalesPO"%>
<%@ page import="com.infodms.dms.po.TtCustomerPO"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>

<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
function returnSubmit(){
	history.back();
}
	function myOnLoad(){
		loadcalendar();   //初始化时间控件
		if(${displaceMentInfo.DISPLACEMENT_TYPE}==<%=Constant.DisplancementCarrequ_replace_1%>){
			document.getElementById("newZuofei").style.display= "none";
			document.getElementById("newCar").style.display="inline";
			}
		if(${displaceMentInfo.DISPLACEMENT_TYPE}==<%=Constant.DisplancementCarrequ_replace_2%>){
				document.getElementById("newZuofei").style.display="inline";
				document.getElementById("newCar").style.display= "none";
		}
		var ctmType = ${vehicleInfo.CTM_TYPE};
		var s_ctmType =<%=Constant.CUSTOMER_TYPE_02%>;
		if(ctmType == s_ctmType){
			document.getElementById("divcompany_table").style.display = "inline";
			document.getElementById("vehicleInfoid").style.display = "none";
		}else{
			document.getElementById("divcompany_table").style.display = "none";
			document.getElementById("vehicleInfoid").style.display = "inline";
		}
	}
	function Submit(checkres){
		if(document.getElementById("cekremark").value==''){
			MyAlert("审核信息不能为空！");
		}
		$('fm').action="<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementCekOrgCar.do?checkres="+checkres;
		$('fm').submit();
	}
</script>
<title>二手车置换申请</title>
</head>
<body onload="myOnLoad();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 整车销售 &gt; 客户信息管理 &gt; 二手车置换申请</div>
<form id="fm" name="fm" method="post">
<input type="hidden" value="<%=Constant.DisplancementCarrequ_replace_1%>" name="displacement_type"></input>
<input type="hidden" value="${DISPLACEMENT_ID}" name="DISPLACEMENT_ID" id="DISPLACEMENT_ID"></input>
<input type="hidden" value="${displaceMentInfo.VEHICLE_ID}" name="VEHICLE_ID" id="VEHICLE_ID"></input>
<table class="table_edit" align=center>
  <tr class="tabletitle">
    <th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />车辆资料</th>
  </tr>
  <tr>
    <td width="15%" align=right>VIN：</td>
    <td align=left>${vehicleInfo.VIN }</td>
    <td align=right>发动机号：</td>
    <td align=left>${vehicleInfo.ENGINE_NO }</td>
  </tr>
  <tr>
    <td align=right>车系：</td>
    <td align=left>${vehicleInfo.SERIES_NAME }</td>
    <td align=right>车型：</td>
    <td align=left>${vehicleInfo.MODEL_NAME }</td>
  </tr>
  <tr>
    <td align=right>物料代码：</td>
    <td align=left>${vehicleInfo.MATERIAL_CODE }</td>
    <td align=right>物料名称：</td>
    <td align=left>${vehicleInfo.MATERIAL_NAME }</td>
  </tr>
  <tr>
    <td align=right>颜色：</td>
    <td align=left>${vehicleInfo.COLOR }</td>
    <td align=right>品牌:</td>
    <td align=left>${vehicleInfo.BRAND}</td>
  </tr>
</table>
<div id="divcompany_table" style="">
<table class="table_edit" align="center" id="company_table">
  <tr class="tabletitle">
    <th colspan="4" align="right"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />公司客户信息</th>
  </tr>

  <tr>
    <td width="15%" align="right">公司名称：</td>
    <td width="35%" align="left">${vehicleInfo.COMPANY_NAME }</td>
    <td width="15%" align="right">公司简称：</td>
    <td width="35%" align="left">${vehicleInfo.COMPANY_S_NAME }</td>
  </tr>
  <tr>
    <td align="right">公司电话：</td>
    <td align="left">${vehicleInfo.COMPANY_PHONE}</td>
    <td align="right">公司规模 ：</td>
    <td align="left">
    <script>document.write(getItemValue(${vehicleInfo.LEVEL_ID}));</script>        
    </td>
  </tr>
  <tr>
    <td align="right">公司性质：</td>
    <td align="left">
    <script>document.write(getItemValue(${vehicleInfo.KIND}));</script>    
    </td>
    <td align="right">目前车辆数：</td>
    <td align="left">${vehicleInfo.VEHICLE_NUM}</td>
  </tr>
    <tr>
    <td align="right">客户来源：</td>
    <td align="left">
    <script>document.write(getItemValue(${vehicleInfo.CTM_FORM}));</script>    
    </td>
  </tr>
  <tr>
    <td align="right">所在地：</td>
    <td colspan="3" align="left">
    省份：<script type='text/javascript'>
           writeRegionName(${vehicleInfo.PROVINCE });
       </script>
  &nbsp;&nbsp;&nbsp;
    地级市：<script type='text/javascript'>
           writeRegionName(${vehicleInfo.CITY });
       </script>&nbsp;&nbsp;&nbsp;
    区、县 :<script type='text/javascript'>
           writeRegionName(${vehicleInfo.TOWN });
       </script>&nbsp;&nbsp;&nbsp;
    </td>
  </tr>
</table>
</div>
<div id="vehicleInfoid"  style="">
<table class="table_edit" align="center" id="ctm_table_id">
  <tr>
    <th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />个人客户信息</th>
  </tr>
  <tr>
    <td width="15%" align="right" id="tcmtd">客户姓名：</td>
    <td width="35%" align="left">
    ${vehicleInfo.CTM_NAME}
    </td>
    <td width="15%" align="right" id="sextd">性别：</td>
    <td align="left">
    <script>document.write(getItemValue(${vehicleInfo.SEX}));</script>      
    </td>
  </tr>
</table>
<table class="table_edit" align="center" id="ctm_table_id_2">
  <tr>
    <td width="15%" align="right">证件类别：</td>
    <td width="35%" align="left">
    <script>document.write(getItemValue(${vehicleInfo.CARD_TYPE}));</script>        
    </td>
    <td width="15%" align="right">证件号码：</td>
    <td width="35%" align="left">${vehicleInfo.CARD_NUM }</td>
  </tr>
  <tr>
    <td align="right">主要联系电话：</td>
    <td align="left">${vehicleInfo.MAIN_PHONE}</td>
    <td align="right">其他联系电话：</td>
    <td align="left">${vehicleInfo.OTHER_PHONE}</td>
  </tr>
  <tr>
    <td align="right">职业：</td>
    <td align="left">
    <script>document.write(getItemValue(${vehicleInfo.PROFESSION}));</script>    
    </td>
    <td align="right">职务：</td>
    <td align="left">${vehicleInfo.JOB}</td>
  </tr>
  <tr>
    <td align="right">所在地：</td>
    <td colspan="3" align="left">
    省份：<script type='text/javascript'>
           writeRegionName(${vehicleInfo.PROVINCE});
       </script>
  &nbsp;&nbsp;&nbsp;
    地级市：<script type='text/javascript'>
           writeRegionName(${vehicleInfo.CITY});
       </script>&nbsp;&nbsp;&nbsp;
    区、县 :<script type='text/javascript'>
           writeRegionName(${vehicleInfo.TOWN});
       </script>&nbsp;&nbsp;&nbsp;
    </td>
  </tr>
  
  <tr>
    <td align="right">详细地址：</td>
    <td colspan="3" align="left">${vehicleInfo.ADDRESS}</td>
  </tr>
</table>
</div>
<table class="table_edit" align="center">
	<tr>
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />二手车置换旧车信息</th>
	</tr>
	<tr>
		<td align="right" width="20%">旧车品牌：</td>
		<td align="left" width="30">${displaceMentInfo.OLD_BRAND_NAME}</td>
		<td align="right" width="20%">旧车底盘号：</td>
		<td align="left" width="30%">${displaceMentInfo.OLD_VIN}</td>
	</tr>
	<tr>
		<td align="right" width="20%">旧车型号名称：</td>
		<td align="left" width="30%">${displaceMentInfo.OLD_MODEL_NAME}</td>
		<td align="right" width="20%">旧车销售日期：</td>
		<td align="left" width="30%">${displaceMentInfo.OLD_SLES_DATE}</td>
	</tr>
	<tr>
		<td align="right" width="20%">反利价格：</td>
		<td align="left"  width="30%">${price}</td>
		<td align="right" width="20%"></td>
		<td align="left"  width="30%"></td>
	</tr>
	</table>
<table class="table_edit" align="center" id="displancement_table">
	<tr>
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />二手车置换新车信息</th>
	</tr>
	</table>
	<div id="newCar" style="">
	<table  class="table_edit" align="center" id="newCar">
	<tr>
		<td align="right" width="20%">生产基地：</td>
		<td align="left" width="30">${displaceMentInfo.NEW_AREA}</td>
		<td align="right" width="20%">新车底盘号：</td>
		<td align="left" width="30%">${displaceMentInfo.NEW_VIN}</td>
	</tr>
	<tr>
		<td align="right" width="20%">新车型号：</td>
		<td align="left" width="30">${displaceMentInfo.NEW_MODEL_NAME}</td>
		<td align="right" width="20%">购车日期：</td>
		<td align="left" width="30%">${displaceMentInfo.NEW_SALES_DATE}</td>
	</tr>
	</table>
	</div>
	<div id="newZuofei" style="">
	<table  class="table_edit" align="center" id="newZuofei">
	<tr>
		<td align="right" width="20%">报废证明编号：</td>
		<td align="left" width="30%">${displaceMentInfo.SCRAP_CERTIFY_NO}</td>
		<td align="right" width="20%">车辆报废时间：</td>
		<td align="left" width="30%">${displaceMentInfo.SCRAP_DATE}</td>
	</tr>
	</table>
	</div>
	<table  class="table_edit" align="center">
	<tr>
		<td align="right" width="20%">审核意见：</td>
		<td align="left" width="80%" colson="3"><textarea id="cekremark" name="cekremark" cols="80" rows="3" ></textarea></td>
	</tr>
</table>
<table  class="table_edit" align="center" >
	<tr class="cssTable">
		<td align="center" colspan="4">
			<input type="button" name="mySubmit"  class="normal_btn" value="同意" onclick="Submit(1);" />
			<input type="button" name="mySubmit"  class="normal_btn" value="驳回" onclick="Submit(2);" />
			<input type="button" name="mySubmit"  class="normal_btn" value="返回" onclick="returnSubmit();" />

		</td>
	</tr>
</table>

</form>
</div>
</body>
</html>
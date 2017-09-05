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
	function myOnLoad(){
		loadcalendar();   //初始化时间控件
		document.getElementById("divcompany_table").style.display = "none";
		document.getElementById("vehicleInfoid").style.display = "none";
	}
	function Submit(){
		if(document.getElementById("NEW_VIN").value==''){
			MyAlert('新车底盘号不能为空！');
		}else{
			$('fm').action="<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementInsertCar.do";
			$('fm').submit();
		}
		}
	function returnSubmit(){
			history.back();
		}
	function doCusChange(){
			
		}

	//获取字符串长度 中文以两个字符计算
	String.prototype.len=function(){   
	    return this.replace(/[^\x00-\xff]/g,"**").trim().length;   
	}   
	function mySubmitFocus(){
		var myLength=document.getElementById("NEW_VIN");
		if(document.getElementById("NEW_VIN").value==''){
				MyAlert('VIN底盘号输入不能为空！');
			}else if(myLength.value.len()!=17){
				MyAlert('请输入完整的17位VIN！');
			}else{
				getCarInfo();
			}
	}
	function getCarInfo(){
		makeNomalFormCall('<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementNewCarInfo.json',showRes,'fm');
	}
	function showRes(json){
		if(json.returnValue==1){
			MyAlert('车辆VIN不存在当前经销商实销数据中，请重新输入。');
		}else{
			document.getElementById("NEW_AREA1").innerHTML=json.newCarInfo.CODE_DESC;
			document.getElementById("NEW_SALES_DATE1").innerHTML=json.newCarInfo.SALESDATE;
			document.getElementById("NEW_MODEL_NAME1").innerHTML=json.newCarInfo.GROUP_CODE;
			document.getElementById("NEW_AREA").value=json.newCarInfo.CODE_DESC;
			document.getElementById("NEW_SALES_DATE").value=json.newCarInfo.SALESDATE;
			document.getElementById("NEW_MODEL_NAME").value=json.newCarInfo.GROUP_CODE;
			document.getElementById("price").innerHTML=json.price;
		}
		var ctmType = json.vehicleInfo.CTM_TYPE;
		var s_ctmType =<%=Constant.CUSTOMER_TYPE_02%>;
		if(ctmType == s_ctmType){
			document.getElementById("divcompany_table").style.display = "inline";
			document.getElementById("vehicleInfoid").style.display = "none";
			document.getElementById("COMPANY_NAME").innerHTML=json.vehicleInfo.COMPANY_NAME;
			document.getElementById("COMPANY_S_NAME").innerHTML=json.vehicleInfo.COMPANY_S_NAME;
			document.getElementById("LEVEL_ID").innerHTML=getItemValue(json.vehicleInfo.LEVEL_ID);
			document.getElementById("KIND").innerHTML=getItemValue(json.vehicleInfo.KIND);
			document.getElementById("VEHICLE_NUM").innerHTML=json.vehicleInfo.VEHICLE_NUM;
			document.getElementById("CTM_FORM").innerHTML=getItemValue(json.vehicleInfo.CTM_FORM);
			document.getElementById("PROVINCE").innerHTML=getRegionName(json.vehicleInfo.PROVINCE);
			document.getElementById("CITY").innerHTML=getRegionName(json.vehicleInfo.CITY);
			document.getElementById("TOWN").innerHTML=getRegionName(json.vehicleInfo.TOWN);
			
		}else{
			document.getElementById("divcompany_table").style.display = "none";
			document.getElementById("vehicleInfoid").style.display = "inline";
			document.getElementById("PROVINCE").innerHTML=getRegionName(json.vehicleInfo.PROVINCE);
		      document.getElementById("CITY").innerHTML=getRegionName(json.vehicleInfo.CITY);
		      document.getElementById("TOWN").innerHTML=getRegionName(json.vehicleInfo.TOWN);
					document.getElementById("CTM_NAME").innerHTML=json.vehicleInfo.CTM_NAME;
					document.getElementById("SEX").innerHTML=getItemValue(json.vehicleInfo.SEX);
					document.getElementById("CARD_TYPE").innerHTML=getItemValue(json.vehicleInfo.CARD_TYPE);
					document.getElementById("MAIN_PHONE").innerHTML=json.vehicleInfo.MAIN_PHONE;
					document.getElementById("OTHER_PHONE").innerHTML=json.vehicleInfo.OTHER_PHONE;
					document.getElementById("PROFESSION").innerHTML=getItemValue(json.vehicleInfo.PROFESSION);
				    document.getElementById("JOB").innerHTML=json.vehicleInfo.JOB;
					document.getElementById("ADDRESS").innerHTML=json.vehicleInfo.ADDRESS;
		}
	}
</script>
<title>二手车置换申请</title>
</head>
<body onload="myOnLoad();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 整车销售 &gt; 客户信息管理 &gt; 二手车置换申请</div>
<form id="fm" name="fm" method="post">
<input type="hidden" value="<%=Constant.DisplancementCarrequ_replace_1%>" name="displacement_type"></input>
<input type="hidden" id="NEW_AREA" name="NEW_AREA" value=""></input>
<input type="hidden" id="NEW_SALES_DATE" name="NEW_SALES_DATE" value=""></input>
<input type="hidden" id="NEW_MODEL_NAME" name="NEW_MODEL_NAME" value=""></input>
<div id="divcompany_table" style="">
<table class="table_edit" align="center" id="company_table">
	<tr class="tabletitle">
		<th colspan="4" align="right"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />公司客户信息</th>
	</tr>

	<tr>
		<td width="15%" align="right">公司名称：</td>
		<td width="35%" align="left"><label id="COMPANY_NAME"></label></td>
		<td width="15%" align="right">公司简称：</td>
		<td width="35%" align="left"><label id="COMPANY_S_NAME"></label></td>
	</tr>
	<tr>
		<td align="right">公司电话：</td>
		<td align="left"><label id="COMPANY_PHONE"></label></td>
		<td align="right">公司规模 ：</td>
		<td align="left"><label id="LEVEL_ID"></label>	
		</td>
	</tr>
	<tr>
		<td align="right">公司性质：</td>
		<td align="left"><label id="KIND"></label>
		</td>
		<td align="right">目前车辆数：</td>
		<td align="left"><label id="VEHICLE_NUM"></label></td>
	</tr>
		<tr>
		<td align="right">客户来源：</td>
		<td align="left"><label id="CTM_FORM"></label>
		</td>
	</tr>
	<tr>
		<td align="right">所在地：</td>
		<td colspan="3" align="left">
		省份：<label id="PROVINCE"></label>
	&nbsp;&nbsp;&nbsp;
		地级市：<label id="CITY"></label>&nbsp;&nbsp;&nbsp;
		区、县 :<label id="TOWN"></label> &nbsp;&nbsp;&nbsp;
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
		<label id="CTM_NAME"></label>
		</td>
		<td width="15%" align="right" id="sextd">性别：</td>
		<td align="left">
		<label id="SEX"></label>		
		</td>
	</tr>
</table>
<table class="table_edit" align="center" id="ctm_table_id_2">
	<tr>
		<td width="15%" align="right">证件类别：</td>
		<td width="35%" align="left">
		<label id="CARD_TYPE"></label>
		</td>
		<td width="15%" align="right">证件号码：</td>
		<td width="35%" align="left"><label id="CARD_NUM"></label>${vehicleInfo.CARD_NUM }</td>
	</tr>
	<tr>
		<td align="right">主要联系电话：</td>
		<td align="left"><label id="MAIN_PHONE"></label></td>
		<td align="right">其他联系电话：</td>
		<td align="left"><label id="OTHER_PHONE"></label></td>
	</tr>
	<tr>
		<td align="right">职业：</td>
		<td align="left"><label id="PROFESSION"></label>
		</td>
		<td align="right">职务：</td>
		<td align="left"><label id="JOB"></label></td>
	</tr>
	<tr>
		<td align="right">所在地：</td>
		<td colspan="3" align="left">
		省份：<label id="PROVINCE"></label>&nbsp;&nbsp;&nbsp;
		地级市：<label id="CITY"></label>&nbsp;&nbsp;&nbsp;
		区、县 :<label id="TOWN"></label> &nbsp;&nbsp;&nbsp;
		</td>
	</tr>
	<tr>
		<td align="right">详细地址：</td>
		<td colspan="3" align="left"><label id="ADDRESS"></label></td>
	</tr>
</table>
</div>	
<table class="table_edit" align="center">
	<tr>
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />二手车置换旧车信息</th>
	</tr>
	<tr>
		<td align="right" width="20%">旧车品牌：</td>
		<td align="left" width="30"><input type="text" class="middle_txt" id="OLD_BRAND_NAME" name="OLD_BRAND_NAME"  value=""  /></td>
		<td align="right" width="20%">旧车底盘号：</td>
		<td align="left" width="30%"><input type="text" class="middle_txt" id="OLD_VIN" name="OLD_VIN"  value=""  /></td>
	</tr>
	<tr>
		<td align="right" width="20%">旧车型号名称：</td>
		<td align="left" width="30"><input type="text" class="middle_txt" id="OLD_MODEL_NAME" name="OLD_MODEL_NAME"  value=""  /></td>
		<td align="right" width="20%">旧车销售日期：</td>
		<td align="left" width="30%"><input name="OLD_SLES_DATE" id="OLD_SLES_DATE" type="text" class="short_txt" value="" datatype="1,is_date,10" hasbtn="true" callFunction="showcalendar(event, 'OLD_SLES_DATE', false);" /></td>
	</tr>
	</table>
<table class="table_edit" align="center" id="displancement_table">
	<tr>
		<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />二手车置换新车信息</th>
	</tr>
	</table>
	<table  class="table_edit" align="center" id="newCar">
	<tr>
		<td align="right" width="20%">生产基地：</td>
		<td align="left" width="30"><label id="NEW_AREA1">&nbsp;</label></td>
		<td align="right" width="20%">新车底盘号：</td>
		<td align="left" width="30%"><input type="text" class="middle_txt" id="NEW_VIN"  onblur ="mySubmitFocus();"  name="NEW_VIN"  value=""  /></td>
	</tr>
	<tr>
		<td align="right" width="20%">新车型号：</td>
		<td align="left" width="30"><label id="NEW_MODEL_NAME1">&nbsp;</label></td>
		<td align="right" width="20%">购车日期：</td>
		<td align="left" width="30%"><label id="NEW_SALES_DATE1">&nbsp;</label></td>
	</tr>
	<tr>
		<td align="right" width="20%">反利价格：</td>
		<td align="left" width="30"><label id="price">&nbsp;</label></td>
		<td align="right" width="20%"></td>
		<td align="left" width="30%"></td>
	</tr>
	</table>
	<table  class="table_edit" align="center">
	<tr>
		<td align="right" width="20%">备注：</td>
		<td align="left" width="80%" colson="3"><textarea id="remark" name="reamrk" cols="80" rows="3" ></textarea></td>
	</tr>
</table>
<table class="table_info" border="0" id="file">
	    <tr>
	        <th>附件列表：<input type="hidden" id="fjids" name="fjids"/>
				<span>
					<input type="button" class="cssbutton"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
				</span>
			</th>
		</tr>
		<tr>
			<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
	</table>
<table  class="table_edit" align="center" >
	<tr class="cssTable">
		<td align="center" colspan="4">
			<input type="button" name="mySubmit"  class="normal_btn" value="提交" onclick="Submit();" />
			<input type="button" name="mySubmit"  class="normal_btn" value="返回" onclick="returnSubmit();" />

		</td>
	</tr>
</table>

</form>
</div>
</body>
</html>
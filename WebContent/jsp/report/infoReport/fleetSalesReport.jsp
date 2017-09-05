<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>经销商库龄分析报表（分区域）</title>
<script type="text/javascript">
function toClearOrgs(){
	document.getElementById("orgCode").value="";
	document.getElementById("orgId").value="";
}
function checkType(){
document.getElementById("orgbuclearbutton").disabled=true;
document.getElementById("orgbu").disabled=true;
document.getElementById("button4").disabled=false;
document.getElementById("button4clearbutton").disabled=false;
document.getElementById("orgCode").value="";
document.getElementById("orgId").value="";
}
function checkOrgType(){
document.getElementById("orgbuclearbutton").disabled=false;
document.getElementById("orgbu").disabled=false;
document.getElementById("button4").disabled=true;
document.getElementById("button4clearbutton").disabled=true;
document.getElementById("dealerCode").value="";
document.getElementById("dealerId").value="";
}
function doInit(){
	loadcalendar();
	if(document.getElementById("orgCode").value ==""&&document.getElementById("dealerCode").value ==""){
	document.getElementById("button4").disabled=true;
	document.getElementById("button4clearbutton").disabled=true;
		document.getElementById("orgbuclearbutton").disabled=false;
		document.getElementById("orgbu").disabled=false;
	}else if(document.getElementById("orgCode").value !=""){
		document.getElementById("button4").disabled=true;
		document.getElementById("button4clearbutton").disabled=true;
			document.getElementById("orgbuclearbutton").disabled=false;
			document.getElementById("orgbu").disabled=false;
		}else{
		document.getElementById("button4").disabled=false;
		document.getElementById("button4clearbutton").disabled=false;
			document.getElementById("orgbuclearbutton").disabled=true;
			document.getElementById("orgbu").disabled=true;
	}
}


function submitFRM(){
		document.FRM.action='<%=request.getContextPath()%>/server2';
		document.FRM.submit();
}
function txtClr(valueId) {
	document.getElementById(valueId).value = '' ;
}
function toClear(){
	document.getElementById("campaignModel").value="";
	document.getElementById("modelId").value="";
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售报表> 整车销售报表 > 大客户实销汇总报表</div>
<form method="POST" name="FRM" id="FRM">
<input type="hidden" name="file" value="commonXml/FleetSalesAll.xml"></input>
<table class="table_edit">
  <tr>
	    <td  align="right" nowrap ></td>
	    <td  align="right" nowrap >交车起止时间：</td>
	    <td class="table_list_th">
	    <input type="hidden" name="aaa" size="15" id="aaa" value="" />
       		<input name="insertBeginTime" id="t5"  readonly="readonly" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t5,t6" hasbtn="true" callFunction="showcalendar(event, 't5', false);">
       		&nbsp;至&nbsp;
       		<input name="insertEndTime" id="t6"   readonly="readonly" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t5,t6" hasbtn="true" callFunction="showcalendar(event, 't6', false);">
        </td>
	    <td></td>
    </tr>
  <tr>
 		<td  align="right" nowrap ></td>
		<td align="right">选择车系：</td>
			<td>
			 <input type="hidden" name="aaa" size="15" id="aaa" value="" />
				<input type="text"  name="groupCode" size="15"  class="short_txt" readonly="readonly" value="" />
				<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','','false','2');" value="..." />
				<input class="normal_btn" type="button" value="清除" onclick="txtClr('groupCode');"/>
			</td>
			<td>&nbsp;</td>
	</tr>
	 <tr>
		<td align="right" nowrap="nowrap">&nbsp;</td>
		<td align="right">选择车型：</td>
			<td class="table_list_th"><input type="hidden" name="area_id" id="area_id" value="" />
				<input type="hidden" name="modelId" id="modelId" onChange="myDate();"  value=""  size="15"/>
				<input type="text" name="campaignModel" class="middle_txt" id="campaignModel" readonly="readonly" size="15"/>
				<input name="button3" type="button" class="mini_btn" onclick="showMaterialCarType_market('groupCode','modelId','true','')" value="..." />
				<input type="button" name="clearbutton" id="clearbutton" class="cssbutton" value="清除" onClick="toClear();"/>
			</td>
			<td>&nbsp;</td>
	</tr>
    	<tr>
		<td  align="right"></td>
		<td align="right"><input type="radio" id="odtype" name="odtype" value="0" onclick="checkType()" checked="checked"></input>选择经销商：</td>
			<td class="table_list_th"><input type="hidden" name="dealerId" size="15" id="dealerId" value="" />
				<input type="text" class="middle_txt" readonly="readonly" name="dealerCode" size="15" id="dealerCode" value="" />
				<input name="button4" type="button"  class="mini_btn" onclick="showOrgDealer('dealerCode','dealerId','true');" value="..." />
				<input type="button" name="button4clearbutton" id="button4clearbutton" class="cssbutton" value="清除" onClick="txtClr('dealerCode');"/>
			</td>
		<td></td>
	</tr>
	<tr>
		<td  align="right"></td>
		<td align="right"><input type="radio" id="odtype" name="odtype" value="0" onclick="checkOrgType()" checked="checked"></input>选择区域：</td>
		<td class="table_list_th">
		<input type="hidden"  name="orgId" size="15" value=""  id="orgId" class="middle_txt" datatype="1,is_noquotation,75" />
		<input type="text"  readonly="readonly"  name="orgCode" size="15" value=""  id="orgCode" class="middle_txt" datatype="1,is_noquotation,75" />
		<input name="orgbu"  id="orgbu" type="button" class="mark_btn" onclick="showOrg('orgCode','orgId','false')" value="..." />
		<input type="button" name="orgbuclearbutton" id="orgbuclearbutton" class="cssbutton" value="清除" onClick="toClearOrgs();"/>
		</td>
		<td align="left" nowrap="nowrap">
		</td>
	</tr>
<tr><td  align="right"></td>
<td align="right">客户类型：</td>
			<td align="left">
			 <input type="hidden" name="aaa" size="15" id="aaa" value="" />
				<label>
					<script type="text/javascript">
						genSelBoxExp("fleetType",<%=Constant.FLEET_TYPE%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
			<td>
			<input name="button2" type=button class="cssbutton" onClick="submitFRM();" value="查询">	
</td>
</tr>
</table>
</form>
</body>
</html>
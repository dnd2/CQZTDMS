<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>集团客户支持</title>
<% String contextPath = request.getContextPath();  %>
<script type="text/javascript">
function doInit(){
	loadcalendar();
}
function submitFRM(){
		document.FRM.action='<%=request.getContextPath()%>/report/FleetActTjReport/getFleetActTj.do';
		document.FRM.target="_blank";
		document.FRM.submit();
}

function downloadFRM(){
	document.FRM.action='<%=request.getContextPath()%>/report/FleetActTjReport/Download.json';
	document.FRM.target="_self";
	document.FRM.submit();
}

function toClear(){
	document.getElementById("companyName").value="";
	document.getElementById("companyId").value="";
}
function togroupClear(){
	
	document.getElementById("groupCode").value="";
}

//经销商公司弹出
	function showCompany(){
		OpenHtmlWindow('<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetContractsMaintain/queryCompany.do',800,450);
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 集团客户管理 &gt; 集团客户支持&gt; 集团客户实销信息审核查询</div>
<form method="POST" name="FRM" id="FRM">
<input type="hidden" name="file" value="commonXml/FleetActTj.xml"></input>
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td align="right">客户名称：</td>
			<td align="left">
				<input name="fleetName" type="text" class="middle_txt" id="fleetName">
			</td>
			<td align="right">客户类型：</td>
			<td align="left">
				<label>
					<script type="text/javascript">
						genSelBoxExp("fleetType",<%=Constant.FLEET_TYPE%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">经销商公司：</td>
			<td align="left">
			<input id="companyName" name="companyName" class="middle_txt" type="text" readonly="readonly" />
			<input id="companyId" name="companyId" type="hidden"/>				
			<input class="mini_btn" type="button" value="..." onclick="showCompany();"/>
			<input class="cssbutton" type="button" value="清除" onclick="toClear();"/>
			</td>
			<td align="right">物料组：</td>
			<td align="left">
				<input type="text" name="groupCode" id="groupCode" class="middle_txt" />
				<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','','true','4');" value="..." />
				<input class="cssbutton" type="button" value="清除" onclick="togroupClear();"/>
			</td>
			<td align="left"></td>
		</tr>
		<tr>
			<td align="right">合同编号：</td>
			<td align="left">
				<input type="text"  name="contractNo" class="middle_txt" id="contractNo"/>
			</td>
			<td align="right">底盘号（VIN）：</td>
			<td align="left">
				<textarea id="vin" name="vin" cols="18" rows="3" ></textarea>
	  		</td>
			<td align="left"></td>
		</tr>
		<tr>
			<td align="right">上报日期：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="t1" name="startDate" datatype="1,is_date,10" group="t1,t2"  value="${date2}" />
				<input class="time_ico" type="button" value=" " onClick="showcalendar(event, 't1', false);" value="&nbsp;" />&nbsp;至&nbsp;
				<input class="short_txt"  type="text" id="t2" name="endDate" datatype="1,is_date,10" group="t1,t2" value="${date}" />
				<input class="time_ico" type="button" value=" " onClick="showcalendar(event, 't2', false);" value="&nbsp;" />
			</td>
			<td align="right">审核状态：</td>
			<td align="left">
				<label>
					<script type="text/javascript">
						genSelBoxExp("checkStatus",<%=Constant.Fleet_SALES_CHECK_STATUS%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
	  		</td>
		</tr>
		<tr>
			<td align="right">&nbsp;</td>
			<td align="left">&nbsp;</td>
			<td align="right">&nbsp;</td>
			<td align="left">&nbsp;</td>
			<td width="15%" align="left">
			<input name="button2" type="button" id="queryBtn" class="cssbutton" onClick="submitFRM();" value="查询">&nbsp;
			<input name="button3" type="button" id="queryBtn" class="cssbutton" onClick="downloadFRM();" value="下载">
			</td>
		</tr>
	</table>
	</form>
</body>
</html>
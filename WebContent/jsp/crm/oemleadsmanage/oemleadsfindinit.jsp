<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
    int yes = Constant.IF_TYPE_YES;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/regionUtils.js"></script>

<script type="text/javascript">
	function doInit(){
		loadcalendar();   //初始化时间控件
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
	
</script>

<title>销售线索查询</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="__extQuery__(1);addRegionInit();">
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>潜客管理>总部线索管理>线索查询
		</div>
		<form id="fm" name="fm" method="post">
			<input type="hidden" name="curPage" id="curPage" value="1" />
			<input type="hidden" name="leadsCode2" id="leadsCode2" value="" />
			<input type="hidden" name="leadsGroup2" id="leadsGroup2" value="" />   
			<input type="hidden" id="dlrId" name="dlrId" value="" />

			<table class="table_query" width="95%" align="center">
				<c:if test="${returnValue==2}">
					<tr>
						<td colspan="6"></td>
					</tr>
				</c:if>
				<tr>
					<td align="right" width="10%">
						客户姓名：
					</td>
					<td>
						<input id="customer_name" name="customer_name" type="text" class="middle_txt" 
								datatype="1,is_textarea,30" size="20"	maxlength="60" />
					</td>
					<td align="right" width="7%">
						联系电话：
					</td>
					<td width="15%">
						<input id="telephone" name="telephone" type="text" class="middle_txt" 
								datatype="1,is_textarea,30"	size="20" maxlength="60" />
					</td>
					<td width="8%" align="right">
						导入时间从：
					</td>
					<td width="22%">
						<div align="left">
							      <input name="startDate" id="startDate" value="" type="text" class="short_txt" datatype="1,is_date,10"
									group="startDate,endDate" hasbtn="true" callFunction="showcalendar(event, 'startDate', false);" />
					&nbsp;到&nbsp; <input name="endDate" id="endDate" value="" type="text" class="short_txt" datatype="1,is_date,10"
										group="startDate,endDate" hasbtn="true"  callFunction="showcalendar(event, 'endDate', false);" />
						</div>
					</td>
				</tr>

				<tr>
					<td align="right" width="10%">分派状态：</td>
					<td>
						<input type="hidden" id="allot_status" name="allot_status" value=""/>
		      			<div id="ddtopmenubar29" class="mattblackmenu">
							<ul> 
								<li>
									<a style="width:130px;" rel="ddsubmenu29" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6029', loadAllotStatus);" deftitle="--请选择--">
									--请选择--</a>
									<ul id="ddsubmenu29" class="ddsubmenustyle"></ul>
								</li>
							</ul>
						</div>
					</td>
					<td align="right" width="7%">
						线索来源：
					</td>
					<td>
						<input type="hidden" id="leads_origin" name="leads_origin" value=""/>
		      			<div id="ddtopmenubar28" class="mattblackmenu">
							<ul> 
								<li>
									<a style="width:130px;" rel="ddsubmenu28" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6015', loadLeadsOrigin);" deftitle="--请选择--">
									--请选择--</a>
									<ul id="ddsubmenu28" class="ddsubmenustyle"></ul>
								</li>
							</ul>
						</div>
					</td>
					<td align="right" width="6%">&nbsp;省份：</td>
					<td align="left" width="32%">
							<select class="min_sel" id="dPro" name="dPro" onchange="_regionCity(this,'dCity')"></select>
						 城市： <select class="min_sel" id="dCity" name="dCity" onchange="_regionCity(this,'dArea')"></select>
						  区县： <select class="min_sel" id="dArea" name="dArea"></select>
					</td>
					<td align="right" width="10%"></td>
					<td>
					</td>
				</tr>
				<tr>
					 <td align="right" width="7%">经销商：</td>
		      <td  width="20%">
				<input type="text" class="short_txt"  style="width:130px;" name="dealerCode" id="dealerCode" size="10" value=""/>
				<input type="hidden" name="dealerId" size="15" id="dealerId" value="" />
				<input name="button2" type="button" class="mini_btn" onclick="showOrgDealerBase('dealerCode','dealerId','true', '${orgId}')" value="..." />
				<input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn"/>
			</td> 
			<td width="8%" align="right">是否超时：</td>
					<td>
					<input type="hidden" id="time_out" name="time_out" value=""/>
			      		<div id="ddtopmenubar27" class="mattblackmenu">
							<ul> 
								<li>
									<a style="width:130px;" rel="ddsubmenu27" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1004', loadTimeOut);" deftitle="--请选择--">
									--请选择--</a>
									<ul id="ddsubmenu27" class="ddsubmenustyle"></ul>
								</li>
							</ul>
						</div>
			</td>
			<td align="right">车系：</td>
			<td align="left">
                	<input id="seriesId" name="seriesId" type="text"  size="13"  readonly="readonly" value=""/> 
					<input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" onclick="toSeriesList('seriesId','','true', '${orgId}')" value="..." />
					<input class="normal_btn" type="button" value="清空" onclick="clrTxt('seriesId');"/>
			</td>
				</tr>
				<tr>
					<td colspan="6" align="center"><input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);"
						value="查询" />
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>

	</div>
	<script type="text/javascript"> 
	
	var myPage;
	var url = "<%=contextPath%>/crm/oemleadsmanage/OemLeadsManage/leadsFindQuery.json";
				
	var title = null;
	var controlCols = "<input type=\"checkbox\" name=\"conAll\" onclick=\"selectAll(this,'leadsCode')\"/>全选&nbsp;&nbsp;"
		+ "<input type=\"button\" value=\"重新分派\" class=\"normal_btn\" onclick=\"batchAuditingConfirm();\"/>";
	var columns = [
				{header: "序号", renderer:getIndex, align:'center'},
				{header: controlCols,align:'center',renderer:createCheckbox},
				{header: "线索编码", dataIndex: 'LEADS_CODE', align:'center'},
				{header: "线索来源", dataIndex: 'LEADS_ORIGIN', align:'center'},
				{header: "收集时间", dataIndex: 'COLLECT_DATE', align:'center'},
				{header: "客户姓名", dataIndex: 'CUSTOMER_NAME', align:'center'},
				{header: "联系电话", dataIndex: 'TELEPHONE', align:'center'},
				{header: "意向车型", dataIndex: 'SERIES_NAME', align:'center'},
				{header: "分派经销商", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "分派时间", dataIndex: 'ALLOT_DEALER_DATE', align:'center'},
				{header: "备注", dataIndex: 'REMARK', align:'center'},
				{header: "分派状态", dataIndex: 'LEADS_STATUS', align:'center'},
				{header: "操作",sortable: false,dataIndex: 'LEADS_CODE',renderer:myLink}
		      ];
	function myLink(value,meta,record){
		//未分派经销商的销售线索无法重新分派
		if(record.data.DEALER_ID == null) {
			return String.format("");
		} else {
			return String.format("<a href=\"#\" id=\"LEADS_CODE\" name=\"LEADS_CODE\" onclick='checkDetailUrl(\""+record.data.LEADS_CODE+"\")'>[重分派]</a>");
		}
	}   
	function checkDetailUrl(leadsCode){
		document.getElementById("leadsCode2").value = leadsCode;
		showOrgDealerByLeads('dealerCode','','false',leadsCode,'','oemre');
		//window.location.href="<%=contextPath%>/crm/oemleadsmanage/OemLeadsManage/oemLeadsReAllotByOemInit.do?leadsCode="+leadsCode;
	}  
	//生成批量审核使用的选择框
	function createCheckbox(value,meta,record){
  	  		//var claimStatus = record.data.STATUS;
  	  		//var authcodeval = record.data.AUTH_CODE;
  	  		//未分派经销商的销售线索无法重新分派
			if(record.data.DEALER_ID == null) {
				resultStr = String.format("");
			} else {
				resultStr = String.format("<input type=\"checkbox\" name=\"leadsCode\" value=\""+record.data.LEADS_CODE+"\"/>");
			}
		return resultStr;
	}
	function clrTxt(txtId){
    	document.getElementById(txtId).value="";
    }

	 function toSeriesList(inputCode ,isMulti ,orgId)
	    {
	    	if(!inputCode){ inputCode = null;}
	    	if(!isMulti){ isMulti = null;}
	    	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	    	OpenHtmlWindow(g_webAppName+'/jsp/crm/report/showSeriesCode.jsp?INPUTCODE='+inputCode+"&ISMULTI="+isMulti+"&ORGID="+orgId,730,390);
	    }
	 //批量审批
	function batchAuditingConfirm(){
		var selectArray = document.getElementsByName('leadsCode');
		if(isSelectCheckBox(selectArray)){
			//MyConfirm("是否批量分派?",batchAuditing,[selectArray]);
			//数组转为字符串
			var leadsGroup = '';
			for(var i=0;i<selectArray.length;i++) {
				if(selectArray[i].checked) {
					leadsGroup = leadsGroup + selectArray[i].value + ",";
				}
			}
			leadsGroup=leadsGroup.substring(0,leadsGroup.length-1);
			document.getElementById("leadsGroup2").value = leadsGroup;
			showOrgDealerByLeads('dealerCode','','false','',leadsGroup,'oemre');
		}else{
			MyAlert("请选择要重新分派的销售线索！");
		}
	}
	function batchAuditing(selectArray)
	{
		//数组转为字符串
		var leadsGroup = '';
		for(var i=0;i<selectArray.length;i++) {
			if(selectArray[i].checked) {
				leadsGroup = leadsGroup + selectArray[i].value + ",";
			}
		}
		leadsGroup=leadsGroup.substring(0,leadsGroup.length-1);
		window.location.href="<%=contextPath%>/crm/oemleadsmanage/OemLeadsManage/oemLeadsReAllotByOemInit.do?leadsGroup="+leadsGroup; 
	}
	//检测是否选择了checkBox
	function isSelectCheckBox(cbArray){
		if(cbArray!=null && cbArray.length>0){
			for(var i=0;i<cbArray.length;i++){
				if(cbArray[i].checked)
					return true;
			}
		}else{
			return false;
		}
		return false;
	}
	//回调函数
	function returnFunction(code){
		var leadsCode = document.getElementById("leadsCode2").value;
		var leadsGroup = document.getElementById("leadsGroup2").value;
		if(leadsCode==null||leadsCode==''){
			$('fm').method = "post" ;
			$('fm').action= "<%=contextPath%>/crm/oemleadsmanage/OemLeadsManage/oemLeadsReAllotByOem.do?leadsGroup="+leadsGroup+"&dealerCode="+code;
			$('fm').submit();
		} else {
			$('fm').method = "post" ;
			$('fm').action= "<%=contextPath%>/crm/oemleadsmanage/OemLeadsManage/oemLeadsReAllotByOem.do?leadsCode="+leadsCode+"&dealerCode="+code;
			$('fm').submit();
		}
		MyAlert("执行完成！");
	}
	_setDate_("reportStartDate", "reportEndDate", "1", "0") ;
</SCRIPT>
	</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar29", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar28", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar27", "topbar")</script>
</body>
</html>
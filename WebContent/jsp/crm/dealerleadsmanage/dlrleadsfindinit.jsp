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
<body onunload='javascript:destoryPrototype();' onload="loadcalendar();__extQuery__(1)">
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>潜客管理>经销商线索管理>线索查询
		</div>
		<form id="fm" name="fm" method="post">
			<input type="hidden" name="curPage" id="curPage" value="1" /> <input
				type="hidden" id="dlrId" name="dlrId" value="" />
			<input type="hidden" name="leadsCode" id="leadsCode" value="" />
			<input type="hidden" name="leadsCode2" id="leadsCode2" value="" />
			<input type="hidden" name="leadsGroup2" id="leadsGroup2" value="" />
			<input type="hidden" name="leadsCodeGroup2" id="leadsCodeGroup2" value="" />
			<table class="table_query" width="95%" align="center">
				<c:if test="${returnValue==2}">
					<tr>
						<td colspan="6"></td>
					</tr>
				</c:if>
				<tr>
					<td align="right" width="10%">客户姓名：</td>
					<td><input id="customer_name" name="customer_name" type="text"
						class="middle_txt" datatype="1,is_textarea,30" size="20"
						maxlength="60" /></td>
					<td align="right" width="7%">联系电话：</td>
					<td width="15%"><input id="telephone" name="telephone"
						type="text" class="middle_txt" datatype="1,is_textarea,30"
						size="20" maxlength="60" /></td>
					<td width="8%" align="right">创建时间从：</td>
					<td width="22%">
						<div align="left">
							<input name="startDate" id="startDate" value="" type="text"
								class="short_txt" datatype="1,is_date,10"
								group="startDate,endDate" style="width: 70px;"/>
								<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value="&nbsp;" />
							  到<input name="endDate" id="endDate" value=""
								type="text" class="short_txt" datatype="1,is_date,10"
								group="startDate,endDate" style="width: 70px;"/>
								<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
						</div></td>
				</tr>

				<tr>
					<td align="right" width="10%">分派状态：</td>
					<td><input type="hidden" id="allot_status" name="allot_status" value=""/>
		      		<div id="ddtopmenubar29" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:130px;" rel="ddsubmenu29" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6029', loadAllotStatus);" deftitle="--请选择--">
								--请选择--</a>
								<ul id="ddsubmenu29" class="ddsubmenustyle"></ul>
							</li>
						</ul></td>
					<td align="right" width="7%">线索来源：</td>
					<td><input type="hidden" id="leads_origin" name="leads_origin" value=""/>
		      		<div id="ddtopmenubar28" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:130px;" rel="ddsubmenu28" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6015', loadLeadsOrigin);" deftitle="--请选择--">
								--请选择--</a>
								<ul id="ddsubmenu28" class="ddsubmenustyle"></ul>
							</li>
						</ul></td>
					<td width="8%" align="right">分派时间从：</td>
					<td width="22%">
						<div align="left">
							<input name="allotStartDate" id="allotStartDate" value="" type="text"
								class="short_txt" datatype="1,is_date,10"
								group="allotStartDate,allotEndDate"  style="width: 70px;"
								callFunction="showcalendar(event, 'allotStartDate', false);" />
								<input class="time_ico" type="button" onClick="showcalendar(event, 'allotStartDate', false);" value="&nbsp;" />
							到<input name="allotEndDate" id="allotEndDate" value=""
								type="text" class="short_txt" datatype="1,is_date,10"
								group="allotStartDate,allotEndDate"  style="width: 70px;"/>
								<input class="time_ico" type="button" onClick="showcalendar(event, 'allotEndDate', false);" value="&nbsp;" />
						</div></td>
				</tr>
				<tr>
				<td width="10%" align="right">是否超时：</td>
					<td><input type="hidden" id="time_out" name="time_out" value=""/>
		      		<div id="ddtopmenubar27" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:130px;" rel="ddsubmenu27" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=1004', loadTimeOut);" deftitle="--请选择--">
								--请选择--</a>
								<ul id="ddsubmenu27" class="ddsubmenustyle"></ul>
							</li>
						</ul></td>
				<td width="18%" align="right">线索状态：</td>
				<td>
				<input type="hidden" id="leads_status" name="leads_status" value=""/>
		      		<div id="ddtopmenubar26" class="mattblackmenu">
						<ul> 
							<li>
								<a style="width:130px;" rel="ddsubmenu26" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6016', loadLeadsStatus);" deftitle="--请选择--">
								--请选择--</a>
								<ul id="ddsubmenu26" class="ddsubmenustyle"></ul>
							</li>
						</ul>
				</td>
				
				<td align="right" width="12%">集客方式：</td>
					<td><input type="hidden" id="collect_fashion" name="collect_fashion" value=""/>
		      		<div id="ddtopmenubar25" class="mattblackmenu">
						<ul> 
							<li>
								<a  style="width:170px;" rel="ddsubmenu25" href="###" isclick="true" onclick="stree.loadtree(this, '<%= request.getContextPath() %>/crm/data/DataManage/initData.json?codeId=6002', loadCollectFashion);" deftitle="--请选择--">
								--请选择--</a>
								<ul id="ddsubmenu25" class="ddsubmenustyle"></ul>
							</li>
						</ul>
						</div>
					</td>
					
				<!-- 顾问登陆不显示此查询条件 -->
				<c:if test="${adviserLogon=='yes' }">
					<td width="18%" align="right"></td>
					 <td>
				     </td>
				</c:if>
				<c:if test="${adviserLogon=='no' }">
					<td width="18%" align="right">顾问：</td>
					 <td> 
				      <select id="adviser" name="adviser">
				      			<option value="">--请选择--</option>
				      		<c:forEach var="item" items="${adviserList }" varStatus="status">
				      			<option id="${item.USER_ID }" value="${item.USER_ID }">${item.NAME }</option>
				      		</c:forEach>
				      	</select>
				      </td>
				</c:if>
				</tr>
				<c:if test="${managerLogon=='yes' }">
				<tr>
					<td align="right" width="10%">分组：</td>
					<td>
						<select id="groupId" name="groupId">
				      			<option value="">--请选择--</option>
				      		<c:forEach var="item" items="${groupList }" varStatus="status">
				      			<option id="${item.GROUP_ID }" value="${item.GROUP_ID }">${item.GROUP_NAME }</option>
				      		</c:forEach>
				      	</select>
					</td>
				</tr>
				</c:if>
				<tr>
					<td align="right">意向车型：</td>
					<td align="left">
                		<input id="seriesId" name="seriesId" type="text"  size="13"  readonly="readonly" value=""/> 
						<input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" onclick="toSeriesList('seriesId','','true', '${orgId}')" value="..." />
						<input class="normal_btn" type="button" value="清空" onclick="clrTxt('seriesId');"/>
              
					</td>
				</tr>
				<tr>
					<td colspan="6" align="center"><input name="queryBtn"
						type="button" class="normal_btn" onclick="__extQuery__(1);"
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
	var url = "<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/dlrLeadsFindQuery.json";
	var title = null;
	var managerLogon = '${managerLogon}';
	if(managerLogon=='yes') {
		
		var controlCols = "<input type=\"checkbox\" name=\"conAll\" onclick=\"selectAll(this,'leadsGroup')\"/>全选&nbsp;&nbsp;"
			+ "<input type=\"button\" value=\"批量分派\" class=\"normal_btn\" onclick=\"batchAuditingConfirm();\"/>";
		var columns = [
						{header: "序号", renderer:getIndex, align:'center'},
						{header: controlCols,align:'center',renderer:createCheckbox},
						{header: "线索来源", dataIndex: 'LEADS_ORIGIN', align:'center'},
						{header: "导入时间", dataIndex: 'CREATE_DATE2', align:'center'},
						{header: "客户姓名", dataIndex: 'CUSTOMER_NAME', align:'center'},
						{header: "联系电话", dataIndex: 'TELEPHONE', align:'center'},
						{header: "意向车型", dataIndex: 'SERIES_NAME', align:'center'},
						{header: "客户描述", dataIndex: 'CUSTOMER_DESCRIBE', align:'center'},
						{header: "分派顾问", dataIndex: 'ADVISER', align:'center'},
						{header: "集客方式", dataIndex: 'JC_WAY', align:'center'},
						{header: "分派时间", dataIndex: 'ALLOT_ADVISER_DATE', align:'center'},
						{header: "处理时间", dataIndex: 'CONFIRM_DATE', align:'center'},
						{header: "线索状态", dataIndex: 'LEADS_STATUS', align:'center'},
						{header: "操作",sortable: false,dataIndex: 'LEADS_ALLOT_ID',renderer:myLink}
				      ];
	} else {
		var columns = [
						{header: "序号", renderer:getIndex, align:'center'},
						{header: "线索来源", dataIndex: 'LEADS_ORIGIN', align:'center'},
						{header: "导入时间", dataIndex: 'CREATE_DATE2', align:'center'},
						{header: "客户姓名", dataIndex: 'CUSTOMER_NAME', align:'center'},
						{header: "联系电话", dataIndex: 'TELEPHONE', align:'center'},
						{header: "意向车型", dataIndex: 'SERIES_NAME', align:'center'},
						{header: "客户描述", dataIndex: 'CUSTOMER_DESCRIBE', align:'center'},
						{header: "分派顾问", dataIndex: 'ADVISER', align:'center'},
						{header: "集客方式", dataIndex: 'JC_WAY', align:'center'},
						{header: "分派时间", dataIndex: 'ALLOT_ADVISER_DATE', align:'center'},
						{header: "处理时间", dataIndex: 'CONFIRM_DATE', align:'center'},
						{header: "线索状态", dataIndex: 'LEADS_STATUS', align:'center'},
						{header: "操作",sortable: false,dataIndex: 'LEADS_ALLOT_ID',renderer:myLink}
				      ];
	}
	
	
	function myLink(value,meta,record){
		 
		//经理权限有重新分派
		var managerLogon = '${managerLogon}';
		if(record.data.IF_CONFIRM == '60321001' && record.data.LEADS_STATUS_CODE != '60161004' && managerLogon=='yes') {
			return String.format("<a href=\"#\" id=\"LEADS_ALLOT_ID\" name=\"LEADS_ALLOT_ID\" onclick='checkDetailUrl(\""+record.data.LEADS_ALLOT_ID+"\",\""+record.data.DEALER_ID+"\",\""+record.data.LEADS_CODE+"\")'>[重新分派]</a>");
		} else {
			return '';
		}
	}   
	function checkDetailUrl(leadsAllotId,dealerId,leadsCode){
		document.getElementById("leadsCode2").value = leadsAllotId;
		document.getElementById("leadsCode").value = leadsCode;
		showAdviserByLeads('dealerCode','','true',leadsAllotId,'',dealerId);
		//window.location.href="<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/dlrLeadsAllotByDlrInit.do?leadsAllotId="+leadsAllotId;
	}
	function returnFunction(code){
		var leadsAllotId = document.getElementById("leadsCode2").value;
		var leadsCode = document.getElementById("leadsCode").value;
		var leadsGroup = document.getElementById("leadsGroup2").value;
		var leadsCodeGroup= document.getElementById("leadsCodeGroup2").value;
		if(leadsAllotId==null||leadsAllotId==''){
			$('fm').method = "post" ;
			$('fm').action= "<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/dlrLeadsAllotByDlr.do?comeFlag=xsfpcx&leadsAllotId=&leadsGroup="+leadsGroup+"&adviserId="+code+"&leadsCode="+leadsCode+"&leadsCodeGroup="+leadsCodeGroup;
			$('fm').submit();
		} else {
			$('fm').method = "post" ;
			$('fm').action= "<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/dlrLeadsAllotByDlr.do?comeFlag=xsfpcx&leadsAllotId="+leadsAllotId+"&leadsGroup=&adviserId="+code+"&leadsCode="+leadsCode;
			$('fm').submit();
		}
		MyAlert('执行成功!');
	}
	//生成批量审核使用的选择框
	function createCheckbox(value,meta,record){
  	  		//var claimStatus = record.data.STATUS;
  	  		//var authcodeval = record.data.AUTH_CODE;
  	  		var managerLogon = '${managerLogon}';
  	  		if(record.data.IF_CONFIRM == '60321001' && record.data.LEADS_STATUS_CODE != '60161004' && managerLogon=='yes') {
			resultStr = String.format("<input type=\"checkbox\" name=\"leadsGroup\" value=\""+record.data.LEADS_ALLOT_ID+"\"/><input type=\"hidden\" name=\"leadsCodez\" value=\""+record.data.LEADS_CODE+"\"/><input type=\"hidden\" name=\"dealerIdz\" value=\""+record.data.DEALER_ID+"\"/>");
  	  		} else {
  	  		resultStr = '';
  	  		}
		return resultStr;
	}
	 //批量审批
	function batchAuditingConfirm(){
		var selectArray = document.getElementsByName('leadsGroup');
		var selectArray2 = document.getElementsByName('leadsCodez');
		var selectArray3 = document.getElementsByName('dealerIdz');
		if(isSelectCheckBox(selectArray)){
			//MyConfirm("是否批量分派?",batchAuditing,[selectArray]);
			//数组转为字符串
			var leadsGroup = '';
			var leadsCodeGroup='';
			for(var i=0;i<selectArray.length;i++) {
				if(selectArray[i].checked) {
					leadsGroup = leadsGroup + selectArray[i].value + ",";
					leadsCodeGroup = leadsCodeGroup + selectArray2[i].value + ",";
				}
			}
			var leadsCode = selectArray2[1].value;
			var dealerId = selectArray3[1].value;
			leadsCodeGroup=leadsCodeGroup.substring(0,leadsCodeGroup.length-1);
			leadsGroup=leadsGroup.substring(0,leadsGroup.length-1);
			document.getElementById("leadsGroup2").value = leadsGroup;
			document.getElementById("leadsCode").value = leadsCode;
			document.getElementById("leadsCodeGroup2").value = leadsCodeGroup;
			showAdviserByLeads('dealerCode','','true','',leadsGroup,dealerId);
		}else{
			MyAlert("请选择要分派的销售线索！");
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
		window.location.href="<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/dlrLeadsAllotByDlrInit.do?leadsGroup="+leadsGroup; 
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
	 function toSeriesList(inputCode ,isMulti ,orgId)
	    {
	    	if(!inputCode){ inputCode = null;}
	    	if(!isMulti){ isMulti = null;}
	    	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	    	OpenHtmlWindow(g_webAppName+'/jsp/crm/report/showSeriesCode.jsp?INPUTCODE='+inputCode+"&ISMULTI="+isMulti+"&ORGID="+orgId,730,390);
	    }
	 function clrTxt(txtId){
	    	document.getElementById(txtId).value="";
	    }
	</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar29", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar28", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar27", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar26", "topbar")</script>
	<script type="text/javascript">ddlevelsmenu.setup("ddtopmenubar25", "topbar")</script>
</body>
</html>
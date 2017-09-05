<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>市场质量信息查询报表</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
	function AlertExcelInfo(){
		
		var info = '<%=request.getAttribute("DataLonger")%>';
		if(info !=null && info!="" && info != "null"){
			MyAlert(info);
		}
	}
</script>
</head>
<body onload="AlertExcelInfo();">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后服务报表 &gt;市场质量信息查询报表</div>
	<form method="post" name = "fm" id="fm">
		<input type="hidden" id="importantLevelsH" name="importantLevelsH">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />市场质量信息查询报表</th>
			
			<tr>
				<td align="right" nowrap="true">重要度：</td>
				<td align="left" nowrap="true">
					<c:forEach var="importantLevel" items="${importantLevelList}">
						<input type="checkbox" name="importantLevels" value="${importantLevel.value}">${importantLevel.key}
					</c:forEach>
				</td>
				<td align="right" nowrap="true">审核人：</td>
				<td align="left" nowrap="true">
					<input type="text" id="verifyBy" name="verifyBy">
				</td>			
			</tr>
			<tr>
				<td align="right" nowrap="true">上报日期：</td>
				<td align="left" nowrap="true">
					<input class="short_txt" id="AdateStart" name="AdateStart" datatype="1,is_date,10"
                           maxlength="10" group="AdateStart,AdateEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'AdateStart', false);" type="button"/>
                	    至
                    <input class="short_txt" id="AdateEnd" name="AdateEnd" datatype="1,is_date,10"
                           maxlength="10" group="AdateStart,AdateEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'AdateEnd', false);" type="button"/>
				</td>
				<td align="right" nowrap="true">审核日期：</td>
				<td align="left" nowrap="true">
					<input class="short_txt" id="VdateStart" name="VdateStart" datatype="1,is_date,10"
                           maxlength="10" group="VdateStart,VdateEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'VdateStart', false);" type="button"/>
                	    至
                    <input class="short_txt" id="VdateEnd" name="VdateEnd" datatype="1,is_date,10"
                           maxlength="10" group="VdateStart,VdateEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'VdateEnd', false);" type="button"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">车系：</td>
				<td align="left" nowrap="true">
					<select id="series" name="series" class="short_sel" onchange="changeModelEvent(this.value,'',false)">
						<option value="">--请选择--</option>
						<c:forEach var="seri" items="${seriesList}">
							<option value="${seri.groupId}" title="${seri.groupName}">${seri.groupName}</option>
						</c:forEach>
					</select>
				</td>
				<td align="right" nowrap="true">车型：</td>
				<td align="left" nowrap="true">
					<select id="model" name="model" class="short_sel">
						<option value="">--请选择--</option>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">经销商全称</td>
				<td align="left" nowrap="true"><input type="text" id="dealerName" name="dealerName"></td>
				<td class="table_query_2Col_label_6Letter" >经销商代码：
	           	<input type="hidden"  id="CODE_ID" value="${code}"/>
	           	</td>
           
	            <td colspan="2" align="left" >
	            	<input class="middle_txt" id="dealerCode" name="DEALER_CODE" value="" type="text"/>
	            	<input class="mark_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCode','','true','',true,'','10771002')"/> 
	            	<input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/>       
	            </td>
			</tr>
				<tr>
				<td align="right" nowrap="true">大区：</td>
				<td align="left" nowrap="true">
					<select id="tmOrg" name="tmorg" class="short_sel" onchange="changeSmallOrgEvent(this.value,'',false)">
						<option value="">--请选择--</option>
						<c:forEach var="org" items="${tmOrgList}">
							<option value="${org.orgId}" title="${org.orgName}">${org.orgName}</option>
						</c:forEach>
					</select>
				</td>
				<td align="right" nowrap="true">小区：</td>
				<td align="left" nowrap="true">
					<select id="tmOrgSmall" name="tmOrgSmall" class="short_sel" onchange="changeSmallOrgDealerEvent(this.value,'',false)">
						<option value="">--请选择--</option>
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="4" align="center">
					<input align="right" class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="search();" />
					<input id="downExcel" name="downExcel" type="button" value="导出Excel" class="normal_btn" onclick="downExcelQuery();" />
        		</td>
			</tr>
		</table>
		
	 <!-- 查询条件 end -->
	 <!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	 <!--分页 end -->
	</form>
<script type="text/javascript">
	function downExcelQuery(){
		fm.action = '<%=contextPath%>/report/service/QualityInfoReportForms/qualityInfoReportFormsExcel.do';
		fm.submit();
	}
	
	function search(){
		setHiddenCheckbox();
		__extQuery__(1);
	}
	
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/report/service/QualityInfoReportForms/queryQualityInfoReportForms.json";
				
	var title = null;
	var columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{header: "重要度",dataIndex: 'IMPORTANTLEVEL',align:'center'},
				{header: "车系名称",dataIndex: 'SERIESNAME',align:'center'},
				{header: "车型", dataIndex: 'MODELNAME', align:'center'},
				{header: "大区", dataIndex: 'ROOTORGNAME'},
				{header: "小区",dataIndex: 'ORGNAME',align:'center'},
				{header: "经销商代码",dataIndex: 'DEALERCODE',align:'center'},
				{header: "经销商全称", dataIndex: 'DEALERNAME', align:'center'},
				{header: "故障名称", dataIndex: 'FAULTNAME'},
				{header: "条件及现象",dataIndex: 'CONDITION',align:'center'},
				{header: "检查结论",dataIndex: 'CHECKRESULT',align:'center'},
				{header: "处理方法", dataIndex: 'CONTENT', align:'center'},
				{header: "配件代码", dataIndex: 'PARTOLDCODE', align:'center'},
				{header: "配件名称",dataIndex: 'PARTCNAME',align:'center'},
				{header: "制造商代码",dataIndex: 'MAKERCODE',align:'center'},
				{header: "制造商名称", dataIndex: 'MAKERNAME', align:'center'},
				{header: "补充说明", dataIndex: 'REMARK', align:'center'},
				{header: "上报日期",dataIndex: 'APPLYADATE',align:'center'},
				{header: "销售日期",dataIndex: 'PURCHASEDDATE',align:'center'},
				{header: "故障日期", dataIndex: 'FAULTDATE', align:'center'},
				{header: "VIN代码", dataIndex: 'VIN', align:'center'},
				{header: "发动机号",dataIndex: 'ENGINENO',align:'center'},
				{header: "用途",dataIndex: 'PURPOSE',align:'center'},
				{header: "道路状况", dataIndex: 'ROAD', align:'center'},
				{header: "温度时间", dataIndex: 'TEMP', align:'center'},
				{header: "发生时机速度",dataIndex: 'HAPPENTIMESPEED',align:'center'},
				{header: "雨水状况",dataIndex: 'RAIN',align:'center'},
				{header: "空调状态", dataIndex: 'AIRSTATUS', align:'center'},
				{header: "平时使用状况", dataIndex: 'USED', align:'center'},
				{header: "审核状态",dataIndex: 'VERIFYSTAUTS',align:'center'},
				{header: "审核时间",dataIndex: 'VERIFYDATE',align:'center'},
				{header: "审核人", dataIndex: 'VERIFYNAME', align:'center'}
		      ];

				//重置下拉框数据
		function resetSelectOption(id,maps,dataName,dataValue,dataId,isdisabled){
			clearSelectNode(id);
			addSelectNode(id,maps,dataName,dataValue,dataId,isdisabled);
		}
		
		//动态删除下拉框节点
		function clearSelectNode(id) {			
			document.getElementById(id).options.length=0; 			
		}
		//动态添加下拉框节点
		function addSelectNode(id,maps,dataName,dataValue,dataId,isdisabled){
			document.getElementById(id).options.add(new Option('-请选择-',''));
			if(maps != null){
				for(var i = 0; i<maps.length;i++){
					if((maps[i])['' +dataValue+''] == dataId){
						document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,true));
					}
					else{
						document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,false));
					}
				}
			}
			
			if(isdisabled == 'true' || isdisabled == true){
				document.getElementById(id).disabled = "disabled";
			}else{
				document.getElementById(id).disabled = "";
			}
			
		}
		
		//车种级联事件
		function changeModelEvent(value,defaultValue,isdisabled){
			if(''!=value){
				makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeVhclMaterialGroup.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeVhclMaterialGroupBack,'fm','');
			}else{
				resetSelectOption('model',null,null,null,null,null);
			}
		}

		//车型级联回调方法：
		function changeVhclMaterialGroupBack(json) {
			resetSelectOption('model',json.vhclList,'GROUPNAME','GROUPID',null,null);
		}
		
		//大区级联小区事件
		function changeSmallOrgEvent(value,defaultValue,isdisabled){
			if(''!=value){
				makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/cascadeOrgSmallOrg.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeSmallOrgEventBack,'fm','');
			}else{
				resetSelectOption('tmOrgSmall',null,null,null,null,null);
			}
		}

		//大区级联小区回调方法：
		function changeSmallOrgEventBack(json) {
			resetSelectOption('tmOrgSmall',json.orgProList,'ORG_NAME','ORG_ID',null,null);
		}
		

		//小区级联经销商事件
		function changeSmallOrgDealerEvent(value,defaultValue,isdisabled){
			if(''!=value){
				makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeOrgDealer.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeSmallOrgDealerEventBack,'fm','');
			}else{
				resetCheckBoxOption('checkDiv',null,null,null,null,null);
			}
		}

		//小区级联经销商回调方法：
		function changeSmallOrgDealerEventBack(json) {
			resetCheckBoxOption('checkDiv',json.dealerList,'DEALER_CODE','DEALER_ID','dealers');
		}
		
		function resetCheckBoxOption(parentID,maps,dataName,dataValue,name){
			clearCheckBoxOption(parentID);
			addCheckBoxOption(parentID,maps,dataName,dataValue,name);
		}
		
		function clearCheckBoxOption(parentID){
			var nodes = document.getElementById(parentID).childNodes;
			for(var i = 0; i<nodes.length;i++){
				document.getElementById(parentID).removeChild(nodes[i]);
			}
		}
		
		function addCheckBoxOption(parentID,maps,dataName,dataValue,name){
			if(maps != null){
				var str="";
				for(var i = 0; i<maps.length;i++){
					if(str == ""){
						str ="<input type='checkbox' name='" +name+"' value='" +(maps[i])['' +dataValue+'']+"' />"+(maps[i])['' +dataName+'']+"&nbsp&nbsp";
					}
					else {
						if((i+1)%10 == 0){
							str = str + "<input type='checkbox' name='" +name+"' value='" +(maps[i])['' +dataValue+'']+"' />"+(maps[i])['' +dataName+''] +"&nbsp&nbsp</br>";
						}else {
							str = str + "<input type='checkbox' name='" +name+"' value='" +(maps[i])['' +dataValue+'']+"' />"+(maps[i])['' +dataName+''] +"&nbsp&nbsp";
						}
					}
				}
				document.getElementById(parentID).innerHTML = str;
			}
		}
	//添加多选框数据
	var checkBoxIds = new Array();
	function addCheckBoxIds(id){
		var cnt = 0;
		var chk=document.getElementsByName(id);
		var l = chk.length;
		checkBoxIds.splice(0,checkBoxIds.length);
		for(var i=0;i<l;i++){
			if(chk[i].checked){
		       cnt++;
		       checkBoxIds.push(chk[i].value);
			}
		 }
		return checkBoxIds;
	}
	
	function setHiddenCheckbox(){
		//setTextData('dealersH',addCheckBoxIds('dealers'),false);
		setTextData('importantLevelsH',addCheckBoxIds('importantLevels'),false);
	}
	
	function setTextData(id,value,isdisabled){
		if(value == null) {
			value = ""; 
			return;
		}
		
		document.getElementById(id).value = value;
		document.getElementById(id).disabled = isdisabled;
	}
	
	//清空经销商框
	function clearInput(){
		var target = document.getElementById('dealerCode');
		target.value = '';
	}
		
</script>
</body>
</html>
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
<title>服务商上报材料费情况表</title>
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
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后服务报表 &gt;服务商上报材料费情况表</div>
	<form method="post" name = "fm" id="fm">
		<input type="hidden" id="typeH" name="typeH">
		<input type="hidden" id="specialH" name="specialH">
		<input type="hidden" id="activityH" name="activityH">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />服务商上报材料费情况表</th>
			<tr>
				<td align="right" nowrap="true">服务站代码：</td>
				<td align="left" nowrap="true">
					<input type="text" id="dealerCode" name="dealerCode">
				</td>
				<td align="right" nowrap="true">服务站名称：</td>
				<td align="left" nowrap="true">
					<input type="text" id="dealerName" name="dealerName">
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
					<select id="tmOrgSmall" name="tmOrgSmall" class="short_sel">
						<option value="">--请选择--</option>
					</select>
				</td>
			</tr>
			
						<tr>
				<td align="right" nowrap="true">结算上报日期：</td>
				<td align="left" nowrap="true">
					<input class="short_txt" id="FStartDate" name="FStartDate" datatype="1,is_date,10"
                           maxlength="10" group="FStartDate,FEndDate"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'FStartDate', false);" type="button"/>
                	    至
                    <input class="short_txt" id="FEndDate" name="FEndDate" datatype="1,is_date,10"
                           maxlength="10" group="FStartDate,FEndDate"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'FEndDate', false);" type="button"/>
				</td>	
				<td align="right" nowrap="true">结算日期：</td>
				<td align="left" nowrap="true">
					<input class="short_txt" id="AStartDate" name="AStartDate" datatype="1,is_date,10"
                           maxlength="10" group="AStartDate,AEndDate"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'AStartDate', false);" type="button"/>
                	    至
                    <input class="short_txt" id="AEndDate" name="AEndDate" datatype="1,is_date,10"
                           maxlength="10" group="AStartDate,AEndDate"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'AEndDate', false);" type="button"/>
				</td>					
			</tr>
			
			<tr>
				<td align="right" nowrap="true">索陪类型：</td>
				<td align="left" nowrap="true">
					<c:forEach var="type" items="${typeList}" varStatus="tl">
						<c:choose>
							<c:when test="${tl.count % 3 == 0}">
								<input type="checkbox" name="types" value="${type.value}">${type.key}</br>
							</c:when>
							<c:otherwise>
								&nbsp;<input type="checkbox" name="types" value="${type.value}">${type.key}
							</c:otherwise>
						</c:choose>
						
					</c:forEach>
				</td>
				<td align="right" nowrap="true">特殊类型：</td>
				<td align="left" nowrap="true">
					<c:forEach var="special" items="${specialInit}" varStatus="tl">
						<c:choose>
							<c:when test="${tl.count % 3 == 0}">
								<input type="checkbox" name="specials" value="${special.value}">${special.key}</br>
							</c:when>
							<c:otherwise>
								&nbsp;<input type="checkbox" name="specials" value="${special.value}">${special.key}
							</c:otherwise>
						</c:choose>
						
					</c:forEach>
				</td>	
			</tr>
			
			<tr>
				<td align="right" nowrap="true">活动类型：</td>
				<td align="left" nowrap="true">
					<c:forEach var="activity" items="${activityInit}" varStatus="tl">
						<c:choose>
							<c:when test="${tl.count % 3 == 0}">
								<input type="checkbox" name="activitys" value="${activity.value}">${activity.key}</br>
							</c:when>
							<c:otherwise>
								&nbsp;<input type="checkbox" name="activitys" value="${activity.value}">${activity.key}
							</c:otherwise>
						</c:choose>
						
					</c:forEach>
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
		setHiddenCheckbox();
		fm.action = '<%=contextPath%>/report/service/DealerFinFDetail/dealerReportFormsExcel.do';
		fm.submit();
	}
	
	function search(){
		//---------------------wenyudan
		var types=document.getElementsByName("types");
		var specials=document.getElementsByName("specials");
		var cnt=0;
		for(var i=0;i<types.length;i++){
			if(types[i].checked){
		       cnt++;
			}
		 }
		for(var i=0;i<specials.length;i++){
			if(specials[i].checked){
		       cnt++;
			}
		 }
		if(cnt==0){
			MyAlert("索赔类型或者特殊类型必选！");
			return;
		}
		//----------------------
		
		
		setHiddenCheckbox();
		__extQuery__(1);
	}
	
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/report/service/DealerFinFDetail/Three_guarantees_json.json";
				
	var title = null;
	var columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{header: "服务站代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "服务站名称",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "结算总金额",dataIndex: 'BALANCE_AMOUNT',align:'center'},
				{header: "维修台数", dataIndex: 'REPAIR_TOTAL', align:'center'}
		      ];
	
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
		setTextData('typeH',addCheckBoxIds('types'),false);
		setTextData('specialH',addCheckBoxIds('specials'),false);
		setTextData('activityH',addCheckBoxIds('activitys'),false);
	}
	
	function setTextData(id,value,isdisabled){
		if(value == null) {
			value = ""; 
			return;
		}
		
		document.getElementById(id).value = value;
		document.getElementById(id).disabled = isdisabled;
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
		
</script>
</body>
</html>
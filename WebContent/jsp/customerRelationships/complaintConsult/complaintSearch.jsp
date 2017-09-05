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
<title>投诉查询</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
	
	function initDate(){
		var   Nowdate=new   Date();       
		document.getElementById('dateStart').value = DateUtil.Format("yyyy-MM-dd",Nowdate);  
		document.getElementById('dateEnd').value = DateUtil.Format("yyyy-MM-dd",Nowdate);  
	}
	
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 投诉咨询管理 &gt;投诉查询</div>
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />查询条件</th>
			
			<tr>
				<td align="right" nowrap="true">VIN号码：</td>
				<td align="left" nowrap="true">
					<input type="text" id="vin" name="vin"/>
				</td>
				<td align="right" nowrap="true">客户姓名：</td>
				<td align="left" nowrap="true">
					<input type="text" id="name" name="name"/>
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">车型：</td>
				<td align="left" nowrap="true">
					<select id="model" name="model">
						<option value=''>-请选择-</option>
						<c:forEach var="mode" items="${modelList}">
							<option value="${mode.groupId}" title="${mode.groupName}">${mode.groupName}</option>
						</c:forEach>
					</select>
				</td>
				<td align="right" nowrap="true">投诉单编号：</td>
				<td><input type="text" id="cpNo" name="cpNo"/></td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">抱怨级别：</td>
				<td align="left" nowrap="true">
					<script type="text/javascript">
						genSelBoxExp("level",<%=Constant.COMPLAINT_LEVEL%>,"",true,"short_sel","","false",'');
					</script>
				</td>
				<td align="right" nowrap="true">联系电话：</td>
				<td align="left" nowrap="true">
					<input type="text" id="tele" name="tele"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">报怨类型：</td>
				<td align="left" nowrap="true">				
					<select id="biztype" name="biztype" class="short_sel">
						<option value=''>-请选择-</option>
						<c:forEach var="bc" items="${bclist}">
							<option value="${bc.CODEID}" title="${bc.CODEDESC}">${bc.CODEDESCVIEW}</option>
						</c:forEach>
					</select>
				</td>
				<td align="right" nowrap="true">受理人：</td>
				<td align="left" nowrap="true">
					<input type="text" id="accUser" name="accUser"/>
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">工单状态：</td>
				<td align="left" nowrap="true">
					<select id="status" name="status" class="short_sel">
						<option value=''>-请选择-</option>
						<c:forEach var="sta" items="${statuslist}">
							<option value="${sta.CODE_ID}" title="${sta.CODE_DESC}">${sta.CODE_DESC}</option>
						</c:forEach>
					</select>
				</td>
				<td align="right" nowrap="true">处理人：</td>
				<td align="left" nowrap="true">
					<input type="text" id="dealUser" name="dealUser"/>
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">大区：</td>
				<td align="left" nowrap="true">
					<select id="region" name="region" class="short_sel">
						<option value=''>-请选择-</option>
						<c:forEach var="tmorg" items="${tmOrgList}">
							<option value="${tmorg.orgId}" title="${tmorg.orgName}">${tmorg.orgName}</option>
						</c:forEach>
					</select>
				</td>							
				<td align="right" nowrap="true">重复投诉：</td>
				<td align="left" nowrap="true">
					<input id="repeatComplaint" name="repeatComplaint" type="checkbox"  value="" onchange="changeRepeatComplaint()">
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">省市：</td>
				<td align="left">
					<select id="pro" name="pro" class="short_sel">
						<option value=''>-请选择-</option>
						<c:forEach var="pro" items="${proviceList}">
							<option value="${pro.REGION_CODE}" title="${pro.REGION_NAME}">${pro.REGION_NAME}</option>
						</c:forEach>
					</select>
				</td>
				<td align="right" nowrap="true">受理日期：</td>
				<td align="left" nowrap="true">
					<input name="dateStart" type="text" group="dateStart,dateEnd" datatype="1,is_date,10" class="short_time_txt" id="dateStart" readonly="readonly"/> 
					<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'dateStart', false);" />  	
		             &nbsp;至&nbsp;
		            <input name="dateEnd" type="text" group="dateStart,dateEnd" datatype="1,is_date,10" class="short_time_txt" id="dateEnd" readonly="readonly"/> 
					<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'dateEnd', false);" /> 
				</td>
			</tr>	
			
			<tr>
				<td align="right" nowrap="true">延迟状态：</td>
				<td align="left" nowrap="true">
					<script type="text/javascript">
						genSelBoxExp("delaystatus",<%=Constant.DELAY_STATUS%>,"",true,"short_sel","","false",'');
					</script>
				</td>
				<td align="right" nowrap="true">处理日期：</td>
				<td align="left" nowrap="true">
					<input name="dealStart" type="text" group="dealStart,dealEnd" datatype="1,is_date,10" class="short_time_txt" id="dealStart" readonly="readonly"/> 
					<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'dealStart', false);" />  	
		             &nbsp;至&nbsp;
		            <input name="dealEnd" type="text" group="dealStart,dealEnd" datatype="1,is_date,10" class="short_time_txt" id="dealEnd" readonly="readonly"/> 
					<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'dealEnd', false);" /> 
				</td>				
			</tr>
			<!-- 艾春 2013.11.27 添加需考核日期查询条件 -->
			<tr>
				<td align="right" nowrap="true">是否及时关闭：</td>
				<td align="left" nowrap="true">
					<script type="text/javascript">
						genSelBoxExp("is_close",<%=Constant.IF_TYPE%>,"",true,"short_sel","","false",'');
					</script>
				</td>
				<td align="right" nowrap="true">需考核日期：</td>
				<td align="left" nowrap="true">
					<input name="KH_START_DATE" type="text" group="KH_START_DATE,KH_END_DATE" datatype="1,is_date,10" class="short_time_txt" id="KH_START_DATE" readonly="readonly"/> 
					<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'KH_START_DATE', false);" />  	
		             &nbsp;至&nbsp;
		            <input name="KH_END_DATE" type="text" group="KH_START_DATE,KH_END_DATE" datatype="1,is_date,10" class="short_time_txt" id="KH_END_DATE" readonly="readonly"/> 
					<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'KH_END_DATE', false);" /> 
				</td>
			</tr>
			<tr>
			<td align="right" nowrap="true">是否一次性及时关闭：</td>
				<td align="left" nowrap="true">
					<script type="text/javascript">
						genSelBoxExp("is_one_close",<%=Constant.IF_TYPE%>,"",true,"short_sel","","false",'');
					</script>
				</td>
				<td align="right" nowrap="true">延期日期：</td>
				<td align="left" nowrap="true">
					<input name="CP_START_DATE" type="text" group="CP_START_DATE,CP_END_DATE" datatype="1,is_date,10" class="short_time_txt" id="CP_START_DATE" readonly="readonly"/> 
					<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'CP_START_DATE', false);" />  	
		             &nbsp;至&nbsp;
		            <input name="CP_END_DATE" type="text" group="CP_START_DATE,CP_END_DATE" datatype="1,is_date,10" class="short_time_txt" id="CP_END_DATE" readonly="readonly"/> 
					<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'CP_END_DATE', false);" /> 
				</td>		
			</tr>
			<!-- 艾春 2013.11.27 添加需考核日期查询条件 -->
			<tr>
				<td colspan="8" align="center">
					<input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="__extQuery__(1);" />
					&nbsp;
          			<input id="downExcel" name="downExcel" type="button" value="转Excel" class="normal_btn" onclick="downExcelQuery();" />
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
		fm.action = '<%=contextPath%>/customerRelationships/complaintConsult/ComplaintSearch/complaintSearchDownExcel.do';
		fm.submit();
	}
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/complaintConsult/ComplaintSearch/queryComplaintSearch.json";
				
	var title = null;

	var columns = [				
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{id:'action',header: "操作",sortable: false,dataIndex: 'CPID',renderer:myLink},
				{header: "抱怨单编号",dataIndex: 'CPNO',align:'center'},				
				{header: "状态", dataIndex: 'CPSTATUS', align:'center',renderer:getItemValue},
				{header: "当前处理人",dataIndex: 'DUSER',align:'center'},			
				{header: "抱怨级别", dataIndex: 'CPLEVEL', align:'center',renderer:getItemValue},
				{header: "抱怨类别",dataIndex: 'BIZCONT',align:'center',renderer:getItemValue},
				{header: "省市", dataIndex: 'PRO', align:'center'},
				{header: "地市",dataIndex: 'CITY',align:'center'},
				{header: "客户姓名", dataIndex: 'CTMNAME', align:'center'},
				{header: "联系电话",dataIndex: 'PHONE',align:'center'},
				{header: "抱怨内容",dataIndex: 'CPCONT',align:'center'},
				{header: "抱怨对象", dataIndex: 'CPOBJECT', align:'center'},
				{header: "抱怨时间",dataIndex: 'CREATEDATE',align:'center'},
				{header: "受理人", dataIndex: 'ACUSER', align:'center'},
				{header: "车种",dataIndex: 'SNAME',align:'center'},
				{header: "VIN号", dataIndex: 'VIN', align:'center'},
				{header: "行驶里程",dataIndex: 'CPMILEAGE',align:'center'},
				{header: "处理部门",dataIndex: 'ORGNAME',align:'center'},
				{header: "处理人", dataIndex: 'DEALUSER', align:'center'},
				{header: "转出时间", dataIndex: 'TURNDATE', align:'center'},
				{header: "回访结果",dataIndex: 'CPSF',align:'center',renderer:getItemValue},
				{header: "处理时长(小时)", dataIndex: 'DEALTIME', align:'center'},
				{header: "延期天数(天)", dataIndex: 'DELAYDATE', align:'center'},
				{header: "超期天数(天)", dataIndex: 'EXCEEDDATE', align:'center'},
				{header: "是否及时关闭",dataIndex: 'ISTIMELYCLOSE',align:'center'},
				{header: "延期超期类型",dataIndex: 'DELAY_TYPE',align:'center'},
				{header: "延期次数",dataIndex: 'TIMES',align:'center'},
				{header: "是否一次处理满意", dataIndex: 'CPISONCESF', align:'center',renderer:getItemValue},
				{header: "最终反馈日期",dataIndex: 'LASTDEALDATE',align:'center'}
		      ];

	function myLink(value,meta,record){
		// 艾春 9.24 修改 经过客户朱晓楠确认，不再分管理员和坐席，都可以编辑投诉
		/*if(record.data.ISADMIN == true ){
			return String.format("<input name='detailBtn' type='button' class='middle_btn' onclick='complaintSearch(\""+ record.data.CPID +"\",\""+ record.data.CTMID +"\")' value='查看'/>&nbsp;<input name='detailBtn' type='button' class='middle_btn' onclick='updateComplaint(\""+ record.data.CPID +"\",\""+ record.data.CTMID +"\",\""+ record.data.STATUS +"\")' value='编辑'/>");
		}else{
			return String.format("<input name='detailBtn' type='button' class='middle_btn' onclick='complaintSearch(\""+ record.data.CPID +"\",\""+ record.data.CTMID +"\")' value='查看'/>");
		}*/
		return String.format("<input name='detailBtn' type='button' class='middle_btn' onclick='complaintSearch(\""+ record.data.CPID +"\",\""+ record.data.CTMID +"\")' value='查看'/>&nbsp;<input name='detailBtn' type='button' class='middle_btn' onclick='updateComplaint(\""+ record.data.CPID +"\",\""+ record.data.CTMID +"\",\""+ record.data.STATUS +"\")' value='编辑'/>");
	}
	function complaintSearch(cpid,ctmid){		
		window.open('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintConsult/complaintConsultWatch.do?openPage=1&cpid='+cpid+'&ctmid='+ctmid);
	}
	function updateComplaint(cpid,ctmid,status){
		window.open('<%=contextPath%>/customerRelationships/complaintConsult/WaitComplaintSearch/waitComplaintUpdate.do?pageId=complaintSearch&openPage=1&cpid='+cpid+'&ctmid='+ctmid+'&stauts='+status);
	}
	
	function changeRegionEvent(value,defaultValue,isdisabled){
		if(''!=value){
			makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/cascadeOrgSmallOrg.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeRegionEventBack,'fm','');
		}else{
			resetSelectOption('pro',null,null,null,null,null);
		}
	}
		
	function changeRegionEventBack(json){
		resetSelectOption('pro',json.orgProList,'ORG_NAME','ORG_ID',json.defaultValue,json.isdisabled);
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
		for(var i = 0; i<maps.length;i++){
			if((maps[i])['' +dataValue+''] == dataId){
				document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,true));
			}
			else{
				document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,false));
			}
		}
		if(isdisabled == 'true' || isdisabled == true){
			document.getElementById(id).disabled = "disabled";
		}else{
			document.getElementById(id).disabled = "";
		}
		
	}
	
	function changeRepeatComplaint(){
		var obj = document.getElementById('repeatComplaint');
		if(obj.checked){
			obj.value = "true";
		}else{
			obj.value = "";
		}
	}
</script>
</body>
</html>
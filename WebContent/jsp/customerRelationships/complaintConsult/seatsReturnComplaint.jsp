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
<title>待处理投诉查询</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 投诉咨询管理 &gt;待处理投诉查询</div>
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
				<td align="right" nowrap="true">联系电话：</td>
				<td align="left" nowrap="true">
					<input type="text" id="tele" name="tele"/>
				</td>
				<td align="right" nowrap="true">受理人：</td>
				<td align="left" nowrap="true">
					<input type="text" id="accuser" name="accuser"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">受理日期：</td>
				<td align="left" nowrap="true">
					<input class="short_txt" id="dateStart" name="dateStart" datatype="1,is_date,10"
                           maxlength="10" group="dateStart,dateEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dateStart', false);" type="button"/>
                	    至
                    <input class="short_txt" id="dateEnd" name="dateEnd" datatype="1,is_date,10"
                           maxlength="10" group="dateStart,dateEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dateEnd', false);" type="button"/>
				</td>
				<td align="right" nowrap="true">处理日期：</td>
				<td align="left" nowrap="true">
					<input class="short_txt" id="dealStart" name="dateStart" datatype="1,is_date,10"
                           maxlength="10" group="dealStart,dealEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dealStart', false);" type="button"/>
                	    至
                    <input class="short_txt" id="dealEnd" name="dealEnd" datatype="1,is_date,10"
                           maxlength="10" group="dealStart,dealEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dealEnd', false);" type="button"/>
				</td>
				<!-- 
				<td align="right" nowrap="true">处理状态：</td>
				<td align="left" nowrap="true">
					<script type="text/javascript">
						genSelBoxExp("status",<%=Constant.COMPLAINT_STATUS%>,"${complaintAcceptMap.DEALMODE}",true,"short_sel","${complaintAcceptMap.DEALMODE}","false",'');
					</script>
				</td>
				 -->
			</tr>
			<c:if test="${isAdmin ==  true}">
			<tr>
				<td align="right" nowrap="true"></td>
				<td align="left" nowrap="true" colspan="3"><input type="checkbox" id="advancedSearch" name="advancedSearch" value="true"  onchange="changeAdvanced()"/>高级查询</td>
			</tr>
			</c:if>
			<c:if test="${isAdmin ==  true}">
			<tr>
				<td align="right" nowrap="true">大区：</td>
				<td align="left" nowrap="true">
					<select id="region" name="region" class="short_sel">
						<option value=''>-全部-</option>
						<c:forEach var="tmorg" items="${tmOrgList}">
							<option value="${tmorg.orgId}" title="${tmorg.orgName}">${tmorg.orgName}</option>
						</c:forEach>
					</select>
				</td>
				<td align="right" nowrap="true">省市：</td>
				<td align="left">
					<select id="pro" name="pro" class="short_sel">
						<option value=''>-全部-</option>
						<c:forEach var="pro" items="${proviceList}">
							<option value="${pro.REGION_ID}" title="${pro.REGION_NAME}">${pro.REGION_NAME}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			</c:if>
			<tr>
				<td colspan="8" align="center">
					<input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="__extQuery__(1);" />
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

	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/complaintConsult/SeatsReturnComplaint/querySeatsReturnComplaint.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{id:'action',header: "操作",sortable: false,dataIndex: 'CPID',renderer:myLink},
				{header: "客户姓名",dataIndex: 'CTMNAME',align:'center'},
				{header: "联系电话", dataIndex: 'PHONE', align:'center'},
				{header: "受理日期",dataIndex: 'ACCDATE',align:'center'},
				{header: "受理人", dataIndex: 'ACCUSER', align:'center'},
				{header: "状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue},
				{header: "抱怨内容", dataIndex: 'CONTENT', align:'center'},
				{header: "业务类型",dataIndex: 'BIZTYPE',align:'center'}
		      ];

	function myLink(value,meta,record){
		if(record.data.STATUS == <%=Constant.COMPLAINT_STATUS_WAIT_FEEDBACK%> ){
			return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='watchComplaintSearch(\""+ record.data.CPID +"\",\""+ record.data.CTMID +"\")' value='查看'/><input name='detailBtn' type='button' class='normal_btn' onclick='updateComplaint(\""+ record.data.CPID +"\",\""+ record.data.CTMID +"\",\""+ record.data.STATUS +"\")' value='处理'/>");
		}else{
			return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='watchComplaintSearch(\""+ record.data.CPID +"\",\""+ record.data.CTMID +"\")' value='查看'/>");
		}		
	}
	function watchComplaintSearch(cpid,ctmid){		
		window.open('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintConsult/complaintConsultWatch.do?openPage=1&cpid='+cpid+'&ctmid='+ctmid) ;
	}
	function updateComplaint(cpid,ctmid,status){
		window.open('<%=contextPath%>/customerRelationships/complaintConsult/SeatsReturnComplaint/seatsReturnComplaintUpdate.do?cpid='+cpid+'&ctmid='+ctmid+'&stauts='+status) ;
	}
	
	function changeRegionEvent(value,defaultValue,isdisabled){
		if(''!=value){
			makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/cascadeOrgSmallOrg.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeRegionEventBack,'fm','');
		}else{
			resetSelectOption('pro',null,null,null,null,null);
		}
	}
	
	
	function AT_Command01(strCmd,txtTel) 
	{
    switch (strCmd) {
        case "Pickup":
            parent.document.ut_atocx.ATAnswer('${LOGON_USER.actn}', "");
            break;
        case "Hangup":
            parent.document.ut_atocx.ATHangup('${LOGON_USER.actn}', "");
            break;
        case "PlaceCall": //呼叫电话
            parent.document.ut_atocx.ATPlaceCall('${LOGON_USER.actn}',txtTel);
            break;
        default:
            MyAlert("未处理命令：" + strCmd);
            break;
    }
    return true;
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
	
	function changeAdvanced(){
		var obj = document.getElementById('advancedSearch');
		if(obj.checked){
			obj.value = "true";
		}else{
			obj.value = "";
		}
	}	
	
	function refreshData(){
		__extQuery__(1);
	}

</script>
</body>
</html>
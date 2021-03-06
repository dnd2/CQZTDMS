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
<title>大区延期车厂审核</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 投诉咨询管理 &gt;大区延期车厂审核</div>
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
				<td align="right" nowrap="true">抱怨级别：</td>
				<td align="left" nowrap="true">
					<script type="text/javascript">
						genSelBoxExp("level",<%=Constant.COMPLAINT_LEVEL%>,"${complaintAcceptMap.DEALMODE}",true,"short_sel","${complaintAcceptMap.DEALMODE}","false",'');
					</script>
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">受理人：</td>
				<td align="left" nowrap="true">
					<input type="text" id="accUser" name="accUser"/>
				</td>
				<td align="right" nowrap="true">处理人：</td>
				<td align="left" nowrap="true">
					<input type="text" id="dealUser" name="dealUser"/>
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
				<td align="right" nowrap="true">工单状态：</td>
				<td align="left" nowrap="true">
					<script type="text/javascript">
						genSelBoxExp("status",<%=Constant.COMPLAINT_STATUS%>,"${complaintAcceptMap.DEALMODE}",true,"short_sel","${complaintAcceptMap.DEALMODE}","false",'');
					</script>
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">处理日期：</td>
				<td align="left" nowrap="true">
					<input class="short_txt" id="dealStart" name="dealStart" datatype="1,is_date,10"
                           maxlength="10" group="dealStart,dealEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dealStart', false);" type="button"/>
                	    至
                    <input class="short_txt" id="dealEnd" name="dealEnd" datatype="1,is_date,10"
                           maxlength="10" group="dealStart,dealEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dealEnd', false);" type="button"/>
				</td>
				
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
			</tr>
			
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
							<option value="${pro.REGION_CODE}" title="${pro.REGION_NAME}">${pro.REGION_NAME}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			
			<tr>
				<td colspan="8" align="center">
					<input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="__extQuery__(1);" />
					&nbsp;
          			<input id="downExcel" name="downExcel"  type="button" value="转Excel" class="normal_btn" onclick="downExcelQuery();" />
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
		fm.action = '<%=contextPath%>/customerRelationships/complaintConsult/ODepartmentComplaintVerify/oDepartmentComplaintVerifyDownExcel.do';
		fm.submit();
	}
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/complaintConsult/ODepartmentComplaintVerify/queryODepartmentComplaintVerify.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{id:'action',header: "操作",sortable: false,dataIndex: 'CPID',renderer:myLink},
				{header: "延期",dataIndex: 'DELAYDATE',align:'center'},
				{header: "抱怨单编号",dataIndex: 'CPNO',align:'center'},
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
				{header: "购车时间",dataIndex: 'BDATE',align:'center'},
				{header: "行驶里程",dataIndex: 'CPMILEAGE',align:'center'},
				{header: "里程范围", dataIndex: 'MILEAGERANGE', align:'center'},
				{header: "处理部门",dataIndex: 'ORGNAME',align:'center'},
				{header: "处理人", dataIndex: 'DEALUSER', align:'center'},
				{header: "签收日期",dataIndex: 'CPACCDATE',align:'center'},
				{header: "转出时间", dataIndex: 'TURNDATE', align:'center'},
				{header: "回访结果",dataIndex: 'CPSF',align:'center',renderer:getItemValue},
				{header: "回访人",dataIndex: 'CUSER',align:'center'},
				{header: "回访日期", dataIndex: 'CDATE', align:'center'},
				{header: "关闭时间",dataIndex: 'CPCDATE',align:'center'},
				{header: "处理时长(小时)", dataIndex: 'DEALTIME', align:'center'},
				{header: "规定处理期限(天)",dataIndex: 'CPLIMIT',align:'center'},
				{header: "延期天数(天)", dataIndex: 'DELAYDATE', align:'center'},
				{header: "规定及时关闭时间",dataIndex: 'SHOULDCLOSETIME',align:'center'},
				{header: "是否及时关闭",dataIndex: 'ISTIMELYCLOSE',align:'center'},
				{header: "是否一次处理满意", dataIndex: 'CPISONCESF', align:'center',renderer:getItemValue},
				{header: "是否正常关闭",dataIndex: 'ISNORMALCLOSE',align:'center'},
				{header: "处理过程", dataIndex: 'DEALCONTENT', align:'center'},
				{header: "最终反馈日期",dataIndex: 'LASTDEALDATE',align:'center'},
				{header: "状态", dataIndex: 'CPSTATUS', align:'center',renderer:getItemValue},
				{header: "当前处理人",dataIndex: 'DUSER',align:'center'}
		      ];

	function myLink(value,meta,record){
		var str = String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='verifyDelay(\""+ record.data.CPID +"\",\""+ record.data.CTMID +"\",\""+ record.data.CLSTATUS +"\")' value='审核'/>");
		//if(record.data.CLSTATUS == 95181002 ){
		//	return str;
		//}else{
		//	return '';
		//}
		return str;
	}
	
	function verifyDelay(cpid,ctmid,clstatus){
		window.open('<%=contextPath%>/customerRelationships/complaintConsult/ODepartmentComplaintVerify/verifyDelay.do?cpid='+cpid+'&ctmid='+ctmid+'&clstatus='+clstatus) ;
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
	function refreshData(){
		__extQuery__(1);
	}

</script>
</body>
</html>
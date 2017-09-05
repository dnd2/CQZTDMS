<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String contextPath = request.getContextPath();
%>
<%@ page import="com.infodms.dms.common.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<style>#clrBtn{min-width: 50px}</style>
<script type="text/javascript" >
	var $J = jQuery.noConflict();
	var myPage;
	var url = "<%=contextPath%>/OutMaintainAction/MaintainSelectList.json";
	var menuUrl = "<%=contextPath%>/OutMaintainAction/MaintainSelect.do";
	var title = null;
	var btnIds = new Array();
	var toAddOrCheckUrl = "<%=contextPath%>/OutMaintainAction/addOrCheckMaintainApplication.do";
	var auditingUrl = "<%=contextPath%>/OutMaintainAction/auditing.json";
	$J(document).ready(function(){
		__extQuery__(1);
		loadcalendar();
	});
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{id:'action',header: "操作",dataIndex: 'ID',renderer:toEditInfo},
				{header: "单据状态",sortable: false,dataIndex: 'STATUS',align:'center'},
				{header: "单据编码",sortable: false,dataIndex: 'EGRESS_NO',align:'center'},
				{header: "制单日期",sortable: false,dataIndex: 'CREATE_DATE',align:'center'},
				{header: "服务站编码",sortable: false,dataIndex: 'DEALER_CODE',align:'center'},
				{header: "服务站",sortable: false,dataIndex: 'DEALER_NAME',align:'center'},
				{header: "VIN码",sortable: false,dataIndex: 'VIN',align:'center'},
				//{header: "发动机号",sortable: false,dataIndex: 'ENGINE_NO',align:'center'},
				{header: "车型名称",sortable: false,dataIndex: 'GROUP_NAME',align:'center'},
				{header: "购车日期",sortable: false,dataIndex: 'PURCHASED_DATE',align:'center'},
				{header: "行驶里程",sortable: false,dataIndex: 'MILEAGE',align:'center'},
				{header: "联系人",sortable: false,dataIndex: 'CUSTOMER_NAME',align:'center'},
				{header: "联系电话",sortable: false,dataIndex: 'TELEPHONE',align:'center'},
				{header: "审核时间",sortable: false,dataIndex: 'MINISTER_AUDITING_DATE',align:'center'},
				{header: "工单号",sortable: false,dataIndex: 'CLAIM_NO',align:'center'}
		      ];

	function toEditInfo(value,metaData,record) {
		var dealerId = $J("#dId").val();
		var IS_RLATION_ORDER = record.data.IS_RLATION_ORDER;
		if(dealerId != null && dealerId.length > 0){
			if(record.data.STATUS=="未提报" || record.data.STATUS=="审核退回"){
				return String.format("<a href='#'  onclick='updateData("+value+")'>[修改]</a>"+"&nbsp;"
						+"<a href='#' onclick='check("+value+",1)' id='check'>[查看]</a>"+"&nbsp;"
						+"<a href='#' onclick='report("+value+")' id='check'>[上报]</a>");
			}else if(record.data.STATUS=="审核通过"){
				if(IS_RLATION_ORDER=='10041002'){
					return String.format("<a href='#' onclick='check("+value+",1)' id='DelAs'>[查看]</a>" +
							"<a href='#' onclick='check("+value+",6)'>[补录]</a>");
				}else{
					return String.format("<a href='#' onclick='check("+value+",1)' id='DelAs'>[查看]</a>");
				}			
			}else{
				return String.format("<a href='#' onclick='check("+value+",1)' id='DelAs'>[查看]</a>");
			}
		}else{
				if(record.data.STATUS=="审核中"){
					return String.format("<a href='#' onclick='check("+value+",3)' id='DelAs'>[审核]</a>"+"&nbsp;"
							+ "<a href='#' onclick='check("+value+",1)' id='DelAs'>[查看]</a>");
				}else{
					return String.format("<a href='#' onclick='check("+value+",1)' id='DelAs'>[查看]</a>");
				}
			
		}
		
	}
	/**
	 * 修改
	 * @param value
	 */
	function updateData(value){
		var id ="?check=2&id="+value;
		window.location.href = toAddOrCheckUrl+id;
	}

	/**
	 * 查看
	 * @param value
	 */
	function check(value,check){
		var id ="?check="+check+"&id="+value;
		window.location.href = toAddOrCheckUrl+id;
	}
	function checkB(value,check,type){
		var id ="?check="+check+"&id="+value+"&type="+type;
		window.location.href = toAddOrCheckUrl+id;
	}
	/**
	 * 转外出维修申请
	 * @param value
	 */
	function checkz(value){
		var form = document.getElementById("fm");
		form.action = globalContextPath+"/AfterSales/base/maintain/OutMaintainAction/checkZ.do?flag=t&id="+value;
		form.submit();	
	}
	//上报
	function report(value) {
		MyConfirm("确定上报？",reportDo,[value]);
	}
	function reportDo(value){
		var tUrl = auditingUrl+"?id="+value;
		makeNomalFormCall(tUrl, message, 'fm');
	}

	function message(json){
		if(json.message == "success"){
			MyAlert("操作成功！");
			backe();
			/*MyAlertForFun("操作成功！",backInit);*/
		}else{
			/*MyAlert("操作失败 ");*/
			MyAlert("操作失败！");
		}
	}

	function backInit(){
		window.location.href = menuUrl;
	}
	function backe(){
		__extQuery__(1);
	}
	/**
	 * 新增用户按钮事件
	 */
	function addMaintainApplication(){
		window.location.href = toAddOrCheckUrl;
	}
	/**
	 * 重置按钮事件
	 */
	function ResetAttentionSite(){
		document.getElementById("subscribe_by").value='';
		document.getElementById("subscribe_Telephone").value='';
		document.getElementById("VIN").value='';
		document.getElementById("License_plate").value='';
	}
	function oemTxt(a,b){
		document.getElementById(a).value="";
		document.getElementById(b).value="";
		}

	//选择经销商控件
	function addDealer(inputId,inputCode,isMulti){
		var  idVal=document.getElementById(inputId).value;
		var url=globalContextPath+"/jsp/AfterSales/serviceActivity/addDealer.jsp?idVal="+idVal+"&INPUTCODE="+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti;
		OpenHtmlWindow(url,730,390);
	}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>外出救援申请</title>
</head>
<body>
<div class="wbox">
	<div class="navigation"><font style="font-size: 13px;font-family:serif;"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理 &gt; 车辆信息管理管理&gt; 外出救援申请</font></div>
	<form method="post" name="fm" id="fm">
		<input type="hidden" id="dId" value="${dealerId }" />
		<input type="hidden" id="type" name="type" value="${type }" />
		<div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
		<div class="form-body">
		<table class='table_query'>
			<tr>
				<td width="5%" style="text-align: right">单据编码：</td>
				<td width="10%">
					<input class="middle_txt"  type="text" name="egress_no" id="egress_no" maxlength="18"/>
				</td>
				<td width="5%" style="text-align: right"> 起始日期：</td>
				<td width="19%" align="left">
					<input id="startDate" name="startDate" readonly="readonly" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="display: inline-block;min-width: 60px;width: 90px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
					至
					<input id="endDate" name="endDate" readonly="readonly" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="display: inline-block;min-width: 60px;width: 90px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
					<input type="button" class="u-button" onclick="oemTxt('startDate','endDate');" value="清 空" id="clrBtn"/> 
				</td>
				<td width="5%" style="text-align: right"> 单据状态：</td>
				<td width="10%" align="left">
				<c:choose>
				<c:when test="${dealerId == nul}">
					<script type="text/javascript">
						genSelBoxExp("status",<%=Constant.OUT_MAINTAIN%>,"",true,"u-select","","false",'<%=Constant.OUT_MAINTAIN_01%>');
					</script>
				</c:when>
				<c:otherwise>
					<script type="text/javascript">
						genSelBoxExp("status",<%=Constant.OUT_MAINTAIN%>,"",true,"u-select","","false",'');
					</script>
				</c:otherwise>  
			</c:choose>
				
				</td>
			</tr>
			<tr>
				<td width="5%" style="text-align: right"> VIN码：</td>
				<td width="10%" align="left">
					<input class="middle_txt"  name="vin" id="vin" type="text"  maxlength="18"/>
				</td>
			<c:if test="${dealerId == nul}">
					<td width="5%" style="text-align: right">经销商：</td>
						<td width="15%">
							<input name="dealerCode" id="dealerCode" type="text" class="middle_txt" onclick="showOrgDealer('dealerCode','dealerId','true','','true','','10771002');" readonly="readonly"/>
							<input type="hidden" name="dealerId" id="dealerId" value=""/>    
							<input class="u-button" type="button" value="清空" onclick="oemTxt('dealerId', 'dealerCode');"/> 
				</td>
			</c:if>
			</tr>
			<tr>
				<td colspan="12" style="text-align: center">
						<input name="queryBtn" type="button" class="u-button u-query" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> &nbsp; 
						<input type="reset" class="u-button u-cancel" value="重 置"/> &nbsp;
				<c:if test="${dealerId != null }">
						<input class="u-button u-submit" name="button2" id="button2" type="button" onclick="addMaintainApplication();" value="新增" />
				</c:if>
				</td>
			</tr>
		</table>
		</div>
		</div>
		<!--分页  -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
</div>	
</body>
</html>
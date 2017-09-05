<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<%@ taglib uri="/jstl/cout" prefix="c"%>
	<%@taglib uri="/jstl/fmt" prefix="fmt"%>
	<%@ taglib uri="/jstl/change" prefix="change"%>
	<%@ page import="java.util.LinkedList"%>
	<%@ page import="java.util.List"%>
	<%@ page import="com.infodms.dms.util.CommonUtils"%>
	<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
	<head>
		<%
			List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>) request.getAttribute("fileList");
			request.setAttribute("fileList", fileList);
		%>
		<%
			String contextPath = request.getContextPath();
			String dutyType = String.valueOf(request.getAttribute("dutyType"));
		%>
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<script type="text/javascript"
			src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
		<script type="text/javascript"
			src="<%=contextPath%>/js/jslib/zyw/jquery-1.3.2.js"></script>
		<script type="text/javascript"
			src="<%=contextPath%>/js/jslib/zyw/jquery-calendar.js"></script>
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<link rel="stylesheet" type="text/css"
			href="<%=contextPath%>/js/jslib/zyw/jquery-calendar.css" />
		<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>

		<title>退换车及善意索赔申请审核查询</title>

<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
	//清除按钮
   function clearInput(inputId,did){
		var inputVar = document.getElementById("inputId");
		inputVar.value = '';
		var didVar = document.getElementById("did");
		didVar.value = '';
	}
	//导出
	function to_excel(){
		 fm.action="<%=contextPath%>/SpecialAction/Toexcelspecialapply.do";
	      fm.submit();
	}
	function getModel(dealerId,dealerCode,dealerName){
	   document.getElementById("dealerCode").value=dealerCode;
	}
	//查总额
	function Querycount(){
	  var url = "<%=contextPath%>/SpecialAction/Querycountspecil.json";
	  sendAjax(url,callback,"fm");
	}
	function callback(json){
	  $("count").value = json.count;
	}
</script>
	</head>
		<body>
			<div class="navigation">
				<img src="../jsp_new/img/nav.gif" />
				&nbsp;当前位置：售后服务管理&gt;特殊费用管理&gt;退换车及善意索赔单查询
			</div>
			<form name="fm" id="fm" >
				<input class="middle_txt" id="specialNo" value="${specialNo }" type="hidden" />
				<input type="hidden" id="dealerCode" value="${dealerCode }">
				<input type="hidden" id="is_claim" value="${t.IS_CLAIM }" />
				<input class="middle_txt" id="type" value="${type }" type="hidden" />
				<input class="middle_txt" id="spe_id" value="${t.SPE_ID }" name="spe_id" type="hidden" />
				<input class="middle_txt" id="special_type" value="1" name="special_type" type="hidden" />

				<!-- 查询条件 end -->
				<!-- 查询条件 begin -->

				<table width=100% border="0" cellpadding="1" cellspacing="1" class="table_query">
					<tr>
						<td width="10%" nowrap="true" align="right">
							申请单号：
						</td>
						<td width="5%" nowrap="true" align="left">
							<input class="long_txt"  type="text" id="apply_no" name="apply_no"/>
						</td>
						<td width="10%" nowrap="true" align="right">
							经销商代码：
						</td>
						<td  colspan="6" align="left" nowrap="nowrap">
							<input class="long_txt"  id="dealerCode" name="dealerCode"
								type="text" />
							<input class="short_txt" id="dealerId" name="dealerId"
								type="hidden" />
					<!-- 	<input name="showBtn" type="button" class="mini_btn"
								style="cursor: pointer;"
								onclick="showOrgDealer('dealerCode','dealerId','true','',true,'','10771002');"
								value="..." />
							<input name="clrBtn" type="button" class="normal_btn"
								onclick="clearInput('dealerCode','dealerId');" value="清除" />  -->	
						</td>
					
                       <td width="5%" nowrap="true" align="left">
							
						</td>
					</tr>
					<tr>
						<td width="10%" nowrap="true" align="right">
							VIN：
						</td>
						<td width="5%" nowrap="true" align="left">
							<input class="long_txt"  type="text" id="VIN" name="VIN" />
						</td>
						<td width="15%" nowrap="true" align="right">
							申请类型：
						</td>
						<td width="15%" nowrap="true" align="left">
							<select class="long_txt"  id="SPECIAL_TYPE" name="SPECIAL_TYPE">
								<option value="">
									-请选择-
								</option>
								<option value="1">
									善意索赔
								</option>
								<option value="0">
									退换车
								</option>
							</select>
						</td>
						<td width="15%" nowrap="true" align="right">
							审核状态：
						</td>
						<td width="15%" nowrap="true" align="left">
							<select id="STATUS" name="STATUS">
								<option value="20501002">
									-审核中-
								</option>
								<option value="20501005">
									-审核通过-
								</option>
								<option value="">
									-全部-
								</option>
								<option value="20501001">
									-未上报-
								</option>
								<option value="20501004">
									-审核拒绝-
								</option>
								<option value="20501006">
									-审核退回-
								</option>
							</select>
						</td>
						 <td width="5%" nowrap="true" align="left">
						</td>
						 <td width="5%" nowrap="true" align="left">
						</td>
					</tr>
					<tr>
						<td width="10%" nowrap="true" align="right">
							供应商代码：
						</td>
						<td width="5%" nowrap="true" align="left">
							<input class="long_txt"  type="text" name="supply_code_dealer" id="supply_code_dealer"/>
						</td>
						<td width="10%" nowrap="true" align="right">
							审核时间：
						</td>
						<td width="5%" nowrap="true" align="left">
							<div align="left">
							<input name="CREATE_DATE_S" type="text" id="CREATE_DATE_S" readonly="readonly" onfocus="calendar();" class="middle_txt"/>
								至
								<input name="CREATE_DATE_D" type="text" id="CREATE_DATE_D"  readonly="readonly" onfocus="calendar();" class="middle_txt"/>
							</div>
						</td>
						<td width="10%" nowrap="true" align="right">
						<!-- 	申报次数大于： -->
						大区：
						</td>
						<td width="5%" nowrap="true" align="left">
						  <input class="short_txt" type="text" id="ROOT_ORG_NAME" name="ROOT_ORG_NAME"/>
						</td>
						<td width="15%" nowrap="true" align="left"></td>
						<td width="15%" nowrap="true" align="left"></td>
					</tr>
					<tr>
					  	<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">服务站简称：</td>
      	               <td width="15%" nowrap="true">
      		            <input  class="long_txt" id="dealer_shortname"  name="dealer_shortname" maxlength="30" type="text"/>
      	               </td>
					</tr>
					<tr>
						<td align="center" colspan="8">
							<input type="button" name="btnQuery" id="btnQuery" value="查询" class="normal_btn" onClick="__extQuery__(1);Querycount();" />
							&nbsp;&nbsp;&nbsp;
							<input type="button" name="bntReset" id="bntReset" value="导出" class="normal_btn" onclick="to_excel();"/>
						</td>
					</tr>
					<tr>
					<td align="center" colspan="3">
						</td>
						<td align="center" colspan="1">
						   <span style="color: red;"> 审批总金额(元)：</span>
						</td>
						<td align="center" colspan="1">
						    <input style="border: none;color: red;" readonly="readonly" type="text" id="count" name="count">
						</td>
						<td align="center" colspan="3">
						</td>
					</tr>
				</table>
<!-- 查询条件 end -->
<!--分页 begin -->
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->

<!--页面列表 begin -->
<script type="text/javascript">
	var myPage;
   //查询路径
	var url = "<%=contextPath%>/SpecialAction/findSpeciaAudit.json";
	var title = null;
    var columns = [
                {header: "序号", renderer:getIndex, align:'center'},		
			    {id:'action', width:'5%',header: "操作",renderer:myLink,align:'center'},
			    {header: "大区", dataIndex: 'ROOT_ORG_NAME', align:'center'},
				{header: "服务商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "服务商简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
			    {header: "申请单号", dataIndex: 'APPLY_NO', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "申请日期", dataIndex: 'APPLY_DATE', align:'center'},
				{header: "审核日期", dataIndex: 'AUDIT_DATE', align:'center'},
				{header: "供应商代码", dataIndex: 'SUPPLY_CODE_DEALER', align:'center'},
				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "申请类型",  align:'center',renderer:thisLink},
				{header: "申请金额",  align:'center',dataIndex: 'APPLY_AMOUNT'},
				{header: "审批金额", dataIndex: 'APPROVAL_AMOUNT', align:'center'}
	      ];
	    function thisLink(value,meta,record){
	        var SPECIAL_TYPE=record.data.SPECIAL_TYPE;
	        if("1"==SPECIAL_TYPE){
	           var type = "善意索赔";
	           return type;
	        }
	        if("0"==SPECIAL_TYPE){
	           var type = "退换车";
	           return type;
	        }
	    }
	function myLink(value,meta,record){
		var status=record.data.ID;
		 var SPECIAL_TYPE=record.data.SPECIAL_TYPE;
		var special_type=record.data.ID;
		var url="";
		var id=record.data.ID;
		var VIN=record.data.VIN;
		var status = record.data.STATUS;  
		var SPECIAL_TYPE=record.data.SPECIAL_TYPE;
		if(status=="20501002"){
		   url+="<a href='#' onclick='audit(\""+id+"\",\""+SPECIAL_TYPE+"\",\""+VIN+"\");'>[审核]</a>";
		}
		var urlView='<%=contextPath%>/SpecialAction/viewSpecialApplyDetailed.do?id='+id+"&type=AudieDetail"+"&special_type="+SPECIAL_TYPE;
		url+="<a href='"+urlView+"');'>[明细]</a>";
		return String.format(url);
	}
	//审核页面
	function audit(id,SPECIAL_TYPE,VIN){
	   window.location="<%=contextPath%>/SpecialAction/AuditReturnCar.do?id="+id+"&VIN="+VIN+"&special_type="+SPECIAL_TYPE;
	}
</script>
			<!--页面列表 end -->
</form>
</body>
</html>
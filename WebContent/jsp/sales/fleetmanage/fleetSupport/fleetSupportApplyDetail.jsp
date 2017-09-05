<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>集团客户支持</title>
<% 
   List  executePlans = (List)request.getAttribute("executePlans");
   List  attachList   = (List)request.getAttribute("attachList");
%>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;集团客户支持>集团客户支持申请>集团客户支持申请意向录入</div>
<form method="post" name = "fm" >
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;提报单位信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="4" align="left">&nbsp;经销商信息</th>
		</tr>
		<tr>
			<td class="table_query_2Col_label_7Letter">经销商名称：</td>
			<td><c:out value="${fleetMap.COMPANY_SHORTNAME}"/></td>
			<td class="table_query_2Col_label_7Letter">批售经理姓名：</td>
			<td><c:out value="${fleetMap.NAME}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_7Letter">批售经理手机：</td>
			<td><c:out value="${fleetMap.PACT_MANAGE_PHONE}"/></td>
			<td class="table_query_2Col_label_7Letter">批售经理邮箱：</td>
			<td><c:out value="${fleetMap.PACT_MANAGE_EMAIL}"/></td>
			
		</tr>
		<tr>
			<td class="table_query_2Col_label_7Letter">提报日期：</td>
			<td><c:out value="${fleetMap.SUBMIT_DATE}"/></td>
			<td></td>
			<td></td>
		</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;集团客户信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="4" align="left">&nbsp;客户信息</th>
		</tr>
		<tr>
			<td>大客户代码：</td>
			<td><c:out value="${fleetMap.FLEET_CODE}"/></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">客户名称：</td>
			<td><c:out value="${fleetMap.FLEET_NAME}"/></td>
			<td class="table_query_2Col_label_4Letter">客户类型：</td>
			<td>
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.FLEET_TYPE}"/>');
				</script>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">主营业务：</td>
			<td>
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.MAIN_BUSINESS}"/>');
				</script>
			</td>
			<td class="table_query_2Col_label_4Letter">资金规模：</td>
			<td>
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.FUND_SIZE}"/>');
				</script>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">人员规模：</td>
			<td>
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.STAFF_SIZE}"/>');
				</script>
			</td>
			<td class="table_query_2Col_label_4Letter">购车用途：</td>
			<td>
				<script type='text/javascript'>
					writeItemValue('<c:out value="${fleetMap.PURPOSE}"/>');
				</script>
			</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">区域：</td>
			<td>
				<script type='text/javascript'>
					writeRegionName('<c:out value="${fleetMap.REGION}"/>');
				</script>
			</td>
			<td class="table_query_2Col_label_4Letter">邮编：</td>
			<td><c:out value="${fleetMap.ZIP_CODE}"/></td>
		</tr>
		<tr style="display:none;">
			<td class="table_query_2Col_label_4Letter">是否批售项目：</td>
		    <td align="left"><script type='text/javascript'>
				writeItemValue('<c:out value="${fleetMap.IS_PACT}"/>');
			  </script>
			  <input type="hidden" name="isPact" id="isPact" value="${fleetMap.IS_PACT}" />
			  </td>
	      	<td class="table_query_2Col_label_4Letter" id="pactPart">批售项目名称：</td>
		    <td align="left">${fleetMap.PACT_NAME}</td>
	      </tr>
		<tr>
			<td class="table_query_2Col_label_4Letter">详细地址：</td>
			<td><c:out value="${fleetMap.ADDRESS}"/></td>
		</tr>
<!--		<tr>-->
<!--			<th colspan="4" align="left">&nbsp;联系人信息</th>-->
<!--		</tr>-->
		<tr>
			<td class="table_query_2Col_label_5Letter">主要联系人：</td>
			<td><c:out value="${fleetMap.MAIN_LINKMAN}"/></td>
			<td class="table_query_2Col_label_4Letter">职务：</td>
			<td><c:out value="${fleetMap.MAIN_JOB}"/></td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_5Letter">电话：</td>
			<td><c:out value="${fleetMap.MAIN_PHONE}"/></td>
			<td class="table_query_2Col_label_4Letter">电子邮件：</td>
			<td><c:out value="${fleetMap.MAIN_EMAIL}"/></td>
		</tr>
		<tr style="display:none;">
			<td class="table_query_2Col_label_5Letter">其他联系人：</td>
			<td><c:out value="${fleetMap.OTHER_LINKMAN}"/></td>
			<td class="table_query_2Col_label_4Letter">职务：</td>
			<td><c:out value="${fleetMap.OTHER_JOB}"/></td>
		</tr>
		<tr style="display:none;">
			<td class="table_query_2Col_label_5Letter">电话：</td>
			<td><c:out value="${fleetMap.OTHER_PHONE}"/></td>
			<td class="table_query_2Col_label_4Letter">电子邮件：</td>
			<td><c:out value="${fleetMap.OTHER_EMAIL}"/></td>
		</tr>
		<tr>
			<th colspan="4" align="left">&nbsp;需求说明</th>
		</tr>
		<tr>
		    <td align="right" class="table_query_2Col_label_5Letter">拜访时间：</td>
		    <td colspan="3"><c:out value="${fleetMap.VISIT_DATE}"/></td>
	      </tr>
<!--	      <tr>-->
<!--		    <td align="right">车系选择：</td>-->
<!--		    <td><c:out value="${fleetMap.GROUP_NAME}"/></td>-->
<!--		    <td align="right">数量：</td>-->
<!--		    <td><c:out value="${fleetMap.SERIES_COUNT}"/></td>-->
<!--	      </tr>-->
	<tr>
		<td class="table_query_2Col_label_5Letter" width="30%">市场信息：</td>
		<td colspan="3">
			${fleetMap.MARKET_INFO}
		</td>
		
    </tr>
    <tr>
		<td class="table_query_2Col_label_5Letter">配置要求：</td>
		<td colspan="3">
			${fleetMap.CONFIG_RQUIRE}
		</td>
		
    </tr>
    <tr>
		<td class="table_query_2Col_label_5Letter">大客户要求折让：</td>
		<td colspan="3">
			${fleetMap.FLEETREQ_DISCOUNT}
		</td>
		
    </tr>
    <tr>
		<td class="table_query_2Col_label_5Letter">其他竞争车型和优惠政策：</td>
		<td colspan="3">
			${fleetMap.OTHERCOMP_FAVORPOL}
		</td>
		
    </tr>
	
    <tr>
    <td colspan="4">
		<div id="showDiv" style="width:100%;">
			<table class="table_list" style="width:100%;border:1px solid #B0C4DE" id="showTab" border="1" cellpadding="1" bordercolor="#B0C4DE" >
				<tr>
					<th nowrap="nowrap" width="20%" ><center>预计车型编码</center></th>
					<th nowrap="nowrap" width="20%" ><center>预计车型名称</center></th>
					<th nowrap="nowrap" width="10%" ><center>车系</center></th>
					<th nowrap="nowrap" width="10%" ><center>预计数量</center></th>
					<th nowrap="nowrap" width="20%" ><center>说明</center></th>
				</tr>
				<c:forEach items="${tfrdMapList}" var="frdMap">
				<tr id="tr${frdMap.DETAIL_ID}">
					<td><input type="hidden" name="materialIds" value="${frdMap.MATERIAL_ID}" >${frdMap.MATERIAL_CODE}</td>
					<td>${frdMap.MATERIAL_NAME}</td>
					<td>${frdMap.GROUP_NAME}</td>
					<td> ${frdMap.AMOUNT}</td>
					<td>${frdMap.DISCRIBE}</td>
				</tr>
				</c:forEach>
			</table>
		</div>
	</td>
		<tr>
			<td class="table_query_2Col_label_5Letter">备注：</td>
			<td colspan="2" align="left"><c:out value="${fleetMap.REQ_REMARK}"/></td>
		</tr>
	</table>
	<c:if test="${checkList!=null}">
		<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;审核信息</div>
		<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<tr>
			<th colspan="5" align="left">&nbsp;审核记录</th>
			</tr>
			<tr align="center">
				<td>审核部门</td>
				<td>审核时间</td>
				<td>审核人</td>
				<td>审核意见</td>
				<td>审核结果</td>
			</tr>
			<c:forEach items="${checkList}" var="checkList">
				<tr align="center">
					<td>${checkList.ORG_NAME}</td>
					<td>${checkList.AUDIT_DATE}</td>
					<td>${checkList.USER_NAME}</td>
					<td>${checkList.AUDIT_REMARK}</td>
					<td>${checkList.CHECK_STATUS}</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;购车意向</div>
<!--	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">-->
<!--		<tr>-->
<!--			<th colspan="4" align="left">&nbsp;意向信息</th>-->
<!--		</tr>-->
<!--		<tr>-->
<!--			<td class="table_query_2Col_label_6Letter">预计采购日期：</td>-->
<!--			<td align="left">-->
<!--           		<input name="purchaseDate" id="t1" value="${intentMap.PURCHASE_DATE}" type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">-->
<!--           		&nbsp;至&nbsp;-->
<!--           		<input name="purendDate" id="t2" value="${intentMap.PUR_END_DATE}" type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">-->
<!--			</td>-->
<!--			<td class="table_query_2Col_label_6Letter">信息来源：</td>-->
<!--			<td align="left"><input type="text" name="infoGivingMan" value="${intentMap.INFO_GIVING_MAN}"/></td>-->
<!--		</tr>-->
<!--		<tr>-->
<!--			<td class="table_query_2Col_label_6Letter">竞争情况说明：</td>-->
<!--			<td  colspan="3" align="left">-->
<!--				<textarea name="competeRemark" id="competeRemark" rows="2" cols="60" datatype="0,null,300">${intentMap.COMPETE_REMARK}</textarea>-->
<!--			</td>-->
<!--		</tr>-->
<!--		<tr>-->
<!--			<td class="table_query_2Col_label_6Letter">其他说明：</td>-->
<!--			<td  colspan="3" align="left">-->
<!--				<textarea name="infoRemark" id="infoRemark" rows="2" cols="60">${intentMap.INFO_REMARK}</textarea>-->
<!--			</td>-->
<!--		</tr>-->
<!--	</table>-->
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="11" align="left" >&nbsp;商务支持信息
				<input class="cssbutton" name="add22" type="button" onclick="addNewGroup()" value ='新增' />
				<input type="text" name="materialCode" size="15" id="materialCode" style="display:none"/>
			</th>
		</tr>
		<tr align="center">
			<td width="10%" align="center">意向车系</td>
			<td width="10%" align="center">数量</td>
			<td width="10%" align="center">实际成交价</td>
			<td width="10%" align="center">启票价</td>
			<td width="10%" align="center">当前促销</td>
<!--			<td width="10%" align="center">利润</td>-->
			<td width="10%" align="center">赠送、承诺</td>
			<td width="10%" align="center">市场开拓</td>
			<td width="10%" align="center">实际利润</td>
			<td width="10%" align="center">申请支持</td>
			<td width="10%" align="center">操作</td>
		</tr>
<!--		<tbody id="tbody1">-->
<!--			<c:if test="${intentId!=null}">-->
<!--				<c:forEach items="${intentList}" var="list">-->
<!--					<tr align="center" class="table_list_row2">-->
<!--						<td>${list.GROUP_NAME}</td>-->
<!--						<td><input id="amount" name="amount" type="text" class="SearchInput"  value="${list.AMOUNT}" size="3" maxlength="6"/></td>-->
<!--						<td><input id="discount" name="discount" type="text" class="SearchInput" value="${list.DISCOUNT}" size="3" maxlength="3"/>%</td>-->
<!--						<td><input id="remark" name="remark" type="text" class="SearchInput" value="${list.REMARK}" size="40" maxlength="200"/></td>-->
<!--						<td><a href="#" onclick="delMaterial();">[删除]</a><input type='hidden' id="groupId${list.INTENT_MODEL}" name="groupId" value="${list.INTENT_MODEL}"></td>-->
<!--					</tr>-->
<!--				</c:forEach>-->
<!--			</c:if>-->
<!--		</tbody>-->
		<tbody id="tbody1">
				<c:forEach  items="${supportInfoList}" var="listMap">
					<tr align="center" >
						<td>${listMap.GROUP_NAME} </td>
						<td><input id="amount${listMap.SUPPORT_INFO_ID}" name="amount" type="text" class="SearchInput" value="${listMap.AMOUNT}"  size="3" maxlength="6" datatype="0,is_double,10"/></td>
						<td><input id="realprice${listMap.SUPPORT_INFO_ID}" onchange="changeOtherValue('${listMap.SUPPORT_INFO_ID}')" name="realprice" type="text" class="SearchInput"  value="${listMap.REAL_PRICE}" size="3" maxlength="6"  /><font color='red' size='5'> -</font></td>
						<td><input id="price${listMap.SUPPORT_INFO_ID}"  onchange="changeOtherValue('${listMap.SUPPORT_INFO_ID}')" name="price" type="text" class="SearchInput" value="${listMap.PRICE}"  size="3" maxlength="6" /><font color='red' size='3'> +</font></td>
						<td><input id="depotproprice${listMap.SUPPORT_INFO_ID}" onchange="changeOtherValue('${listMap.SUPPORT_INFO_ID}')" name="depotproprice" type="text" class="SearchInput"   value="${listMap.DEPOT_PRO_PRICE}"size="3" maxlength="6"  /><font color='red' size='5'> -</font></td>
<!--						<td><input id="profit${listMap.SUPPORT_INFO_ID}" name="profit" type="text" class="SearchInput"  value="${listMap.PROFIT}" size="3" maxlength="6" datatype="0,is_double,10" onchange="changeOtherValue('profit${listMap.SUPPORT_INFO_ID}',2)"/></td>-->
						<td><input id="gandaccept${listMap.SUPPORT_INFO_ID}"  onchange="changeOtherValue('${listMap.SUPPORT_INFO_ID}')" name="gandaccept" type="text" class="SearchInput"  value="${listMap.GIVE_AND_ACCEPT}" size="3" maxlength="6"  /><font color='red' size='5'> -</font></td>
						<td><input id="marketdevelop${listMap.SUPPORT_INFO_ID}" onchange="changeOtherValue('${listMap.SUPPORT_INFO_ID}')"  name="marketdevelop" type="text" class="SearchInput"  value="${listMap.MARKET_DEVELOP}" size="3" maxlength="6"  /><font color='red' size='3'>=</font></td>
						<td><input id="realprofit${listMap.SUPPORT_INFO_ID}" onchange="changeOtherValue('${listMap.SUPPORT_INFO_ID}')" name="realprofit"  class="SearchInput" size="3" maxlength="6"  value="${listMap.REAL_PROFIT}" disabled="disabled"/></td>

						<td><input id="requestsupport${listMap.SUPPORT_INFO_ID}" name="requestsupport" type="text" class="SearchInput"  value="${listMap.REQUEST_SUPPORT}"size="3" maxlength="200" datatype="0,is_double,10"/></td>
						<td><a href="#" onclick="delMaterial();">[删除]</a><input type='hidden' id="groupId" name="groupId"  value="${listMap.INTENT_SERIES}"></td>
					</tr>
				</c:forEach>
			</tbody>
			<tr align="left">
			<td colspan="11">备注：<textarea style="margin-left: 0px;" name="supportRemark" id="supportRemark" rows="2" cols="60" >${fleetMap.SUPPORT_REMARK}</textarea></td>
			</tr>
		</table>
		
<!--	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;意向附件</div>-->
<!--	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_info">-->
<!--		<tr>-->
<!--	        <th colspan="3" align="left">附件列表：<input type="hidden" id="fjids" name="fjids"/>-->
<!--				<span>-->
<!--					<input type="button" class="cssbutton"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>-->
<!--				</span>-->
<!--			</th>-->
<!--		</tr>-->
<!--		-->
<!--		<tr>-->
<!--			<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>-->
<!--  		</tr>-->
<!--  		<table id="attachTab" class="table_info">-->
<!--  		<% if(attachList!=null&&attachList.size()!=0){ %>-->
<!--  		<c:forEach items="${attachList}" var="attls">-->
<!--		    <tr class="table_list_row1" id="${attls.FJID}">-->
<!--		    <td><a target="_blank" href="${attls.FILEURL}">${attls.FILENAME}</a></td>-->
<!--		    <td colspan="2"><input type="button" onclick="delAttach('${attls.FJID}')" class="normal_btn" value="删 除"/></td>-->
<!--		    </tr>-->
<!--		</c:forEach>-->
<!--		<%} %>-->
<!--		</table>-->
<!--	</table>-->
		<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		
		<tr align="center">
			<td colspan="5">
				<input type="hidden" name="groupIds" id="Ids"/>
				<input type="hidden" name="prices" id="prices"/>
				<input type="hidden" name="depotproprices" id="depotproPrices"/>
<!--				<input type="hidden" name="profits" id="profits"/>-->
				<input type="hidden" name="gandaccepts" id="gandaccepts"/>
				<input type="hidden" name="marketdevelops" id="marketdevelops"/>
				<input type="hidden" name="realprices" id="realprices"/>
				<input type="hidden" name="realprofits" id="realprofits"/>
				<input type="hidden" name="requestsupports" id="requestsupports"/>
				<input type="hidden" name="amounts" id="amounts"/>
				<input type="hidden" name="delAttachs" id="delAttachs" value=""/>
				<input type="hidden" name="fleetId" id="fleetId" value="${fleetMap.FLEET_ID}"/>
				<input type="hidden" name="intentId" id="intentId" value="${intentId}"/>
				<input class="cssbutton" name="button1" type="button" onclick="toSave();" value ="保存" />
				<input class="cssbutton" name="button3" type="button" onclick="toSubmit();" value ="申请" />
				<input class="cssbutton" name="button2" type="button" onclick="toBack();" value ="返回" />
			</td>
		</tr>
	</table>
	
</form>
<script type="text/javascript"><!--
	function doInit(){
	showPactPart() ;
		loadcalendar();  //初始化时间控件
	}
	//物料弹出选择
	function addNewGroup(){
		var ids = "";
		var myForm = document.getElementById("fm");
		for (var i=0; i<myForm.length; i++){  
			var obj = myForm.elements[i];
			if(obj.id.length>=7 && obj.id.substring(0,7)=="groupId"){
				if(ids&&ids.length>0){
					ids = ids +","+ obj.value;
				}else{
					ids = obj.value;
				}
			}   
		}
		OpenHtmlWindow(g_webAppName+"/jsp/sales/fleetmanage/fleetSupport/addNewGroup.jsp?GROUPLEVEL=2&GROUPIDS="+ids,770,410);
	}
	//删除产品链接
	function delMaterial(){	
	  	document.getElementById("tbody1").deleteRow(window.event.srcElement.parentElement.parentElement.rowIndex-2);  
	}
	//新增产品列表
	function addRow(value1,value2,value3){		
		var timeValue = new Date().getTime();
		var newRow = document.getElementById("tbody1").insertRow();
		//newRow.className  = "table_list_row2";
		var newCell = newRow.insertCell(0);
		newCell.align = "center";
		newCell.innerHTML = value2;
		newCell = newRow.insertCell(1);
		newCell.align = "center";
		newCell.innerHTML = "<input id='discount"+timeValue+"' name='amount' type='text' class='SearchInput' value='' size='3' maxlength='3' datatype='0,is_double,10'/><font color='red'>*</font> ";
		newCell = newRow.insertCell(2);
		newCell.align = "center";
		newCell.innerHTML = "<input id='realprice"+timeValue+"'  onchange='changeOtherValue("+timeValue+")' name='realprice' type='text' class='SearchInput' value='' size='3' maxlength='16' datatype='0,is_double,10'/><font color='red' size='5'>-</font>";
		newCell = newRow.insertCell(3);
		newCell.align = "center";
		newCell.innerHTML = "<input id='price"+timeValue+"'  onchange='changeOtherValue("+timeValue+")' name='price' type='text' class='SearchInput' value='' size='3' maxlength='6' datatype='0,is_double,10'/><font color='red' size='3'>+</font>";
		newCell = newRow.insertCell(4);
		newCell.align = "center";
		newCell.innerHTML = "<input id='depotproprice"+timeValue+"' onchange='changeOtherValue("+timeValue+")' name='depotproprice' type='text' class='SearchInput' size='3' value='' maxlength='16' width='100%' datatype='0,is_double,10'/> <font color='red'size='5'>-</font>";
		//newCell = newRow.insertCell(5);
		//newCell.align = "center";
		//newCell.innerHTML = "<input id='profit"+timeValue+"' onchange='changeOtherValue('"+timeValue+"')' name='profit' type='text' class='SearchInput' value='' size='3' maxlength='16' width='100%' datatype='0,is_double,10'/> <font color='red'>*</font>";
		newCell = newRow.insertCell(5);
		newCell.align = "center";
		newCell.innerHTML = "<input id='gandaccept"+timeValue+"' onchange='changeOtherValue("+timeValue+")' name='gandaccept' type='text' class='SearchInput' value='' size='3' maxlength='16' datatype='0,is_double,10'/> <font color='red' size='5'>-</font>";
		newCell = newRow.insertCell(6);
		newCell.align = "center";
		newCell.innerHTML = "<input id='marketdevelop"+timeValue+"'  onchange='changeOtherValue("+timeValue+")' name='marketdevelop' type='text' class='SearchInput' value='' size='3' maxlength='16' datatype='0,is_double,10'/><font color='red' size='3'>=</font>";
		newCell = newRow.insertCell(7);
		newCell.align = "center";
		newCell.innerHTML = "<input id='realprofit"+timeValue+"' name='realprofit' type='text' class='SearchInput' value='' size='3' maxlength='16'  disabled='disabled'/>";
		newCell = newRow.insertCell(8);
		newCell.align = "center";
		newCell.innerHTML = "<input id='requestsupport"+timeValue+"' name='requestsupport' type='text' class='SearchInput' value='' size='3' maxlength='16' datatype='0,is_double,10' /> <font color='red'>*</font>";
		newCell = newRow.insertCell(9);
		newCell.align = "center";
		newCell.innerHTML = "<a href='#' onclick='delMaterial();'>[删除]</a><input type='hidden' id='groupId"+timeValue+"' name='groupId' value='"+value1+"'>";
		 addListener();
	}
	//保存校验
	function toSave(){
		/* var purchaseDate = document.getElementById("purchaseDate").value;
		var competeRemark = document.getElementById("competeRemark").value;
		var purendDate = document.getElementById("purendDate").value;
	
		if(!purchaseDate){
			MyAlert("请填写预计采购时间！");
			return false;
		}
		if(!purendDate){
			MyAlert("请填写预计采购时间!");
			return false;
		}
		if(!competeRemark){
			MyAlert("请填写竞争情况说明！");
			return false;
		}*/
		var groupIds ='';
		var prices='';
		var amounts = '';
		var depotproprices ='';
		//var profits='';
		var gandaccepts='';
		var marketdevelops='';
		var realprices='';
		var realprofits='';
		var requestsupports='';
		//var discounts ='';
		var groupId = document.getElementsByName("groupId");
		var price = document.getElementsByName("price");
		var amount = document.getElementsByName("amount");
		var depotproprice = document.getElementsByName("depotproprice");
		//var profit = document.getElementsByName("profit");
		var gandaccept = document.getElementsByName("gandaccept");
		var marketdevelop = document.getElementsByName("marketdevelop");
		var realprice = document.getElementsByName("realprice");
		var realprofit = document.getElementsByName("realprofit");
		var requestsupport = document.getElementsByName("requestsupport");
		for(var i=0 ;i< groupId.length; i++){
			/*if(remark[i].value.trim()==""){
				MyAlert("车型备注不能为空！");
				return false;
			}*/
			if(price[i].value.trim()==""){
				MyAlert("价格不能为空！");
				return false;
			}
			if(amount[i].value.trim()==""){
				MyAlert("数量不能为空！");
				return false;
			}
			if(depotproprice[i].value.trim()==""){
				MyAlert("当前销价不能为空！");
				return false;
			}
			//if(profit[i].value.trim()==""){
				//MyAlert("利润不能为空！");
				//return false;
			//}
			if(marketdevelop[i].value.trim()==""){
				MyAlert("市场开拓不能为空！");
				return false;
			}
			if(gandaccept[i].value.trim()==""){
				MyAlert("赠送、承诺不能为空！");
				return false;
			}
			if(realprice[i].value.trim()==""){
				MyAlert("实际成交价不能为空！");
				return false;
			}
			if(realprofit[i].value.trim()==""){
				MyAlert("实际利润不能为空！");
				return false;
			}
			if(requestsupport[i].value.trim()==""){
				MyAlert("申请支持不能为空！");
				return false;
			}
			/*if(discount[i].value.trim()==""){
				MyAlert("支持点位不能为空！");
				return false;
			}*/

			groupIds = groupId[i].value + ',' + groupIds;

			/*if(remark[i].value.trim()==""){
				remarks = 'null,' + remarks;
			}
			else{
				remarks = remark[i].value + ',' + remarks;
			}*/
			prices=price[i].value+','+prices
			amounts = amount[i].value + ',' + amounts;
			depotproprices = depotproprice[i].value + ',' + depotproprices;
			//profits = profit[i].value + ',' + profits;
			gandaccepts = gandaccept[i].value + ',' + gandaccepts;
			marketdevelops = marketdevelop[i].value + ',' + marketdevelops;
			realprices = realprice[i].value + ',' + realprices;
			realprofits = realprofit[i].value + ',' + realprofits;
			requestsupports = requestsupport[i].value + ',' + requestsupports;
			
			//discounts = discount[i].value + ',' + discounts;
		}
		document.getElementById("Ids").value=groupIds;
		document.getElementById("prices").value=prices;
		//document.getElementById("remarks").value=remarks;
		document.getElementById("amounts").value=amounts;
		document.getElementById("depotproprices").value=depotproprices;
	//	document.getElementById("profits").value=profits;
		document.getElementById("gandaccepts").value=gandaccepts;
		document.getElementById("marketdevelops").value=marketdevelops;
		document.getElementById("realprices").value=realprices;
		document.getElementById("realprofits").value=realprofits;
		document.getElementById("requestsupports").value=requestsupports;
		//document.getElementById("discounts").value=discounts;
		MyConfirm("确认保存？",toAdd);
	}
	//保存提交
	function toAdd(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetSupportApply/fleetSupportApplySave.json',showResult,'fm');
	}
	//申请校验
	function toSubmit(){
		/*var purchaseDate = document.getElementById("purchaseDate").value;
		var competeRemark = document.getElementById("competeRemark").value;
		var purendDate = document.getElementById("purendDate").value;
		var newRow = document.getElementById("tbody1").childNodes.length;
		if(newRow<1){
			MyAlert("请填写意向车型！");
			return false;
		}
		if(!purchaseDate){
			MyAlert("请填写预计采购时间！");
			return false;
		}
		if(!purendDate){
			MyAlert("请填写预计采购时间！");
			return false;
		}
		if(!competeRemark){
			MyAlert("请填写竞争情况说明！");
			return false;
		}
		var groupIds ='';
		var remarks ='';
		var amounts = '';
		var discounts ='';
		var groupId = document.getElementsByName("groupId");
		var remark = document.getElementsByName("remark");
		var amount = document.getElementsByName("amount");
		var discount = document.getElementsByName("discount");
		for(var i=0 ;i< groupId.length; i++){
			if(remark[i].value.trim()==""){
				MyAlert("车型备注不能为空！");
				return false;
			}
			if(amount[i].value.trim()==""){
				MyAlert("数量不能为空！");
				return false;
			}
			if(discount[i].value.trim()==""){
				MyAlert("支持点位不能为空！");
				return false;
			}
			groupIds = groupId[i].value + ',' + groupIds;
			if(remark[i].value.trim()==""){
				remarks = 'null,' + remarks;
			}
			else{
				remarks = remark[i].value + ',' + remarks;
			}
			amounts = amount[i].value + ',' + amounts;
			discounts = discount[i].value + ',' + discounts;
		}
		document.getElementById("Ids").value=groupIds;
		document.getElementById("remarks").value=remarks;
		document.getElementById("amounts").value=amounts;
		document.getElementById("discounts").value=discounts;**/
		
		var groupIds ='';
		var prices='';
		var amounts = '';
		var depotproprices ='';
		//var profits='';
		var gandaccepts='';
		var marketdevelops='';
		var realprices='';
		var realprofits='';
		var requestsupports='';
		//var discounts ='';
		var groupId = document.getElementsByName("groupId");
		var price = document.getElementsByName("price");
		var amount = document.getElementsByName("amount");
		var depotproprice = document.getElementsByName("depotproprice");
		//var profit = document.getElementsByName("profit");
		var gandaccept = document.getElementsByName("gandaccept");
		var marketdevelop = document.getElementsByName("marketdevelop");
		var realprice = document.getElementsByName("realprice");
		var realprofit = document.getElementsByName("realprofit");
		var requestsupport = document.getElementsByName("requestsupport");
		for(var i=0 ;i< groupId.length; i++){
			/*if(remark[i].value.trim()==""){
				MyAlert("车型备注不能为空！");
				return false;
			}*/
			if(price[i].value.trim()==""){
				MyAlert("价格不能为空！");
				return false;
			}
			if(amount[i].value.trim()==""){
				MyAlert("数量不能为空！");
				return false;
			}
			if(depotproprice[i].value.trim()==""){
				MyAlert("车厂促销价不能为空！");
				return false;
			}
			//if(profit[i].value.trim()==""){
			//	MyAlert("利润不能为空！");
			//	return false;
			//}
			if(marketdevelop[i].value.trim()==""){
				MyAlert("市场开拓不能为空！");
				return false;
			}
			if(gandaccept[i].value.trim()==""){
				MyAlert("赠送、承诺不能为空！");
				return false;
			}
			if(realprice[i].value.trim()==""){
				MyAlert("实际成交价不能为空！");
				return false;
			}
			if(realprofit[i].value.trim()==""){
				MyAlert("实际利润不能为空！");
				return false;
			}
			if(requestsupport[i].value.trim()==""){
				MyAlert("申请支持不能为空！");
				return false;
			}
			/*if(discount[i].value.trim()==""){
				MyAlert("支持点位不能为空！");
				return false;
			}*/

			groupIds = groupId[i].value + ',' + groupIds;

			/*if(remark[i].value.trim()==""){
				remarks = 'null,' + remarks;
			}
			else{
				remarks = remark[i].value + ',' + remarks;
			}*/
			prices=price[i].value+','+prices
			amounts = amount[i].value + ',' + amounts;
			depotproprices = depotproprice[i].value + ',' + depotproprices;
			//profits = profit[i].value + ',' + profits;
			gandaccepts = gandaccept[i].value + ',' + gandaccepts;
			marketdevelops = marketdevelop[i].value + ',' + marketdevelops;
			realprices = realprice[i].value + ',' + realprices;
			realprofits = realprofit[i].value + ',' + realprofits;
			requestsupports = requestsupport[i].value + ',' + requestsupports;
			
			//discounts = discount[i].value + ',' + discounts;
		}
		document.getElementById("Ids").value=groupIds;
		document.getElementById("prices").value=prices;
		//document.getElementById("remarks").value=remarks;
		document.getElementById("amounts").value=amounts;
		document.getElementById("depotproprices").value=depotproprices;
		//document.getElementById("profits").value=profits;
		document.getElementById("gandaccepts").value=gandaccepts;
		document.getElementById("marketdevelops").value=marketdevelops;
		document.getElementById("realprices").value=realprices;
		document.getElementById("realprofits").value=realprofits;
		document.getElementById("requestsupports").value=requestsupports;
		MyConfirm("确认申请？",toConfirm);
	}
	//申请提交
	function toConfirm(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetSupportApply/fleetSupportApplyConfirm.json',showResult,'fm');
	}
	//返回
	function toBack(){
		$('fm').action='<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetSupportApply/fleetSupportApplyInit.do';
		$('fm').submit();
	}
	//回调方法
	function showResult(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("操作成功！");
			$('fm').action='<%=request.getContextPath()%>/sales/fleetmanage/fleetSupport/FleetSupportApply/fleetSupportApplyInit.do';
			$('fm').submit();
		}else{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	
	function showPactPart() {
		var iIsPact = document.getElementById("isPact").value ;
		var oPactPart = document.getElementById("pactPart") ;
		
		if(iIsPact == <%=Constant.IF_TYPE_YES%>) {
			oPactPart.style.display = "inline" ;
		} else {
			oPactPart.style.display = "none" ;
		}
	}
	function delAttach(value){
	    var fjId = value;
	    var delAttachs = document.getElementById("delAttachs").value;
	    document.getElementById(value).style.display = "none";
	    delAttachs = delAttachs + "," + fjId;
	    document.getElementById("delAttachs").value = delAttachs;
	}
	//分录修改操作
	 function  changeOtherValue(timeValue){
	 	//公式：实际成交价-启票价+当前促销-赠送承诺-市场开拓=实际利润
	 	var realpriceId="realprice"+timeValue;//实际成交价ID
	 	var priceId="price"+timeValue;//启票价ID
	 	var depotpropriceId="depotproprice"+timeValue;//当前促销ID
	 	var gandacceptId="gandaccept"+timeValue;//赠送承诺ID
	 	var marketdevelopId="marketdevelop"+timeValue;//市场开拓ID
	 	var realprofitId="realprofit"+timeValue;//实际利润ID
		var a=0.0;
		var b=0.0;
		var c=0.0;
		var d=0.0;
		var e=0.0;
		var total=0.0;
		a=$(realpriceId).value;
		b=$(priceId).value;
		c=$(depotpropriceId).value;
		d=$(gandacceptId).value;
		e=$(marketdevelopId).value;
		a=checkIsNull(a);
		b=checkIsNull(b);
		c=checkIsNull(c);
		d=checkIsNull(d);
		e=checkIsNull(e);
		//如果转换后和转换前的不同就说明不是数字
		if(!checkIsMoneyType(a)){
			MyAlert("输入的数据不是金额类型！！");
			$(realpriceId).value="0.0";
			return;
		}
		if(!checkIsMoneyType(b)){
			MyAlert("输入的数据不是金额类型！！");
			$(priceId).value="0.0";
			return;
		}
		if(!checkIsMoneyType(c)){
			MyAlert("输入的数据不是金额类型！！");
			$(depotpropriceId).value="0.0"
			return;
		}
		if(!checkIsMoneyType(d)){
			MyAlert("输入的数据不是金额类型！！");
			$(gandacceptId).value="0.0";
			return;
		}
		if(!checkIsMoneyType(e)){
			MyAlert("输入的数据不是金额类型！！");
			$(marketdevelopId).value="0.0"
			return;
		}
		total=parseFloat(a)-parseFloat(b)+parseFloat(c)-parseFloat(d)-parseFloat(e);
		$(realprofitId).value=total;
	}
	//处理为空的数据
	function checkIsNull(value){
		if(value==""||null==value){
			return "0.0";
		}
		return value;
	}
	//校验值是否是金额类型如果是返回true不是返回false
	function checkIsMoneyType(value){
		var a=value;
		var b=parseFloat(value);
		if(a!=b){
			return false;
		}else{
			return true;
		}
	}
	</script>
</body>
</html>
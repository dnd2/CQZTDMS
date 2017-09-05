<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>活动执行方案提报</title>
<% String contextPath = request.getContextPath();  
List  attachList   = (List)request.getAttribute("attachList");
%>

<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 市场活动管理 &gt; 活动方案管理&gt; 活动执行方案提报</div>
<form method="post" name="fm" id="fm">
<input type="hidden" value="${amountList1}" id="amountList1" name="amountList1"></input>
<input type="hidden" value="${amountList2}" id="amountList2" name="amountList2"></input>
<input type="hidden" value="${amountList3}" id="amountList3" name="amountList3"></input>
<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;业务范围</div>
	<table class="table_query" align="center">
		<tr class= "tabletitle">
			<th align = "right" width="10%">选择业务范围：</th>
			<th align = "left" >
				<select name="areaId" class="short_sel" onchange="" disabled="disabled">
					<c:forEach items="${areaList}" var="po">
						<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
					</c:forEach>
				</select>
				<input type="hidden" name="dealerId" id="dealerId" />
				<input type="hidden" name="area_id" id="area_id" value="" />
				
			</th>
		</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;方案信息</div>
	<table id="table1" width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="5" align="left">&nbsp;方案录入</th>
		</tr>
		<tr>
			<td align="right">品牌：</td>
			<td align="left">
				<select name="groupId" id="groupId" class="short_sel" onchange="toClear();" disabled="disabled">
					<c:forEach items="${list}" var="list">
						<option value="${list.GROUP_ID}" <c:if test="${list.GROUP_ID==map2.GROUP_ID}">selected</c:if>>${list.GROUP_NAME}</option>
					</c:forEach>
				</select>
			</td>
			<td align="right">活动车型：</td>
			<td align="left">
				<input type="text" name="campaignModel" id="campaignModel" readonly="readonly" datatype="0,is_null,1000" size="30"/>
				<input type="hidden" name="modelId" id="modelId"/>
				<!--<input type="button" name="selectbutton" id="selectbutton" class="mini_btn" value="..." onclick="showMaterialGroupToModel('campaignModel','modelId','true','3','true');"/>
				-->
				<input name="button3" type="button" disabled="disabled" class="mini_btn" onclick="showMaterialGroup_market('groupCode','','true','')" value="..." />
				<input type="button" name="clearbutton" id="clearbutton" class="cssbutton" value="清除" onClick="toClear();"/>
				<input type="hidden" name="area_id" id="area_id" value="" />
			</td>
			<td width="20%"></td>
		</tr>
		<tr>
			<td align="right">车厂方案编号：</td>
			<td align="left">
				<input readOnly="readonly" type="text" name="campaignNo" id="campaignNo" value="${map2.CAMPAIGN_NO}" datatype="0,is_digit_letter,17" <c:if test="${map2.CAMPAIGN_TYPE==11251001}">disabled="disabled"</c:if>/>
			</td>
			<td align="right">方案名称：</td>
			<td align="left">
				<input  readOnly="readonly" type="text" name="campaignName" id="campaignName" value="${map2.CAMPAIGN_NAME}" datatype="0,is_textarea,50" size="30" maxlength="50" <c:if test="${map2.CAMPAIGN_TYPE==11251001}">disabled="disabled"</c:if>/>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">计划开始日期：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="startDate" name="startDate" value="${map2.START_DATE}" group="startDate,endDate" datatype="0,is_date,10" <c:if test="${map2.CAMPAIGN_TYPE==11251001}">disabled="disabled"</c:if>/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value="&nbsp;" <c:if test="${map2.CAMPAIGN_TYPE==11251001}">disabled="disabled"</c:if>/>
			</td>
			<td align="right">计划结束日期：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="endDate" name="endDate" value="${map2.END_DATE}" group="startDate,endDate" datatype="0,is_date,10" <c:if test="${map2.CAMPAIGN_TYPE==11251001}">disabled="disabled"</c:if>/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" <c:if test="${map2.CAMPAIGN_TYPE==11251001}">disabled="disabled"</c:if>/>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">活动主题：</td>
			<td colspan="4" align="left">
				<input type="text" readOnly="readonly"  name="campaignSubject" id="campaignSubject" value="${map2.CAMPAIGN_SUBJECT}" datatype="0,is_textarea,50" size="90" maxlength="50" <c:if test="${map2.CAMPAIGN_TYPE==11251001}">disabled="disabled"</c:if>/>
			</td>
		</tr>
		<tr>
			<td align="right">活动对象：</td>
			<td colspan="4" align="left">
				<input type="text"  readOnly="readonly" name="campaignObject" id="campaignObject" value="${map2.CAMPAIGN_OBJECT}" size="90" maxlength="50" <c:if test="${map2.CAMPAIGN_TYPE==11251001}">disabled="disabled"</c:if>/>
			</td>
		</tr>
		<tr>
			<td align="right">活动目的：</td>
			<td colspan="4" align="left">
				<textarea name="campaignPurpose" readOnly="readonly"  id="campaignPurpose" rows="4" cols="70" <c:if test="${map2.CAMPAIGN_TYPE==11251001}">disabled="disabled"</c:if>>${map2.CAMPAIGN_PURPOSE}</textarea>
			</td>
		</tr>
		<tr>
			<td align="right">活动要求：</td>
			<td colspan="4" align="left">
				<textarea name="campaignNeed" readOnly="readonly"  id="campaignNeed" rows="4" cols="70" <c:if test="${map2.CAMPAIGN_TYPE==11251001}">disabled="disabled"</c:if>>${map2.CAMPAIGN_NEED}</textarea>
			</td>
		</tr>
		<tr>
			<td align="right">活动主要内容：</td>
			<td colspan="4" align="left">
				<textarea name="campaignDesc" id="campaignDesc" rows="4" cols="70" <c:if test="${map2.CAMPAIGN_TYPE==11251001}"> disabled="disabled"</c:if>>${map2.CAMPAIGN_DESC}</textarea>
			</td>
		</tr>
		<tr>
			<th colspan="5" align="left">&nbsp;区域说明：</th>
		</tr>
		<tr>
			<td align="right">活动地点说明：</td>
			<td colspan="4" align="left">
				<textarea name="execAddDesc" id="execAddDesc"  disabled="disabled" rows="4" cols="70">${map2.EXEC_ADD_DESC}</textarea>
			</td>
		</tr>
		<tr>
			<td align="right">区域建议及整改意见：：</td>
			<td colspan="4" align="left">
				<textarea name="adviceDesc" id="adviceDesc"  rows="4"  disabled="disabled" cols="70">${map2.ADVICE_DESC}</textarea>
			</td>
		</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;执行方案信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="5" align="left">&nbsp;目标录入</th>
		</tr>
		<tr>
			<td align="center">项目</td>
			<td align="center">目标</td>
		</tr>
		<tr class="table_list_row2">
			<td align="center">来电来店数</td>
			<td align="center"><input type="text" name="callsHousesCntTgt" id="callsHousesCntTgt" datatype=”1,is_digit,6” value="${map3.CALLS_HOUSES_CNT_TGT}"/></td>
		</tr>
		<tr class="table_list_row2">
			<td align="center">信息留存量</td>
			<td align="center"><input type="text" name="reserveCntTgt" id="reserveCntTgt" datatype=”1,is_digit,6” value="${map3.RESERVE_CNT_TGT}"/></td>
		</tr>
		<tr class="table_list_row2">
			<td align="center">启票数</td>
			<td align="center"><input type="text" name="orderCntTgt" id="orderCntTgt" datatype=”1,is_digit,6” value="${map3.ORDER_CNT_TGT}"/></td>
		</tr>
		<tr class="table_list_row2">
			<td align="center">实销数</td>
			<td align="center"><input type="text" name="deliveryCntTgt" id="deliveryCntTgt" datatype=”1,is_digit,6” value="${map3.DELIVERY_CNT_TGT}"/></td>
		</tr>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="12" align="left">&nbsp;可用费用
			</th>
		</tr>
		<tr class="table_list_row2">
			<td align="center">经销商可用推广费用</td>
			<td align="center">区域可用推广费用</td>
		</tr>
		<tr class="table_list_row2">
			<td align="center"><span id='availableAmount1'></span><input type="hidden" name="availableAmount5" id="availableAmount5" value=""/></td>
			<td align="center"><span id='availableAmount2'></span><input type="hidden" name="availableAmount6" id="availableAmount6" value=""/></td>
		</tr>
	</table>
	<a href="anchor_B"></a>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="10" align="left">&nbsp;活动费用录入
				<input class="cssbutton" name="add22" type="button" onclick="addTbody1();" value ='新增' />
			</th>
		</tr>
		<tr>
			<td align="center">费用类型</td>
			<td align="center">费用来源</td>
			<td align="center">活动类别</td>
			<td align="center">活动主题</td>
			<td align="center">项目明细</td>
			<td align="center">规格/单位</td>
			<td align="center">项目单价（元）</td>
			<td align="center">项目数量</td>
			<td align="center">计划费用（元）</td>
			<td align="center">操作</td>
		</tr>
		<tbody id="tbody1">
		<c:if test="${list1!=null}">
			<c:forEach items="${list1}" var="list1">
				<tr class="table_list_row2" align="center">
					<td>
						<label>
							<script type="text/javascript">
								genSelBoxExp("costType1"+${list1.COST_ID},<%=Constant.COST_TYPE%>,"${list1.COST_TYPE}",'',"","onchange=toChange1("+${list1.COST_ID}+")","false",'');
							</script>
						</label>
					</td>
					<td>
						<label>
							<script type="text/javascript">
								genSelBoxExp("costSource1",<%=Constant.COST_SOURCE%>,"${list1.COST_ACCOUNT}",'',"","","false",'');
							</script>
						</label>
					</td>
					<td>
						<label>
							<script type="text/javascript">
								genSelBoxExp("campaignType",<%=Constant.COSTACTIVITY_TYPE%>,"${list1.ACTIVITY_TYPE}",'',"","","false",'');
							</script>
						</label>
					</td>
					<td>
						<input id="itemContent${list1.COST_ID}" name="itemContent" type="text" value="${list1.ACTIVITY_CONTENT}" size="20" maxlength="50"/>
					</td>
					<td>
						<input id="itemName${list1.COST_ID}" name="itemName" type="text" value="${list1.ITEM_NAME}" size="20" maxlength="15"/>
					</td>
					<td>
						<input id="itemRemark${list1.COST_ID}" name="itemRemark" type="text" value="${list1.ITEM_REMARK}" size="20" maxlength="50"/>
					</td>
					<td>
						<input id="itemPrice${list1.COST_ID}" name="itemPrice" type="text" value="${list1.ITEM_PRICE}" onBlur="toChangeNum1('${list1.COST_ID}');" size="10" maxlength="10"/>
					</td>
					<td>
						<input id="itemCount${list1.COST_ID}" name="itemCount" type="text" value="${list1.ITEM_COUNT}" onBlur="toChangeNum1('${list1.COST_ID}');" size="6" maxlength="6"/>
					</td>
					<td>
						<span id="planCost1${list1.COST_ID}">${list1.PLAN_COST}</span><input id="cost1${list1.COST_ID}" name="planCost1" type="hidden" value="${list1.PLAN_COST}"/>
					</td>
					<td>
						<a href="#anchor_B" onclick="delTbody1();">[删除]</a>
					</td>
				</tr>
			</c:forEach>
			</c:if>
		</tbody>
		<tr>
			<td colspan="6" align="right"><strong>费用小计：</strong></td>
			<td align="center"><strong><span id="totalPrice1">0</span></strong></td>
			<td align="left"></td>
		</tr>
	</table>
	<a NAME="anchor_A"></a>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<th colspan="11" align="left">&nbsp;媒体投放费用录入
				<input class="cssbutton" name="add11" type="button" onclick="addTbody2();" value ='新增' />
			</th>
		</tr>
		<tr>
			<td align="center">费用类型</td>
			<td align="center">费用来源</td>
			<td align="center">广告日期</td>
			<td align="center">广告主题</td>
			<td align="center">媒体</td>
			<td align="center">规格/版式大小/电视片时间</td>
			<td align="center">结算价格</td>
			<td align="center">频次/数量</td>
			<td align="center">计划费用</td>
			<td align="center">操作</td>
		</tr>
		<tbody id="tbody2">
		<c:if test="${list2!=null}">
			<c:forEach items="${list2}" var="list2">
				<tr class="table_list_row2" align="center">
					<td>
						<label>
							<script type="text/javascript">
								genSelBoxExp("costType2"+${list2.COST_ID},<%=Constant.COST_TYPE%>,"${list2.COST_TYPE}",'',"","onchange=toChange2("+${list2.COST_ID}+")","false",'');
							</script>
						</label>
					</td>
						<td>
						<label>
							<script type="text/javascript">
								genSelBoxExp("costSource2",<%=Constant.COST_SOURCE%>,"${list2.PAYMENT_ACCOUNT}",'',"","","false",'');
							</script>
						</label>
					</td>
					<td>
						<input name="advDate" class="short_txt" id="advDate${list2.COST_ID}" value="${list2.ADV_DATE}" type="text" datatype="1,is_date,10"/> 
						<input class="time_ico" type="button" onClick="showcalendar(event,'advDate${list2.COST_ID}',false);" value="&nbsp;"/>
					</td>
					<td>
						<input id="advSubject${list2.COST_ID}" name="advSubject" type="text" value="${list2.ADV_SUBJECT}" size="10" maxlength="15"/>
					</td>
					<td>
						<input id="advMedia${list2.COST_ID}" name="advMedia" type="text" value="${list2.ADV_MEDIA}" size="15" maxlength="50"/>
					</td>
					<td>
						<input id="mediaSize${list2.COST_ID}" name="mediaSize" type="text" value="${list2.REMARK}" size="20" maxlength="15"/>
					</td>
					<td>
						<input id="mediaPrice${list2.COST_ID}" name="mediaPrice" type="text" value="${list2.ITEM_PRICE}" size="10" maxlength="10" onBlur="toChangeNum2('${list2.COST_ID}');"/>
					</td>
					<td>
						<input id="mediaCount${list2.COST_ID}" name="mediaCount" type="text" value="${list2.ITEM_COUNT}" size="3" maxlength="6" onBlur="toChangeNum2('${list2.COST_ID}');"/>
					</td>
					<td>
						<span id="planCost2${list2.COST_ID}">${list2.PLAN_COST}</span><input id="cost2${list2.COST_ID}" name="planCost2" type="hidden" value="${list2.PLAN_COST}"/>
					</td>
					<td>
						<a href="#anchor_A" onclick="delTbody2();">[删除]</a>
					</td>
				</tr>
			</c:forEach>
			</c:if>
		</tbody>
		<tr>
			<td colspan="8" align="right"><strong>费用小计：</strong></td>
			<td align="center"><strong><span id="totalPrice2">0</span></strong></td>
			<td align="left"></td>
		</tr>
	</table>
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;审批信息</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
	        <th colspan="7">&nbsp;审批记录</th>
		</tr>
		<tr align="center">
			<td>审批部门</td>
			<td>审批人员</td>
			<td>审批状态</td>
			<td>审批意见</td>
			<td>审批时间</td>
			<td>审批附件</td>
		</tr>
		<c:forEach items="${list3}" var="list3">
			<tr class="table_list_row2" align="center">
				<td>${list3.ORG_NAME}</td>
				<td>${list3.NAME}</td>
				<td>
					<script type="text/javascript">
						writeItemValue('${list3.CHECK_STATUS}');
					</script>
				</td>
				<td>${list3.CHECK_DESC}</td>
				<td>${list3.CHECK_DATE}</td>
				<td><a target="_blank" href="${list3.FILEURL}">${list3.FILENAME}</a></td>
			</tr>
		</c:forEach>
	</table>
	<!-- 添加附件start -->
	<div><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;方案附件</div>
	<table class="table_info" border="0" id="file">
	    <tr>
	        <th>附件列表：<input type="hidden" id="fjids" name="fjids"/>
				<span>
					<input type="button" class="cssbutton"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
				</span>
			</th>
		</tr>
		<tr>
			<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
	</table>
	<table id="attachTab" class="table_info">
  		<% if(attachList!=null&&attachList.size()!=0){ %>
  		<c:forEach items="${attachList}" var="attls">
		    <tr class="table_list_row1" id="${attls.FJID}">
		    <td><a target="_blank" href="${attls.FILEURL}">${attls.FILENAME}</a></td>
		    <td><input type=button onclick="delAttach('${attls.FJID}')" class="normal_btn" value="删 除"/></td>
		    </tr>
		</c:forEach>
		<%} %>
		</table>
	<!-- 添加附件end -->
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
			<td colspan="4" align="center">
				<input type="hidden" name="campaignId" value="${map2.CAMPAIGN_ID}" />
				<input type="hidden" name="dealerId" value="${map2.DEALRE_ID}" />
				<input type="hidden" name="executeId" value="${map2.EXECUTE_ID}" />
				<input type="hidden" name="targetId" value="${map3.TARGET_ID}" />
				<input type="hidden" name="costTypes1" id="costTypes1"/>
				<input type="hidden" name="itemNames" id="itemNames"/>
				<input type="hidden" name="itemRemarks" id="itemRemarks"/>
				<input type="hidden" name="itemPrices" id="itemPrices"/>
				<input type="hidden" name="itemCounts" id="itemCounts"/>
				<input type="hidden" name="costSources1" id="costSources1"/>
				<input type="hidden" name="planCosts1" id="planCosts1"/>
				<input type="hidden" name="costTypes2" id="costTypes2"/>
				<input type="hidden" name="advDates" id="advDates"/>
				<input type="hidden" name="advSubjects" id="advSubjects"/>
				<input type="hidden" name="advMedias" id="advMedias"/>
				<input type="hidden" name="mediaSizes" id="mediaSizes"/>
				<input type="hidden" name="mediaPrices" id="mediaPrices"/>
				<input type="hidden" name="mediaCounts" id="mediaCounts"/>
				<input type="hidden" name="costSources2" id="costSources2"/>
				<input type="hidden" name="planCosts2" id="planCosts2"/>
				<input type="button" class="cssbutton" name="button3" onClick="toSave();" value="保存"/>
				<input type="button" class="cssbutton" name="button5" onClick="toSubmit();" value="提报"/>
				<input type="button" class="cssbutton" name="button4" onClick="toBack();" value="返回"/>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	//页面初始化
	function doInit(){
		totalPrice();
		loadcalendar();
		document.getElementById("modelId").value="${group_id}";
		document.getElementById("campaignModel").value="${group_code}";
	}
	//设置业务范围ID,经销商ID
	function getDealerAreaId(arg){
		var areaObj = document.getElementById("areaId");
		var areaId = areaObj.value.split("|")[0];
		var dealerId = areaObj.value.split("|")[1];
		document.getElementById("dealerId").value = dealerId;
		getAvailableAmount();
	}
	//选择活动车型
	function toSelect(){
		OpenHtmlWindow('<%=contextPath%>/sales/marketmanage/planissued/ActivitiesPlanMake/queryModelInit.do',800,450);
	}
	//清除功能
	function toClear(){
		document.getElementById("campaignModel").value="";
		document.getElementById("modelId").value="";
	}
	//设置可用费用
	function getAvailableAmount(){
		var dealerId = document.getElementById("dealerId").value;
        	var availableAmount1=${amountList1}
       	    document.getElementById("availableAmount1").innerText= amountFormat(availableAmount1);
       	    document.getElementById("availableAmount5").value= availableAmount1;
	       	var availableAmount2=${amountList2}
	      	document.getElementById("availableAmount2").innerText= amountFormat(availableAmount2);
	      	document.getElementById("availableAmount6").value= availableAmount2;
	}
	//新增列表1
	function addTbody1(){
		var timeValue = new Date().getTime();
		var newRow = document.getElementById("tbody1").insertRow();
		newRow.className  = "table_list_row2";
		var newCell = newRow.insertCell(0);
		newCell.align = "center";
		newCell.innerHTML = genSelBoxStrExp("costType1"+timeValue,<%=Constant.COST_TYPE%>,"",false,"","onchange=toChange1("+timeValue+")","false",'');
		newCell = newRow.insertCell(1);
		newCell.align = "center";
		newCell.innerHTML = genSelBoxStrExp("costSource1",<%=Constant.COST_SOURCE%>,"",false,"","","false",'');
		newCell = newRow.insertCell(2);
		newCell.align = "center";
		newCell.innerHTML = genSelBoxStrExp("campaignType",<%=Constant.COSTACTIVITY_TYPE%>,"",false,"","","false",'');
		newCell = newRow.insertCell(3);
		newCell.align = "center";
		newCell.innerHTML = "<input id='itemContent"+timeValue+"' name='itemContent' type='text' size='25' maxlength='50'/>";
		newCell = newRow.insertCell(4);
		newCell.align = "center";
		newCell.innerHTML = "<input id='itemName"+timeValue+"' name='itemName' type='text' size='20' maxlength='15'/>";
		newCell = newRow.insertCell(5);
		newCell.align = "center";
		newCell.innerHTML = "<input id='itemRemark"+timeValue+"' name='itemRemark' type='text' size='15' maxlength='50'/>";
		newCell = newRow.insertCell(6);
		newCell.align = "center";
		newCell.innerHTML = "<input id='itemPrice"+timeValue+"' name='itemPrice' type='text' size='10' maxlength='10' onBlur='toChangeNum1(\""+timeValue+"\");'/>";
		newCell = newRow.insertCell(7);
		newCell.align = "center";
		newCell.innerHTML = "<input id='itemCount"+timeValue+"' name='itemCount' type='text' size='6' maxlength='6' onBlur='toChangeNum1(\""+timeValue+"\");'/>";
		newCell = newRow.insertCell(8);
		newCell.align = "center";
		newCell.innerHTML = "<span id='planCost1"+timeValue+"'>0</span><input id='cost1"+timeValue+"' name='planCost1' type='hidden' value='0'/>";
		newCell = newRow.insertCell(9);
		newCell.align = "center";
		newCell.innerHTML = "<a href='#anchor_B' onclick='delTbody1();'>[删除]</a>";
	}
	//计划费用1
	function toChangeNum1(value1){
		var count = document.getElementById("itemCount"+value1).value;
		var price = document.getElementById("itemPrice"+value1).value;
		var planCost = Number(count)*Number(price);
		document.getElementById("planCost1"+value1).innerText=amountFormat(planCost);
		document.getElementById("cost1"+value1).value = planCost;
		var planCost = document.getElementsByName("planCost1");
		var totalPrice = 0;
		for (var i=0; i<planCost.length; i++){  
			totalPrice += Number(planCost[i].value);  
		}
		document.getElementById("totalPrice1").innerText = amountFormat(totalPrice);
	}
	//费用合计
	function totalPrice(){
		var planCost1 = document.getElementsByName("planCost1");
		var planCost2 = document.getElementsByName("planCost2");
		var totalPrice1 = 0;
		var totalPrice2 = 0;
		for (var i=0; i<planCost1.length; i++){  
			totalPrice1 += Number(planCost1[i].value);  
		}
		for (var i=0; i<planCost2.length; i++){  
			totalPrice2 += Number(planCost2[i].value);  
		}
		document.getElementById("totalPrice1").innerText = amountFormat(totalPrice1);
		document.getElementById("totalPrice2").innerText = amountFormat(totalPrice2);
	}
	//删除列表1
	function delTbody1(){	
	  	document.getElementById("tbody1").deleteRow(window.event.srcElement.parentElement.parentElement.rowIndex-2);  
	}
	function delAttach(value){
  		var fjId = value;
  		var delAttachs = document.getElementById("delAttachs").value;
		document.getElementById(value).style.display="none";
		delAttachs=delAttachs+","+fjId;
		document.getElementById("delAttachs").value=delAttachs;
	}
	//新增列表2
	function addTbody2(){
		loadcalendar();
		var timeValue = new Date().getTime();
		var newRow = document.getElementById("tbody2").insertRow();
		newRow.className  = "table_list_row2";
		var newCell = newRow.insertCell(0);
		newCell.align = "center";
		newCell.innerHTML = genSelBoxStrExp("costType2"+timeValue,<%=Constant.COST_TYPE%>,"",false,"","onchange=toChange2("+timeValue+")","false",'');
		newCell = newRow.insertCell(1);
		newCell.align = "center";
		newCell.innerHTML = genSelBoxStrExp("costSource2",<%=Constant.COST_SOURCE%>,"",false,"","","false",'');
		newCell = newRow.insertCell(2);
		newCell.align = "center"; 
		newCell.innerHTML = "<input name='advDate' class='short_txt' id='advDate"+timeValue+"' type='text' readOnly='readOnly' datatype=\"1,is_date,10\"/> <input class='time_ico' type='button' onClick='showcalendar(event,\"advDate"+timeValue+"\",false);' value='&nbsp;'/>";
		newCell = newRow.insertCell(3);
		newCell.align = "center";
		newCell.innerHTML = "<input id='advSubject"+timeValue+"' name='advSubject' type='text' size='10' maxlength='15'/>";
		newCell = newRow.insertCell(4);
		newCell.align = "center";
		newCell.innerHTML = "<input id='advMedia"+timeValue+"' name='advMedia' type='text' size='15' maxlength='50'/>";
		newCell = newRow.insertCell(5);
		newCell.align = "center";
		newCell.innerHTML = "<input id='mediaSize"+timeValue+"' name='mediaSize' type='text' size='20' maxlength='15'/>";
		newCell = newRow.insertCell(6);
		newCell.align = "center";
		newCell.innerHTML = "<input id='mediaPrice"+timeValue+"' name='mediaPrice' type='text' size='10' maxlength='10' onBlur='toChangeNum2(\""+timeValue+"\");'/>";
		newCell = newRow.insertCell(7);
		newCell.align = "center";
		newCell.innerHTML = "<input id='mediaCount"+timeValue+"' name='mediaCount' type='text' size='3' maxlength='6' onBlur='toChangeNum2(\""+timeValue+"\");'/>";
		newCell = newRow.insertCell(8);
		newCell.align = "center";
		
		newCell.innerHTML = "<span id='planCost2"+timeValue+"'>0</span><input id='cost2"+timeValue+"' name='planCost2' type='hidden' value='0'/>";
		newCell = newRow.insertCell(9);
		newCell.align = "center";
		newCell.innerHTML = "<a href='#anchor_A' onclick='delTbody2();'>[删除]</a>";
	}
	//计划费用2
	function toChangeNum2(value1){
		var count = document.getElementById("mediaPrice"+value1).value;
		var price = document.getElementById("mediaCount"+value1).value;
		var planCost = Number(count)*Number(price);
		document.getElementById("planCost2"+value1).innerText=amountFormat(planCost);
		document.getElementById("cost2"+value1).value = planCost;
		var planCost = document.getElementsByName("planCost2");
		var totalPrice = 0;
		for (var i=0; i<planCost.length; i++){  
			totalPrice += Number(planCost[i].value);  
		}
		document.getElementById("totalPrice2").innerText = amountFormat(totalPrice);
	}
	//删除列表1
	function delTbody2(){	
	  	document.getElementById("tbody2").deleteRow(window.event.srcElement.parentElement.parentElement.rowIndex-2);  
	}
	//去除disable
	function toDisableCencle(){
		var myTable = document.getElementById("fm");
		for (var i=0; i<myTable.length; i++){  
			var obj = myTable.elements[i];
			obj.disabled=false;
		}
	}
	//联动1
	function toChange1(value){
		var rowObj = window.event.srcElement.parentElement.parentElement;
		var costType = document.getElementById("costType1"+value).value;
		if(costType==<%=Constant.COST_TYPE_02%>){
			rowObj.cells[1].innerHTML=genSelBoxStrExp("costSource1",<%=Constant.COST_SOURCE%>,"",false,"","","false",'<%=Constant.COST_SOURCE_01%>,<%=Constant.COST_SOURCE_02%>');
		}
		if(costType==<%=Constant.COST_TYPE_01%>){
			rowObj.cells[1].innerHTML=genSelBoxStrExp("costSource1",<%=Constant.COST_SOURCE%>,"",false,"","","false",'');
		}
	}
	//联动2
	function toChange2(value){
		var rowObj = window.event.srcElement.parentElement.parentElement;
		var costType = document.getElementById("costType2"+value).value;
		if(costType==<%=Constant.COST_TYPE_02%>){
			rowObj.cells[7].innerHTML=genSelBoxStrExp("costSource2",<%=Constant.COST_SOURCE%>,"",false,"","","false",'<%=Constant.COST_SOURCE_01%>,<%=Constant.COST_SOURCE_02%>');
		}
		if(costType==<%=Constant.COST_TYPE_01%>){
			rowObj.cells[7].innerHTML=genSelBoxStrExp("costSource2",<%=Constant.COST_SOURCE%>,"",false,"","","false",'');
		}
	}

	function t_trim(str){
		return str.replace(/(^\s*)|(\s*$)/g, "");
	}

	//清除功能
	function toClear(){
		document.getElementById("campaignModel").value="";
		document.getElementById("modelId").value="";
	}
	//保存校验
	function toSave(){
		var newRow = document.getElementById("tbody1");
		var mylength=newRow.rows.length;
		if(mylength<1){
			MyAlert("活动费用输入不能为空！");
			return;
		}
		var myForm = document.getElementById("fm");
		var costType1 = '';
		var costType2 = '';
		for (var m=0; m<myForm.length; m++){  
			var obj = myForm.elements[m];
			if(obj.id.length>=9 && obj.id.substring(0,9)=="costType1"){
				var subStr = obj.id.substring(9,obj.id.length);
				var costType = document.getElementById("costType1"+subStr).value;
				costType1 = costType+","+costType1;
			}   
		}  
		for (var n=0; n<myForm.length; n++){  
			var obj = myForm.elements[n];
			if(obj.id.length>=9 && obj.id.substring(0,9)=="costType2"){
				var subStr = obj.id.substring(9,obj.id.length);
				var costType = document.getElementById("costType2"+subStr).value;
				costType2 = costType+","+costType2;
			}   
		}
		var itemName = '';
		var itemRemark = '';
		var itemPrice = '';
		var itemCount = '';
		var costSource1 = '';
		var planCost1 = '';
		var advDate = '';
		var advSubject = '';
		var advMedia = '';
		var mediaSize = '';
		var mediaPrice = '';
		var mediaCount = '';
		var costSource2 = ''; 
		var planCost2 = ''; 
		var itemNames = document.getElementsByName("itemName");
		var itemRemarks = document.getElementsByName("itemRemark");
		var itemPrices = document.getElementsByName("itemPrice");
		var itemCounts = document.getElementsByName("itemCount");
		var costSources1 = document.getElementsByName("costSource1");
		var planCosts1 = document.getElementsByName("planCost1");
		var advDates = document.getElementsByName("advDate");
		var advSubjects = document.getElementsByName("advSubject");
		var advMedias = document.getElementsByName("advMedia");
		var mediaSizes = document.getElementsByName("mediaSize");
		var mediaPrices = document.getElementsByName("mediaPrice");
		var mediaCounts = document.getElementsByName("mediaCount");
		var costSources2 = document.getElementsByName("costSource2");
		var planCosts2 = document.getElementsByName("planCost2");
		var len = itemNames.length;
		var ln = advSubjects.length;
		
		var isMoney = /^(([1-9]\d*)|0)(\.\d{1,2})?$/;
		var isNumber = /^\+?[1-9][0-9]*$/; 
		
		for(var i = 0; i < len; i++){
			if(t_trim(itemNames[i].value)){
				itemName = t_trim(itemNames[i].value)+ ','+itemName;//项目明细
			}else{
				MyAlert("请正确填写项目明细!");
				return;
			}
			itemRemark = itemRemarks[i].value+ ','+itemRemark;
			if(t_trim(itemPrices[i].value) && "0" != t_trim(itemPrices[i].value) && isMoney.test(t_trim(itemPrices[i].value))){
				itemPrice = t_trim(itemPrices[i].value)+ ','+itemPrice;
			}else{
				MyAlert("请正确填写项目单价!");
				return;
			}
			if(t_trim(itemCounts[i].value) && "0" != t_trim(itemCounts[i].value) && isNumber.test(t_trim(itemCounts[i].value))){
				itemCount = t_trim(itemCounts[i].value)+ ','+itemCount;
			}else{
				MyAlert("请正确填写项目数量!");
				return;
			}
			costSource1 = costSources1[i].value+ ','+costSource1;
			planCost1 = planCosts1[i].value+ ','+planCost1;
		}
		for(var j = 0; j< ln; j++){
			if(t_trim(advDates[j].value)){
				advDate = advDates[j].value+ ','+advDate;
			}else{
				MyAlert("请正确填写广告日期");
				return;
			}
			if(t_trim(advSubjects[j].value)){
				advSubject = t_trim(advSubjects[j].value)+ ','+advSubject;
			}else{
				MyAlert("请正确填写广告主题");
				return;
			}
			if(t_trim(advMedias[j].value)){
				advMedia = t_trim(advMedias[j].value)+ ','+advMedia;
			}else{
				MyAlert("请正确填写媒体");
				return;
			}
			if(t_trim(mediaSizes[j].value)){
				mediaSize = t_trim(mediaSizes[j].value)+ ','+mediaSize;
			}else{
				MyAlert("请正确填写规格/版式大小/电视片时间");
				return;
			}
			var a1_ = t_trim(mediaPrices[j].value);
			if(a1_ && isMoney.test(t_trim(a1_))){
				mediaPrice = a1_+ ','+mediaPrice;
			}else{
				MyAlert("请正确填写结算价格");
				return;
			}

			var b1_ = t_trim(mediaCounts[j].value);
			if(b1_ && "0" != b1_ && isNumber.test(b1_)){
				mediaCount = b1_+ ','+mediaCount;
			}else{
				MyAlert("请正确填写频次/数量");
				return;
			}
			costSource2 = costSources2[j].value+ ','+costSource2;
			planCost2 = planCosts2[j].value+ ','+planCost2;
		}
		document.getElementById("costTypes1").value=costType1;
		document.getElementById("itemNames").value=itemName;
		document.getElementById("itemRemarks").value=itemRemark;
		document.getElementById("itemPrices").value=itemPrice;
		document.getElementById("itemCounts").value=itemCount;
		document.getElementById("costSources1").value=costSource1;
		document.getElementById("planCosts1").value=planCost1;
		document.getElementById("costTypes2").value=costType2;
		document.getElementById("advDates").value=advDate;
		document.getElementById("advSubjects").value=advSubject;
		document.getElementById("advMedias").value=advMedia;
		document.getElementById("mediaSizes").value=mediaSize;
		document.getElementById("mediaPrices").value=mediaPrice;
		document.getElementById("mediaCounts").value=mediaCount;
		document.getElementById("costSources2").value=costSource2;
		document.getElementById("planCosts2").value=planCost2;
		MyConfirm("确认保存？", toAdd);
	}
	//保存提交
	function toAdd(){
		toDisableCencle();
		makeNomalFormCall('<%=request.getContextPath()%>/sales/marketmanage/planmanage/ActivitiesPlanReport/activitiesPlanDetailSave.json',showResult,'fm');
	}
	//提报校验
	function toSubmit(){
		var myForm = document.getElementById("fm");
		var costType1 = '';
		var costType2 = '';
		for (var m=0; m<myForm.length; m++){  
			var obj = myForm.elements[m];
			if(obj.id.length>=9 && obj.id.substring(0,9)=="costType1"){
				var subStr = obj.id.substring(9,obj.id.length);
				var costType = document.getElementById("costType1"+subStr).value;
				costType1 = costType+","+costType1;
			}   
		}  
		for (var n=0; n<myForm.length; n++){  
			var obj = myForm.elements[n];
			if(obj.id.length>=9 && obj.id.substring(0,9)=="costType2"){
				var subStr = obj.id.substring(9,obj.id.length);
				var costType = document.getElementById("costType2"+subStr).value;
				costType2 = costType+","+costType2;
			}   
		}
		var itemName = '';
		var itemRemark = '';
		var itemPrice = '';
		var itemCount = '';
		var costSource1 = '';
		var planCost1 = '';
		var advDate = '';
		var advSubject = '';
		var advMedia = '';
		var mediaSize = '';
		var mediaPrice = '';
		var mediaCount = '';
		var costSource2 = ''; 
		var planCost2 = ''; 
		var itemNames = document.getElementsByName("itemName");
		var itemRemarks = document.getElementsByName("itemRemark");
		var itemPrices = document.getElementsByName("itemPrice");
		var itemCounts = document.getElementsByName("itemCount");
		var costSources1 = document.getElementsByName("costSource1");
		var planCosts1 = document.getElementsByName("planCost1");
		var advDates = document.getElementsByName("advDate");
		var advSubjects = document.getElementsByName("advSubject");
		var advMedias = document.getElementsByName("advMedia");
		var mediaSizes = document.getElementsByName("mediaSize");
		var mediaPrices = document.getElementsByName("mediaPrice");
		var mediaCounts = document.getElementsByName("mediaCount");
		var costSources2 = document.getElementsByName("costSource2");
		var planCosts2 = document.getElementsByName("planCost2");
		var len = itemNames.length;
		var ln = advSubjects.length;
		for(var i = 0; i < len; i++){
			itemName = itemNames[i].value+ ','+itemName;
			itemRemark = itemRemarks[i].value+ ','+itemRemark;
			itemPrice = itemPrices[i].value+ ','+itemPrice;
			itemCount = itemCounts[i].value+ ','+itemCount;
			costSource1 = costSources1[i].value+ ','+costSource1;
			planCost1 = planCosts1[i].value+ ','+planCost1;
		}
		for(var j = 0; j< ln; j++){
			advDate = advDates[j].value+ ','+advDate;
			advSubject = advSubjects[j].value+ ','+advSubject;
			advMedia = advMedias[j].value+ ','+advMedia;
			mediaSize = mediaSizes[j].value+ ','+mediaSize;
			mediaPrice = mediaPrices[j].value+ ','+mediaPrice;
			mediaCount = mediaCounts[j].value+ ','+mediaCount;
			costSource2 = costSources2[j].value+ ','+costSource2;
			planCost2 = planCosts2[j].value+ ','+planCost2;
		}
		document.getElementById("costTypes1").value=costType1;
		document.getElementById("itemNames").value=itemName;
		document.getElementById("itemRemarks").value=itemRemark;
		document.getElementById("itemPrices").value=itemPrice;
		document.getElementById("itemCounts").value=itemCount;
		document.getElementById("costSources1").value=costSource1;
		document.getElementById("planCosts1").value=planCost1;
		document.getElementById("costTypes2").value=costType2;
		document.getElementById("advDates").value=advDate;
		document.getElementById("advSubjects").value=advSubject;
		document.getElementById("advMedias").value=advMedia;
		document.getElementById("mediaSizes").value=mediaSize;
		document.getElementById("mediaPrices").value=mediaPrice;
		document.getElementById("mediaCounts").value=mediaCount;
		document.getElementById("costSources2").value=costSource2;
		document.getElementById("planCosts2").value=planCost2;
		MyConfirm("确认提报？",toConfirm);
	}
	//提报提交
	function toConfirm(){
		toDisableCencle();
		makeNomalFormCall('<%=request.getContextPath()%>/sales/marketmanage/planmanage/ActivitiesPlanReport/activitiesPlanDetailConfirm.json',showResult,'fm');
	}
	//返回方法
	function toBack(){
		$('fm').action='<%=request.getContextPath()%>/sales/marketmanage/planmanage/ActivitiesPlanReport/activitiesPlanReportInit.do';
		$('fm').submit();
	}
	//设置业务范围ID,经销商ID
	function getDealerAreaId(arg){
		var areaObj = document.getElementById("areaId");
		var areaId = areaObj.value.split("|")[0];
		var dealerId = areaObj.value.split("|")[1];
		document.getElementById("dealerId").value = dealerId;
		getAvailableAmount();
		var area_id = arg.split("|")[0];
		document.getElementById("area_id").value=area_id;
	}
	//回调方法
	function showResult(json){
		if(json.returnValue == '1'){
			window.parent.MyAlert("操作成功！");
			$('fm').action='<%=request.getContextPath()%>/sales/marketmanage/planmanage/ActivitiesPlanReport/activitiesPlanReportInit.do';
			$('fm').submit();
		}else if(json.returnValue == '2'){
			MyAlert(json.returnStr+",请重新输入！");
		}else{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
</script>
</body>
</html>
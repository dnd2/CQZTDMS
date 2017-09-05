<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<%@ page import="java.util.*"%>
    <%@taglib uri="/jstl/cout" prefix="c"%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<title>市场活动方案制定（省份）修改（制定方案）</title>
		<%
			String contextPath = request.getContextPath();
			List provinceAttachList = (List) request.getAttribute("provinceAttachList");
			List areaAttachList = (List) request.getAttribute("areaAttachList");
		%>

        <script type="text/javascript">
        function doInit(){
            loadcalendar();
        }
function toValidate(arg){
    if(checkForm()){
        var title='';
        if(arg==0){
           title='确认下发?';
        }else if(arg==1){
           title='确认保存?';
        }
        if (submitForm('fm')) {
            MyConfirm(title, toConfirm, [arg]); // 0表示为提报,1表示为保存
        }
    }
}
//操作提交
function toConfirm(value){
    document.getElementById('subm').value = value;
    //setDisTrue('button4', 'button5', 'button6');
    makeNomalFormCall(g_webAppName + '/sales/marketmanage/planmanage/ActivitiesPlanProvinceMake/doMod.json', showResult, 'fm');
    //setTimeout("setDisFalse('button4','button5','button6')", 10000);
}
//回调方法
function showResult(json){
    if (json.returnValue == '1') {
        window.parent.MyAlert("操作成功！");
        $('fm').action = g_webAppName + '/sales/marketmanage/planmanage/ActivitiesPlanProvinceMake/doInit.do';
        $('fm').submit();
    }
    else {
        MyAlert("操作失败！请联系系统管理员！");
    }
}
//验证
function checkForm(){

    var inputList = document.body.getElementsByTagName("input");

    var toPlaceCount = 0;
    var toTelStoreCount = 0;
    var createCardsCount = 0;
    var orderCount = 0;
    var turnCarCount = 0;
    var allCost=0;
    var companyCost=0;
    	
    for (var i = 0; i < inputList.length; i++) {
        if (inputList[i].getAttribute("sumTag") != null) {
            var sumTag = inputList[i].getAttribute("sumTag");
            if (sumTag == 'toPlaceCount') {
                toPlaceCount += parseInt(inputList[i].value);
            }
            if (sumTag == 'toTelStoreCount') {
                toTelStoreCount += parseInt(inputList[i].value);
            }
            if (sumTag == 'createCardsCount') {
                createCardsCount += parseInt(inputList[i].value);
            }
            if (sumTag == 'orderCount') {
                orderCount += parseInt(inputList[i].value);
            }
            if (sumTag == 'turnCarCount') {
                turnCarCount += parseInt(inputList[i].value);
            }
			if (sumTag == 'allCost') {
                allCost += parseFloat(inputList[i].value);
            }
           
            if (sumTag == 'companyCost') {
                companyCost += parseFloat(inputList[i].value);
            }

        }
    }
    if (document.getElementById('toPlaceCount').value > toPlaceCount) {
        MyAlert("方案范围内的来场客流数之和必须大于等于小区目标的来场客流数！");
        return false;

    }
    if (document.getElementById('toTelStoreCount').value > toTelStoreCount) {
        MyAlert("方案范围内的来电来店数之和必须大于等于小区目标的来电来店数！");
        return false;

    }

    if (document.getElementById('createCardsCount').value > createCardsCount) {
        MyAlert("方案范围内的建卡数之和必须大于等于小区目标的建卡数！");
        return false;

    }
    if (document.getElementById('orderCount').value > orderCount) {
        MyAlert("方案范围内的订单数之和必须大于等于小区目标的订单数！");
        return false;

    }
    if (document.getElementById('turnCarCount').value > turnCarCount) {
        MyAlert("方案范围内的交车数之和必须大于等于小区目标的交车数！");
        return false;

    }
	if (parseFloat(document.getElementById('allCost').value) < allCost) {
        MyAlert("方案范围内的总费用之和必须小于等于小区目标的总费用！");
        return false;

    }
    if (parseFloat(document.getElementById('companyCost').value) < companyCost) {
        MyAlert("方案范围内的公司支持和必须小于等于小区目标的公司支持！");
        return false;

    }
    return true;
}

//接收子窗口传回的值
function getDealerInfo(ids, names,codes){
    var ids = document.getElementById("dealerIds").value = ids;
    var arrIDs = ids.split(',');
    var names = names.split(',');
    var codes = codes.split(',');

    for (var i = 0; i < arrIDs.length; i++) {
        var code = codes[i];
        var name = names[i];
        var org = arrIDs[i];
        var trNode = document.getElementById(arrIDs[i]);
        if (trNode != null) {
            var trID = trNode.id;
            if (trID.indexOf(arrIDs[i]) == 0) {
                MyAlert("该区域已存在，请选择其他区域！");
            }
            else {
                addRow(arrIDs[i], name, code, org, prn);
            }
        }
        else {
            addRow(arrIDs[i], name, code);
        }
    }
    return;

}

//根据取到的值动态生成表格
function addRow(dealerId, dealerName, dealerCode){
    var addTable = document.getElementById("dealerTable");
    var rows = addTable.rows;
    var length = rows.length;
    var insertRow = addTable.insertRow(length);
    insertRow.className = "table_list_row";
    insertRow.id = dealerId;
    insertRow.insertCell(0);
    insertRow.insertCell(1);
    insertRow.insertCell(2);
    insertRow.insertCell(3);
    insertRow.insertCell(4);
    insertRow.insertCell(5);
    insertRow.insertCell(6);
    insertRow.insertCell(7);
    insertRow.insertCell(8);
    insertRow.insertCell(9);
    insertRow.insertCell(10);
    insertRow.insertCell(11);
    insertRow.insertCell(12);
    insertRow.insertCell(13);
    insertRow.insertCell(14);

    addTable.rows[length].cells[0].innerHTML = "<td nowrap='nowrap'>" + dealerCode + "</td>";
    addTable.rows[length].cells[1].innerHTML = "<td nowrap='nowrap'>" + dealerName + "<input type='hidden' name='dlrIds' id='dlrIds" + dealerId + "' value='" + dealerId + "'/></td>";

    //addTable.rows[length].cells[2].innerHTML = "<td nowrap='nowrap'><input name='planType" + dealerId + "' 	id='planType" + dealerId + "' 	size='5' sumTag='planType' type='text' value='0' datatype='0,is_double,8' decimal='0'></input></td>";

    addTable.rows[length].cells[2].innerHTML = "<td nowrap='nowrap'>" + genSelBoxExp_html("planType" + dealerId, 1160, "-1", false, "", '', "false", '') +
    "</td>";




    addTable.rows[length].cells[3].innerHTML = "<td nowrap='nowrap'><input name='projectName" + dealerId + "' 	id='projectName" + dealerId + "' 	size='5' sumTag='projectName' type='text' value='' datatype='0,is_textarea,50' decimal='0'></input></td>";


    addTable.rows[length].cells[4].innerHTML = "<td nowrap='nowrap'><input name='executionTimeB" + dealerId + "' 	id='executionTimeB" + dealerId + "' size='10' sumTag='executionTimeB' type='text'  datatype='0,is_date,10' group='executionTimeB"+dealerId+",executionTimeE"+dealerId+"' onClick=\"showcalendar(event, 'executionTimeB" + dealerId + "', false);\"></td>";
    addTable.rows[length].cells[5].innerHTML = "<td nowrap='nowrap'><input name='executionTimeE" + dealerId + "' 	id='executionTimeE" + dealerId + "' size='10' sumTag='executionTimeE' type='text'  datatype='0,is_date,10' group='executionTimeB"+dealerId+",executionTimeE"+dealerId+"' onClick=\"showcalendar(event, 'executionTimeE" + dealerId + "', false);\"></td>";

    addTable.rows[length].cells[6].innerHTML = "<td nowrap='nowrap'><input name='allCost" + dealerId + "' 	id='allCost" + dealerId + "' 	size='5' sumTag='allCost' type='text' value='0' datatype='0,is_double,8' decimal='0'></input></td>";
    addTable.rows[length].cells[7].innerHTML = "<td nowrap='nowrap'><input name='companyCost" + dealerId + "' 	id='companyCost" + dealerId + "'  size='5' sumTag='companyCost' type='text' value='0' datatype='0,is_double,8' decimal='0'></input></td>";

	addTable.rows[length].cells[8].innerHTML = "<td nowrap='nowrap'>"+genSelBoxExp_html("costType" + dealerId, 3004, "-1", false, "", '', "false", '')+"</td>";




    addTable.rows[length].cells[9].innerHTML = "<td nowrap='nowrap'><input name='toPlaceCount" + dealerId + "'  id='toPlaceCount" + dealerId + "'  size='5' sumTag='toPlaceCount' type='text' value='0' datatype='0,is_double,8' decimal='0'></input></td>";
    addTable.rows[length].cells[10].innerHTML = "<td nowrap='nowrap'><input name='toTelStoreCount" + dealerId + "'  id='toTelStoreCount" + dealerId + "'  size='5' sumTag='toTelStoreCount' type='text' value='0' datatype='0,is_double,8' decimal='0'></input></td>";
    addTable.rows[length].cells[11].innerHTML = "<td nowrap='nowrap'><input name='createCardsCount" + dealerId + "' id='createCardsCount" + dealerId + "'  size='5' sumTag='createCardsCount' type='text' value='0' datatype='0,is_double,8' decimal='0'></input></td>";
    addTable.rows[length].cells[12].innerHTML = "<td nowrap='nowrap'><input name='orderCount" + dealerId + "'   id='orderCount" + dealerId + "'   size='5' sumTag='orderCount' type='text' value='0' datatype='0,is_double,8' decimal='0'></input></td>";
    addTable.rows[length].cells[13].innerHTML = "<td nowrap='nowrap'><input name='turnCarCount" + dealerId + "'  id='turnCarCount" + dealerId + "'   size='5' sumTag='turnCarCount' type='text' value='0' datatype='0,is_double,8' decimal='0'></input></td>";
    addTable.rows[length].cells[14].innerHTML = "<td nowrap='nowrap'><input type='button' class='normal_btn' onclick='delRow(" + dealerId + ")' value='删除'/></td>";
    addListener();
}


// 删除表格
function delRow(dealerId){
    var oDealerIds = document.getElementById("dealerIds");
    var aDealerIds = oDealerIds.value.split(",");
    for (i in aDealerIds) {
        if (aDealerIds[i] == dealerId) {
            aDealerIds.splice(i, 1);
        }
    }

    oDealerIds.value = aDealerIds.join();

    var tbodyNode = document.getElementById("dealerTable");
    var trNode = document.getElementById(dealerId);

    tbodyNode.removeChild(trNode);
}


//删除附件
function delAttach(value){
    var fjId = value;
    var delAttachs = document.getElementById("delAttachs").value;
    document.getElementById(value).style.display = "none";
    if (delAttachs.length == 0) {
        delAttachs = fjId;
    }
    else {
        delAttachs = delAttachs + "," + fjId;
    }
    document.getElementById("delAttachs").value = delAttachs;
}

        function goBack(){
            document.fm.action= '<%=contextPath%>/sales/marketmanage/planmanage/ActivitiesPlanProvinceMake/doInit.do';
            document.fm.submit();
        }
        
        </script>

	</head>
	<body>
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />
			&nbsp;当前位置： 市场活动管理 &gt; 活动方案管理 &gt; 市场活动方案制定(省系)
		</div>
		<form method="post" name="fm" id="fm">
			<div>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
				&nbsp;活动信息
			</div>
			<table id="table1" width=100% border="0" align="center"
				cellpadding="1" cellspacing="1" class="table_query">
				<tr>
					<td align="right">
						活动车型：
					</td>
					<td align="left">
						<input type="text" name="campaignModel" id="campaignModel"
							readonly="readonly" datatype="0,is_null,1000" size="30"
							value="${modelName}" />
					</td>
					<td width="20%"></td>
				</tr>
				<tr>
					<td align="right">
						车厂活动编号：
					</td>
					<td align="left">
						<input type="text" name="campaignNo" id="campaignNo"
							value="${ttCampaignPO.campaignNo}" datatype="0,is_digit_letter,17"
							readonly="readonly" />
					</td>
					<td align="right">
						活动名称：
					</td>
					<td align="left">
						<input type="text" name="campaignName" id="campaignName"
							readonly="readonly" value="${ttCampaignPO.campaignName}"
							datatype="0,is_textarea,100" size="30" />
					</td>
					<td></td>
				</tr>
				<tr>
					<td align="right">
						活动开始日期：
					</td>
					<td align="left">
						<input class="short_txt" type="text" readonly="readonly"
							id="startDate" name="startDate" value="${startDate}"
							group="startDate,endDate" datatype="0,is_date,10" />
					</td>
					<td align="right">
						活动结束日期：
					</td>
					<td align="left">
						<input class="short_txt" type="text" readonly="readonly"
							id="endDate" name="endDate" value="${endDate}"
							group="startDate,endDate" datatype="0,is_date,10" />
					</td>
					<td></td>
				</tr>
				<tr>
					<td align="right">
						活动主题：
					</td>
					<td colspan="4" align="left">
						<input type="text" name="campaignSubject" id="campaignSubject"
							readonly="readonly" value="${ttCampaignPO.campaignSubject}"
							datatype="1,is_textarea,100" size="90" />
					</td>
				</tr>
				<tr>
					<td align="right">
						活动对象：
					</td>
					<td colspan="4" align="left">
						<input type="text" name="campaignObject" id="campaignObject"
							readonly="readonly" value="${ttCampaignPO.campaignObject}" size="90"
							datatype="1,is_textarea,100">
					</td>
				</tr>
				<tr>
					<td align="right">
						活动目的：
					</td>
					<td colspan="4" align="left">
						<input type="text" name="campaignPurpose" id="campaignPurpose"
							readonly="readonly" datatype="1,is_textarea,1000" size="70"
							value="${ttCampaignPO.campaignPurpose}">
					</td>
				</tr>
				<tr>
					<td align="right">
						活动要求：
					</td>
					<td colspan="4" align="left">
						<input type="text" name="campaignNeed" id="campaignNeed"
							readonly="readonly" datatype="1,is_textarea,1000" size="70"
							value="${ttCampaignPO.campaignNeed}">
					</td>
				</tr>
				<tr>
					<td align="right">
						活动主要内容：
					</td>
					<td colspan="4" align="left">
						<input type="text" name="campaignDesc" id="campaignDesc"
							readonly="readonly" datatype="1,is_textarea,1000" size="70"
							value="${ttCampaignPO.campaignDesc}">
					</td>
				</tr>
			</table>
<div>
    <img class="nav" src="<%=contextPath%>/img/subNav.gif" />
    &nbsp;事业部附件
</div>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_info">
		<tr>
	        <th colspan="3" align="left">附件列表：<input type="hidden"/>
			</th>
		</tr>

  		<table class="table_info">
  		<% if(areaAttachList!=null&&areaAttachList.size()!=0){ %>
  		<c:forEach items="${areaAttachList}" var="attls">
		    <tr class="table_list_row1" id="${attls.FJID}">
		    <td><a target="_blank" href="${attls.FILEURL}">${attls.FILENAME}</a></td>
		    </tr>
		</c:forEach>
		<%} %>
		</table>
    </table>
			<div>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
				&nbsp;目标
			</div>
			<table width=100% border="0" align="center" cellpadding="1"
				cellspacing="1" class="table_list">
				<tr>
					<th>
						区域编码
					</th>
					<th>
						区域
					</th>
					<th>
						活动形式
					</th>
					<th>
						项目名称
					</th>
					<th colspan="2">
						执行时间
					</th>
					<th>
						总费用
					</th>
					<th>
						公司支持
					</th>
					<th>
						费用处理方式
					</th>
					<th>
						来场客流数
					</th>
					<th>
						来电来店数
					</th>
					<th>
						建卡数
					</th>
					<th>
						订单数
					</th>
					<th>
						交车数
					</th>
				</tr>
				<tbody>

					<tr class="table_list_row">
						<td>
							${toPO.orgCode}
						</td>
						<td>
							${toPO.orgName}
						</td>
						<td>
							<script>
						    	document.write(getItemValue(${planPO.planType}));
						    </script>
						</td>
						<td>
							<input type="text" decimal="0"
								value="${planPO.projectName}" size="5" readonly="true">
						</td>
						<td>
							<input type="text" value="${executionTimeB}" size="10" readonly="true">
						</td>
						<td>
							<input type="text" value="${executionTimeE}" size="10" readonly="true">
						</td>
						<td>
							<input type="text" size="5" id="allCost" value="${planPO.allCost}" readonly="true">
						</td>
						<td>
							<input type="text" size="5"  id="companyCost" value="${planPO.companyCost}" readonly="true">
						</td>
						<td>
							<script>
                                document.write(getItemValue(${planPO.costType}));
						    </script>
						</td>
						<td>
							<input type="text" size="5" id="toPlaceCount" value="${planPO.toPlaceCount}" readonly="true">
						</td>
						<td>
							<input type="text" size="5" id="toTelStoreCount" value="${planPO.toTelStoreCount}" readonly="true">
						</td>
						<td>
							<input type="text" size="5" id="createCardsCount" value="${planPO.createCardsCount}" readonly="true">
						</td>
						<td>
							<input type="text" size="5" id="orderCount"  value="${planPO.orderCount}" readonly="true">
						</td>
						<td>
							<input type="text" size="5" id="turnCarCount"  value="${planPO.turnCarCount}" readonly="true">
						</td>
					</tr>
				</tbody>
			</table>
			<div>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
				&nbsp;省系规划活动范围
			</div>
			<table width=100% border="0" align="center" cellpadding="1"
				cellspacing="1" class="table_list">
				<tr>
					<th colspan="15" align="left">
						<input type="hidden" name="orgId" size="15" value="" id="orgId"
							class="middle_txt" datatype="1,is_noquotation,500" />
						<input type="hidden" name="dealerCode" size="15" value=""
							id="dealerCode" class="middle_txt" datatype="1,is_noquotation,500" />
						<input name="orgbu" id="orgbu" type="button" class="cssbutton"
							onclick="showOrgDealer5('dealerCode', '', 'true', '${orgId}', 'true');"
							value="新增" />
					</th>
				<tr>
					<th>
						区域编码
					</th>
					<th>
						区域
					</th>
					<th>
						活动形式
					</th>
					<th>
						项目名称
					</th>
					<th colspan="2">
						执行时间
					</th>
					<th>
						总费用
					</th>
					<th>
						公司支持
					</th>
					<th>
						费用处理方式
					</th>
					<th>
						来场客流数
					</th>
                    <th>
						来电来店数
					</th>
					<th>
						建卡数
					</th>
					<th>
						订单数
					</th>
					<th>
						交车数
					</th>
                    <th>
						操作
					</th>
				</tr>
				<tbody id="dealerTable">
					<c:forEach items="${list2}" var="item">
					<tr class="table_list_row" id="${item.DEALER_ID}">
						<td>
							${item.DEALER_CODE}
						</td>
						<td>
							${item.DEALER_SHORTNAME}
							<input type="hidden" value="${item.DEALER_ID}"
								id="${item.DEALER_ID}" name="dlrIds">
						</td>
						<td>
							<script>
						    		genSelBoxExp("planType" + ${item.DEALER_ID}, 1160, "${item.PLAN_TYPE}", true, "", '', "false", '')
						    </script>
						</td>
						<td>
							<input type="text" decimal="0" datatype="2,is_textarea,50"
								value="${item.PROJECT_NAME}" sumtag="projectName" size="5"
								id="projectName${item.DEALER_ID}"
								name="projectName${item.DEALER_ID}" >
						</td>
						<td>
							<input type="text" group="executionTimeB${item.DEALER_ID},executionTimeE${item.DEALER_ID}"
								datatype="2,is_date,10" sumtag="executionTimeB" size="10"
								id="executionTimeB${item.DEALER_ID}"
								name="executionTimeB${item.DEALER_ID}" value="${item.EXECUTION_TIME_B}"
                                onclick="showcalendar(event, 'executionTimeB${item.DEALER_ID}', false);">
						</td>
						<td>
							<input type="text" group="executionTimeB${item.DEALER_ID},executionTimeE${item.DEALER_ID}"
								datatype="2,is_date,10" sumtag="executionTimeE" size="10"
								id="executionTimeE${item.DEALER_ID}"
								name="executionTimeE${item.DEALER_ID}" value="${item.EXECUTION_TIME_E}"
                                onclick="showcalendar(event, 'executionTimeE${item.DEALER_ID}', false);">
						</td>
						<td>
							<input type="text" decimal="0" datatype="2,is_double,8" value="${item.ALL_COST}"
								sumtag="allCost" size="5" id="allCost${item.DEALER_ID}"
								name="allCost${item.DEALER_ID}" >
						</td>
						<td>
							<input type="text" decimal="0" datatype="2,is_double,8" value="${item.COMPANY_COST}"
								sumtag="companyCost" size="5" id="companyCost${item.DEALER_ID}"
								name="companyCost${item.DEALER_ID}">
						</td>
						<td>
							<script>
                                genSelBoxExp("costType" + ${item.DEALER_ID}, 3004, "${item.COST_TYPE}", false, "", '', "false", '')
						    </script>
						</td>
						<td>
							<input type="text" decimal="0" datatype="2,is_double,8" value="${item.TO_PLACE_COUNT}"
								sumtag="toPlaceCount" size="5"
								id="toPlaceCount${item.DEALER_ID}"
								name="toPlaceCount${item.DEALER_ID}">
						</td>
						<td>
							<input type="text" decimal="0" datatype="2,is_double,8" value="${item.TO_TEL_STORE_COUNT}"
								sumtag="toTelStoreCount" size="5"
								id="toTelStoreCount${item.DEALER_ID}"
								name="toTelStoreCount${item.DEALER_ID}">
						</td>
						<td>
							<input type="text" decimal="0" datatype="2,is_double,8" value="${item.CREATE_CARDS_COUNT}"
								sumtag="createCardsCount" size="5"
								id="createCardsCount${item.DEALER_ID}"
								name="createCardsCount${item.DEALER_ID}">
						</td>
						<td>
							<input type="text" decimal="0" datatype="2,is_double,8" value="${item.ORDER_COUNT}"
								sumtag="orderCount" size="5" id="orderCount${item.DEALER_ID}"
								name="orderCount${item.DEALER_ID}">
						</td>
						<td>
							<input type="text" decimal="0" datatype="2,is_double,8" value="${item.TURN_CAR_COUNT}"
								sumtag="turnCarCount" size="5" id="turnCarCount${item.DEALER_ID}"
								name="turnCarCount${item.DEALER_ID}">
						</td>
                        <td>
							<input type="button" value="删除"
								onclick="delRow(${item.DEALER_ID})" class="normal_btn">
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
            <!-- 添加附件start -->
            <div>
                <img class="nav" src="<%=contextPath%>/img/subNav.gif" />
                &nbsp;省系附件
            </div>
            <table class="table_info" border="0" id="file">
                <tr>
                    <th>
                        附件列表：
                        <input type="hidden" id="fjids" name="fjids" />
                        <span> <input type="button" class="cssbutton"
                                onclick="showUpload('<%=contextPath%>')" value='添加附件' /> </span>
                    </th>
                </tr>
                <tr>
                    <td width="100%" colspan="2"><jsp:include
                            page="${contextPath}/uploadDiv.jsp" /></td>
                </tr>
            </table>
            <table id="attachTab" class="table_info">
                <%
                    if (provinceAttachList != null && provinceAttachList.size() != 0) {
                %>
                <c:forEach items="${provinceAttachList}" var="attls">
                    <tr class="table_list_row1" id="${attls.FJID}">
                        <td>
                            <a target="_blank" href="${attls.FILEURL}">${attls.FILENAME}</a>
                        </td>
                        <td>
                            <input type=button onclick="delAttach('${attls.FJID}')"
                                class="normal_btn" value="删 除" />
                            <input type="hidden" name="delAttachs" id="delAttachs" value="" />
                        </td>
                    </tr>
                </c:forEach>
                <%
                    }
                %>
            </table>
            <!-- 添加附件end -->
			<table width=100% border="0" align="center" cellpadding="1"
				cellspacing="1" class="table_query">
				<tr>
					<td colspan="4" align="center">
						<input type="hidden" name="campaignId" value="${campaignId}" />
						<input type="hidden" name="dealerIds" value=""/>
						<input type="hidden" name="subm" value=""/>
						<input type="hidden" name="spaceId" value="${spaceId}"/>
						<input type="hidden" name="planId" value="${planPO.planId}"/>
                        <input type="button" class="cssbutton" name="button6"
							onClick="toValidate('1');" value="保存" />
						<input type="button" class="cssbutton" name="button5"
							onClick="toValidate('0');" value="下发" />
						<input type="button" class="cssbutton" name="button4"
							onClick="goBack();" value="返回" />
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
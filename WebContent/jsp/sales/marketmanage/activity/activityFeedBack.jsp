<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<%@ page import="java.util.*"%>
    <%@taglib uri="/jstl/cout" prefix="c"%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<title>市场活动反馈</title>
		<%
			String contextPath = request.getContextPath();
			List provinceAttachList = (List) request.getAttribute("provinceAttachList");
			List areaAttachList = (List) request.getAttribute("areaAttachList");
		%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/sales/marketmanage/activity/activity.js">
	</script>
        <script type="text/javascript">
        function doInit(){
            loadcalendar();
        }
        
         //查询市场活动
	function toActivitiesList(){
		OpenHtmlWindow('<%=contextPath%>/sales/marketmanage/activity/ActivityApply/toActivityMakerList.do',800,600);
	}
	 //显示市场活动信息
	function showActivitiesInfo(makerCode,makerId){
		document.getElementById("makerCode").value = makerCode;
		document.getElementById("maker_id").value = makerId;
	}
	
      function clrMakerTxt(){
        	$("makerCode").value="";
        	$("maker_id").value="";
        }
 function toClear(){
    document.getElementById("campaignModel").value = "";
    document.getElementById("modelId").value = "";
    document.getElementById("groupName").value = "";
}
function toValidate(arg){
    if(checkForm()){
        var title='';
        if(arg==0){
           title='确认提交?';
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
   makeNomalFormCall(g_webAppName + '/sales/marketmanage/activity/ActivityFeedBack/doSave.json?operateFlag='+value, showResult, 'fm');
}
//回调方法
function showResult(json){
    if (json.returnValue == '1') {
        window.parent.MyAlert("操作成功！");
        $('fm').action = g_webAppName + '/sales/marketmanage/activity/ActivityFeedBack/doInit.do';
        $('fm').submit();
    }
    else {
        MyAlert("操作失败！请联系系统管理员！");
    }
}
//验证
function checkForm(){
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
    var str0="<td nowrap='nowrap'>" + dealerCode + "</td>"
    addTable.rows[length].cells[0].innerHTML =str0 ;
    var str1="<td nowrap='nowrap'>" + dealerName + "<input type='hidden' name='dlrIds' id='dlrIds" + dealerId + "' value='" + dealerId + "'/></td>";
   	addTable.rows[length].cells[1].innerHTML =str1 ;
   	var str2="<td nowrap='nowrap'><input name='firstClient" + dealerId + "' 	id='firstClient" + dealerId + "'" 
   	str2+="size='5' sumTag='projectName' type='text' value='' datatype='0,is_textarea,50' decimal='0'></input></td>"
    addTable.rows[length].cells[2].innerHTML = str2;
    var str3="<td nowrap='nowrap'><input name='mixClient" + dealerId + "' 	id='mixClient" + dealerId + "' 	size='5' sumTag='projectName'";
  	 str3+="type='text' value='' datatype='0,is_textarea,50' decimal='0'></input></td>";
    addTable.rows[length].cells[3].innerHTML =str3 ;
   	var str4="<td nowrap='nowrap'><input name='aimCard" + dealerId + "' 	id='aimCard" + dealerId + "' 	size='5' sumTag='projectName'";
  	 str4+="type='text' value='' datatype='0,is_textarea,50' decimal='0'></input></td>";
    addTable.rows[length].cells[4].innerHTML = str4;
    var str5="<td nowrap='nowrap'><input name='aimOrder" + dealerId + "' 	id='aimOrder" + dealerId + "' 	size='5' sumTag='projectName'";
  	 str5+="type='text' value='' datatype='0,is_textarea,50' decimal='0'></input></td>";
    addTable.rows[length].cells[5].innerHTML = str5;
    str6="<td nowrap='nowrap'><input type='button' class='normal_btn' onclick='delRow(" + dealerId + ")' value='删除'/></td>";
    addTable.rows[length].cells[6].innerHTML = str6;
    
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
			&nbsp;当前位置： 市场活动管理 &gt; 市场活动管理 &gt; 市场活动反馈
		</div>
		<form method="post" name="fm" id="fm">
		<input type="hidden" name="activityId" id="activityId" value="${ttcp.activityId}" />
		<input type="hidden" name="status" id="status" value="${ttcp.status}" />
			
			<table width=100% border="0" align="center" cellpadding="1"
				cellspacing="1" class="table_query">
					
				<tr>
					<td class="table_query_2Col_label_6Letter">
						活动名称：
					</td>
					<td>
					${ttcp.activityName}
						
					</td>
					<td class="table_query_2Col_label_6Letter">
						活动类型：
					</td>
					<td colspan="4">
					<script type="text/javascript">writeItemValue(${ttcp.activityType})</script>
					</td>
					
				</tr>
				<tr>
					<td class="table_query_2Col_label_6Letter">
						活动车型：
					</td>
					<td>
					${serialName}
					</td>
					<td class="table_query_2Col_label_6Letter">
						财务处理方式：
					</td>
					<td colspan="4">
					<script type="text/javascript">writeItemValue(${ttcp.costType} )</script>
					</td>
				</tr>
				
				<tr>
					<td class="table_query_2Col_label_6Letter">
						活动总费用预估(含税、万元)：
					</td>
					<td>
					${ttcp.totalPre}
					</td>
					<td class="table_query_2Col_label_5Letter">
						公司支持费用(万元)：
					</td>
					<td>
					${ttcp.totalSupport}
					</td>
				</tr>
				<tr>
					<td class="table_query_2Col_label_6Letter">
						活动开始日期：
					</td>
					<td>
					${startDate}
					</td>
					<td class="table_query_2Col_label_6Letter">
						活动结束日期：
					</td>
					<td>
					${endDate}
					</td>
				</tr>
				<tr>
					<td class="table_query_2Col_label_6Letter">
						活动主题：
					</td>
					<td colspan="4">
					${ttcp.activityTheme}
					</td>
				</tr>
				<tr>
					<td class="table_query_2Col_label_6Letter">
						项目执行方：
					</td>
					<td>
					${makerCode}
					</td>
					</tr>
				<tr>
					<td class="table_query_2Col_label_6Letter">
						活动备注：
					</td>
					<td colspan="4">
					${ttcp.activityRemark}
					</td>
				</tr>
				<tr>
					<td class="table_query_2Col_label_6Letter">
						议价金额(含税、万元)：
					</td>
					<td>
						${ttcp.suggestMoney}
					</td>
				<td class="table_query_2Col_label_6Letter">
						支持比例：
					</td>
					<td>
					${ttcp.supportRatio}
					</td>
				</tr>
			</table>
			
			<div>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
				&nbsp;目标
			</div>
			<table class="table_query" align="center" border="0" cellpadding="1"
				cellspacing="1" width="100%" id="dealerTable1">
				<tbody>

					<tr class="table_list_row2">
						<td align="center" width="10%">
							项目
						</td>
						<td align="center" width="20%">
							目标
						</td>
						<td align="center" width="20%">
							实际
						</td>
						<td align="center" width="10%">
							完成率
						</td>
						<td align="center" width="20%">
							原因分析
						</td>
						<td align="center" width="20%">
							改进措施
						</td>
					</tr>
					<tr class="table_list_row2">
						<td align="center">
							首客
						</td>
						<td align="center" id="aim1">
						${ttcp.totalFclient}
						</td>
						<td align="center">
							<input name="actfTotal" id="actfTotal" type="text" 
								value='0' datatype="0,is_double,8" decimal="0" readonly="readonly"  />
						</td>
						<td align="center" id="rate1">
						
						</td>
						<td align="center" rowspan="4">
							<textarea rows="10" cols="30" name="reasonAnaly" id="reasonAnaly" datatype="0,is_textarea,200"></textarea>
						</td>
						<td align="center" rowspan="4">
							<textarea rows="10" cols="30" name="suggestTip" id="suggestTip" datatype="0,is_textarea,200"></textarea>
						</td>
					</tr>
					<tr class="table_list_row2">
						<td align="center">
							混客
						</td>
						<td align="center" id="aim2">
						${ttcp.totalMclient}
						</td>
						<td align="center">
							<input name="actMixTotal" id="actMixTotal" type="text"
								value='0' datatype="0,is_double,8" decimal="0" readonly="readonly"   />
						</td>
						<td align="center" id="rate2">
						
						</td>
					</tr>
					<tr class="table_list_row2">
						<td align="center">
							建卡
						</td>
						<td align="center" id="aim3">
							${ttcp.totalAimcard}
						</td>
						<td align="center">
							<input name="actCardTotal" id="actCardTotal" type="text"
								value='0' datatype="0,is_double,8" decimal="0" readonly="readonly"  />
						</td>
						<td align="center" id="rate3">
						
						</td>
					</tr>
					<tr class="table_list_row2">
						<td align="center">
							订单
						</td>
						<td align="center" id="aim4">
							${ttcp.totalAimorder}
						</td>
						<td align="center">
							<input name="actOrderTotal" id="actOrderTotal" type="text"
								value='0' datatype="0,is_double,8" decimal="0" readonly="readonly"  />
						</td>
						<td align="center" id="rate4">
						
						</td>
					</tr>
				</tbody>
			</table>
			<div>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
				&nbsp;活动范围
			</div>
			<table width=100% border="0" align="center" cellpadding="1"
				cellspacing="1" class="table_list">
				<tr>
					<th colspan="15" align="left">
						<input type="hidden" name="orgId" size="15" value="" id="orgId"
							class="middle_txt" datatype="1,is_noquotation,500" />
						<input type="hidden" name="dealerCode" size="15" value=""
							id="dealerCode" class="middle_txt" datatype="1,is_noquotation,500" />
					</th>
				<tr>
					<th>
						经销商编码
					</th>
					<th>
						经销商名称
					</th>
					<th>
						首客目标
					</th>
					<th>
						实际首客
					</th>
                    <th>
						混客目标
					</th>
					 <th>
						实际混客
					</th>
					<th>
						建卡目标
					</th>
					<th>
						实际建卡
					</th>
					<th>
						订单目标
					</th>
					<th>
						实际订单
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
								id="${item.DEALER_ID}" name="dlrIds" />
						</td>
						<td>
						${item.FCLIENT}
<!--							<input type="text" decimal="0" datatype="2,is_textarea,50"-->
<!--								value=""  size="5"-->
<!--								id="firstClient${item.DEALER_ID}"-->
<!--								name="firstClient${item.DEALER_ID}" />-->
						</td>
						<td>
							<input type="text" decimal="0" datatype="2,is_double,8"
							 size="5" id="actFirstClient${item.DEALER_ID}"
								name="actFirstClient${item.DEALER_ID}" onchange="calActNumber();calRatio();"/>
						</td>
						<td>
						${item.MIXCLIENT}
						</td>
						<td>
							<input type="text" decimal="0" datatype="2,is_double,8" 
								 size="5" id="actMixClient${item.DEALER_ID}"
								name="actMixClient${item.DEALER_ID}" onchange="calActNumber();calRatio();"/>
						</td>
						<td>
						${item.AIMCARD}
						</td>
						<td>
							<input type="text" decimal="0" datatype="2,is_double,8" 
								sumtag="toTelStoreCount" size="5"
								id="actAimCard${item.DEALER_ID}"
								name="actAimCard${item.DEALER_ID}" onchange="calActNumber();calRatio();"/>
						</td>
						<td>
						${item.AIMORDER}
						</td>
						<td>
							<input type="text" decimal="0" datatype="2,is_double,8" 
								 size="5" id="actAimOrder${item.DEALER_ID}"
								name="actAimOrder${item.DEALER_ID}" onchange="calActNumber();calRatio();"/>
								<input type="hidden" decimal="0" datatype="2,is_double,8" value="${item.SUB_ID}"
								 size="5" id="subId${item.DEALER_ID}"
								name="subId${item.DEALER_ID}" />
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
            <!-- 添加附件start -->
            <div>
                <img class="nav" src="<%=contextPath%>/img/subNav.gif" />
                &nbsp;活动附件
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
						<input type="button" class="cssbutton" name="button4"
							onClick="goBack();" value="返回" />
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
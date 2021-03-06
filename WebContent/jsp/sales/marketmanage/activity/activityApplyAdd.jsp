<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<%@ page import="java.util.*"%>
    <%@taglib uri="/jstl/cout" prefix="c"%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<title>市场活动申请</title>
		<%
			String contextPath = request.getContextPath();
			List provinceAttachList = (List) request.getAttribute("provinceAttachList");
			List areaAttachList = (List) request.getAttribute("areaAttachList");
		%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/sales/marketmanage/activity/activity.js"></script>
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
function toValidate(arg){
    if(checkForm()){
        var title='';
        if(arg==1){
           title='确认保存?';
        }else if(arg==0){
           title='确认提交?';
        }
        if (submitForm('fm')) {
            MyConfirm(title, toConfirm, [arg]); // 0表示为提报,1表示为保存
        }
    }
}
//操作提交
function toConfirm(value){
    //setDisTrue('button4', 'button5', 'button6');
    
    makeNomalFormCall(g_webAppName + '/sales/marketmanage/activity/ActivityApply/doSave.json?operateFlag='+value, showResult, 'fm');
    
}
//回调方法
function showResult(json){
    if (json.returnValue == '1') {
        window.parent.MyAlert("操作成功！");
        $('fm').action = g_webAppName + '/sales/marketmanage/activity/ActivityApply/doInit.do';
        $('fm').submit();
    }else if(json.returnValue == '2'){
    	 MyAlert("经销商不在同一个大区，请重新选择！！！");
    }else{
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
   	str2+="size='5' sumTag='projectName'  onchange='calNumber()' type='text' value='' datatype='2,is_double,8' decimal='0'></input></td>"
    addTable.rows[length].cells[2].innerHTML = str2;
    var str3="<td nowrap='nowrap'><input name='mixClient" + dealerId + "' 	id='mixClient" + dealerId + "' 	size='5' sumTag='projectName'";
  	 str3+="type='text' value='' datatype='2,is_double,8' onchange='calNumber()' decimal='0'></input></td>";
    addTable.rows[length].cells[3].innerHTML =str3 ;
   	var str4="<td nowrap='nowrap'><input name='aimCard" + dealerId + "' 	id='aimCard" + dealerId + "' 	size='5' sumTag='projectName'";
  	 str4+="type='text' value='' datatype='2,is_double,8'  onchange='calNumber()' decimal='0'></input></td>";
    addTable.rows[length].cells[4].innerHTML = str4;
    var str5="<td nowrap='nowrap'><input name='aimOrder" + dealerId + "' 	id='aimOrder" + dealerId + "' 	size='5' sumTag='projectName'";
  	 str5+="type='text' value='' datatype='2,is_double,8'  onchange='calNumber()' decimal='0'></input></td>";
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
    calNumber();
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
       history.back();
 }
        
        </script>

	</head>
	<body>
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />
			&nbsp;当前位置： 市场活动管理 &gt; 市场活动管理 &gt; 市场活动申请
		</div>
		<form method="post" name="fm" id="fm">
			<table width=100% border="0" align="center" cellpadding="1"
				cellspacing="1" class="table_query">
				<tr>
					<td class="table_query_2Col_label_6Letter">
						活动名称：
					</td>
					<td>
						<input type="text" class="long_txt" name="activityName"
							id="activityName" datatype="0,is_textarea,50" size="30"
							maxlength="50" />
					</td>
					<td class="table_query_2Col_label_6Letter">
						活动类型：
					</td>
					<td colspan="4">
						<script type="text/javascript">
	               	 	genSelBoxExp("activityType",9190,"91901002",false,"mini_sel","","false",'');
	            		</script> 
					</td>
					
				</tr>
				<tr>
					<td class="table_query_2Col_label_6Letter">
						负责人：
					</td>
					<td>
						<input type="text" class="long_txt" name="chargeMan"
							id="chargeMan" datatype="0,is_textarea,50" size="30"
							maxlength="50" />
					</td>
					<td class="table_query_2Col_label_6Letter">
						月份：
					</td>
					<td colspan="4">
					<select id="activity_month" name="activity_month">
						<c:forEach items="${months}" var="po" >
							<option>${po}</option>
						</c:forEach>
					</select>
					</td>
				</tr>
				<tr>
					<td class="table_query_2Col_label_6Letter">
						活动车型：
					</td>
					<td>
						<input type="hidden" class="middle_txt" name="campaignModel"
							id="campaignModel" readonly="readonly" datatype="0,is_null,1000"
							size="30" />
                        <input type="text" class="middle_txt" name="groupName"
							id="groupName" datatype="0,is_null,1000" readonly="readonly"
							size="30" />
						<input type="hidden" name="modelId" id="modelId" />
						<input name="button3" type="button" class="mini_btn"
							onclick="showMaterialCarType_market('groupCode','','true','')"
							value="..." />
						<input type="button" name="clearbutton" id="clearbutton"
							class="cssbutton" value="清除" onClick="toClear();" />
						<input type="hidden" name="area_id" id="area_id" value="" />
					</td>
					<td class="table_query_2Col_label_6Letter">
						财务处理方式：
					</td>
					<td colspan="4">
						<script type="text/javascript">
	               	 	genSelBoxExp("disCountType",9191,"91911002",false,"mini_sel","","false",'');
	            		</script> 
					</td>
				</tr>
				
				<tr>
					<td class="table_query_2Col_label_6Letter">
						活动总费用预估(含税、万元)：
					</td>
					<td>
						<input type="text" class="middle_txt" name="totalPre"
							id="totalPre" datatype="0,isMoney,30"
							 value="" />
					</td>
					<td class="table_query_2Col_label_5Letter">
						公司支持费用(万元)：
					</td>
					<td>
						<input type="text" class="long_txt" name="totalSupport"
							id="totalSupport" datatype="0,isMoney,50" size="30"
							maxlength="50" />
					</td>
				</tr>
				<tr>
					<td class="table_query_2Col_label_6Letter">
						活动开始日期：
					</td>
					<td>
						<input class="short_txt" type="text" readonly="readonly"
							id="startDate" name="startDate" group="startDate,endDate"
							datatype="0,is_date,10" />
						<input class="time_ico" type="button"
							onClick="showcalendar(event, 'startDate', false);" value="&nbsp;" />
					</td>
					<td class="table_query_2Col_label_6Letter">
						活动结束日期：
					</td>
					<td>
						<input class="short_txt" type="text" readonly="readonly"
							id="endDate" name="endDate" group="startDate,endDate"
							datatype="0,is_date,10" />
						<input class="time_ico" type="button"
							onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
					</td>
				</tr>
				<tr>
					<td class="table_query_2Col_label_6Letter">
						活动主题：
					</td>
					<td colspan="4">
						<input type="text" class="middle_txt" style="width: 450px"
							name="actTheme" id="actTheme"
							datatype="0,is_textarea,50" size="90" maxlength="50" />
					</td>
				</tr>
				<tr>
					<td class="table_query_2Col_label_6Letter">
						项目执行方：
					</td>
					<td>
					<input id="makerCode" name="makerCode" type="text" value="" class="middle_txt"  size="30"  /> 
					<input id="maker_id" name="maker_id" value="" type="hidden" class="middle_txt" /> 
					<input id="type" name="type" value="" type="hidden"/> 
					<input type="button" value="..." class="mini_btn" onclick="toActivitiesList();" />
					<input type="button" value="清空" class="normal_btn" onclick="clrActivityTxt();" />
					</td>
					</tr>
				<tr>
					<td class="table_query_2Col_label_6Letter">
						活动备注：
					</td>
					<td colspan="4">
						<textarea name="activityRemark" id="activityRemark" rows="4" cols="70"></textarea>
					</td>
				</tr>
			</table>
			
			<div>
				<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
				&nbsp;目标
			</div>
			<table class="table_query" align="center" border="0" cellpadding="1"
				cellspacing="1" width="100%">
				<tbody>

					<tr class="table_list_row2">
						<td align="center">
							项目
						</td>
						<td align="center">
							目标
						</td>
					</tr>
					<tr class="table_list_row2">
						<td align="center">
							首客目标
						</td>
						<td align="center">
							<input name="fTotal" id="fTotal" type="text"
								value='0' datatype="0,is_double,8" decimal="0" readonly="readonly" />
						</td>
					</tr>
					<tr class="table_list_row2">
						<td align="center">
							混客目标
						</td>
						<td align="center">
							<input name="mixTotal" id="mixTotal" type="text"
								value='0' datatype="0,is_double,8" decimal="0" readonly="readonly" />
						</td>
					</tr>
					<tr class="table_list_row2">
						<td align="center">
							建卡目标
						</td>
						<td align="center">
							<input name="cardTotal" id="cardTotal" type="text"
								value='0' datatype="0,is_double,8" decimal="0" readonly="readonly" />
						</td>
					</tr>
					<tr class="table_list_row2">
						<td align="center">
							订单目标
						</td>
						<td align="center">
							<input name="orderTotal" id="orderTotal" type="text" value='0'
								datatype="0,is_double,8" decimal="0" readonly="readonly" />
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
						<input name="orgbu" id="orgbu" type="button" class="cssbutton"
							onclick="showOrgDealerMarket('dealerCode', '', 'true', '${orgId}', 'true');"
							value="新增" />
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
						混客目标
					</th>
					<th>
						建卡目标
					</th>
					<th>
						订单目标
					</th>
                    <th>
						操作
					</th>
				</tr>
				<tbody id="dealerTable">
					
				</tbody>
			</table>
            <!-- 添加附件start -->
            <div>
                <img class="nav" src="<%=contextPath%>/img/subNav.gif" />
                &nbsp;市场活动附件
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
							onClick="toValidate('0');" value="提交" />
						<input type="button" class="cssbutton" name="button4"
							onClick="goBack();" value="返回" />
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
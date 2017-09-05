<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
    String contextPath = request.getContextPath();
%>
<title>库存盘点调整申请</title>
<script type="text/javascript">
$(function(){
	__extQuery__(1);
});
	var myPage;
var url = "<%=contextPath%>/parts/storageManager/partStoInveManager/entityInvImpAction/entityInvImpSearch.json";
var title = null;
var columns = [
			{header: "序号", dataIndex: 'RESULT_ID', renderer:getIndex,align:'center'},
			{header: "盘点单号", dataIndex: 'CHANGE_CODE', align:'center'},
			{header: "申请单号", dataIndex: 'RESULT_CODE', align:'center'},
			{header: "盘点类型", dataIndex: 'CHECK_TYPE', align:'center',renderer:getItemValue},
			{header: "盘点仓库", dataIndex: 'WH_CNAME', align:'center'},
			{header: "导入人", dataIndex: 'NAME', align:'center'},
			{header: "导入日期", dataIndex: 'CREATE_DATE', align:'center'},
			{header: "单据状态", dataIndex: 'STATE', align:'center',renderer:getItemValue},
			{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
	      ];

//设置超链接
function myLink(value,meta,record){
	var resultId = record.data.RESULT_ID;
	var str = "<a href=\"#\" onclick='viewDetail(\""+resultId+"\")'>[查看]</a>&nbsp;";
	str = str + "<a href=\"#\" onclick='confirmCommit(\""+resultId+"\")'>[提交]</a>&nbsp;<a href=\"#\" onclick='confirmDisable(\""+resultId+"\")'>[作废]</a>";
	return String.format(str);
}

//查看
function viewDetail(parms){
	document.getElementById("resultId").value = parms;
	document.fm.action="<%=contextPath%>/parts/storageManager/partStoInveManager/entityInvImpAction/viewStockDeatilInit.do";
	document.fm.target="_self";
	document.fm.submit();
}

    
//下载
function exportPartStockExcel(){
	document.fm.action="<%=contextPath%>/parts/storageManager/partStoInveManager/entityInvImpAction/exportPartStockStatusExcel.do";
	document.fm.target="_self";
	document.fm.submit();
}

//上传
function uploadExcel(){
	btnDisable();
	var actionURL = "<%=contextPath%>/parts/storageManager/partStoInveManager/entityInvImpAction/entityInvImpInit.do";
	document.getElementById("actionURL").value = actionURL;
	fm.action = "<%=contextPath%>/parts/storageManager/partStoInveManager/entityInvImpAction/entityInvImpUpload.do";
	fm.submit();
}

//上传检查和确认信息
function confirmUpload(){
	if(fileVilidate()){
		MyConfirm("确定上传盘点结果文件?",uploadExcel,[]);
	}
	
}

function fileVilidate(){
	var msg = "";
	if(document.getElementById("inveId").value==""){
		msg += "请先选择盘点单号!</br>";
	}
	if(msg != "")
	{
		MyAlert(msg);
		return false;
	}
	var importFileName = $("#uploadFile").val();
	if(importFileName==""){
	    MyAlert("请选择上传文件");
		return false;
	}
	var index = importFileName.lastIndexOf(".");
	var suffix = importFileName.substr(index+1,importFileName.length).toLowerCase();
	if(suffix != "xls" && suffix != "xlsx"){
	MyAlert("请选择Excel格式文件");
		return false;
	}
	return true;
}

//提交
function confirmCommit(parms){
	var optionType = "commit";
	MyConfirm("确定提交该盘点结果?",commitOrder,[[parms,optionType]]);
}

//失效
function confirmDisable(parms){
	var optionType = "disable";
	MyConfirm("确定作废该盘点结果?",commitOrder,[[parms,optionType]]);
}

function commitOrder(paramArr){
	var parms = paramArr[0];
	var optionType = paramArr[1];
	var url = "<%=contextPath%>/parts/storageManager/partStoInveManager/entityInvImpAction/commitOrDisableInveRes.json?resultId=" + parms +"&optionType=" + optionType;
	if("commit" == optionType){
		sendAjax(url,getResult,'fm');
	}else if("disable" == optionType){
		sendAjax(url,getDisableResult,'fm');
	}	
}
function getResult(json){
	if(null != json){
        if (json.errorExist != null && json.errorExist.length > 0) {
        	MyAlert(json.errorExist);
        	__extQuery__(json.curPage);
        } else if (json.success != null && json.success == "true") {
        	MyAlert("提交操作成功!");
        	__extQuery__(json.curPage);
        } else {
            MyAlert("提交操作失败，请联系管理员!");
        }
	}
}

function getDisableResult(json){
	if(null != json){
        if (json.errorExist != null && json.errorExist.length > 0) {
        	MyAlert(json.errorExist);
        	__extQuery__(json.curPage);
        } else if (json.success != null && json.success == "true") {
        	MyAlert("作废操作成功!");
        	window.location = "<%=contextPath%>/parts/storageManager/partStoInveManager/entityInvImpAction/entityInvImpInit.do";
        } else {
            MyAlert("作废操作失败，请联系管理员!");
        }
	}
}

function showUpload(){
	var uploadDiv = document.getElementById("uploadDiv");
	if(uploadDiv.style.display=="block" ){
		uploadDiv.style.display = "none";
	}else {
		uploadDiv.style.display = "block";
	}
}

//下载上传模板
function exportExcelTemplate(){
	fm.action = "<%=contextPath%>/parts/storageManager/partStoInveManager/entityInvImpAction/exportExcelTemplate.do";
	fm.submit();
}

</script>

</head>
<body>
	<div class="wbox">
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置： 配件仓库管理 &gt;库存盘点管理&gt;库存盘点调整申请
				<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
				<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
				<input type="hidden" name="companyName" id="companyName" value="${companyName }" />
				<input type="hidden" name="resultId" id="resultId" value="" />
				<input type="hidden" name="actionURL" id="actionURL" value="" />
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">盘点单号：</td>
							<td>
								<input class="middle_txt" type="text" name="inveCode" id="inveCode" />
							</td>
							<td class="right">盘点类型：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("inveType", <%=Constant.PART_STOCK_INVE_TYPE%> , "", true, "", "", "false", "");
								</script>
							</td>
							<td class="right">盘点仓库：</td>
							<td>
								<select name="whId" id="whId" class="u-select">
									<option value="">-请选择-</option>
									<c:if test="${WHList!=null}">
										<c:forEach items="${WHList}" var="list">
											<option value="${list.WH_ID }">${list.WH_CNAME }</option>
										</c:forEach>
									</c:if>
								</select>
							</td>
						</tr>
						<tr>
							<td class="right">申请单号：</td>
							<td>
								<input class="middle_txt" type="text" name="resultCode" id="resultCode" />
							</td>
							<td class="right">导入日期：</td>
							<td>
								<input id="checkSDate" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" style="width:80px;"/>
								<input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
								至
								<input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" style="width:80px;"/>
								<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
							</td>
							<td class="right">导入人：</td>
							<td>
								<input class="middle_txt" type="text" name="improterName" id="improterName" />
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input class="u-button" type="button" value="上传文件" id="upload_button" name="button1" onclick="showUpload();">
								<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
								<input class="u-button" type="reset" value="重 置">
								<input class="u-button" type="button" value="导 出" onclick="exportPartStockExcel()" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div style="display: none; heigeht: 5px" id="uploadDiv">
				<table class="table_query">
					<th colspan="2"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 上传文件</th>
					<tr style="line-height: 30px;">
						<td class="right" width="100px">可用盘点单号：</td>
						<td>
							<select name="inveId" id="inveId" class="u-select">
								<option value="">-请选择-</option>
								<c:if test="${inveList!=null}">
									<c:forEach items="${inveList}" var="list">
										<option value="${list.CHANGE_ID }">${list.CHANGE_CODE }</option>
									</c:forEach>
								</c:if>
							</select> <font color="red">*</font>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<font color="red"> 
								&nbsp;
								&nbsp;
								<input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate()" /> 文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;
							</font>
							<input type="file" name="uploadFile" id="uploadFile" style="width: 250px" datatype="0,is_null,2000" value="" />
							&nbsp;
							<input type="button" id="upbtn" class="normal_btn" value="确 定" onclick="confirmUpload()" />
						</td>
					</tr>
				</table>
			</div>

			<!-- 查询条件 end -->
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
		</form>
</body>
</div>
</html>
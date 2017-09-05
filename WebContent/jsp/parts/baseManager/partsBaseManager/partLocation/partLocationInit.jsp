<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件货位信息查询</title>
<script type="text/javascript">
$(function(){
	locSearch('normal');
});
var myPage;
var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/partLocationQuery.json";
var orgId = document.getElementById("orgId").value + "";
var oemOrgId = <%=Constant.OEM_ACTIVITIES%> + "";

var title = null;
var columns = null;
function locSearch(parms){
	document.getElementById("searchType").value = parms;
	if("normal" == parms){
		if(oemOrgId == orgId){
			columns = [
						{header: "序号", style: 'text-align:left', renderer:getIndex,width:'7%'},
						{id:'action',header: "操作",sortable: false,dataIndex: 'LOC_ID',renderer:myLink ,align:'center'},
						{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
						{header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
	        //          {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
						{header: "货位编码", dataIndex: 'LOC_CODE', style: 'text-align:left'},
			//			{header: "货位名称", dataIndex: 'LOC_NAME', style: 'text-align:left'},
						{header: "附属货位", dataIndex:'SUB_LOC', style: 'text-align:left'},
//							{header: "库管员", dataIndex: 'WH_MAN', style: 'text-align:center'},
//						{header: "包装尺寸", dataIndex:'PKG_SIZE', style: 'text-align:left'},
//						{header: "最小包装量", dataIndex:'OEM_MIN_PKG'},
//						{header: "整包发运量", dataIndex:'MIN_PKG'},
						/* {header: "新增日期", dataIndex: 'CREATE_DATE', style: 'text-align:left'}, */
						{header: "修改日期", dataIndex: 'UPDATE_DATE', style: 'text-align:center'},
			//			{header: "是否有效", dataIndex:'STATE', style: 'text-align:center', renderer: getItemValue},
						{header: "仓库", dataIndex: 'WH_NAME', style: 'text-align:left'}
						
			      ];
		}else{
			columns = [
						{header: "序号", style: 'text-align:left', renderer:getIndex,width:'7%'},
						{id:'action',header: "操作",sortable: false,dataIndex: 'LOC_ID',renderer:myLink ,align:'center'},
						{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
						{header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
	        //          {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
						{header: "货位编码", dataIndex: 'LOC_CODE', style: 'text-align:left'},
			//			{header: "货位名称", dataIndex: 'LOC_NAME', style: 'text-align:left'},
						{header: "附属货位", dataIndex:'SUB_LOC', style: 'text-align:left'},
			//			{header: "包装尺寸", dataIndex:'PKG_SIZE', style: 'text-align:left'},
			//			{header: "最小包装量", dataIndex:'OEM_MIN_PKG', style: 'text-align:right'},
			//			{header: "整包发运量", dataIndex:'MIN_PKG'},
						/* {header: "新增日期", dataIndex: 'CREATE_DATE', style: 'text-align:left'}, */
						{header: "修改日期", dataIndex: 'UPDATE_DATE', style: 'text-align:left'},
			//			{header: "是否有效", dataIndex:'STATE', style: 'text-align:left', renderer: getItemValue},
						{header: "仓库", dataIndex: 'WH_NAME', style: 'text-align:left'}
						
			      ];
		}
	}else{
		if(oemOrgId == orgId){
			columns = [
						{header: "序号", style: 'text-align:left', renderer:getIndex,width:'7%'},
						{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
						{header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
	        //          {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
						{header: "货位编码(原)", dataIndex: 'OLD_LOC_CODE', style: 'text-align:left'},
						{header: "货位编码(现)", dataIndex: 'LOC_CODE', style: 'text-align:left'},
			//			{header: "货位名称", dataIndex: 'LOC_NAME', style: 'text-align:left'},
						{header: "附属货位(原)", dataIndex:'OLD_SUB_LOC', style: 'text-align:left'},
						{header: "附属货位(现)", dataIndex:'SUB_LOC', style: 'text-align:left'},
						{header: "库管员(原)", dataIndex: 'OLD_WH_MAN', style: 'text-align:center'},
						{header: "库管员(现)", dataIndex: 'WH_MAN', style: 'text-align:center'},
						{header: "修改日期", dataIndex: 'CREATE_DATE', style: 'text-align:center'},
						{header: "仓库", dataIndex: 'WH_NAME'}
			      ];
		}else{
			columns = [
						{header: "序号", style: 'text-align:left', renderer:getIndex,width:'7%'},
						{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
						{header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
	        //          {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
						{header: "货位编码(原)", dataIndex: 'OLD_LOC_CODE', style: 'text-align:left'},
						{header: "货位编码(现)", dataIndex: 'LOC_CODE', style: 'text-align:left'},
			//			{header: "货位名称", dataIndex: 'LOC_NAME', style: 'text-align:left'},
						{header: "附属货位(原)", dataIndex:'OLD_SUB_LOC', style: 'text-align:left'},
						{header: "附属货位(现)", dataIndex:'SUB_LOC', style: 'text-align:left'},
			//			{header: "库管员(原)", dataIndex: 'OLD_WH_MAN', style: 'text-align:center'},
			//			{header: "库管员(现)", dataIndex: 'WH_MAN', style: 'text-align:center'},
						{header: "修改日期", dataIndex: 'CREATE_DATE', style: 'text-align:center'},
						{header: "仓库", dataIndex: 'WH_NAME', style: 'text-align:left'}
			      ];
		}
		
	}
	__extQuery__(1);
}
//设置超链接
function myLink(value,meta,record){
//    var state = record.data.STATE;
//    if(state=='<%=Constant.STATUS_DISABLE %>'){
//        return String.format("<a href=\"#\" onclick='valid(\""+value+"\")'>[有效]</a>");
//    }	    
//		return String.format("<a href=\"#\" onclick='sel(\""+value+"\")'>[修改]</a>&nbsp;<a href=\"#\" onclick='cel(\""+value+"\")'>[失效]</a>");
//		var yiwei = "<a href=\"#\" onclick='moveSeat(\""+value+"\")'>[移位]</a>";
	var yiwei ="";
    if('${orgId}'=='<%=Constant.OEM_ACTIVITIES %>'){
        return String.format(yiwei+"<a href=\"#\" onclick='sel(\""+value+"\")'>[修改]</a>");
    }else{
        return String.format("<a href=\"#\" onclick='sel(\""+value+"\")'>[修改]</a>");
    }

}
function moveSeat(v){
	OpenHtmlWindow(g_webAppName +"/parts/storageManager/partDistributeMgr/PartDistributeMgr/selectLocationInit.do?loc_id="+v,700,400);
}
var LOC_ID = null;
function codeSet(i,c,n){
	var v = i+","+c+","+n;
	var url_1 = g_webAppName +"/parts/baseManager/partsBaseManager/PartLocation/moveSeat.json";
	var params = "RELOC_ID="+i+"&LOC_CODE="+c+"&LOC_NAME="+n+"&LOC_ID="+LOC_ID;		
	makeCall(url_1,moveCallBack,params);
}
function moveCallBack(json){
	if(json.returnValue == 3){
		parent.MyAlert("配件库存为零，不需要做移位操作！");
	}else if(json.returnValue == 1){
		locSearch('normal');
		parent.MyAlert("配件移位操作完成！");
	}else{
		MyAlert("操作失败！请联系系统管理员！");
	}
}
//打开制造商信息修改页面
function sel(value){
	btnDisable();
	var orgId = document.getElementById("orgId").value + ""
	window.location.href='<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/partTypeUpdateInit.do?Id='+value+'&orgId='+orgId;
}
//失效
function cel(value){
    MyConfirm("确定要失效?",celAction,[value]);
}

//有效
function valid(value){
     MyConfirm("确定要有效?",validAction,[value]);
}

function celAction(value){
	btnDisable();
    var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/partNotState.json?Id='+value+'&curPage='+myPage.page;
    makeNomalFormCall(url,handleCel,'fm');
}

function validAction(value){
	btnDisable();
    var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/partEnableState.json?Id='+value+'&curPage='+myPage.page;
    makeNomalFormCall(url,handleCel,'fm');
}

function handleCel(jsonObj) {
	btnEnable();
    if(jsonObj!=null){
       var success = jsonObj.success;
       MyAlert(success);
       __extQuery__(jsonObj.curPage);
    }
}
function partAdd(){
	btnDisable();
	window.location.href = '<%=contextPath %>/parts/baseManager/partsBaseManager/PartLocation/partLocationAddInit.do';
}

//下载
function exportPartStockExcel(){
	document.fm.action="<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/exportPartLocExcel.do";
	document.fm.target="_self";
	document.fm.submit();
}

//变更导出
function exportLcHsExcel(){
	document.fm.action="<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/exportLcHsExcel.do";
	document.fm.target="_self";
	document.fm.submit();
}

//上传
function uploadExcel(){
	btnDisable();
	fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/locImpUpload.do";
	fm.submit();
}

//上传检查和确认信息
function confirmUpload(){
	if(fileVilidate()){
		MyConfirm("确定导入选择的文件?",uploadExcel,[]);
	}
}

function fileVilidate(){
	var whId = document.getElementById("WH_ID").value;
	var msg = "";
	if(whId==""){
		msg += "请先选择仓库!</br>";
	}
	if(msg != ""){
		MyAlert(msg);
		return false;
	}
	var importFileName = $("#uploadFile")[0].value;
	if(importFileName==""){
	    MyAlert("请选择导入文件!");
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
	fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartLocation/exportExcelTemplate.do";
	fm.submit();
}


</script>

</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 基础信息管理 &gt; 配件基础信息维护 &gt; 配件货位关系维护
		</div>
		<form method="post" name="fm" id="fm" method="post" enctype="multipart/form-data">
			<input type="hidden" id="searchType" name="searchType" value="" />
			<input type="hidden" id="orgId" name="orgId" value="${orgId}" />
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">配件编码：</td>
							<td>
								<input class="middle_txt" type="text" name="PART_OLDCODE" />
							</td>

							<!-- 
            <td width="10%"   class="right" >件号：</td>
            <td  ><input class="middle_txt" type="text" name="PART_CODE" /></td>
           -->
							<td class="right">仓库名称：</td>
							<td>
								<select name="WH_ID" id="WH_ID" class="u-select" onchange="__extQuery__(1);">
									<c:forEach var="map" items="${list}">
										<option value="${map.WH_ID}">${map.WH_NAME}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td class="right">货位编码：</td>
							<td>
								<input class="middle_txt" type="text" name="LOC_CODE" />
							</td>
							<td class="right">附属货位：</td>
							<td>
								<input class="middle_txt" type="text" name="SUB_COL" />
							</td>
							<td class="right">&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td class="center" colspan="4">
								<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onClick="locSearch('normal')" />
								<input class="u-button" type="button" value="新 增" onclick="partAdd();" />
								<input class="u-button" type="button" value="导 出" onclick="exportPartStockExcel()" />
								<!-- 		  <input class="normal_btn" type="button" value="变更查询" name="BtnQuery" id="queryBtn" onClick="locSearch('history')"/> -->
								<!-- 		  <input class="normal_btn" type="button" value="变更导出" onclick="exportLcHsExcel()"/> -->
								<!--           <input class="normal_btn" type="button" value="批量导入" id="upload_button" name="button1"onclick="showUpload();"> -->
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div style="display: none; heigeht: 5px" id="uploadDiv">
				<table class="table_query">
					<th colspan="6">
						<img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 上传文件
					</th>
					<tr>
						<td colspan="2">
							<font color="red"> &nbsp;&nbsp;<input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate()" /> 仓库、文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;
							</font>
							<input type="file" name="uploadFile" id="uploadFile" style="width: 250px" datatype="0,is_null,2000" value="" />
							&nbsp;
							<input type="button" id="upbtn" class="normal_btn" value="确 定" onclick="confirmUpload()" />
						</td>
					</tr>
				</table>
			</div>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
</body>
</html>
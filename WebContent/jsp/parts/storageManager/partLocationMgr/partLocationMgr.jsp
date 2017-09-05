<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%String contextPath = request.getContextPath();%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>货位维护</title>
</head>
<body onload="locSearch('normal');"> <!-- onunload='javascript:destoryPrototype()' loadcalendar(); -->
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 仓储相关信息维护 &gt; 货位维护</div>
<form method="post" name ="fm" id="fm" method="post" enctype="multipart/form-data">
	<input type="hidden" id="searchType" name="searchType" value="" />
	<div class="form-panel">
		<h2><img src="/CQZTDMS/jmstyle/img/search-ico.png" class="panel-query-title nav" />查询条件</h2>
		<div class="form-body">
			<table class="table_query">
				<tr>
				<td align="right">库房：</td>
				<td align="left">
					<select name="WH_ID" id="WH_ID" class="short_sel u-select" onchange="changeSub();">
						<c:forEach items="${wareHouseList}" var="wareHouse">
							<option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
						</c:forEach>
					</select>
				</td>
				<td width="10%" align="right">货位编码：</td>
				<td width="20%"><input class="middle_txt" type="text" name="LOC_CODE" /></td>
				<td width="10%" align="right">是否有效：</td>
				<td align="left" class="table_info_2col_input">
					<label>
						<script type="text/javascript">
							genSelBoxExp("STATE",<%=Constant.STATUS%>,"",false,"short_sel u-select","","false",'');
						</script>
					</label>
				</td>
			</tr>
			<tr>
				<td class="txt-center" align="center" colspan="6">
					<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onClick="locSearch('normal');"/>
					<input class="u-button" type="button" value="新 增" onclick="partLocationAdd();"/>  
					<!--  
					<input class="u-button" type="button" value="变更导出" onclick="exportLcHsExcel()"/>
					-->
					<input class="u-button" type="button" value="批量导入" id="upload_button" name="button1"onclick="showUpload('uploadDiv');">
					<input class="u-button" type="button" value="批量更新" onclick="showUpload('uploadUpdateDiv');"/>
					<input class="u-button" type="button" value="变更查询" name="BtnQuery" id="queryBtn" onClick="locSearch('history')"/>
				</td>
			</tr>
			</table>
			<!-- 批量导入 -->
			<div style="display:none ;" id="uploadDiv" class="upload-divide">
				<table class="table_query">
				<th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 上传文件</th>
					<tr>
						<td colspan="2">
							<input type="button" class="u-button" value="下载模版" onclick="exportExcelTemplate()" />
							<font color="red"> 库房、文件选择后,点 [ 确定 ] 按钮,完成上传操作：</font>
						</td>
					</tr>
					<tr>
						<td style="padding-left: 30px;">
							<span>库房：</span>
							<select name="WH_ID_IMP" id="WH_ID_IMP" class="short_sel u-select">
								<c:forEach items="${wareHouseList}" var="wareHouse">
									<option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
								</c:forEach>
							</select>
							<input type="file" name="uploadFile" id="uploadFile" style="width: 250px" datatype="0,is_null,2000" value="" />
							<input type="button" id="upbtn" class="u-button" value="确 定" onclick="confirmUpload()" />
						</td>
					</tr>
				</table>
			</div>
			<!-- 批量更新 -->
			<div style="display:none;" id="uploadUpdateDiv" class="upload-divide">
				<table class="table_query">
				<th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 上传批量更新文件</th>
					<tr>
						<td colspan="2">
							<input type="button" class="long_btn u-button" value="下载批量更新模版" onclick="exportUpdateTemplate()" />
							<font color="red">库房、文件选择后,点 [ 确定 ] 按钮,完成上传操作：</font>
						</td>
					</tr>	
					<tr>
						<td style="padding-left: 30px;">
							<span>库房：</span>
							<select name="WH_ID_UPD" id="WH_ID_UPD" class="short_sel u-select">
							<c:forEach items="${wareHouseList}" var="wareHouse">
									<option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
							</c:forEach>
							</select>
							<input type="file" name="uploadFileUpdate" id="uploadFileUpdate" style="width: 250px" datatype="0,is_null,2000" value="" /> &nbsp;
							<input type="button" id="upbtn" class="u-button" value="确 定" onclick="confirmUploadUpdate();" />
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div></body>

<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/parts/storageManager/partLocation/PartLocation/query.json";	
	var title = null;
	var columns = null;
		      
	function locSearch(parms)
	{
		document.getElementById("searchType").value = parms;
		if("normal" == parms){
			columns = [
					{header: "序号", style: 'text-align:left', renderer:getIndex,width:'7%'},
					{header: "货位编码", dataIndex: 'LOC_CODE', style: 'text-align:center',renderer: modifyCode},
                    //{header: "货位名称", dataIndex: 'LOC_NAME', style: 'text-align:center'},
                    {header: "库房", dataIndex: 'WH_NAME', style: 'text-align:center'},
					{header: "是否有效", dataIndex:'STATE', style: 'text-align:center', renderer: getItemValue},
					{header: "新增日期", dataIndex: 'CREATE_DATE', style: 'text-align:center'}
					//{header: "修改日期", dataIndex: 'UPDATE_DATE', style: 'text-align:center'}
		      ];
		}else if("history" == parms){
			columns = [
					{header: "序号", style: 'text-align:left', renderer:getIndex,width:'7%'},
					{header: "旧货位编码", dataIndex: 'OLD_LOC_CODE', style: 'text-align:center'},
                    {header: "现在货位编码", dataIndex: 'LOC_CODE', style: 'text-align:center'},
					//{header: "旧货位名称", dataIndex: 'OLD_LOC_NAME', style: 'text-align:center'},
                    //{header: "现在货位名称", dataIndex: 'LOC_NAME', style: 'text-align:center'},
					{header: "变更时间", dataIndex: 'CREATE_DATE', style: 'text-align:center'},
					{header: "是否有效", dataIndex:'STATUS', style: 'text-align:center', renderer: getItemValue}
		      ];			
		}
		__extQuery__(1);
	}	      
		      
	function modifyCode(value,meta,records){
		var positionId = records.data.POSITION_ID;
		if(positionId=="" || positionId==null){
			var loc_id = records.data.LOC_ID;
			var whId = records.data.WH_ID;
			var htmlText = "<input type='text' style='background-color:#FF9;text-align:center' id='"+loc_id+"' value='"+value+"' onblur='save1(\""+loc_id+"\",\""+value+"\",\""+whId+"\");'/> ";
			return String.format(htmlText);
		}else{
			return String.format(value);
		}
	}
	function save1(loc_id,loc_code,whId){
		var new_loc_code = document.getElementById(loc_id).value;
		if(new_loc_code == ""){
			document.getElementById(loc_id).value = loc_code;
			MyAlert("修改货位不能为空！");
			return;
		}
		if(new_loc_code != loc_code){
			MyConfirm("确定修改货位编码吗？",save2,[{loc_id:loc_id,old_loc_code:loc_code,whId:whId}]);
		}
	}
	function save2(arr){
		var old_loc_code = arr.old_loc_code;
		var loc_id = arr.loc_id;
		var whId = arr.whId;
		var id = new String(loc_id);
		var new_loc_code = document.getElementById(id).value;
		var url2 = "<%=contextPath%>/parts/storageManager/partLocation/PartLocation/modifyLocCode.json";
		var params = "OLD_LOC_CODE="+old_loc_code+"&NEW_LOC_CODE="+new_loc_code+"&WH_ID="+whId;
		makeCall(url2,cbFunc,params);
	}
	function cbFunc(json){
		if(json.returnValue == 1){
			MyAlert("修改编码成功!");
		}else if(json.returnValue == 2){
			MyAlert("修改编码失败,该货位编码存在!");
		}else{
			MyAlert("修改编码发生异常，请与管理员联系!");
		}
	}
	//下载
	function exportPartStockExcel(){
		document.fm.action="<%=contextPath%>/parts/storageManager/partLocation/PartLocation/exportPartLocExcel.do";
		document.fm.target="_self";
		document.fm.submit();
	}

	//上传
	function uploadExcel(){
		btnDisable();
		fm.action = "<%=contextPath%>/parts/storageManager/partLocation/PartLocation/locImpUpload.do";
		fm.submit();
	}

	//上传检查和确认信息
	function confirmUpload()
	{
		if(fileVilidate("uploadFile"))
		{
			MyConfirm("确定导入选择的文件?",uploadExcel,[]);
		}

	}
	//上传更新检查和确认信息
	function confirmUploadUpdate()
	{
		if(fileVilidate("uploadFileUpdate"))
		{
			MyConfirm("确定导入选择的文件进行更新吗？",uploadUpdate,[]);
		}
	}
	function uploadUpdate(){
		btnDisable();
		fm.action = g_webAppName+"/parts/storageManager/partLocation/PartLocation/impUpdateUpload.do";
		fm.submit();
	}
	
	function fileVilidate(fileId){
		
		var importFileName = $(fileId).value;
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

	function showUpload(divId){
		if(divId=="uploadUpdateDiv"){
			document.getElementById("uploadDiv").style.display = "none";
		}else{
			document.getElementById("uploadUpdateDiv").style.display = "none";
		}
		var uploadDiv = document.getElementById(divId);
		if(uploadDiv.style.display=="block" ){
			uploadDiv.style.display = "none";
		}else {
			uploadDiv.style.display = "block";
		}
	}
	//下载批量更新模板
	function exportUpdateTemplate(){
		fm.action = "<%=contextPath%>/parts/storageManager/partLocation/PartLocation/exportUpdateTemplate.do";
		fm.submit();
	}
	//下载上传模板
	function exportExcelTemplate(){
		fm.action = "<%=contextPath%>/parts/storageManager/partLocation/PartLocation/exportExcelTemplate.do";
		fm.submit();
	}
	function partLocationAdd(){
		window.location.href = g_webAppName+"/parts/storageManager/partLocation/PartLocation/partLocationAdd.do";
	}
	//失效按钮
	function btnDisable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });

	}

	//有效按钮
	function btnEnable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = "";
	    });

	}
</script>
</html>
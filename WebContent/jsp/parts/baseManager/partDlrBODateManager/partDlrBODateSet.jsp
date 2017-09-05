<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>BO有效期设置</title>
<%
	String contextPath = request.getContextPath();
%>
<script type="text/javascript">
    function doInit(){
   		__extQuery__(1);
	}
</script>
</head>
<body onload="doInit()">
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; BO有效期设置</div>
	<form method="post" name="fm" id="fm" method="post" enctype="multipart/form-data">
		<input type="hidden" id="selPartId" name="selPartId" value=""/>
		<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
		<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }"/>
		<div class="form-panel">
			<h2><img src="/CQZTDMS/jmstyle/img/search-ico.png" alt="" class="panel-query-title nav" />查询条件</h2>
			<div class="form-body">
				<table class="table_query">
					<tr >
						<td class="right" width="10%" align="right">服务商编码：</td>
						<td width="20%"><input class="middle_txt" type="text"  name="cldCode" /></td>
						<td class="right" width="10%" align="right">服务商名称：</td>
						<td width="20%" ><input class="middle_txt" type="text"  name="cldName"/></td>
						<td class="right" width="10%" align="right">状态：</td>
						<td width="20%">
						<script type="text/javascript">
							genSelBoxExp("STATE",<%=Constant.STATUS %>,"<%=Constant.STATUS_ENABLE %>",true,"short_sel u-select","","false",'');
						</script>
						</td>
					</tr>
					<tr>
						<td class="txt-center" align="center" colspan="6">
							<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
							<input class="u-button" type="button" value="导 出" onclick="expDlrBODaysExcel()"/>
							<%-- <input class="u-button" type="button" value="批量导入" id="upload_button" name="button1"onclick="showUpload();">--%>
						</td>
					</tr>
				</table>
				<div style="display:none;" id="uploadDiv">
					<table  class="table_query">
					<th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 上传文件</th>
						<tr>
							<td colspan="2"><font color="red"> 
							&nbsp;&nbsp;<input type="button" class="u-button" value="下载模版" onclick="exportExcelTemplate()" />
							文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font> 
							<input type="file" name="uploadFile" id="uploadFile" style="width: 250px" datatype="0,is_null,2000" value="" /> &nbsp; 
							<input type="button" id="upbtn" class="u-button" value="确 定" onclick="confirmUpload()" />
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		<!--分页 end -->
	</form>
</div>	

<script type="text/javascript">
	var myPage;

	var url = "<%=contextPath%>/parts/baseManager/partDlrBODateManager/dlrBoDateSetAction/dlrBoDaysSetSearch.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'RELATION_ID',align: 'center', renderer: getIndex},
                {id:'action',header: "操作",sortable: false,dataIndex: 'RELATION_ID',renderer:myLink},
				{header: "服务商编码", dataIndex: 'CHILDORG_CODE', align:'center'},
				{header: "服务商名称", dataIndex: 'CHILDORG_NAME', style:'text-align:left;'},
				{header: "是否有效", dataIndex:'STATE', align:'center', renderer:getItemValue},
				{header: "BO有效期(月)", dataIndex:'BO_DAYS',renderer:getBOText}

		      ];
	     

	function myLink(value,meta,record){
		return String.format("<a href=\"#\" onclick='updateInfo(\""+value+"\")'>[保存]</a>");
	}

	function getBOText(value,meta,record)
	{
		var rlId = record.data.RELATION_ID;
		return String.format("<input type='text' id='boDays_"+rlId+"' onchange='dataTypeCheck(\"boDays_"+rlId+"\")' value='"+value+"'/>");
	}

	function dataTypeCheck(boTextId){
		var boDaysObj = document.getElementById(boTextId);
		var boDaysVal = boDaysObj.value;
		if(isNaN(boDaysVal)){
			MyAlert("请输入数字!");
			boDaysObj.value = "";
			return false;
		}
		//var re = /^[1-9]+[0-9]*]*$/;
		var re = /^\d+$/;
		if(!re.test(boDaysVal)){
			MyAlert("请输入正整数或0!");
			boDaysObj.value = "";
			return false;
		}
		return true;
	}

	//更新
	function updateInfo(value){
		if(dataTypeCheck("boDays_"+value)){
			MyConfirm("确认保存设置?",updAction,[value]);
		}
	}

	function updAction(value){
		var boDays = document.getElementById("boDays_"+value).value;
		btnDisable();
	    var url = '<%=contextPath%>/parts/baseManager/partDlrBODateManager/dlrBoDateSetAction/updateBoDays.json?rlId='+value+'&boDays='+boDays+'&curPage='+myPage.page;
	    makeNomalFormCall(url,showResult,'fm');
	}

	function showResult(json){
		btnEnable();
		if(null != json){
	        if (json.errorExist != null && json.errorExist.length > 0) {
	        	 MyAlert(json.errorExist);
	        } else if (json.success != null && json.success == "true") {
	        	MyAlert("保存成功!");
	        	__extQuery__(json.curPage);
	        } else {
	            MyAlert("保存失败，请联系管理员!");
	        }
		}
	}
	
	//下载
	function expDlrBODaysExcel(){
		document.fm.action="<%=contextPath%>/parts/baseManager/partDlrBODateManager/dlrBoDateSetAction/expDlrBODaysExcel.do";
		document.fm.target="_self";
		document.fm.submit();
	}

	//上传
	function uploadExcel(){
		btnDisable();
		fm.action = "<%=contextPath%>/parts/baseManager/partPlannerQueryManager/partPlannerQuery/purProUpload.do";
		fm.submit();
	}

	//上传检查和确认信息
	function confirmUpload(){
		if(fileVilidate()){
			MyConfirm("确定导入选择的文件?",uploadExcel,[]);
		}
		
	}

	function fileVilidate(){
		var importFileName = $("uploadFile").value;
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
		fm.action = "<%=contextPath%>/parts/baseManager/partPlannerQueryManager/partPlannerQuery/exportExcelTemplate.do";
		fm.submit();
	}
  </script>
<!--页面列表 end -->
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>备件采购属性维护</title>
<style>#uploadDiv{margin:10px 0}#uploadDiv .table_query,#uploadDiv .table_query th{background-color: transparent}</style>
<%
	String contextPath = request.getContextPath();
%> 
<script type="text/javascript">
	var myPage;

	var url = "<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/PartContractQuery.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'SEQUENCE_ID', renderer:getIndex,style:'text-align:center'},
		        {id: 'action', header: "操作", sortable: false, dataIndex: 'DEF_ID', renderer: myLink,style: 'text-align:center'},
				{header: "合同号", dataIndex: 'CONTRACT_NUMBER', style:'text-align:center'},
				{header: "合同类型", dataIndex: 'CONTRACT_TYPE', style:'text-align:center'},
		        {header: "供应商编码", width: '4%',dataIndex: 'VENDER_CODE',style:'text-align:center'},
		        {header: "供应商名称",width: '4%',dataIndex: 'VENDER_NAME', style:'text-align:center'},
				{header: "备注(来源)",width: '4%',dataIndex: 'REMARK', style:'text-align:center'},
				{header: "合同创建人",width: '4%',dataIndex: 'NAME', style:'text-align:center'},
				{header: "合同有效期起", dataIndex: 'CONTRACT_SDATE', style:'text-align:center'},
				{header: "合同有效期止", dataIndex: 'CONTRACT_EDATE', style:'text-align:center'},
		        {header: "合同是否过期", dataIndex: 'IS_OUT', style:'text-align:center'},
				{header: "是否有效", dataIndex: 'STATE', style:'text-align:center'},
				{header: "是否临时", dataIndex: 'ISTEMP', style:'text-align:center'}
		      ];

	function myLink(value, meta, record) {
		var state = record.data.STATE;
		var partId = record.data.CONTRACT_NUMBER;
		if (state=="无效") {
			return String.format("<a href=\"#\" onclick='enableData(\"" + partId + "\")' id='sel'>[有效]</a>");
		}else {
			return String.format("<a href=\"#\" onclick='updateThing(\"" + partId + "\")'>[修改]</a><a href=\"#\" onclick='cel(\"" + partId + "\")'>[失效]</a>");
		}
	}

	function updateThing(obj){
		document.getElementById("contractnum").value=obj;
		fm.action = '<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/updateByIdInit.do';
		fm.submit();
		document.getElementById("contractnum").value="";
	}

	function searchVender(){
		window.location.href="<%=contextPath%>/jsp/parts/baseManager/PartContractManager/searchVender.jsp";
		<%--OpenHtmlWindow('<%=contextPath%>/jsp/parts/baseManager/PartContractManager/searchVender.jsp', 1100, 600);--%>
	}

	function requery() {
		url = "<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/PartContractQuery.json";
		columns = [
			{header: "序号", dataIndex: 'SEQUENCE_ID', renderer:getIndex,style:'text-align:center'},
			{id: 'action', header: "操作", sortable: false, dataIndex: 'DEF_ID', renderer: myLink,style: 'text-align:center'},
			{header: "合同号", dataIndex: 'CONTRACT_NUMBER', style:'text-align:center'},
			{header: "合同类型", dataIndex: 'CONTRACT_TYPE', style:'text-align:center'},
			{header: "供应商编码", width: '4%',dataIndex: 'VENDER_CODE',style:'text-align:center'},
			{header: "供应商名称",width: '4%',dataIndex: 'VENDER_NAME', style:'text-align:center'},
			{header: "备注(来源)",width: '4%',dataIndex: 'REMARK', style:'text-align:center'},
			{header: "合同创建人",width: '4%',dataIndex: 'NAME', style:'text-align:center'},
			{header: "合同有效期起", dataIndex: 'CONTRACT_SDATE', style:'text-align:center'},
			{header: "合同有效期止", dataIndex: 'CONTRACT_EDATE', style:'text-align:center'},
			{header: "合同是否过期", dataIndex: 'IS_OUT', style:'text-align:center'},
			{header: "是否有效", dataIndex: 'STATE', style:'text-align:center'},
			{header: "是否临时", dataIndex: 'ISTEMP', style:'text-align:center'}
		];
		__extQuery__(1);

	}

	function updateRecord() {
		url = "<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/PartContractQueryHis.json";
		columns = [
			{header: "序号", dataIndex: 'SEQUENCE_ID', renderer:getIndex,style:'text-align:center'},
			{header: "合同号", dataIndex: 'CONTRACT_NUMBER', style:'text-align:center'},
			{header: "合同类型", dataIndex: 'CONTRACT_TYPE', style:'text-align:center'},
			{header: "备件编码", dataIndex: 'PART_OLDCODE', style:'text-align:center'},
			{header: "备件名称", dataIndex: 'PART_CNAME',style:'text-align:center'},
			{header: "备件件号", dataIndex: 'PART_CODE', style:'text-align:center'},
			{header: "合同价", dataIndex: 'CONTRACT_PRICE', style:'text-align:center'},
			{header: "供应商编码", width: '4%',dataIndex: 'VENDER_CODE',style:'text-align:center'},
			{header: "供应商名称",width: '4%',dataIndex: 'VENDER_NAME', style:'text-align:center'},
			{header: "合同有效期起", dataIndex: 'CONTRACT_SDATE', style:'text-align:center'},
			{header: "合同有效期止", dataIndex: 'CONTRACT_EDATE', style:'text-align:center'},
			{header: "修改日期", dataIndex: 'UPDATE_DATE', style:'text-align:center'},
			{header: "修改人", dataIndex: 'NAME', style:'text-align:center'}
		];
		__extQuery__(1);

	}


	function query() {
		url = "<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/rePartContractQuery.json";
		columns = [
			{header: "序号", dataIndex: 'SEQUENCE_ID', renderer:getIndex,style:'text-align:center'},
			//{header: "<input name='cbAll' id='cbAll' onclick='selectAll(this,\"cb\")' type='checkbox'  />", dataIndex: 'ID', align: 'center', renderer: checkLink},
			//{id: 'action', header: "操作", sortable: false, dataIndex: 'DEF_ID', renderer: myLink,style: 'text-align:center'},
			{header: "合同号", dataIndex: 'CONTRACT_NUMBER', style:'text-align:center'},
			{header: "合同类型", dataIndex: 'CONTRACT_TYPE', style:'text-align:center'},
			{header: "备件编码", dataIndex: 'PART_OLDCODE', style:'text-align:center'},
			{header: "备件名称", dataIndex: 'PART_CNAME',style:'text-align:center'},
			{header: "备件件号", dataIndex: 'PART_CODE', style:'text-align:center'},
			{header: "合同价", dataIndex: 'CONTRACT_PRICE', style:'text-align:center'},
			{header: "合同创建人",width: '4%',dataIndex: 'NAME', style:'text-align:center'},
			{header: "供应商编码", width: '4%',dataIndex: 'VENDER_CODE',style:'text-align:center'},
			{header: "供应商名称",width: '4%',dataIndex: 'VENDER_NAME', style:'text-align:center'},
			{header: "合同有效期起", dataIndex: 'CONTRACT_SDATE', style:'text-align:center'},
			{header: "合同有效期止", dataIndex: 'CONTRACT_EDATE', style:'text-align:center'},
			{header: "合同是否过期", dataIndex: 'IS_OUT', style:'text-align:center'},
			{header: "是否有效", dataIndex: 'STATE', style:'text-align:center'}
		];
		__extQuery__(1);
	}
	function  Insert(){
		fm.action = "<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/InsertConInit.do";
		fm.submit();
	}
	function insertInput(value, meta, record) {
		var output = '<input type="text" class="phone_txt" style=" background-color:#FF9;text-align:right"  onblur="checkPrice(this)" datatype="0,is_double,18" decimal="7"  id="CON_PRICE' + record.data.DEF_ID + '" name="CON_PRICE' + record.data.DEF_ID + '"  value="' + value + '" size ="10" style="background-color:#FF9"/>\n';
		return output;
	}

	function checkPrice(obj) {
		if (obj.value == "") {
			MyAlert("价格不能为空!");
			return;
		}
		var patrn = /^((0\.[0-9]*[1-9][0-9]*)|([1-9][0-9]*\.[0-9]+)|([1-9][0-9]*))$/;
		if (!patrn.exec(obj.value)) {
			MyAlert("价格无效,请重新输入!");
			obj.value = "";
			return;
		} else {
			if (obj.value.indexOf(".") >= 0) {
				var patrn = /^[0-9]{0,10}.[0-9]{0,7}$/;
				if (!patrn.exec(obj.value)) {
					MyAlert("价格整数部分不能超过10位,且保留精度最大为7位!");
					obj.value = "";
					return;
				}
			} else {
				var patrn = /^[0-9]{0,10}$/;
				if (!patrn.exec(obj.value)) {
					MyAlert("价格整数部分不能超过10位!");
					obj.value = "";
					return;
				}
			}
		}
	}
	//保存
	function save(){
		var obj=document.getElementsByName("cb");
		var n=0;
		var defId="";
		for(var i=0;i<obj.length;i++){
			if(obj[i].checked){
				var parms=obj[i].value;
				if(n>0){
					defId+=",";
				}
				defId+=parms;
				n++;
			}

		}
		if(n>0){
			if(confirm("确定要保存?")){
				btnDisable();
				var url = '<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/savePartPlanner.json?partId='+defId+'&curPage='+myPage.page;
				makeNomalFormCall(url,showResult,'fm');
			}
		}else{
			MyAlert("你未选中！");


		}
	}
	function exportContractDetilExcel(){
		document.fm.action="<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/expPartPurProExcel1.do";
		document.fm.target="_self";
		document.fm.submit();
	}

	//下载
	function exportPartStockExcel(){
		document.fm.action="<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/expPartPurProExcel.do";
		document.fm.target="_self";
		document.fm.submit();
	}

	//上传
	function uploadExcel(){
		btnDisable();
		fm.action = "<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/purProUpload.do";
		fm.submit();
	}

	//上传检查和确认信息
	function confirmUpload(){
		if(fileVilidate()){
			MyConfirm("确定导入选择的文件?",uploadExcel,[]);
		}
	}

	function fileVilidate(){
		var importFileName = $("#uploadFile").val();
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
		fm.action = "<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/exportExcelTemplate.do";
		fm.submit();
	}
	
	//设置有效
	function enableData(parms){
		MyConfirm("确定要有效?", function(){
			document.getElementById("contractnum").value=parms;
   	     	var url = '<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/enablePartPlanner.json?DefId='+parms+'&curPage='+myPage.page;
   	  		makeNomalFormCall(url,showResult,'fm');
		});
	}

	 //设置失效：
    function cel(parms) {
		MyConfirm("确定要失效?", function(){
			document.getElementById("contractnum").value=parms;
   	     	var url = '<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/celPartPlanner.json?DefId='+parms+'&curPage='+myPage.page;
   	  		makeNomalFormCall(url,showResult,'fm');
		});
    }
    function showResult(json) {
    	btnEnable();
        if (json.errorExist != null && json.errorExist.length > 0) {
            MyAlert(json.errorExist);
        } else if (json.success != null && json.success == "true") {
        	layer.msg("操作成功!", {icon: 1});
        	__extQuery__(json.curPage);
        } else {
            MyAlert("保存失败，请联系管理员!");
        }
    }

	function succAlert() {
		if ('${success}' != '') {
			MyAlert("操作成功");
		}
	}

	//多选框
	function checkLink(value, meta, record) {
		var partID = record.data.DEF_ID;
	    var output = '<input name="cb" type="checkbox" value="' + partID + '"/>';
	    return String.format(output);
	}

	function  checkNumber(obj){
		if(obj.value==null){
			return;
		}
		var pattern = /^(\d){0,10}$/;
		if (!pattern.exec(obj.value)){
			MyAlert("不能输入数字以外的字符.");
			obj.value="";
			return;
		}
		if(obj.value.length>3){
			MyAlert("输入数据有误，请重新输入");
			obj.value="";
			return;
		}

	}
   function  checkXNumber(obj){
	   if(obj.value==""){
		   return;
	   }
	   var patrn = /^[0-9]{0,10}.[0-9]{0,2}$/;
	   if (!patrn.exec(obj.value)) {
		   MyAlert("价格整数部分不能超过10位,且保留精度最大为2位!");
		   obj.value = "";
		   return;
	   }
   }

   $(function(){__extQuery__(1);succAlert();});
</script>

</head>
<body>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
<div class="wbox">
<input type="hidden" id="selPartId" name="selPartId" value=""/>
<input type="hidden" id="CON_NUM" name="CON_NUM" value=""/>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt;供应商管理 &gt;合同管理&gt;查询</div>
    <div class="form-panel">
    <h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
    <div class="form-body">
    <table class="table_query">
		<tr>
			<td class="right">合同编号：</td>
			<td><input class="middle_txt" type="text" name="CONTRACT_NUMBER" /></td>
			<td class="right">合同类型：</td>
			<td>
				<script type="text/javascript">
					genSelBox("CONTRACT_TYPE", <%=Constant.CONTRACT_TYPE%>, "", true, "");
				</script>
			</td>
			<td class="right">合同有效期：</td>
			<td>
				<input name="STARTDATE" type="text" class="middle_txt" id="STARTDATE" value=""/>
			<input name="button2" value=" " type="button" class="time_ico"/>
			</td>
	  </tr>
	  <tr>
			<td class="right">备件编码：</td>
			<td><input class="middle_txt" type="text" name="PART_OLDCODE" /></td>
			<td class="right">备件名称：</td>
			<td><input class="middle_txt" type="text" name="PART_NAME" /></td>
			<td class="right">合同是否过期：</td>
			<td>
				<script type="text/javascript">
					genSelBox("IS_OUT", <%=Constant.IF_TYPE%>, "<%=Constant.IF_TYPE_NO%>", true, "");
				</script>
			</td>
	  </tr>
	<tr>
		<td class="right">供应商编码：</td>
		<td><input class="middle_txt" type="text" name="VENDER_CODE" /></td>
		<td class="right">供应商名称：</td>
		<td><input class="middle_txt" type="text" name="VENDER_NAME" /></td>
		<td class="right">供应商类型：</td>
		<td>
			<script type="text/javascript">
				genSelBox("VENDER_TYPE", <%=Constant.VENDER_TYPE%>, "", true, "");
			</script>
		</td>
	</tr>
	  <tr>
	  <td class="right">是否有效：</td>
	  <td>
			<script type="text/javascript">
				genSelBox("STATE", <%=Constant.STATUS%>, "", true, "");
			</script>
	  </td>
	  <td class="right">是否临时：</td>
	  <td>
			<script type="text/javascript">
				genSelBox("ISTEMP", <%=Constant.IF_TYPE%>, "", true, "", "");
			</script>
	  </td>
	  </tr>

		<tr>
			<td class="center" colspan="6">
				<input class="u-button" type="button" value="合同查询" name="BtnQuery" id="queryBtn" onclick="requery();" />
			<input class="u-button" type="button" value="合同明细查询" id="save_button1" name="button2" onclick="query();">
			<input class="u-button" type="reset" value="重置" onclick="reset()"/>
			<input class="u-button" type="button" value="新增" id="save_button" name="button2" onclick="Insert();">
			<input class="u-button" type="button" value="合同导出" onclick="exportPartStockExcel()"/>
			<input class="u-button" type="button" value="合同明细导出" onclick="exportContractDetilExcel()"/>
          	<input class="u-button" type="button" value="批量导入" id="upload_button" name="button1"onclick="showUpload();">
			<input class="u-button" type="button" value="查询历史" id="sea_button1" name="button2" onclick="updateRecord();">
			<input class="u-button" type="button" value="未签合同查询" id="sea_button2" name="button2" onclick="searchVender();">
			</td>
		</tr>
	  </table>
	  <div style="display:none;" id="uploadDiv" class="upload-divide">
		<table  class="table_query">
		  <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 上传文件</th>
			<tr>
				<td colspan="2"><font color="red"> 
				&nbsp;&nbsp;<input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate()" />
				文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font> 
				<input type="file" name="uploadFile" id="uploadFile" style="width: 250px" datatype="0,is_null,2000" value="" /> &nbsp; 
				<input type="button" id="upbtn" class="normal_btn" value="确 定" onclick="confirmUpload()" />
				</td>
			</tr>
		</table>
	</div>
  	</div>
  	</div>
	<input type="hidden" name="contractnum" id="contractnum" value=""/>

  <!--分页 begin -->
  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
</form>
</div>
<!--页面列表 end -->
</body>
</html>
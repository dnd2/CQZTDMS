<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>合同查询</title>
<%
	String contextPath = request.getContextPath();
%> 
<script type="text/javascript">
	var myPage;

	var url = "<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/PartContractInfo.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'SEQUENCE_ID', renderer:getIndex,style:'text-align: center'},
				{header: "合同号", dataIndex: 'CONTRACT_NUMBER', style:'text-align: center'},
				{header: "合同类型", dataIndex: 'CONTRACT_TYPE', style:'text-align:center'},
		        {header: "供应商编码", width: '4%',dataIndex: 'VENDER_CODE',style:'text-align:center'},
		        {header: "供应商名称",width: '4%',dataIndex: 'VENDER_NAME', style:'text-align:center'},
				{header: "备注(来源)",width: '4%',dataIndex: 'REMARK', style:'text-align:center'},
				{header: "合同创建人",width: '4%',dataIndex: 'NAME', style:'text-align:center'},
				{header: "合同有效期起", dataIndex: 'CONTRACT_SDATE', style:'text-align: center',renderer:formatDate},
				{header: "合同有效期止", dataIndex: 'CONTRACT_EDATE', style:'text-align:center',renderer:formatDate},
		        {header: "合同是否过期", dataIndex: 'IS_OUT', style:'text-align:center'},
		        {header: "是否有效", dataIndex: 'STATE', style:'text-align:center'},
		        {header: "是否临时", dataIndex: 'ISTEMP', style:'text-align:center'}
		      ];

	function requery() {
		url = "<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/PartContractInfo.json";
		columns = [
			{header: "序号", dataIndex: 'SEQUENCE_ID', renderer:getIndex,style:'text-align:center'},
			{header: "合同号", dataIndex: 'CONTRACT_NUMBER', style:'text-align:center'},
			{header: "合同类型", dataIndex: 'CONTRACT_TYPE', style:'text-align:center'},
			{header: "供应商编码", width: '4%',dataIndex: 'VENDER_CODE',style:'text-align:center'},
			{header: "供应商名称",width: '4%',dataIndex: 'VENDER_NAME', style:'text-align:center'},
			{header: "备注(来源)",width: '4%',dataIndex: 'REMARK', style:'text-align:center'},
			{header: "合同创建人",width: '4%',dataIndex: 'NAME', style:'text-align:center'},
			{header: "合同有效期起", dataIndex: 'CONTRACT_SDATE', style:'text-align:center',renderer:formatDate},
			{header: "合同有效期止", dataIndex: 'CONTRACT_EDATE', style:'text-align:center',renderer:formatDate},
			{header: "合同是否过期", dataIndex: 'IS_OUT', style:'text-align:center'},
			{header: "是否有效", dataIndex: 'STATE', style:'text-align:center'},
			{header: "是否临时", dataIndex: 'ISTEMP', style:'text-align:center'}
		];
		__extQuery__(1);

	}

	function  getContractQuery(){
		url = "<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/outTimeContractQuery.json";
		columns = [
			{header: "序号", dataIndex: 'SEQUENCE_ID', renderer:getIndex,style:'text-align:center'},
			{header: "合同号", dataIndex: 'CONTRACT_NUMBER', style:'text-align:center'},
			{header: "合同类型", dataIndex: 'CONTRACT_TYPE', style:'text-align:center',renderer:getItemValue},
			{header: "供应商编码", width: '4%',dataIndex: 'VENDER_CODE',style:'text-align:center'},
			{header: "供应商名称",width: '4%',dataIndex: 'VENDER_NAME', style:'text-align:center'},
			{header: "配件编码",width: '4%',dataIndex: 'PART_OLDCODE', style:'text-align:center'},
			{header: "配件名称",width: '4%',dataIndex: 'PART_CNAME', style:'text-align:center'},
			{header: "备注(来源)",width: '4%',dataIndex: 'REMARK', style:'text-align:center'},
			{header: "合同创建人",width: '4%',dataIndex: 'NAME', style:'text-align:center'},
			{header: "合同有效期起", dataIndex: 'CONTRACT_SDATE', style:'text-align:center',renderer:formatDate},
			{header: "合同有效期止", dataIndex: 'CONTRACT_EDATE', style:'text-align:center',renderer:formatDate}
		];
		__extQuery__(1);
	}

	//格式化日期
	function formatDate(value, meta, record) {
		var output = value.substr(0, 10);
		return output;
	}
	function getVenderQuery(){
		url = "<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/contractSign.json";
		columns = [
			{header: "序号", dataIndex: 'SEQUENCE_ID', renderer:getIndex,style:'text-align:center'},
			{header: "供应商编码", width: '4%',dataIndex: 'VENDER_CODE',style:'text-align:center'},
			{header: "供应商名称",width: '4%',dataIndex: 'VENDER_NAME', style:'text-align:center'}
		];
		__extQuery__(1);
	}

	function myLink(value, meta, record) {
		return String.format("<a href=\"#\" onclick='view(\"" + value + "\")'>[回复]</a>");
	}

	function query() {
		url = "<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/rePartDDContractQuery.json";
		columns = [
			{header: "序号", dataIndex: 'SEQUENCE_ID', renderer:getIndex,style:'text-align:center'},
			{header: "配件编码", dataIndex: 'PART_OLDCODE', style:'text-align:center'},
			{header: "配件名称", dataIndex: 'PART_CNAME',style:'text-align:center'},
			{header: "供应商编码", width: '4%',dataIndex: 'VENDER_CODE',style:'text-align:center'},
			{header: "供应商名称",width: '4%',dataIndex: 'VENDER_NAME', style:'text-align:center'},
			{header: "配件件号", dataIndex: 'PART_CODE', style:'text-align:center'},
			{header: "合同号", dataIndex: 'CONTRACT_NUMBER', style:'text-align:center'},
			{header: "合同类型", dataIndex: 'CONTRACT_TYPE', style:'text-align:center'},
			{header: "合同价", dataIndex: 'CONTRACT_PRICE', style:'text-align:center'},
			{header: "合同创建人",width: '4%',dataIndex: 'NAME', style:'text-align:center'},
			{header: "合同有效期起", dataIndex: 'CONTRACT_SDATE', style:'text-align:center',renderer:formatDate},
			{header: "合同有效期止", dataIndex: 'CONTRACT_EDATE', style:'text-align:center',renderer:formatDate},
			{header: "合同是否过期", dataIndex: 'IS_OUT', style:'text-align:center'},
			{header: "是否有效", dataIndex: 'STATE', style:'text-align:center'},
			{header: "是否临时", dataIndex: 'ISTEMP', style:'text-align:center'}
		];
		__extQuery__(1);
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
		document.fm.action="<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/expPartPurProExcel4.do";
		document.fm.target="_self";
		document.fm.submit();
	}
	function exportConExcel(){
		document.fm.action="<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/expPartPurProExcel5.do";
		document.fm.target="_self";
		document.fm.submit();
	}

	//下载
	function exportPartStockExcel(){
		document.fm.action="<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/expPartPurProExcel2.do";
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
	function confirmUpload()
	{

		if(fileVilidate())
		{
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
		fm.action = "<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/exportExcelTemplate.do";
		fm.submit();
	}
	
	//设置有效
	function enableData(parms)
	{
		if(confirm("确定要有效?")){
   	     	var url = '<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/enablePartPlanner.json?DefId='+parms+'&curPage='+myPage.page;
   	  		makeNomalFormCall(url,showResult,'fm');
   	    }
	}

	 //设置失效：
    function cel(parms) {
    	if(confirm("确定要失效?")){
   	     	var url = '<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/celPartPlanner.json?DefId='+parms+'&curPage='+myPage.page;
   	  		makeNomalFormCall(url,showResult,'fm');
   	    }
    }
    function showResult(json) {
    	btnEnable();
        if (json.errorExist != null && json.errorExist.length > 0) {
            MyAlert(json.errorExist);
        } else if (json.success != null && json.success == "true") {
        	MyAlert("保存成功!"); 
        	__extQuery__(json.curPage);
        } else {
            MyAlert("保存失败，请联系管理员!");
        }
    }

		function succAlert() {
			if ('${success}' != '') {
				MyAlert('${success}');
			}
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

   $(function(){
  		__extQuery__(1);succAlert();
  	});
</script>

</head>
<body、>
<div class="wbox">
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
<input type="hidden" name="contractnum" id="contractnum" value=""/>
<input type="hidden" id="selPartId" name="selPartId" value=""/>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt;  供应商管理  &gt; 合同查询</div>
    <div class="form-panel">
    <h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
    <div class="form-body">
    <table class="table_query">
	  <tr>
		  <td class="right">合同编号：</td>
		  <td><input class="middle_txt" type="text" name="CONTRACT_NUMBER" /></td>
		  <td class="right">合同有效期：</td>
		  <td>
		  		<input name="STARTDATE" type="text" class="middle_txt" id="STARTDATE" value=""/>
			  <input name="button2" value=" " type="button" class="time_ico"/>
		  </td>
		  <td class="right">是否有效：</td>
		  <td>
			  <script type="text/javascript">
				  genSelBox("STATE", <%=Constant.STATUS%>, "", true, "");
			  </script>
		  </td>
	  </tr>
	  <tr>
		  <td class="right">配件编码：</td>
		  <td><input class="middle_txt" type="text" name="PART_OLDCODE" /></td>
		  <td class="right">配件名称：</td>
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
		 <td class="right">是否临时：</td>
		 <td>
			  <script type="text/javascript">
				  genSelBox("ISTEMP", <%=Constant.IF_TYPE%>, "", true, "");
			  </script>
		 </td>
	</tr>
	<tr>
		<td class="center" colspan="6">
		  <input class="u-button" type="button" value="基本合同查询" name="BtnQuery" id="queryBtn" onclick="requery();" />
			<input class="u-button" type="button" value="订单合同查询" id="save_button1" name="button2" onclick="query();">
			<input class="u-button" type="reset" value="重置" onclick="reset()"/>
			<input class="u-button" type="button" value="基本合同导出" onclick="exportPartStockExcel()"/>
			<input class="u-button" type="button" value="订单合同导出" onclick="exportContractDetilExcel()"/>
			<input class="u-button" type="button" value="90天内到期合同" name="BtnQuery" id="queryBtn3" onclick="getContractQuery();" />
			<input class="u-button" type="button" value="90天内到期合同导出" onclick="exportConExcel()"/>
			<input class="u-button" type="button" value="未签订合同供应商" name="BtnQuery" id="queryBtn5" onclick="getVenderQuery();" />
		</td>
	</tr>
  </table>
  </div>
  </div>
  <div style="display:none ; heigeht: 5px" id="uploadDiv">
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

  <!--分页 begin -->
  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
</form>
</div>
</body>
</html>
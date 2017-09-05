<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件包装储运维护</title>
<%
	String contextPath = request.getContextPath();
%>
<script type="text/javascript">
    function doInit(){
   		//loadcalendar();  //初始化时间控件
   		__extQuery__(1);
	}
</script>
</head>
<body onload="doInit()">
<form method="post" name="fm" id="fm" method="post" enctype="multipart/form-data">
<div class="wbox">
<input type="hidden" id="selPartId" name="selPartId" value=""/>
<input type="hidden" id="pkgSizeText" name="pkgSizeText" value=""/>
<input type="hidden" id="oemMinPkgText" name="oemMinPkgText" value=""/>
<input type="hidden" id="minPkgText" name="minPkgText" value=""/>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
基信息管理 &gt; 配件基础信息维护 &gt; 配件包装储运维护</div>
  <table class="table_query">
	<th colspan="6"><img class="nav"
		src="<%=contextPath%>/img/subNav.gif" /> 查询条件</th>
	<tr>
		<td width="10%" align="right">配件编码：</td>
		<td width="20%"><input class="middle_txt" type="text" name="PART_OLDCODE" /></td>
		<td width="10%" align="right">配件名称：</td>
		<td width="20%"><input class="middle_txt" type="text" name="PART_CNAME" /></td>
        <td width="10%" align="right">件号：</td>
        <td width="20%"><input class="middle_txt" type="text" name="PART_CODE"/>
	</tr>
	<tr>
		<td align="center" colspan="6">
		  <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
		  <input class="normal_btn" type="button" value="导 出" onclick="exportPartStockExcel()"/>
          <input class="normal_btn" type="button" value="批量导入" id="upload_button" name="button1"onclick="showUpload();">
		</td>
	</tr>
  </table>
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
<script type="text/javascript">
	var myPage;

	var url = "<%=contextPath%>/parts/baseManager/partPkgStkManager/partPkgStkAction/partPkgStkSearch.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'PART_ID', renderer:getIndex,style:'text-align:left'},
                {id:'action',header: "操作",sortable: false,dataIndex: 'PART_ID',renderer:myLink ,align:'center'},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', style:'text-align:left'},
				{header: "配件名称", dataIndex: 'PART_CNAME',style:'text-align:left'},
                {header: "件号", dataIndex: 'PART_CODE', style:'text-align:left'},
				{header: "包装尺寸", dataIndex: 'PKG_SIZE', style:'text-align:center',renderer: pkgSizeText},
				{header: "最小包装量", dataIndex: 'OEM_MIN_PKG', style:'text-align:center',renderer: oemMinPkgText},
				{header: "整包发运量", dataIndex: 'MIN_PKG', style:'text-align:center',renderer: minPkgText}

		      ];
	     
	//设置超链接
	function myLink(value,meta,record)
	{
		var partID = record.data.PART_ID;
		return String.format("<a href=\"#\" onclick='save(\""+partID+"\")'>[保存]</a>");
	}

	//包装尺寸
	function pkgSizeText(value,meta,record)
	{
		var partID = record.data.PART_ID;
		return String.format("<input type='text' id='pkgSizeText_"+partID+"' name='pkgSizeText_"+partID+"' value=\""+value+"\" />");
	}

	//最小包装量
	function oemMinPkgText(value,meta,record)
	{
		var partID = record.data.PART_ID;
		return String.format("<input type='text' id='oemMinPkgText_"+partID+"' name='oemMinPkgText_"+partID+"' value=\""+value+"\" onchange='dataTypeCheck(this)'/>");
	}

	//整包发运量
	function minPkgText(value,meta,record)
	{
		var partID = record.data.PART_ID;
		return String.format("<input type='text' id='minPkgText_"+partID+"' name='minPkgText_"+partID+"' value=\""+value+"\" onchange='dataTypeCheck(this)'/>");
	}

	//数据验证
	function dataTypeCheck(obj)
	{
		var value = obj.value;
		if("" == value)
		{
			return;
		}
	    if (isNaN(value)) {
	        MyAlert("请输入数字!");
	        obj.value = "";
	        return;
	    }
	    var re = /^[1-9]+[0-9]*]*$/;
	    if (!re.test(obj.value)) {
	        MyAlert("请输入正整数!");
	        obj.value = "";
	        return;
	    }
	}

	//保存
	function save(parms)
	{
		var pkgSizeText = document.getElementById("pkgSizeText_"+parms).value;
		var oemMinPkgText = document.getElementById("oemMinPkgText_"+parms).value;
		var minPkgText = document.getElementById("minPkgText_"+parms).value;

		if(null == pkgSizeText || "null" == pkgSizeText || "" == pkgSizeText)
		{
			MyAlert("包装尺寸不能为空!");
			return false;
		}
		if(null == oemMinPkgText || "null" == oemMinPkgText || "" == oemMinPkgText)
		{
			MyAlert("最小包装量不能为空!");
			return false;
		}

		if(null == minPkgText || "null" == minPkgText || "" == minPkgText)
		{
			MyAlert("整包发运量不能为空!");
			return false;
		}
		if(confirm("确定要保存?")){
			document.getElementById("pkgSizeText").value = pkgSizeText;
			document.getElementById("oemMinPkgText").value = oemMinPkgText;
			document.getElementById("minPkgText").value = minPkgText;
			btnDisable();
   	     	var url = '<%=contextPath%>/parts/baseManager/partPkgStkManager/partPkgStkAction/savePkgStk.json?partId='+parms+'&curPage='+myPage.page;
   	  		makeFormCall(url,showResult,'fm');
   	    }
	}

	//下载
	function exportPartStockExcel(){
		document.fm.action="<%=contextPath%>/parts/baseManager/partPkgStkManager/partPkgStkAction/expPartPurProExcel.do";
		document.fm.target="_self";
		document.fm.submit();
	}

	//上传
	function uploadExcel(){
		btnDisable();
		fm.action = "<%=contextPath%>/parts/baseManager/partPkgStkManager/partPkgStkAction/purProUpload.do";
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
		fm.action = "<%=contextPath%>/parts/baseManager/partPkgStkManager/partPkgStkAction/exportExcelTemplate.do";
		fm.submit();
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
<!--页面列表 end -->
</body>
</html>
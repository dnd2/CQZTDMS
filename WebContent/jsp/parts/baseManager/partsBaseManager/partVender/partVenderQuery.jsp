<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>供应商信息查询</title>

<script type="text/javascript" >
var myPage;
var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartVenderManager/queryPartVenderInfo.json";
var title = null;
var columns = [
            {header: "序号",width:'10%',  renderer:getIndex},
            {id:'action',header: "操作",sortable: true,dataIndex: 'VENDER_ID',renderer:myLink , style:"text-align: center"},
			{header: "供应商编码", dataIndex: 'VENDER_CODE', style:"text-align: center"},
			{header: "供应商名称", dataIndex: 'VENDER_NAME', style:"text-align: center"},
			{header: "开票类型", dataIndex: 'INV_TYPE', style:"text-align: center",renderer:getItemValue},
			{header: "联系人", dataIndex: 'LINKMAN', style:"text-align: center"},
			{header: "联系电话", dataIndex: 'TEL', style:"text-align: center"},
			{header: "修改日期", dataIndex: 'UPDATE_DATE', style:"text-align: center"},
			{header: "修改人", dataIndex: 'ACNT', style:"text-align: center"},
			{header: "是否有效", dataIndex: 'STATE', style:"text-align: center",renderer:getItemValue}

	      ];

// 设置超链接
function myLink(value,meta,record){    
    var state = record.data.STATE;
    if(state=='<%=Constant.STATUS_DISABLE %>'){
        return String.format("<a href=\"#\" onclick='viewVender(\""+value+"\")'>[查看]</a><a href=\"#\" onclick='valid(\""+value+"\")'>[有效]</a>");
    }	    
		return String.format("<a href=\"#\" onclick='viewVender(\""+value+"\")'>[查看]</a><a href=\"#\" onclick='sel(\""+value+"\")'>[修改]</a><a href=\"#\" onclick='cel(\""+value+"\")'>[失效]</a>");
}

// 查看供应商信息
function viewVender(value){
	 window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartVenderManager/viewPartVenderInfo.do?venderId='+value;
}

// 打开供应商信息修改页面
function sel(value)
{
	 window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartVenderManager/queryPartVenderDetail.do?venderId='+value+'&curPage='+myPage.page;
<%--     OpenHtmlWindow('<%=contextPath%>/parts/baseManager/partsBaseManager/PartVenderManager/queryPartVenderDetail.do?venderId='+value+'&curPage='+myPage.page,870,300, '供应商信息维护'); --%>
}

// 下载模板
function exportVenderTemplate() {
    fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartVenderManager/exportVenderTemplate.do";
    fm.submit();
}

// 失效
function cel(value){
    MyConfirm("确定要失效?",celAction,[value]);
}

// 有效
function valid(value){
     MyConfirm("确定要有效?",validAction,[value]);
}

function celAction(value){
    var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartVenderManager/celPartVender.json?venderId='+value+'&curPage='+myPage.page;
   makeNomalFormCall(url,handleCel,'fm');
}

function validAction(value){
    var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartVenderManager/validPartVender.json?venderId='+value+'&curPage='+myPage.page;
    makeNomalFormCall(url,handleCel,'fm');
}

function handleCel(jsonObj) {
  if(jsonObj!=null){
     var success = jsonObj.success;
     layer.msg(success, {icon: 1});
      __extQuery__(jsonObj.curPage);
  }
}
function reloadAction(json){
	turnQuery();
}

function turnQuery() {
	 __extQuery__(1);
	
}

//通过excel导入供应商信息
function uploadExcel() {
    var fileValue = document.getElementById("uploadFile").value;

    if (fileValue == "") {
        MyAlert("请选择文件!");
        return;
    }
    var fi = fileValue.substring(fileValue.length - 3, fileValue.length);
    if (fi != 'xls') {
        MyAlert('导入文件格式不对,请导入xls文件格式');
        return false;
    }
    if(confirm("确定导入?")){
    	fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartVenderManager/uploadVenderExcel.do";
        fm.submit();
    }
}
function showUpload(){
    if($("#uploadTable")[0].style.display == "none"){
        $("#uploadTable")[0].style.display = "block";
    }else {
        $("#uploadTable")[0].style.display = "none";
    }
}

$(function(){__extQuery__(1);});
</script>
</head>
<body>
	<!-- onunload='javascript:destoryPrototype()' -->
	<div class="wbox">
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件管理 &gt;基础信息管理 &gt; 配件基础信息维护 &gt; 供应商信息维护
		</div>
		<form method="post" name="fm" id="fm" enctype="multipart/form-data">
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">供应商编码：</td>
							<td>
								<input class="middle_txt" type="text" name="VENDER_CODE" />
							</td>
							<td class="right">供应商名称：</td>
							<td>
								<input class="middle_txt" type="text" name="VENDER_NAME" />
							</td>
							<td class="right">是否有效：</td>
							<td>
								<script type="text/javascript">
                    				genSelBoxExp("STATE",<%=Constant.STATUS%>,"<%=Constant.STATUS_ENABLE%>", true, "short_sel u-select", "", "false", '');
								</script>
							</td>
						</tr>
						<tr>
							<td class="formbtn-aln" colspan="6" class="center">
								<input name="BtnQuery" id="queryBtn" class="u-button" type="button" value="查 询" onClick="__extQuery__(1)" />
								<input class="u-button" type="button" value="新 增" onclick="window.location.href='<%=contextPath%>/parts/baseManager/partsBaseManager/PartVenderManager/addPartVenderInit.do'" />
								<input name="upload" id="upload" class="u-button" type="button" value="批量导入" onClick="showUpload();" />
							</td>
						</tr>
					</table>
					<table class="table_edit form-upload-bar" id="uploadTable" style="display: none">
						<tr>
							<td>
								<font color="red"> <input type="button" class="u-button" value="模版下载" onclick="exportVenderTemplate()" /> 文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;
								</font>
								<input type="file" name="uploadFile" style="width: 250px" id="uploadFile" value="" />
								&nbsp;
								<input type="button" id="upbtn" class="u-button" value="确定" onclick="uploadExcel()" />
							</td>
						</tr>
					</table>
				</div>
			</div>

			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
</body>
</html>
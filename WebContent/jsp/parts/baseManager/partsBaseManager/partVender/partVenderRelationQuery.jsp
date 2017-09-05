<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>配件主信息维护</title>
<script type="text/javascript" >
var myPage;

var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartVenderManager/partVenderRelationQuery.json";
			
var title = null;

var columns = [
            {header: "序号",width:'10%',  renderer:getIndex},
            {id:'action',header: "操作",sortable: true,dataIndex: 'PART_ID',renderer: myLink , style:"text-align: center"},
			{header: "配件编码", dataIndex: 'PART_CODE', style:"text-align: center", width: '5%'},
	        {header: "配件名称", dataIndex: 'PART_CNAME', style: "text-align: center", width: '5%'},
			{header: "配件类型", dataIndex: 'PRODUCE_STATE', style:"text-align: center",renderer: getItemValue},
			{header: "车型", dataIndex: 'MODEL_NAME', style:"text-align: center"},
			{header: "是否禁用", dataIndex: 'IS_PART_DISABLE', style:"text-align: center", renderer: getItemValue},
			{header: "是否有效", dataIndex: 'STATE', style:"text-align: center", renderer: getItemValue},
			{header: "供货比例", dataIndex: 'COEFF_NUM', style:"text-align: center"} 
	      ];


//设置超链接
function myLink(value,meta,record){    
	return String.format("<a href='#' onclick=\"relationSet('"+value+"')\">[供货比例设置]</a>");
}

// 供货比例设置
function relationSet(partId){
	window.location.href='<%=contextPath%>/parts/baseManager/partsBaseManager/PartVenderManager/partVenderRelationMagInit.do?partId='+partId+'&curPage=' + myPage.page;
}

//下载模板
function exportVenderTemplate() {
    fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartVenderManager/exportVenderTemplate.do";
    fm.submit();
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
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />
&nbsp;当前位置：配件管理 &gt;基础信息管理 &gt; 供应商管理 &gt; 供应商供货比例设置
</div>
<form method="post" name ="fm" id="fm" enctype="multipart/form-data">
<div class="form-panel">
    <h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
    <div class="form-body">
		<table class="table_query">
		    <tr>
		      <td class="right">配件编码：</td>
		      <td><input class="middle_txt" type="text" name="PART_OLDCODE" /></td>
		      <td class="right">配件名称：</td>
		      <td><input class="middle_txt" type="text"  name="PART_CNAME"/></td>
		      <td class="right">是否有效：</td>
		      <td>
		      <script type="text/javascript">
			       genSelBox("STATE",<%=Constant.STATUS%>,"<%=Constant.STATUS_ENABLE%>",true,"","");
			  </script>
		      </td>
	      </tr>
	      <tr>
			<td colspan="6" class="center" >
			  <input name="BtnQuery" id="queryBtn" class="u-button" type="button" value="查 询" onClick="__extQuery__(1);"/>
	       	</td>
		  </tr>
		</table>
	</div>
</div>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>
</div>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>制造商信息查询</title>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(${curPage});loadcalendar();">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：
		基础信息管理 &gt; 配件基础信息维护 &gt; 制造商信息维护
</div>
<form method="post" name ="fm" id="fm" enctype="multipart/form-data">
<input type="hidden" name="curPage" id="curPage" value="${curPage}"/>
	<table class="table_query">
		<th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 查询条件</th>
	    <tr>
	      <td width="10%" class="table_query_right" align="right">制造商编码：</td>
	      <td  width="20%"><input class="middle_txt" type="text" name="MAKER_CODE" /></td>
	      <td width="10%" class="table_query_right" align="right">制造商名称：</td>
	      <td  width="20%"><input class="middle_txt" type="text"  name="MAKER_NAME"/></td>
	      <td  width="10%" class="table_query_right" align="right">是否有效：</td>
	      <td width="20%"  >
	         <script type="text/javascript">
		       genSelBoxExp("STATE",<%=Constant.STATUS %>,"<%=Constant.STATUS_ENABLE%>",true,"short_sel","","false",'');
		     </script>
	     </td>
      </tr>
      <!-- 
	    <tr>
	      <td width="10%" class="table_query_right" align="right" >供应商编码：</td>
	      <td  width="20%"><input class="middle_txt" type="text" name="VENDER_CODE" /></td>
	      <td width="10%" class="table_query_right" align="right"  >供应商名称：</td>
	      <td  width="20%"><input class="middle_txt" type="text"  name="VENDER_NAME"/></td>
      </tr>
       -->
      <tr  >
		<td colspan="6" align="center">
		  <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询" onClick="__extQuery__(1)"/>
		  <input class="normal_btn" type="button" value="新 增" 
		  onclick="window.location.href='<%=contextPath%>/parts/baseManager/partsBaseManager/PartMakerManager/addPartMakderInit.do'"/>
           <input name="upload" id="upload" class="normal_btn" type="button" value="批量导入" onClick="showUpload();"/>
		  </td>
	  </tr>
	</table>
	<table class="table_edit" id="uploadTable" style="display: none">
            <tr>
                <td><font color="red">
                    <input type="button" class="normal_btn" value="模版下载" onclick="exportMakerTemplate()"/>
                    文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font>
                    <input type="file" name="uploadFile" style="width: 250px" id="uploadFile" value=""/>
                    &nbsp;
                    <input type="button" id="upbtn" class="normal_btn" value="确定" onclick="uploadExcel()"/></td>
            </tr>
        </table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

<script type="text/javascript" >
autoAlertException();//输出错误信息
var myPage;
var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartMakerManager/queryPartMakerInfo.json";
			
var title = null;

var columns = [
            {header: "序号",width:'10%',  renderer:getIndex},
            {id:'action',header: "操作",sortable: false,dataIndex: 'MAKER_ID',renderer:myLink , style: 'text-align:left'},
			{header: "制造商编码", dataIndex: 'MAKER_CODE',  style: 'text-align:left'},
			{header: "制造商名称", dataIndex: 'MAKER_NAME',  style: 'text-align:left'},
	//		{header: "供应商编码", dataIndex: 'VENDER_CODE',  style: 'text-align:left'},
	//		{header: "供应商名称", dataIndex: 'VENDER_NAME',  style: 'text-align:left'},
			{header: "联系人", dataIndex: 'LINKMAN',  style: 'text-align:left'},
			{header: "联系电话", dataIndex: 'TEL',  style: 'text-align:left'},
			{header: "修改日期", dataIndex: 'UPDATE_DATE',  style: 'text-align:center'},
			{header: "修改人", dataIndex: 'ACNT',  style: 'text-align:center'},
			{header: "是否有效", dataIndex: 'STATE',  style: 'text-align:center',renderer:getItemValue}

	      ];

//设置超链接  begin      

//设置超链接
function myLink(value,meta,record)
{    
    var state = record.data.STATE;
    if(state=='<%=Constant.STATUS_DISABLE %>'){
        return String.format("<a href=\"#\" onclick='viewMaker(\""+value+"\")'>[查看]</a><a href=\"#\" onclick='valid(\""+value+"\")'>[有效]</a>");
    }	    
		return String.format("<a href=\"#\" onclick='viewMaker(\""+value+"\")'>[查看]</a><a href=\"#\" onclick='sel(\""+value+"\")'>[修改]</a><a href=\"#\" onclick='cel(\""+value+"\")'>[失效]</a>");
//		return String.format("<a href=\"#\" onclick='viewMaker(\""+value+"\")'>[查看]</a><a href=\"#\" onclick='sel(\""+value+"\")'>[修改]</a><a href=\"#\" onclick='cel(\""+value+"\")'>[失效]</a><a href=\"#\" onclick='detail(\""+value+"\")'>[设置配件明细]</a>");
}

//查看制造商信息
function viewMaker(value){
     OpenHtmlWindow('<%=contextPath%>/parts/baseManager/partsBaseManager/PartMakerManager/viewPartMakerInfo.do?makerId='+value,1000,300);
}

//打开制造商信息修改页面
function sel(value)
{
	window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartMakerManager/queryPartMakerDetail.do?makerId='+value+'&curPage='+myPage.page;
}

//下载模板
function exportMakerTemplate() {
    fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartMakerManager/exportMakerTemplate.do";
    fm.submit();
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
   var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartMakerManager/celPartMaker.json?makerId='+value+'&curPage='+myPage.page;
   makeNomalFormCall(url,handleCel,'fm');
}

function validAction(value){
    var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartMakerManager/validPartMaker.json?makerId='+value+'&curPage='+myPage.page;
    makeNomalFormCall(url,handleCel,'fm');
}
//配件制造商设置
function detail(value){
    window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartMakerManager/addPartMakderRelationInit.do?makerId='+value+'&curPage='+myPage.page;
}

function handleCel(jsonObj) {
  if(jsonObj!=null){
     var success = jsonObj.success;
     MyAlert(success);
      __extQuery__(jsonObj.curPage);
  }
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
    fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartMakerManager/uploadMakerExcel.do";
    fm.submit();
}
    function showUpload(){
        if($("uploadTable").style.display == "none"){
            $("uploadTable").style.display = "block";
        }else{
            $("uploadTable").style.display = "none";
        }
    }
</script>
</div>
</body>
</html>
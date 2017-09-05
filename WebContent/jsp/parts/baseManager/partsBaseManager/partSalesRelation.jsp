<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<% String contextPath = request.getContextPath(); %>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>配件采购关系维护</title>

<script type="text/javascript">
var myPage;	
var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesRelation/partQuery.json";
var title = null;
var columns = [
	{header: "序号", align: 'center', renderer: getIndex, width: '7%'},
       {id:'action',header: "操作",sortable: false,dataIndex: 'RELATION_ID',renderer:myLink , style: 'text-align: center'},
	{header: "销售单位代码", dataIndex: 'PARENTORG_CODE',  style: 'text-align: center'},
	{header: "销售单位", dataIndex: 'PARENTORG_NAME',  style: 'text-align: center'},
	{header: "服务商代码", dataIndex: 'CHILDORG_CODE',  style: 'text-align: center'},
	{header: "服务商", dataIndex: 'CHILDORG_NAME',  style: 'text-align: center'},
	{header: "是否有效", dataIndex:'STATE',  style: 'text-align: center', renderer: getItemValue}
];

//设置超链接  begin      

//设置超链接
function myLink(value,meta,record)
{
	 var state = record.data.STATE;
	 var childOrgId = record.data.CHILDORG_ID;
	 var parentId = record.data.PARENTORG_ID;
        if(state=='<%=Constant.STATUS_DISABLE %>'){
            return String.format("<a href=\"#\" onclick='selRel(\""+value+"\")'>[查看下级单位]</a>"+"<a href=\"#\" onclick='valid("+value+","+childOrgId+")'>[有效]</a>"
    	            +"<a href=\"#\" onclick='delRel("+value+","+childOrgId+")'>[删除]</a>");
        }	    
  		return String.format("<a href=\"#\" onclick='selRel(\""+value+"\")'>[查看下级单位]</a>"
  			                  +"<a href=\"#\" onclick='selSun(\""+value+"\")'>[维护下级单位]</a><a href=\"#\" onclick='cel("+value+","+childOrgId+")'>[失效]</a>"
  			                  +"<a href=\"#\" onclick='delRel("+value+","+childOrgId+")'>[删除]</a>");
}
function selSun(value){
	window.location.href = '<%=contextPath %>/parts/baseManager/partsBaseManager/PartSalesRelation/selSun.do?Id='+value+'&curPage='+myPage.page;
	
}

function exportPartSRelationExcel(){
       fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesRelation/exportPartSRelationExcel.do";
       fm.target = "_self";
       fm.submit();
   }

 function exportPartRelationTemp() {
        fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesRelation/exportPartRelationTemp.do";
        fm.submit();
    }

 function uploadExcel() {
        var fileValue = document.getElementById("uploadFile").value;

        if (fileValue == "") {
            layer.msg("请选择文件!", {icon: 15});
            return;
        }
        var fi = fileValue.substring(fileValue.length - 3, fileValue.length);
        if (fi != 'xls') {
            layer.msg('导入文件格式不对,请导入xls文件格式', {icon: 15});
            return false;
        }
        fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesRelation/uploadPartRelExcel.do";
        fm.submit();
    }
    
//打开下级单位查看页面
function selRel(value)
{
	var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesRelation/selRel.do?Id='+value+'&curPage='+myPage.page;
	OpenHtmlWindow(url,700,580);
}
//失效
function cel(value,childOrgId){
    MyConfirm("该单位下的所有下级单位都会失效,确定要失效?",celAction,[[value,childOrgId]]);
}
//删除
function delRel(value,childOrgId){
    MyConfirm("该单位下的所有下级单位都会删除,确定要删除?",delAction,[[value,childOrgId]]);
}

//有效
function valid(value,childOrgId){
     MyConfirm("确定要有效?",validAction,[[value,childOrgId]]);
}

function celAction(paramArr){
	var value = paramArr[0];
	var childOrgId = paramArr[1];
    var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesRelation/partNotState.json?Id='+value+'&curPage='+myPage.page+'&pId='+childOrgId;
    makeNomalFormCall(url,handleCel,'fm');
}

function delAction(paramArr){
	var value = paramArr[0];
	var childOrgId = paramArr[1];
    var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesRelation/deleteRelation.json?Id='+value+'&curPage='+myPage.page+'&pId='+childOrgId;
    makeNomalFormCall(url,handleCel,'fm');
}

function validAction(paramArr){
	var value = paramArr[0];
	var childOrgId = paramArr[1];
    var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesRelation/partEnableState.json?Id='+value+'&curPage='+myPage.page+'&pId='+childOrgId;
    makeNomalFormCall(url,handleCel,'fm');
}

function handleCel(jsonObj) {
  if(jsonObj!=null){
     var success = jsonObj.success;
     MyAlert(success, function(){
		__extQuery__(jsonObj.curPage);
     });
  }
}

function showUpload(){
    if($("#uploadTable")[0].style.display == "none"){
        $("#uploadTable")[0].style.display = "block";
    }else {
        $("#uploadTable")[0].style.display = "none";
    }
}

//选择经销商可以维护时显示新增行
function doCusChange(value){
	if(value==10041002){
		document.getElementById("addBtn").style.display="inline";
	}else if(value==10041001){
		document.getElementById("addBtn").style.display="none";
	}
}
$(function(){
	__extQuery__(1);
});
</script>
</head>
<body> <!-- onunload='javascript:destoryPrototype()' is not defined -->
<div class="wbox">
	<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 配件采购关系维护</div>
	<form method="post" name="fm" id="fm" enctype="multipart/form-data">
		<div class="form-panel">
			<h2>
				<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
			</h2>
			<div class="form-body">
				<table class="table_query" >
					<th colspan="6"></th>
					<tr >
					<td width="10%"  class="table_query_right right" align="right">销售单位：</td>
					<td width="20%"><input class="middle_txt" type="text"  name="FATHER_NAME" /></td>
					<td width="10%" class="table_query_right right" align="right">服务商：</td>
					<td width="20%" ><input class="middle_txt" type="text"  name="SUN_NAME"/></td>
					<td width="10%" class="table_query_right right" align="right">状态：</td>
					<td width="20%">
						<script type="text/javascript">
						genSelBoxExp("STATE",<%=Constant.STATUS %>,"<%=Constant.STATUS_ENABLE %>",true,"short_sel u-select","","false",'');
						</script>
					</td>
				</tr>
				<tr>
					<td class="formbtn-aln" colspan="6" align="center">
						<input name="BtnQuery" id="queryBtn" class="u-button" type="button" value="查 询" onclick="__extQuery__(1);"/>
						<input name="expButton" id="expButton" class="u-button" type="button" value="导出" onclick="exportPartSRelationExcel();"/>
						<input name="upload" id="upload" class="u-button" type="button" value="批量导入" onClick="showUpload();"/>
					</td>
				</tr>
				</table>
			</div>
		</div>
		<table class="table_edit" id="uploadTable"  style="display: none">
			<tr>
				<td>
					<font color="red">
						<input type="button" class="u-button" value="模版下载" onclick="exportPartRelationTemp()"/>
						文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;
					</font>
					<input type="file" name="uploadFile" style="width: 250px" id="uploadFile" value=""/> &nbsp;
					<input type="button" id="upbtn" class="u-button" value="确定" onclick="uploadExcel()"/>
				</td>
			</tr>
		</table>
			
	<!-- 查询条件 end -->
	<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end -->
	</form>   
</div>	

</body>
</html>

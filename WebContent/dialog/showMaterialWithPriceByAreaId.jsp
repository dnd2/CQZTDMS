<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/dyncdtree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/DestoryPrototype.js"></script>
<%
	String ctx = request.getContextPath();
    String inputId = request.getParameter("INPUTID");
    String inputName = request.getParameter("INPUTNAME");
    String isMulti = request.getParameter("ISMULTI");
    String areaId = request.getParameter("areaId");
    String ids = request.getParameter("ids");
    String productId = request.getParameter("productId") ;
    String groupLevel = "5";
    String matType = request.getParameter("matType");
    String typeHide = request.getParameter("typeHide") ;
%>
<script>
<!--
var filecontextPath="<%=ctx%>";
var tree_url = "<%=ctx%>/materialGroup/MaterialGroupTree/materialQueryByAreaId.json";
var inputId="<%=inputId%>";
var inputName="<%=inputName%>";
var isMulti="<%=isMulti%>";
var tree_root_id = {"tree_root_id" : ""};
var subStr = "list";
function createNode(reobj) {
	var prolistobj = reobj[subStr];
	var groupId,parentGroupId,groupName,groupCode,groupLevel;
	a.remove(addNodeId+"_");
	for(var i=0; i<prolistobj.length; i++) {
		groupId = prolistobj[i].groupId;
		groupName = prolistobj[i].groupName;
		groupCode = prolistobj[i].groupCode;
		parentGroupId = prolistobj[i].parentGroupId;
		groupLevel = prolistobj[i].groupLevel;
		a.add(groupId,addNodeId,groupName,groupCode,groupLevel);
		//a.add(groupId+"_",groupId,"loading...","","","",a.icon.loading,"","");
	}
	a.draw();
	$('GROUPLEVEL').value = "<%=groupLevel%>";
} 		
var createModelTree =function(){
	$('GROUPLEVEL').value = 2;
	setValue();
	$('dtree').setStyle("top",29);
	$('dtree').setStyle("left",-1);	
	a.config.closeSameLevel=false;
	a.config.myfun="productPos";
	a.config.folderLinks=true;
	a.delegate=function (id)
	{
		addNodeId = a.aNodes[id].id;	
	    var nodeID = a.aNodes[id].id;
	    $('tree_root_id').value = nodeID;
	    sendAjax(tree_url,createNode,'fm');
	}
	sendAjax(tree_url,createTree,'fm');
	a.closeAll();
 }
 	



function productPos(id) {
	var groupId = a.aNodes[id].id;
	//var proname = a.aNodes[id].name;
	$('groupId').value = groupId;
	setValue();
	__extQuery__(1);
 }
 
function createTree(reobj) {
	var prolistobj = reobj[subStr];
	var groupId,parentGroupId,groupName,groupCode,groupLevel;
	for(var i=0; i<prolistobj.length; i++) {
		groupId = prolistobj[i].groupId;
		groupName = prolistobj[i].groupName;
		groupCode = prolistobj[i].groupCode;
		parentGroupId = prolistobj[i].parentGroupId;
		groupLevel = prolistobj[i].groupLevel;
		if(parentGroupId == "-1") { //系统根节点
			a.add(groupId,"-1",groupName,groupCode,groupLevel);
		} else {
			a.add(groupId,parentGroupId,groupName,groupCode,groupLevel);
			//a.add(groupId+"_",groupId,"loading...","","","",a.icon.loading,"","");
		}
	}
	a.draw();
	$('GROUPLEVEL').value = "<%=groupLevel%>";
 }
function setValue()
{
	$('INPUTID').value = inputId;
	$('INPUTNAME').value = inputName;
	$('ISMULTI').value = isMulti;
	$('GROUPLEVEL').value = 2;
}
//--
</script>
</head>
<body onunload='javascript:destoryPrototype();' onload="createModelTree(1);" >
	<div id='dtree' class="dtree" style=" margin:2px;float:left;border-right:solid 1px;">
	    <script type="text/javascript">
	    	a = new dTree('a','dtree','false','false','true',<%=groupLevel%>,'<%=ctx%>/materialGroup/MaterialGroupTree/querySubMaterialList.json');
	    </script>
	</div>
	  <div style="float:right;">
	 <form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="groupId" name="groupId" value="" />
		<input type="hidden" id="INPUTID" name="INPUTID" value="" />
		<input type="hidden" id="INPUTNAME" name="INPUTNAME" value="" />
		<input type="hidden" id="ISMULTI" name="ISMULTI" value="" />
		<input type="hidden" id="productId" name="productId" value="<%=productId %>" />
		<input type="hidden" id="areaId" name="areaId" value="<%=areaId%>" />
		<input type="hidden" id="ids" name="ids" value="<%=ids%>" />
		<input type="hidden" id="GROUPLEVEL" name="GROUPLEVEL" value="" />
		<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
		<table class="table_query" >
			<tr>
			 <%if("false".equals(typeHide)){%>
			 <td class="table_query_label" nowrap="nowrap">物料类型：</td>
			    <td class="table_query_input" nowrap="nowrap">
			    	<script type="text/javascript">
				      	genSelBoxExp("mat_type",<%=Constant.MAT_TYPE%>,<%=matType%>,false,"min_sel","","false",'');
				    </script>
			    </td>
			    <%}else{ %>
				    <td class="table_query_label" nowrap="nowrap"></td>
				    <td class="table_query_input" nowrap="nowrap">
			    </td>
			    <%} %>
				<td nowrap="nowrap" class="table_query_label">物料代码：</td>
				<td class="table_query_input"   nowrap="nowrap">
					<input 
					id="materialCode" name="materialCode" class="middle_txt" type="text" />				
				</td>
				<td class="table_query_label" nowrap="nowrap" >物料名称：</td>
				<td class="table_query_input" nowrap="nowrap">
					<input 
					id="materialName" name="materialName" class="middle_txt" type="text" />				
				</td>
				<td class="table_query_label" nowrap="nowrap" >资源情况：</td>
				<td class="table_query_input" nowrap="nowrap">
					<select id="resAmount" name="resAmount">
						<option value="-1">--请选择--</option>
						<option value="1">有</option>
						<option value="0">无</option>
					</select>		
				</td>
		    </tr>
			<tr align="left">
			  <td colspan="8" nowrap="nowrap" class="table_query_label" style="text-align: center;">
			  	<table width="100%" border="0">
	                <tr>
	                  <td><input name="button2" type="button" class="normal_btn" id="queryBtn" onclick="goToSelect();" value="查 询"/></td>
	                  <td><input name="button" type="button" class="normal_btn" onclick="window.close();" value="关 闭" /></td>
	                  <td><input name="reset" type="button" class="normal_btn" onclick="clsTxt();" value="清 除" /></td>
	                </tr>
              	</table>
              </td>
		  </tr>
	   </table>
			<jsp:include page="${ctx}/queryPage/orderHidden.html" />
	   </form>
		<jsp:include page="${ctx}/queryPage/pageDiv.html" />
		</div>
<script type="text/javascript">
<!--

var url = "<%=ctx%>/materialGroup/MaterialGroupTree/materialListQueryWithPriceByAreaId.json";
//设置表格标题
var title= null;
var page
//设置列名属性
var columns = [
				{header: "选择",   dataIndex: 'MATERIAL_ID',  width:"50px",renderer:myLink},
				{header: "资源情况",dataIndex: 'VEHICLE_AMOUNT'},
				{header: "物料代码",dataIndex: 'MATERIAL_CODE',style:"text-align:left;",renderer:myTitle},
				{header: "物料名称",dataIndex: 'MATERIAL_NAME',style:"text-align:left;"},
				{header: "物料价格",dataIndex: 'PRICE',style:"text-align:left;",renderer: function(value){return amountFormat(value);}}
		      ];
function myLink(value,meta,rec){
	var data = rec.data;
	if($('ISMULTI').value=='true')
	{
		
		return "<input type='checkbox' name='cb' id=\""+data.MATERIAL_ID+"\"  value=\""+data.MATERIAL_CODE+"\" />"
    }else
    {
    	return "<input type='radio' name='rd' onclick='setModel(\""+data.MATERIAL_ID+"\",\""+data.MATERIAL_NAME+"\",\""+data.MATERIAL_CODE+"\")' />"  
    }
}
//显示title
function  myTitle(value,meta,rec){
	
	return "<span title='配置："+rec.data.ERP_PACKAGE+"\n装备状态:"+rec.data.ERP_NAME+"\n说明："+rec.data.REMARK2+"'>"+value+"</span>";
}
var setModel = function (materialId,materialName,materialCode){
    //modified by LQ,接口请求没有'inIframe',这里需要判断
    var parentWin = window.dialogArguments;
    parentWin.$(inputId).value = materialCode;
    parentWin.addMaterial();
    var sels = document.getElementById('ids').value;
    sels = sels == "" ? materialId : sels + "," + materialId;
    document.getElementById('ids').value = sels;
    __extQuery__(1);
	// parent._hide();
}
function setCheckModel(){
	var reCode="";
	var materialCheckBoxs=document.getElementsByName("cb");
	if(!materialCheckBoxs)return;
	for(var i=0;i<materialCheckBoxs.length;i++)
	{
		if(materialCheckBoxs[i].checked)
		{
			if(reCode && reCode.length > 0)
				reCode += "," + materialCheckBoxs[i].value;
			else
				reCode = reCode+materialCheckBoxs[i].value;
	    }
	}
	if(parent.$('inIframe'))
	{
		parentDocument.getElementById(inputId).value=reCode;
		parentContainer.addMaterial();
		_hide();	
	}else
	{
		parent.$(inputId).value=reCode;
		parent.addMaterial();
		parent._hide();
	}
}
function clsTxt(){
	parentDocument.getElementById(inputId).value="";
	_hide();
}
function checkAll(){
	var materialCheckBoxs=document.getElementsByName("cb");
	if(!materialCheckBoxs) return;
	for(var i=0;i<materialCheckBoxs.length;i++)
	{
		materialCheckBoxs[i].checked=true;
	}
}
function goToSelect(){
	$('groupId').value="";
	  setValue();
	__extQuery__(1);
}

function reflushQuery(dataArray) {
	var selected = '';
	if(dataArray) {
		for(i=0;i<dataArray.length;i++) {
			selected += dataArray[i][0] + ',';
		}
	}
	selected = selected.substring(0, selected.length-1);
	
	document.getElementById('ids').value = selected;
	
	__extQuery__(1);
}
//--
</script>	
</body>
</html>
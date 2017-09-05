<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
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
    String gids = request.getParameter("gids");
    String productId = request.getParameter("productId") ;
    String groupLevel = "5";
%>
<script>
var inputId="<%=inputId%>";
var inputName="<%=inputName%>";
var isMulti="<%=isMulti%>";
var gids="<%=gids%>";
<!--
<%-- var filecontextPath="<%=ctx%>";
var tree_url = "<%=ctx%>/materialGroup/MaterialGroupTree/materialQueryByAreaIdAndGroupId.json";
var inputId="<%=inputId%>";
var inputName="<%=inputName%>";
var isMulti="<%=isMulti%>";
var gids="<%=gids%>";
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
} --%>
//--
</script>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);" >
<%-- <div id='dtree' class="dtree" style=" margin:2px;float:left;border-right:solid 1px;">
       <script type="text/javascript">
        a = new dTree('a','dtree','false','false','true',<%=groupLevel%>,'<%=ctx%>/materialGroup/MaterialGroupTree/querySubMaterialList.json');
        </script>
    </div> --%>
	 <div style="float:right;">
	 <form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="groupId" name="groupId" value="" />
		<input type="hidden" id="INPUTID" name="INPUTID" value="<%=inputId%>" />
		<input type="hidden" id="INPUTNAME" name="INPUTNAME" value="<%=inputName%>" />
		<input type="hidden" id="ISMULTI" name="ISMULTI" value="<%=isMulti%>" />
		<input type="hidden" id="productId" name="productId" value="<%=productId %>" />
		<input type="hidden" id="areaId" name="areaId" value="<%=areaId%>" />
		<input type="hidden" id="gids" name="gids" value="<%=gids%>" />
		<input type="hidden" id="GROUPLEVEL" name="GROUPLEVEL" value="" />
		<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
		<table class="table_query" >
			<tr>
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
		    </tr>
			<tr align="left">
			  <td colspan="4" nowrap="nowrap" class="table_query_label"><table width="100%" border="0">
                <tr>
                  <td><input name="button2" type="button" class="normal_btn" 
                  		id="queryBtn" onclick="goToSelect();" value="查 询"/></td>
                  <td><input name="button" type="button" class="normal_btn" onclick="_hide();" value="关 闭" /></td>
                  <td><input name="reset" type="button" class="normal_btn" onclick="clsTxt();" value="清 除" /></td>
                  <%if("true".equals(isMulti)){%>
                  <td><input name="queren" type="button" class="normal_btn" onclick="setCheckModel();" value="确认" /></td>
                  <td><input name="queren" type="button" class="normal_btn" onclick="checkAll();" value="全选" /></td>
                  <%}%>
                  
                </tr>
              </table></td>
		  </tr>
	   </table>
			<jsp:include page="${ctx}/queryPage/orderHidden.html" />
	   </form>
		<jsp:include page="${ctx}/queryPage/pageDiv.html" />
		</div>
<script type="text/javascript">
<!--

var url = "<%=ctx%>/materialGroup/MaterialGroupTree/materialListQueryByAreaIdAndGroupId.json";
//设置表格标题
var title= null;
//设置列名属性



var columns = [
				{header: "选择",   dataIndex: 'MATERIAL_ID',  width:"50px",renderer:myLink},
				{header: "物料代码",dataIndex: 'MATERIAL_CODE',width:"80px"},
				{header: "物料名称",dataIndex: 'MATERIAL_NAME'}
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
var setModel = function (materialId,materialName,materialCode){
    //modified by LQ,接口请求没有'inIframe',这里需要判断
	if (parent.$('inIframe')) {
		parentDocument.getElementById(inputId).value = materialCode;
		parentContainer.addMaterial();
	} else {
		parent.$(inputId).value = materialCode;
		parent.addMaterial();
	}
	parent._hide();
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
	 /*  setValue(); */
	__extQuery__(1);
}
//--
</script>	
</body>
</html>
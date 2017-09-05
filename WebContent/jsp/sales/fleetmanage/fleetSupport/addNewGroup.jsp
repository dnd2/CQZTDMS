<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/dtree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/DestoryPrototype.js"></script>
<%
	String ctx = request.getContextPath();
    String groupIds = request.getParameter("GROUPIDS");
    String groupLevel = request.getParameter("GROUPLEVEL");
%>
<script>
<!--
var filecontextPath="<%=ctx%>";
var tree_url = "<%=ctx%>/sales/fleetmanage/fleetSupport/FleetSupportApply/materialGroupQuery.json";
var groupIds="<%=groupIds%>";
var groupLevel="<%=groupLevel%>";
var tree_root_id = {"tree_root_id" : ""};
var subStr = "list";
function createNode(reobj) {
	var prolistobj = reobj[subStr];
	var groupId,parentGroupId,groupName,groupCode;
	a.remove(addNodeId+"_");
	for(var i=0; i<prolistobj.length; i++) {
		groupId = prolistobj[i].groupId;
		groupName = prolistobj[i].groupName;
		groupCode = prolistobj[i].groupCode;
		parentGroupId = prolistobj[i].parentGroupId;
		a.add(groupId,addNodeId,groupName,groupCode);
		//a.add(groupId+"_",groupId,"loading...","","","",a.icon.loading,"","");
	}
	a.draw();
} 		
var createModelTree =function(){
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
	var groupId,parentGroupId,groupName,groupCode;
	for(var i=0; i<prolistobj.length; i++) {
		groupId = prolistobj[i].groupId;
		groupName = prolistobj[i].groupName;
		groupCode = prolistobj[i].groupCode;
		parentGroupId = prolistobj[i].parentGroupId;
		if(parentGroupId == "-1") { //系统根节点
			a.add(groupId,"-1",groupName,groupCode);
				
		} else {
			a.add(groupId,parentGroupId,groupName,groupCode);
			//a.add(groupId+"_",groupId,"loading...","","","",a.icon.loading,"","");
		}
	}
	a.draw();
	goToSelect();
 }
function setValue()
{
	$('GROUPIDS').value = groupIds;
	$('GROUPLEVEL').value = groupLevel;
}
//--
</script>
</head>
<body onunload='javascript:destoryPrototype()' onload="createModelTree(1);" >
<div id='dtree' class="dtree" style=" margin:2px;float:left;border-right:solid 1px;">
        <script type="text/javascript">
        a = new dTree('a','dtree','false','false','true');
        </script>
    </div>
	 <div style="float:right;">
	 <form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="groupId" name="groupId" value="" />
		<input type="hidden" id="GROUPIDS" name="GROUPIDS" value="" />
		<input type="hidden" id="GROUPLEVEL" name="GROUPLEVEL" value="" />
		<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
		<table class="table_query" >
			<tr>
				<td nowrap="nowrap" class="table_query_label">物料组代码：<input 
					id="groupCode" name="groupCode" class="middle_txt" type="text" /></td>
				<td class="table_query_label" nowrap="nowrap" >物料组名称：<input 
					id="groupName" name="groupName" class="middle_txt" type="text" /></td>
				 <td  align="right"><input name="button2" type="button" class="cssbutton" onclick="goToSelect();" value="查 询"/>
                  <input name="button" type="button" class="cssbutton" onclick="_hide();" value="关 闭" /></td>
		    </tr>
		    </table>
			<jsp:include page="${ctx}/queryPage/orderHidden.html" />
			<jsp:include page="${ctx}/queryPage/pageDiv.html" />	
	   </form>
		</div>
		
		
<script type="text/javascript">
<!--

var url = "<%=ctx%>/sales/fleetmanage/fleetSupport/FleetSupportApply/groupListQuery.json";
//设置表格标题
var title= null;
//设置列名属性



var columns = [
                {header: "序号", align:'center', renderer:getIndex,width:"5px"},
				{header: "选择",   dataIndex: 'GROUP_ID', width:"10px",renderer:myLink},
				{header: "物料组代码",dataIndex: 'GROUP_CODE',width:"40px"},
				{header: "物料组名称",dataIndex: 'GROUP_NAME',width:"50px"}
		      ];
function myLink(value,meta,rec){
	var data = rec.data;
    return "<input type='radio' name='rd' onclick='setModel(\""+data.GROUP_ID+"\",\""+data.GROUP_NAME+"\",\""+data.GROUP_CODE+"\")' />"  
}
var setModel = function (groupId,groupName,groupCode){
	parent.$('inIframe').contentWindow.addRow(groupId,groupName,groupCode);
	_hide();
}
function clsTxt(){
	parent.$('inIframe').contentWindow.$(inputId).value="";
	_hide();
}
function goToSelect(){
	$('groupId').value="";
	  setValue();
	__extQuery__(1);
	
}
function getIndex(){}
//--
</script>	
</body>
</html>
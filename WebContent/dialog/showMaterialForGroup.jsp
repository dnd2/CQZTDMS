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
    String group_id = request.getParameter("group_id");
    String inputName = request.getParameter("INPUTNAME");
    String inputColorName = request.getParameter("inputColorName");
    String isMulti = request.getParameter("ISMULTI");
    String isAllArea = request.getParameter("ISALLAREA");
    String groupLevel = "5";
%>
<script>
<!--
var filecontextPath="<%=ctx%>";
var tree_url = "<%=ctx%>/materialGroup/MaterialGroupTree/materialQuery.json";
var group_id="<%=group_id%>";
var inputName="<%=inputName%>";
var isMulti="<%=isMulti%>";
var isAllArea="<%=isAllArea%>";
var inputColorName="<%=inputColorName%>";
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
	goToSelect();
 }
function setValue()
{
	$('group_id').value = group_id;
	$('INPUTNAME').value = inputName;
	$('ISMULTI').value = isMulti;
	$('ISALLAREA').value = isAllArea;
	$('inputColorName').value = inputColorName;
	$('GROUPLEVEL').value = 2;
}
//--
</script>
</head>
<body onunload='javascript:destoryPrototype()' onload="createModelTree(1);" >
<div id='dtree' class="dtree" style=" margin:2px;float:left;border-right:solid 1px;">
        <script type="text/javascript">
        a = new dTree('a','dtree','false','false','true',<%=groupLevel%>,'<%=ctx%>/materialGroup/MaterialGroupTree/querySubMaterialList.json');
        </script>
    </div>
	 <div style="position:relative;float:right;
					clear:both;
					overflow-x:hidden;
					overflow-y:hidden;
					display:inline;
					scrollbar-3dlight-color:#595959;
					scrollbar-arrow-color:#CCCCCC;
					scrollbar-base-color:#CFCFCF;
					scrollbar-darkshadow-color:#FFFFFF;
					scrollbar-face-color:#F3F4F8;
					scrollbar-highlight-color:#FFFFFF;
					scrollbar-shadow-color:#595959;">
	 <form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="groupId" name="groupId" value="" />
		<input type="hidden" id="group_id" name="group_id" value="" />
		<input type="hidden" id="INPUTNAME" name="INPUTNAME" value="" />
		<input type="hidden" id="ISMULTI" name="ISMULTI" value="" />
		<input type="hidden" id="ISALLAREA" name="ISALLAREA" value="" />
		<input type="hidden" id="inputColorName" name="inputColorName" value="" />
		<input type="hidden" id="GROUPLEVEL" name="GROUPLEVEL" value="" />
		<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
		<table class="table_query" >
			<tr>
				<td nowrap="nowrap" class="table_query_label">配置名称：<input 
					id="materialCode" name="materialCode" class="middle_txt" type="text" /></td>
				 <td><input name="button2" type="button" class="cssbutton" 
                  		id="queryBtn1" onclick="goToSelect();" value="查 询"/>&nbsp;
                  		<input name="button" type="button" class="cssbutton" onclick="_hide();" value="关 闭" /></td>
		    </tr>
	   </table>
			<jsp:include page="${ctx}/queryPage/orderHidden.html" />
			<jsp:include page="${ctx}/queryPage/pageDiv.html" />
	   </form>
		<div style="margin-top:25px;float: left" id="sel">
           <input name="queren1" type="button" class="cssbutton" onclick="checkAll();" value="全选" />
           <input class="cssbutton" type="button" name ="queren2" value="全不选" onclick="doDisCheckAll()"/>
           <input name="queren3" type="button" class="cssbutton" onclick="setCheckModel();" value="确认" />               
		</div>
		<script language="JavaScript">
	    	if(isMulti == "true")
		    	document.getElementById("sel").style.display = "";
	    	else
	    	{
	    		document.getElementById("sel").style.display = "none";
	    	}	
	    </script>
		</div>
<script type="text/javascript">
<!--

var url = "<%=ctx%>/materialGroup/MaterialGroupTree/QueryPackageName.json";
//设置表格标题
var title= null;
//设置列名属性



var columns = [
                {header: "序号", align:'center', renderer:getIndex,width:"5px"},
				{header: "配置名称",dataIndex: 'PACKAGE_NAME',  width:"10px",renderer:myLink}
		      ];
function myLink(value,meta,rec){
	var data = rec.data;
	if($('ISMULTI').value=='true'){
		
		return "<input type='checkbox' name='cb' id=\""+data.PACKAGE_ID+"\"  value=\""+data.PACKAGE_NAME+"\" />";
    }else
    {
    	return "<input type='radio' name='rd' value=\""+data.PACKAGE_NAME+"\"  onclick='setPackageName(\""+data.PACKAGE_NAME+"\",\""+data.PACKAGE_ID+"\")'>"+data.PACKAGE_NAME+"</input>";
    }
}
var setPackageName = function (COLOR_NAME,id){
	//add by zhaojinyu, 区分系统登录和接口登录,接口登录没有 parent.$('inIframe'),需要判断
	if (parent.$('inIframe')) 
	{
		parentContainer.setValue(COLOR_NAME,inputColorName);
		parentContainer.setID(id,inputName);
		_hide();
	}else
	{
		parent.$(group_id).value = materialCode;
		if(inputName != "null" && inputName != ""){
			parent.$(inputName).value = materialName;
		}
		parent._hide();
	}
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
	
	//add by zhaojinyu, 区分系统登录和接口登录,接口登录没有 parent.$('inIframe'),需要判断
	if (parent.$('inIframe')) 
	{
		parentDocument.getElementById(group_id).value=reCode;
		parent.$(group_id).value=reCode;
		_hide();
	}else
	{
		parent.$(group_id).value=reCode;
		parent._hide();	
	}
}
function checkAll(){
	var materialCheckBoxs=document.getElementsByName("cb");
	if(!materialCheckBoxs) return;
	for(var i=0;i<materialCheckBoxs.length;i++)
	{
		materialCheckBoxs[i].checked=true;
	}
}
function doDisCheckAll(){
	var groupCheckBoxs=document.getElementsByName("cb");
	if(!groupCheckBoxs) return;
	for(var i=0;i<groupCheckBoxs.length;i++)
	{
		groupCheckBoxs[i].checked=false;
	}
}
function goToSelect(){
	$('groupId').value="";
	  setValue();
	__extQuery__(1);
}
function doInit(){
	$('groupId').value="";
	  setValue();
	__extQuery__(1);
}
//--
</script>	
</body>
</html>
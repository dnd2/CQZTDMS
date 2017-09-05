<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<link href="<%=request.getContextPath()%>/style/dtree1.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/dyncdtree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/DestoryPrototype.js"></script>
<%
	String ctx = request.getContextPath();
    String inputId = request.getParameter("INPUTID");
    String inputName = request.getParameter("INPUTNAME");
    String isMulti = request.getParameter("ISMULTI");
    String isAllArea = request.getParameter("ISALLAREA");
    String ids = request.getParameter("IDS");
    String groupLevel = request.getParameter("GROUPLEVEL");
		if("".equals(groupLevel)||groupLevel==null){
    		 groupLevel = "5";
		}
%>
<script>
<!--
var filecontextPath="<%=ctx%>";
var tree_url = "<%=ctx%>/materialGroup/MaterialGroupTree/materialGroupByAddOrderQuery.json";
var inputId="<%=inputId%>";
var inputName="<%=inputName%>";
var ids="<%=ids%>";
var isMulti="<%=isMulti%>";
var isAllArea="<%=isAllArea%>";
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
var createModelTree =function(){
	//$('GROUPLEVEL').value = 5;
	document.getElementById("GROUPLEVEL").value=5;
	setValue();
	//$('dtree').setStyle("top",29);
	//$('dtree').setStyle("left",-1);	
	document.getElementById("dtree").style.top="29";
	document.getElementById("dtree").style.left="-1";	
	a.config.closeSameLevel=false;
	a.config.myfun="productPos";
	a.config.folderLinks=true;
	a.delegate=function (id)
	{
		addNodeId = a.aNodes[id].id;	
	    var nodeID = a.aNodes[id].id;
	    //$('tree_root_id').value = nodeID;
	    document.getElementById("tree_root_id").value= nodeID;
	    sendAjax(tree_url,createNode,'fm');
	}
	sendAjax(tree_url,createTree,'fm');
	//a.closeAll();
 }
 	



function productPos(id) {
	var groupId = a.aNodes[id].id;
	//var proname = a.aNodes[id].name;
	//$('groupId').value = groupId;
	document.getElementById("groupId").value = groupId;
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
	/* $('INPUTID').value = inputId;
	$('INPUTNAME').value = inputName;
	$('ISMULTI').value = isMulti;
	$('ids').value = ids;
	$('ISALLAREA').value = isAllArea;
	$('GROUPLEVEL').value = 5; */
	document.getElementById("INPUTID").value= inputId;
	document.getElementById("INPUTNAME").value= inputName;
	document.getElementById("ISMULTI").value = isMulti;
	document.getElementById("ids").value = ids;
	document.getElementById("ISALLAREA").value = isAllArea;
	document.getElementById("GROUPLEVEL").value = 5;
}
//--
</script>
</head>
<body onunload='javascript:destoryPrototype()' onload="createModelTree(1);" >
<div class="wbox">
<div id='dtree' class="dtree" style=" margin:2px;float:left;border-right:solid 1px;width:30%;">
        <script type="text/javascript">
        a = new dTree('a','dtree','false','false','true',<%=groupLevel%>,'<%=ctx%>/materialGroup/MaterialGroupTree/querySubMaterialList.json');
        </script>
    </div>
	 <div style="float:left;width:69%;
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
		<input type="hidden" id="INPUTID" name="INPUTID" value="" />
		<input type="hidden" id="INPUTNAME" name="INPUTNAME" value="" />
		<input type="hidden" id="ISMULTI" name="ISMULTI" value="" />
		<input type="hidden" id="ids" name="ids" value="" />
		<input type="hidden" id="ISALLAREA" name="ISALLAREA" value="" />
		<input type="hidden" id="GROUPLEVEL" name="GROUPLEVEL" value="" />
		<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
        <input type="hidden" id="hideCheckedMaterialId"/>
		<table class="table_query" >
			<tr>
				<td nowrap="nowrap" class="table_query_label">物料代码：<input 
					id="materialCode" name="materialCode" class="middle_txt" type="text" /></td>
				<td class="table_query_label" nowrap="nowrap" >物料名称：<input 
					id="materialName" name="materialName" class="middle_txt" type="text" /></td>
				 <td><input name="button2" type="button" class="cssbutton" 
                  		id="queryBtn1" onclick="goToSelect();" value="查 询"/>&nbsp;<input name="button" type="button" class="cssbutton" onclick="_hide();" value="关 闭" /></td>
		    </tr>
	   </table>
			<jsp:include page="${ctx}/queryPage/orderHidden.html" />
			<jsp:include page="${ctx}/queryPage/pageDiv.html" />
		<div style="margin-top:15px;float: left" id="sel">
           <input name="queren1" type="button" class="cssbutton" onclick="checkAll();" value="全选" />
           <input class="cssbutton" type="button" name ="queren2" value="全不选" onclick="doDisCheckAll()"/>
           <input name="queren3" type="button" class="cssbutton" onclick="setCheckModel();" value="确认" />               
		</div>
	   </form>
		</div>
	</div>	
		<script language="JavaScript">
	    	if(isMulti == "true")
		    	document.getElementById("sel").style.display = "";
	    	else
	    	{
	    		document.getElementById("sel").style.display = "none";
	    	}	
	    </script>
<script type="text/javascript">
<!--

var parentContainer = parent || top;
if( parentContainer.frames ['inIframe'] ){
	parentContainer = parentContainer.document.getElementById ('inIframe').contentWindow;
}
var parentDocument =parentContainer.document;

var url = "<%=ctx%>/materialGroup/MaterialGroupTree/materialListQueryByOrder.json";
//设置表格标题
var title= null;
//设置列名属性



var columns = [
                {header: "序号", align:'center', renderer:getIndex,width:"5px"},
				{header: "选择",dataIndex: 'MATERIAL_ID',  width:"10px",renderer:myLink},
				{header: "物料代码",dataIndex: 'MATERIAL_CODE',width:"40px"},
				{header: "物料名称",dataIndex: 'MATERIAL_NAME',width:"50px", style:'text-align:left;'}
		      ];
function myLink(value,meta,rec){
	var data = rec.data;
	if(document.getElementById("ISMULTI").value=='true')
	{		
		return "<input type='checkbox' name='cb' id=\""+data.MATERIAL_ID+"\"  value=\""+data.MATERIAL_CODE+"\" />"
    }else
    {
    	
    	return "<input type='radio' name='rd' onclick='setModel(\""+data.MATERIAL_ID+"\",\""+data.MATERIAL_NAME+"\",\""+data.MATERIAL_CODE+"\")' />"  
    }
}
var setModel = function (materialId,materialName,materialCode){
    //modified by LQ,接口请求没有'inIframe',这里需要判断
	if (parent.document.getElementById ('inIframe')) {
		parent.document.getElementById ('inIframe').contentWindow.document.getElementById ('INPUTID').value = materialCode;
		parent.document.getElementById ('inIframe').contentWindow.addMaterial();
	} else {
		parent.document.getElementById ('INPUTID').value = materialCode;
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
    var arr=reCode.split(",");
   for(var i=0;i<arr.length;i++){
	   MyAlert(arr[i]);
	   
       parent.document.getElementById('inIframe').contentWindow.document.getElementById('materialCode').value=arr[i];
       //parent.$('inIframe').contentWindow.document.getElementById('INPUTID').value=arr[i];
        //parent.document.getElementById('inIframe').contentWindow.addMaterial();
        parent.document.getElementById('inIframe').contentWindow.addMaterial();
    }
	//parent.$('inIframe').contentWindow.$(inputId).value=reCode;
    // parent.$('inIframe').contentWindow.addMaterial();
	_hide();
}
function checkAll(){
	var materialCheckBoxs=document.getElementsByName("cb");
	if(!materialCheckBoxs) return;
	for(var i=0;i<materialCheckBoxs.length;i++)
	{
		materialCheckBoxs[i].checked=true;
		checkedClickInMaterial(materialCheckBoxs[i]);
	}
}
function doDisCheckAll(){
	var groupCheckBoxs=document.getElementsByName("cb");
	if(!groupCheckBoxs) return;
	for(var i=0;i<groupCheckBoxs.length;i++)
	{
		groupCheckBoxs[i].checked=false;
		checkedClickInMaterial(groupCheckBoxs[i]);
	}
}
function goToSelect(){
	//$('groupId').value="";
	document.getElementById ('groupId').value="";
	  setValue();
	__extQuery__(1);
}
//================增加选择框的id处理 start by chenyub@yonyou.com===================
function saveCheckedId(checkObj){
	var hideCheckedMaterial = document.getElementById(hideCheckedMaterialId);
	if(hideCheckedMaterial&&checkObj){
		if(hideCheckedMaterial.value.indexOf(checkObj.value)==-1){
			hideCheckedMaterial.value = clearValue(hideCheckedMaterial.value+","+checkObj.value,',');
		}
	}
}
function removeCheckedId(checkObj){
	var hideCheckedMaterial = document.getElementById(hideCheckedMaterialId);
	if(hideCheckedMaterial&&checkObj){
		if(hideCheckedMaterial.value.indexOf(checkObj.value)!=-1){
			hideCheckedMaterial.value = clearValue(hideCheckedMaterial.value.replace(checkObj.value,''),',');
		}
	}
}

//单个复选框的点击事件
function checkedClickInMaterial(_node){
	//checkboxClick(_node);
	if(_node){
		if(_node.checked==true){
			saveCheckedId(_node.nextSibling);
		} else if(_node.checked==false){
			removeCheckedId(_node.nextSibling);
		}
	}
}
//================增加选择框的id处理 end by chenyub@yonyou.com=====================
//--
</script>	
</body>
</html>
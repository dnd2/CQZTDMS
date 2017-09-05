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
    String groupLevel = request.getParameter("GROUPLEVEL");
    if(groupLevel == null || groupLevel.equals("") || groupLevel.equals("null")){
    	groupLevel = "4";
    }
    String groupLevel2 = request.getParameter("GROUPLEVEL");
    String isAllArea = request.getParameter("ISALLAREA");
%>
<script>
<!--

var filecontextPath="<%=ctx%>";
var tree_url = "<%=ctx%>/materialGroup/MaterialGroupTree/materialGroupQuery.json";
var inputId="<%=inputId%>";
var inputName="<%=inputName%>";
var isMulti="<%=isMulti%>";
var groupLevel="<%=groupLevel%>";
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
	setValue();
	//$('dtree').setStyle("top",29);
	//$('dtree').setStyle("left",-1);	
	a.config.closeSameLevel=false;
	a.config.myfun="productPos";
	a.config.folderLinks=true;
	a.delegate=function (id)
	{
		addNodeId = a.aNodes[id].id;	
	    var nodeID = a.aNodes[id].id;
	    document.getElementById("tree_root_id").value = nodeID;
	    sendAjax(tree_url,createNode,'fm');
	}
	sendAjax(tree_url,createTree,'fm');
	a.closeAll();
 }
 	



function productPos(id) {
	var groupId = a.aNodes[id].id;
	//var proname = a.aNodes[id].name;
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
	document.getElementById("INPUTID").value = inputId;
	document.getElementById("INPUTNAME").value = inputName;
	document.getElementById("ISMULTI").value = isMulti;
	document.getElementById("GROUPLEVEL2").value = "<%=groupLevel2%>";
	//document.getElementById("GROUPLEVEL").value = "2";
	document.getElementById("ISALLAREA").value = isAllArea;
}
//--
</script>
</head>
<body  onload="createModelTree(1);" >
<div class="wbox">
<div id='dtree' class="dtree" style="margin:2px;float:left;border-right:solid 1px;width: 20%">
        <script type="text/javascript">
        a = new dTree('a','dtree','false','false','true',<%=groupLevel%>,'<%=ctx%>/materialGroup/MaterialGroupTree/querySubMaterialList.json');
        </script>
    </div>
	 <div style="float:left;width: 79%">
	 <form id="fm" name="fm" method="post">
	 <div class="form-panel">
		<h2><img src="<%=ctx%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="groupId" name="groupId" value="" />
		<input type="hidden" id="INPUTID" name="INPUTID" value="" />
		<input type="hidden" id="INPUTNAME" name="INPUTNAME" value="" />
		<input type="hidden" id="ISMULTI" name="ISMULTI" value="" />
		<input type="hidden" id="GROUPLEVEL" name="GROUPLEVEL" value="" />
		<input type="hidden" id="GROUPLEVEL2" name="GROUPLEVEL2" value="" />
		<input type="hidden" id="ISALLAREA" name="ISALLAREA" value="" />
		<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
        <input type="hidden" id="hideCheckedMaterialGroupId"/>
		<table class="table_query" >
			<tr>
				<td nowrap="nowrap" class="table_query_label">物料组代码：<input 
					id="groupCode" name="groupCode" class="middle_txt" type="text" /></td>
				<td class="table_query_label" nowrap="nowrap" >物料组名称：<input 
					id="groupName" name="groupName" class="middle_txt" type="text" /></td>
		    </tr>
		    <tr>
				 <td colspan="4"  style="text-align:center">
				 <input name="queryBtn" type="button" class=normal_btn id="queryBtn" onclick="goToSelect();" value="查 询"/>
                  <input name="button" type="button" class="normal_btn" onclick="_hide();" value="关 闭" /></td>
		    </tr>
		    </table>
		    </div>
			</div>
			<jsp:include page="${ctx}/queryPage/orderHidden.html" />
			<jsp:include page="${ctx}/queryPage/pageDiv.html" />	
	   </form>
		<div style="margin-top:25px;float: left" id="sel">
           <input name="queren1" type="button" class="cssbutton" onclick="checkAll();" value="全选" />
           <input class="cssbutton" type="button" name ="queren2" value="全不选" onclick="doDisCheckAll()"/>
           <input name="queren3" type="button" class="cssbutton" onclick="setCheckModel();" value="确认" />               
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
		</div>
		
		
<script type="text/javascript">
<!--

var parentContainer = __parent();
if( parentContainer.frames ['inIframe'] ){
	parentContainer = parentContainer.document.getElementById ('inIframe').contentWindow;
}
var parentDocument =parentContainer.document;

var url = "<%=ctx%>/materialGroup/MaterialGroupTree/groupListQuery.json";
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
	if(document.getElementById("ISMULTI").value=='true')
	{
		
		return "<input type='checkbox' name='cb' onclick='checkedClickInMaterialGroup(this);' id=\""+data.GROUP_ID+"\"  value=\""+data.GROUP_CODE+"\" /><input type='hidden'  value=\""+data.GROUP_ID+"\" />"
    }else
    {
    	
    	return "<input type='radio' name='rd' onclick='setModel(\""+data.GROUP_ID+"\",\""+data.GROUP_NAME+"\",\""+data.GROUP_CODE+"\",\""+data.GROUP_LEVEL+"\")' />"  
    }
}
var setModel = function (groupId,groupName,groupCode,groupLevel){
	parentDocument.getElementById(inputId).value = groupCode;
	
	if( inputName && parentDocument.getElementById(inputName) ){
		parentDocument.getElementById(inputName).value = groupId;
	}
	
	 //如果父页面有ID等于colorflag的文本框，则执行相应方法
	if( parentDocument.getElementById("flag") 
			&& parentDocument.getElementById("flag").value == 'colorflag' ){
		parentContainer.getColor();
	}
	//如果物料组等级等与1且父页面有id等于colorTable的table，则让该table显示，否则隐藏
	if( parentDocument.getElementById("colorTable") ){
		  var dis = groupLevel==1?"block":"none";
		  parentDocument.getElementById("colorTable").style.display = dis;
	}
	
// 	if(parent.$('inIframe'))
// 	{  
// 	  parentDocument.getElementById(inputId).value = groupCode;
// 	  if(!inputName && !parentDocument.getElementById(inputName)) {
// 		  parentDocument.getElementById(inputName).value = groupId;
// 	  }
// 	  //如果父页面有ID等于colorflag的文本框，则执行相应方法
// 	  if(typeof(parentDocument.getElementById('flag'))!= "undefined"&&parentDocument.getElementById('flag')!=null){
// 		  if(parentDocument.getElementById('flag').value == 'colorflag'){
// 	         parentContainer.getColor();
// 		  }
// 	  }
// 	  //如果物料组等级等与1且父页面有id等于colorTable的table，则让该table显示，否则隐藏
// 	  if(groupLevel==1){
// 		  if(typeof(parentDocument.getElementById('colorTable'))!= "undefined"&&parentDocument.getElementById('colorTable')!=null){
// 			  parentDocument.getElementById('colorTable').style.display = 'block';
// 		  }
// 	  }else{
// 		  if(typeof(parentDocument.getElementById('colorTable'))!= "undefined"&&parentDocument.getElementById('colorTable')!=null){
// 			  parentDocument.getElementById('colorTable').style.display = 'none';
// 		  }
// 	  }
	  _hide();
// 	}else
// 	{
// 		parent.$(inputId).value = groupCode;
// 		if(!inputName && !parent.$(groupName)) {
// 			parent.$(groupName).value = groupName;
// 		}
// 		//如果父页面有ID等于colorflag的文本框，则执行相应方法
// 	    if( typeof(parent.$('flag'))!="undefined"&&parent.$('flag')!=null){
// 		  if(parent.$('flag').value == 'colorflag'){
// 	         parent.getColor();
// 		  }
// 	    }
		
// 	  //如果物料组等级等与1且父页面有id等于colorTable的table，则让该table显示，否则隐藏
// 	  if(groupLevel==1){
// 		  if(typeof(parent.$('colorTable'))!= "undefined"&&parent.$('colorTable')!=null){
// 			  parent.$('colorTable').style.display = 'block';
// 		  }
// 	  }else{
// 		  if(typeof(parent.$('colorTable'))!= "undefined"&&parent.$('colorTable')!=null){
// 			  parent.$('colorTable').style.display = 'none';
// 		  }
// 	  }
	  parent._hide();	
// 	}
}
function setCheckModel(){
	var reCode="";
	var reId = "";
	var groupCheckBoxs=document.getElementsByName("cb");
	if(!groupCheckBoxs)return;
	for(var i=0;i<groupCheckBoxs.length;i++)
	{
		if(groupCheckBoxs[i].checked)
		{
			if(reCode && reCode.length > 0) {
				reCode += "," + groupCheckBoxs[i].value;
				reId += "," + groupCheckBoxs[i].id;
			} else {
				reCode = reCode+groupCheckBoxs[i].value;
				reId = reId+groupCheckBoxs[i].id;
			}
	    }
	}
	reCode = document.getElementById("hideCheckedId").value;
	//parentDocument.getElementById(inputId).value = groupCode;
	//不能直接使用，需要先判断  20150415 ranke
	if( inputId && parentDocument.getElementById(inputId) ){
		parentDocument.getElementById(inputId).value = reCode;
	}
	//end
	
	if( inputName && parentDocument.getElementById(inputName) ){
		parentDocument.getElementById(inputName).value = reCode;
	}
// 	if(parent.$('inIframe'))
// 	{
// 		parentDocument.getElementById(inputId).value=reCode;
// 		if(!inputName && !parentDocument.getElementById(inputName)) {
// 			parentDocument.getElementById(inputName).value=reId;
// 		}
// 		_hide();	
// 	}else
// 	{
// 		parent.$(inputId).value=reCode;
// 		if(!inputName && !parent.$(inputName)) {
// 			parent.$(inputName).value=reId;
// 		}
// 		parent._hide();
// 	}
	_hide();
}
/* function clsTxt(){
	if(parent.$('inIframe'))
	{
	parentDocument.getElementById(inputId).value="";
	_hide();
	}else
	{
		parent.$(inputId).value="";
		parent.$(inputName).value="";
		parent._hide();
	}
} */
function checkAll(){
	var groupCheckBoxs=document.getElementsByName("cb");
	if(!groupCheckBoxs) return;
	for(var i=0;i<groupCheckBoxs.length;i++)
	{
		groupCheckBoxs[i].checked=true;
		checkedClickInMaterialGroup(groupCheckBoxs[i]);
	}
}
function doDisCheckAll(){
	var groupCheckBoxs=document.getElementsByName("cb");
	if(!groupCheckBoxs) return;
	for(var i=0;i<groupCheckBoxs.length;i++)
	{
		groupCheckBoxs[i].checked=false;
		checkedClickInMaterialGroup(groupCheckBoxs[i]);
	}
}
function goToSelect(){
	document.getElementById("groupId").value="";
	  setValue();
	__extQuery__(1);
	
}
function getIndex(){}

//================增加选择框的id处理 start by chenyub@yonyou.com===================
function saveCheckedId(checkObj){
	var hideCheckedMaterialGroup = document.getElementById(hideCheckedMaterialGroupId);
	if(hideCheckedMaterialGroup&&checkObj){
		if(hideCheckedMaterialGroup.value.indexOf(checkObj.value)==-1){
			hideCheckedMaterialGroup.value = clearValue(hideCheckedMaterialGroup.value+","+checkObj.value,',');
		}
	}
}
function removeCheckedId(checkObj){
	var hideCheckedMaterialGroup = document.getElementById(hideCheckedMaterialGroupId);
	if(hideCheckedMaterialGroup&&checkObj){
		if(hideCheckedMaterialGroup.value.indexOf(checkObj.value)!=-1){
			hideCheckedMaterialGroup.value = clearValue(hideCheckedMaterialGroup.value.replace(checkObj.value,''),',');
		}
	}
}

//单个复选框的点击事件
function checkedClickInMaterialGroup(_node){
	checkboxClick(_node);
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
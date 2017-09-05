<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<%
	String ctx = request.getContextPath();
    String inputId = request.getParameter("INPUTID");
    String inputName = request.getParameter("INPUTNAME");
    String isMulti = request.getParameter("ISMULTI");
    String groupLevel = request.getParameter("GROUPLEVEL");
    String activityId = request.getParameter("ACTIVITYID");
    
%>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/dtree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/DestoryPrototype.js"></script>
<script>
<!--
var filecontextPath="<%=ctx%>";
var tree_url = "<%=ctx%>/claim/serviceActivity/ServiceActivityManageModel/serviceMaterialGroupQuery.json";
var inputId="<%=inputId%>";
var inputName="<%=inputName%>";
var isMulti="<%=isMulti%>";
var groupLevel="<%=groupLevel%>";
var activityId="<%=activityId%>";
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
	$('INPUTID').value = inputId;
	$('INPUTNAME').value = inputName;
	$('ISMULTI').value = isMulti;
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
	    <input type="hidden" name="ids" id="ids" />
	    <input type="hidden" name="ModelList" id="ModelList" value="<%=request.getAttribute("permissions")%>"/>
	    <input type="hidden" name="activityId" id="activityId" value="<%=activityId%>"/>
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="groupId" name="groupId" value="" />
		<input type="hidden" id="INPUTID" name="INPUTID" value="" />
		<input type="hidden" id="INPUTNAME" name="INPUTNAME" value="" />
		<input type="hidden" id="ISMULTI" name="ISMULTI" value="" />
		<input type="hidden" id="GROUPLEVEL" name="GROUPLEVEL" value="" />
		<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
		<table class="table_query" >
			<tr>
				<td nowrap="nowrap" class="table_query_label">车型代码：<input 
					id="groupCode" name="groupCode" class="middle_txt" type="text" /></td>
				<td class="table_query_label" nowrap="nowrap" >车型名称：<input 
					id="groupName" name="groupName" class="middle_txt" type="text" /></td>
				 <td  align="right"><input name="button2" type="button" class="cssbutton" id="queryBtn1" onclick="goToSelect();" value="查 询"/>
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

var url = "<%=ctx%>/claim/serviceActivity/ServiceActivityManageModel/serviceGroupListQuery.json";
//设置表格标题
var title= null;
//设置列名属性



var columns = [
                {header: "序号", align:'center', renderer:getIndex,width:"5px"},
				{header: "选择",   dataIndex: 'GROUP_ID', width:"10px",renderer:myLink},
				{header: "车型代码",dataIndex: 'GROUP_CODE',width:"40px"},
				{header: "车型名称",dataIndex: 'GROUP_NAME',width:"50px"}
		      ];
function myLink(value,meta,rec){
	var data = rec.data;
	if($('ISMULTI').value=='true')
	{
		
		return "<input type='checkbox' name='cb' id=\""+data.GROUP_ID+"\"  value=\""+data.GROUP_ID+"\" />"
    }else
    {
    	
    	return "<input type='radio' name='rd' onclick='setModel(\""+data.GROUP_ID+"\",\""+data.GROUP_NAME+"\",\""+data.GROUP_CODE+"\")' />"  
    }
}
var setModel = function (groupId,groupName,groupCode){
	parentDocument.getElementById(inputId).value = groupCode;
	_hide();
}
function setCheckModel(){
	var reCode="";
	var groupCheckBoxs=document.getElementsByName("cb");
	if(!groupCheckBoxs)return;
	for(var i=0;i<groupCheckBoxs.length;i++)
	{
		if(groupCheckBoxs[i].checked)
		{
			if(reCode && reCode.length > 0)
				reCode += "," + groupCheckBoxs[i].value;
			else
				reCode = reCode+groupCheckBoxs[i].value;
	    }
	}
	 if(reCode==""){
		  //MyDivAlert("请选择车型！");
		  //return false;
		   MyDivConfirm("是否确认增加？",checkedSuccess,[reCode,<%=activityId%>]);
	 }
	 MyDivConfirm("是否确认增加？",checkedSuccess,[reCode,<%=activityId%>]);
	//parentDocument.getElementById(inputId).value=reCode;
	//_hide();
}

function checkedSuccess(str,activityId){
makeNomalFormCall('<%=request.getContextPath()%>/claim/serviceActivity/ServiceActivityManageModel/serviceActivityManageModelOption.json?groupIds='+str+"&activityId="+activityId,delBack,'fm','queryBtn');
}

//新增回调方法：
function delBack(json) {
	if(json.success != null && json.success == "true") {
		MyDivAlert("新增成功！");
		parent.window._hide();
	} else {
		MyDivAlert("新增失败！请联系管理员！");
	}
}

//回显复选框
function customerFunc(json){
    document.getElementById("ids").value = json.ids;
	var chk = document.getElementById("ids").value;
	var chkNew=chk.toString();
	if(!chkNew){return}
	var chk=chkNew.split(",");
	var modelist=document.getElementById("ModelList").value;
	var modelNew=modelist.toString();
	if(!modelNew){return}
	var model=modelNew.split(",");
	for(var i=0;i<model.length;i++){
		for(var j=0;j<chk.length;j++){  
			if(model[i]==chk[j]){
				document.getElementById(model[i]).checked="checked";
			}
		}
	}
	}
	

function clsTxt(){
	parentDocument.getElementById(inputId).value="";
	_hide();
}
function checkAll(){
	var groupCheckBoxs=document.getElementsByName("cb");
	if(!groupCheckBoxs) return;
	for(var i=0;i<groupCheckBoxs.length;i++)
	{
		groupCheckBoxs[i].checked=true;
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
function getIndex(){}
//--
</script>	
</body>
</html>
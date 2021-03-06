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
    String inputId = request.getParameter("INPUTID");
    String inputName = request.getParameter("INPUTNAME");
    String isMulti = request.getParameter("ISMULTI");
    String groupLevel = request.getParameter("GROUPLEVEL");
%>
<script>
//页面初始化
function doInit(){
	$('groupId').value="";
	  setValue();
	__extQuery__(1);
}
<!--
var area_id =  parentDocument.getElementById("area_id").value;//父页面以业务范围id
var filecontextPath="<%=ctx%>";
var tree_url = "<%=ctx%>/materialGroup/MaterialGroupTree/materialCarTypeGroupQuery_Sel.json?area_id="+area_id;
var inputId="<%=inputId%>";
var inputName="<%=inputName%>";
var isMulti="<%=isMulti%>";
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
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="groupId" name="groupId" value="" />
		<input type="hidden" id="INPUTID" name="INPUTID" value="" />
		<input type="hidden" id="INPUTNAME" name="INPUTNAME" value="" />
		<input type="hidden" id="ISMULTI" name="ISMULTI" value="" />
		<input type="hidden" id="GROUPLEVEL" name="GROUPLEVEL" value="" />
		<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
		<table class="table_query" >
			<tr>
				<td nowrap="nowrap" class="table_query_label">物料组代码：<input 
					id="groupCode" name="groupCode" class="middle_txt" type="text" /></td>
				<td class="table_query_label" nowrap="nowrap" >物料组名称：<input 
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

var url = "<%=ctx%>/materialGroup/MaterialGroupTree/groupListCarTypeQuery_Sel.json?area_id="+area_id;
//设置表格标题
var title= null;
//设置列名属性



var columns = [
                {header: "序号", align:'center', renderer:getIndex,idth:70},
				{header: "选择",   dataIndex: 'GROUP_ID', width:"10px",renderer:myLink},
				{header: "物料组代码",dataIndex: 'GROUP_CODE',width:"40px"},
				{header: "物料组名称",dataIndex: 'GROUP_NAME',width:"50px"}
		      ];
function myLink(value,meta,rec){
	var data = rec.data;
	if($('ISMULTI').value=='true'){
		return "<input type='checkbox' name='cb' id=\""+data.GROUP_ID+"\"  value=\""+data.GROUP_ID+"|"+data.GROUP_CODE+"\" />"
    }else{
    	
    	return "<input type='radio' name='rd' onclick='setModel(\""+data.GROUP_ID+"\",\""+data.GROUP_NAME+"\",\""+data.GROUP_CODE+"\")' />"  
    }
}
var setModel = function (groupId,groupName,groupCode){
	parentDocument.getElementById(inputId).value = groupCode;
	parentDocument.getElementById(inputName).value = groupId;
	_hide();
}
function setCheckModel(){
	var reId="";//物料组id
	var reCode="";//物料组代码
	var groupCheckBoxs=document.getElementsByName("cb");
	if(!groupCheckBoxs)
	return;
	for(var i=0;i<groupCheckBoxs.length;i++){
		if(groupCheckBoxs[i].checked){
			if(reId && reId.length > 0){
				reId += "," + groupCheckBoxs[i].value.split("|")[0];
				reCode += "," + groupCheckBoxs[i].value.split("|")[1];
			}
			else{
				reId = reId+groupCheckBoxs[i].value.split("|")[0];
				reCode = reCode+groupCheckBoxs[i].value.split("|")[1];
			}	
	    }
	}
	if(!reId){
		MyDivAlert("请选择物料组信息!");
		return;
	}else{
		addMaterialGroupAction(reId,reCode);
	}
}

function addMaterialGroupAction(reId,reCode){
	if(parent.$('inIframe')){
		//parentDocument.getElementById(inputId).value=reCode;
		//parentDocument.getElementById(inputName).value = reId;
		var i_id = parentDocument.getElementById("modelId").value;	
		var i_name = parentDocument.getElementById("campaignModel").value;		

		var i_id_Array = new Array();
		var i_name_Array = new Array();
		
        var i_id_old = i_id.split(",");		//原值	
        var i_name_old = i_name.split(","); //原值
        
        var i_id_new  = reId.split(",");    //新值
		var i_name_new = reCode.split(","); //新值
        
        var reIds =reId.split(",");
        if(i_id){
        	i_id_Array.push(i_id);
    		i_name_Array.push(i_name);
    		
            var flag = false;
			for(var j=0;j<i_id_new.length;j++){
				var str_reCodes = "";
                var str_reIds = "";
				for(k=0;k<i_id_old.length;k++){
					if(i_id_new[j]+"" != i_id_old[k]+""){
						flag = true;
						str_reIds = i_id_new[j];
						str_reCodes = i_name_new[j];
					}else{
						flag = false;
						break;
					}
				}
			    if(flag){
			    	i_id_Array.push(str_reIds);
			    	i_name_Array.push(str_reCodes);
				}
			}
        
			parentDocument.getElementById("modelId").value = i_id_Array.toString();
			parentDocument.getElementById("campaignModel").value = i_name_Array.toString();
		_hide();	
        }else{
        	parentDocument.getElementById("modelId").value = reId;
        	parentDocument.getElementById("campaignModel").value = reCode;
    		parent._hide();
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
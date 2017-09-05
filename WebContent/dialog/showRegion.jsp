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
    String inputId = request.getParameter("INPUTCODE");
    String inputName = request.getParameter("INPUTID");
    String isMulti = request.getParameter("ISMULTI");
    String dealer_type = request.getParameter("DEALER_TYPE");
    String ORGID = request.getParameter("ORGID");
%>
<script type="text/javascript">

var filecontextPath="<%=ctx%>";
var ORGID="<%=ORGID%>";
var tree_url ="<%=ctx%>/sysmng/usemng/SgmDealerSysUser/initOrgTree.json?ORGID="+ORGID;
var inputId="<%=inputId%>";
var inputName="<%=inputName%>";
var isMulti="<%=isMulti%>";
var dealer_type="<%=dealer_type%>";
var tree_root_id = {"tree_root_id" : ""};
var subStr = "funlist";
function createNode(reobj) {
	var prolistobj = reobj[subStr];
	var orgId,parentOrgId,orgName,orgCode;
	a.remove(addNodeId+"_");
	for(var i=0; i<prolistobj.length; i++) {
		orgId = prolistobj[i].orgId;
		orgName = prolistobj[i].orgName;
		orgCode = prolistobj[i].orgCode;
		parentOrgId = prolistobj[i].parentOrgId;
		a.add(orgId,addNodeId,orgName,orgCode);
		//a.add(orgId+"_",orgId,"loading...","","","",a.icon.loading,"","");
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
	var orgId = a.aNodes[id].id;
	//var proname = a.aNodes[id].name;
	$('orgId').value = orgId;
	setValue();
	__extQuery__(1);
 }
 
function createTree(reobj) {
	var prolistobj = reobj[subStr];
	var orgId,parentOrgId,orgName,orgCode;
	for(var i=0; i<prolistobj.length; i++) {
		orgId = prolistobj[i].orgId;
		orgName = prolistobj[i].orgName;
		orgCode = prolistobj[i].orgCode;
		parentOrgId = prolistobj[i].parentOrgId;
		if(parentOrgId == "-1") { //系统根节点
			a.add(orgId,"-1",orgName,orgCode);
				
		} else {
			a.add(orgId,parentOrgId,orgName,orgCode);
			//a.add(orgId+"_",orgId,"loading...","","","",a.icon.loading,"","");
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
	$('DEALER_TYPE').value = dealer_type;
}

</script>
</head>
<body onunload='javascript:destoryPrototype()' onload="createModelTree(1);" >
<div id='dtree' class="dtree" style=" margin:2px;float:left;border-right:solid 1px;width: 20%">
        <script type="text/javascript">
        a = new dTree('a','dtree','false','false','true');
        </script>
    </div>
	 <div style="float:left;width: 79%;">
	 <div style="width:100%;">
	 <form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="orgId" name="orgId" value="" />
		<input type="hidden" id="INPUTID" name="INPUTID" value="" />
		<input type="hidden" id="DEALER_TYPE" name="DEALER_TYPE" value="" />
		<input type="hidden" id="INPUTNAME" name="INPUTNAME" value="" />
		<input type="hidden" id="ISMULTI" name="ISMULTI" value="" />
		<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
        <input type="hidden" id="hideCheckedId"/>
        <input type="hidden" id="hideCheckedRegionId"/>
		<table class="table_query" >
			<tr>
				<td nowrap="nowrap" class="table_query_label">省份代码：<input 
					id="regCode" name="regCode" class="middle_txt" type="text" /></td>
				<td class="table_query_label" nowrap="nowrap" >省份名称：<input 
					id="regName" name="regName" class="middle_txt" type="text" /></td>
				 <td  align="right"><input name="button2" type="button" class="cssbutton" id="queryBtn" onclick="goToSelect();" value="查 询"/>
                  <input name="button" type="button" class="cssbutton" onclick="_hide();" value="关 闭" /></td>
		    </tr>
		    </table>
			<jsp:include page="${ctx}/queryPage/orderHidden.html" />
			<jsp:include page="${ctx}/queryPage/pageDiv.html" />	
		<div style="float: left" id="sel">
           <input name="queren1" type="button" class="cssbutton" onclick="checkAll();" value="全选" />
           <input class="cssbutton" type="button" name ="queren2" value="全不选" onclick="doDisCheckAll()"/>
           <input name="queren3" type="button" class="cssbutton" onclick="setCheckModel();" value="确认" />   
		</div>
	   </form>
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

var url = "<%=ctx%>/sysmng/usemng/SgmDealerSysUser/allRegionQuery.json?COMMAND=1&ORGID="+ORGID;
//设置表格标题
var title= null;
//设置列名属性

var columns = [
                {header: "序号", align:'center', renderer:getIndex,width:"5px"},
				{header: "选择",   dataIndex: 'ORG_ID', width:"10px",renderer:myLink},
				{header: "省份代码",dataIndex: 'ORG_CODE',width:"40px"},
				{header: "省份名称",dataIndex: 'ORG_NAME',width:"50px"}
		      ];
function myLink(value,meta,rec){
	var data = rec.data;
	if($('ISMULTI').value=='true'){	
		return "<input type='checkbox' name='cb' id=\""+data.ORG_ID+"\" onclick='checkedClickInRegion(this);'  value=\""+data.ORG_CODE+"\" /><input type='hidden' name='cc' id=\""+data.ORG_ID+"\"  value=\""+data.ORG_ID+"\" />"
    }else{	
    	return "<input type='radio' name='rd' onclick='setModel(\""+data.ORG_ID+"\",\""+data.ORG_NAME+"\",\""+data.ORG_CODE+"\")' />"  
    }
}
var setModel = function (regId,regName,regCode){
	if(inputId&&inputId.length>0){
		parentDocument.getElementById(inputId).value = orgCode;
	}
	if(inputName&&inputName.length>0&&inputName!='null'){
		parentDocument.getElementById(inputName).value=orgId;
	}
	_hide();
}
function setCheckModel(){
	var reCode="";
	var reId="";
	reCode = $(hideCheckedId).value;
	reId = $(hideCheckedRegionId).value;
	/*var groupCheckBoxs=document.getElementsByName("cb");
	var groupCheckBoxsId=document.getElementsByName("cc");
	
	if(!groupCheckBoxs)return;
	for(var i=0;i<groupCheckBoxs.length;i++)
	{
		if(groupCheckBoxs[i].checked)
		{
			if(reCode && reCode.length > 0){
				reCode += "," + groupCheckBoxs[i].value;
				reId += "," + groupCheckBoxsId[i].value;}
			else{
				reCode = reCode+groupCheckBoxs[i].value;
			reId = reId+groupCheckBoxsId[i].value;}
	    }
	}*/
	
// 	if(parent.document .getElementById ('inIframe') == null) {
// 		if(top.getElementById(inputId)) {
// 			top.getElementById(inputId).value = reCode;
// 		}
// 		if(top.$(inputName)) {
// 			top.getElementById(inputName).value = reId;
// 		}
// 	} else {
		//parentDocument.getElementById(inputId).value=reCode;
		//parentDocument.getElementById(inputName).value=reId;
		//要先进行判断
		if( inputId && parentDocument.getElementById(inputId) ){
			parentDocument.getElementById(inputId).value = reCode;
		}
		
		if( inputName && parentDocument.getElementById(inputName) ){
			parentDocument.getElementById(inputName).value = reId;
		}
// 	}
		
	
	
	_hide();
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
		checkedClickInRegion(groupCheckBoxs[i]);
	}
	
}
function doDisCheckAll(){
	var groupCheckBoxs=document.getElementsByName("cb");
	if(!groupCheckBoxs) return;
	for(var i=0;i<groupCheckBoxs.length;i++)
	{
		groupCheckBoxs[i].checked=false;
		checkedClickInRegion(groupCheckBoxs[i]);
	}
}
function goToSelect(){
	$('orgId').value="";
	  setValue();
	__extQuery__(1);
	
}
function getIndex(){}

// ================增加选择框的id处理 start by chenyub@yonyou.com===================
function saveCheckedId(checkObj){
	var hideCheckedRegion = document.getElementById(hideCheckedRegionId);
	if(hideCheckedRegion&&checkObj){
		if(hideCheckedRegion.value.indexOf(checkObj.value)==-1){
			hideCheckedRegion.value = clearValue(hideCheckedRegion.value+","+checkObj.value,',');
		}
	}
}
function removeCheckedId(checkObj){
	var hideCheckedRegion = document.getElementById(hideCheckedRegionId);
	if(hideCheckedRegion&&checkObj){
		if(hideCheckedRegion.value.indexOf(checkObj.value)!=-1){
			hideCheckedRegion.value = clearValue(hideCheckedRegion.value.replace(checkObj.value,''),',');
		}
	}
}

// 单个复选框的点击事件
function checkedClickInRegion(_node){
	checkboxClick(_node);
	if(_node){
		if(_node.checked==true){
			saveCheckedId(_node.nextSibling);
		} else if(_node.checked==false){
			removeCheckedId(_node.nextSibling);
		}
	}
	refreshCheckedNum();
}
// ================增加选择框的id处理 end by chenyub@yonyou.com=====================


//--
</script>	
</body>
</html>
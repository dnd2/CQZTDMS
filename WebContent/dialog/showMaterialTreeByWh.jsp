<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
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
    String sendWare = request.getParameter("sendWare");//发运仓库
    String receiveWare = request.getParameter("receiveWare");//收货仓库
    String ids = request.getParameter("ids");
    String groupLevel = "5";
%>
<script>
var filecontextPath="<%=ctx%>";
var tree_url = "<%=ctx%>/materialGroup/MaterialGroupTree/materialQuery.json";
var inputId="<%=inputId%>";
var inputName="<%=inputName%>";
var isMulti="<%=isMulti%>";
var sendWare="<%=sendWare%>";
var receiveWare="<%=receiveWare%>";
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
	document.getElementById('groupId').value = groupId;
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
	document.getElementById('GROUPLEVEL').value = "<%=groupLevel%>";
 }
function setValue()
{
	document.getElementById('INPUTID').value = inputId;
	document.getElementById('INPUTNAME').value = inputName;
	document.getElementById('ISMULTI').value = isMulti;
	document.getElementById('GROUPLEVEL').value = 4;
	document.getElementById('SENDWARE').value = sendWare;
	document.getElementById('RECEIVEWARE').value = receiveWare;
}
//--
</script>
</head>
<body onunload='javascript:destoryPrototype()' onload="createModelTree(1);goToSelect();" >
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
		<input type="hidden" id="SENDWARE" name="SENDWARE" value="" />
		<input type="hidden" id="RECEIVEWARE" name="RECEIVEWARE" value="" />
		<input type="hidden" id="ids" name="ids" value="<%=ids%>" />
		<input type="hidden" id="GROUPLEVEL" name="GROUPLEVEL" value="" />
		<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
		<table class="table_query" >
			<tr>
				<td nowrap="nowrap" class="table_query_label">整车物料码：</td>
				<td class="table_query_input"   nowrap="nowrap">
					<input 
					id="materialCode" name="materialCode" class="middle_txt" type="text" maxlength="20"  />				
				</td>
				<td class="table_query_label" nowrap="nowrap" >整车物料名称：</td>
				<td class="table_query_input" nowrap="nowrap">
					<input 
					id="materialName" name="materialName" class="middle_txt" type="text" maxlength="20"  />				
				</td>
		    </tr>
			<tr align="left">
			  <td colspan="4" nowrap="nowrap" class="table_query_label">
				  <table width="100%" border="0">
	                <tr>
	                  <td>
	                  	<input name="button2" type="button" class="normal_btn" id="queryBtn" onclick="goToSelect();" value="查 询"/>
	                  	<input name="button" type="button" class="normal_btn" onclick="_hide();" value="关 闭" />
	                  </td>
	                  <td></td>
	                  <%if("true".equals(isMulti)){%>
	                  <td><input name="queren" type="button" class="normal_btn" onclick="setCheckModel();" value="确认" /></td>
	                  <td><input name="queren" type="button" class="normal_btn" onclick="checkAll();" value="全选" /></td>
	                  <%}%>
	                  
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

var url = "<%=ctx%>/materialGroup/MaterialGroupTree/materialListQueryByware.json";
//设置表格标题
var title= null;
var page=null;
//设置列名属性



var columns = [
				{header: "选择",   dataIndex: 'MATERIAL_ID', renderer:myLink},
				{header: "车系代码", dataIndex: 'SERIES_CODE', align:'center'},
				{header: "车系名称", dataIndex: 'SERIES_NAME', align:'center'},
				{header: "车型代码", dataIndex: 'MODEL_CODE', align:'center'},
				{header: "车型名称", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "配置代码", dataIndex: 'PACKAGE_CODE', align:'center'},
				{header: "配置名称", dataIndex: 'PACKAGE_NAME', align:'center'},
				{header: "颜色代码", dataIndex: 'COLOR_CODE', align:'center'},
				{header: "颜色", dataIndex: 'COLOR_NAME', align:'center'},	
				{header: "整车物料码",dataIndex: 'MATERIAL_CODE'}
		      ];
function myLink(value,meta,rec){
	var data = rec.data;
	if(document.getElementById('ISMULTI').value=='true')
	{
		
		return "<input type='checkbox' name='cb' id=\""+data.MATERIAL_ID+"\"  value=\""+data.MATERIAL_CODE+"\" />";
    }else
    {
    	var str="<input type='radio' name='rd' onclick='setModel(\""+data.MATERIAL_ID+"\",\""+data.MATERIAL_NAME+"\",\""+data.MATERIAL_CODE+"\",\""+data.SERIES_ID+
    			"\",\""+data.SERIES_CODE+"\",\""+data.SERIES_NAME+"\",\""+data.MODEL_ID+"\",\""+data.MODEL_CODE+"\",\""+data.MODEL_NAME+
    			"\",\""+data.PACKAGE_ID+"\",\""+data.PACKAGE_CODE+"\",\""+data.PACKAGE_NAME+"\",\""+data.COLOR_CODE+"\",\""+data.COLOR_NAME+"\")'/>";
    	return str; 
    }
}
var setModel = function (materialId,materialName,materialCode,seriesId,seriesCode,seriesName,
		modelId,modelCode,modelName,packageId,packageCode,pakageName,colorCode,colorName){
	if (parent.document.getElementById('inIframe')) {
		//parent.document.getElementById('inIframe').contentWindow.document.getElementById(inputId).value = materialCode;
		var sendWareN=document.getElementById("SENDWARE").value;//当前所选发运仓库
		var receiveWareN=document.getElementById("RECEIVEWARE").value;//当前所选收货仓库
		//获取库存剩余量
		var url2 = "<%=ctx%>/sales/storage/sendmanage/DispatchOrderManage/getStockNumByWhId.json?materialId="+materialId+"&sendWareId="+sendWareN;
		makeNomalFormCall(url2,function(json){
			var table = parent.document.getElementById('inIframe').contentWindow.document.getElementById('tbody1');
		   var PACKAGE_ID = parent.document.getElementById('inIframe').contentWindow.document.getElementsByName('PACKAGE_ID');
		   var MATERIAL_ID = parent.document.getElementById('inIframe').contentWindow.document.getElementsByName('MATERIAL_ID');
		   
		   
		   var fag = true;
		   if(PACKAGE_ID.length > 0)
		   {
		      for(var j = 0 ; j < MATERIAL_ID.length ; j++)
		      {
		         if(materialId == MATERIAL_ID[j].value)
		         {
		             fag = false;
		         }
		      }
		   }
		    if(fag)
		    { 
				var row=table.insertRow(0);
				row.className="table_list_row1";
				var cel1=row.insertCell(0);
				var cel2=row.insertCell(1);
				var cel3=row.insertCell(2);
				var cel4=row.insertCell(3);
				var cel5=row.insertCell(4);
				var cel6=row.insertCell(5);
				var cel7=row.insertCell(6);
				var cel8=row.insertCell(7);
				cel1.innerHTML=seriesName+ '<input type="hidden" id="SERIES_ID'+materialId+'"  name="SERIES_ID"  value="'+seriesId+'"/><input type="hidden" id="SEND_WARE'+materialId+'"  name="SEND_WARE"  value="'+sendWareN+'"/><input type="hidden" id="RECEIVE_WARE'+materialId+'"  name="RECEIVE_WARE"  value="'+receiveWareN+'"/>';
				cel2.innerHTML=modelName + '<input type="hidden" id="MODEL_ID'+materialId+'"  name="MODEL_ID"  value="'+modelId+'"/>';
				cel3.innerHTML=pakageName + '<input type="hidden" id="PACKAGE_ID'+materialId+'"  name="PACKAGE_ID"  value="'+packageId+'"/>';
				cel4.innerHTML=materialCode+ '<input type="hidden" id="MATERIAL_ID'+materialId+'"  name="MATERIAL_ID"  value="'+materialId+'"/>' + '<input type="hidden" id="MATERIAL_CODE'+materialId+'"  name="MATERIAL_CODE"  value="'+materialCode+'"/>';
				cel5.innerHTML=colorName + '<input type="hidden" id="COLOR_NAME'+materialId+'"  name="COLOR_NAME"  value="'+colorName+'"/>';

				cel6.innerHTML=json.stockMap.WARE_NUM;
				cel7.innerHTML='<input type="text" id="COMMIT_NUMBER'+materialId+'"  name="COMMIT_NUMBER" onblur="blurBack('+json.stockMap.WARE_NUM+','+materialId+',this);"  datatype="0,is_digit,4" value="'+0+'"/>';
				
				
				cel8.innerHTML ='<input type="button" class="normal_btn" onclick="delt(this)" class=cssbutton value="删除" />';
				//_hide();
		    }else
		    {
		       MyAlert('该物料已经添加!');
		    }
			__extQuery__(1);
		},'fm');
		
	} else {
		parent.document.getElementById(inputId).value = materialCode;
		parent.addMaterial();
		__extQuery__(1);
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
	if(parent.document.getElementById('inIframe'))
	{
		parent.document.getElementById('inIframe').contentWindow.document.getElementById(inputId).value=reCode;
		parent.document.getElementById('inIframe').contentWindow.addMaterial();
		_hide();	
	}else
	{
		parent.document.getElementById(inputId).value=reCode;
		parent.addMaterial();
		parent._hide();
	}
}
function clsTxt(){
	parent.document.getElementById('inIframe').contentWindow.document.getElementById(inputId).value="";
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
	document.getElementById('groupId').value="";
	  setValue();
	__extQuery__(1);
}

function reflushQuery(dataArray) {
	var selected = '';
	if(dataArray) {
		for(i=0;i<dataArray.length;i++) {
			selected += dataArray[i][1] + ',';
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
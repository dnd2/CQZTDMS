<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<%@page import="com.infodms.dms.po.*" %>
<%@page import="com.infodms.dms.dao.claim.serviceActivity.*" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<!-- <script type="text/javascript" src="<%=request.getContextPath()%>/js/web/dyncdtree.js"></script> -->
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
	goToSelect();
}
function setValue()
{
	$('INPUTID').value = inputId;
	$('INPUTNAME').value = inputName;
	$('ISMULTI').value = isMulti;
	$('GROUPLEVEL2').value = "<%=groupLevel2%>";
	$('GROUPLEVEL').value = "2";
	$('ISALLAREA').value = isAllArea;
}
function addoptions(ids,select){
	MyAlert(ids);
	MyAlert(select);
	 var selectForums=document.getElementById('cxzid');
	 selectForums.options.length=1;
	 ids=ids.replace(" ").split(",");
	 for(var i=1;i<ids.length;i++){
	  selectForums.options.add(categoryWithForums[ids[i]]);
	  if(ids[i]==select){
	   selectForums.options[selectForums.options.length-1].selected=true;
	   }
	 }
	 selectForums.focus();
	}
function selectCategory(index){
	 if($('groupIdXi').options.length==0){
	    return;
	  }
	 $('groupIdXi').options[index].selected = true;
	 $('groupIdXi').options[index].onclick();
	}

//以下为二级级联JS YH 2010.11.25
var categoryWithForums={};
(function(){
 var selectForums=document.getElementById('cxzid');
 for(var i=0;i<selectForums.options.length;i++){
  categoryWithForums[selectForums.options[i].value]=selectForums.options[i];
 }
 //selectForums.options.length=0;
 selectCategory(0);
})();


function isCheck(groupId){
	var url = "<%=ctx%>/claim/dealerClaimMng/ClaimBillMaintainMain/queryCon1.json?groupId="+groupId;
	makeNomalFormCall(url,showAuditValue,'fm','queryBtn');
}

//回调函数
function showAuditValue(json){
	var list = json.list;
	var CODE_NAME = document.getElementById('cxzid');
	//先清空下拉列表的数据
	var codeNameLen=CODE_NAME.options.length;
	if(codeNameLen>1){
		for(var i = 1 ; i < codeNameLen; i++){
			CODE_NAME.removeChild(CODE_NAME.options[1]);
		}
	}
	//再把查出来的值放进去
	for(var i = 0 ; i < list.length; i++){ 
		var _option = new Option(list[i].WRGROUP_NAME,list[i].WRGROUP_NAME);   
		CODE_NAME.options.add(_option);  
	}
}

//控制鼠标和键盘的操作
window.document.onkeydown = function (){
	if(event.keyCode==13){
		isFlag();
	};
} 
function isFlag(){
	//if($('CODE').value==''){
		//MyAlert('请选择大系统查询!');
		//return false;
	//}else{
		//if($('CODE_NAME').value==''){
			//MyAlert('请选择子系统查询!');
			//return false;
		//}else{
			__extQuery__(1)
		//}
	//}
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
	 <div style="float:right;">
	 <form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="groupId" name="groupId" value="" />
		<input type="hidden" id="INPUTID" name="INPUTID" value="" />
		<input type="hidden" id="INPUTNAME" name="INPUTNAME" value="" />
		<input type="hidden" id="ISMULTI" name="ISMULTI" value="" />
		<input type="hidden" id="GROUPLEVEL" name="GROUPLEVEL" value="" />
		<input type="hidden" id="GROUPLEVEL2" name="GROUPLEVEL2" value="" />
		<input type="hidden" id="ISALLAREA" name="ISALLAREA" value="" />
		<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
		<table class="table_query">
		
		<%
		ServiceActivityManageModelDao dao = ServiceActivityManageModelDao.getInstance();
		List<TmVhclMaterialGroupPO> list = dao.serviceActivityGroupName(); //车系及子车型组ID
		List cxandcxzid = new ArrayList();
		
		for(TmVhclMaterialGroupPO tpo : list){				
			Map map = new HashMap();
			map.put("TmVhclMaterialGroupPO", tpo);
			cxandcxzid.add(map);
		}
		%>
		
		<tr>
  	  <td class="table_query_2Col_label_5Letter">车系:</td>
  	  <td>
	  	<select name="groupIdXi" id="groupIdXi" onchange="isCheck(this.value)">
	  	         <option value="">--请选择--</option>
				 <%for(int i=0;i<cxandcxzid.size();i++){ 
					Map map = (Map)cxandcxzid.get(i);
					TmVhclMaterialGroupPO popo = (TmVhclMaterialGroupPO)map.get("TmVhclMaterialGroupPO");
				 %>
				 <option value="<%=popo.getGroupId() %>">
					<%=popo.getGroupName() %>
				 </option>
				 <%} %>	
		</select>
      </td>
  
  <td class="table_query_2Col_label_5Letter">车型组:</td>
  	  <td>
	  	<select name="cxzid" id="cxzid">
	  	        <option value="">--请选择--</option>
		</select>
      </td>
  
				<td nowrap="nowrap" class="table_query_label">车型代码：<input 
					id="groupCode" name="groupCode" class="middle_txt" type="text" /></td>
				<td class="table_query_label" nowrap="nowrap" >车型名称：<input 
					id="groupName" name="groupName" class="middle_txt" type="text" /></td>
			</tr>
		    <tr>
				<td colspan="6" align="center">
					<input name="button2" type="button" class="cssbutton" id="queryBtn1" onclick="goToSelect();" value="查 询"/>
					<input name="button" type="button" class="cssbutton" onclick="_hide();" value="关 闭" />
                </td>
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

var url = "<%=ctx%>/materialGroup/MaterialGroupTree/groupListQuery1.json";
//设置表格标题
var title= null;
//设置列名属性



var columns = [
                {header: "序号", align:'center', renderer:getIndex,width:"5px"},
				{header: "选择",   dataIndex: 'GROUP_ID', width:"10px",renderer:myLink},
				{header: "车系",dataIndex: 'PARENT_CODE',width:"20px"},
				{header: "车型组",dataIndex:'WRGROUP_NAME',width:"20px"},
				{header: "车型代码",dataIndex: 'GROUP_CODE',width:"20px"},
				{header: "车型名称",dataIndex: 'GROUP_NAME',width:"25px"}
		      ];
function myLink(value,meta,rec){
	var data = rec.data;
	if($('ISMULTI').value=='true')
	{
		
		return "<input type='checkbox' name='cb' id=\""+data.GROUP_ID+"\"  value=\""+data.GROUP_CODE+"\" />"
    }else
    {
    	
    	return "<input type='radio' name='rd' onclick='setModel(\""+data.GROUP_ID+"\",\""+data.GROUP_NAME+"\",\""+data.GROUP_CODE+"\")' />"  
    }
}
var setModel = function (groupId,groupName,groupCode){
	if(parent.$('inIframe'))
	{
	  parentDocument.getElementById(inputId).value = groupCode;
	  _hide();
	}else
	{
		parent.$(inputId).value = groupCode;
		parent._hide();	
	}
}
function setCheckModel(){
	var reCode="";
	var inName = "";
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
	    if(groupCheckBoxs[i].checked)
		{
			if(inName && inName.length > 0)
				inName += "," + groupCheckBoxs[i].id;
			else
				inName = reCode+groupCheckBoxs[i].id;
	    }
	}
	if(parent.$('inIframe'))
	{
		parentDocument.getElementById(inputId).value=reCode;
		parentDocument.getElementById(inputName).value=inName;
		_hide();	
	}else
	{
		parent.$(inputId).value=reCode;
		parent.$(inputName).value=reCode;
		parent._hide();
	}
	
}
function clsTxt(){
	if(parent.$('inIframe'))
	{
	parentDocument.getElementById(inputId).value="";
	parentDocument.getElementById(inputName).value="";
	_hide();
	}else
	{
		parent.$(inputId).value="";
		parent.$(inputName).value="";
		parent._hide();
	}
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
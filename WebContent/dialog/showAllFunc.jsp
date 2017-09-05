<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/web/dtree.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/jslib/DestoryPrototype.js"></script>
<%
	String ctx = request.getContextPath();
	String inputId = request.getParameter("INPUTID");
	String inputName = request.getParameter("INPUTNAME");
	String isMulti = request.getParameter("ISMULTI");
%>
<script>
<!--
var filecontextPath="<%=ctx%>";
var tree_url ="<%=ctx%>/help/questionSolve/QuestionReported/initFuncTree.json";
var inputId="<%=inputId%>";
var inputName="<%=inputName%>";
var isMulti="<%=isMulti%>";
var parentContainer = parent || top;
if( parentContainer.frames ['inIframe'] ){
	parentContainer = parentContainer.document.getElementById ('inIframe').contentWindow;
}
var parentDocument =parentContainer.document;
var tree_root_id = {"tree_root_id" : ""};
var subStr = "funclist";
function createNode(reobj) {
	var prolistobj = reobj[subStr];
	var funcId,parentFuncId,funcName;
	a.remove(addNodeId+"_");
	for(var i=0; i<prolistobj.length; i++) {
		funcId = prolistobj[i].funcId;
		funcName = prolistobj[i].funcName;
		parentFuncId = prolistobj[i].parFuncId;
		a.add(funcId,addNodeId,funcName);
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
	var funcId = a.aNodes[id].id;
	//var proname = a.aNodes[id].name;
	$('funcId').value = funcId;
	setValue();
	__extQuery__(1);
 }
 
function createTree(reobj) {
	var prolistobj = reobj[subStr];
	var funcId,parentFuncId,funcName;
	for(var i=0; i<prolistobj.length; i++) {
		funcId = prolistobj[i].funcId;
		funcName = prolistobj[i].funcName;
		parentFuncId = prolistobj[i].parFuncId;
		if(parentFuncId == "0") { //系统根节点
			a.add(funcId,"-1",funcName);
				
		} else {
			a.add(funcId,parentFuncId,funcName);
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
}
//--
</script>
</head>
<body onunload='javascript:destoryPrototype()'
	onload="createModelTree(1);">
	<div id='dtree' class="dtree"
		style="margin: 2px; float: left; border-right: solid 1px;">
		<script type="text/javascript">
        a = new dTree('a','dtree','false','false','true');
        </script>
	</div>
	<div style="float: right;">
		<form id="fm" name="fm" method="post">
			<input type="hidden" name="curPage" id="curPage" value="1" /> 
			<input type="hidden" id="funcId" name="funcId" value="" /> 
			<input type="hidden" id="INPUTID" name="INPUTID" value="" /> 
			<input type="hidden" id="INPUTNAME" name="INPUTNAME" value="" /> 
			<input type="hidden" id="ISMULTI" name="ISMULTI" value="" /> 
			<input type="hidden" name="tree_root_id" id="tree_root_id" value="" />
			<table class="table_query">
				<tr>
					<td nowrap="nowrap" class="table_query_label">功能代码：
						<input id="queryFuncId" name="queryFuncId" class="middle_txt" type="text" style="height:18px;"/></td>
					<td class="table_query_label" nowrap="nowrap">功能名称：
						<input id="funcName" name="funcName" class="middle_txt" type="text" style="height:18px;"/></td>
					<td align="right"><input name="button2" type="button"
						class="cssbutton" id="queryBtn" onclick="goToSelect();"
						value="查 询" /> <input name="button" type="button"
						class="cssbutton" onclick="_hide();" value="关 闭" /></td>
				</tr>
			</table>
			<jsp:include page="${ctx}/queryPage/orderHidden.html" />
			<jsp:include page="${ctx}/queryPage/pageDiv.html" />
		</form>
		<div style="margin-top: 25px; float: left" id="sel">
			<input name="queren1" type="button" class="cssbutton"
				onclick="checkAll();" value="全选" /> <input class="cssbutton"
				type="button" name="queren2" value="全不选" onclick="doDisCheckAll()" />
			<input name="queren3" type="button" class="cssbutton"
				onclick="setCheckModel();" value="确认" />
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
		var url = "<%=ctx%>/sysmng/notice/noticeModel/NoticeModelAction/getMenuList.json?is_func=1";
		//设置表格标题
		var title = null;
		
		//设置列名属性
		var columns = [ {
			header : "序号",
			align : 'center',
			renderer : getIndex,
			width : "5px"
		}, {
			header : "选择",
			dataIndex : 'funcId',
			width : "10px",
			renderer : myLink
		},  {
			header : "功能名称",
			dataIndex : 'funcName',
			width : "50px"
		} ];
		function myLink(value, meta, rec) {
			var data = rec.data;
			if ($('ISMULTI').value == 'true') {
				return "<input type='checkbox' name='cb' id=\""+data.funcId+"\"  value=\""+data.funcId+"\" /><input type='hidden' name='cc' id=\""+data.funcId+"\"  value=\""+data.funcId+"\" /><input type='hidden' name='theNames' id='" + data.funcId + "' value='" + data.funcName + "' />"
			} else {
				return "<input type='radio' name='rd' onclick='setModel(\"" + data.funcId + "\",\"" + data.funcName + "\")' />"
			}
		}
		var setModel = function(funcId, funcName) {
			if (inputId && inputId.length > 0) {
				parentDocument.getElementById(inputId).value = funcId;
				parentDocument.getElementById(inputName).value = funcName;
			}
			try{
				if(parentContainer){
					var callbackFunc = parentContainer.getModel;
					if(typeof callbackFunc == 'function'){
						callbackFunc();
					}
				}
			}catch (e) {
				
			}
			_hide();
		}
		function setCheckModel() {
			var reCode = "";
			var reId = "";
			var reName = "";
			var groupCheckBoxs = document.getElementsByName("cb");
			var groupCheckBoxsId = document.getElementsByName("cc");
			var groupCheckBoxsName = document.getElementsByName("theNames");

			if (!groupCheckBoxs)
				return;
			for ( var i = 0; i < groupCheckBoxs.length; i++) {
				if (groupCheckBoxs[i].checked) {
					if (reCode && reCode.length > 0) {
						reName += "," + groupCheckBoxsName[i].value
						reCode += "," + groupCheckBoxs[i].value;
						reId += "," + groupCheckBoxsId[i].value;
					} else {
						reName = reName + groupCheckBoxsName[i].value;
						reCode = reCode + groupCheckBoxs[i].value;
						reId = reId + groupCheckBoxsId[i].value;
					}
				}
			}

			if (parentDocument.getElementById(inputId)) {
				parentDocument.getElementById(inputId).value = reCode;
			}

			if (parentDocument.getElementById(inputName)) {
				parentDocument.getElementById(inputName).value = reId;
			}

			if (parentDocument.getElementById(theName)) {
				parentDocument.getElementById(theName).value = reName;
			}

			_hide();
		}
		function clsTxt() {
			parentDocument.getElementById(inputId).value = "";
			parentDocument.getElementById(inputName).value = "";
			_hide();
		}
		function checkAll() {
			var groupCheckBoxs = document.getElementsByName("cb");
			if (!groupCheckBoxs)
				return;
			for ( var i = 0; i < groupCheckBoxs.length; i++) {
				groupCheckBoxs[i].checked = true;
			}
		}
		function doDisCheckAll() {
			var groupCheckBoxs = document.getElementsByName("cb");
			if (!groupCheckBoxs)
				return;
			for ( var i = 0; i < groupCheckBoxs.length; i++) {
				groupCheckBoxs[i].checked = false;
			}
		}
		function goToSelect() {
			$('funcId').value = "";
			setValue();
			__extQuery__(1);

		}
		function getIndex() {
		}
	</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.common.FileConstant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="<%=FileConstant.regionJsUrl%>"></script>
<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
<jsp:include page="${contextPath}/common/globalVariable.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/mootools.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/jslib/my-grid-pager.js" charset="UTF-8"></script>

<script type="text/javascript" src="<%=contextPath %>/js/validate/validate.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath%>/js/dict.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/web/model_tree.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/web/dtree.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/web/common.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/InfoAjax.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/dialog_new.js"></script>
</head>
<script>
	   var filecontextPath="<%=contextPath%>";
</script>	
<body onunload='javascript:destoryPrototype()' onload="createModelTree()" >
<div id='dtree' class="dtree" style=" margin:2px;float:left;border-right:solid 1px;">
        <script type="text/javascript">
        a = new dTree('a','dtree','false','false','true');
        </script>
    </div>
	 <div style="float:right;">
	 <form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="proId" name="proId" value="" />
		<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
		<table class="table_query" >
			<tr>
				<td class="table_query_label" nowrap="nowrap">车型代码：</td>
				<td class="table_query_input"   nowrap="nowrap">
					<input datatype="1,is_noquotation,<%=Constant.Length_Check_Char_100 %>" id="modelCode" name="modelCode" class="short_txt" type="text" />
				</td>
				<td class="table_query_label" nowrap="nowrap" >车型名称：</td>
				<td class="table_query_input" nowrap="nowrap">
					<input datatype="1,is_noquotation,<%=Constant.Length_Check_Char_100 %>" id="modelName" name="modelName" class="middle_txt" type="text" />
				</td>
			</tr>
			<tr>
				<td class="table_query_label" nowrap="nowrap">
					<input class="normal_btn" id="queryBtn" type="button" value="查 询" onclick="__extQuery__(1);"/>
				</td>
				<td class="table_query_label" nowrap="nowrap">
					<input class="normal_btn" type="reset" value="重 置" />
				</td>
				<td class="table_query_label" nowrap="nowrap">
					<input class="normal_btn" type="button" onclick="_hide();" value="关 闭" />
				</td>
<!--				<td class="table_query_label" nowrap="nowrap">-->
<!--					<input class="normal_btn" onclick="clsTxt();" type="reset" value="清 除" />-->
<!--				</td>-->
			</tr>
		</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		</form>
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</div>
</body>
<script>
	var myPage;
	var url = "<%=contextPath%>/common/ModelMng/queryModelWholeSale.json";
	
	var tree_url = "<%=contextPath%>/sysproduct/productmanage/CarModelManager/initProductTree.json";	
	//设置表格标题
	var title= null;
	//设置列名属性
	var columns = [
					{header: "选择", dataIndex: 'modelId',width:"50px",renderer:myLink},
					{header: "车型代码",width:"50px", dataIndex: 'modelCode'},
					{header: "车型名称", dataIndex: 'modelName'}
			      ];

	function myLink(value,meta,record){
		return "<input type='radio' name='rd' onclick='setModel(\""+record.data.modelId+"\",\""+record.data.modelName+"\",\""+record.data.priceOne+"\",\""+record.data.modelCode+"\",\""+record.data.brandName+"\")' />"
	}
	
	function setModel(modelId,modelName,priceOne,modelCode,brandName){
		parentContainer.returnValue(modelName,modelCode,brandName);
		_hide();
	}
	
	function setPro(proId){
		$('proId').value = proId;
		__extQuery__(1);
	}
	
	function clsTxt(){ //清除经销商文本框
		_hide();
	}

		var tree_root_id = {"tree_root_id" : ""};
 	var subStr = "funlist";
 		
	function createModelTree() {
	$('dtree').setStyle("top",59);
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
	var proid = a.aNodes[id].id;
	var proname = a.aNodes[id].name;
	$('proId').value = proid;
	__extQuery__(1);
 }
 
  function createTree(reobj) {
	var prolistobj = reobj[subStr];
	var proId,parentProId,proName,proCode;
	for(var i=0; i<prolistobj.length; i++) {
		proId = prolistobj[i].proId;
		proName = prolistobj[i].proName;
		proCode = prolistobj[i].proCode;
		parentProId = prolistobj[i].parentProId;
		if(parentProId == proId) { //系统根节点
			a.add(proId,"-1",proName,proCode);
		} else {
			a.add(proId,parentProId,proName,proCode);
			a.add(proId+"_",proId,"loading...","","","",a.icon.loading,"","");
		}
	}
	
	a.draw();
 }

 function createNode(reobj) {
	var prolistobj = reobj[subStr];
	var proId,parentProId,proName,proCode;
	a.remove(addNodeId+"_");
	for(var i=0; i<prolistobj.length; i++) {
		proId = prolistobj[i].proId;
		proName = prolistobj[i].proName;
		proCode = prolistobj[i].proCode;
		parentProId = prolistobj[i].parentProId;
		a.add(proId,addNodeId,proName,proCode);
			a.add(proId+"_",proId,"loading...","","","",a.icon.loading,"","");
	}
	a.draw();
 }
</script>
</html>
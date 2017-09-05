<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=contextPath %>/style/content.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/dtree1.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/mootools.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/dtree.js"></script>
<title>个人资料</title>
<style>
.img {
	border: none
}
</style>
</head>

<script type="text/javascript">
	var fun_tree_url = "<%=contextPath%>/individualInfo/individual/IndiviDual/initFunTree.json";
	var userfuns = "<%=request.getAttribute("userfuns")%>";
	var thisPose = "<%=request.getAttribute("poseId")%>";
</script>

<body onload="showFunsTree()">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 个人信息 &gt; 个人信息 &gt; 个人资料</div>
	<form id="fm" name="fm" action="" method="post">
	<input value="" type="hidden" name="deptId" />
	<input value="" type="hidden" name="poseId" />
</form>

<table class="table_list" style="border-bottom:1px solid #DAE0EE"  border="0" id="roll">
	<tr class="table_list_th">
		<td colspan="1"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;功能列表</td>
	</tr>
	<tr id="row1" class="table_list_th" >
		<td valign="top" style="padding:10px;line-height:1em;">
			<div class="dtree" id="funTree">
				<script type="text/javascript">
					var b;
				</script>
			</div>
		</td>
	</tr>
</table>

</body>
<script type="text/javascript">
var myfuns = new Array();

function isVev(fId) {
	for(var n=0; n<myfuns.length; n++) {
		if(myfuns[n].indexOf(fId) == 0) {
			return true;
		}
	}
	return false;
}

	function showFunsTree() {
		if(userfuns != "") {
			var sp = userfuns.split(",");
			for(var i=0; i<sp.length; i++) {
				myfuns.push(sp[i]);
			}
		}
		b = null;
		b = new dTree('b','funTree','false','false','true'); 
		b.config.closeSameLevel=false;
		b.config.myfun="setDataAuth";
		b.config.folderLinks=false;
		sendAjax(fun_tree_url,createFunTree,'fm');
		b.closeAll();
	}
	
	function createFunTree(reobj) {
		var funlistobj = reobj["funlist"];
		var funcCode,parFuncId,funcId,funcName;
		for(var i=0; i<funlistobj.length; i++) {
			parFuncId = funlistobj[i].parFuncId;
			funcId = funlistobj[i].funcId;
			funcName = funlistobj[i].funcName;
			funcCode = funlistobj[i].funcCode;
			if(parFuncId == funcId) {
				b.add(funcId,"-1",funcName);
			} else if(funcId.length<=8 && isVev(funcId)){
				b.add(funcId,parFuncId,funcName);
			}
		}
		b.draw();
		b.openAll();
	}

	function setDataAuth(id) {
		
	}
</script>
</html>
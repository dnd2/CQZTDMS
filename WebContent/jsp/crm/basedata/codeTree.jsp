<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>OpenOfficeGeneration Documentation</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/tree.css" />
	<style>
	body {
	background-color: #FFFFFF;
	}
	.mover {
	background-color: #FF9999;
	color: #FFEEEE;
	}
	
	.special {
	color: #CC0000;
	}
	
	.tafelTree h3, .tafelTree p, .tafelTree ol {
	margin: 0;
	padding: 0;
	}
	
	.tafelTree p {
	padding-bottom: 1em;
	}
	
	.tafelTree h3 {
	color: #009900;
	}
	.float1{
		width:30%;
		height:200px;
		float:left;
		background-color:blue;
		margin-left:20px;
	}
	</style>
<!--	<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/basedata/prototype.js"></script>-->
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/prototype.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/basedata/effects.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/basedata/builder.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/basedata/controls.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/basedata/slider.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/basedata/dragdrop.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/basedata/codeTree.js"></script>
<!--	<script type="text/javascript" src="js/prototype.js"></script>-->
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/basedata/scriptaculous.js"></script>
<!--	<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/basedata/Tree-optimized.js"></script>-->
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/basedata/Tree.js"></script>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  潜客管理 &gt; 客户管理 &gt; 竞品车型列表</div>
	<form id="fm" name="fm" method="post">
<script type="text/javascript">
			function funcOpen (branch, response) {
				return true;
			}
			var tree = null;
			var structs =null;
			function TafelTreeInit () {
				codeTree.loadTree('','fm', tree, funClick);
				/*
				var structs = [
				{
				'id':'root1',
				'txt':'顶部',
				'items':[
					{
					'id':'child1',
					'txt':'Un enfant1',
					'canhavechildren' : true,
					'items':[{
						'id':'child11',
					'txt':'Un enfant11',
					'canhavechildren' : true
					}]
					},
					{
					'id':'child2',
					'txt':'Un enfant 2',
					'canhavechildren' : true
					},
					{
					'id':'child3',
					'txt':'Un enfant 3',
					'canhavechildren' : true
					},
					{
					'id':'child4',
					'txt':'Un enfant 4',
					'canhavechildren' : true
					}
				]
				}
				];
				tree = new TafelTree('codeTree', structs, {
					'generate' : true,
					'imgBase' : '/dms/images/imgs/',
					'defaultImg' : 'page.gif',
					'defaultImgOpen' : 'folderopen.gif',
					'defaultImgClose' : 'folder.gif',
					'onClick' : funClick
				});
				*/
			}
			
		function funClick(one,two,three){
			MyAlert(one.struct.id);
		}
		
		
		String.prototype.replaceAll = function(s1,s2) { 
    		return this.replace(new RegExp(s1,"gm"),s2); 
		}
			</script>
			<br /><br />
			<div style="width:100%;">
			<div class="float1"></div>
			<div class="float1">所有下拉列表
			<div id="codeTree"></div>
			</div>
			<div class="float1"></div>
			</div>
	</form>
	</div>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/mootools.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/web/dealer_black_tree.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/web/dtree.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/validate/validate.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/jslib/dialog_new.js"></script>
	<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/dict.js"></script>
<title>经销商用户维护(SGM)</title>
<style>
.img {
	border: none
}
</style>
</head>

<body onload="showPan();">
<form id="fm2" name="fm2">
<input type="hidden" name="curPage2" id="curPage2" value="1" />
<input type="hidden" name="DEPT_ID" id="DEPT_ID" value="" />
<input type="hidden" id="orderCol2" name="orderCol2" value="" />
<input type="hidden" id="order2" name="order2" value="" />
<div id='pan' style="z-index: 3000;position:absolute;border:1px solid #5E7692;background: #FFFFFF; width: 715px;height: 379px;">
	<div id='myquery' style="z-index: 3001;position:absolute;border:1px solid #5E7692;width: 715px;height: 30px;">
		<table class="table_info" border="0" style="height: 30px;" width="100%">
		<tr>
				<td class="table_query_3Col_label_5Letter" nowrap="nowrap">经销商代码：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<input class="middle_txt" id="DRLCODE" datatype="1,is_noquotation,30" name="DRLCODE" onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
				</td>
				<td class="table_query_3Col_label_5Letter" nowrap="nowrap">经销商简称：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<input class="middle_txt" id="DELSNAME" datatype="1,is_noquotation,30" name="DELSNAME" onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
				</td>
				<td class="table_query_3Col_input" nowrap="nowrap"><input class="normal_btn" type="button" value="查 询" id="queryBtn2" onclick="getDrl(1)"/>
				<input class="normal_btn" type="button" value="重 置" onclick="requery2()"/></td>
			</tr>
		</table>
	</div>
	<div id='dtree' class="dtree" style="z-index: 3000;position:absolute;border:1px solid #5E7692;width: 213px;height: 349px;">
        <script type="text/javascript">
        a = new dTree('a','dtree','false','false','true');
        </script>
    </div>
    <div id="drlv" style="z-index: 3000;position:absolute;border:1px solid #5E7692;width: 501px;height: 349px;  overflow-y: auto; overflow-x:hidden;">
    	<br />
    	<table width="100%">
    		<tr>
    			<td>
    				<div id="_page2" style="display:none;"></div>
					<div id="myGrid2" ></div>
					<div id="myPage2" class="pages"></div>
    			</td>
    		</tr>
    	</table>
    </div>
</div>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<div id="erdiv" style="position: absolute; top:-1000px; background: #FDFFCE; height: 17px; border:1px solid #FFBA43; display: none;">
<img style="margin-top: 1px; margin-left: 2px;" src="<%=contextPath%>/img/exclamation.gif" />
<span id="ermsg" style="color: red; position: absolute; margin-top: 3px;"></span></div>
</body>
</html>
<script>
validateConfig.isOnBlur = false;
var drlurl = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/drlQuery.json?COMMAND=1";
var tree_url = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/initOrgTree.json";








</script>
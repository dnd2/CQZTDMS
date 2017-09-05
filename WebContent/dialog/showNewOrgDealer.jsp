<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>

<!-- created by andy.ten@tom.com 20100526 通用选择经销商 -->
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
    String inputCode = request.getParameter("INPUTCODE");
    String inputName = request.getParameter("INPUTNAME");
    String isMulti = request.getParameter("ISMULTI");
    String orgId = request.getParameter("ORGID");
    String inputId = request.getParameter("INPUTID");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<link href="<%=contextPath %>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
<jsp:include page="${contextPath}/common/globalVariable.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/mootools.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/jslib/my-grid-pager.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/framecommon/default.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath %>/js/framecommon/DialogManager.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath %>/js/framecommon/dealerTree.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath%>/js/web/dtree.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/validate/validate.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
<script type="text/javascript" src="<%=contextPath %>/js/dict.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/jslib/dialog_new.js"></script>
<title>通用选择框</title>

<script language="JavaScript">
var filecontextPath="<%=contextPath%>";
var inputCode = "<%=inputCode%>";
var inputId = "<%=inputId%>";
var isMulti = "<%=isMulti%>";
var orgId = "<%=orgId%>";
var drlurl = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/allDrlQuery.json?COMMAND=1&ORGID="+orgId;
var tree_url = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/initOrgTree.json?ORGID="+orgId;
function doInit()
{
	$('pan').setOpacity("0");
	$('pan').setStyle("top",-1000);
	panw = $('pan').getStyle('width');
	panh = $('pan').getStyle('height');
	myDealerEffects = new Fx.Styles('pan', {duration: 277,transition: Fx.Transitions.linear});
	showPan(isMulti ,orgId);
}
function test()
{
	var dealerCode = document.getElementById("DEALER_NAME").value;
	var dealerIds = document.getElementById("DEALER_IDS").value;
	if(dealerCode && dealerCode.length > 0)
	{
		//add by liuqiang, 区分系统登录和接口登录,接口登录没有 parent.$('inIframe'),需要判断
		if (parent.$('inIframe')) 
		{
			parentDocument.getElementById(inputCode).value = document.getElementById("DEALER_NAME").value;
		} else 
		{
			if(parent.$(inputCode))
				parent.$(inputCode).value = document.getElementById("DEALER_NAME").value;
			else if(window && window.dialogArguments)
			{
				window.dialogArguments.$(inputCode).value = document.getElementById("DEALER_NAME").value;
				try
				{
					window.dialogArguments.modalDialogWinBeforeClose();
				}catch(e){}	
			}
		}
	}
	if(dealerIds && dealerIds.length > 0)
	{
		if (parent.$('inIframe')) 
		{
			parentDocument.getElementById(inputId).value = document.getElementById("DEALER_IDS").value;
		} else 
		{
			parent.$(inputId).value = document.getElementById("DEALER_IDS").value;
		}
		
	}	
}

function doNewConfirm()
{
	//MyAlert(1111);
	var tab = document.getElementById("myTable2");
	if(!tab)
	{
		_hide();
		closePan();
	}
	var codes = "";
	var ids = "";
	if(tab.rows.length >1)
	{
		for(var i=1; i < tab.rows.length; i++)
		{
			var checkObj = tab.rows[i].cells[1].firstChild;
			if(checkObj.checked ==  true)
			{
				var code = tab.rows[i].cells[3].innerText;
				if(codes)
					codes += "," + code;
			    else
			        codes = code;
			    var id = tab.rows[i].cells[1].firstChild.id;
			    if(ids.length > 0)
			        ids += "," + id;
			    else
			        ids = id;
			      
			}
		}
	}
	if(codes && codes.length > 0)
	   $('DEALER_NAME').value = codes;
	if(ids && ids.length > 0)
	   $('DEALER_IDS').value = ids;
	_hide();
	closePan();
}

</script>
</head>
<body onbeforeunload="test();">
<div id="_page" style="margin-top:15px;display:none;"></div>
<div id="myGrid" ></div>
<div id="myPage" class="pages"></div>
<form method="post" name ="fm" id="fm3" style="display:none">
  <table>
	<input id="DEALER_CODE" name="DEALER_CODE" type="hidden" />
	<input id="DEALER_NAME" name="DEALER_NAME" type="hidden"/>
	<input id="DEALER_IDS" name="DEALER_IDS" type="hidden"/>
  </table>	
</form>	
<form id="fm2" name="fm2">
	<input type="hidden" id="tree_root_id" name="tree_root_id" value="" />
	<input id="DEALER_ID" name="DEALER_ID" type="hidden"/>
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
		<div id='dtree' class="dtree" style="z-index: 3000;position: absolute;overflow:auto;border:1px solid #5E7692;width: 213px;height: 349px;">
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
	    	<div id="sel">
	    	     <input class="normal_btn" type="button" value="全选" onclick="doAllClick()"/>
	    	     <input class="normal_btn" type="button" value="全不选" onclick="doDisAllClick()"/>
	    	     <input class="normal_btn" type="button" value="确认" onclick="doNewConfirm()"/>
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
    	
	</div>
	<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
</body>
</html>
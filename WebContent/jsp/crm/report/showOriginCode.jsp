<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
 
<html xmlns="http://www.w3.org/1999/xhtml">

<%
	String contextPath = request.getContextPath();
String inputCode = request.getParameter("INPUTCODE");
String isMulti = request.getParameter("ISMULTI");
String orgId = request.getParameter("ORGID");
String isDealerType=request.getParameter("isDealerType");
String areaId=request.getParameter("AREA_ID");
%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<link href="<%=contextPath %>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/mootools.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/framecommon/default.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath %>/jsp/crm/report/originList.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/validate/validate.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath %>/js/jslib/dialog_new.js"></script>
<title>通用选择框</title>

<script language="JavaScript">
var filecontextPath="<%=contextPath%>";
var inputCode = "<%=inputCode%>";
var isMulti =  "<%=isMulti%>";
var orgId = "<%=orgId%>";
var isDealerType="<%=isDealerType%>";
var areaId= "<%=areaId%>";
var drlurl = "<%=contextPath%>/crm/report/ClueReportTotal/getLeadsOriginListAll.json?COMMAND=1&ORGID="+orgId+"&AREA_ID="+areaId+"&isDealerType="+isDealerType;
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
{	var inputNames = document.getElementById("DEALER_NAME").value;
	var dealerCode = document.getElementById("DEALER_CODE").value;
	var dealerIds = document.getElementById("DEALER_IDS").value;
	if(dealerCode && dealerCode.length > 0)
	{
		//add by liuqiang, 区分系统登录和接口登录,接口登录没有 parent.$('inIframe'),需要判断
		if (parent.$('inIframe')) 
		{
			parent.$('inIframe').contentWindow.$(inputCode).value = document.getElementById("DEALER_CODE").value;	
		} else 
		{
			if(parent.$(inputCode))
				parent.$(inputCode).value = document.getElementById("DEALER_CODE").value;
			else if(window && window.dialogArguments)
			{
				window.dialogArguments.$(inputCode).value = document.getElementById("DEALER_CODE").value;
				try
				{
					window.dialogArguments.modalDialogWinBeforeClose();
				}catch(e){}	
			}
		}
	}
	if(inputNames && inputNames.length > 0)
	{
		if (parent.$('inIframe')) 
		{
			if(myinputName!="null" && myinputName!=null && myinputName!="" ){
			parent.$('inIframe').contentWindow.$(myinputName).value = document.getElementById("DEALER_NAME").value;
			}
		} else 
		{
			if(myinputName!="null" && myinputName!=null && myinputName!="" ){
			parent.$(myinputName).value = document.getElementById("DEALER_NAME").value;
			}
		}
		
	}	
	if(dealerIds && dealerIds.length > 0)
	{
		if (parent.$('inIframe')) 
		{
			parent.$('inIframe').contentWindow.$(inputId).value = document.getElementById("DEALER_IDS").value;
			
		} else 
		{
			parent.$(inputId).value = document.getElementById("DEALER_IDS").value;
		}
		
	}	
}

</script>
</head>
<body onload="genLocSel('txt1','','','','','');" onbeforeunload="test();">
<div id="_page" style="margin-top:15px;display:none;"></div>
<div id="myGrid" ></div>
<div id="myPage" class="pages"></div>
<form method="post" name ="fm" id="fm3" style="display:none">
  <table>
  <tr>
  <td>
	<input id="DEALER_CODE" name="DEALER_CODE" type="text" value=""/>
	<input id="DEALER_NAME" name="DEALER_NAME" type="text"/>
	<input id="DEALER_IDS" name="DEALER_IDS" type="text" value=""/>
	</td>
	</tr>
  </table>	
</form>	
<form id="fm2" name="fm2">
	<input type="hidden" id="tree_root_id" name="tree_root_id" value="" />
	<input id="DEALER_ID" name="DEALER_ID" type="hidden"/>
	<input type="hidden" name="curPage2" id="curPage2" value="1" />
	<input type="hidden" name="DEPT_ID" id="DEPT_ID" value="" />
	<input type="hidden" id="orderCol2" name="orderCol2" value="" />
	<input type="hidden" id="order2" name="order2" value="" />
	 
	<div id='pan' style="z-index: 3000;position:absolute;border:1px solid #5E7692;background: #FFFFFF; width: 715px;height: 385px;">
		<div id='myquery'  >
			 
						<input class="middle_txt" id="DRLCODE" datatype="1,is_noquotation,30" name="DRLCODE" onblur="isCloseDealerTreeDiv(event,this,'pan')" type="hidden"/>
                    <%--<td class="table_query_3Col_label_5Letter" nowrap="nowrap" sytle="display:none">区域：</td>
                    <td class="table_query_3Col_input" nowrap="nowrap" sytle="display:none">
                        <select class="short_sel" id="txt1" name="downtown"></select>
                        <!--<select name="downtown" id="downtown__">
                                <option value="">-请选择-</option>
                             <c:forEach items="${list}" var="downtownList" >
                                <option value="${downtownList.REGION_ID }">${downtownList.REGION_NAME }</option>
                            </c:forEach>
                        </select>-->
                    </td>--%>
		 
		</div>
		<div id='myquery1'  >
		<div id='dtree' class="dtree"  >
    	</div>
    	<div id="drlv"  >
	    <br />
	    	<table width="100%">
	    		<tr>
	    			<td>
	    				<div id="_page2" style="display:none;"></div>
						<div id="myGrid2" ></div>
						<div id="myPage2" class="pages" ></div>
	    			</td>
	    		</tr>
	    	</table>
	    	<div id="sel">
	    	     <input class="normal_btn" type="button" value="全选" onclick="doAllClick()"/>
	    	     <input class="normal_btn" type="button" value="全不选" onclick="doDisAllClick()"/>
	    	     <input class="normal_btn" type="button" value="确认" onclick="doConfirm()"/>
	    	</div>
	    	<script language="JavaScript">
	    	if(isMulti == "true")
		    	document.getElementById("sel").style.display = "";
	    	else
	    	{
	    		document.getElementById("sel").style.display = "";
	    	}	
	    	</script>
    	</div>
    	</div>
    	</div>
    	
	<div id="loader"  ></div>
</form>
</body>
</html>
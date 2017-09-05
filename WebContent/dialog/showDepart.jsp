<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>

<!-- created by andy.ten@tom.com 20100526 通用职位选择 -->
<html xmlns="http://www.w3.org/1999/xhtml">

<%
	String contextPath = request.getContextPath();
    String inputCode = request.getParameter("INPUTCODE");
    String myinputName = request.getParameter("inputName");
    String isMulti = request.getParameter("ISMULTI");
    String orgId = request.getParameter("ORGID");
    String inputId = request.getParameter("INPUTID");
    String isAllLevel = request.getParameter("ISALLLEVEL");
    String isAllArea = request.getParameter("ISALLAREA");
    String isDealerType=request.getParameter("isDealerType");
    String areaId=request.getParameter("AREA_ID");
    System.out.println("isDealerType : " + isDealerType);
%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<link href="<%=contextPath %>/style/content.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
<jsp:include page="${contextPath}/common/globalVariable.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/regionData.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/mootools.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/jslib/my-grid-pager.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/framecommon/default.js" charset="UTF-8"></script>
<%-- <script type="text/javascript" src="<%=contextPath %>/js/framecommon/DialogManager.js" charset="UTF-8"></script> --%>
<script type="text/javascript" src="<%=contextPath %>/js/framecommon/departTree.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath%>/js/framecommon/departDtree.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/validate/validate.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=FileConstant.codeJsUrl%>"></script>
<script type="text/javascript" src="<%=contextPath %>/js/dict.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/jslib/dialog_new.js"></script>
<title>通用选择框</title>

<script language="JavaScript">
<!--

var parentContainer = parent || top;
if( parentContainer.frames ['inIframe'] ){
	parentContainer = parentContainer.document.getElementById ('inIframe').contentWindow;
}
var parentDocument =parentContainer.document;

var filecontextPath="<%=contextPath%>";
var inputCode = "<%=inputCode%>";
var inputId = "<%=inputId%>";
var myinputName = "<%=myinputName%>";
var isMulti = "<%=isMulti%>";
var orgId = "<%=orgId%>";
var isDealerType="<%=isDealerType%>";
var areaId="<%=areaId%>";
var drlurl = "<%=contextPath%>/customerRelationships/baseSetting/OrgCustomSearch/queryOrgCustomUser.json";
var tree_url = "<%=contextPath%>/customerRelationships/baseSetting/OrgCustomSearch/initOrgTree.json?levelType=all";
function doInit()
{
	$('pan').setOpacity("0");
	$('pan').setStyle("top",-1000);
	panw = $('pan').getStyle('width');
	panh = $('pan').getStyle('height');
	myDealerEffects = new Fx.Styles('pan', {duration: 277,transition: Fx.Transitions.linear});
	showDepartPan(isMulti ,'');
}
function test()
{	
}

--></script>
</head>
<body onbeforeunload="test();">
<div id="_page" style="margin-top:15px;display:none;"></div>
<div id="myGrid" ></div>
<div id="myPage" class="pages"></div>
<form method="post" name ="fm" id="fm3" style="display:none">
  <table>
  <tr>
  <td>
	<input id="DEALER_CODE" name="DEALER_CODE" type="hidden" value=""/>
	<input id="DEALER_NAME" name="DEALER_NAME" type="hidden"/>
	<input id="DEALER_IDS" name="DEALER_IDS" type="hidden" value=""/>
	</td>
	</tr>
  </table>	
</form>	
<form id="fm2" name="fm2">
	<input type="hidden" id="FUNS" name="FUNS" />
	<input type="hidden" id="tree_root_id" name="tree_root_id" value="" />
	<input id="DEALER_ID" name="DEALER_ID" type="hidden"/>
	<input type="hidden" name="curPage2" id="curPage2" value="1" />
	<input type="hidden" name="DEPT_ID" id="DEPT_ID" value="" />
	<input type="hidden" id="orderCol2" name="orderCol2" value="" />
	<input type="hidden" id="order2" name="order2" value="" />
	<input type="hidden" id="isAllLevel" name="isAllLevel" value="<%=isAllLevel%>" />
	<input type="hidden" id="isAllArea" name="isAllArea" value="<%=isAllArea%>" />
	<div id='pan' style="z-index: 3000;position:absolute;border:1px solid #5E7692;background: #FFFFFF; width: 980px;height: 440px;">
		<div id='myquery' style="z-index: 3001;position:absolute;border:1px solid #5E7692;width: 980px;height: 60px;">
			<table class="table_info" border="0" style="height: 60px;" width="100%">
			<tr>
					<td class="table_query_3Col_label_5Letter" nowrap="nowrap">姓名：</td>
					<td class="table_query_3Col_input" nowrap="nowrap">
						<input class="middle_txt" id="DRLCODE" datatype="1,is_noquotation,30" name="USERNAME" onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
					</td>
					<td class="table_query_3Col_label_5Letter" nowrap="nowrap">编号：</td>
					<td class="table_query_3Col_input" nowrap="nowrap">
						<input class="middle_txt" id="DELSNAME" datatype="1,is_noquotation,30" name="USERCODE" onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
					</td>
				</tr>
			<tr>
				
<!--				<td class="table_query_3Col_label_5Letter" nowrap="nowrap">经销商评级：</td>-->
<!--				<td class="table_query_3Col_input" nowrap="nowrap">-->
<!--					<script type="text/javascript">-->
<!--		   	 			genSelBoxExp("dealerClass",<%=Constant.DEALER_CLASS_TYPE%>,"",true,"short_sel","onchange='getPlanVer()'","false",'');-->
<!--					</script>	-->
<!--				</td>-->

	
				<td class="table_query_3Col_label_5Letter" nowrap="nowrap"></td>
				
				<td  nowrap="nowrap" align="center"><input class="normal_btn" type="button" value="查 询" id="queryBtn2" onclick="getDrl(1)"/>
					<input class="normal_btn" type="button" value="重 置" onclick="requery2()"/>
					<input name="button" type="button" class="normal_btn" onclick="_hide();" value="关 闭" />
					 </td>
					 <td class="table_query_3Col_input" nowrap="nowrap">
					
				</td>
					 
				<td class="table_query_3Col_input" nowrap="nowrap" style="display:none">区域：</td>
				<td class="table_query_3Col_input" nowrap="nowrap" style="display:none">
					<select class="short_sel" id="txt1" name="downtown"></select>
					<!--<select name="downtown" id="downtown__">
							<option value="">-请选择-</option>
	      				 <c:forEach items="${list}" var="downtownList" >
	       					<option value="${downtownList.REGION_ID }">${downtownList.REGION_NAME }</option>
		   				</c:forEach>
      				</select>-->
				</td>	
			</tr>
			</table>
		</div>
		<div id='myquery1' style="z-index: 3000;position:absolute;border:1px solid #5E7692;width: 980px;top: 30px;height: 0px;">
		<div id='dtree' class="dtree" style="z-index: 3000;position: absolute;overflow:auto;border:1px solid #5E7692;width: 210px;height: 378px;top: 30px;">

    	</div>
    	<div id="drlv" style="z-index: 3000;position:absolute;border:1px solid #5E7692;width: 768px;height: 378px;  overflow-y: auto; overflow-x:auto;">
	    <br />
	    	<table width="100%">
	    		<tr>
	    			<td valign="top">
	    				<div  style="height: 378px; overflow-y: auto; overflow-x: hidden;">
	    				<div id="_page2" style="display:none;"></div>
						<div id="myGrid2" ></div>
						<div id="myPage2" class="pages" ></div>
						</div>
	    			</td>
	    			<td>
	    				<input class="normal_btn" style = "margin: 5px 10px;" type="button" value="==》添加" onclick="addSelect();"/></br>
	    				<input class="normal_btn" style = "margin: 5px 10px;" type="button" value="《==删除" onclick="delSelect();"/></br>
	    				<input class="normal_btn" style = "margin: 5px 10px;"  type="button" value="确认" onclick="chooseConfim();"/>
	    			</td>
	    			<td valign="top">
		    			<div style="height: 378px; overflow-y: auto; overflow-x: hidden;">
		    				<table class="table_list">
					 			<tr class="table_list_th">
					 				<th>序号</th>
					 				<th><input type= "checkBox" id="rightGet" onclick = "rightAllClick();"/></th>
					 				<th>用户名</th>
					 				<th>职位类型</th>
<!-- 					 				<th>职位名称</th> -->
					 			</tr>
					 			<tbody id="tbody2">
					 					
					 			</tbody>
					 		</table>
		    			</div>
	    			</td>
	    		</tr>
	    	</table>
	    	
	    	<div id="sel">
<!-- 	    	     <input class="normal_btn" type="button" value="全选" onclick="doAllClick()"/> -->
<!-- 	    	     <input class="normal_btn" type="button" value="全不选" onclick="doDisAllClick()"/> -->
<!-- 	    	     <input class="normal_btn" type="button" value="确认" onclick="chooseConfim()"/> -->
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
<!--     	<div style="z-index: 3000;position:absolute;border:1px solid #5E7692;width: 384px;height: 378px;  overflow-y: auto; overflow-x:auto;"> -->
<!--     		<table class="table_list"> -->
<!-- 	 			<tr> -->
<!-- 	 				<th>序号</th> -->
<!-- 	 				<th>操作</th> -->
<!-- 	 				<th>用户名</th> -->
<!-- 	 				<th>职位类型</th> -->
<!-- 	 				<th>职位名称</th> -->
<!-- 	 			</tr> -->
<!-- 	 			<tbody id="tbody2"> -->
<!-- 	 					<tr class="table_list_row2"> -->
<!-- 	 						<td>1</td> -->
<!-- 	 						<td><a href="javascript:;" onclick="">[删除]</a></td> -->
<%-- 	 						<td>${list.NAME }<input type='hidden' name='recOemDealer' value='${list.USER_ID }'/></td> --%>
<!-- 	 						<td>dsfasdf</td> -->
<!-- 	 						<td>dsfasfds</td> -->
<!-- 	 					</tr> -->
<!-- 	 			</tbody> -->
<!-- 	 		</table> -->
<!--     	</div> -->
    	
    	
    	</div>
    	</div>
    	
	<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@page import="com.infodms.dms.common.FileConstant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
    String inputCode = request.getParameter("INPUTCODE");
    String inputName = request.getParameter("INPUTNAME");
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
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" /> 
	<link href="<%=contextPath %>/style/dtree1.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/dtree.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/jmstyle/jslib/dealer.js"></script>
	<title>通用选择框</title>
	
	<script language="JavaScript">
		var filecontextPath="<%=contextPath%>";
		var inputCode = "<%=inputCode%>";
		var inputId = "<%=inputId%>";
		var inputName = "<%=inputName%>";
		var isMulti = "<%=isMulti%>";
		var orgId = "<%=orgId%>";
		var isDealerType="<%=isDealerType%>";
		var areaId="<%=areaId%>";
		var drlurl = url = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/allDrlQueryForZotye.json?COMMAND=1&ORGID="+orgId+"&AREA_ID="+areaId+"&isDealerType="+isDealerType;
		var tree_url = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/initOrgTree.json?ORGID="+orgId;
		
		$(function() {
			TreeFunc.loadData( {
				url: tree_url,
				dom: $('#dtree'),
				ajax: sendAjax,
				formName: 'fm2',
				cols: {id: 'orgId', name: 'orgName', parent: 'parentOrgId'},
				rootPath: filecontextPath,
				nodeUrl: 'url',
				myFun: 'Dealer.dealerPos'
			}, function( o ) {
				tree = o;
				Dealer.init( 'tree', __extQuery__ , isMulti);
			});
			
			if(isMulti == "true")
				document.getElementById("sel").style.display = "";
			else
			{
				document.getElementById("sel").style.display = "none";
			}
		
			/* genLocSel('txt1','','','','',''); */
			__extQuery__(1);
		});
	</script>
</head>
<body class="dealer-show-rel"> 
<div class="wbox">	
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
	<form id="fm" name="fm2">
		<input type="hidden" id="tree_root_id" name="tree_root_id" value="" />
		<input id="DEALER_ID" name="DEALER_ID" type="hidden"/>
		<input type="hidden" name="curPage2" id="curPage2" value="2" />
		<input type="hidden" name="DEPT_ID" id="DEPT_ID" value="" />
		<input type="hidden" id="orderCol2" name="orderCol2" value="" />
		<input type="hidden" id="order2" name="order2" value="" />
		<input type="hidden" id="isAllLevel" name="isAllLevel" value="<%=isAllLevel%>" />
		<input type="hidden" id="isAllArea" name="isAllArea" value="<%=isAllArea%>" />
		<div id='pan' class="couple-panel">
			<div id='myquery' class="panel-top-bar">
				<input type="hidden" id="hideCheckedId"/>
				<input type="hidden" id="hideCheckedDealerId"/>
				<div class="form-panel">
					<h2>筛选条件</h2>
					<div class="form-body">
						<table class="table_query" style="height: 60px;" width="100%">
							<tr>
								<td>经销商代码：</td>
								<td>
									<input class="middle_txt" id="DRLCODE" datatype="1,is_noquotation,30" name="DRLCODE" onblur="" type="text"/>
								</td>
								<td>经销商简称：</td>
								<td>
									<input class="middle_txt" id="DELSNAME" datatype="1,is_noquotation,30" name="DELSNAME" onblur="" type="text"/>
								</td>
							</tr>
							<tr>
								<!-- <td></td> -->
								<td colspan="4" nowrap="nowrap" class="center">
									<input class="u-button u-query" type="button" value="查 询" id="queryBtn" onclick="__extQuery__(1)"/> <!-- getDrl(1,<%=isMulti%>) -->
									<input class="u-button u-reset" type="button" value="重 置" onclick="requery2()"/>
									<input class="u-button u-reset" type="button"  onclick="_hide();" value="关 闭" />
								<!-- &nbsp;&nbsp;&nbsp;&nbsp;已选中:<label style='color:red;' id='checkedNumNodeId'></label> -->
									</td>
									<!-- <td>
								</td> -->
									
								<!-- <td style="display:none">区域：</td>
								<td style="display:none">
									<select class="short_sel" id="txt1" name="downtown"></select>
								</td>	 -->
							</tr>
						</table>
					</div>
				</div>
			</div>
			<div id='myquery1' class="panel-main">
				<div class="dtree panel-col-left form-panel">
					<h2>销售区域信息</h2>
					<div class="form-body">
						<div id="dtree" class="dtree"></div>
					</div>
				</div>
				<div id="drlv" class="panel-col-right form-panel">
					<h2>经销商信息</h2>
					<div class="form-body dealer-list">
						<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
						<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
						<div id="sel">
							<input class="u-button" type="button" value="全选" onclick="doAllClick()"/>
							<input class="u-button" type="button" value="全不选" onclick="doDisAllClick()"/>
							<input class="u-button u-submit" type="button" value="确认" onclick="doConfirm()"/>
						</div>
					</div>		
				</div>
			</div>
		</div>
	</form>
</div>
<script>
function doAllClick() {
	$('input[name="chkbox"]').prop("checked",true); 
}

function doDisAllClick() {
	$("input[name='chkbox']").prop("checked", false); 
}

function doConfirm() {
	//var chkbox = document.getElementsByName("chkbox") ;
	
	var chks=$("input:checked");
	
	var len = chks.length;
	
	var sDealerId = "" ;
	var sDealerCode = "" ;
	var sDealerName = "" ;
	
	for(var i=0; i<len; i++) {
		if(i == 0) {
			sDealerId = chks[i].value ;
			sDealerCode = document.getElementById( "c" + chks[i].value).value ;
			sDealerName = document.getElementById( "n" + chks[i].value).value ;
		}
		else {
			sDealerId += "," + chks[i].value  ;
			sDealerCode += "," + document.getElementById("c" + chks[i].value).value ;
			sDealerName += "," + document.getElementById("n" + chks[i].value).value ;
		}
	}
	
	$('#DEALER_ID')[0].value = sDealerId;
    $('#DEALER_IDS')[0].value = sDealerId;
    $('#DEALER_NAME')[0].value = sDealerName;

    if ($('#DEPT_NAME')[0] != null) {
        $('#DEPT_NAME')[0].value = "";
    }

    if ($('#DEPT_ID')[0] != null) {
        $('#DEPT_ID')[0].value = "";
    }
    
    try{
    	if(inputId!=undefined && inputId!='' && inputId != null && inputId != 'null'){
    		 parentContainer.document.getElementById(inputId).value = sDealerId;
    	}
    	if(inputCode!=undefined && inputCode!='' && inputCode != null && inputCode != 'null'){
       		 parentContainer.document.getElementById(inputCode).value = sDealerCode;
       	}
    	if(inputName!=undefined && inputName!='' && inputName != null && inputName != 'null'){
       		 parentContainer.document.getElementById(inputName).value = sDealerName;
       	}
    }catch(e){}

    try {
        _hide();
    } catch (e) {
        window.close();
    } 
}
</script>
</body>
</html>
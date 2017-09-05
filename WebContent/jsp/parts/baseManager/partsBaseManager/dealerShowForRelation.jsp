<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>

<!-- created by andy.ten@tom.com 20100526 通用选择经销商 -->
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
    String vId=request.getParameter("vId");
%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" /> 
<link href="<%=contextPath %>/style/dtree1.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/dtree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jmstyle/jslib/dealer.js"></script>
<title>通用选择框</title>

<script language="JavaScript">


var filecontextPath="<%=contextPath%>";
var inputCode = "<%=inputCode%>";
var inputId = "<%=inputId%>";
var myinputName = "<%=myinputName%>";
var isMulti = <%=isMulti%>;
var orgId = "<%=orgId%>";
var isDealerType="<%=isDealerType%>";
var areaId="<%=areaId%>";
var vId ="<%=vId%>" ;
var drlurl = url = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/allDrlQuery.json?COMMAND=1&ORGID="+orgId+"&AREA_ID="+areaId+"&isDealerType="+isDealerType;
var tree_url = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/initOrgTree.json?ORGID="+orgId;
var parentContainer = parent || top;
var parentDocument =parentContainer.document;
var tree;

function doConfirmAdd(){
   var flag = Dealer.doConfirmAddRelation();
   if(!flag){
      layer.msg("请选择经销商!", {icon: 15});
   }else{
	    //var fatherId = parentDocument.getElementById("FATHER_ID").value;
	    //var fatherCode = parentDocument.getElementById("FATHER_CODE").value;
	    //var fatherName = parentDocument.getElementById("FATHER_NAME").value;
	    var fatherId = vId ;
	    var sunId = $("#DEALER_IDS")[0].value;
		var array = sunId.split(",");
		if(array&&array.length){
			for(var i=0;i<array.length;i++){
				if(fatherId==array[i]){
					layer.msg("不能添加自己为下级,请重新选择子机构!", {icon: 15});
					return;
				}
			}
		}

		MyConfirm("确定添加下级单位?", function() {
			sendAjax('<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesRelation/saveSalerRelation.json?FATHER_ID='+fatherId,addResult,'fm2');
		});
    }
}

function addResult(jsonObj){
   if(jsonObj!=null){
	    var error = jsonObj.error;
	    var success = jsonObj.success;
	    if(error){
	    	layer.msg(error, {icon: 2});
	    }
	    if(success){
	        layer.msg(success, {icon: 1});
	        parentContainer.__extQuery__(1);
	        _hide();
	    }
	}
}

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
		Dealer.init( 'tree', __extQuery__, isMulti);
		if(isMulti){
			document.getElementById("sel").style.display = "";
		}else {
			document.getElementById("sel").style.display = "none";
		}	
	});

	new CheckboxHelper( {chEl: '.dealer-check'} );
	genLocSel('txt1','','','','','');
	__extQuery__(1);
	
	$('input[type=radio]').each(function (obj) {
		var t = this;
	});
	
});

var columns = [
	{header: "序号", align:'center', renderer:getIndex,width:'7%'},
	{header: "选择", dataIndex: 'dealerId', align:'center',width: '33px' ,renderer:seled},
	{header: "经销商代码", dataIndex: 'dealerCode', align:'center'},
	{header: "经销商全称", dataIndex: 'dealerName', align:'center'}
];

function seled(value, meta, record) {
	var sIsMulti = typeof sIsMulti == 'undefined' ? true : false;

 	if (sIsMulti)
 		return "<input type='radio' onclick='singleSelect("+value+",\""+record.data.dealerCode+"\",\""+record.data.dealerShortname+"\")' name='de' id='"+value+"' />";
    else
    	return "<input type='checkbox' value='"+value+"' name='"+value+"' id='"+value+"' /><input type='hidden' id=\""+record.data.dealerCode+"\"  value=\""+record.data.dealerCode+"\" />";
}

/* function getDrl(page) {
 	var vurl = drlurl+(drlurl.lastIndexOf("?") == -1?"?":"&")+"curPage2="+page;
	// ==================自定义每页条数start by chenyu=======================
	var pageSizeSelect = document.getElementById("mainPageSize");
	var pageSizePara = '';
	if(pageSizeSelect){
		pageSizePara += "&mainPageSize="+pageSizeSelect.value;
	}
	// ==================自定义每页条数end by chenyu=========================
	// ==================记录用户选中复选框start by chenyu====================
	vurl += pageSizePara; //+ getCheckedParamValues();
	// ==================记录用户选中复选框end by chenyu======================
 	if(submitForm('fm2')) {
 		makeNomalFormCall(vurl, pageBack, 'fm2');
 	} else {
 		$("#queryBtn2")[0].disabled = "";
 	}
} */
</script>
</head>
<body class="dealer-show-rel">
<div class="wbox">
	<form method="post" name ="fm2" id="fm2" style="display:none">
		<table>
			<tr>
				<td>
					<input id="DEALER_CODE" name="DEALER_CODE" type="hidden" value=""/>
					<input id="DEALER_NAME" name="DEALER_NAME" type="hidden" value=""/>
					<input id="DEALER_IDS" name="DEALER_IDS" type="hidden" value=""/>
				</td>
			</tr>
		</table>	
	</form>	
	<form id="fm" name="fm">
		<input type="hidden" id="tree_root_id" name="tree_root_id" value="" />
		<input id="DEALER_ID" name="DEALER_ID" type="hidden"/>
		<input type="hidden" name="curPage2" id="curPage2" value="1" />
		<input type="hidden" name="DEPT_ID" id="DEPT_ID" value="" />
		<input type="hidden" id="orderCol2" name="orderCol2" value="" />
		<input type="hidden" id="order2" name="order2" value="" />
		<input type="hidden" id="isAllLevel" name="isAllLevel" value="<%=isAllLevel%>" />
		<input type="hidden" id="isAllArea" name="isAllArea" value="<%=isAllArea%>" />
		<div id='pan' class="couple-panel">
			<div id='myquery' class="panel-top-bar">
				<div class="form-panel">
					<h2>添加下级单位</h2>
					<div class="form-body">
						<table class="table_info" border="0" style="height: 60px;" width="100%">
							<tr>
								<td class="table_query_3Col_label_5Letter" nowrap="nowrap">经销商代码：</td>
								<td class="table_query_3Col_input" nowrap="nowrap">
									<input class="middle_txt" id="DRLCODE" datatype="1,is_noquotation,30" name="DRLCODE"  type="text"/>
								</td>
								<td class="table_query_3Col_label_5Letter" nowrap="nowrap">经销商简称：</td>
								<td class="table_query_3Col_input" nowrap="nowrap">
									<input class="middle_txt" id="DELSNAME" datatype="1,is_noquotation,30" name="DELSNAME"  type="text"/>
								</td>
							</tr>	
							<tr>
								<td class="table_query_3Col_label_5Letter" nowrap="nowrap">区域：</td>
								<td class="table_query_3Col_input" nowrap="nowrap">
									<select class="short_sel u-select" id="txt1" name="downtown"></select>
									<!--<select name="downtown" id="downtown__">
											<option value="">-请选择-</option>
										<c:forEach items="${list}" var="downtownList" >
											<option value="${downtownList.REGION_ID }">${downtownList.REGION_NAME }</option>
										</c:forEach>
									</select>-->
								</td>
								<td class="table_query_3Col_label_5Letter" nowrap="nowrap">经销商评级：</td>
								<td class="table_query_3Col_input" nowrap="nowrap">
									<script type="text/javascript">
										genSelBoxExp("dealerClass",<%=Constant.DEALER_CLASS_TYPE%>,"",true,"short_sel u-select","","false",'');
									</script>	
								</td>
								<td class="table_query_3Col_input" nowrap="nowrap">
									<input class="u-button u-query" type="button" value="查 询" id="queryBtn2" onclick="__extQuery__(1)"/>
									<input class="u-button u-reset" type="button" value="重 置" onclick="requery2()"/>
								</td>
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
						<div id="sel" class="sel-buttons">
							<input class="u-button dealer-check ch-all" type="button" value="全选" />
							<input class="u-button dealer-check" type="button" value="全不选"/>
							<input class="u-button u-sumbit" type="button" value="确认" onclick="doConfirmAdd();"/>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
</div>	
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件采购关系维护</title>
<script type="text/javascript">
$(function(){
	__extQuery__(1)
});
var myPage;
var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesRelation/salerChildQuery.json";
var title = null;
var columns = [
	{header: "序号", align: 'center', renderer: getIndex, width: '7%'},
	{header: "服务商代码", dataIndex: 'CHILDORG_CODE', style: 'text-align: center;'},
	{header: "服务商", dataIndex: 'CHILDORG_NAME', style: 'text-align: center;'},
	{header: "是否有效", dataIndex:'STATE', align:'center', renderer: getItemValue},
	{id:'action',header: "操作",sortable: false,dataIndex: 'RELATION_ID',renderer:myLink ,align:'center'}
];
function myLink(value,meta,record)
{
	var state = record.data.STATE;
	var childOrgId = record.data.CHILDORG_ID;
		if(state=='<%=Constant.STATUS_DISABLE %>'){
			return String.format("<a href=\"#\" onclick='valid("+value+","+childOrgId+")'>[有效]</a>"
					+"<a href=\"#\" onclick='delRel("+value+","+childOrgId+")'>[删除]</a>");
		}	    
		return String.format("<a href=\"#\" onclick='cel("+value+","+childOrgId+")'>[失效]</a>"
				+"<a href=\"#\" onclick='delRel("+value+","+childOrgId+")'>[删除]</a>");
}

//失效
function cel(value,childOrgId){
	MyConfirm("该单位下的所有下级单位都会失效,确定要失效?",celAction,[[value,childOrgId]]);
}

function celAction(paramArr){
	var value = paramArr[0];
	var childOrgId = paramArr[1];
	var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesRelation/partNotState.json?Id='+value+'&curPage='+myPage.page+'&pId='+childOrgId;
	makeNomalFormCall(url,handleCel,'fm');
}

//有效
function valid(value,childOrgId){
	MyConfirm("确定要有效?",validAction,[[value,childOrgId]]);
}

function validAction(paramArr){
	var value = paramArr[0];
	var childOrgId = paramArr[1];
	var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesRelation/partEnableState.json?Id='+value+'&curPage='+myPage.page+'&pId='+childOrgId;
	makeNomalFormCall(url,handleCel,'fm');
}

//删除
function delRel(value,childOrgId){
	MyConfirm("该单位下的所有下级单位都会删除,确定要删除?",delAction,[[value,childOrgId]]);
}

function delAction(paramArr){
	var value = paramArr[0];
	var childOrgId = paramArr[1];
	var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesRelation/deleteRelation.json?Id='+value+'&curPage='+myPage.page+'&pId='+childOrgId;
	makeNomalFormCall(url,handleCel,'fm');
}
function handleCel(jsonObj) {
if(jsonObj!=null){
	var success = jsonObj.success;
	MyAlert(success, function(){
		__extQuery__(jsonObj.curPage);
	});
}
}
function back(){
	window.location.href="<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesRelation/partSalesRelationInit.do";
}
function add(){
	if(""==$("#dealerCode")[0].value){
		layer.msg("服务商代码不能为空!", {icon: 15});
		return;
	}

	if(submitForm('fm')){	
		var fatherId = $("#FATHER_ID")[0].value;
		var sunId = $("#dealerId")[0].value;
		var array = sunId.split(",");
		if(array&&array.length){
			for(var i=0;i<array.length;i++){
				if(fatherId==array[i]){
					layer.msg("不能添加自己为下级,请重新选择子机构!", {icon: 15});
					return;
				}
			}
		}

		MyConfirm("确定保存?", function() {
			sendAjax('<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesRelation/saveSalerRelation.json',showResult,'fm');
		});
	} 
}
function showResult(json){
	if(json!=null){
		var errorinfo = json.error;
		if(errorinfo!=null && errorinfo!=''){
			MyAlert(errorinfo);
		}else{
			window.location.href='<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesRelation/partSalesRelationInit.do';
		}
	}
}
function clrTxt(value){
	$('#' + value)[0].value="";
	$("#dealerId")[0].value="";
	$("#dealerName")[0].value="";
}

function showPartDealer1(inputCode ,inputId ,isMulti ,orgId, isAllLevel, isAllArea,isDealerType,inputName, vId){
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	if(!isAllLevel){ isAllLevel = null;}
	if(!isAllArea){ isAllArea = null;}
	if(!isDealerType){ isDealerType = null;}
	if(!inputName){ inputName = null;}
	var url = g_webAppName+'/jsp/parts/baseManager/partsBaseManager/dealerShowForRelation.jsp';
	url += '?INPUTCODE='+inputCode;
	url += "&INPUTID="+inputId;
	url += "&ISMULTI="+isMulti;
	url += "&ORGID="+orgId;
	url += "&ISALLLEVEL="+isAllLevel;
	url += "&ISALLAREA="+isAllArea;
	url += "&isDealerType="+isDealerType;
	url += "&inputName="+inputName;
	url += "&vId="+vId;
	OpenHtmlWindow(url,730,580);
}
</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 配件采购关系维护 &gt; 维护下级单位
		</div>
		<form name="fm" id="fm" method="post">
			<input type="hidden" name="curPage" id="curPage" value="${curPage}" />
			<div class="form-panel">
				<h2>维护下级单位</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right f-bold" align="right" align="center">销售单位代码:</td>
							<td>${fatherCode}
								<input type="hidden" id="FATHER_CODE" name="FATHER_CODE" value="${fatherCode}" />
							</td>
							<td class="right f-bold" align="right">销售单位名称:</td>
							<td>${fatherName}
								<input type="hidden" id="FATHER_NAME" name="FATHER_NAME" value="${fatherName}" />
								<input type="hidden" id="FATHER_ID" name="FATHER_ID" value="${fatherId}" id="FATHER_ID" />
							</td>
						</tr>
						<tr>
							<td class="right">服务商代码:</td>
							<td>
								<input type="text" class="middle_txt" name="DEALER_CODE" value="" />
							</td>
							<td class="right">服务商名称:</td>
							<td>
								<input type="text" style="width: 250px;" class="middle_txt" name="DEALER_NAME" value="" />
							</td>
						</tr>
						<tr>
							<td class="formbtn-aln" colspan="4" align="center">
								<input type="button" name="queryBtn" id="queryBtn" value="查 询" onclick="__extQuery__(1);" class="u-button" />
								<input type="button" name="saveBtn" id="saveBtn" value="添加" onclick="showPartDealer1('DEALER_CODE','DEALER_ID','true','',true,true,false,'','${fatherId}')" class="u-button" />
								<input type="button" name="saveBtn" id="saveBtn" value="返 回" onclick="back();" class="u-button" />
							</td>
						</tr>
						<!-- 	<tr>
						<td colspan="4" align="center">
							<input class="middle_txt" type="text"  name="childOrgName" id=""childOrgName"" />
							<input type="button" name="saveBtn" id="saveBtn" value="查询" onclick="__extQuery(1)__"  class="u-button"/>
						</td>
					</tr> -->
					</table>
				</div>
			</div>

			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
		</form>
	</div>
</body>
</html>

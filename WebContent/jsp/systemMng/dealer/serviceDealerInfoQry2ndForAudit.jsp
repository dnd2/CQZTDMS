<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>售后经销商查询</title>
<script type="text/javascript">
var dealerLevel=<%=Constant.DEALER_LEVEL_01%>;
function doInit()
{
	__extQuery__(1);
}   

function downloadFunc() {
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/dealerInfoDownload.json" ;
	document.fm.action = url ;
	document.fm.submit() ;
}

window.onload = function(){
	MyAlert($("from[name='fm']").serialize());
}
</script>
</head>

<body onload="genLocSel('txt1','txt2','');">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;二级经销商审核</div>
<form method="post" id="fm" name="fm">
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>
<input id="user_id" name="user_id" type="hidden" value="${user_id}"/>
<input type="hidden" name="curPage" id="curPage" value="${curPage}" />
<input id="DEALER_TYPE" name="DEALER_TYPE" value="<%=Constant.DEALER_TYPE_DVS %>" type="hidden"/>
<table class="table_query" border="0">
	<tr>
		<td align=right>经销商代码：</td>
		<td align=left><input
			name="DEALER_CODE" maxlength="30" datatype="1,is_noquotation,30" id="DEALER_CODE" type="text" class="middle_txt" /></td>
		<td align=right>经销商简称：</td>
		<td align=left><input
			name="DEALER_NAME" maxlength="30" datatype="1,is_noquotation,75" id="DEALER_NAME" type="text" class="middle_txt" /></td>
	</tr>
	<tr>
		<td align=right>经销商状态：</td>
		<td align=left>
		<label>
				<script type="text/javascript">
					genSelBoxExp("DEALERSTATUS",<%=Constant.STATUS%>,"-1",true,"short_sel",'',"false",'');
				</script>
		</label>
		</td>
		<td align=right>授权类型：</td>
		<td align=left>
			<select name="AUTHORIZATION_TYPE"  id="AUTHORIZATION_TYPE">
			<option value="">-请选择-</option>
             		<option value="形象店">形象店</option>
             		<option value="特约站">特约站</option>
             		<option value="代理库">代理库</option>
             		<option value="专卖店">专卖店</option>
             	</select>
		</td>
	</tr>
	<tr>
		<td align=right>上级组织：</td>
		<td align=rleft>
		<input type="text"  name="parentOrgCode" size="15" value=""  id="parentOrgCode" class="middle_txt" datatype="1,is_noquotation,75" />
		<input name="orgbu"  id="orgbu" type="button" class="mark_btn" onclick="showOrg1('parentOrgCode','','false')" value="&hellip;" />
		<input class="cssbutton" type="button" value="清空" onclick="clrTxt('parentOrgCode');"/>
		</td>
		<td align=right>上级经销商：</td>
		<td align=left>
		<input type="text"  name="sJDealerCode" size="15" value=""  id="sJDealerCode" class="middle_txt" datatype="1,is_noquotation,75"/>
		<input name="dealerbu"  id="dealerbu" type="button" class="mark_btn" onclick="showOrgDealer('sJDealerCode','','false','','true')" value="&hellip;" />
		<input class="cssbutton" type="button" value="清空" onclick="clrTxt('sJDealerCode');"/>
		</td>
	</tr>
	<tr>
		<td align=right>信息状态：</td>
	    <td align=left>
		    <label>
				<script type="text/javascript">
					genSelBoxExp("SERVICE_STATUS",<%=Constant.DLR_SERVICE_STATUS_SECEND%>,"",'true',"short_sel",'',"false",'');
				</script>
		    </label>
	    </td>
		<td align=right>经销商公司：</td>
		<td align=left>
		<input class="middle_txt" id="COMPANY_NAME" style="cursor: pointer;" name="COMPANY_NAME" type="text" readonly="readonly"/>
		<input class="mark_btn" type="button" value="&hellip;" onclick="showCompany('<%=contextPath %>')"/>
		<input class="cssbutton" type="button" value="清空" onclick="clrTxt('COMPANY_NAME');clrTxt('COMPANY_ID');"/>
		</td>
	</tr>
	<tr>
   <td align=right>选择大区：</td>
		        <td align=left>
					<select id="orgId" name="orgId">
		        		<option value="">-- 请选择 --</option>
		        		<c:forEach items="${orglist}" var="list">
		        			<option value="${list.ORG_ID}">${list.ORG_NAME}</option>
		        		</c:forEach>
		        	</select>
	</td>	
		<td align=right>选择省份：</td>
      		<td align=left>
				<input type="text"  readonly="readonly"  name="regionCode" size="15" value=""  id="regionCode" class="middle_txt" />
				<input name="regbu"  id="regbu" type="button" class="mark_btn" onclick="showRegion1('regionCode','regionId','true')" value="..." />
				<input type="hidden"  name="regionId" size="15" value=""  id="regionId" />
				<input type="button"  class="cssbutton" value="清除" onclick="clrTxt('regionCode');clrTxt('regionId');"/>
			</td>


	</tr>
	<c:if test="${user_id==92871003||user_id==92871004}">
	<tr>
		<td align=right>销售形象是否合格：</td>
		<td align=left><select id="IS_QUALIFIED_SALES" name="IS_QUALIFIED_SALES">
				<option value="">-- 请选择 --</option>
				<option value="10041001">是</option>
				<option value="10041002">否</option>
		</select></td>
		<td align=right>售后形象是否合格：</td>
		<td align=left><select id="IS_QUALIFIED_SERVICE" name="IS_QUALIFIED_SERVICE">
				<option value="">-- 请选择 --</option>
				<option value="10041001">是</option>
				<option value="10041002">否</option>
		</select></td>
	</tr>
	</c:if>
			<tr align="center">
		<td colspan="4" class="table_query_4Col_input" style="text-align: center">
			<input  type=reset class="normal_btn"  value="重置" id="clearBtn" /> &nbsp; 
			<input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> &nbsp; 
			<input name="queryBtn" type="button" class="normal_btn" onclick="batchAccept()" value="批量通过" id="batchPass" /> &nbsp;
			<input name="queryBtn" type="button" class="normal_btn" onclick="batchRebut()" value="批量驳回" id="batchBack" /> &nbsp;
		</td>
	</tr>
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />	
</form>
<script>
var myPage;
var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/queryServiceDealerInfo2nd.json";
var title= null;
var columns = [
				{header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"codeIds\")'/>", width:'8%',sortable: false,dataIndex: 'DEALER_ID',renderer:myCheckBox},
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'DEALER_ID',renderer:myLink},
				{header: "备注", dataIndex: 'H_REMARK',renderer:getText},
				{header: "大区",width:'10%',   dataIndex: 'ROOT_ORG_NAME'},
				{header: "省份",width:'10%',   dataIndex: 'REGION_NAME'},
				{header: "经销商代码",width:'10%',   dataIndex: 'DEALER_CODE'},
				{header: "经销商名称",width:'10%', dataIndex: 'DEALER_NAME'},
				{header: "经销商简称", width:'20%', dataIndex: 'DEALER_SHORTNAME'},
				{header: "经销商等级", width:'10%', dataIndex: 'DEALER_LEVEL',renderer:getItemValue},
				{header: "信息状态", width:'10%', dataIndex: 'SERVICE_STATUS',renderer:getItemValue},
				{header: "上级经销商", width:'20%', dataIndex: 'PARENT_DEALER_NAME'},
				{header: "组织名称", width:'20%', dataIndex: 'ORG_NAME'},
				{header: "所属公司", width:'20%', dataIndex: 'COMPANY_NAME'},
				{header: "授权类型",width:'10%',   dataIndex: 'AUTHORIZATION_TYPE'}
// 				{header: "状态", width:'10%', dataIndex: 'STATUS',renderer:getItemValue}
			  ];
function myLink(dealer_id){
	var link = "<input type='button' class='normal_btn' onclick='searchDetail("+dealer_id+")' value='详细信息' />";
// 		link += "&nbsp;&nbsp;<input type='button' class='normal_btn' value='查看地址' onclick='dialogAddressLink("+ dealer_id +")' />";
		link += "&nbsp;&nbsp;<input type='button' class='normal_btn' onclick='addViConstruct("+dealer_id+")' value='通过' />";
		link += "&nbsp;&nbsp;<input type='button' class='normal_btn' value='驳回' onclick='bohui("+ dealer_id +")' />";
    return String.format(link);
} 
function myCheckBox(value,metaDate,record){
	return String.format("<input type='checkbox' name='codeIds' value='" + value +"' />");
}
function batchAccept(){		
	if (confirm("确认要批量通过?")){
		var delIds = new Array(); 
		var cnt = 0;
		var chk=document.getElementsByName("codeIds");
		var l = chk.length;
		for(var i=0;i<l;i++){
			if(chk[i].checked){
		       cnt++;
		       delIds.push(chk[i].value);
			}
		 }
		if(cnt==0){
		    MyAlert("请勾选要通过的数据！");
		    return ;
		}
		var url = "";
		var user_id = document.getElementById("user_id").value;
		if(user_id == 92871003){
			var IS_QUALIFIED_SALES = document.getElementById("IS_QUALIFIED_SALES").value;
			var IS_QUALIFIED_SERVICE = document.getElementById("IS_QUALIFIED_SERVICE").value;
			if(IS_QUALIFIED_SALES==""){
				MyAlert("请选择销售形象是否合格");
				return;
			}
			if(IS_QUALIFIED_SERVICE==""){
				MyAlert("请选择售后形象是否合格");
				return;
			}
			url = '<%=contextPath%>/sysmng/dealer/DealerInfo/batchAccept.do?DEALER_ID='+delIds+'&IS_QUALIFIED_SALES='+IS_QUALIFIED_SALES+'&IS_QUALIFIED_SERVICE='+IS_QUALIFIED_SERVICE;
		}else{
			url = '<%=contextPath%>/sysmng/dealer/DealerInfo/batchAccept.do?DEALER_ID='+delIds;
		}
	    fm.action = url;
	    fm.submit();
	}
}
function batchRebut(){		
	if (confirm("确认要驳回?")){
		var delIds = new Array(); 
		var cnt = 0;
		var chk=document.getElementsByName("codeIds");
		var l = chk.length;
		for(var i=0;i<l;i++){
			if(chk[i].checked){
		       cnt++;
		       delIds.push(chk[i].value);
			}
		 }
		if(cnt==0){
		    MyAlert("请勾选要驳回的数据！");
		    return ;
		}
		var url = '<%=contextPath%>/sysmng/dealer/DealerInfo/batchRebut.do?DEALER_ID='+delIds;
	    fm.action = url;
	    fm.submit();
	}
}
function bohui(dealer_id,curPage) {
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/BOHUIForDealerAudit.do?DEALER_ID="+dealer_id+"&curPage="+curPage;
	fm.action = url;
 	fm.submit();
}
function addViConstruct(obj){
	var user_id = document.getElementById("user_id").value;
	if(user_id == 92871003){
		var IS_QUALIFIED_SALES = document.getElementById("IS_QUALIFIED_SALES").value;
		var IS_QUALIFIED_SERVICE = document.getElementById("IS_QUALIFIED_SERVICE").value;
		if(IS_QUALIFIED_SALES==""){
			MyAlert("请选择销售形象是否合格");
			return;
		}
		if(IS_QUALIFIED_SERVICE==""){
			MyAlert("请选择售后形象是否合格");
			return;
		}
	}
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/SHENHETONGGUOForDealerAudit.do?DEALER_ID="+obj;
	fm.action = url;
 	fm.submit();
}
function dialogAddressLink(dealer_id) {
	return OpenHtmlWindow("<%=contextPath%>/jsp/systemMng/dealer/dealerAddressQueryForSH.jsp?DEALER_ID="+dealer_id,700,500);
}
function getText(value,meta,record){
	var DEALER_ID = record.data.DEALER_ID;
		return String.format("<input name='remark_"+DEALER_ID+"'  id='remark_"+DEALER_ID+"' type='text' value='"+value+"'  class='long_txt'>");
}
function searchDetail(obj){
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfoQuery/queryDealerInfoDetail2nd.do?DEALER_ID="+obj+"&DEALER_TYPE=<%=Constant.DEALER_TYPE_DVS%>";
	OpenHtmlWindow(url,800,600);
}

function formatDate(value,meta,record){
	if(value!=null && value!=""){
		return value.substring(0,7).replace("-","");
	}
	else{
		return "";
	}
}
function fwset(value){
	if(value!="" && value==<%=Constant.MSG_TYPE_2%>){//售后才放出这查询条件
		document.getElementById("sh_seach").style.display="";
		document.getElementById("sh_xxzt").style.display="none";
	}else{
		document.getElementById("sh_seach").style.display="none";
		document.getElementById("sh_xxzt").style.display="";
	}
}
function doCusChange(value)
{
}
function add()
{
	window.location.href='<%=contextPath%>/sysmng/dealer/DealerInfo/addNewDealer.do';
}

function clrTxt(txtId){
   	document.getElementById(txtId).value = "";
}

function downloadFunc() {
    var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/dealerInfoDownload.json" ;
    document.fm.action = url ;
    document.fm.submit() ;
}
function exportFunc() {
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfoQuery/dealerInfoDownloadNew.do?DEALER_TYPE=<%=Constant.DEALER_TYPE_DVS%>" ;
	document.fm.action = url ;
	document.fm.submit() ;
}
function showRegion1(inputCode ,inputId,isMulti )
{   
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showRegion.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti,730,390);
}
function showOrg1(inputCode ,inputId ,isMulti ,orgId)
{
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showOrg.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId,730,390);
}
</script>
</body>
</html>

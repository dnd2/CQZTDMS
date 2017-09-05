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
<title>形象店支持查询</title>
<script type="text/javascript">
var dealerLevel=<%=Constant.DEALER_LEVEL_01%>;
function doInit()
{
	__extQuery__(1);
}   
</script>
</head>

<body onload="genLocSel('txt1','txt2','');">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 财务管理 &gt; 财务管理 &gt;形象店支持查询</div>
<form method="post" id="fm" name="fm">
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>
<input id="user_id" name="user_id" type="hidden" value="${user_id}"/>
<input id="DEALER_TYPE" name="DEALER_TYPE" value="<%=Constant.DEALER_TYPE_DVS %>" type="hidden"/>
<input id="maxAuditYear" name="maxAuditYear" value="${maxAuditYear}" type="hidden"/>
<table class="table_query" border="0">
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商代码：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="DEALER_CODE" maxlength="30" datatype="1,is_noquotation,30" id="DEALER_CODE" type="text" class="middle_txt" /></td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商简称：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="DEALER_NAME" maxlength="30" datatype="1,is_noquotation,75" id="DEALER_NAME" type="text" class="middle_txt" /></td>
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
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">选择省份：</td>
      		<td class="table_query_4Col_input" nowrap="nowrap">
				<input type="text"  readonly="readonly"  name="regionCode" size="15" value=""  id="regionCode" class="middle_txt" />
				<input name="regbu"  id="regbu" type="button" class="mark_btn" onclick="showRegion('regionCode','regionId','true')" value="..." />
				<input type="hidden"  name="regionId" size="15" value=""  id="regionId" />
				<input type="button"  class="cssbutton" value="清除" onclick="clrTxt('regionCode');clrTxt('regionId');"/>
			</td>
	</tr>
	<tr>
		<td align="right">选择年份：</td>
		<td align="left" colspan="3">
			<script type="text/javascript">
				var maxAuditYear = document.getElementById("maxAuditYear").value;
				var selectText = "第<select name='auditYear'>";
				selectText += "<option value=''>-- 请选择 --</option>";
				for (var i = 1; i <= maxAuditYear; i++) {
					selectText += "<option value='"+i+"'>"+i+"</option>";
				}
				selectText += "</select>年待审核";
				document.write(selectText);
			</script>
		</td>
	</tr>
<!-- 	<tr> -->
<!-- 			<td align="right" bgcolor="#F3F4F8" class="table_query_tb">状态：</td> -->
<!-- 			<td align="left" bgcolor="#FFFFFF"> -->
<!-- 				<script type="text/javascript"> -->
<%-- 					genSelBox("status",<%=Constant.VI_CONSTRUCT_STATUS %>,"-1",true,"short_sel",'',"false",''); --%>
<!-- 				</script> -->
<!-- 			</td> -->
<!-- 	</tr> -->
	<tr align="center">
		<td colspan="4" class="table_query_4Col_input" style="text-align: center">
			<input  type=reset class="normal_btn"  value="重置" id="clearBtn" /> &nbsp; 
			<input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> &nbsp; 
			<input name="doBatchAuditPassBtn" type="button" class="long_btn" onclick="doBatchAudit('pass')" value="批量审核通过" id="doBatchAuditPassBtn" /> &nbsp; 
			<input name="doBatchAuditBackBtn" type="button" class="long_btn" onclick="doBatchAudit('nopass')" value="批量审核驳回" id="doBatchAuditBackBtn" /> &nbsp; 
		</td>
	</tr>
</table>
</form>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />	
<script>
var myPage;
var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/queryViConstructAudit.json";
var title= null;
var columns = [
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'DEALER_ID',renderer:myLink},
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"groupIds\")'></input>",sortable: false,dataIndex: 'DEALER_ID',renderer:myCheckBox},
				{header: "大区",width:'10%',   dataIndex: 'ROOT_ORG_NAME'},
				{header: "省份",width:'10%',   dataIndex: 'REGION_NAME'},
				{header: "经销商代码",width:'10%',   dataIndex: 'DEALER_CODE'},
				{header: "经销商名称",width:'10%', dataIndex: 'DEALER_NAME'},
				{header: "经销商简称", width:'20%', dataIndex: 'DEALER_SHORTNAME'},
				{header: "SUV审核状态",width:'20%', dataIndex: 'IS_HAVE_AUDIT_SUV'},
				{header: "MPV审核状态",width:'20%', dataIndex: 'IS_HAVE_AUDIT_MPV'}
// 				{header: "备注", width:'10%', dataIndex: 'REMARK'},
// 				{header: "状态", width:'10%', dataIndex: 'H_STATE'}
			  ];
function myLink(dealer_id,meta,record){
	var link;
// 	var state = record.data.H_STATE;
// 	if(state=='渠道部审核中'||state=='销售部审核中'||state=='总经理审核中'||state=='已上报'){
// 		link = "<input type='button' class='normal_btn' onclick='addViConstruct("+dealer_id+")' value='维护' />";
// 	}else if(state=='已保存'||state=='驳回'){
// 		link = "<input type='button' class='normal_btn' onclick='addViConstruct("+dealer_id+")' value='维护' />";
// 	}else if(state=='终止'||state=='总经理审核通过'){
		link = "<input type='button' class='normal_btn' onclick='addViConstruct("+dealer_id+")' value='查看' />";
// 	}else{
// 		link = "<input type='button' class='normal_btn' onclick='addViConstruct("+dealer_id+")' value='新增' />";
// 	}
	    
    return String.format(link);
} 
// 批量处理
function myCheckBox(value,metaDate,record){
	var IS_HAVE_AUDIT_SUV = record.data.IS_HAVE_AUDIT_SUV;
	var IS_HAVE_AUDIT_MPV = record.data.IS_HAVE_AUDIT_MPV;
	var str = "";
	if (!IS_HAVE_AUDIT_SUV && !IS_HAVE_AUDIT_MPV) {
		str = "<input type='checkbox' disabled='disabled' />";
	} else {
		str = "<input type='checkbox' id='groupIds' name='groupIds' value='" + value + "' />";
		if (IS_HAVE_AUDIT_SUV) {
			str += "<input type='hidden' id='YEAR_FLAG_SUV' name='YEAR_FLAG_SUV' value='" + IS_HAVE_AUDIT_SUV.substring(1,2) + "' />";
		} else {
			str += "<input type='hidden' id='YEAR_FLAG_SUV' name='YEAR_FLAG_SUV' value='0' />";
		}
		if (IS_HAVE_AUDIT_MPV) {
			str += "<input type='hidden' id='YEAR_FLAG_MPV' name='YEAR_FLAG_MPV' value='" + IS_HAVE_AUDIT_MPV.substring(1,2) + "' />";
		} else {
			str += "<input type='hidden' id='YEAR_FLAG_MPV' name='YEAR_FLAG_MPV' value='0' />";
		}
	}
	return String.format(str);
}
function dialogAddressLink(dealer_id) {
	return OpenHtmlWindow("<%=contextPath%>/jsp/systemMng/dealer/dealerAddressQuery.jsp?DEALER_ID="+dealer_id,700,500);
}

function addViConstruct(obj){
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/queryViConstructDealer.do?DEALER_ID="+obj+"&DEALER_TYPE=<%=Constant.DEALER_TYPE_DVS%>";
	fm.action = url;
 	fm.submit();
}

function formatDate(value,meta,record){
	if(value!=null && value!=""){
		return value.substring(0,7).replace("-","");
	}
	else{
		return "";
	}
	
}
function add()
{
	window.location.href='<%=contextPath%>/sysmng/dealer/DealerInfo/addNewDealer.do';
}

function clrTxt(txtId){
   	document.getElementById(txtId).value = "";
}
// 选择商务政策
function selectOther(id, otherid) {
	OpenHtmlWindow(g_webAppName + '/sales/financemanage/SalesPolicyManager/queryDeployedPolicyInit.do?id='+id+'&otherid='+otherid,700,500);
}
function doBatchAudit(auditResultFlag) {
	MyConfirm("确认要进行批量审核?",auditProcess,[auditResultFlag],function(){return;},null);
}
function auditProcess(auditResultFlag) {
	var arrayObj = new Array(); 
	arrayObj=document.getElementsByName("groupIds");
	var arrayObj1 = new Array(); 
	arrayObj1=document.getElementsByName("YEAR_FLAG_SUV");
	var arrayObj2 = new Array(); 
	arrayObj2=document.getElementsByName("YEAR_FLAG_MPV");

	// 批量交接的行变量
	var flg = true;
	var yearFlagSuvs = "";
	var yearFlagMpvs = "";
	var dealerIds = "";
	for (var i = 0; i < arrayObj.length; i++) {
		if(arrayObj[i].checked){
			flg = false;
			dealerIds = dealerIds + arrayObj[i].value + ",";
			yearFlagSuvs = yearFlagSuvs + arrayObj1[i].value + ",";
			yearFlagMpvs = yearFlagMpvs + arrayObj2[i].value + ",";
		}
	}
	// 未选择经销商进行批量放证
	if (flg) {
		MyAlert("您还未选择需要审核的经销商!");
		return;
	}

	document.getElementById("doBatchAuditPassBtn").disabled = "disabled";
	document.getElementById("doBatchAuditBackBtn").disabled = "disabled";
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/doBatchAudit.do?yearFlagSuvs="+yearFlagSuvs+"&yearFlagMpvs="+yearFlagMpvs+"&auditResultFlag="+auditResultFlag+"&dealerIds="+dealerIds;
	fm.action = url;
	fm.submit();	
}
</script>
</body>
</html>

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
<title>经销商维护</title>
<script type="text/javascript">
var dealerLevel=<%=Constant.DEALER_LEVEL_01%>;
function doInit()
{
	doQuery();
}   

function downloadFunc() {
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/dealerInfoDownload.json" ;
	document.fm.action = url ;
	document.fm.submit() ;
}
function exportFunc() {
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/dealerInfoDownload2nd.json" ;
	document.fm.action = url ;
	document.fm.submit() ;
}
function dialogAddressUpdateLink(dealer_id) {
	return OpenHtmlWindow("<%=contextPath%>/jsp/systemMng/dealer/dealerAddressVindicate.jsp?DEALER_ID="+dealer_id,700,500);
}
</script>
</head>

<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;二级销售经销商维护</div>
<form method="post" id="fm" name="fm">
<input id="user_id" name="user_id" type="hidden" value="${user_id}"/>
<input type="hidden" name="curPage" id="curPage" value="${curPage}" />
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>
<table class="table_query" border="0">
	<c:if test="${isDealer!='no'}"><tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商代码：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="DEALER_CODE" maxlength="30" datatype="1,is_noquotation,30" id="DEALER_CODE" type="text" class="middle_txt" /></td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商名称：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="DEALER_NAME" maxlength="30" datatype="1,is_noquotation,75" id="DEALER_NAME" type="text" class="middle_txt" /></td>
	</tr></c:if>
	<tr style="display:none;">
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商类型：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"> 
			<select id="DEALER_TYPE" name="DEALER_TYPE">
				<option value="<%=Constant.DEALER_TYPE_DVS %>">整车销售</option>
			</select>
		</td>
	</tr>

		<tr>
               	<td align="right">经营类型：</td>
               	<td align="left">
               		<select id="WORK_TYPE" name="WORK_TYPE">
               		<option value="">-请选择-</option>
               			<option value="代理">代理</option>
               			<option value="直营">直营</option>
               			<option value="托管">托管</option>
               		</select>
               	</td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商状态：</td>
			    <td align="left">
					<script type="text/javascript">
						genSelBoxExp("SERVICE_STATUS",<%=Constant.DLR_SERVICE_STATUS_SECEND%>,"${dMap.SERVICE_STATUS}",'true',"short_sel",'',"false",'');
					</script>
			    </td>
			    

	</tr>
	<tr>
			<c:if test="${isDealer!='no'}"><tr>
   <td align=right>选择大区：</td>
		        <td align=left>
					<select id="orgId" name="orgId">
		        		<option value="">-- 请选择 --</option>
		        		<c:forEach items="${orglist }" var="list">
		        			<option value="${list.ORG_ID }">${list.ORG_NAME }</option>
		        		</c:forEach>
		        	</select>
	</td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">选择省份：</td>
      		<td class="table_query_4Col_input" nowrap="nowrap">
				<input type="text"  readonly="readonly"  name="regionCode" size="15" value=""  id="regionCode" class="middle_txt" />
				<input name="regbu"  id="regbu" type="button" class="mark_btn" onclick="showRegion('regionCode','regionId','true','10771002')" value="..." />
				<input type="hidden"  name="regionId" size="15" value=""  id="regionId" />
				<input type="button"  class="cssbutton" value="清除" onclick="clrTxt('regionCode');clrTxt('regionId');"/>
			</td>


	</tr>
	</c:if><tr>
		<tr>
            <td align="right" bgcolor="#F3F4F8" class="table_query_tb">审核状态：</td>
            <c:if test="${isDealer!='no'}">
			<td align="left" bgcolor="#FFFFFF">
				<script type="text/javascript">
				genSelBoxExp("status",<%=Constant.DEALER_SECEND_STATUS %>,"-1",true,"short_sel",'',"false",'92901001');
				</script>
			</td></c:if>
			<c:if test="${isDealer=='no'}"><td align="left" bgcolor="#FFFFFF">
				<script type="text/javascript">
				genSelBoxExp("status",<%=Constant.DEALER_SECEND_STATUS %>,"-1",true,"short_sel",'',"false",'');
				</script>
			</td></c:if>
	</tr>
	<tr align="center">
		<td colspan="4" class="table_query_4Col_input" style="text-align: center">
			<input name="button2" type="button" class="normal_btn" onclick="importDealer()" value="导入" /> &nbsp; 
			<input name="queryBtn" type="button" class="normal_btn" onclick="doQuery()" value="查 询" id="queryBtn" /> &nbsp; 
<!-- 			<input type="button" class="normal_btn" id="downloadIt" name="downloadIt" onclick="exportFunc() ;" value="下 载" />&nbsp; -->
			<c:if test="${isDealer!='yes'}">
			<input name="button2" type="button" class="normal_btn" onclick="add()" value="添 加" /> &nbsp;</c:if>
<!-- 			<input name="button2" type="button" class="long_btn" onclick="importDealerAddress()" value="地址批量导入" /> &nbsp;  -->
		</td>
	</tr>
</table>
</form>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />	
<script>
var myPage;
var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/query2ndDealerInfo.json";
var title= null;
var columns = [
				{id:'action',header: "查看", walign:'center',idth:70,sortable: false,dataIndex: 'DEALER_ID',renderer:myLink1},
				{header: "审核状态",width:'10%',   dataIndex: 'H_STATE'},
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'DEALER_ID',renderer:myLink},
				{header: "备注",width:'10%',   dataIndex: 'H_REMARK'},
               	{header: "大区",width:'10%',   dataIndex: 'ROOT_ORG_NAME'},
				{header: "省份",width:'10%',   dataIndex: 'REGION_NAME'},
				{header: "经销商代码",width:'10%',   dataIndex: 'DEALER_CODE'},
				{header: "经销商名称", width:'20%', dataIndex: 'DEALER_NAME'},
				{header: "经销商等级", width:'10%', dataIndex: 'DEALER_LEVEL',renderer:getItemValue},
				{header: "上级经销商名称", width:'20%', dataIndex: 'PARENT_DEALER_NAME'},
				{header: "组织名称", width:'20%', dataIndex: 'ORG_NAME'},
				{header: "所属公司", width:'20%', dataIndex: 'COMPANY_NAME'},
				{header: "经销商状态", width:'10%', dataIndex: 'SERVICE_STATUS',renderer:getItemValue},
				{header: "经营类型", width:'10%', dataIndex: 'SHOP_TYPE'}
			  ];
function myLink(value,meta,record){
	var link;
	if(record.data.H_STATE=='驳回'){
		if(record.data.IS_DEALER=='no'){
			 link = ""; 
		}else{
			 link = "<a href=\"<%=contextPath%>/sysmng/dealer/DealerInfo/queryDealerInfoDetail2nd.do?DEALER_ID="+value+"\">[修改]</a>"; 
		}
		
	}else if(record.data.H_STATE=='保存'){
		if(record.data.IS_DEALER=='no'){
			 link = ""; 
		}else{
			 link = "<a href=\"<%=contextPath%>/sysmng/dealer/DealerInfo/queryDealerInfoDetail2nd.do?DEALER_ID="+value+"\">[修改]</a>"; 
		} 
	}else if(record.data.H_STATE=='审核通过'){   
		link = "&nbsp;&nbsp;<a href=\"javascript:;\" onclick=\"dialogAddressUpdateLink("+ value +")\">[地址维护]</a>";
	}else{
		link ="";
	}
    return String.format(link);
} 
function searchDetail(obj){
	var curPage = 1;
	if(document.getElementById("pageInput")!=null||document.getElementById("pageInput")!=undefined){
		curPage = document.getElementById("pageInput").value;
	}
	
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfoQuery/queryDealerInfoDetail2nd.do?curPage="+curPage+"&DEALER_ID="+obj+"&DEALER_TYPE=<%=Constant.DEALER_TYPE_DVS%>";
	OpenHtmlWindow(url,800,600);
}
function myLink1(dealer_id){
	var link = "<input type='button' class='normal_btn' onclick='searchDetail("+dealer_id+")' value='详细信息' />";
// 		link += "&nbsp;&nbsp;<input type='button' class='normal_btn' value='查看地址' onclick='dialogAddressLink("+ dealer_id +")' />";
    return String.format(link);
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
function add()
{
	window.location.href='<%=contextPath%>/sysmng/dealer/DealerInfo/addNewCsDealer2nd.do';
}

function clrTxt(txtId){
   	document.getElementById(txtId).value = "";
}
function importDealer(){
	window.location.href='<%=contextPath%>/sysmng/dealer/ShDealerImport/sh2ndImportInit.do';	
}
function importDealerAddress(){
	window.location.href='<%=contextPath%>/sysmng/dealer/XsDealerImport/xsImportAddressInit.do';	
}
function showRegion1(inputCode ,inputId,isMulti )
{   
//    toClearDealers();
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showRegion.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti,730,390);
}

function doQuery() {
    var msg = "";
    if (msg != "") {
        //弹出提示
        MyAlert(msg);
        return;
    }
    //执行查询
    var curPage = document.getElementById("curPage").value;
    __extQuery__(curPage);
}
function bohui(dealer_id) {
	var curPage = 1;
	if(document.getElementById("pageInput")!=null||document.getElementById("pageInput")!=undefined){
		curPage = document.getElementById("pageInput").value;
	}
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/BOHUIForDealerDetail.do?curPage="+curPage+"&DEALER_ID="+dealer_id;
 	fm.action = url;
 	fm.submit();
}
</script>
</body>
</html>

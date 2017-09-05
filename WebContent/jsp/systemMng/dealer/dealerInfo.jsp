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
	//loadcalendar();
	genLocSel('txt1','','','','',''); // 加载省份城市和县
}   

function downloadFunc() {
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/dealerInfoDownload.json" ;
	document.fm.action = url ;
	document.fm.submit() ;
}
function exportFunc() {
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/dealerInfoDownloadNew.json" ;
	document.fm.action = url ;
	document.fm.submit() ;
}

function dialogAddressUpdateLink(dealer_id) {
	return OpenHtmlWindow("<%=contextPath%>/jsp/systemMng/dealer/dealerAddressVindicate.jsp?DEALER_ID="+dealer_id,700,500);
}
</script>
</head>

<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;销售经销商维护</div>
	<form method="post" id="fm" name="fm">
	<input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>
	<div class="form-panel">
		<h2>售经销商维护</h2>
		<div class="form-body">
			<table class="table_query" border="0">
				<tr>
					<td class="table_query_3Col_label_6Letter right" nowrap="nowrap">经销商代码：</td>
					<td class="table_query_4Col_input" nowrap="nowrap"><input
						name="DEALER_CODE" maxlength="30" datatype="1,is_noquotation,30" id="DEALER_CODE" type="text" class="middle_txt" /></td>
					<td class="table_query_3Col_label_6Letter right" nowrap="nowrap">经销商名称：</td>
					<td class="table_query_4Col_input" nowrap="nowrap"><input
						name="DEALER_NAME" maxlength="30" datatype="1,is_noquotation,75" id="DEALER_NAME" type="text" class="middle_txt" /></td>
				</tr>
				<tr style="display:none;">
					<td class="table_query_3Col_label_6Letter right" nowrap="nowrap">经销商类型：</td>
					<td class="table_query_4Col_input" nowrap="nowrap"> 
						<select id="DEALER_TYPE" class="u-select" name="DEALER_TYPE">
							<option value="<%=Constant.DEALER_TYPE_DVS %>">经销商整车销售</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="table_query_3Col_label_6Letter right" nowrap="nowrap">验收形象等级：</td>
					<td class="table_query_4Col_input" nowrap="nowrap">
						<script type="text/javascript">
									genSelBoxExp("IMAGE_COMFIRM_LEVEL",<%=Constant.IMAGE_LEVEL%>,"",'true',"short_sel u-select",'',"false",'');
						</script> 
					</td>
					<td class="table_query_3Col_label_6Letter right" nowrap="nowrap">授权类型：</td>
					<td class="table_query_4Col_input" nowrap="nowrap">
						<select name="AUTHORIZATION_TYPE"  id="AUTHORIZATION_TYPE" class="u-select">
								<option value="">-请选择-</option>
								<option value="形象店">形象店</option>
								<option value="特约站">特约站</option>
								<option value="代理库">代理库</option>
								<option value="专卖店">专卖店</option>
							</select>
					</td>
				</tr>
					<tr>
							<td class="right" align="right">经营类型：</td>
							<td align="left">
								<select id="WORK_TYPE" name="WORK_TYPE" class="u-select">
								<option value="">-请选择-</option>
									<option value="代理">代理</option>
									<option value="直营">重点客户</option>
								</select>
							</td>
					<td class="table_query_3Col_label_6Letter right" nowrap="nowrap">经销商状态：</td>
							<td align="left">
								<script type="text/javascript">
									genSelBoxExp("SERVICE_STATUS",<%=Constant.DLR_SERVICE_STATUS%>,"${dMap.SERVICE_STATUS}",'true',"short_sel u-select",'',"false",'');
								</script>
								<font color="red">*</font>
							</td>
							

				</tr>
						<tr>
			<td class="right" align=right>选择大区：</td>
							<td align=left>
								<select id="orgId" name="orgId" class="u-select">
									<option value="">-- 请选择 --</option>
									<c:forEach items="${orglist}" var="list">
										<option value="${list.ORG_ID}">${list.ORG_NAME}</option>
									</c:forEach>
								</select>
				</td>	
					<td class="table_query_3Col_label_6Letter right" nowrap="nowrap">选择省份：</td>
						<td class="table_query_4Col_input" nowrap="nowrap">
							<input type="text"  readonly="readonly"  name="regionCode" size="15" value=""  id="regionCode" class="middle_txt" />
							<input name="regbu"  id="regbu" type="button" class="mark_btn" onclick="showRegion('regionCode','regionId','true','10771001')" value="..." />
							<input type="hidden"  name="regionId" size="15" value=""  id="regionId" />
							<input type="button"  class="cssbutton u-button" value="清除" onclick="clrTxt('regionCode');clrTxt('regionId');"/>
						</td>


				</tr>
					<tr>
							<td class="right" width="10%" align="right">授权日期：</td>
							<td width="22%" align="left">
							<input class="time_txt" id="AUTHORIZATION_SCREATE_DATE" name="AUTHORIZATION_SCREATE_DATE" datatype="1,is_date,10" maxlength="10" value="" style="width:65px" group="AUTHORIZATION_SCREATE_DATE,AUTHORIZATION_ECREATE_DATE"/>
								<input class="time_ico" value=" " type="button"/>
								至
								<input class="time_txt" id="AUTHORIZATION_ECREATE_DATE" name="AUTHORIZATION_ECREATE_DATE" datatype="1,is_date,10" value="" style="width:65px" maxlength="10" group="AUTHORIZATION_ECREATE_DATE,AUTHORIZATION_ECREATE_DATE"/>
								<input class="time_ico" value=" " type="button"/>
							</td>
				</tr>
				<tr style="display:none;">
					<td class="table_query_3Col_label_6Letter right" nowrap="nowrap">经销商类型：</td>
					<td class="table_query_4Col_input" nowrap="nowrap"> 
						<select id="DEALER_TYPE" name="DEALER_TYPE" class="u-select">
							<option value="<%=Constant.DEALER_TYPE_DVS %>">整车销售</option>
						</select>
					</td>
				</tr>
				<tr align="center">
					<td colspan="4" class="table_query_4Col_input" style="text-align: center">
						<input name="button2" type="button" class="u-button" onclick="importDealer()" value="导入" /> &nbsp; 
						<input name="queryBtn" type="button" class="u-button u-query" onclick="doQuery()" value="查 询" id="queryBtn" /> &nbsp; 
						<input type="button" class="u-button" id="downloadIt" name="downloadIt" onclick="exportFunc() ;" value="下 载" />&nbsp;
						<input name="button2" type="button" class="u-button u-submit" onclick="add()" value="添 加" /> &nbsp;
						<input name="button2" type="button" class="long_btn u-button" onclick="importDealerAddress()" value="地址批量导入" /> &nbsp;
					</td>
				</tr>
			</table>
			</form>
		</div>
	</div>
	
</div>	
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />	
<script>
var myPage;
var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/queryDealerInfo.json";
var title= null;
var columns = [{header: "大区",width:'10%',   dataIndex: 'ROOT_ORG_NAME'},
				{header: "省份",width:'10%',   dataIndex: 'REGION_NAME'},
				{header: "经销商代码",width:'10%',   dataIndex: 'DEALER_CODE'},
				{header: "经销商名称", width:'20%', dataIndex: 'DEALER_NAME'},
				{header: "经销商等级", width:'10%', dataIndex: 'DEALER_LEVEL',renderer:getItemValue},
				//{header: "经销商类型", width:'10%', dataIndex: 'DEALER_TYPE',renderer:getItemValue},
				{header: "上级经销商名称", width:'20%', dataIndex: 'PARENT_DEALER_NAME'},
				{header: "组织名称", width:'20%', dataIndex: 'ORG_NAME'},
				{header: "所属公司", width:'20%', dataIndex: 'COMPANY_NAME'},
				{header: "经销商状态", width:'10%', dataIndex: 'SERVICE_STATUS',renderer:getItemValue},
				{header: "授权类型", width:'10%', dataIndex: 'AUTHORIZATION_TYPE'},
				{header: "授权时间 ", width:'10%', dataIndex: 'AUTHORIZATION_DATE'},
				{header: "经营类型", width:'10%', dataIndex: 'SHOP_TYPE'},
				{header: "验收形象等级", width:'10%', dataIndex: 'IMAGE_COMFIRM_LEVEL',renderer:getItemValue},
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'DEALER_ID',renderer:myLink}
			  ];
function myLink(dealer_id){
	var link = "<a href=\"<%=contextPath%>/sysmng/dealer/DealerInfo/queryDealerInfoDetail.do?DEALER_ID="+dealer_id+"\">[修改]</a>"; 
	link += "&nbsp;&nbsp;<a href=\"javascript:;\" onclick=\"dialogAddressUpdateLink("+ dealer_id +")\">[地址维护]</a>";
    return String.format(link);
} 
function importDealerAddress(){
	window.location.href='<%=contextPath%>/sysmng/dealer/XsDealerImport/xsImportAddressInit.do';	
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
	/*if(value == ""){
		document.getElementById("sJDealerCode").disabled="";
		document.getElementById("dealerbu").disabled="";
		document.getElementById("orgCode").disabled="";
		document.getElementById("orgbu").disabled="";
	}
	else{
		if(dealerLevel==value)
		{
			document.getElementById("sJDealerCode").disabled="true";
			document.getElementById("dealerbu").disabled="true";
			document.getElementById("orgCode").disabled="";
			document.getElementById("orgbu").disabled="";
			
		}else
		{
			document.getElementById("sJDealerCode").disabled="";
			document.getElementById("dealerbu").disabled="";
			document.getElementById("orgCode").disabled="true";
			document.getElementById("orgbu").disabled="true";		
		}
	}*/
}
function add()
{
	window.location.href='<%=contextPath%>/sysmng/dealer/DealerInfo/addNewDealer.do';
}

function clrTxt(txtId){
   	document.getElementById(txtId).value = "";
}
function importDealer(){
	window.location.href='<%=contextPath%>/sysmng/dealer/XsDealerImport/xsImportInit.do';	
}
function showRegion1(inputCode ,inputId,isMulti )
{   
//    toClearDealers();
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	OpenHtmlWindow(g_webAppName+'/dialog/showRegion.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti,730,390);
}

//查询
function doQuery() {
    var msg = "";
    //校验时间范围
    if (document.getElementById("AUTHORIZATION_SCREATE_DATE").value != "") {
        if (document.getElementById("AUTHORIZATION_ECREATE_DATE").value == "") {
            msg += "请填写授权结束日期!</br>"
        }
    }
    if (document.getElementById("AUTHORIZATION_ECREATE_DATE").value != "") {
        if (document.getElementById("AUTHORIZATION_SCREATE_DATE").value == "") {
            msg += "请填写授权开始日期!</br>"
        }
    }
    if (msg != "") {
        //弹出提示
        MyAlert(msg);
        return;
    }
    //执行查询
    __extQuery__(1);
}
</script>
</body>
</html>

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

	/*var dl=document.getElementById("DEALERLEVEL").value;
	if(dl != ""){
		if(dealerLevel==dl)
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
	genLocSel('txt1','','','','',''); // 加载省份城市和县
}   

function downloadFunc() {
	var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/dealerInfoDownload.json" ;
	document.getElementById("fm").action = url ;
	document.getElementById("fm").submit() ;
}
</script>
</head>

<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;经销商维护</div>
	<form id="fm" name="fm">
		<div class="form-panel">
			<h2>经销商维护</h2>
				<div class="form-body">
	<input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>
	<table class="table_query" border="0">
		<tr>
			<td class="right" nowrap="nowrap">经销商代码：</td>
			<td  nowrap="nowrap"><input
				name="DEALER_CODE" maxlength="30" datatype="1,is_noquotation,30" id="DEALER_CODE" type="text" class="middle_txt" /></td>
			<td class="right" nowrap="nowrap">经销商简称：</td>
			<td nowrap="nowrap"><input
				name="DEALER_NAME" maxlength="30" datatype="1,is_noquotation,75" id="DEALER_NAME" type="text" class="middle_txt" /></td>
		</tr>
		<tr>
			<td class="right" nowrap="nowrap">经销商等级：</td>
			<td nowrap="nowrap"> 
			<label>
					<script type="text/javascript">
						genSelBoxExp("DEALERLEVEL",<%=Constant.DEALER_LEVEL%>,"-1",'true',"",'',"false",'');
					</script>
			</label>
			</td>
			<td class="right" nowrap="nowrap">经销商状态：</td>
			<td nowrap="nowrap">
			<label>
					<script type="text/javascript">
						genSelBoxExp("DEALERSTATUS",<%=Constant.STATUS%>,"-1",true,"",'',"false",'');
					</script>
			</label>
			</td>
		</tr>
		<tr>
			<td class="right" nowrap="nowrap">上级组织：</td>
			<td nowrap="nowrap">
			<input type="text"  name="orgCode" size="15" value=""  id="orgCode" class="middle_txt" readonly="readonly" datatype="1,is_noquotation,75" onclick="showOrg('orgCode','','false')"/>
			<input class="cssbutton" type="button" value="清空" onclick="clrTxt('orgCode');"/>
			</td>
			<td class="right" nowrap="nowrap">上级经销商：</td>
			<td nowrap="nowrap">
			<input type="text"  name="sJDealerCode" size="15" value=""  id="sJDealerCode" readonly="readonly" style="cursor: pointer;" onclick="showOrgDealer('sJDealerCode','','false','','true')" class="middle_txt" datatype="1,is_noquotation,75"/>
			<input class="cssbutton" type="button" value="清空" onclick="clrTxt('sJDealerCode');"/>
			</td>
		</tr>
		<tr>
			<td class="right" nowrap="nowrap">经销商类型：</td>
			<td nowrap="nowrap"> 
			<label>
					<script type="text/javascript">
						genSelBoxExp("DEALERTYPE",<%=Constant.DEALER_TYPE%>,"-1",true,"",'',"false",'');
					</script>
			</label>
			</td>
			<td class="right" nowrap="nowrap">经销商公司：</td>
			<td nowrap="nowrap">
			<input class="middle_txt" id="COMPANY_NAME" style="cursor: pointer;" name="COMPANY_NAME" type="text" readonly="readonly" onclick="showCompany('<%=contextPath %>')"/>
			<input class="cssbutton" type="button" value="清空" onclick="clrTxt('COMPANY_NAME');clrTxt('COMPANY_ID');"/>
			</td>
		</tr>
		<tr>
			<td class="right" nowrap="nowrap">省份：</td>
			<td nowrap="nowrap"> 
			<!-- <label>
					<script type="text/javascript">
						genSelBoxExp("DEALERCLASS",<%=Constant.DEALER_CLASS_TYPE%>,"-1",true,"",'',"false",'');
					</script>
			</label> -->
				<select class="u-select" id="txt1" name="province"></select>
			
			</td>
			<td class="right" nowrap="nowrap"></td>
			<td nowrap="nowrap">
			</td>
		</tr>
		<!-- <tr>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">选择业务范围：</td>
			<td class="table_query_4Col_input" nowrap="nowrap"> 
			    <select name="areaId" class="short_sel" id="areaId">
			    	<option value="">-请选择-</option>
					<c:if test="${areaList!=null}">
						<c:forEach items="${areaList}" var="list">
							<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
						</c:forEach>
					</c:if>
				</select>
			</td>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap"></td>
			<td class="table_query_4Col_input" nowrap="nowrap"></td>
		</tr> -->
		<tr align="center">
			<td colspan="4" class="table_query_4Col_input" style="text-align: center">
				<input name="queryBtn" type="button" class="u-button u-query" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> &nbsp; 
				<input type="button" class="normal_btn" id="downloadIt" name="downloadIt" onclick="downloadFunc() ;" value="下 载" />&nbsp;
				<input name="button2" type="button" class="u-button u-submit" onclick="add()" value="新增" /> &nbsp;
			</td>
		</tr>
	</table>
	</div>
	</div>
	</form>
</div>

<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />	
<script>
var myPage;
var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/querySalesDealerInfo.json";
var title= null;
var dl=document.getElementById("DEALERLEVEL").value;
var columns = [
				{header: "经销商代码",width:'10%',   dataIndex: 'DEALER_CODE'},
				{header: "经销商简称", width:'20%', dataIndex: 'DEALER_SHORTNAME'},
				{header: "经销商等级", width:'10%', dataIndex: 'DEALER_LEVEL',renderer:getItemValue},
				{header: "经销商类型", width:'10%', dataIndex: 'DEALER_TYPE',renderer:getItemValue},
				{header: "上级经销商名称", width:'20%', dataIndex: 'SHANGJINAME'},
				{header: "上级组织", width:'20%', dataIndex: 'SHANGJIORGNAME'},
				{header: "经销商公司", width:'20%', dataIndex: 'COMPANY_SHORTNAME'},
				{header: "状态", width:'10%', dataIndex: 'STATUS',renderer:getItemValue},
				{header: "创建人", width:'10%', dataIndex: 'CREATEPER'},
				{header: "创建时间", width:'10%', dataIndex: 'CREATEDATE',renderer:formatDate},
				{header: "更改人", width:'10%', dataIndex: 'UPDATEPER'},
				{header: "更改时间", width:'10%', dataIndex: 'UPDATEDATE'},
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'DEALER_ID',renderer:myLink}
			  ];
function myLink(dealer_id){
    return String.format(
    		 "<a href=\"<%=contextPath%>/sysmng/dealer/DealerInfo/querySalesDealerInfoDetail.do?DEALER_ID="
            +dealer_id+"\">[修改]</a>");
} 


function formatDate(value,meta,record){
	if(value!=null && value!=""){
		return value.substring(0,7).replace("-","");
	}
	else{
		return "";
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
	window.location.href='<%=contextPath%>/sysmng/dealer/DealerInfo/goAddSalesDealer.do';
}

function clrTxt(txtId){
   	document.getElementById(txtId).value = "";
}
</script>
</body>
</html>

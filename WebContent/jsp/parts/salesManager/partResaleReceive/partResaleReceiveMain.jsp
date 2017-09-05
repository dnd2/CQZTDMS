<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<title>配件零售/领用单</title>

<script type="text/javascript" >
$(function(){
	__extQuery__(1);
});
var myPage;

var url = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/partResRecSearch.json";
			
var title = null;

var columns = [
			{header: "序号", dataIndex: 'RETAIL_ID', renderer:getIndex,align:'center'},
			{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'},
			{header: "制单单号", dataIndex: 'RETAIL_CODE', align:'center'},
			{header: "类型", dataIndex: 'CHG_TYPE', align:'center',renderer:getItemValue},
			{header: "制单单位", dataIndex: 'SORG_CNAME', align:'center'},
			{header: "仓库", dataIndex: 'WH_CNAME', align:'center'},
			{header: "制单人", dataIndex: 'NAME', align:'center'},
			{header: "制单日期", dataIndex: 'CREATE_DATE', align:'center'},
			{header: "状态", dataIndex: 'STATE', align:'center',renderer:getItemValue}
	      ];

//设置超链接
function myLink(value,meta,record)
{
	var changeId = record.data.RETAIL_ID;
	var state = record.data.STATE;
	var str = "";
	var saveState = <%=Constant.PART_RESALE_RECEIVE_ORDER_TYPE_01 %>;
var cmmtState = <%=Constant.PART_RESALE_RECEIVE_ORDER_TYPE_02 %>;
	if(saveState == state)
	{
		str = "<a href=\"#\" onclick='viewDetail(\""+changeId+"\")'>[查看]</a>&nbsp;<a href=\"#\" onclick='ConformCommit(\""+changeId+"\")'>[提交]</a>&nbsp;<a href=\"#\" onclick='openPtPage(\""+changeId+"\")'>[打印订单]</a>"
	}
	else if(cmmtState == state)
	{
		str = "<a href=\"#\" onclick='viewDetail(\""+changeId+"\")'>[查看]</a>&nbsp;<a href=\"#\" onclick='openPtPage(\""+changeId+"\")'>[打印订单]</a>";
	}
	else
	{
		str = "<a href=\"#\" onclick='viewDetail(\""+changeId+"\")'>[查看]</a>";
	}
	return String.format(str);
}

function ConformCommit(retailId)
{
	MyConfirm("确定提交?",commitOrder,[retailId]);
}

function commitOrder(retailId){
	btnDisable();
	var url = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/commitOrderInfos.json?retailId="+ retailId +"&curPage=" + myPage.page;	
	sendAjax(url,getResult,'fm');
}

function getResult(json){
	btnEnable();
	if(null != json){
        if (json.errorExist != null && json.errorExist.length > 0) {
        	 MyAlert(json.errorExist);
        	 __extQuery__(json.curPage);
        } else if (json.success != null && json.success == "true") {
        	MyAlert("提交成功!", function(){
	        	__extQuery__(json.curPage);
        	});
        } else {
            MyAlert("提交失败，请联系管理员!");
        }
	}
}

//查看
function viewDetail(parms){
	btnDisable();
	var optionType = "view";
	document.fm.action="<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/viewOrderDeatilInint.do?changeId=" + parms +"&optionType=" + optionType;
	document.fm.target="_self";
	document.fm.submit();
}

//打印页面
function openPtPage(parms){
	var optionType = "print";
	document.fm.action="<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/viewOrderDeatilInint.do?changeId=" + parms +"&optionType=" + optionType;
	document.fm.target="_blank";
	document.fm.submit();
}

//新增领用
function addRec(){
	var parentOrgId = document.getElementById("parentOrgId").value;
	var companyName = document.getElementById("companyName").value;
	var addType = "rec";
	var actionURL = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/partOrderAddInit.do?addType=" + addType;
	document.getElementById("actionURL").value = actionURL;
	btnDisable();
	document.fm.action = actionURL;
	document.fm.target = "_self";
	document.fm.submit();
}

//新增零售
function addRes(){
	var parentOrgId = document.getElementById("parentOrgId").value;
	var companyName = document.getElementById("companyName").value;
	var addType = "res";
	var actionURL = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/partOrderAddInit.do?addType=" + addType;
	document.getElementById("actionURL").value = actionURL;
	btnDisable();
	document.fm.action = actionURL;
	document.fm.target = "_self";
	document.fm.submit();
}
    
//下载
function exportPartStockExcel(){
	document.fm.action="<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/exportSaleOrdersExcel.do";
	document.fm.target="_self";
	document.fm.submit();
}

//失效按钮
function btnDisable(){

    $$('input[type="button"]').each(function(button) {
        button.disabled = true;
    });

}

//有效按钮
function btnEnable(){

    $$('input[type="button"]').each(function(button) {
        button.disabled = "";
    });

}
</script>
</head>
<body>
  <form method="post" name ="fm" id="fm">
	<div class="wbox">
		<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />
		  &nbsp;当前位置： 配件零售领用管理  &gt; 配件零售领用单
		  <input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
		  <input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }"/>
		  <input type="hidden" name="companyName" id="companyName" value="${companyName }"/>
		  <input type="hidden" name="actionURL" id="actionURL" value=""/>
		</div>
		<table class="table_query">
			<th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 查询条件</th>
		    <tr>
		      <td width="10%"   align="right" >制单单号：</td>
		      <td width="20%" >
		        <input class="long_txt" type="text" name="changeCode" id="changeCode"/>
		      </td>
		      <td width="10%"   align="right" >类型：</td>
		      <td width="20%" >
		        <script type="text/javascript">
		        genSelBoxExp("orderType",<%=Constant.PART_SALE_STOCK_REMOVAL_TYPE%>,"",true,"short_sel","","false","").toString();
		        </script>
		      </td>
		      <td width="10%"   align="right" >状态：</td>
		      <td width="20%" >
		        <script type="text/javascript">
		        genSelBoxExp("orderState",<%=Constant.PART_RESALE_RECEIVE_ORDER_TYPE%>,"",true,"short_sel","","false","").toString();
		        </script>
		      </td>
	       </tr>
	       <tr>
		    <td width="10%"   align="right" >制单日期：</td>
		    <td width="22%">
		      <input id="checkSDate" class="short_txt" name="checkSDate"  datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
        	  <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
			 至
			  <input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
			  <input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
		    </td>
		    <td width="10%"   align="right" >仓库：</td>
		    <td width="20%" >
		      <select name="whId" id="whId" class="short_sel" >
		        <option value="">-请选择-</option>
	  			<c:if test="${WHList!=null}">
					<c:forEach items="${WHList}" var="list">
						<option value="${list.WH_ID }">${list.WH_CNAME }</option>
					</c:forEach>
				</c:if>
	  		  </select>
		    </td>
		    <td width="10%"   align="right" ></td>
		    <td width="20%" >
		    </td>
	      </tr>
	      <tr>
	    	<td  align="center" colspan="6" >
	    	  <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)"/>
	    	  <input class="long_btn" type="button" value="新增领用单" onclick="addRec()"/>
	    	  <input class="long_btn" type="button" value="新增零售单" onclick="addRes()"/>
	    	  <input class="normal_btn" type="button" value="导 出" onclick="exportPartStockExcel()"/>
	    	</td>    
		  </tr>
		</table>
	</div>
	
	<!-- 查询条件 end -->
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end -->
  </form>
</body>
</html>
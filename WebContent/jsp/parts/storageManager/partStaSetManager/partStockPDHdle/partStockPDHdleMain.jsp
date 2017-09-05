<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
	String contextPath = request.getContextPath();
%>
<title>库存盘点调整查询</title>
<script language="javascript" type="text/javascript">
	function doInit(){
		loadcalendar();  //初始化时间控件
		__extQuery__(1);
	}
</script>
</head>
<body>
  <form name="fm" id="fm" method="post" enctype="multipart/form-data">
	<div class="wbox">
		<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />
		  &nbsp;当前位置： 配件仓库管理  &gt;库存状态变更&gt;库存盘点封存处理
		  <input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
		  <input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }"/>
		  <input type="hidden" name="companyName" id="companyName" value="${companyName }"/>
		  <input type="hidden" name="resultId" id="resultId" value=""/>
		  <input type="hidden" name="pageType" id="pageType" value = ""/>
		</div>
	<table class="table_query">
	<th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 查询条件</th>
	<tr>
		<td width="10%"   align="right">盘点单号：</td>
		<td width="20%">
		  <input class="middle_txt" type="text" maxlength="20"  name="inveCode" id="inveCode" value="" />
		</td>
		<td width="10%"   align="right">导入日期：</td>
		<td width="22%">
		  <input id="impSDate" class="short_txt" name="impSDate" datatype="1,is_date,10" maxlength="10" group="impSDate,impEDate" />
		  <input class="time_ico" onclick="showcalendar(event, 'impSDate', false);" value=" " type="button" />
		 至
		  <input id="impEDate" class="short_txt" name="impEDate" datatype="1,is_date,10" maxlength="10" group="impSDate,impEDate" />
		  <input class="time_ico" onclick="showcalendar(event, 'impEDate', false);" value=" " type="button" />
		</td>
		<td width="10%"   align="right">导入人：</td>
		<td width="20%">
		  <input class="middle_txt" type="text" maxlength="20"  name="improterName" id="improterName" />
		</td>
	</tr>
	<tr>
		<td width="10%"   align="right">申请单号：</td>
		<td width="20%">
		  <input class="middle_txt" type="text" maxlength="20"  name="resultCode" id="resultCode" />
		</td>
		<td width="10%"   align="right">提交日期：</td>
		<td width="22%">
		  <input id="comSDate" class="short_txt" name="comSDate" datatype="1,is_date,10" maxlength="10" group="comSDate,comEDate" />
		  <input class="time_ico" onclick="showcalendar(event, 'comSDate', false);" value=" " type="button" />
		 至
		  <input id="comEDate" class="short_txt" name="comEDate" datatype="1,is_date,10" maxlength="10" group="comSDate,comEDate" />
		  <input class="time_ico" onclick="showcalendar(event, 'comEDate', false);" value=" " type="button" />
		</td>
		<td width="10%"   align="right">提交人：</td>
		<td width="20%">
		  <input class="middle_txt" type="text" maxlength="20"  name="commitName" id="commitName" />
		</td>
	</tr>
	<tr>
		<td width="10%"   align="right">盘点仓库：</td>
		<td width="20%">
		  <select name="whId" id="whId" class="short_sel">
			<option value="">-请选择-</option>
			<c:if test="${WHList!=null}">
				<c:forEach items="${WHList}" var="list">
					<option value="${list.WH_ID }">${list.WH_CNAME }</option>
				</c:forEach>
			</c:if>
		  </select>
		</td>
		<td width="10%"   align="right">审核日期：</td>
		<td width="22%">
		  <input id="cheSDate" class="short_txt" name="cheSDate" datatype="1,is_date,10" maxlength="10" group="cheSDate,cheEDate" />
		  <input class="time_ico" onclick="showcalendar(event, 'cheSDate', false);" value=" " type="button" />
		 至
		  <input id="cheEDate" class="short_txt" name="cheEDate" datatype="1,is_date,10" maxlength="10" group="cheSDate,cheEDate" />
		  <input class="time_ico" onclick="showcalendar(event, 'cheEDate', false);" value=" " type="button" />
		</td>
		<td width="10%"   align="right">审核人：</td>
		<td width="20%">
		  <input class="middle_txt" type="text" maxlength="20"  name="checkName" id="checkName" />
		</td>
	</tr>
	<tr>
		<td width="10%"   align="right">盘点类型：</td>
		<td width="20%">
		  <script type="text/javascript">
			   genSelBoxExp("inveType",<%=Constant.PART_STOCK_INVE_TYPE%>,"",true,"short_sel","","false",'');
		  </script>
		  <input type="hidden" name="orderState" id="orderState" value="<%=Constant.PART_INVE_ORDER_STATE_06%>" /> 
		</td>
		<td width="10%"   align="right">处理日期：</td>
		<td width="22%">
		  <input id="hanSDate" class="short_txt" name="hanSDate" datatype="1,is_date,10" maxlength="10" group="hanSDate,hanEDate" />
		  <input class="time_ico" onclick="showcalendar(event, 'hanSDate', false);" value=" " type="button" />
		 至
		  <input id="hanEDate" class="short_txt" name="hanEDate" datatype="1,is_date,10" maxlength="10" group="hanSDate,hanEDate" />
		  <input class="time_ico" onclick="showcalendar(event, 'hanEDate', false);" value=" " type="button" />
		</td>
		<td width="10%"   align="right">处理人：</td>
		<td width="20%">
		  <input class="middle_txt" type="text" maxlength="20"  name="handleName" id="handleName" />
		</td>
	</tr>
	<tr>
		<td width="10%"   align="right">是否完全处理：</td>
		<td width="20%">
		  <script type="text/javascript">
			   genSelBoxExp("isJF",<%=Constant.IF_TYPE%>,<%=Constant.IF_TYPE_NO %>,true,"short_sel","","false",'');
		  </script>
		</td>
		<td width="10%"   align="right">配件编码：</td>
		<td width="20%">
		  <input class="long_txt" type="text" maxlength="20"  name="partOldcode" id="partOldcode" />
		</td>
		<td width="10%"   align="right">配件名称：</td>
		<td width="20%">
		  <input class="middle_txt" type="text" maxlength="20"  name="partCname" id="partCname" />
		</td>
	</tr>
	<tr>
		<td align="center" colspan="6">
		  <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
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
  <script type="text/javascript" >
	var myPage;

	var url = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockPDHdleAction/partStockPDHdleSearch.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'RESULT_ID', renderer:getIndex,align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,style:'text-align:left;'},
				{header: "盘点单号", dataIndex: 'CHANGE_CODE', align:'center'},
				{header: "申请单号", dataIndex: 'RESULT_CODE', align:'center'},
				{header: "盘点类型", dataIndex: 'CHECK_TYPE', align:'center',renderer:getItemValue},
				{header: "盘点仓库", dataIndex: 'WH_CNAME', align:'center'},
				{header: "导入人", dataIndex: 'IMP_NAME', align:'center'},
				{header: "导入日期", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "提交人", dataIndex: 'COMM_NAME', align:'center'},
				{header: "提交日期", dataIndex: 'COMMIT_DATE', align:'center'},
				{header: "审核人", dataIndex: 'CHE_NAME', align:'center'},
				{header: "审核日期", dataIndex: 'CHECK_DATE', align:'center'},
				{header: "处理人", dataIndex: 'HAN_NAME', align:'center'},
				{header: "处理日期", dataIndex: 'HANDLE_DATE', align:'center'},
				{header: "处理方式", dataIndex: 'HANDLE_TYPE', align:'center',renderer:getItemValue},
				{header: "是否完全处理", dataIndex: 'JF_STATE', align:'center',renderer:getItemValue}
		      ];

	//设置超链接
	function myLink(value,meta,record)
	{
		var resultId = record.data.RESULT_ID;
		var jsState = record.data.JF_STATE;
		var typeYes = <%=Constant.IF_TYPE_YES %>;
		var str = "";

		if(typeYes != jsState)
		{
			str = "<a href=\"#\" onclick='viewDetail(\""+resultId+"\")'>[查看]</a>&nbsp;<a href=\"#\" onclick='handleDetail(\""+resultId+"\")'>[处理]</a>";
		}
		else
		{
			str = "<a href=\"#\" onclick='viewDetail(\""+resultId+"\")'>[查看]</a>&nbsp;";
		}
		return String.format(str);
	}

	//查看
	function viewDetail(parms){
		document.getElementById("resultId").value = parms;
		document.getElementById("pageType").value = "view";
		btnDisable();
		document.fm.action="<%=contextPath%>/parts/storageManager/partStaSetManager/partStockPDHdleAction/viewStockDeatilInit.do";
		document.fm.target="_self";
		document.fm.submit();
	}

	//解封
	function handleDetail(parms){
		document.getElementById("resultId").value = parms;
		document.getElementById("pageType").value = "handle";
		btnDisable();
		document.fm.action="<%=contextPath%>/parts/storageManager/partStaSetManager/partStockPDHdleAction/viewStockDeatilInit.do";
		document.fm.target="_self";
		document.fm.submit();
	}

	//下载
	function exportPartStockExcel(){
		document.fm.action="<%=contextPath%>/parts/storageManager/partStaSetManager/partStockPDHdleAction/exportPartStockStatusExcel.do";
		document.fm.target="_self";
		document.fm.submit();
	}

	//失效按钮
	function btnDisable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });

	    var inputArr = document.getElementsByTagName("a");
        for (var i = 0; i < inputArr.length; i++) {
            inputArr[i].disabled = true;
        }

	}

	//有效按钮
	function btnEnable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = "";
	    });

	    var inputArr = document.getElementsByTagName("a");
        for (var i = 0; i < inputArr.length; i++) {
            inputArr[i].disabled = false;
        }

	}
  </script>
</body>
</html>
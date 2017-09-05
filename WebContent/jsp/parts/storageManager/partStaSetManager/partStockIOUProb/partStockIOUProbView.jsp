<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<title>配件借条处理</title>
<script language="javascript" type="text/javascript">
	function doInit(){
		__extQuery__(1);
	}
</script>
</head>
<body>
  <form method="post" name ="fm" id="fm">
	<div class="wbox">
		<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />
		  &nbsp;当前位置： 配件仓库管理&gt;配件状态变更&gt;配件借条处理&gt;查看
		  <input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
		  <input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }"/>
		</div>
		<table class="table_query">
		  <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 信息</th>
		  <tr>
		      <td width="10%"   align="right" >变更单号：</td>
		      <td width="20%" >
		      ${map.CHANGE_CODE}
		       <input type="hidden" value="${map.CHANGE_ID}" name="changeId" id="changeId"/>
		      </td>
		      <td width="10%"   align="right" >制单人：</td>
		      <td width="20%" >
		      ${map.NAME}
		      </td>
		      <td width="10%"   align="right" >制单单位：</td>
		      <td width="20%">
		      ${map.CHGORG_CNAME}
		      </td>
	      </tr>
	      <tr>
	        <td width="10%"   align="right" >仓库：</td>
		    <td width="20%" >
		    ${map.WH_CNAME}
		    </td>
	        <td width="10%"   align="right" >备注：</td>
		    <td width="50%" colspan="3">
		    ${map.REMARK}
		    </td>
	      </tr>
		</table>
	</div>
	<th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 配件信息</th>
	
	<!-- 查询条件 end -->
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end -->
	<table class="table_query">
	  <tr align="center">
	    <td colspan="6">
		  <input class="normal_btn" type="button" value="返 回" onclick="goBack()"/>
		</td>
	  </tr>
	</table>
  </form>
  <script type="text/javascript" >
	var myPage;

	var url = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockIOUProbAction/partStockDetailSearch.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'DTL_ID', renderer:getIndex,align:'center'},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', style:'text-align: left;'},
				{header: "配件名称", dataIndex: 'PART_CNAME', style:'text-align: left;'},
				{header: "件号", dataIndex: 'PART_CODE', style:'text-align: left;'},
				{header: "可用库存", dataIndex: 'STOCK_QTY', align:'center'},
				{header: "业务类型", dataIndex: 'CHANGE_REASON', align:'center',renderer:getItemValue},
				{header: "调整类型", dataIndex: 'CHANGE_TYPE', align:'center',renderer:getItemValue},
				{header: "调整数量", dataIndex: 'RETURN_QTY', align:'center'},
				{header: "备注", dataIndex: 'REMARK', style:'text-align: left;'}
		      ];

	function goBack(){
		btnDisable();
		//fm.action = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockIOUProbAction/partStockIOUProbInit.do";
		//fm.submit();
		window.history.back(-1);
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
</body>
</html>
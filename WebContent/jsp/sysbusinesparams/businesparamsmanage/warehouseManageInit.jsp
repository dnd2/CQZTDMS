<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>

<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>仓库信息维护</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  系统管理 &gt; 系统业务参数维护 &gt; 仓库信息维护</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
				<td width="20%" class="tblopt"><div align="right">仓库名称：</div></td>
				<td width="20%" >
      				<input type="text" id="warehouseName" name="warehouseName" datatype="1,is_textarea,40" />
    			</td>
    			<td width="10%" class="tblopt"><div align="right">仓库类别：</div></td>
				<td width="20%" >
      				<script type="text/javascript">
						genSelBoxExp("warehouseType",<%=Constant.WAREHOUSE_TYPE%>,"",true,"short_sel",'',"false",'');
					</script>
    			</td>
				<td class="table_query_3Col_input" >
					<input type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> 
					<input type="button" class="normal_btn" onclick="toAddWarehouseInfo();" value="新 增" id="queryBtn" /> 
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
<script type="text/javascript" >

	var myPage;
	
	var url = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/WarehouseManage/warehouseManageList.json?COMMAND=1";
	
	var title = null;

	var columns = [
				{header: "仓库类别", dataIndex: 'WAREHOUSE_TYPE', align:'center',renderer:getItemValue},
				{header: "仓库代码", dataIndex: 'WAREHOUSE_CODE', align:'center'},
				{header: "仓库级别", dataIndex: 'WAREHOUSE_LEVEL', align:'center',renderer:getItemValue},
				{header: "仓库名称", dataIndex: 'WAREHOUSE_NAME', align:'center'},
				{header: "仓库状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'WAREHOUSE_ID',renderer:myLink}
		      ];
		      
	function myLink(warehouse_id){
        return String.format(
        		 "<a href=\"<%=contextPath%>/sysbusinesparams/businesparamsmanage/WarehouseManage/toEditwarehouseInfo.do?warehouse_id="
                +warehouse_id+"\">[修改]</a>");
    }

    function toAddWarehouseInfo(){
    	fm.action = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/WarehouseManage/toAddWarehouseInfo.do";
		fm.submit();
    }
	    
 </script>    
</body>
</html>
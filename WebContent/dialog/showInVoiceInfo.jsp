<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
	String invoiceNum = request.getParameter("invoiceNum");
	String lineNum = request.getParameter("lineNum");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">

</script>

<title>发票管理</title>
</head>
<body  onload="__extQuery__(1);"> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  财务管理 &gt; 发票管理 &gt; 发票管理</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
				<td width="15%" class="tblopt" align="left"><div align="right">产品编译：</div></td>
				<td width="15%" >
      				<input type="text" id="materialCode" name="materialCode" class="middle_txt" size="20"   />
    			</td>
    			<td width="15%" class="tblopt" align="left"><div align="right">产品名称：</div></td>
				<td width="15%" >
      				<input type="text" id="materialName" name="materialName" class="middle_txt" size="20"   />
    			</td>
				<td class="table_query_3Col_input" >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" class="normal_btn" onclick="findQuery();" value="查 询" id="queryBtn" /> 
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
	
</div>
<script type="text/javascript">

	var myPage;
	var invoiceNum = "<%=invoiceNum%>";
	var lineNum = "<%=lineNum%>";
	var url = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/getMaterialInfo.json?invoiceNum="+invoiceNum;
	//var url = "<%=contextPath%>/crm/customer/CustomerManage/getIntentVechileList.json?COMMAND=1";
	var title = null;
	var columns = [
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'materialId',renderer:myLink},
				{header: "序号", renderer:getIndex, align:'center'},
				{header: "产品编码", dataIndex: 'materialCode', align:'center'},
				{header: "产品名称", dataIndex: 'materialName', align:'center'}
		      ];

	function myLink(value,meta,rec){
		var data = rec.data;
		return "<input type='radio'  name='pose_id' value='"+value+"' onclick='submit_(\""+data.materialCode+"\",\""+data.materialName+"\",\""+data.materialId+"\");' />";
    }
		
	var index =1;
	function getIndex(){
		return index++;
	}
	
	function findQuery(){
		index=1;
		__extQuery__(1);
	}
	
	function submit_(intent_id,series_name,materialId){
		if (parent.$('inIframe')) {
			parentContainer.showInfo(lineNum,intent_id,series_name,materialId);	
			parentContainer._hide();
					
		}else {			
			parentContainer.showInfo(lineNum,intent_id,series_name,materialId);
			parentContainer._hide();
		}
	}
	
</script>    
</body>
</html>
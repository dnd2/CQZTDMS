<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		var fleet_id =  '' ;
		
		if(parent.$('inIframe')) {
			fleet_id = parent.$('inIframe').contentWindow.$("fleet_id").value;
		} else {
			fleet_id = top.$("fleet_id").value ;
		}
		
		document.getElementById("fleet_id").value=fleet_id;
	}
</script>

<title>实销信息上报</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 客户信息管理 &gt; 实销信息上报&gt; 集团客户合同列表</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
				<td width="20%" class="tblopt"><div align="right">合同编号：</div></td>
				<td width="30%" >
      				<input type="text" id="contract_no" name="contract_no" class="middle_txt" size="20"   />
    			</td>
				<td width="20%" class="tblopt"><div align="right">合同数量：</div></td>
				<td width="30%" >
      				<input type="text" id="contract_amount" name="contract_amount" class="middle_txt" size="20"   />
    			</td>
			</tr>
			<tr>
				<td width="20%" class="tblopt"><div align="right">有效期起：</div></td>
				<td width="30%" >
      				<input name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);" />
    			</td>
				<td width="20%" class="tblopt"><div align="right">有效期止：</div></td>
				<td width="30%" >
      				<input name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);" />
    			</td>
			</tr>
			<tr align="center">
				<td colspan="4">
					<input type="hidden" id="fleet_id" name="fleet_id" />
					<input type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> 
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
	
</div>
<script type="text/javascript">

	var myPage;
	var url = "<%=contextPath%>/sales/customerInfoManage/SalesReport/getFleetContractList.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'CONTRACT_ID',renderer:myLink},
				{header: "合同编号", dataIndex: 'CONTRACT_NO', align:'center'},
				{header: "合同数量", dataIndex: 'CONTRACT_AMOUNT', align:'center'},
				{header: "支持点位", dataIndex: 'DISCOUNT', align:'center'},
				{header: "有效期起", dataIndex: 'START_DATE', align:'center'},
				{header: "有效期止", dataIndex: 'END_DATE', align:'center'}
		      ];

	function myLink(value,meta,rec){
		var data = rec.data;
		return "<input type='radio'  name='ctm_id' value='"+value+"' onclick=submit_(\""+data.CONTRACT_ID+"\",\""+data.CONTRACT_NO+"\"); />"
    }

	function submit_(contract_id,contract_no){
		if (parent.$('inIframe')) {
			_hide();
			parent.$('inIframe').contentWindow.showFleetContractInfo(contract_id,contract_no);
		}else{
			parent._hide();
			parent.showFleetContractInfo(contract_id,contract_no);
		}
	}

</script>    
</body>
</html>
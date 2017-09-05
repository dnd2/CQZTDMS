<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>

<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商列表</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;经销商列表</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
				<td width="15%" class="tblopt"><div align="right">经销商代码：</div></td>
				<td width="20%" >
      				<input type="text" id="dealerCode" name="dealerCode" datatype="1,is_textarea,20" />
    			</td>
    			<td width="15%" class="tblopt"><div align="right">经销商名称：</div></td>
				<td width="20%" >
      				<input type="text" id="dealerName" name="dealerName" datatype="1,is_textarea,100" />
    			</td>
				<td class="table_query_3Col_input" >
					<input type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> 
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
<form  name="form1" id="form1">
<table class="table_query" width="85%" align="center">
	<tr class="table_list_row2">
		<td align="center">
			<input type="button" name="button1" class="cssbutton" onclick="submit_();" value="确定" /> 
		</td>
	</tr>
</table>
</form>

<script type="text/javascript" >
	document.form1.style.display = "none";
	var HIDDEN_ARRAY_IDS=['form1'];

	var myPage;
	
	var url = "<%=contextPath%>/sales/balancecentermanage/dealerordermanage/DealerOrderCheck/queryDealerList.json?COMMAND=1";
	
	var title = null;

	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"dealerCodes\")' />全选", width:'4%',sortable: false,dataIndex: 'DEALER_CODE',renderer:myCheckBox},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center',width:'48%'},
				{header: "经销商名称", dataIndex: 'DEALER_SHORTNAME', align:'center',width:'48%'}
		      ];
		      
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='dealerCodes' value='"+value+"' />");
	}

    function submit_(){
		var dealerCodes = document.getElementsByName("dealerCodes");
		var dealerCode = "";
		for(var i=0;i<dealerCodes.length;i++){
			if(dealerCodes[i].checked){
				dealerCode = dealerCode + dealerCodes[i].value + ",";
			}
		}
		if(!dealerCode){
			MyDivAlert("请选择经销商!");
			return;
		}else{
			parent.$('inIframe').contentWindow.$("dealerCode").value = dealerCode.substring(0,dealerCode.length-1);
			parent._hide();
		}
    }
 </script>    
</body>
</html>
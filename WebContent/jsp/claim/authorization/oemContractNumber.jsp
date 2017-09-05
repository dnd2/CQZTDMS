<%-- 
创建时间 : 2010.09.30
             创建人:lishuai
             功能描述：车厂查看开票的结果
--%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>合同号维护</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
	    function clearInput(inputId){
			var inputVar = document.getElementById(inputId);
			inputVar.value = '';
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;合同号维护
</div>
<form method="post" name="fm" id="fm">
<table align="center" class="table_query">
	<tr>

		<td align="right" nowrap>经销商代码：</td>
       
        
        <td align="left" >
				<input class="long_txt" id="dealerCode"  name="dealerCode" value="" readonly="readonly" type="text"/>
				<input type="hidden" name="dealerId" id="dealerId" value=""/>
	            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','dealerId','true','',true);" value="..." />        
	            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput('dealerCode');" value="清除"/>
			</td>
        <td align="right" nowrap="nowrap">生产基地：</td>
		<td align="left" nowrap="nowrap">
			<script type="text/javascript">
			genSelBoxContainStr("YIELDLY",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'<%=CommonUtils.checkNull(request.getAttribute("yieldly"))%>');
		    </script>
		</td>
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">
		var myPage;
		var url = "<%=contextPath%>/claim/authorization/BalanceMain/oemContractNumberView.json";
		var title = null;
		
		var columns = [
					{header: "序号",align:'center',renderer:getIndex},
					{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
					{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
					{header:'合同号',dataIndex:'CONTRACT_NO',align:'center'},
					{header: "操作",dataIndex: 'ID',align:'center', renderer:accAudut}
			      ];

	//修改的超链接
	
	function accAudut(value,meta,record){
			return String.format(
					"<a href='#' onClick='OpenHtmlWindow(\"<%=request.getContextPath()%>/claim/authorization/BalanceMain/oemContractNumberModifilyInit.do?id="+value+"\",300,300)'>[修改]</a><a href='#' onClick='deleteConstract("+value+")'>[删除]</a>"
					);
	}
	function deleteConstract(value){
		MyAlert(value);
		if(confirm("您确定删除吗？")){
			 fm.action="<%=contextPath%>/claim/authorization/BalanceMain/oemContractNumberDelete.do?id="+value;
			 fm.submit();
		}

	}
	function addConstractNo(){
		fm.action="<%=contextPath%>/claim/authorization/BalanceMain/addConstractNoInit.do?";
		 fm.submit();
	}
</script>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrGatherBalancePO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>结算单索赔单明细</title>
<% String contextPath = request.getContextPath(); %>
</head>
<script type="text/javascript">
	function doInit(){
		__extQuery__(1);
	}

	function closeWindow(){
		try{
			_hide();
		}catch(e){
			window.close();
		}
	}

	//设置超链接    
	function queryClaimNo(value,meta,record){
		return String.format("<a href=\"#\" onclick=\"queryDetail("+record.data.ID+")\">[" + value + "]</a>");
	}
	//查询索赔单明细
	function queryDetail(id){
		window.open("<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillDetailForward.do?ID="+id);
	}
</script>
<%String id = (String)request.getAttribute("id"); %>
<body>
	<form method="post" name ="fm" id="fm">
		<table width="100%">
		<tr><td>
	 	<div class="navigation">
	 		<input type="hidden" id="balanceId" name="balanceId" value="<%=id %>"/>
			<img src="<%=contextPath%>/img/nav.gif"/>&nbsp; 当前位置：售后服务管理&gt;索赔结算管理&gt;结算单索赔单明细
		</div>
		<table class="table_edit">
	    	<tr>
		    	<th><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
	        </tr>
        </table>
        <!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
        <!--分页 end --> 
        <script type="text/javascript">
	        var myPage;
	        //查询路径
	        var url = "<%=contextPath%>/claim/application/DealerBalance/queryClaimInfo.json";	
	        var title = null;
	        var columns = [
						{id:'action',header:"序号",align:'center',renderer:getIndex},
						{header: "索赔单号",dataIndex: 'CLAIM_NO',align:'center',renderer:queryClaimNo},
						{header: "申请金额",dataIndex: 'REPAIR_TOTAL',align:'center'},
	       				{header: "结算金额",dataIndex: 'BALANCE_AMOUNT',align:'center'},
	       				{header: "扣款金额",dataIndex:'SUB_AMOUNT',align:'center'}
	       		      ];
        </script>
        <br>
        </td></tr>
        <tr>
        	<td align="center">
        		<input type="button" name="closeBtn" class="normal_btn" value="关闭" onclick="closeWindow();"/>
        	</td>
        </tr>
        </table>
        <script type="text/javascript">
        	doInit();	
        </script>
	</form>
</body>
</html>
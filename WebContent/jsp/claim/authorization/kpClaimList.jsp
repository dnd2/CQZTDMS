<%-- 
创建时间 : 2010.08.27
             创建人:lishuai
             功能描述：经销商查看开票的结果
--%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>结算单管理</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;开票索赔单明细
</div>
<form method="post" name="fm" id="fm">
	<input type="hidden" id="balanceId" name="balanceId" value="${balanceId}"></input>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">
//查询索赔单明细
	function accAudut(value,meta,record)
	{
  		var resObj = String.format("<a href='javascript:claimdetail("+record.data.ID+")'>"+value+"</a>");
  		//var claimStatus = record.data.STATUS;
  		return resObj;
	}
		function claimdetail(id){
			var tarUrl = "<%=contextPath%>/claim/application/ClaimBillStatusTrack/claimBillDetailForward.do?ID="+id;
			 window.open(tarUrl,"索赔单信息", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
			/*var width=900;
			var height=500;
			var screenW = window.screen.width-30;	
			var screenH = document.viewport.getHeight();
			if(screenW!=null && screenW!='undefined')
				width = screenW;
			if(screenH!=null && screenH!='undefined')
				height = screenH;
			
			OpenHtmlWindow(tarUrl,width,height);*/
		}
		var myPage;
		var url = "<%=contextPath%>/claim/application/DealerNewKp/kpClaimList.json?action=do";
		var title = null;
		
		var columns = [
					{header: "序号", renderer:getIndex, align:'center'},
					{header: "索赔单号",dataIndex: 'CLAIM_NO',align:'center',renderer:accAudut},
					{header: "申请金额",dataIndex: 'REPAIR_TOTAL',align:'center'},
					{header: "结算金额",dataIndex: 'BALANCE_AMOUNT',align:'center'},
					{header: "扣款金额",dataIndex: 'KONGKUAN',align:'center'}
			      ];
		__extQuery__(1);
</script>
</body>
</html>
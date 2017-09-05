<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- created by lishuai103@yahoo.com.cn 20100603 配件采购订单明细 -->
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件采购订单明细</title>
<script type="text/javascript">
	function doInit(){
		__extQuery__(1);
	}
</script>
</head>
<body>
<form name="fm" id="fm">
<div class="navigation">
<img src="../../../img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件索赔&gt;配件索赔查询
</div>
 	<table class="table_edit">
   		<tr ><th colspan="7"><img class="nav" src="../../../img/subNav.gif" />基本信息</th></tr>
   		<tr >
     		<td align="right" nowrap><span >索赔单号：</span></td>
     		<td align="left" nowrap><c:out value="${ps.CLAIM_NO}"/></td>
     		<td align="right" nowrap><span >索赔经销商代码：</span></td>
     		<td align="left" nowrap><c:out value="${ps.DEALER_CODE}"/></td>
     		<td align="right" nowrap>索赔经销商名称：</td>
     		<td align="left" nowrap><c:out value="${ps.DEALER_NAME}"/></td>
   		</tr>
   		<tr >
     		<td width="12%" align="right" nowrap><span >采购订单编号：</span></td>
     		<td width="10%" align="left" nowrap><c:out value="${ps.ORDER_NO}"/></td>
     		<td align="right" nowrap="nowrap"><span >货运单号：</span></td>
     		<td align="left" nowrap="nowrap"><c:out value="${ps.DO_NO}"/></td>
     		<td align="right" nowrap="nowrap"><span >签收时间</span>：</td>
     		<td align="left" nowrap="nowrap"><c:out value="${ps.SIGN_DATE}"/></td>
   		</tr>
   		<tr >
     		<td align="right" nowrap="nowrap"><span >索赔申请时间</span>：</td>
     		<td align="left" nowrap="nowrap"><c:out value="${ps.APPLY_DATE}"/></td>
     		<td align="right" nowrap="nowrap">&nbsp;</td>
     		<td align="left" nowrap="nowrap">&nbsp;</td>
     		<td align="right" nowrap>&nbsp;</td>
     		<td align="left" nowrap>&nbsp;</td>
   		</tr>
 	</table>
 	<br/>
 	<table class="table_list" style="border-bottom:1px solid #DAE0EE">
        <tr>
          <th>审核时间</th>
          <th>审核部门</th>
          <th>审核状态</th>
          <th>审核意见</th>
        </tr>
        <c:forEach items="${lists}" var="pf" varStatus="status">
       		<tr <c:if test="${status.count%2==0}"> class="table_list_row1" </c:if>
       			<c:if test="${status.count%2!=0}"> class="table_list_row2" </c:if>>
            	<td><c:out value="${pf.CHECK_DATE}"/></td>
            	<td><c:out value="${pf.ORG_NAME}"/></td>
            	<td>
					<script type="text/javascript">
		   				writeItemValue('<c:out value="${pf.CHECK_STATUS}"/>');
		  			</script>
				</td>
           	 	<td><c:out value="${pf.CHECK_REMARK}"/></td>
        	</tr>
       	</c:forEach> 
    </table>
    
     	<input type="hidden" id="orderId" value='<c:out value="${ps.ORDER_ID}"/>' />
     	<input type="hidden" id="claimId" value='<c:out value="${ps.CLAIM_ID}"/>' />
     	<input type="hidden" id="signNo" value='<c:out value="${signNo}"/>' />
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<script type="text/javascript">

	    var orderId = document.getElementById("orderId").value;
		var claimId = document.getElementById("claimId").value;
		var signNo = document.getElementById("signNo").value;
		var title = "配件信息";
		
		var url = "<%=contextPath%>/partsmanage/partclaim/PartClaimCheck/partClaimItem.json?signNo="+ signNo + "&claimId=" + claimId;
		var columns = [
				{header: "配件号", dataIndex: 'PART_ID', align:'center'},
				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "单位", dataIndex: 'UNIT', align:'center'},
				{header: "订货数量", dataIndex: 'ORDER_COUNT', align:'center'},
				{header: "货运数量", dataIndex: 'COUNT', align:'center'},
				{header: "签收数量", dataIndex: 'SIGN_QUANTITY', align:'center'},
				{header: "索赔数量", dataIndex: 'CLAIM_COUNT', align:'center'},
				{header: "索赔类型", dataIndex: 'CLAIM_TYPE_NAME', align:'center'}
		      ];
    
</script>
 <table class="table_edit">
        <tr>
          <td width="37%"  align="center" >
          	  <input type="button" name="BtnNo222" value="关闭" class="normal_btn" onclick="_hide()"></td>
        </tr>
 </table>
</body>
</html>
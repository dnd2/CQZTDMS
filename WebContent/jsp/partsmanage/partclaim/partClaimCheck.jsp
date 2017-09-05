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
<img src="../../../img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件索赔&gt;配件索赔审核
</div>
 	<table class="table_edit">
  		<th colspan="7"><img class="nav" src="../../../img/subNav.gif" />审核操作</th>
          <tr > 
            <td height="12" align=left>审核意见：</td>
            <td align=left><span class="tbwhite">
            	<textarea name='checkRemark'  id='checkRemark'   rows='2' cols='70' ></textarea>
            	<input type="button" onclick="passCheck()" class="normal_btn" style="width=8%" value="通过"/>
            	<input type="button" onclick="backCheck()" class="normal_btn" style="width=8%" value="驳回"/>
            	<input type="button" onclick="javascript:history.go(-1)" class="normal_btn" style="width=8%" value="返回"/>
            </span></td>
          </tr>
	</table>
	<br />
 	<table class="table_edit">
   		<th colspan="7"><img class="nav" src="../../../img/subNav.gif" />基本信息</th>
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
     	<input type="hidden" id="orderId" value='<c:out value="${ps.ORDER_ID}"/>' />
     	<input type="hidden" id="claimId" name="claimId" value='<c:out value="${ps.CLAIM_ID}"/>' />
     	<input type="hidden" id="signNo" value='<c:out value="${ps.SIGN_NO}"/>' />
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
		var url = "<%=contextPath%>/partsmanage/partclaim/PartClaimCheck/partClaimItem.json?signNo="+ signNo+"&claimId="+claimId;
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
	//用checkFlag区分通过或驳回操作
	function passCheck() {
		var url = "<%=contextPath%>/partsmanage/partclaim/PartClaimCheck/partClaimCheck.json?checkFlag=1";
		makeNomalFormCall(url ,cb,'fm','queryBtn');
	}
	function backCheck() {
		var url = "<%=contextPath%>/partsmanage/partclaim/PartClaimCheck/partClaimCheck.json?checkFlag=2";
		makeNomalFormCall(url ,cb,'fm','queryBtn');
	}
	function cb() {
		parent.window.MyAlert('审核成功');
		window.location = '<%=contextPath%>/jsp/partsmanage/partclaim/partClaimCheckQuery.jsp';
	}	      
</script>
</body>
</html>
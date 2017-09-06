<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>抵扣通知单查询</title>
</head>
<script type="text/javascript">
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/oldPartDeductionListQuery.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{header: "索赔申请单号",dataIndex: 'APP_CLAIM_NO',align:'center'},
  				{header: "VIN", dataIndex: 'VIN', align:'center'},
  				{header: "项目类型", dataIndex: 'OBJ_TYPE', align:'center',renderer:getItemValue},
  				{header: "项目代码", dataIndex: 'OBJ_CODE', align:'center'},
  				{header: "项目名称", dataIndex: 'OBJ_NAME', align:'center'},
  				{header: "抵扣费用", dataIndex: 'DEDUCTION_AMOUNT', align:'center'},
  				{header: "抵扣原因", dataIndex: 'DEDUCT_REMARK', align:'center',renderer:getItemValue},
  				{header: "备注", dataIndex: 'OTHER_REMARK', align:'center'}
  		      ];
   function doInit(){
	   __extQuery__(1);
   }
</script>
<body>
<div class="wbox">
<div class="navigation">
  <img src="../../../img/nav.gif" width="11" height="11" />&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;抵扣通知单明细
</div>
</div>
<form id="fm" name="fm">
<input type="hidden" name="claimId" id="claimId" value="${claimId}" />
<input type="hidden" name="curPage" id="curPage" value="1" />
<!--分页 begin -->
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
<br/>
<br>
<table width=100% border="0" cellspacing="0" cellpadding="0">
  <tr>
  <td height="12" align="center" >
    <input class="normal_btn" type="button" onclick="_hide();" value="关 闭" /></td>
  </td>
  </tr>
</table>
</form>
</body>
</html>
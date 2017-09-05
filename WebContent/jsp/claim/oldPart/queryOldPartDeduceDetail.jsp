<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.bean.ClaimDeduceOldPartDetailBean"%>
<%@page import="java.util.List"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔旧件抵扣--明细</title>
<% 
   String contextPath = request.getContextPath();
   ActionContext act = ActionContext.getContext();
   AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
   String logonName = logonUser.getName();
   ClaimDeduceOldPartDetailBean detailBean = (ClaimDeduceOldPartDetailBean)request.getAttribute("detailBean");
%>
</head>
<body onload="__extQuery__(1);">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理 &gt;索赔旧件管理 &gt;索赔旧件抵扣&gt;明细</div>
 <form method="post" name ="fm" id="fm">
  <input type="hidden" name="curPage" id="curPage" value="1" />
  <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
    <tr bgcolor="F3F4F8">
      <td align="right">经销商代码：</td>
      <td>
        <%=detailBean.getDealer_code() %>
      </td>
      <td align="right">经销商简称：</td>
      <td>
        <%=detailBean.getDealer_name() %>
      </td>
      <td align="right">所属区域：</td>
      <td>
        <%=detailBean.getAttach_area() %>
      </td>
      <td align="right">货运方式：</td>
      <td>
        <%=detailBean.getTransport_desc() %>
      </td>
    </tr>
    <tr bgcolor="F3F4F8">
      <td align="right">回运清单号：</td>
      <td>
        <%=detailBean.getReturn_no() %>
      </td>
      <td align="right">建单日期：</td>
      <td>
        <%=detailBean.getCreate_date() %>
      </td>
      <td align="right">提报日期：</td>
      <td>
        <%=detailBean.getReport_date() %>
      </td>
      <td align="right">入库日期：</td>
      <td>
        <%=detailBean.getStore_date() %>
      </td>
    </tr>
    <tr bgcolor="F3F4F8">
      <%-- 
      <td align="right">索赔单提交日期(年/月)：</td>
      <td>
        <%=detailBean.getWr_start_date() %>
      </td>
      --%>
      <td align="right">装箱数量：</td>
      <td>
        <%=detailBean.getParkage_amount()%>
      </td>
      <td align="right">差异数：</td>
      <td>
        <%=detailBean.getDiff_amount() %>
      </td>
      <td align="right">审批人：</td>
      <td>
        <%=detailBean.getApprove_name() %>
      </td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
  <table class="table_list">
    <tr> 
      <td height="12" align="center">
                    货运单号：<%=detailBean.getTran_no()%>
      </td>
      <td height="12" align="center">
      </td>
    </tr>
    <tr>
      <td height="12" align="center">
      </td>
      <td height="12" align=center>
       <input type="button" onclick="exportExcel();" class="normal_btn" style="width=8%" value="导出"/>
        &nbsp;&nbsp;
       <input type="button" onclick="javascript:window.close();" class="normal_btn" style="width=8%" value="关闭"/></td>
    </tr>
  </table>
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<script type="text/javascript">
var myPage;
//查询路径
var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartDeduceManager/searchDetailList.json?RETURN_ID="+<%=detailBean.getReturn_id()%>;
				
var title = null;

var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "索赔申请单号", dataIndex: 'claim_no', align:'center'},
				{header: "VIN", dataIndex: 'vin', align:'center'},
				{header: "需回运数量", dataIndex: 'n_return_amount', align:'center'},
				{header: "入库数量", dataIndex: 'sign_amount', align:'center'},
				{header: "差异数量", dataIndex: 'diff_amount', align:'center'},
				{header: "结算状态", dataIndex: 'balance_status', align:'center',renderer:showBalanceStatus},
				{header: "抵扣金额", dataIndex: 'deduct_amount', align:'center'},
				{header: "结算抵扣金额", dataIndex: 'balance_deduct', align:'center'},
				{header: "抵扣",align:'center',renderer:myLink}
		      ];
//全选checkbox
function myCheckBox(value,metaDate,record){
   return String.format("<input type='checkbox' id='orderIds' name='orderIds' value='" + value + "' />");
}
//超链接设置
function myLink(value,meta,record){
   var  claim_back_id=record.data.return_id;//回运清单主键
   var  vin=record.data.vin;//vin
   var  claim_no=record.data.claim_no;//索赔单号
   var  claim_id=record.data.claim_id;//索赔主键
   var  diff_amount=record.data.diff_amount;//差异数量
   if(diff_amount>0){
	   return String.format(
		       "<a href='#' onclick=\"isBalance('"+claim_back_id+"','"+claim_no+"','"+claim_id+"','"+vin+"');\">[抵扣]</a>"
				);
   }else{
	   return String.format("--");
   }
}
//结算标识显示
function showBalanceStatus(value,meta,record){
   if(value=='<%=Constant.CLAIM_APPLY_ORD_TYPE_07%>'){
	   return String.format("已结算");
   }else{
	   return String.format("未结算");
   }
}
//判断索赔单是否结算
function isBalance(claim_back_id,claim_no,claim_id,vin){
	var isBalance_url="<%=contextPath%>/claim/oldPart/ClaimOldPartDeduceManager/isBalanceOper.json?claim_back_id="
		 + claim_back_id+"&claim_no="+claim_no+"&vin="+vin+"&claim_id="+claim_id;
	makeNomalFormCall(isBalance_url,afterViewBalance,'fm','createOrdBtn');
}
//查看是否结算回调函数
function afterViewBalance(json){
	var isBalance=json.isBalance;
	var claim_back_id=json.claim_back_id;
	var claim_id=json.claim_id;
	var claim_no=json.claim_no;
	var vin=json.vin;
	if(isBalance=='0'){
       MyAlert("该索赔单还未结算，不能进行抵扣！");
	}else if(isBalance=='1'){
	   goToDeduct(claim_back_id,claim_no,claim_id,vin);
	}
}
//进入抵扣页面
function goToDeduct(claim_back_id,claim_no,claim_id,vin){
	var goDeductUrl="<%=contextPath%>/claim/oldPart/ClaimOldPartDeduceManager/deducePreViewInfo.do?claim_back_id="
	      +claim_back_id+"&claim_no="+claim_no+"&vin="+vin+"&claim_id="+claim_id;
	OpenHtmlWindow(goDeductUrl,900,500);
}
//导出
function exportExcel(){
	fm.action = "<%=contextPath%>/claim/oldPart/ClaimOldPartDeduceManager/toExcel.do?RETURN_ID="+<%=detailBean.getReturn_id()%>;
	fm.submit();
}
</script>
</body>
</html>

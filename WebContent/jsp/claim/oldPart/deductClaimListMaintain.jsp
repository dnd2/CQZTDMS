<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.bean.ClaimApproveAndStoredReturnInfoBean"%>
<%@page import="com.infodms.dms.bean.TtAsWrOldPartSignDetailListBean"%>
<%@page import="java.util.List"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title>索赔旧件抵扣维护</title>
<% 
   String contextPath = request.getContextPath();
   ActionContext act = ActionContext.getContext();
   AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
   String logonName = logonUser.getName();
   ClaimApproveAndStoredReturnInfoBean detailBean = (ClaimApproveAndStoredReturnInfoBean)request.getAttribute("returnListBean");
   List<TtAsWrOldPartSignDetailListBean> detailList = (List)request.getAttribute("detailList");
%>
</head>
<BODY onload="__extQuery__(1);">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理 &gt;索赔旧件管理 &gt;索赔旧件抵扣维护</div>
 <form method="post" name ="fm" id="fm">
  <!--分页 begin -->
  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
  <table class="table_list">
   <tr> 
     <td height="10" align="center" colspan="2">
     </td>
   </tr>
   <tr> 
     <td height="10" align="center" colspan="2">
      <input type="button" onclick="askCanncel();" class="long_btn" style="width=8%" value="取消整单抵扣"/>
      &nbsp;&nbsp;
      <input type="button" onclick="javascript:history.go(-1);" class="normal_btn" style="width=8%" value="返回"/>
    </td>
   </tr>
  </table>
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<script type="text/javascript">
var url="<%=contextPath%>/claim/oldPart/ClaimOldPartDeduceOrdManager/queryDeductOrdMaintainInfo.json?deduct_id="
	+<%=request.getAttribute("deduct_id")%>;
var title = null;
var columns = [
				{header: "序号", align:'center',renderer:getIndex},
				{header: "索赔申请单", dataIndex: 'claim_no', align:'center',renderer:queryClaimOrdDetail},
				{header: "VIN", dataIndex: 'vin', align:'center'},
				{header: "需回运数", dataIndex: 'part_amount', align:'center'},
				{header: "抵扣数", dataIndex: 'deduct_amount', align:'center'},
				{header: "配件扣款", dataIndex: 'part_deduct_money', align:'center',renderer:partFeeLink},
				{header: "维修项目扣款", dataIndex: 'hour_deduct_money', align:'center',renderer:hourFeeLink},
				{header: "其他项目扣款", dataIndex: 'other_deduct_money', align:'center',renderer:otherFeeLink},
				{id:'action',header: "操作",sortable: false,dataIndex: 'deduct_id',align:'center',renderer:deductLink}
		      ];
//配件文本框
function partFeeLink(value,metaDate,record){
   var deduct_id=record.data.deduct_id;
   return String.format("<input type='text' id='part_money"+deduct_id+"' name='part_money"+deduct_id+"' value='" + value + "' size=\"5\"	 readonly=\"readonly\" />");
}
//维修项目文本框
function hourFeeLink(value,metaDate,record){
   var deduct_id=record.data.deduct_id;
   return String.format("<input type='text' id='hour_money"+deduct_id+"' name='hour_money"+deduct_id+"' value='" + value + "' size=\"5\" readonly=\"readonly\" />");
}
//其他项目文本框
function otherFeeLink(value,metaDate,record){
   var deduct_id=record.data.deduct_id;
   return String.format("<input type='text' id='other_money"+deduct_id+"' name='other_money"+deduct_id+"' value='" + value + "' size=\"5\" readonly=\"readonly\" />");
}
//抵扣连接
function deductLink(value,metaDate,record){
   var claim_no=record.data.claim_no;
   return String.format("<a href='#' onclick=\"deductMaintain('"+value+"','"+claim_no+"');\">[修改]</a>");
}
//进入抵扣维护界面
function deductMaintain(deduct_id,claim_no){
	var url_params="?deduct_id="+deduct_id
	              +"&claim_no="+claim_no;
	OpenHtmlWindow('<%=contextPath%>/claim/oldPart/ClaimOldPartDeduceOrdManager/queryDeductInfoById.do'+url_params,900,500);
}
//索赔单明细
function queryClaimOrdDetail(value,metaDate,record){
	return String.format("<a href=\"<%=contextPath%>/claim/application/ClaimBillStatusTrack/claimBillDetailForward.do?ID="
			+ record.data.claim_id + "\" target=\"_blank\">" + value + "</a>");
}
function askCanncel(){
	MyConfirm("确认取消抵扣？",entireDeductDel,"");
}
function entireDeductDel(){
	var del_url="<%=contextPath%>/claim/oldPart/ClaimOldPartDeduceOrdManager/delDeductInfoOper.json?"
		+"deduct_id="+'<%=request.getAttribute("deduct_id")%>';
	makeNomalFormCall(del_url,delSingleCallBack,'fm','createOrdBtn');
}
//删除单条抵扣回调函数
function delSingleCallBack(json){
	var retCode=json.retCode;
	if(retCode!=null&&retCode!=''){
	   if(retCode=='success'){
	      parent.window.MyAlert("取消抵扣单成功！");
	      fm.action = "<%=contextPath%>/claim/oldPart/ClaimOldPartDeduceOrdManager/queryListPage.do";
	  	  fm.submit();
	   }
       if(retCode=='failure_001'){
          MyDivAlert("该抵扣单已经结算，不能取消！");
          return;
       }
       if(retCode=='failure_002'){
    	   MyDivAlert("获取抵扣参数失败！");
           return;
       }
       if(retCode=='failure_003'){
    	   MyDivAlert("无法查询到抵扣明细信息！");
           return;
       }
       if(retCode=='failure_004'){
    	   MyDivAlert("查询索赔信息失败！");
           return;
       }
	}
}
</script>
</BODY>
</html>

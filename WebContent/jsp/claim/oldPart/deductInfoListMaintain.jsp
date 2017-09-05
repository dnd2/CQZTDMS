<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.bean.DeductClaimInfoBean"%>
<%@page import="com.infodms.dms.bean.DeductDetailListBean"%>
<%@page import="com.infodms.dms.bean.DeductVinInfoBean"%>
<%@page import="com.infodms.dms.bean.ClaimLabourItemListBean"%>
<%@page import="com.infodms.dms.bean.ClaimDeductOtherItemListBean"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.List"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔旧件抵扣单维护--抵扣修改</title>
<% 
   String contextPath = request.getContextPath();
   ActionContext act = ActionContext.getContext();
   AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
   String logonName = logonUser.getName();
   DeductClaimInfoBean claimInfoBean = (DeductClaimInfoBean)request.getAttribute("claimInfoBean");
   DeductVinInfoBean vinBean = (DeductVinInfoBean)request.getAttribute("vinBean");
   String vin=vinBean==null?"":vinBean.getVin();//
   String vehicle_no=vinBean==null?"":vinBean.getVehicle_no();//牌照号
   String engine_no=vinBean==null?"":vinBean.getEngine_no();//发动机号
   String series_name=vinBean==null?"":vinBean.getSeries_name();//车系
   String package_name=vinBean==null?"":vinBean.getPackage_name();//车型
   String gearbox_no=vinBean==null?"":vinBean.getGearbox_no();//变速箱号
   String rearaxle_no=vinBean==null?"":vinBean.getRearaxle_no();//后桥号
   String transfer_no=vinBean==null?"":vinBean.getTransfer_no();//分动器号
   String yieldly=vinBean==null?"":vinBean.getYieldly();//产地
   
   //获得抵扣配件列表
   List<DeductDetailListBean> deductPartList = (List)request.getAttribute("deductPartList");
   //获得抵扣工时列表
   List<DeductDetailListBean> deductHourList = (List)request.getAttribute("deductHourList");
   //获得抵扣其他项目列表
   List<DeductDetailListBean> deductOtherList = (List)request.getAttribute("deductOtherList");
%>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理 &gt;索赔旧件管理 &gt;索赔旧件抵扣单维护&gt;抵扣修改</div>
 <form method="post" name ="fm" id="fm">
  <input type="hidden" name="claim_back_id" id="claim_back_id" value="<%=request.getAttribute("claim_back_id")%>" />
  <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
     <tr>
        <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />
          <a href="#" onclick="tabDisplayControl('baseTabId')">基本信息</a></th>
     </tr>
  </table>
  <table class="table_edit" id="baseTabId" style="display:none">
     <tr bgcolor="F3F4F8">
       <td align="right">经销商代码：</td>
       <td><%=CommonUtils.checkNull(claimInfoBean.getDealer_code())%></td>
       <td align="right">维修站简称：</td>
       <td>
        <%=CommonUtils.checkNull(claimInfoBean.getDealer_name())%>
       </td>
       <td align="right">索赔申请单：</td>
       <td>
         <%=CommonUtils.checkNull(claimInfoBean.getClaim_no())%>
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">维修工单号：</td>
       <td><%=CommonUtils.checkNull(claimInfoBean.getRo_no())%></td>
       <td align="right">工单开始日期：</td>
       <td>
         <%=CommonUtils.checkNull(claimInfoBean.getRo_startdate())%>
       </td>
       <td align="right">工单结束日期：</td>
       <td>
         <%=CommonUtils.checkNull(claimInfoBean.getRo_enddate())%>
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">进厂里程数：</td>
       <td>
         <%=CommonUtils.checkNull(claimInfoBean.getIn_mileage())%>公里
       </td>
       <td align="right">保修开始日期：</td>
       <td>
        <%=CommonUtils.checkNull(claimInfoBean.getGuarantee_date())%>
       </td>
       <td align="right">接待人：</td>
       <td><%=CommonUtils.checkNull(claimInfoBean.getServe_advisor())%></td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">VIN：</td>
       <td>
         <%=CommonUtils.checkNull(vin)%>
       </td>
       <td align="right">牌照号：</td>
       <td>
        <%=CommonUtils.checkNull(vehicle_no)%>
       </td>
       <td align="right">发动机号：</td>
       <td><%=CommonUtils.checkNull(engine_no)%></td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">车系：</td>
       <td>
        <%=CommonUtils.checkNull(series_name)%>
       </td>
       <td align="right">车型：</td>
       <td><%=CommonUtils.checkNull(package_name)%></td>
       <td align="right">变速箱号：</td>
       <td>
         <%=CommonUtils.checkNull(gearbox_no)%>
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">后桥号：</td>
       <td>
        <%=CommonUtils.checkNull(rearaxle_no)%>
       </td>
       <td align="right">分动器号：</td>
       <td><%=CommonUtils.checkNull(transfer_no)%></td>
       <td class="table_query_3Col_label_7Letter">产地：</td>
       <td><%=CommonUtils.checkNull(yieldly)%></td>
     </tr>
  </table>
  <table width=100% border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="91908E"  class="table_edit">
     <tr>
	   <th colspan="15"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />索赔抵扣配件列表</th>
     </tr>
     <tr bgcolor="F3F4F8">
       <th align="center">标志</th>
       <th align="center">配件代码</th>
       <th align="center">配件名称</th>
       <th align="center">已结算金额(元)</th>
       <th align="center">累计抵扣金额(元)</th>
       <th align="center">当次抵扣金额(元)</th>
       <th align="center">可变更抵扣上限(元)</th>
       <th align="center">变更抵扣金额(元)</th>
       <th align="center">抵扣原因</th>
       <th align="center">备注</th>
     </tr>
     <c:forEach var="deductPartList" items="${deductPartList}" varStatus="num">
      <tr class="table_list_row1">
       <td>
          <input type="checkbox" id="deduct_part_id" 
           name="deduct_part_id" value="deduct_part_id<c:out value='${deductPartList.deduct_id}'/>"/>
       </td>
       <td>
          <c:out value='${deductPartList.item_code}'/>
       </td>
       <td>
          <c:out value='${deductPartList.item_name}'/>
       </td>
       <td>
          <script type="text/javascript">
             document.write(amountFormat('<c:out value='${deductPartList.balance_amount}'/>'));
          </script>
          <input type="hidden" id="deduct_part_balance<c:out value='${deductPartList.deduct_id}'/>" 
           name="deduct_part_balance<c:out value='${deductPartList.deduct_id}'/>"
           value="<c:out value='${deductPartList.balance_amount}'/>"/>
       </td>
       <td>
          <script type="text/javascript">
             document.write(amountFormat('<c:out value='${deductPartList.deduct_amount}'/>'));
          </script>
       </td>
       <td>
          <script type="text/javascript">
             document.write(amountFormat('<c:out value='${deductPartList.deduct_money}'/>'));
          </script>
          <input type="hidden" id="deducted_part_money<c:out value='${deductPartList.deduct_id}'/>" 
           name="deducted_part_money<c:out value='${deductPartList.deduct_id}'/>" 
           value="<c:out value='${deductPartList.deduct_money}'/>"/>
       </td>
       <td>
          <script type="text/javascript">
             var use_balance=${deductPartList.balance_amount-deductPartList.deduct_amount+deductPartList.deduct_money};
             document.write(amountFormat(use_balance));
          </script>
       </td>
       <td>
          <input type="text" id="deduct_part_money<c:out value='${deductPartList.deduct_id}'/>"
           name="deduct_part_money<c:out value='${deductPartList.deduct_id}'/>" class="short_txt" value="0" datatype="1,isMoney,10" blurback="true"
           blurValue="'deduct_part_money<c:out value='${deductPartList.deduct_id}'/>','<c:out value='${deductPartList.deduct_amount}'/>','<c:out value='${deductPartList.deduct_money}'/>'"/>
       </td>
       <td>
        <script type="text/javascript">
          var id='<c:out value='${deductPartList.deduct_id}'/>';
          genSelBoxExp("back_type"+id,<%=Constant.OLDPART_DEDUCT_TYPE%>,'<c:out value='${deductPartList.deduct_reason}'/>',true,"","","true",'');
        </script>
       </td>
       <td>
         <input type="text" id="deduct_part_remark<c:out value='${deductPartList.deduct_id}'/>"
           name="deduct_part_remark<c:out value='${deductPartList.deduct_id}'/>" class="short_txt"
           value="<c:out value='${deductPartList.remark}'/>" datatype="1,is_digit_letter_cn,60"/>
       </td>
     </tr>
     </c:forEach> 
  </table>
  <table width=100% border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="91908E"  class="table_edit">
     <tr>
	   <th colspan="9"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />索赔抵扣维修项目列表</th>
     </tr>
     <tr bgcolor="F3F4F8">
       <th align="center">标志</th>
       <th align="center">作业代码</th>
       <th align="center">作业名称</th>
       <th align="center">已结算金额(元)</th>
       <th align="center">累计抵扣金额(元)</th>
       <th align="center">当次抵扣金额(元)</th>
       <th align="center">可变更抵扣上限(元)</th>
       <th align="center">变更抵扣金额(元)</th>
       <th align="center">备注</th>
     </tr>
     <c:forEach var="deductHourList" items="${deductHourList}" varStatus="num">
      <tr class="table_list_row1">
       <td>
          <input type="checkbox" id="deduct_hour_id" 
           name="deduct_hour_id" value="deduct_hour_id<c:out value='${deductHourList.deduct_id}'/>"/>
       </td>
       <td>
          <c:out value='${deductHourList.item_code}'/>
       </td>
       <td>
          <c:out value='${deductHourList.item_name}'/>
       </td>
       <td>
          <script type="text/javascript">
             document.write(amountFormat('<c:out value='${deductHourList.balance_amount}'/>'));
          </script>
          <input type="hidden" id="deduct_hour_balance<c:out value='${deductHourList.deduct_id}'/>" 
           name="deduct_hour_balance<c:out value='${deductHourList.deduct_id}'/>"
           value="<c:out value='${deductHourList.balance_amount}'/>"/>
       </td>
       <td>
          <script type="text/javascript">
             document.write(amountFormat('<c:out value='${deductHourList.deduct_amount}'/>'));
          </script>
       </td>
       <td>
          <script type="text/javascript">
             document.write(amountFormat('<c:out value='${deductHourList.deduct_money}'/>'));
          </script>
          <input type="hidden" id="deducted_hour_money<c:out value='${deductHourList.deduct_id}'/>" 
           name="deducted_hour_money<c:out value='${deductHourList.deduct_id}'/>" 
           value="<c:out value='${deductHourList.deduct_money}'/>"/>
       </td>
       <td>
          <script type="text/javascript">
             var use_balance=${deductHourList.balance_amount-deductHourList.deduct_amount+deductHourList.deduct_money};
             document.write(amountFormat(use_balance));
          </script>
       </td>
        <td>
          <input type="text" id="deduct_hour_money<c:out value='${deductHourList.deduct_id}'/>"
           name="deduct_hour_money<c:out value='${deductHourList.deduct_id}'/>" class="short_txt" value="0" datatype="1,isMoney,10" blurback="true"
           blurValue="'deduct_hour_money<c:out value='${deductHourList.deduct_id}'/>','<c:out value='${deductHourList.deduct_amount}'/>','<c:out value='${deductHourList.deduct_money}'/>'"/>
       </td>
       <td>
         <input type="text" id="deduct_hour_remark<c:out value='${deductHourList.deduct_id}'/>"
           name="deduct_hour_remark<c:out value='${deductHourList.deduct_id}'/>" class="short_txt"
           value="<c:out value='${deductHourList.remark}'/>" datatype="1,is_digit_letter_cn,60"/>
       </td>
     </tr>
     </c:forEach> 
  </table>
  <br/>
  <table></table>
  <table></table>
  <br/>
  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="91908E"  class="table_edit">
	 <tr>
        <th colspan="9"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />索赔抵扣其他项目列表</th>
     </tr>
     <tr bgcolor="F3F4F8">
       <th align="center">标志</th>
       <th align="center">项目名称</th>
       <th align="center">已结算金额(元)</th>
       <th align="center">累计抵扣金额(元)</th>
       <th align="center">当次抵扣金额(元)</th>
       <th align="center">可变更抵扣上限(元)</th>
       <th align="center">变更抵扣金额(元)</th>
       <th align="center">备注</th>
     </tr>
     <c:forEach var="deductOtherList" items="${deductOtherList}" varStatus="num">
      <tr class="table_list_row1">
       <td>
          <input type="checkbox" id="deduct_other_id" 
           name="deduct_other_id" value="deduct_other_id<c:out value='${deductOtherList.deduct_id}'/>"/>
       </td>
       <td>
          <c:out value='${deductOtherList.item_name}'/>
       </td>
       <td>
          <script type="text/javascript">
             document.write(amountFormat('<c:out value='${deductOtherList.balance_amount}'/>'));
          </script>
          <input type="hidden" id="deduct_other_balance<c:out value='${deductOtherList.deduct_id}'/>" 
           name="deduct_other_balance<c:out value='${deductOtherList.deduct_id}'/>"
           value="<c:out value='${deductOtherList.balance_amount}'/>"/>
       </td>
       <td>
          <script type="text/javascript">
             document.write(amountFormat('<c:out value='${deductOtherList.deduct_amount}'/>'));
          </script>
       </td>
       <td>
          <script type="text/javascript">
             document.write(amountFormat('<c:out value='${deductOtherList.deduct_money}'/>'));
          </script>
          <input type="hidden" id="deducted_other_money<c:out value='${deductOtherList.deduct_id}'/>" 
           name="deducted_other_money<c:out value='${deductOtherList.deduct_id}'/>"
           value="<c:out value='${deductOtherList.deduct_money}'/>"/>
       </td>
       <td>
          <script type="text/javascript">
             var use_balance=${deductOtherList.balance_amount-deductOtherList.deduct_amount+deductOtherList.deduct_money};
             document.write(amountFormat(use_balance));
          </script>
       </td>
        <td>
          <input type="text" id="deduct_other_money<c:out value='${deductOtherList.deduct_id}'/>"
           name="deduct_other_money<c:out value='${deductOtherList.deduct_id}'/>" class="short_txt" value="0" datatype="1,isMoney,10" blurback="true"
           blurValue="'deduct_other_money<c:out value='${deductOtherList.deduct_id}'/>','<c:out value='${deductOtherList.deduct_amount}'/>','<c:out value='${deductOtherList.deduct_money}'/>'"/>
       </td>
       <td>
         <input type="text" id="deduct_other_remark<c:out value='${deductOtherList.deduct_id}'/>"
           name="deduct_other_remark<c:out value='${deductOtherList.deduct_id}'/>" class="short_txt"
           value="<c:out value='${deductOtherList.remark}'/>" datatype="1,is_digit_letter_cn,60"/>
       </td>
     </tr>
     </c:forEach> 
  </table>
  <table class="table_list">
    <tr > 
      <td height="12" align="center">
       <input type="button" onclick="deduct();" class="normal_btn" style="width=8%" value="抵扣"/>
       &nbsp;&nbsp;
       <input type="button" onclick="javascript:_hide();" class="normal_btn" style="width=8%" value="关闭"/>
      </td>
    </tr>
  </table>
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<script type="text/javascript">
//抵扣操作
function deduct(){
	if(checkSelectedStatic()){
       return;
	}
	var url="<%=contextPath%>/claim/oldPart/ClaimOldPartDeduceOrdManager/deductOper.json";
	var url_params="?deduct_id="+'<%=request.getAttribute("deduct_main_id")%>';
	
	var part_select_str=getSelectStr(document.getElementsByName("deduct_part_id"));
	var hour_select_str=getSelectStr(document.getElementsByName("deduct_hour_id"));
	var other_select_str=getSelectStr(document.getElementsByName("deduct_other_id"));
	url_params+="&deduct_part_select_str="+part_select_str
	          +"&deduct_hour_select_str="+hour_select_str
	          +"&deduct_other_select_str="+other_select_str;
    //将抵扣配件抵扣金额添加到url中
    var tmp_money="";
    var deduct_reason="";
    var deduct_remark="";
    var tempArr="";
    if(part_select_str.length>0){
    	tempArr=part_select_str.split(",");
    	for(var i=0;i<tempArr.length;i++){
        	tmp_money=document.getElementById(tempArr[i].replace("deduct_part_id","deduct_part_money")).value;
        	deduct_reason=document.getElementById(tempArr[i].replace("deduct_part_id","back_type")).value;
        	deduct_remark=document.getElementById(tempArr[i].replace("deduct_part_id","deduct_part_remark")).value;
            var deducted_money=document.getElementById(tempArr[i].replace("deduct_part_id","deducted_part_money")).value;
        	var checkResult=checkMoney(tempArr[i].replace("deduct_part_id","deduct_part_money"),deducted_money);
        	if(checkResult=='1'){
               return;
            }
        	if(deduct_reason==''){
        		MyAlert("请选择配件的抵扣原因！");
                return;
            }
        	url_params+="&"+tempArr[i].replace("deduct_part_id","deducted_part_money")+"="+deducted_money;
        	url_params+="&"+tempArr[i].replace("deduct_part_id","deduct_part_money")+"="+tmp_money;
        	url_params+="&"+tempArr[i].replace("deduct_part_id","back_type")+"="+deduct_reason;
        }
    }
    //将抵扣工时抵扣金额添加到url中
    if(hour_select_str.length>0){
    	tempArr=hour_select_str.split(",");
        for(var i=0;i<tempArr.length;i++){
        	var deducted_money=document.getElementById(tempArr[i].replace("deduct_hour_id","deducted_hour_money")).value;
        	var checkResult=checkMoney(tempArr[i].replace("deduct_hour_id","deduct_hour_money"),deducted_money);
        	if(checkResult=='1'){
                return;
            }
        	tmp_money=document.getElementById(tempArr[i].replace("deduct_hour_id","deduct_hour_money")).value;
        	url_params+="&"+tempArr[i].replace("deduct_hour_id","deducted_hour_money")+"="+deducted_money;
        	url_params+="&"+tempArr[i].replace("deduct_hour_id","deduct_hour_money")+"="+tmp_money;
        }
    }
    //将抵扣其他项目抵扣金额添加到url中
    if(other_select_str.length>0){
    	tempArr=other_select_str.split(",");
        for(var i=0;i<tempArr.length;i++){
        	var deducted_money=document.getElementById(tempArr[i].replace("deduct_other_id","deducted_other_money")).value;
        	var checkResult=checkMoney(tempArr[i].replace("deduct_other_id","deduct_other_money"),deducted_money);
        	if(checkResult=='1'){
                return;
            }
        	tmp_money=document.getElementById(tempArr[i].replace("deduct_other_id","deduct_other_money")).value;
        	url_params+="&"+tempArr[i].replace("deduct_other_id","deducted_other_money")+"="+deducted_money;
        	url_params+="&"+tempArr[i].replace("deduct_other_id","deduct_other_money")+"="+tmp_money;
        }
    }
    if(submitForm('fm')){
   	  document.getElementById("baseTabId").style.display = 'none';
   	  //MyDivConfirm("确认抵扣？",deduct_confirm,[url+url_params]);
   	  if(confirm("确认抵扣？")){
  		deduct_confirm(url+url_params);
  	  }
   	}
}
function deduct_confirm(comfirm_url){
	makeNomalFormCall(comfirm_url,afterCall,'fm','createOrdBtn');
}
//回调处理
function afterCall(json){
	var retCode=json.retCode;
	var failure_item_code=json.failure_item_code;
	if(retCode=="success"){
		MyAlert("抵扣成功！");
		parent.window._hide();
		var deduct_id='<%=request.getAttribute("deduct_main_id")%>';
		parentDocument.getElementById('part_money'+deduct_id).value = json.part_total_amount;
		parentDocument.getElementById('hour_money'+deduct_id).value = json.hour_total_amount;
		parentDocument.getElementById('other_money'+deduct_id).value = json.other_total_amount;
	}else if(retCode=="failure_999"){
		MyAlert("对不起，您没有抵扣权限！");
	}else if(retCode=="failure_201"){
		MyAlert("抵扣失败，由于获得页面的抵扣编号失效！");
	}else if(retCode=="failure_301"){
		MyAlert("抵扣失败，获得索赔配件信息不合法！");
	}else if(retCode=="failure_302"){
		MyAlert("抵扣失败，没找到抵扣配件信息！");
	}else if(retCode=="failure_303"){
		MyAlert("修改\""+failure_item_code+"\"索赔配件表抵扣金额失败！");
	}else if(retCode=="failure_401"){
		MyAlert("抵扣失败，获得索赔工时信息不合法！");
	}else if(retCode=="failure_402"){
		MyAlert("抵扣失败，没找到抵扣工时信息！");
	}else if(retCode=="failure_403"){
		MyAlert("修改\""+failure_item_code+"\"索赔工时表抵扣金额失败！");
	}else if(retCode=="failure_501"){
		MyAlert("抵扣失败，获得索赔其他项目信息不合法！");
	}else if(retCode=="failure_502"){
		MyAlert("抵扣失败，没找到抵扣其他项目信息！");
	}else if(retCode=="failure_503"){
		MyAlert("修改\""+failure_item_code+"\"索赔其他项目表抵扣金额失败！");
	}
}
//获得抵扣参数，拼成串返回
function getDeductParameterStr(idStr){
	if(idStr.length==0) return "";
	var params="";
	var strArr=idStr.split(",");
	var mon="";
	for(var i=0;i<strArr.length;i++){
	   mon=document.getElementById(strArr[i].replace("id","money")).value;
	   params+="&"+strArr[i].replace("id","money")+"="+mon;
	}
	return params;
}
//检查选中项目
function checkSelectedStatic(){
	var part_list_size=0;
	var hour_list_size=0;
	var other_list_size=0;
	var total_size=0;
	part_list_size=getSelectStr(document.getElementsByName("deduct_part_id")).length;
	hour_list_size=getSelectStr(document.getElementsByName("deduct_hour_id")).length;
	other_list_size=getSelectStr(document.getElementsByName("deduct_other_id")).length;
	total_size=part_list_size+hour_list_size+other_list_size;
	if(total_size==0||total_size=='0'){
		MyAlert("您还没有选择任何抵扣项目！");
		return true;
	}
	return false;
}
//获得选中项目的id串，用","分开
function getSelectStr(select){
	var len=select.length;
	var retStr="";
	for(var i=0;i<len;i++){
	  if(select[i].checked){
		retStr+=select[i].value+",";
	  }
	}
	retStr=retStr.substring(0,retStr.length-1);
	return retStr;
}
function blurBack(obj){
	var id=obj;
	var param=document.getElementById(id).blurValue.split(",");
	if(param.length==3){
		var deductObjId=param[0]==null||param[0]==""?"":param[0].substring(1,param[0].length-1);
		var deduct_amount=param[1]==null||param[1]==""?0:param[1].substring(1,param[1].length-1);
		var deducted_money=param[2]==null||param[2]==""?0:param[2].substring(1,param[2].length-1);
		checkMoney(deductObjId,deduct_amount,deducted_money);
	}
}
//检查抵扣金额
function checkMoney(id,deduct_amount,deducted_money){//deduct_amount是已抵扣的总金额，deducted_money是历史抵扣金额
	document.getElementById("baseTabId").style.display = 'none';//将基本信息缩进
	var balance=parseFloat(document.getElementById(id.replace("money","balance")).value);//已结算金额
	var deduct_money=parseFloat(document.getElementById(id).value);//用户填写的变更金额
	var use_deduct_money=balance-parseFloat(deduct_amount)+parseFloat(deducted_money);//本次可使用的变更抵扣金额上限
	if(deduct_money<0){
	  MyAlert("变更抵扣金额不能少于0元！");
      return 1;
	}
	if(deduct_money>balance){
	   MyAlert("变更抵扣金额已超过已结算金额！");
	   return 1;
	}
	if(deduct_money>use_deduct_money){
	   MyAlert("变更抵扣金额已超过可变更抵扣上限！");
	   return 1;
	}
	return 0;
}
//控制信息列表显示或隐藏
function tabDisplayControl(tableId){
	var tab = document.getElementById(tableId);
	if(tab.style.display=='none'){
		tab.style.display = '';
	}else{
		tab.style.display = 'none';
	}
}
</script>
</body>
</html>

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
<title>二次抵扣索赔单查询</title>
</head>
<body onload="doInit();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;二次抵扣索赔单查询</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="id" id="id" value="" />
  	  <input type="hidden" name="flag" id="flag" value="" />
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
  	  <div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
    <TABLE class="table_query">
       <tr>
         <td class="right" >经销商： </td>
         <td align="left" nowrap="true">
           <input name="dealerName" id="dealerName" type="text" class="middle_txt" onclick="showOrgDealer('dealerCode','dealerId','true','','true','','10771002','dealerName');" readonly="readonly"/>
           <input type="hidden" name="dealerId" id="dealerId" value=""/>    
           <input type="hidden" name="dealerCode" id="dealerCode" value=""/>  
           <input class="normal_btn" type="button" value="清空" onclick="clearDealerInfo('dealerId', 'dealerCode');"/> 
		 </td>	
		  <td class="right" >索赔单号：</td>
          <td >
            <input id="appClaimNo" name="appClaimNo" value="" type="text" class="middle_txt">
          </td>  
      </tr>
       <tr>
         <td class="right" >索赔类型：</td>
          <td>
            <script type="text/javascript">
             genSelBoxExp("repairType",<%=Constant.REPAIR_TYPE%>,"",true,"u-select","","false",'');
            </script>
          </td>   
          <td class="right" >VIN：</td>
          <td >
            <input id="vin" name="vin" value="" type="text" class="middle_txt">
          </td>        
       </tr>
       <tr>
         <td class="center" colspan="4" nowrap="nowrap">
           <input class="u-button u-query" type="button" name="queryBtn" id="queryBtn" value="查询"  onClick="__extQuery__(1);">
         </td>
       </tr>
  </table>
  </div>
  </div>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form> 
</div>
<br>
</body>
<script type="text/javascript">
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/claimDeductionSecondQuery.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
				{id:'action',header: "操作",sortable: false,dataIndex: 'CLAIM_ID',renderer:myLink,align:'center'},
  				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
  				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
  				{header: "索赔单号", dataIndex: 'APP_CLAIM_NO', align:'center',renderer:setAppClaimNo},
  				{header: "索赔类型", dataIndex: 'REPAIR_TYPE', align:'center',renderer:getItemValue},
  				{header: "索赔总费用", dataIndex: 'APPLY_TOTAL_AMOUNT', align:'center'},
  				{header: "一次抵扣总费用", dataIndex: 'DEDUCTION_AMOUNT', align:'center'},
  				{header: "二次抵扣总费用", dataIndex: 'SECOND_DEDUCTION_AMOUNT', align:'center'}
  		      ];
  		      
   //超链接设置
   function myLink(value,meta,record){  
	 var claimId = record.data.CLAIM_ID;
	 return String.format("<a href='#' onclick=check("+claimId+")>[二次抵扣]</a>");
   }
   //二次抵扣查看
   function check(value){
   	 var id ="?claimId="+value;
   	 OpenHtmlWindow("<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/claimDeductionSecondShow.do"+id,1200,700);
   }
   //设置索赔单号
   function setAppClaimNo(value,meta,record){
     var claimId = record.data.CLAIM_ID;
     return String.format("<a href='#' onclick=fmFind("+claimId+")>"+value+"</a>");
   }
   function fmFind(value){
	 //alert(value);
     var form = document.getElementById("fm");
     document.getElementById("id").value = value;
     document.getElementById("flag").value = "se";
	 form.action ="<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/auditApp.do";
	 form.submit();	
   } 
   function doInit(){
	   __extQuery__(1);
	  loadcalendar();
   }
   function clearDealerInfo(){
      document.getElementById("dealerId").value="";
	  document.getElementById("dealerCode").value="";
	  document.getElementById("dealerName").value="";
   }
</script>

</html>
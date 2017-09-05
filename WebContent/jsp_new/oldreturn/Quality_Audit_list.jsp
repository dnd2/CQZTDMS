<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="com.infodms.dms.po.TmBusinessAreaPO,java.util.*" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件审批入库</title>
<% String contextPath = request.getContextPath();
Integer num = (Integer)request.getAttribute("currP");
if (num == null) {
	num = 1;
}
%>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;质量部入库审核</div>
  <form id="fm" name="fm">
  		<input type="hidden" id="curPage" name="curPage" value="${currP }">
    <TABLE class="table_query">
       <tr>
         <td align="right" nowrap="nowrap"  >经销商代码：</td>            
         <td align="left" nowrap="nowrap" >
         <input id="dealerCode" name="dealerCode" value="${dealerCodeSS }" type="text" class="middle_txt" />
         </td>
         <td align="right" nowrap="nowrap" >经销商名称：</td>
         <td align="left" nowrap="nowrap"><input id="dealerName" name="dealerName" value="${dealerName }" type="text" class="middle_txt" datatype="1,is_name,15" callFunction="javascript:MyAlert();"></td>
       </tr>
       <tr>
          <td align="right" nowrap="nowrap" >旧件回运清单号：</td>
         <td align="left" nowrap="nowrap"><input id="back_order_no" name="back_order_no" value="${back_order_no }" type="text" class="middle_txt"></td>
       </tr>
       <tr>
       		<td align="right" nowrap="nowrap" style="display: none" >生产基地：</td>
			<td align="left" nowrap="nowrap" style="display: none">
			 <script type="text/javascript">
				            genSelBoxExp("YIELDLY_TYPE",<%=Constant.PART_IS_CHANGHE%>,'<%=request.getAttribute("yieldly")%>',true,"short_sel","","false",'');
				        </script>
		
			</td>
	         <td align="right" nowrap="nowrap" >货运方式：</td>
	         <td align="left" nowrap="nowrap">
	          <script type="text/javascript">
	            genSelBoxExp("freight_type",<%=Constant.OLD_RETURN_STATUS%>,"${freight_type }",true,"short_sel","","false",'<%=Constant.OLD_RETURN_STATUS_02%>,<%=Constant.OLD_RETURN_STATUS_04%>');
	           </script>
	          </td>
      
         <td align="right" nowrap="nowrap" >提报时间： </td>
         <td align="left" nowrap="true">
			<input name="report_start_date" type="text" class="short_time_txt" id="report_start_date" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'report_start_date', false);" />  	
             &nbsp;至&nbsp; <input name="report_end_date" type="text" class="short_time_txt" id="report_end_date" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'report_end_date', false);" /> 
		</td>	
      </tr>
       <tr>
         <td align="right" nowrap="nowrap" >货运单号：</td>
          <td align="left" nowrap="nowrap"><input id="trans_no" name="trans_no" value="${trans_no }" type="text" class="middle_txt">
          </td>    
          <td align="right" nowrap="nowrap" >旧件回运清单状态：</td>
          <td align="left" nowrap="nowrap">
            <script type="text/javascript">
             genSelBoxExp("back_type",<%=Constant.BACK_LIST_STATUS%>,"${back_type }",true,"short_sel","","false",'<%=Constant.BACK_LIST_STATUS_01%>,<%=Constant.BACK_LIST_STATUS_02%>');
            </script>
          </td>          
       </tr>
       <tr>
         <td align="center" colspan="4" nowrap="nowrap">
           <input class="normal_btn" type="button" name="queryBtn" id="queryBtn" value="查询"  onClick="__extQuery__(1);">
         </td>
       </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form> 
<br>
<script type="text/javascript">
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/OldReturnAction/QualityAudit.json?type=query";			
   var title = null;

   var num = '<%=num%>';

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
				{id:'action',header: "操作",sortable: false,dataIndex: 'id',renderer:myLink,align:'center'},
  				{header: "经销商代码",dataIndex: 'DEALER_ID',align:'center'},
  				{header: "回运类型",dataIndex: 'RETURN_TYPE',align:'center',renderer:getItemValue},
  				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
  				{header: "基地", dataIndex: 'YIELDLY', align:'center',renderer:getItemValue},
  				{header: "旧件回运清单号", dataIndex: 'CLAIM_NO', align:'center'},
  				{header: "旧件回运清单状态", dataIndex: 'BACK_DESC', align:'center'},
  				{header: "提报日期", dataIndex: 'RETURN_DATE', align:'center',renderer:formatDate},
  				{header: "签收日期", dataIndex: 'SIGN_DATE', align:'center',renderer:formatDate},
  				{header: "审核日期", dataIndex: 'CREATE_DATE', align:'center'},
  				{header: "审核人", dataIndex: 'IN_WARHOUSE_NAME', align:'center'},
  				{header: "操作人", dataIndex: 'NAME', align:'center'},
  				{header: "货运方式", dataIndex: 'RETURN_DESC', align:'center'},
  				{header: "旧件回运起止时间", dataIndex: 'WR_START_DATE', align:'center'},
  				{header: "索赔单数", dataIndex: 'WR_AMOUNT', align:'center'},
  				{header: "装箱数", dataIndex: 'PARKAGE_AMOUNT', align:'center'},
  				{header: "索赔配件数", dataIndex: 'PART_AMOUNT', align:'center'},
  				{header: "货运单号", dataIndex: 'TRANS_NO', align:'center'},
  				{header: "三包员电话", dataIndex: 'TEL', align:'center'}
  		      ];
  		     __extQuery__(1);
   //超链接设置
   function myLink(value,meta,record){
	   var back_type=record.data.back_type;
	   var box_no = record.data.box_no;//装箱单号
	   var currStatus = record.data.BACK_DESC; 
	   var id = record.data.ID; 
 	   var   MonthFirstDay=new   Date(2012,5,1);     
 	   var str="";
 	   if("已审核"==currStatus){
 	       str+="<a href='#' onclick=isCheck12("+id+")>[审核]</a>";
 	 	}
 	   str+="<a href='#' onclick=isCheck1("+id+")>[打印]</a>";
 	   str+="<a href='#' onClick='OpenHtmlWindow(\"<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/oldPartSignAuditDetail.do?CLAIM_ID="+id+"\",900,500)'>[明细]</a>";
	  return String.format(str);
   }

   //首先检查是否填写货运单号，否则不予上报
   function checkTransNo(report_id,boxNumber){
	   if(boxNumber>0){
	       MyAlert('有数据未填写装箱单号!');
	       return;
	   }else{
		   var url= "<%=contextPath%>/claim/oldPart/ClaimBackPieceReportManager/checkTransNo.json?report_id="+report_id;
		   makeNomalFormCall(url,isReport,'fm','');
	   }
   }
   function isReport(json){
	   var isFillTransNo=json.flag;
	   var report_id=json.report_id;
	   //暂时去掉限制货运单号的限制(要放开去掉注释就OK)
	   if(isFillTransNo=='0'||isFillTransNo==null){
           MyAlert("货运单号不能为空!");
           return;
	   }
	   MyConfirm("确认上报吗？",report,[report_id]);
   }
   function report(str){
       fm.action="<%=contextPath%>/claim/oldPart/ClaimBackPieceReportManager/reportClaimInfo11.do?report_id="+str;
       fm.method="post";
       fm.submit();
   }
   
   
   function isCheck(id){
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/goApproveAndStoredPage6.do?CLAIM_ID="+id;
       fm.method="post";
       fm.submit();
   }
   //审核
   function isCheck12(id){
	        fm.action ="<%=contextPath%>/OldReturnAction/QualityAuditView.do?CLAIM_ID="+id+"&types=0";
		    fm.method="post";
		    fm.submit();
   }
   
   function isCheck1221(id){
	   var ok = 'ok';
		if(ok=='ok'){
			fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/oldPartSignAudit.do?CLAIM_ID="+id+"&types=1";
		    fm.method="post";
		    fm.submit();
		}else{
			MyAlert('前期有未审核的旧件!');
		}
   }
   function showDetail21(json){
		var ok = json.ok;
		var id = json.id;
		if(ok=='ok'){
			fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/goApproveAndStoredPage41.do?CLAIM_ID="+id;
		    fm.method="post";
		    fm.submit();
		}else{
			MyAlert('前期有未审核的旧件!');
		}
	}

   function isCheck13(id){
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/goApproveAndStoredPage51.do?CLAIM_ID="+id;
	   fm.method="post";
	   fm.submit();
	}
	
   function isCheck1(id){
     fm.action="<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryBoxNo2.do?id="+id;
	   fm.method="post";
	   fm.submit();
   }
	
   //格式化时间为YYYY-MM-DD
   function formatDate(value,meta,record) {
	 if (value==""||value==null) {
		return "";
	 }else {
		return value.substr(0,16);
	 }
   }
   function doInit(){
	  loadcalendar();
   }
</script>
</BODY>
</html>
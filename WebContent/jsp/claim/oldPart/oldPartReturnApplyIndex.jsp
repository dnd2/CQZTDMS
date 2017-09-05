<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>旧件回运申请管理</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body onload="init();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;旧件回运申请管理</div>
  <form id="fm" name="fm">
  	<input type="hidden" name="COMMAND" id="COMMAND" value="1" />	
  	<input type="hidden" name="dealerCode" id="dealerCode" value="${DEALER_CODE }" />
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="delId" id="delId" value="" />
    <table class="table_query">
       <tr>
         <td align="right" nowrap="nowrap">回运申请单号：</td>
         <td><input id="RETURN_APPLY_NO" name="RETURN_APPLY_NO" value="" type="text" maxlength="20" class="middle_txt" ></td>
		 <td align="right" nowrap="nowrap">回运申请单状态：</td>
         <td>&nbsp;
         	<c:choose>
         		<c:when test="${DEALER_CODE==null}">
         			<script type="text/javascript">
         			genSelBoxContainStr("STATUS",<%=Constant.SP_JJ_RETURN_APPLY_STATUS%>,"",true,"short_sel","","false",'<%=Constant.SP_JJ_RETURN_APPLY_STATUS_02%>,<%=Constant.SP_JJ_RETURN_APPLY_STATUS_03%>,<%=Constant.SP_JJ_RETURN_APPLY_STATUS_04%>');
		           </script>
         		</c:when>
         		<c:otherwise>
         		<script type="text/javascript">
		            genSelBoxExp("STATUS",<%=Constant.SP_JJ_RETURN_APPLY_STATUS%>,"",true,"short_sel","","false",'');
		           </script>
         		</c:otherwise>
         	</c:choose>
             
         </td>
       </tr>
       <tr>
         <td align="center" colspan="4"><input class="normal_btn" type="button" name="qryButton" value="查询"  onClick="__extQuery__(1);">
         <c:if test="${DEALER_CODE!=null }">
           <input class="normal_btn"  type="button" name="button1" value="新增"  onClick="location='oldparReturnApplyAdd.do?COMMAND=1';"/>
           </c:if> 
           </td>
       </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form> 
<br>
<script type="text/javascript">
   var myPage;
   var dealerCode = document.getElementById('dealerCode').value;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartReturnApplyManager/queryOldpartReturnApply.json?dealerCode="+dealerCode;
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{id:'action',header: "操作",sortable: false,dataIndex: 'RETURN_APPLY_ID',renderer:operateLink,align:'center'},
  				{header: "服务站代码",dataIndex: 'DEALER_CODE',align:'center'},
  				{header: "服务站名称",dataIndex: 'DEALER_SHORTNAME',align:'center'},//return_no
  				{header: "申请单号",dataIndex: 'RETURN_APPLY_NO',align:'center'},
  				{header: "申请单状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue},
  				{header: "上报人", dataIndex: 'REPORT_USER', align:'center'},
  				{header: "上报时间", dataIndex: 'REPORT_DATE', align:'center'},
  				{header: "审核人", dataIndex: 'CHECK_USER', align:'center'},
  				{header: "审核时间", dataIndex: 'CHECK_DATE', align:'center'}, 
  				{header: "发运单号", dataIndex: 'SEND_NO', align:'center'}, 
  				{header: "发运时间", dataIndex: 'SEND_DATE', align:'center'}, 
  				{header: "发运人", dataIndex: 'SEND_LINK_USER', align:'center'}, 
  				{header: "发运人联系手机", dataIndex: 'SEND_LINK_PHONE', align:'center'}, 
  				{header: "补录人", dataIndex: 'SEND_USER', align:'center'} 
  		      ];
  		     
   __extQuery__(1);
 //超链接设置
   function operateLink(value,meta,record){
	   var width=800;
	   var height=500;
	   var screenW = window.screen.width-30;	
	   var screenH = document.viewport.getHeight();
	   if(screenW!=null && screenW!='undefined')
		   width = screenW;
	   if(screenH!=null && screenH!='undefined')
		   height = screenH;
   	   var status=record.data.STATUS;
   	  
   	   var dealerCode = document.getElementById('dealerCode').value;
	   if(dealerCode==''){
		   var link = "<a href='#' onclick=\"checkTransportDetail("+value+",1)\">[审核]</a>";
		   if(status==<%=Constant.SP_JJ_RETURN_APPLY_STATUS_03%>){
			   link = "<a href='#' onclick=\"checkTransportDetail("+value+",2)\">[查看]</a>"+
			   "<a href='#' onclick=\"returnApplyTran("+value+")\">[补录]</a>";
		   }
			if(status==<%=Constant.SP_JJ_RETURN_APPLY_STATUS_04%>){
				link = "<a href='#' onclick=\"checkTransportDetail("+value+",2)\">[查看]</a>";
		   }
		   
		return String.format(
				link
			   );
	 }else{
	 	var commitCode = <%=Constant.SP_JJ_RETURN_APPLY_STATUS_02%>;
	 	var link = "<a href='#' onclick=\"checkTransportDetail("+value+",2)\">[查看]</a>";
	 	if(status==<%=Constant.SP_JJ_RETURN_APPLY_STATUS_01%>){
	 		link = link+"<a href='#' onclick=\"modifyTransportDetail("+value+")\">[修改]</a>"+
	 		"<a href='#' onclick=\"changeDo("+value+","+commitCode+")\">[上报]</a>";
	 	}
		return String.format(
				link
	   	   );
  	  
  	 }  
   }
 	//审核查看
   function checkTransportDetail(returnApplyId,viewOrCheck){
	   var i_url="<%=contextPath %>/claim/oldPart/ClaimOldPartReturnApplyManager/oemReturnApplyCheck.do?RETURN_APPLY_ID="+returnApplyId+"&VIEW_OR_CHECK="+viewOrCheck;
	   location.href=i_url;
   }
 	//修改
   function modifyTransportDetail(returnApplyId){
	   var i_url="<%=contextPath %>/claim/oldPart/ClaimOldPartReturnApplyManager/modifyReturnApplyDetail.do?RETURN_APPLY_ID="+returnApplyId;
	   location.href=i_url;
   }
   function changeDo(returnApplyId,status)
	{  
		MyConfirm("确认上报",changeTransportStatus, [returnApplyId,status]);
	}
   function changeTransportStatus(returnApplyId,status){
	   var i_url="<%=contextPath %>/claim/oldPart/ClaimOldPartReturnApplyManager/changeReturnApplyStatus.do?RETURN_APPLY_ID="+returnApplyId+"&STATUS="+status;
	   location.href=i_url;
   }
 	function returnApplyTran(returnApplyId){
 	   var i_url="<%=contextPath %>/claim/oldPart/ClaimOldPartReturnApplyManager/oldpartReturnApplyTran.do?RETURN_APPLY_ID="+returnApplyId;
 	   location.href=i_url;
 	}
 	function init(){
 		var msg = '<%=request.getAttribute("returnValue")%>';
 		if(msg!='null'){
 			MyAlert(msg);
 		}
 	}
</script>
</body>
</html>
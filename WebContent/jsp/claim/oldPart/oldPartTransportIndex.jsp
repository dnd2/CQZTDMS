<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔件回运清单管理</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body onload="">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔旧件运输管理</div>
  <form id="fm" name="fm">
  	<input type="hidden" name="COMMAND" id="COMMAND" value="1" />	
  	<input type="hidden" name="dealerCode" id="dealerCode" value="${DEALER_CODE }" />
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="delId" id="delId" value="" />
    <table class="table_query">
       <tr>
         <td align="right" nowrap="nowrap">运输单号：</td>
         <td><input id="TRANSPORT_NO" name="TRANSPORT_NO" value="" type="text" maxlength="20" class="middle_txt" datatype="1,is_null,50" callFunction="javascript:MyAlert();"></td>
		 <td align="right" nowrap="nowrap">运输单状态：</td>
         <td>&nbsp;
         	<c:choose>
         		<c:when test="${DEALER_CODE==null}">
         			<script type="text/javascript">
         			 	   genSelBoxExp("TRANSPORT_STATUS",<%=Constant.SP_JJ_TRANSPORT_STATUS%>,"",true,"short_sel","","false",'<%=Constant.SP_JJ_TRANSPORT_STATUS_01%>');
		           </script>
         		</c:when>
         		<c:otherwise>
         		<script type="text/javascript">
		            genSelBoxExp("TRANSPORT_STATUS",<%=Constant.SP_JJ_TRANSPORT_STATUS%>,"",true,"short_sel","","false",'');
		           </script>
         		</c:otherwise>
         	</c:choose>
             
         </td>
       </tr>
     <c:if test="${DEALER_CODE==null }">    
       <tr>
       	 <td align="right" nowrap="nowrap">商家名称：</td>
         <td><input id="DEALER_NAME" name="DEALER_NAME" value="" type="text" maxlength="20" class="middle_txt"></td>
         <td align="right" nowrap="nowrap">商家代码：</td>
         <td><input id="DEALER_CODE" name="DEALER_CODE" value="" type="text" maxlength="20" class="middle_txt"></td>
       </tr>
       <tr>
		 <td align="right" nowrap="nowrap">物流公司：</td>
         <td>
         	<input type="text" name="transport_name" id="transport_name" class="middle_txt"/>
         </td>
         <td></td>
         <td></td>
         
       </tr>
      </c:if>   
       <tr>
         <td align="center" colspan="4"><input class="normal_btn" type="button" name="qryButton" value="查询"  onClick="__extQuery__(1);">
          <c:if test="${DEALER_CODE==null }">
              <input class="normal_btn" type="button" name="qryButton" value="导出"  onClick="ExportToexcel();">
          </c:if> 
         <c:if test="${DEALER_CODE!=null }">
           <input class="normal_btn"  type="button" name="button1" value="新增"  onClick="location='oldpartTransportAdd.do?COMMAND=1';"/>
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
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartTransportManager/queryOldpartTransport.json?dealerCode="+dealerCode;
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{id:'action',header: "操作",sortable: false,dataIndex: 'TRANSPORT_ID',renderer:operateLink,align:'center'},
  				{header: "服务站代码",dataIndex: 'DEALER_CODE',align:'center'},
  				{header: "服务站名称",dataIndex: 'DEALER_NAME',align:'center'},//return_no
  				{header: "申请单号",dataIndex: 'TRANSPORT_NO',align:'center'},
  				{header: "申请单状态",dataIndex: 'TRANSPORT_STATUS',align:'center',renderer:getItemValue},
  				{header: "上报人", dataIndex: 'REPORT_NAME', align:'center'},
  				{header: "上报时间", dataIndex: 'REPORT_DATE', align:'center'},
  				{header: "审核人", dataIndex: 'CHECK_NAME', align:'center'},
  				{header: "审核时间", dataIndex: 'CHECK_DATE', align:'center'},
  				{header: "审核结果", dataIndex: 'STATUS', align:'center',renderer:getIt},
  				{header: "审核备注", dataIndex: 'AUDIT_REMARK', align:'center'} 
  		      ];
  		     
   __extQuery__(1);

   function getIt(value,meta,record){
	   var str="";
     if(value==1){
    	 str+="同意";
       }else if(value==2){
    	  str+="不同意";
       }else if(value==3){
    	  str+="审核中";
       }
     return  String.format(str);
    }
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
   	   var status=record.data.TRANSPORT_STATUS;
   	  
   	   var dealerCode = document.getElementById('dealerCode').value;
	   if(dealerCode==''){
	   var link='';
		   if(status==<%=Constant.SP_JJ_TRANSPORT_STATUS_02%>){
			    link+= "<a href='#' onclick=\"checkTransportDetail("+value+",1)\">[审核]</a>";
		   }
		   link += "<a href='#' onclick=\"checkTransportDetail("+value+",2)\">[查看]</a>";
		   if(status==<%=Constant.SP_JJ_TRANSPORT_STATUS_03%>){
			   link += "<a href='#' onclick=\"checkTransport("+value+")\">[是否失效]</a>";
			 }
		return String.format(
				link
			   );
	 }else{
	 	var commitCode = <%=Constant.SP_JJ_TRANSPORT_STATUS_02%>;
	 	var deleteCode = <%=Constant.SP_JJ_TRANSPORT_STATUS_05%>;
	 	var link = "<a href='#' onclick=\"checkTransportDetail("+value+",2)\">[查看]</a>";
	 	if(status==<%=Constant.SP_JJ_TRANSPORT_STATUS_01%>||status==<%=Constant.SP_JJ_TRANSPORT_STATUS_04%>){
	 		link = link+"<a href='#' onclick=\"modifyTransportDetail("+value+")\">[修改]</a>"+
	 		"<a href='#' onclick=\"changeDo("+value+","+commitCode+")\">[上报]</a>"+
	 		"<a href='#' onclick=\"changeDo("+value+","+deleteCode+")\">[删除]</a>";
	 	}
		return String.format(link);
  	  
  	 }  
   }
   function checkTransport(value){
	   MyConfirm("确认失效后,服务站将不能使用该运输方式,确认失效?",changeStatus,[value]);
       }
   function changeStatus(value){
       var url = "<%=contextPath %>/claim/oldPart/ClaimOldPartTransportManager/updateOldPartTransportStatus.json?TRANSPORT_ID="+value;
       sendAjax(url,showFunc,'fm');
	   }
   function showFunc(json){
      if(1==json.succ)
          {
            MyAlert("设置失效成功!");
            __extQuery__(1);
          }else{
        	  MyAlert("设置失效失败!");
              }
	   }
 	//审核查看
   function checkTransportDetail(transportId,viewOrCheck){
	   var i_url="<%=contextPath %>/claim/oldPart/ClaimOldPartTransportManager/oemTransportCheck.do?TRANSPORT_ID="+transportId+"&VIEW_OR_CHECK="+viewOrCheck;
	   location.href=i_url;
   }
 	//修改
   function modifyTransportDetail(transportId){
	   var i_url="<%=contextPath %>/claim/oldPart/ClaimOldPartTransportManager/modifyTransportDetail.do?TRANSPORT_ID="+transportId;
	   location.href=i_url;
   }
   function changeDo(transportId,transportStatus)
	{  
		var msg = transportStatus==<%=Constant.SP_JJ_TRANSPORT_STATUS_02%>?'上报':'删除';
		MyConfirm("确认"+msg+"！",changeTransportStatus, [transportId,transportStatus]);
	}
 	//删除上报
   function changeTransportStatus(transportId,transportStatus){
	   var i_url="changeTransportStatus.do?TRANSPORT_ID="+transportId+"&TRANSPORT_STATUS="+transportStatus;
	   location.href=i_url;
   }
   function ExportToexcel(){
	   var dealerCode = document.getElementById('dealerCode').value;
	   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartTransportManager/ExportOldpartTransport.do?dealerCode="+dealerCode;
	   location.href=url;
	}
</script>
</body>
</html>
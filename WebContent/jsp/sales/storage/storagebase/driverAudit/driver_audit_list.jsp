<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>司机审核列表页面</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" >var $J=$.noConflict(); </script>

<!--页面列表 begin -->

<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/sales/storage/storagebase/DriverAuditAction/driverAuditList.json?query=query";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\"  onclick='selectAll(this,\"driver_id\")' />选择", align:'center',sortable:false, dataIndex:'id',width:'1%',renderer:checkBoxShow},
				{header: "承运商", width:'15%', dataIndex: 'LOGI_NAME'},
				{header: "司机姓名", width:'15%', dataIndex: 'DRIVER_NAME'},
				{header: "司机电话", width:'7%', dataIndex: 'DRIVER_PHONE'},
				{header: "申请时间", width:'7%', dataIndex: 'APPLY_DATE'},
				{header: "审核人", width:'7%', dataIndex: 'AUDIT_NAME'},
				{header: "审核时间", width:'7%', dataIndex: 'AUDIT_DATE'},
				{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink,align:'center'}
	      ];
		function checkBoxShow(value,meta,record){
			var  DRIVER_ID = record.data.DRIVER_ID;
			 var status=record.data.STATUS;
			var str ="";
			if(status==20501002){
				str ="<input type='checkbox' id='recesel'   name='driver_id' value='" + record.data.DRIVER_ID + "' />";
		  	}
			return String.format(str);
		}
	      function isAgree(value,meta,record){
	    	  if(null==value || ""==value ||"1"==value){
	    		  return String.format("是");
	    	  }else{
	    		  return String.format("否");
	    	  }
	      }
	     function mybacklink(value,meta,record){
              var id=record.data.ID;
              var str ='<a href="#" onclick="viewbackaduit('+id+'); ">'+value+'<a>';
              return   String.format(str);
		  }
		  function viewbackaduit(claimId){
			  var printUrl = "<%=contextPath%>/ClaimAction/viewbackaduit.do?bizId="+claimId;
			  OpenHtmlWindow(printUrl,900,500);
		}
	      function showPrintPage1(claimId){ 
	    	  var printUrl = "<%=contextPath%>/ClaimAction/barcodePrintDoGet.do?dtlIds="+claimId;
	          window.open(printUrl,'','toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=1200'); 
	       }
	      function showPrintPage2(claimId,claimType){ 
	            var tarUrl = "<%=contextPath%>/ClaimAction/claimBillForwardPrint.do?dtlIds="+claimId+"&claimType="+claimType;
	            window.open(tarUrl,'','toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=1200'); 
	       }
	      function myLink(value,meta,record){
		    var status=record.data.STATUS;
		    var driver_id=record.data.DRIVER_ID;
		  	var url="";
		  	if(status==20501002){
		  		url+="<a href='#' onclick='auditDriver(\""+driver_id+"\")';'>[审核]</a>";
		  	}
	        return String.format(url);
	      }
	      //司机审核页面去
	      function auditDriver(driver_id){
	    	  window.location.href='<%=contextPath%>/sales/storage/storagebase/DriverAuditAction/driverAuditPage.do?driver_id='+driver_id;
<%-- 	    	  OpenHtmlWindow('<%=contextPath%>/sales/storage/storagebase/DriverAuditAction/driverAuditPage.do?driver_id='+driver_id,700,500); --%>
	      }
	      
	      function report(obj,id){
	    	  MyConfirm("是否确认上报？",reportsubmit,[id]);
	      }
	      
	      function reportsubmit(id){
	    	  var url='<%=contextPath%>/ClaimAction/normalReportSubmit.json?id='+id;
	    	  sendAjax(url,function(json){
	    		  if(json.succ!="-1"){
	    			  MyAlert("提示：上报成功！");
	    			  __extQuery__(1);
	    		  }else{
	    			  MyAlert("提示：上报成功失败！");
	    			  return;
	    		  }
	    	  },'fm');
	      }
	      function cancel(obj,id){
	    	  MyConfirm("是否确认撤销上报？",cancelsubmit,[id]);
	      }
	      function cancelsubmit(id){
	    	  sendAjax('<%=contextPath%>/ClaimAction/normalCancel.json?id='+id,backCancel,'fm');
	      }
	      function backCancel(json){
	    	  if(json.succ!="-1"){
	    		  MyAlert("提示：撤销成功！");
	    		  __extQuery__(1);
	    	  }else{
	    		  MyAlert("提示：撤销失败！");
	    	  }
	      }
	      function del(obj,id){
	    	  MyConfirm("是否确认废弃？",delsubmit,[id]);
	      }
	      function delsubmit(id){
	    	  sendAjax('<%=contextPath%>/ClaimAction/normalDelete.json?id='+id,backDel,'fm');
	      }
	      function backDel(json){
	    	  if(json.succ!="-1"){
	    		  MyAlert("提示：作废成功！");
	    		  __extQuery__(1);
	    	  }else{
	    		  MyAlert("提示：作废失败！");
	    	  }
	      }
	      function add(){
	    	  OpenHtmlWindow('<%=contextPath%>/jsp_new/claim/normalAddInit.jsp',400,200);
	      }
	      function chooseType(claim_type){
	    	  window.location.href='<%=contextPath%>/ClaimAction/normalAdd.do?claim_type='+claim_type;
	      }
	      
	      function print(){
	    		var allChecks = document.getElementsByName('recesel');
	    			var ids="";
	    			var count=0;
	    			for(var i = 0;i<allChecks.length;i++){
	    				if(allChecks[i].checked){
	    				ids = allChecks[i].value+","+ids;
	    				count++;
	    			}
	    		}
	    		if(count==0){
	    		MyAlert("请选择要打印的数据!");
	    		return false;
	    		}
	    		if(ids!=""){
	    		ids=ids.substring(0,ids.length-1);
	    		}
	    		showPrintPage1(ids);
	    	}
	      function findamount(){
	    	  var url = "<%=contextPath%>/ClaimAction/findamount.json?type=normal";
	    	  sendAjax(url,findamountback,'fm');
		  }
		  function findamountback(json){
			  document.getElementById("amount").innerHTML=json.SUMAMOUNT;
	      }
		  function exportToexcel(){
	    	  window.location.href='<%=contextPath%>/ClaimAction/exportnormalManageList.do';
		  }
		  //批量审核
		  function auditAll(status){
			  var len = $J("input[name='driver_id']:checked").length;
			  if(len<=0){
				  MyAlert("没有勾选需要审核的单据！");
				  return ; 
			  }else{
				  var driver_ids ="";
				  $J("input[name='driver_id']:checked").each(function(){
					  driver_ids = driver_ids+$J(this).val()+",";
				  });
				  driver_ids = driver_ids.substring(0,driver_ids.length-1);
				  $J("#driver_id").val(driver_ids);
			  }
			  var audit_remark = $J("#audit_remark").val();
			  if(status==20501004 && audit_remark==""){
				  MyAlert("审核拒绝,审核备注不能为空");
				  return ;
			  }
			  MyConfirm("是否确定审核？",function(){
			  var url = "<%=contextPath%>/sales/storage/storagebase/DriverAuditAction/driverAuditAll.json?status="+status;
				 sendAjax(url,auditallback,'fm');
			  });
		  }
		  
		  function auditallback(json){
			  if(json.reslut=="SUCCESS"){
				  MyAlertForFun("审核成功！",function () {window.location.reload();});
			  }else{
				  MyAlert("审核失败！");
			  }
		  }
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation"><img src="<%=contextPath%>/jsp_new/img/nav.gif" />&nbsp;当前位置：储运管理&gt;基础管理&gt;司机审核</div>
<form name="fm" id="fm">
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>司机审核</h2>
<div class="form-body">
<input type="hidden" name="driver_id" id="driver_id"  />
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="right" nowrap="true">司机姓名：</td>
		<td width="15%" align="left"><input name="driver_name" type="text" id="driver_name"  maxlength="30" class="middle_txt"/></td>
		<td width="10%" class="right" nowrap="true">司机电话：</td>
      	<td width="15%" align="left">
      	<input name="driver_phone" type="text" id="driver_phone"  maxlength="30" class="middle_txt"/>
      	</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td width="10%" nowrap="true" class="right">状态：</td>
		<td width="15%" nowrap="true" align="left">
		     <script type="text/javascript">
		       genSelBoxExp("status",<%=Constant.DRIVER_TYPE%>,"20501002",true,"u-select","","false",'20501001');
		    </script>
		</td>
		<td class="right">承运商：</td>
			<td align="left">
			<c:if test="${logi!=null }">
			<select name="carrier_id" id="carrier_id" class="u-select" >
				<c:if test="${list_logi!=null}">
					<c:forEach items="${list_logi}" var="list_logi">
					  <c:if test="${logi==list_logi.LOGI_ID }">
						<option value="${list_logi.LOGI_ID}">${list_logi.LOGI_NAME}</option>
					  </c:if>
					</c:forEach>
				</c:if>
	  		</select>
			</c:if>
			<c:if test="${logi==null }">
		 	<select name="carrier_id" id="carrier_id" class="u-select" >
		 		<option value="">-请选择-</option>
				<c:if test="${list_logi!=null}">
					<c:forEach items="${list_logi}" var="list_logi">
						<option value="${list_logi.LOGI_ID}">${list_logi.LOGI_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
			</c:if>
	  		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td colspan="8" class="table_query_4Col_input" style="text-align: center">
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="u-button u-query" onClick="__extQuery__(1);"/> &nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="u-button u-reset" />
    	</td>
    </tr>
    <tr>
       <td align="center" colspan="8">
          <input type="button" name="btnQuery" id="auditall1" value="批量通过" class="normal_btn" onClick="auditAll(20501003);"/>
          <input type="button" name="btnQuery" id="auditall2" value="批量拒绝" class="normal_btn" onClick="auditAll(20501004);"/>
          <input type="text" name="audit_remark" id="audit_remark" class="middle_txt"/>
       </td>
    </tr>
</table>
</div>
</div>
<!-- 查询条件 end -->

<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
</html>
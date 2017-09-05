<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>一般索赔单列表查询页面</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/ClaimAction/normalManageList.json?query=true";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\"  onclick='selectAll(this,\"recesel\")' />选择", align:'center',sortable:false, dataIndex:'id',width:'2%',renderer:checkBoxShow},
				{header: "索赔单号", width:'15%', dataIndex: 'CLAIM_NO'},
				{header: "索赔类型", width:'7%', dataIndex: 'CLAIM_TYPE',renderer:getItemValue},
				{header: "结算基地", width:'7%', dataIndex: 'BALANCE_YIELDLY',renderer:getItemValue},
				{header: "修改次数", width:'15%', dataIndex: 'SUBMIT_TIMES'},
				{header: "退回次数", width:'15%', dataIndex: 'BACK_TIMES',renderer:mybacklink},
				{header: "VIN", width:'15%', dataIndex: 'VIN'},
				{header: "审核金额", width:'15%', dataIndex: 'BALANCE_AMOUNT'},
				{header: "车主", width:'15%', dataIndex: 'OWNER_NAME'},
				{header: "建单时间", width:'15%', dataIndex: 'CREATE_DATE'},
				{header: "申请状态", width:'15%', dataIndex: 'STATUS',renderer:getItemValue},
				{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink,align:'center'}
	      ];
		function checkBoxShow(value,meta,record){
			return String.format("<input type='checkbox' id='recesel' onclick='checkDate(this,this.value)'  name='recesel' value='" + record.data.ID + "' />");
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
		    var claim_type=record.data.CLAIM_TYPE;
		    var is_import=record.data.IS_IMPORT;
		  	var url="";
		    if(status==10791001 || status==10791006){
		    	var urlUpdate="<%=contextPath%>/ClaimAction/normalUpdateInit.do?id="+value+"&claim_type="+claim_type;
		    	url+="<a href='"+urlUpdate+"'>[修改]</a>";
		    	//url+="<a href='#' onclick='report(this,\""+ value +"\")';'>[上报]</a>";
		    	if(status==10791001){//索赔单退回和拒赔的不允许废弃
		    		url+="<a href='#' onclick='del(this,\""+ value +"\")';'>[废弃]</a>";
		    	}
		    }
	    	var urlView="<%=contextPath%>/ClaimAction/normalView.do?id="+value+"&claim_type="+claim_type;
		    url+="<a href='"+urlView+"'>[明细]</a>";
		    if(status==10791003){
		    	url+="<a href='#' onclick='cancel(this,\""+ value +"\")';'>[撤销上报]</a>";
		    }
		    if(status>10791007){
		    	url+="<a href='#' onclick='showPrintPage1(\""+value+"\")';'>[标签]</a>";
		    	if(is_import!="10041001"){
		    	url+="<a href='#' onclick='showPrintPage2(\""+value+"\",\""+claim_type+"\")';'>[索赔单打印]</a>";
			    }
		    }
	        return String.format(url);
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
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;经销商索赔单管理&gt;一般索赔单管理
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%"class="table_query_2Col_label_5Letter" nowrap="true">索赔单号：</td>
		<td width="15%"><input name="claim_no" type="text" id="claim_no"  maxlength="30" class="middle_txt"/></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">开始时间：</td>
      	<td width="15%"><input name="beginTime" type="text" id="beginTime" readonly="readonly" onfocus="calendar();" class="middle_txt"/></td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">结束时间：</td>
      	<td width="15%"><input name="endTime" type="text" id="endTime"  readonly="readonly" onfocus="calendar();" class="middle_txt"/></td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">索赔单状态：</td>
		<td width="15%" nowrap="true">
		     <script type="text/javascript">
		       genSelBoxExp("status",<%=Constant.CLAIM_APPLY_ORD_TYPE%>,"",true,"short_sel","","false",'10791001,10791003,10791007,10791009,10791010,10791011,10791012,10791013,10791014,10791015,10791016');
		    </script>
		</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">索赔单类型：</td>
		<td width="15%" nowrap="true">
		     <script type="text/javascript">
		       genSelBoxExp("claim_type",<%=Constant.CLA_TYPE%>,"",true,"short_sel","","false",'10661002,10661003,10661004,10661005,10661008,10661009,10661011,10661012');
		    </script>
		</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">VIN：</td>
      	<td width="15%" nowrap="true">
      	      <input name="vin" type="text" id="vin" maxlength="100" class="middle_txt"/>
      	</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">跟踪状态：</td>
		<td width="15%" nowrap="true">
			<script type="text/javascript">
		       genSelBoxExp("STATUS",<%=Constant.STATUS_TYPE%>,"",true,"short_sel","","false",'');
		    </script>
		</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">工单号:</td>
		<td width="15%" nowrap="true">
			 <input name="ro_no" type="text" id="ro_no" maxlength="100" class="middle_txt"/>
		</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">预授权单号:</td>
      	<td width="15%" nowrap="true">
			 <input name="ysq_no" type="text" id="ysq_no" maxlength="100" class="middle_txt"/>
      	</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		<input type="button"  name="bntAdd"  id="bntAdd"  value="新增" onclick="add();" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);findamount();"/>
    		&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="exportBtn" value="导出" class="normal_btn" onClick="exportToexcel();"/>
    		&nbsp;&nbsp;&nbsp;
    		<input type="button"  name="BtnAdd" id="queryBtn"  value="标签打印"  class="normal_btn" onClick="print()" >
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    	</td>
    </tr>
     <tr>
      <td align="center" colspan="8">总金额：<span style="color: red;" id="amount"></span></td>
    </tr>
</table>
<!-- 查询条件 end -->

<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
</html>
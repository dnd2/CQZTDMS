<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>工单</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/ClaimAction/pdiManageList.json?query=true";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "索赔单号", width:'15%', dataIndex: 'CLAIM_NO'},
				{header: "索赔类型", width:'7%', dataIndex: 'CLAIM_TYPE',renderer:getItemValue},
				//{header: "结算基地", width:'7%', dataIndex: 'BALANCE_YIELDLY',renderer:getItemValue},
				{header: "修改次数", width:'15%', dataIndex: 'SUBMIT_TIMES'},
				{header: "退回次数", width:'15%', dataIndex: 'BACK_TIMES',renderer:mybacklink},
				{header: "VIN", width:'15%', dataIndex: 'VIN'},
				{header: "审核金额", width:'15%', dataIndex: 'BALANCE_AMOUNT'},
				{header: "是否完好", width:'15%', dataIndex: 'IS_AGREE',renderer:isAgree},
				{header: "建单时间", width:'15%', dataIndex: 'CREATE_DATE'},
				{header: "申请状态", width:'15%', dataIndex: 'STATUS',renderer:getItemValue},
				{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink,align:'center'}
	      ];
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
	      function myLink(value,meta,record){
		    var status=record.data.STATUS;
		  	var url="";
		    if(status==10791001 || status==10791006){
		    	var urlUpdate="<%=contextPath%>/ClaimAction/pdiUpdateInit.do?id="+value;
		    	url+="<a href='"+urlUpdate+"'>[修改]</a>";
		    	url+="<a href='#' onclick='delPdi(this,\""+ value +"\")';'>[废弃]</a>";
		    }
	    	var urlView="<%=contextPath%>/ClaimAction/pdiView.do?id="+value;
		    url+="<a href='"+urlView+"'>[明细]</a>";
	    	
	        return String.format(url);
	      }
	      var id_del="";
	      function delPdi(obj,id){
	    	  id_del=id;
	    	  MyConfirm("是否确认废弃？",delsubmit,"");
	      }
	      function delsubmit(){
	    	  sendAjax('<%=contextPath%>/ClaimAction/pdiDelete.json?id='+id_del,backDel,'fm');
	      }
	      function backDel(json){
	    	  if(json.suc!="-1"){
	    		  MyAlert("提示：作废成功！");
	    		  __extQuery__(1);
	    	  }else{
	    		  MyAlert("提示：作废失败！");
	    	  }
	      }
	      function add(){
	    	  window.location.href='<%=contextPath%>/ClaimAction/pdiAdd.do';
	      }
	      function findamount(){
	    	  var url = "<%=contextPath%>/ClaimAction/findamount.json?type=pdi";
	    	  sendAjax(url,findamountback,'fm');
		  }
		  function findamountback(json){
			  document.getElementById("amount").innerHTML=json.SUMAMOUNT;
	      }
		  function exportToexcel(){
	    	  window.location.href='<%=contextPath%>/ClaimAction/exportpdiManageList.do';
		  }
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;经销商索赔单管理&gt;PDI索赔单管理
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
		       genSelBoxExp("status",<%=Constant.CLAIM_APPLY_ORD_TYPE%>,"",true,"short_sel","","false",'10791001,10791009,10791010,10791011,10791012,10791014,10791015,10791016');
		    </script>
		</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">VIN：</td>
      	<td width="15%" nowrap="true">
      	      <input name="vin" type="text" id="vin" maxlength="100" class="middle_txt"/>
      	</td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true"></td>
      	<td width="15%" nowrap="true">
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
<%-- 
创建时间 : 2010.08.27
             创建人:lishuai
             功能描述：经销商查看开票的结果
--%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>结算单管理</title>
	<script type="text/javascript">
	    function doInit(){
		   loadcalendar();
		   __extQuery__(1);
		}
		//清空经销商框
		function clearInput(){
			$('dealerCode').value="";
			$('dealerId').value="";
		}
	
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;开票单维护
         <span style="color: red;">新增时请选择经销商</span>
</div>
<form method="post" name="fm" id="fm">
<input type="hidden" name="status" value="${status}"/>
<input type="hidden" id="chek_status" name="chek_status" value=""/>
<input type="hidden" name="id" value="${id}"/>
<table align="center" class="table_query">
	<tr>
		<td align="right" nowrap="true">制单日期：</td>
		<td align="left" nowrap="true">
		  <input class="short_txt" id="startDate" name="startDate" datatype="1,is_date,10"
                 maxlength="10" group="startDate,endDate"/>
          <input class="time_ico" value=" " onclick="showcalendar(event, 'startDate', false);" type="button"/>
              至
          <input class="short_txt" id="endDate" name="endDate" datatype="1,is_date,10"
                 maxlength="10" group="startDate,endDate"/>
          <input class="time_ico" value=" " onclick="showcalendar(event, 'endDate', false);" type="button"/>
		</td>
		<td align="right" nowrap="true">经销商代码：</td>
		<td align="left" nowrap="true">
			<input class="middle_txt" id="dealerCode"  name="dealerCode" type="text" value="${dealerCode }" readonly="readonly"/>
            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','dealerId','true','',true);" value="..." />        
            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/>
			<input class="middle_txt" id="dealerId"  name="dealerId" value="${dealerId}" type="hidden" />
			<input class="middle_txt" id="poseId"  name="poseId" value="${poseId}" type="hidden"/>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">收票日期：</td>
		<td align="left" nowrap="true">
		  <input class="short_txt" id="collectStartDate" name="collectStartDate" datatype="1,is_date,10"
                 maxlength="10" group="collectStartDate,collectEndDate"/>
          <input class="time_ico" value=" " onclick="showcalendar(event, 'collectStartDate', false);" type="button"/>
              至
          <input class="short_txt" id="collectEndDate" name="collectEndDate" datatype="1,is_date,10"
                 maxlength="10" group="collectStartDate,collectEndDate"/>
          <input class="time_ico" value=" " onclick="showcalendar(event, 'collectEndDate', false);" type="button"/>
		</td>
		<td align="right" nowrap="true">收票人：</td>
		<td align="left" nowrap="true">
		 <input type="text" id="collectTickets" name="collectTickets" />
		</td>	
	</tr>
	<tr>
		<td align="right" nowrap="true">验票日期：</td>
		<td align="left" nowrap="true">
		  <input class="short_txt" id="checkStartDate" name="checkStartDate" datatype="1,is_date,10"
                 maxlength="10" group="checkStartDate,checkEndDate"/>
          <input class="time_ico" value=" " onclick="showcalendar(event, 'checkStartDate', false);" type="button"/>
              至
          <input class="short_txt" id="checkEndDate" name="checkEndDate" datatype="1,is_date,10"
                 maxlength="10" group="checkStartDate,checkEndDate"/>
          <input class="time_ico" value=" " onclick="showcalendar(event, 'checkEndDate', false);" type="button"/>
		</td>
		<td align="right" nowrap="true">验票人：</td>
		<td align="left" nowrap="true">
		 <input type="text" id="checkTickets" name="checkTickets" />
		</td>	
	</tr>
	<tr>
		<td align="right" nowrap="true">转账日期：</td>
		<td align="left" nowrap="true">
		  <input class="short_txt" id="transferStartDate" name="transferStartDate" datatype="1,is_date,10"
                 maxlength="10" group="transferStartDate,transferEndDate"/>
          <input class="time_ico" value=" " onclick="showcalendar(event, 'transferStartDate', false);" type="button"/>
              至
          <input class="short_txt" id="transferEndDate" name="transferEndDate" datatype="1,is_date,10"
                 maxlength="10" group="transferStartDate,transferEndDate"/>
          <input class="time_ico" value=" " onclick="showcalendar(event, 'transferEndDate', false);" type="button"/>
		</td>
		<td align="right" nowrap="true">转账人：</td>
		<td align="left" nowrap="true">
		 <input type="text" id="transferTickets" name="transferTickets" />
		</td>	
	</tr>
	<tr>
		<td align="right" nowrap="true">审核状态：</td>
		<td align="left" nowrap="true">
			<select id="state" name="state">
				<option value="">-请选择-</option>
				<option value="1">等待收票</option>
				<option value="2">等待验票</option>
				<option value="3">等待转账</option>
				<option value="4">已转账</option>
			</select>
		</td>
		<td align="right" nowrap="true">结算编号：</td>
		<td align="left" nowrap="true">
		 <input type="text" id="REMARK" name="REMARK" />
		</td>	
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">
		var myPage;
		var url = "<%=contextPath%>/claim/application/DealerNewKp/newDealerTickets.json";
		var title = null;
		
		var columns = [
					{header: "序号",renderer:getIndex,align:'center'},
					{header: "操作",dataIndex: 'DEALER_ID',align:'center', renderer:accAudut},
					{header: "结算编号",dataIndex: 'REMARK',align:'center'},
					{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
					{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
					{header: "开始时间",dataIndex: 'START_DATE',align:'center'},
					{header: "结束时间",dataIndex: 'END_DATE',align:'center'},
					{header: "制单日期",dataIndex: 'CREATE_DATE',align:'center'},	
					{header: "审核状态",dataIndex: 'STATE',align:'center'},	
// 					{header: "劳务费",dataIndex: 'LABOUR_AMOUNT',align:'center'},	
// 					{header: "材料费",dataIndex: 'PART_AMOUNT',align:'center'},
					{header: "上次行政扣款总金额",dataIndex: 'LABOUR_SUM',align:'center'},
					{header: "本次行政扣款总金额",dataIndex: 'DATUM_SUM',align:'center'},	
					{header: "总计(元)",dataIndex: 'AMOUNT_SUM',align:'center'},	
					{header: "收票人",dataIndex: 'COLLECT_TICKETS',align:'center'},
					{header: "收票时间",dataIndex: 'COLLECT_TICKETS_DATE',align:'center'},
					{header: "验票人",dataIndex: 'CHECK_TICKETS',align:'center'},
					{header: "验票时间",dataIndex: 'CHECK_TICKETS_DATE',align:'center'},
					{header: "转账人",dataIndex: 'TRANSFER_TICKETS',align:'center'},
					{header: "转账时间",dataIndex: 'TRANSFER_TICKETS_DATE',align:'center'},
					{header: "备注",dataIndex: 'NOTES',align:'center', renderer:show1}
			      ];

		function show1(value,meta,record){
			 var REMARK=record.data.NOTES;
			if(REMARK==null || REMARK==undefined){
	              return "--";
				}else if (REMARK.length>6) {
					REMARK = REMARK.substring(1,6)+"……";
					
				}
			 return REMARK;
		}
	      
	//修改的超链接
	
	
	var balanecNo="";
	function accAudut(value,meta,record)
	{
		var status = record.data.STATE;
			if("${id}"=="collect"){
				if(record.data.STATUS == 1)
		        {
					balanecNo=record.data.REMARK;
					var str="";
					str+="<a href='#' onclick=queryInfo01("+record.data.DEALER_ID+",'"+record.data.START_DATE+"','"+record.data.END_DATE+"','"+balanecNo+"')>[收票确认]</a>";
                    str+="<a href='#' onclick=addremark('"+record.data.BALANCE_ODER+"')>[添加备注]</a>";
                    str+="<a href='#' onclick=queryInfo02('"+record.data.REMARK+"','"+record.data.DEALER_ID+"')>[入库单打印]</a>";
		        	return String.format(str);
		        	
		        }else
		        {
		        	return String.format("<a href='#' onclick=queryInfo02('"+record.data.REMARK+"','"+record.data.DEALER_ID+"')>[入库单打印]</a>");
		        }
			}else if("${id}"=="check"){
				if(record.data.STATUS == 2)
		        {
					balanecNo=record.data.REMARK;
		        	return String.format("<a href='#' onclick=queryInfo01("+record.data.DEALER_ID+",'"+record.data.START_DATE+"','"+record.data.END_DATE+"','"+balanecNo+"')>[验票确认]</a>");
		        }else
		        {
		        	return String.format("");
		        }
			}else if("${id}"=="transfer"){
				if(record.data.STATUS == 3)
		        {
					balanecNo=record.data.REMARK;
		        	return String.format("<a href='#' onclick=queryInfo01("+record.data.DEALER_ID+",'"+record.data.START_DATE+"','"+record.data.END_DATE+"','"+balanecNo+"')>[转账确认]</a>");
		        }else
		        {
		        	return String.format("");
		        }
			}else if("等待收票"==status){
				return String.format("<a href='#' onclick=queryInfo01("+record.data.DEALER_ID+",'"+record.data.START_DATE+"','"+record.data.END_DATE+"','"+balanecNo+"')>[添加备注]</a>");
			}
/* 	        if(record.data.STATUS == 1)
	        {
	        	return String.format("<a href='#' onclick=queryInfo01("+record.data.DEALER_ID+",'"+record.data.START_DATE+"','"+record.data.END_DATE+"')>[收票确认]</a>");
	        }else if(record.data.STATUS == 2)
	        {
	        	return String.format("<a href='#' onclick=queryInfo01("+record.data.DEALER_ID+",'"+record.data.START_DATE+"','"+record.data.END_DATE+"')>[验票确认]</a>");
	        }else if(record.data.STATUS == 3)
	        {
	        	return String.format("<a href='#' onclick=queryInfo01("+record.data.DEALER_ID+",'"+record.data.START_DATE+"','"+record.data.END_DATE+"')>[转账确认]</a>");
	        } */
			
	}
	function addremark(balance_oder){
		OpenHtmlWindow('<%=contextPath%>/InvoiceAction/addRemarkPayment.do?type=add&balance_oder='+balance_oder,800,500);
	}
	
	 function queryInfo02(BALANCE_ODER,DEALER_ID)
   {
      window.open('<%=contextPath%>/claim/application/DealerNewKp/AppprintRk.do?BALANCE_ODER='+BALANCE_ODER+'&DEALER_ID='+DEALER_ID,"入库单打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
   }
	
	function getdealerId(val)
	{
		var url = '<%=contextPath%>/claim/application/DealerNewKp/getdealerId.json?dealerCode='+val ;
		makeNomalFormCall(url,function(json){
				var msg=json.msg;
				if(msg=='false1'){
					MyAlert('没有此经销商 ！');
					document.getElementById('addBtn').disabled = true; 
				}else if(msg=='false2')
				{
					MyAlert('此经销商开票权限已锁 ！');
					document.getElementById('addBtn').disabled = true; 
				}
				else{
					document.getElementById('addBtn').disabled = false; 
					 document.getElementById('dealerId').value = json.dealerId;
				}
			 },'fm');
	}
  
	function changeMoney(value){
		var money=parseFloat(value);
		if(money<0){
			return 0;
		}else{
			return value;
		}
	}
	function deleteInfo(balanceId)
	{
		MyConfirm('你确定要删除开票单?',function(){
			var url = '<%=contextPath%>/claim/application/DealerNewKp/kpDelete.json?balanceId='+balanceId ;
			makeNomalFormCall(url,function(json){
				var msg=json.msg;
				if(msg=='false1'){
					MyAlert('不能删除2011-05-08之前的索赔单!');
				}
				else if(msg=='false'){
					MyAlert('请先删除时间靠后的结算单!');
				}else{
					MyAlert('操作成功!',__extQuery__(1));
				}
			 },'fm');
		});
		
	}
	function deleteInfoBack(json){
		var msg=json.msg;
		if(msg=='false'){
			MyAlert('请先删除时间靠后的结算单!');
		}else{
			MyAlert('操作成功!');
		}
	}

	//zhumingwei 2011-12-19
	function queryPrint(val){
		makeNomalFormCall('<%=contextPath%>/claim/application/DealerNewKp/queryStatus.json?balanceId='+val,function(json){
			var msg=json.msg;
			if(msg=='ok'){
				window.open('<%=contextPath%>/claim/authorization/BalanceMain/queryPrintInfo.do?id='+val,'','left=0,top=0,width='+ (screen.availWidth - 10) +',height='+ (screen.availHeight-50) +',toolbar=no,menubar=no,scrollbars=no,location=no');
			}
			if(msg=='nook'){
				MyAlert('未打印抽查明细,请先完成!');
				return;
			}
			if(msg=='ok1'){
				window.open('<%=contextPath%>/claim/authorization/BalanceMain/queryPrintInfo.do?id='+val,'','left=0,top=0,width='+ (screen.availWidth - 10) +',height='+ (screen.availHeight-50) +',toolbar=no,menubar=no,scrollbars=no,location=no');
			}
		 },'fm');
	}
	var val="";
	var START_DATE="";
	var END_DATE="";
	function queryInfo01(val1,START_DATE1,END_DATE1,balanecNo)
	{
		val=val1;
		START_DATE=START_DATE1;
		END_DATE=END_DATE1;
		sendAjax('<%=contextPath%>/claim/authorization/BalanceMain/checkMoneyByBalanceNo.json?balanecNo='+balanecNo,backCheck,'fm');
	}
	function backCheck(json){
// 		    document.getElementById("chek_status").value=json.succ;
// 		    MyAlert(document.getElementById("chek_status").value);
			fm.action = "<%=contextPath%>/claim/authorization/BalanceMain/queryFinancialTickets.do?id="+val+"&START_DATE="+START_DATE+"&END_DATE="+END_DATE+"&typesta="+1+"&check_status="+json.succ;
			fm.submit();
	}
	function queryInfo(val,START_DATE,END_DATE)
	{
		 window.open('<%=contextPath%>/claim/authorization/BalanceMain/queryFinancialInfoView.do?id='+val+'&START_DATE='+START_DATE+'&END_DATE='+END_DATE,"开票通知单打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
	}
	//跳转到新增结算页面
	function forwordToAddPage(frmId,url,targetVar){
	    var dealerId= document.getElementById('dealerId').value;
	    if(dealerId.length == 0)
	    {
	    	MyAlert('请选择经销商！');
	    }else
	    {
	    	var frmVar = document.getElementById(frmId);
			frmVar.action = url;
			frmVar.target = targetVar;
			frmVar.submit();
	    }
	  
		
	}
	
</script>
</body>
</html>
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
		   __extQuery__(1);
		}
	    var logonUserDealerCode = '${logonUserDealerCode}';
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;开票单维护
         <span style="color: red;">新增时请选择经销商</span>
</div>
<form method="post" name="fm" id="fm">
<div class="form-panel">
	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
	<div class="form-body">
<table align="center" class="table_query">
	<tr>
		<td style="text-align: right">结算编号：</td>
      	<td>
      	    <input class="middle_txt" id="BalanceNo"  name="BalanceNo" type="text" maxlength="40"/>
      	</td>
		<td style="text-align: right">制单日期：</td>
		<td>
      		<input id="startDate" name="startDate" readonly="readonly" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="display: inline-block;min-width: 60px;width: 90px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
	     	至
	 		<input id="endDate" name="endDate" readonly="readonly" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="display: inline-block;min-width: 60px;width: 90px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
	 		<input type="button" class="normal_btn" onclick="oemTxt('startDate','endDate');" value="清 空" id="clrBtn"/> 
		</td>
		<td style="text-align: right">经销商代码：</td>
      	<td>
      	 	<input class="middle_txt" id="dealerCode" onclick="showOrgDealer('dealerCode','','true','',true,'','10771002');" readonly="readonly" name="dealerCode" type="text"/>       
            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput('dealerCode');" value="清除"/>
      	</td>      
	</tr>
	<tr>
		<!-- <td style="text-align: right">索赔单号：</td>
      	<td>
      	 	<input class="middle_txt" id="claim_no"  name="claim_no" type="text" maxlength="40"/>
      	</td> -->
      	<!-- <td style="text-align: right">运费单号：</td>
		<td>
			<input class="middle_txt" id="return_no"  name="return_no" type="text" maxlength="40"/>
		</td>  -->
<!--         <td style="text-align: right">结算人：</td>
      	<td>
      		 <input type="text" class="middle_txt" id="applyPersonName" name="applyPersonName" maxlength="50" />
      	</td> -->
	</tr>
	<tr>
		<td colspan="8" style="text-align: center">
			<input class="u-button u-query" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
			&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="u-button u-cancel" />
			&nbsp;&nbsp;&nbsp;&nbsp;
			<span id="aaaa"><input class="u-button u-submit" type="button" name="addBtn" id="addBtn" value="新增" onclick="showAllKP();"/></span> 
			&nbsp;&nbsp;&nbsp;&nbsp;
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="导出" onclick="toExport();"/>	
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">
		var myPage;
		var url = "<%=contextPath%>/claim/application/DealerNewKp/delaerKpMainQuery.json";
		var title = null;
		
		var columns = [
					{header: "序号",renderer:getIndex,align:'center'},
					{header: "操作",dataIndex: 'DEALER_ID',align:'center', renderer:accAudut},
					{id:'action',header: "单据状态", width:'10%', dataIndex: 'STATUS',renderer: getItemValue},
					{header: "结算编号",dataIndex: 'BALANCE_NO',align:'center'},
					{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
					{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
					{header: "开始时间",dataIndex: 'START_DATE',align:'center'},
					{header: "结束时间",dataIndex: 'END_DATE',align:'center'},	
					{header: "制单日期",dataIndex: 'CREATE_DATE',align:'center'},		
					{header: "上次行政扣款总金额",dataIndex: 'ADMIN_DEDUCT',align:'center'},
					{header: "本次行政扣款总金额",dataIndex: 'FINANCIAL_DEDUCT',align:'center'},	
					{header: "开票金额(元)",dataIndex: 'NOTE_AMOUNT',align:'center'},	
					{header: "收票人",dataIndex: 'REVIEW_APPLICATION_BY',align:'center'},
					{header: "收票时间",dataIndex: 'COLLECT_TICKETS_DATE',align:'center'}
					
			      ];	    
	//修改的超链接	
	function accAudut(value,meta,record)
	{ 
	        if(record.data.STATUS == '60771002')
	        {
	        	return String.format("<a href='#' onclick=queryInfo01("+record.data.DEALER_ID+",'"+record.data.START_DATE+"','"+record.data.END_DATE+"','"+record.data.BALANCE_NO+"')>[查看]</a>"+
	        						 "<a href='#' onclick=queryInfosp("+record.data.DEALER_ID+",'"+record.data.START_DATE+"','"+record.data.END_DATE+"','"+record.data.BALANCE_NO+"')>[收票]</a>");
	        }else{
		    	   return String.format("<a href='#' onclick=queryInfo01("+record.data.DEALER_ID+",'"+record.data.START_DATE+"','"+record.data.END_DATE+"','"+record.data.BALANCE_NO+"')>[查看]</a>");
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
	function queryInfo01(DEALER_ID,START_DATE,END_DATE,BALANCE_NO)//查询
	{
		 fm.action = "<%=contextPath%>/claim/application/DealerNewKp/queryFinancialInfoView07.do?flag=se&dealerId="+DEALER_ID+"&START_DATE="+START_DATE+"&END_DATE="+END_DATE+"&BALANCE_NO="+BALANCE_NO;
		 fm.submit();
	}	
	function queryInfosp(DEALER_ID,START_DATE,END_DATE,BALANCE_NO)//收票
	{
		 fm.action = "<%=contextPath%>/claim/application/DealerNewKp/queryFinancialInfoView07.do?flag=sp&dealerId="+DEALER_ID+"&START_DATE="+START_DATE+"&END_DATE="+END_DATE+"&BALANCE_NO="+BALANCE_NO;
		 fm.submit();
	}

	function showAllKP(){
		OpenHtmlWindow('<%=contextPath%>/jsp/claim/authorization/showAllInvoice.jsp',800,500);
	}
	function setSupplier(DEALER_ID,SDATE,EDATE,RETURN_ID){
		fm.action = "<%=contextPath%>/claim/application/DealerNewKp/addDealerKpInit.do?dealerId="+DEALER_ID+"&sdate="+SDATE+"&edate="+EDATE+"&returnId="+RETURN_ID;
		fm.submit();
	}
	function oemTxt(a,b){
		document.getElementById(a).value="";
		document.getElementById(b).value="";
}
</script>
</body>
</html>
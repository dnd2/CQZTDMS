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
</head>
<body onload="__extQuery__(1);">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;开票单维护
</div>
<form method="post" name="fm" id="fm">
<div class="form-panel">
	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
	<div class="form-body">
<table align="center" class="table_query">
	<tr>
		<td style="text-align: right;width: 10%">结算编号：</td>
      	<td style="width: 20%">
      	    <input class="middle_txt" id="BalanceNo"  name="BalanceNo" type="text" maxlength="40"/>
      	</td>
		<td style="text-align: right;width: 10%">制单日期：</td>
		<td style="width: 30%">
      		<input id="startDate" name="startDate" readonly="readonly" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="display: inline-block;min-width: 60px;width: 90px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
	     	至
	 		<input id="endDate" name="endDate" readonly="readonly" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="display: inline-block;min-width: 60px;width: 90px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
	 		<input type="button" class="normal_btn" onclick="oemTxt('startDate','endDate');" value="清 空" id="clrBtn"/> 
		</td>
		<td style="text-align: right"></td>
      	<td>
      	</td>
	</tr>
	<tr>
		<td colspan="6" style="text-align: center">		
			<input class="u-button u-query" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1);"/>
			&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="u-button u-cancel" />
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">
		var myPage;
		var url = "<%=contextPath%>/claim/application/DealerNewKp/delaerKpMaindlQuery.json";
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
					{header: "结算日期",dataIndex: 'CREATE_DATE',align:'center'},	
 					//{header: "运费条数",dataIndex: 'COUNT_RETURN',align:'center',renderer:return_link},	
					{header: "上次行政扣款总金额",dataIndex: 'ADMIN_DEDUCT',align:'center'},
					{header: "本次行政扣款总金额",dataIndex: 'FINANCIAL_DEDUCT',align:'center'},	
					{header: "开票金额(元)",dataIndex: 'NOTE_AMOUNT',align:'center'},	
					{header: "收票人",dataIndex: 'REVIEW_APPLICATION_BY',align:'center'},
					{header: "收票时间",dataIndex: 'COLLECT_TICKETS_DATE',align:'center'}
			      ];
	      
	//修改的超链接
	function accAudut(value,meta,record)
	{ 
	    if(record.data.STATUS == '60771001' || record.data.STATUS == '60771004')
	    {
	    	if(parseFloat(record.data.NOTE_AMOUNT)>0){
	    		return String.format("<a href='#' onclick=queryInfo01("+record.data.DEALER_ID+",'"+record.data.START_DATE+"','"+record.data.END_DATE+"','"+record.data.BALANCE_NO+"')>[查看]</a>"+
	    				"<a href='#' onclick=queryInfoAdd("+record.data.DEALER_ID+",'"+record.data.START_DATE+"','"+record.data.END_DATE+"','"+record.data.BALANCE_NO+"')>[编辑]</a>"+
	    				"<a href='#' onclick=report('"+record.data.ID+"')>[上报]</a>");
	    	}else{
	    		 return String.format("<a href='#' onclick=queryInfo01("+record.data.DEALER_ID+",'"+record.data.START_DATE+"','"+record.data.END_DATE+"','"+record.data.BALANCE_NO+"')>[查看]</a>");
	    	}
	    	   
	    }else{
	    	   return String.format("<a href='#' onclick=queryInfo01("+record.data.DEALER_ID+",'"+record.data.START_DATE+"','"+record.data.END_DATE+"','"+record.data.BALANCE_NO+"')>[查看]</a>");
	    }			
	}
 
	//zhumingwei 2011-12-19
	function queryPrint(val){
		makeNomalFormCall('<%=contextPath%>/claim/application/DealerNewKp/queryStatus.json?balanceId='+val,function(json){
			var msg=json.msg;
			if(msg=='ok'){
				window.open('<%=contextPath%>/claim/application/DealerNewKp/queryPrintInfo.do?id='+val,'','left=0,top=0,width='+ (screen.availWidth - 10) +',height='+ (screen.availHeight-50) +',toolbar=no,menubar=no,scrollbars=no,location=no');
			}
			if(msg=='nook'){
				MyAlert('未打印抽查明细,请先完成!');
				return;
			}
			if(msg=='ok1'){
				window.open('<%=contextPath%>/claim/application/DealerNewKp/queryPrintInfo.do?id='+val,'','left=0,top=0,width='+ (screen.availWidth - 10) +',height='+ (screen.availHeight-50) +',toolbar=no,menubar=no,scrollbars=no,location=no');
			}
		 },'fm');
	}
	function queryInfo01(DEALER_ID,START_DATE,END_DATE,BALANCE_NO)//查询
	{
		 fm.action = "<%=contextPath%>/claim/application/DealerNewKp/queryFinancialInfoView07.do?flag=se&dealerId="+DEALER_ID+"&START_DATE="+START_DATE+"&END_DATE="+END_DATE+"&BALANCE_NO="+BALANCE_NO;
		 fm.submit();
	}
	function queryInfoAdd(DEALER_ID,START_DATE,END_DATE,BALANCE_NO)//编辑
	{
		 fm.action = "<%=contextPath%>/claim/application/DealerNewKp/queryFinancialInfoView07.do?flag=add&dealerId="+DEALER_ID+"&START_DATE="+START_DATE+"&END_DATE="+END_DATE+"&BALANCE_NO="+BALANCE_NO;
		 fm.submit();
	}
	
	function queryInfo(DEALER_ID,START_DATE,END_DATE,BALANCE_NO)
	{
		window.open("<%=contextPath%>/claim/application/DealerNewKp/queryFinancialInfoView.do?dealerId="+DEALER_ID+"&START_DATE="+START_DATE+"&END_DATE="+END_DATE+"&BALANCE_NO="+BALANCE_NO,"开票通知单打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
	}
	function oemTxt(a,b){
		document.getElementById(a).value="";
		document.getElementById(b).value="";
	}
	function report(id){			
		MyConfirm("是否确认上报？",reportCommit,[id]);
	}

	function reportCommit(id){
		var rurl="<%=contextPath%>/claim/application/DealerNewKp/reportBalanceBill.json?id="+id;
		makeNomalFormCall(rurl,reportCommitBack,"fm");
	}
	function reportCommitBack(json){
		if(json.msg=="0"){
			MyAlert("操作成功！",Back);
		}else{
			MyAlert("存在相同已上报信息，请勿重复操作！");
		}
	}
	function Back(){
		__extQuery__(1);
	}
</script>
</body>
</html>
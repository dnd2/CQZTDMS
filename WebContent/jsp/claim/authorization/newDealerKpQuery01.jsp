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
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;开票单维护
         <span style="color: red;">新增时请选择经销商</span>
</div>
<form method="post" name="fm" id="fm">
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
		 <input type="text" id="dealerCode" name="dealerCode" onblur="getdealerId(this.value)"/>
		 <input type="hidden" id="dealerId" name="dealerId" />
		</td>
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
			&nbsp;&nbsp;
						<span id="aaaa"><input class="normal_btn" disabled="disabled" type="button" name="addBtn" id="addBtn" value="新增" 
				onclick="forwordToAddPage('fm','<%=contextPath%>/claim/application/DealerNewKp/addDealerKpInit01.do','_self');"/></span> 
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">
		var myPage;
		var url = "<%=contextPath%>/claim/application/DealerNewKp/delaerKpMainQuery01.json";
		var title = null;
		
		var columns = [
					{header: "序号",renderer:getIndex,align:'center'},
					{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
					{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
					{header: "开始时间",dataIndex: 'START_DATE',align:'center'},
					{header: "结束时间",dataIndex: 'END_DATE',align:'center'},
					{header: "制单日期",dataIndex: 'CREATE_DATE',align:'center'},	
					{header: "劳务费",dataIndex: 'LABOUR_AMOUNT',align:'center'},	
					{header: "材料费",dataIndex: 'PART_AMOUNT',align:'center'},
					{header: "上次行政扣款总金额",dataIndex: 'LABOUR_SUM',align:'center'},
					{header: "本次行政扣款总金额",dataIndex: 'DATUM_SUM',align:'center'},	
					{header: "总计(元)",dataIndex: 'AMOUNT_SUM',align:'center'},	
					{header: "操作",dataIndex: 'DEALER_ID',align:'center', renderer:accAudut}
			      ];
	      
	//修改的超链接
	function accAudut(value,meta,record)
	{ 
			return String.format("<a href='#' onclick=queryInfo01("+record.data.DEALER_ID+",'"+record.data.START_DATE+"','"+record.data.END_DATE+"')>[查看]</a><a href='#' onclick=queryInfo("+record.data.DEALER_ID+",'"+record.data.START_DATE+"','"+record.data.END_DATE+"') >[打印]</a>");
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
	function queryInfo01(val,START_DATE,END_DATE)
	{
		fm.action = "<%=contextPath%>/claim/authorization/BalanceMain/queryFinancialInfoView02.do?id="+val+"&START_DATE="+START_DATE+"&END_DATE="+END_DATE;
		 fm.submit();
	}
	
	function queryInfo(val,START_DATE,END_DATE)
	{
		window.open('<%=contextPath%>/claim/authorization/BalanceMain/queryFinancialInfoView03.do?id='+val+'&START_DATE='+START_DATE+'&END_DATE='+END_DATE,"开票通知单打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
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
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
         当前位置：售后服务管理&gt;索赔结算管理&gt;正负激励开票单维护
         <span style="color: red;">新增时请选择经销商</span>
</div>
<form method="post" name="fm" id="fm">
<table align="center" class="table_query">
	<tr align="center">
		<td align="right" nowrap="true">开票单号：</td>
		<td align="left" nowrap="true">
		 <input type="text" id="REMARK" name="REMARK" />
		</td>
		<c:if test="${type != 1}">
			<td align="right" nowrap="true">经销商代码：</td>
			<td align="left" nowrap="true">
			 <input type="text" id="dealerCode" name="dealerCode" onblur="getdealerId(this.value)"/>
			 <input type="hidden" id="dealerId" name="dealerId" />
			</td>
		</c:if>
		
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
			&nbsp;&nbsp;
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">
		var myPage;
		var url = "<%=contextPath%>/claim/application/DealerNewKp/delaerFineQuery.json";
		var title = null;
		
		var columns = [
					{header: "序号",renderer:getIndex,align:'center'},
					{header: "开票单号",dataIndex: 'REMARK',align:'center'},
					{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
					{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
					{header: "开票时间",dataIndex: 'CREATE_DATE',align:'center'},
					{header: "正负激励劳务费",dataIndex: 'PLUS_MINUS_LABOUR_SUM',align:'center'},	
					{header: "正负激励材料费",dataIndex: 'PLUS_MINUS_DATUM_SUM',align:'center'},
					{header: "总计(元)",dataIndex: 'AMOUNT_SUM',align:'center'},	
					{header: "操作",dataIndex: 'REMARK',align:'center', renderer:accAudut}
			      ];
	      
	//修改的超链接
	function accAudut(value,meta,record)
	{ 
	         if(record.data.IS_ROOD != 2)
	         {
	             return String.format("<a href='#' onclick=queryInfo01('"+record.data.REMARK+"')>[查看]</a><a href='#' onclick=queryInfo('"+record.data.REMARK+"','0') >[确认]</a><a href='#' onclick=queryInfo('"+record.data.REMARK+"','1') >[驳回]</a>");
	         }else
	         {
	         	 return String.format("<a href='#' onclick=queryInfo01('"+record.data.REMARK+"')>[查看]</a>");
	         }
			
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
	function queryInfo01(val)
	{
		fm.action = "<%=contextPath%>/claim/authorization/BalanceMain/queryFineInfo.do?id="+val;
		 fm.submit();
	}
	
	function queryInfo(val,type)
	{
			var url = '<%=contextPath%>/claim/authorization/BalanceMain/checkBalance.json?id='+val+'&type='+type ;
			makeNomalFormCall(url,function(json){
				MyAlert('确认成功'); 
				  __extQuery__(1);
			 },'fm');
	}
	//跳转到新增结算页面
	function forwordToAddPage()
	{
		var url = '<%=contextPath%>/claim/application/DealerNewKp/insertBalancefine.json' ;
		makeNomalFormCall(url,function(json){
				var msg=json.msg;
				if(msg == 'false')
				{
				   MyAlert('此经销商没有单独开票的正负激励！');
				}else
				{
				   MyAlert('单独开票成功！');
				}
			 },'fm');
	}
	
</script>
</body>
</html>
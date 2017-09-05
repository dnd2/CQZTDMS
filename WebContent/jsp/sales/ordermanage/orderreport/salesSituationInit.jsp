<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>

<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预售车辆登记</title>
<script type="text/javascript">
function doInit(){
   __extQuery__(1);
   loadcalendar(); 
}   
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：经销商实销管理 > 客户信息管理 > 预售车辆登记 </div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align=center width="95%">
	 <tr>
	    <td width="37%" align="right" nowrap>   </td>
	    <td width="43%" class="table_query_2Col_input" nowrap>
	        <span id="data_start" class="innerHTMLStrong"></span>
			<span id="data_end"   class="innerHTMLStrong"></span>
        </td>
	    <td width="20%" align=left nowrap>&nbsp;</td>
    </tr> 
	<tr>
	  <td align="right">制单时间：</td>
      <td colspan="2">
			<div align="left">
           		<input name="startDate" id="t1" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);" />
           		&nbsp;至&nbsp;
           		<input name="endDate" id="t2" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);" />
          		</div>	
		</td>
		<td align=left nowrap>&nbsp;</td>
	</tr>
	<tr>
      <td align="right" nowrap></td>
      <td align="right" nowrap></td>
      <td align="left" nowrap></td>
    </tr>
	<tr>
      <td align="right" nowrap>&nbsp;</td>
	   <td align="center" nowrap>
	   	<input id="queryBtn" name="button22" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">
	   	<input id="addBtn" name="button22" type=button class="cssbutton" onClick="loginAdd();" value="新增">
	   </td>
	   <td></td>
	</tr>
  </table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript">
	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/SalesSituation/salesSituationQuery.json";
				
	var title = null;

	var columns = [
				{header: "单据编码", dataIndex: 'SITUATION_NO', align:'center'},
				{header: "制单时间", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "单据状态", dataIndex: 'CODE_DESC', align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'SITUATION_ID',renderer:myLink ,align:'center'}
		      ];		         
	
	//修改的超链接
	function myLink(value,meta,record){
		var data = record.data;
		var ret = "";
		if(record.data.STATE == '<%=Constant.FORECAST_STATUS_UNCONFIRM%>'){
			ret += "<a href='#' onclick='loginMod(\""+ value +"\")'>[修改]</a>"
			ret += "<a href='#' onclick='confirmDel(\""+ value +"\")'>[删除]</a>";
			ret += "<a href='#' onclick='confirmSubmit(\""+ value +"\")'>[提报]</a>";
		}
		
		ret += "<a href='#' onclick='confirmInfo(\""+ value +"\")'>[查看]</a>";
  		return String.format(ret);
	}
	
	function loginAdd(){
		$('fm').action = '<%=request.getContextPath()%>/sales/ordermanage/orderreport/SalesSituation/salesSituationAddInit.do';
	 	$('fm').submit();
	}
	
	function loginMod(arg){
		$('fm').action = '<%=request.getContextPath()%>/sales/ordermanage/orderreport/SalesSituation/salesSituationModInit.do?situationId='+arg;
	 	$('fm').submit();
	}
	
	function confirmInfo(arg){
		$('fm').action = '<%=request.getContextPath()%>/sales/ordermanage/orderreport/SalesSituation/salesSituationQueryInfo.do?situationId='+arg;
	 	$('fm').submit();
	}
	
	//删除方法：
	function confirmDel(arg){
		MyConfirm("确认删除？",del,[arg]);
	}  
	//删除
	function del(arg){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/SalesSituation/salesSituationDel.json?situationId='+arg,delBack,'fm');
	}
	//删除回调方法：
	function delBack(json) {
		if(json.returnValue == '1') {
			MyAlert("删除成功！");
			__extQuery__(1);
		} else {
			MyAlert("删除失败！请联系管理员！");
		}
	} 
	
	
	function confirmSubmit(arg){
		MyConfirm("是否确认提交?",orderSubmit,[arg]);
	}
	
	function orderSubmit(arg){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/SalesSituation/salesSituationSubmit.json?situationId='+arg,showResult,'fm');
	}
	
	function showResult(json){
		if(json.returnValue == '1'){
			__extQuery__(1);
		}else{
			MyAlert("提交失败！请联系管理员！");
		}
	}
</script>
</body>
</html>

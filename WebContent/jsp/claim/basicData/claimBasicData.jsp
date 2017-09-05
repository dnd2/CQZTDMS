<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.TtAsWrLabourPricePO"%>
<%@page import="com.infodms.dms.util.StringUtil"%>
<%
	String contextPath = request.getContextPath();
	List<TtAsWrLabourPricePO> lists = (List<TtAsWrLabourPricePO>)request.getAttribute("lists");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔工时单价设定</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body onload="success();">
<div class="wbox">
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;索赔工时单价设定</div>
<form name='fm' id='fm' method="post">
<div class="form-panel">
		<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
<table class="table_query" >
	<tr>
 		<td style="text-align:right">经销商代码：</td>
 		<td>
 			<textarea cols="50" rows="2" id="dealer_code" readonly="readonly" name="dealer_code" onclick="showOrgDealer('dealer_code','dealer_id','true','','true','','10771002');"></textarea>&nbsp;
 			<input type="hidden" name="dealer_id" id="dealer_id"/>
 			<input  type="hidden" id = "success"  value="${success }"/>
 			<!-- <input type="button" value="..." class="mini_btn" onclick="showOrgDealer('dealer_code','dealer_id','true','','true','','10771002');"/>&nbsp; -->
 			<input type="button" value="清除" class="normal_btn" onclick="wrapOut();"/>
 		</td>
 		<td style="text-align:right">经销商名称：</td>
		<td><input type="text" class="middle_txt" name="dealer_name"/></td>
	</tr>
	<tr>
 		<td colspan="4" style="text-align:center">
 			<input class="normal_btn" type="button" name="queryBtn" id="queryBtn" value="查询" onclick="mainQuery();"/> 
 			&nbsp;&nbsp;
 			<input class="normal_btn" type="button" onclick="goAdd();" value="新增"/>
 			&nbsp;&nbsp;
 			<input class="normal_btn" type="button" onclick="goImport();" value="批量导入"/>
 		</td>
	</tr>
</table>
</div>
</div>
<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />

</form>
</div>
<script type="text/javascript">
	var myPage;
	var url = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/laborPriceMainQuery.json' ;
	
	var title = null ;

	var columns = [
	               {header:'序号',width:'8%',align:'center',renderer:getIndex},
	               {header:'操作',width:'8%',align:'center',renderer:myHandler},
	               {header:'经销商代码',width:'12%',align:'center',dataIndex:'DEALER_CODE'},
	               {header:'经销商名称',width:'14%',align:'center',dataIndex:'DEALER_NAME'},
	                <%
              		TtAsWrLabourPricePO po = null ;
              		for(int i=0;i<lists.size();i++){
              			po = lists.get(i);
              			if(i == lists.size() - 1) {
             	    %>
              		{id:'action',header:'<%=po.getSeriesCode()%>',width:'10%',align:'center',dataIndex:'<%=StringUtil.fmtSpecialStr(po.getSeriesCode())%>'}
              		<%
              			} else {
              		%>
              		{id:'action',header:'<%=po.getSeriesCode()%>',width:'10%',align:'center',dataIndex:'<%=StringUtil.fmtSpecialStr(po.getSeriesCode())%>'},
              		<%		
              			}
              		}
              		%>
		           	];
	function mainQuery(){
		__extQuery__(1);
	}
	function goAdd(){
		location = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/labourPriceAddInit.do' ;
	}
	function goImport(){
		location = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/inportPer.do' ;
	}
	function wrapOut(){
		document.getElementById("dealer_code").value = '' ;
		document.getElementById("dealer_id").value = '' ;
	}
	function myHandler(value,meta,record){
		var did = record.data.DEALER_ID ;
		//return '<a  href="<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/labourPriceUpdateInit.do?did='+did+'">[修改]</a>' ;
	 return String.format(
         "<a href=\"<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/labourPriceUpdateInit.do?did="
			+ did + "\">[修改]</a><a  href=\"#\" onclick='sel(\""+did+"\")'>[删除]</a>");
	
	}
	function sel(str){
	MyConfirm("是否确认删除？",del,[str]);
}  
//删除
function del(str){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/labourPriceDelete.json?did='+str,delBack,'fm','');
}
//删除回调方法：
function delBack(json) {
	if(json.success != null && json.success == "true") {
		MyAlert("删除成功！");
		__extQuery__(1);
	} else {
		MyAlert("删除失败！请联系管理员！");
	}
}
function success(){
var tis = document.getElementById("success").value;
if(tis!=null&&tis!=""){
MyAlert(tis);
}
}
</script>
</body>
</html>

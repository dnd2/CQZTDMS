<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
	List claimBasicList = (List)request.getAttribute("CLAIMBASIC");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔基本参数设定</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="wbox">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;索赔配件加价率设定</div>


  <form name='fm' id='fm'>
  <div class="form-panel">
		<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">


  <table class="table_query">

       <tr>            
        <td class="table_query_2Col_label_5Letter" style="text-align:right">经销商代码：</td>            
        <td>
		<input type="text" class="middle_txt" readonly="readonly" rows="2" cols="53" id="dealerCode" name="dealerCode"  onclick="showOrgDealer('dealerCode','','true','','true','','10771002');"/>
		     <!-- <input name="button1" type="button" class="mini_btn" style="cursor: pointer;"  onclick="showOrgDealer('dealerCode','','true','','true','','10771002');" value="..." />         -->
		     <input name="button2" type="button" class="normal_btn" onclick="clr();" value="清除"/>
        </td>   
        <td class="table_query_2Col_label_5Letter" style="text-align:right">经销商名称：</td>
        <td><input type="text" name="DEALER_NAME" id="DEALER_NAME"  class="middle_txt" value=""/></td>     
        <td class="table_query_3Col_label_6Letter" style="text-align:right">配件加价率(%)：</td>
        <td><input type="text" name="parameter_value" id="parameter_value" class="middle_txt" /></td>       
       </tr>
	   <tr>
             <td colspan="6" style="text-align:center">
             	<input name="queryBtn" class="normal_btn" id="queryBtn" type="button"  value="查询"  onclick="__extQuery__(1)"/>
				<input class="normal_btn" type="button" id="addBtn" name="button1" value="新增"  onclick="add();"/>
				<input class="normal_btn" type="button" id="beanchBtn" name="button1" value="批量修改"  onclick="basicDataMod();"/>
				加价率：<input class="middle_txt" style="width:50px" id ="addPrice" type="text" name="addPrice" value=""  />
			<!--  	东安：<input dataType = "0,isMoney_4,9" class="little_txt"  id ="addPriceD" type="text" name="addPriceD" value=""  />-->
				<span style="color:red">注：批量修改,在此输入加价率</span>
			</td>
       </tr>
 	</table>
 	</div>
 	</div>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</div>
  <!--分页 end -->
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/ClaimBasicParamsMain/claimBasicParmsQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
	{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"recesel\")' />全选", align:'center',sortable:false, dataIndex:'DEALER_CODE',width:'2%',renderer:checkBoxShow},
	{header: "操作",sortable: false,dataIndex: 'DEALER_CODE',renderer:myLink ,align:'center'},			
	{header: "经销商代码",sortable: false,dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",sortable: false,dataIndex: 'DEALER_SHORTNAME',align:'center'},				
       <% for(int i=0;i<claimBasicList.size();i++){ 
    	 HashMap temp = (HashMap)claimBasicList.get(i);
		  %>
		  	{header: "<%=temp.get("CODE_DESC")%>",sortable: false,dataIndex: "<%=temp.get("CODE_ID")%>",align:'center'}
		  <%
   			 }	%>	
				
		      ];

//设置超链接  begin      
	
	//修改的超链接设置
	function myLink(value,meta,record){
		return String.format("<a  href='#' onclick='updateIt(\"" + value + "\")'>[修改]</a>");
	}
	
	function checkBoxShow(value,meta,record){
	return String.format("<input type='checkbox' id='recesel'  name='recesel' value='" + record.data.DEALER_CODE + "' />");
}
	
	function updateIt(value) {
		var url = "<%=contextPath%>/claim/basicData/ClaimBasicParamsMain/claimBasicParmsUpdateInit.do?DEALER_CODE=" + value ;
		OpenHtmlWindow(url, 650, 380, '服务中心配件加价率修改');
		
		/* fm.action = url ;
		fm.submit() ; */
	}
function basicDataMod() {
	var allChecks = document.getElementsByName("recesel");
	var price = document.getElementById("addPrice").value;
	//var price2 = document.getElementById("addPriceD").value;
	var allFlag = false;
	var ids="";
	var reg = /^(\d+\.\d{1,2}|\d+)$/;
	for(var i = 0;i<allChecks.length;i++){
		if(allChecks[i].checked){
			allFlag = true;
			ids = allChecks[i].value+","+ids;
		}
	}
	if(ids!=""){
	ids=ids.substring(0,ids.length-1);
	}
	if(allFlag){
	if(price==""){
		MyAlert("请输入配件加价率?");
		}else if(!reg.test(price)){
			document.getElementById("addPrice").value="";
			//$("addPriceD").value="";
			MyAlert("配件加价率:最多2位小数的数字！");
		}else{
			MyConfirm("确认批量审批?",changeSubmit,[ids,price,0]);
			}
	}else{
		MyAlert("请选择数据后再点击操作批量审核按钮！");
	}
}

function changeSubmit(ids,value,value2) {
	$("addBtn").disabled=true;
	$("beanchBtn").disabled=true;
	$("queryBtn").disabled=true;
	var url="<%=contextPath%>/claim/basicData/ClaimBasicParamsMain/claimBasicParmsUpdate2.json?codes="+ids+"&value="+value+"&values="+value2;
	makeNomalFormCall(url,showResult22,'fm');
}
function showResult22(json){
	var msg=json.msg;
	if(msg=='01'){
	document.getElementById("addPrice").value="";
	//document.getElementById("addPriceD").value="";
	$("addBtn").disabled=false;
	$("beanchBtn").disabled=false;
	$("queryBtn").disabled=false;
		MyAlert('批量修改成功');
		__extQuery__(1);
	}else{
		MyAlert('操作失败,请联系管理员');
	}
}
//设置超链接 end
  //新增
  function add(){
    fm.action ="<%=contextPath%>/claim/basicData/ClaimBasicParamsMain/claimBasicParmsAddInit.do";
    fm.submit();    
  }
//清除方法
 function clr() {
	document.getElementById('DEALER_NAME').value = '';
	document.getElementById('dealerCode').value = '';
  }
</script>  
  </body>
</html>

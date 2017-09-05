<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@page import="java.util.*"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>汇总出库根据配件代码批量修改供应商</title>
<% String contextPath = request.getContextPath();%>
<script type="text/javascript">
	function showList(obj,partCode,supply_code,supply_name){
		$("part_code").value=partCode;
		$("supply_code").value=supply_code;
		$("supply_name").value=supply_name;
		__extQuery__(1);
	}
</script>
</head>
<body >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;批量修改供应商</div>
 <form method="post" name ="fm" id="fm">
 <input type="hidden" name="part_code" id="part_code" value=""/>
 <input type="hidden" name="yieldly" id="yieldly" value="95411001" />
 <input type="hidden" name="type" id="type" value="modify" />
 <input type="hidden" name="is_code" id="is_code" value="10041001" />
 <input type="hidden" name="SUPPLY_CODE" id="supply_code" value="" />
 <input type="hidden" name="supply_name" id="supply_name" value="" />
 <input type="hidden" name="page_amount" id="page_amount" value="99999" />
 
 <table id="addOutPart" border="1" cellpadding="1" cellspacing="1" class="table_query" width="100%" style="text-align: center;">
 	<th colspan="13">
		<img class="nav" src="../jsp_new/img/subNav.gif"/>汇总配件信息
	</th>
 	<tr>
         <td nowrap='true' width="20%">配件代码</td>
         <td nowrap='true' width="20%">配件名称</td>
         <td nowrap='true' width="20%">供应商代码</td>
         <td nowrap='true' width="20%">供应商名称</td>
         <td nowrap='true' width="20%">实件库存数</td>
     </tr>
     <c:forEach var="p" items="${partList}">
     	<tr>
     		 <td nowrap='true' width="20%"><input type="hidden"  name="PART_CODE" value="${p.PART_CODE }"/>${p.PART_CODE }</td>
     		 <td nowrap='true' width="20%">${p.PART_NAME }</td>
     		 <td nowrap='true' width="20%">${p.SUPPLY_CODE }</td>
     		 <td nowrap='true' width="20%">${p.SUPPLY_NAME }</td>
     		 <td nowrap='true' width="20%"><a href="#" onclick="showList(this,'${p.PART_CODE }','${p.SUPPLY_CODE }','${p.SUPPLY_NAME }');">${p.RETRUN_AMOUNT }</a></td>
     	 </tr>
     </c:forEach>
</table>
 <table id="addOutPart" border="1" cellpadding="1" cellspacing="1" class="table_query" width="100%" style="text-align: center;">
 		<tr>
        	<td style="color: #252525;width: 115px;text-align: right">供应商编码： </td>
        	<td nowrap colspan="3">
         	<input id="mod_code" name="mod_code" value="" readonly="readonly" type="text" class="middle_txt" maxlength="25">
        	<input id="mod_name" name="mod_name" value="" readonly="readonly" type="text" class="middle_txt" maxlength="55" >
         	<input  name="addsupply" value="......" type="button" class="normal_btn" onclick="addSupply();" >
          	<input  id="mod" name="mod" value="批量修改" type="button" class="normal_btn" onclick="chackData();" >
	  		</td>
	  </tr>
</table>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
<br />
<script type="text/javascript">
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/oldPartReNameQuery.json";
				
   var title = null;

   var columns = [
   				{header: "序号",align:'center',renderer:getIndex},
   				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"recesel\")' />全选", align:'center',sortable:false, dataIndex:'DEALER_CODE',width:'2%',renderer:checkBoxShow},
  				{header: "配件代码", dataIndex: 'partCode', align:'center'},
  				{header: "配件名称", dataIndex: 'partName', align:'center'},
  				{header: "供应商代码", dataIndex: 'producerCode', align:'center'},
  				{header: "供应商名称", dataIndex: 'producerName', align:'center'},
  				{header: "服务站代码", dataIndex: 'dealerCode', align:'center'},
  				{header: "服务站名称", dataIndex: 'dealerName', align:'center'},
  				{header: "索赔单号",dataIndex: 'claimNo',align:'center'},
  				{header: "旧件数量", dataIndex: 'signAmount',align:'center'}
  		      ];
	
	function checkBoxShow(value,meta,record){
		var partCode = record.data.partCode;
	  	var claimId = record.data.claimId;
	  	var id= claimId+","+partCode;
		return String.format("<input type='checkbox' id='recesel'  name='recesel' value='" + id + "' />");
	}


	function modifyInfo(partCode,claimId){
		var is_code = $('is_code').value;
	  	fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/oldPartSupplyModFoward.do?partCode="+partCode+"&claimId="+claimId+"&is_code="+is_code;
     	fm.method="post";
     	fm.submit();
	}
	
	function clearInput(){
		$('dealerCode').value='';$('dealerId').value='';
	}

   //格式化时间为YYYY-MM-DD
   function formatDate(value,meta,record) {
	 if (value==""||value==null) {
		return "";
	 }else {
		return value.substr(0,10);
	 }
   }
   function addSupply(){
	   var part_code=document.getElementsByName("PART_CODE");
		var code=part_code[1].value;
		OpenHtmlWindow('<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/selectSupplierForward.do?partCode='+code+'&yieldly='+$("yieldly").value,800,430);	
	}
	function setSupplier(code,name){
		$('mod_code').value=code;
		$('mod_name').value=name;
	}
	
	function chackData(){
		if($('mod_code').value=="" || $('mod_name').value==""){
		MyAlert("请选择供应商!");
		return;
	}
	var temp =0 ;
	var allChecks = document.getElementsByName("recesel");
	for(var i = 0;i<allChecks.length;i++){
		if(allChecks[i].checked){
			temp++;
		}
	}
	if(temp==0){
	MyAlert("请选择需要修改的数据!");
	return ;
	}
	MyConfirm("确认批量修改?",changeSubmit,[]);
}
function changeSubmit() {
	$('mod').disabled=true;
	makeFormCall('<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/supplierSave.json?type=2',showResult,'fm');		
}
function showResult(json){
		if(json.success != null && json.success == "true"){
			MyAlert("修改成功,请等待页面刷新!");
			$('mod').disabled=false;
			window.location.reload(); 
		}else{
			MyAlert("修改失败，请联系管理员！");
		}
	}
</script>
</body>
</html>

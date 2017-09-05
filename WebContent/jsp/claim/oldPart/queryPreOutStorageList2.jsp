<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件出库</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;无旧件索赔新增</div>
  <form id="fm" name="fm">
  <input type="hidden" name="yieldly" id="yieldly" value="${yieldly }" />
  <input type="hidden" name="idStr" id="idStr" value="" />
    <table class="table_query">
       <tr>
	         <td class="table_query_3Col_label_5Letter">供应商简称： </td>
	         <td nowrap="nowrap">
	          <input id="supply_name" name="supply_name" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	         </td>
	           <td class="table_query_3Col_label_5Letter">供应商代码： </td>
	         <td nowrap="nowrap">
	          <input id="supply_code" name="supply_code" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	         </td>
       </tr>
       <tr>
	         <td class="table_query_3Col_label_5Letter">配件名称： </td>
	         <td nowrap="nowrap">
	          <input id="part_name" name="part_name" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	         </td>
	         <td class="table_query_3Col_label_5Letter">配件代码：</td>
	         <td nowrap="nowrap">
	            <input id="part_code" name="part_code" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	         </td>
       </tr>
       <tr>
         <td align="center" nowrap="nowrap" colspan="4">
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="__extQuery__(1);">
           <input type="button" onclick="history.back();" class="normal_btn" style="width=8%" value="返回"/>
         </td>
       </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
  
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<form name="form1" style="display:none">
  <table id="bt" class="table_list">
	  <tr>
        <th height="12" align="center">
         <input type="button" onclick="preChecked();" id="save_btn" class="normal_btn" style="width=8%" value="生成单据"/>
         &nbsp;&nbsp;
         <input type="button" onclick="history.back();" id="save_btn2" class="normal_btn" style="width=8%" value="返回"/></th>
	  </tr>
  </table>
</form>
<br>
<script type="text/javascript">
   document.form1.style.display = "none";

   var HIDDEN_ARRAY_IDS=['form1'];
   
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryNoPartStoreList.json";
				
   var title = null;
   

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{id:'action',header: "全选<input type='checkbox' id='checkAll' name='checkAll' onclick='selectAll(this,\"orderIds\")'>", width:'8%',sortable: false,dataIndex: 'ID',renderer:myCheckBox},
  				{header: "索赔单号", dataIndex: 'CLAIM_NO', align:'center',renderer:myLink1},
  				{header: "配件代码",dataIndex: 'DOWN_PART_CODE',align:'center',renderer:myLink2},
  				{header: "配件名称", dataIndex: 'DOWN_PART_NAME', align:'center',renderer:myLink3},
  				{header: "供应商代码",dataIndex: 'DOWN_PRODUCT_CODE',align:'center',renderer:myLink4},
  				{header: "供应商名称",dataIndex: 'DOWN_PRODUCT_NAME',align:'center',renderer:myLink5},
  				{header: "配件数量",dataIndex: 'QUANTITY',align:'center',renderer:myLink6},
  				{header: "备注",dataIndex: '',align:'center',renderer:myRemark}
  		      ];
  
	__extQuery__(1);
	 //全选checkbox
   function myCheckBox(value,metaDate,record){
   	  return String.format("<input type='checkbox' id='orderIds"+record.data.id+"' name='orderIds' value='" + value + "' />");
   }
	 function myLink1(value,metaDate,record){
    	return String.format( ""+value+"<input type='hidden'  id='claimNo"+record.data.ID+"' name='claimNo"+record.data.ID+"' value='"+value+"' />");
   }
  function myLink2(value,metaDate,record){
    	return String.format( ""+value+"<input type='hidden'  id='partCode"+record.data.ID+"' name='partCode"+record.data.ID+"' value='"+value+"' />");
   }
  function myLink3(value,metaDate,record){
    	return String.format( ""+value+"<input type='hidden'  id='partName"+record.data.ID+"' name='partName"+record.data.ID+"' value='"+value+"' />");
   }
  function myLink4(value,metaDate,record){
    	return String.format( ""+value+"<input type='hidden'  id='supplyCode"+record.data.ID+"' name='supplyCode"+record.data.ID+"' value='"+value+"' />");
   }
  function myLink5(value,metaDate,record){
    	return String.format( ""+value+"<input type='hidden'  id='supplyName"+record.data.ID+"' name='supplyName"+record.data.ID+"' value='"+value+"' />");
   }
  function myLink6(value,metaDate,record){
    	return String.format( ""+value+"<input type='hidden'  id='allAmount"+record.data.ID+"' name='allAmount"+record.data.ID+"' value='"+value+"' />");
   }
   //生成备注文本框
    function myRemark(value,metaDate,record){
   	  return String.format("<input type='text'  class='middle_txt' maxlength='20' id='remark"+record.data.ID+"' name='remark"+record.data.ID+"' value='' />");
   }
   //格式化时间为YYYY-MM-DD
   function formatDate(value,meta,record) {
	 if (value==""||value==null) {
		return "";
	 }else {
		return value.substr(0,10);
	 }
   }
   function doInit(){
	   loadcalendar();
   }

   //选中预检查
   function preChecked() {
   	var str="";
   	var chk = document.getElementsByName("orderIds");
   	var len = chk.length;
   	var cnt = 0;
   	for(var i=0;i<len;i++){    
   		if(chk[i].checked){    
   			str = chk[i].value+","+str; 
   			cnt++;
   		}
   	}
   	if(str!=""){
   	str = str.substring(0,str.length-1);
   	}
   	$('idStr').value=str;
   	if(cnt==0){
          MyAlert("请选择数据!");
          return;
       }else{
         var delUrl = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/checkPart.json";
	   		makeNomalFormCall(delUrl,refreshPage,'fm','');
       }
   }
   function refreshPage(json){
   if(json.note=="Y"){
   	MyAlert("一次只能针对一家供应商开单!");
   	return false;
   }else{
    MyConfirm("确认开单？",outOfStore,[]);
    }
   }
    function outOfStore(str){
     $('save_btn').disabled="disabled";
     $('save_btn2').disabled="disabled";
 	 var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/addNoPartNoticePer.json";
 	 makeNomalFormCall(url,afterCall,'fm','createOrdBtn');
   }
   //签收回调处理
   function afterCall(json){
   	var retCode=json.updateResult;
    if(retCode!=null&&retCode!=''){
      if(retCode=="updateSuccess"){
    	    MyAlert("开单成功!");
 		__extQuery__(1);
 		 $('save_btn').disabled=false;
 		 $('save_btn2').disabled=false;
      }else  {
    	    MyAlert("出库失败!请联系管理员!");
    	    $('save_btn').disabled=false;
    	    $('save_btn2').disabled=false;
     }
   }
  }
</script>
</body>
</html>
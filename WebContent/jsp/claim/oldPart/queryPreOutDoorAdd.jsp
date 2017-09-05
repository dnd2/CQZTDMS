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
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔旧件出门证新增</div>
  <form id="fm" name="fm">
  <input type="hidden" name="yieldly" id="yieldly" value="${yieldly }" />
    <table class="table_query">
       <tr>
	         <td class="table_query_3Col_label_5Letter">供应商简称： </td>
	         <td nowrap="nowrap" >
	          <input id="supply_name" name="supply_name" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	          <span style="color:red">*</span>
	         </td>
	          <td class="table_query_3Col_label_5Letter">车型代码：</td>
	         <td nowrap="nowrap">
	            <input id="model_code" name="model_code" value="" type="text" class="middle_txt" datatype="1,is_null,30">
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
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="checkDate();">
           <input type="button" onclick="backTo();" class="normal_btn" style="width=8%" value="返回"/>
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
         <input type="button" onclick="preChecked();" id="save_btn" class="long_btn" style="width=30%" value="生成出门证"/>
         &nbsp;&nbsp;
         <input type="button" onclick="backTo();" class="normal_btn" style="width=8%" value="返回"/></th>
	  </tr>
  </table>
</form>
<br>
<script type="text/javascript">
   document.form1.style.display = "none";

   var HIDDEN_ARRAY_IDS=['form1'];
   
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/outDoorAddQuery.json";
				
   var title = null;
   

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{id:'action',header: "全选<input type='checkbox' id='checkAll' name='checkAll' onclick='selectAll(this,\"orderIds\")'>", width:'8%',sortable: false,dataIndex: 'ID',renderer:myCheckBox},
  				{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
  				{header: "配件名称",dataIndex: 'PART_NAME',align:'center'},
  				{header: "供应商代码", dataIndex: 'SUPPLAY_CODE', align:'center'},
  				{header: "供应商名称",dataIndex: 'SUPPLAY_NAME',align:'center'},
  				{header: "出库件类型",dataIndex: 'OUT_PART_TYPE',align:'center',renderer:getItemValue},
  				{header: "车型",dataIndex: 'MODEL_CODE',align:'center'},
  				{header: "开票车型",dataIndex: 'MODEL_CODE2',align:'center',renderer:myModel},
  				{header: "出库数",dataIndex: 'OUT_TOTAL',align:'center'},
  				{header: "开票数",dataIndex: 'OUT_TOTAL',align:'center',renderer:myOutNum},
  				{header: "备注",dataIndex: '',align:'center',renderer:myRemark}
  		      ];

   //生成出库数文本框
    function myOutNum(value,metaDate,record){
   	  return String.format("<input type='text'  class='short_txt'  maxlength='10' id='outNum"+record.data.ID+"'   name='outNum"+record.data.ID+"'  value='"+value+"' /><span style='color:red'>*</span><input type='hidden'  class='short_txt' id='need"+record.data.ID+"' name='need"+record.data.ID+"'  value='"+record.data.OUT_TOTAL+"'/>");
   } 
   //生成车型输入文本框
    function myModel(value,metaDate,record){
   	  return String.format("<input type='text'  class='short_txt'  maxlength='10' id='modelCode"+record.data.ID+"'   name='modelCode"+record.data.ID+"'  value='"+value+"' /><span style='color:red'>*</span>");
   }
   //生成备注文本框
    function myRemark(value,metaDate,record){
   	  return String.format("<input type='hidden'  id='partCode"+record.data.ID+"' name='partCode"+record.data.ID+"' value='"+record.data.PART_CODE+"' /><input type='hidden'  id='partName"+record.data.ID+"' name='partName"+record.data.ID+"' value='"+record.data.PART_NAME+"' /><input type='text'  class='middle_txt' maxlength='30' id='remark"+record.data.ID+"' name='remark"+record.data.ID+"' value='' />");
   }
   //全选checkbox
   function myCheckBox(value,metaDate,record){
   	  return String.format("<input type='checkbox' id='orderIds' name='orderIds' value='" + value + "' /><input type='hidden'  class='middle_txt' maxlength='30' id='supplyCode"+record.data.ID+"' name='supplyCode"+record.data.ID+"' value='"+record.data.SUPPLAY_CODE+"' /><input type='hidden'  class='middle_txt' id='OUT_PART_TYPE"+record.data.ID+"' name='OUT_PART_TYPE"+record.data.ID+"' value='"+record.data.OUT_PART_TYPE+"' />");
   }
   //格式化时间为YYYY-MM-DD
   function formatDate(value,meta,record) {
	 if (value==""||value==null) {
		return "";
	 }else {
		return value.substr(0,10);
	 }
   }
   
   function checkDate(){
   var name = $('supply_name').value;
   if(name==""){
   	MyAlert("必须输入供应商!");
   	return false;
   }else{
   __extQuery__(1);
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
   	if(cnt==0){
          MyAlert("请选择要开票的配件！");
          return;
       }
       //else if(cnt>4){
       //   MyAlert("一次最多开4条数据！");
      //    return false;
      // }
	  MyConfirm("确认生成出门证？",outOfStore,[str.substring(0,str.length-1)]);
   }
   //出库操作
   function outOfStore(str){
     var selectIdStr=str;
     var idArr=selectIdStr.split(",");
     var url_str="?idStr="+selectIdStr;
     var reg = /^\d+$/;
     var total = 0;
     for(var i=0;i<idArr.length;i++){
     var outNum = document.getElementById('outNum'+idArr[i]).value;
     var need = document.getElementById('need'+idArr[i]).value;
     var modelCode = document.getElementById('modelCode'+idArr[i]).value;
     if(modelCode==""){
     	MyAlert("请输入开票车型!");
     	return false;
     }
     if(outNum==""){
        MyAlert("请输入开票数！");
    	return false;
       }
    if(!reg.test(outNum)){
    	MyAlert("出库数为数字且为正整数！");
		return false;
    }
   
	 if(parseInt(outNum)-parseInt(need)>0){
      	MyAlert("开票数不能大于出库数！");
 			return false;
      }
     }
     $('save_btn').disabled="disabled"
 	 var url= "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/outOfDoorAdd.json"+url_str;
 	 makeNomalFormCall(url,afterCall,'fm','createOrdBtn');
   }
   //签收回调处理
   function afterCall(json){
   	var retCode=json.updateResult;
    if(retCode!=null&&retCode!=''){
      if(retCode=="updateSuccess"){
    	    MyAlert("出门证生成成功!");
    	    if(json.yieldly==<%=Constant.PART_IS_CHANGHE_01%>){
    	    fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryOut.do";
    	    }else{
    	    fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryOut2.do";
    	    }
			fm.submit();
      }else if(json.msg!=null&&json.msg!=""){
      	MyAlert(json.msg);
      	 $('save_btn').disabled=false;
      }else {
    	    MyAlert("出门证生成失败!");
    	    $('save_btn').disabled=false;
     }
   }
  }

   function backTo(){
   var yieldly = $('yieldly').value;
	if(yieldly == '<%=Constant.PART_IS_CHANGHE_01 %>'){
	 fm.action= "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryOut.do";
	}else if(yieldly == '<%=Constant.PART_IS_CHANGHE_02 %>'){
	 fm.action= "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryOut2.do";
	 }
 	 fm.method="post";
     fm.submit();
   }
</script>
</body>
</html>
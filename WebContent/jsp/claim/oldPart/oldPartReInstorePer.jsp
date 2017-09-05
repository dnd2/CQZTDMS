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
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔旧件二次入库</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
  	  <input type="hidden" name="yieldly" id="yieldly" value="${yieldly }" />
  	  <input type="hidden" name="type" id="type" value="${type }" />
    <TABLE class="table_query">
       <tr>
         <td style="color: #252525;width: 115px;text-align: right">供应商简称：</td>
         <td><input id="supply_name" name="supply_name" value="" type="text" class="middle_txt" maxlength="25" ></td>
          <td style="color: #252525;width: 115px;text-align: right">供应商编码： </td>
         <td nowrap>
          <input id="SUPPLY_CODE" name="SUPPLY_CODE" value="" type="text" class="middle_txt" maxlength="25">
         </td>
       </tr>
       <tr>
         <td style="color: #252525;width: 115px;text-align: right">旧件代码：</td>
         <td><input id="part_code" name="part_code" value="" type="text" class="middle_txt" maxlength="25" ></td>
          <td style="color: #252525;width: 115px;text-align: right">旧件名称： </td>
         <td nowrap>
          <input id="part_name" name="part_name" value="" type="text" class="middle_txt" maxlength="25">
         </td>
       </tr>
        <tr>
         <td style="color: #252525;width: 115px;text-align: right">索赔单号：</td>
         <td><input id="claim_no" name="claim_no" value="" type="text" class="middle_txt" maxlength="25" ></td>
          <td style="color: #252525;width: 115px;text-align: right">旧件编号： </td>
         <td nowrap>
          <input id="barcode_no" name="barcode_no" value="" type="text" class="middle_txt" maxlength="25">
         </td>
       </tr>
        <tr>
         <td style="color: #252525;width: 115px;text-align: right">是否开票：</td>
         <td>
         	<select  class="short_sel" name="isInvoice" id="isInvoice" >
         		<option value="1">是</option>
         		<!-- <option value="0">否</option> -->
         	</select>
         </td>
          <td style="color: #252525;width: 115px;text-align: right">零件性质：</td>
          
         <td nowrap>
	         <script type="text/javascript">
				  genSelBoxExp("is_main_code",<%=Constant.RESPONS_NATURE_STATUS%>,"",true,"short_sel","","false",'');
				 </script>
	         </td>
         </td>
       </tr>
       <tr>
         <td align="center" colspan="4">
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="__extQuery__(1);">
           &nbsp;&nbsp;
         </td>
       </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form> 
<br>
<script type="text/javascript">
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/oldPartReInstoreQuery.json";
				
   var title = null;

   var columns = [
   				{header: "序号",align:'center',renderer:getIndex},
   				{header: "操作", dataIndex: 'id', align:'center',renderer:myLink},
  				{header: "是否补偿", dataIndex: 'id', align:'center',renderer:linkRadio},
  				{header: "是否主因件", dataIndex: 'isMainCode', align:'center',renderer:getItemValue},
  				{header: "配件代码", dataIndex: 'partCode', align:'center'},
  				{header: "配件名称", dataIndex: 'partName', align:'center'},
  				{header: "供应商代码", dataIndex: 'producerCode', align:'center'},
  				{header: "供应商名称", dataIndex: 'producerName', align:'center'},
  				{header: "索赔单号",dataIndex: 'claimNo',align:'center'},
  				{header: "编号", dataIndex: 'barcodeNo', align:'center'},
  				{header: "扣件原因", dataIndex: 'deductRemark', align:'center',renderer:getItemValue}
  		      ];
  		      __extQuery__(1);
  	function linkRadio(value,meta,record){
  		var str="<input type='radio' name='check' value='1'/>";
  		return String.format(str);
  	}
  	function myLink(value,meta,record){
  	var str="";
  		str="<a href='#' onClick='modifyInfo(\""+value+"\",\""+record.data.mainPartCode+"\",\""+record.data.isMainCode+"\");'>[二次入库]</a>";
  		return String.format( str);
	}

	function modifyInfo(id,mainPartCode,isMainCode){
		var ids=document.getElementsByName("check");
		var isCompensate="";
		for(var i=0;i<ids.length;i++){
			if(ids[i].checked){
				isCompensate+=ids[i].value;
			}
		}
		var alertMsg="确定二次入库？";
		if(isCompensate!=""){
			alertMsg+="并补偿服务站？";
		}
		MyConfirm(alertMsg,report,[id,mainPartCode,isMainCode]);
	}
	function report(id,mainPartCode,isMainCode){
		var ids=document.getElementsByName("check");
		var isCompensate="";
		for(var i=0;i<ids.length;i++){
			if(ids[i].checked){
				isCompensate+=ids[i].value;
			}
		}
		var url ="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/oldPartReInstoreSave.json?id="+id+"&isCompensate="+isCompensate+"&mainPartCode="+mainPartCode+"&isMainCode="+isMainCode;
    	makeNomalFormCall(url,afterCall,'fm','');
	}
 function afterCall(json){
	   if(json.succ!=null&&json.succ=="1"){
          MyAlert("二次入库成功!");
          __extQuery__(1);
	   }else{
	  	MyAlert("二次入库失败!");
	   }
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
</script>
</BODY>
</html>
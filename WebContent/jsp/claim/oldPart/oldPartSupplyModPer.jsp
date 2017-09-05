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
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔旧件供应商修改</div>
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
         <td><input id="claim_no" name="claim_no" value="" type="text" class="middle_txt" maxlength="25" >
         </td>
         <td align="right">
         	请选择服务站：
         </td>
         <td>
        	<input class="middle_txt" id="dealerCode"  name="dealerCode" type="text" readonly="readonly"/>
			<input class="middle_txt" id="dealerId"  name="dealerId" type="hidden"/>
            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','dealerId','true','','true','','10771002');" value="..." />        
            <input name="clrBtn" type="button" class="normal_btn" onClick="clearInput();" value="清除"/>  
         </td>
          <td style="color: #252525;width: 115px;text-align: right;display: none">旧件编号： </td>
         <td nowrap style="display: none"">
          <input id="barcode_no" name="barcode_no" value="" type="text" class="middle_txt" maxlength="25">
         </td>
       </tr>
       
        <tr>
         <td align="right">是否实物：</td>
         <td> 
         <select name="is_code" id="is_code" value="" onchange="findData();" class="short_sel">
			<option value="<%=Constant.IF_TYPE_YES%>">是</option>
			<option value="<%=Constant.IF_TYPE_NO%>">否</option>
			</select>
        </td>
 			
         <td style="color: #252525;width: 115px;text-align: right">入库时间：</td>
         <td align="left" nowrap="true">
			<input name="indate_s" type="text" class="short_time_txt" id="indate_s"  readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'indate_s', false);" />  	
             &nbsp;至&nbsp; <input name="indate_e" type="text" class="short_time_txt"  id="indate_e" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'indate_e', false);" /> 
		</td>	
       </tr>
       <tr>
         <td style="color: #252525;width: 115px;text-align: right">件类型：</td>
         <td colspan="3">
            <select id="part_type" name="part_type"  class="short_sel">
               <option value="">请选择</option>
               <option value="0">常规件</option>
               <option value="1">切换件</option>
            </select>
         </td>
       </tr>
       
       <tr>
         <td align="center" colspan="4">
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="findData();">
           &nbsp;&nbsp;
    	   <input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
         </td>
       </tr>
        <tr>
         <td align="center" colspan="4">
         <a style="color: red;">旧件供应商修改值大于库存值：含实物无返件的和切换件数量。</a>
         </td>
       </tr>
        <tr>
         <td style="color: #252525;width: 115px;text-align: right">供应商编码： </td>
         <td nowrap colspan="3">
          <input id="mod_code" name="mod_code" value="" readonly="readonly" type="text" class="middle_txt" maxlength="25">
         <input id="mod_name" name="mod_name" value="" readonly="readonly" type="text" class="middle_txt" maxlength="55" >
          <input  name="addsupply" value="......" type="button" class="normal_btn" onclick="addSupply('${bean.partCode }');" >
           <input  id="mod" name="mod" value="批量修改" type="button" class="normal_btn" onclick="chackData();" >
         </td>
         <td>有件数量：<span style="color: red;" id="sign_amount_text">${returnAmount }</span>
         		  &nbsp;|&nbsp;&nbsp;无件数量：<span style="color: red;" id="noReturn_text">${noReturnAmount }</span>
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
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/oldPartReNameQuery.json";
				
   var title = null;

   var columns = [
   				{header: "序号",align:'center',renderer:getIndex},
   				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"recesel\")' />全选", align:'center',sortable:false, dataIndex:'DEALER_CODE',width:'2%',renderer:checkBoxShow},
   				{header: "操作", dataIndex: 'id', align:'center',renderer:myLink},
  				{header: "配件代码", dataIndex: 'part_code', align:'center'},
  				{header: "配件名称", dataIndex: 'part_name', align:'center'},
  				{header: "供应商代码", dataIndex: 'supply_code', align:'center'},
  				{header: "供应商名称", dataIndex: 'supply_name', align:'center'},
  				{header: "服务站代码", dataIndex: 'dealer_code', align:'center'},
  				{header: "服务站名称", dataIndex: 'dealer_name', align:'center'},
  				{header: "索赔单号",align:'center',renderer:thisLink},
  				{header: "是否切换件", dataIndex: 'is_qhj', align:'center'},
  				{header: "旧件数量", dataIndex: 'all_amount',align:'center'}
  		      ];
   function findData(){
	   document.getElementById("sign_amount_text").innerHTML = '';
	   document.getElementById("noReturn_text").innerHTML = '';
	   makeNomalFormCall("<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/findAllSignNumSum.json",function(json){
			document.getElementById("sign_amount_text").innerHTML = json.returnAmount == null ? '0' : json.returnAmount;
			document.getElementById("noReturn_text").innerHTML = json.noReturnAmount == null ? '0' : json.noReturnAmount;
		},'fm');
		__extQuery__(1);
   }
  	function myLink(value,meta,record){
  	var partCode = record.data.part_code;
  	var claimId = record.data.claim_id;
  	var str="";
  		str="<a href='#' onClick='modifyInfo(\""+partCode+"\",\""+claimId+"\");'>[修改]</a>";
  		return String.format( str);
	}
	
	function thisLink(value,meta,record){
	  var claimNo = record.data.claim_no;
	  var str="";
	  str="<a href='#' onClick='LogInfo(\""+claimNo+"\");'>"+claimNo+"</a>";
  		return String.format( str);
	}
	
	function LogInfo(claimNo){
	var str = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/LogInfoByclaimNo.do?claimNo="+claimNo;
	   OpenHtmlWindow(str,800,400);
	    
	}
	
	function checkBoxShow(value,meta,record){
	var partCode = record.data.part_code;
  	var claimId = record.data.claim_id;
  	var id= claimId+";"+partCode;
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
   function doInit(){
	   loadcalendar();
   }
   function addSupply(code){
		OpenHtmlWindow('<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/selectSupplierForward.do?code='+code+'&yieldly='+$("yieldly").value,800,430);	
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
			__extQuery__(1);
		}else{
			MyAlert("修改失败，请联系管理员！");
		}
	}
</script>
</BODY>
</html>
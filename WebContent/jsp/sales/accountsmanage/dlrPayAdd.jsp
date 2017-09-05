<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.bean.AclUserBean"%>
<%@taglib uri="/jstl/cout" prefix="c" %>

<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<script type="text/javascript">
	var url = '<%=contextPath%>/sales/accountsmanage/DlrPayInquiry/dlrPayConfirm.json';
	
	function saveBack(json){
		goBack();	
	}
	
	function goConfirm(){	
	
		var dealerName = document.getElementById("dealerName").value;
		var ticketNumber = document.getElementById("ticketNumber").value;
		var howMuch = document.getElementById("howMuch").value;
		var payDate = document.getElementById("payDate").value;
		var accountsType = document.getElementById("accountsType").value;
		if(dealerName==null||dealerName==""){
			MyAlert("经销商名字为空");
			return;
		}
		else if(ticketNumber==null||ticketNumber==""){
			MyAlert("票号为空");
			return;
		}
		else if(accountsType==null||accountsType==''){
			MyAlert("账户类型为空");
			return;
		}
		else if(howMuch==null||howMuch==""){
			MyAlert("金额为空");
			return;
		}
		else if(payDate==null||payDate==""){
			MyAlert("付款日期为空");
			return;
		}		 
		else
		{
			if(!dateHandle(payDate)){
				sendAjax(url,saveBack,'fm');
			}
			//sendAjax(url,saveBack,'fm');
			//$('fm').submit();
		}
	 	
	}	
	
	function  dateHandle(date){
				
		var end_date = new Date(date.replace("-", "/").replace("-", "/"));
		var currentdate = new Date();		
		//var days = end_date.getTime() - currentdate.getTime();
		//var time = parseInt(days / (1000 * 60 * 60 * 24));
		
		if(end_date.getFullYear()>currentdate.getFullYear()){
			MyAlert("不能大于当前时间");
			return true;
		}
		else if(end_date.getMonth()>currentdate.getMonth()){
			MyAlert("不能大于当前时间");
			return true;
		}
		else if(end_date.getDate()>currentdate.getDate()){
			MyAlert("不能大于当前时间");
			return true;
		}
		return false;
	}
	
	function goBack(){
		window.location = "<%=contextPath%>/sales/accountsmanage/DlrPayInquiry/dlrPayInquiryInit.do";
	}
	
	//开票日期内容清空
	function clrTxt(valueId) {
		document.getElementById(valueId).value = '' ;
		document.getElementById('dealerName').value = '' ;
	}
	
	function muchInputChang(id){
		var y=document.getElementById(id).value;
		//c=y.toLowerCase();
		//document.getElementById(id).value=y.replace(/[^\d.]/g,''); 
		onlyNumber(document.getElementById(id));
	
	}
	
	function onlyNumber(obj){
		//得到第一个字符是否为负号
		var t = obj.value.charAt(0); 
		//先把非数字的都替换掉，除了数字和. 
		obj.value = obj.value.replace(/[^\d\.]/g,''); 
		//必须保证第一个为数字而不是. 
		obj.value = obj.value.replace(/^\./g,''); 
		//保证只有出现一个.而没有多个. 
		obj.value = obj.value.replace(/\.{2,}/g,'.'); 
		//保证.只出现一次，而不能出现两次以上 
		obj.value = obj.value.replace('.','$#$').replace(/\./g,'').replace('$#$','.');
		var strs= new Array(); //定义一数组 
	    strs= obj.value.split(".");	

		if(strs.length>1){
			if(strs[1].length>2){
				var last = strs[1].substring(0,2) ;
				obj.value =strs[0]+"."+last;
			}
		}
		
		//如果第一位是负号，则允许添加
		if(t == '-'){
			obj.value = '-'+obj.value;
		}
				
	}
</script>  

</head>
<body onunload="javascript:destoryPrototype();">
<div id="loader" style="position: absolute; z-index: 200; background-color: rgb(255, 204, 0); padding: 1px; top: 4px; left: 455px; display: none; background-position: initial initial; background-repeat: initial initial; "> 正在载入中... </div>

<title>经销商付款录入</title>
<div class="wbox">
	
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 财务管理 &gt; 账务收款 &gt; 经销商付款录入</div>
<form id="fm" name="fm" method="post">

<div id="invoiceInfoId">	
<div class="form-panel">
	<h2>经销商付款录入</h2>
	<div class="form-body">	
		<table class="table_query" class="right" id="ctm_table_id">
			<tbody>
					<tr>			         		               	
						<td class="right" >经销商名称：</td>
						<td colspan="1"  class="left">
							<input type="hidden"  name="dealerCode" size="15"  id="dealerCode" readonly="readonly"/>
		            		<input type="hidden"  name="dealerId" size="15"  id="dealerId" readonly="readonly"/>
		            		<input type="text"  name="dealerName" class="middle_txt" size="15"  id="dealerName" onclick="showOrgDealer('dealerCode', 'dealerId', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', 'dealerName');" readonly="readonly"/>
		        			<input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCode');"/>
						</td>
					</tr>
					
					<tr>
						<td class="right">票号：</td>
						<td class="left">
						<input name="ticketNumber" id="ticketNumber" maxlength="20" class="middle_txt" type="text" value=""/>
						</td>
					</tr>
											
				     <tr class="csstr">
				       <td class="right">账户类型：</td>
				       <td class="left">
					      <select name="accountsType" id="accountsType" class='u-select'>
						       <c:forEach items="${accountsTypeList}" var="accountsTypeList" >
						       		<option value="${accountsTypeList.TYPE_ID }">${accountsTypeList.TYPE_NAME}</option>
							   </c:forEach>
					      </select>
				   	   </td>
				    </tr>
				    
		    		<tr>
						<td class="right">金额：</td>
						<td class="left">
						<input name="howMuch" id="howMuch" class="middle_txt" type="text" value="" onkeyup="muchInputChang(this.id)"/>
						</td>
					</tr>
					
					<tr>
						<td class="right">付款日期：</td>
						<td>
						<input name="payDate"  id="payDate" value="${date }" type="text" class="middle_txt" onFocus="WdatePicker({el:$dp.$('payDate'), maxDate:'#F{$dp.$D(\'payDate1\')}'})" />
		      			<input  id="payDate1"  type="hidden"   />
						</td>
					</tr>
					
					<tr>
						<td class="right">备注：</td>
						<td class="left">
						<textarea name="remark" id="remark" class=" form-control" style="width:300px" rows="3" cols="30"></textarea>
						</td>
					</tr>
					
					<tr>
					    <td class="right"></td>
						<td class="left" >
							<input type="hidden" name=invoiveId id="invoiveId"/>
							<input type="button" class="u-button u-query"  value="确认" onclick="goConfirm();"/>
							<input type="button" class="u-button u-reset"  value="取消" onclick="goBack();"/>
						</td>
					</tr>
			</tbody></table>
		</div>
	 </div>
	</form>
	
	<form name="form1" id="form1">
		<table>
			<tr>
				<div id="paySaveMessage" style="color: red ; width:200px "></div>
			</tr>
		</table>
	</form>
	
	</div>
  
</body>
</html>

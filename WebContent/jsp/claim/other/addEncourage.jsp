<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>罚款作业</TITLE>
</HEAD>
<BODY>
<div class="navigation">
  <img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;特殊费用管理&gt;正负激励&gt;奖励</div>

<form method="post" name="fm" id="fm" >
<input type=hidden name="dealerId" id="dealerId" value="${dealerId }"/>
<input type=hidden name="flag" id="flag" value="1"/>
<table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">经销商代码：</td>
		<td align="left" style="width:245px">
			${dealerCode }
		</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">经销商名称：</td>
		<td align="left" style="width:245px">
			${dealerName }
		</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">厂家：</td>
		<td align="left" style="width:245px">
			<select style="width: 152px;" class="middle_txt" name="yieldly" id="yieldly">
				 <option value="" >
    				-请选择-
    			  </option>
	              <c:forEach var="Area" items="${Area}" >
 				  <option value="${Area.areaId}" >
    				<c:out value="${Area.areaName}"/>
    			  </option>
    			 </c:forEach>
             </select>
		</td>
	</tr>
	<tr>
	<td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">奖励日期：</td>
		<td align="left" style="width:245px">
			${encourageDate }
		</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">奖励金额：</td>
		<td align="left" style="width:245px">
		    <input type='text' datatype="0,is_yuan" maxlength="8" name='labour_sum' value="" id='labour_sum' class="short_txt" />元
		    <input type="hidden" name="labour_type"  value="94141001">
		</td>
		<td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">奖励人：</td>
		<td align="left" style="width:245px">
			${encourageMan }
		</td>
	</tr>
	<tr>
		<td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">奖励原因：</td>
		<td colspan="5">
		<input type="hidden"
			name="DEALER_ID" value="<%=request.getAttribute("DEALER_ID") %>" />
		<textarea rows="4" cols="100" name='FINE_REASON' datatype="0,is_null,100" id='FINE_REASON'></textarea>
			</td>
	</tr>
	<tr>
		<td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">备注：</td>
		<td colspan="5">
			<textarea rows="4" cols="100" name='remark' datatype="1,is_null,100" id='remark'></textarea>
		</td>
	</tr>
	<tr><td colspan="6">
        <input class="normal_btn"  name="add4" type="button" onclick="selectMainNew();" value ='关联通知' /></td>
    </tr>
    <tr>
      <td colspan="6">
      	<table class="table_list_line" id="t_news" border="1" width="100%">
	        <tr >
	          <th width="10%" align="center" nowrap="nowrap" >NO </th>
	          <th width="30%" align="center" nowrap="nowrap" >编码 </th>
	          <th width="50%" align="center" nowrap="nowrap" >新闻名称</th>
	          <th width="10%" align="center" nowrap="nowrap" >操作 </th>
	        </tr>
		</table>
		</td>
	</tr>
    	<tr>
			<td colspan="6" style="text-align: center;"><input type="button"  name="button" onclick="confirmAdd()" value="保存" class="normal_btn" />
			<input type="button" name="Submit3" value="返回" onClick="Back();"
			class="normal_btn"></td>
		</tr>
      </table>
</form>

<script type="text/javascript">
	function confirmAdd() {
	    var yieldly = document.getElementById('yieldly').value;	
	    var resion = document.getElementById('FINE_REASON').value; 
	    var labour_sum = document.getElementById('labour_sum').value; 
	 	 if(yieldly==''){
	 		MyAlert("请选择厂家！");
	 	}else if(labour_sum==''){
	 		MyAlert("请填写奖励金额！");
	 	}else if(resion==''){
	 		MyAlert("请填写奖励原因！");
	 	}else {
	 		if(submitForm('fm')){
	 			MyConfirm("是否保存？",confirmAdd0,[]);
	 		}
		}
	}
//确定按钮事件
function confirmAdd0() {
		var url=globalContextPath+"/claim/other/Bonus/punish.json?fine_type=80641002";
		sendAjax(url,callBackspecilAddInfo,'fm');  	
}
function callBackspecilAddInfo(json){
	var msg=json.msg;
	if(msg=="00"){
		MyAlertForFun("保存成功！",Back);
	}else{
		MyAlert("保存失败！");
	}	
}
function Back() {
		var fm = document.getElementById('fm');
		fm.action='<%=request.getContextPath()%>/claim/other/Bonus/bonusForward.do';
		fm.submit();
}

	 // 动态生成表格
 	function addRow(tableId,newId,newCode,newTitle){
 	
 	
	    var addTable = document.getElementById(tableId);
		var rows = addTable.rows;
		var length = rows.length;
		var insertRow = addTable.insertRow(length);


		insertRow.className = "table_list_row1";
		insertRow.insertCell(0);
		insertRow.insertCell(1);
		insertRow.insertCell(2);
		insertRow.insertCell(3);
			addTable.rows[length].cells[0].innerHTML =  '<td>'+length+'</td>';
			addTable.rows[length].cells[1].innerHTML =  '<td><a href="#" onclick="viewNews('+newId+')">'+newCode+'</a><input type=hidden name="newsId" value="'+newId+'"/></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td>'+newTitle+'</td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><input class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRowConfirm(this);" /></td>';
			
			return addTable.rows[length];
		}
	//选择新闻窗口
		function selectMainNew(){
			OpenHtmlWindow('<%=contextPath%>/claim/other/Bonus/newsQuery.do',800,500);
		}
		function deleteRowConfirm(obj){
			 var tabl=document.all['t_news'];
			 var index = obj.parentElement.parentElement.rowIndex;
			 
			 tabl.deleteRow(index); 
			 countSeq();
		}
		function countSeq(){
			 var table=document.all['t_news'];
		 
		 /*******beg重新定义序号*******/
		 var trs = table.getElementsByTagName('tr');//
		 var rowCount=trs.length;
		 var index=0;
		 for(var i = 1; i <rowCount ; i++){
			 var cells = trs[i].cells;
			 cells[0].innerHTML=++index;
		 }
		}
	function viewNews(value){
		OpenHtmlWindow("<%=contextPath%>/claim/basicData/HomePageNews/viewNews.do?comman=2&newsId="+value,800,500);
	}
</script>
</BODY>
</html>


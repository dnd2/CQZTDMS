<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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
  <img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;其他功能&gt;罚款作业明细</div>

<form method="post" name="fm" id="fm" >
<input type=hidden name="dealerId" id="dealerId" value="${dealerId }"/>
<input type=hidden name="BALANCE_AMOUNT" id="BALANCE_AMOUNT" value="${AMOUNT }"/>
<input type=hidden name="OLD_DECODE" id="OLD_DECODE" value="${oldDecode }"/>
<input type=hidden name="DECUTE" id="DECUTE" value="${DECUTE }"/>
<input type=hidden name="flag" id="flag" value="${flag }"/>
<input type=hidden name="feeType" id="feeType" value="${feeType }"/>
<table class="table_edit">
	<tr>
		<td class="table_edit_1Col_label_5Letter">经销商代码：</td>
		<td align="left">
			${dealerCode }
		</td>
		<td class="table_edit_1Col_label_5Letter">经销商名称：</td>
		<td align="left">
			${dealerName }
		</td>
		<td class="table_edit_1Col_label_5Letter">厂家：</td>
		<td align="left">
			<script type="text/javascript">
			genSelBoxContainStr("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,'<%=CommonUtils.checkNull(request.getAttribute("yieldly"))%>',false,"short_sel","","false",'<%=CommonUtils.checkNull(request.getAttribute("yieldly"))%>');
		    </script><font color="red">*</font>
	

		</td>
	</tr>
	<tr>
	<td class="table_edit_1Col_label_5Letter">扣款日期：</td>
		<td align="left">
			${encourageDate }
		</td>
		<td class="table_edit_1Col_label_5Letter">扣款金额：</td>
		<td align="left"><input type='text' datatype="0,is_yuan"  name='FINE_SUM'  id='FINE_SUM' class="short_txt" />元</td>
		<td class="table_edit_1Col_label_5Letter">扣款人：</td>
		<td align="left">
			${encourageMan }
		</td>
	</tr>
	<tr>
		<td class="table_edit_1Col_label_5Letter">被扣款单据号:</td>
		<td align="left">${NO }<input type="hidden" name="documentsNo" id="documentsNo" value="${NO }"/><input type="hidden" name="id" id="id" value="${ID }"/></td>
		<td class="table_edit_1Col_label_5Letter">申请总金额：</td>
		<td align="left">${REPAIR_TOTAL}</td>
		<td class="table_edit_1Col_label_5Letter">结算总金额：</td>
		<td align="left">${AMOUNT }</td>
	</tr>
	<tr>
		<td class="table_edit_1Col_label_5Letter">旧件扣款金额:</td>
		<td align="left">${oldDecode }</td>
		<td class="table_edit_1Col_label_5Letter">二次复核金额：</td>
		<td align="left">${DECUTE}</td>

	</tr>
	<tr>
		<td class="table_edit_1Col_label_5Letter">罚款原因：</td>
		<td colspan="5">
		<textarea rows="2" cols="100" name='FINE_REASON' datatype="0,is_null,100" id='FINE_REASON'></textarea>
			</td>
	</tr>
	<tr>
		<td class="table_edit_1Col_label_5Letter">备 注：<input type="hidden"
			name="DEALER_ID" value="<%=request.getAttribute("DEALER_ID") %>" /></td>
		<td colspan="5">
			<textarea rows="2" cols="100" name='REMARK'  id='REMARK'></textarea>
		</td>
	</tr>
	<tr><td colspan="6">
        <input class="normal_btn"  name="add4" type="button" onclick="selectMainNew();" value ='新增' /></td>
    </tr>
    <tr>
      <td colspan="6">
      	<table class="table_list_line" id="t_news" border="1">
	        <tr >
	          <th width="50" align="center" nowrap="nowrap" >NO </th>
	          <th width="220" align="center" nowrap="nowrap" >编码 </th>
	          <th width="400" align="center" nowrap="nowrap" >新闻名称</th>
	          <th width="80" align="center" nowrap="nowrap" >操作 </th>
	        </tr>
		</table>
		</td>
	</tr>
</table>
 	<table border="0" class="table_edit">
    	<tr>
			<TD colspan="6" align="center"><input type="button"  name="button" onclick="confirmAdd()" value="确定" class="normal_btn" />
			<input type="button" name="Submit3" value="返回" onClick="history.back();"
			class="normal_btn"></td>
		</tr>
      </table>
</form>

<script type="text/javascript">
	function confirmAdd() {
	    var yieldly = $('yieldly');	
	    var resion = $('FINE_REASON'); 
	 	if (document.getElementById("FINE_SUM").value==0) {
	 		MyAlert("罚款金额不能为0！");
	 	} else if(!yieldly || !yieldly.value ||yieldly.value==''){
	 		MyAlert("扣款必须选择生产基地！");
	 	}else if(!resion || !resion.value ||resion.value==''){
	 		MyAlert("扣款必须填写扣款原因！");
	 	}else {
			confirmAdd0();
		}
	}
//确定按钮事件
function confirmAdd0() {
	if(submitForm('fm')){
	
			var balance = $('BALANCE_AMOUNT').value;
			var oldDecode = $('OLD_DECODE').value;
			var decode = $('DECUTE').value;
			var FINE_SUM = $('FINE_SUM').value;
			if(parseFloat(oldDecode)+parseFloat(decode)+parseFloat(FINE_SUM)>parseFloat(balance)){
			MyAlert("扣款总金额大于了结算总金额不能再进行扣款");
			}else{
				var fm = document.getElementById('fm');
					if(confirm("确定提交吗？")){
						fm.action='<%=request.getContextPath()%>/claim/application/ClaimManualAuditing/secondCheckSave.do';
						fm.submit();
					}
			}
		}
}
function changeOtherForeBack(json){
	if(json.success=='false'){
		MyAlert("操作成功！");
		 _hide();
	}
	else{
		MyAlert("失败");
	}
	
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
		window.open("<%=contextPath%>/claim/basicData/HomePageNews/viewNews.do?comman=2&newsId="+value,800,500);
	}
</script>
</BODY>
</html>


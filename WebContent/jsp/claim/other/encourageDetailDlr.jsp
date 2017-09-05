<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c" %>
<% String contextPath = request.getContextPath(); %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>罚款作业</TITLE>
</HEAD>
<BODY>
<div class="navigation">
  <img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;其他功能&gt;罚款作业明细</div>
<form method="post" name="fm" id="fm" >
<input type=hidden name="dealerId" id="dealerId" value="${mapEncourage.DEALER_ID }"/>
<input type=hidden name="yieldly" id="yieldly" value="${mapEncourage.YIELDLY }"/>
<input type=hidden name="fine_type" id="fine_type" value="${mapEncourage.FINE_TYPE }"/>
<input type=hidden name="fine_id" id="fine_id" value="${mapEncourage.FINE_ID }"/>
<table class="table_edit">
	<tr>
		<td class="table_edit_1Col_label_5Letter">经销商代码：</td>
		<td align="left">
			${mapEncourage.DEALER_CODE }
		</td>
		<td class="table_edit_1Col_label_5Letter">经销商名称：</td>
		<td align="left">
			${mapEncourage.DEALER_NAME }
		</td>
		<td class="table_edit_1Col_label_5Letter">厂家：</td>
		<td align="left">
			${mapEncourage.AREA_NAME }
		</td>
	</tr>
	<tr>
	<td class="table_edit_1Col_label_5Letter">奖惩日期：</td>
		<td align="left">
			${mapEncourage.FINE_DATE }
		</td>
		<td class="table_edit_1Col_label_5Letter">奖惩金额：</td>
		<td align="left">
		  ${mapEncourage.LABOUR_SUM } 元
		</td>
		<td class="table_edit_1Col_label_5Letter">奖惩人：</td>
		<td align="left">
			${mapEncourage.CREATE_BY }
		</td>
	</tr>
	<tr>
		<td class="table_edit_1Col_label_5Letter">类型：</td>
		<td align="left">
		  <script type='text/javascript'>
				       var activityType=getItemValue('${mapEncourage.FINE_TYPE}');
				       document.write(activityType) ;
				     </script>
		</td>		
	</tr>
	<tr>
		<td class="table_edit_1Col_label_5Letter">备注：</td>
		<td colspan="5">
			${mapEncourage.REMARK }
		</td>
	</tr>
	<tr>
		<td class="table_edit_1Col_label_5Letter">奖惩原因：</td>
		<td align="left">
			${mapEncourage.FINE_REASON}
	    </td>
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
	        <c:if test="${!empty listNews}">
	        	<c:forEach var="newDetail" items="${listNews}" varStatus="vs">
	        		<tr >
			          <th width="50" align="center" nowrap="nowrap" >${vs.index+1 } </th>
			          <th width="220" align="center" nowrap="nowrap" >${newDetail.NEWS_CODE } </th>
			          <th width="400" align="center" nowrap="nowrap" >${newDetail.NEWS_TITLE }</th>
			          <th width="80" align="center" nowrap="nowrap" ><a href="#" onclick='viewNews(${newDetail.NEWS_ID })'>查看</a></th>
			        </tr>
	        	</c:forEach>
	        </c:if>
		</table>
		</td>
	</tr>
    	<tr>
			<td colspan="6" style="text-align: center;">
			<input type="button" name="Submit3" value="关闭" onClick="_hide();" class="normal_btn"></td>
		</tr>
      </table>
</form>

<script type="text/javascript">
	function confirmAdd() {
	    var yieldly = $('yieldly');	
	 	if (document.getElementById("FINE_SUM").value==0) {
	 		MyAlert("罚款金额不能为0！");
	 	} else if(!yieldly || !yieldly.value ||yieldly.value==''){
	 		MyAlert("奖惩必须加入产地！");
	 	}else {
			MyConfirm("是否增加？",confirmAdd0,[]);
		}
	}
	function modify(){
		if(submitForm('fm')){
			 var yieldly = $('yieldly');	
			 	if (document.getElementById("FINE_SUM").value==0) {
			 		MyAlert("罚款金额不能为0！");
			 	} else if(!yieldly || !yieldly.value ||yieldly.value==''){
			 		MyAlert("奖惩必须加入产地！");
			 	}else {
					var fm = document.getElementById('fm');
					fm.action='<%=request.getContextPath()%>/claim/other/Bonus/punishModify.do';
					fm.submit();
				}
	
			}
	}
//确定按钮事件
function confirmAdd0() {
	if(submitForm('fm')){
		var fm = document.getElementById('fm');
		fm.action='<%=request.getContextPath()%>/claim/other/Bonus/punish.do';
		fm.submit();
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
			addTable.rows[length].cells[0].innerHTML =  '<td>'+length+'<input type=hidden name="newsId" value="'+newId+'"/></td>';
			addTable.rows[length].cells[1].innerHTML =  '<td>'+newCode+'</td>';
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


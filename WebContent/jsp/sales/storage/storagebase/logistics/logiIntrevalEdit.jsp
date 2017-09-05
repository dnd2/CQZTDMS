<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>区间编辑</title>
<script type="text/javascript">
	
	function ok(){
		var beginNum = document.getElementsByName("beginNum");
		if(beginNum.length <= 0) {
			MyAlert("请添加计算规则!");
			return;
		}
		MyConfirm("确定修改?",okcommit);
	}
	
	function okcommit(){
		document.getElementById("queryBtn").disabled = true;
		var url = "<%=contextPath%>/sales/storage/storagebase/LogisticsManage/logiIntrevalUpdate.json";
		makeFormCall(url,resultFunc,"fm");
	}
	
	function resultFunc(json) {
		if(json.message == "00") {
			parent.MyAlert("操作成功!");
			cancel();
		} else {
			MyAlert("操作失败,请联系管理员!");
			document.getElementById("queryBtn").disabled = false;
		}
	}
	
	function cancel(){
		location.href = "<%=contextPath%>/sales/storage/storagebase/LogisticsManage/editLogiIntervalInit.do";
	}
	
	function getSelectedText(name){
		var obj=document.getElementById(name);
		for(i=0;i<obj.length;i++){
		   if(obj[i].selected==true){
		    return obj[i].innerText;      //关键是通过option对象的innerText属性获取到选项文本
		   }
		}
	}
	
	//添加
	function addRow(){
		if(!submitForm("fm"))return;
		var beginNum = document.getElementById("addBeginNum").value;
		var endNum = document.getElementById("addEndNum").value;
		if(parseFloat(endNum) < parseFloat(beginNum)){
			showTip(document.getElementById("addEndNum"),"结束数量不能小于开始数量!",getTip());
			return;
		}
		var assDays = document.getElementById("addAssDays").value;
		var table = document.getElementById('tbody');
		var row=table.insertRow(0);
		row.className="table_list_row1";
		var cel1=row.insertCell(0);
		var cel2=row.insertCell(1);
		var cel3=row.insertCell(2);
		var cel4=row.insertCell(3);
		cel1.innerHTML = beginNum + "<input type='hidden' name='beginNum' value='"+beginNum+"' />";
		cel2.innerHTML = endNum + "<input type='hidden' name='endNum' value='"+endNum+"' />";
		cel3.innerHTML = assDays + "<input type='hidden' name='assDays' value='"+assDays+"' />";
		cel4.innerHTML ="<input type='button'  onclick='delt(this)' class=normal_btn value='删除' />";
		
		document.getElementById("addBeginNum").value = "";
		document.getElementById("addEndNum").value = "";
		document.getElementById("addAssDays").value = "";
		document.getElementById("addBeginNum").focus();
	}
	
	//删除
	function delt(obj){
		var table  = document.getElementById('tbody');
		var tr = obj.parentNode.parentNode;
		var trs = table.rows;
		for (i = 0; i < trs.length; i++){
			if (trs[i]==tr){
				table.deleteRow(i);
			}
		}
	}
</script>
</head>
<body>
<div class="wbox">
	 <div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置:储运管理>储运基础数据>装配天数设置</div>
	 <form method="post" name="fm" id="fm">
	 	<table class="table_query" bordercolor="#DAE0EE">
	 		<tr>
	 			<td class="right">开始数量：</td>
	 			<td align="left"><input type="text" id="addBeginNum" name="addBeginNum" datatype="0,is_digit,12"/></td>
	 			<td class="right">结束数量：</td>
	 			<td align="left"><input type="text" id="addEndNum" name="addEndNum" datatype="0,is_digit,12"/></td>
	 			<td class="right">装配天数：</td>
	 			<td align="left"><input type="text" id="addAssDays" name="addAssDays"  datatype="0,is_digit,12"/></td>
	 			<td align="center"><input class="normal_btn" type="button" value="添加" name="add" id="add" onclick="addRow();"/></td>
	 		</tr>
	 		<tr>
	 			<td colspan="7" align="left"><font color="red">*注意:计算规则为大于开始数量，小于等于结束数量</font></td>
	 		</tr>
	 	</table>
	 	<table style="border-bottom: #dae0ee 1px solid" class=table_list>
	 		<tr class=csstable>
	 			<th align="center">开始数量</th>
	 			<th align="center">结束数量</th>
	 			<th align="center">装配天数</th>
	 			<th align="center">操作</th>
	 		</tr>
	 		<tbody id="tbody">
	 			<c:if test="${rInterval != null }">
	 				<c:forEach var="ri" items="${rInterval }">
	 					<tr class=table_list_row1>
	 						<td align="center">${ri.BEGIN_NUM }<input type="hidden" name="beginNum" value="${ri.BEGIN_NUM }" /></td>
	 						<td align="center">${ri.END_NUM }<input type="hidden" name="endNum" value="${ri.END_NUM }" /></td>
	 						<td align="center">${ri.ASS_DAYS }<input type="hidden" name="assDays" value="${ri.ASS_DAYS }" /></td>
	 						<td align="center"><input type="button"  onclick="delt(this)" class=normal_btn value="删除" /></td>
	 					</tr>
	 				</c:forEach>
	 			</c:if>
	 		</tbody>
	 	</table>
	 	<table class="table_query" bordercolor="#DAE0EE">
		    <tr>
		      <td align="center" colspan="6">
		      	<input class="normal_btn" type="button" value="保存" name="btnOk" id="queryBtn" onclick="ok();">
		      </td>
		    </tr>
	 	</table>
	 </form>
 </div>
</body>
</html>
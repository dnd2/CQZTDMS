<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="com.infodms.dms.po.TtDealerActualSalesPO"%>
<%@ page import="com.infodms.dms.po.TtCustomerPO"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
	}

</script>

<title>实销信息上报</title>
</head>
<body> 
<div class="wbox">
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
	<table class="table_list" id="file">
    <tr class= "tabletitle">
      <td align = "left" colspan="3"><img class="nav" src="<%=contextPath%>/img/nav.gif" />其他联系人信息 <a href="#" onclick="addTblRow();"> <img src="<%=contextPath%>/img/add.png" alt="新增" width="16" height="16" align="absmiddle"  onclick="addLinkman();"/></a></td>
    </tr>
    <tr>
      <th width="13%" nowrap="nowrap" >姓名</th>
      <th width="14%" nowrap="nowrap" >主要联系电话</th>
      <th width="16%" nowrap="nowrap" >其他联系电话</th>
      <th width="45%" nowrap="nowrap" >联系目的</th>
      <th width="12%" nowrap="nowrap" >操作</th>
    </tr>
    <c:forEach items="${linkmanList}" var="linkmanList" varStatus="vstatus">
		<tr id="linkmanList${vstatus.index }" class="<c:if test='${vstatus.index%2==0}'>table_list_row1</c:if><c:if test='${vstatus.index%2!=0}'>table_list_row2</c:if>">
			<td><div align="center">${linkmanList.NAME}</div></td>
			<td><div align="center">${linkmanList.MAIN_PHONE}</div></td>
			<td><div align="center">${linkmanList.OTHER_PHONE}</div></td>
			<td><div align="center">${linkmanList.CONTRACT_REASON}</div></td>
			<td><div align="center"><input class="normal_btn" type="button" value="删除" width="80%" onclick="deleteOtherLink(${linkmanList.LM_ID })" /></div></td>
		</tr>
	</c:forEach>
  </table>
</form>
</div>

<script type="text/javascript">

	function addTblRow() {
		var tbl = document.getElementById('file');
		var rowObj = tbl.insertRow(tbl.rows.length);
		rowObj.className  = "table_list_row2";
		var cell1 = rowObj.insertCell(0);
		var cell2 = rowObj.insertCell(1);
		var cell3 = rowObj.insertCell(2);
		var cell4 = rowObj.insertCell(3);
		var cell5 = rowObj.insertCell(4);
		
		cell1.innerHTML = '<TD align="center"><div align="center"><input type="text" maxlength="20" name="linkMan_name" style="width: 80%" /><font color="#FF0000">*</font></div></TD>';
		cell2.innerHTML = '<TD align="center"><div align="center"><input type="text" maxlength="15" name="linkMan_main_phone" style="width: 70%" /><font color="#FF0000">*</font></div></TD>';
		cell3.innerHTML = '<TD align="center"><div align="center"><input type="text" maxlength="15" name="linkMan_other_phone" style="width: 80%" /></div></TD>';
		cell4.innerHTML = '<TD align="center"><div align="center"><input type="text" maxlength="200" name="linkMan_contract_reason" style="width: 80%" /></div></TD>';
		cell5.innerHTML = '<TD align="center"><div align="center"><input class="normal_btn" type="button" value="删除" width="80%" onclick="deleteTblRow(this);" /></div></TD></TR>';
	}
	function deleteTblRow(obj) {
		var idx = obj.parentElement.parentElement.parentElement.rowIndex;
		var tbl = document.getElementById('file');
		tbl.deleteRow(idx);		
	}
	function deleteOtherLink(lm_id){
		MyConfirm("是否删除?",deleteOtherLinkAction,[lm_id]);
	}
	function deleteOtherLinkAction(lm_id){
		makeNomalFormCall('<%=contextPath%>/sales/customerInfoManage/SalesReport/delOtherLinkMan.json?lm_id='+lm_id,showResult,'fm');
	}

	function showResult(json){
		queryOtherLinkList();
	}
	
	function queryOtherLinkList() {
		//parent.$('inIframe').contentWindow.queryOtherLinkMan();
		window.parent.queryOtherLinkMan();
	}
	function getlinkMan_names(){
		var linkMan_names = document.getElementsByName("linkMan_name");
		return linkMan_names;
	}
	function getlinkMan_main_phones(){
		var linkMan_main_phones = document.getElementsByName("linkMan_main_phone");
		return linkMan_main_phones;
	}
	function getlinkMan_other_phones(){
		var linkMan_other_phones = document.getElementsByName("linkMan_other_phone");
		return linkMan_other_phones;
	}
	function getlinkMan_contract_reasons(){
		var linkMan_contract_reasons = document.getElementsByName("linkMan_contract_reason");
		return linkMan_contract_reasons;
	}
</script>    
</body>
</html>
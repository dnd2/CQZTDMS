<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>接口收发明细查询</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body>
<div class="navigation">
	<img src="<%=contextPath %>/img/nav.gif" />&nbsp;接口收发明细查询
</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2>接口收发明细查询</h2>
	<div class="form-body">
  	<table class="table_query">
	<tr>	
		<td class="right">接口表：</td>
		<td>
		    <input name="tableName" id="tableName" type="hidden" value="" />
		    <textarea rows="tableDesc" cols="20"  name="tableDesc" id ="tableDesc" readonly></textarea>
		    <input name="search" id="search" class="normal_btn"  onclick="querySet();" type="button" value="选择" />
		</td>
		<td class="right">接受发送日期：</td>
		<td>
			<input class="short_txt" readonly="readonly"  type="text" id="dateStart" name="dateStart" onFocus="WdatePicker({el:$dp.$('dateStart'), maxDate:'#F{$dp.$D(\'endDate\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
			<input class="short_txt" readonly="readonly"  type="text" id="endDate" name="endDate" onFocus="WdatePicker({el:$dp.$('endDate'), minDate:'#F{$dp.$D(\'dateStart\')}'})"  style="cursor: pointer;width: 80px;"/>
		</td>
	</tr>
	<tr>
    	<td colspan="4" class="table_query_4Col_input" style="text-align: center">
    		<input type="button" name="BtnQuery" id="queryBtn"  value="查询"  class="u-button u-query" onClick="queryData()" />
    		<input type="reset" class="u-button u-reset" value="重 置"/>
    		<input type="button" name="BtnQuery" id="queryBtn"  value="导出"  class="normal_btn" onClick="exportData()" />
    	</td>
    </tr>
    </table>
</div>
	</div>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script>

function doInit() {
	 loadcalendar();  //初始化时间控件
}


    var dl_id;
 	var myPage;
	var url = "<%=contextPath%>/sales/tsimng/TsiLogManager/queryTableContent.json";
	var title = null;
	var columns = [];
	
	function querySet(){
		OpenHtmlWindow('<%=contextPath%>/sales/tsimng/TsiLogManager/queryTiTable.do?type=1',800,500);
	}
	
	
	function queryData(){
		var tableName = document.getElementById('tableName').value;
		if(tableName != null && tableName != ""){
			var url = "<%=contextPath%>/sales/tsimng/TsiLogManager/queryTableColumnInfo.json?tableName="+tableName;
			makeNomalFormCall(url,showColumnData,'fm');
		}else{
			MyAlert("请选择所要查选的表");
		}
		
	}
	
	function showColumnData(json){
		columns=[];
		columns.push({header: "序号",sortable: false,align:'center',renderer:getIndex});
			for(var i=0;i<json.list.length;i++){//动态循环字段
				columns.push({header: json.list[i].COMMENTS, dataIndex:  json.list[i].COLUMN_NAME,align:'center'});
			}
			__extQuery__(1);
	}
	
	function exportData(){
		var tableName = document.getElementById('tableName').value;
		if(tableName != null && tableName != ""){
			fm.action = "<%=contextPath%>/sales/tsimng/TsiLogManager/exportTableColumnInfo.do?tableName="+tableName;
		    fm.submit();
		}else{
			MyAlert("请选择所要查选的表");
		}
	}
</script>
</body>

</html>

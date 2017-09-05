<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
    String contextPath = request.getContextPath();
%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<title>配件变量新增</title>
<jsp:include page="${path}/common/jsp_head_new.jsp" />
<script type="text/javascript">
    //提交
	function addData(){
		var ids = document.getElementsByName("index");
		var strids = '';
		var flag = true;
		if($('#TYPEID')[0].value == ''){
			layer.msg('请选择变量类型！', {icon: 15});
			return;
		}
		if(ids.length<=0){
			layer.msg("请添加项目，否则请点返回！", {icon: 15});
			return ;
		}
		for(var i = 0; i < ids.length; i++){
			var name = document.getElementById('FIX_NAME'+(i+1)).value;
			if(name == ''){
				flag = false;
				layer.msg("第["+(i+1)+"]行变量名为空，请验证！", {icon: 15});
				return ; 
			}
			for(var n = 1; n <ids.length; n++){
				var repeatName = document.getElementById('FIX_NAME'+(n+1)).value;
				if(repeatName == name){
					flag = false;
					layer.msg("第["+(i+1)+"]行与第["+(n+1)+"]行的变量名称重复，请修改！", {icon: 15});
					return;
				}
			}
		}
		
/* 		for(i=0,o=1;i<ids.length-1;i++,o++){
			for(j=i+1,n=o+1;j<ids.length;j++,n++){
				var oldname = document.getElementById("FIX_NAME"+ids[i].value).value;
				var newname = document.getElementById("FIX_NAME"+ids[j].value).value;
				if(oldname==''){
					flag = false;
					MyAlert("第["+o+"]行变量名为空，请验证！");
					return ; 
				}
				if(oldname==newname){
					flag = false;
					MyAlert("第["+o+"]行与第["+n+"]行的变量名称重复，请修改！");
					return;
				}
			}
		} */
		if(flag){
			for(i=0;i<ids.length;i++){
				strids +=ids[i].value+",";
			}
			document.getElementById("ids").value=strids.substring(0,strids.length-1);
			fm.action = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartFixcodeQuery/add.do';
			fm.submit();
		}
	}	
    function addTblRow() {
		var tbl = document.getElementById('file');
		var ids = document.getElementsByName("index");
		var rowObj = tbl.insertRow(tbl.rows.length);
		var id = 0;
		for(i=0;i<ids.length;i++){
			id = ids[i].value;
		}
		id= parseInt(id)+1;
		if(tbl.rows.length%2 == 0) {
			rowObj.className  = "table_list_row2";
		}else{
			rowObj.className  = "table_list_row1";
		}
		
		var cell1 = rowObj.insertCell(0);
		var cell2 = rowObj.insertCell(1);
		var cell3 = rowObj.insertCell(2);
		var cell4 = rowObj.insertCell(3);
		var cell5 = rowObj.insertCell(4);
		var cell6 = rowObj.insertCell(5);
		
		
		cell1.innerHTML = '<tr><td align="center" nowrap><label class="u-checkbox"><input type="checkbox" name="plan"/><span></span></label><input type="hidden" name="index" value="'+(id)+'"/></td>';
		cell2.innerHTML = '<td align="center" nowrap>'+(id)+'</td>';
		cell3.innerHTML = '<td align="center" ><INPUT id="FIX_NAME'+(id)+ '" class="long_txt middle_txt" name="FIX_NAME'+(id)+ '"></td>';
		cell4.innerHTML = '<td align="center">  '+(id)+ ' </td>';
		cell5.innerHTML = '<td align="center"><INPUT id="SORT_NO'+(id)+ '" class="long_txt middle_txt" name="SORT_NO'+(id)+ '" value="'+(id)+'">  </td>';
		cell6.innerHTML = '<td><input type="button" class="u-button"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" /></td></TR>';
	}
    //删除
    function deleteTblRow(obj) {
		var idx = obj.parentElement.parentElement.rowIndex;
		var tbl = document.getElementById('file');
		tbl.deleteRow(idx);		
	}
    function deleteRes() {
		var obj = document.getElementsByName('plan');
		MyConfrim('你确定要删除该记录吗？', function(){

			var flag = false;
			var a = document.getElementById("file");
			var arr = new Array();
			var k = 0;
			for(var i=0; i<obj.length; i++) {
				if(obj.item(i).checked){
					flag = true;
					arr[k++]=obj.item(i).parentElement.parentElement.rowIndex;
				}
			}
			for(var j=arr.length-1; j>=0; j--) {
				a.deleteRow(arr[j]);
			}

			if(!flag) {
				layer.msg("你未选中需要删除的记录，请重新选择！", {icon: 15});
			}
		});
	}
    //全选
    function selectAll(){
		var obj = document.getElementsByName('plan');
		for(var i=0; i<obj.length; i++) {
			if(obj.item(i).checked) {
				obj.item(i).checked = false;
			}else{
				obj.item(i).checked = true;
			}
		}
	}
</script>
</head>
<body>
	<div class="wbox">
		<form name="fm" id="fm" method="post">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 配件变量维护 &gt; 新增
			</div>
			<div class="form-panel">
				<h2>
					<img class="panel-icon nav" src="<%=request.getContextPath()%>/img/subNav.gif" />变量类型
				</h2>
				<div class="form-body">
						<span>变量类型：</span>
						<c:choose>
							<c:when test="${not empty typelist}">
								</select>
							</c:when>
							<c:otherwise>

							</c:otherwise>
						</c:choose>
						<select class="u-select" id="TYPEID" name="TYPEID">
							<c:if test="${not empty typelist}">
								<c:forEach items="${typelist}" var="var">
									<option value="${var.CODE_ID }">${var.CODE_DESC }</option>
								</c:forEach>
							</c:if>
						</select>
						<input type="button" class="normal_btn" name="queryBtn6" value="增加" onclick="addTblRow();" />
						<input type="button" class="normal_btn" name="queryBtn6" value="删除" onclick="deleteRes();" />
				</div>
			</div>
					
					<table id="file" class="table_list tb-list-1" style="border-bottom: 1px solid #DAE0EE">
						<tr class="table_list_row0 tb-list-title">
							<td>
								<label class="u-checkbox"><input type="checkbox" onclick="selectAll()" /><span></span></label>
							</td>
							<td>序号</td>
							<td>变量名称</td>
							<td>变量值</td>
							<td>排序号</td>
							<td>操作</td>
						</tr>
						<tr class="table_list_row1 td-pd-1">
							<td align="center">
								<label class="u-checkbox"><input type="checkbox" name="plan" /><span></span></label>
								<input type="hidden" name="index" value="1" />
							</td>
							<td align="center">1</td>
							<td align="center">
								<input type="text" id="FIX_NAME1" class="long_txt middle_txt" name="FIX_NAME1" />
							</td>
							<td align="center">1</td>
							<td align="center">
								<input type="text" id="SORT_NO1" class="long_txt middle_txt" name="SORT_NO1" value="1" />
							</td>
							<td>
								<input type="button" class="normal_btn" name="queryBtn" value="删除" onclick="deleteTblRow(this);" />
							</td>
						</tr>
					</table>
					<table width="100%" align="center">
						<tr>
							<td height="2"></td>
						</tr>
						<tr>
							<input type="hidden" id="ids" name="ids" value="" />
							<td align="center">
								<input class="u-button" type="button" value="保存" name="button1" onclick="addData();">
									&nbsp; <input class="u-button" type="button" value="返回" name="button1" onclick="history.back();">
							</td>
						</tr>
						<tr>
							<td height="1"></td>
						</tr>
					</table>
		</form>
	</div>

</body>
</html>

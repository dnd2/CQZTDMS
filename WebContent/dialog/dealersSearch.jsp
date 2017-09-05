<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<link href="<%=request.getContextPath()%>/style/dtree_default.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/dtree1.js"></script>
</head>
	
<body onunload='javascript:destoryPrototype()' >
<div style=" margin:2px;float:left;border-right:solid 1px;">
			<script type="text/javascript">
				var rs = ${orgs}.orgs;
				d = new dTree('d', "<%=contextPath%>");
				for(var i=0;i<rs.length;i++){
					d.add(rs[i].id,rs[i].pid,rs[i].name,"javascript:setOrg(" + rs[i].id + ");");
				}
				document.write(d);
				d.closeAll();
				d.o(1);
		
			</script>
	 </div>
	 <div style="float:right;">
	 <form id='fm' name='fm'>
		<input type="hidden" id="orgId" name="orgId" value="" />
		<input type="hidden" id="DEALER_IDS" name="DEALER_IDS" value="${DEALER_IDS }" />
		<input type="hidden" id="BRAND_IDS" name="BRAND_IDS" value="${BRAND_IDS }" />
		<table class="table_query" >
			<tr>
				<td class="table_query_label" nowrap="nowrap">代理商代码：</td>
				<td class="table_query_input"   nowrap="nowrap">
					<input datatype="1,is_noquotation,<%=Constant.Length_Check_Char_100 %>" id="dlrCode" name="dlrCode" class="short_txt" type="text" />
				</td>
				<td class="table_query_label" nowrap="nowrap" >代理商：</td>
				<td class="table_query_input" nowrap="nowrap">
					<input datatype="1,is_noquotation,<%=Constant.Length_Check_Char_100 %>" id="dlrName" name="dlrName" class="middle_txt" type="text" />
				</td>
			</tr>
			<tr>
				<td class="table_query_label" nowrap="nowrap">
					<input class="normal_btn" id="queryBtn" type="button" value="查 询" onclick="__extQuery__(1);"/>
				</td>
				<td class="table_query_label" nowrap="nowrap">
					<input class="normal_btn" onclick="clsTxt();" type="reset" value="清 除" />
				</td>
				<td class="table_query_label" nowrap="nowrap">
					<input class="normal_btn" onclick="addAllFirst();" type="reset" value="全 选" />
				</td>
				<td class="table_query_label" nowrap="nowrap">
					<input name="button2" type="button" class="normal_btn" onclick="add()" value="添 加" />
				</td>
				<td class="table_query_label" nowrap="nowrap">
					<input class="normal_btn" type="button" onclick="_hide();" value="关 闭" />
				</td>
				
			</tr>
		</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		</form>
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</div>
		<table width="100%">
</table>
</body>
<script>
	var url = "<%=contextPath%>/common/OrgMng/queryDlrs.json";
	//设置表格标题
	var title= null;
	//设置列名属性
	var columns = [
					{header: "<input type='checkbox' id='selectAll' name='selectAll' onclick='checkselectAllBox(this)'  />", dataIndex: 'dlrId',width:"50px",renderer:myLink},
					{header: "代理商代码",width:"50px", dataIndex: 'dlrCode'},
					{header: "代理商", dataIndex: 'dlrName'},
					{header: "代理商简称", dataIndex: 'dlrShortName'}
			      ];
	
	function myLink(value,meta,record){
		return "<input type='checkbox' name='selectOne' onclick='checkselectAllBox(this)' value=\""+record.data.dlrId+"\" id=\""+record.data.dlrId+"\"'  />"
	}
	var objarr = document.getElementsByName('selectOne');
	function checkselectAllBox(obj) {
		if(obj.id == "selectAll" && obj.checked == true) {
			for(var i=0; i<objarr.length; i++) {
				objarr[i].checked = true;
			}
		} else if(obj.id == "selectAll" && obj.checked == false) {
			for(var i=0; i<objarr.length; i++) {
				objarr[i].checked = false;
			}
		} else {
			if(obj.checked == false) {
				$('selectAll').checked = false;
			} else {
				var st = true;
				for(var i=0; i<objarr.length; i++) {
					if(objarr[i].checked == false) {
						st = false;
						break;
					}
				}
				st ? $('selectAll').checked = true : "";
			}
		}
	}
	
	function add() {
			var myDealer = null;
			if($('DEALER_IDS').value != "") {
				myDealer = $('DEALER_IDS').value.split(",");
			}
			var temp = new Array();
			for(var i=0; i<objarr.length; i++)
			{
				if(objarr[i].checked == true) 
				{
					temp.push(objarr[i].id);
				}
			}
			if(myDealer != null) {
				for(var i=0; i<myDealer.length; i++)
				{
					temp.push(myDealer[i]);
				}
			}
			parentContainer.showDealer(temp.toString());
		
		_hide();
	}
	function addAllFirst() {
		if(submitForm('fm')){
			sendAjax('<%=contextPath%>/common/OrgMng/queryAllDlrs.json',addAll,'fm');
		}
	}
	function addAll(json) {
		var myDealer = null;
		if($('DEALER_IDS').value != "") {
			myDealer = $('DEALER_IDS').value.split(",");
		}
		var temp = new Array();
		for(var i = 0 ; i < (json.dealerList).length ; i ++ )
		{
			temp.push(((json.dealerList)[i]).dlrId);
		}
		if(myDealer != null) {
			for(var i=0; i<myDealer.length; i++)
			{
				temp.push(myDealer[i]);
			}
		}
		parentContainer.showDealer(temp.toString());
	
		_hide();
	}
	
	function setDlr(dlrId,dlrName){
		parentDocument.getElementById('dealerName').value = dlrName;
		parentDocument.getElementById('dealerId').value = dlrId;
		_hide();
	}
	
	function setOrg(orgId){
		$('orgId').value = orgId;
		__extQuery__(1);
	}
	
	function clsTxt(){ //清除经销商文本框
		parentDocument.getElementById('dealerName').value = "";
		parentDocument.getElementById('dealerId').value = "";
		_hide();
	}
</script>
</html>
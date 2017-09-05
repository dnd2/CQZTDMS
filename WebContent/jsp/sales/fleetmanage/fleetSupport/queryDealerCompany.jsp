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
 <div style="float:right;">
	 <form id='fm' name='fm'>
		<table class="table_query" >
			<tr>
				<td class="table_query_label" align="right">公司代码：</td>
				<td class="table_query_input" align="left">
					<input datatype="1,is_noquotation,<%=Constant.Length_Check_Char_100 %>" id="companyCode" name="companyCode" class="middle_txt" type="text" />
				</td>
				<td class="table_query_label" align="right">公司名称：</td>
				<td class="table_query_input" align="left">
					<input datatype="1,is_noquotation,<%=Constant.Length_Check_Char_100 %>" id="companyName" name="companyName" class="middle_txt" type="text" />
				</td>
				<td width="30%">
					<input name="button2" type=button class="cssbutton" onclick="__extQuery__(1);" value="查询"/>
					<input class="cssbutton" type="reset" value="重置" />
					<input class="cssbutton" type="button" onclick="_hide();" value="关闭" />
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
	<form name ="form1" id="form1">
		<table class="table_query" >
			<tr>
				<td class="table_query_input" align="left">
					<input name="button2" type=button class="cssbutton" onclick="toSubmit();" value="确定"/>
				</td>
			</tr>
		</table>
	</form>
 </div>
<script language="javascript">
	var url = "<%=contextPath%>/sales/fleetmanage/fleetSupport/FleetSupportApplyCheck/queryCom.json";
	//设置表格标题
	var title= null;
	//设置列名属性
	var columns = [
					{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"companyId\")' />", width:'8%',sortable: false,dataIndex: 'companyId',renderer:myCheckBox},
					{header: "公司代码",width:"50px", dataIndex: 'companyCode'},
					{header: "公司名称", dataIndex: 'companyName'},
					{header: "公司简称", dataIndex: 'companyShortname'}
			      ];
	
	function myCheckBox(value,meta,record){
		return "<input type='checkbox' name='companyId' id=\""+record.data.companyId+"\" value=\""+record.data.companyId+"\"/><input type='hidden' name='companyNames' value=\""+record.data.companyShortname+"\"/>"
	}
	function toSubmit(){
		var ids="";
		var names="";
		var chk = document.getElementsByName("companyId");
		var cname = document.getElementsByName("companyNames");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i < l;i++){        
			if(chk[i].checked)
			{   
				if(ids&&ids.length>0){    
					ids = chk[i].value+","+ids;
					names = cname[i].value+","+names;
				}else{
					ids = ids+chk[i].value;
					names = names+cname[i].value;  
				}
				cnt++;
			}
		}
		if(cnt==0){
			MyAlert("请选择！");
	        return false;
		}
		setCom(ids,names);
	}
	function setCom(companyId,companyName){
		parent.$('inIframe').contentWindow.$('companyName').value = companyName;
		parent.$('inIframe').contentWindow.$('companyId').value = companyId;
		_hide();
	}
	function doInit(){
		__extQuery__(1);
	}
</script>
</body>
</html>
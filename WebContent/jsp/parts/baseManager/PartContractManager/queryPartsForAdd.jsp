<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String contextPath = request.getContextPath();
%>
<html>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配件件信息</title>
</head>
<script type="text/javascript">
	var flag = true ;
	var url="<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/queryPartsDialog.json";
				
	var title = null;

	var columns = [
	           {header:'序号',align:'center',renderer:getIndex},
	           {header:'<input type=\"checkbox\" id = \"selectedAll\" onclick=\"selectAll()\" />&nbsp;全选',dataIndex:'PART_ID',renderer:myCheckBox},
	           {header: "配件编码",sortable: false,dataIndex: 'PART_OLDCODE',align:'center'},
	           {header: "配件名称",sortable: false,dataIndex: 'PART_CNAME',align:'center'},
	           {header: "配件件号",sortable: false,dataIndex: 'PART_CODE',align:'center'}
		   		]; 
		      
	function myCheckBox(value,meta,record){
		return String.format("<input type='checkbox' name='parts' value='"+record.data.PART_ID+"@@"+record.data.PART_CODE+"@@"+record.data.PART_OLDCODE+"@@"+record.data.PART_CNAME+"' />");
	}

	//全选
	function selectAll()
	{
		var selectAll = document.getElementById("selectedAll");
		var objArr = document.getElementsByName('parts');
		if(true == selectAll.checked)
		{
			for(var i=0;i<objArr.length;i++)
			{
				objArr[i].checked = true;
			}
		}
		else
		{
			for(var i=0;i<objArr.length;i++)
			{
				objArr[i].checked = false;
			}
		}
	}
	function setMainPartCode(v1,v2){
		 //调用父页面方法
		var v1=v1;
		if(!v1) return;
		if(flag){
			for(var i=0;i<v1.length;i++){
				v[i].checked=true;
			}
			flag = false ;
		} else if(!flag){
			for(var i=0;i<v1.length;i++){
				v1[i].checked=false;
			}
			flag = true ;
		}
	}
	
	function winClose(){
		var codes = document.getElementsByName('parts');
		var userIdsNames = [];
		var k = 0;
		for(var i=0;i<codes.length;i++){
			if(codes[i].checked==true){
				userIdsNames[k] = codes[i].value;
				k++;
			}
		}
		if(userIdsNames.length <= 0)
		{
			MyAlert('请至少选择一个新增的配件!');
			return false;
		}
		if(__parent().setPartCode==undefined)
			MyAlert('调用父页面setLaborList方法出现异常!');
		else{
			btnDisable();
			__parent().setPartCode(userIdsNames);
			_hide();
		}
	}
	$(function(){__extQuery__(1);});
</script>
<body>
<div class="wbox">
<div class="navigation">
	<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 基础信息管理 &gt;计划相关信息维护 &gt; 供应商管理 &gt;合同管理&gt;新增&gt;配件选择
</div>
<form method="post" name = "fm" id="fm">
   <div class="form-panel">
   <h2><img src="<%=contextPath%>/img/subNav.gif"/>新增合同</h2>
   <div class="form-body">
	<table class="table_query">
		<tr>         
			<td class="right">配件编码：</td>
			<td>
			<input class="middle_txt" id="partolcode"  name="partolcode" type="text" datatype="1,is_null,20"/>
			</td>
			<td wclass="right">配件名称：</td>
			<td><input type="text" name="partcname" id="partcname" datatype="1,is_null,30" class="middle_txt" value=""/></td>   
		</tr>
		<tr>
			<td class="center" colspan="6" >
				<input type="button" onclick="__extQuery__(1);" class="u-button"  value="查询" name="BtnQuery" id="queryBtn" />
				<input type="button" value="确 认" class="u-button" onclick="winClose();"/>
				<input type="button" value="关 闭" class="u-button" onclick="_hide();"/>
			</td>
		</tr>
	</table>
   </div>
   </div>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />  
</form>
</div>
</body>
</html>
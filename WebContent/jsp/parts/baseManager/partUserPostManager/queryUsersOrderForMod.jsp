<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String contextPath = request.getContextPath();
%>
<html>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>人员信息</title>
</head>
<script type="text/javascript">
	var flag = true ;
	var url = "<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/queryUsersOrderDialog.json";		
	var title = null;
	var columns = [
	   {header:'序号',align:'center',renderer:getIndex,width:'3%'},
	   {header:'<input type=\"checkbox\" id = \"selectedAll\" onclick=\"selectAll()\" />&nbsp;全选',dataIndex:'CODE_ID',renderer:myCheckBox,align:'center',width:'4%'},
// 	           {header:'人员账户',dataIndex:'ACNT',align:'center'},
	   {header:'订单类型',dataIndex:'CODE_DESC',align:'center'}
// 	           {header:'人员名称',dataIndex:'NAME',align:'center'}订单类型选择
	]; 
		      
	function myCheckBox(value,meta,record){
		return String.format("<input type='checkbox' name='users' value='"+value+"' />");
	}

	//全选
	function selectAll()
	{
		var selectAll = document.getElementById("selectedAll");
		var objArr = document.getElementsByName('users');
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

	function setMainPartCode(v1){
		 //调用父页面方法
		var v1=v1;
		if(!v1) return;
		if(flag){
			for(var i=0;i<v1.length;i++){
				MyAlert("checked: " + v1);
				v[i].checked=true;
			}
			flag = false ;
		} else if(!flag){
			for(var i=0;i<v1.length;i++){
				MyAlert("unchecked: " + v1);
				v1[i].checked=false;
			}
			flag = true ;
		}
	}
	
	function winClose(){
		var codes = document.getElementsByName("users");
		var users = [];
		var k = 0;
		for(var i=0;i<codes.length;i++){
			if(codes[i].checked==true){
				users[k] = codes[i].value;
				k++;
			} 
		}
		if(users.length <= 0)
		{
			layer.msg('请至少选择一个新增的订单类型!', {icon: 15});
			return false;
		}

		MyConfirm("确认是否新增?", function() {
			checkForm();
		});
	}

	function checkForm()
	{
		var userId = document.getElementById("userId").value;
		var codes = document.getElementsByName("users");
		var users = [];
		var k = 0;
		for(var i=0;i<codes.length;i++){
			if(codes[i].checked==true){
				users[k] = codes[i].value;
				k++;
			} 
		}
		btnDisable();
		var url = '<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/insertPartUserOrder.json?users='+users+'&userId='+userId;
  		makeNomalFormCall(url,showResult,'fm');
	}

	function showResult(json) {
		btnEnable();
	    if (json.errorExist != null && json.errorExist.length > 0) {
	    	layer.msg("订单类型：【" + json.errorExist + "】已添加，不能重复添加!", {icon: 15});
	    } else if (json.success != null && json.success == "true") {
	    	layer.msg("新增成功!", {icon: 1});
	    	__parent().__extQuery__(1);
			_hide();        
	    } else {
	        layer.msg("新增失败，请联系管理员!", {icon: 2});
	    }
	}

	$(function(){
		__extQuery__(1);
	});
</script>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：基础信息管理 &gt; 配件基础信息维护 &gt; 订单类型选择</div>
	<form method="post" name = "fm" id="fm">
		<input type="hidden" id="fixValue" name="fixValue" value="${fixValue }"/>
		<input type="hidden" id="userId" name="userId" value="${userId}"/>
		<table class="table_query" border="0" >
			<tr>
				<td class="right">订单类型：</td>
				<td>
					<input name="orderType" type="text" class="middle_txt"/>
				</td>
				<td class="center">
					<input type="button" onclick="__extQuery__(1);" class="u-button u-query"  value="查询" name="BtnQuery" id="queryBtn" />
					<input type="button" value="确认" class="u-button u-submit" onclick="winClose();"/>
					<input type="button" value="关闭" class="u-button u-cancel" onclick="_hide();"/>
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />  
	</form>	
</div>	
  

</body>
</html>
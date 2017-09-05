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

	var url = "<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/queryUsersDialog.json";
				
	var title = null;

	var columns = [
	           {header:'序号',align:'center',renderer:getIndex,width:'3%'},
	           {header:'<label class="u-checkbox"><input type=\"checkbox\" id = \"selectedAll\" onclick=\"selectAll()\" /><span></span></label>全选',dataIndex:'USER_ID',renderer:myCheckBox,align:'center',width:'4%'},
	           {header:'人员账户',dataIndex:'ACNT',align:'center'},
	           {header:'人员名称',dataIndex:'NAME',align:'center'}
		   		]; 
		      
	function myCheckBox(value,meta,record){
		return String.format("<label class='u-checkbox'><input type='checkbox' name='users' value='"+record.data.USER_ID+"' /><span></span></label>");
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
				layer.msg("checked: " + v1, {icon: 15});
				v[i].checked=true;
			}
			flag = false ;
		} else if(!flag){
			for(var i=0;i<v1.length;i++){
				layer.msg("unchecked: " + v1, {icon: 15});
				v1[i].checked=false;
			}
			flag = true ;
		}
	}
	
	function winClose(){
		var fixValue = document.getElementById("fixValue").value;
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
			layer.msg('请至少选择一个新增的人员!', {icon: 15});
			return false;
		}

		confirm('确认是否新增?', function() {
			checkForm();
		});
	}

	function checkForm()
	{
		var fixValue = document.getElementById("fixValue").value;
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
		var url = '<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/insertPartUserPost.json?users='+users+'&fixValue='+fixValue;
  		makeNomalFormCall(url,showResult,'fm');
	}

	function showResult(json) {
		btnEnable();
	    if (json.errorExist != null && json.errorExist.length > 0) {
	    	layer.msg("人员：【" + json.errorExist + "】已添加，不能重复添加!", {icon: 15});
	    } else if (json.success != null && json.success == "true") {
	    	layer.msg("新增成功!", {icon: 1});
	    	__parent().__extQuery__(1);
			_hide();        
	    } else {
	        layer.msg("新增失败，请联系管理员!", {icon: 2});
	    }
	}

	//失效按钮
	function btnDisable(){
	    $('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });

	}

	//有效按钮
	function btnEnable(){
	    $('input[type="button"]').each(function(button) {
	        button.disabled = "";
	    });

	}
	
	$(function(){
		__extQuery__(1);
	});
</script>
<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：基础信息管理 &gt; 配件基础信息维护 &gt; 人员选择</div>
  <form method="post" name = "fm" id="fm">
    <input type="hidden" id="fixValue" name="fixValue" value="${fixValue }"/>
    <table class="table_query form-tb-bottom" border="0">
      <tr>
        <td class="right" width="10%" align="right">人员名称：</td>
        <td>
            <input name="NAME" type="text" class="middle_txt"/>
        </td>
        <td class="center">
        	<input type="button" onclick="__extQuery__(1);" class="u-button u-query"  value="查询" name="BtnQuery" id="queryBtn" />
        	<input type="button" value="确认" class="u-button" onclick="winClose();"/>
        	<input type="button" value="关闭" class="u-button u-cancel" onclick="_hide();"/>
        </td>
      </tr>
    </table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />  
</form>
</body>
</html>
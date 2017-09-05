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
           {header:'<input type=\"checkbox\" id = \"selectedAll\" onclick=\"selectAll()\" />&nbsp;全选',dataIndex:'USER_ID',renderer:myCheckBox,align:'center',width:'4%'},
           {header:'人员账户',dataIndex:'ACNT',align:'center'},
           {header:'人员名称',dataIndex:'NAME',align:'center'}
 		]; 
	      
function myCheckBox(value,meta,record){
	return String.format("<input type='checkbox' name='users' value='"+record.data.USER_ID+"@@"+record.data.NAME+"' />");
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

// 确认
function winClose(){
	var codes = document.getElementsByName('users');
	var fixValue = $("#fixValue").val();
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
		MyAlert('请至少选择一个新增的人员!');
		return false;
	}
	if(__parent().setPartCode==undefined){
		MyAlert('调用父页面setLaborList方法出现异常!');
	}else{
		btnDisable();
		__parent().setPartCode(userIdsNames,fixValue);
		_hide();
	}
}

</script>
<body onload="__extQuery__(1);">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：基础信息管理 &gt; 配件基础信息维护 &gt; 人员信息选择</div>
 <form method="post" name = "fm" id="fm">
 <input type="hidden" id="fixValue" value="${fixValue }"/>
    <table class="table_query" border="0" >
      <tr>
        <td class="right">人员名称：</td>
        <td>
            <input name="NAME" type="text" class="middle_txt"/>
        </td>
        <td class="table_query_2Col_input">
        	<input type="button" value="查 询" class="u-button u-query" onclick="__extQuery__(1);" id="queryBtn" name="BtnQuery" />
        	<input type="button" value="确 认" class="u-button u-submit" onclick="winClose();"/>
        	<input type="button" value="关 闭" class="u-button u-cancel" onclick="_hide();"/>
        </td>
      </tr>
    </table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />  
</form>
</body>
</html>
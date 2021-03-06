<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String contextPath = request.getContextPath();
%>
<html>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>仓库信息</title>
</head>
<script type="text/javascript">
	var flag = true ;

	var url = "<%=contextPath%>/parts/baseManager/partPlannerWarehouseManager/partPlannerWarehouseAction/queryWarehouseDialog.json";
				
	var title = null;

	var columns = [
	           {header:'序号',align:'center',renderer:getIndex,width:'3%'},
	           {header:'<input type=\"checkbox\" id = \"selectedAll\" onclick=\"selectAll()\" />全选',dataIndex:'WH_ID',renderer:myCheckBox,align:'center',width:'4%'},
	     //      {header:'仓库ID',dataIndex:'WH_ID',align:'center'},
	           {header:'仓库名称',dataIndex:'WH_NAME',align:'center'}
		   		]; 
		      
	function myCheckBox(value,meta,record){
		return String.format("<input type='checkbox' name='wareHouse' value='"+record.data.WH_ID+"@@"+record.data.WH_NAME+"' />");
	}

	//全选
	function selectAll()
	{
		var selectAll = document.getElementById("selectedAll");
		var objArr = document.getElementsByName('wareHouse');
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
		var codes = document.getElementsByName('wareHouse');
		var whIdsNames = [];
		var k = 0;
		for(var i=0;i<codes.length;i++){
			if(codes[i].checked==true){
				whIdsNames[k] = codes[i].value;
				k++;
			}
		}
		if(whIdsNames.length <= 0)
		{
			layer.msg('请至少选择一个新增的仓库!', {icon: 2});
			return false;
		}
		if(__parent().setPartCode==undefined)
			MyAlert('调用父页面setLaborList方法出现异常!');
		else{
			btnDisable();
			__parent().setPartCode(whIdsNames);
			_hide();
		}
	}

	$(function() {
		__extQuery__(1);
	});
</script>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：基础信息管理 &gt; 配件基础信息维护 &gt; 仓库信息选择</div>
<form method="post" name = "fm" id="fm">
    <table class="table_query" border="0" >
      <tr>
        <td class="right">仓库名称：</td>
        <td>
            <input name="WH_NAME" type="text" class="middle_txt"/>
        </td>
        <td class="left">
        	<input type="button" onclick="__extQuery__(1);" class="u-button u-query"  value="查 询" name="BtnQuery" id="queryBtn" />
        	<input type="button" value="确 认" class="u-button u-submit" onclick="winClose();"/>
        	<input type="button" value="关 闭" class="u-button u-cancel" onclick="_hide();"/>
        </td>
      </tr>
    </table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />  
</form>
</div>
</body>
</html>
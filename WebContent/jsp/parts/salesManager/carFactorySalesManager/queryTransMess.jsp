<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String contextPath = request.getContextPath();
%>
<html>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配件信息</title>
</head>
<script type="text/javascript" >

</script>
<body onload="__extQuery__(1);">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;装箱信息选择</div>
 <form method="post" name = "fm" id="fm">
    <table class="table_query" border="0" >
      <!-- 
      <tr>            
        <td width="20%" class="table_query_2Col_label_4Letter">配件编码：</td>            
        <td width="30%">
			<input  class="middle_txt" id="partolcode"  name="partolcode" type="text" datatype="1,is_null,20"/>
        </td>
        <td width="20%" class="table_query_2Col_label_4Letter">配件名称：</td>
        <td width="30%"><input type="text" name="partcname" id="partcname" datatype="1,is_null,30" class="middle_txt" value=""/></td>   
      </tr>
       -->
      <tr>
        <td align="center" colspan="6" >
         <!-- 	<input type="button" onclick="__extQuery__(1);" class="normal_btn"  value="查询" name="BtnQuery" id="queryBtn" /> -->
        	<input type="button" value="确 认" class="normal_btn" onclick="winClose();"/>
        	<input type="button" value="关 闭" class="normal_btn" onclick="_hide();"/>
        </td>
      </tr>
    </table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />  
</form>

<script type="text/javascript">
	var flag = true ;

	var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTrans/queryMessDialog.json";
				
	var title = null;

	var columns = [
	           {header:'序号',align:'center',renderer:getIndex},
	           {header:'<input type=\"checkbox\" id = \"selectedAll\" onclick=\"selectAll()\" />&nbsp;全选',dataIndex:'FIX_ID',renderer:myCheckBox},
	          // {header: "配件编码",sortable: false,dataIndex: 'PART_OLDCODE',align:'center'},
	           {header: "装箱人信息",sortable: false,dataIndex: 'FIX_NAME',align:'center'}
		   		]; 
		      
	function myCheckBox(value,meta,record){
		return String.format("<input type='checkbox' name='msgs' value='"+record.data.FIX_NAME+"' />");
	}

	//全选
	function selectAll()
	{
		var selectAll = document.getElementById("selectedAll");
		var objArr = document.getElementsByName('msgs');
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
		var codes = document.getElementsByName('msgs');
		var messagegs = [];
		var k = 0;
		for(var i=0;i<codes.length;i++){
			if(codes[i].checked==true){
				messagegs[k] = codes[i].value;
				k++;
			}
		}
		if(messagegs.length <= 0)
		{
			MyAlert('请至少选择一个装箱人信息!');
			return false;
		}
		if(parentContainer.setMessage==undefined)
			MyAlert('调用父页面setLaborList方法出现异常!');
		else{
			btnDisable();
			parentContainer.setMessage(messagegs);
			_hide();
		}
	}

	//失效按钮
	function btnDisable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });

	}

	//有效按钮
	function btnEnable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = "";
	    });

	}
</script>
</body>
</html>
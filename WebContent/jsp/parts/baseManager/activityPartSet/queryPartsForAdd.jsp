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
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：基础数据管理 &gt; 配件基础数据维护 &gt; 活动配件明细设置 &gt; 配件选择</div>
 <form method="post" name = "fm" id="fm">
    <table class="table_query" border="0" >
      <tr>            
        <td width="20%" class="table_query_2Col_label_4Letter">配件编码：</td>            
        <td width="30%">
			<input  class="middle_txt" id="partolcode"  name="partolcode" type="text" datatype="1,is_null,20"/>
        </td>
        <td width="20%" class="table_query_2Col_label_4Letter">配件名称：</td>
        <td width="30%"><input type="text" name="partcname" id="partcname" datatype="1,is_null,30" class="middle_txt" value=""/></td>   
      </tr>
      <tr>
        <td align="center" colspan="6" >
        	<input type="button" onclick="__extQuery__(1);" class="normal_btn"  value="查询" name="BtnQuery" id="queryBtn" />
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

	var url = "<%=contextPath%>/parts/baseManager/partSTOSet/partSTOSetAction/queryPartsDialog.json";
				
	var title = null;

	var columns = [
	           {header:'序号',align:'center',renderer:getIndex},
	           {header:'<input type=\"checkbox\" id = \"selectedAll\" onclick=\"selectAll()\" />&nbsp;全选',dataIndex:'PART_ID',renderer:myCheckBox},
	           {header: "配件编码",sortable: false,dataIndex: 'PART_OLDCODE',align:'center'},
	           {header: "配件名称",sortable: false,dataIndex: 'PART_CNAME',align:'center'}
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
		if(parentContainer.setPartCode==undefined)
			MyAlert('调用父页面setLaborList方法出现异常!');
		else{
			btnDisable();
			parentContainer.setPartCode(userIdsNames);
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
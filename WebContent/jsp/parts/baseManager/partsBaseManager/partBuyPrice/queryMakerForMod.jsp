<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String contextPath = request.getContextPath();
%>
<html>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>制造商信息</title>
</head>
<script type="text/javascript" >

</script>
<body onload="__extQuery__(1);">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：基础信息管理 &gt; 配件基础信息维护 &gt; 配件采购价格维护 &gt; 选择制造商</div>
  <form method="post" name = "fm" id="fm">
    <input type="hidden" id="venderId" name="venderId" value="${venderId }"/>
    <input type="hidden" id="partId" name="partId" value="${partId }"/>
    <input type="hidden" id="dfVender" name="dfVender" value="${dfVender }"/>
    <input type="hidden" id="buyPrice" name="buyPrice" value="${buyPrice }"/>
    <table class="table_query" border="0" >
      <tr>            
        <td width="10%" >制造商编码：</td>            
        <td width="30%">
			<input  class="middle_txt" id="makerCode"  name="makerCode" type="text" datatype="1,is_null,20"/>
        </td>
        <td width="10%" >制造商名称：</td>
        <td width="30%"><input type="text" name="makerName" id="makerName" datatype="1,is_null,30" class="middle_txt" value=""/></td>   
       </tr>
      <tr>
        <td colspan="4" align="center">
        	<input type="button" onclick="__extQuery__(1);" class="normal_btn"  value="查询" name="BtnQuery" id="queryBtn" />
        	<input type="button" value="确认" class="normal_btn" onclick="winClose();"/>
        	<input type="button" value="关闭" class="normal_btn" onclick="_hide();"/>
        </td>
      </tr>
    </table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />  
</form>

<script type="text/javascript">
	var flag = true ;

	var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/queryMakersDialog.json";
				
	var title = null;

	var columns = [
	           {header:'序号',align:'center',renderer:getIndex},
	           {header:'<input type=\"checkbox\" id = \"selectedAll\" onclick=\"selectAll()\" />&nbsp;全选',dataIndex:'PART_ID',renderer:myCheckBox},
	           {header: "制造商编码",sortable: false,dataIndex: 'MAKER_CODE',align:'center'},
	           {header: "制造商名称",sortable: false,dataIndex: 'MAKER_NAME',style:'text-align: left;'}
		   		]; 
		      
	function myCheckBox(value,meta,record){
		return String.format("<input type='checkbox' name='makers' value='"+record.data.MAKER_ID+"' />");
	}

	//全选
	function selectAll()
	{
		var selectAll = document.getElementById("selectedAll");
		var objArr = document.getElementsByName('makers');
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
		var ids = document.getElementsByName("makers");
		var makers = [];
		var k = 0;
		for(var i=0;i<ids.length;i++){
			if(ids[i].checked==true){
				makers[k] = ids[i].value;
				k++;
			} 
		}
		if(makers.length <= 0)
		{
			MyAlert('请至少选择一个新增的制造商!');
			return false;
		}
		if(confirm("确认是否新增?"))
		{
			checkForm();
		}
	}

	function checkForm()
	{
		var ids = document.getElementsByName("makers");
		var makers = [];
		var k = 0;
		for(var i=0;i<ids.length;i++){
			if(ids[i].checked==true){
				makers[k] = ids[i].value;
				k++;
			} 
		}
		btnDisable();
		var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/insertRelation.json?makers='+makers;
  		makeFormCall(url,showResult,'fm');
	}

	function showResult(json) {
		btnEnable();
	    if (json.errorExist != null && json.errorExist.length > 0) {
	    	MyAlert("制造商编码：【" + json.errorExist + "】已添加，不能重复添加!");
	    } else if (json.success != null && json.success == "true") {
	    	MyAlert("新增成功!");
	    	parentContainer.__extQuery__(1);
			_hide();        
	    } else {
	        MyAlert("新增失败，请联系管理员!");
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
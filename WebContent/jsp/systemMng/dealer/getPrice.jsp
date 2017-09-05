<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商维护</title>
<script type="text/javascript">
function doInit()
{  
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;新增经销商价格</div>
 <form method="post" name = "fm" id="fm" >
 <input id="DEALER_ID" name="DEALER_ID" type="hidden" value="${dealerId}"/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    	<tr>
		    <td class="table_query_2Col_label_6Letter">价格类型描述：
		    </td>
		    <td>
		   		<input type="text" class="middle_txt" id="priceDesc" name="priceDesc" value="" />
		   		<input type="hidden" name="priceId" id="priceId" value="" />
		   		<input type="button" class="normal_btn" onclick="__extQuery__(1)" value="查 询" id="queryBtn" /> 
		   		<input type="button" value="添加" name="saveBtn" class="normal_btn" onclick="savePrice()"/>	
				<input type="button" value="关闭" name="cancelBtn"  class="normal_btn" onclick="_hide();" />
		    </td>
	     </tr>
	     <!--<tr>
		    <td class="table_query_2Col_label_6Letter">默认价格：
		    </td>
		    <td>
		   		<label>
					<script type="text/javascript">
						genSelBoxExp("DEFAULTPRICE",<%=Constant.IF_TYPE%>,"<c:out value="11"/>",'',"min_sel",'',"false",'');
					</script>
		  		</label>
		  		
		    </td>
	     </tr>
	    --></table> 


	</form>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
    <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    
	 <!-- <form  name="form1" id="form1">
    	<table class="table_query" width="85%" align="center" border="0"  id="roll">	
	  	 <tr>
			 <td align="center">
				 <input type="button" value="添加" name="saveBtn" class="normal_btn" onclick="savePrice()"/>	
				 <input type="button" value="关闭" name="cancelBtn"  class="normal_btn" onclick="_hide();" />
				
			 </td>
		</tr>
		</table>
	</form>  -->
<script type="text/javascript" >
//document.form1.style.display = "none";
//var HIDDEN_ARRAY_IDS=['form1'];

var myPage;
var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/addPrice_showPrice.json?COMMAND=1";
var title = null;
var columns = [
			{id:'action',header:"选择" , width:'6%',sortable: false,dataIndex: 'PRICE_ID',renderer:myRadioSelect},
			{header: "价格类型代码", dataIndex: 'PRICE_CODE', align:'center'},
			{header: "价格类型描述", dataIndex: 'PRICE_DESC', align:'center'},
			{header: "失效日期", dataIndex: 'END_DATE', align:'center'},
			{header: "创建日期", dataIndex: 'CREATE_DATE', align:'center'}
	      ];
function myRadioSelect(value,metaDate,record){
	return String.format("<input type='radio' name='price_Id' value='" + value + "' />");
}


function savePrice(){
	if(submitForm('fm')){
		var priceIds = document.getElementsByName("price_Id");
		var flag = false;
		for(var i=0;i<priceIds.length;i++){
			if(priceIds[i].checked){
				document.getElementById("priceId").value = priceIds[i].value;
				flag=true;
				break;
			}
		}
		if(!flag){
			MyAlert('价格不能为空,请选择价格！');
			return;
		}else{
			toSubmit();
		}
	} 
}
function toSubmit(){
	makeNomalFormCall('<%=contextPath%>/sysmng/dealer/DealerInfo/savePrice.json',showResult,'fm');
}
//回调方法
function showResult(json){
	if(json.returnValue == '1'){
		parentContainer.parentMonth();
		_hide();
	}else{
		MyAlert("操作失败！请联系系统管理员！");
	}
}
</script>

</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>选择服务商</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script language="javascript" type="text/javascript">
	function doInit(){
			__extQuery__(1);
	}
</script>
</head>
<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件财务管理 &gt; 服务商账户查询 &gt; 选择服务商</div>
  <form name='fm' id='fm'>
  <input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
  <table class="table_query">
       <tr>            
        <td width="15%"   align="right">服务商编码：
        </td>            
        <td>
        	<input  class="middle_txt" id="dealerCodeSelect"  name="dealerCodeSelect" type="text" datatype="1,is_null,20"/>
        	<input id="dealerCode" name="dealerCode" type="hidden" />
	<!-- 	<input  class="middle_txt" id="dealerNameSelect"  name="dealerNameSelect" type="text" datatype="1,is_null,20"/>  
			<input id="dealerName" name="dealerName" type="hidden" /> -->
        </td>
        <td class="table_query_2Col_label_4Letter"></td>
        <td></td>   
       </tr>
       <tr>
        <td colspan="4" align="center">
       	  <input class="normal_btn" type="button" value="查 询"  name="BtnQuery" id="queryBtn"  onclick="__extQuery__(1)"/>&nbsp;
          <input type="button" value="确 认" class="normal_btn" onclick="winClose();"/>&nbsp;
	      <input class="normal_btn" type="button" name="button1" value="关 闭"  onclick="_hide();"/>
        </td>
       </tr>       
 	</table>
 	<br/>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
<script type="text/javascript" >
var myPage;
var url = "<%=request.getContextPath()%>/parts/financeManager/dealerAccQueryManager/partDealerAccQueryAction/partDealerSelect.json?query=1";
var title = null;
var columns = [
			{header: "<input type=\"checkbox\" id = \"selectedAll\" onclick=\"selectAll()\" />全选",sortable: false,dataIndex: 'DEALER_ID',align:'center',renderer:myCheckBox},
			{header: "服务商编码",sortable: false,dataIndex: 'DEALER_CODE',align:'center'},
			{header: "服务商名称",sortable: false,dataIndex: 'DEALER_NAME',align:'center'}
	      ];
	function myLink(value,metadata,record){
		return String.format(
				"<input type='hidden' id='"+record.data.DEALER_ID+"' name='"+record.data.DEALER_ID+"' value='"+record.data.DEALER_CODE+"'/>"+
				"<input type='radio'  value='"+record.data.DEALER_ID+"' onclick='selbyid(this);'/>");
	}

	function myCheckBox(value,meta,record){
		return String.format("<input type='checkbox' name='dealers' value='"+record.data.DEALER_CODE+"' />");
	}

	//全选
	function selectAll()
	{
		var selectAll = document.getElementById("selectedAll");
		var objArr = document.getElementsByName('dealers');
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

	function winClose(){
		var codes = document.getElementsByName('dealers');
		var dealerCodes = [];
		var k = 0;
		for(var i=0;i<codes.length;i++){
			if(codes[i].checked==true){
				dealerCodes[k] = codes[i].value;
				k++;
			}
		}
		if(parentContainer.setPartCode==undefined)
			MyAlert('调用父页面setLaborList方法出现异常!');
		else{
			parentContainer.setPartCode(dealerCodes);
			_hide();
		}
	}
	
	function selbyid(obj){
		$('dealerCode').value = document.getElementById(obj.value).value;
//		$('dealerName').value = document.getElementById(obj.value).value;
		_hide();
	}
	
	function returnBefore()
	{
	    var Name = 'dealerCode';
		var dealerName = document.getElementById("dealerCode").value;
		if(dealerName && dealerName.length > 0)
		   	parentDocument.getElementById(Name).value = dealerName;	
	}
</script> 
</form>
</body>
</html>
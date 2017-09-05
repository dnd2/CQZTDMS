<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>

<!-- created by lishuai103@yahoo.com.cn 20100603 通用选择供应商 -->
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
    String inputCode = request.getParameter("INPUTCODE");
    String inputId = request.getParameter("INPUTID");
    String isMulti = request.getParameter("ISMULTI");
    
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>通用选择框</title>

<script language="JavaScript">

	function doInit()
	{
		__extQuery__(1);
	}

</script>
</head>
<body onbeforeunload="doSupp()">
<form method="post" name ="fm" id="fm">
	<input id="SUPPLIER_CODE" name="SUPPLIER_CODE" type="hidden" value="" />
	<input id="SUPPLIER_ID" name="SUPPLIER_ID" type="hidden" value=""/>	
	<table class="table_edit">
    <tr>
      <td class="table_query_3Col_label_6Letter" nowrap="nowrap">供应商代码：</td>
      <td class="table_query_3Col_input" nowrap="nowrap">
      	<input class="middle_txt" id="suppCode" name="suppCode" value="" type="text"/>
      </td>
      <td class="table_query_3Col_label_6Letter" nowrap="nowrap">供应商名称：</td>
      <td>
      	<input name="suppName" type="text" id="suppName"  class="middle_txt"/>
      </td>
      <td>&nbsp;</td>
      <td>
      	<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1);" >
      </td>
  	</tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<form name="form1" style="display:none">
   <table class="table_list" id="table1" >
  	  <tr>
  	  	<th align="left">
    		<input class="normal_btn" type="button" value="全选" onclick="doAllClick()"/>
	    	<input class="normal_btn" type="button" value="清空" onclick="doDisAllClick()"/>
	    	<input class="normal_btn" type="button" value="确认" onclick="doConfirm()"/>
	    	<input class="normal_btn" type="button" value="关闭" onclick="parent._hide()"/>
       </th>
  	  </tr>
   </table>
  </form>
<script type="text/javascript" >
	document.form1.style.display = "none";
	var HIDDEN_ARRAY_IDS=['form1'];
	
	var myPage;

	var url = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/allSuppQuery.json";
				
	var title = null;

	var columns = [
				{header: "选择", dataIndex: 'supplierId', align:'center',renderer:seled},
				{header: "供应商代码", dataIndex: 'supplierCode', align:'center'},
				{header: "供应商名称", dataIndex: 'supplierName', align:'center'},
				{header: "简称", dataIndex: 'shortName', align:'center'},
				{header: "联系人", dataIndex: 'linkMan', align:'center'},
				{header: "联系电话", dataIndex: 'phoneNumber', align:'center'}
		      ];
		      
	function seled(value,meta,record){
		var isMulti = "<%=isMulti%>";
 		if(isMulti == "false"){
 			return "<input type='radio' onclick='singleSelect("+value+",\""+record.data.supplierCode+"\")' name='checkCode' />";
 		}else{
        	return "<input type='checkbox' name='checkCode' id='checkCode' value='"+ value + "||" +record.data.supplierCode+"' />";
        }
    }
    
    function singleSelect(val1,val2){
    	 $('SUPPLIER_CODE').value = val2;
    	 $('SUPPLIER_ID').value = val1;
    	_hide();
    }
    
    function doSupp()
	{
		var inputCode = "<%=inputCode%>";
		var inputId = "<%=inputId%>";
		var supplierCode = document.getElementById("SUPPLIER_CODE").value;
		var supplierId = document.getElementById("SUPPLIER_ID").value;
		if(supplierCode && supplierCode.length > 0){
			parentDocument.getElementById(inputCode).value = document.getElementById("SUPPLIER_CODE").value;
		}
		if(supplierId && supplierId.length > 0){
			parentDocument.getElementById(inputId).value = document.getElementById("SUPPLIER_ID").value;
		}
	}
	
	function doAllClick()
	{
		var chk = document.getElementsByName("checkCode");
		var l = chk.length;
		for(var i=0;i<l;i++)
		{        
			chk[i].checked = true;
		}
	}
	
	function doDisAllClick()
	{
		var chk = document.getElementsByName("checkCode");
		var l = chk.length;
		for(var i=0;i<l;i++)
		{        
			chk[i].checked = false;
		}
	}
	
	function doConfirm()
	{
		var chk = document.getElementsByName("checkCode");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++)
		{        
			if(chk[i].checked)
			{            
				cnt++;			
			}
		}
        if(cnt==0)
        {
             MyDivAlert("请选择供应商！");
        }
        else if(cnt>1){
        	 MyDivAlert("请选择一个供应商！");
            }


        else{
	        var codes = "";
			var ids = "";
	        for(var i=0;i<l;i++)
			{        
				if(chk[i].checked)
				{
					if(chk[i].value)
					{
						var arr = chk[i].value.split("||");
						if(ids)
						ids += "," + arr[0];
				    	else
				        ids = arr[0];
				        if(codes)
						codes += "," + arr[1];
				    	else
				        codes = arr[1];
				    }    
				}				
			}
	
			if(codes && codes.length > 0){
			   $('SUPPLIER_CODE').value = codes;
			}   
			if(ids && ids.length > 0){
			   $('SUPPLIER_ID').value = ids;
			}
			_hide();
		}
	}
</script>
</body>
</html>
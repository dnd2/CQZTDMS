<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
    String childorgName = request.getParameter("childorgName");
    String childorgId = request.getParameter("childorgId");
    String venderId = request.getParameter("venderId");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>通用选择框</title>

<script language="JavaScript">

</script>
</head>
<body onload="__extQuery__(1);" onbeforeunload="doSupp();">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 选择订货单位</div>
<form method="post" name ="fm" id="fm">
	<input id="childorgName" name="childorgName" type="hidden" value="" />
	<input id="childorgId" name="childorgId" type="hidden" value="" />
	<input id="venderId" name="venderId" type="hidden" value="<%=venderId %>" />
	<table class="table_edit">
    <tr>
      <td   align="right" nowrap="nowrap">订货单位编码：</td>
      <td class="table_query_3Col_input" nowrap="nowrap">
      	<input class="middle_txt" id="childOrgCode" name="childOrgCode" value="" type="text"/>
      </td>
      <td   align="right" nowrap="nowrap">订货单位名称：</td>
      <td>
      	<input name="childOrgName" type="text" id="childOrgName"  class="middle_txt"/>
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
<script type="text/javascript" >

	var myPage;

	var url = "<%=contextPath%>/report/partReport/partSalesReport/PartSalesDtlReport/queryChildOrgByGyzx.json";
				
	var title = null;

	var columns = [
				{header: "选择", dataIndex: 'CHILDORG_ID', align:'center',renderer:seled},
				{header: "订货单位编码", dataIndex: 'CHILDORG_CODE', align:'center'},
				{header: "订货单位名称", dataIndex: 'CHILDORG_NAME', align:'center'}
		      ];
		      
	function seled(value,meta,record){
		var data = record.data;
		var orgId = data.CHILDORG_ID;
		var orgCode = data.CHILDORG_CODE;
		var orgName = data.CHILDORG_NAME;
		return "<input type='radio' name='singleSel' onclick='singleSelect(\""+orgId+"\",\""+orgCode+"\",\""+orgName+"\");' />";
    }

    function singleSelect(orgId,orgCode,orgName){
    	 $('childorgName').value = orgName;
    	 $('childorgId').value = orgId;
    	 _hide();
    }
  //关闭弹出窗口的时候执行该方法
    function doSupp()
	{   
		var childorgName = "<%=childorgName%>";
		var childorgId = "<%=childorgId%>";
		var childorgName1 = document.getElementById("childorgName").value;
		var childorgId1 = document.getElementById("childorgId").value;
		if(childorgName && childorgName.length > 0){
			if(parent.$('inIframe') == null){//修复不在inIframe中时参数赋值问题
				if(top.$(childorgName)) {
					top.$(childorgName).value = childorgName1;
				}
			}else{
			parentDocument.getElementById(childorgName).value = document.getElementById("childorgName").value;
			}
		}
		if(childorgId && childorgId.length > 0){
			if(parent.$('inIframe') == null){//修复不在inIframe中时参数赋值问题
				if(top.$(childorgId)) {
					top.$(childorgId).value = childorgId1;
				}
			}else{
			parentDocument.getElementById(childorgId).value = document.getElementById("childorgId").value;
			}
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

	function checkAll(){
		var groupCheckBoxs=document.getElementsByName("checkCode");
		if(!groupCheckBoxs) return;
		for(var i=0;i<groupCheckBoxs.length;i++)
		{
			groupCheckBoxs[i].checked=true;
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
             MyDivAlert("请选择配件！");
        }else{
	        var codes = "";
	        var OldCodes = "";
	        var names = "";
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
				        if(OldCodes)
				        OldCodes += "," + arr[2];
				    	else
				    	OldCodes = arr[2];
				        if(names)
				        	names += "," + arr[3];
				    	else
				    		names = arr[3];
				    }    
				}				
			}
	
			if(codes && codes.length > 0){
			   $('PART_CODE').value = codes;
			}   
			if(OldCodes && OldCodes.length > 0){
			   $('PART_OLDCODE').value = OldCodes;
			}   
			if(names && names.length > 0){
			   $('PART_CNAME').value = names;
			}   
			if(ids && ids.length > 0){
			   $('PART_ID').value = ids;
			}
			_hide();
		}
	}
</script>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>

<!-- created by lishuai103@yahoo.com.cn 20100603 通用选择供应商 -->
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
    String PROVINCE = request.getParameter("PROVINCE");
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
		loadOrgList();
<%-- 		MyAlert(<%=PROVINCE%>); --%>
<%-- 		document.getElementById("PROVINCE").value = '<%=PROVINCE%>'; --%>
<%-- 		changeOrg(<%=ROOT_ORG_ID%>); --%>
<%-- 		MyAlert(<%=ROOT_ORG_ID%>); --%>
		var orgId = document.getElementById("PROVINCE");
// 		MyAlert(orgId.length);
		for (var int = 0; int < orgId.length; int++) {
// 			MyAlert(orgId[int].value);
			if(orgId[int].value==<%=PROVINCE%>){
// 				MyAlert(orgId[int].value);
				orgId[int].selected=true;
				break;
			}
		}
		
// 		format(val, fmt);
		__extQuery__(1);
	}
</script>
</head>
<body onload="_genPro__('PROVINCE','','');">
<form method="post" name ="fm" id="fm">
	<input id="isShow" name="isShow" type="hidden" value="true"/>	
	<input id="disIds_" name="disIds_" type="hidden" value=""/>	
	<input id="disIds_o" name="disIds_o" type="hidden" value=""/>	
	<input id="YIELDLY" name="YIELDLY" type="hidden" value=""/>	
	
	<table class="table_edit">
<!-- 	<tr> -->
<!-- 	</tr> -->
<!-- 			  <td align="right">大区：</td>  -->
<!-- 		  <td align="left"> -->
<!-- 			<select id="__large_org" name="__large_org" onchange="changeOrg(this.value)"></select> -->
<!-- 			</td>   -->
<!-- 			<td align="right">省份：</td>  -->
<!-- 		  	<td align="left" colspan="3"> -->
<!-- 				<select id="__province_org" name="__province_org"><option value="">==请选择==</option></select> -->
<!-- 			</td> -->
    <tr>
     <td align="right">省份：</td>  
		    <td class="table_query_2Col_input">
	  		<select class="min_sel" id="PROVINCE" name="PROVINCE"></select>
     </td> 
      <td>
      	<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1);" >
      </td>
  	</tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<form name="form1">
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
	var myPage;
	var url = "<%=contextPath%>/sales/storage/storagebase/LogisticsManage/cityQuery.json";
	//设置表格标题
	var title= null;
	//设置列名属性
	var columns = [
	                {header: "序号", align:'center', renderer:getIndex,width:"70"},
	                {header: "选择", dataIndex: 'REGION_CODE', width:"10px",renderer:seled},
					{header: "地市级",dataIndex: 'CITY_NAME',width:"50px"}
			      ];  
	function seled(value,meta,record){
        return "<input type='checkbox' name='checkCode' id='checkCode' value='"+ value + "||"+ record.data.CITY_NAME + "'/>";
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
             MyDivAlert("请选择区域！");
        }else{
			var ids = "";
			var codes = "";
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
			document.getElementById("isShow").value="false";
			parentContainer.showDisId(ids,codes);
			_hide();
		}
	}
</script>
</body>
</html>
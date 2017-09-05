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
   // String disIds_o = request.getParameter("disIds_o");//已有的ID
    
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>通用选择框</title>

<script language="JavaScript">

	function doInit()
	{
		var dis=parentContainer.getDisValues();
		var yieldly=document.getElementById("YIELDLY");
		yieldly.value=parentContainer.document.getElementById("YIELDLY").value
		document.getElementById("disIds_o").value=dis;
		__extQuery__(1);
	}
</script>
</head>
<body onload="genLocSel('txt1','','');">
<form method="post" name ="fm" id="fm">
	<input id="isShow" name="isShow" type="hidden" value="true"/>	
	<input id="disIds_" name="disIds_" type="hidden" value=""/>	
	<input id="disIds_o" name="disIds_o" type="hidden" value=""/>	
	<input id="YIELDLY" name="YIELDLY" type="hidden" value=""/>	
	
	<table class="table_edit">
	<tr>
		<td colspan=2><font color="red">注:只有维护了城市里程数的省份才能分派给物流商</font></td>
	</tr>
    <tr>
     <td align="right">省份：</td>  
		    <td class="table_query_2Col_input">
	  		<select class="min_sel" id="txt1" name="PROVINCE" onchange="_genCity(this,'txt2')"></select>
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
	var url = "<%=contextPath%>/sales/storage/storagebase/LogisticsManage/cityMileageQuery.json";
	//设置表格标题
	var title= null;
	//设置列名属性
	var columns = [
	                {header: "序号", align:'center', renderer:getIndex,width:"70"},
	                {header: "选择", dataIndex: 'DIS_ID', width:"10px",renderer:seled},
					{header: "省份",dataIndex: 'PROVINCE_NAME',width:"40px"},
					{header: "地市级",dataIndex: 'CITY_NAME',width:"50px"}
			      ];  
	function seled(value,meta,record){
        return "<input type='checkbox' name='checkCode' id='checkCode' value='"+ value + "' />";
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
	        var oldIds=document.getElementById("disIds_o").value;
			document.getElementById("isShow").value="false";
			parentContainer.showDisId(oldIds,ids);
			_hide();
		}
	}
</script>
</body>
</html>
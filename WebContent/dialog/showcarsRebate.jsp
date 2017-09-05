<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>

<!-- created by lishuai103@yahoo.com.cn 20100603 通用选择车系明细 -->
<html xmlns="http://www.w3.org/1999/xhtml">
<%
/**
 * 车系弹出层
 * inputId   : 回填页面物料组code域id
 * inputName ：回填页面物料组name域id
 * isMulti   : true值多选，否则单选
 * areaId   : 业务范围id
 * ids   : 已选、需要过滤掉的物料id
 */
	String contextPath = request.getContextPath();
    String inputId = request.getParameter("INPUTID");
    String inputName = request.getParameter("INPUTNAME");
    String isMulti = request.getParameter("ISMULTI");
    String areaId = request.getParameter("areaId");
    String ids = request.getParameter("ids");
    String groupLevel = "2";
    
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>通用选择框</title>

<script language="JavaScript">
var isMulti = "<%=isMulti%>";

	function doInit()
	{	
		__extQuery__(1);
	}
	function rebateDialogInit(){
    	if(isMulti == "true")
	    	document.getElementById("form1").style.display = "";
    	else
    	{
    		document.getElementById("form1").style.display = "none";
    	}	
	}
<%-- 	function customerFunc(){
		var chk = document.getElementsByName("checkCode");//所有
		
		var dis=document.getElementById("disIds_o").value='<%=disIds_o==null?"":disIds_o%>';
		var array=new Array();
		if(dis!=null){
			array=dis.split(",");
			if(array.length>0){
				for(var i=0;i<array.length;i++){
					for(var j=0;j<chk.length;j++)
					{    
						if(array[i]!='' && array[i]==chk[j].value){
							chk[j].checked = true;
						}
					}
				}
			}
		}
		
} --%>
</script>
</head>
<body onload="rebateDialogInit()">
<form method="post" name ="fm" id="fm">
	<input id="disIds_" name="disIds_" type="hidden" value=""/>	
	<input id="disIds_o" name="disIds_o" type="hidden" value="<%=ids%>"/><!--已选  -->	
	<table class="table_edit">
    <tr>
     <td align="right">车系编码：</td>  
	<td class="table_query_input"   nowrap="nowrap">
					<input id="groupCode" name="groupCode" class="middle_txt" type="text" />				
	</td>
	     <td align="right">车系名称：</td>  
	<td class="table_query_input"   nowrap="nowrap">
					<input id="groupName" name="groupName" class="middle_txt" type="text" />				
	</td>
	
	

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
	var url = "<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoApplication/carSalesRebateInfo.json";
	//设置表格标题
	var title= null;
	//设置列名属性
	var columns = [
	           
	                {header: "选择", dataIndex: 'SERIES_ID', width:"10px",renderer:seled},
	                {header: "车系编码",dataIndex: 'SERIES_CODE',width:"40px"},
	                {header: "车系",dataIndex: 'SERIES_NAME',width:"40px"}
	
			      ];  
	function seled(value,meta,record){
    	if(isMulti == "true"){
            return "<input type='checkbox' name='checkCode' id='checkCode' value='"+ value + "' />";
    	} else {
            return "<input type='radio' name='checkCode' id='checkCode' value='"+ record.data.SERIES_NAME + "' onclick='doConfirm()'/>";
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
             MyDivAlert("请选择车系！");
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
	 
			if(ids && ids.length > 0){
			   $('disIds_').value = ids;
			}
			parentContainer.showSalesDisId(ids);
			_hide();
		}
	}
</script>
</body>
</html>
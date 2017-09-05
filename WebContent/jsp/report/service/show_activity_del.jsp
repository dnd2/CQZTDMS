<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>

<!-- created by lishuai103@yahoo.com.cn 20100603 通用选择车系明细 -->
<html xmlns="http://www.w3.org/1999/xhtml">
<%
/**
 * 
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
<script type="text/javascript" src="${contextPath}/js/web/jquery-1.7.2.min.js"></script>

<title>通用选择框</title>

<script language="JavaScript">

	function doInit()
	{
		__extQuery__(1);
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
<body onload="">
<form method="post" name ="fm" id="fm">
	<input id="disIds_" name="disIds_" type="hidden" value=""/>	
	<input id="disIds_o" name="disIds_o" type="hidden" value="<%=ids%>"/><!--已选  -->	
	<table class="table_edit">
    <tr>
	
	        <td class="table_query_2Col_label_5Letter">活动代码：</td>
        <td align="left">
            <input name="ACTIVITY_CODE" id="ACTIVITY_CODE" type="text" class="middle_txt" size="5" />
        </td>
      <td class="table_query_2Col_label_5Letter">活动名称：</td>
        <td align="left">
            <input name="ACTIVITY_NAME" id="ACTIVITY_NAME" type="text" class="middle_txt" size="5" />
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
	var url = "<%=contextPath%>/report/service/TechnologyUpgrade/queryActivity.json";
	//设置表格标题
	var title= null;
	//设置列名属性
	var columns = [
	           
	                
					{id:'action',header: "选择",sortable: false,dataIndex: 'ACTIVITY_ID',renderer:seled,align:'center'},
					{header: "活动代码", dataIndex: 'ACTIVITY_CODE', align:'center'},
					{header: "活动名称", dataIndex: 'ACTIVITY_NAME', align:'center'},
					{header: "活动类型", dataIndex: 'ACTIVITY_TYPE', align:'center',renderer:getItemValue}
	
			      ];  
	function seled(value,meta,record){
        return "<input type='checkbox' name='checkCode' id='checkCode' value='"+ value + "' /><input type='hidden' name='activityCode' id='activityCode' value='"+ record.data.ACTIVITY_CODE + "' />";
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
		var activityCode = document.getElementsByName("activityCode");
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
             MyDivAlert("请选择活动！");
        }else{
	        var codes = "";
			var ids = "";
			var aCode = "";
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
		         
					   aCode+=activityCode[i].value+",";	

	
				    }
					
		
				}
				
				
				
			}
	 
			if(ids && ids.length > 0){
			   $('disIds_').value = ids;
			}
			var inputName = "<%=inputName%>";
			parentDocument.getElementById(inputName).value="";
			parentDocument.getElementById(inputName).value = ids;
			parentDocument.getElementById("activityNo").value="";
			parentDocument.getElementById("activityNo").value = aCode.substring(0,aCode.length-1);

			_hide();
		}
	}
</script>
</body>
</html>
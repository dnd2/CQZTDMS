<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	String sqlName = request.getParameter("sqlName");
	String callFunc = request.getParameter("callFunc");
	String paraJson = request.getParameter("paraJson");
	//String checkJson = new String(request.getParameter("checkJson").getBytes("ISO8859-1"), "UTF-8");	
	//System.out.println("-------checkJson="+checkJson);
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查询</title>
</head>


<body onload ="__extQuery__(1)">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input type='hidden' name='sqlName' id='sqlName' value='<%=sqlName%>'/>
<input type='hidden' name='paraJson' id='paraJson' value='<%=paraJson%>'/>
    <table class="table_query" >
	    <tr id="groupId">
	        <td width="20%">&nbsp;</td>
	        <td width="20%">查询条件：</td>
	        <td width="40%"><input type="text" id="para" name="para" jset="para"></td>	        
	        <td width="20%"><input name="queryBtn" id="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询"/></td>	        	        
        </tr>
    </table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<table border="0" class="table_query">
  <tr>
    <td colspan="4" align="center">
    <input class="normal_btn" type="button" value="确定" onclick="conf();"/>
    <input class="normal_btn" type="button" value="关闭" onclick="_hide();"/>
    </td>
  </tr>
</table>
</form>
</body>
<script type="text/javascript">
	var myPage;
	var url = "<%=contextPath%>/common/PopSelectManager/getResult.json";
	var title = null;
	var columns = [
				{header: "<input type='checkbox' name='checkAll' onclick='checkAllBox(this,\"ck\")' />",align:'center',dataIndex:'ID',renderer:getCheck},
				{header: "省份（服务商）",dataIndex:'DESCR',style:'text-align:left;'}
				];
    //var ckjson={};
	var ckjson=JSON.parse(parentDocument.getElementById('checkJson').value);
	
	function getCheck(value, meta, record){      
		if(ckjson[value]){
			var formatString = "<input type='checkbox' checked value="+value+" data="+JSON.stringify(record.data)+" name='ck' id='ck' onclick='check(this);'/>";			
		}else{
			var formatString = "<input type='checkbox' value="+value+" data="+JSON.stringify(record.data)+" name='ck' id='ck' onclick='check(this);'/>";
		}		
		return String.format(formatString);
	}
	
    /*checkbox触发函数*/
	function check(obj){
		if(obj.checked){		
		    ckjson[obj.value]=obj.getAttribute("data");
	    }else{
	    	delete ckjson[obj.value];
	    }
	}

    /*确定之后执行*/
	function conf(){
		var arrayJSON = new Array();
		for(var value in ckjson){
			if(typeof(ckjson[value])=="string"){	
			    arrayJSON.push(JSON.parse(ckjson[value]));
			}
			if(typeof(ckjson[value])=="object"){	
			    arrayJSON.push(ckjson[value]);
			}			
		}
		parentContainer.<%=callFunc%>(arrayJSON);
		_hide();
	}

	//全选
	function checkAllBox(checkObj,checkBoxName){
	    window.event.cancelBubble = true;
		var allChecks = document.getElementsByName(checkBoxName);
		if(checkObj.checked){
			for(var i = 0;i<allChecks.length;i++){
				allChecks[i].checked = true;
				check(allChecks[i]);
			}
		}else{
			for(var i = 0;i<allChecks.length;i++){
				allChecks[i].checked = false;
				check(allChecks[i]);
			}
		}
	}	
		
</script>
</html>

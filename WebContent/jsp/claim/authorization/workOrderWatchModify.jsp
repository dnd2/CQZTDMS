<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%
	String contextPath = request.getContextPath();
	List levellist = (List)request.getAttribute("LEVELLIST");
	HashMap map = (HashMap)request.getAttribute("SELMAP");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>修改工单授权信息</title> 
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="wbox">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔授权管理&gt;工单预授权维护</div>
<form name='fm' id='fm'>
<input type="hidden" name="NUM" id="NUM" value='<%=map.get("NUM")==null?"":map.get("NUM")%>'/>
<div class="form-panel">
				<h2>基本信息</h2>
					<div class="form-body">
		  <table class="table_query">
			    <tr>
			    	<td  style="text-align: right">维修类型： </td>
			       <td>
			       <%=map.get("CODE_DESC")==null?"":map.get("CODE_DESC")%>
			       </td>
			    </tr>
			    </table>
			    </div>
			    </div>
			    <div class="form-panel">
				<h2>授权信息</h2>
					<div class="form-body">
			    <table class="table_list">
		  <% for(int i=0;i<levellist.size();i++){ 
			  HashMap temp = (HashMap)levellist.get(i);
			  boolean flag = false;
			  if(temp.get("APPROVAL_LEVEL_CODE").equals(map.get("S"+temp.get("APPROVAL_LEVEL_CODE")))){
				  flag = true;
			  }
		  %>
		  <%if(i%3==0){%><tr><%}%>
				<td><input type="checkbox"  name="<%=temp.get("APPROVAL_LEVEL_CODE")%>" value="<%=temp.get("APPROVAL_LEVEL_CODE")%>" <%if(flag)out.print("checked"); %> />
				<%=temp.get("APPROVAL_LEVEL_NAME")%></td>
		   <%if(i%3==2){%></tr><%}%> 	
		  <%} %>
		  </table>
		  <table class="table_query">
			<tr> 
		     	 <td style="text-align:center">
		        <input name="ok" type="button" class="normal_btn" id="commitBtn"  value="确定"  onclick="checkFormUpdate();"/>
		        <input name="back" type="button" class="normal_btn" value="返回" onclick="_hide() ;"/>
		        </td>
		    </tr>
		  </table>
		  </div>
		  </div>
	</form>
	</div>
<script>

	//表单提交方法：
	function checkForm(){
			document.getElementById("commitBtn").disabled =true ;
			makeNomalFormCall('<%=contextPath%>/claim/authorization/ClaimPartWatchMain/workOrderWatchUpdate.json',showResult,'fm');			
	}
	function goBack(){
		history.go(-1);
	}
	function showResult(json){
		if(json.success != null && json.success == "true"){
		MyAlert("修改成功!");
		__parent().__extQuery__(1) ;
		_hide() ;
			<%-- window.location.href = "<%=contextPath%>/claim/authorization/ClaimPartWatchMain/autoOrderInit.do"; --%>
		}else{
			MyAlert("修改失败，请联系管理员！");
		}
	}
	//表单提交前的验证：
	function checkFormUpdate(){
		if(!submitForm('fm')) {
			return false;
		}
		 var selectvalue="";
		 var itemlength=fm.elements.length;
		  for(var k=0;k<parseInt(itemlength);k++){		
		        if(fm.elements[k].type=="checkbox"&&fm.elements[k].checked)
		        {
		          selectvalue=selectvalue + fm.elements[k].value +"," ;		          
		        }
		  }
		  if(selectvalue.length>1){
		  	selectvalue = selectvalue.substring(0,selectvalue.length-1);
		  }else{
		  	MyAlert("您至少选择一种授权级别");
		  	return false;
		  }					
		MyConfirm("是否确认修改?",checkForm);
	}
	//页面跳转：
	function sendPage(){
		window.location.href = "<%=contextPath%>/claim/authorization/ClaimPartWatchMain/autoOrderInit.do";
	}	
function checkAll(obj){
	 var itemlength=fm.elements.length;
		  for(var k=0;k<parseInt(itemlength);k++){		
		        if(fm.elements[k].type=="checkbox"&&fm.elements[k].value<obj.value)
		        {
		         fm.elements[k].checked = obj.checked;       
		        }
		  }
	}
</script>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%
	String contextPath = request.getContextPath();
	List levellist = (List)request.getAttribute("LEVELLIST");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>新增索赔授权监控配件大类</title> 
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔授权管理&gt;监控配件大类维护</div>
<form name='fm' id='fm'>
<input type="hidden" name="wrmodelgrouplist" id="wrmodelgrouplist" value="<%=request.getAttribute("wrmodelgrouplist")%>"/>
<input id="PARTTYPE_NAME" name="PARTTYPE_NAME" type="hidden" />
<input id="PARTTYPE_ID" name="PARTTYPE_ID" type="hidden" />
		  <table class="table_edit">
			    <tr>
			    	<td  style="color: #252525;width: 115px;text-align: right">配件大类代码： </td>
			       <td>
			       <input  class="middle_txt" id="PARTTYPE_CODE"  name="PARTTYPE_CODE" type="text" datatype="0,is_null,27"/>
			       <a href="#" onclick="sel();">选择</a>
			       </td>
			    </tr>
		    <tr>
		    	<td  style="color: #252525;width: 115px;text-align: right" rowspan="<%=levellist.size()/3+2%>">授权级别： </td>
		    </tr>
		  <% for(int i=0;i<levellist.size();i++){ 
			  HashMap temp = (HashMap)levellist.get(i);
		  %>
		  <%if(i%3==0){%><tr><%}%>
				<td><input type="checkbox" name="<%=temp.get("APPROVAL_LEVEL_CODE")%>" value="<%=temp.get("APPROVAL_LEVEL_CODE")%>" />
				<%=temp.get("APPROVAL_LEVEL_NAME")%></td>
		   <%if(i%3==2){%></tr><%}%> 	
		  <%} %>
		 <%
			for(int j=levellist.size();j<levellist.size()+3;j++){
				if(j%3==0)break;
	   		 %>
	   		 <%}%>
			<tr> 
		     	 <td colspan="6" align="center">
		        <input name="ok" type="button" class="normal_btn" id="commitBtn"  value="确定"  onclick="checkFormUpdate();"/>
		        <input name="back" type="button" class="normal_btn" value="返回" onclick="sendPage();"/>
		        </td>
		    </tr>
		  </table>
	</form>
<script>

//选择页面
function sel(){
	OpenHtmlWindow('<%=contextPath%>/claim/authorization/ClaimPartWatchMain/claimPartBigSelect.do',900,500);
}
	//表单提交方法：
	function checkForm(){
			makeFormCall('<%=contextPath%>/claim/authorization/ClaimPartWatchMain/claimPartBigWatchAdd.json',showResult,'fm');			
	}
	function goBack(){
		history.go(-1);
	}
	function showResult(json){
		if(json.errorExist != null && json.errorExist.length > 0){
			MyAlert("配件代码：【"+json.errorExist+"】系统已存在！");
		}else if(json.success != null && json.success == "true"){
			disableBtn($("commitBtn"));
			window.location.href = "<%=contextPath%>/claim/authorization/ClaimPartWatchMain/claimPartWatchBigInit.do";
		}else{
			disableBtn($("commitBtn"));
			MyAlert("新增失败，请联系管理员！");
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
		MyConfirm("是否确认新增?",checkForm);
	}
	//页面跳转：
	function sendPage(){
		window.location.href = "<%=contextPath%>/claim/authorization/ClaimPartWatchMain/claimPartWatchBigInit.do";
	}
</script>
</body>
</html>
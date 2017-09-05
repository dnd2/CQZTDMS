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
<title>新增索赔授权监控工时</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="wbox">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔授权管理&gt;监控工时维护</div>
<form name='fm' id='fm'>
<div class="form-panel">
		<h2>基础信息</h2>
			<div class="form-body">
<input type="hidden" name="wrmodelgrouplist" id="wrmodelgrouplist" value="<%=request.getAttribute("wrmodelgrouplist")%>"/>
<input id="LABOUR_OPERATION_NAME" name="LABOUR_OPERATION_NAME" type="hidden" />
		  <table class="table_query">
			    <tr >
			      <td style="text-align: right">索赔工时车型组：</td>
			      <td> 
					  <script type="text/javascript">
			              var wrmodelgrouplist=document.getElementById("wrmodelgrouplist").value;
			    	      var str="";
			    	      str += "<select id='WRGROUP_ID' name='WRGROUP_ID' onchange=\"clrLable() ;\"  datatype='0,is_null,18' class='u-select'>";
			    	      str+=wrmodelgrouplist;
			    		  str += "</select>";
			    		  document.write(str);
				       </script>
			      </td>
			    </tr>
			    <tr>
			    	<td  style="text-align: right">工时代码： </td>
			       <td>
			       <input  class="middle_txt" id="LABOUR_OPERATION_NO" readonly="readonly" datatype='0,is_null,50'  onclick="sel();" name="LABOUR_OPERATION_NO" type="text" />
			       <!-- <a href="#" onclick="sel();">选择</a> -->
			       </td>
			    </tr>
			    </table>
			    </div>
			    </div>
			    <div class="form-panel">
			<h2>授权信息</h2>
				<div class="form-body">
					<table class="table_list">
						<tr>
							<% for(int i=0;i<levellist.size();i++){ 
								HashMap temp = (HashMap)levellist.get(i);
								%>
								<td><input type="checkbox"  name="<%=temp.get("APPROVAL_LEVEL_CODE")%>" value="<%=temp.get("APPROVAL_LEVEL_CODE")%>"  >
								<%=temp.get("APPROVAL_LEVEL_NAME")%></td>
								<%}%>
							</tr>
		    		</table>
		    <%-- <tr>
		    	<td  style="text-align: right">授权级别： </td>
		    </tr>
		  <% for(int i=0;i<levellist.size();i++){ 
			  HashMap temp = (HashMap)levellist.get(i);
		  %>
		  <%if(i%3==0){%><tr><%}%>
				<td><input type="checkbox"  name="<%=temp.get("APPROVAL_LEVEL_CODE")%>" value="<%=temp.get("APPROVAL_LEVEL_CODE")%>" />
				<%=temp.get("APPROVAL_LEVEL_NAME")%></td>
		   <%if(i%3==2){%></tr><%}%> 	
		  <%} %>
		 <%
			for(int j=levellist.size();j<levellist.size()+3;j++){
				if(j%3==0)break;
	   		 %>
	   		 <tr>
	     	    <td style="text-align: right"></td>
	         </tr>
	   		 <%}%> --%>
	   		 <table class="table_query">
			<tr> 
		     	 <td style="text-align: center">
		        <input name="ok" type="button" class="normal_btn" id="commitBtn"  value="确定"  onclick="checkFormUpdate();"/>
		        <input name="back" type="button" class="normal_btn" value="返回" onclick="sendPage();"/>
		        </td>
		    </tr>
		  </table>
		  </div>
		  </div>
<script>
function clrLable() {
	document.getElementById("LABOUR_OPERATION_NO").value = "" ;
	 ocument.getElementById("LABOUR_OPERATION_NAME").value = "" ;
}
//选择页面
function sel(){
    var groupid = document.getElementById('WRGROUP_ID').value;
	OpenHtmlWindow('<%=contextPath%>/claim/authorization/ClaimLaborWatchMain/claimLaborSelect.do?ID='+groupid,900,500);
}
	//表单提交方法：
	function checkForm(){
<%--			disableBtn($("commitBtn"));--%>
		makeNomalFormCall('<%=contextPath%>/claim/authorization/ClaimLaborWatchMain/claimLaborWatchAdd.json',showResult,'fm');			
	}
	function goBack(){
		history.go(-1);
	}
	function showResult(json){
		if(json.errorCode != null && json.errorCode.length > 0){
			MyAlert("工时代码：【"+json.errorCode+"】和所选车型组没有绑定！");
		}else if(json.errorExist != null && json.errorExist.length > 0){
			MyAlert("工时代码：【"+json.errorExist+"】系统已存在！");
		}else if(json.success != null && json.success == "true"){
		MyAlert("新增成功!");
			document.getElementById("commitBtn").disabled = true ;
			window.location.href = "<%=contextPath%>/claim/authorization/ClaimLaborWatchMain/claimLaborWatchInit.do";
		}else{
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
		//FRM.APPROVAL_LEVEL.value=selectvalue;			
		MyConfirm("是否确认新增?",checkForm);
	}
	//页面跳转：
	function sendPage(){
		window.location.href = "<%=contextPath%>/claim/authorization/ClaimLaborWatchMain/claimLaborWatchInit.do";
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
	</form>
	</div>
</body>
</html>
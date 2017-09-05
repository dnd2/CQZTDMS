<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.HashMap"%>
<%
	String contextPath = request.getContextPath();
	HashMap map = (HashMap)request.getAttribute("SELMAP");
	String aflag = String.valueOf(request.getAttribute("auditFlag"));
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>修改授权人员管理</title> 
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="wbox">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔授权管理&gt;授权人员管理</div>
<form name='fm' id='fm'>
<div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
<input type="hidden" name="ID" id="ID" value='<%=map.get("USER_ID")==null?"":map.get("USER_ID")%>'/>
<input type="hidden" name="levellist" id="levellist" value="<%=request.getAttribute("levellist")%>"/>
<input type="hidden" name="auditFlag" id="auditFlag" value="<%=request.getAttribute("auditFlag") %>"/>
		  <table class="table_query">
			    <tr >
			      <td class="table_edit_2Col_label_4Letter" style="text-align:right">员工姓名：</td>
			      <td>
			      	<input type="hidden" name="wrgroup_id" id="wrgroup_id" value='<%=map.get("NAME")==null?"":map.get("NAME")%>'/>
			      	<%=map.get("NAME")==null?"":map.get("NAME")%>
			      </td>
			    </tr>
			    <tr>
			    	<td  class="table_edit_2Col_label_4Letter" style="text-align:right">授权级别： </td>
			       <td>
					   <script type="text/javascript">
			              var levellist = document.getElementById("levellist").value;
			    	      var str="";
			    	      str += "<select id='APPROVAL_LEVEL_CODE' name='APPROVAL_LEVEL_CODE' onchange='changeCase(this.value);'  class='u-select'>";
			    	      str+="<option value='000'>--请选择--</option>";
			    	      str+= levellist;
			    		  str += "</select>";
			    		  document.write(str);
				       </script>			       
			       </td>
			    </tr>
			    <%
			    	if(aflag.equals(String.valueOf(Constant.AUDIT_TYPE_01))){
			    %>
			    <tr>
			    	<td  class="table_edit_2Col_label_4Letter" style="text-align:right">授权代码： </td>
			       <td>
			       	<input type="text" name="PERSON_CODE" id="PERSON_CODE" class="middle_txt" datatype="0,is_digit,4" readonly="readonly"   value='<%=map.get("PERSON_CODE")==null?"":map.get("PERSON_CODE")%>'/>
			       </td>
			    </tr>	
			    <%
			    	}
			    %>		    
			<tr> 
		     	 <td colspan="4" style="text-align:center">
		        <input name="ok" type="button" class="normal_btn" id="commitBtn"  value="确定"  onclick="checkFormUpdate()"/>
		        <input name="back" type="button" class="normal_btn" value="返回" onclick="_hide() ;"/>
		        </td>
		    </tr>
		  </table>
		  </div>
		  </div>
<script>

	//表单提交方法：
	function checkForm(){
			document.getElementById("commitBtn").disabled = true ;
			makeNomalFormCall('<%=contextPath%>/claim/authorization/ClaimManMain/claimManUpdate.json',showResult,'fm');			
	}
	function goBack(){
		history.go(-1);
	}
	function showResult(json){
		if(json.success != null && json.success == "true"){
			MyAlert("修改成功!");
			__parent().__extQuery__(1) ;
			_hide() ;
		}else{
			MyAlert("修改失败，请联系管理员！");
		}
	}
	//表单提交前的验证：
	function checkFormUpdate(){
		if(!submitForm('fm')) {
			return false;
		}	
		if($('APPROVAL_LEVEL_CODE').value=='000'){
			MyConfirm("确认取消该人员级别?",checkForm);
		}else{		
			MyConfirm("是否确认修改?",checkForm);
		}
	}
	function changeCase(value){
	$('#PERSON_CODE')[0].value=value;
	}
</script>
	</form>
	</div>
</body>
</html>
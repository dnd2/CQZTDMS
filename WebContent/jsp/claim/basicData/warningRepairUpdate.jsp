<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%
	String contextPath = request.getContextPath();
	List levellist = (List)request.getAttribute("LEVELLIST");
	HashMap map = (HashMap)request.getAttribute("SELMAP");
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>三包预警规则设置</title> 
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="wbox">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基本数据&gt;三包预警规则设置&gt;三包预警规则设置修改</div>
<form method="post" name='fm' id='fm'>
<div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
<input type="hidden" name="waningId" id="waningId" value="<%=map.get("WARNING_REPAIR_ID")%>" />
		  <table class="table_query">
		  	<tr>
	            <td style="text-align:right">预警类别：</td>
	            <td>
	                 <script type="text/javascript">
	                 	document.write(getItemValue(<%=map.get("WARNING_TYPE")%>));
		             </script>
	            </td>
	            
	            <%if(map.get("WARNING_TYPE").toString().equals("69991001")){ %>
		            <td style="text-align:right">总成：</td>
		            <td>
		                 <%=map.get("CHANGE_NAME") %>
		            </td>
		         <%}else if(map.get("WARNING_TYPE").toString().equals("69991002")){ %>
		            <td style="text-align:right">严重安全故障模式：</td>
		            <td>
		                 <%=map.get("CHANGE_NAME") %>
		            </td>
		         <%}else{ %>
		          
		         <%} %>
	            
	            <td style="text-align:right">状态：</td>
	            <td>
					<script type="text/javascript">
		              genSelBoxExp("STATUS",<%=Constant.STATUS%>,"<%=map.get("STATUS")%>",false,"","","true",'');
		            </script>
	            </td>
          </tr>
          
          <tr>
            <td style="text-align:right">预警等级：</td>
            <td>
				<script type="text/javascript">
	              genSelBoxExp("WAINING_LEVEL",<%=Constant.SWANINGTIME_LEVEL%>,"<%=map.get("WAINING_LEVEL")%>",false,"","","true",'');
	            </script>
            </td>
            <td style="text-align:right">是否累计：</td>
            <td>
				<script type="text/javascript">
	              genSelBoxExp("IS_ACCUMULATIVE",<%=Constant.IF_TYPE%>,"<%=map.get("IS_ACCUMULATIVE")%>",false,"","","true",'');
	            </script>
            </td>
            <td style="text-align:right">规则代码：</td>
            <td>
                 <%=map.get("WARNING_CODE") %>
            </td>
          </tr>
          <tr>
            <td style="text-align:right">预警说明：</td>
            <td>
				<input name="WAINING_REMARK" id="WAINING_REMARK" type="text" class="middle_txt" value="<%=map.get("WAINING_REMARK")%>"/>
            </td>
            <td style="text-align:right">预警起次数：</td>
            <td>
            	<input name="WARNING_NUM_START" id="WARNING_NUM_START" datatype="0,is_null,30"  type="text" class="middle_txt" value="<%=map.get("WARNING_NUM_START")%>"/>
            </td>
            <td style="text-align:right">预警止次数：</td>
            <td>
				<input name="WARNING_NUM_END" id="WARNING_NUM_END" datatype="0,is_null,30"  type="text" class="middle_txt" value="<%=map.get("WARNING_NUM_END")%>"/>
            </td>
          </tr>
          <tr>
           <td style="text-align:right">有效起时限(月)：</td>
            <td>
            	<input name="VALID_START_DATE" id="VALID_START_DATE" datatype="0,is_null,30"  type="text" class="middle_txt" value="<%=map.get("VALID_START_DATE")%>"/>
            </td>
            <td style="text-align:right">有效止时限(月)：</td>
            <td>
            	<input name="VALID_DATE" id="VALID_DATE" datatype="0,is_null,30"  type="text" class="middle_txt" value="<%=map.get("VALID_DATE")%>"/>
            </td>
            <td style="text-align:right">有效起里程：</td>
            <td>
				<input name="VALID_START_MILEAGE" id="VALID_START_MILEAGE" datatype="0,is_null,30"  type="text" class="middle_txt" value="<%=map.get("VALID_START_MILEAGE")%>"/>
            </td>
           </tr>
           <tr>
            <td style="text-align:right">有效止里程：</td>
            <td>
				<input name="VALID_MILEAGE" id="VALID_MILEAGE" datatype="0,is_null,30"  type="text" class="middle_txt" value="<%=map.get("VALID_MILEAGE")%>"/>
            </td>
          </tr>
          <tr>
            <td style="text-align:right">法规条款：</td>
            <td colspan="5">
            	<textarea id="CLAUSE_STATUTE" name="CLAUSE_STATUTE" datatype="0,is_null,1000" rows="3" cols="80"><%=map.get("CLAUSE_STATUTE")%></textarea>
            </td>
          </tr>
          </table>
          </div>
          </div>
          <div class="form-panel">
			<h2>授权级别</h2>
				<div class="form-body">
          			<table class="table_list">
          <tr>
            <!-- <td style="text-align:right">授权级别：</td> -->
            	<% for(int i=0;i<levellist.size();i++){ 
			  		HashMap temp = (HashMap)levellist.get(i);
			  		boolean flag = false;
					  if(temp.get("APPROVAL_LEVEL_CODE").equals(map.get(temp.get("APPROVAL_LEVEL_CODE")))){
						  flag = true;
					  }
		  		%>
		  		
            	<td ><input type="checkbox" name="<%=temp.get("APPROVAL_LEVEL_CODE")%>" value="<%=temp.get("APPROVAL_LEVEL_CODE")%>" <%if(flag)out.print("checked"); %>/>
				<%=temp.get("APPROVAL_LEVEL_NAME")%>
				</td>
				<%}%>
          </tr>
          </table>
          <table class="table_query">
			<tr> 
		     	 <td colspan="6" style="text-align:center">
		        <input name="ok" type="button" id="commitBtn" class="normal_btn"  value="确定"  onclick="checkForm();"/>
		        <input name="back" type="button" class="normal_btn" value="返回" onclick="_hide();"/>
		        </td>
		    </tr>
		  </table>
		  </div>
		  </div>
	</form>
<script>
//表单提交前的验证：
function checkForm(){
	submitForm('fm') == true ? downloadConfirm() : "";
}
//表单提交方法：
function downloadConfirm(){
	var selectvalue="";
	var itemlength=fm.elements.length;
		for(var k=0;k<parseInt(itemlength);k++){
			if(fm.elements[k].type=="checkbox"&&fm.elements[k].checked){
				selectvalue=selectvalue + fm.elements[k].value +"," ;		          
	        }
	    }
	if(selectvalue.length>1){
	  	selectvalue = selectvalue.substring(0,selectvalue.length-1);
	}else{
	  	MyAlert("您至少选择一种授权级别");
	  	return false;
	}	
	MyConfirm("是否确认修改？",downloadUpdate);
}
function downloadUpdate(){
	document.getElementById("commitBtn").disabled = true ;
	makeNomalFormCall('<%=contextPath%>/claim/basicData/WarningRepairMain/doWaningUpdate.json',updateBack,'fm','');
}
//修改后的回调方法：
function updateBack(json) {
	if(json.success != null && json.success == "true") {
		MyAlert("修改成功 !") ;
    	__parent().__extQuery__(1) ;
    	_hide() ;
	} else {
		MyAlert("修改失败！");
	}
}
//页面跳转
function sendPage(){
	window.location = '<%=contextPath%>/claim/basicData/WarningRepairMain/WarningRepairInit.do';
}
//返回
function onBack(){
    location="<%=contextPath%>/claim/basicData/WarningRepairMain/WarningRepairInit.do";   
}
</script>
</div>
</body>
</html>
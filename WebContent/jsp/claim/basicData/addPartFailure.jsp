<%@page import="com.infodms.dms.po.TtAsWrFaultLegalPO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	TtAsWrFaultLegalPO po = (TtAsWrFaultLegalPO)request.getAttribute("faultList");
%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>零件法定名称维护</title>
</head>
<body>
<div class="wbox">
<script type="text/javascript">
function save(){
		var part_code = document.getElementById("PART_CODE").value;
		if(part_code==null || part_code==''){
			MyAlert("请选择配件!");
			return;
		}else{
			var url="<%=request.getContextPath()%>/claim/basicData/FailureMaintainMain/saveAddParts.json";
			makeNomalFormCall(url,showResult22,'fm');
		}
}
function showResult22(json){
	if(json.flag){
		MyAlert("新增成功");
		location=  "<%=contextPath%>/claim/basicData/FailureMaintainMain/AddPartInfoInit.do?faultId="+json.fId;
	}else{
		MyAlert(json.msg);
	}
}
function selectPart(){
	 var url = '<%=contextPath%>/claim/basicData/WorkRankMain/partCodeInit.do' ;
		OpenHtmlWindow(url,800,550);
}
function setPartBase(code,name){
	 document.getElementById("PART_CODE").value=code;
	 document.getElementById("PART_NAME").value=name;
}
function clearInput(){
	 document.getElementById("PART_CODE").value="";
	 document.getElementById("PART_NAME").value="";
}
</script>

<form name='fm' id='fm' method="post">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基本数据&gt;严重安全性能故障法定名称维护&gt;配件新增</div>
  <div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
   <table  class="table_query">
          <tr>
          	<td style="text-align:right">故障法定代码：</td>
          	<td>
          		<input name = "ID" id="ID" type="hidden" value="<%=po.getFaultId()%>" /><%=po.getFaultCode() %>
          	</td>
          	<td style="text-align:right">故障法定名称：</td>
          	<td><%=po.getFaultName() %>
          </tr>
          <tr>
          	<td style="text-align:right">配件代码：</td>
          	<td>
          	<input type="text" class="middle_txt" name="PART_CODE"  value="" size="10" id="PART_CODE" onclick="selectPart();" readonly />
          	<!-- <input type="button" value="..." class="mini_btn" onclick="selectPart();"/> -->
	        <input type="button" value="清空" class="normal_btn" onclick="clearInput();"/>
          	</td>
          	<td style="text-align:right">配件名称：</td>
          	<td>
          	<input type="text" class="middle_txt" name="PART_NAME"  value="" size="10" id="PART_NAME" readonly />
          	</td>
          </tr>           
		  <tr>    
      		<td colspan="4" style="text-align:center">
            
            <input class="normal_btn" type="button" value="保存" name="add" onclick="save();"/>
            <input type="button"  onclick="javascript:history.go(-1) ;" class="normal_btn" id="backId"  value="返回" />
           </td>
          </tr>
      </table>
      </div>
      </div>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
</form>
</div>
</body></html>
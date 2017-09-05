<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	Long userId = (Long)request.getAttribute("logonUser");
	
%>

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆性质查询</title>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<div class="wbox">
<form name='fm' id='fm' method="post">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 车辆性质变化</div>
<script type="text/javascript" >
var parentContainer = parent || top;
if( parentContainer.frames ['inIframe'] ){
	parentContainer = parentContainer.document.getElementById ('inIframe').contentWindow;
}
var parentDocument =parentContainer.document;
	function add()
	{
		 var code = '';
		 var codeDesc="";
		 var codeBusi="";
		 var codeid = document.getElementsByName('codeids');
		 for(var i = 0 ; i < codeid.length ; i++ )
		 {
		 	 if(codeid[i].checked)
			 {
		 		 var ss = codeid[i].value ;
		 		 var s1 = document.getElementById('codeId'+codeid[i].value).value ;
				 codeDesc = codeDesc+document.getElementById('codeDesc'+codeid[i].value).value+',';
				 code = code+document.getElementById('codeId'+codeid[i].value).value+',';
				 codeBusi = codeBusi + codeid[i].value + ',';
			 }
		 }
		 var codes = code;
		 codes = codes.substring(0, codes.length-1); 
		 codeDesc = codeDesc.substring(0, codeDesc.length-1); 
		 codeBusi = codeBusi.substring(0, codeBusi.length-1); 
		 parentDocument.getElementById('code_id').value = codes;
		 parentDocument.getElementById('code_busi').value = codeBusi;
		 parentDocument.getElementById('codeDesc').value = codeDesc;
		 _hide();
	}
</script>
<div class="form-panel">
					<h2>查询结果</h2>
			<div class="form-body">
  <table class="table_list">
  <tr>
  	<th>选择</th>
  	<th>车辆性质</th>
  </tr>
  <c:forEach var="codeList" items="${codeList}">
  <tr>
  	<td>
  	<c:if test="${codeList.num == 0}"><input type="checkbox" name="codeids"  value="${codeList.codeBusi}" /></c:if>
  	<c:if test="${codeList.num != 0}"><input type="checkbox" checked="checked" name="codeids" id="${codeList.codeBusi}" value="${codeList.codeBusi}" /></c:if>
  	</td>
  	<td>${codeList.codeDesc}
  	<input type="hidden" name='codeBusi' id="codeBusi${codeList.codeBusi}" value="${codeList.codeBusi}"  />
  	<input type="hidden" name='codeDesc' id="codeDesc${codeList.codeBusi}" value="${codeList.codeDesc}"  />
	<input type="hidden" name='codeid' id="codeId${codeList.codeBusi}" value="${codeList.codeId}"  />
  	</td>
  </tr>
  </c:forEach>
  <c:if test="${judetype == 0}">
  <tr>
  <td>
  	<span style="color: red;">你选择的车辆性质已经完成选择了</span>
  </td>
  </tr>
  </c:if>
  <tr>
  <td colspan="2" style="text-align:center">
  <c:if test="${judetype == 0}">
	  
	  	<input type="button" disabled="disabled" class="normal_btn" value="确定" onClick="add();"/>
 </c:if>	
 <c:if test="${judetype != 0}">
	  	<input type="button"  class="normal_btn" value="确定" onClick="add();"/>
 </c:if>
     	<input type="button"  class=normal_btn value="取消" onClick=" _hide();"/>
    </td>	  
  </tr>
  </table>
  </div>
  </div>
</form>
</div>
</body>
</html>
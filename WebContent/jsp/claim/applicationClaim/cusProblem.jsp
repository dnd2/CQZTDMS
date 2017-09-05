<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	String id = request.getParameter("id");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<body onload="loadcalendar();">
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation">
<img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：顾客问题选择 </div>
</div>
<form name="fm" id="fm">
<input class="middle_txt" id="id"  name="id" value="<%=id%>" type="hidden"/>
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">大系统：</td>
      	<td width="15%" nowrap="true">
      		<select class="middle_txt" style="width: 152px;" onchange="vrtCodeChange();" name="VRT_CODE" id="VRT_CODE">				 
	              <c:forEach var="Cus1" items="${listCus1}" >
 				  <option value="${Cus1.VRT_CODE}" >
    				<c:out value="${Cus1.VRT_NAME}"/>
    			  </option>
    			 </c:forEach>
             </select>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">子系统：</td>
      	<td width="15%" nowrap="true">
      		<select class="middle_txt" style="width: 152px;" name="VFG_CODE" id="VFG_CODE">				 
             </select>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">顾客问题：</td>
		<td width="15%" nowrap="true">
			<input class="middle_txt" id="CCC_NAME"  name="CCC_NAME" maxlength="30" type="text"/>
		</td>
	</tr>
	<tr>
    	<td style="text-align: center;"colspan="6">
    		<input type="button" name="btnQuery" id="btnQuery" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    		<input name="button2" type="button" class="normal_btn" onclick="parent._hide();" value="关 闭" />
    	</td>
    </tr>
</table>
<!-- 查询条件 end -->

<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
</html>
<script language="JavaScript" >
 $(document).ready(function(){
	vrtCodeChange();
});  
	var myPage;
	
	var url = "<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/seCusProblem.json?flag=t";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'ID',renderer:mySelect,align:'center'},
				{header: "顾客问题代码", dataIndex: 'CCC_CODE', align:'center'},
				{header: "顾客问题名称", dataIndex: 'CCC_NAME', align:'center'}
		      ];
		      
	function mySelect(value,metaDate,record){
		 return String.format("<input type='radio' name='rd' onclick='setData(\""+record.data.CCC_CODE+"\",\""+record.data.CCC_NAME+"\")' />");
	}

	function setData(v1,v2){
		 //调用父页面方法
		 if(v2==null||v2=="null"){
		 	v2 = "";
		 }
		 if(v1==null||v1=="null"){
		 	v1 = "";
		 }
		 var id=document.getElementById("id").value;
		if (__parent().$('inIframe')) {
			__parent().setcusProblem(v1,v2,id);
 		} else {
 			__parent().setcusProblem(v1,v2,id);
		}
 		_hide();
	}
	//查询子系统
	function vrtCodeChange(){
		var VRT_CODE=document.getElementById("VRT_CODE").value;
		if(""!=VRT_CODE){
			var url="<%=contextPath%>/claim/dealerClaimMng/ApplicationClaim/cusProblemz.json?VRT_CODE="+VRT_CODE;
			sendAjax(url,function(json){
				var t=json.listCus2;
				if(null!=t){
					$("#VFG_CODE option").remove(); 
					for(var i=0; i<t.length;i++){ 
						$("#VFG_CODE").append("<option value='"+t[i].VFG_CODE+"'>"+t[i].VFG_NAME+"</option>");
					}
				}else{
					MyAlert("暂无数据！");
				}
			},"fm");
		} 
	}
</script>
</body>
</html>

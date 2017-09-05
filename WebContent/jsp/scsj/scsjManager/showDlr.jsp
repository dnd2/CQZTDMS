<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); 
%>
<script language="JavaScript" >
	function init(){
		__extQuery__(1);
	}

</script>
<body onload="init();">
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：经销商选择 </div>
</div>
  <form  name="fm" id="fm" method="post">
  <input id="ksId" name = "ksId" type="hidden" value="${ksId}"/>
	   <!--查询条件begin-->
    <table class="table_query" border="0" >
      <tr>
        <td class="table_query_2Col_label_5Letter">经销商名称：</td>
        <td align="left">
            <input name="dlrName" id="dlrName" type="text" class="middle_txt" size="100" />
        </td>
      </tr>
      <tr>
       <td colspan="4" align="center"><input name="button" id="queryBtn" type="button" onclick="__extQuery__(1);" class="normal_btn"  value="查询" />
        <input name="button" id="queryBtn" type="button" onclick="_hide()" class="normal_btn"  value="关闭" />
        </td>
      </tr>
    </table>
      <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script language="JavaScript" >
	var myPage;
	
	var url = "<%=contextPath%>/scsj/scsjManager/scsjManagerAction/queryDlr.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'ROOT_DEALER_ID',renderer:mySelect,align:'center'},
				{header: "大区", dataIndex: 'ROOT_ORG_NAME', align:'center'},
				{header: "省份", dataIndex: 'REGION_NAME', align:'center'},
				{header: "经销商", dataIndex: 'DEALER_NAME', align:'center',style:'text-align:left'}
		      ];
		      
	function mySelect(value,metaDate,record){
		 var ksId = document.getElementById("ksId").value;
		 return String.format("<input type='radio' name='rd' onclick='setMainWork(\""+value+"\",\""+ksId+"\")' />");
	}

	function setMainWork(v1,v2){
		 //调用父页面方法
		 if(v1==null||v1=="null"){
		 	v1 = "";
		 }
		 if(v2==null||v2=="null"){
			 	v2 = "";
			 }
 		if (parent.$('inIframe')) {
 			parentContainer.setMainWork(v1,v2);
 		} else {
			parent.setMainWork(v1,v2);
		}
	 			//关闭弹出页面
	 			parent._hide();
	}
	
</script>
</body>
</html>

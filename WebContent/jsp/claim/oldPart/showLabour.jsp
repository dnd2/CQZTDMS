<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); 
%>
<script language="JavaScript" >
	function init(){
		__extQuery__(1)
	}

</script>
<body onload="init();">
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：工时选择 </div>
</div>
  <form  name="fm" id="fm" method="post">
    <table class="table_query" border="0" >
      <tr>
        <td class="table_query_2Col_label_5Letter">工时代码：</td>
        <td align="left">
            <input name="LABOUR_CODE" id="LABOUR_CODE" type="text" class="middle_txt"  />
        </td>
      <td class="table_query_2Col_label_5Letter">工时名称：</td>
        <td align="left">
            <input name="LABOUR_NAME" id="LABOUR_NAME" type="text" class="middle_txt" />
       </td>
       
      </tr>
      <tr>
       <td colspan="4" align="center"><input name="button" id="queryBtn" type="button" onclick="init();" class="normal_btn"  value="查询" />
        <input name="button" id="queryBtn" type="button" onclick="_hide()" class="normal_btn"  value="关闭" />
        </td>
      </tr>
    </table>
    <!--查询条件end-->
      <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script language="JavaScript" >
	var myPage;
	
	var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/showLabourList.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'labourCode',renderer:mySelect,align:'center'},
				{header: "工时代码", dataIndex: 'LABOUR_CODE', align:'center'},
				{header: "工时名称", dataIndex: 'LABOUR_NAME', align:'center'},
				{header: "工时数", dataIndex: 'LABOUR_HOUR', align:'center'}
		      ];
		      
	function mySelect(value,metaDate,record){
		 return String.format("<input type='radio' name='rd' onclick='setLabour(\""+record.data.LABOUR_CODE+"\",\""+record.data.LABOUR_NAME+"\",\""+record.data.LABOUR_HOUR+"\")' />");
	}

	function setLabour(v1,v2,v3){
		 //调用父页面方法
		 if(v2==null||v2=="null"){
		 	v2 = "";
		 }
		 if(v3==null||v3=="null"){
		 	v3 = "";
		 }
		 if(v1=="null"||v1==null){
		 	v1 = "";
		 }
		 
 		if (parent.$('inIframe')) {
 			parentContainer.setLabour(v1,v2,v3);
 		} else {
			parent.setLabour(v1,v2,v3);
		}
 		if (parent.$('inIframe')) {
 			if(parentContainer.cloMainTime==1) {
	 			//关闭弹出页面
	 			parent._hide();
 			}
 		}else {
 			if(parent.cloMainTime==1) {
	 			//关闭弹出页面
	 			parent._hide();
 			}	
 		}
	}
	
</script>
</body>
</html>

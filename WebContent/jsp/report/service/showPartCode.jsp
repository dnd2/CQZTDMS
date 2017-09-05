<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<script language="JavaScript" >
function init(){
		__extQuery__(1)
}
</script>
<body onload="init();">
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：配件选择 </div>
</div>
  <form  name="fm" id="fm" method="post">
   <!--查询条件begin-->
    <table class="table_query" border="0" >
      <tr>
        <td class="table_query_2Col_label_8Letter"><div align="right"><span class="tabletitle">配件代码：</span></div></td>
        <td align="left">
            <input name="PART_CODE" id="PART_CODE" type="text" class="middle_txt" size="17" />
        </td>
        <td class="table_query_2Col_label_5Letter">配件名称：</td>
        <td align="left">
            <input name="PART_NAME" id="PART_NAME" type="text" class="middle_txt" size="5" />
        </td>
        </tr>
        <tr>
        <td colspan="4" align="center">
        <input name="button" id="queryBtn" type="button" onclick="init();" class="normal_btn"  value="查询" />
        <input name="button" id="queryBtn" type="button" onclick="_hide()" class="normal_btn"  value="关闭" />
         <input name="button" id="queryBtn" type="button" onclick="sure();" class="normal_btn"  value="确定" />
        </td>
      </tr>
    </table>
    <!--查询条件end-->
    <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script language="JavaScript" >
	var myPage;
	
	var url = "<%=contextPath%>/report/service/ClaimReport/queryPartCode.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'PART_CODE',renderer:mySelect,align:'center'},
				{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'}
		      ];
		      
	function mySelect(value,metaDate,record){
		 return String.format("<input type='checkbox' name='rd' value='"+value+"'  />");
	}

	function sure(){
	var code = document.getElementsByName("rd");
	var str="";
	var len = code.length;
	for(var i=0;i<len;i++){        
   		if(code[i].checked){            
   			str = str+code[i].value+","; 
   		}
   	}
	if(str!=""){
		str = str.substring(0,str.length-1);
	}
		
 		if (parent.$('inIframe')) {
 			parentContainer.setMainPartCode(str);
 		} else {
			parent.setMainPartCode(str);
		}
 		if(parentContainer.cloMainPart==1) {
 			//关闭弹出页面
 			parent._hide();
 		 }
	}

	
</script>
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<body onload="__extQuery__(1)">
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：代码选择 </div>
</div>
  <form  name="fm" id="fm" method="post">
   <!--查询条件begin-->
    <table class="table_query" border="0" >
      <tr>
        <td class="table_query_2Col_label_5Letter"><div align="right"><span class="tabletitle">故障代码：</span></div></td>
        <td align="left">
            <input name="CODE" type="text" class="middle_txt" size="17" />
        </td>
        <td class="table_query_2Col_label_5Letter">故障名称：</td>
        <td align="left">
            <input name="CODE_NAME" type="text" class="middle_txt" size="5" />
        </td>
         <td class=""><input name="button" id="queryBtn" type="button" onclick="__extQuery__(1)" class="normal_btn"  value="查询" />
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
	var type='<%=request.getAttribute("type")%>';//1为质损程度，2为质损区域，3为质损类型，4为故障代码
	var labourCode = '<%=request.getAttribute("labourCode")%>';
	if (type=="1") {
		code = "质损程度代码";
		name = "质损程度名称";
	}else if (type=="2") {
		code = "质损区域代码";
		name = "质损区域名称";
	}else if (type=="3") {
		code = "质损类型代码";
		name = "质损类型名称";
	}else if (type=="4") {
		code = "故障名称";
		name = "故障代码";
	}
	var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/getChngCode.json?type="+type+"&labourCode="+labourCode;
				
	var title = null;
	
	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'id',align:'center',renderer:mySelect},
				{header: code, dataIndex: 'code', align:'center'},
				{header: name, dataIndex: 'codeName', align:'center'}
		      ];
		      
	function mySelect(value,metaDate,record){
		 return String.format("<input type='radio' name='rd' onclick='setCode(\""+record.data.code+"\",\""+record.data.codeName+"\")' />");
	}

	function setCode(v1,v2){
		 //调用父页面方法
		 if(v1==null||v1=="null"){
		 	v1 = "";
		 }
		 if(v2==null||v2=="null"){
		 	v2 = "";
		 }
		 if (parent.$('inIframe')) {
 		 parentContainer.setCode(v1,v2);
 		 } else {
		 parent.setCode(v1,v2);
		 }
 			//关闭弹出页面
 			parent._hide();
 		
	}

</script>
</body>
</html>

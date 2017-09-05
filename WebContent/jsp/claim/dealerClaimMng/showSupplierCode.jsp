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
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：供应商选择 </div>
</div>
  <form  name="fm" id="fm" method="post">
   <!--查询条件begin-->
    <table class="table_query" border="0" >
     <tr>
      <td class="table_query_3Col_label_6Letter" nowrap="nowrap">供应商代码：</td>
      <td class="table_query_3Col_input" nowrap="nowrap">
      	<input class="middle_txt" id="maker_code" name="maker_code" value="" type="text"/>
      	<input class="middle_txt" id="partcode" name="partcode" value="${partcode }" type="hidden"/>
      	<input class="middle_txt" id="pkid" name="pkid" value="${id }" type="hidden"/>
      </td>
      <td class="table_query_3Col_label_6Letter" nowrap="nowrap">供应商名称：</td>
      <td>
      	<input name="maker_shotname" type="text" id="maker_shotname"  class="middle_txt"/>
      </td>
  	</tr>
      <tr>
       <td colspan="4" align="center"><input name="button" id="queryBtn" type="button" onclick="__extQuery__(1);" class="normal_btn"  value="查询" />
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
	var partcode=$('partcode').value;
	var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/querySupplierCode.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'MAKER_ID',renderer:mySelect,align:'center'},
				{header: "供应商代码", dataIndex: 'MAKER_CODE', align:'center'},
				{header: "供应商名称", dataIndex: 'MAKER_SHOTNAME', align:'center'}
		      ];
		      
	function mySelect(value,metaDate,record){
		 return String.format("<input type='radio' name='rd' onclick='setMainWork(\""+record.data.MAKER_CODE+"\",\""+record.data.MAKER_SHOTNAME+"\")' />");
	}

	function setMainWork(v1,v2){
		 //调用父页面方法
		 if(v2==null||v2=="null"){
		 	v2 = "";
		 }
		 if(v1==null||v1=="null"){
		 	v1 = "";
		 }
		var id =$('pkid').value;
 		if (parent.$('inIframe')) {
 			parentContainer.setMainSupplierCode(v1,v2,id);
 		} else {
			parent.setMainSupplierCode(v1,v2,id);
		}
	 	//关闭弹出页面
	 	parent._hide();
	}
	
</script>
</body>
</html>

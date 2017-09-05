<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<body onload="__extQuery__(1)">
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：配件选择 </div>
</div>
  <form  name="fm" id="fm" method="post">
  <input type="hidden" name="GROUP_ID" id="GROUP_ID" value="<%=request.getAttribute("GROUP_ID") %>"/>
  <input type="hidden" name="roNo" id="GROUP_ID" value="<%=request.getAttribute("roNo") %>"/>
  <input type="hidden" name="vin" id="GROUP_ID" value="<%=request.getAttribute("vin") %>"/>
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
        <td class="table_query_2Col_label_8Letter"><div align="right"><span class="tabletitle">件号：</span></div></td>
        <td align="left">
            <input name="ERPD_CODE" id="ERPD_CODE" type="text" class="middle_txt" size="17" />
        </td>
      
        </tr>
        
        <tr>
        <td colspan="4" align="center"><input name="button" id="queryBtn" type="button" onclick="__extQuery__(1)" class="normal_btn"  value="查询" />
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
	
	var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryPartCode.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'partId',renderer:mySelect,align:'center'},
				{header: "配件代码", dataIndex: 'partCode', align:'center'},
				{header: "件号", dataIndex: 'erpdCode', align:'center'},
				{header: "配件名称", dataIndex: 'partName', align:'center'}
				
				//{header: "旧件供应商名称", dataIndex: 'supplierName', align:'center'}
				//{header: "预授权", dataIndex: 'fore', align:'center'}
		      ];
		      
	function mySelect(value,metaDate,record){
		 return String.format("<input type='radio' name='rd' onclick='setMainPartCode(\""+record.data.partId+"\",\""+record.data.erpdCode+"\",\""+record.data.partName+"\",\""+record.data.partCode+"\")' />");
	}

	function setMainPartCode(v1,v2,v3,v4){
		 //调用父页面方法
		 if(v2==null||v2=="null"){
		 	v2 = "";
		 }
		 if(v3==null||v3=="null"){
		 	v3 = "";
		 }
		 
 		//parentContainer.setMainPartCode(v1,v2,v3,v4,v5,v6);
 		if (parent.$('inIframe')) {
 			parentContainer.setMainPartCode(v1,v2,v3,v4);
 			parent._hide();
 		} else {
			parent.setMainPartCode(v1,v2,v3,v4);
			
		}
 		if(parentContainer.cloMainPart==1) {
 			//关闭弹出页面
 			parent._hide();
 		 }

 		
	}

	
</script>
</body>
</html>

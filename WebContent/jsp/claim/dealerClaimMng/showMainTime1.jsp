<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); 
%>
<body onload="__extQuery__(1);">
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：工时选择 </div>
</div>
  <form  name="fm" id="fm" method="post">
  <input type="hidden" name="timeCode" id="timeCode" value="<%=request.getAttribute("timeCode")%>"/>
  <input type="hidden" name="timeId" id="timeId" value="<%=request.getAttribute("timeId")%>"/>
  <input type="hidden" name="TREE_CODE" id="TREE_CODE" value="<%=request.getAttribute("TREE_CODE")%>"/>
  <input type="hidden" name="WRGROUP_ID" id="WRGROUP_ID" value="<%=request.getAttribute("modelId")%>"/>
  <input type="hidden" name="list01" id="list01" value="<%=request.getAttribute("GROUP")%>"/>
  <input type="hidden" name="roNo" id="roNo" value="<%=request.getAttribute("roNo")%>"/>
  <input type="hidden" name="vin" id="vin" value="<%=request.getAttribute("vin")%>"/>
  <input type="hidden" name="yiedly" id="yiedly" value="<%=request.getAttribute("yiedly")%>"/>
   <input type="hidden" name="price" id="price" value="<%=request.getAttribute("price")%>"/>
  <input type="hidden" name="aCode" id="aCode" value="<%=request.getAttribute("aCode")%>"/>
   <!--查询条件begin-->
    <table class="table_query" border="0" >
      <tr>
        <td class="table_query_2Col_label_5Letter">工时代码：</td>
        <td align="left">
            <input name="LABOUR_CODE" id="LABOUR_CODE" type="text" class="middle_txt" size="5" />
        </td>
      <td class="table_query_2Col_label_5Letter">工时名称：</td>
        <td align="left">
            <input name="CN_DES" id="CN_DES" type="text" class="middle_txt" size="5" />
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
	
	var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/queryTime1.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'labourCode',renderer:mySelect,align:'center'},
				{header: "工时代码", dataIndex: 'LABOUR_CODE', align:'center'},
				{header: "工时名称", dataIndex: 'WR_LABOURNAME', align:'center'}
		      ];
		      
	function mySelect(value,metaDate,record){
		 return String.format("<input type='radio' name='rd' onclick='setMainTime(this,\""+record.data.LABOUR_ID+"\",\""+record.data.LABOUR_CODE+"\",\""+record.data.WR_LABOURNAME+"\",\""+record.data.LABOUR_FIX+"\",\""+record.data.LABOUR_PRICE+"\")' />");
	}

	function setMainTime(checkBox,v1,v2,v3,v4,v5){
		 //调用父页面方法
		 if(v2==null||v2=="null"){
		 	v2 = "";
		 }
		 if(v3==null||v3=="null"){
		 	v3 = "";
		 }
		 if(v4=="null"||v4==null){
		 	v4 = "";
		 }
		 if(v5==null||v5=="null"){
		 	v5 = "";
		 }
		 if(checkBox.checked){
				if (parent.$('inIframe')) 
				{
					parentContainer.setMainTime(v1,v2,v3,v4,v5);
					parentContainer.setCodesAndNames();
				}
			   _hide();
		}
	}
	
</script>
</body>
</html>

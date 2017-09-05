<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<body onload="__extQuery__(1);">
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：新件选择 </div>
</div>
  <form  name="fm" id="fm" method="post">
  <input type="hidden" name="GROUP_ID" id="GROUP_ID" value="<%=request.getAttribute("GROUP_ID") %>"/>
  <input type="hidden" name="roNo" id="GROUP_ID" value="<%=request.getAttribute("roNo") %>"/>
  <input type="hidden" name="vin" id="GROUP_ID" value="<%=request.getAttribute("vin") %>"/>
  <input type="hidden" name="yiedlyType" id="yiedlyType" value="<%=request.getAttribute("yiedlyType") %>"/>
  <input type="hidden" name="repairType" id="repairType" value="<%=request.getAttribute("repairType") %>"/>
   <input type="hidden" name="aCode" id="aCode" value="<%=request.getAttribute("aCode") %>"/>
   <input type="hidden" name="partPrice" id="partPrice" value="<%=request.getAttribute("partPrice") %>"/>
   <!--查询条件begin-->
    <table class="table_query" border="0" >
      <tr>
        <td class="table_query_2Col_label_8Letter" nowrap="true"><div align="right"><span class="tabletitle">新件代码：</span></div></td>
        <td align="left">
            <input name="PART_CODE" id="PART_CODE" type="text" class="middle_txt" size="17" />
        </td>
        <td class="table_query_2Col_label_5Letter" nowrap="true">新件名称：</td>
        <td align="left">
            <input name="PART_NAME" id="PART_NAME" type="text" class="middle_txt" size="5" />
        </td>
        <td class="table_query_2Col_label_5Letter" nowrap="true">活动类型：</td>
        <td align="left">
           	<script type="text/javascript">
    			genSelBoxExp("activity_type",<%=Constant.PART_ACTIVITY_TYPE%>,"",true,"short_sel","","false",'');
           	</script>
        </td>
        </tr>
        <tr>
        <td colspan="6" align="center"><input name="button" id="queryBtn" type="button" onclick="__extQuery__(1);" class="normal_btn"  value="查询" />
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
	
	var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/queryPartCode4.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'DEF_ID',renderer:mySelect,align:'center'},
				{header: "活动编码", dataIndex: 'ACTIVITY_CODE', align:'center'},
				{header: "新件代码", dataIndex: 'REPART_OLDCODE', align:'center'},
				{header: "新件名称", dataIndex: 'REPART_NAME', align:'center'},
				{header: "旧件代码", dataIndex: 'PART_OLDCODE', align:'center'},
				{header: "旧件名称", dataIndex: 'PART_NAME', align:'center'}
		      ];
		      
	function mySelect(value,metaDate,record){
		 return String.format("<input type='radio' name='DEF_ID' onclick='setMainPartCode(this,\""+record.data.PART_ID+"\",\""+record.data.REPART_OLDCODE+"\",\""+record.data.REPART_NAME+"\",\""+record.data.PART_OLDCODE+"\",\""+record.data.PART_NAME+"\",\""+record.data.DEF_ID+"\")' />");
	}

	function setMainPartCode(obj,v1,v2,v3,v4,v5,v6){
		 //调用父页面方法
		 if(v2==null||v2=="null"){
		 	v2 = "";
		 }
		 if(v3==null||v3=="null"){
		 	v3 = "";
		 }
		 if(v4==null||v4=="null"){
		 	v4 = "";
		 }
		 if(v5==null||v5=="null"){
		 	v5 = "";
		 }
		 if(v6==null||v6=="null"){
		 	v6 = "";
		 }
		 if(obj.checked){
				if (parent.$('inIframe')) 
				{
					parentContainer.setMainPartCode(v1,v2,v3,v4,v5,v6);
				}
			   _hide();
		}
	}

	
</script>
</body>
</html>

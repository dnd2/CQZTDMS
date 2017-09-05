<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<body onload="__extQuery__(1)">
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：故障模式选择 </div>
</div>
  <form  name="fm" id="fm" method="post">
  <div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
   <!--查询条件begin-->
    <table class="table_query" border="0" >
      <tr>
        <td style="text-align:right"><div align="right"><span class="tabletitle">故障模式代码：</span></div></td>
        <td align="left">
            <input name="failureCode" id="failureCode" type="text" class="middle_txt" size="17" />
        </td>
        <td style="text-align:right">故障模式名称：</td>
        <td align="left">
            <input name="failureName" id="failureName" type="text" class="middle_txt" size="5" />
        </td>
        </tr>
        <tr>
        <td colspan="4" style="text-align:center">
        	<input name="queryBtn" id="queryBtn" type="button" onclick="__extQuery__(1)" class="normal_btn"  value="查询" />
        	<input name="button" id="queryBtn" type="button" onclick="_hide()" class="normal_btn"  value="关闭" />
        </td>
      </tr>
    </table>
    </div>
    </div>
    <!--查询条件end-->
    <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script language="JavaScript" >
	var myPage;
	
	var url = "<%=request.getContextPath()%>/claim/basicData/FailureMaintainMain/queryFailureFromBase.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'faultTypeId',renderer:mySelect,align:'center'},
				{header: "故障模式代码", dataIndex: 'faultTypeCode', align:'center'},
				{header: "故障模式名称", dataIndex: 'faultTypeName', align:'center'}
		      ];
		      
	function mySelect(value,metaDate,record){
		 return String.format("<input type='radio' name='rd' onclick='queryRulePart(\""+record.data.faultTypeCode+"\")' />");
	}

	function setMainPartCode(v1,v2,v3){
		 //调用父页面方法
		 if(v2==null||v2=="null"){
		 	v2 = "";
		 }
 		if (__parent()) {
 			__parent().setMainPartCode(v1,v2,v3);
 		} else {
			parent.setMainPartCode(v1,v2,v3);
		}
 		if(__parent().cloMainPart==1) {
 			//关闭弹出页面
 			parent._hide();
 		 }
	}
	
	function queryRulePart(faultCode){
		 var url="<%=request.getContextPath()%>/claim/basicData/FailureMaintainMain/queryFaultNameByFaultCode.json?faultCode="+faultCode;
		 makeNomalFormCall(url,queryPartResult,"fm");
	}
	function queryPartResult(json){
		var part = json.ps;
		setMainPartCode(part.FAULT_TYPE_CODE,part.FAULT_TYPE_NAME,'');
	}
	
</script>
</body>
</html>

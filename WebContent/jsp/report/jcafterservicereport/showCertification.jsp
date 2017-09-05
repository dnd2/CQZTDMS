<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>合格证寄送情况明细</title>
<% String contextPath = request.getContextPath();%>
<script>
var arrayobj = new Array();
function doInit() {
	__extQuery__(1);
}
</script>
</head>

<body>
<div class="navigation">
<img src="<%=contextPath %>/img/nav.gif" />&nbsp;合格证寄送情况明细
</div>
<form name="fm" id="fm">
  	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
  	<tr>
    	<td align="center" colspan="5">
    		<input type="button" name="return1" onclick="parent.window._hide();"  class="normal_btn" value="关闭"/>
    		<input type="hidden" name="ORDER" id="ORDER" value="<%=request.getParameter("ORDER") %>" />
    		<input type="hidden" name="ORG" id="ORG" value="<%=request.getParameter("ORG") %>" /><!-- 正常工单是否选择标志 -->
    	</td>
    </tr>
    </table>

	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script language="javascript">
 	var myPage;
	var url = "<%=contextPath%>/report/jcafterservicereport/CertificationStatus/queryCertificationDetail.json";
	var title = null;
	var columns = [
				{header: "发运单号", dataIndex: 'DMS_ORDER_CODE', align:'center'},
				{header: "生产基地", dataIndex: 'CA_ERP_ORG_NAME', align:'center'},
				{header: "特快专递号", dataIndex: 'EMS_NUMBER', align:'center'},
				{header: "合格证寄送数量", dataIndex: 'CERTIFICATION_QUANTITY', align:'center'},
				{header: "寄送时间", dataIndex: 'POSTED_DATE', align:'center'},
				{header: "创建时间", dataIndex: 'CREATE_DATE', align:'center'}
		      ];
		      
function mySelect(value,metaDate,record){

    pushArray(record);
    
	if (record.data.ORDER_VALUABLE_TYPE == 13591002) {
		//return String.format("<input type='radio' name='rd' onclick='setRoNo(\""+record.data.RO_NO+"\", \""+record.data.DEALER_CODE+"\", \""+record.data.IN_MILEAGE+"\", \""+record.data.FREE_TIMES+"\",\""+getNextIN_MILEAGE(record.data.RO_NO)+"\")' />");
		//问题工单不可选
		return String.format("<input type='radio' name='rd' disabled='true'/>");
	} else {
		//只能选最新的正常工单
		var ro_sel = document.getElementById("ro_sel").value;
		if (ro_sel) {
			return String.format("<input type='radio' name='rd' disabled='true'/>");
		} else {
			document.getElementById("ro_sel").value = 'ro_sel';
			return String.format("<input type='radio' name='rd' onclick='setRoNo(\""+record.data.RO_NO+"\", \""+record.data.DEALER_CODE+"\", \""+record.data.IN_MILEAGE+"\", \""+record.data.FREE_TIMES+"\",\""+getNextIN_MILEAGE(record.data.RO_NO)+"\", \""+record.data.DEALER_NAME+"\")' />");
		}
	}
}
function pushArray(obj){ //YH 2010.11.15
   
    arrayobj.push(obj); // MyAlert("工单编码："+obj.data.RO_NO); 
}

function getNextIN_MILEAGE(RO_NO){
  var k;
  for(var i=0;i<arrayobj.length;i++){
    if(arrayobj[i].data.RO_NO == RO_NO){
      k = i;
      break;
    }
  }
  if(k+1>=arrayobj.length){
    //MyAlert('没有下个工单号');
    return 0;
  }else {
    //MyAlert("返回下一个行程数:"+arrayobj[k+1].data.IN_MILEAGE);
    return arrayobj[k+1].data.IN_MILEAGE;
  }
  
}
	
function setRoNo(roNo, dealerCode, inMileage, freeTimes,nextInMileage, dealerName) {
	parent._hide();
	showRo(roNo, dealerCode, inMileage, freeTimes,getNextIN_MILEAGE(roNo), dealerName);
} 

//刷新父页面 父页面赋值
function showRo(roNo, dealerCode, inMileage, freeTimes,nextInMileage, dealerName){
	if (parent.$('inIframe')) {	    
		parentContainer.showRo(roNo, dealerCode, inMileage, freeTimes,nextInMileage, dealerName);	
	} else {	    
		parent.showRo(roNo, dealerCode, inMileage, freeTimes,nextInMileage, dealerName);
	}
}
</script>
</body>

</html>

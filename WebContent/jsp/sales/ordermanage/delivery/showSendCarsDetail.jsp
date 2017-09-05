<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息变更申请</title>
<% String contextPath = request.getContextPath(); %>

</head>
<body onload="__extQuery__(1);">
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:销售订单管理 &gt; 订单发运 &gt;在途车辆信息查询&gt;在途车辆明细
</div>
<form name="fm" id="fm">

<input name="flag" type="hidden" id="flag"  class="middle_txt" value=""/>
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_edit">

    <tr>
    	<td align="center" colspan="5">
    		<input type="button" name="replyBtn" id="replyBtn"  value="关  闭"  class="normal_btn" onClick="_hide();" >
    		<input type="button" name="backBtn" id="backBtn"  value="返回"  class="normal_btn" onClick="back();" style="display:none;">
    	</td>
    </tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script type="text/javascript" >

	var myPage;
	var sendCarNo = parent.$('inIframe').contentWindow.$('sendCarsNo').value;
	//查询路径           
	var url = "<%=contextPath%>/sales/ordermanage/delivery/OrderCommandQuery/showSendCarsDetail.json?COMMAND=1&sendCarNo="+sendCarNo;
	
	var title = null;
	
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "VIN",dataIndex: 'VIN',align:'center'},
				{header: "物料代码",dataIndex: 'MATERIAL_CODE',align:'center'},
				{header: "物料名称",dataIndex: 'MATERIAL_NAME',align:'center'},
				{header: "验收状态", dataIndex: 'IF_INSPECTION', align:'center',renderer:myHref}
		      ];
		      
	function myHref(value,meta,record){
		var msg = "未验收";
		if(value=='1'){
			msg = '已验收';
		}
		return String.format(msg);
	}

</script>
</body>
</html>
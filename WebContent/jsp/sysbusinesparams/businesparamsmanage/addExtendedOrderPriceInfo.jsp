<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.Map"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>新增超期常规订单资源单价</title>
<style type="text/css">
	.textDisable {
 		color: #A0A0A0;
 	}
</style>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  系统管理 &gt; 系统业务参数维护 &gt; 新增超期常规订单资源单价</div>
	<form id="fm" name="fm" method="post">
		<table class="table_query" border="0">
			<tr>
				<td width="15%" class="tblopt"><div align="right">年份：</div></td>
				<td width="20%" >
      				<input type="text" class='textDisable' id="year" name="year" datatype="0,is_digit,4" value="${year}" readonly="readonly" onfocus="this.blur();"/>
    			</td>
    		</tr> 
			<tr>
    			<td width="15%" class="tblopt"><div align="right">月份：</div></td>
				<td width="20%" >
      				<input type="text" class='textDisable' id="month" name="month" datatype="0,is_digit,2" value="${month}" readonly="readonly" onfocus="this.blur();"/>
    			</td>
			</tr>
			<tr>
    			<td width="15%" class="tblopt"><div align="right">价格：</div></td>
				<td width="20%" >
					<input type="text" id="price" name="price" datatype="0,isMoney,10" value="" />
    			</td> 
    		</tr>
    		<tr>
    			<td width="15%" class="tblopt"><div align="right">备注：</div></td>
				<td width="20%" >
					<textarea id="remark" name="remark" rows="4" cols="20"></textarea>
    			</td> 
    		</tr>
			<tr>
    			<td align="center"  colspan="3">
					<input type="button"  class="normal_btn" onclick="validate();" value="提 交" id="queryBtn" />
					<input type="button"  class="normal_btn" onclick="history.back();" value="返回" id="btn" /> 
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
<script type="text/javascript" >

	function validate(){
		if(submitForm('fm')){
			MyConfirm("是否提交?",submit);
		}
	}
	
	function submit(){
		fm.action = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/ExtendedOrderPriceMaintenance/addExtendedOrderPrice.do";
		fm.submit();
	}

</script>    
</body>
</html>
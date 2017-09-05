<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="com.infodms.dms.po.TtDealerActualSalesPO"%>
<%@ page import="com.infodms.dms.po.TtCustomerPO"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>

<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function myOnLoad(){
		loadcalendar();   //初始化时间控件
	}
	
	function isSubmit() {
		if(submitForm("fm")) {
			MyConfirm("确认操作？", checkSubmit) ;
		}
	}
	
	function checkSubmit() {
		var url = "<%=contextPath%>/sales/displacement/DisplacementCarPrice/checkTypeExt.json" ;
		
		makeFormCall(url, Submit, "fm") ;
	}
	
	function Submit(json){
		var count = json.count ;
		
		if(count == 0) {
			Insert() ;
		} else {
			MyConfirm("对应价格的置换类型和置换资格已存在，是否确认更新价格？", update) ;
		}
	}
	
	function Insert() {
		$('fm').action="<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementPrcInsertCarInfo.do";
		$('fm').submit();
	}
	
	function update() {
		$('fm').action="<%=contextPath%>/sales/displacement/DisplacementCarPrice/DisplacementPrcUpdateCarInfo.do";
		$('fm').submit();
	}
	
	function returnSubmit(){
			history.back();
		}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
</script>
<title>二手车置换资格价格维护新增</title>
</head>
<body onload="myOnLoad();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 整车销售 &gt; 客户信息管理 &gt; 二手车置换资格价格维护新增</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="dealerId2" size="15" id="dealerId2" value="" />
<input type="hidden" value="<%=Constant.DisplancementCarrequ_replace_1%>" name="displacement_type"></input>
<div id="divcompany_table" style="">
<table class="table_edit" align="center" id="company_table">
	<tr>
		<td width="15%" align="right">置换资格：</td>
		<td width="35%" align="left">
		<script type="text/javascript">
					genSelBoxExp("DISPLACEMENT_PRC",<%=Constant.DisplancementCarrequ_zige%>,"",false,"short_sel",'',"false",'');
			</script>
		</td>
		<td width="15%" align="right">返利价格：</td>
		<td width="35%" align="left"><input type="text" class="middle_txt" name="PRICE" id="PRICE" datatype="0,is_double,12" value=""  /></td>
	</tr>
	<tr>
		<td align="right">置换类型：</td>
		<td align="left"><script type="text/javascript">
					genSelBoxExp("DISPLACEMENT_TYPE",<%=Constant.DisplancementCarrequ_replace%>,"",false,"short_sel",'',"false",'');
			</script></td>
	</tr>
</table>
</div>
<table  class="table_edit" align="center" >
	<tr class="cssTable">
		<td align="center" colspan="4">
			<input type="button" name="mySubmit"  class="normal_btn" value="提交" onclick="isSubmit();" />
			<input type="button" name="mySubmit"  class="normal_btn" value="返回" onclick="returnSubmit();" />

		</td>
	</tr>
</table>

</form>
</div>
</body>
</html>
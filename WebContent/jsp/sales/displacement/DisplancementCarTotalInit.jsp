<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/activityfunc.js"></script>
<title>二手车置换汇总</title>
<script type="text/javascript">
<!--
	function doInit(){
		_setDate_("startDate", "endDate", "1", "0") ;
		_setDate_("signInStartDate", "signInEndDate", "1", "0") ;
		loadcalendar();  //初始化时间控件
		genLocSel('txt1','','','','',''); // 加载省份城市和县
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
  //->
</script>
</head>
<body onunload="javascript:destoryPrototype();"> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 二手车置换 &gt;二手车置换汇总</div>
	<form id="fm" name="fm" method="post">
		<table class="table_query" border="0">
			<tr>
				<td width="20%" align="right">提报日期：</td>
				<td width="25%" align="left">
					<input class="short_txt" readonly="readonly"  type="text" id="startDate" name="startDate" group="startDate,endDate" datatype="1,is_date,10"/>
					<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value="&nbsp;" />至
					<input class="short_txt"  readonly="readonly" type="text" id="endDate" name="endDate" group="startDate,endDate" datatype="1,is_date,10"/>
					<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
				</td>
				<td width="20%" align="right">签收日期：</td>
				<td width="25%" align="left">
					<input class="short_txt" readonly="readonly"  type="text" id="signInStartDate" name="signInStartDate" group="signInStartDate,signInEndDate" datatype="1,is_date,10"/>
					<input class="time_ico" type="button" onClick="showcalendar(event, 'signInStartDate', false);" value="&nbsp;" />至
					<input class="short_txt"  readonly="readonly" type="text" id="signInEndDate" name="signInEndDate" group="signInStartDate,signInEndDate" datatype="1,is_date,10"/>
					<input class="time_ico" type="button" onClick="showcalendar(event, 'signInEndDate', false);" value="&nbsp;" />
				</td>
			</tr>
			<tr>
			   <td width="20%"><div align="right">置换资格：</div></td>
				<td width="20%">
					<script type="text/javascript">
						genSelBoxExp("zige",<%=Constant.DisplancementCarrequ_zige%>,"",true,"short_sel",'',"false",'');
					</script>
				</td>
	               <td width="20%"><div align="right">置换类型：</div></td>
				<td width="20%">
					<script type="text/javascript">
						genSelBoxExp("type",<%=Constant.DisplancementCarrequ_replace%>,"",true,"short_sel",'',"false",'');
					</script>
				</td>		
			</tr>
			<tr>
				<td width="20%"><div align="right">基地：</div></td>
				<td width="20%">
					<script type="text/javascript">
						genSelBoxExp("base",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel",'',"false",'');
					</script>
				</td>
				<td width="20%"><div align="right">省份：</div></td>
				<td width="20%"><select class="short_sel" id="txt1" name="region"></select></td>
	   			<td width="20%"></td>
				<td width="20%"></td>
	   			<td>
	   			</td>
			</tr>
			<tr>
				<td align="right">置换状态：</td>
				<td align="left">
					<script type="text/javascript">
							genSelBoxExp("status",<%=Constant.DisplancementCarrequ_cek%>,"",true,"short_sel",'',"false",'');
					</script>
					<font color="red">*</font>
					<input type="hidden" name="area_id" id="area_id" value="" />
				</td>
				<td width="20%"></td>
				<td width="20%"></td>
			</tr>
			<tr>		
				<td width="20%"></td>
				<td width="20%"></td>
				<td align="left">
					<input type="hidden" name="areas" id="areas" value="${areas }" />
					<input type="button" class="normal_btn" onclick="AnalyseExclel_cash();" value="导出兑现" id="queryBtn" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" class="normal_btn" onclick="AnalyseExclel_detail();" value="导出明细" id="queryBtn" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" class="normal_btn" onclick="requery();" value="重 置" id="queryBtn" />  
				</td>
			</tr>
		</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />	
	</form>
</div>
<script type="text/javascript">
  function requery() {
    $('zige').value = '';
    $('type').value = '';
    $('base').value = '';
    $('txt1').value = '';
    $('status').value = '';
  }	
 function AnalyseExclel_detail(){
	 if($('status').value == null || $('status').value == "") {
		 MyAlert("请先选择置换状态!");
		 return false;
	 } else {
		var fm = document.getElementById('fm');
		$('fm').action='<%=contextPath%>/sales/displacement/DisplacementCarChk/AnalyseExclel_detail.do';
		$('fm').submit();
	 }
   }
  function AnalyseExclel_cash(){
	 if($('status').value == null || $('status').value == "") {
		 MyAlert("请先选择置换状态!");
		 return false;
	 } else {
	  var fm = document.getElementById('fm');
	  $('fm').action='<%=contextPath%>/sales/displacement/DisplacementCarChk/AnalyseExclel_cash.do';
	  $('fm').submit();
	 }
   }
</script>   
</body>
</html>
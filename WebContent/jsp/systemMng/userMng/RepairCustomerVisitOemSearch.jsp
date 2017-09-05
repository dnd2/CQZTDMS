<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>维修回访客户报表(OEM端)</title>
<style>
.img {
	border: none
}
</style>
<script language="JavaScript">      
 function doInit() {
   		loadcalendar();	
	}
	
//格式化时间为YYYY-MM-DD
 function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
	function requery() {
		$('CON_APPLY_DATE_START_ID').value="";
		$('CON_APPLY_DATE_END_ID').value="";
		$('dealerCode').value="";
	}	
	function VisitExclel(){
	var fm = document.getElementById('fm');
	var strDate = $('CON_APPLY_DATE_START_ID').value;
	var endDate = $('CON_APPLY_DATE_END_ID').value;
	if(null == strDate || '' == strDate){
	  MyAlert('开始日期不能为空！');
	  return false;
	}else if (null == endDate || '' == endDate){
	  MyAlert('结束日期不能为空！');
	  return false;
	}else {
	  fm.action='<%=contextPath%>/sysmng/usemng/RepairCustomerVisit/AnalyseExclel.do';
	  fm.submit();
	 }
   }
	function toClearRegion(){
	 $('regionCode').value = ''; 
	}
	function toClearDealers(){
	 $('dealerCode').value = '';
	}
	function CallshowRegDealer(){
	  var regionCode =  $('regionCode').value ;
      showRegDealer('dealerCode','dealerId','true','',regionCode);
	}
</script>
</head>

<body onload='doInit()'>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理 &gt; 信息反馈查询 &gt; 维修回访客户回访表(车厂端)</div>
<form method="post" name = "fm" id="fm">
<input type="hidden" name="curPage" id="curPage" value="1" />
<input type="hidden"  name="regionId" size="15" value=""  id="regionId" />
<table class="table_query" border="0">
<tr>
     <td align="right" class="table_query_2Col_label_4Letter">选择省份：</td>
      <td class="table_list_th">
		<input type="text"  readonly="readonly"  name="regionCode" size="15" value=""  id="regionCode" class="middle_txt" datatype="1,is_noquotation,75" />
		<input name="regbu"  id="regbu" type="button" class="mark_btn" onclick="showRegion('regionCode','regionId','true')" value="..." />
		<input type="button"  class="cssbutton" value="清除" onClick="toClearRegion();"/>
		</td>		
		<td align="center" class="table_query_2Col_label_3Letter">经销商：</td>
			<td class="table_list_th"><input type="hidden" name="dealerId" size="15" id="dealerId" value="" />
				<input type="text" class="middle_txt" readonly="readonly" name="dealerCode" size="15" id="dealerCode" value="" />
				<input name="button4" type="button"  class="mini_btn" onclick="CallshowRegDealer()"; value="..." />
				<input type="button" name="button4clearbutton" id="button4clearbutton" class="cssbutton" value="清除" onClick="toClearDealers();"/>
			</td>			
    </tr>
    <tr>
			<td class="table_query_2Col_label_5Letter">选择日期：</td>
              <td  nowrap="nowrap">
              <input type="text" name="CON_APPLY_DATE_START" id="CON_APPLY_DATE_START_ID"  datatype="0,is_date,10" group="CON_APPLY_DATE_START_ID,CON_APPLY_DATE_END_ID" hasbtn="true" callFunction="showcalendar(event, 'CON_APPLY_DATE_START_ID', false);"/>
              至
  			<input type="text" name="CON_APPLY_DATE_END" id="CON_APPLY_DATE_END_ID"  datatype="0,is_date,10" group="CON_APPLY_DATE_START_ID,CON_APPLY_DATE_END_ID" hasbtn="true" callFunction="showcalendar(event, 'CON_APPLY_DATE_END_ID', false);"/>
  			</td>
</tr>
	<tr>
		<td align="center" colspan="4">
            <blockquote><input type="button" class="normal_btn" value="导出报表" onclick="VisitExclel();" /> 
            <input type="button" class="normal_btn" value="重 置" onclick="requery()" /></blockquote>
		</td>
	</tr>
</table>
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
</body>
</html>

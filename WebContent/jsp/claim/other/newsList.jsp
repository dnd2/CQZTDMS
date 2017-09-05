<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
	Long userId = (Long)request.getAttribute("logonUser");
%>

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>首页新闻</title>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<div class="wbox">
<form name='fm' id='fm'>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：首页新闻</div>
  <div class="form-panel">
  <h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
  <table class="table_query">
  				<tr>
  					<td style="text-align:right">新闻编码：</td>
  					<td align="left">
  						<input name="newsCode" id="newsCode" type="text" size="16"  class="middle_txt" value="" >
  					</td>
  					<td style="text-align:right">新闻标题：</td>
  					<td align="left">
  						<input name="newsTitle" id="newsTitle" type="text"  class="middle_txt" value="" >
  					</td>
  				</tr>
  				<tr>
  					<td style="text-align:right" nowrap="nowrap" >发布日期：</td>
           			<td align="left" nowrap="nowrap" colspan="3">
		            	<!-- <input class="short_txt" id="t1" name="APPLY_DATE_START" datatype="1,is_date,10"
		           		 maxlength="10" group="t1,t2"/>
		        	 	<input class="time_ico" value=" " onclick="showcalendar(event, 't1', false);" type="button"/> -->
		        	 	
		        	 	<input id="t1" name="APPLY_DATE_START"  class="Wdate" type="text" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'t2\')}'})" readonly/>
		          		 至
		          		 <input id="t2" name="APPLY_DATE_END"  class="Wdate" type="text" onclick="WdatePicker({minDate:'#F{$dp.$D(\'t1\')}'})" readonly/>
			        	 <!-- <input class="short_txt" id="t2" name="APPLY_DATE_END" datatype="1,is_date,10"
			           	  maxlength="10" group="t1,t2"/>
			        	 <input class="time_ico" value=" " onclick="showcalendar(event, 't2', false);" type="button"/> -->
		            </td>
  				</tr>
				<tr>
					<td colspan="4" style="text-align:center">
						<input class="normal_btn" name="queryBtn" id="queryBtn" type="button" onclick="__extQuery__(1);" value="查询"/>&nbsp;&nbsp;
						<input class="normal_btn" name="back" id="back" type="button" onclick="_hide()" value="关闭"/>	
					</td>
				</tr>
			</table>
			</div>
			</div>
</form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
  </div>
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/HomePageNews/HomePageNewsQuary3.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{id:'action',header: "选择",sortable: false,dataIndex: 'NEWS_ID',renderer:mySelect,align:'center'},
				{header: "单据编码",sortable: false,dataIndex: 'NEWS_CODE',align:'center',renderer:hrefNews},				
				{header: "发表人",sortable: false,dataIndex: 'VOICE_PERSON',align:'center'},
				{header: "发表日期",sortable: false,dataIndex: 'NEWS_DATE',align:'center',renderer:formatDate},
				{header: "标题",sortable: false,dataIndex: 'NEWS_TITLE',align:'center'},
				{header: "新闻类别",sortable: false,dataIndex: 'NEWS_TYPE',align:'center',renderer:getItemValue}
		      ];

	function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	} 
	function viewNews(value){
		OpenHtmlWindow("<%=contextPath%>/claim/basicData/HomePageNews/viewNews.do?comman=2&newsId="+value,800,600);
	}
	function hrefNews(value,meta,record){
		return String.format(
				 "<a href=\"#\" onclick='viewNews(\""+record.data.NEWS_ID+"\")'>"+value+"</a>" 
		) ;
	}
	
	function mySelect(value,metaDate,record){
		 //var vin = parentDocument.getElementById('vin').value;
		 return String.format("<input type='radio' name='rd' onclick='queryNews("+value+");' />");
	}
	function queryNews(newId){
		 var url="<%=contextPath%>/claim/other/Bonus/newsQuery.json?COMMAND=1&newId="+newId;
		 makeNomalFormCall(url,queryNewsResult,"fm");
	}
	function queryNewsResult(json){
		var oneNew = json.map;
		var parentContainer = __parent() ;
		parentContainer.addRow('t_news',oneNew.NEWS_ID,oneNew.NEWS_CODE,oneNew.NEWS_TITLE);
		_hide();
	}
	//修改的超链接设置
		__extQuery__(1);
</script>
</body>
</html>
<!--
<html xmlns="http://www.w3.org/1999/xhtml">

<body>
<table width="80%" height="90%" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="197" align="center"><p><img src="/img/chana/main.jpg" width="997" height="500" /></p></td>
  </tr>
</table>
<script type="text/javascript" >
</script>
</body>
</html>-->
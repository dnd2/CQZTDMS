<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>潜客等级分析报表</title>
<% String contextPath = request.getContextPath();  %>

<script type="text/javascript">

	function download(){
		if(submitForm('fm')){
			document.fm.action='<%=request.getContextPath()%>/crm/report/PotentialCustomerAnalysisReport/doDownload.json';
			document.fm.target="_self";
			document.fm.submit();
		}
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理  &gt; 潜客等级分析报表 &gt; 潜客等级分析报表</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
		
			<td align="right">查询日期：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="startDate" name="startDate" group="startDate,endDate" datatype="1,is_date,10" value=""/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value="&nbsp;" />至
				<input class="short_txt"  type="text" id="endDate" name="endDate" group="startDate,endDate" datatype="1,is_date,10" value=""/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
			</td>
			<td align="right">车系：</td>
			<td align="left">
              		<input id="seriesId" name="seriesId" type="text"  size="13"  readonly="readonly" value=""/> 
					<input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" onclick="toSeriesList('seriesId','','true', '${orgId}')" value="..." />
					<input class="normal_btn" type="button" value="清空" onclick="clrTxt('seriesId');"/>
			</td>
		
			<c:if test="${!empty  flag}" >
        		<td align="right" nowrap>合作经销商：</td>
             	 <td align="left">
	        		<input id="cdealerCode" name="cdealerCode" type="text"  size="30"  readonly="readonly" value=""/> 
					<input class="mini_btn" id="dlbtn" name="dlbtn" type="button" onclick="toCompetVechileList('cdealerCode','','true', '${orgId}')" value="..." />
					  <input class="normal_btn" type="button" value="清空" onclick="clrTxt('cdealerCode');"/>
        	  </td>
        	</c:if>
        	 
			 <c:if test="${dutyType!=10431005}">
              	<td align="right" nowrap>选择经销商：</td>
             	 <td align="left">
                  <input type="text" name="dealerCode" size="20" value="" id="dealerCode"/>
                  <input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" onclick="showOrgDealer('dealerCode','','true', '${orgId}','','')" value="..." />               
                  <input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCode');"/>
              	</td>
        	</c:if>
              <td width="20%" align="left">
                    <input name="queryBtn" type="button" class="cssbutton" onClick="doSearch();" value="查询">
                      <c:if test= "${!empty download}">  
                    	<input name="companyDownloadBtn" type=button class="cssbutton" onClick="download();" value="下载">
                    </c:if>
              </td>
		</tr>
	</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
    //初始化
    function doInit(){
    	//__extQuery__(1);
   		loadcalendar();  //初始化时间控件
	}
	function clrTxt(txtId){
    	document.getElementById(txtId).value="";
    }
    function doSearch(){ 
        document.fm.action='<%=contextPath%>/crm/report/PotentialCustomerAnalysisReport/doSearch.do';
		document.fm.target="_blank";
		document.fm.submit();
    }
    function toCompetVechileList(inputCode ,isMulti ,orgId)
    {
    	if(!inputCode){ inputCode = null;}
    	if(!isMulti){ isMulti = null;}
    	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
    	OpenHtmlWindow(g_webAppName+'/jsp/crm/report/showDealerCode.jsp?INPUTCODE='+inputCode+"&ISMULTI="+isMulti+"&ORGID="+orgId,730,390);
    }
    function toSeriesList(inputCode ,isMulti ,orgId)
    {
    	if(!inputCode){ inputCode = null;}
    	if(!isMulti){ isMulti = null;}
    	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
    	OpenHtmlWindow(g_webAppName+'/jsp/crm/report/showSeriesCode.jsp?INPUTCODE='+inputCode+"&ISMULTI="+isMulti+"&ORGID="+orgId,730,390);
    }

</script>
<!--页面列表 end -->
</body>
</html>
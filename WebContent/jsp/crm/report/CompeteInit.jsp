<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/common.js"></script>
<title>竞品统计表</title>
<% String contextPath = request.getContextPath();  %>

<script type="text/javascript">

	function download(){
		if(submitForm('fm')){
			document.fm.action='<%=request.getContextPath()%>/crm/report/ComepeteReport/doDownload.json';
			document.fm.target="_self";
			document.fm.submit();
		}
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理  &gt;竞品统计表 &gt; 竞品统计表</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td align="right">查询日期：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="startDate" name="startDate" group="startDate,endDate" datatype="1,is_date,10" value="${today}"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'startDate', false);" value="&nbsp;" />至
				<input class="short_txt"  type="text" id="endDate" name="endDate" group="startDate,endDate" datatype="1,is_date,10" value="${today}"/>
				<input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
			</td>
		<td align="right">线索来源：</td>
			<td align="left">
              		<input id="originId" name="originId" type="text"  size="13"  readonly="readonly" value=""/> 
					<input class="mini_btn" id="dlbtn2" name="dlbtn2" type="button" onclick="toOriginList('originId','','true', '')" value="..." />
					<input class="normal_btn" type="button" value="清空" onclick="clrTxt('originId');"/>
			</td>
        	
        <td  align="right"></td>
		<td align="right"><input type="radio" id="odtype" name="odtype" value="0" onclick="checkType()" checked="checked"></input>选择经销商：</td>
			<td class="table_list_th"><input type="hidden" name="dealerId" size="15" id="dealerId" value="" />
				<input type="text" class="middle_txt" readonly="readonly" name="dealerCode" size="15" id="dealerCode" value="" />
				<input name="button4" type="button"  class="mini_btn" onclick="showOrgDealer('dealerCode','dealerId','true');" value="..." />
				<input type="button" name="button4clearbutton" id="button4clearbutton" class="cssbutton" value="清除" onClick="toClearDealers();"/>
			</td>
		<td></td>
        	
    
		
        </tr>	
        <tr>	
        <td  align="right"></td>
		<td align="right"><input type="radio" id="odtype" name="odtype" value="0" onclick="checkOrgType()" checked="checked"></input>选择区域：</td>
		<td class="table_list_th">
		<input type="hidden"  name="orgId" size="15" value=""  id="orgId" class="middle_txt" datatype="1,is_noquotation,75" />
		<input type="text"  readonly="readonly"  name="orgCode" size="15" value=""  id="orgCode" class="middle_txt" datatype="1,is_noquotation,75" />
		<input name="orgbu"  id="orgbu" type="button" class="mark_btn" onclick="showOrg('orgCode','orgId','false')" value="..." />
		<input type="button" name="orgbuclearbutton" id="orgbuclearbutton" class="cssbutton" value="清除" onClick="toClearOrgs();"/>
		</td>	
        	
        	
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
	function toClearDealers(){
		document.getElementById("dealerCode").value="";
		document.getElementById("dealerId").value=""
	}
	function toClearOrgs(){
		document.getElementById("orgCode").value="";
		document.getElementById("orgId").value="";
	}

	function checkType(){
		document.getElementById("orgbuclearbutton").disabled=true;
		document.getElementById("orgbu").disabled=true;
		document.getElementById("button4").disabled=false;
		document.getElementById("button4clearbutton").disabled=false;
		document.getElementById("orgCode").value="";
		document.getElementById("orgId").value="";
		}
	function checkOrgType(){
		document.getElementById("orgbuclearbutton").disabled=false;
		document.getElementById("orgbu").disabled=false;
		document.getElementById("button4").disabled=true;
		document.getElementById("button4clearbutton").disabled=true;
		document.getElementById("dealerCode").value="";
		document.getElementById("dealerId").value="";
		}
	
    function doSearch(){ 
        document.fm.action='<%=contextPath%>/crm/report/CompeteReport/doSearch.do';
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

    function toOriginList(inputCode ,isMulti ,orgId)
    {
    	if(!inputCode){ inputCode = null;}
    	if(!isMulti){ isMulti = null;}
    	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
    	OpenHtmlWindow(g_webAppName+'/jsp/crm/report/showOriginCode.jsp?INPUTCODE='+inputCode+"&ISMULTI="+isMulti+"&ORGID="+orgId,730,390);
    }

</script>
<!--页面列表 end -->
</body>
</html>
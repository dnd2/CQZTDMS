<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>票据传真及原件收到情况</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit()
	{
   		loadcalendar();  //初始化时间控件
	}

</script>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理&gt;票据传真及原件收到情况</div>

  <form method="post" name="fm" id="fm">
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
   <!-- 查询条件 begin -->
  <table class="table_query">
 		<tr>
            <td align="right" nowrap>传真收到时间： </td>
            <td nowrap>
            	<div align="left">
            		<input name="beginTime" id="t1" value="" type="text" class="short_txt"  group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            		&nbsp;至&nbsp;
            		<input name="endTime" id="t2" value="" type="text" class="short_txt"  group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            	</div>
			</td>
			 <td width="19%" align="right" nowrap="nowrap">票据号：</td>
            <td colspan="2" align="left" >
            	<input class="middle_txt" id="draftCode" name="draftCode" value="" type="text" />     	
            </td> 
          </tr>       
          <tr>
            <td align="right" nowrap>票据类型：</td>
            <td>			
            	<select class="short_sel" name="draftType">
            		<option value="">-请选择-</option>
	            	<option value="0">承兑汇票</option>
	            	<option value="1">三方信贷</option>
	            	<option value="2">兵财融资</option>
				</select>   	     
            </td> 
			<td align="right" nowrap="nowrap">票据收到状态：</td>
            <td>
            	<select class="short_sel" name="draftStatus">
            		<option value="">-请选择-</option>
	            	<option value="0">传真件</option>
	            	<option value="1">原件</option>
				</select>   	     	   		
            </td>
          </tr>    
          <tr> 
           <td>&nbsp;</td>        
            <td align="left" nowrap>
            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询"  onclick="__extQuery__(1)"/>&nbsp;
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
	var myPage;
//查询路径
	var url = "<%=contextPath%>/report/jcafterservicereport/DraftFaxStatus/queryDraftFaxStatus.json";
				
	var title = null;

	var columns = [
				{header: "票据号", dataIndex: 'DRAFT_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_ERP_CODE', align:'center'},
				{header: "金额",dataIndex: 'DRAFT_MONEY' ,align:'center'},
				{header: "受理单位", dataIndex: 'CA_ERP_ORG_CODE', align:'center'},
				{header: "票据类型", dataIndex: 'DRAFT_TYPE', align:'center'},
				{header: "出票银行", dataIndex: 'DRAFT_BANK_NAME', align:'center'},
				{header: "传真件收到日期", dataIndex: 'FAX_RECEIVE_DATE', align:'center'},
				{header: "原件收到日期", dataIndex: 'ORIGINAL_RECEIVE_DATE', align:'center'},
				{header: "票据收到状态标识", dataIndex: 'RECEIVE_STATUS', align:'center'},			
				{header: "创建时间", dataIndex: 'CREATE_DATE', align:'center'}
		      ];
		      
//设置超链接  begin      
	function exportExcel()
	{
		fm.action = "<%=contextPath%>/report/jcafterservicereport/DraftFaxStatus/DraftFaxStatusExcel.do";
		fm.submit();
	}

  function showMonthFirstDay()     
  {     
	  var Nowdate = new Date();     
	  var MonthFirstDay = new Date(Nowdate.getYear(),Nowdate.getMonth(),1);     
	  return DateUtil.Format("yyyy-MM-dd",MonthFirstDay);  
  }     
  function showMonthLastDay()     
  {     
	  var Nowdate = new Date();     
	  var MonthNextFirstDay = new Date(Nowdate.getYear(),Nowdate.getMonth()+1,1);     
	  var MonthLastDay = new Date(MonthNextFirstDay-86400000);     
	  return DateUtil.Format("yyyy-MM-dd",MonthLastDay);   
  }

  $('beginTime').value=showMonthFirstDay();
  $('endTime').value=showMonthLastDay();
  $('pbeginTime').value=showMonthFirstDay();
  $('pendTime').value=showMonthLastDay();
  $('bbeginTime').value=showMonthFirstDay();
  $('bendTime').value=showMonthLastDay();

	function clrTxt()
	{
		document.getElementById("groupCode").value = "";
	}
  
//设置超链接 end
	
</script>
<!--页面列表 end -->

</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆维修换件次数</title>
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
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理&gt;轿车售后索赔报表&gt;车辆维修换件次数</div>

  <form method="post" name="fm" id="fm">
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
   <!-- 查询条件 begin -->
  <table class="table_query">
 		<tr>
            <td align="right" nowrap>起止时间： </td>
            <td nowrap>
            	<div align="left">
            		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            		&nbsp;至&nbsp;
            		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            	</div>
			</td>
            <td align="right" nowrap>出厂时间： </td>
            <td nowrap>
            	<div align="left">
            		<input name="pbeginTime" id="t3" value="" type="text" class="short_txt" datatype="0,is_date,10" group="t3,t4" hasbtn="true" callFunction="showcalendar(event, 't3', false);">
            		&nbsp;至&nbsp;
            		<input name="pendTime" id="t4" value="" type="text" class="short_txt" datatype="0,is_date,10" group="t3,t4" hasbtn="true" callFunction="showcalendar(event, 't4', false);">
            	</div>
			</td>
          </tr>
          <tr>
            <td align="right" nowrap>购车时间： </td>
            <td nowrap>
            	<div align="left">
            		<input name="bbeginTime" id="t5" value="" type="text" class="short_txt" datatype="0,is_date,10" group="t5,t6" hasbtn="true" callFunction="showcalendar(event, 't5', false);">
            		&nbsp;至&nbsp;
            		<input name="bendTime" id="t6" value="" type="text" class="short_txt" datatype="0,is_date,10" group="t5,t6" hasbtn="true" callFunction="showcalendar(event, 't6', false);">
            	</div>
			</td>
            <td align="right" nowrap>维修类别： </td>
            <td nowrap>
		    	<script type="text/javascript">
 					genSelBoxExp("claType",<%=Constant.CLA_TYPE%>,"",true,"short_sel","","false",'<%=Constant.CLA_TYPE_02%>,<%=Constant.CLA_TYPE_03%>,<%=Constant.CLA_TYPE_04%>,<%=Constant.CLA_TYPE_05%>,<%=Constant.CLA_TYPE_06%>,<%=Constant.CLA_TYPE_08%>');
				</script>
			</td>
          </tr>                 
          <tr>
            <td align="right" nowrap="nowrap">车型大类：</td>
            <td nowrap>
            	<select class="short_sel" name="modelName">
            		<option value="">-请选择-</option>
	            	<c:if test="${modelList!=null}">
						<c:forEach items="${modelList}" var="list2">
							<option value="${list2.WRGROUP_ID}">${list2.WRGROUP_NAME}</option>
						</c:forEach>
					</c:if>
				</select>
			</td>
			<td align="right" nowrap="nowrap">车型：</td>
            <td>
            	<input type="text" class="long_txt" name="groupCode" size="15" id="groupCode" value="" />
		   		<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','','true','3')" value="&hellip;" />
           		<input class="normal_btn" type="button" value="清空" onclick="clrTxt('groupCode');"/>
            </td>
          </tr>
          <tr>
            <td align="right" nowrap >&nbsp;</td>
            <td colspan="6" nowrap>&nbsp;</td>
            <td align="left" nowrap>
            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询"  onclick="__extQuery__(1)"/>&nbsp;
            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="下载"  onclick="exportExcel()" />
            </td>
            <td>&nbsp;</td>
            <td align="right" >&nbsp;</td>
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
	var url = "<%=contextPath%>/report/jcafterservicereport/PreserveVehicleChangeCountReport/queryPreserveVehicleWorkCountReport.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'NUM', align:'center'},
				{header: "车型", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "作业代码",sortable: false,dataIndex: 'PART_CODE' ,align:'center'},
				{header: "作业名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "次数", dataIndex: 'PCOUNT', align:'center'}
		      ];
		      
//设置超链接  begin      
	function exportExcel()
	{
		fm.action = "<%=contextPath%>/report/jcafterservicereport/PreserveVehicleChangeCountReport/preserveVehicleWorkCountReportExcel.do";
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
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>合格证送达情况</title>
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
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理&gt;合格证送达情况</div>

  <form method="post" name="fm" id="fm">
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
   <!-- 查询条件 begin -->
  <table class="table_query">
 		<tr>
            <td align="right" nowrap>寄送时间： </td>
            <td nowrap>
            	<div align="left">
            		<input name="beginTime" id="t1" value="" type="text" class="short_txt"  group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            		&nbsp;至&nbsp;
            		<input name="endTime" id="t2" value="" type="text" class="short_txt"  group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            	</div>
			</td>
			 <td width="19%" align="right" nowrap="nowrap">发运单号：</td>
            <td colspan="2" align="left" >
            	<input class="middle_txt" id="dmsOrderCode" name="dmsOrderCode" value="" type="text" />     	
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
	var url = "<%=contextPath%>/report/jcafterservicereport/CertificationStatus/queryCertificationStatus.json";
				
	var title = null;

	var columns = [
				{header: "发运单号", dataIndex: 'DMS_ORDER_CODE', align:'center'},
				{header: "生产基地", dataIndex: 'CA_ERP_ORG_NAME', align:'center'},
				{header: "经销商ERP代码",dataIndex: 'DEALER_ERP_CODE' ,align:'center'},
				{header: "订单车辆数量", dataIndex: 'CAR_QUANTITY', align:'center'},
				{header: "资金类型", dataIndex: 'ACCOUNT_TYPE_NAME', align:'center'},
				{header: "创建时间", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "查看明细", dataIndex: '', align:'center',renderer:oper}
		      ];

//查看明细
	function oper(value,meta,record) {      
			  return String.format("<a href=\"#\" onclick='viewDetail(\""+record.data.DMS_ORDER_CODE+"\",\""+record.data.CA_ERP_ORG_CODE+"\")'>[明细]</a>");		
		}

    function viewDetail(ORDER,ORG){
     //MyAlert('ORDER:'+ORDER+'ORG:'+ORG);
     OpenHtmlWindow('<%=contextPath%>/jsp/report/jcafterservicereport/showCertification.jsp?ORDER='+ORDER+'&ORG='+ORG,700,500);  
    }
		      
//设置超链接  begin      
	function exportExcel()
	{
		fm.action = "<%=contextPath%>/report/jcafterservicereport/CertificationStatus/CertificationStatusExcel.do";
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
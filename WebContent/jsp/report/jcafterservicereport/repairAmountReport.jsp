<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆维修明细报表</title>
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
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 报表管理&gt;技术室管理&gt;车辆维修明细报表</div>

  <form method="post" name="fm" id="fm">
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
   <!-- 查询条件 begin -->
  <table class="table_query">
 		<tr>
            <td align="right" nowrap>生产日期： </td>
            <td nowrap>
            	<div align="left">
            		<input name="beginTime" id="t1" value="" type="text" class="short_txt"  group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            		&nbsp;至&nbsp;
            		<input name="endTime" id="t2" value="" type="text" class="short_txt"  group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            	</div>
			</td>
            <td align="right" nowrap>审核通过日期： </td>
            <td nowrap>
            	<div align="left">
            		<input name="pbeginTime" id="t3" value="" type="text" class="short_txt"  group="t3,t4" hasbtn="true" callFunction="showcalendar(event, 't3', false);">
            		&nbsp;至&nbsp;
            		<input name="pendTime" id="t4" value="" type="text" class="short_txt"  group="t3,t4" hasbtn="true" callFunction="showcalendar(event, 't4', false);">
            	</div>
			</td>
          </tr>
          <tr>
            <td align="right" nowrap>购车时间： </td>
            <td nowrap>
            	<div align="left">
            		<input name="bbeginTime" id="t5" value="" type="text" class="short_txt"  group="t5,t6" hasbtn="true" callFunction="showcalendar(event, 't5', false);">
            		&nbsp;至&nbsp;
            		<input name="bendTime" id="t6" value="" type="text" class="short_txt"  group="t5,t6" hasbtn="true" callFunction="showcalendar(event, 't6', false);">
            	</div>
			</td>
            <td width="19%" align="right" nowrap="nowrap">经销商代码：</td>
            <td colspan="2" align="left" >
            	<input class="middle_txt" id="dealerCode" name="dealerCode" value="${dealerCode}" type="text" />
            	<input class="mark_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCode','','true','','true')"/>   
            	<input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/> 
            </td>
          </tr>                 
          <tr>
            <td align="right" nowrap>车系：</td>
            <td>			
            	<select class="short_sel" name="seriesName">
            		<option value="">-请选择-</option>
	            	<c:if test="${seriesList!=null}">
						<c:forEach items="${seriesList}" var="list3">
							<option value="${list3.GROUP_CODE}">${list3.GROUP_NAME}</option>
						</c:forEach>
					</c:if>
				</select>   	     
            </td> 
			<td align="right" nowrap="nowrap">车型名称：</td>
            <td>
            	<input class="middle_txt" id="modelName" name="modelName" value="" type="text" />	   		
            </td>
          </tr>
          <tr>
            <td align="right" nowrap>生产基地：</td>
             <td>
				<script type="text/javascript">
					genSelBoxContainStr("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel",'',"false",'${yieldly}');
				</script>
            </td>     
            <td align="right" >零件授权：</td>
            <td align="left">
            	<input  id="partYes" name="partYes" value="10041001" type="checkbox" checked="checked"/>是
            	<input  id="partNo" name="partNo" value="10041002" type="checkbox" />否
            	<input  id="NoNeed" name="NoNeed" value="NoNeed" type="checkbox" checked="checked"/>不需要授权
            </td> 
            </tr>
            <tr>         		
            <td align="right" >故障描述：</td>
            <td align="left">
            	<input class="middle_txt" id="bugDesc" name="bugDesc" value="" type="text" />
            </td>
            <td align="right" >旧件名称：</td>
            <td align="left">
            	<input class="middle_txt" id="oldPartName" name="oldPartName" value="" type="text" />
            </td>
		 </tr>
            <tr>    		
            <td align="right" >故障名称：</td>
            <td align="left">
            	<input class="middle_txt" id="bugName" name="bugName" value="" type="text" />
            </td>  			
			<td align="right" >零件名称：</td>
            <td align="left">
            	<input class="middle_txt" id="partName" name="partName" value="" type="text" />
            </td>		
          </tr>
          <tr>
           <td align="right" >VIN码：</td>
            <td align="left">
            	<input class="middle_txt" id="vin" name="vin" value="" type="text" />
            </td>
          </tr>       
          <tr>
            <td align="right" >作业名称：</td>
            <td align="left">
            	<input class="middle_txt" id="jobName" name="jobName" value="" type="text" />
            </td>
            <td align="left" nowrap>
            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询"  onclick="__extQuery__(1)"/>&nbsp;
            	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="下载"  onclick="exportExcel()" />
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
	var url = "<%=contextPath%>/report/jcafterservicereport/RepairAmountReport/queryRepairAmountReport.json";
				
	var title = null;

	var columns = [
				{header: "索赔申请单号", dataIndex: 'CLAIM_NO', align:'center'},
				{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "VIN码", dataIndex: 'VIN', align:'center'},
				{header: "发动机号",dataIndex: 'ENGINE_NO' ,align:'center'},
				{header: "生产日期", dataIndex: 'PRODUCT_DATE', align:'center'},
				{header: "购车日期", dataIndex: 'PURCHASED_DATE', align:'center'},
				{header: "工单开始日期", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "核通过日期", dataIndex: 'AUDITING_DATE', align:'center'},
				{header: "行驶里程", dataIndex: 'IN_MILEAGE', align:'center'},
				{header: "索赔类型", dataIndex: 'CLAIM_TYPE', align:'center'},
				{header: "质损类型", dataIndex: 'DEGRADATION_TYPE', align:'center',renderer:getItemValue},
				{header: "故障描述", dataIndex: 'TROUBLE_DESCS', align:'center'},
				{header: "故障原因", dataIndex: 'TROUBLE_REASON', align:'center'},
				{header: "维修措施", dataIndex: 'REPAIR_METHOD', align:'center'},
				{header: "申请备注", dataIndex: 'REMARK2', align:'center'},
				{header: "故障名称", dataIndex: 'TROUBLE_CODE_NAME', align:'center'},
				{header: "顾客问题-ccc代码", dataIndex: 'TROUBLE_TYPE', align:'center'},
				{header: "零件故障", dataIndex: 'REMARK', align:'center'},
				{header: "作业名称", dataIndex: 'WR_LABOURNAME', align:'center'},
				{header: "工时金额", dataIndex: 'LABOUR_AMOUNT', align:'center'},
				{header: "作业累积", dataIndex: 'FIRST_PART', align:'center'},
				{header: "整单追加工时", dataIndex: 'APPENDLABOUR_NUM', align:'center'},
				{header: "旧件编码", dataIndex: 'DOWN_PART_CODE', align:'center'},
				{header: "旧件名称", dataIndex: 'DOWN_PART_NAME', align:'center'},
				{header: "换件数量", dataIndex: 'QUANTITY', align:'center'},
				{header: "零件金额", dataIndex: 'AMOUNT', align:'center'},
				{header: "供应商", dataIndex: 'PRODUCER_NAME', align:'center'},
				{header: "分销中心", dataIndex: 'ROOT_ORG_NAME', align:'center'},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "接待员", dataIndex: 'SERVE_ADVISOR', align:'center'},
				{header: "维修站联系人", dataIndex: 'LINK_MAN', align:'center'},
				{header: "联系电话", dataIndex: 'PHONE', align:'center'},
				{header: "送修人", dataIndex: 'DELIVERER', align:'center'},
				{header: "送修人电话", dataIndex: 'DELIVERER_PHONE', align:'center'},
				{header: "零件是否授权", dataIndex: 'IS_AGREE', align:'center'}
		      ];
		      
//设置超链接  begin      
	function exportExcel()
	{
		fm.action = "<%=contextPath%>/report/jcafterservicereport/RepairAmountReport/getRepairAmountReportExcel.do";
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
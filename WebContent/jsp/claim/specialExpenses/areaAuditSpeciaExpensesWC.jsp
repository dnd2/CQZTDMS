<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>特殊费用审核(大区)</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}

</script>
</head>
<body>

<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理 &gt; 特殊费用管理 &gt;特殊费用审核(大区)</div>
  
  <form method="post" name="fm" id="fm">
  
  <!-- 车系列表 -->
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
  
   <!-- 查询条件 begin -->
    <table class="table_query" >
		<tr>
			<td align="right" nowrap="true">经销商代码：</td>
			<td align="left" nowrap="true">
				<input class="long_txt" id="dealerCode"  name="dealerCode" type="text"/>
	            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
	            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput('dealerCode');" value="清除"/>
			</td>
			<td align="right" nowrap="true">&nbsp;</td>
			<td align="left" nowrap="true">&nbsp;</td>
		</tr>
          <tr>
            <td width="7%" align="right" nowrap>费用编号：</td>
            <td colspan="6">
            	<input name="feeNo" id="feeNo" type="text" size="18"  class="middle_txt" value="" >
            </td>
			<td align="right">生产基地：</td>
			<td align="left">
				<script type="text/javascript">
					genSelBoxContainStr("YIELDLY",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'<%=CommonUtils.checkNull(request.getAttribute("yieldly"))%>');
		    	</script>
			</td>
          </tr>                  
          <tr>
            <td align="right" nowrap="nowrap" >上报时间：</td>
            <td colspan="6" nowrap="nowrap">
            	<div align="left">
            		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            		&nbsp;至&nbsp;
            		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            	</div>
            </td>
            <td align="right" nowrap="nowrap" >费用类型：</td>
            <td colspan="6" nowrap="nowrap">
              	<script type="text/javascript">
 					 genSelBoxExp2("feeType",<%=Constant.FEE_TYPE%>,"",true,"short_sel","","false",'');
			  	</script>
            </td>
          </tr>
          
           <tr style="display: none" id="fee">
          	<td colspan="7" nowrap="nowrap">
          	<td align="right" nowrap="nowrap" >费用渠道：</td>
          	 <td   id="fee_Channel" style="display: none" >
              	<script type="text/javascript">
 					 genSelBoxExp("feeChannel",<%=Constant.FEE_TYPE1%>,"",true,"short_sel","","false",'');
			  	</script>
            </td>
            <td  id="fee_Channel_1" style="display: none">
              	<script type="text/javascript">
 					 genSelBoxExp("feeChannel_1",<%=Constant.FEE_CHANNEL%>,"",true,"short_sel","","false",'');
			  	</script>
            </td>
          </tr>
          <tr>
            <td align="center" colspan="14">
            	<input class="normal_btn" type="button" name="queryBtn" id="queryBtn" value="查询"  onClick="__extQuery__(1);">
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
	var url = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/areaAuditQuerySpeciaExpensesWC.json?COMMAND=1";
				
	var title = null;

	var columns = [
				{header: "序号", renderer:getIndex, align:'center'},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "单据号码", dataIndex: 'FEE_NO', align:'center',renderer:mySelect},
				{header: "上报日期", dataIndex: 'MAKE_DATE', align:'center'},
				{header: "结算厂家", dataIndex: 'YIELD', align:'center',renderer:getItemValue},
				{header: "费用类型", dataIndex: 'FEE_TYPE', align:'center',renderer:getItemValue},
				{header: "申报金额(元)", dataIndex: 'DECLARE_SUM1', align:'center',renderer:amountFormat},
				{header: "工单状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
		      ];
		      
//设置超链接  begin      
	
	//修改的超链接
	function myLink(value,meta,record)
	{
  		return String.format("<a href='#' onclick='auditSpecialExpenses(\""+record.data.ID+"\",\""+ record.data.FEE_TYPE +"\")'>[审核]</a>");
	}
	
	//修改的超链接设置
	function auditSpecialExpenses(val1,val2)
	{
		fm.action = '<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/areaAuditSpecialExpensesWC.do?id=' + val1 + "&feeType=" + val2;
	 	fm.submit();
	}
	
	//设置超链接
	function mySelect(value,meta,record)
	{
  		return String.format("<a href=\"#\" onclick='sel(\""+record.data.ID+"\",\""+ record.data.FEE_TYPE +"\")'>["+ value +"]</a>");
	}
	
	//详细页面
	function sel(val1,val2)
	{
		OpenHtmlWindow('<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/speciaExpensesInfo.do?id='+val1+"&feeType="+val2,800,500);
	}
	
	function clearInput(inputId){
		var inputVar = document.getElementById(inputId);
		inputVar.value = '';
	}
	function doMySel(obj){
		  if(obj!=null||obj!=''){
				if(obj==<%=Constant.FEE_TYPE_01%>){
					$('fee').style.display="";
					$('fee_Channel').style.display="";
					$('fee_Channel_1').style.dis
					$('fee_Channel_1').style.display="none";
					
				}if(obj==<%=Constant.FEE_TYPE_02%>){
					$('fee').style.display="";
					$('fee_Channel_1').style.display="";
					$('fee_Channel').style.display="none";
				}
			}
		}
//设置超链接 end
	
</script>
<!--页面列表 end -->


</body>
</html>
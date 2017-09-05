<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>特殊费用查询</title>
<% String contextPath = request.getContextPath(); 
	String dutyType = String.valueOf(request.getAttribute("dutyType"));
%>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}

</script>
</head>
<body>

<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;特殊费用管理&gt;特殊费用查询</div>
  
  <form method="post" name="fm" id="fm">
  
  <!-- 车系列表 -->
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
  
   <!-- 查询条件 begin -->
    <table class="table_query" >
		<tr>
			<td align="right" nowrap="nowrap">经销商代码：</td>
			<td td colspan="6" align="left" nowrap="nowrap">
				<input class="long_txt" id="dealerCode"  name="dealerCode" type="text"/>
				<input class="long_txt" id="dealerId"  name="dealerId" type="hidden"/>
	            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','dealerId','true','',true,'','10771002');" value="..." />        
	            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput('dealerCode','dealerId');" value="清除"/>
			</td>
			<td align="right" nowrap="nowrap">生产基地：</td>
			<td colspan="6" nowrap="nowrap">
				<select style="width: 152px;" name="yieldly" id="yieldly">
				 <option value="" >
    				-请选择-
    			  </option>
	              <c:forEach var="Area" items="${Area}" >
 				  <option value="${Area.areaId}" >
    				<c:out value="${Area.areaName}"/>
    			  </option>
    			 </c:forEach>
             </select>
			</td>
		</tr>
          <tr>
            <td width="7%" align="right" nowrap>特殊费用编号：</td>
            <td colspan="6">
            	<input name="feeNo" id="feeNo" type="text" size="18"  class="middle_txt" value="" >
            </td>
			<td align="right" nowrap="nowrap" >费用类型：</td>
					<td>
						<script type="text/javascript">
 					 genSelBoxExp("feeType",<%=Constant.FEE_TYPE%>,"",true,"short_sel","","false",'');
			  	</script>	
					</td>
          </tr>                  
          <tr>
           <td align="right" nowrap="nowrap" >制单时间：</td>
            <td colspan="6" nowrap="nowrap">
            	<div align="left">
            		  <input class="short_txt" id="CREATE_DATE_S" name="CREATE_DATE_S" datatype="1,is_date,10"
                           maxlength="10" group="CREATE_DATE_S,CREATE_DATE_D"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'CREATE_DATE_S', false);" type="button"/>
                    至
                    <input class="short_txt" id="CREATE_DATE_D" name="CREATE_DATE_D" datatype="1,is_date,10"
                           maxlength="10" group="CREATE_DATE_S,CREATE_DATE_D"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'CREATE_DATE_D', false);" type="button"/>
            	</div>
            </td>
            <td align="right" nowrap="nowrap" >上报时间：</td>
            <td colspan="6" nowrap="nowrap">
            	<div align="left">
            		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            		&nbsp;至&nbsp;
            		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            	</div>
            </td>
            
          </tr>
          <tr>
          <td align="right" nowrap="nowrap" >单据状态：</td>
          <td colspan="6" nowrap="nowrap">
	         <script type="text/javascript">
			genSelBoxExp("STATUS",<%=Constant.SPEFEE_STATUS%>,"",true,"short_sel","","false",'');
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
	var url = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/queryOemSpeciaExpenses.json";
				
	var title = null;

	var columns = [
				{header: "序号", renderer:getIndex, align:'center'},				
				{header: "特殊费用单号", dataIndex: 'FEE_NO', align:'center',renderer:mySelect},
				{header: "服务站代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "服务站名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "制单日期", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "上报日期", dataIndex: 'MAKE_DATE', align:'center'},
				{header: "审核日期", dataIndex: 'AUDITING_DATE', align:'center'},
				{header: "产地", dataIndex: 'AREA_NAME', align:'center'},
				{header: "费用类型", dataIndex: 'FEE_TYPE', align:'center',renderer:getItemValue},
				{header: "申报金额", dataIndex: 'DECLARE_SUM', align:'center',renderer:amountFormat},
				{header: "服务经理审核意见", dataIndex: 'AUDITING_OPINION', align:'center'},
				{header: "部门总监审核意见", dataIndex: 'SECTION_AUDITING_OPINION', align:'center'},
				{header: "销售公司审核意见", dataIndex: 'MANEGER_AUDITING_OPINION', align:'center'},
				{header: "集体公司审核意见", dataIndex: 'OFFICE_AUDITING_OPINION', align:'center'},
				{header: "单据状态", dataIndex: 'STATUS', align:'center'},
				//{header: "单据最终状态", dataIndex: 'O_STATUS', align:'center'},
				{header: "预授权最终状态", dataIndex: 'N_LEVEL', align:'center'}
		      ];
		      
//设置超链接  begin      
	
	//修改的超链接
	function getStutasValue(value,meta,record)
	{
		var val = value;
		if(val=='<%=Constant.SPEFEE_STATUS_07 %>')
		{
			val = '<%=Constant.SPEFEE_STATUS_06 %>';
		}
		return getItemValue(val);
	}
	
	function myLink(value,meta,record)
	{
  		return String.format("<a href='#' onclick='auditSpecialExpenses(\""+record.data.ID+"\",\""+ record.data.FEE_TYPE +"\")'>[审核]</a>");
	}
	
	//修改的超链接设置
	function auditSpecialExpenses(val1,val2)
	{
		fm.action = '<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/auditSpecialExpensesInfo.do?id=' + val1 + "&feeType=" + val2;
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
	
	function clearInput(inputId,did){
		var inputVar = document.getElementById(inputId);
		inputVar.value = '';
		var didVar = document.getElementById(did);
		didVar.value = '';
	}

	function doCusChange(obj){
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
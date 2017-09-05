<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>正负激励</TITLE>
<% 
	String contextPath = request.getContextPath();
%>
<SCRIPT LANGUAGE="JavaScript">
	var myPage;
//查询路径
	var url = "<%=contextPath%>/report/service/DealerFinFDetail/queryOEMDealer.json";
				
	var title = null;
	var columns = [
				{header: "序号", renderer:getIndex, align:'center'},
				{header: "服务站代码", width:'10%',dataIndex: 'DEALER_CODE'},
				{header: "服务站名称", align:'center',dataIndex: 'DEALER_NAME'},
				{header: "补贴类型", align:'center',dataIndex: 'REMARK'},
				{header: "奖惩劳务费", align:'center',dataIndex: 'LABOUR_SUM'},
				{header: "奖惩材料费", align:'center',dataIndex: 'DATUM_SUM'},
				{header: "费用标志", align:'center',dataIndex: 'FINE_TYPE',renderer:getItemValue},
				{header: "可用标志", align:'center',dataIndex: 'PAY_STATUS',renderer:getItemValue},
				{header: "补贴编号", align:'center',dataIndex: 'LABOUR_BH'},
				{header: "导入人", align:'center',dataIndex: 'NAME'},
				{header: "录入时间", align:'center',dataIndex: 'FINE_DATE'},
				{header: "产地", width:'10%',dataIndex: 'AREA_NAME'},
				{header: "备注", align:'center',dataIndex: 'FINE_REASON'},
				{header: "开票通知单号", align:'center',dataIndex: 'BALANCE_ODER'}
	      ];
		      
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
	}
</SCRIPT>

</HEAD>
<BODY onload='doInit()'>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：报表管理&gt;其他功能&gt;正负激励报表</div>
  <form method="post" name = "fm" id="fm">
	
    <TABLE align=center width="95%" class="table_query" >
          <tr>
               <td  align="right">补贴编号：</td>
             <td>  <input name="ButieBh" id="ButieBh" value="" type="text" class="middle_txt" />
      
			</td>
           <td class="table_query_2Col_label_6Letter" >经销商代码：
           <input type="hidden"  id="CODE_ID" value="${code}"/>
           </td>
           
            <td  align="left" >
            	<input type="text" name="DEALER_CODE"  id="DEALER_CODE" />  
            </td>
            <td class="table_query_2Col_label_6Letter">经销商名称：</td>
            <td><input name="DEALER_NAME" id="DEALER_NAME" value="" type="text" class="middle_txt" datatype="1,is_digit_letter_cn" /></td>
          </tr>
          <tr>
            <td  align="right">奖惩时间：</td>
			<td  align="left" >
		 	<input class="short_txt" id="deductStartDate" name="deductStartDate" datatype="1,is_date,10"
           	 maxlength="10" group="deductStartDate,deductEndDate"/>
        	 <input class="time_ico" value=" " onclick="showcalendar(event, 'deductStartDate', false);" type="button"/>
          	 至
	         <input class="short_txt" id="deductEndDate" name="deductEndDate" datatype="1,is_date,10"
	             maxlength="10" group="deductStartDate,deductEndDate"/>
	         <input class="time_ico" value=" " onclick="showcalendar(event, 'deductEndDate', false);" type="button"/>
			</td>
			<td class="table_query_2Col_label_6Letter">补贴类型：</td>
           <td >
          	 <input name="SubsidiesType" id="SubsidiesType" value="" type="text" class="middle_txt" datatype="1,is_digit_letter_cn" />
           </td>
	       <td class="table_query_2Col_label_6Letter">奖惩类型：</td>
           <td >
          	 <script type="text/javascript">
 					 genSelBoxExp("fine_type",8064,"",true,"short_sel","","false",'');
			  	</script>
           </td>
          </tr>

          <tr>
            <td colspan="6" align="center" nowrap><input id="queryBtn" class="normal_btn" type="BUTTON" name="button1" value="查询"  onclick="__extQuery__(1);" />
            <input id="queryBtn" class="normal_btn" type="BUTTON" name="button1" value="导出excel"  onclick="goImport();" />
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


<br>

<SCRIPT LANGUAGE="JavaScript">
function goImport(){
    fm.action = '<%=contextPath%>/report/service/DealerFinFDetail/resourcesAuditDown.do';
    fm.submit();
}
</script>

</BODY>
</html>
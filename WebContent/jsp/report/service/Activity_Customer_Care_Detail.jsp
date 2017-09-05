<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.*" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>客户关怀活动数据明细表</TITLE>
<% 
	String contextPath = request.getContextPath();
   List<Map<String, Object>> list = (List<Map<String, Object>>)request.getAttribute("list");
%>
<SCRIPT LANGUAGE="JavaScript">

		      
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
   		
   		
	}
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/report/service/CustomerCareActivityDel/CustomerCareActivityQueryDel.json";
	var title = null;
	var columns = [
				{header: "大区",dataIndex: 'ROOT_ORG_NAME',align:'center'},
				{header: "服务商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "服务商名称",dataIndex: 'DEALER_NAME',align:'center'},
				{header: "主题编号", dataIndex: 'SUBJECT_NO', align:'center'},
				{header: "活动主题", dataIndex: 'SUBJECT_NAME', align:'center'},
				{header: "活动编号", dataIndex: 'ACTIVITY_CODE', align:'center'},
				{header: "工单号码", dataIndex: 'RO_NO', align:'center'},
				{header: "车型", dataIndex: 'MODEL', align:'center'},
				{header: "VIN号码", dataIndex: 'VIN', align:'center'},
				{header: "开单日期", dataIndex: 'RO_CREATE_DATE', align:'center'},
				{header: "公里数", dataIndex: 'IN_MILEAGE', align:'center'},
				{header: "车主", dataIndex: 'OWNER_NAME', align:'center'},
				{header: "车主电话", dataIndex: 'MAIN_PHONE', align:'center'},
				{header: "送修人", dataIndex: 'DELIVERER', align:'center'},
				{header: "送修人电话", dataIndex: 'DELIVERER_PHONE', align:'center'},
				{header: "免费检测费用", dataIndex: 'FREE_JC_AMOUNT', align:'center'},
				{header: "赠送金额", dataIndex: 'ZS_AMOUNT', align:'center'},
				{header: "赠送礼品费用", dataIndex: 'LP_AMOUNT', align:'center'},
				{header: "赠送配件费用", dataIndex: 'PART_ZS_AMOUNT', align:'center'},
				{header: "赠送保养金额", dataIndex: 'BAOYANG_ZS_AMOUNT', align:'center'},
				{header: "材料优惠费用", dataIndex: 'PART_AMOUNT', align:'center'},
				{header: "工时优惠费用", dataIndex: 'LABOUR_AMOUNT', align:'center'}


		      ];
</SCRIPT>

</HEAD>
<BODY onload='doInit()'>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后服务报表&gt;经销商客户关怀活动总结汇总表</div>
  <form method="post" name = "fm" id="fm">
	
    <TABLE align=center width="95%" class="table_query" >
        <tr>
    	     <td class="table_query_2Col_label_6Letter">活动类型：</td>
           <td >
			  <script type="text/javascript">
	              genSelBoxExp("serviceactivity_type",<%=Constant.SERVICEACTIVITY_TYPE%>,"",true,"short_sel","","false",'<%=Constant.SERVICEACTIVITY_TYPE_01%>');
	       </script>
           </td>
             <td  align="right">主题编号：</td>
             <td>  <input name="ButieBh" id="ButieBh" value="" type="text" class="middle_txt" />
      
			</td>
          </tr>
          <tr>
			<td wclass="table_query_2Col_label_6Letter"idth="15%" align="right">大区：</td>
				<td >
					<select class="short_sel" id="big_org" name="big_org" >
						<option value="">--请选择--</option>
						<%for(int i=0;i<list.size();i++){%>
							<option value="<%=list.get(i).get("ROOT_ORG_ID") %>"><%=list.get(i).get("ROOT_ORG_NAME") %></option>
						<%} %>
					</select>
				</td>
             <td  align="right">主题名称：</td>
             <td>  <input name="ButieName" id="ButieName" value="" type="text" class="middle_txt" />
			</td>
			</tr>
			<tr>
		 <td  class="table_query_3Col_label_5Letter">服务商代码：</td>
			<td align="left">
				<input type="text" name="dealer_code" id="dealer_code" class="long_txt"/>
				<input type="hidden" name="dealer_id" id="dealer_id"/>
				<input type="button" class="mini_btn" value="..." onclick="showOrgDealer('dealer_code','dealer_id',true,'',true,'','10771002');"/>
            	<input type="button" class="normal_btn" value="清除" onclick="clrTxt('dealer_id','dealer_code')"/>
			</td>
			<tr>
            <td colspan="6" align="center" nowrap><input id="queryBtn" class="normal_btn" type="BUTTON" name="button1" value="查询"  onclick="__extQuery__(1);" />
            <input id="queryBtn" class="normal_btn" type="BUTTON" name="button1" value="导出excel"  onclick="goImport();" />
            </td>
          </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->

</form>


<br>

<SCRIPT LANGUAGE="JavaScript">
function clrTxt(value1,value2){
	document.getElementById(value1).value = "";
	document.getElementById(value2).value = "";

}
function chaxun(){
    fm.action = '<%=contextPath%>/report/service/CustomerCareActivity/CustomerCareActivityQuery.do';
    fm.submit();	
	
}

function goImport(){
	
    fm.action = '<%=contextPath%>/report/service/CustomerCareActivityDel/CustomerCareActivityDelExport.do';
    fm.submit();
}
</script>

</BODY>
</html>
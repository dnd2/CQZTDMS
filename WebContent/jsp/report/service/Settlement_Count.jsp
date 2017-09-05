<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.*" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>结算明细</TITLE>
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
 	var url = "<%=contextPath%>/report/service/BaseReport/SettlementCountQueryData.json";
	var title = null;
	var columns = [
				{header: "服务站代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "上报年月",dataIndex: 'REPORT_DATE',align:'center'},
				{header: "结算编号",dataIndex: 'BALANCE_ODER',align:'center'},
				{header: "服务站名称",dataIndex: 'ROOT_DEALER_NAME',align:'center'},
				{header: "正常维修数量",dataIndex: 'CLA_TYPE_01',align:'center'},
				{header: "售前单数量",dataIndex: 'CLA_TYPE_07',align:'center'},
				{header: "特殊单数量",dataIndex: 'CLA_TYPE_10',align:'center'},
				{header: "外派单数量",dataIndex: 'CLA_TYPE_09',align:'center'},
				{header: "工时费",dataIndex: 'BALANCE_LABOUR_AMOUNT',align:'center'},
				{header: "材料费",dataIndex: 'BALANCE_PART_AMOUNT',align:'center'},
				{header: "派出服务费",dataIndex: 'BALANCE_NETITEM_AMOUNT',align:'center'},
				{header: "强保数量",dataIndex: 'CLA_TYPE_02',align:'center'},
				{header: "强保工时费",dataIndex: 'LABOUR_PRICE',align:'center'},
				{header: "强保材料费",dataIndex: 'PART_PRICE',align:'center'},
				{header: "强保金额合计",dataIndex: 'COUNT_PRICE',align:'center'},
				{header: "活动数量",dataIndex: 'CLA_TYPE_06',align:'center'},
				{header: "活动工时费",dataIndex: 'BALANCE_LABOUR_AMOUNT_06',align:'center'},
				{header: "活动材料费",dataIndex: 'BALANCE_PART_AMOUNT_06',align:'center'},
				{header: "活动赠送费",dataIndex: 'BALANCE_NETITEM_AMOUNT_06',align:'center'},
				{header: "活动金额合计",dataIndex: 'BALANCE_AMOUNT_06',align:'center'},
				{header: "特殊费用数量",dataIndex: 'SPEE_COUNT',align:'center'},
				{header: "特殊费用工时费",dataIndex: 'SPEE_LABOUR',align:'center'},
				{header: "特殊费用材料费",dataIndex: 'SPEE_PART',align:'center'},
				{header: "特殊费用金额合计",dataIndex: 'SPEE_BALANCE',align:'center'},
				{header: "凭证补办数量",dataIndex: 'PING_COUNT',align:'center'},
				{header: "凭证补办金额合计",dataIndex: 'PING_BALANCE',align:'center'},
				{header: "正负激励数量",dataIndex: 'FINE_COUNT',align:'center'},
				{header: "正负激励工时费",dataIndex: 'FINE_LABOUR',align:'center'},
				{header: "正负激励材料费",dataIndex: 'FINE_PART',align:'center'},
				{header: "运费",dataIndex: 'YUNFEI',align:'center'},
				{header: "抵扣费用",dataIndex: 'DISCOUNT',align:'center'},
				{header: "总计单据总数",dataIndex: 'COUNT_SUM',align:'center'},
				{header: "总计劳务费",dataIndex: 'BALANCE_LABOUR_AMOUNT_COUNT',align:'center'},
				{header: "总计材料费",dataIndex: 'BALANCE_PART_AMOUNT_COUNT',align:'center'},
				{header: "总计金额总计",dataIndex: 'BALANCE_AMOUNT_COUNT',align:'center'}

		      ]; 
</SCRIPT>

</HEAD>
<BODY onload='doInit()'>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后服务报表&gt;各服务商结算汇总</div>
  <form method="post" name = "fm" id="fm">
	
    <TABLE align=center width="95%" class="table_query" >
       
         <tr>
	        <td align="right">结算基地：</td>
			<td>
				<script type="text/javascript">
				    genSelBoxExp("YIELDLY_TYPE",9541,"",true,"short_sel","","false",'');
				</script>
			</td>
			<td width="15%" align="right">系统确认时间：</td>
			<td align="left" nowrap="true">
			<input name="bDate" type="text" class="short_time_txt" id="bDate" group="bDate,eDate"  datatype="1,is_date,10"readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'bDate', false);" />  	
	            &nbsp;至&nbsp; <input name="eDate" type="text" class="short_time_txt" group="bDate,eDate"  datatype="1,is_date,10"id="eDate" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'eDate', false);" /> 
			</td>
						
			</tr>

			<tr>
				 <td align="right">服务商商代码：</td>
				 <td align="left">
	              <input id="supply_code" name="supply_code" value="" type="text" class="middle_txt" >
				 </td>
				 <td  align="right">服务商全称：</td>
	             <td>  <input name="supply_name" id="supply_name" value="" type="text" class="middle_txt" />
				</td>
			</tr>
			<tr>
			 <td width="15%" align="right">大区：</td>
			<td width="30%" align="left">
				<select class="short_sel" id="big_org" name="big_org" onchange="updateSmallOrg(this)">
					<option value="">--请选择--</option>
					<%for(int i=0;i<list.size();i++){%>
						<option value="<%=list.get(i).get("ROOT_ORG_ID") %>"><%=list.get(i).get("ROOT_ORG_NAME") %></option>
					<%} %>
				</select>
			</td>
			<td width="15%" align="right">小区：</td>
				<td width="30%" align="left" id = "small">
					<select class="short_sel" id="small_org" name="small_org" >
						<option value="">--请选择--</option>
					</select>
			</td>
			</tr>
			
			<tr>
            <td colspan="6" align="center" nowrap><input id="queryBtn" class="normal_btn" type="BUTTON" name="button1" value="查询"  onclick=" __extQuery__(1);" />
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
function   showMonthFirstDay()     
{     
	  var   Nowdate=new   Date();     
	  var   MonthFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth(),1);     
	  return DateUtil.Format("yyyy-MM-dd",MonthFirstDay);  
}     
function   showMonthLastDay()     
{     
	  var   Nowdate=new   Date();     
	  var   MonthNextFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth()+1,1);     
	  var   MonthLastDay=new   Date(MonthNextFirstDay-86400000);     
	  return DateUtil.Format("yyyy-MM-dd",MonthLastDay);   
}     
// $('CREATE_DATE_STR').value=showMonthFirstDay();
//$('bDate').value=showMonthFirstDay();
//$('eDate').value=showMonthLastDay();


//清空
function clrTxt1(str){
	document.getElementById(str).value = "";
}

function goImport(){
	
    fm.action = '<%=contextPath%>/report/service/BaseReport/SettlementCountExport.do';
    fm.submit();
}


function updateSmallOrg(obj){
	if(obj.value==""){
	var str = '<select class="short_sel" id="small_org" name="small_org" > <option value="">--请选择--</option> </select>';
		document.getElementById('small').innerHTML = str;
	}else{
		makeNomalFormCall('<%=contextPath%>/report/service/ClaimReport/changeSmallOrg.json?ID='+obj.value,changeBack,'fm','');
	}
	}
	
	function changeBack(json) {
		if(json.smallList != null && json.smallList != "") {
		document.getElementById('small').innerHTML = json.smallList;
		} else {
			MyAlert("加载小区失败！");
		}
	}


</script>

</BODY>
</html>
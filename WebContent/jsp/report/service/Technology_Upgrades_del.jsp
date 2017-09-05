<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.*" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>技术升级明细查询</TITLE>
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
 	var url = "<%=contextPath%>/report/service/TechnologyUpgrade/TechnologyUpgradeQueryData.json";
	var title = null;
	var columns = [
				{header: "结算基地",dataIndex: 'BALANCE_YIELDLY',align:'center',renderer:getItemValue},
				{header: "工单号",dataIndex: 'RO_NO',align:'center'},
				{header: "结算单号",dataIndex: 'CLAIM_NO',align:'center'},
				{header: "服务站代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "服务站全称", dataIndex: 'DEALER_NAME', align:'center'},				
				{header: "一级站代码", dataIndex: 'PDEALER_CODE', align:'center'},
				{header: "一级站全称", dataIndex: 'PDEALER_NAME', align:'center'},
				{header: "大区", dataIndex: 'ROOT_ORG_NAME', align:'center'},
				{header: "小区", dataIndex: 'ORG_NAME', align:'center'},
				{header: "车系(名称)", dataIndex: 'SERIES_NAME', align:'center'},
				{header: "车型", dataIndex: 'MODEL_CODE', align:'center'},
				{header: "牌照号", dataIndex: 'LICENSE_NO', align:'center'},
				{header: "发动机号", dataIndex: 'ENGINE_NO', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "生产日期", dataIndex: 'PRODUCT_DATE', align:'center'},
				{header: "购车日期", dataIndex: 'PURCHASED_DATE', align:'center'},
				{header: "车主", dataIndex: 'CTM_NAME', align:'center'},
				{header: "车主电话", dataIndex: 'MAIN_PHONE', align:'center'},
				{header: "车主地址", dataIndex: 'ADDRESS', align:'center'},
				{header: "工单维修日期", dataIndex: 'RO_CREATE_DATE', align:'center'},
				{header: "工单结算日期", dataIndex: 'FOR_BALANCE_TIME', align:'center'},
				
				{header: "行驶里程",dataIndex: 'IN_MILEAGE',align:'center'},
				{header: "配件维修类型",dataIndex: 'REPAIR_TYPE',align:'center'},
				{header: "故障代码",dataIndex: 'MAL_NAME',align:'center'},
				{header: "故障描述", dataIndex: 'REMARK', align:'center'},
				{header: "故障件代码", dataIndex: 'DOWN_PART_CODE', align:'center'},				
				{header: "故障件件号", dataIndex: 'DOWN_PART_NO', align:'center'},
				{header: "故障件名称", dataIndex: 'DOWN_PART_NAME', align:'center'},
				{header: "是否主因件", dataIndex: 'RESPONSIBILITY_TYPE', align:'center',renderer:getItemValue},
				{header: "更换件代码", dataIndex: 'PART_CODE', align:'center'},
				{header: "更换件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "配件单价", dataIndex: 'APPLY_PRICE', align:'center'},
				{header: "配件数量", dataIndex: 'QUANTITY', align:'center'},
				{header: "材料费", dataIndex: 'APPLY_AMOUNT', align:'center'},
				{header: "主因件工时单价", dataIndex: 'APPLY_PRICE1', align:'center'},
				{header: "主因件工时费", dataIndex: 'APPLY_AMOUNT1', align:'center'},
				{header: "换下件制造商代码", dataIndex: 'DOWN_PRODUCT_CODE', align:'center'},
				{header: "换下件制造商名称", dataIndex: 'DOWN_PRODUCT_NAME', align:'center'},
				{header: "活动编号", dataIndex: 'ACTIVITY_CODE', align:'center'},
				{header: "活动名称", dataIndex: 'ACTIVITY_NAME', align:'center'},
				{header: "优惠材料系数", dataIndex: 'FREE_PART', align:'center'},
				{header: "优惠工时系数", dataIndex: 'FREE_LABOUR', align:'center'},
				
				{header: "赠送费用", dataIndex: 'APPLY_AMOUNT2', align:'center'},
				{header: "活动开始日期", dataIndex: 'FACTSTARTDATE', align:'center'},
				{header: "活动结束日期", dataIndex: 'FACTENDDATE', align:'center'}

		      ]; 
</SCRIPT>

</HEAD>
<BODY onload='doInit()'>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后服务报表&gt;技术升级明细查询</div>
  <form method="post" name = "fm" id="fm">
	
    <TABLE align=center width="95%" class="table_query" >
        <tr>
        <td align="right">结算基地：</td>
		<td>
			<script type="text/javascript">
			    genSelBoxExp("YIELDLY_TYPE",<%=Constant.PART_IS_CHANGHE%>,"",true,"short_sel","","false",'');
			</script>
		</td>
             <td  align="right">VIN：</td>
             <td> <input id="vin" class="middle_txt" name="vin" />
			</td>
          </tr>
        <tr>
       <td align="right">车系选择：</td>
		         <td>
	         		<select id="serisid" name="serisid" style="width: 152px;">
		        		<option value="">-- 请选择 --</option>
		        		<c:forEach items="${serislist }" var="list">
		        			<option value="${list.GROUP_CODE }">${list.GROUP_NAME }</option>
		        		</c:forEach>
		        	</select>
		         </td>
		 <td align="right">车型选择：</td>
         <td>
             <input type="text"  name="groupCode" class="middle_txt" size="15" id="groupCode" value=""  jset="para"/>
	         <input name="button3" type="button" class="normal_btn" style="width:20px;" onclick="showMaterialGroup('groupCode','','true','3')" value="..." />
	         <input class="normal_btn" type="button" value="清除" onclick="clrTxt1('groupCode');"/>
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
		 <td align="right">经销商代码：</td>
			<td align="left">
				<input type="text" name="dealer_code" id="dealer_code" class="long_txt"/>
				<input type="hidden" name="dealer_id" id="dealer_id"/>
				<input type="button" class="mini_btn" value="..." onclick="showOrgDealer('dealer_code','dealer_id',true,'',true,'','10771002');"/>
            	<input type="button" class="normal_btn" value="清除" onclick="clrTxt('dealer_id','dealer_code')"/>
			</td>
			 <td  align="right">经销商全称：</td>
             <td>  <input name="dealerName" id="dealerName" value="" type="text" class="middle_txt" />
			</td>
			</tr>
			<tr>
					<td width="15%" align="right">工单维修日期：</td>
			<td align="left" nowrap="true">
				<input name="bDate" type="text" class="short_time_txt" group="bDate,eDate" datatype="1,is_date,10" id="bDate" readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'bDate', false);" />  	
	             &nbsp;至&nbsp; <input name="eDate" type="text"  class="short_time_txt" id="eDate" group="bDate,eDate" datatype="1,is_date,10"  readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'eDate', false);" /> 
			</td>
			<td width="15%" align="right">发动机：</td>
			<td width="30%" align="left">
				<input type="text" name="engine_no" value="" class="middle_txt" id="engine_no"/>
				
		  </td>
			</tr>
			<tr>
					<td width="15%" align="right">工单结算日期：</td>
			<td align="left" nowrap="true">
				<input name="bgDate" type="text" class="short_time_txt" id="bgDate" group="bgDate,egDate" datatype="1,is_date,10"  readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'bgDate', false);" />  	
	             &nbsp;至&nbsp; <input name="egDate" type="text" class="short_time_txt" id="egDate" group="bgDate,egDate" datatype="1,is_date,10"  readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'egDate', false);" /> 
			</td>
			<td width="15%" align="right">活动编号：</td>
			<td width="30%" align="left">
				<input type="text" name="activityNo" value="" class="middle_txt" id="activityNo" 	/>
		    	<input type="hidden" name="activityId" value="" id="activityId" />
				
				<input type="button" class="mini_btn" value="..." onclick="showActivity('activityId','activityId',true,'','','')"/>
				<input type="button" class="normal_btn" value="清除" onclick="clrTxt('activityNo','activityId')"/>
		  </td>
			</tr>
						<tr>
					<td width="15%" align="right">生产日期：</td>
			<td align="left" nowrap="true">
				<input name="crbDate" type="text" class="short_time_txt" id="crbDate" group="crbDate,creDate" datatype="1,is_date,10" readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'crbDate', false);" />  	
	             &nbsp;至&nbsp; <input name="creDate" type="text" class="short_time_txt" id="creDate" group="crbDate,creDate" datatype="1,is_date,10" readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'creDate', false);" /> 
			</td>
			<td width="15%" align="right">活动名称：</td>
			<td width="30%" align="left">
				<input type="text" name="activityName" value="" class="middle_txt" id="activityName"/>
		  </td>
			</tr>
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

function showActivity(inputCode ,inputName ,isMulti , areaId, ids, productId){
	if(!inputCode){ inputCode = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow(g_webAppName+"/jsp/report/service/show_activity_del.jsp?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&areaId="+areaId+"&ids="+ids+"&productId="+productId,730,390);
}
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
$('bDate').value=showMonthFirstDay();
$('eDate').value=showMonthLastDay();
$('bgDate').value=showMonthFirstDay();
$('egDate').value=showMonthLastDay();
$('crbDate').value=showMonthFirstDay();
$('creDate').value=showMonthLastDay();
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
function clrTxt(value1,value2){
	document.getElementById(value1).value = "";
	document.getElementById(value2).value = "";

}
//清空
function clrTxt1(str){
	document.getElementById(str).value = "";
}

function goImport(){
	
    fm.action = '<%=contextPath%>/report/service/TechnologyUpgrade/TechnologyUpgradeQueryDataExport.do';
    fm.submit();
}
</script>

</BODY>
</html>
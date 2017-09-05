<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.*" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>索赔旧件签收情况明细表</TITLE>
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
 	var url = "<%=contextPath%>/report/service/ClaimOldSignDel/ClaimOldSignDelQueryData.json";
	var title = null;
	var columns = [

				{header: "服务站代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "服务站全称", dataIndex: 'DEALER_NAME', align:'center'},				
				{header: "回运批次", dataIndex: 'PC', align:'center'},
				{header: "小区", dataIndex: 'ORG_NAME', align:'center'},
				{header: "货运方式", dataIndex: 'TRANSPORT_TYPE', align:'center'},
				{header: "回运类型", dataIndex: 'RETURN_TYPE', align:'center'},
				{header: "发运单号", dataIndex: 'TRANSPORT_NO', align:'center'},
				{header: "发运时间", dataIndex: 'SEND_TIME', align:'center'},
				{header: "装箱总数", dataIndex: 'PARKAGE_AMOUNT', align:'center'},
				{header: "实到箱数", dataIndex: 'REAL_BOX_NO', align:'center'},
				{header: "包装情况", dataIndex: 'PART_PAKGE', align:'center'},
				{header: "故障卡情况", dataIndex: 'PART_MARK', align:'center',renderer:getItemValue},
				{header: "清单情况", dataIndex: 'PART_DETAIL', align:'center'},
				{header: "签收人", dataIndex: 'NAME', align:'center'},
				{header: "签收时间", dataIndex: 'SIGN_DATE', align:'center'},
				{header: "三包员电话", dataIndex: 'TEL', align:'center'},
				{header: "签收备注",dataIndex: 'SIGN_REMARK',align:'center'}

		      ]; 
</SCRIPT>

</HEAD>
<BODY onload='doInit()'>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后服务报表&gt;索赔旧件签收情况明细表</div>
  <form method="post" name = "fm" id="fm">
	
    <TABLE align=center width="95%" class="table_query" >

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
					<td width="15%" align="right">发运时间：</td>
			<td align="left" nowrap="true">
				<input name="bDate" type="text" class="short_time_txt" id="bDate" readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'bDate', false);" />  	
	             &nbsp;至&nbsp; <input name="eDate" type="text" class="short_time_txt" id="eDate" readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'eDate', false);" /> 
			</td>
			<td width="15%" align="right">回运批次：</td>
			<td width="30%" align="left">
				<input type="text" name="delivery_batch" value="" class="middle_txt" id="delivery_batch"/>
		  </td>
			</tr>
			<tr>
					<td width="15%" align="right">签收时间：</td>
			<td align="left" nowrap="true">
				<input name="bgDate" type="text" class="short_time_txt" id="bgDate" readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'bgDate', false);" />  	
	             &nbsp;至&nbsp; <input name="egDate" type="text" class="short_time_txt" id="egDate" readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'egDate', false);" /> 
			</td>
			<td width="15%" align="right">签收人：</td>
			<td width="30%" align="left">
				<input type="text" name="Signpeople" value="" class="middle_txt" id="Signpeople"/>
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
	
    fm.action = '<%=contextPath%>/report/service/ClaimOldSignDel/ClaimOldSignDelExport.do';
    fm.submit();
}
</script>

</BODY>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat" %>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>索赔单上报</TITLE>

<SCRIPT LANGUAGE="JavaScript">
var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/application/ClaimBillStatusTrack/applicationQuerydate.json";
				
	var title = null;
	var columns = [
					{header: "序号", align:'center', renderer:getIndex,align:'center'},
					{header: "选择<input type='checkbox' name='checkAll' onclick='selectAll(this,\"checkId\")' />", width:'3%',align:'center',sortable: false,dataIndex: 'ID',renderer:myCheckBox},
					{header: "索赔单号", width:'', dataIndex: 'CLAIM_NO'},
					{header: "开票日期", width:'', dataIndex: 'INVOICE_DATE'},
					{header: "邮寄日期", width:'', dataIndex: 'POST_DATE'},
					{header: "收票日期", width:'', dataIndex: 'TICK_DATE'},
					{header: "审核日期", width:'', dataIndex: 'RO_STARTDATE'},
					{header: "结算日期", width:'', dataIndex: 'RO_ENDDATE'},
					{header: "凭证日期", width:'', dataIndex: 'PROOF_DATE'},
					{header: "索赔单类型", width:'', dataIndex: 'CLAIM_TYPE',renderer:getItemValue},
					{header: "产地", width:'', dataIndex: 'YIELDLY'},
					{header: "经销商", width:'', dataIndex: 'DEALER_NAME'},
					{header: "车型", width:'', dataIndex: 'GROUP_NAME'},
					{header: "VIN", width:'', dataIndex: 'VIN'},
					{header: "申请费", width:'', dataIndex: 'REPAIR_TOTAL'},
					{header: "结算费用", width:'', dataIndex: 'GROSS_CREDIT'},
					{header: "状态", width:'', dataIndex: 'STATUS',renderer:getItemValue}
		      ];
		      
	    function doInit()
		{
		   loadcalendar(); 
		}
		
		function myCheckBox(value,metaDate,record){
			input2='<input type="checkbox" id="checkId" name="checkId" value="'+value+ '"/>';
			return String.format(input2);
	}
	  
	
	function selectCheck() {
		var obj = document.getElementsByName('checkId');
		var POST_DATE = document.getElementById('POST_DATE').value;
		var TICK_DATE = document.getElementById('TICK_DATE').value;
		var PROOF_DATE = document.getElementById('PROOF_DATE').value;
		if(POST_DATE.length == 0 && TICK_DATE.length == 0 && PROOF_DATE.length == 0)
		{
		MyAlert('请选择要加入的日期');
		return false;
		}
		res = confirm("你确认要记录这个日期日期吗？");
		var flag = false;
		if(res) {
			for(var i=0; i<obj.length; i++) {
				if(obj.item(i).checked) {
					flag = true;
				}
			}
			if(!flag){
				MyAlert("对不起,你没有选中需要记录的索赔单，请重新选择！");
			}else
			{
				MyConfirm("是否确认更新选中日期",adddate);
			}
		}
		
		
	}
	function adddate()
	{
		makeNomalFormCall('<%=contextPath%>/claim/application/ClaimBillStatusTrack/applicationQueryupdate.json',returnBack,'fm','');
	}
	function returnBack(json)
	{
		MyAlert(json.yes);
		__extQuery__(1);
	}
	
	 function clr() {
	document.getElementById('dealerCode').value = "";
	document.getElementById('dealer_id').value = "";
	
  }
   function clr1() {
	document.getElementById('modelCode').value = "";
	document.getElementById('group_id').value = "";
	
  }
  
</SCRIPT>

</HEAD>
<BODY onload=doInit();>

<DIV class=navigation><IMG src="../../../img/nav.gif"> 当前位置：售后服务管理&gt;索赔结算管理&gt;索赔相关时间记录</DIV>
<form id="fm" method="post" name="fm">
<TABLE class="table_query" border=0 align=center>
  <TBODY>
    <TR>
      <TD width="20%" align="right" noWrap>索赔单号：</TD>
      <TD width="30%" align=left noWrap><INPUT id=claimNo class=middle_txt name="claimNo"></TD>
      <td width="20%" nowrap="nowrap" align="right">车型组：</td>
      <td width="30%" nowrap="nowrap" class="table_query_4Col_input">
        <input id="modelCode" class="short_txt" name="modelCode" type="text" />
        <input type="hidden" name="group_id" id="group_id"/>
         <input class="mini_btn" type="button" value="&hellip;" onclick="showMaterialGroup1('modelCode','group_id','true','',true)" />
        <input class="normal_btn" onclick="clr1();" value="清空" type="button" /></td>
    </TR>
    <TR>
      <td align="right" nowrap="nowrap">VIN：</td>
      <td align="left" nowrap="nowrap"><input id="vin" class="middle_txt" name="vin" /></td>
     <td width="20%" nowrap="nowrap" align="right" >经销商代码：</td>
     <td width="30%" nowrap="nowrap" class="table_query_4Col_input">
       <input class="middle_txt" id="dealerCode" style="cursor: pointer;" name="dealerCode" type="text" />
       <input type="hidden" name="dealer_id" id="dealer_id"/>
       <input class="mini_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCode','dealer_id','true','',true,'','10771002')" />
       <input name="button2" type="button" class="normal_btn" onclick="clr();" value="清除"/>
     </td>
    </TR>
    <TR>
      <td align="right" nowrap="nowrap">索赔类型：</td>
      <td align="left" nowrap="nowrap"><script type="text/javascript">
				 genSelBoxExp("CLAIM_TYPE",1066,"",true,"short_sel","","false",'');
		    </script>
        </td>
      <td align="right" nowrap="nowrap">经销商名称：</td>
      <td nowrap="nowrap" class="table_query_4Col_input"><input id="dealerName" class="middle_txt" name="dealerName" /></td>
    </TR>
    <TR>
      <td align="right" nowrap="nowrap">产地：</td>
      <td align="left" nowrap="nowrap">
		    	<select style="width: 152px;" name="yieldly" id="yieldly">
  			  <option value="" >
    				-请选择-
    			  </option>
	              <c:forEach var="areaPO" items="${areaPO}" >
 				  <option value="${areaPO.areaId}" >
    				<c:out value="${areaPO.areaName}"/>
    			  </option>
    			 </c:forEach>
              </select>
  			 </td>
      <td align="right" nowrap="nowrap">审核时间：</td>
      <td align="left" nowrap="nowrap"><input name="startDate" type="text" class="short_time_txt" id="startDate"  value=""/>
        <input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'startDate', false);" value=" " />
        至
        <input name="endDate" type="text" class="short_time_txt" id="endDate"  value=""/>
        <input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'endDate', false);" value=" " /></td>
    </TR>
    <TR>
      <td align="right" nowrap="nowrap">状态：</td>
      <td align="left" nowrap="nowrap"><script type="text/javascript">
			genSelBoxExp("claimStatus",1079,"10791008",true,"short_sel","","false",'10791001,10791002,10791003,10791004,10791005,10791006');
		</script>
        </td>
      <td align="right" nowrap="nowrap">结算审核时间：</td>
      <td align="left" nowrap="nowrap"><input name="startDate2" type="text" class="short_time_txt" id="startDate2"  value=""/>
        <input name="button2" type="button" class="time_ico" onclick="showcalendar(event, 'startDate2', false);" value=" " />
        至
        <input name="endDate2" type="text" class="short_time_txt" id="endDate2"  value=""/>
        <input name="button2" type="button" class="time_ico" onclick="showcalendar(event, 'endDate2', false);" value=" " /></td>
    </TR>
    <TR>
      <TD colspan="4" align=center><input class="normal_btn" value="查询" onClick="__extQuery__(1);" type="button" name="queryBtn" /></TD>
    </TR>
  </TBODY>
</TABLE>
<!--分页  -->
<TABLE class=table_list>
  <TBODY>
    <TR class="table_list_row0">
      <TD noWrap>邮寄日期：
        <input name="POST_DATE" type="text" class="short_time_txt" id="POST_DATE"  value=""/>
        <input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'POST_DATE', false);" value=" " />
        &nbsp; &nbsp;收票日期：
        <input name="TICK_DATE" type="text" class="short_time_txt" id="TICK_DATE"  value=""/>
        <input name="button4" type="button" class="time_ico" onclick="showcalendar(event, 'TICK_DATE', false);" value=" " />
        &nbsp; &nbsp;凭证日期：
        <input name="PROOF_DATE" type="text" class="short_time_txt" id="PROOF_DATE"  value=""/>
        <input name="button5" type="button" class="time_ico" onclick="showcalendar(event, 'PROOF_DATE', false);" value=" " />
        <input class="normal_btn" value="确认" type="button" onclick="selectCheck()"/></td>
    </TR>
  </TBODY>
</TABLE>
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
</BODY>
</html>
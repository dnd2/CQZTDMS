<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.po.TmBusinessAreaPO,java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
	String contextPath = request.getContextPath();
List<TmBusinessAreaPO> list = (List<TmBusinessAreaPO>)request.getAttribute("yieldlys");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>索赔单上报</TITLE>

<SCRIPT LANGUAGE="JavaScript">
var myPage;
	//查询路径
	var url = "<%=contextPath%>/ClaimBalanceAction/claimManualAuditingForQuery.json";
					
	var title = null;

	var columns = [
					{id:'action',header: "操作",dataIndex: 'ID',renderer:claimAuditing},
					{header: "经销商代码",sortable: false,dataIndex: 'DEALER_CODE',align:'center'},
					{header: "经销商简称",sortable: false,dataIndex: 'DEALER_NAME',align:'center'},	
					{header: "索赔单审核状态",sortable: false,dataIndex: 'STATUS',align:'center',renderer:getItemValue},	
					{header: "大区",dataIndex: 'ROOT_ORG_NAME'},			
					{header: "索赔单号",dataIndex: 'CLAIM_NO'},			
					{header: "工单号",dataIndex: 'RO_NO'},
					{header: "索赔类型",sortable: false,dataIndex: 'CLAIM_TYPE',align:'center',renderer:getItemValue}, 
					{header: "VIN",sortable: false,dataIndex: 'VIN',align:'center'},
					{header: "修改次数",sortable: false,dataIndex: 'SUBMIT_TIMES',align:'right'},
					{header: "索赔金额(元)",sortable: false,dataIndex: 'REPAIR_TOTAL',align:'right'},
					{header: "申请日期",dataIndex: 'CREATE_DATE',renderer:formatDate}
				  ];
	function claimAuditing(value,meta,record){
		var t = record.data.CREATE_DATE;
		if (t==""||t==null) {
			t = "";
		}else {
			t= t.substr(0,4);
		}
    	return String.format("<a href=\"#\" onclick=auditingClaim("+value+",'"+t+"',"+record.data.CLAIM_TYPE+") >[撤销审核]</a>");
     }   
        
	function auditingClaim(value,create_date,type){
		var page = 1 ;
	    try{
	    	if(""==create_date||null==create_date||"null"==create_date){
				MyAlert("该索赔单的申请日期为空,可能影响审核的准确性,请联系管理员核实!");
				return;
			}
		    ///page = $$('.mini_txt')[1].value;
		    if(10661002==type){
               MyAlert("提示：保养索赔单不允许撤销！");
               return;
		    }else if(10661011==type ){
	           MyAlert("提示：PDI单据不允许撤销！");
	           return;
			}
		    var tarUrl = '<%=contextPath%>/ClaimBalanceAction/claimBalanceAuditingPage.do?id='+ value+'&type=1&claim_type='+type;
          	fm.action=tarUrl;
    		fm.method="post";
    		fm.submit();
	    }catch(e){
			MyAlert(e);
		   }
	}
 function checkBoxShow(value,meta,record){
	return String.format("<input type='checkbox' id='recesel' name='recesel' value='" + record.data.ID + "' />");
}
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
	}
	
	//清空经销商框
		function clearInput(){
			$('dealerCode').value="";
			$('dealerId').value="";
		}
	//返回
	function goBack(){
		window.location.href = "<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplyQuery.json";
	}
	function inIt(){
		 __extQuery__(1);
	}
	//得到选择的值,按照‘,’隔开
	function getCheckedToStr(name) {
		var str="";
		var chk = document.getElementsByName(name);
		if (chk==null){
			return "";
		}else {
		var l = chk.length;
		for(var i=0;i<l;i++){        
			if(chk[i].checked)
			{            
			str = chk[i].value+","+str; 
			}
		}
			return str.substring(0,str.length-1);
		}
	}
	function addApproval(type) {
	var allChecks = document.getElementsByName("recesel");
	var optiona = document.getElementById("ADUIT_REMARK").value;
	var allFlag = false;
	for(var i = 0;i<allChecks.length;i++){
		if(allChecks[i].checked){
			allFlag = true;
		}
	}
	if(allFlag){
	if((optiona==null||optiona=="")&&type==2){
		MyAlert("批量退回必须填写审核内容!");
		return false;
		}else{
			MyConfirm("确认批量审核?",changeSubmit,[type]);
			}
	}else{
		MyAlert("请选择数据后再点击操作批量通过按钮！");
	}
}
function changeSubmit(type) {
	$('queryBtn').disabled=true;
	$('save').disabled=true;
	$('save2').disabled=true;
	var id = getCheckedToStr('recesel');
	var url="<%=contextPath%>/claim/dealerClaimMng/DealerClaimReport/reportClaim.json?ID="+id+"&type="+type;
	makeNomalFormCall(url,showResult22,'fm');
}
function showResult22(json){
	var str = json.ACTION_RESULT;
	if(str==1){
		MyAlert('批量审核成功');
		$('ADUIT_REMARK').value="";
		__extQuery__(1);
	}else{
		MyAlert('操作失败,请联系管理员');
	}
	$('queryBtn').disabled=false;
	$('save').disabled=false;
	$('save2').disabled=false;
}
function formatDate(value,meta,record) {
	if (value==""||value==null) {
		return "";
	}else {
		return value.substr(0,16);
	}
}
//设置超链接 end
</SCRIPT>

</HEAD>
<BODY onload="inIt();">

<div class="navigation"><img src="../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔申请&gt;索赔申请撤销审核作业
</div>
    <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
         <tr>
		<td align="right" nowrap="true">经销商代码：</td>
		<td align="left" nowrap="true">
			<input class="middle_txt" id="dealerCode"  name="dealerCode" type="text" value="${dealerCode }" readonly="readonly"/>
            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','dealerId','true','',true);" value="..." />        
            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/>
			<input class="middle_txt" id="dealerId"  name="dealerId" value="${dealerId}" type="hidden" readonly="readonly"/>
		</td>
		<td align="right" nowrap="true">经销商名称：</td>
		<td align="left" nowrap="true">
			<input type="text" name="DEALER_NAME" id="DEALER_NAME" datatype="1,is_digit_letter_cn,30" class="middle_txt" value=""/>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">索赔单号：</td>
        <td>
        	<input name="CLAIM_NO" value="" type="text" class="middle_txt"/>
        </td>
		<td align="right" nowrap="true">索赔类型：</td>
		<td align="left" nowrap="true">
			<script type="text/javascript">
				 genSelBoxExp("CLAIM_TYPE",<%=Constant.CLA_TYPE%>,"",true,"short_sel",'',"false",'10661002,10661011');
		    </script>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">零件代码：</td>
        <td>
        	<input name="partCode" value="" type="text" class="middle_txt"/>
        </td>
		<td align="right" nowrap="true">作业代码：</td>
		<td>
        	<input name="wrLabourCode" value="" type="text" class="middle_txt"/>
        </td>
	</tr>
	<tr>
	    <td align="right" nowrap="true">工单号：</td>
		<td align="left" nowrap="true">
			<input name="CON_RO_NO" class="middle_txt" type="text" value="" /> 
			<input name="CON_LINE_NO" id="CON_LINE_NO" value="" type="hidden" datatype="1,is_digit,3" class="mini_txt"/>
		</td>
		<td align="right" nowrap="true" rowspan="2">VIN：</td>
		<td align="left" rowspan="2">
			<textarea name="CON_VIN" rows="3" cols="18"></textarea>
		</td>
	</tr>
	<tr>
		<td align="right">大区：</td>
		<td align="left">
			<select name="ORG_ID" class="short_sel">
				<option value="">-请选择-</option>
				<c:forEach var="org" items="${org }">
					<option value="${org.ORG_ID }">${org.ORG_NAME }</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">申请日期：</td>
		<td align="left" nowrap="true" colspan="3">
			<input type="text" name="CON_APPLY_DATE_START" id="CON_APPLY_DATE_START"
             value="${CON_APPLY_DATE_START }" type="text" class="short_txt" 
             datatype="1,is_date,10" group="CON_APPLY_DATE_START,CON_APPLY_DATE_END" 
             hasbtn="true" callFunction="showcalendar(event, 'CON_APPLY_DATE_START', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="CON_APPLY_DATE_END" id="CON_APPLY_DATE_END" 
 			value="${CON_APPLY_DATE_END }" type="text" class="short_txt" datatype="1,is_date,10" 
 			group="CON_APPLY_DATE_START,CON_APPLY_DATE_END" 
 			hasbtn="true" callFunction="showcalendar(event, 'CON_APPLY_DATE_END', false);"/>
		</td>
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1);"/>
		</td>
	</tr>
  </table>
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
<script type="text/javascript">
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
$('RO_STARTDATE').value=showMonthFirstDay();
$('RO_ENDDATE').value=showMonthLastDay();
</script>
</BODY>
</html>
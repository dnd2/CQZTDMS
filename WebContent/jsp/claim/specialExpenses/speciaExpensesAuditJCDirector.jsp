<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>特殊费用审核</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}

</script>
</head>
<body>

<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;特殊费用管理&gt;特殊费用审核</div>
  
  <form method="post" name="fm" id="fm">
  
  <!-- 车系列表 -->
   <!-- 查询条件 begin -->
    <table class="table_query" >
		<tr>
			<td align="right" nowrap="true">经销商代码：</td>
			<td align="left" nowrap="true">
				<input class="long_txt" id="dealerCode"  name="dealerCode" type="text"/>
	            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true,'','10771002');" value="..." />        
	            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput('dealerCode');" value="清除"/>
			</td>
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
		</tr>
          <tr>
            <td width="7%" align="right" nowrap>特殊费用编号：</td>
            <td colspan="6">
            	<input name="feeNo" id="feeNo" type="text" size="18"  class="middle_txt" value="" >
            </td>
			<td align="right">生产基地：</td>
			<td align="left">
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
            <td align="right" nowrap="nowrap" >上报时间：</td>
            <td colspan="6" nowrap="nowrap">
            	<div align="left">
            		 <input class="short_txt" id="beginTime" name="beginTime" datatype="1,is_date,10"
                           maxlength="10" group="beginTime,endTime"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'beginTime', false);" type="button"/>
                    至
                    <input class="short_txt" id="endTime" name="endTime" datatype="1,is_date,10"
                           maxlength="10" group="beginTime,endTime"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'endTime', false);" type="button"/>
            	</div>
            </td>
            <td align="right" nowrap="nowrap" >费用类型：</td>
            <td colspan="6" nowrap="nowrap">
              	<script type="text/javascript">
 					 genSelBoxExp("feeType",<%=Constant.FEE_TYPE%>,"",true,"short_sel","","false",'');
			  	</script>
            </td>
          </tr>
          <tr>
            <td align="center" colspan="14">
            	<input class="normal_btn" type="button" name="queryBtn" id="queryBtn" value="查询"  onClick="__extQuery__(1);">
            	<input class="normal_btn" type="button" name="queryBtn" id="queryBtn" value="确认通过"  onClick="batchAuditingConfirm();">
            	<input class="normal_btn" type="button" name="queryBtn" id="queryBtn" value="确认驳回"  onClick="batchAuditingConfirm01();">
            	<span style="color: red;">驳回时请输入驳回理由</span>
            	<input type="text" size="60" maxlength="200" name="director_AUDITING_OPINION" id="director_AUDITING_OPINION"  >
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
	var url = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/auditQuerySpeciaExpensesJCDirector.json";
				
	var title = null;

	var controlCols = "<input type=\"checkbox\" name=\"claimAll\" onclick=\"selectAll(this,'specialId')\"/>全选&nbsp;&nbsp;"
							+ "<input type=\"button\" value=\"批量审核\" class=\"normal_btn\" onclick=\"batchAuditingConfirm();\"/>";

	var columns = [
				{header: "序号", renderer:getIndex, align:'center'},
				{header: "选择<input type='checkbox' onclick='selectAll(this,\"specialId\")' />", width:'3%',align:'center',sortable: false,dataIndex: 'FID',renderer:createCheckbox},
				{header: "特殊费用编号", dataIndex: 'FEE_NO', align:'center',renderer:mySelect},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "制单日期", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "上报日期", dataIndex: 'MAKE_DATE', align:'center'},
				{header: "结算厂家", dataIndex: 'AREA_NAME', align:'center'},
				{header: "费用类型", dataIndex: 'FEE_TYPE', align:'center',renderer:getItemValue},
				{header: "申报金额(元)", dataIndex: 'DECLARE_SUM1', align:'center',renderer:amountFormat},
				{header: "单据状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue}
		      ];
		      
//设置超链接  begin      
	
	//修改的超链接
	function myLink(value,meta,record)
	{
  		return String.format("<a href='#' onclick='auditSpecialExpenses(\""+record.data.AID+"\",\""+ record.data.FEE_TYPE +"\",\""+ record.data.FID +"\",\""+ record.data.O_STATUS +"\",\""+ record.data.STATUS +"\")'>[审核]</a>");
	}
	
	//修改的超链接设置
	function auditSpecialExpenses(val1,val2,val3,val5,val4)
	{
		fm.action = '<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/auditSpecialExpensesInfoJC.do?aid=' + val1 + "&feeType=" + val2+"&fid=" + val3+"&status=" + val4+"&o_status="+val5;
	 	fm.submit();
	}
	
	//设置超链接
	function mySelect(value,meta,record)
	{
  		return String.format("<a href=\"#\" onclick='sel(\""+record.data.FID+"\",\""+ record.data.FEE_TYPE +"\")'>["+ value +"]</a>");
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
	
	
	//生成批量审核使用的选择框
	function createCheckbox(value,meta,record){
  	  		//var claimStatus = record.data.STATUS;
  	  		//var authcodeval = record.data.AUTH_CODE;
		var resultStr = String.format("<input type=\"checkbox\" name=\"specialId\" value=\""+record.data.FID+"\"/>");
		return resultStr;
	}
	
	//依次审批全部
	function cliamAllAudit()
    {
    
	    var frm = document.getElementById("fm");
	    frm.action = "<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/auditSpecialExpensesInfoByOneJC.do";
	    frm.submit();
    }
    //批量审批
	function batchAuditingConfirm(){
		var selectArray = document.getElementsByName('specialId');
		if(isSelectCheckBox(selectArray)){
			MyConfirm("是否批量审核!",batchAuditing,[]);
		}else{
			MyAlert("请选择要审批的特殊费用单！");
		}
	}
	
	function batchAuditingConfirm01(){
		var selectArray = document.getElementsByName('specialId');
		var director_AUDITING_OPINION = document.getElementById('director_AUDITING_OPINION').value;  
		if(director_AUDITING_OPINION.length == 0)
		{
			MyAlert("请输入驳回理由！");
		}
		else if(isSelectCheckBox(selectArray)){
			MyConfirm("是否批量审核!",batchAuditing01,[]);
		}else{
			MyAlert("请选择要审批的特殊费用单！");
		}
	}
	
	function batchAuditing01(val)
	{
		makeNomalFormCall("<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/auditSpeciaExpensesBatch02JC.json?",showAuditValue,'fm','queryBtn'); 
	}
	
	function batchAuditing(val)
	{
		makeNomalFormCall("<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/auditSpeciaExpensesBatch01JC.json?",showAuditValue,'fm','queryBtn'); 
	}
	
	//检测是否选择了特殊费用单
	function isSelectCheckBox(cbArray){
		if(cbArray!=null && cbArray.length>0){
			for(var i=0;i<cbArray.length;i++){
				if(cbArray[i].checked)
					return true;
			}
		}else{
			return false;
		}
		return false;
	}
	function showAuditValue(json){
		if(json.returnValue=='1'){
			MyAlert("批量成功！");
			__extQuery__(1);
		}else{
			MyAlert("操作操作失败，请联系管理员！");
		}
		
	}
//设置超链接 end
	
</script>
<!--页面列表 end -->


</body>
</html>
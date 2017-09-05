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
	var url = "<%=contextPath%>/claim/other/Bonus/queryOEMDealer.json";
				
	var title = null;
	var columns = [
				{header: "产地", width:'10%',dataIndex: 'AREA_NAME'},
				{header: "经销商代码", width:'10%',dataIndex: 'DEALER_CODE'},
				{header: "经销商名称", align:'center',dataIndex: 'DEALER_NAME'},
				{header: "补贴编号", align:'center',dataIndex: 'LABOUR_BH'},
				{header: "奖惩类型", align:'center',dataIndex: 'FINE_TYPE',orderCol:"FINE_TYPE",renderer:getItemValue},
				{header: "补贴类型", align:'center',dataIndex: 'REMARK'},
				{header: "奖惩劳务费", align:'center',dataIndex: 'LABOUR_SUM'},
				{header: "奖惩材料费", align:'center',dataIndex: 'DATUM_SUM'},
				{header: "奖惩时间", align:'center',dataIndex: 'FINE_DATE'},
				{header: "奖惩原因", align:'center',dataIndex: 'FINE_REASON'},
				{header: "导入人", align:'center',dataIndex: 'NAME'}
	      ];
		      
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
	}
	//清空经销商框
	function clearInput(){
		var target = document.getElementById('dealerCode');
		target.value = '';
	}

</SCRIPT>

</HEAD>
<BODY onload='doInit()'>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;其他功能&gt;正负激励</div>
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
            	<input class="middle_txt" id="dealerCode" name="DEALER_CODE" value="" type="text"/>
            	<input class="mark_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCode','','true','',true)"/> 
            	<input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/>       
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
           <input name="SubsidiesType" id="SubsidiesType" value="" type="text" class="middle_txt" datatype="1,is_digit_letter_cn" /></td>
	       <td></td>
	       <td></td>
          </tr>

          <tr>
            <td colspan="6" align="center" nowrap><input id="queryBtn" class="normal_btn" type="BUTTON" name="button1" value="查询"  onclick="__extQuery__(1);" />
            <input id="queryBtn" class="normal_btn" type="BUTTON" name="button1" value="导出excel"  onclick="goImport();" />
            <input id="queryBtn" class="normal_btn" type="BUTTON" name="button1" value="返回"  onclick="importSave();" />
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
function importSave() {
	     var url='<%=contextPath%>/claim/other/Bonus/bonusForward.do';
	     fm.action = url;
	     fm.submit();
		}
function goImport(){
    fm.action = '<%=contextPath%>/claim/other/Bonus/resourcesAuditDown.do';
    fm.submit();
}
</script>

</BODY>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.common.Constant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<body onload="__extQuery__(1);loadcalendar();">
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：服务站修改历史 </div>
</div>
  <form  name="fm" id="fm" method="post">
  	<input type=hidden name="COMMAND" value="1"/>
  	<input type=hidden name="CONTACT_ID" value="${CONTACT_ID }"/>
  	<input type="hidden" name="dealer_id" id="dealer_id"/>
  	<table class="table_query" border="0" >
        <tr>
      	<!-- <td width="10%" align="right">所选经销商：</td>
		<td width="30%">
			<input type="text" name="dealer_code" id="dealer_code" class="long_txt"/>
			<input type="button" class="mini_btn" value="..." onclick="showOrgDealer('dealer_code','dealer_id',true,'',true,'','10771002');"/>
           	<input type="button" class="normal_btn" value="清除" onclick="wrapOut2();"/>
		</td> -->
		<td align="right">修改时间：</td>
		<td align="left">
			<input class="short_txt" readonly="readonly"  type="text" id="change_date_start" name="change_date_start" group="change_date_start,change_date_end" datatype="1,is_date,10"/>
			<input class="time_ico" type="button" onClick="showcalendar(event, 'change_date_start', false);" value="&nbsp;" />
			<input class="short_txt"  readonly="readonly" type="text" id="change_date_end" name="change_date_end" group="change_date_start,change_date_end" datatype="1,is_date,10"/>
			<input class="time_ico" type="button" onClick="showcalendar(event, 'change_date_end', false);" value="&nbsp;" />
		</td>
<%--         <td class="table_query_2Col_label_8Letter"><div align="right"><span class="tabletitle">状态：</span></div></td>
        <td align="left">
            <script type="text/javascript">
       			genSelBoxExp("CON_STATUS",<%=Constant.STATUS%>,<%=Constant.STATUS_ENABLE%>,true,"short_sel","","false",'');
           </script>
        </td> --%>
    </table>
	<table class="table_query" border="0">
		<tr>
			<td align="center">
				<input name="queryBtn" id="queryBtn" type="button" onclick="__extQuery__(1);" class="normal_btn"  value="查询" />
				<input name="closeBtn" id="closeBtn" type="button" onclick="_hide();" class="normal_btn"  value="关闭" />
			</td>
		</tr>
	</table>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script language="JavaScript" >
	var myPage;
	$('dealer_id').value=<%=request.getParameter("dealerId")%>;
	var url = "<%=contextPath%>/sysusermng/sysuserinfo/SysPasswordManager/queryContactChangeHistory.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'index', align:'center',renderer:getIndex},
				{header: "服务站代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "服务站名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "修改前职位", dataIndex: 'OLD_POSE', align:'center',renderer:getItemValueforthispage},
				{header: "修改后职位", dataIndex: 'NEW_POSE', align:'center',renderer:getItemValueforthispage},
				{header: "修改前姓名", dataIndex: 'OLD_NAME', align:'center'},
				{header: "修改后姓名", dataIndex: 'NEW_NAME', align:'center'},
				{header: "修改前电话", dataIndex: 'OLD_PHONE', align:'center'},
				{header: "修改后电话", dataIndex: 'NEW_PHONE', align:'center'},
				{header: "修改前热线电话", dataIndex: 'OLD_HOT_LINE', align:'center'},
				{header: "修改后热线电话", dataIndex: 'NEW_HOT_LINE', align:'center'},
				{header: "修改前状态", dataIndex: 'OLD_STATUS', align:'center',renderer:getItemValueforthispage},
				{header: "修改后状态", dataIndex: 'NEW_STATUS', align:'center',renderer:getItemValueforthispage},
				{header: "修改人", dataIndex: 'CHANGE_USER', align:'center'},
				{header: "修改时间", dataIndex: 'CHANGE_DATE', align:'center'}
		      ];
	
	function getItemValueforthispage(codeId){ // 根据codeId拿到某个属性的codeDesc，用在ext
		var itemValue = null;
		for(var i=0;i<codeData.length;i++){
			if(codeData[i].codeId == codeId){
				itemValue = codeData[i].codeDesc;
			}
		}
		return itemValue==null?codeId:itemValue;
	}
		      
</script>
</body>
</html>

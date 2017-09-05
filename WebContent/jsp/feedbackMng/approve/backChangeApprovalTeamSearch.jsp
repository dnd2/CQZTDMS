<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script>
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
<TITLE>退换车申请书大区审核</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<BODY>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理&gt;信息反馈审批&gt;退换车申请书大区审核</div>
  <form method="post" name = "fm" id="fm">
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
  	<input type="hidden" name="curPage" id="curPage" value="1" />
<table align="center" width="95%" class="table_query" >
      <tr>
        <td width="7%" align="right" nowrap="nowrap">工单号：</td>
        <td colspan="6"><input name="ORDER_ID" id="ORDER_ID" datatype="1,is_digit_letter,18" value="" type="text" class="middle_txt" />
        </td>
        <td width="13%" align="right" nowrap="nowrap">提报时间： </td>
        <td colspan="2" align="left" >
           	<div align="left">
           		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
           		&nbsp;至&nbsp;
           		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
           	</div>        
        </td>
      </tr>
      <tr>
        <td align="right" nowrap="nowrap" >经销商代码：</td>
        <td colspan="6" nowrap="nowrap">
 				<input class="middle_txt" id="dealerCode" style="cursor: pointer;" name="dealerCode" type="text"/>
            <input name="button1" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
		</td>
        <td align="right" nowrap="nowrap">经销商名称：</td>
        <td><input type="text" name="DEALER_NAME" id="DEALER_NAME" datatype="1,is_digit_letter_cn,30" class="middle_txt" value=""/></td>
        <td align="right" >&nbsp;</td>
      </tr>
      <tr>
        <td align="right" nowrap="nowrap" >车辆识别码(VIN)：</td>
        <td colspan="6" nowrap="nowrap"><input type="text" name="VIN" id="VIN"  class="middle_txt" value=""/></td>
        <td align="right" nowrap="nowrap"><!-- 退换类型： --></td>
        <td><script type="text/javascript">
	              //genSelBoxExp("EX_TYPE",<%=Constant.EX_TYPE%>,"",true,"short_sel","","true",'');
	     </script></td>
        <td align="right" >&nbsp;</td>
      </tr>
      <tr>
        <td align="right" nowrap="nowrap" >车型：</td>
        <td colspan="6" nowrap="nowrap">
        <script type="text/javascript">
              var seriesList=document.getElementById("seriesList").value;
    	      var str="";
    	      str += "<select id='vehicleSeriesList' name='vehicleSeriesList'>";
    	      str+=seriesList;
    		  str += "</select>";
    		  document.write(str);
	        </script>
        </td>
        <td align="right" nowrap="nowrap"></td>
        <td>&nbsp;</td>
        <td align="right" >&nbsp;</td>
      </tr>      
      <tr>
        <td width="7%" align="right" nowrap="nowrap" >&nbsp;</td>
        <td colspan="6" nowrap="nowrap">&nbsp;</td>
        <td width="13%" align="right" nowrap="nowrap">&nbsp;</td>
        <td width="19%">&nbsp;</td>
        <td width="22%" align="right" >&nbsp;</td>
      </tr>
      <tr>
        <td align="right" nowrap="nowrap" >&nbsp;</td>
        <td colspan="6" nowrap="nowrap">&nbsp;</td>
        <td align="left" nowrap="nowrap"><input class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1);" /></td>
        <td>&nbsp;</td>
        <td align="right" >&nbsp;</td>
      </tr>
    </table>
	<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>     
  </form>
    <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
  <br>
</BODY>
</html>
<script language="JavaScript">
var myPage;
	var url = "<%=request.getContextPath()%>/feedbackmng/approve/BackChangeApproveTeam/backChangeApproveTeamQuery.json?COMMAND=1";

	var title = null;
	
	var columns = [
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_NAME' ,align:'center'},
				{header: "工单号",sortable: false,dataIndex: 'ORDER_ID',renderer:mySelect ,align:'center'},
				{header: "车辆识别码(VIN)",sortable: false,dataIndex: 'VIN',align:'center'},
				{header: "车系",sortable: false,dataIndex: 'GROUP_NAME',align:'center'},
				//{header: "退换类型",sortable: false,dataIndex: 'EX_TYPE',align:'center' ,renderer:getItemValue},
				{header: "提报时间",sortable: false,dataIndex: 'EX_DATE',align:'center',renderer:getDate},
				{header: "工单状态",sortable: false,dataIndex: 'EX_STATUS',align:'center',renderer:getItemValue},
				{header: "操作",sortable: false,dataIndex: 'ORDER_ID',renderer:myLink ,align:'center'}
		      ];

//设置超链接  begin      
	
	//修改的超链接设置
	function myLink(value,meta,record){
	    return String.format(
         "<a href=\"<%=contextPath%>/feedbackmng/approve/BackChangeApproveTeam/backChangeApproveTeamAuditInit.do?ORDER_ID="
			+ value + "\">[审核]</a>");
	}
	
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='orderIds' value='" + value + "' />");
	}
	//日期格式化：
	function getDate(value,metaDate,record){
		return String.format(value.substring(0,10));
	}	
	//设置超链接
	function mySelect(value,meta,record){
  		return String.format(
         "<a href=\"#\" onclick='sel(\""+record.data.ORDER_ID+"\")'>["+ value +"]</a>");
	}
	
	//详细页面
	function sel(value){
		OpenHtmlWindow('<%=contextPath%>/feedbackmng/approve/BackChangeApproveTeam/detailBackChangeApply.do?ORDER_ID='+value,900,500);
	}
	
//设置超链接 end
</script>

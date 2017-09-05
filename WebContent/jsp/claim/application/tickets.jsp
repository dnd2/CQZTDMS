
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" >

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>结算室审核</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar(); 
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;结算室收票
</div>
<form method="post" name="fm" id="fm">
<input type="hidden" value="${balance_yieldly}" name="balance_yieldly"/>
<table align="center" class="table_query" border='0'>
	<tr>
		<td align="right" nowrap="true">服务站：</td>
        <td class="table_query_4Col_input" nowrap="nowrap">
			<input class="middle_txt" id="dealerName"  name="dealerName" type="text"/>
		</td>
		<td align="right" nowrap="true">发出日期：</td>
        <td class="table_query_4Col_input" nowrap="nowrap">
   	    <input class="short_txt" id="startDate" name="startDate" datatype="1,is_date,10"
                      maxlength="10" group="startDate,endDate"/>
       <input class="time_ico" value=" " onclick="showcalendar(event, 'startDate', false);" type="button"/>
		</td>
		<td align="right" nowrap="true">收到日期</td>
		<td align="left" nowrap="true">
		 <input class="short_txt" id="endDate" name="endDate" datatype="1,is_date,10"
	                    maxlength="10" group="startDate,endDate"/>
      	 <input class="time_ico" value=" " onclick="showcalendar(event, 'endDate', false);" type="button"/>
		</td>
	</tr>
	<tr>
		
	</tr>
	
	<tr>
		<td colspan="5" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
			&nbsp;&nbsp;&nbsp;
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="新增收单" onclick="addtickets()"/>
			&nbsp;&nbsp;&nbsp;
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="套打" onclick="Printtickets()"/>
		</td>
	</tr>
	<tr>
	<td>
	</td>
	</tr>
</table>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	
<script type="text/javascript">
		var myPage;
		var url = "<%=contextPath%>/claim/application/ClaimManualAuditing/quyertickets.json";
		var title = null;
		var columns = [
					{header: "序号",align:'center',renderer:getIndex},	
					{header: "服务站名称",dataIndex: 'DEALERNAME',align:'center'},
					{header: "信函总类",dataIndex: 'LETTER',align:'center',renderer:getItemValue},
					{header: "单据批号",dataIndex: 'GOODSNUM',align:'center'},
					{header: "收单数",dataIndex: 'APLCOUNT',align:'center'},
					{header: "昌河运费",dataIndex: 'CARRIAGE',align:'center'},
					{header: "东安运费",dataIndex: 'DA_CARRIAGE',align:'center'},
					{header: "总运费",dataIndex: 'SUM_CARRIAGE',align:'center'},
					{header: "发出日期",dataIndex: 'STARTDATE',align:'center'},
					{header: "收到日期",dataIndex: 'ENDDATE',align:'center'},
					{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:tickteview ,align:'center'}
			      ];
			      
	function addtickets()
	{
		fm.action = "<%=contextPath%>/claim/application/ClaimManualAuditing/addtickets.do";
		fm.submit();
	}
	function tickteview(value,meta,record)
	{
		 return String.format(" <input  type='checkbox' name='ids' value='"+value+"' />  <a href=\"<%=contextPath%>/claim/application/ClaimManualAuditing/tickteview.do?id="+ value + "\">[查看]</a><a href='#' onclick=deltick("+value+") >[删除]</a><a href='#' onclick=queryInfo("+value+") >[打印]</a>");
	}
	
	function deltick(value)
	{
		MyConfirm("是否确认删除单据？",delettick,[value]);
	}
	function delettick(value)
	{
	   var url = "<%=contextPath%>/claim/application/ClaimManualAuditing/delettick.json?id="+value ;
		sendAjax(url,returndelt,"fm");
	}
	function returndelt(json)
	{
		if(json.meg == 'no')
		{
			MyAlert('室主任已审核不能够删除');
		}else
		{
			MyAlert('删除成功 ！');
			__extQuery__(1);
		}
		
	}
	function queryInfo(value)
	{
		window.open('<%=contextPath%>/claim/application/ClaimManualAuditing/tickteprint.do?id='+value,"收单打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
	}
	function Printtickets()
	{
	   var ids = document.getElementsByName('ids');
	   var print = "";
	   var fag = false;
	   for(var i = 0 ; i < ids.length ; i++)
	   {
	     if(ids[i].checked)
	     {
	       fag = true;
	       print = print + ids[i].value + ",";
	     }
	   }
	   if(fag)
	   {
	       var balance_yieldly = document.getElementById('balance_yieldly').value;
	   	  window.open('<%=contextPath%>/claim/application/ClaimManualAuditing/tickteprint.do?balance_yieldly='+balance_yieldly+"&ids="+ print ,"收单打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
	   } else
	   {
	      MyAlert('请选择需要套打的收单必须是相同经销商！');
	   }
	    
	 
	}
</script>
</form>
</body>
</html>
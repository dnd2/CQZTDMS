<%-- 
创建时间 : 2010.08.27
             创建人:lishuai
             功能描述：经销商查看开票的结果
--%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>结算单管理</title>
	<script type="text/javascript">
	    function doInit(){
		   loadcalendar();
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;付款凭证管理
</div>
<form method="post" name="fm" id="fm">
<input type="hidden" value="${balance_yieldly}" name="balance_yieldly"/>
<table align="center" class="table_query">
	<tr>
		<td align="right" nowrap="true">服务站代码：</td>
        <td class="table_query_4Col_input" nowrap="nowrap">
			<input class="middle_txt" id="dealerCode"  name="dealerCode" type="text"/>
		</td>
		<td align="right" nowrap="true">服务站名称：</td>
        <td class="table_query_4Col_input" nowrap="nowrap">
			<input class="middle_txt" id="dealerName"  name="dealerName" type="text"/>
		</td>
		<td align="right" nowrap="true">打印日期：</td>
        <td class="table_query_4Col_input" nowrap="nowrap">
			 <input class="short_txt" id="startDate" name="startDate" datatype="1,is_date,10"
                 maxlength="10" group="startDate,endDate"/>
          <input class="time_ico" value=" " onclick="showcalendar(event, 'startDate', false);" type="button"/>
              至
          <input class="short_txt" id="endDate" name="endDate" datatype="1,is_date,10"
                 maxlength="10" group="startDate,endDate"/>
          <input class="time_ico" value=" " onclick="showcalendar(event, 'endDate', false);" type="button"/>

		</td>
		<td align="right" nowrap="true">结算编号</td>
		<td align="left" nowrap="true">
			<input class="middle_txt" id="balance_oder"  name="balance_oder" type="text"/>
		</td>
	</tr>
	
	
	<tr>
		<td colspan="5" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
			&nbsp;&nbsp;&nbsp;
			<input class="normal_btn" type="button" value="导出" onclick="expront();"/>
			&nbsp;&nbsp;&nbsp;
			<input class="normal_btn" style="width: 80px;"  type="button" value="新增付款凭证" onclick="addPayment()"/>
			
		</td>
	</tr>
	
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">
		var myPage;
		var url = "<%=contextPath%>/claim/application/ClaimManualAuditing/paymentManage.json?comm=1";
		var title = null;
		var columns = [
					{header: "序号",renderer:getIndex,align:'center'},	
					{header: "结算编号",dataIndex: 'REMARK',align:'center'},
					{header: "服务站代码",dataIndex: 'DEALER_CODE',align:'center'},
					{header: "服务站名称",dataIndex: 'DEALER_NAME',align:'center'},
					{header: "上报月份",dataIndex: 'START_DATE',align:'center'},
					{header: "申请总金额",dataIndex: 'AMOUNT_SUM',align:'center'},
					{header: "主任确认意见",dataIndex: 'MARK',align:'center'},
					{id:'action',header: "操作",sortable: false,dataIndex: 'STATUS',renderer:tickteview ,align:'center'}
			      ];
       function tickteview(value,metaDate,record)
       {
       		
       		var resObj = '';
			if(value == '0')
			{
				 resObj = String.format("<a href='#' onclick='update(\""+ value +"\",\""+record.data.REMARK+"\")'>[修改]</a>");
			}else
			{
				 resObj = String.format("<a href='#' onclick='inFor(\""+ value +"\",\""+record.data.REMARK+"\")'>[查看]</a><a href='#' onclick='print(\""+ value +"\",\""+record.data.REMARK+"\")'>[打印]</a>");
			}
			return resObj;
	   }
	   
	  function expront()
      {
      	fm.action = "<%=contextPath%>/report/service/ActivityBalanceDetail/expront.do";
		fm.submit();
      }  
	   
	   function update(value,REMARK)
	   {
	   		
	   		fm.action = "<%=contextPath%>/claim/application/ClaimManualAuditing/getMessgeMf.do?balance_oder="+REMARK;
			fm.submit();
	   		
	   }
	   function inFor(value,REMARK)
	   {
	   		fm.action = "<%=contextPath%>/claim/application/ClaimManualAuditing/getMessgeInFor.do?balance_oder="+REMARK;
			fm.submit();
	   }
	   
	   function print(value,REMARK)
	   {
	   		window.open("<%=contextPath%>/claim/application/ClaimManualAuditing/getMessgeInPrint.do?balance_oder="+REMARK,"","toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=500");
	   }
	   
	   function addPayment()
	   {
	   		fm.action = "<%=contextPath%>/claim/application/ClaimManualAuditing/paymentManageInit.do";
			fm.submit();
	   }
	
</script>
</body>
</html>
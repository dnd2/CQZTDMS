<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%String contextPath = request.getContextPath();%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>三包结算统计表</title>
	<script type="text/javascript">
	    function doInit(){
		   loadcalendar();
		}
	    function exportExcel(){
			fm.action = "<%=contextPath%>/claim/application/DealerBalance/selectClaimBalanceReportExcel.json";
			fm.submit();
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />当前位置：售后服务管理&gt;索赔结算管理&gt;三包结算统计表</div>
<form method="post" name="fm" id="fm">
<table align="center" class="table_query">
	<tr>
		<td align="right" nowrap="true">经销商代码：</td>
		<td align="left" nowrap="true">
			<input class="middle_txt" id="dealerCode"  name="dealerCode" type="text"/>
            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/>
		</td>
		<td align="right" nowrap="true">经销商名称：</td>
		<td align="left" nowrap="true">
			<input type="text" name="DEALER_NAME" id="DEALER_NAME" datatype="1,is_digit_letter_cn,30" class="middle_txt" value=""/>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">生产厂家：</td>
        <td>
        	<script type="text/javascript">
				 genSelBoxExp("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'');
		    </script>
        </td>
		<td align="right" nowrap="true">统计时间起止：</td>
		<td align="left" nowrap="true">
			<input type="text" name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
		</td>
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
			<input class="normal_btn" type="button" id="queryBtn" name="button1" value="下载"  onclick="exportExcel()" />
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">
		var myPage;
		var url = "<%=contextPath%>/claim/application/DealerBalance/selectClaimBalance.json";
		var title = null;
		
		var columns = [
						{header: "序号",align:'center',renderer:getIndex},
						{header: "维修站编码",dataIndex: 'DEALER_CODE',align:'center'},
						{header: "维修站名称",dataIndex: 'DEALER_NAME',align:'center'},
						{header: "生产厂家",dataIndex: 'YIELDLY',align:'center',renderer:getItemValue},
						{header: "走保次数",dataIndex: 'CON',align:'center'},
						{header: "总走保工时费",dataIndex: 'BY_LABOUR_AMOUNT',align:'center'},
						{header: "总走保材料费",dataIndex: 'BY_PART_AMOUNT',align:'center'},
						{header: "走保费",dataIndex: 'FREE_AMOUNT',align:'center'},
						{header: "工时费",dataIndex: 'LABOUR_AMOUNT',align:'center'},
						{header: "材料费",dataIndex: 'PART_AMOUNT',align:'center'},
						{header: "工时费合计",dataIndex: 'AMOUNT_LABOUR',align:'center'},
						{header: "材料费合计",dataIndex: 'AMOUNT_PART',align:'center'},
						{header: "合计",dataIndex: 'SUM_BALANCE_AMOUNT',align:'center'}
			      ];
</script>
</body>
</html>
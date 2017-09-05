<%-- 
创建时间 : 2010.08.27
             创建人:lishuai
             功能描述：结算室审核，可对索赔单进行批量审核，和逐条审核
--%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>北汽幻速开票</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;北汽幻速开票
</div>
<form method="post" name="fm" id="fm">
<table align="center" class="table_query">
	<tr>
	<td align="center">
	转账开始日期 :
		 <input class="short_txt" id="startDate" name="startDate" datatype="1,is_date,10"
                 maxlength="10" group="startDate,endDate"/>
          <input class="time_ico" value=" " onclick="showcalendar(event, 'startDate', false);" type="button"/>
              至
          <input class="short_txt" id="endDate" name="endDate" datatype="1,is_date,10"
                 maxlength="10" group="startDate,endDate"/>
          <input class="time_ico" value=" " onclick="showcalendar(event, 'endDate', false);" type="button"/>
          </td>
          <td align="center">
	转账结束日期 :
		 <input class="short_txt" id="startDate_1" name="startDate_1" datatype="1,is_date,10"
                 maxlength="10" group="startDate_1,endDate_1"/>
          <input class="time_ico" value=" " onclick="showcalendar(event, 'startDate_1', false);" type="button"/>
              至
          <input class="short_txt" id="endDate_1" name="endDate_1" datatype="1,is_date,10"
                 maxlength="10" group="startDate,endDate_1"/>
          <input class="time_ico" value=" " onclick="showcalendar(event, 'endDate_1', false);" type="button"/>
          </td>
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	
	<script type="text/javascript">
	
	      var myPage;
		//查询路径
		var url = "<%=contextPath%>/claim/application/DealerNewKp/DealerUnitKpShQuery.json";
		//标题			
		var title = null;
	    //显示列表控制
		var columns = [
						{header: "序号",align:'center',renderer:getIndex},
						{header: "操作",dataIndex: 'BRO_NO',align:'center',renderer:accAudut},
						{header: "验票开始时间",dataIndex: 'CHECK_TICKETS_DATE_S',align:'center'},		
						{header: "验票结束时间",dataIndex: 'CHECK_TICKETS_DATE',align:'center'},
						{header: "购货方",dataIndex: 'GUNIT',align:'center'},
						{header: "销货方",dataIndex: 'XUNIT',align:'center'},
						{header: "开票单号",dataIndex: 'BRO_NO',align:'center'},
						{header: "状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue},
						{header: "创建人",dataIndex: 'NAME',align:'center'},
						{header: "创建时间",dataIndex: 'CREATE_DATE',align:'center'},
						{header: "审核人",dataIndex: 'APPROVAL_NAME',align:'center'},
						{header: "审核时间",dataIndex: 'APPROVAL_DATE',align:'center'},
						{header: "审核备注",dataIndex: 'APPROVAL_REMARKS',align:'center'},
						{header: "发票号",dataIndex: 'INVOICE_NO',align:'center'},
						{header: "发票批号",dataIndex: 'INVOICE_BATCH_NO',align:'center'}
						
						
			      ];
			      
     //修改的超链接
		function accAudut(value,meta,record)
		{ 
            var res = "";
            res += "<a href='#' onclick='DealerUnitKpJs(\""+record.data.CHECK_TICKETS_DATE_S+"\",\""+record.data.CHECK_TICKETS_DATE+"\",\""+record.data.BRO_NO+"\",\""+record.data.ID+"\")'>[审核]</a>";
            res += "<a href='#' onclick='DealerUnitKpXh(\""+record.data.CHECK_TICKETS_DATE_S+"\",\""+record.data.CHECK_TICKETS_DATE+"\",\""+record.data.BRO_NO+"\",\""+record.data.INVOICE_NO+"\",\""+record.data.INVOICE_BATCH_NO+"\")'>[北汽幻速销货清单打印]</a>";
			return res;
		}	
		function DealerUnitKpJs(CHECK_TICKETS_DATE_S,CHECK_TICKETS_DATE,BRO_NO,ID)
		{
		   location.href = '<%=request.getContextPath()%>/claim/application/DealerNewKp/DealerUnitKpJsSh.do?CHECK_TICKETS_DATE_S='+CHECK_TICKETS_DATE_S+'&CHECK_TICKETS_DATE='+CHECK_TICKETS_DATE+'&BRO_NO='+BRO_NO+'&ID='+ID;
		}
	</script>
</body>
</html>
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

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>结算单管理</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
          当前位置：售后服务管理&gt;索赔结算管理&gt;开票单位变更申请
</div>
<form method="post" name="fm" id="fm">
<table align="center" class="table_query">
	<tr>
		<td align="right" nowrap="true">结算单号：</td>
        <td>
        	<input name="balanceNo" value="" type="text" class="middle_txt"/>
        </td>
		<td align="right" nowrap="true">申请状态：</td>
		<td align="left" nowrap="true">
			<script type="text/javascript">
				 genSelBoxExp("CHANGE_STATUS",<%=Constant.CHANGE_STATUS%>,"",true,"short_sel","","true",'');
		    </script>
		</td>
	</tr>
	
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
			&nbsp;&nbsp;
			<input class="normal_btn" type="button" name="addBtn" id="addBtn" value="新增" 
				onclick="forwordToAddPage('fm','<%=contextPath%>/claim/application/DealerNewKpUpdate/addDealerKp.do','_self');"/>
				
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">
		var myPage;
		var url = "<%=contextPath%>/claim/application/DealerNewKpUpdate/viewDealerNewKpOem.json";
		var title = null;
		
		var columns = [
		           	{header: "结算单号",dataIndex: 'BALANCE_NO',align:'center'},
		        	{header: "提报日期",dataIndex: 'SUBMIT_DATE',align:'center',renderer:formatDate},
		        	{header: "原开票单位",dataIndex: 'INVOICE_MARK',align:'center'},
		        	{header: "改后开票单位",dataIndex: 'NEW_INVOICE_MARK',align:'center'},
		        	{header: "批复人",dataIndex: 'NAME',align:'center'},
		        	{header: "批复日期",dataIndex: 'AUDITING_DATE',align:'center', renderer:formatDate},
		        	{header: "申请状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue},
		        	{header: "操作",dataIndex: 'ID',align:'center', renderer:accAudut}
		        	
			      ];


		//跳转到新增结算页面
		function forwordToAddPage(frmId,url,targetVar){
			var frmVar = document.getElementById(frmId);
			frmVar.action = url;
			frmVar.target = targetVar;
			frmVar.submit();
		}
		//修改的超链接
		function accAudut(value,meta,record)
		{ 
			var status=record.data.STATUS;
			if(status==<%=Constant.CHANGE_STATUS_1 %>||status==<%=Constant.CHANGE_STATUS_3 %>||status==<%=Constant.CHANGE_STATUS_4 %>){
				return String.format("<a href='#' onclick='viewDealerKp(\""+ value +"\")'>[查看]</a>");
			}if(status==<%=Constant.CHANGE_STATUS_2 %>){
				return String.format("<a href='#' onclick='auditingFoward(\""+ value +"\")'>[审批]</a>");
			}
		}

		function formatDate(value,meta,record){
			return value!=""&&value!=null?value.substring(0,11):"";
		}

		function queryInfo(id){
			fm.action = "<%=contextPath%>/claim/application/DealerNewKpUpdate/modifyDealerKp.do?ID="+id;
			fm.submit();
		}
		function queryPrint(id){
			MyConfirm("请确认是否提报？",submitGo,[id]);
		}
		 function submitGo (id){
				MyAlert(id);
				makeNomalFormCall("<%=contextPath%>/claim/application/DealerNewKpUpdate/submitKpDealer.json?ID="+id,submitKP,'fm','queryBtn');
			
		}
		function submitKP(json){
			if(json.success=="true"){
				MyAlert("提报成功");__extQuery__(1);
			}else{
				MyAlert("提报失败");
			}
		}
		
		function viewDealerKp(value){
			OpenHtmlWindow("<%=contextPath%>/claim/application/DealerNewKpUpdate/viewDealerKp.do?id="+value,800,500);
					
		}

		function auditingFoward(value){
			fm.action = "<%=contextPath%>/claim/application/DealerNewKpUpdate/oemAuditingDelaerKP.do?ID="+value;
			fm.submit();
		}
		__extQuery__(1);
</script>
</body>
</html>
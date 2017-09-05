<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>预授权技术部查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/YsqAction/ysqTechDirectorList.json?query=true";
			
var title = null;

var columns = [
				{header: "序号", sortable : false, renderer:getIndex},
				{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink1,align:'center'},
				{header: "预授权单号", width:'15%', dataIndex: 'YSQ_NO'},
				{header: "索赔类型", width:'15%', dataIndex: 'CLAIM_TYPE',renderer:getItemValue},
				{header: "VIN", width:'15%', dataIndex: 'VIN'},
				{header: "进厂里程数", width:'15%', dataIndex: 'MILEAGE'},
				{header: "主损件代码", width:'15%', dataIndex: 'PART_CODE'},
				{header: "主损件名称", width:'15%', dataIndex: 'PART_NAME'},
				{header: "授权状态", width:'15%', dataIndex: 'STATUS',renderer:getItemValue},
				{header: "最后审核时间", width:'15%', dataIndex: 'RECORD_DATE'},
				{header: "流程是否结束", width:'15%', dataIndex: 'IS_END',renderer:link_end}
	      ];
		function link_end(value,meta,record){
			 var str="";
			 var id = record.data.ID;
			 if(-1==value){
				 str="<a herf='#' onclick='showWorkFlow(\""+id+"\");' style='color: red;'>已结束(查)</a>";
			 }else if(value>=1){
				 str="<a herf='#' onclick='showWorkFlow(\""+id+"\");' style='color: green;'>未结束(查)</a>";
			 }else{
				 str="未开始";
			 }
			 return String.format(str);
		 }
		 function showWorkFlow(id){
			 OpenHtmlWindow('<%=contextPath%>/YsqAction/showWorkFlow.do?id='+id,800,500);
		 }
			function myLink1(value,meta,record){
				var vin = record.data.VIN;
				var status=record.data.STATUS;
				var isEnd=record.data.IS_END;
				var res=record.data.JUGE_RES;
				var createBy=record.data.CREATE_BY;
				var url="";
				var urlView="<%=contextPath%>/YsqAction/ysqView.do?id="+value+"&vin="+vin+"&createBy="+createBy;
				var urlAudit="<%=contextPath%>/YsqAction/ysqTechDirectorAudit.do?id="+value+"&vin="+vin+"&createBy="+createBy;
				if(res==9){//有维护件 无800
					if(status==93461005 && isEnd==2){
						url+="<a href='"+urlAudit+"'>[审核]</a>";
					}
				} 
				if(res==5){//有维护件 有800
					if(status==93461005 && isEnd==2){
						url+="<a href='"+urlAudit+"'>[审核]</a>";
					}
					if(status==93461005 && isEnd==4){
						url+="<a href='"+urlAudit+"'>[审核]</a>";
					}
				}
				if(res==5){//有维护件 有3000
					if(status==93461003 && isEnd==4){
						url+="<a href='"+urlAudit+"'>[审核]</a>";
					}
					if(status==93461003 && isEnd==2){
						url+="<a href='"+urlAudit+"'>[审核]</a>";
					}
				}
				if(res==10){//无维护件 无800
				}
				if(res==6){//无维护件 有800 
					if(status==93461003  && isEnd==2){
						url+="<a href='"+urlAudit+"'>[审核]</a>";
					}
				}
				url+="<a href='"+urlView+"'>[明细]</a>";
				if(status==93461005){
					url+="<a herf='#' onclick='auditEidt("+value+");'>[撤销审核]</a>";
			    }
			    return String.format(url);
			}
			//撤销审核
			function auditEidt(val){
                var url = '<%=contextPath%>/YsqAction/auditEidt.json?id='+val;
                sendAjax(url,function showFunc(json){
                    if(json.succ=1){
                         MyAlert("提示:操作成功!");
                         __extQuery__(1);
                     }else{
                    	 MyAlert("提示:操作失败,该预授权已被使用无法撤销!");
                     }
                },"fm");
		   }
</script>
<!--页面列表 end -->
</head>
<body onload="loadcalendar();">
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔预授权管理&gt;预授权技术部查询</div>
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%"  nowrap="true">预授权申请号：</td>
		<td width="15%">
			<input name="ysq_no" id="ysq_no" value="" maxlength="20" type="text"  class="middle_txt" />
		</td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">开始时间：</td>
      	<td width="15%"><input name="begin_time" type="text" id="beginTime" readonly="readonly" value="" onfocus="calendar();" class="middle_txt"/></td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">结束时间：</td>
      	<td width="15%"><input name="end_time" type="text" id="endTime"  readonly="readonly" value="${endTime }" onfocus="calendar();" class="middle_txt"/></td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td width="10%"class="table_query_2Col_label_5Letter" nowrap="true">VIN：</td>
		<td width="15%">
				<input name="vin" id="vin" type="text"  value="" class="middle_txt" maxlength="30"/>
		</td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">维修类型：</td>
      	<td width="15%">
      		<script type="text/javascript">
         	genSelBoxExp("claim_type",<%=Constant.CLA_TYPE%>,"",true,"short_sel","","false",'10661002,10661003,10661004,10661005,10661008,10661009,10661011,10661012');
         	 </script>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">预警：</td>
      	<td width="15%">
      		<script type="text/javascript">
         	genSelBoxExp("is_warning",<%=Constant.IF_TYPE%>,"",true,"short_sel","","false",'');
         	 </script>
      	</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td width="10%"class="table_query_2Col_label_5Letter" nowrap="true">申请单状态：</td>
		<td width="15%">
			<script type="text/javascript">
         	genSelBoxExp("status",<%=Constant.STATUS_YSQ%>,"<%=Constant.STATUS_YSQ_02%>",true,"short_sel","","false",'93461001');
         	 </script>
		</td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">大区：</td>
      	<td width="15%">
      	<input id="org_name" name="org_name" type="text" class="middle_txt" />
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">审核时间：</td>
      	<td width="30%" colspan="2">
      	  <input name="audit_date_start" type="text" id="audit_date_start" readonly="readonly" value="" onfocus="calendar();" class="short_txt"/>
      	   至
      	  <input name="audit_date_end" type="text" id="audit_date_end" readonly="readonly" value="" onfocus="calendar();" class="short_txt"/>
      	</td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		<input type="button" name="btnQuery" id="btnQuery"  value="查询"  class="normal_btn" onClick="__extQuery__(1);" >
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;&nbsp;
    	</td>
    </tr>
</table>
<!-- 查询条件 end -->

<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
</html>
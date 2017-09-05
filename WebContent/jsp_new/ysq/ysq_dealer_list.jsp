<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>预授权服务站查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/YsqAction/ysqDealerList.json?query=true";
			
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
	     		 OpenHtmlWindow('<%=contextPath%>/YsqAction/showWorkFlowTemp.do?id='+id,800,500);
	     	 }
		    function myLink1(value,meta,record){
				var vin = record.data.VIN;
				var status=record.data.STATUS;
				var createBy=record.data.CREATE_BY;
		    	var url="";
		    	var urlView="<%=contextPath%>/YsqAction/ysqView.do?id="+value+"&vin="+vin+"&createBy="+createBy;
		    	var urlUpdate="<%=contextPath%>/YsqAction/ysqUpate.do?id="+value+"&vin="+vin+"&createBy="+createBy;
		    	if(93461001!=status && 93461010!=status){
		    		url+="<a href='#' onclick='print(\""+value+"\",\""+vin+"\",\""+createBy+"\");'>[打印]</a>";
		    	}
		    	if(93461001==status || 93461006==status || 93461004==status){
		    		url+="<a href='"+urlUpdate+"'>[修改]</a>";
		    	}
		    	if(93461001==status){
		    		url+="<a href='#' onclick='report(\""+value+"\");'>[上报]</a>";
		    		url+="<a href='#' onclick='del(\""+value+"\");'>[删除]</a>";
		    	}
		    	url+="<a href='"+urlView+"'>[明细]</a>";
		        return String.format(url);
		    }
		    function report(id){
		    	  MyConfirm("是否确认上报？",reportsubmit,[id]);
		    }
		    
		    function reportsubmit(id){
		    	  var url='<%=contextPath%>/YsqAction/ysqReportSubmit.json?id='+id;
		    	  sendAjax(url,function(json){
		    		  if(json.succ!="-1"){
		    			  MyAlert("提示：上报成功！");
		    			  __extQuery__(1);
		    		  }else{
		    			  MyAlert("提示：上报失败！");
		    			  return;
		    		  }
		    	  },'fm');
		      }
		    
		    function print(id,vin,createBy){
		    	var tarUrl ="<%=contextPath%>/YsqAction/ysqPrint.do?id="+id+"&vin="+vin+"&createBy="+createBy;
				window.open(tarUrl,'','left=0,top=0,width='+screen.availWidth +'- 10,height='+screen.availHeight+'-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
		    }
		    function del(id){
		    	MyConfirm("是否确认删除？",delCommit,[id]);
		    }
		    function delCommit(id){
			    var urlDel='<%=contextPath%>/CommonAction/del.json?tableName=Tt_As_Wr_ysq&idName=id&id='+id;
		    	sendAjax(urlDel,function(json){
		    		if(json.succ=="1"){
		    			MyAlert("提示：删除成功！");
		    			 qury_fun();
		    		}else{
		    			MyAlert("提示：删除失败！");
		    		}
		    	},'fm');
	    	}
		    function add_fun(obj){
				window.location.href = "<%=contextPath%>/YsqAction/ysqAdd.do";
			}
		    function qury_fun(obj){
		    	__extQuery__(1);
		    }
</script>
<!--页面列表 end -->
</head>
<body>
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔预授权管理&gt;预授权经销商查询</div>
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
      	<td width="15%"><input name="begin_time" type="text" id="beginTime" readonly="readonly" value="${startTime }" onfocus="calendar();" class="middle_txt"/></td>
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
         	genSelBoxExp("status",<%=Constant.STATUS_YSQ%>,"",true,"short_sel","","false",'');
         	 </script>
		</td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true"></td>
      	<td width="15%">
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true"></td>
      	<td width="15%">
      	</td>
		<td width="12.5%"></td>
	</tr>
</table>
		<table width='100%' border='0' style='text-align: center;'
			cellspacing='0' cellpadding='0'>
			<tr>
				<td>&nbsp;&nbsp;&nbsp;<input id='add_btn' name='add_btn'
					value='新增' type='button' class='normal_btn'
					onclick='add_fun(this);' />&nbsp;&nbsp;&nbsp;<input id='queryBtn'
					name='qury_btn' value='查询' type='button' class='normal_btn'
					onclick='qury_fun(this);' />&nbsp;&nbsp;&nbsp;<input id='res_btn'
					name='res_btn' value='重置' type='reset' class='normal_btn'
					onclick='res_fun(this);' /></td>
			</tr>
		</table>
		<br />

		<!-- 查询条件 end -->

<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
</html>
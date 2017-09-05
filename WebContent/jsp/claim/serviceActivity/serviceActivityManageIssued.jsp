<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动计划下发</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
//日历控件初始化
function doInit()
	{
	   loadcalendar();
	}
</script>
</head>

<body>	
<form name="fm" id="fm" method="post">
		<input type="hidden" name="activityId" id="activityId" />
	<div class="navigation">
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动下发计划
	</div>
	<table class="table_query">
			<tr>
              <td class="table_query_2Col_label_5Letter" >活动编号： </td>
	              <td align="left">
		             	 	<input type="text"  name="activityCode"  id="activityCode"  class="middle_txt"  size="25" datatype="1,is_digit_letter,25" />
            	  </td>
	              <td class="table_query_2Col_label_5Letter" >活动名称：</td>
	              <td align="left">
	              	<input type="text" name="activityName" class="middle_txt"/>
	              </td>
            </tr>
            <tr>
				<td  class="table_query_2Col_label_5Letter">活动日期：</td>
				<td align="left">
				 	 <div align="left">
	            		<input name="startdate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
           	           至 <input name="enddate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
           	         </div>
				</td>
				<td  class="table_query_2Col_label_5Letter" >活动状态：</td>
	              <td align="left">
		                <script type="text/javascript">
	   					    genSelBoxExp("status",<%=Constant.SERVICEACTIVITY_STATUS%>,"",true,"short_sel","","true",'');
	  				    </script>
  				</td>
			</tr>
			<tr>
					<td align="center" class="zi" colspan="5">
						<input class="normal_btn" type="button" name="button1" value="查询" onclick="__extQuery__(1);"/>
					</td>
			</tr>
	</table>
   <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
   <!--分页 end --> 
</form>
 <br/>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
//查询路径
	var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageIssued/serviceActivityManageIssuedQuery.json";
				
	var title = null;

	var columns = [
				{header: "活动编号", dataIndex: 'ACTIVITY_CODE', align:'center'},
				{header: "活动名称",dataIndex: 'ACTIVITY_NAME' ,align:'center'},
				{header: "发布日期 ",dataIndex: 'RELEASEDATE' ,align:'center'},
				{header: "开始日期",dataIndex: 'STARTDATE' ,align:'center'},
				{header: "活动状态",dataIndex: 'STATUS' ,align:'center' ,renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ACTIVITY_ID',renderer:mySelect  ,align:'center'}
		      ];
	//功能：状态交替出现;
	//状态：1.尚未发布；2.已经发布；3.重新发布;
	//描述：当新建活动时：
	//      1.活动状态为尚未发布，[操作]应为[首次发布]；
	//      2.[首次发布]操作之后，活动状态修改为：已经发布，[操作]应为[重新发布];
	//      3.修改活动内容时：活动状态为：重新发布，[操作]应为[重新发布];
	function mySelect(value,meta,record){
		var activityId=record.data.ACTIVITY_ID; //活动ID
		var status = record.data.STATUS;        //活动状态
		if(status == '<%=Constant.SERVICEACTIVITY_STATUS_01 %>')
		{
			return String.format("<a href=\"#\" onclick='firstSel(\""+activityId+"\")'>[首次发布]</a><a href=\"#\" onclick='sel(\""+activityId+"\")'>[明细]</a>");
		}else
		{
			return String.format("<a href=\"#\" onclick='repeatSel(\""+activityId+"\")'>[重新发布]</a><a href=\"#\" onclick='sel(\""+activityId+"\")'>[明细]</a>");
		}
  		
	}
	//首次发布
	function firstSel(value){
		makeNomalFormCall("<%=contextPath%>/claim/serviceActivity/ServiceActivityManageIssued/serviceActivityManageIssuedUpdateStatus.json?activityId="+value,showForwordValue,'fm','queryBtn');
	}
	//关闭子页面并刷新父页面
	function showForwordValue(json){
		if(json.returnValue == '1')
		{
			MyAlert("首次发布成功！");
			__extQuery__(1);
		}else if(json.returnValue==10){
			MyAlert("发布失败---车型，车龄，生产基地必须有一项已存在！");
		}else{   
			if(json.returnValue == '2'){
				MyAlert("发布失败---执行经销商不存在！");
			}else{
	            if(json.returnValue == '3'&&json.returnValue == '4'&&json.returnValue == '5'){
	            	MyAlert("发布失败---车型、车龄、车辆性质不存在！");
		        }
		        if(json.returnValue == '3'&&json.returnValue != '4'&&json.returnValue != '5'){
		        	MyAlert("发布失败---车型不存在、车龄、车辆性质存在！");
			   }
		        if(json.returnValue != '3'&&json.returnValue == '4'&&json.returnValue != '5'){
		        	MyAlert("发布失败---车龄不存在、车型、车辆性质存在！");
			   }
		        if(json.returnValue != '3'&&json.returnValue != '4'&&json.returnValue == '5'){
		        	MyAlert("发布失败---车辆性质不存在、车型、车龄存在！");
			   }
			    if(json.returnValue == '6'){
			    	MyAlert("发布失败---(车龄,车型,生产基地)与VIN必须存在一个！");
			   }
	       }
			
		}
	}
	//重新发布
	function repeatSel(value){
		$("activityId").value = value;
		fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageIssued/serviceActivityManageIssuedInfoQuery.do";
		fm.submit();
	}
	//详细页面
	function sel(value){
		$("activityId").value = value;
		//fm.action ="/claim/serviceActivity/ServiceActivityManageIssued/serviceActivityManageIssuedInfo.do";
		fm.action ="<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/getActivityIdInfo.do";
		fm.submit();
	}
</script>
<!--页面列表 end -->
</body>
</html>
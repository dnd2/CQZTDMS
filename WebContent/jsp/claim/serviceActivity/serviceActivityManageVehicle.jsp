<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动车辆信息确认</title>
<%
	String contextPath = request.getContextPath();
%>
<% //List<TtAsActivityPO> list=(List<TtAsActivityPO>)request.getAttribute("list");
%>
<script type="text/javascript">
function doInit()
	{
	   loadcalendar();
	}
</script>
</head>

<body>
<!-- 查询条件 begin -->
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;服务活动管理&gt;服务活动车辆信息确认
	</div>
<form method="post" name="fm" id="fm">
<table width="95%" align=center class="table_query">
			<tr>
			      <td class="table_query_2Col_label_5Letter">活动编号： </td>
	              <td align="left">
	               <input type="text"   class="middle_txt"  style="cursor: pointer;" id="activityCodes" name="activityCodes"  />
            	  </td>
                  <td class="table_query_2Col_label_5Letter">活动名称： </td>
	              <td align="left">
		              <input type="text"   class="middle_txt"  style="cursor: pointer;" id="activity_name" name="activity_name"  />
		              <input type="hidden" name="activityCode" id="activityCode"/>
				      <input type="button" class="mini_btn" value="..." onclick="openQueryName();"/>
				      <input type="button" class="normal_btn" value="清除" onclick="clrName();"/>
            	  </td>
            </tr>
            <tr>
					<td class="table_query_2Col_label_5Letter">维修状态：</td>
					<td align="left">
					 	 <script type="text/javascript">
   					          genSelBoxExp("repairStatus",<%=Constant.SERVICEACTIVITY_REPAIR_STATUS%>,"",true,"short_sel","","true",'');
  				         </script>	
					</td>
					<td class="table_query_2Col_label_5Letter">销售状态：</td>
					<td align="left" colspan="2">
					       <script type="text/javascript">
   					           genSelBoxExp("saleStatus",<%=Constant.SERVICEACTIVITY_SALE_STATUS%>,"",true,"short_sel","","true",'');
  			               </script>
					</td>
			</tr>
			<tr>
			      <td class="table_query_2Col_label_5Letter">发布状态：</td>
					<td align="left">
					 	 <script type="text/javascript">
   					          genSelBoxExp("status",<%=Constant.SERVICEACTIVITY_STATUS%>,"",true,"short_sel","","true",'');
  				         </script>	
					</td>
            </tr>
			<tr>
				<td colspan="4"  align="left">车辆责任经销商不在执行经销商列表中：
				      <input type="checkBox" name="checkedDealer" id="checkedDealer" value="1"/>
				 </td>
			</tr>
			<tr>
				<td colspan="11" align="center">
			 	 <input class="normal_btn" type="button" name="button1" value="查询" onclick="__extQuery__(1);" />
			 	 <!--
			 	 <input class="normal_btn" type="button" value="删除" name="modify" onclick="subChecked();" />
				 -->
				</td>
			</tr>
</table>
<!--分页 begin --> 
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /> 
<jsp:include page="${contextPath}/queryPage/pageDiv.html" /> 
<!-- 分页 end --> 
</form>
<br />
<!--页面列表 begin --> 
<script type="text/javascript">
	var myPage;
//查询路径
	var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageVehicle/serviceActivityManageVehicleQuery.json";
				
	var title = null;

	var columns = [
				{header: "活动编号", dataIndex: 'ACTIVITY_CODE', align:'center'},
				{header: "活动名称", dataIndex: 'ACTIVITY_NAME', align:'center'},
				{header: "VIN",dataIndex: 'VIN' ,align:'center'},
				{header: "责任经销商代码  ",dataIndex: 'DEALER_CODE' ,align:'center'},
				{header: "责任经销商名称 ",dataIndex: 'DEALER_NAME' ,align:'center'},
				{header: "销售状态  ",dataIndex: 'SALE_STATUS' ,align:'center',renderer:getItemValue},
				{header: "维修状态   ",dataIndex: 'REPAIR_STATUS' ,align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ACTIVITY_ID',renderer:serviceActivityManageModifyInit ,align:'center'}
		      ];
	//修改/删除/明细的超链接设置
	function serviceActivityManageModifyInit(value,meta,record){
	    var status =<%=Constant.SERVICEACTIVITY_STATUS_01%>//尚未发布
	    var activityStatus=record.data.STATUS;
	    if(status==activityStatus){
			var dealerCode=record.data.DEALER_CODE;
			var id=record.data.ID;
			var vin=record.data.VIN;
		    return String.format(
	         "<a href=\"<%=contextPath%>/claim/serviceActivity/ServiceActivityManageVehicle/serviceActivityManageModifyInit.do?activityId="+ value + "&dealerCode="+ dealerCode +"&vin="+vin+" \">[修改]</a><a href=\"#\" class=\"colorChanage\" onclick='delPre("+id+");'>[删除]</a>");
       }else{
			 return String.format(
			 "<a href=\"#\" class=\"colorChanage\" onclick='warningUpdate();'>[修改]</a><a href=\"#\" class=\"colorChanage\" onclick='warningDel();'>[删除]</a>");
			 }
	}
	//提示已经完成
	function warningUpdate(){
		MyAlert("已经发布不能修改！");
		}
	//提示无法删除
	function warningDel(){
		MyAlert("已经发布不能删除！");
		}
    //删除
    function delPre(id){
     MyConfirm("是否确认删除？",del,[id]);
    }
	function del(id){
		fm.action="<%=contextPath%>/claim/serviceActivity/ServiceActivityManageVehicle/serviceActivityManageDelete.do?orderIds="+ id;
		fm.submit();
	}
	
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='orderIds' value='" + value + "' />");
	}
	//删除确认 开始
	function subChecked() {
		var str="";
		var chk = document.getElementsByName("orderIds");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++){        
		if(chk[i].checked)
		{            
		str = chk[i].value+","+str; 
		cnt++;
		}
		}
		if(cnt==0){
		        MyAlert("请选择！");
		        return;
		    }else{
		    		 MyConfirm("是否确认删除？",serviceActivityManageDelete,[str]);
		    }
		} 
	//删除确认 结束
	//删除开始
	function serviceActivityManageDelete(str){
	makeNomalFormCall('<%=request.getContextPath()%>/claim/serviceActivity/ServiceActivityManageVehicle/serviceActivityManageDelete.json?orderIds='+str,returnBack,'fm','queryBtn');
	}
	//删除回调函数
	function returnBack(json){
		var del = json.returnValue;
		if(del==1){
			__extQuery__(1);
			MyAlert("删除成功！");
		}else{
			MyAlert("删除失败！请联系管理员！");
		}
	}
	//删除结束
  function openQueryName(){
	var url = "<%=request.getContextPath()%>/jsp/claim/serviceActivity/serviceActivityShowQuery.jsp?flag=1";
	OpenHtmlWindow(url,900,500);
	}
  function clrName(){
    document.getElementById('activity_name').value = '';
  }
  function showName(activity_id,activity_code,activity_name){
  //MyAlert('activity_id:'+activity_id+'|activity_code:'+activity_code+'|activity_name:'+activity_name);
    document.getElementById('activity_name').value = activity_name;
    document.getElementById('activityCode').value = activity_code;
  }
</script> 
<!--页面列表 end -->
</body>
</html>
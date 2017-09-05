<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>工单维护</TITLE>
<SCRIPT LANGUAGE="JavaScript">
	var myPage;
	//查询路径
	var url = "<%=contextPath%>/RepairOrderAction/queryRepairOrderDelet.json";
				
	var title = null;

	var columns = [
					{header: "序号",sortable: false,align:'center',renderer:getIndex},
					{header: "工单号", width:'15%', dataIndex: 'RO_NO'},
					{header: "维修类型", width:'7%', dataIndex: 'REPAIR_TYPE_CODE',renderer:getItemValue},
					{header: "结算基地", width:'7%', dataIndex: 'BALANCE_YIELDLY',renderer:getItemValue},
					{header: "车牌号", width:'7%', dataIndex: 'LICENSE'},
					{header: "VIN", width:'15%', dataIndex: 'VIN'},
					{header: "车型", width:'15%', dataIndex: 'MODEL'},
					{header: "车主", width:'15%', dataIndex: 'OWNER_NAME'},
					{header: "工单开始时间", width:'15%', dataIndex: 'RO_CREATE_DATE',renderer:formatDate},
					{header: "进厂里程数", width:'15%', dataIndex: 'IN_MILEAGE'},
					{header: "单据保养次数", width:'15%', dataIndex: 'FREE_TIMES'},
					{header: "工单状态", width:'15%', dataIndex: 'RO_STATUS',renderer:getItemValue},
					{header: "预警", width:'15%', dataIndex: 'IS_WARNING',renderer:getItemValue},
					{header: "预授权状态", width:'15%', dataIndex: 'FORL_STATUS',renderer:getItemValue}
					//{header: "申请日期", width:'15%', dataIndex: 'createDate',renderer:formatDate},
					//{header: "申请状态", width:'15%', dataIndex: 'status',renderer:getItemValue},
					//{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'id',renderer:myLink1,align:'center'}
		      ];
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
	}
	//行号加工单号
	function roLine(value,metadata,record) {
		return value+"-"+record.data.lineNo;
	}
	//单位代码渲染函数
	function dezero(value,metadata,record) {
		if (value==0){
			return "";
		}else {
			return value;
		}
	}
	
	//格式化时间为YYYY-MM-DD
	function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
	//修改的超链接设置
	function myLink1(value,meta,record){
		var rostatus=record.data.roStatus;
		var roNo=record.data.roNo;
			if(rostatus==<%=Constant.RO_STATUS_02%>){
			return String.format("<a href=\"<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail.do?roNo="+roNo+"&type=4&ID="+ value + "\">[明细]</a>");
			}
			if(rostatus!=<%=Constant.RO_STATUS_02%>){
					if(record.data.forlStatus==<%=Constant.RO_FORE_01%>){
						return String.format("<a href=\"<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail.do?roNo="+roNo+"&type=4&ID="+ value + "\">[明细]</a>");
					}
					if(record.data.forlStatus==0){
						return String.format("<a href=\"<%=contextPath%>/repairOrder/RoMaintainMain/roModifyForward.do?roNo="+roNo+"&type=4&ID="+ value + "\">[修改]</a>");
					}
				}
			if(rostatus==<%=Constant.RO_STATUS_01%>&&record.data.forlStatus!=<%=Constant.RO_FORE_01%>){
				return String.format("<a href=\"<%=contextPath%>/repairOrder/RoMaintainMain/roModifyForward.do?roNo="+roNo+"&type=4&ID="+ value + "\">[修改]</a>");
			}
			else{

				return String.format("<a href=\"<%=contextPath%>/repairOrder/RoMaintainMain/roModifyForward.do?roNo="+roNo+"&type=4&ID="+ value + "\">[明细]</a>");
			}
			

	}
	//设置超链接
	function mySelect(value,meta,record){
  		return String.format(
         "<a href=\"#\" onclick='sel(\""+record.data.orderId+"\")'>"+ value +"</a>");

	}
	//具体操作
	function sel(){
		MyAlert("超链接！");
	}
	
	//取得已经选择的checkbox，拼接成字符串，各项目以,隔开
	function getCheckedToStr(name) {
		var str="";
		var chk = document.getElementsByName(name);
		if (chk==null){
			return "";
		}else {
		var l = chk.length;
		for(var i=0;i<l;i++){        
			if(chk[i].checked)
			{            
			str = chk[i].value+","+str; 
			}
		}
			return str;
		}
	}
	//上报
	function submitId(){
	var str=getCheckedToStr("orderIds");
		if (str!=""){
		MyConfirm("确认上报？",submitApply,[str]);
		}else {
			MyAlert("请选择至少一条要上报的申请单！");
		}
	}
	//上报操作
	function submitApply (str) {
		
			makeNomalFormCall('<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplySubmit.json?orderIds='+str,returnBack0,'fm','queryBtn');
		
	}
	//上报回调函数
	function returnBack0(json){
		var avl = json.returnValue;
		if(avl==1){
			__extQuery__(1);
			MyAlert("上报成功！");
		}else{
			MyAlert("上报失败！请联系管理员！");
		}
	}
	//删除
	function deleteId(){
	var str=getCheckedToStr("orderIds");
		if (str!=""){
		MyConfirm("确认删除？",deleteApply,[str]);
		}else {
			MyAlert("请选择至少一条要删除的申请单！");
		}
	}
	function deleteApply(str) {
		
			makeNomalFormCall('<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplyDelete.json?orderIds='+str,returnBack,'fm','queryBtn');
		
	}
	//删除回调函数
	function returnBack(json){
		var avl = json.returnValue;
		if(avl==1){
			__extQuery__(1);
			MyAlert("删除成功！");
		}else{
			MyAlert("删除失败！请联系管理员！");
		}
	}
	//回调函数
	function showResult(json){
		if(json.ACTION_RESULT == '1'){
			goBack();
			MyConfirm("新增成功！点击确认返回查询界面或者点击左边菜单进入其他功能！","window.location.href = '<%=request.getContextPath()%>/sysmng/orgmng/DlrInfoMng/queryAllDlrInfo.do'");
		}else if(json.ACTION_RESULT == '2'){
			MyAlert("新增失败！请重新载入或者联系系统管理员！");
		}
	}
	//返回
	function goBack(){
		window.location.href = "<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplyQuery.json";
	}
	
	function queryPer(){
	var star = $('RO_CREATE_DATE').value;
	var end = $('DELIVERY_DATE').value;
	  if(star==""||end ==""){
	  	MyAlert("查询时间必须选择");
	 	 return false;
	  }else if(star>end){
	  	MyAlert("开始时间不能大于结束时间");
	  	return false;
	  }else {
	   var s1 = star.replace(/-/g, "/");
		var s2 = end.replace(/-/g, "/");
		var d1 = new Date(s1);
		var d2 = new Date(s2);
		var time= d2.getTime() - d1.getTime();
		var days = parseInt(time / (1000 * 60 * 60 * 24));
		if(days>=93){
		MyAlert("时间跨度不能超过3个月");
	  		return false;
		}
	 	 __extQuery__(1);
	  }
	}
//设置超链接 end
</script>
</HEAD>
<BODY onload="doInit();">
<div class="navigation"><img class="nav" src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;维修登记&gt;工单废弃查询<span style="color: red;font-weight: bold;">(只能查询2015-5-25之前的数据)</span></div>
  <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
          <tr>
            <td  class="table_query_2Col_label_7Letter">工单号：</td>
            <td><input name="RO_NO" id="RO_NO" value="" type="text"  class="middle_txt" maxlength="20" />
            </td>
            <td rowspan="2" class="table_query_2Col_label_7Letter" >VIN：</td>
 			<td rowspan="2"  align="left">
 			  <input name="VIN" id="VIN" datatype="1,is_vin" type="text"  value="" class="middle_txt"/>
 			<!--<textarea name="VIN" cols="18" rows="3" datatype="1,is_digit_letter"></textarea>-->
 			</td>
          </tr>
          <tr>
            <td  class="table_query_2Col_label_7Letter">维修类型：</td>
            <td >
            <script type="text/javascript">
	              genSelBoxExp("REPAIR_TYPE",<%=Constant.REPAIR_TYPE%>,"",true,"short_sel","","false",'');
	       	</script>
 		    </td> 	         
          </tr>    
                         
          <tr>
            <td class="table_query_2Col_label_7Letter">工单开始日期：</td>
            <td align="left" nowrap="true">
			<input name="RO_CREATE_DATE" type="text" class="short_time_txt" id="RO_CREATE_DATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'RO_CREATE_DATE', false);" />  	
             &nbsp;至&nbsp; <input name="DELIVERY_DATE" type="text" class="short_time_txt" id="DELIVERY_DATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'DELIVERY_DATE', false);" /> 
		</td>	
             
            <td  class="table_query_2Col_label_7Letter">工单状态：</td>
  			<td >
  			<script type="text/javascript">
	              genSelBoxExp("RO_STATUS",<%=Constant.RO_STATUS%>,"",true,"short_sel","","false",'');
	        </script>
  			</td>
          </tr>
          <tr>
          	<td class="table_query_2Col_label_7Letter">
          		授权状态：
          	</td>
          	<td >
          	  	<script type="text/javascript">
	              genSelBoxExp("RO_FORE",<%=Constant.RO_FORE%>,"",true,"short_sel","","false",'');
	        </script>
            <td class="table_edit_2Col_label_7Letter">
						结算基地：
					</td>
					<td>
					 <script type="text/javascript">
				            genSelBoxExp("YIELDLY_TYPE",<%=Constant.PART_IS_CHANGHE%>,"",true,"short_sel","","false",'');
				        </script>
					</td>
           
          </tr>
          <tr style="display:none ">
        
					<td class="table_edit_2Col_label_7Letter" >排序字段：</td>
					<td >
					<select  name="orderBy" class="short_sel"  onchange="__extQuery__(1);">
					<option value="" >--请选择--</option>
					<option value="ro_no"  >按工单号降序</option>
					<option value="create_date" selected="selected" >按开单时间降序</option>
					</select>
					</td>
					<td colspan="2"></td>
          </tr>
          <tr>
				<td class="table_edit_2Col_label_7Letter">
										预警：
				</td>          
          		<td>
					 <script type="text/javascript">
				            genSelBoxExp("IS_WARNING",<%=Constant.IF_TYPE%>,"",true,"short_sel","","false",'');
				     </script>
				</td>
          </tr>
    	  <tr>
            <td colspan="4" align="center" nowrap><input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="queryPer();" />
			</td>
            <td  align="right" ></td>
  </table>
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
</BODY>
<script type="text/javascript">
  function   showMonthFirstDay()     
  {     
	  var   Nowdate=new   Date();     
	  var   MonthFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth(),1);     
	  return DateUtil.Format("yyyy-MM-dd",MonthFirstDay);  
  }     
  function   showMonthLastDay()     
  {     
	  var   Nowdate=new   Date();     
	  var   MonthNextFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth()+1,1);     
	  var   MonthLastDay=new   Date(MonthNextFirstDay-86400000);     
	  return DateUtil.Format("yyyy-MM-dd",MonthLastDay);   
  }     
  $('RO_CREATE_DATE').value=showMonthFirstDay();
  $('DELIVERY_DATE').value=showMonthLastDay();
</script>
</html>
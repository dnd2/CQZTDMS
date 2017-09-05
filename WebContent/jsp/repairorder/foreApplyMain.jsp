<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>索赔单维护</TITLE>
<SCRIPT LANGUAGE="JavaScript">
	var myPage;
	//查询路径
	var url = "<%=contextPath%>/repairOrder/RoMaintainMain/foreApplyQuery.json";
				
	var title = null;

	var columns = [
					{header: "工单号", width:'15%', dataIndex: 'roNo',renderer:roRender},
					{header: "维修类型", width:'7%', dataIndex: 'repairTypeCode',renderer:getItemValue},
					{header: "VIN", width:'15%', dataIndex: 'vin'},
					{header: "进厂里程数", width:'15%', dataIndex: 'inMileage'},
					{header: "工单状态", width:'15%', dataIndex: 'roStatus',renderer:getItemValue},
					{header: "结算基地", width:'7%', dataIndex: 'balanceYieldly',renderer:getItemValue},
					{header: "预警", width:'15%', dataIndex: 'isWarning',renderer:getItemValue},
					{header: "授权状态", width:'15%', dataIndex: 'forlStatus',renderer:getItemValue},
					{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'id',renderer:myLink1,align:'center'}
		      ];
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
	}
	//
	function roRender(value,metadata,record) {
		return '<a href="#" onclick="roLink(\''+record.data.id+'\',\''+value+'\')" >'+value+'</a>';
		//OpenHtmlWindow('<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail.do?ID='+record.data.id,800,500)
	}
	function roLink (id,roNo){
		var fm = document.getElementById('fm');
		fm.action="<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail.do?roNo="+roNo+"&type=5&ID="+id;
		fm.submit();
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
		var insurationCode = record.data.insurationCode;
		var roNoType = record.data.repairTypeCode;
		var roNo = record.data.roNo;
		
		if('<%=Constant.RO_STATUS_01%>'==record.data.roStatus&&record.data.isDe==0){
			if(record.data.approvalYn==1&&record.data.forlStatus==0||record.data.approvalYn==1&&record.data.forlStatus==<%=Constant.RO_FORE_03%>){
				var url1 = "<a href=\"<%=contextPath%>/repairOrder/RoMaintainMain/roModifyForward.do?type=4&ID="
					+ value + "\">[预授权申请]</a>";
				var url2 = "<a href=\"<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail.do?roNo="+roNo+"&type=1&ID="+ value + "\">[明细]</a>";
				return String.format(url2+url1);
				
			}
		}
	}
		
	//配件大类配件窗口
	
	
	function showRepairPartWin(roNo,ID,checkFlag){
		if(checkFlag!=''){
			//OpenHtmlWindow("<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail.do?type=2&roNo="+roNo+"&ID="+ID+"&checkFlag="+checkFlag,800,500);
		window.location.href="<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail2.do?type=2&roNo="+roNo+"&ID="+ID+"&checkFlag="+checkFlag;
		
		}else{
			window.location.href="<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail2.do?type=2&roNo="+roNo+"&ID="+ID;
		}
	}
	
	function showRepairSpecial(roNo,ID,checkFlag){
		OpenHtmlWindow("<%=contextPath%>/repairOrder/RoMaintainMain/roForwardSpecial.do?type=2&roNo="+roNo+"&ID="+ID,800,500);
	
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
	function (json){
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
<BODY onload="doInit()">
<div class="navigation"><img src="../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔预授权&gt;预授权申请</div>
  <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
          <tr>
            <td  class="table_query_2Col_label_7Letter">工单号：</td>
            <td><input name="RO_NO" id="RO_NO" value="" maxlength="20" type="text"  class="middle_txt" />
            <input name="APPLICTION" id="APPLICTION" value="" type="hidden" class="middle_txt" />
            </td>
            <td rowspan="2" class="table_query_2Col_label_7Letter" >VIN：</td>
 			<td rowspan="2"  align="left">
 			<!--  <input name="VIN" id="VIN" datatype="1,is_vin" type="text"  value="" class="middle_txt"/>-->
 			<textarea name="VIN" cols="18" rows="3" datatype="1,is_digit_letter"></textarea>
 			</td>
          </tr>
          <tr>
            <td  class="table_query_2Col_label_7Letter">维修类型：</td>
            <td >
            <script type="text/javascript">
	              genSelBoxExp("REPAIR_TYPE",<%=Constant.REPAIR_TYPE%>,"",true,"short_sel","","false",'');
	       	</script>
            <!-- <select name="REPAIR_TYPE">
            <option value="">-请选择-</option>
            <option value="DQBY">定期保养</option>
            <option value="NBXL">内部维修</option>
            <option value="PTWX">普通维修</option>
            <option value="SQWX">售前维修</option>
            <option value="XCZH">新车装潢</option>
            </select>
          
			<script type="text/javascript">
	              genSelBoxExp("REPAIR_TYPE",<%=Constant.REPAIR_TYPE%>,"",true,"short_sel","","true",'');
	       </script>
	        -->
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
            <td  class="table_query_2Col_label_7Letter">申请单状态：</td>
  			<td >
  			<script type="text/javascript">
	              genSelBoxExp("RO_STATUS",<%=Constant.RO_STATUS%>,"",true,"short_sel","","false",'');
	        </script>
  			</td>
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
          <tr style="display: none">
          	<td class="table_query_2Col_label_7Letter">
          		授权状态：
          	</td>
          	<td >
          	  	<script type="text/javascript">
	              genSelBoxExp("RO_FORE",<%=Constant.RO_FORE%>,"",true,"short_sel","","false",'');
	        </script>
          	</td>
          </tr>
    	  <tr>
            <td colspan="4" align="center" nowrap><input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="queryPer();" />
			</td>
			</tr>
  </table>
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
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
</form>
</BODY>
</html>
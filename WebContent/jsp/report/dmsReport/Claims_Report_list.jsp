<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TmBusinessAreaPO,java.util.*" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/change" prefix="change" %>
<%
	String contextPath = request.getContextPath();
List<TmBusinessAreaPO> list = (List<TmBusinessAreaPO>)request.getAttribute("yieldlys");
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>索赔单冻结报表</TITLE>

<SCRIPT LANGUAGE="JavaScript">
var myPage;
	//查询路径
	var url = "<%=contextPath%>/report/dmsReport/Application/ClaimsReport.json?type=query";
				
	var title = null;

	var columns = [
					{header: "序号", width:'10%',renderer:getIndex},
					{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink1,align:'center'},
					{id:'id',header: "经销商代码", width:'10%', dataIndex: 'DEALER_CODE'},
					{header: "经销商简称", width:'11%', dataIndex: 'DEALER_SHORTNAME'},
					{header: "工单号", width:'15%', dataIndex: 'RO_NO',renderer:myLink},
					{header: "索赔单号", width:'15%', dataIndex: 'CLAIM_NO',renderer:myLink},
					{header: "车型", width:'7%', dataIndex: 'MODEL_NAME'},
					{header: "索赔类型", width:'7%', dataIndex: 'CLAIM_TYPE',renderer:getItemValue},
					{header: "结算基地", width:'7%', dataIndex: 'YIELDLY'},
					{header: "修改次数", width:'5%', dataIndex: 'SUBMIT_TIMES'},
					{header: "退回次数",sortable: false,dataIndex: 'BACK_TIMES',renderer:mybacklink},
					{header: "VIN", width:'15%', dataIndex: 'VIN'},
					{header: "申请日期", width:'15%', dataIndex: 'SUB_DATE',renderer:formatDate},
					{header: "申请状态", width:'15%', dataIndex: 'STATUS',renderer:getItemValue},
					{header: "是否扣件", width:'15%', dataIndex: 'KOU_JIAN'},
					{header: '索赔总金额',dataIndex:'BALANCE_AMOUNT'},
					{header: "审核人", width:'15%', dataIndex: 'AUDITING_MAN'},
					{header:'审核时间',width:'10%',dataIndex:'AUDITING_DATE',renderer:formatDate2}
					
		      ];
		      
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
	}
	function mybacklink(value,meta,record){
        var id=record.data.ID;
        var str ='<a href="#" onclick="viewbackaduit('+id+'); ">'+value+'<a>';
        return   String.format(str);
	  }
	  function viewbackaduit(claimId){
		  var printUrl = "<%=contextPath%>/ClaimAction/viewbackaduit.do?bizId="+claimId;
		  OpenHtmlWindow(printUrl,900,500);
	}
	//行号加工单号
	function roLine(value,metadata,record) {
		//return value+"-"+record.data.lineNo;
		return value;
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
			return value.substr(0,16);
		}
	}
	function formatDate2(value,meta,record) {
		if(record.data.status =='<%=Constant.CLAIM_APPLY_ORD_TYPE_03%>'){
			return '';
		}else if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,16);
		}
	}
	//工单的超链接
	function myLink(value,meta,record){
        return String.format(
               value);
    }
	//修改的超链接设置
	function myLink1(value,meta,record){
		var width=900;
		var height=500;
		var str="";
		var screenW = window.screen.width-30;	
		var screenH = document.viewport.getHeight();
	var isImport = record.data.isImport;
	var roNo = record.data.RO_NO;
	var claim_type = record.data.CLAIM_TYPE;
		if(screenW!=null && screenW!='undefined')
			width = screenW;
		if(screenH!=null && screenH!='undefined')
			height = screenH;
			if(isImport==<%=Constant.IF_TYPE_YES%>){
			return String.format("<a href=\"#\" onclick=\"showPrintPage1('"+value+"')\">[标签]</a>");
			}else{
  		return String.format("<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/ClaimBalanceAction/claimBalanceAuditingPage.do?goBackType=2&roNo="+roNo+"&id="
			+ value + "&claim_type="+claim_type+"&view=view\","+width+","+height+")' >[明细]</a><a href=\"#\" onclick=\"showPrintPage1('"+value+"')\">[标签]</a>");
	}
	}
	function showPrintPage1(claimId){ 
        
        var printUrl = "<%=contextPath%>/ClaimAction/barcodePrintDoGet.do?dtlIds="+claimId;
        window.open(printUrl,'','toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=1200'); 

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
	//清空经销商框
		function clearInput(){
			var target = document.getElementById('dealerCode');
			target.value = '';
		}
	//返回
	function goBack(){
		window.location.href = "<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplyQuery.json";
	}
	function queryPer(){
	var star = $('RO_STARTDATE').value;
	var end = $('RO_ENDDATE').value;
	  if(star==""||end ==""){
	  	MyAlert("申请时间必须选择");
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
	 	 __extQuery__(1);
	  }
	}
	
	function clearClaimInput(){
		$('CLAIM_DESC').value="";
		$('CLAIM_TYPE').value="";
	}
	function findamount(){
  	  var url = "<%=contextPath%>/report/dmsReport/Application/SumClaimAmount.json";
  	  sendAjax(url,findamountback,'fm');
	  }
	  function findamountback(json){
		  document.getElementById("amount").innerHTML=json.SUMAMOUNT;
    }

	function  ExportApp(){
		 fm.action="<%=contextPath%>/report/dmsReport/Application/ClaimsReportExport.do";
	       fm.submit();
	}
//设置超链接 end
</SCRIPT>

</HEAD>
<BODY>

<div class="navigation"><img src="<%=request.getContextPath()%>/jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔申请&gt;索赔单查询</div>
    <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
          <tr>
            <td class="table_query_2Col_label_6Letter"> 经销商代码：</td>
            <td align="left" >
            
            <input class="middle_txt" id="dealerCode"  name="DEALER_CODE" type="text" readonly="readonly"/>
            <input name="button1" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','dealerId','true','','true','','10771002');" value="..." />        
            <input name="button2" type="button" class="normal_btn" onClick="clearInput();" value="清除"/> 
            </td>
              <td class="table_query_2Col_label_6Letter">经销商名称：</td>
            <td><input name="DEALER_NAME" id="DEALER_NAME" value="" type="text" class="middle_txt" datatype="1,is_digit_letter_cn" /></td>
          </tr>
          <tr>
            <td  class="table_query_2Col_label_6Letter">索赔类型：</td>
            <td  >
<!-- 			<script type="text/javascript"> -->
<%-- 	              genSelBoxExp("CLAIM_TYPE",<%=Constant.CLA_TYPE%>,"",true,"short_sel","","false",''); --%>
<!-- 	       </script> -->
				<input class="middle_txt" id="CLAIM_DESC"  name="CLAIM_DESC" type="text" readonly="readonly"/>
				<input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showTcCode('CLAIM_TYPE','CLAIM_DESC','<%=Constant.CLA_TYPE%>','');" value="..." />        
	            <input name="clrBtn" type="button" class="normal_btn" onclick="clearClaimInput();" value="清除"/>
				<input class="middle_txt" id="CLAIM_TYPE"  name="CLAIM_TYPE" type="hidden" />
 		    </td> 
 		    <td  class="table_query_2Col_label_6Letter">申请单状态：</td>
  			<td >
  				<script type="text/javascript">
  					genSelBoxExp("STATUS",<%=Constant.CLAIM_APPLY_ORD_TYPE%>,"",true,"short_sel","","false",'10791001,10791009,10791003,10791007,10791010,10791011,10791012,10791013,10791014,10791015,10791016');
  				</script>
  			    </td>
          </tr>                   
          <tr>
            <td  class="table_query_2Col_label_6Letter">工单号：</td>
            <td  align="left" ><input type="text" name="RO_NO" id="RO_NO"  maxlength="20"  value="" 

class="middle_txt"/><INPUT name="LINE_NO" id="LINE_NO" value="" type="hidden"   

class="mini_txt"/></td>	    
			<td  class="table_query_2Col_label_6Letter">索赔单号：</td>   
			<td><input type="text" name="CLAIM_NO" id="CLAIM_NO"   value="" maxlength="20" 
class="middle_txt"/></td>      
 	
          </tr>
          <tr>
            <td  class="table_query_2Col_label_6Letter">零件代码：</td>
            <td  align="left" ><input type="text" name="partCode" id="partCode"  maxlength="30"  value="" class="middle_txt"/></td>	    
			<td  class="table_query_2Col_label_6Letter">作业代码：</td>   
			<td><input type="text" name="wrLabourCode" id="wrLabourCode"   value="" maxlength="30" class="middle_txt"/></td>      
 	
          </tr>
          
          <tr>
          <td class="table_query_2Col_label_6Letter" >申请时间：</td>
           <td align="left" nowrap="true">
			<input name="RO_STARTDATE" type="text" class="short_time_txt" id="RO_STARTDATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'RO_STARTDATE', false);" />  	
             &nbsp;至&nbsp; <input name="RO_ENDDATE" type="text" class="short_time_txt" id="RO_ENDDATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'RO_ENDDATE', false);" /> 
		</td>	
          
              <td class="table_query_2Col_label_6Letter">授权人：</td>
		    <td>
		    <input type="text" class="middle_txt"name="PERSON" value="" maxlength="25" />
		    </td>
          </tr>
		<tr>
		    <td class="table_query_2Col_label_6Letter" >产地：</td>
		    <td>
		      <select name="YIELDLY" id="YIELDLY" onChange="showDate(this.value);" class="short_sel">
					<option value="">-请选择-</option>
					<%for(int i=0;i<list.size();i++){ %>
						<option value="<%=list.get(i).getAreaId() %>"><%=list.get(i).getAreaName() %></option>
					<%} %>
					</select>
		    </td>
		    		<td  class="table_query_2Col_label_6Letter">VIN：</td>
 			<td align="left" rowspan="2">
 			<!-- <input name="VIN" id="VIN" type="text"  value="" class="short_txt"/>-->
 			<textarea name="VIN" cols="18" rows="3" ></textarea>
 			</td>
		   
	    </tr>
	    <tr>
	    	<td class="table_query_2Col_label_6Letter">审核时间：</td>
	    	 <td align="left" nowrap="true">
			<input name="approve_date" type="text" class="short_time_txt" id="approve_date" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'approve_date', false);" />  	
             &nbsp;至&nbsp; <input name="approve_date2" type="text" class="short_time_txt" id="approve_date2" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'approve_date2', false);" /> 
		</td>
	    </tr>
	    <tr>
	    	<td class="table_query_2Col_label_6Letter">是否导入：</td>
	    	<td align="left" nowrap="true">
	    		<script type="text/javascript">
	    			genSelBoxExp("is_import",<%=Constant.IF_TYPE%>,"${back_type }",true,"short_sel","","false",'');
	    		</script>
	    	</td>
	    	<td class="table_query_2Col_label_6Letter">审核人：</td>
	    	<td align="left" nowrap="true">
	    		<input type="text" class="middle_txt"name="foreAuthPerson" value="" maxlength="25" />
	    	</td>
	    </tr>
	    <tr>
			<td align="right" >车型：</td>
			<td align="left">
				<select id="model" name="model" class="short_sel">
					<option value=''>-请选择-</option>
					<c:forEach var="mode" items="${modelList}">
						<option value="${mode.groupCode}" title="${mode.groupCode}">${mode.groupName}</option>
					</c:forEach>
				</select>
			</td>
			<td  class="table_query_2Col_label_6Letter">跟踪状态：</td>
  			<td >
  			<script type="text/javascript">
		       genSelBoxExp("STATUS",<%=Constant.STATUS_TYPE%>,"",true,"short_sel","","false",'');
		    </script>
	       </td>
		</tr>
    	  <tr>
            <td colspan="4" align="center" nowrap>
            <input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="queryPer();findamount();" />
            <input id="queryBtn" class="normal_btn" type="button" name="button" value="导出"  onClick="ExportApp();" />
			</td>
            <td  align="right" ></td>
          </tr>
           <tr>
             <td align="center" colspan="8">总金额(元)：<span style="color: red;background-color: white;" id="amount"></span></td>
           </tr>
  </table>
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
<script type="text/javascript">
function   showMonthFirstDay()     
{     
	  var   Nowdate=new   Date();     
	  var   MonthFirstDayTT=new   Date(Nowdate.getYear(),Nowdate.getMonth()-11);
	  var   MonthFirstDay=new   Date(MonthFirstDayTT-86400000);
	  return DateUtil.Format("yyyy-MM-dd",MonthFirstDay);  
}     
function   showMonthLastDay()     
{     
	  var   Nowdate=new   Date();     
	  var   MonthNextFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth()+1,1);     
	  var   MonthLastDay=new   Date(MonthNextFirstDay-86400000);     
	  return DateUtil.Format("yyyy-MM-dd",MonthLastDay);   
}     
$('RO_STARTDATE').value=showMonthFirstDay();
$('RO_ENDDATE').value=showMonthLastDay();
</script>
</BODY>
</html>
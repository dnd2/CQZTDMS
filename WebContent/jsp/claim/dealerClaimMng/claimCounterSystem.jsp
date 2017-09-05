<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.TmBusinessAreaPO" %>
<%
	String contextPath = request.getContextPath();
	List <TmBusinessAreaPO> list = (List <TmBusinessAreaPO>)request.getAttribute("yieldly");
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>反索赔管理</TITLE>

<SCRIPT LANGUAGE="JavaScript">
var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/applicationSystemQuery.json";
				
	var title = null;

	var columns = [
					{header: "序号",sortable: false,align:'center',renderer:getIndex},
					{header: "索赔单号", width:'15%', dataIndex: 'claimNo',renderer:myLink},
					{header: "反索赔", width:'10%', dataIndex: 'isCounter',renderer:isCounter},
					{header: "索赔类型", width:'7%', dataIndex: 'claimType',renderer:getItemValue},
					{header: "工单号", width:'15%', dataIndex: 'roNo'},
					{header: "车系", width:'15%', dataIndex: 'seriesName'},
					{header: "车型", width:'15%', dataIndex: 'model'},
					{header: "送修人姓名", width:'15%', dataIndex: 'deliverer'},
					{header: "送修人电话", width:'15%', dataIndex: 'delivererPhone'},
					{header: "VIN", width:'15%', dataIndex: 'vin'},
					{header: "结算基地", width:'7%', dataIndex: 'balanceYieldly',renderer:getItemValue},
					{header: "生产基地", width:'15%', dataIndex: 'yieldlyName'},
					{header: "申请日期", width:'15%', dataIndex: 'subDate',renderer:formatDate},
					{header: "审核日期", width:'15%', dataIndex: 'reportDate',renderer:formatDate},
					{header: "申请状态", width:'15%', dataIndex: 'status',renderer:getItemValue},
					{header: '索赔总金额',dataIndex:'repairTotal',renderer:amountFormat}
		      ];


	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
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
			return value.substr(0,10);
		}
	}
	function formatDate2(value,meta,record) {
		if(record.data.status =='<%=Constant.CLAIM_APPLY_ORD_TYPE_03%>'){
			return '';
		}else if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
	//工单的超链接
	function myLink(value,meta,record){
		var width=900;
		var height=500;

		var screenW = window.screen.width-30;	
		var screenH = document.viewport.getHeight();

		if(screenW!=null && screenW!='undefined'){
			width = screenW;
		}
		if(screenH!=null && screenH!='undefined'){
			height = screenH;
		}
		var claimType = record.data.claimType;
		var roNo = record.data.roNo;
		var id = record.data.id;
		 return String.format("<a href='#' onclick=\"counter('"+record.data.id+"','"+roNo+"','"+claimType+"')\">["+value+"]</a>");
    }
	function isCounter(value,meta,record){
		if(value&&0!=value&&'0'!=value){
			return '是';
		} else {
			return '否';
		}
	}
	
	function counter(id,roNo,claimType)
	{
		var detailUrl ='<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimSystemDetailForward.do?id='+id+'&claim_type='+claimType+'&roNo='+roNo;
		OpenHtmlWindow(detailUrl, 1000, 550);
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
	
function print2(){
	var allChecks = document.getElementsByName('recesel');
		var ids="";
		var count=0;
		for(var i = 0;i<allChecks.length;i++){
			if(allChecks[i].checked){
			ids = allChecks[i].value+","+ids;
			count++;
		}
	}
	if(count==0){
	MyAlert("请选择要打印的数据!");
	return false;
	}
	if(ids!=""){
	ids=ids.substring(0,ids.length-1);
	}
	showPrintPage1(ids);
}
function queryPer(type){
	var star = $('RO_STARTDATE').value;
	var end = $('RO_ENDDATE').value;
	  if(star==""||end ==""){
	  	MyAlert("建单时间必须选择");
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
		if(type==1){
		 __extQuery__(1);
		document.getElementById("queryBtn2").disabled = false;
		}else{
		document.getElementById("queryBtn2").disabled = true;
		var fm = document.getElementById('fm');
		fm.action='<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/export.do';
		fm.method="post";
		fm.submit();
		}
	  }
	}
//设置超链接 end
</SCRIPT>

</HEAD>
<BODY>

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：索赔结算管理&gt;反索赔管理&gt;反索赔明细查询</div>
    <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
          <tr>
            <td  class="table_query_2Col_label_6Letter">索赔单号：</td>
            <td><input name="CLAIM_NO" id="CLAIM_NO" value="" maxlength="20" type="text"  class="middle_txt" />
            </td>
            <td  class="table_query_2Col_label_6Letter">工单号：</td>
            <td  align="left" ><input type="text" name="RO_NO" id="RO_NO" maxlength="20"  value="" class="middle_txt"/>
			<INPUT name="LINE_NO" id="LINE_NO" value="" type="hidden"   class="mini_txt"/></td>	            
          </tr>
          <tr>
            <td  class="table_query_2Col_label_6Letter">索赔类型：</td>
            <td >
			<script type="text/javascript">
	              genSelBoxExp("CLAIM_TYPE",<%=Constant.CLA_TYPE%>,"",true,"short_sel","","false",'');
	       </script>
 		    </td> 	         
 			<td  class="table_query_2Col_label_6Letter">VIN：</td>
 			<td  align="left" >
 			<!-- <input name="VIN" id="VIN" type="text" datatype="1,is_vin" value="" class="short_txt"/>-->
 			<textarea name="VIN" cols="18" rows="3" ></textarea>
 			</td>
          </tr>                   
          <tr>
             <td class="table_query_2Col_label_7Letter" >建单时间：</td>
             <td align="left" nowrap="true">
			<input name="RO_STARTDATE" type="text" class="short_time_txt" id="RO_STARTDATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'RO_STARTDATE', false);" />  	
             &nbsp;至&nbsp; <input name="RO_ENDDATE" type="text" class="short_time_txt" id="RO_ENDDATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'RO_ENDDATE', false);" /> 
		</td>
            <td  class="table_query_2Col_label_6Letter">索赔单状态：</td>
  			<td >
  			<script type="text/javascript">
	              genSelBoxExp("STATUS",<%=Constant.CLAIM_APPLY_ORD_TYPE%>,"",true,"short_sel","","false",'<%=Constant.CLAIM_APPLY_ORD_TYPE_02%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_05%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_04%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_10%>');
	       </script>
  			</td>
        
          </tr>
     	  <tr style="display: none">
            <td  class="table_query_2Col_label_6Letter">送修人姓名：</td>
            <td><input name="DELIVERER" id="DELIVERER" value="" type="text"  maxlength="15" class="middle_txt" />
            </td>
            <td  class="table_query_2Col_label_6Letter">送修人电话：</td>
            <td  align="left" ><input type="text" name="DELIVERER_PHONE" id="DELIVERER_PHONE" maxlength="18"  value="" class="middle_txt"/> </td>
          </tr>
           <tr>
         	 <td class="table_query_2Col_label_6Letter">结算审核时间：</td>
         	  <td align="left" nowrap="true">
			<input name="balance_approve_date" type="text" class="short_time_txt" id="balance_approve_date" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'balance_approve_date', false);" />  	
             &nbsp;至&nbsp; <input name="balance_approve_date2" type="text" class="short_time_txt" id="balance_approve_date2" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'balance_approve_date2', false);" /> 
		</td>
           	<td  class="table_query_2Col_label_6Letter">生产基地：</td>
            <td  align="left" >
            <select name="YIELDLY_TYPE" id ="YIELDLY_TYPE" style="width: 150px"  value="${YIELDLY}">
            	<option value="">--请选择--</option>
            	<%for(int i=0;i<list.size();i++){ %>
            		<option value="<%=list.get(i).getAreaId() %>"><%=list.get(i).getAreaName() %></option>
            	<%} %>
            </select>
            </td>
           </tr>
           <!-- 艾春 13.11.20 添加是否二级查询条件 -->
			
			 <tr>
            <td  class="table_query_2Col_label_6Letter">是否二级：</td>
            <td><select name="IS_SECOND" id="IS_SECOND" class="short_sel">
			  	    <option value="" selected="selected">请选择</option>
			  		<option value="1">是</option>
			  		<option value="0">否</option>
			  	</select>
            </td>
            <td  class="table_query_2Col_label_6Letter">经销商代码：</td>
				<td  align="left" >
				       <input class="middle_txt" id="dealerCode"  name="dealerCode" type="text"/>
			           <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true,'','10771002');" value="..." />        
			           <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput('dealerCode');" value="清除"/>
				</td>       
          </tr>
           <!-- 艾春 13.11.20 添加是否二级查询条件 -->
             <tr>
            <td colspan="4" align="center" nowrap>
            <input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="queryPer(1);" />
			</td>
            <td  align="right" ></td>
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
$('RO_STARTDATE').value=showMonthFirstDay();
$('RO_ENDDATE').value=showMonthLastDay();
</script>
</BODY>
</html>
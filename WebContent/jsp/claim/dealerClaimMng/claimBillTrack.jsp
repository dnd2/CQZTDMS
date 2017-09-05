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
<TITLE>索赔单状态跟踪</TITLE>

<SCRIPT LANGUAGE="JavaScript">
var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/applicationQuery.json";
				
	var title = null;

	var columns = [
					{header: "序号",sortable: false,align:'center',renderer:getIndex},
					{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\"  onclick='selectAll(this,\"recesel\")' />选择", align:'center',sortable:false, dataIndex:'id',width:'2%',renderer:checkBoxShow},
					{header: "工单号", width:'15%', dataIndex: 'roNo',renderer:myLink},
					{header: "索赔类型", width:'7%', dataIndex: 'claimType',renderer:getItemValue},
					{header: "索赔单号", width:'15%', dataIndex: 'claimNo'},
					{header: "车系", width:'15%', dataIndex: 'seriesName'},
					{header: "车型", width:'15%', dataIndex: 'groupName'},
					{header: "送修人姓名", width:'15%', dataIndex: 'deliverer'},
					{header: "送修人电话", width:'15%', dataIndex: 'delivererPhone'},
					{header: "VIN", width:'15%', dataIndex: 'vin'},
					{header: "结算基地", width:'7%', dataIndex: 'balanceYieldly',renderer:getItemValue},
					{header: "生产基地", width:'15%', dataIndex: 'yieldlyName'},
					{header: "建单日期", width:'15%', dataIndex: 'createDate',renderer:formatDate},
					{header: "申请日期", width:'15%', dataIndex: 'subDate',renderer:formatDate},
					{header: "审核日期", width:'15%', dataIndex: 'reportDate',renderer:formatDate},
					{header: "申请状态", width:'15%', dataIndex: 'status',renderer:getItemValue},
					{header: '索赔申请金额',dataIndex:'repairTotal',renderer:amountFormat},
					{header: '索赔审批金额',dataIndex:'balanceAmount',renderer:amountFormat},
					//{header:'索赔结算时间',width:'10%',dataIndex:'accountDate',renderer:formatDate2},
					{width:'5%',header: "打印",sortable: false,dataIndex: 'id',renderer:myLink1,align:'center'}
		      ];


	function checkBoxShow(value,meta,record){
		return String.format("<input type='checkbox' id='recesel' onclick='checkDate(this,this.value)'  name='recesel' value='" + record.data.id + "' />");
	}
    function showPrintPage1(claimId){ 
        
        var printUrl = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/barcodePrintDoGet.do?dtlIds="+claimId;
        window.open(printUrl,'','toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=1200'); 

      }
    function showPrintPage2(claimId,claimType){ 
             var tarUrl = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillForwardPrint.do?dtlIds="+claimId+"&claimType="+claimType;
	    	window.open(tarUrl,'','left=0,top=0,width='+screen.availWidth +'- 10,height='+screen.availHeight+'-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
      }
	      
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

		if(screenW!=null && screenW!='undefined')
			width = screenW;
		if(screenH!=null && screenH!='undefined')
			height = screenH;
		var isImport = record.data.isImport;
		if(isImport==<%=Constant.IF_TYPE_YES%>){
		 return String.format("["+value+"]");
		}else{
        return String.format(
               "<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillDetailForward.do?roNo="+value+"&ID="+record.data.id+"\","+1000+","+height+")'>["+value+"]</a>");
    }
    }
    
    function myLink1(value,meta,record) {
    	//MyAlert(screen.availWidth);
    	var currStatus = record.data.status;
    	var roNo = record.data.roNo;
    	var claimType = record.data.claimType;
    	  var   MonthFirstDay=new   Date(2012,4,1);     
    	var isImport = record.data.isImport;
    	if(isImport==<%=Constant.IF_TYPE_YES%>){
    		var res = "<a href=\"#\" onclick=\"showPrintPage1('"+value+"')\">[标签]</a>"
    			
   		}else{
    			var res = "<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillDetailForward.do?roNo="+roNo+"&ID="
				+ value + "\",1000,500)'>[索赔单明细]</a>";
    			res = res + "|<a href=\"#\" onclick=\"showPrintPage1('"+value+"')\">[标签]</a>"
    			if(currStatus!=10791001  ){//&&claimType!=10661006
    			 res=res + "|<a href=\"#\" onclick=\"showPrintPage2('"+value+"',"+claimType+")\">[结算单打印]</a>"
    			}
   		}
    		return res;
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
	
function clearClaimInput(){
	$('CLAIM_DESC').value="";
	$('CLAIM_TYPE').value="";
}
//设置超链接 end
</SCRIPT>

</HEAD>
<BODY>

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;经销商索赔管理&gt;索赔单状态跟踪<span style="color: red;font-weight: bold;">(只能查询2015-5-25之前的数据)</span></div>
    <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
          <tr>
            <td  class="table_query_2Col_label_6Letter">索赔单号：</td>
            <td><input name="CLAIM_NO" id="CLAIM_NO" value="" maxlength="20" type="text"  class="middle_txt" />
            </td>
            <td  class="table_query_2Col_label_6Letter">工单号：</td>
            <td  align="left" ><input type="text" name="RO_NO" id="RO_NO" maxlength="20"  value="" class="middle_txt"/>
			<INPUT name="LINE_NO" id="LINE_NO" value="" type="hidden" datatype="1,is_digit"  class="mini_txt"/></td>	            
          </tr>
          <tr>
            <td  class="table_query_2Col_label_6Letter">索赔类型：</td>
            <td >
<!-- 			<script type="text/javascript"> -->
<%-- 	              genSelBoxExp("CLAIM_TYPE",<%=Constant.CLA_TYPE%>,"",true,"short_sel","","false",''); --%>
<!-- 	       </script> -->
				<input class="middle_txt" id="CLAIM_DESC"  name="CLAIM_DESC" type="text" readonly="readonly"/>
				<input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showTcCode('CLAIM_TYPE','CLAIM_DESC','<%=Constant.CLA_TYPE%>','');" value="..." />        
	            <input name="clrBtn" type="button" class="normal_btn" onclick="clearClaimInput();" value="清除"/>
				<input class="middle_txt" id="CLAIM_TYPE"  name="CLAIM_TYPE" type="hidden" />
 		    </td> 	         
 			<td  class="table_query_2Col_label_6Letter">VIN：</td>
 			<td  align="left" >
 			<!-- <input name="VIN" id="VIN" type="text" datatype="1,is_vin" value="" class="short_txt"/>-->
 			<textarea name="VIN" cols="18" rows="3" ></textarea>
 			</td>
          </tr>       
          <tr>
            <td  class="table_query_2Col_label_6Letter">零件代码：</td>
            <td  align="left" ><input type="text" name="partCode" id="partCode"  maxlength="30"  value="" class="middle_txt"/></td>	    
			<td  class="table_query_2Col_label_6Letter">作业代码：</td>   
			<td><input type="text" name="wrLabourCode" id="wrLabourCode"   value="" maxlength="30" class="middle_txt"/></td>      
 	
          </tr>            
          <tr>
             <td class="table_query_2Col_label_6Letter" >建单时间：</td>
             <td align="left" nowrap="true">
			<input name="RO_STARTDATE" type="text" class="short_time_txt" id="RO_STARTDATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'RO_STARTDATE', false);" />  	
             &nbsp;至&nbsp; <input name="RO_ENDDATE" type="text" class="short_time_txt" id="RO_ENDDATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'RO_ENDDATE', false);" /> 
		</td>
            <td  class="table_query_2Col_label_6Letter">索赔单状态：</td>
  			<td >
  			<script type="text/javascript">
	              genSelBoxExp("STATUS",<%=Constant.CLAIM_APPLY_ORD_TYPE%>,"",true,"short_sel","","false",'<%=Constant.CLAIM_APPLY_ORD_TYPE_02%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_04%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_10%>');
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
			 <td align="right" >车型：</td>
				<td align="left">
					<select id="model" name="model" class="short_sel">
						<option value=''>-请选择-</option>
						<c:forEach var="mode" items="${modelList}">
							<option value="${mode.groupCode}" title="${mode.groupCode}">${mode.groupName}</option>
						</c:forEach>
					</select>
				</td>
			  <td  class="table_query_2Col_label_6Letter">是否导入：</td>
           		<td align="left" nowrap="true">
	    		<script type="text/javascript">
	    			genSelBoxExp("is_import",<%=Constant.IF_TYPE%>,"${back_type }",true,"short_sel","","false",'');
	    		</script>
		    	</td>
			</tr>
           <!-- 艾春 13.11.20 添加是否二级查询条件 -->
             <tr>
            <td colspan="4" align="center" nowrap>
            <input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="queryPer(1);setTotal();" />
            <input type="button"  name="BtnAdd" id="queryBtn"  value="标签打印"  class="normal_btn" onClick="print2()" >
            <input type="button"  name="button" id="queryBtn2"  value="导出"  class="normal_btn" onClick="queryPer(2);" >
						<a id ="count" style="color:red;">
						</a>&nbsp;&nbsp;&nbsp;&nbsp;
						<a id= "totalBalanceAmount" style="color:red;">
						</a>
					</td>
           </tr>
  </table>
  
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
<script type="text/javascript">

function setTotal(){
	makeNomalFormCall('<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/applicationQueryTotal.json',setTotalBack,'fm','');
}

function setTotalBack(json){
	var amount=json.totalBalanceAmount;
	if(amount==null||amount=='null'||amount==""){
	amount=0;
	}
	$('count').innerHTML = "总条数为："+json.count;
	$('totalBalanceAmount').innerHTML = "审批总金额数为："+amount+"元整";
}

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
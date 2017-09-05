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
<TITLE>索赔单废弃</TITLE>

<SCRIPT LANGUAGE="JavaScript">
var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/applicationDeleteQuery.json";
				
	var title = null;

	var columns = [
					{header: "工单号", width:'15%', dataIndex: 'roNo',renderer:myLink},
					{header: "索赔类型", width:'7%', dataIndex: 'claimType',renderer:getItemValue},
					{header: "索赔单号", width:'15%', dataIndex: 'claimNo'},
					{header: "结算基地", width:'7%', dataIndex: 'balanceYieldly',renderer:getItemValue},
					{header: "车系", width:'15%', dataIndex: 'seriesCode'},
					{header: "车型", width:'15%', dataIndex: 'modelCode'},
					{header: "送修人姓名", width:'15%', dataIndex: 'deliverer'},
					{header: "送修人电话", width:'15%', dataIndex: 'delivererPhone'},
					{header: "VIN", width:'15%', dataIndex: 'vin'},
					{header: "申请日期", width:'15%', dataIndex: 'createDate',renderer:formatDate},
					{header: "申请状态", width:'15%', dataIndex: 'status',renderer:getItemValue},
					{header:'审核时间',width:'10%',dataIndex:'auditingDate',renderer:formatDate2},
					{width:'5%',header: "操作",sortable: false,dataIndex: 'id',renderer:myLink1,align:'center'}
		      ];

    //显示打印页面
    function showPrintPage(claimId){
      var printUrl = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/printClaimPartLabel.do?id="+claimId;
      window.open(printUrl,'','toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=500'); 
    }
		      
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
	}
	//行号加工单号
	function roLine(value,metadata,record) {
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
		
        return String.format(
               "<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillDetailForward.do?roNo="+value+"&ID="+record.data.id+"\","+width+","+height+")'>["+value+"]</a>");
    }
    
    function myLink1(value,meta,record) {
    	var applicationDel =  record.data.applicationDel;
    	var status =  record.data.status;
    	var claimNo =  record.data.claimNo;
        //if(applicationDel=='<%=Constant.RO_APP_STATUS_03 %>' && (status=='<%=Constant.CLAIM_APPLY_ORD_TYPE_04.toString() %>' || status=='<%=Constant.CLAIM_APPLY_ORD_TYPE_05.toString() %>' || status=='<%=Constant.CLAIM_APPLY_ORD_TYPE_06.toString() %>')){
        	//return String.format("<input type=\"button\" value=\"申请废弃\" disabled='disabled' class=\"normal_btn\"/>");
        //}else if((status=='<%=Constant.CLAIM_APPLY_ORD_TYPE_04.toString() %>' || status=='<%=Constant.CLAIM_APPLY_ORD_TYPE_05.toString() %>' || status=='<%=Constant.CLAIM_APPLY_ORD_TYPE_06.toString() %>') && applicationDel!='<%=Constant.RO_APP_STATUS_03 %>'){
	    	//return String.format("<a href='<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillDetailDeleteForward.do?ID="+value+"'>[申请废弃]</a>");
        //}else{
        	return String.format("<a href='#' onclick=isCheck('"+value+"','"+claimNo+"')>[废弃]</a>");
        //}
    }

    function isCheck(id,claimNo){
        //var url="<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillDetailDeleteForward.do?ID="+value+"";
    	//var url="<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillDetailDeleteForward.json?ID="+id;
    	var url="<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/isCount.json?claimNo="+claimNo+"&ID="+id;
    	makeNomalFormCall(url,returnBack11,'fm','');
    }
    
	function returnBack11(json){
		var flag = json.flag;
		var id = json.id;
		var claimNo = json.claimNo;
		if(flag=='false'){
			MyAlert("此索赔单被生成了结算单,不能删除!");
		}else{
			location.href="<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillDetailDeleteForward.do?claimNo="+claimNo+"&ID="+id;
		}
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
	
//设置超链接 end
</SCRIPT>

</HEAD>
<BODY>

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;经销商索赔管理&gt;索赔单废弃</div>
    <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
          <tr>
            <td  class="table_query_2Col_label_6Letter">索赔单号：</td>
            <td><input  name="CLAIM_NO" id="CLAIM_NO" maxlength="25" value="" type="text"  class="middle_txt" />
            </td>
            <td  class="table_query_2Col_label_6Letter">工单号：</td>
            <td  align="left" ><input type="text" name="RO_NO" id="RO_NO"  maxlength="20"  value="" class="middle_txt"/>
			<INPUT name="LINE_NO" id="LINE_NO" value="" type="hidden" datatype="1,is_digit"  class="mini_txt"/></td>	            
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
             <td class="table_query_2Col_label_6Letter" >单据创建时间：</td>
            <td align="left" nowrap="true">
			<input name="RO_STARTDATE" type="text" class="short_time_txt" id="RO_STARTDATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'RO_STARTDATE', false);" />  	
             &nbsp;至&nbsp; <input name="RO_ENDDATE" type="text" class="short_time_txt" id="RO_ENDDATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'RO_ENDDATE', false);" /> 
		</td>
            <td colspan="2"></td>
  		
        
          </tr>
     	  <tr style="display: none">
            <td  class="table_query_2Col_label_6Letter">送修人姓名：</td>
            <td><input name="DELIVERER" id="DELIVERER" value="" type="text" maxlength="25"  class="middle_txt" />
            </td>
            <td  class="table_query_2Col_label_6Letter">送修人电话：</td>
            <td  align="left" ><input type="text" name="DELIVERER_PHONE" id="DELIVERER_PHONE" maxlength="18" value="" class="middle_txt"/> </td>
          </tr>
          
             <tr>
            <td colspan="4" align="center" nowrap><input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="queryPer();" />
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
</BODY>
<script type="text/javascript">
  function   showMonthFirstDay()     
  {     
	  var   Nowdate=new   Date();     
	  var   MonthFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth()-1,1);     
	  return DateUtil.Format("yyyy-MM-dd",MonthFirstDay);  
  }     
  function   showMonthLastDay()     
  {     
	  var   Nowdate=new   Date();     
	  var   MonthNextFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth()+1,1);     
	  var   MonthLastDay=new   Date(MonthNextFirstDay-86400000);     
	  return DateUtil.Format("yyyy-MM-dd",MonthLastDay);   
  }     
 // $('CREATE_DATE_STR').value=showMonthFirstDay();
  $('RO_STARTDATE').value=showMonthFirstDay();
  $('RO_ENDDATE').value=showMonthLastDay();
   function queryPer(){
	var star = $('RO_STARTDATE').value;
	var end = $('RO_ENDDATE').value;
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
</script>
</html>
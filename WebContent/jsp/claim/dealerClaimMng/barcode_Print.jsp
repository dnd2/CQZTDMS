<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="com.infodms.dms.po.TmBusinessAreaPO" %>
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
List <TmBusinessAreaPO> list = (List <TmBusinessAreaPO>)request.getAttribute("yieldly");
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>旧件标签批量打印</TITLE>

<SCRIPT LANGUAGE="JavaScript">


var sTheId = "${theId}" ;
aGroupDtlId = sTheId.split(",") ;
var aGroupDtlId = new Array() ;

var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/applicationQuery.json";
				
	var title = null;

	var columns = [
					{id:'check',header: "选择", width:'6%',sortable: false,dataIndex: 'ID',renderer:myCheckBox},
					{header: "工单号", width:'15%', dataIndex: 'roNo',renderer:myLink},
					{header: "索赔类型", width:'7%', dataIndex: 'claimType',renderer:getItemValue},
					{header: "索赔单号", width:'15%', dataIndex: 'claimNo'},
					{header: "车系", width:'15%', dataIndex: 'seriesName'},
					{header: "车型", width:'15%', dataIndex: 'model'},
					{header: "送修人姓名", width:'15%', dataIndex: 'deliverer'},
					{header: "送修人电话", width:'15%', dataIndex: 'delivererPhone'},
					{header: "VIN", width:'15%', dataIndex: 'vin'},
					{header: "结算基地", width:'7%', dataIndex: 'balanceYieldly',renderer:getItemValue},
					{header: "生产基地", width:'15%', dataIndex: 'yieldlyName'},
					{header: "申请日期", width:'15%', dataIndex: 'createDate',renderer:formatDate},
					{header: "申请状态", width:'15%', dataIndex: 'status',renderer:getItemValue},
					{header: '索赔总金额',dataIndex:'repairTotal',renderer:amountFormat},
					{header:'索赔结算时间',width:'10%',dataIndex:'accountDate',renderer:formatDate2},
					{width:'5%',header: "打印",sortable: false,dataIndex: 'id',renderer:myLink1,align:'center'}
		      ];



	function myCheckBox(value,metaDate,record){
		var theValue = value  ;
		return String.format("<input type=\"checkbox\" name='theIds' value='" + theValue + "' onclick='chkChg(this);' />");
	}
	function chkChg(obj) {
		 var iLen = aGroupDtlId.length ;
		 
		 var aDtl = obj.value.split(",") ;
		 var sTheId = aDtl[0] ;
		
		 
		 if(obj.checked) {
			 aGroupDtlId.push(sTheId) ;
		 } else {
			 for(var i=0; i<iLen; i++) {
				 if(aGroupDtlId[i] == sTheId) {
					 aGroupDtlId.splice(i, 1) ;			
				 }
			 }
		 }
	}

	
    //显示打印页面
    function showPrintPage(claimId){

     // var printUrl = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/printClaimPartLabel.do?id="+claimId;
     // window.open(printUrl,'','toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=500'); 
 var printUrl = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/barcodePrintDoGet.do?dtlIds="+claimId;
        window.open(printUrl,'','toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=500'); 

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
		
        return String.format(
               "<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillDetailForward.do?ID="+record.data.ID+"\","+width+","+height+")'>["+value+"]</a>");
    }
    
    function myLink1(value,meta,record) {
    	//MyAlert(screen.availWidth);
    	var currStatus = record.data.status;

    	var res = "";
		res = res + "<a href=\"#\" onclick=\"showPrintPage('"+value+"')\">[打印配件标签]</a>"
		
    	if(currStatus=='<%=Constant.CLAIM_APPLY_ORD_TYPE_04%>'){ 
    		return res;
    	}else if(currStatus=='<%=Constant.CLAIM_APPLY_ORD_TYPE_07%>'){
    		return res;
    	}else if(currStatus=='<%=Constant.CLAIM_APPLY_ORD_TYPE_08%>'){
    		return res;
    	}else {
    		return "";
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

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;经销商索赔管理&gt;旧件批量打印</div>
    <form method="post" name ="fm" id="fm">
    <input type="hidden" name="dtlIds" id="dtlIds" />
   <TABLE  class="table_query">
          <tr>
            <td  class="table_query_2Col_label_6Letter">索赔单号：</td>
            <td><input name="CLAIM_NO" id="CLAIM_NO" value="" maxlength="20" type="text" datatype="1,is_digit_letter,25"  class="middle_txt" />
            </td>
            <td  class="table_query_2Col_label_6Letter">工单号：</td>
            <td  align="left" ><input type="text" name="RO_NO" id="RO_NO" maxlength="20" datatype="1,is_digit_letter,30" value="" class="middle_txt"/>
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
             <td class="table_query_2Col_label_6Letter" >申请时间：</td>
             <td nowrap="nowrap">
            <input type="text" class="short_txt" readonly="readonly" name="RO_STARTDATE" id="RO_STARTDATE"  datatype="1,is_date,10" group="RO_STARTDATE,RO_ENDDATE" hasbtn="true" callFunction="showcalendar(event, 'RO_STARTDATE', false);"/>
            至
            <input type="text" class="short_txt" readonly="readonly" name="RO_ENDDATE" id="RO_ENDDATE"  datatype="1,is_date,10" group="RO_STARTDATE,RO_ENDDATE" hasbtn="true" callFunction="showcalendar(event, 'RO_ENDDATE', false);"/>
  			</td>
            <td  class="table_query_2Col_label_6Letter">索赔单状态：</td>
  			<td >
  			<script type="text/javascript">
	              genSelBoxExp("STATUS",<%=Constant.CLAIM_APPLY_ORD_TYPE%>,"",true,"short_sel","","false",'<%=Constant.CLAIM_APPLY_ORD_TYPE_02%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_03%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_04%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_10%>');
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
             <td>
           		 <input type="text" readonly="readonly" class="short_txt" name="balance_approve_date" id="balance_approve_date"  datatype="1,is_date,10" group="balance_approve_date,balance_approve_date2" hasbtn="true" callFunction="showcalendar(event, 'balance_approve_date', false);"/>
                             至
                 <input type="text" readonly="readonly" class="short_txt" name="balance_approve_date2" id="balance_approve_date2"  datatype="1,is_date,10" group="balance_approve_date,balance_approve_date2" hasbtn="true" callFunction="showcalendar(event, 'balance_approve_date2', false);"/>
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
         
             <tr>
            <td colspan="4" align="center" nowrap><input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="__extQuery__(1);" />
			&nbsp;<input id="queryBtn" class="normal_btn" type="button" name="button" value="打印"  onClick="print();" />
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

function print(){
	if(aGroupDtlId) {
		var iLen = aGroupDtlId.length ;
		if(aGroupDtlId.length>10){
			MyAlert("最多只能选择10张索赔单！") ;
			return false ;
			}
		if(!iLen) {
			MyAlert("请选择索赔单！") ;
			
			return false ;
		} 
	} else {
		MyAlert("请选择索赔单！") ;
		
		return false ;
	}
	MyConfirm("确认打印？", addSure) ;
}


function addSure() {
	 	document.getElementById("dtlIds").value = aGroupDtlId.join() ;
		 document.forms[0].target="_blank";
 		document.forms[0].method="post";
		document.forms[0].action="<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillTrack/barcodePrintDoGet.do";
		document.forms[0].submit();
}

</script>
</BODY>
</html>
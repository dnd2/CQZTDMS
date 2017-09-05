<%-- 创建时间 : 2010.06.03
             功能描述：索赔申请上报功能首页，用于查询和上报索赔申请单。 
--%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>索赔申请上报</title>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
    function doInit()
	{
	   loadcalendar();
	}
</script>
</head>
<body onload="doInit();">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
              当前位置： 售后服务管理&gt;经销商索赔管理&gt;索赔单上报
  </div>
  <form method="post" id="fm" name="fm">
  <input type="hidden" name="dealerLevel" id="dealerLevel" value="${dealerLevel }"/>
    <TABLE align="center" class="table_query" >
          <tr>
            <td align="right" nowrap="true">索赔单号：</td>
            <td>
            	<input name="CLAIM_NO" value=""  maxlength="21" type="text" class="middle_txt">
            </td>
            <td align="right" nowrap="true">工单号：</td>
            <td align="left">
              <input type="text" name="RO_NO" maxlength="20" value="" class="middle_txt"/>
              <input name="LINE_NO" value="" type="hidden"   class="mini_txt" >
            </td>	            
          </tr>
          <tr>
            <td align="right" nowrap="true">索赔类型：</td>
            <td>
				<script type="text/javascript">
				    genSelBoxExp("CLAIM_TYPE",<%=Constant.CLA_TYPE%>,"",true,"short_sel","","false",'');
				</script>
 		    </td> 	
 		    <td align="right" nowrap="true" rowspan="2">VIN：</td>
 			<td align="left" rowspan="2">
 			    <textarea name="VIN" rows="3" cols="18"></textarea>
 			    <!-- 只有未上报的索赔申请单可以上报 -->
 				<input type="hidden" name="CLAIM_STATUS" id="CLAIM_STATUS" value="<%=Constant.CLAIM_APPLY_ORD_TYPE_01%>"/>
 			</td>         
          </tr>                   
          <tr>
            <td align="right" nowrap="true" >创建时间： </td>
             <td align="left" nowrap="true">
			<input name="APPLY_DATE_START" type="text" class="short_time_txt" id="APPLY_DATE_START" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'APPLY_DATE_START', false);" />  	
             &nbsp;至&nbsp; <input name="APPLY_DATE_END" type="text" class="short_time_txt" id="APPLY_DATE_END" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'APPLY_DATE_END', false);" /> 
		</td>
           
          </tr>
    	  <tr>
            <td align="center" colspan="4" nowrap="true">
            	<input class="normal_btn" type="button" name="queryBtn1" id="queryBtn" value="查询"  onClick="queryPer();" >
				<input name="report" type="button" id="beanchQuery" class="normal_btn"  value="批量上报" onclick="beachReport();"/>
			</td>
		</tr>
  </table>
  <script type="text/javascript" >
		var myPage;
		var url = "<%=contextPath%>/claim/dealerClaimMng/DealerClaimReport/dealerClaimReportQuery.json";
		var title = null;
		
		var columns = [
					{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"recesel\")' />全选", align:'center',sortable:false, dataIndex:'ID',width:'2%',renderer:checkBoxShow},
					{header: "索赔申请单号",sortable: false,dataIndex: 'CLAIM_NO',align:'center',renderer:claim_no},
					//{header: "经销商代码",sortable: false,dataIndex: 'DEALER_CODE',align:'center'},
					//{header: "经销商名称",sortable: false,dataIndex: 'DEALER_NAME',align:'center'},				
					{id:'action', header: "工单号",dataIndex: 'RO_NO'},
					{header: "索赔类型",sortable: false,dataIndex: 'CLAIM_TYPE',align:'center',renderer:getItemValue}, 
					{header: "结算基地", width:'7%', dataIndex: 'BALANCE_YIELDLY',renderer:getItemValue},
					{header: "修改次数",sortable: false,dataIndex: 'SUBMIT_TIMES',align:'center'},
					{header: "VIN",sortable: false,dataIndex: 'VIN',align:'center'},
					{header: "创建日期",sortable: false,dataIndex: 'CREATE_DATE',align:'center'},
					{header: "申请状态",sortable: false,dataIndex: 'STATUS',align:'center',renderer:getItemValue},
					{id:'action',header: "操作",dataIndex: 'ID',renderer:claimReport}
			      ];
	      
		//需要挂接索赔申请状态查询的明细查询功能
		function claimDetail(value,meta,record){
			var width=800;
			var height=500;

			var screenW = window.screen.width-30;	
			var screenH = document.viewport.getHeight();

			if(screenW!=null && screenW!='undefined')
				width = screenW;
			if(screenH!=null && screenH!='undefined')
				height = screenH;
			var CLAIM_NO = record.data.CLAIM_NO;
		    return String.format("<a href='#' onclick=\"OpenHtmlWindow('<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail.do?CLAIM_NO="+CLAIM_NO+ "&roNo=" +value+ "&ID="
					+ record.data.REDID + "',"+1000+","+height+")\">[" + value + "]</a>");
	    }   
	    
	    function claim_no(value,meta,record){
	    	var roNo = record.data.RO_NO;
			var res = "<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillDetailForward.do?roNo="+roNo+ "&ID="
				+ record.data.ID + "\",1000,500)'>["+value+"]</a>";
				return String.format(res);
	    }   
	//设置复选框
function checkBoxShow(value,meta,record){
  
      return String.format("<input type='checkbox' id='recesel' name='recesel' value='" + value+ "' />");
     
 
	
}

function claimReport(value,meta,record){
		    var money = record.data.BALANCE_AMOUNT;
		    var type = record.data.CLAIM_TYPE;
		    var claimNo = record.data.CLAIM_NO;
		    var str = "<a href=\"#\" id=portId"+record.data.ID+" onclick=confirm("+value+","+money+","+type+",this) >[上报]</a>";
		    if(record.data.PRINT==1){
		    	str+="<a href=\"#\" onclick=print("+record.data.ID+","+claimNo+"); >[打印]</a>";
		    }
		 //   if(record.data.REMAKE != null )
		  //  {
		     // str = record.data.REMAKE;
		    //}
		    return String.format(str);
	    }
	    
	    //打印跳转
 function print(id,claimNo){
	var tarUrl = "<%=contextPath%>/claim/dealerClaimMng/DealerClaimReport/dealerClaimReportPrint.do?claimNo="+claimNo+"&id="+id;
	window.open(tarUrl,'','left=0,top=0,width='+screen.availWidth +'- 10,height='+screen.availHeight+'-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');

	   }

	    function confirm(value,money,type,aobj) {
	    var dealerLevel = document.getElementById("dealerLevel").value;
		if(dealerLevel=="10851002"){
			MyAlert("二级经销商不能上报索赔单!");
			return false;
		}
		  	makeNomalFormCall('<%=contextPath%>/claim/dealerClaimMng/DealerClaimReport/reportClaimCheck.json?ID='+ value,checkResult,'fm');
		}

	    //上报索赔申请单
//function reportClaim(value,aobj){
//			disableLink(aobj);
	//		makeNomalFormCall('<%=contextPath%>/claim/dealerClaimMng/DealerClaimReport/reportClaim.json?ID='+ value,showResult,'fm');
	//	}
	    //批量上报索赔申请单
function changeSubmit(value){
	$('beanchQuery').disabled=true;
	$('queryBtn').disabled=true;
	var str = value.split(",");
	for(var i=0;i<str.length;i++){
		$('portId'+str[i]).disabled=true;
	}
			makeNomalFormCall('<%=contextPath%>/claim/dealerClaimMng/DealerClaimReport/reportClaimSubmmit.json?ID='+ value,showResult,'fm');
		}
function checkResult(json){
	var str = json.str;
	var ids = json.ID;
	if(str!=""){
		MyAlert(str);
	}else{
	MyConfirm("确认上报?",changeSubmit,[ids]);
	}
}
function beachReport(){
		var allChecks = document.getElementsByName("recesel");
		var dealerLevel = document.getElementById("dealerLevel").value;
		if(dealerLevel=="10851002"){
			MyAlert("二级经销商不能上报索赔单!");
			return false;
		}
	var allFlag = false;
	var ids = "";
	for(var i = 0;i<allChecks.length;i++){
		if(allChecks[i].checked){
			allFlag = true;
			ids = allChecks[i].value+","+ids;
		}
	}
	if(ids!=""){
	ids=ids.substring(0,ids.length-1);
	}
	if(allFlag){
		makeNomalFormCall('<%=contextPath%>/claim/dealerClaimMng/DealerClaimReport/reportClaimCheck.json?ID='+ ids,checkResult,'fm');
	}else{
		MyAlert("请选择数据后再点击操作批量上报按钮！");
	}
	}
function showResult(json){
			if(json.ACTION_RESULT == '1'){
					MyAlert("上报成功!");
					$('beanchQuery').disabled=false;
					$('queryBtn').disabled=false;
				 __extQuery__(1);
			}
		}

		function disableLink(obj){
			obj.disabled = true;
			obj.style.border = '1px solid #999';
			obj.style.background = '#EEE';
			obj.style.color = '#999';
		}
  </script>
</form>
<!--分页  -->
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</body>
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
  $('APPLY_DATE_START').value=showMonthFirstDay();
  $('APPLY_DATE_END').value=showMonthLastDay();
  function queryPer(){
	var star = $('APPLY_DATE_START').value;
	var end = $('APPLY_DATE_END').value;
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
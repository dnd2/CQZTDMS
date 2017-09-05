<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>
<head>
<%
String contextPath=request.getContextPath();
ActionContext act = ActionContext.getContext();
AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);   
String  dutyType = logonUser.getDutyType();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 需求预测调整 </title>
<script language="JavaScript" src="<%=contextPath %>/js/ut.js"></script>

<script language="JavaScript">
		//定义全局变量接收返回回来的数据
		var dutyType = <%=dutyType%>;
		var returnDataList=null;
		var returnDataList1=null;
		var countTotal=0;
		var groupCodes=new Array();
       function executeQueryOrg(){
            var orgCode = document.getElementById('orgCode').value ;
    	    document.getElementById("tb2").style.display='none';
       		document.getElementById("btns").style.display="none";
		    var areaId = document.getElementById("areaId").value;	    
			var url = "<%=contextPath%>/sales/planmanage/RequirementForecast/RequireForecastManage/requireForecastReportModelSearch.json";
			makeCall(url,showJson,{areaId:areaId});
			
       }
        function executeQueryDealer(){
            var dealerCode = document.getElementById('dealerCode').value;       
    	    document.getElementById("tb2").style.display='none';
       		document.getElementById("btns").style.display="none";
		    var areaId = document.getElementById("areaId").value;  
			var url = "<%=contextPath%>/sales/planmanage/RequirementForecast/RequireForecastManage/requireForecastReportModelSearch.json";
            makeCall(url,showJson,{areaId:areaId,dealerCode:dealerCode});		   
       }
       
       //构建页面
		function showJson(json){
			var ymlist=json.mapList;
			var infolist=json.infoList;
			//清空数据
			var tbody=document.getElementById("tbody1");
			clrTbody(tbody);
			//如果没有返回结果，显示无数据提示页
			if(ymlist.length==0||null==infolist){
				showNoRsTable();
				return;
			}
			//把ymlist的值赋值给全局变量-yinsh
			returnDataList=infolist;
			returnDataList1=ymlist;

			//已上报,等待审核 rejectId
			if(infolist.length == 1 && infolist[0].SERIES_NAME == '已上报,等待审核'){
				hiddenRejectId();
			}
			//			
			
			var thtr=document.createElement("tr");
			var thtr1=document.createElement("tr");
			var scth=document.createElement("th");
			scth.rowSpan=2;
			scth.appendChild(document.createTextNode("车系代码"));
			var snth=document.createElement("th");
			snth.rowSpan=2;
			snth.appendChild(document.createTextNode("车系名称"));
			var mcth=document.createElement("th");
			mcth.rowSpan=2;
			mcth.appendChild(document.createTextNode("车型代码"));
			var mnth=document.createElement("th");
			mnth.rowSpan=2;
			mnth.appendChild(document.createTextNode("车型名称"));
			thtr.appendChild(scth);
			thtr.appendChild(snth);
			thtr.appendChild(mcth);
			thtr.appendChild(mnth);
			for(var i=0;i<ymlist.length;i++){
				var th=document.createElement("th");
				th.colSpan=2;
				th.align="center";
				th.appendChild(document.createTextNode(ymlist[i].YEAR+'-'+ymlist[i].MONTH));
				thtr.appendChild(th);
				var th1=document.createElement("th");
				var th2=document.createElement("th");
				th1.appendChild(document.createTextNode("下级汇总"));
				th2.appendChild(document.createTextNode("预测数量"));
				thtr1.appendChild(th1);
				thtr1.appendChild(th2);
			}
			var opth=document.createElement("th");
			opth.rowSpan=2;
			opth.appendChild(document.createTextNode("操作"));
			thtr.appendChild(opth);
			tbody.appendChild(thtr);
			tbody.appendChild(thtr1);
			
			for(var i=0;i<infolist.length;i++){
       			var tdtr=document.createElement("tr");
       			var trclass;
       			if(i%2!=0){
       				tdtr.className="table_list_row1";
       			}else{
       				tdtr.className="table_list_row2";
       			}
       			var sctd=document.createElement("td");
       			sctd.appendChild(document.createTextNode(infolist[i].SERIES_CODE));
       			var sntd=document.createElement("td");
       			sntd.appendChild(document.createTextNode(infolist[i].SERIES_NAME));
       			var mctd=document.createElement("td");
       			mctd.appendChild(document.createTextNode(infolist[i].MODEL_CODE));
       			var mntd=document.createElement("td");
       			mntd.appendChild(document.createTextNode(infolist[i].MODEL_NAME));
       			tdtr.appendChild(sctd);
       			tdtr.appendChild(sntd);
       			tdtr.appendChild(mctd);
       			tdtr.appendChild(mntd);
       		    for(var j=0;j<ymlist.length;j++){
       		    	var td1=document.createElement("td");
       		    	td1.appendChild(document.createTextNode(infolist[i]['D'+j]));
       		    	var td2=document.createElement("td");
       		    	td2.appendChild(document.createTextNode(infolist[i]['S'+j]));
       		    	tdtr.appendChild(td1);
       		    	tdtr.appendChild(td2);
       		    }
       		    var optd=document.createElement("td");
       		    var ipt=document.createElement("a");
				ipt.href='#';
				ipt.name=infolist[i].GROUP_ID;
				if(ipt.name)
				ipt.innerHTML="[调整]";
				ipt.onclick= function(){
					document.getElementById("modelId").value = this.name;
					//if(null == $('#modelId').value || '' == $('#modelId').value){						
					if(null == this.name || '' == this.name){
					  MyAlert('非法操作！');
					}
					else 
					{
					  var url ="<%=contextPath%>/sales/planmanage/RequirementForecast/RequireForecastManage/requireForecastSearch.do";
		       	      form1.action=url;
		       	      form1.method='post';
		       	      form1.submit();
					}	       	    
				};
				optd.appendChild(ipt);
				tdtr.appendChild(optd);
       		    tbody.appendChild(tdtr);
       		}
       		document.getElementById("btns").style.display="inline";
       		document.getElementById("area").value=json.areaId;
		}
      
        function clrTbody(obj){
	        for(var i=0,len = obj.rows.length;i<len;i++){
               obj.deleteRow(0);
			}
	    }
       function showNoRsTable(){
       		document.getElementById("tb2").style.display='inline';
       }
       function hiddenRejectId(){
       		document.getElementById("rejectId").style.display='none';
       }        
        function isReport(){
        	
      	 MyConfirm("是否确认上报信息?",reportSubmit);
       }
       function reportSubmit(){
       	   var url ="<%=contextPath%>/sales/planmanage/RequirementForecast/RequireForecastManage/requireForecastReport.do";
       	   form1.action=url;
       	   form1.method='post';
       	   form1.submit();
       }
       function showDealerUnreport(){
           var areaId=document.getElementById("area").value;
           OpenHtmlWindow("<%=contextPath%>/sales/planmanage/RequirementForecast/RequireForecastManage/unreportForwardAction.do?area="+areaId,800,500);      
       }
       
       function showOrgUnreport(){
           OpenHtmlWindow("<%=contextPath%>/sales/planmanage/RequirementForecast/RequireForecastManage/unreportForwardAction.do?org=1",800,500);      
       }
       
       function showDealerDetail(){
           OpenHtmlWindow("<%=contextPath%>/sales/planmanage/RequirementForecast/RequireForecastManage/dealerDetail.do?org=2",800,500);
       
       }
       //查看大区明细
       function showAreaDetail(){
           var areaId=document.getElementById("area").value;
           OpenHtmlWindow("<%=contextPath%>/sales/planmanage/RequirementForecast/RequireForecastManage/areaDetail.do?area="+areaId,800,500);
       
       }
        function showLessreport(){
           var areaId=document.getElementById("area").value;
           OpenHtmlWindow("<%=contextPath%>/sales/planmanage/RequirementForecast/RequireForecastManage/lessReportForwardAction.do?area="+areaId,800,500);
       
       }
        function isRebut(){
      	 MyConfirm("是否确认驳回信息?",OEM_REBUT);
       }
        function OEM_REBUT(){
        var orgCode=document.getElementById("orgCode").value;
        if(null == orgCode || '' == orgCode) {
            MyAlert('请选择要驳回的大区！');     
        }else{
            var areaId=document.getElementById("areaId").value.split(",")[0];
            var url = "<%=contextPath%>/sales/planmanage/RequirementForecast/RequireForecastManage/OemRebut.json";
            makeCall(url,showOemRebut,{orgCode:orgCode,areaId:areaId});  
           }
       }
       function showOemRebut(json){
        var flag = json.flag ;		
        var duty=document.getElementById("logonDuty").value;
		if(flag == "0") {
		    MyAlert("驳回失败！") ; 	
		} else {
			if(duty==10431001){
				executeQueryOrg();
			}else if(duty==10431003){
				executeQueryDealer();
			}
			//loadData();
			MyAlert("驳回成功！") ;
		}
      }
       function isRebutDealer(){
      	 MyConfirm("是否确认驳回经销商信息?",ORG_REBUT);
       }
       function ORG_REBUT(){
        var dealerCode = document.getElementById("dealerCode").value;
        if(null == dealerCode|| '' == dealerCode) {
            MyAlert('请选择要驳回的经销商！');     
        }else{
            var areaId=document.getElementById("areaId").value.split(",")[0];
            var url = "<%=contextPath%>/sales/planmanage/RequirementForecast/RequireForecastManage/OrgRebut.json";
            makeCall(url,showOemRebut,{dealerCode:dealerCode,areaId:areaId});  
           }
       }
       function showDealerRebut(json){
        var flag = json.flag ;	
		if(flag == "0") {
		    MyAlert("驳回失败或已经驳回！") ; 	
		} else {			
			loadData();
			MyAlert("驳回成功！") ;
		}
      }
      function clrTxt(id){
       document.getElementById(id).value = '';
     }
     //start yinshunhui
    function loadData(){
    	//$("queryBtn").click();
      if(10431001==dutyType){
   		  executeQueryOrg();
   	  }
   	  if(10431003==dutyType){
   		  executeQueryDealer();
   	  }
  }
     //end
     //上报之前验证用户是否已经调整数据
     function beforeIsReport(){
     	 var url = "<%=contextPath%>/sales/planmanage/RequirementForecast/RequireForecastManage/checkDataIsModify.json";
     	 var returnData=new Array();
     	 var returnData1=new Array();
     	 for(var i=0;i<returnDataList.length;i++){
     	 	returnData[i]=returnDataList[i].MODEL_CODE;
     	 	for(var j=0;j<returnDataList1.length;j++){
     	 		returnData1[i]=returnDataList[i]["D"+j];
     	 	}
     	 	
     	 }
     	 makeCall(url,checkDataIsModifyReturn,{dataList:returnData,dataList1:returnData1}); 
     }
     function checkDataIsModifyReturn(json){
     	 countTotal=json.data[0].count;
     	groupCodes=json.data[0].dataSet;
     	if(countTotal==0){
     		flag=true;
     	}else if(countTotal==1){
     		str=changeArryToString(groupCodes)
     		if(str!=''){
     			MyAlert("有"+str+"没有调整,请调整后上报！！！");
     			flag=false;
     		}else{
     		flag=true;
     		}
     		
     	}else{
     		str=changeArryToString(groupCodes);
     		MyAlert("有"+str+"数据没有调整,请调整后上报！！！");
     		flag=false;
     	}
     	if(flag){
     		isReport();
     	}
     	
     
     }
     function changeArryToString(groupCodes){
    	 var str='';
     	if(groupCodes==''){
     		return str;
     	}
     	
     	for(var i=0;i<groupCodes.length;i++){
     		if(groupCodes[i]==null||groupCodes[i]==''){
     			continue;
     		}
     		str+=groupCodes[i]+",";
     	}
     	if(str!=''){
     		return str.substring(0,str.length-1);
     	}else{
     		return str;
     	}
     	
     }
     
     function showInfo(code,name){    
    	 document.getElementById('orgCode').value = code;
     }
     
     /*
      * 选择大区
      * */
     function showOrgLargerEgion( )
     {
     	
     	OpenHtmlWindow(g_webAppName+'/dialog/showOrgLargerEgion.jsp?haveOwn=0',800,500);
     }
     
     
</script>
</head>

<body onload = "loadData()">
	<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>需求预测> 需求预测调整
	</div>
<form name="fm" method="post" id="fm">
<input type="hidden" id="logonDuty" name="logonDuty" value="${LOGON_USER.dutyType}" />
<div class="form-panel">
	<h2>需求预测调整</h2>
	<div class="form-body">	
			
	<table class="table_query" id="subtab" >
	  <tr class="csstr" align="center">
	    <td style="display: none;"> 请选择业务范围：
	      <select name="areaId" id="areaId" onchange="executeQueryOpe();">
		       <c:forEach items="${areaBusList}" var="areaBusList" >
		       		<c:if test="${areaId == areaBusList.AREA_ID}">
		       			<option selected="selected" value="${areaBusList.AREA_ID },${areaBusList.DEALER_ID }">${areaBusList.AREA_NAME }</option>
		       		</c:if>
		       		<c:if test="${areaId != areaBusList.AREA_ID}">
		       			<option value="${areaBusList.AREA_ID },${areaBusList.DEALER_ID }">${areaBusList.AREA_NAME }</option>
		       		</c:if>
			   </c:forEach>
	      </select>
	    </td>
	       <td id="rejectId" style="display: inline">
	       <c:if test="${LOGON_USER.dutyType eq 10431001 }">
	          选择驳回区域:
	            <input type="text" class="middle_txt" id="orgCode" class="middle_txt" name="orgCode" value="" size="15" onclick="showOrgLargerEgion();"  readonly="readonly"/>
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('orgCode');"/>
				<input type="button"  class="u-button u-reset"  value="驳回大区" onclick="isRebut();" />
			</c:if>
			<c:if test="${LOGON_USER.dutyType eq 10431003 }">
			选择驳回经销商：
				<input type="text"  name="dealerCode" class="middle_txt" size="15"  id="dealerCode"  onclick="showOrgDealer('dealerCode', 'dealerId', 'true', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', 'dealerName');" readonly="readonly"/>
	            <input type="hidden"  name="dealerId" size="15"  id="dealerId" readonly="readonly"/>
	            <input type="hidden"  name="dealerName" size="15"  id="dealerName" readonly="readonly"/>
	        	<input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCode');"/>
				<input type="button"  class="u-button u-reset"  value="驳回经销商" onclick="isRebutDealer();" />
			</c:if>	
			 </td> 
	  </tr>  
	  <tr align="center">
	  <td colspan="2" align="center">
	       <c:if test="${LOGON_USER.dutyType eq 10431001 }">
	    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="executeQueryOrg();" />   	
	    	</c:if>
	    	<c:if test="${LOGON_USER.dutyType eq 10431003 }">
	    	  <input type="button" id="queryBtn" class="u-button u-query"  value="查询" onclick="executeQueryDealer();" />
	    	</c:if>
	    </td>
	  </tr>
	</table>
  </div>
 </div>
</form>
<form name="form1" id="form1">
	<table style="display: none">
		<tr>
			<td align="center">
				<input type="hidden" name="modelId" id="modelId" value="" />
			    <input type="hidden" name="area" id="area" value="" />
	     	</td>
		</tr>
	</table>
</form>
</div>

<table class="table_list" id="tb1" >
<tbody id="tbody1"></tbody>
</table>
<table class=table_query style="display:none" id="btns">
  <tr>
     <td>
	     <input name="button"  type="button" class="u-button u-query" onclick="beforeIsReport();" value="上报/完成" />	     	     
	    <c:if test="${LOGON_USER.dutyType eq 10431003 }">
	      <input name="button3" type="button" class="u-button u-reset" onclick="showDealerUnreport();" value="未提报经销商名单" />
	      <input type="button"  class="u-button u-submit"  value="查看经销商明细" onclick="showDealerDetail();" />
	    </c:if>
	    <c:if test="${LOGON_USER.dutyType eq 10431001 }">
	      <input name="button3" type="button" class="u-button u-reset" onclick="showOrgUnreport();" value="未提报大区名单" />
	      <input type="button"  class="u-button u-submit"  value="查看大区明细" onclick="showAreaDetail();" />
	    </c:if>
     </td>
  </tr>
</table>
<table class="table_query" id="tb2" style="display:none">
	<tr>
		<td align="center"><font color="red">没有满足条件的数据</font></td>
	</tr>
</table>
</body>
</html>

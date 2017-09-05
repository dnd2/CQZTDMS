<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
	String info = (String)request.getAttribute("DataLonger");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>客户信息查询</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
	function AlertExcelInfo(){
		var info = '<%=info%>';
		if(info !=null && info!="" && info != "null"){
			MyAlert(info);
		}
	}
</script>
</head>
<body onload="AlertExcelInfo()">
	
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 客户管理 &gt;客户信息查询</div>
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />客户信息查询</th>
			
			<tr>
				<td align="right" nowrap="true">购买日期：</td>
				<td align="left" nowrap="true">
					<input type="text" name="purchasedDateStart" id="purchasedDateStart" readonly="readonly"
		             value="" type="text" class="short_txt" 
		             datatype="1,is_date,10" group="purchasedDateStart,purchasedDateEnd" 
		             hasbtn="true" callFunction="showcalendar(event, 'purchasedDateStart', false);" maxlength="10"/>
		           	  至
		           	<input type="text" name="purchasedDateEnd" id="purchasedDateEnd" readonly="readonly"
		             value="" type="text" class="short_txt" 
		             datatype="1,is_date,10" group="purchasedDateStart,purchasedDateEnd" 
		             hasbtn="true" callFunction="showcalendar(event, 'purchasedDateEnd', false);" maxlength="10"/>
				</td>
				<td align="right" nowrap="true">客户名称：</td>
				<td align="left" nowrap="true">
					<input type="text" id="ctmName" class="middle_txt"  name="ctmName"/>
				</td>
				<td align="right" nowrap="true">手机号码：</td>
				<td align="left" nowrap="true">
					<input type="text" id="tel" class="middle_txt" name="tel"/>
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">汽车产地：</td>
				<td align="left" nowrap="true">
					<select id="yieldly" name="yieldly" class="short_sel" >
						<option value=''>-请选择-</option>
						<c:forEach var="yieldly" items="${yieldlyList}">
							<option value="${yieldly.areaId}" title="${yieldly.areaName}">${yieldly.areaName}</option>
						</c:forEach>
					</select>
				</td>
				<td align="right" nowrap="true">汽车种类：</td>
				<td align="left" nowrap="true">
					<select id="series" name="series" class="short_sel" >
						<option value=''>-请选择-</option>
						<c:forEach var="series" items="${seriesList}">
							<option value="${series.groupId}" title="${series.groupName}">${series.groupName}</option>
						</c:forEach>
					</select>
				</td>
				<td align="right" nowrap="true">汽车型号：</td>
				<td align="left" nowrap="true">
					<input type="text" id="model" class="middle_txt" name="model"/>
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">发动机号：</td>
				<td align="left" nowrap="true">
					<input type="text" id="engineNo" class="middle_txt" name="engineNo"/>
				</td>
				<td align="right" nowrap="true">底盘号码：</td>
				<td align="left" nowrap="true">
					<input type="text" id="vin"  class="middle_txt" name="vin"/>
				</td>
				<td align="right" nowrap="true">客户级别：</td>
				<td align="left" nowrap="true">
					<select id="guestStars" name="guestStars" class="short_sel" >
						<option value=''>-请选择-</option>
							<c:forEach var="gs" items="${guestStarsList}">
								<option value="${gs.CODE_ID}" title="${gs.CODE_DESC}">${gs.CODE_DESC}</option>
							</c:forEach>
					</select>
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">省份：</td>
				<td align="left" nowrap="true">
<%--					<input type="text" id="province"  class="middle_txt" name="province"/>--%>
					<select id="province" name="province" class="short_sel" onchange="changeCityEvent(this.value,'',false)">
						<option value=''>-请选择-</option>
						<c:forEach var="pro" items="${proviceList}">
							<option value="${pro.REGION_CODE}" title="${pro.REGION_NAME}">${pro.REGION_NAME}</option>
						</c:forEach>
					</select>
				</td>
				<td align="right" nowrap="true">县市：</td>
				<td align="left" nowrap="true">
<%--					<input type="text" id="city"  class="middle_txt"name="city"/>--%>
					<select id="city" name="city" class="short_sel">
						<option value=''>-请选择-</option>
					</select>
				</td>
				<td align="right" nowrap="true">车辆用途：</td>
				<td align="left" nowrap="true">
					<select id="use" name="use" class="short_sel" >
						<option value=''>-请选择-</option>
							<c:forEach var="ul" items="${useList}">
								<option value="${ul.CODE_ID}" title="${ul.CODE_DESC}">${ul.CODE_DESC}</option>
							</c:forEach>
					</select>
				</td>
			</tr>
	
			<tr>
				<td colspan="8" align="center">
					<input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="queryPer();" />
					&nbsp;
          			<input id="downExcel" name="downExcel" type="button" value="导出Excel" class="normal_btn" onclick="downExcelQuery();" />
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

	function downExcelQuery(){
		fm.action = '<%=contextPath%>/customerRelationships/clientManage/ClientInforManage/clientInforManageExcel.do';
		fm.submit();
	}
	
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/clientManage/ClientInforManage/queryClientInforManage.json";
				
	var title = null;

	var columns = [
				{header: "客户名称",dataIndex: 'CTMNAME',align:'center'},
				{header: "手机号", dataIndex: 'PHONE', align:'center'},
				{header: "VIN号",dataIndex: 'VIN',align:'center'},
				{header: "车型", dataIndex: 'MODELNAME', align:'center'},
				{header: "购车日期", dataIndex: 'BUYDATE', align:'center'},
				{header: "省份",dataIndex: 'PROVINCE',align:'center'},
				{header: "县市", dataIndex: 'CITY', align:'center'},
				{header: "客户级别",dataIndex: 'GUESTSTARS',align:'center'},
				{header: "地区", dataIndex: 'TOWN', align:'center'},
				{header: "经销商名称",dataIndex: 'DEALERNAME',align:'center'}/*,
				{id:'action',header: "操作",sortable: false,dataIndex: 'ORDERID',renderer:myLink}*/
		      ];

	function myLink(value,meta,record){
		if(record.data.CALLCENTER_CHECK_STATUS=="18011002"){//审核通过的才能在这里驳回 
			return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='viewDetail(\""+ value +"\")' value='查看'/><input name='rejectBtn' id='rejectBtn_"+value+"' type='button' class='normal_btn' onclick='rejectActualSales(\""+ value+"\")' value='驳回'/>");
			
		}
		return "<input name='detailBtn' type='button' class='normal_btn' onclick='viewDetail(\""+ value +"\")' value='查看'/>";
	}

	function checkSubmit(status){
		makeNomalFormCall('<%=contextPath%>/customerRelationships/clientManage/ClientInforManage/save.json?status='+status,saveBack,'fm','');
	}

	function hiddenRejectBtn(o){
		document.getElementById('rejectBtn_'+o.order_id).style.display='none';
	}
	
	function rejectActualSales(order_id){
		if(confirm("确认要驳回实销档案信息吗?")){
			var status = '<%=Constant.CALLCENTER_CHECK_STATUS_03 %>';
			var cUrl= '<%=contextPath%>/customerRelationships/clientManage/ClientInforManage/save.json?status='+status+'&ids='+order_id+'&comm=1'+'&checkRamark=';
			makeNomalFormCall(cUrl,'hiddenRejectBtn','fm','');
			document.getElementById('rejectBtn_'+order_id).style.display='none';
		}
	}
	function viewDetail(value){	
		window.open("<%=contextPath%>/customerRelationships/clientManage/ClientInforManage/watchClientInforManage.do?id="+value);
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
 // $('CREATE_DATE_STR').value=showMonthFirstDay(); checkSDate
  

  
  function queryPer(){
	 
  	var star1 = $('purchasedDateStart').value;
    var end1 =  $('purchasedDateEnd').value;
    
    var ctmName = $('ctmName').value;
    var tel = $('tel').value;
    var yieldly = $('yieldly').value;
    var series = $('series').value;
    var model = $('model').value;
    var engineNo = $('engineNo').value;
    var vin = $('vin').value;
    var guestStars = $('guestStars').value;
    var province = $('province').value;
    var city = $('city').value;
    var use = $('use').value;
    
    if(ctmName == "" && tel == "" && yieldly == ""
    		&& series == ""&& model == ""&& engineNo == ""
    		&& vin == ""&& guestStars == ""&& province == ""
    		&& city == ""&& use == ""){
    	  if( star1==""||end1==""){
		  	 MyAlert("购买时间或者其它条件必须要录入一个!");
		 	 return false;
		  }else if( (star1>end1)){
		  	 MyAlert("开始时间不能大于结束时间!");
		  	 return false;
		  }else{
			var s3 = star1.replace(/-/g, "/");
			var s4 = end1.replace(/-/g, "/");
			 
			var d3 = new Date(s3);
			var d4 = new Date(s4);
			 
			var time1= d4.getTime() - d3.getTime();
		 
			var days1 = parseInt(time1 / (1000 * 60 * 60 * 24));
			
			if(days1>365){
				MyAlert("时间跨度不能超过一年!");
		  		return false;
			}
			 __extQuery__(1);
		  }
    }else{
    	 __extQuery__(1);
    }

	}

 	function changeCityEvent(value,defaultValue,isdisabled){
		if(''!=value){
			makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeRegion.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeCityBack,'fm','');
		}else{
			resetSelectOption('city',null,null,null,null,null);
		}
	}

	//城市级联回调方法：
	function changeCityBack(json) {
		resetSelectOption('city',json.regionList,'REGION_NAME','REGION_CODE',json.defaultValue,json.isdisabled);
	}
	
	//重置下拉框数据
	function resetSelectOption(id,maps,dataName,dataValue,dataId,isdisabled){
		clearSelectNode(id);
		addSelectNode(id,maps,dataName,dataValue,dataId,isdisabled);
	}
	
	//动态删除下拉框节点
	function clearSelectNode(id) {			
		document.getElementById(id).options.length=0; 			
	}
	//动态添加下拉框节点
	function addSelectNode(id,maps,dataName,dataValue,dataId,isdisabled){
		document.getElementById(id).options.add(new Option('-请选择-',''));
		if(maps != null){
			for(var i = 0; i<maps.length;i++){
				if((maps[i])['' +dataValue+''] == dataId){
					document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,true));
				}
				else{
					document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,false));
				}
			}
		}
		
		if(isdisabled == 'true' || isdisabled == true){
			document.getElementById(id).disabled = "disabled";
		}else{
			document.getElementById(id).disabled = "";
		}
		
	}
  
</script>


</body>
</html>
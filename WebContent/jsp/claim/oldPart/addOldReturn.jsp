<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TmBusinessAreaPO,java.util.*" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib prefix="change" uri="/jstl/change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔件回运清单新增</title>
<% String contextPath = request.getContextPath(); 
List<TmBusinessAreaPO> list = (List<TmBusinessAreaPO>)request.getAttribute("yieldlyList");
%>
</head>
<script type="text/javascript">
	function keyListnerResp(){
	   if((typeof window.event)!= 'undefined'){
		   var type = event.srcElement.type;   
	       var code = event.keyCode;
	       if(type=='text'||type=='textarea'){
	    	   event.returnValue=true;
	       }else{//如 不是文本域则屏蔽 Alt+ 方向键 ← Alt+ 方向键 →   //屏蔽后退键    
	         if(code==8||((window.event.altKey)&&((code==37)||(code==39)))){
	            event.returnValue=false;       
	         }
	       }
	   }
	}
	var myDate=new Date();
	/**
	 * 显示年份列表框
	 * yearComponent:年份id
	 * showNum:显示年份的数目,默认显示最近5年
	 */
	function showYearList(yearComponentName,showNum){
		if(yearComponentName==null||yearComponentName=='') return;
		var yearSelect=$(yearComponentName);
		var curYear=myDate.getFullYear();
		var curMon=myDate.getMonth()+1;
		if(yearSelect!=null){
		   clearOptions(yearSelect.options);
		}
		if(showNum==null||showNum==''){
			showNum=5;
		}
		for(var yearCount=0;yearCount<showNum;yearCount++){
	      var varItem = new Option(curYear-yearCount,curYear-yearCount);
	      yearSelect.options.add(varItem);
	    }
		if(curMon==1){//删除跨年时的年份
	      for(var i = 0; i < yearSelect.options.length; i++) {        
	        if (yearSelect.options[i].value == curYear) {        
	            yearSelect.options.remove(i);        
	            break;        
	        }        
	      }
	      yearSelect.value=curYear-1;
	    }else{
	      yearSelect.value=curYear;
	    }
	}
	/**
	 * 显示月份列表框
	 * monthComponentName:月份id
	 */
	function showMonthList(monthComponentName){
		if(monthComponentName==null||monthComponentName=='') return;
		var monSelect=$(monthComponentName);
		
		var curMon=myDate.getMonth()+1;
		if(monSelect!=null){
		   clearOptions(monSelect.options);
		}
		for(var monCount=1;monCount<=12;monCount++){
	      if((curMon-monCount)>=0){
	        var varItem = new Option(monCount,monCount);
	        monSelect.options.add(varItem);
	      }else if(curMon==1){
	    	var varItem = new Option(monCount,monCount);
	        monSelect.options.add(varItem);
	      }
	    }
		if(curMon==1){
			monSelect.value=12;
		}else{
			monSelect.value=myDate.getMonth()+1;//默认选择为本月
		}
		
	}
	/**
	 * @param 联动年份id
	 * @param 联动月份id
	 * @return
	 */
	function changeMonthList(yearName,monName){
		var yearSelect=$(yearName);
		var monSelect=$(monName);
		if(yearSelect==null||monSelect==null) return;
		var selectedYearValue=yearSelect.value;
		var curMon=myDate.getMonth()+1;
		clearOptions(monSelect.options);
		if(selectedYearValue==null||selectedYearValue==''){
			selectedYearValue=myDate.getFullYear();
	    }
		if(selectedYearValue==myDate.getFullYear()){
	      for(var monCount=1;monCount<=12;monCount++){
	        if((curMon-monCount)>=0){
	          var varItem = new Option(monCount,monCount);
	          monSelect.options.add(varItem);
	        }
	      }
	    }else{
	      for(var monCount=1;monCount<=12;monCount++){
	        var varItem = new Option(monCount,monCount);
	          monSelect.options.add(varItem);
	      }
	    }
	}
	//清除下拉式控件信息
	function clearOptions(colls){ 
	  var length = colls.length; 
	  for(var i=length-1;i>=0;i--){ 
	     colls.remove(i); 
	  } 
	}

	function doInit(){
	   loadcalendar();
	   showDate(<%=Constant.PART_IS_CHANGHE_01%>);
	   showYearList("yearStartSelect","8");
	   showYearList("yearEndSelect","8");
	   showMonthList("monStartSelect");
	   showMonthList("monEndSelect");
	}
	
	function showDate(yieldly){
	if(yieldly !=-1){
		var url = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/showDate.json?yieldly="+yieldly+"&dealer_id=${dealer_id}";
		sendAjax(url,showDetail2,"fm")
	}else{
			$('div').innerText="";
			$('startDate').value="";

			$('divEnd').innerText="";
			$('endDate').value="";
	}
	}

	function showDetail2(json){
		var oldDate = json.oldDate;
		var oldDateEnd = json.oldDateEnd;
		if(oldDate!='false'){
			var oldTime = oldDate.substring(0,10);
			$('div').innerText=oldTime;
			$('startDate').value=oldTime;

			var oldTimeEnd = oldDateEnd.substring(0,10);
			$('divEnd').innerText=oldTimeEnd;
			$('endDate').value=oldTimeEnd;
			
		}else{
			MyAlert("此产地没有旧件!");
			$('div').innerText='';
			$('startDate').value='';
		}
	}
	
	function show1(obj){
		if(obj.value=="10731001"){
			$('show1').style.display='';
			$('show3').style.display='none';
			$('show4').style.display='none';
			$('show5').style.display='';
			$('show6').style.display='';
			$("bowrrowNo").value='';
			$("bowrrowNo").value="";
		}
		if(obj.value=="10731002"){
			var a = $("transportTable");
			deleteRow(a);
			$("bowrrowNo").value='';
			$('show1').style.display='none';
			$('show3').style.display='';
			$('show4').style.display='';
			$('show5').style.display='none';
			$('show6').style.display='none';
		}
	}
	function deleteRow(a){
		if(a.childNodes.length>0){
			for(var j=0;j<a.childNodes.length;j++){
				a.removeChild(a.childNodes[j]);
				
			}
			for(var j=0;j<a.childNodes.length;j++){
				a.removeChild(a.childNodes[j]);
				
			}
			for(var j=0;j<a.childNodes.length;j++){
				a.removeChild(a.childNodes[j]);
				
			}
			for(var j=0;j<a.childNodes.length;j++){
				a.removeChild(a.childNodes[j]);
				
			}
		}
	}
	function showListData(){
		var dealer_id=$('dealer_id').value;
		OpenHtmlWindow('<%=contextPath%>/claim/oldPart/EmergencyDevice/showListData.do?dealer_id='+dealer_id,800,500);
	}
	function setDataShow(id){
		$("bowrrowNo").value=id;
		var a = $("transportTable");
		deleteRow(a)
		sendAjax('<%=contextPath%>/claim/oldPart/EmergencyDevice/setDataShow.json?id='+id,queryDateByIdBack,'fm');
		
	}
	function queryDateByIdBack(json){
		var list=json.borrowSubclassList;
		for(var i =0;i<list.size();i++){
		var addTable = $("transportTable");
		var rows = addTable.rows;
		var length = rows.length;
		var insertRow = addTable.insertRow(length);
		insertRow.className = "table_list_row1";
		insertRow.insertCell(0);
		insertRow.insertCell(1);
		insertRow.insertCell(2);
		insertRow.insertCell(3);
		insertRow.insertCell(4);
		
		addTable.rows[length].cells[0].innerHTML =  '<td><input type="hidden" class="middle_txt" name="claim_id" value='+list[i].claimId+' /><input type="hidden" class="middle_txt" name="part_id" value='+list[i].partId+' /><input type="hidden" class="middle_txt" name="claim_no" value='+list[i].claimNo+' />'+list[i].claimNo+'</td>';
		addTable.rows[length].cells[1].innerHTML =  '<td><input type="hidden" class="middle_txt" name="vin" value='+list[i].vin+' />'+list[i].vin+'</td>';
		addTable.rows[length].cells[2].innerHTML =  '<td nowrap="true"><input type="hidden" class="middle_txt" name="part_code" value='+list[i].partCode+' />'+list[i].partCode+'</td>';
		addTable.rows[length].cells[3].innerHTML =  '<td nowrap="true"><input type="hidden" class="middle_txt" name="part_name" value='+list[i].partName+'/>'+list[i].partName+'</td>';
		addTable.rows[length].cells[4].innerHTML =  '<td nowrap="true"><input type="hidden" class="middle_txt" name="problem_reason" value='+list[i].problemReason+' />'+list[i].problemReason+'</td>';
		}
	}
</script>
<body onload="doInit();" onkeydown="keyListnerResp();">
	<div class="navigation">
	<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔旧件管理 &gt;索赔件回运清单管理
	</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" id="dealer_id" value="${dealer_id }">
		<div id="submitTimeDiv" style="display: ">
			<table class="table_query">
				<tr>
					<td align="right" nowrap="nowrap">&nbsp;&nbsp;产地：</td>
					<td align="left">
						<select name="YIELDLY_TYPE" id="YIELDLY_TYPE" style="width: 150px;" onChange="showDate(this.value);">
							<option value="-1">-请选择-</option>
							<option value="<%=Constant.PART_IS_CHANGHE_01%>" selected>重庆</option>
						</select>
						<input type="hidden" id="backType" name="backType" value="<%=Constant.BACK_TRANSPORT_TYPE_02%>">
					</td>
					
					<td  align="right" nowrap="nowrap" id="show3" >&nbsp;&nbsp;索赔审核通过时间：</td>
					<td  align="left" nowrap="nowrap" id="show4">
						<label id="div"></label>
						<input type="hidden" id="startDate" name="startDate" value="" />
					           至  
					    <label id="divEnd"></label>           
					    <input name="endDate" type="hidden" id="endDate" value=""/>
					</td>
					<td width="10%"  nowrap="nowrap" id="show5"  style="display: none;">&nbsp;<span style="color: red;">提示：紧急调件</span></td>
					<td width="15%"  nowrap="nowrap" id="show6"  style="display: none;">&nbsp;<span style="color: red;">没有审核时间</span>
					</td>
					<td width="25%" >
						&nbsp;&nbsp;<input class="normal_btn"type="button" id="createOrdBtn" name="createOrdBtn" value="确定" onclick="createReturnOrderConfirm();">&nbsp;&nbsp;
					</td>
				</tr>
			</table>
		</div>
	
		<div  id="show1" style="display: none;" >
		<table class="table_query" width="100%;"  width="100%" border="0" cellspacing="0" cellpadding="0" class="table_edit">
			<tr>
				<td  width="10%" nowrap="true">
					借件单：
				</td>
				<td  width="15%" nowrap="true">
					<input type="text" id="bowrrowNo" name="bowrrowNo" class="middle_txt" readonly="readonly" />&nbsp;&nbsp;<a href="#" onclick="showListData();">选择</a>
				</td>
				<td width="10%"  nowrap="nowrap">&nbsp;</td>
				<td width="15%"  nowrap="nowrap">&nbsp;</td>
				<td width="10%"  nowrap="nowrap">&nbsp;</td>
				<td width="15%"  nowrap="nowrap">&nbsp;</td>
				<td width="10%"  nowrap="nowrap">&nbsp;</td>
				<td width="15%"  nowrap="nowrap">&nbsp;</td>
					
			</tr>
		</table>
		<table  width="100%;"  width="100%" border="0" cellspacing="0" cellpadding="0" class="table_edit">
		<tr>
			<th width="10%" nowrap="true"class="table_query_2Col_label_7Letter">索赔单号</th>
			<th width="10%" nowrap="true"class="table_query_2Col_label_7Letter">VIN</th>
			<th width="10%" nowrap="true"class="table_query_2Col_label_7Letter">配件代码</th>
			<th width="10%" nowrap="true"class="table_query_2Col_label_7Letter">配件名称</th>
			<!-- <th width="10%" nowrap="true"class="table_query_2Col_label_7Letter">件号</th>
			<th width="10%" nowrap="true"class="table_query_2Col_label_7Letter">条码</th> -->
			<th width="10%" class="table_query_2Col_label_7Letter">故障描述</th>
		<tr>
		<tbody id="transportTable">
			
		</tbody>
	</table>
	</form>
	</div>
	<div id="blanceWait" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'>
	</div>
<script type="text/javascript">
	function createReturnOrderConfirm(){
		var yieldly_type=$('YIELDLY_TYPE').value;
		var backType=$('backType').value;
		var bowrrowNo=$("bowrrowNo").value;
		if(yieldly_type==-1){
			MyAlert("提示：请选择产地！");
			return;
		}
		if(backType==10731001){//紧急调件
			if(bowrrowNo==""){
				MyAlert("提示：紧急调件请选择借件单！");
				return;
			}
		}else if(backType==10731002){
			var startDate=$("startDate").value;
		    var endDate=$("endDate").value;
		    var year=endDate.substring(0,4);
			var mouth=endDate.substring(5,7);
			var day=endDate.substring(8,10);
		    if(startDate>endDate){
			       MyAlert("该月旧件已完成,请下月再新增!");
			       return;
			}
		}else{
			MyAlert("提示：请选择回运类型！");
			return;
		}
			MyConfirm("是否创建回运清单？",createReturnOrder,[]);
	}
	//创建回运清单
	function createReturnOrder(){
		var backType=$('backType').value;
	    var startDate=$("startDate").value;
	    var endDate=$("endDate").value;
	    var mouth=endDate.substring(5,7);
	    var borrowNo=$("bowrrowNo").value;
	    var join_url="<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/addReturnOrder1.json?startDate="+startDate+"&endDate="+endDate+"&mouth="+mouth+"&backType="+backType+"&borrowNo="+borrowNo+"&dealer_id=${dealer_id}";
	    makeNomalFormCall(join_url,showDetail1,'fm','createOrdBtn');
	}
	function showDetail1(json){
		var backType=$('backType').value;
		var startDate=$("startDate").value;
		var endDate=$("endDate").value;
		if(backType==10731002){
			var ok = json.ok;
			var flag=json.flag;
			var nowTime1=json.nowTime1;
			var nowTime2=json.nowTime2;
	
			var yearTime1=json.yearTime1;
			var yearTime2=json.yearTime2;
			if(ok=='ok'){
	    		MyAlert('还未到结算日!');
	    		return;
			}
			if(ok=='hasMore'){
	    		MyAlert('选择的月份含有未上报或者未审核的单据!');
	    		return;
			}
			if(Number(yearTime1)<=Number(yearTime2)){
				if(Number(nowTime2)>Number(nowTime1)){
					MyAlert('不能选择当前月份,只能选择当前月的上月回运!');
		    		return;
				}
			}
		}
			var borrowNo=$("bowrrowNo").value;
			var join_url="<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/addOldReturn.json?startDate="+startDate+"&endDate="+endDate+"&borrowNo="+borrowNo+"&dealer_id=${dealer_id}";
			makeNomalFormCall(join_url,showDetail,'fm','createOrdBtn');
	}
	//现在该回运清单明细
	function showDetail(json){
		var status = json.SUCCESS;
        if('SUCCESS'==status){
    		var returnID = json.returnID;
	    	var tarUrl = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryReturnOrderDetail.do?ORDER_ID="+returnID+"&closeFlag=1";
			location.href = tarUrl;
		}else if('DEALED'==status){
			MyAlert("其他人正在创建回运清单！");
			location.href = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/returnOrder.do";
		}else{
			MyAlert("没有需要回运的配件！");
			location.href = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/returnOrder.do";
		}
	}

	 function waitBlance(){
			if($('blanceWait')){
				var screenW = document.viewport.getWidth()/2;	
				var screenH = document.viewport.getHeight()/2;
				$('blanceWait').style.left = (screenW-20) + 'px';
				$('blanceWait').style.top = (screenH-20) + 'px';
				$('blanceWait').innerHTML = ' 正在处理中... ';
				$('blanceWait').show();
			}
	}
		
	 function noticeBlance(){
		if($('blanceWait'))
			$('blanceWait').hide();
	 }
</script>
</body>
</html>
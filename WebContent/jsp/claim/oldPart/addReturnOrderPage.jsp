<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.sequenceUitl.SequenceManager"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔件回运清单新增</title>
<% String contextPath = request.getContextPath(); %>
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
		var yearSelect=document.getElementById(yearComponentName);
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
		var monSelect=document.getElementById(monthComponentName);
		
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
		var yearSelect=document.getElementById(yearName);
		var monSelect=document.getElementById(monName);
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
	   showYearList("yearStartSelect","8");
	   showYearList("yearEndSelect","8");
	   showMonthList("monStartSelect");
	   showMonthList("monEndSelect");
	}
	
	function showDate(yieldly){
		var url = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/showDate.json?yieldly="+yieldly;
		sendAjax(url,showDetail2,"fm")
	}

	function showDetail2(json){
		var oldDate = json.oldDate;
		var oldTime = oldDate.substring(0,10);
		$('div').innerText=oldTime;
		$('startDate').value=oldTime;
	}

</script>
<body onload="doInit();" onkeydown="keyListnerResp();">
	<div class="navigation">
	<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔旧件管理 &gt;索赔件回运清单管理
	</div>
	<form id="fm" name="fm" method="post">
		<div id="submitTimeDiv" style="display: ">
			<table class="table_query">
				<tr>
					<td align="right">产地：</td>
					<td align="left">
				        <script type="text/javascript">
				            genSelBoxExp("YIELDLY_TYPE",<%=Constant.YIELDLY_TYPE%>,"",true,"min_sel","onChange=showDate(this.value);","true",'');
				        </script>
					</td>
					<td align="right">回运类型：</td>
					<td align="left">
				        <script type="text/javascript">
				            genSelBoxExp("backType",<%=Constant.BACK_TRANSPORT_TYPE%>,"",false,"short_sel","","true",'<%=Constant.BACK_TRANSPORT_TYPE_01%>');
				        </script>
					</td>
					<td align="right" nowrap="nowrap">索赔单提交时间段：</td>
					<td align="left" nowrap="nowrap">
						<label id="div"></label>
						<input type="hidden" id="startDate" name="startDate" value="" />
					           至      
					    <input name="endDate" type="text" id="endDate" class="short_txt"
					datatype="0,is_date,10" hasbtn="true"
					callFunction="showcalendar(event, 'endDate', false);" />
					
						
					</td>
					<td align="right"><input class="normal_btn"
						type="button" id="createOrdBtn" name="createOrdBtn" value="确定"
						onclick="createReturnOrderConfirm();">&nbsp;&nbsp;</td>
				</tr>
			</table>
		</div>
	</form>
	<div id="blanceWait" 
		style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'>
	</div>
<script type="text/javascript">
	function createReturnOrderConfirm(){
		if(submitForm('fm')) {	
			var startDate=document.getElementById("startDate").value;
		    var endDate=document.getElementById("endDate").value;
		    var year=endDate.substring(0,4);
			var mouth=endDate.substring(5,7);
			//var mouth1 = mouth++;
			var day=endDate.substring(8,10);
			var YIELDLY_TYPE=document.getElementById("YIELDLY_TYPE").value;
			if(YIELDLY_TYPE==''){
				MyAlert('请选择基地!');
				return;
			}
		    if(startDate>endDate){
			       MyAlert("起始年份不应大于结束年份!");
			       return;
			}
			//if(month>12){//如果当前大于12月，则年份转到下一年   
				//mouth1 -=12;        //月份减 
				//year++;            //年份增   
			//}
			var last=(new Date((new Date(year,mouth,1)).getTime()-1000*60*60*24)).getDate();//获取当月最后一天日期
			//if(mouth!=12){
				if(day!=last){
					MyAlert('必须选择当月的最后一天!');
					return;
				}
			//}
			MyConfirm("是否创建回运清单？",createReturnOrder,[]);
	    }
	}

	//创建回运清单
	function createReturnOrder(){
	    var startDate=document.getElementById("startDate").value;
	    var endDate=document.getElementById("endDate").value;
	    var mouth=endDate.substring(5,7);
	    var join_url="<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/addReturnOrder1.json?startDate="+startDate+"&endDate="+endDate+"&mouth="+mouth;
	    makeNomalFormCall(join_url,showDetail1,'fm','createOrdBtn');
	}

	function showDetail1(json){
		var ok = json.ok;
		//var day = json.day;
		//var day1=json.day1;
		var flag=json.flag;
		var nowTime1=json.nowTime1;
		var nowTime2=json.nowTime2;

		var yearTime1=json.yearTime1;
		var yearTime2=json.yearTime2;
		if(ok=='ok'){
    		MyAlert('还未到结算日!');
    		return;
		}
		//if(day!=day1 && day!=null){
			//MyAlert('12月份必须选择结算日!');
    		//return;
		//}
		if(Number(yearTime1)<=Number(yearTime2)){
			if(Number(nowTime2)>=Number(nowTime1)){
				MyAlert('不能选择当前月份,只能选择当前月的上月回运!');
	    		return;
			}
		}
		if(flag=='false'){
			MyAlert('请先在顶腾系统中完成11月份前[旧件清单]的提报!');
    		return;
		}
		var startDate=document.getElementById("startDate").value;
		var endDate=document.getElementById("endDate").value;
		var join_url="<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/addReturnOrder.json?startDate="+startDate+"&endDate="+endDate;
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
			location.href = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryReturnOrder.do";
		}else{
			MyAlert("没有需要回运的配件！");
			location.href = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryReturnOrder.do";
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
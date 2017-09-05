<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>月度任务下发  </title>
<script language="JavaScript" src="<%=contextPath %>/js/ut.js"></script>

<script language="JavaScript">
       function doInit(){
        	visbtn(0);
        	//加一个方法，初始化下拉框
        }
        function visbtn(act){
          if(act==0){
            document.getElementById("btns").style.display='none';
          }else{
            document.getElementById("btns").style.display='inline';
          }
        }
     
		function querySubmit111(){
			document.getElementById("nors").style.display="none";
			document.getElementById("btns").style.display="none";
		    var areaId=document.getElementById("areaId").value;
			var url = "<%=contextPath%>/sales/planmanage/MonthTarget/SubMonthTargetPublish/monthPlanPublistQuery.json";
			makeCall(url,showJson,{areaId:areaId});
		}
		//构建页面
		function showJson(json){
			document.getElementById("fm").disabled = false ;
	    	document.fm1__.disabled = false ;
			var subinfo=json.subInfo;
			var glist=json.areaGroupList;
			var planlist=json.planList;
			var groupIds='';//所有车系拼成串
			var table=document.getElementById("tbody1");
			clrTbody(table);
			var tbody=document.getElementById("rsThTbody");
			clrTbody(tbody);
			var rstobdy=document.getElementById("rstobdy");
			clrTbody(rstobdy);
			if(subinfo==null||glist.length==0||planlist.length==0){
			  document.getElementById("nors").style.display="inline";
			  return;
			}
			document.getElementById("btns").style.display="inline";
			var tr1=document.createElement("tr");//TD值
			//var td1=document.createElement("td");
			//td1.appendChild(document.createTextNode(subinfo.ORG_CODE));
			var td2=document.createElement("td");
			td2.appendChild(document.createTextNode(subinfo.ORG_NAME));
			//tr1.appendChild(td1);
			tr1.appendChild(td2);
			var tr=document.createElement("tr");//TH行
			//var th1=document.createElement("th");
			//th1.appendChild(document.createTextNode("大区代码"));
			var th2=document.createElement("th");
			th2.appendChild(document.createTextNode("大区"));
			//tr.appendChild(th1);
			tr.appendChild(th2);
			//在建行时把TD值写进去
            for(var i=0;i<glist.length;i++){
			    //创建表头
			    var th=document.createElement("th");
			    th.appendChild(document.createTextNode(glist[i].GROUP_NAME));
			    //创建表头下的TD，将值写进去
			    var td=document.createElement("td");
			    td.appendChild(document.createTextNode(subinfo['A'+glist[i].GROUP_ID]));//在SQL查询时别名用的是A+GROUP_ID
			    tr1.appendChild(td);
			    tr.appendChild(th);
			    //创建一个hidden来保存某个车第的总数
			    var shid=document.createElement('input');
			    shid.name='sum'+glist[i].GROUP_ID;
			    shid.id='sum'+glist[i].GROUP_ID;
			    shid.type='hidden';
			    shid.setAttribute('sn',glist[i].GROUP_NAME);
			    shid.value=subinfo['A'+glist[i].GROUP_ID];
			    tr1.appendChild(shid);
			    groupIds+=glist[i].GROUP_ID+',';
			}
			groupIds=groupIds.substring(0,groupIds.length-1);
			var groupIdstr=document.createElement('input');
			groupIdstr.name='groupIdstr';
			groupIdstr.id='groupIdstr';
			groupIdstr.type='hidden';
			groupIdstr.value=groupIds;
			tr1.appendChild(groupIdstr);
			
			table.appendChild(tr);
			table.appendChild(tr1);
			//构建结果TBODY
			var rsthtr1=document.createElement("tr");
			rsthtr1.className="table_list_row"+(i%2+1);
			var rsth1=document.createElement("th");
			rsth1.rowSpan=2;
			rsth1.appendChild(document.createTextNode("经销商代码"));
			rsthtr1.appendChild(rsth1);
			var rsth2=document.createElement("th");
			rsth2.rowSpan=2;
			rsth2.appendChild(document.createTextNode("经销商名称"));
			rsthtr1.appendChild(rsth2);
			var glist1=json.areaGroupList;
			for(var i=0;i<glist1.length;i++){
			   var rsth=document.createElement("th");
			   rsth.colSpan=2;
			   rsth.appendChild(document.createTextNode(glist1[i].GROUP_NAME));
			   rsthtr1.appendChild(rsth);
			}
			var rsthtr2=document.createElement("tr");
			for(var i=0;i<glist1.length;i++){
			   var rsthy=document.createElement("th");
			   var rsthm=document.createElement("th");
			   /**
			   * modify by zhaolunda 2010-08-18
			   * 需求调整为车厂直接下发给经销商任务，"年度目标"调整为"车厂下发任务"
			   */
			   rsthy.appendChild(document.createTextNode("车厂下发任务"));
			   rsthm.appendChild(document.createTextNode("月度任务"));
			   rsthtr2.appendChild(rsthy);
			   rsthtr2.appendChild(rsthm);
			}
		    tbody.appendChild(rsthtr1);
		    tbody.appendChild(rsthtr2);
		    
		    //遍历结果集，构建列表
		    var rstbody=document.getElementById("rstobdy");
		    clrTbody(rstbody);
		    
		    var sumtr=document.createElement("tr");
		    var sumtd=document.createElement("td");
		    sumtd.colSpan=2;
		    sumtd.appendChild(document.createTextNode("合计"));
		    sumtr.appendChild(sumtd);
		    var len=glist.length;
		    var sumArr= new Array(len*2);
		    for(var i=0;i<sumArr.length;i++){
		       sumArr[i]=0;
		    }
		    for(var i=0;i<planlist.length;i++){
		      var k=0;
		      var rstr1=document.createElement("tr");
		      rstr1.className="table_list_row"+(i%2+1);
		      var tdcode=document.createElement("td");
		      tdcode.appendChild(document.createTextNode(planlist[i].DEALER_CODE));
		      var tdname=document.createElement("td");
		      tdname.appendChild(document.createTextNode(planlist[i].DEALER_SHORTNAME));
		      rstr1.appendChild(tdcode);
		      rstr1.appendChild(tdname);
		      for(var j=0;j<glist.length;j++){
		      var std=document.createElement("td");
		           var y=planlist[i]['Y'+glist[j].GROUP_ID]+0;
		           sumArr[k]+=y;
		           k++;
		           sumArr[k]+=planlist[i]['M'+glist[j].GROUP_ID];
		           k++
				   var tdy=document.createElement("td");
				   var tdm=document.createElement("td");
				   var iptm=document.createElement("input");
				   iptm.type="text";
				   iptm.name=planlist[i].DEALER_ID+"amt"+glist[j].GROUP_ID;
				   iptm.id='id'+i+j;
				   iptm.size=3;
				   iptm.maxLength=3;
				   iptm.setAttribute("datatype","1,is_digit,4");
				   //iptm.datatype="1,is_digit,4";
				   //iptm.onblur=function(){MyAlert(1);};
				   iptm.value=planlist[i]['M'+glist[j].GROUP_ID];
				   iptm.attachEvent('onchange',getSum);
				   tdy.appendChild(document.createTextNode(planlist[i]['Y'+glist[j].GROUP_ID]));
				   tdm.appendChild(iptm);
				   rstr1.appendChild(tdy);
				   rstr1.appendChild(tdm);
			  }
			  rstbody.appendChild(rstr1);
		    }
		    var j=0;
		    for(var i=0;i<sumArr.length;i++){
		      var groupId;
		      if(i%2!=0){
		         groupId=glist[j].GROUP_ID;
		         j++;
		      }
		      var td=document.createElement("td");
		      td.id='s'+groupId;
		      td.appendChild(document.createTextNode(sumArr[i]));
		      sumtr.appendChild(td);
		    }
		    rstbody.appendChild(sumtr);
		    visbtn(1);
		    document.getElementById("areaId1").value=json.areaId;
		    document.getElementById("year").value=json.year;
		    document.getElementById("month").value=json.month;
		    document.getElementById("planType").value=json.planType;
	    }
	    //onchange时计算合计
	    function getSum(){
	        var se=event.srcElement;
	        var sname=se.name;
	        var sum=0;
	        var groupId=sname.substring(sname.indexOf('amt')+3,sname.length);
	        var obj=document.getElementById('s'+groupId);
	        var ipts=document.getElementsByTagName("input");
	        for(var i=0;i<ipts.length;i++){
	        	if(ipts[i].name.indexOf('amt'+groupId)!=-1){
					sum+=parseInt(ipts[i].value,10);
				}	        	
	        }
	        obj.innerText=sum;
	    }
	    //提交时校验合计是否与月度任务总数相同
	    function checkSum(){
	        var ipts=document.getElementsByTagName('input');
	    	var groupIds=document.getElementById('groupIdstr').value;
	    	var groupArr=groupIds.split(',');
	    	for(var i=0;i<groupArr.length;i++){
	    	    var sum=0;
	    	    var groupId=groupArr[i];
	    		var sumObj=document.getElementById('sum'+groupId);
	    		for(var j=0;j<ipts.length;j++){
	    			 if(ipts[j].name.indexOf('amt'+groupId)!=-1){
	    			 	 sum+=parseInt(ipts[j].value, 10);
	    			 }
	    		}
	    		if(parseInt(sum, 10)<parseInt(sumObj.value, 10)){
	    			MyAlert(sumObj.sn+'月度任务总数不能小于车厂下发合计');
	    			return false;
	    		}
	    	}
	    	return true;
	    }
	    //清空表格
	    function clrTbody(obj){
	        for(var i=0,len = obj.rows.length;i<len;i++){
               obj.deleteRow(0);
			}
	    }
	    function subChecked(actType){
	        if(submitForm('fm1')){
	        	if(checkSum()){
			   	 var str="";
			    	document.getElementById("actType").value=actType;
			    	if(actType==1) {
						MyConfirm("是否执行确认操作?",confirmSubmit);
			    	}
					if(actType==2) {
						MyConfirm("是否执行确认操作?",confirmSubmit__);
					}
				}
			}
	    }
	    function confirmSubmit(){
	    	makeNomalFormCall('<%=contextPath %>/sales/planmanage/MonthTarget/SubMonthTargetPublish/monthPlanPublishOpe.json',querySubmit111,'fm1');
	    	document.getElementById("fm").disabled = true ;
	    	document.fm1__.disabled = true ;
	    }

	    function confirmSubmit__(){
	        fm1.action =  "<%=contextPath %>/sales/planmanage/MonthTarget/SubMonthTargetPublish/monthPlanPublishOpe.do";
			fm1.submit();
			document.getElementById("fm").disabled = true ;
	    	document.fm1__.disabled = true ;
	    }
	    
</script>
</head>

<body>
	<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>月度任务>月度任务下发  
	</div>
<%
   List list=(List)request.getAttribute("list");
   if(null!=list&&list.size()>0){
%>
<form name="fm" id="fm" method="post">
<table class="table_query" id="subtab" width="95%">
  <tr class="csstr">
    <td id="aaa"></td>
    <td> 请选择业务范围：
     <select name="buss_area" id="areaId">
	       <c:forEach items="${list}" var="list" >
	       		<option value="${list.PLAN_YEAR },${list.PLAN_MONTH },${list.AREA_ID },${list.CODE_ID }">${list.PLAN_YEAR }年${list.PLAN_MONTH }月${list.AREA_NAME }-${list.CODE_DESC }</option>
		   </c:forEach>
      </select>
    </td>
    <td>
    	<input type="button" class="cssbutton"  value="查询" onclick="querySubmit111();" />
    </td>
  </tr>  
</table>
</form>
</div>
<div>
<table class="table_list" id="nors" style="display: none;">
	<tr>
		<td align="center"><font color="red">没有满足符合条件的数据</font></td>
	</tr>
</table>
</div>
<table class="table_list" style="border-bottom:1px solid #DAE0EE" width="95%" border="1">
<tbody id="tbody1"></tbody>
</table>
<form name="fm1" id="fm1__" method="post">
<table class="table_list">
   <tbody id="rsThTbody"></tbody>
   <tbody id="rstobdy"></tbody>
</table>
<table width="95%" border="0" align="center" class="table_query" id="">
<tbody id="btns" >
    <tr> 
      <td align="left">
        <input type="hidden" name="year" id="year" value="" />
        <input type="hidden" name="month" id="month" value="" />
        <input type="hidden" name="planType" id="planType" value="" />
        <input type="hidden" name="areaId" id="areaId1" value="" />
        <input type="hidden" name="actType" id="actType" value="" />
        <input class="cssbutton" type="button" value="保存" name="bt_save" id="bt_save" onclick="subChecked(1);" />
        <input class="cssbutton" type="button" value="下发" name="bt_publish" id="bt_publish" onclick="subChecked(2);" />
        <input class="cssbutton" type="button" value="返回" name="bt_back" id="bt_back" onclick="history.back();" />	
      </td>
    </tr>
</tbody>
</table>
</form>
<%}else{ %>
<table class="table_query" id="nors">
	<tr>
		<td align="center"><font color="red">没有可下发的月度任务</font></td>
	</tr>
</table>
<%} %>
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.bean.TtAsActivityBean"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@page import="com.infodms.dms.bean.TtAsActivityBean"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c" %>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动管理</title>
<% String contextPath = request.getContextPath(); %>
<%
TtAsActivityBean beforeVehicle = (TtAsActivityBean) request.getAttribute("beforeVehicle");//服务活动信息-服务车辆活动范围
TtAsActivityBean afterVehicle = (TtAsActivityBean) request.getAttribute("afterVehicle");//服务活动信息-服务车辆活动范围
List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");
%>			
<% 
TtAsActivityBean ActivityPO=(TtAsActivityBean)request.getAttribute("ActivityBean");
%>
<script type="text/javascript">
 //日历控件初始化
	function doInit()
		{
		   loadcalendar();
		}
//索赔选择
	function chickFixfee(show){
		if(document.fm.isFixfee.checked==true){
			document.fm.isClaim.checked=true;
			show.style.display ="block";
		}else {
		 show.style.display ="none";
		//document.fm.partFee.value =0.0;
        //document.fm.worktimeFee.value =0.0;
		}
	}
	function fix(show){
          if(document.fm.isFixfee.checked==true){
           show.style.display ="block";
           }else{
           show.style.display ="none";
           //document.fm.partFee.value =0.0;
           //document.fm.worktimeFee.value =0.0;
           }
    }
    function chickClaim(show){
		if(document.fm.isClaim.checked==false&&document.fm.isFixfee.checked==true){
			document.fm.isClaim.checked=true;
		}
	}
	//重新发布 开始
		function subChecked(action){
			/*JS取日期之间的相差的天数*/
			var single = $('single').value ;
			var single_car = $('single_car').value ;
			var max = $('max').value ;
			var max_car = $('max_car').value ;
			if(single*1>single_car*1 || max*1>max_car*1){
				MyAlert('单台次与单经销商活动次数不能改小！');
				return ;
			}
		      var number;
		      var endDate=document.getElementById("endDate").value;
		      var startDate=document.getElementById("startDate").value;
		      var uploadPrePeriod=document.getElementById("uploadPrePeriod").value;
		      var dt1 =  new Date(endDate.replace(/-/g,"\/"));
		      var dt2 =  new Date(startDate.replace(/-/g,"\/"));
		            if(dt1.getTime()>=dt2.getTime()){
		           number = (dt1.getTime()- dt2.getTime())/(24*60*60*1000)
		           }
		            if(uploadPrePeriod>number){
		              MyAlert("超过活动最大天数");
		              uploadPrePeriod.focus();
		              return false;  
		            }
				if(!submitForm('fm')) {
					return false;
				}			
				if(action == 'up'){
			    	 if(document.fm.isClaim.checked==true){
			    		 document.fm.isClaim.value="0";
			    	 }
			    	 if(document.fm.isFixfee.checked==true){
			    		 document.fm.isFixfee.value="1";
			    	 }
			    // disableBtn($("commitBtn"));//点击按钮后，按钮变成灰色不可用;	 
			     MyConfirm("是否重新发布？",serviceActivityManageUpdate);
			     
			}
	}
	
	function serviceActivityManageUpdate(){
			makeNomalFormCall("<%=contextPath%>/claim/serviceActivity/ServiceActivityManageIssued/serviceActivityManageUpdate.json?activityId="+<%=request.getAttribute("activityId")%>,showForwordValue,'fm','queryBtn');
	}
	
function showForwordValue(json){
		if(json.returnValue == '1')
		{
			//MyDivAlert("重新发布成功！");
			//__extQuery__(1);
			goBack();
		}else
		{   
			
			if(json.returnValue == '2'){
				MyAlert("发布失败---执行经销商不存在！");
			}else{
	            if(json.returnValue == '3'&&json.returnValue == '4'&&json.returnValue == '5'){
	            	MyAlert("发布失败---车型、车龄、车辆性质不存在！");
		        }
		        if(json.returnValue == '3'&&json.returnValue != '4'&&json.returnValue != '5'){
		        	MyAlert("发布失败---车型不存在、车龄、车辆性质存在！");
			   }
		        if(json.returnValue != '3'&&json.returnValue == '4'&&json.returnValue != '5'){
		        	MyAlert("发布失败---车龄不存在、车型、车辆性质存在！");
			   }
		        if(json.returnValue != '3'&&json.returnValue != '4'&&json.returnValue == '5'){
		        	MyAlert("发布失败---车辆性质不存在、车型、车龄存在！");
			   }
			    if(json.returnValue == '6'){
			    	MyAlert("发布失败---(车龄,车型,生产基地)与VIN必须存在一个！");
			   }
	       }
			
		}
	}
	//修改 结束
	//服务活动-VIN清单导入开始
	function openVIN(){
		OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityVinImport/serviceActivityVinImportInit.do?activityId='+<%=request.getAttribute("activityId")%>+"&flag=againImport",800,500);
	}
	//服务活动-VIN清单导入结束
	
	function downloadTemplate(){
		fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityVinImport/serviceActivityVinDownLoad.do";
		fm.submit();
	 }
    //返回主页面
    function goBack(){
     	fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageIssued/serviceActivityManageIssuedInit.do";
		fm.submit();
   }
    function viewNews(value){
		OpenHtmlWindow("<%=contextPath%>/claim/basicData/HomePageNews/viewNews.do?comman=2&newsId="+value,800,500);
	}
</script>
</head>

<body onLoad="fix(show)">
<table cellSpacing=0 cellPadding=0 width="100%" border=0>
	<tbody>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td height="30">
			<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动管理</div>
			</td>
		</tr>
	</tbody>
</table>
<form method="post" name="fm" id="fm">
	<input type="hidden" name="activityId" id="activityId" value="<%=request.getAttribute("activityId")%>"/>
	<input type="hidden" id="single" value="<%=ActivityPO.getSingleCarNum()==null?0:ActivityPO.getSingleCarNum()%>" />
	<input type="hidden" id="max" value="<%=ActivityPO.getMaxCar()==null?0:ActivityPO.getMaxCar()%>" />
<table class="table_edit">
    <tr>
		<th colspan="6">
			<img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息
		</th>
	</tr>
	<tr>
		<td width="10%" align="right">活动编号：</td>
		<td width="20%">
			<%=ActivityPO.getActivityCode()%>
		</td>
	</tr>
	<tr>
		<td width="10%" align="right">活动名称：</td>
		<td width="20%">
		  <%=ActivityPO.getActivityName()%>
		</td>
		<td width="10%" align="right">活动类型：</td>
		<td width="20%">
			<script type="text/javascript">
				writeItemValue('<%=ActivityPO.getActivityType()%>')
			</script>
		</td>
	</tr>
	<tr>
	   <td width="10%" align="right">活动类别：</td>
		<td>
		  <script type="text/javascript">
		       writeItemValue('<%=ActivityPO.getActivityKind()%>')
  		  </script>
		</td>
		<td width="10%" align="right">活动日期：</td>
		<td >
			<div align="left">
            	   <input name="startDate" id="t1" value="<%=ActivityPO.getStartdate()%>" type="hidden" class="short_txt" datatype="0,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
            	   <%=ActivityPO.getStartdate()%>
            	 至：<input name="endDate" id="t2" value="<%=ActivityPO.getEnddate()%>" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
            </div>
	    </td>
	</tr>
	<tr>
        <td width="10%" align="right">活动结束日期后&nbsp;&nbsp;</td>
		<td >
			<input type="text" name="uploadPrePeriod" id="uploadPrePeriod" size="20" maxlength="4" value='<%=ActivityPO.getUploadPrePeriod()==null?"":ActivityPO.getUploadPrePeriod()%>' datatype="1,is_digit,18" />
			&nbsp;天内上传
		</td>
		<td width="10%" align="right">处理方式：</td>
		<td >
			    <script type="text/javascript">
			         writeItemValue('<%=ActivityPO.getDealwith()%>')
  		       </script>
	    </td>
	</tr>
	<tr>
        <td width="10%" align="right">单台次活动次次数：</td>
		<td >
			<input type="text" datatype="0,is_digit,4" class="middle_txt" name="single_car" id="single_car" value="<%=ActivityPO.getSingleCarNum()==null?"":ActivityPO.getSingleCarNum()%>"/>
		</td>
		<td width="10%" align="right">单经销商活动总次数：</td>
		<td >
			<input type="text" datatype="0,is_digit,6" class="middle_txt" name="max_car" id="max_car" value="<%=ActivityPO.getMaxCar()==null?"":ActivityPO.getMaxCar()%>"/>
	    </td>
	</tr>
	<tr>
     	<td width="10%" align="right">服务活动车辆范围：</td>
     	<td >
     	  <input type="checkbox" value="<%=beforeVehicle.getCodeId()%>" name="beforeVehicle" id="beforeVehicle"
     	   <%if("11321001".equals(request.getAttribute("vicle1"))) {%>checked="checked"<%}%> disabled="disabled" />
     	  <script type='text/javascript'>
				       var beforeVehicle=getItemValue('<%=beforeVehicle.getCodeId()%>');
				       document.write(beforeVehicle) ;
		 </script>
     	 <input type="checkbox" value="<%=afterVehicle.getCodeId()%>" name="afterVehicle" id="afterVehicle"
     	   <%if("11321002".equals(request.getAttribute("vicle2"))) {%>checked="checked"<%}%> disabled="disabled"  />
     	 <script type='text/javascript'>
				       var afterVehicle=getItemValue('<%=afterVehicle.getCodeId()%>');
				       document.write(afterVehicle) ;
		 </script>
     	</td>
	</tr>
	<tr id=show>
	    <td width="10%" align="right">活动费用：</td>
		<td >
			<%=ActivityPO.getActivityFee()==null?"":ActivityPO.getActivityFee()%>
		</td>
		<!--
		<td class="table_edit_2Col_label_5Letter">配件费用：</td>
		<td align="left">
			<input type="text" name="partFee" id="partFee" class="middle_txt" size="20" value='<%=ActivityPO.getPartFee()==null?"":ActivityPO.getPartFee()%>' datatype="0,is_digit_letter,9" >
		</td>
		<td class="table_edit_2Col_label_5Letter">工时费用：</td>
		<td align="left">
			<input type="text" name="worktimeFee" id="worktimeFee" class="middle_txt" size="20" value='<%=ActivityPO.getWorktimeFee()==null?"":ActivityPO.getWorktimeFee()%>' datatype="0,is_digit_letter,9" >
		</td>
	    -->
	</tr>
	<tr>
		<td width="10%" align="right">&nbsp;</td>
		<td >
			<input type=checkbox name="isClaim" id="isClaim" value="<%=ActivityPO.getIsClaim()==null?"1":ActivityPO.getIsClaim()%>"
			 <%if("0".equals(ActivityPO.getIsClaim())) {%>checked="checked"<%}%> onclick="chickClaim(show)"
			/>索赔</td>
		<td width="10%" align="right">&nbsp;</td>
		<td >
			<input type=checkbox name="isFixfee" id="isFixfee" onclick="chickFixfee(show)" value="<%=ActivityPO.getIsFixfee()==null?"0":ActivityPO.getIsFixfee()%>"
			<%if("1".equals(ActivityPO.getIsFixfee())) {%>checked="checked" <%}%>
			/>索赔是否为固定费用
			            <script type="text/javascript">
	        		if(document.fm.isFixfee.checked==true){
	        			document.fm.isClaim.checked=true;
	        			show.style.display ="block";
	        		}else {
	        			document.getElementById("partFee").value="";
		        		document.getElementById("worktimeFee").value="";
	        		 	show.style.display ="none";
	        		}
            </script>
		</td>
	</tr>
	<tr>
		<td width="10%" align="right">解决方案说明：</td>
		<td colspan="3">
			<textarea name="solution" id="solution" class="SearchInput" rows="8" cols="85" datatype="0,is_digit_letter_cn,200"><%=ActivityPO.getSolution()==null?"":ActivityPO.getSolution()%></textarea>
		</td>
	</tr>
	<tr>
		<td width="10%" align="right">索赔申请指导：</td>
		<td colspan="3" align="left">
			<textarea name="claimGuide" id="claimGuide" class="SearchInput" rows="8" cols="85" datatype="0,is_digit_letter_cn,200"><%=ActivityPO.getClaimGuide()==null?"":ActivityPO.getClaimGuide()%></textarea>
		</td>
	</tr>
	       <tr >
	          <th width="50" align="center" nowrap="nowrap" >NO </th>
	          <th width="220" align="center" nowrap="nowrap" >编码 </th>
	          <th width="400" align="center" nowrap="nowrap" >新闻名称</th>
	          <th width="80" align="center" nowrap="nowrap" >操作 </th>
	        </tr>
	        <c:if test="${!empty listNews}">
	        	<c:forEach var="newDetail" items="${listNews}" varStatus="vs">
	        		<tr >
			          <th width="50" align="center" nowrap="nowrap" >${vs.index+1 } </th>
			          <th width="220" align="center" nowrap="nowrap" >${newDetail.NEWS_CODE } </th>
			          <th width="400" align="center" nowrap="nowrap" >${newDetail.NEWS_TITLE }</th>
			          <th width="80" align="center" nowrap="nowrap" ><a href="#" onclick='viewNews(${newDetail.NEWS_ID })'>查看</a></th>
			        </tr>
	        	</c:forEach>
	        </c:if>
</table>
	

<br/>
<table class="table_edit">
	<tr>
		<td colspan="4" align="center"></td>
	</tr>
	<tr>
		<td width="1000"  align="center">
			<input type="button" name="vin_insert" class="long_btn" onclick="openVIN();" value="活动VIN导入" />
			<!--<input type="button" name="vin_load" class="long_btn"   onclick="downloadTemplate();" value="下载VIN模版" /> -->
			<input type="button" name="bt_add" class="long_btn" onclick="subChecked('up');" value="重新发布" id="commitBtn"/>
		    <input type="button" name="bt_back" class="normal_btn" onclick="javascript:history.go(-1)" value="返回"/>
		</td>
	</tr>
</table>
</form>
</body>
</html>

 <!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.bean.TtAsActivityBean"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c" %>


<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动管理</title>
<% String contextPath = request.getContextPath(); %>
<% TtAsActivityBean ActivityPO=(TtAsActivityBean)request.getAttribute("ActivityPO");%>
<%
TtAsActivityBean beforeVehicle = (TtAsActivityBean) request.getAttribute("beforeVehicle");//服务活动信息-服务车辆活动范围
TtAsActivityBean afterVehicle = (TtAsActivityBean) request.getAttribute("afterVehicle");//服务活动信息-服务车辆活动范围
List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");
%>	
<script type="text/javascript">
    //日历控件初始化
	function doInit()
		{
		   loadcalendar();
		   checkDirect();
		   checkType();
		}
	//索赔选择
	function chickFixfee(show){
		if(document.fm.isFixfee.checked==true){
			document.fm.isClaim.checked=true;
			show.style.display ="block";
			document.getElementById("showItem").style.display ="none";//活动项目隐藏
		}else {
		 show.style.display ="none";
		 document.getElementById("showItem").style.display ="inline";//活动项目显示
		//document.fm.partFee.value =0.0;
        //document.fm.worktimeFee.value =0.0;
		}
	}
	function chickClaim(show){
		if(document.fm.isClaim.checked==false&&document.fm.isFixfee.checked==true){
			document.fm.isClaim.checked=true;
		}
		if(document.fm.isClaim.checked==true){
		 document.getElementById("showItem").style.display ="inline";//活动项目显示
		}else{
		 document.getElementById("showItem").style.display ="none";//活动项目隐藏
		}
		if(document.fm.isClaim.checked==true&&document.fm.isFixfee.checked==true){
		     document.getElementById("showItem").style.display ="none";//活动项目隐藏
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
	//修改 开始
	function serviceActivityManageUpdate(){
			fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/serviceActivityManageUpdate.do?activityId="+<%=ActivityPO.getActivityId()%>+"&flag=add";
			fm.submit();
	}
	
		function subChecked(action){
			/*JS取日期之间的相差的天数*/
		      var number;
		      var endDate=document.getElementById("endDate").value;
		      var startDate=document.getElementById("startDate").value;
		      var uploadPrePeriod=document.getElementById("uploadPrePeriod").value;
		      var dt1 =  new Date(endDate.replace(/-/g,"\/"));
		      var dt2 =  new Date(startDate.replace(/-/g,"\/"));
		            if(dt1.getTime()>=dt2.getTime()){
		           number = (dt1.getTime()- dt2.getTime())/(24*60*60*1000)
		           }
		           // if(uploadPrePeriod>number){
		           //   MyAlert("超过活动最大天数");
		           //   uploadPrePeriod.focus();
		           //   return false;  
		           // }
		    //服务活动车辆范围开始
		    var beforeVehicle =document.fm.beforeVehicle;
		    var afterVehicle =document.fm.afterVehicle;
			if(beforeVehicle.checked==false&&afterVehicle.checked==false){
				MyAlert("请选择服务活动车辆范围！");
				return false;
			}			
			//服务活动车辆范围结束
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
		     //disableBtn($("commitBtn"));//点击按钮后，按钮变成灰色不可用;
		     MyConfirm("是否确认修改？",serviceActivityManageUpdate);
		     
		}
	}
		    
	//修改 结束
	服务活动-车型列表开始
	function openModel(){
		OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageModel/serviceActivityManageModelQuery.do?activityId='+<%=ActivityPO.getActivityId()%>+"&flag=s",800,500);
	}
	/**
	 * 服务活动车型：选择物料组树界面
	 * inputId   : 回填页面物料组code域id
	 * inputName ：回填页面物料组name域id
	 * isMulti   : true值多选，否则单选
	 * groupLevel：输出的物料组等级
	 */
	function serviceShowMaterialGroup(inputCode ,inputName ,isMulti ,groupLevel,activityId)
	{
		if(!inputCode){ inputCode = null;}
		if(!inputName){ inputName = null;}
		if(!groupLevel){ groupLevel = null;}
		if(!activityId){ activityId = null;}
		OpenHtmlWindow("<%=contextPath%>/claim/serviceActivity/ServiceActivityManageModel/serviceGroupListQuery.do?INPUTID="+inputCode+"&INPUTNAME="+inputName+"&ISMULTI="+isMulti+"&GROUPLEVEL="+groupLevel+"&ACTIVITYID="+activityId,770,410);
	}
	//服务活动-车型列表结束
	//服务活动-车龄定义列表开始
	function openVehicleAge(){
	    var beforeVehicle ="";
	    if(document.fm.beforeVehicle.checked==true){
	    	beforeVehicle =document.fm.beforeVehicle.value;
	    }
		OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageVehicleAge/serviceActivityManageVehicleAgeQuery.do?activityId='+<%=ActivityPO.getActivityId()%>+'&beforeVehicle='+beforeVehicle,500,300);
	}
	//服务活动-车龄定义列表结束
	//服务活动-车辆性质列表开始
	function openCharactor(){
	    var beforeVehicle =document.fm.beforeVehicle;
	    if(beforeVehicle.checked==true){
				MyAlert("选择售前车不能维护车辆性质！");
				return false;
			}	
		OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageCharactor/serviceActivityManageCharactorQuery.do?activityId='+<%=ActivityPO.getActivityId()%>,800,500);
	}
	//服务活动-活动项目列表开始
	function openItem(){
		OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageItem/serviceActivityManageItemQuery.do?activityId='+<%=ActivityPO.getActivityId()%>,800,500);
	}
	
	//服务活动-活动项目列表结束
	//服务活动-生产基地列表开始
	function openProduceBase(){
		OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageProduceBase/serviceActivityManageProduceBaseQuery.do?activityId='+<%=ActivityPO.getActivityId()%>,800,500);
	}
	
	//服务活动-生产基地列表结束
	//服务活动-VIN清单导入开始
	function openVIN(){
		OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityVinImport/serviceActivityVinImportInit.do?activityId='+<%=ActivityPO.getActivityId()%>,800,500);
	}
	//服务活动-VIN清单导入结束
	
	function downloadTemplate(){
		fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityVinImport/serviceActivityVinDownLoad.do";
		fm.submit();
	 }
	//活动类别下拉框onchange事件
	function doMySel(value){
		if(value=='<%=Constant.SERVICEACTIVITY_KIND_02%>'){
			$('isFixfee').checked = true ;
			chickFixfee(show) ;
			$('isFixfee').disabled = 'disabled' ;
		}else{
			$('isFixfee').checked = false ;
			chickFixfee(show) ;
			$('isFixfee').disabled = false ;
		}
	}
	// 生产基地下拉框显示控制
	function showYieldly(value){
		if(value==1)
			$('yieldly1').style.display = 'block' ;
		else
			$('yieldly1').style.display = 'none' ;
	}
	// 活动类别的联动
	function checkType(){
		if('<%=Constant.SERVICEACTIVITY_KIND_02%>'=='<%=ActivityPO.getActivityKind()%>'){
			$('show').style.display='block';
			$('activityFee').value = 0 ;
			$('activityFee').disabled = 'disabled' ;
			$('isFixfee').checked = true ;
			$('isFixfee').disabled = true ;
		}
	}
	checkType();
	// 结算指向的联动
	function checkDirect(){
		if('0'!='<%=ActivityPO.getSetDirect()%>'){
			$('default').checked = false ;
			$('directed').checked = true ;
			$('yieldly1').style.display = 'block' ;
		}			
	}
	checkDirect();

	function openMilage(){
		var 	activeId = $('activityId').value 
			OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/getActivityMilage.do?activeId='+activeId,800,500);
		}
	function showFree(free){
	  $('activityFee').value = free;
	}	
</script>
</head>

<body onLoad="fix(show);checkHiddenItem();">
<table cellSpacing=0 cellPadding=0 width="100%" border=0>
	<tbody>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td height="30">
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动管理
			</div>
			</td>
		</tr>
	</tbody>
</table>
<form method="post" name="fm" id="fm">
<table class="table_edit">
    <tr>
		<th colspan="6">
			<img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息
		</th>
	</tr>
	<tr>
		<td width="10%" align="right">活动编号：</td>
		<td width="20%">
		<input type="hidden" name="activityId" id="activityId" value="<%=request.getAttribute("activityId")%>"  />
		<input type="hidden" name="attIds" id="attIds" value=""/><!-- 删除附件隐藏 -->
		<%=ActivityPO.getActivityCode()%>
			<!--  <input name="activityCode" id="activityCode" value="" type="text" class="middle_txt" datatype="0,is_digit_letter,18" >-->
		</td>
		
	</tr>
	<tr>
	   <td width="10%" align="right">活动名称：</td>
		<td width="20%">
		   <input name="activityName" id="activityName" value="<%=ActivityPO.getActivityName()%>" type="text" class="middle_txt" datatype="0,is_null,30" >
		</td>
		<td width="10%" align="right">活动类型：</td>
		<td width="20%">
		   <script type="text/javascript">
   					genSelBoxExp("activityType",<%=Constant.SERVICEACTIVITY_TYPE %>,"<%=ActivityPO.getActivityType()%>",false,"short_sel","","false",'');
  		   </script>
		</td>
		
	</tr>
	<tr>
	   <td width="10%" align="right">活动类别：</td>
		<td width="20%">
		  <script type="text/javascript">
   					genSelBoxExp2("activityKind",<%=Constant.SERVICEACTIVITY_KIND%>,"<%=ActivityPO.getActivityKind()%>",false,"short_sel","","false",'');
  		  </script>
		</td>
		<td width="10%" align="right" >活动日期：</td>
		<td width="20%">
			<div align="left">
              <input name="startDate" id="t1" value="<%=ActivityPO.getStartdate()%>" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
               至 <input name="endDate" id="t2" value="<%=ActivityPO.getEnddate()%>" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
            </div>
	    </td>
	</tr>
	<tr>
	    <td width="10%" align="right">距活动结束后&nbsp;&nbsp;</td>
		<td width="20%">
			<input type='text' class='middle_txt' name='uploadPrePeriod' id='uploadPrePeriod' size='20' maxlength='4' value='<%=ActivityPO.getUploadPrePeriod()==null?"":ActivityPO.getUploadPrePeriod()%>' datatype="1,is_digit,18" />
			天内上传总结
		</td>
		<td width="10%" align="right">处理方式：</td>
		<td width="20%">
			    <script type="text/javascript">
   					genSelBoxExp("dealwith",<%=Constant.SERVICEACTIVITYDEAL_WITH%>,"<%=ActivityPO.getDealwith()%>",false,"short_sel","","false",'');
  		       </script>
	    </td>
	</tr>
	<tr>
		<td width="10%" align="right">单台次活动次数：</td>
		<td width="20%">
			<input type="text" class="middle_txt" name="single_num" id="single_num" datatype="0,is_digit,4" value="<%=ActivityPO.getSingleCarNum()==null?"":ActivityPO.getSingleCarNum()%>"/>
		</td>
		<td width="10%" align="right">单经销商活动总次数：</td>
		<td>
			<input type="text" class="middle_txt" name="car_max" id="car_max" datatype="0,is_digit,6" value="<%=ActivityPO.getMaxCar()==null?"":ActivityPO.getMaxCar()%>"/>
		</td>
	</tr>
	<tr>
     	<td  width="10%" align="right" >服务活动车辆范围：</td>
     	<td width="20%">
     	  <input type="checkbox" value="<%=beforeVehicle.getCodeId()%>" name="beforeVehicle" id="beforeVehicle" 
     	   <%if("11321001".equals(request.getAttribute("vicle1"))) {%>checked="checked"<%}%>  />
     	  <script type='text/javascript'>
				       var beforeVehicle=getItemValue('<%=beforeVehicle.getCodeId()%>');
				       document.write(beforeVehicle) ;
		 </script>
     	 <input type="checkbox" value="<%=afterVehicle.getCodeId()%>" name="afterVehicle" id="afterVehicle"
     	   <%if("11321002".equals(request.getAttribute("vicle2"))) {%>checked="checked"<%}%>  />
     	 <script type='text/javascript'>
				       var afterVehicle=getItemValue('<%=afterVehicle.getCodeId()%>');
				       document.write(afterVehicle) ;
		 </script>
		 <span style="color:red">*</span>
     	</td>
     	<td width="10%" align="right">结算指向：</td>
     	<td width="20%">
     		<input type="radio" name="default" id="default" value="1" checked onclick="showYieldly(0);"/>按生产基地结算
     		<input type="radio" name="default" id="directed" value="2" onclick="showYieldly(1);"/>定向结算
     		<span id="yieldly1" style="display:none">
     			<script type="text/javascript">
					genSelBoxExp("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"<%=ActivityPO.getSetDirect()%>",false,"short_sel","","true",'');
				</script>
     		</span>
     	</td>
	</tr>
	<tr id="show">
	    <td width="10%" align="right" >活动费用：</td>
		<td width="20%">
		<input type="hidden" id="milage" value="<%=ActivityPO.getMilageConfine() %>"> 
			<input type="text" name="activityFee" id="activityFee"  value='<%=ActivityPO.getActivityFee()%>'  class="middle_txt" datatype="0,is_double,9">
		</td>
		<td width="10%" align="right" >里程限制：</td>
		<td width="20%">
	<input type="radio" name="misage" id="a" value="<%=Constant.ACTIVI_MILAGE_YES %>" onClick="y_e();"/>是
     		<input type="radio" name="misage" id="b" value="<%=Constant.ACTIVI_MILAGE_NO %>" onClick="n_o();" checked/>否
     	</td>
		<!--
		<td class="table_edit_2Col_label_5Letter">配件费用：</td>
		<td align="left">
			<input type="text" name="partFee" id="partFee" class="middle_txt" size="20" value='<%=ActivityPO.getPartFee()==null?"":ActivityPO.getPartFee()%>' datatype="0,is_double,7" >
		</td>
		<td class="table_edit_2Col_label_5Letter">工时费用：</td>
		<td align="left">
			<input type="text" name="worktimeFee" id="worktimeFee" class="middle_txt" size="20" value='<%=ActivityPO.getWorktimeFee()==null?"":ActivityPO.getWorktimeFee()%>' datatype="0,is_double,4" >
		</td>
		-->
	</tr>
	<tr>
		<td width="10%" align="right">&nbsp;</td>
		<td width="20%">
			<input type=checkbox name="isClaim" id="isClaim" value="<%=ActivityPO.getIsClaim()==null?"1":ActivityPO.getIsClaim()%>"
			 <%if("0".equals(ActivityPO.getIsClaim())) {%>checked="checked"<%}%>  onclick="chickClaim(show)"
			/>索赔</td>
		<td width="10%" align="right">&nbsp;</td>
		<td width="20%">
			<input type=checkbox name="isFixfee" id="isFixfee" onclick="chickFixfee(show)" value="<%=ActivityPO.getIsFixfee()==null?"0":ActivityPO.getIsFixfee()%>"
			<%if("1".equals(ActivityPO.getIsFixfee())) {%>checked="checked" <%}%>
			/>索赔是否为固定费用
			            <script type="text/javascript">
	        		if(document.fm.isFixfee.checked==true){
	        			document.fm.isClaim.checked=true;
	        			show.style.display ="block";
	        		}else {
	        		    document.getElementById("activityFee").value="";
		        		//document.getElementById("partFee").value="";
		        		//document.getElementById("worktimeFee").value="";
	        		 	show.style.display ="none";
	        		}
            </script>
		</td>
	</tr>
	
	<tr>
		<td width="10%" align="right">解决方案说明：</td>
		<td colspan="3" align="left">
			<textarea name="solution" id="solution" class="SearchInput" rows="8" cols="90" datatype="0,is_null,200"><%=ActivityPO.getSolution()==null?"":ActivityPO.getSolution()%></textarea>
		</td>
	</tr>
	<tr>
		<td width="10%" align="right">索赔申请指导：</td>
		<td colspan="3" align="left">
			<textarea name="claimGuide" id="claimGuide" class="SearchInput" rows="8" cols="90" datatype="0,is_null,200"><%=ActivityPO.getClaimGuide()==null?"":ActivityPO.getClaimGuide()%></textarea>
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
		<td colspan="4" align="center">
			<input type="button" name="bt_add" class="long_btn" onclick="subChecked('up');" value="提交主数据" id="commitBtn"/>
		    <input type="button" name="bt_back" class="normal_btn" onclick="javascript:history.go(-1)" value="返回"/>
	    </td>
	</tr>
	<tr>
		<td colspan="4" align="center"></td>
	</tr>
	<tr>
		<td width="1000"  align="center">
			<input type="button" name="type_add" class="normal_btn"  onclick="openModel();" value="车型" /> 
			<!-- <input type="button" name="type_add" class="normal_btn"  onclick="serviceShowMaterialGroup('groupCode','','true','3','<%=ActivityPO.getActivityId()%>')"  value="车型" /> -->
			<input type="button" id="milage_add" name="milage_add" class="normal_btn"   onclick="openMilage();" value="里程限制" /> 
			<input type="button" name="age_add" class="normal_btn"   onclick="openVehicleAge();" value="车龄" /> 
			<input type="button" name="veh_add" class="normal_btn"  onclick="openCharactor();" value="车辆性质" /> 
			<input type="button" id="showItem" name="showItem" class="normal_btn"  onclick="openItem();" value="活动项目" />
			<input type="button" name="act_add" class="long_btn"  onclick="openProduceBase();" value="生产基地" />
			<input type="button" name="vin_insert" class="long_btn" onclick="openVIN();" value="活动VIN导入" />
			<!-- <input type="button" name="vin_load" class="long_btn"   onclick="downloadTemplate();" value="下载VIN模版" /> -->
		</td>
	</tr>
</table>
<script type="text/javascript">
//1.当服务活动为索赔时显示“活动项目”按钮;
//2.当服务活动为索赔是否为固定费用时，隐藏“活动项目”按钮;
//3.当默认状态下 “活动项目”按钮隐藏;

  function checkHiddenItem(){
	    if("0"=='<%=ActivityPO.getIsClaim()%>'){//当服务活动为索赔时显示“活动项目”按钮;
	          document.getElementById("showItem").style.display  ="inline";
	 	}
	   if("0"=='<%=ActivityPO.getIsClaim()%>'&&"1"=='<%=ActivityPO.getIsFixfee()%>'){
	 	      document.getElementById("showItem").style.display  ="none";
	 	}
	 	if("0"!='<%=ActivityPO.getIsClaim()%>'){//当服务活动不是索赔时隐藏“活动项目”按钮;
	          document.getElementById("showItem").style.display  ="none";
	 	}
 }
  //售前车
 function checkSelect(){
    if("11321001"==<%=request.getAttribute("vicle1")%>){
      document.fm.beforeVehicle.checked=true;
    }
   
 }
 if($('milage').value==1){
		$('a').checked=true;
		
}else{
	  $('b').checked=true;
	  $('milage_add').disabled=true;
	}
 function y_e(){
		$('milage_add').disabled=false;
	}
	 function n_o(){
		 $('milage_add').disabled=true;
		 }


  function viewNews(value){
	OpenHtmlWindow("<%=contextPath%>/claim/basicData/HomePageNews/viewNews.do?comman=2&newsId="+value,800,500);
  }
</script>
</form>
</body>
</html>
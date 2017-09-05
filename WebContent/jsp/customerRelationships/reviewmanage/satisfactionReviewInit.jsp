<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import=" com.infodms.dms.common.Constant"%>
<%@ page import="java.util.Date" %>
    <%@page import="java.util.List"%>
    <%@page import="java.util.Map"%>
<%@taglib uri="/jstl/cout" prefix="c" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<script type="text/javascript">
function doInit()
{
	loadcalendar();
}
</script>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>我的客户回访</title>
<% String contextPath = request.getContextPath();
	List<Map<String,Object>> list = (List<Map<String,Object>>)request.getAttribute("stations");
%>
</head>
<body >
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户化关系管理 &gt; 回访管理 &gt;满意度回访
</div>
<form method="post" name="fm" id="fm">
<input type="hidden" id="isClick" name="isClick" />
    <input type="hidden" id="sss" name="sss" value=''/>
<TABLE class=table_query>
  <TBODY>
    <tr class="">
      <td width="15%" align="right" nowrap="nowrap">维修日期：</td>
      <td width="24%"><input name="checkSDate" class="short_txt" id="checkSDate" maxlength="10" group="checkSDate,checkEDate" datatype="1,is_date,10" />
        <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" type="button" value=" " />
 至 
<input name="checkEDate" class="short_txt" id="checkEDate" maxlength="10" group="checkSDate,checkEDate" datatype="1,is_date,10" />
<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" type="button" value=" " /></td>
      <td align="right"> VIN号：</td>
      <td><input  id="VIN" class="middle_txt"  name="VIN"  /></td>
      <td align="right">&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr class="">
      <td align="right" nowrap="nowrap">汽车种类：</td>
      <td><select id="ddlClasses" class="short_sel" name="ddlClasses">
        <option selected="selected" value="">--请选择--</option>
       <c:if test="${seriesList!=null}">
			<c:forEach items="${seriesList}" var="list3">
				<option value="${list3.GROUP_NAME}">${list3.GROUP_NAME}</option>
			</c:forEach>
		</c:if>
      </select></td>
      <td width="10%" align="right">汽车型号：</td>
      <td><input type="text" id="ddlAutoTypeCode" name="ddlAutoTypeCode" class="middle_txt"/></td>
      <!-- <td width="21%"><select id="ddlAutoTypeCode" class="short_sel" name="ddlAutoTypeCode">
        <option selected="selected" value="">--请选择--</option>
       <c:if test="${vechileTypeList!=null}">
			<c:forEach items="${vechileTypeList}" var="vl">
				<option value="${vl.GROUP_ID}">${vl.GROUP_NAME}</option>
			</c:forEach>
		</c:if>
      </select></td> -->
      <td width="15%" align="center">&nbsp;</td>
      <td width="15%" align="center"><span class="table">
        <input name="button433" type="button" class="normal_btn" onclick="queryPer();" align="right" value="查询" />
      </span> </td>
    </tr>
    <tr class="">
      <td align="right">服务站类型：</td>
      <td colspan="3"><table cellspacing="0" cellpadding="0" class="">
        <!--<tr>
          <td><input id="chkServiceStationType_0" type="checkbox" name="chkServiceStationType"/>
            <label for="chkServiceStationType_0">4S店</label></td>
          <td><input id="chkServiceStationType_1" type="checkbox" name="chkServiceStationType"/>
            <label for="chkServiceStationType_1">标准4S店</label></td>
          <td><input id="chkServiceStationType_2" type="checkbox" name="chkServiceStationType"/>
            <label for="chkServiceStationType_2">基本4S店</label></td>
          <td><input id="chkServiceStationType_3" type="checkbox" name="chkServiceStationType" />
            <label for="chkServiceStationType_3">二级站</label></td>
          <td><input id="chkServiceStationType_4" type="checkbox" name="chkServiceStationType" />
            <label for="chkServiceStationType_4">快修店</label></td>
          <td><input id="chkServiceStationType_5" type="checkbox" name="chkServiceStationType" />
            <label for="chkServiceStationType_5">专修一类</label></td>
          <td><input id="chkServiceStationType_6" type="checkbox" name="chkServiceStationType" />
            <label for="chkServiceStationType_6">专修二类</label></td>
        </tr>  -->
        <tr>
        	<%if(list!=null && list.size()>0){ %>
        		<%for(int i=0;i<list.size();i++){ %>
        		<td><input id="chkServiceStationType_<%=i %>" type="checkbox" name="chkServiceStationType" value="<%=list.get(i).get("CODE_ID") %>"/>
            		<label for="chkServiceStationType_<%=i %>"><%=list.get(i).get("CODE_DESC") %></label></td>
            	<%} %>
            <%} %>
        </tr>
      </table></td>
      <td align="center">&nbsp;</td>
      <td align="center"> </td>
    </tr>
    <tr class="">
      <td colspan="2" align="right">&nbsp;</td>
      <td colspan="2" align="left"><span class="table">
        <input name="button" type="button" class="normal_btn" align="right" value="设置" onclick="setReview();"/>
      </span>
      <input type="text" style="WIDTH: 80px" id="txtRand"  name="txtRand" onafterpaste="this.value=this.value.replace(/\D/g,'');"  onkeyup="this.value=this.value.replace(/\D/g,'');"  value="" />
        <!--  input id="txtRandom" style="WIDTH: 80px" name="txtRandom" />-->
        <span class="table">
        <input type="hidden" id ="Results" name="Results" value="" />
        <input type="hidden" id ="VINS" name="VINS" value="" />
        <input name="button2" type="button" class="normal_btn" align="right" value="随机选择" onclick="getOpt();"/>
        </span><span id="RegularExpressionValidator1" style="COLOR: red; DISPLAY: none" controltovalidate="txtRandom" errormessage="请输入一个数字" display="Dynamic" validationexpression="^[0-9]*[1-9][0-9]*$" isvalid="true">请输入一个数字</span> <span id="RequiredFieldValidator1" style="COLOR: red; DISPLAY: inline" controltovalidate="txtRandom" errormessage="请输入一个数字" initialvalue="initialvalue" display="Dynamic" isvalid="false">请输入一个数字</span></td>
      <td align="center">&nbsp;</td>
      <td align="center"> </td>
    </tr>
    </TBODY>
</TABLE>
<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
 <!--分页 end --> 
</form> 
<br/>
<!--页面列表 begin -->
<script type="text/javascript" >
var myPage;
//查询路径
	var url = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/reviewSatisfactionQuery.json";
	var title = null;
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"code\")' />全选", align:'center',sortable:false, dataIndex:'CTM_ID',width:'2%',renderer:checkBoxShow},
				{header: "单据号码",sortable: false,dataIndex: 'RO_NO',renderer:setIsClick,align:'center'}, 
				//{header: "用户姓名 ",sortable: false,dataIndex: 'OWNER_NAME',align:'center'},
				{header: "用户姓名 ",sortable: false,dataIndex: 'CTM_NAME',align:'center'},
				//{header: "ID ",sortable: false,dataIndex: 'CTM_ID',align:'center'},
				//{header: "vin ",sortable: false,dataIndex: 'VIN',align:'center'},
				{header: "维修类型",sortable: false,dataIndex: 'REPAIR_TYPE_CODE',align:'center'},
				{header: "维修日期",sortable: false,dataIndex: 'RO_CREATE_DATE',align:'center'},
				{header: "汽车型号",sortable: false,dataIndex: 'MODEL_CODE',align:'center'},
				{header: "汽车种类",sortable: false,dataIndex: 'SERIES_NAME',align:'center'},
				{header: "公里数",sortable: false,dataIndex: 'TOTAL_MILEAGE',align:'center'},
				{header: "服务站",sortable: false,dataIndex: 'DEALER_NAME',align:'center'}
		      ];
	//设置复选框
	function checkBoxShow(value,meta,record){
		return String.format("<input type='checkbox' id='code' name='code' value='" + record.data.CTM_ID + "' /><input type='hidden' value='"+record.data.VIN+"' name='vin'/>");
	}
	function setIsClick(value,meta,record){
		document.fm.isClick.value='true';
		return record.data.RO_NO;
	}
	//获取多选框所选的ID
    var ids = new Array();
    var vins = new Array();
	function getOpt(){
		var cnt = 0;
		var codes = document.getElementsByName("code");
		var vinId = document.getElementsByName("vin");
		var l = codes.length;
		ids.splice(0,ids.length);
		for(var i=0;i<l;i++){
			if(codes[i].checked){
				cnt++;
				//MyAlert(codes[i].value+":::"+vinId[i+1].value);
				ids.push(codes[i].value);
				vins.push(vinId[i+1].value);
			}
		}
		if(cnt==0){
			//不选为全选
			radomSel();
		}else{
			//从选中的数据中选择
			selRandom(cnt,ids,vins);
		}
	}
	//
	function radomSel()
	{
		var ischeck=document.fm.isClick.value;
    	if(null==ischeck||"".indexOf(ischeck)==0)
    	{
    		MyAlert("请先进行查询再点击设置按钮！");
	       	return false;
    	}
		var ram=document.fm.txtRand.value;
		var t=document.fm.sss.value;
		if(t!=null)
		{
			if(ram==null)
			{
				MyAlert('请输入一个随机数字！');
				return false;
			}else if(ram<=0){
				MyAlert('请输入一个大于0的数字！');
				return false;
			}else if (ram>tot(json))
			{
				MyAlert('请输入一个小于'+tot(json)+'的数字！');
				return false;
			}
	
		}else{
			MyAlert('请先进行查询再随机选择！');
			return false;
		}	
		radomSelSubmit();
	}

	function radomSelSubmit() {
			var url="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/satisRandSel.json";
			makeNomalFormCall(url,showResult33,'fm');
	    }
	    
	function showResult33(json,url){
		var msg=json.msg;
		if(msg=='01'){
			MyAlert('随机选择成功');
			document.fm.Results.value=json.ids;
			document.fm.VINS.value=json.vins;
			//MyAlert(json.ids);
			__extQuery__(1);
		}else{
			MyAlert('随机选择失败,请联系管理员');
		}	
	}
	//从多选框中选择
	function selRandom(cnt,ids,vins){
		var ischeck=document.fm.isClick.value;
    	if(null==ischeck||"".indexOf(ischeck)==0)
    	{
    		MyAlert("请先进行查询再点击设置按钮！");
	       	return false;
    	}
		var ram=document.fm.txtRand.value;
		var t=document.fm.sss.value;
		if(t!=null)
		{
			if(ram==null)
			{
				MyAlert('请输入一个随机数字！');
				return false;
			}else if(ram<=0){
				MyAlert('请输入一个大于0的数字！');
				return false;
			}else if (ram>cnt)
			{
				MyAlert('请输入一个小于'+cnt+'的数字！');
				return false;
			}
	
		}else{
			MyAlert('请先进行查询再随机选择！');
			return false;
		}	
		//随机选 Results里面存的值？？
		var return_array = new Array();
		var return_vin = new Array();
	    for (var i = 0; i<ram; i++) {
	        if (ids.length>0) {
	            var arrIndex = Math.floor(Math.random()*ids.length);
	            return_array[i] = ids[arrIndex];
	            return_vin[i] = vins[arrIndex];
	            //
	            //MyAlert(return_array[i]+":"+return_vin[i]);
	            ids.splice(arrIndex, 1);
	            vins.splice(arrIndex,1);
	        } else {
	            break;
	        }
	    }
	    var re_ids = return_array.join(",");
	    var re_vins = return_vin.join(",");
	    MyAlert('随机选择成功');
	    document.fm.Results.value=re_ids;
	    document.fm.VINS.value=re_vins;
	}

	 //设置操作
    function setReview() {
    	var results=document.fm.txtRand.value;
    	if(null==results||"".indexOf(results)==0)
    	{
    		MyAlert("请先进行随机选择再点击设置按钮！");
	       	return false;
    	}
    	var resu=document.fm.Results.value;
    	if(null==resu||"".indexOf(resu)==0)
    	{
    		MyAlert("数据有误，请重新进行随机选择！");
	       	return false;
    	}
    	if(resu != null){
    		checkReview();
    	}
	}
    function checkReview(){
		var url="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/checkSatisReview.json";
		makeNomalFormCall(url,confirmReview,'fm');
	}
    function confirmReview(){
		var msg = json.msg;
		if(msg!=null&&msg=="no"){
			MyAlert("所选客户今天已经生成满意度回访，请重新选择回访客户！");
			return false;
		}else if(msg!=null&&msg=="yes"){
			MyConfirm("确认设置?", setSubmit);
		}
	}
	function setSubmit() {
		var url="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/generateSatisReview.json";
		makeNomalFormCall(url,showResult22,'fm');
    }
    
    function showResult22(json){
		var msg=json.msg;
		var count=json.count;
		if(msg=='01'){
			MyAlert('成功设置[ '+count+' ]个满意度回访！');
			document.fm.Results.value='';	
			document.fm.VINS.value=''
			//__extQuery__(1);
		}else{
			MyAlert('设置失败,请联系管理员');
		}
	}
    function tot(json)
	{
		if(null==json)
		{
			MyAlert('请先点查询数据！');
			return false;
		}else{
			var tot=json.tot;
			return tot;
		}
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
  $('checkSDate').value=showMonthFirstDay();
  $('checkEDate').value=showMonthLastDay();
  
  function queryPer(){
	
  	var star1 = $('checkSDate').value;
    var end1 =  $('checkEDate').value;
	  if(star1==""||end1==""){
	  	MyAlert("查询时间必须选择");
	 	 return false;
	  }else if((star1>end1)){
	  	MyAlert("开始时间不能大于结束时间");
	  	return false;
	  }else {
		var s3 = star1.replace(/-/g, "/");
		var s4 = end1.replace(/-/g, "/");
		var d3 = new Date(s3);
		var d4 = new Date(s4);
		var time1= d4.getTime() - d3.getTime();
		var days1 = parseInt(time1 / (1000 * 60 * 60 * 24));
		if(days1>=93){
			MyAlert("时间跨度不能超过3个月");
	  		return false;
		}
	 	 __extQuery__(1);
	  }
	}
</script>  
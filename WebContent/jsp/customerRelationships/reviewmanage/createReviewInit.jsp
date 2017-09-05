<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import=" com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>  
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
<% String contextPath = request.getContextPath(); %>
</head>
<body >
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：当前位置： 客户化关系管理 &gt; 回访管理 &gt;生成客户回访
</div>
  <form method="post" name="fm" id="fm">
    <input type="hidden" id="isClick" name="isClick" />
    <input type="hidden" id="sss" name="sss" value=''/>
 <TABLE class=table_query>
  <TBODY>
    <tr class="">
      <td width="15%" align="right" nowrap="nowrap">购买日期：</td>
      <td width="24%"><input name="checkSDate" class="short_txt" id="checkSDate" maxlength="10" group="checkSDate,checkEDate" datatype="1,is_date,10" readonly />
        <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" type="button" value=" " />
        至 
        <input name="checkEDate" class="short_txt" id="checkEDate" maxlength="10" group="checkSDate,checkEDate" datatype="1,is_date,10" readonly />
        <input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" type="button" value=" " /></td>
      <td align="right">维修日期：</td>
      <td><input name="roSDate" class="short_txt" id="roSDate" maxlength="10" group="roSDate,roEDate" datatype="1,is_date,10" readonly />
        <input class="time_ico" onclick="showcalendar(event, 'roSDate', false);" type="button" value=" " />
        至 
        <input name="roEDate" class="short_txt" id="roEDate" maxlength="10" group="roSDate,roEDate" datatype="1,is_date,10" readonly />
        <input class="time_ico" onclick="showcalendar(event, 'roEDate', false);" type="button" value=" " /></td>
    </tr>
    <tr class="">
      <td align="right" nowrap="nowrap">汽车型号：</td>
      <td><input type="text" id="ddlAutoTypeCode" name="ddlAutoTypeCode" class="middle_txt"/></td>
      <!--  <td><select id="ddlAutoTypeCode" class="short_sel" name="ddlAutoTypeCode">
        <option selected="selected" value="">--请选择--</option>
        	<c:if test="${vechileTypeList!=null}">
			<c:forEach items="${vechileTypeList}" var="vl">
				<option value="${vl.GROUP_ID}">${vl.GROUP_NAME}</option>
			</c:forEach>
			</c:if>
      </select></td> -->
      <td width="10%" align="right">汽车种类：</td>
      <td width="31%"><select id="ddlClasses" class="short_sel" name="ddlClasses">
          <option selected="selected" value="">--请选择--</option>
          <c:if test="${seriesList!=null}">
            <c:forEach items="${seriesList}" var="list3">
              <option value="${list3.GROUP_NAME}">${list3.GROUP_NAME}</option>
            </c:forEach>
          </c:if>
        </select></td>
    </tr>
    <tr class="">
      <td align="right">用户级别：</td>
      <td><script type="text/javascript">
           genSelBoxExp("txtUserLevel",<%=Constant.GUEST_STARS%>,null,true,"short_sel","","false",'');
		</script></td>
      <td align="right">VIN号：</td>
      <td><input id="txtBatholithNum" class="middle_txt"  name="txtBatholithNum" /></td>
    </tr>
    <tr class="">
      <td align="right">地区：</td>
      <td><select class="short_sel" id="downtown" name="downtown">
          <option value=''>-请选择-</option>
          <c:forEach var="pro" items="${proviceList}">
            <option value="${pro.REGION_CODE}" title="${pro.REGION_NAME}">${pro.REGION_NAME}</option>
          </c:forEach>
        </select></td>
      <td align="right">用途：</td>
      <td><script type="text/javascript">
           genSelBoxExp("txtPurpose",<%=Constant.SERVICEACTIVITY_CHARACTOR%>,null,true,"short_sel","","false",'');
	</script>
    </tr>
    <tr class="">
      <td align="right">客户类型：</td>
      <td>
      	<script type="text/javascript">
		   genSelBoxExp("customer_type",<%=Constant.CUSTOMER_TYPE%>,null,true,"short_sel","","false",'');
		</script>
	  </td>
      <td align="right">经销商名称：</td>
      <td><input id="dealerName" class="middle_txt"  name="dealerName" /></td>
    </tr>
    <tr>
      <td align="right">生成回访类型：</td>
      <td><script type="text/javascript">
           genSelBoxExp("RV_TYPE",<%=Constant.TYPE_RETURN_VISIT%>,null,true,"short_sel","","false",'');
	</script>
        <font color="red">请选择要生成的回访类型</font></td>
        
    </tr>
    <tr class="">
      <td colspan="3" align="center"><span class="table">
        <input name="button" type="button" class="normal_btn" align="right" onclick="setReview();" value="设置" />
        </span>
        <input type="text" id="txtRand"  name="txtRand" onafterpaste="this.value=this.value.replace(/\D/g,'');"  onkeyup="this.value=this.value.replace(/\D/g,'');"  value="" />
        <span class="table">
        <input type="hidden" id ="Results" name="Results" value="" />
        <input type="hidden" id ="VINS" name="VINS" value="" />
        <input name="button2" type="button" class="normal_btn" align="right" value="随机选择"  onclick="getOpt();" />
        </span> <font color="red">请输入一个数字</font>
      <td><span class="table">
        <input name="queryBtn" id="queryBtn" type="button" class="normal_btn" align="right" onclick="queryPer();" value="查询" />
        </span></td>
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
<script type="text/javascript" ><!--
var myPage;
//查询路径
	var url = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/createReviewInitQuery.json";
	var title = null;

	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"code\")' />全选", align:'center',sortable:false, dataIndex:'CTM_ID',width:'2%',renderer:checkBoxShow},
				{header: "客户名称",sortable: false,dataIndex: 'CTM_NAME',align:'center'},
				{header: "手机号码",sortable: false,dataIndex: 'MAIN_PHONE',align:'center'}, 
				//{header: "id",sortable: false,dataIndex: 'CTM_ID',align:'center'}, 
				//{header: "VIN",sortable: false,dataIndex: 'VIN',align:'center'}, 
				{header: "客户类型 ",sortable: false,dataIndex: 'CTM_TYPE',renderer:setIsClick,align:'center'},
				{header: "省份",sortable: false,dataIndex: 'PROVINCE',align:'center'},
				{header: "城市",sortable: false,dataIndex: 'CITY',align:'center'},
				{header: "汽车种类",sortable: false,dataIndex: 'SERIES_NAME',align:'center'},
				{header: "汽车型号",sortable: false,dataIndex: 'MODEL_CODE',align:'center'},
				{header: "用户级别",sortable: false,dataIndex: 'GUEST_STARS',align:'center',renderer:getItemValue},
				{header: "经销商代码",sortable: false,dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",sortable: false,dataIndex: 'DEALER_NAME',align:'center'}
		      ];
		      
		
	//设置复选框
	function checkBoxShow(value,meta,record){
		return String.format("<input type='checkbox' id='code' name='code' value='" + record.data.CTM_ID + "' /><input type='hidden' value='"+record.data.VIN+"' name='vin'/>");
	}
	function setIsClick(value,meta,record){
		document.fm.isClick.value='true';
		return record.data.CTM_TYPE;
	}
	 //设置操作
    function setReview() {
    	var results=document.fm.txtRand.value;
    	if(null==results||"".indexOf(results)==0)
    	{
    		MyAlert("请先进行随机选择再点击设置按钮！");
	       	return false;
    	}
    	var rvType=document.fm.RV_TYPE.value;
    	if(null==rvType||"".indexOf(rvType)==0)
    	{
    		MyAlert("请选择回访类型！");
	       	return false;
    	}
    	var resu=document.fm.Results.value;
    	if(null==resu||"".indexOf(resu)==0)
    	{
    		MyAlert("数据有误，请重新进行随机选择！");
	       	return false;
    	}
    	if(resu!=null&&rvType!=null){
        	checkReview(resu);//检查同一个人是否在同一天做同样的回访
    	}
	}
	function checkReview(resu){
		var url="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/checkReview.json";
		makeNomalFormCall(url,confirmReview,'fm');
	}
	function confirmReview(){
		var msg = json.msg;
		if(msg!=null&&msg=="no"){
			MyAlert("所选客户今天已经生成同种类型回访，请重新选择回访类型或客户！");
			return false;
		}else if(msg!=null&&msg=="yes"){
			MyConfirm("确认设置?", setSubmit);
		}
	}
	function setSubmit() {
		var url="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/generateReview.json";
		makeNomalFormCall(url,showResult22,'fm');
    }
    
    function showResult22(json){
		var msg=json.msg;
		if(msg=='01'){
			MyAlert('成功设置[ '+json.count+' ]个满意度回访！');
			document.fm.Results.value='';	
			document.fm.VINS.value=''
			//MyAlert('设置成功');
			//document.fm.Results.value='';	
			//__extQuery__(1);
		}else{
			MyAlert('设置失败,请联系管理员');
		}
		
	}
    //获取多选框所选的CTM_ID
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
				//MyAlert(codes[i].value+":::"+vinId[i].value);
				ids.push(codes[i].value);
				vins.push(vinId[i].value);
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
			var url="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/radomSel.json";
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
	function selRandom(cnt,ids){
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
		//随机选
		var return_array = new Array();
		var return_vin = new Array();
	    for (var i = 0; i<ram; i++) {
	        if (ids.length>0) {
	            var arrIndex = Math.floor(Math.random()*ids.length);
	            return_array[i] = ids[arrIndex];
	            return_vin[i] = vins[arrIndex];
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
	
  function   showMonthFirstDay()     
  {     
	  var firstDate = new Date();
	  firstDate.setDate(1);
	  return new XDate(firstDate).toString('yyyy-MM-dd');  
  }     
  function   showMonthLastDay()     
  {     
	  var firstDate = new Date();
	  firstDate.setDate(1); //第一天
	  var endDate = new Date(firstDate);
	  endDate.setMonth(firstDate.getMonth()+1);
	  endDate.setDate(0);
	  return new XDate(endDate).toString('yyyy-MM-dd') ;
  }     
 // $('CREATE_DATE_STR').value=showMonthFirstDay(); checkSDate
  
  $('checkSDate').value=showMonthFirstDay();
  $('checkEDate').value=showMonthLastDay();
  
  function queryPer(){
	 
  	var star1 = $('checkSDate').value;
    var end1 =  $('checkEDate').value;
    var roSDate = $('roSDate').value;
    var roEDate =  $('roEDate').value;
    
	  if( (star1==""||end1=="") && ( roSDate =="" || roEDate=="") ){
	  	MyAlert("2个日期必须选择其中一个");
	 	 return false;
	  }else if( (star1>end1)){
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
</body>
</html>
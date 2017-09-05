<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<title>配件运费加价设置</title>
<script language="javascript" type="text/javascript">
	function doInit(){
			//loadcalendar();  //初始化时间控件
			__extQuery__(1);
	}
</script>
</head>
<body>
  <form method="post" name ="fm" id="fm">
    <input type="hidden" name="defineId" id="defineId" value="" />
    <input type="hidden" name="freeTimes" id="freeTimes" value="" />
    <input type="hidden" name="freeOption" id="freeOption" value="" />
    <input type="hidden" name="freeCondition" id="freeCondition" value="" />
    <input type="hidden" name="markupRatio" id="markupRatio" value="" />
      <input type="hidden" name="minPrice" id="minPrice" value="" />
	<div class="wbox">
		<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：
			基础信息管理 &gt; 配件基础信息维护 &gt; 配件运费加价设置
		</div>
		<table class="table_query">
			<th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 查询条件</th>
	       <tr>
		      <td width="10%" align="right">订单类型：</td>
			  <td width="20%">
			    <script type="text/javascript">
			   	 genSelBoxExp("orderType",<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE%>,"",true,"short_sel","","false",'');
			  </script>
		      </td>
		      <td width="10%" align="right">服务商类型：</td>
			  <td width="20%">
			    <select name="dealerType" id="dealerType" class="short_sel">
					<option value="">-请选择-</option>
					<c:if test="${vendList!=null}">
					<c:forEach items="${vendList}" var="list">
						<option value="${list.FIX_VALUE }">${list.FIX_NAME }</option>
					</c:forEach>
					</c:if>
				</select>
		     </td>
		     <td width="10%" align="right">是否有效：</td>
			  <td width="20%">
			    <script type="text/javascript">
			   	 genSelBoxExp("STATE",<%=Constant.STATUS%>,<%=Constant.STATUS_ENABLE%>,true,"short_sel","","false",'');
			  </script>
		     </td>
	       </tr>
	       <tr>
	    	<td  align="center" colspan="6" >
	    	  <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)"/>
	    	  <input class="normal_btn" type="button" value="新 增" onclick="relationAdd()"/>
	    	  <input class="normal_btn" type="button" value="导 出" onclick="exportPartFreightageExcel()"/>
	    	</td>
		  </tr>
		</table>
	</div>
	
	<!-- 查询条件 end -->
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end -->
  </form>
  <script type="text/javascript" >
	var myPage;

	var url = "<%=contextPath%>/parts/baseManager/partFreightageSet/partFreightageSetAction/partFreightageSetSearch.json";
	
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'DEF_ID', renderer:getIndex, style: 'text-align:left'},
                {id:'action',header: "操作",sortable: false,dataIndex: 'DEF_ID',renderer:myLink},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', style: 'text-align:left',renderer:getItemValue},
				{header: "服务商类型", dataIndex: 'FIX_NAME', style: 'text-align:left'},
				{header: "免运费次数", dataIndex: 'FREE_TIMES', renderer:getFTText},
				{header: "免运费设置", dataIndex: 'FRG_OPTION', renderer:getFTOption},
				{header: "免运费条件(含税)", dataIndex: 'FREE_CONDITION', renderer:getFCText},
				{header: "加收比例", dataIndex: 'MARKUP_RATIO', renderer:getMKText},
                {header: "最低运费", dataIndex: 'MIN_PIRCE', renderer:getMText},
				{header: "是否有效", dataIndex: 'STATE', renderer:getItemValue}

		      ];
	     
	
	//设置超链接
	function myLink(value,meta,record)
	{
		var defineId = record.data.DEF_ID;
		var state = record.data.STATE;
		var disableValue = <%=Constant.STATUS_DISABLE%>;
		if(disableValue == state){
			return String.format("<a href=\"#\" onclick='enableData(\""+defineId+"\")'>[有效]</a>");
        } else {
        	return String.format("<a href=\"#\" onclick='cel(\""+defineId+"\")'>[失效]</a>&nbsp;<a href=\"#\" onclick='saveData(\""+defineId+"\")'>[保存]</a>");
    //    	return String.format("<a href=\"#\" onclick='cel(\""+defineId+"\")'>[失效]</a>&nbsp;<a href=\"#\" onclick='formod(\""+defineId+"\")'>[维护]</a>");
		}
  		
	}

	//设置免费次数
	function getFTText(value,meta,record)
	{
		var defineId = record.data.DEF_ID;
		return String.format("<input type='text' style='text-align: right;' name='FT_"+defineId+"' id='FT_"+defineId+"' value='"+value+"' onchange='dataTypeCheck(this)' />");
	}

	//免运费设置
	function getFTOption(value,meta,record)
	{
		var defineId = record.data.DEF_ID;
		return String.format(genSelBoxExpStr("OPT_"+defineId,<%=Constant.PART_FREIGHTAGE_OPTION%>,value,true,"short_sel","","false",''));
	}

	//设置免运费条件
	function getFCText(value,meta,record)
	{
		var defineId = record.data.DEF_ID;
		return String.format("<input type='text' style='text-align: right;' name='FC_"+defineId+"' id='FC_"+defineId+"' value='"+value+"' onchange='dataTypeCheck2(this)' />");
	}

	//设置加收比例
	function getMKText(value,meta,record)
	{
		var defineId = record.data.DEF_ID;
		return String.format("<input type='text' style='text-align: right;' name='MK_"+defineId+"' id='MK_"+defineId+"' value='"+value+"' onchange='dataTypeCheck1(this)' />");
	}

    //设置最低运费
    function getMText(value,meta,record)
    {
        var defineId = record.data.DEF_ID;
        return String.format("<input type='text' style='text-align: right;' name='M_"+defineId+"' id='M_"+defineId+"' value='"+value+"' onchange='dataTypeCheck(this)' />");
    }

    //重写返回下拉框
    function genSelBoxExpStr(id,type,selectedKey,setAll,_class_,_script_,nullFlag,expStr){
    	var str = "";
    	var arr;
    	if(expStr.indexOf(",")>0)
    		arr = expStr.split(",");
    	else {
    		expStr = expStr+",";
    		arr = expStr.split(",");
    	}
    	str += "<select id='" + id + "' name='" + id +"' class='"+ _class_ +"' " + _script_ ;
    	// modified by lishuai@infoservice.com.cn 2010-05-18 解决select下拉框中增加属性datatype判断bug begin
    	if(nullFlag && nullFlag == "true"){
    		str += " datatype='0,0,0' ";
    	}
    	// end
    	str += " onChange=doCusChange(this.value);> ";
    	if(setAll){
    		str += genDefaultOpt();
    	}
    	for(var i=0;i<codeData.length;i++){
    		var flag = true;
    		for(var j=0;j<arr.length;j++){
    			if(codeData[i].codeId == arr[j]){
    				flag = false;
    			}
    		}
    		if(codeData[i].type == type && flag){
    			str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '"+codeData[i].codeDesc+"' >" + codeData[i].codeDesc + "</option>";
    		}
    	}
    	str += "</select>";	
    	return str;
    }
    
	//数据验证
	function dataTypeCheck(obj)
	{
		var value = obj.value;
	    if (isNaN(value) || "" == value) {
	        MyAlert("请输入数字!");
	        obj.value = "";
	        return;
	    }
	    var re = /^((0)|([1-9]+[0-9]*]*))$/;
	    if (!re.test(obj.value)) {
	        MyAlert("请输入正整数!");
	        obj.value = "";
	        return;
	    }
	}

	function dataTypeCheck1(obj)
	{
		var value = obj.value;
	    if (isNaN(value) || "" == value) {
	        MyAlert("请输入数字!");
	        obj.value = "";
	        return;
	    }
	    if(1 < value)
	    {
	    	MyAlert("加收比例不能大于 1 !");
	        obj.value = "";
	        return;
	    }
	    obj.value = parseFloat(value).toFixed(2);
	}

	function dataTypeCheck2(obj)
	{
		var value = obj.value;
		value = value + "";
		value = parseFloat(value.replace(new RegExp(",","g"),""));
	    if (isNaN(value) || "" == value) {
	        MyAlert("请输入数字!");
	        obj.value = 0.00;
	        return;
	    }
	    if(0 > value)
	    {
		    MyAlert("免运费条件不能小于 0!");
		    obj.value = (0.00).toFixed(2);
		    return;
	    }
	    obj.value = addKannma(value.toFixed(2));
	}

	//千分格式
	function addKannma(number) {  
	    var num = number + "";  
	    num = num.replace(new RegExp(",","g"),"");   
	    // 正负号处理   
	    var symble = "";   
	    if(/^([-+]).*$/.test(num)) {   
	        symble = num.replace(/^([-+]).*$/,"$1");   
	        num = num.replace(/^([-+])(.*)$/,"$2");   
	    }   
	  
	    if(/^[0-9]+(\.[0-9]+)?$/.test(num)) {   
	        var num = num.replace(new RegExp("^[0]+","g"),"");   
	        if(/^\./.test(num)) {   
	        num = "0" + num;   
	        }   
	  
	        var decimal = num.replace(/^[0-9]+(\.[0-9]+)?$/,"$1");   
	        var integer= num.replace(/^([0-9]+)(\.[0-9]+)?$/,"$1");   
	  
	        var re=/(\d+)(\d{3})/;  
	  
	        while(re.test(integer)){   
	            integer = integer.replace(re,"$1,$2");  
	        }   
	        return symble + integer + decimal;   
	  
	    } else {   
	        return number;   
	    }   
	}

	//保存设置
	function saveData(defineId) {
		var freeTimes = document.getElementById("FT_" + defineId).value;
		var freeOption = document.getElementById("OPT_" + defineId).value;
    	var freeCondition = document.getElementById("FC_" + defineId).value;
    	freeCondition = freeCondition + "";
    	freeCondition = freeCondition .replace(new RegExp(",","g"),"");
    	var markupRatio = document.getElementById("MK_" + defineId).value;
        var minPrice = document.getElementById("M_" + defineId).value;
    	if("" == freeTimes || null == freeTimes)
    	{
    		MyAlert('请设置免运费次数!');
			return false;
    	}

    	if("" == freeOption || null == freeOption)
    	{
    		MyAlert('请设置免运费设置!');
			return false;
    	}

    	if("" == freeCondition || null == freeCondition)
    	{
    		MyAlert('请设置免运费条件!');
			return false;
    	}

    	if("" == markupRatio || null == markupRatio)
    	{
    		MyAlert('请设置加收比例!');
			return false;
    	}
    	
    	document.getElementById("defineId").value = defineId;
    	document.getElementById("freeTimes").value = freeTimes;
    	document.getElementById("freeOption").value = freeOption;
    	document.getElementById("freeCondition").value = freeCondition;
    	document.getElementById("markupRatio").value = markupRatio;
        document.getElementById("minPrice").value = minPrice;
		if(confirm("确定保存设置?")){
			btnDisable();
	     	var url = '<%=contextPath%>/parts/baseManager/partFreightageSet/partFreightageSetAction/updatePartFreightageSet.json';
	  		makeFormCall(url,showResult,'fm');
	    }
	}

	//设置失效：
    function cel(parms) {
    	if(confirm("确定失效该数据?")){
    		btnDisable();
   	     	var url = '<%=contextPath%>/parts/baseManager/partFreightageSet/partFreightageSetAction/celPartFreightageSet.json?disabeParms='+parms+'&curPage='+myPage.page;
   	  		makeFormCall(url,showResult,'fm');
   	    }
    }

    //设置有效：
    function enableData(parms) {
    	if(confirm("确定有效该数据?")){
    		btnDisable();
   	     	var url = '<%=contextPath%>/parts/baseManager/partFreightageSet/partFreightageSetAction/enablePartFreightageSet.json?enableParms='+parms+'&curPage='+myPage.page;
   	  		makeFormCall(url,showResult,'fm');
   	    }
    }

    function showResult(json) {
    	btnEnable();
        if (json.errorExist != null && json.errorExist.length > 0) {
           MyAlert(json.errorExist);
        } else if (json.success != null && json.success == "true") {
        	MyAlert("操作成功!");
        	__extQuery__(json.curPage);          
        } else {
            MyAlert("操作失败，请联系管理员!");
        }
    }

    //维护
    function formod(defineId)
    {
    	btnDisable();
    	document.getElementById("defineId").value = defineId;
    	document.fm.action = "<%=contextPath%>/parts/baseManager/partFreightageSet/partFreightageSetAction/partFreightageSetFormodInit.do";
		document.fm.target="_self";
		document.fm.submit();
    }

  	//新增
	function relationAdd(){
		btnDisable();
		window.location.href ="<%=contextPath%>/parts/baseManager/partFreightageSet/partFreightageSetAction/partFreightageSetAddInit.do";
	}

	function exportPartFreightageExcel()
	{
		document.fm.action = "<%=contextPath%>/parts/baseManager/partFreightageSet/partFreightageSetAction/exportPartFreightageExcel.do";
		document.fm.target="_self";
		document.fm.submit();
	}

	//失效按钮
	function btnDisable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });

	}

	//有效按钮
	function btnEnable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = "";
	    });

	}
  </script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/calendar.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>配件销售价格维护</title>
<script type="text/javascript" >
$(document).ready(function(){
	__extQuery__(1);
});

	var myPage;

	var url = "<%=contextPath%>/parts/baseManager/partSalePrice/PartSalePrice/queryPartSalePrice.json";

	var title = null;

	var columns = [
				{header: "序号",  style: 'text-align:left',renderer:getIndex},
                {id:'action',header: "操作",sortable: false,dataIndex: 'PRICE_ID',renderer:myLink ,align:'center'},
				{header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align: center'},
				{header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align: center'},
                {header: "件号", dataIndex: 'PART_CODE',  style: 'text-align: center'},
                {header: "调拨价有效开始日期", dataIndex: 'PRICE_VALID_START_DATE',  style: 'text-align: center',renderer: insertInputDate},
                {header: "调拨价有效结束日期", dataIndex: 'PRICE_VALID_END_DATE',  style: 'text-align: center',renderer: insertInputDate},
                {header: "促销价有效开始日期", dataIndex: 'SALE_PRICE_START_DATE',  style: 'text-align: center',renderer: insertInputDate},
                {header: "促销价有效结束日期", dataIndex: 'SALE_PRICE_END_DATE',  style: 'text-align: center',renderer: insertInputDate},
//                {header: "结算基地", dataIndex: 'PART_IS_CHANGHE', style: "text-align: center", renderer: getItemValue},
				{header: '${map.prciceName1}(元)', dataIndex: 'SALE_PRICE1',  style: 'text-align: center',renderer: insertInputSalePrice},
				{header: '${map.prciceName2}(元)', dataIndex: 'SALE_PRICE2',  style: 'text-align: center',renderer: insertInputSalePrice},
				{header: '${map.prciceName3}(元)', dataIndex: 'SALE_PRICE3',  style: 'text-align: center',renderer: insertInputSalePrice},
				{header: '${map.prciceName4}(元)', dataIndex: 'SALE_PRICE4',  style: 'text-align: center',renderer: insertInputSalePrice},
				{header: '${map.prciceName5}(元)', dataIndex: 'SALE_PRICE5',  style: 'text-align: cente',renderer: insertInputSalePrice},
				{header: '${map.prciceName6}(元)', dataIndex: 'SALE_PRICE6',  style: 'text-align: cente',renderer: insertInputSalePrice},
				{header: '${map.prciceName7}(元)', dataIndex: 'SALE_PRICE7',  style: 'text-align: cente',renderer: insertInputSalePrice},
				{header: '${map.prciceName8}(元)', dataIndex: 'SALE_PRICE8',  style: 'text-align: cente',renderer: insertInputSalePrice},
				{header: '${map.prciceName9}(元)', dataIndex: 'SALE_PRICE9',  style: 'text-align: cente',renderer: insertInputSalePrice},
				{header: '${map.prciceName10}(元)', dataIndex: 'SALE_PRICE10',  style: 'text-align: cente',renderer: insertInputSalePrice},
				{header: '${map.prciceName11}(元)', dataIndex: 'SALE_PRICE11',  style: 'text-align: cente',renderer: insertInputSalePrice},
				{header: '${map.prciceName12}(元)', dataIndex: 'SALE_PRICE12',  style: 'text-align: cente',renderer: insertInputSalePrice},
				{header: '${map.prciceName13}(元)', dataIndex: 'SALE_PRICE13',  style: 'text-align: cente',renderer: insertInputSalePrice},
				{header: '${map.prciceName14}(元)', dataIndex: 'SALE_PRICE14',  style: 'text-align: cente',renderer: insertInputSalePrice},
				{header: '${map.prciceName15}(元)', dataIndex: 'SALE_PRICE15',  style: 'text-align: cente',renderer: insertInputSalePrice}
				/*{header: "修改日期", dataIndex: 'UPDATE_DATE',  style: 'text-align:left'},
				{header: "修改人", dataIndex: 'NAME',  style: 'text-align:left'},*/

		      ];


	function loadExcelTemplate(){
		fm.action = "<%=contextPath%>/parts/baseManager/partSalePrice/PartSalePrice/exportExcelTemplate.do";
		fm.submit();
	}
	function loadPartPriceExcel(){
		fm.action = "<%=contextPath%>/parts/baseManager/partSalePrice/PartSalePrice/exportPartPriceExcel.do";
		fm.submit();
	}
	function loadExcelIntoDB(){
        var filevalue = fm.uploadFile.value;
        if (filevalue == '') {
            MyAlert('导入文件不能空!');
            return false;
        }
        var fi = filevalue.substring(filevalue.length - 3, filevalue.length);
        if (fi != 'xls') {
            MyAlert('导入文件格式不对,请导入xls文件格式');
            return false;
        }
        MyConfirm("确认导入选中文件? 确定后请耐心等待...", conmmitFile, [])
		
	}

	function conmmitFile()
	{
		disableAllStartBtn();
		fm.action = "<%=contextPath%>/parts/baseManager/partSalePrice/PartSalePrice/loadPartPriceDataIntoDB.do";
		fm.submit();
	}
	function myLink(value,meta,record)
	{
  		return String.format("<a href=\"#\" onclick='save(\""+value+"\")'>[保存]</a>"+"<a href=\"#\" onclick='queryHis(\""+value+"\")'>[查看修改历史]</a>");
	}
	function queryHis(value){
		OpenHtmlWindow('<%=contextPath%>/parts/baseManager/partSalePrice/PartSalePrice/queryModifyHis.do?priceId='+value,1300,550);
	}
	//保存
	function save(value)
	{
			var msg ="";
		  	var PRICE_VALID_START_DATE=document.getElementById("PRICE_VALID_START_DATE"+value).value;
		  	var PRICE_VALID_END_DATE=document.getElementById("PRICE_VALID_END_DATE"+value).value;
		  	var SALE_PRICE_START_DATE=document.getElementById("SALE_PRICE_START_DATE"+value).value;
		  	var SALE_PRICE_END_DATE=document.getElementById("SALE_PRICE_END_DATE"+value).value;
		  	var SALE_PRICE1=document.getElementById("SALE_PRICE1"+'+'+value).value;
			var SALE_PRICE2=document.getElementById("SALE_PRICE2"+'+'+value).value;
			var SALE_PRICE3=document.getElementById("SALE_PRICE3"+'+'+value).value;
			var SALE_PRICE4=document.getElementById("SALE_PRICE4"+'+'+value).value;
			var SALE_PRICE5=document.getElementById("SALE_PRICE5"+'+'+value).value;
			var SALE_PRICE6=document.getElementById("SALE_PRICE6"+'+'+value).value;
			var SALE_PRICE7=document.getElementById("SALE_PRICE7"+'+'+value).value;
			var SALE_PRICE8=document.getElementById("SALE_PRICE8"+'+'+value).value;
			var SALE_PRICE9=document.getElementById("SALE_PRICE9"+'+'+value).value;
			var SALE_PRICE10=document.getElementById("SALE_PRICE10"+'+'+value).value;
			var SALE_PRICE11=document.getElementById("SALE_PRICE11"+'+'+value).value;
			var SALE_PRICE12=document.getElementById("SALE_PRICE12"+'+'+value).value;
			var SALE_PRICE13=document.getElementById("SALE_PRICE13"+'+'+value).value;
			var SALE_PRICE14=document.getElementById("SALE_PRICE14"+'+'+value).value;
			var SALE_PRICE15=document.getElementById("SALE_PRICE15"+'+'+value).value;
		  if(isNaN(SALE_PRICE1)){
             msg += "请输入正确格式的${map.prciceName1}! </br>";
           }
           if(isNaN(SALE_PRICE2)){
             msg += "请输入正确格式的${map.prciceName2}! </br>";
           }
           if(isNaN(SALE_PRICE3)){
             msg += "请输入正确格式的${map.prciceName2}! </br>";
           }
           if(isNaN(SALE_PRICE4)){
              msg += "请输入正确格式的${map.prciceName2}! </br>";
           }
           if(isNaN(SALE_PRICE5)){
             msg += "请输入正确格式的${map.prciceName2}! </br>";
           }
           if(isNaN(SALE_PRICE6)){
             msg += "请输入正确格式的${map.prciceName2}! </br>";
           }
           if(isNaN(SALE_PRICE7)){
             msg += "请输入正确格式的${map.prciceName2}! </br>";
           }
           if(isNaN(SALE_PRICE8)){
             msg += "请输入正确格式的${map.prciceName2}! </br>";
           }
           if(isNaN(SALE_PRICE9)){
             msg += "请输入正确格式的${map.prciceName2}! </br>";
           }
           if(isNaN(SALE_PRICE10)){
             msg += "请输入正确格式的${map.prciceName2}! </br>";
           }
           if(isNaN(SALE_PRICE11)){
             msg += "请输入正确格式的${map.prciceName2}! </br>";
           }
           if(isNaN(SALE_PRICE12)){
             msg += "请输入正确格式的${map.prciceName2}! </br>";
           }
           if(isNaN(SALE_PRICE13)){
             msg += "请输入正确格式的${map.prciceName2}! </br>";
           }
           if(isNaN(SALE_PRICE14)){
             msg += "请输入正确格式的${map.prciceName2}! </br>";
           }
           if(isNaN(SALE_PRICE15)){
             msg += "请输入正确格式的${map.prciceName2}! </br>";
           }
          if(msg!=""){
          	MyAlert(msg);
          	return;
          }

	  	var saveUrl = "<%=contextPath%>/parts/baseManager/partSalePrice/PartSalePrice/savePartSalePrice.json?PRICE_ID="+value
						 +"&PRICE_VALID_START_DATE="+PRICE_VALID_START_DATE
						 +"&PRICE_VALID_END_DATE="+PRICE_VALID_END_DATE
						 +"&SALE_PRICE_START_DATE="+SALE_PRICE_START_DATE
						 +"&SALE_PRICE_END_DATE="+SALE_PRICE_END_DATE
						 +"&SALE_PRICE1="+SALE_PRICE1
						 +"&SALE_PRICE2="+SALE_PRICE2
						 +"&SALE_PRICE3="+SALE_PRICE3
						 +"&SALE_PRICE4="+SALE_PRICE4
						 +"&SALE_PRICE5="+SALE_PRICE5
						 +"&SALE_PRICE6="+SALE_PRICE6
						 +"&SALE_PRICE7="+SALE_PRICE7
						 +"&SALE_PRICE8="+SALE_PRICE8
						 +"&SALE_PRICE9="+SALE_PRICE9
						 +"&SALE_PRICE10="+SALE_PRICE10
						 +"&SALE_PRICE11="+SALE_PRICE11
						 +"&SALE_PRICE12="+SALE_PRICE12
						 +"&SALE_PRICE13="+SALE_PRICE13
						 +"&SALE_PRICE14="+SALE_PRICE14
						 +"&SALE_PRICE15="+SALE_PRICE15
						 +'&curPage='+myPage.page;
		MyConfirm("确认保存设置?", saveRecord, [saveUrl, value],  null, null);
	}
	
	function saveRecord(saveUrl, id){
      	document.getElementById("SALE_PRICE1"+'+'+id).disabled=true;
		document.getElementById("SALE_PRICE2"+'+'+id).disabled=true;
		document.getElementById("SALE_PRICE3"+'+'+id).disabled=true;
		document.getElementById("SALE_PRICE4"+'+'+id).disabled=true;
		document.getElementById("SALE_PRICE5"+'+'+id).disabled=true;
		document.getElementById("SALE_PRICE6"+'+'+id).disabled=true;
		document.getElementById("SALE_PRICE7"+'+'+id).disabled=true;
		document.getElementById("SALE_PRICE8"+'+'+id).disabled=true;
		document.getElementById("SALE_PRICE9"+'+'+id).disabled=true;
		document.getElementById("SALE_PRICE10"+'+'+id).disabled=true;
		document.getElementById("SALE_PRICE11"+'+'+id).disabled=true;
		document.getElementById("SALE_PRICE12"+'+'+id).disabled=true;
		document.getElementById("SALE_PRICE13"+'+'+id).disabled=true;
		document.getElementById("SALE_PRICE14"+'+'+id).disabled=true;
		document.getElementById("SALE_PRICE15"+'+'+id).disabled=true;
		makeNomalFormCall(saveUrl,veiwPrices,'fm');
	}
	
	function veiwPrices(jsonObj){
		if(null != jsonObj.success && "ture" == jsonObj.success)
		{
			MyAlert("价格修改成功");
			__extQuery__(jsonObj.curPage);
		}
		else
		{
			MyAlert("价格修改失败,请联系管理员!");
		}
	}
	//设置价格列表
	function partPriceList(){
<%-- 		OpenHtmlWindow('<%=contextPath%>/parts/baseManager/partSalePrice/PartSalePrice/addPartSalePrice.do?',1100,400); --%>
		fm.action='<%=contextPath%>/parts/baseManager/partSalePrice/PartSalePrice/addPartSalePrice.do?';
		fm.submit();
	}
	
	// 插入日期文本框
	function insertInputDate(value,meta,record){
		var dataIndex = this.dataIndex;
		var priceId = record.data.PRICE_ID;
		var onclickStr = "WdatePicker(";
		if(dataIndex == 'PRICE_VALID_START_DATE'){
			// 调拨价有效开始日期	
			onclickStr += "{maxDate:'#F{$dp.$D(\\'PRICE_VALID_END_DATE"+priceId+"\\')}'}";
		}else if(dataIndex == 'PRICE_VALID_END_DATE'){
			// 调拨价有效结束日期
			onclickStr += "{minDate:'#F{$dp.$D(\\'PRICE_VALID_START_DATE"+priceId+"\\')}'}";
		}else if(dataIndex == 'SALE_PRICE_START_DATE'){
			//  促销价有效开始日期
			onclickStr += "{maxDate:'#F{$dp.$D(\\'SALE_PRICE_END_DATE"+priceId+"\\')}'}";
		}else if(dataIndex == 'SALE_PRICE_END_DATE'){
			// 促销价有效结束日期
			onclickStr += "{minDate:'#F{$dp.$D(\\'SALE_PRICE_START_DATE"+priceId+"\\')}'}";
		}
		onclickStr += ")";
		var output = '<input id="'+dataIndex+priceId+'" datatype="1,is_date,10" value="'+value+'"  onclick="'+onclickStr+'" class="Wdate" type="text" maxlength="10"';
		output += 'style="width: 100px;">';
	    return output;
	}
	
	//插入文本框
	function insertInputSalePrice(value,meta,record){
		var inputId = this.dataIndex+ '+' + record.data.PRICE_ID;
	    var output = '<input type="text" onchange="checkNumberLength(this,8,2)" id="'+inputId+'" value="'+value +'" size ="10"'
	    		   + ' size ="10" style=" background-color: #FF9; text-align: right; width: 70px;" datatype="1,is_double,10" decimal="2">';
	    return output;
	}

    //验证最大长度指定的正小数,inputObj为input对象，beforeLength为小数点前面的位数个数，afterLength为小数点后面的位数个数
    function checkNumberLength(inputObj, beforeLength, afterLength) {
        if (inputObj.value.indexOf(".") >= 0) {
            //var regex = new RegExp("/^[0-9]{0," + beforeLength + "}.[0-9]{0," + afterLength + "}$/");  +\.[0-9]{2})[0-9]*
            var regex = new RegExp("/^[0-9]{0,10}.\\d{0,2}$/");
            if(regex.test(inputObj.value)==true){
                MyAlert("请录入正确的采购金额，且小数保留精度最大为"+afterLength+"位!");
                inputObj.value="";
                inputObj.focus();
            }else {
                var re = /([0-9]+\.[0-9]{2})[0-9]*/;
                inputObj.value=  inputObj.value.replace(re,"$1");
            }
        }
        else {
        	var re = "/^\d{"+beforeLength+"}$/";
        	if(re.test(inputObj.value)){
                MyAlert("只能是数字，且纯数字不能超过或等于"+beforeLength+"位!");
                inputObj.value="";
                inputObj.focus();
            }else {

            }
        }
    }
function disableAllStartBtn(){
		var inputArr = document.getElementsByTagName("input");
		for(var i=0;i<inputArr.length;i++){
			if(inputArr[i].type=="button"){
				inputArr[i].disabled=true;
			}
		}
	}
function enableAllStartBtn(){
	var inputArr = document.getElementsByTagName("input");
	for(var i=0;i<inputArr.length;i++){
		if(inputArr[i].type=="button"){
			inputArr[i].disabled=false;
		}
	}
}
    function showUpload(){
        if($("#uploadTable")[0].style.display == "none"){
            $("#uploadTable")[0].style.display = "block";
        }else {
            $("#uploadTable")[0].style.display = "none";
        }
    }

</script>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 配件销售价格维护 </div>
    <form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input type="hidden" name="curPage" id="curPage" value="${curPage}"/>
	<div class="form-panel">
	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
    <div class="form-body">
	<table class="table_query">
	    <tr>
	      <td class="right">配件编码：</td>
	      <td><input class="middle_txt" type="text"  name="PART_OLDCODE"  /></td>
	      <td class="right">配件名称：</td>
	      <td><input class="middle_txt" type="text"  name="PART_CNAME" /></td>
          <td class="right">件号：</td>
          <td><input class="middle_txt" type="text" name="PART_CODE"  /></td>
	    </tr>
	    <tr>
	    	<td class="center" colspan="6" >
		    	<input class="u-button" type="button" value="查 询" onclick="__extQuery__(1);"/>
				<input class="u-button" type="reset" value="重 置">
		   	    <input class="u-button" type="button" value="导 出" onclick="loadPartPriceExcel();"/>
		   	    <input class="u-button" type="button" value="设置价格列表" onclick="partPriceList();disableAllStartBtn();"/>
		        <input class="u-button" type="button" value="批量导入" onclick="showUpload();"/>
	        </td>
		</tr>
	</table>
	</div>
	</div>
    <table class="table_edit" id="uploadTable" style="display: none">
	  <tr>
	    <td>
	    	<font color="red">
		      	<input type="button" class="normal_btn" onclick="loadExcelTemplate()" value="模版下载"/>
	        	文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;
        	</font>
	      <input type="file" name="uploadFile" style="width: 250px"  id="uploadFile" value="" />
	      &nbsp;
		  <input type="button" class="normal_btn" onclick="loadExcelIntoDB()" value="确定"/></td>
    </tr>
  </table>
  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!-- 	</table> -->
</form>
</div>
</body>

</html>

<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件折扣率修改</title>
<style type="text/css">
.table_list_row0 td {
	background-color:#FFFFCC;
	border: 1px solid #DAE0EE;
	white-space:    nowrap;
}
</style>
</head>
<body onunload='javascript:destoryPrototype();' onload="__extQuery__(1);loadcalendar();showInfo();">
<div class="wbox">
	<div class="navigation"> <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
	  配件管理 &gt; 基础信息管理 &gt ;配件基础信息维护 &gt; 配件折扣率维护 &gt; 修改
	</div>
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
    <input type="hidden" name="DISCOUNT_ID" id="DISCOUNT_ID" value="${po['DISCOUNT_ID'] }"/>
    <input type="hidden" name="dpIds" id="dpIds" value=""/>
    <input type="hidden" name="dpCodes" id="dpCodes" value=""/>
    <input type="hidden" name="dpNames" id="dpNames" value=""/>
    <input type="hidden" name="curPage" id="curPage" value="${curPage }"/>
	<table class="table_edit">
            <th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" class="nav" />信息</th>
	     <tr>
	      <td width="10%" class="table_query_right" align="right">折扣类型：</td>
	      <td width="20%">
	      <script type="text/javascript">
		       genSelBoxExp("DISCOUNT_TYPE",<%=Constant.PART_DISCOUNT_TYPE%>,${po['DISCOUNT_TYPE'] },false,"short_sel","disabled='disabled'","false",'');
	      </script>
	      </td>
	      <td width="10%" class="table_query_right" align="right">有效日期：</td>
	      <td width="30%">
	            <input name="startDate" id="t1" value=' <fmt:formatDate value="${po['VALID_FROM'] }"/>' type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2">
        		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't1', false);"/>
        		&nbsp;至&nbsp;
        		<input name="endDate" id="t2" value='<fmt:formatDate value="${po['VALID_TO'] }"/>' type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2">
        		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't2', false);"/>
	      </td>
	       <td  width="10%" class="table_query_right" align="right">折扣率：</td>
	      <td  width="20%">
	      <input class="short_txt" type="text" name="DISCOUNT_RATE" id="DISCOUNT_RATE" datatype="0,is_null" value="${po['DISCOUNT_RATE'] }"/>
	      </td>
	     
	    </tr>
	    
      <tr>
	        <td width="10%" class="table_query_right" align="right" >是否有效：</td>
      		<td width="20%">
      		<script type="text/javascript">
      		genSelBoxExp("STATE",<%=Constant.STATUS%>,${po['STATE'] },false,"short_sel","","false",'');
	       </script>
	       </td>
	       <td  width="10%" class="table_query_right" align="right">服务商：</td>
	      <td  width="20%">
	      <input class="middle_txt" type="text" name="DEALER_CODE" id="DEALER_CODE"  readonly="readonly" />
	      <input type="button" class="mark_btn" onclick="showMyOrgDealer('DEALER_CODE', '', 'true','',true,true,false,'');" value="&hellip;" />
	      </td>
      </tr>
      <tr >
	        
      </tr>
        </table>
	<table class="table_edit">
            <tr>
    	<td colspan="6" align="center">
            <input type="button" name="saveBtn" id="saveBtn" value="保存" onclick="updateDiscount();"  class="normal_btn"/>
            <input type="button" name="returnBtn" id="returnBtn" value="返 回" 

onclick="javascript:goback();"  class="normal_btn"/>
        </td>
    </tr>
        </table>
        <table id="file" class="table_list" style="display:none">
    <tr>
      <th colspan="7" align="left"><img class="nav" src="<%=contextPath%>/img/nav.gif" />配件折扣明细
	  </th>
    </tr>
    </table>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
    <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script type="text/javascript" >

autoAlertException();
var myPage;
var dpIds;
var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartDiscountRateManager/queryPartDiscountDtlInfoById.json";
				
var title = null;

var columns = [
				{header: "序号", align:'center', renderer:getIndex,width:'7%'},
				{header: "编码", dataIndex: 'DP_CODE', align:'center',renderer:InpurtText3},
				{header: "名称", dataIndex: 'DP_NAME', align:'center'},
				{header: "折扣率", dataIndex: 'RATE', align:'center',renderer:InpurtText1},
                {header: "数量", dataIndex: 'AMOUNT', align:'center',renderer:InpurtText2},
				{header: "是否有效", dataIndex: 'STATE',align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'DTL_ID',renderer:myLink ,align:'center'}
			  ];

function myLink(value,meta,record){
	var state = record.data.STATE;
	if(state==<%=Constant.STATUS_ENABLE%>){
		return String.format("<a href=\"#\" onclick='disableDtl(\""+value+"\")'>[失效]</a>");
	}
	if(state==<%=Constant.STATUS_DISABLE%>){
		return String.format("<a href=\"#\" onclick='enableDtl(\""+value+"\")'>[有效]</a>");
	}
}

function InpurtText1(value,meta,record){
    var rate = record.data.RATE;
    var dtl_id = record.data.DTL_ID;
    return String.format('<input type="text" onchange="checkNumberLength(this);" name="DIS_RATE2" id="DIS_RATE2'+dtl_id+'" value='+rate+' >')
}
function InpurtText2(value,meta,record){
    var amount = record.data.AMOUNT;
    var dtl_id = record.data.DTL_ID;
    return String.format('<input type="text" onchange="checkAmount(this);" name="AMOUNT" id="AMOUNT'+dtl_id+'" value='+amount+' >')
}
function InpurtText3(value,meta,record){
    var dtl_id = record.data.DTL_ID;
    return String.format('<input type="hidden" name="DTL_ID"  value='+dtl_id+' >')
}

function disableDtl(value){
	if(confirm("确定失效?")){
		btnDisable();
		var url1 = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartDiscountRateManager/disablePartDiscountDtl.json?dtlId='+value+'&curPage='+myPage.page;
		sendAjax(url1, stateResult, 'fm');
	}
}

function enableDtl(value){
	if(confirm("确定有效?")){
		btnDisable();
		var url1 = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartDiscountRateManager/enablePartDiscountDtl.json?dtlId='+value+'&curPage='+myPage.page;
		sendAjax(url1, stateResult, 'fm');
	}
}

function stateResult(jsonObj){
	btnEable();
	if(jsonObj){
		var success = jsonObj.success;
	    var error = jsonObj.error;
	    var exceptions = jsonObj.Exception;
	    if(success){
	    	MyAlert(success);
	    	__extQuery__(jsonObj.curPage);
	    }else if(error){
	    	MyAlert(error);
	    }else if(exceptions){
	    	MyAlert(exceptions.message);
		}
	}
}

function setPartDiscountDtl(id,OldCode,name){
	var ids = id.split(",");
	var OldCodes = OldCode.split(",");
	var names = name.split(",");
	if(dpIds){
		for(var i=0;i<dpIds.length;i++){
			for(var j=0;j<ids.length;j++){
				if(dpIds[i]==ids[j]){
					 MyAlert("名称:"+names[j]+" 已经存在!");
					 return false;
				}
			}
		}
	}

    document.getElementById("dpIds").value = ids;
    document.getElementById("dpCodes").value = OldCodes;
    document.getElementById("dpNames").value = names;
	var url1 = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartDiscountRateManager/savePartDiscountDtl.json?';
    makeNomalFormCall(url1, getResult, 'fm');
    return true;
}

function getResult(jsonObj){
	if(jsonObj){
		var success = jsonObj.success;
	    var error = jsonObj.error;
	    var exceptions = jsonObj.Exception;
	    if(success){
	    	__extQuery__(1);
	    }else if(error){
	    	MyAlert(error);
	    }else if(exceptions){
	    	MyAlert(exceptions.message);
		}
	}
}

function showMyOrgDealer(inputCode ,inputId ,isMulti ,orgId, isAllLevel, isAllArea,isDealerType,inputName){
	if(!inputCode){ inputCode = null;}
	if(!inputId){ inputId = null;}
	if(!isMulti){ isMulti = null;}
	if(!orgId || orgId == 'false' || orgId == 'true'){ orgId = null;}
	if(!isAllLevel){ isAllLevel = null;}
	if(!isAllArea){ isAllArea = null;}
	if(!isDealerType){ isDealerType = null;}
	if(!inputName){ inputName = null;}
	OpenHtmlWindow("<%=contextPath%>/jsp/parts/baseManager/partsBaseManager/partDiscount/showMyOrgDealer.jsp?INPUTCODE="+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ORGID="+orgId+"&ISALLLEVEL="+isAllLevel+"&ISALLAREA="+isAllArea+"&isDealerType="+isDealerType+"&inputName="+inputName,730,390);
}

function updateDiscount(){
	if (!submitForm('fm')) {
        return;
    }
	var rateObj = $("DISCOUNT_RATE");
	var flag = checkNumberLength(rateObj);
	if(!flag){
		return;
	}

	var dtls = document.getElementsByName("DTL_ID");
	
	for(var i=0;i<dtls.length;i++){
        var rateObj = document.getElementById("DIS_RATE2"+dtls[i].value);//折扣率
        var amountObj = document.getElementById("AMOUNT"+dtls[i].value);//数量
        var flag1 = checkNumberLength(rateObj);
        if(!flag1){
    		return;
    	}
    	if(amountObj.value&&amountObj.value!=0){
    		var patrn = /^[1-9][0-9]*$/;
   	        if (!patrn.exec(amountObj.value)) {
   	            MyAlert("数量只能输入正整数！");
   	            $(amountObj).value=$(amountObj).value.replace(/\D/g,'');
   	            $(amountObj).focus();
   	            return;
   	        }
    	}
    }
	
	if(confirm("确定修改?")){
		btnDisable();
		$("DISCOUNT_TYPE").disabled="";
		var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartDiscountRateManager/updatePartDiscount.json?';
	    sendAjax(url, updateResult, 'fm');
	}
}

function updateResult(jsonObj){
	btnEable();
	if(jsonObj){
		var success = jsonObj.success;
	    var error = jsonObj.error;
	    var exceptions = jsonObj.Exception;
	    if(success){
		    MyAlert(success);
		    window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartDiscountRateManager/queryPartDiscountRateInit.do';
	    }else if(error){
	    	MyAlert(error);
	    }else if(exceptions){
	    	MyAlert(exceptions.message);
		}
	}
}

function callBack(json){
	btnEable();
	var ps;
	dpIds = json.dpIds;
	//设置对应数据
	if(Object.keys(json).length>0){
		keys = Object.keys(json);
		for(var i=0;i<keys.length;i++){
		   if(keys[i] =="ps"){
			   ps = json[keys[i]];
			   break;
		   }
		}
	}
	//生成数据集
	if(ps.records != null){
		$("_page").hide();
		$('myGrid').show();
		new createGrid(title,columns, $("myGrid"),ps).load();			
		//分页
		myPage = new showPages("myPage",ps,url);
		myPage.printHtml();
		hiddenDocObject(2);
		document.getElementById("saveBtn").disabled="";
	}else{
		$("_page").show();
		$("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据</div>";
		$("myPage").innerHTML = "";
		removeGird('myGrid');
		$('myGrid').hide();
		hiddenDocObject(1);
	}
}

function checkAmount(obj){
	 var patrn = /^[1-9][0-9]*$/;
    if (!patrn.exec(obj.value)) {
        MyAlert("数量只能输入正整数!");
        $(obj).value=$(obj).value.replace(/\D/g,'');
        $(obj).focus();
        return;
    }
}

//验证精度不超过3的正小数
function checkNumberLength(inputObj) {
	if(inputObj.value==null||inputObj.value==""){
		MyAlert("折扣率不能为空!");
		inputObj.value="";
        inputObj.focus();
        return false;
	}
	if(isNaN(inputObj.value)){
		MyAlert("折扣率是小于等于1的正数，且小数保留精度最大为3位!");
        inputObj.value="";
        inputObj.focus();
        return false;
	}
	if(inputObj.value>1||inputObj.value==0){
		MyAlert("折扣率是小于等于1的正数，且小数保留精度最大为3位!");
        inputObj.value="";
        inputObj.focus();
        return false;
	}
    if (inputObj.value.indexOf("0.") >= 0) {
    	var pattern = /(^0\.(0{1,2}[1-9])$)|(^0\.([1-9]{1,3})$)/;
        if(!pattern.exec(inputObj.value)){
            MyAlert("折扣率是小于等于1的正数，且小数保留精度最大为3位!");
            inputObj.value="";
            inputObj.focus();
            return false;
        }
    }
    if (inputObj.value.indexOf("1.") >= 0) {
    	var pattern1 = /^1\.(0{1,3})$/;
        if(!pattern1.exec(inputObj.value)){
        	MyAlert("折扣率是小于等于1的正数，且小数保留精度最大为3位!");
            inputObj.value="";
            inputObj.focus();
            return false;
        }
    }
    return true;
}
//返回查询页面
function goback(){
	window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartDiscountRateManager/queryPartDiscountRateInit.do';
}
</script>
</div>
</body>
</html>

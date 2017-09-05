<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件折扣率新增</title>
<style type="text/css">
.table_list_row0 td {
	background-color:#FFFFCC;
	border: 1px solid #DAE0EE;
	white-space:    nowrap;
}
</style>
</head>
<body onunload='javascript:destoryPrototype();' onload="loadcalendar();">
<div class="wbox">
	<div class="navigation"> <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
	  配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 配件折扣率维护 &gt; 新增
	</div>
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
    <input type="hidden" name="PART_ID" id="PART_ID"/>
	<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" >
		<th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" class="nav" />信息</th>
	     <tr>
	      <td width="10%"   align="right">折扣类型：</td>
	      <td width="20%">
	      <script type="text/javascript">
		       genSelBoxExp("DISCOUNT_TYPE",<%=Constant.PART_DISCOUNT_TYPE%>,"",false,"short_sel","","false",'');
	      </script>
	      </td>
	      <td width="10%"   align="right">有效日期：</td>
	      <td width="30%">
	            <input name="startDate" id="t1" value="" type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2">
        		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't1', false);"/>
        		&nbsp;至&nbsp;
        		<input name="endDate" id="t2" value="" type="text" class="short_txt" datatype="0,is_date,10" group="t1,t2">
        		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't2', false);"/>
	      </td>
	      <td  width="10%"  align="right">折扣率：</td>
	      <td  width="20%">
	      <input class="short_txt" type="text" name="DISCOUNT_RATE" id="DISCOUNT_RATE" onblur="chkRate(this);"/>
	      <font color="red">*</font>
	      </td>
	    </tr>
	    <tr id="<%=Constant.PART_DISCOUNT_TYPE_01 %>">
	        <td width="10%"   align="right" >服务商类型：</td>
      		<td width="20%">
      		<select id="FIX_TYPE" name="FIX_TYPE">
	         <c:forEach items="${list}" var="fixType">
             <option value="${fixType.FIX_VALUE }">${fixType.FIX_NAME }</option>
             </c:forEach>
	       </select>
	       </td>
      </tr>
	  <tr style="display:none" id="<%=Constant.PART_DISCOUNT_TYPE_02 %>">
	       <td  width="10%"  align="right">服务商：</td>
	      <td  width="20%">
	      <input class="middle_txt" type="text" name="DEALER_CODE" id="DEALER_CODE" datatype="0,is_null" readonly="readonly"/>
	      <input type="button" class="mark_btn" onclick="showMyOrgDealer('DEALER_CODE', '', 'true','',true,true,false,'');" value="&hellip;" />
	      </td>
      </tr>
	  <tr style="display:" id="<%=Constant.PART_DISCOUNT_TYPE_04 %>">
	       <td  width="10%"  align="right">金额：</td>
	      <td  width="20%">
	      <input class="middle_txt" type="text" name="ORDER_AMOUNT" id="ORDER_AMOUNT" datatype="0,is_null"/>
	      </td>
      </tr>
	    <tr style="display:none" id="<%=Constant.PART_DISCOUNT_TYPE_03 %>">
	       <td  width="10%"  align="right">配件：</td>
	      <td  width="20%">
	      <input class="middle_txt" type="text" name="PART_OLDCODE" id="PART_OLDCODE" datatype="0,is_null" readonly="readonly"/>
	      <input type="hidden" name="PART_CODE" id="PART_CODE"/>
	      <input type="hidden" name="PART_CNAME" id="PART_CNAME"/>
	      <input type="hidden" name="PART_ID" id="PART_ID"/>
	      <input class="mark_btn" type="button" value="&hellip;" onclick="showPartInfo1('PART_CODE','PART_OLDCODE','PART_CNAME','PART_ID','true')"/>
	      </td>
      </tr>
      <tr>
    	<td colspan="6" align="center">
            <input type="button" name="saveBtn" id="saveBtn" value="保存" onclick="saveDiscount();"  class="normal_btn"/>
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
    <table id="dtlInfo" class="table_list" style="display:none">
    <tr class="table_list_row0">
      <td>序号</td>
      <td>编码</td>
      <td>名称</td>
      <td>折扣率</td>
      <td>数量</td>
      <td>操作</td>
    </tr>
  </table>
  <table width="60%" align="right" style="display:none" id="pageTab">
		<tr>
			<td>
				<div id="barcon" name="barcon"></div>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript" >
var num = 1;//表格行数
var curNo=1;//当前页
function setPartDiscountDtl(id,OldCode,name){
	var disRate = $("DISCOUNT_RATE").value;
	var tbl = $('dtlInfo');
	var ids = id.split(",");
	var OldCodes = OldCode.split(",");
	var names = name.split(",");
    for(var i=1;i<tbl.rows.length;i++){
    	var no = tbl.rows[i].cells[0].firstChild.data;//序号
    	var dpId = $("DP_ID"+no).value;
        for(var j=0;j<ids.length;j++){
            if(dpId==ids[j]){
                MyAlert("名称:"+names[j]+" 已经存在!");
                return false;
            }
        }
    }
    document.getElementById("pageTab").style.display="";
	addCell(tbl,ids,OldCodes,names,disRate);
	return true;
}

function addCell(tbl,ids,OldCodes,names,disRate){
	for(var i = 0 ;i<ids.length;i++){
		var tempRow=0; 
		tempRow=tbl.rows.length; //行数
		var idTemp;
		var oldCodeTemp;
		var nameTemp;
		idTemp = ids[i];
		oldCodeTemp = OldCodes[i];
		nameTemp = names[i];

		var Rows=tbl.rows;//类似数组的Rows
		var newRow=tbl.insertRow(tbl.rows.length);//插入新的一行 
		  if(tempRow%2==0){
			  newRow.className   = "table_list_row2";
		  }else{
			  newRow.className  = "table_list_row1";
		  }
		  var Cells=newRow.cells;//类似数组的Cells
		  for(var j=0;j<6;j++){
			  var newCell=Rows(newRow.rowIndex).insertCell(Cells.length); 
			  newCell.align="center";
			  switch (j) 
			    { 
			      case 0 : newCell.innerHTML= tempRow+'<input type="hidden" name="DP_ID" id="DP_ID'+tempRow+'" value='+idTemp+'>';break; 
			      case 1 : newCell.innerHTML = oldCodeTemp +'<input type="hidden" name="DP_CODE" value='+oldCodeTemp+'>' ;break;
			      case 2 : newCell.innerHTML = nameTemp +'<input type="hidden" name="DP_NAME" value='+nameTemp+'>' ;break;
			      case 3 : newCell.innerHTML = '<input type="text" onchange="chkRate(this);" name="DIS_RATE'+tempRow+'" id="DIS_RATE'+tempRow+'" value='+disRate+'>' ;break;
                  case 4 : newCell.innerHTML = '<input type="text" onchange="checkAmount(this);" name="AMOUNT" id="AMOUNT'+tempRow+'">' ;break;
			      case 5 : newCell.innerHTML = '<input type="button" class="normal_btn" value="删除" onclick="delTableRow('+tempRow+');"/>' ;break;
			    } 
		  }
	}
	num = tbl.rows.length;
	goPage(1,10);
}

function checkAmount(obj){
	 var patrn = /^[1-9][0-9]*$/;
     if (!patrn.exec(obj.value)) {
         MyAlert("请录入正整数!");
         $(obj).value=$(obj).value.replace(/\D/g,'');
         $(obj).focus();
         return;
     }
}
function delTableRow(rowNum){ 
	   var tbl=document.getElementById("dtlInfo");
	    if (tbl.rows.length >rowNum){ 
	       tbl.deleteRow(rowNum); 
	      for (var i=rowNum;i<tbl.rows.length;i++)
	       {
	         tbl.rows[i].cells[0].innerText=i;
	         tbl.rows[i].cells[5].innerHTML="<input type=\"button\" class=\"cssbutton\"  name=\"deleteBtn\" value=\"删除\" onclick='delTableRow("+i+")'/></td></tr>";
	         if(i%2==0){
			    	tbl.rows[i].className   = "table_list_row2";
				  }else{
					  tbl.rows[i].className  = "table_list_row1";
				  }      
	      }
	   }
	    num = tbl.rows.length;
	    if(num==1){//如果已经全部删除
	   		document.getElementById("pageTab").style.display="none";
	   		$("PART_OLDCODE").value="";
	   		$("DEALER_CODE").value="";
	   		return;
	   	}
	    if(((num-1)%10)==0){
		    if(curNo==1){//如果是首页
		    	goPage(curNo,10);
		    	return;
		    }
	   		goPage((curNo-1),10);
	   		return;
	   	}  
	    goPage(curNo,10);
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

function showPartInfo1(inputCode,inputOldCode,inputName ,inputId ,isMulti){
	var disRate = $("DISCOUNT_RATE").value;
	var inputObj = $("DISCOUNT_RATE");
	if(!disRate){
		MyAlert("请先填写折扣率!");
		inputObj.focus();
		return;
	}
	if(isNaN(disRate)){
		MyAlert("折扣率是小于等于1的正数，且小数保留精度最大为3位!");
        inputObj.value="";
        inputObj.focus();
        return;
	}
	if(inputObj.value>1||inputObj.value==0){
		MyAlert("折扣率是小于等于1的正数，且小数保留精度最大为3位!");
        inputObj.value="";
        inputObj.focus();
        return;
	}
    if (inputObj.value.indexOf("0.") >= 0) {
    	var pattern = /(^0\.(0{1,2}[1-9])$)|(^0\.([1-9]{1,3})$)/;
        if(!pattern.exec(inputObj.value)){
            MyAlert("折扣率是小于等于1的正数，且小数保留精度最大为3位!");
            inputObj.value="";
            inputObj.focus();
            return;
        }
    }
    if (inputObj.value.indexOf("1.") >= 0) {
    	var pattern1 = /^1\.(0{1,3})$/;
        if(!pattern1.exec(inputObj.value)){
        	MyAlert("折扣率是小于等于1的正数，且小数保留精度最大为3位!");
            inputObj.value="";
            inputObj.focus();
            return;
        }
    }
	if(!inputName){ inputName = null;}
	if(!inputId){ inputId = null;}
	OpenHtmlWindow("<%=contextPath%>/jsp/parts/baseManager/partsBaseManager/partDiscount/partSelectForDis.jsp?INPUTCODE="+inputCode+"&INPUTOLDCODE="+inputOldCode+"&INPUTNAME="+inputName+"&INPUTID="+inputId+"&ISMULTI="+isMulti,730,390);
}

//验证折扣率
function chkRate(inputObj){
	if(!inputObj.value){
		MyAlert("折扣率不能为空!");
		return;
	}
	if(isNaN(inputObj.value)){
		MyAlert("折扣率是小于等于1的正数，且小数保留精度最大为3位!");
        inputObj.value="";
        return;
	}
	if(inputObj.value>1||inputObj.value==0){
		MyAlert("折扣率是小于等于1的正数，且小数保留精度最大为3位!");
        inputObj.value="";
        return;
	}
    if (inputObj.value.indexOf("0.") >= 0) {
        var pattern = /(^0\.(0{1,2}[1-9])$)|(^0\.([1-9]{1,3})$)/;
        if(!pattern.exec(inputObj.value)){
            MyAlert("折扣率是小于等于1的正数，且小数保留精度最大为3位!");
            inputObj.value="";
            return;
        }
    }
    if (inputObj.value.indexOf("1.") >= 0) {
    	var pattern1 = /^1\.(0{1,3})$/;
        if(!pattern1.exec(inputObj.value)){
        	MyAlert("折扣率是小于等于1的正数，且小数保留精度最大为3位!");
            inputObj.value="";
            return;
        }
    }

      var tbl = $('dtlInfo');
      var Rows=tbl.rows;//类似数组的Rows
	  for(var i=1;i<Rows.length;i++){
		  var Cells=Rows[i].cells;//类似数组的Cells
		  var rateCell=Cells[3];
		  rateCell.innerHTML = '<input type="text" name="DIS_RATE'+i+'" value='+inputObj.value+'>' ;
	  }
}

function doCusChange(value){
	if(value==<%=Constant.PART_DISCOUNT_TYPE_01%>){
		document.getElementById(value).style.display="";
		document.getElementById(<%=Constant.PART_DISCOUNT_TYPE_02%>).style.display="none";
		document.getElementById(<%=Constant.PART_DISCOUNT_TYPE_03%>).style.display="none";
		document.getElementById(<%=Constant.PART_DISCOUNT_TYPE_04%>).style.display="";
		document.getElementById("file").style.display="none";
		document.getElementById("dtlInfo").style.display="none";
		document.getElementById("pageTab").style.display="none";
	}
	if(value==<%=Constant.PART_DISCOUNT_TYPE_02%>){
		document.getElementById(value).style.display="";
		document.getElementById(<%=Constant.PART_DISCOUNT_TYPE_01%>).style.display="none";
		document.getElementById(<%=Constant.PART_DISCOUNT_TYPE_03%>).style.display="none";
		document.getElementById(<%=Constant.PART_DISCOUNT_TYPE_04%>).style.display="none";
		document.getElementById("file").style.display="";
		document.getElementById("dtlInfo").style.display="";
		document.getElementById("pageTab").style.display="none";
		var tbl = $('dtlInfo');
		var ln = tbl.rows.length;
		if(ln>1){
			for (var i=tbl.rows.length-1;i>=1;i--)
		       {
				tbl.deleteRow(i);
		      }
		}
		$("PART_OLDCODE").value="";
	}
	if(value==<%=Constant.PART_DISCOUNT_TYPE_03%>){
		document.getElementById(value).style.display="";
		document.getElementById(<%=Constant.PART_DISCOUNT_TYPE_01%>).style.display="none";
		document.getElementById(<%=Constant.PART_DISCOUNT_TYPE_02%>).style.display="none";
		document.getElementById(<%=Constant.PART_DISCOUNT_TYPE_04%>).style.display="none";
		document.getElementById("file").style.display="";
		document.getElementById("dtlInfo").style.display="";
		document.getElementById("pageTab").style.display="none";
		var tbl = $('dtlInfo');
		var ln = tbl.rows.length;
		if(ln>1){
			for (var i=tbl.rows.length-1;i>=1;i--)
		       {
				tbl.deleteRow(i);
		      }
		}
		$("DEALER_CODE").value="";
	}
	if(value==<%=Constant.PART_DISCOUNT_TYPE_04%>){
		document.getElementById(value).style.display="";
		document.getElementById(<%=Constant.PART_DISCOUNT_TYPE_01%>).style.display="none";
		document.getElementById(<%=Constant.PART_DISCOUNT_TYPE_02%>).style.display="none";
		document.getElementById(<%=Constant.PART_DISCOUNT_TYPE_03%>).style.display="none";
		document.getElementById("file").style.display="none";
		document.getElementById("dtlInfo").style.display="none";
		document.getElementById("pageTab").style.display="none";
		var tbl = $('dtlInfo');
		var ln = tbl.rows.length;
		for(var i=1;i<ln;i++){
			if(tbl.hasChildNodes()){
				tbl.removeChild(tbl.lastChild);
			}
		}
	}
}

function goPage(pno,psize){
	curNo = pno;
	var itable = document.getElementById("dtlInfo");
	var totalPage = 0;//总页数
	var pageSize = psize;//每页显示行数
	if((num-1)/pageSize > parseInt((num-1)/pageSize)){   
   		 totalPage=parseInt((num-1)/pageSize)+1;   
   	}else{   
   		totalPage=parseInt((num-1)/pageSize);   
   	} 
   	
	var currentPage = pno;//当前页数
	var startRow = (currentPage - 1) * pageSize+1;//开始显示的行   
   	var endRow = currentPage * pageSize+1;//结束显示的行   
   	endRow = (endRow > num)? num : endRow;
	//前三行始终显示
	for(i=0;i<1;i++){
		var irow = itable.rows[i];
		irow.style.display = "block";
	}
	
	for(var i=1;i<num;i++){
		var irow = itable.rows[i];
		if(i>=startRow&&i<endRow){
			irow.style.display = "block";	
		}else{
			irow.style.display = "none";
		}
	}
	var tempStr = "共"+(num-1)+"条记录  共"+totalPage+"页 当前第"+currentPage+"页";
	if(currentPage>1){
		tempStr += "<a href=\"#\" onClick=\"goPage("+(currentPage-1)+","+psize+")\">上一页</a>"
	}else{
		tempStr += "上一页";	
	}
	if(currentPage<totalPage){
		tempStr += "<a href=\"#\" onClick=\"goPage("+(currentPage+1)+","+psize+")\">下一页</a>";
	}else{
		tempStr += "下一页";	
	}
	if(currentPage>1){
		tempStr += "<a href=\"#\" onClick=\"goPage("+(1)+","+psize+")\">首页</a>";
	}else{
		tempStr += "首页";
	}
	if(currentPage<totalPage){
		tempStr += "<a href=\"#\" onClick=\"goPage("+(totalPage)+","+psize+")\">尾页</a>";
	}else{
		tempStr += "尾页";
	}
	document.getElementById("barcon").innerHTML = tempStr;
	
}

function saveDiscount(){//保存配件折扣率
	if (!submitForm('fm')) {
        return;
    }
	var rateObj = $("DISCOUNT_RATE");
	var flag = checkNumberLength(rateObj);
	if(!flag){
		return;
	}

	var tbl = $('dtlInfo');
	for(var i=1;i<tbl.rows.length;i++){
        var rateObj = tbl.rows[i].cells[3].firstChild;//折扣率
        var flag1 = checkNumberLength(rateObj);
        if(!flag1){
    		return;
    	}
    }
	
	var discountType = $("DISCOUNT_TYPE").value;
    if(discountType==<%=Constant.PART_DISCOUNT_TYPE_03%>){
        for(var i=1;i<tbl.rows.length;i++){
            var no = tbl.rows[i].cells[4].firstChild.value;//序号
            if( no == ""){
                MyAlert("第"+i+"行数量不能为空！");
                return;
            }
            var patrn = /^[1-9][0-9]*$/;
            if (!patrn.exec(no)) {
                MyAlert("第"+i+"行数量只能输入正整数！");
                return;
            }

        }
    }
	if(discountType==<%=Constant.PART_DISCOUNT_TYPE_04%>){
		var obj = $("ORDER_AMOUNT");
		if (!obj.value) {
	        MyAlert("金额不能为空!");
	        return;
	    }
	    var patrn = /^((0\.[0-9]*[1-9][0-9]*)|([1-9][0-9]*\.[0-9]+)|([1-9][0-9]*))$/;
	    if (!patrn.exec(obj.value)) {
	        MyAlert("金额无效,请重新输入!");
	        obj.value = "";
	        return;
	    } else {
	        if (obj.value.indexOf(".") >= 0) {
	            var patrn = /^[0-9]{0,10}.[0-9]{0,2}$/;
	            if (!patrn.exec(obj.value)) {
	                MyAlert("金额整数部分不能超过10位,且保留精度最大为2位!");
	                obj.value = "";
	                return;
	            }
	        } else {
	            var patrn = /^[0-9]{0,10}$/;
	            if (!patrn.exec(obj.value)) {
	                MyAlert("金额整数部分不能超过10位!");
	                obj.value = "";
	                return;
	            }
	        }
	    }
	}

	if(confirm("确定保存?")){
		btnDisable();
		var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartDiscountRateManager/savePartDiscount.json?';
		sendAjax(url, getResult, 'fm');
	}
}

function getResult(jsonObj){
	btnEable();
	if(jsonObj!=null){
	    var success = jsonObj.success;
	    var error = jsonObj.error;
	    var curQty = jsonObj.curQty;
	    var partId = jsonObj.partId;
	    var exceptions = jsonObj.Exception;
	    if(success){
	    	MyAlert(success);
	    	window.location.href = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartDiscountRateManager/queryPartDiscountRateInit.do";
	    }else if(error){
	    	MyAlert(error);
	    }else if(exceptions){
	    	MyAlert(exceptions.message);
		}
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

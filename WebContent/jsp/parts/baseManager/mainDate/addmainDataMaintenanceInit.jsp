<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${path}/common/jsp_head_new.jsp" />
<script language="javascript" type="text/javascript">
    function doInit(){
		loadcalendar();  //初始化时间控件
	}
</script>
<title>补品赠送设置新增</title>
</head>
<BODY >
<div class="wbox">
  <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件基础信息管理 &gt; 配件基础信息维护 &gt; 配件赠送设置新增</div>

  	<form id="fm" name="fm" method="post">
	  <table width="100%" align="center" class="table_list" style="background-color: #F0F7F2;">
    <tr style="height: 30px; line-height: 30px;">
      <td width="10%"  align="right">赠品方式：</td>
	  <td width="20%" align="left" >
	  <script type="text/javascript">
	   	 genSelBoxExp("giftWay",<%=Constant.PART_GIFT_WAY%>,<%=Constant.PART_GIFT_WAY_01%>,true,"short_sel","onchange='showTable()'","false",'');
	  </script>
	  <font color="red">*</font>
	  </td>
	  <td width="10%" align="right">活动日期：</td>
	  <td width="25%" align="left">
	    <input id="checkSDate" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" readonly="readonly" />
	    <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
	    至&nbsp;
	    <input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" readonly="readonly" />
		<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
		<font color="red">*</font>
	  </td>
	  <td width="10%" align="right">是否本部发起：</td> 
	  <td width="20%" align="left">
		<script type="text/javascript">
	   	 genSelBoxExp("isOemStart",<%=Constant.IF_TYPE%>,<%=Constant.IF_TYPE_NO%>,true,"short_sel","","false",'');
		</script>
	  </td> 
    </tr>
  </table>
  <table class="table_list" id="tableDefault"  style="background-color: #F0F7F2; display: none;" >
    <tr style="height: 30px; line-height: 30px;">
      <td width="10%" align="right">配件赠品描述：</td> 
	  <td width="25%" align="left">
		  <input type="text" id="PER_REMARK2" name="PER_REMARK2" class="long_txt" />
		  <font color="red">*</font>
	  </td>
	  <td align="center" width="30%" colspan="2">
      <input class="normal_btn" type="button" id="saveButton" value="保 存" name="button5" onclick="savepart();"/>
      <input class="normal_btn" type="button" value="返 回" id="backButton" name="button2" onclick="goBack();"/>
      </td>
	  <td width="10%" align="right" ></td> 
	  <td width="20%" align="left">
	  </td> 
    </tr>
	<tr style="height: 30px; line-height: 30px;">
          <th colspan=6 align=left><img class=nav src="<%=contextPath%>/img/subNav.gif" />选择配件
          <input type="hidden" name="PART_OLDCODE" id="PART_OLDCODE" />
	      <input type="hidden" name="PART_CODE" id="PART_CODE"/>
	      <input type="hidden" name="PART_CNAME" id="PART_CNAME"/>
	      <input type="hidden" name="PART_ID" id="PART_ID"/>
          <input class="normal_btn" onclick="showPartInfo1('PART_CODE','PART_OLDCODE','PART_CNAME','PART_ID','true');" value="增加" type="button" name="cancelBtn2" /></th>
    </tr>
 
  </table>
  <table class="table_list" id="tableBlock"  style="background-color: #F0F7F2; display: block;" >
    <tr style="height: 30px; line-height: 30px;">
      <td width="10%" align="right">配件赠品描述：</td> 
	  <td width="25%" align="left">
		  <input type="text" id="PER_REMARK1" name="PER_REMARK1" class="long_txt" />
		  <font color="red">*</font>
	  </td>
	  <td width="10%" align="right">销货金额大于：</td> 
	  <td width="25%" align="left">
		<input type="text" id="PER_NAME" name="PER_NAME" class="middle_txt" onchange="dataTypeCheck(this)" /> (单位：<font color="blue">元</font>)
		<font color="red">*</font>
	  </td>
	  <td align="center" width="30%" colspan="2">
      <input class="normal_btn" type="button" id="saveButton" value="保 存" name="button5" onclick="savepart();"/>
      <input class="normal_btn" type="button" value="返 回" id="backButton" name="button2" onclick="goBack();"/>
      </td>
    </tr>
	<tr style="height: 30px; line-height: 30px;">
          <th colspan=6 align=left><img class=nav src="<%=contextPath%>/img/subNav.gif" />选择配件
          <input type="hidden" name="PART_OLDCODE" id="PART_OLDCODE" />
	      <input type="hidden" name="PART_CODE" id="PART_CODE"/>
	      <input type="hidden" name="PART_CNAME" id="PART_CNAME"/>
	      <input type="hidden" name="PART_ID" id="PART_ID"/>
          <input class="normal_btn" onclick="showPartInfo1('PART_CODE','PART_OLDCODE','PART_CNAME','PART_ID','true');" value="增加" type="button" name="cancelBtn2" /></th>
    </tr>
  </table>
  
  <table id="file" class="table_list" style="display:none">
    <tr>
      <th colspan="7" align="left"><img class="nav" src="<%=contextPath%>/img/nav.gif" />配件赠送明细
	  </th>
    </tr>
  </table>
  <div id="listDiv1" style="display:block;">
  <table id="dtlInfo1" class="table_list" >
    <tr class="table_list_row2">
      <td>序号</td>
      <td>配件编码</td>
      <td>配件名称</td>
      <td>赠送数量</td>
      <td>是否有效</td>
      <td>操作</td>
    </tr>
  </table>
  </div>
  <div id="listDiv2" style="display:none">
  <table id="dtlInfo2" class="table_list" >
    <tr class="table_list_row2">
      <td>序号</td>
      <td>配件编码</td>
      <td>配件名称</td>
      <td>满足数量</td>
      <td>赠送数量</td>
      <td>是否有效</td>
      <td>操作</td>
    </tr>
  </table>
  </div>
  <table width="60%" align="right" style="display:none" id="pageTab">
		<tr>
			<td>
				<div id="barcon" name="barcon"></div>
			</td>
		</tr>
	</table>

  </form>

</div>
</BODY>
<script type="text/javascript">
function showTable()
{
	var giftWay2 = <%=Constant.PART_GIFT_WAY_02%>;
	var giftWay = document.getElementById("giftWay").value;
	var tableObj1 = document.getElementById("tableDefault");
	var tableObj2 = document.getElementById("tableBlock");
	var divObj1 = document.getElementById("listDiv1");
	var divObj2 = document.getElementById("listDiv2");
	var tbl = "";
	if(giftWay2 == giftWay)
	{
		tableObj2.style.cssText = "display: none;background-color: #F0F7F2;";
		tableObj1.style.cssText = "display: block;background-color: #F0F7F2;";
		divObj1.style.cssText = "display: none;";
		divObj2.style.cssText = "display: block;";
	}
	else
	{
		tableObj1.style.cssText = "display: none;background-color: #F0F7F2;";
		tableObj2.style.cssText = "display: block;background-color: #F0F7F2;";
		divObj2.style.cssText = "display: none;";
		divObj1.style.cssText = "display: block;";
	}
}
function savepart(){
	var PER_REMARK = "";
	var giftWay2 = <%=Constant.PART_GIFT_WAY_02%>;
	var giftWay = document.getElementById("giftWay").value;
	if(giftWay2 == giftWay)
	{
		PER_REMARK = document.getElementById("PER_REMARK2").value;
		if(PER_REMARK == null || "" == PER_REMARK){
			MyAlert("请填写配件赠品描述!");	
			return false;
		}
	}
	else
	{
		PER_REMARK = document.getElementById("PER_REMARK1").value;
		if(PER_REMARK == null || "" == PER_REMARK){
			MyAlert("请填写配件赠品描述!");	
			return false;
		}
		var  PER_NAME = document.getElementById("PER_NAME").value;
		if(PER_NAME==null||PER_NAME.length==0){
			MyAlert("请填写销货金额!");	
			return false;
		}
	}
	var   Amount= document.getElementsByName("Amount");
	
	if(Amount==null||Amount.length==0)
	{
		MyAlert("请选择配件赠送明细!");	
		return false;

	}
	for(var i=0;i<Amount.length;i++){
		if(Amount[i]==null||Amount[i]==""){
			MyAlert("请填写配件赠送数量!");	
			return false;	
		}
		var returnPrice = is_digit(Amount[i]);
		if(returnPrice != true) {
			MyAlert("配件赠送数量"+returnPrice);
			return false;	
		}
		
	}
	if (submitForm('fm')) {
		MyConfirm("确认保存设置?", savePart);
	}

}
function savePart()
{
	btnDisable();
	var url = '<%=contextPath%>/parts/baseManager/mainData/mainDataMaintenance/saveMainDataMaintenance.json';
	makeFormCall(url,showResult,'fm');

}

function showResult(json) {
	btnEnable();
    if (json.errorExist != null && json.errorExist.length > 0) {
    	MyAlert(json.errorExist);
    } else if (json.success != null && json.success == "true") {
    	MyAlert("新增成功!");
    	location.href = "<%=request.getContextPath()%>/parts/baseManager/mainData/mainDataMaintenance/mainDataMaintenanceInit.do";
    } else {
    	MyAlert("新增失败，请联系管理员!");
    }
}

//数据验证
function dataTypeCheck1(obj)
{
	var value = obj.value;
    if (isNaN(value) || "" == value) {
        MyAlert("请输入数字!");
        obj.value = "";
        return;
    }
    var re = /^([1-9]+[0-9]*]*)$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入正整数!");
        obj.value = "";
        return;
    }
}

function dataTypeCheck(obj)
{
	var value = obj.value;
	value = value + "";
	value = parseFloat(value.replace(new RegExp(",","g"),""));
    if (isNaN(value) || "" == value) {
        MyAlert("请输入数字!");
        obj.value = (0.00).toFixed(2);
        return;
    }
    if(0 > value)
    {
	    MyAlert("销货金额大于不能小于 0!");
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
var num = 1;//表格行数
var curNo=1;//当前页
function setPartDiscountDtl(id,OldCode,name,code){
	var giftWay2 = <%=Constant.PART_GIFT_WAY_02%>;
	var giftWay = document.getElementById("giftWay").value;
	var divObj1 = document.getElementById("listDiv1");
	var divObj2 = document.getElementById("listDiv2");
	var tbl = "";
	if(giftWay2 == giftWay)
	{
		divObj1.style.cssText = "display: none;";
		if("block" != divObj2.style.display + "")
		{
			divObj2.style.cssText = "display: block;";
		}
		tbl = document.getElementById("dtlInfo2");
	}
	else
	{
		divObj2.style.cssText = "display: none;";
		if("block" != divObj1.style.display + "")
		{
			divObj1.style.cssText = "display: block;";
		}
		tbl = document.getElementById("dtlInfo1");
	}
	 
	var ids = id.split(",");
	var OldCodes = OldCode.split(",");
	var codes = code.split(",");
	var names = name.split(",");
	var prePartIds = document.getElementsByName("partIds");
	var partIdArr = new Array(); 
	if(null != prePartIds && prePartIds.length > 0)
	{
		var idsSize = prePartIds.length;
		for(var i = 0; i < idsSize; i++)
		{
			partIdArr.push([prePartIds[i].value]);
		}
	}
	
	for(var j=0;j<ids.length;j++){
    	if(partIdArr.length > 0 && partIdArr.toString().indexOf(ids[j]) > -1)
			{
            MyAlert("名称:"+names[j]+" 已经存在!");
            return false;
        }
    }
    
    document.getElementById("pageTab").style.cssText = "display: block;";
    
	addCell(tbl,ids,OldCodes,names,codes);
	return true;
}

function addCell(tbl,ids,OldCodes,names,code){

	for(var i = 0 ;i<ids.length;i++){
		var tempRow=0; 
		tempRow=tbl.rows.length; //行数
		var idTemp;
		var oldCodeTemp;
		var nameTemp;
		var codeTemp;
		idTemp = ids[i];
		oldCodeTemp = OldCodes[i];
		nameTemp = names[i];
		codeTemp = code[i];

		var Rows=tbl.rows;//类似数组的Rows
		var newRow=tbl.insertRow(tbl.rows.length);//插入新的一行 
		  if(tempRow%2==0){
			  newRow.className   = "table_list_row2";
		  }else{
			  newRow.className  = "table_list_row1";
		  }
		  var Cells=newRow.cells;//类似数组的Cells
		  var giftWay2 = <%=Constant.PART_GIFT_WAY_02%>;
		  var giftWay = document.getElementById("giftWay").value;
		  if(giftWay2 == giftWay)
		  {
			  for(var j=0;j<7;j++){
				  var newCell=Rows(newRow.rowIndex).insertCell(Cells.length); 
				  newCell.align="center";
				  switch (j) 
				    { 
				      case 0 : newCell.innerHTML= tempRow;break; 
				      case 1 : newCell.innerHTML = oldCodeTemp +'<input type="hidden" name="partIds" value='+idTemp+'><input type="hidden" name="DP_CODE" value='+oldCodeTemp+'>' ;break;
				      case 2 : newCell.innerHTML = nameTemp +'<input type="hidden" name="DP_NAME" value='+nameTemp+'><input type="hidden" name="partCode" value='+codeTemp+'>' ;break;
				      case 3 : newCell.innerHTML = '<input id="meetQty+'+j+'" type="text" name="meetQty" value="1" onchange="dataTypeCheck1(this)" datatype="0,is_digit,10" >' ;break;
				      case 4 : newCell.innerHTML = '<input id="Amount+'+j+'" type="text" name="Amount" value="1" onchange="dataTypeCheck1(this)" datatype="0,is_digit,10" >' ;break;
				      case 5 : newCell.innerHTML = '有效<input id="sTatus" name="sTatus" value ="10011001" type="hidden">';break;
				      case 6 : newCell.innerHTML = '<input type="button" class="normal_btn" value="删除" onclick="delTableRow('+tempRow+');"/>' ;break;  
				    } 
			  }
		  }
		  else
		  {
			  for(var j=0;j<6;j++){
				  var newCell=Rows(newRow.rowIndex).insertCell(Cells.length); 
				  newCell.align="center";
				  switch (j) 
				    { 
				      case 0 : newCell.innerHTML= tempRow;break; 
				      case 1 : newCell.innerHTML = oldCodeTemp +'<input type="hidden" name="partIds" value='+idTemp+'><input type="hidden" name="DP_CODE" value='+oldCodeTemp+'>' ;break;
				      case 2 : newCell.innerHTML = nameTemp +'<input type="hidden" name="DP_NAME" value='+nameTemp+'><input type="hidden" name="partCode" value='+codeTemp+'>' ;break;
				      case 3 : newCell.innerHTML = '<input id="Amount+'+j+'" type="text" name="Amount" value="1" onchange="dataTypeCheck1(this)" datatype="0,is_digit,10" >' ;break;
				      case 4 : newCell.innerHTML = '有效<input id="sTatus" name="sTatus" value ="10011001" type="hidden">';break;
				      case 5 : newCell.innerHTML = '<input type="button" class="normal_btn" value="删除" onclick="delTableRow('+tempRow+');"/>' ;break;  
				    } 
			  }
		  }
		  
	}
	num = tbl.rows.length;
	goPage(1,15);
}

function delTableRow(rowNum){
	var giftWay2 = <%=Constant.PART_GIFT_WAY_02%>;
	var giftWay = document.getElementById("giftWay").value;
	var tbl = "";
	if(giftWay2 == giftWay)
	{
		tbl = document.getElementById("dtlInfo2");
	}
	else
	{
		tbl = document.getElementById("dtlInfo1");
	}
    
    if (tbl.rows.length >rowNum){ 
       tbl.deleteRow(rowNum); 
      for (var i=rowNum;i<tbl.rows.length;i++)
       {
         tbl.rows[i].cells[0].innerText= i;
         if(giftWay2 == giftWay)
     	{
        	 tbl.rows[i].cells[6].innerHTML="<input type=\"button\" class=\"cssbutton\"  name=\"deleteBtn\" value=\"删除\" onclick='delTableRow("+i+")'/></td></tr>";
     	}
     	else
     	{
     		tbl.rows[i].cells[5].innerHTML="<input type=\"button\" class=\"cssbutton\"  name=\"deleteBtn\" value=\"删除\" onclick='delTableRow("+i+")'/></td></tr>";
     	}
         
         if(i%2==0){
		    	tbl.rows[i].className   = "table_list_row2";
			  }else{
				  tbl.rows[i].className  = "table_list_row1";
			  }      
       	  }
    }
    num = tbl.rows.length;
    if(num==1){//如果已经全部删除
   		document.getElementById("pageTab").style.cssText="display: none;";
   	    
   		$("PART_OLDCODE").value="";
   		$("DEALER_CODE").value="";
   		return;
   	}
   	if(((num-1)%15)==0){
	    if(curNo==1){//如果是首页
	    	goPage(curNo,15);
	    	return;
	    }
   		goPage((curNo-1),15);
   		return;
   	}  
    goPage(curNo,15);
} 


function showPartInfo1(inputCode,inputOldCode,inputName ,inputId ,isMulti){
	if(!inputName){ inputName = null;}
	if(!inputId){ inputId = null;}
	OpenHtmlWindow("<%=contextPath%>/jsp/parts/baseManager/partsBaseManager/partDiscount/partSelectForDis.jsp?INPUTCODE="+inputCode+"&INPUTOLDCODE="+inputOldCode+"&INPUTNAME="+inputName+"&INPUTID="+inputId+"&ISMULTI="+isMulti,730,500);
}


function goPage(pno,psize){
	curNo = pno;
	var giftWay2 = <%=Constant.PART_GIFT_WAY_02%>;
	var giftWay = document.getElementById("giftWay").value;
	var itable = "";
	if(giftWay2 == giftWay)
	{
		itable = document.getElementById("dtlInfo2");
	}
	else
	{
		itable = document.getElementById("dtlInfo1");
	}
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
	var tempStr = "共"+(num-1)+"条记录  共"+totalPage+"页 当前第"+currentPage+"页 ";
	if(currentPage>1){
		tempStr += "<a href=\"#\" onClick=\"goPage("+(currentPage-1)+","+psize+")\">&nbsp;上一页&nbsp;</a>"
	}else{
		tempStr += " 上一页 ";	
	}
	if(currentPage<totalPage){
		tempStr += "<a href=\"#\" onClick=\"goPage("+(currentPage+1)+","+psize+")\">&nbsp;下一页&nbsp;</a>";
	}else{
		tempStr += " 下一页 ";	
	}
	if(currentPage>1){
		tempStr += "<a href=\"#\" onClick=\"goPage("+(1)+","+psize+")\">&nbsp;首页&nbsp;</a>";
	}else{
		tempStr += " 首页 ";
	}
	if(currentPage<totalPage){
		tempStr += "<a href=\"#\" onClick=\"goPage("+(totalPage)+","+psize+")\">&nbsp;尾页&nbsp;</a>";
	}else{
		tempStr += " 尾页 ";
	}
	document.getElementById("barcon").innerHTML = tempStr;
	
}



function goBack(){
	btnEnable();
	window.location.href = "<%=request.getContextPath()%>/parts/baseManager/mainData/mainDataMaintenance/mainDataMaintenanceInit.do";
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
</html>

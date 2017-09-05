<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/common.js"></script>
<title>服务活动</title>
<style type="text/css">

</style>
<script type="text/javascript">
$(document).ready(function(){
	//loadcalendar();  //初始化时间控件 
	setPartCodes();
	setCodesAndNames();
	});
//动态生成表格
function addRow(tableId){
    var addTable = document.getElementById(tableId);
	var rows = addTable.rows;
	var length = rows.length;
	var insertRow = addTable.insertRow(length);
	insertRow.className = "table_list_row1";
	insertRow.insertCell(0);
	insertRow.insertCell(1);
	insertRow.insertCell(2);
	insertRow.insertCell(3);

	if(tableId == 'partTableId'){
		insertRow.insertCell(4);
		insertRow.insertCell(5);
		insertRow.insertCell(6);
		insertRow.insertCell(7);
	}
	if (tableId=='itemTableId') {
		/* addTable.rows[length].cells[0].innerHTML =  '<td style="text-align: center"><input type="hidden"  id="LABOUR_ID'+length+'" name="LABOUR_ID" /><input type="text" class="middle_txt"  id="LABOUR_CODE'+length+'" name="LABOUR_CODE" readonly  value="" size="10"/><a href="#" name="a1" id="'+length+'"style="text-decoration:none;" onClick="javascript:addLabour(this);">&nbsp;选 择</a><span style="color:red">*</span></td>'; */ 
		addTable.rows[length].cells[0].innerHTML =  '<td style="text-align: center"><input type="hidden"  id="LABOUR_ID'+length+'" name="LABOUR_ID" /><input type="text" class="middle_txt"  id="LABOUR_CODE'+length+'" name="LABOUR_CODE" readonly  value="" size="10" onClick="javascript:addLabour(' + length + ');"/><span style="color:red">*</span></td>';
		addTable.rows[length].cells[1].innerHTML =  '<td style="text-align: center"><span class="tbwhite"><input type="text" id="LABOUR_NAME'+length+'" name="LABOUR_NAME" class="middle_txt" value="" size="10"  readonly /></span></td>';
		addTable.rows[length].cells[2].innerHTML =  '<td style="text-align: center"><span class="tbwhite"><input type="text" id="LABOUR_HOUR'+length+'" name="LABOUR_HOUR" class="middle_txt" value="" size="10"  readonly /></span></td>';
		addTable.rows[length].cells[3].innerHTML =  '<td style="text-align: center"><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this);"/></td>';
	}else if (tableId == 'partTableId') {
		/* addTable.rows[length].cells[0].innerHTML =  '<td align="center"><input type="hidden"  id="PART_ID'+length+'" name="PART_ID" /><input type="text" class="middle_txt" name="PART_CODE"   value="" size="10" id="PART_CODE'+length+'" readonly/><a href="#" id="'+length+'" name="a2" style="text-decoration:none;" onClick="javascript:addPart(this);">&nbsp;选 择</a><span style="color:red">*</span></td>'; */
		addTable.rows[length].cells[0].innerHTML =  '<td align="center"><input type="hidden"  id="PART_ID'+length+'" name="PART_ID" /><input type="text" class="middle_txt" style="width:120px;" name="PART_CODE"   value="" size="10" id="PART_CODE'+length+'" onClick="javascript:addPart(' + length + ');" readonly/><span style="color:red">*</span></td>';
		addTable.rows[length].cells[1].innerHTML =  '<td align="center"><input type="text" class="middle_txt" style="width:120px;" name="PART_NAME"   id="PART_NAME'+length+'"  maxlength="20"/></td>';
		addTable.rows[length].cells[2].innerHTML =  '<td align="center" ><input type="text" class="middle_txt"  style="width:40px;" size="10" name="QUANTITY" id="QUANTITY'+length+'" /></td>';
		addTable.rows[length].cells[3].innerHTML =  '<td align="center"><input type="hidden"  id="hoursCode'+length+'" name="hoursCode" value=""/><select value="" name="Labour0" id="Labour0" class="u-select" style="width:150px"><option value="-1">-请选择-</option></select></td>';
		addTable.rows[length].cells[4].innerHTML =  genSelBoxExpPay("HAS_PART"+length,<%=Constant.IF_TYPE%>,"",true,"u-select","","false",'');
		addTable.rows[length].cells[5].innerHTML =  '<td align="center"><select id="Part'+length+'"  name="Part0" class="u-select"><option value="-1">-请选择-</option></select><input type="hidden"  id="partMain" name="partMain" value=""/></td>';
		addTable.rows[length].cells[6].innerHTML =  genSelBoxExpPay("PART_USE_TYPE",<%=Constant.PART_USE_TYPE%>,"",false,"u-select","","false",'');
		addTable.rows[length].cells[7].innerHTML = '<td align="center"><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this);"/></td>';
	
		}
	 	setCodesAndNames();
		return addTable.rows[length];
	}
	
	
	function changeId(){
		var LID = document.getElementsByName("LABOUR_ID");
		var LCODE = document.getElementsByName("LABOUR_CODE");
		var LNAME = document.getElementsByName("LABOUR_NAME");
		var LHOUR = document.getElementsByName("LABOUR_HOUR");
		var a1 = document.getElementsByName("a1");
		var l = LID.length;
		var k=0;
		for(var i=0;i<l;i++)
		{   			
			k=i+1; 
			a1[i].id=k;
			LID[i].id="LABOUR_ID"+k;
			LCODE[i].id="LABOUR_CODE"+k;
			LNAME[i].id="LABOUR_NAME"+k;
			LHOUR[i].id="WRGROUP_CODE"+k;
		}
	}
	
	function changeIds(){
		var PID = document.getElementsByName("PART_ID");
		var PCODE = document.getElementsByName("PART_CODE");
		var PNAME = document.getElementsByName("PART_NAME");
		var QUANTITY = document.getElementsByName("QUANTITY");
		var HPART = document.getElementsByName("HAS_PART");
		var a2 = document.getElementsByName("a2");
		var l = PID.length;
		var k=0;
		for(var i=0;i<l;i++)
		{   			
			k=i+1; 
			a2[i].id=k;
			PID[i].id="PART_ID"+k;
			PCODE[i].id="PART_CODE"+k;
			PNAME[i].id="PART_NAME"+k;
			QUANTITY[i].id="QUANTITY"+k;
			HPART[i].id="HAS_PART"+k;
		}
	}
	
	//删除行
	function delItem(obj){
	    var tr = this.getRowObj(obj);
	   
		   if(tr != null){
			    tr.parentNode.removeChild(tr);
			    setCodesAndNames();
				changeId()
		   }else{
		    throw new Error("the given object is not contained by the table");
		   }
		   setCodesAndNames();
	}
	//根据得到的行对象得到所在的行数
	function getRowNo(obj){
	   var trObj = getRowObj(obj); 
	   var trArr = trObj.parentNode.children;
	   var ret;
	 for(var trNo= 0; trNo < trArr.length; trNo++){
	  if(trObj == trObj.parentNode.children[trNo]){
	  		ret = trNo;
	  		break;
	  }
	 }
	 return ret;
	}
	
	//得到行对象
	function getRowObj(obj)
	{
	   var i = 0;
	   while(obj.tagName.toLowerCase() != "tr"){
	    obj = obj.parentNode;
	    if(obj.tagName.toLowerCase() == "table")
	  return null;
	   }
	   return obj;
	}

	function genSelBoxExpPay(id,type,selectedKey,setAll,_class_,_script_,nullFlag,expStr){
		var str = "";
		var arr;
		if(expStr.indexOf(",")>0)
			arr = expStr.split(",");
		else {
			expStr = expStr+",";
			arr = expStr.split(",");
		}
		if(id=="PART_USE_TYPE"){
			str += "<select id='" + id + "' name='PART_USE_TYPE' class='"+ _class_ +"' style='width:80px' " + _script_ ;
			if(nullFlag && nullFlag == "true"){
				str += " datatype='0,0,0' ";
			}
			str += " > ";
		
		}else{
			str += "<select id='" + id + "' name='HAS_PART' class='"+ _class_ +"' style='width:100px' " + _script_ ;
			if(nullFlag && nullFlag == "true"){
				str += " datatype='0,0,0' ";
			}
			str += " onChange=setPartCodes();> ";
		}
		
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
	
	//增加工时
	function addLabour(strs){
		str=strs;
		var chk = document.getElementsByName("LABOUR_CODE");
		var l = chk.length;
		var cnt="";
		var k=0;
		for(var i=0;i<l;i++)
		{   			
			k=i+1;  
			if(document.getElementById("LABOUR_CODE"+k).value!=""){
				cnt+=document.getElementById("LABOUR_CODE"+k).value+",";
			}
		}
		OpenHtmlWindow("<%=contextPath%>/jsp/afterSales/serviceActivity/addLabour.jsp?id="+str+'&cnt='+cnt,800,600);
	}
	
	function setLabourCode(code,name,id,LaId,Lhour){
		document.getElementById("LABOUR_ID"+id).value=LaId;
		document.getElementById("LABOUR_HOUR"+id).value=Lhour;
		document.getElementById("LABOUR_CODE"+id).value=code;
		document.getElementById("LABOUR_NAME"+id).value=name;
		setCodesAndNames();
	}
	
	//增加配件
	function addPart(strs){
		str=strs;
		var chk = document.getElementsByName("PART_ID");
		var l = chk.length;
		var cnt="";
		for(var i=0;i<l;i++)
		{   			
			k=i+1;  
			if(document.getElementById("PART_ID"+k)){
				if(document.getElementById("PART_ID"+k).value!=""){
					cnt+=document.getElementById("PART_ID"+k).value+",";
				}
			}
		}
		OpenHtmlWindow("<%=contextPath%>/jsp/afterSales/serviceActivity/addPart.jsp?id="+str+'&cnt='+cnt,800,600);
	}
	
	function setMainPartCode(code,name,id,pId){
		document.getElementById("PART_ID"+id).value=pId;
		document.getElementById("PART_CODE"+id).value=code;
		document.getElementById("PART_NAME"+id).value=name;
		setCodesAndNames();
		
	}
	//定义维修项代码和名称全局变量
	var itemcodes = new Array();
	var itemnames = new Array();
	function setCodesAndNames(){

		//刷新全局数组
		itemcodes = new Array();
		itemnames = new Array();
		
		//赋值全局数据
		var WR_LABOURCODEs = document.getElementsByName("LABOUR_CODE");
		var WR_LABOURNAMEs = document.getElementsByName("LABOUR_NAME");
		for(var i=0; i<WR_LABOURCODEs.length; i++){
			itemcodes[i] = WR_LABOURCODEs[i].value;
			itemnames[i] = WR_LABOURNAMEs[i].value;
		}
		
		//刷新维修配件上的维修项目值
		var hoursCode = document.getElementsByName("hoursCode");
		var Labour0s = document.getElementsByName("Labour0");
		if(Labour0s.length > 0){
			//清空所有值
			var name = '';
			for(var j=0; j<Labour0s.length; j++){
				if(j==0)
				{
					name = Labour0s[j].options.value;
				}
				Labour0s[j].options.length = 1;
			}  
			
			for(var l =0; l < itemcodes.length ;l++)
			{
				if(name == itemcodes[l]){
				var type = itemcodes[0];
				var type1 = itemnames[0];
				itemcodes[0] = name;
				itemcodes[l] = type;
				itemnames[0] = itemnames[l];
				itemnames[l] = type1;
				}
			}
			//重新赋值
			var isT;
			for(var k=0; k<Labour0s.length; k++){
				for(var a=0; a<itemcodes.length; a++){
					if(itemcodes[a]==hoursCode[k].value){
						isT = true;
					}else{
						isT=false;
					}
					if(itemcodes[a]!=""){
						var varItem = new Option(itemnames[a],itemcodes[a],false,isT);
						Labour0s[k].options.add(varItem);
					}
				}
			}
		}
	}
	
	//循环配件列表生成主因件选择框
	
		
	function setPartCodes(){
		//刷新全局数组
		partids = new Array();
		partcodes = new Array();
		partnames = new Array();
		var temp=0;
		var k=0;
		//赋值全局数据
		var chk = document.getElementsByName("PART_ID");
		var l = chk.length;
			for(var a =0;a<l;a++){
				k=a+1;
				 var  myselect=document.getElementById("HAS_PART"+k);
				
				 var index=myselect.selectedIndex ; 
				 var malValue =myselect.options[index].value;
				 
				if(malValue==<%=Constant.IF_TYPE_YES%>){//如果是主因件则加入集合
					//document.getElementById("Part"+k).disabled=true;
					if(document.getElementById("PART_ID"+k))
						partids[temp]=document.getElementById("PART_ID"+k).value;
					if(document.getElementById("PART_CODE"+k))
						partcodes[temp]=document.getElementById("PART_CODE"+k).value;
					if(document.getElementById("PART_NAME"+k))
						partnames[temp]=document.getElementById("PART_NAME"+k).value;
					
					temp++;
				}else{
					if(document.getElementById("Part"+k)){
						document.getElementById("Part"+k).disabled=false;
					}
				}
			}
		//刷新维修配件上的维修项目值
		var partMain = document.getElementsByName("partMain");
		var mainPartCode = document.getElementsByName("Part0");
		if(mainPartCode.length > 0){
			//清空所有值
			var name = '';
			 for(var j=0; j<mainPartCode.length; j++){
				if(j==0)
				{
					name = mainPartCode[j].options.value;
				}
				mainPartCode[j].options.length = 1;
			} 
			//重新赋值
			var m=0;
			var isT;
			for(var k=0; k<mainPartCode.length; k++){
				m=k+1;
				 var  myselect=document.getElementById("HAS_PART"+m);
				 var index=myselect.selectedIndex ; 
				 var malValue =myselect.options[index].value;
				 var z = 0;
				 var isT=false;
				for(var a=0; a<partcodes.length; a++){
					if(malValue==<%=Constant.IF_TYPE_NO%>){//如果是次因件，则让其选择对应的主因件
						if(partMain[k].value!=""){
						if(partids[a]==partMain[k].value){
							isT = true;
							}
						}
					if(partcodes[a]!=""){
						var varItem = new Option(partcodes[a],partids[a],false,isT);
					 	mainPartCode[k].options.add(varItem);
						}

					} 
				}
			}
		}
	}
	//删除行 配件
	function delPartItem(obj){
	   var tr = this.getRowObj(obj);
	   if(tr != null){
	    tr.parentNode.removeChild(tr);
	    changeIds();
	    setPartCodes();
	   }else{
	    throw new Error("the given object is not contained by the table");
	   }
	}
	//选择车型组
	function showLabor(){
		var url = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/laborListInit2.do' ;
		OpenHtmlWindow(url,800,600);
	}
	
	function setLaborList(codes,Wcodes){
		 var scode="";
		 var wcode="";
			 for(var i=0;i<codes.length;i++){
				 scode+=codes[i]+",";
				 wcode += Wcodes[i]+",";
			 }
		 	$('#WRGROUP_ID')[0].value = scode.substring(0,scode.length-1);
		 	$('#WRGROUP_CODE')[0].value = wcode.substring(0,wcode.length-1);
		}

	function cleanInput(){
		$('#WRGROUP_ID')[0].value="";
		$('#WRGROUP_CODE')[0].value='';
	}
	
	

	function setDealerValue(inputId,inputName,id,name){
		document.getElementById(inputId).value=id;
	    document.getElementById(inputName).value=name;
	}

	function setMoreDealerValue(inputId,inputName,userIdsNames,type){
	    //定义一数组
	    var strsTemp = userIdsNames.split("@@"); //字符分割
	    var id = strsTemp[1];
	    var name = strsTemp[0];
	    var code = strsTemp[2];
		if(type == 1){
			var values = document.getElementById(inputId).value;
			values += id + ",";
			document.getElementById(inputId).value = values;//重新赋值
			$("#tr").after('<tr id="tr'+id+'" style="BACKGROUND-COLOR: #fdfdfd" class="right">'
					        +'<td class = "dealerNum" style="text-align: center" colspan="1" nowrap="nowrap" class = "dealerNum"></td>'
					        +'<td class="center" colspan="2" nowrap="nowrap">'+ code + '</td>'
					        +'<td class="center" colspan="2" nowrap="nowrap">'+ name + '</td>'
					        +'<td class="center" colspan="1" nowrap="nowrap"><input onclick="deleteRow('+id+',\''+inputId+'\');" value="删除" type="button" class="normal_btn"/></td></tr>');
			//设置行号
			setDealerNum();
		}else{
			deleteRow(id,inputId);
		}
	}
		//设置行号
		function setDealerNum(){
			var dealerNum = document.getElementsByClassName("dealerNum");//模板行号
			for (var i = 0 ;i < dealerNum.length ; i++) {
				dealerNum[i].innerHTML = i+1;
			}
		}

		//经销商删除行并且重新赋值
		function deleteRow(id,inputId){
			var values = document.getElementById(inputId).value;
			values = values.replace(id+",","");
			document.getElementById(inputId).value = values;//重新赋值
			//删除行
			$("#tr"+id).remove();
			//设置行号
			setDealerNum();
		}
		//新增经销商
		function addDealer(inputId,inputCode,isMulti){
			var  idVal=document.getElementById(inputId).value;
			var url="<%=contextPath%>/jsp/afterSales/serviceActivity/showDealer.jsp?idVal="+idVal+"&INPUTCODE="+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti;
			OpenHtmlWindow(url,800,600);
		}
		
		var menuUrl = "<%=contextPath%>/afterSales/serviceActivity/ServiceActivityAction/serviceActivityPageInit.do";
		var saveUrl = "<%=contextPath%>/afterSales/serviceActivity/ServiceActivityAction/saveData.json";
		// 保存信息反馈类型
		function saveData(status) {
			if($("#activity_strate_date").val()!=null && $("#activity_strate_date").val()!='' && $("#activity_end_date").val()!=''  && $("#activity_end_date").val()!=null ){
				var beginDate=$("#activity_strate_date").val();  
				var endDate=$("#activity_end_date").val();  
				var d1 = new Date(beginDate.replace(/\-/g, "\/"));  
				var d2 = new Date(endDate.replace(/\-/g, "\/"));  
				var dd=new Date();
				if(d1 > d2){
					MyAlert("活动开始日期不能大于活动结束日期！");
					return;
				}
				if(d2<dd){
					MyAlert("活动结束日期不能小于当前日期！");
					return;
				}
			}
			if($("#activity_strate_mileage").val()!=null && $("#activity_strate_mileage").val()!="" && $("#activity_end_mileage").val()!=null && $("#activity_end_mileage").val()!=""){
				
				if(parseFloat($("#activity_strate_mileage").val()) > parseFloat($("#activity_end_mileage").val())){
					MyAlert("开始里程数不能大于结束里程数！");
					return;
				}
			}
			if($("#activity_sales_strate_date").val()!=null && $("#activity_sales_strate_date").val()!='' && ($("#activity_sales_end_date").val()==''  || $("#activity_sales_end_date").val()==null) ){
				MyAlert("结束实销日期不能为空！");
				return;
			}
			if(($("#activity_sales_strate_date").val()==null || $("#activity_sales_strate_date").val()=='' )&& $("#activity_sales_end_date").val()!=''  && $("#activity_sales_end_date").val()!=null ){
				MyAlert("开始实销日期不能为空！");
				return;
			}
			
			if($("#activity_sales_strate_date").val()!=null && $("#activity_sales_strate_date").val()!='' && $("#activity_sales_end_date").val()!=''  && $("#activity_sales_end_date").val()!=null ){
				var beginDate=$("#activity_sales_strate_date").val();  
				var endDate=$("#activity_sales_end_date").val();  
				var d1 = new Date(beginDate.replace(/\-/g, "\/"));  
				var d2 = new Date(endDate.replace(/\-/g, "\/"));  
				if(d1 > d2){
					MyAlert("开始实销日期不能大于结束实销日期！");
					return;
				}
			}
			if($("#activity_name").val()=='' || $("#activity_name").val()==null){
				MyAlert("活动名称不能为空！");
				return;
			}
			if($("#activity_code").val()=='' || $("#activity_code").val()==null){
				MyAlert("活动编号不能为空！");
				return;
			}
			if($("#activity_strate_date").val()=='' || $("#activity_strate_date").val()==null){
				MyAlert("活动开始日期不能为空！");
				return;
			}
			if($("#activity_end_date").val()=='' || $("#activity_end_date").val()==null){
				MyAlert("活动结束日期不能为空！");
				return;
			}
			var reg=/^(?:\d?\d|100)$/;
			if($("#activity_discount").val()=='' || $("#activity_discount").val()==null){
				MyAlert("折扣率不能为空！");
				return;
			}else if(!reg.test($("#activity_discount").val())||$("#activity_discount").val()==0){
				MyAlert("请输入正确的折扣率!");
				document.getElementById("activity_discount").value="";
				return;
			}
			if($("#trouble_desc").val()=='' || $("#trouble_desc").val()==null){
				MyAlert("故障描述不能为空！");
				return;
			}
			if($("#trouble_reason").val()=='' || $("#trouble_reason").val()==null){
				MyAlert("故障原因不能为空！");
				return;
			}
			if($("#maintenance_measures").val()=='' || $("#maintenance_measures").val()==null){
				MyAlert("维修措施不能为空！");
				return;
			}
			var length_labour = document.getElementById("itemTableId").rows.length;
			if(length_labour<2){
				MyAlert("维修工时不能为空！");
				return;
			}
			var length_part = document.getElementById("partTableId").rows.length;
			if(length_part<2){
				MyAlert("维修配件不能为空！");
				return;
			}
			var reg1=/^(?:\d?\d|10000)$/;
			var puts = document.getElementsByName('QUANTITY');
			var lab = document.getElementsByName('Labour0');
			var has = document.getElementsByName('HAS_PART');
			var part = document.getElementsByName('Part0');
			var labourCode = document.getElementsByName('LABOUR_CODE');
			var partId = document.getElementsByName('PART_ID');
			for(var i=0;i<labourCode.length;i++){
				if(labourCode[i].value==""||labourCode[i].value==null){
					MyAlert("维修工时不能为空！");
					return;
				}
				var isct=0;
				for(var x=0; x<partId.length; x++){
					if(partId[x].value==""||partId[x].value==null){
						MyAlert("维修配件不能为空！");
						return;
					}
					if(puts[x].value == '0' || puts[x].value==''){
						MyAlert('配件数量必须大于0!');
						puts[x].value="";
						return;   
					}else if(!reg1.test(puts[x].value)){
						MyAlert('请正确填写配件数量!');
						puts[x].value="";
						return;  
					}else if(lab[x].value == '-1' || lab[x].value==''){
						MyAlert('请选择对应的工时!');
						return;  
					}else if(has[x].value == '-1' || has[x].value==''){
						MyAlert('请选择是否主因件!');
						return;  
					}
					else if(has[x].value =='10041002'&&(part[x].value == '-1' || part[x].value=='')){
						MyAlert('请选择对应的主因件!');
						return;  
					}else if(lab[x].value ==labourCode[i].value ){
						isct++;
						}
					}
				if(isct==0){
					MyAlert("工时代码："+labourCode[i].value+'未被使用，请选择对应配件使用!');
					return; 
				}
			}
			
			if(status==1){
				$("#activity_status").val("96291001");//尚未发布
				 MyConfirm("确定保存吗？",EditAsDo);
			}else{
				$("#activity_status").val("96291002");//已经发布
				 MyConfirm("确定发布吗？",EditAsDo);
			}
			
		}
		function EditAsDo(){
			var tUrl = saveUrl;
	 		document.getElementById("saveBtn1").disabled=true;
	 		document.getElementById("saveBtn2").disabled=true;
			sendAjax(tUrl, showResult, 'fm');
		}
		function showResult(json){
			if(json.message == "1"){
				MyAlert("保存成功！");
				backInit();
			}else if(json.message == "2"){
				MyAlert("发布成功！");
				backInit();
			}else {
				document.getElementById("saveBtn1").disabled=false;
		 		document.getElementById("saveBtn2").disabled=false;
				MyAlert(json.message);
			}
		}

		function backInit(){
			window.location.href = menuUrl;
		}
		//导入VIN
		function checkVin(status){
			var  activityId=document.getElementById("activityId").value;
			var url="<%=contextPath%>/jsp/afterSales/serviceActivity/checkVin.jsp?activityId="+activityId+"&status="+status;
			OpenHtmlWindow(url,730,390);
		}
</script>

</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：售后服务&gt;维修管理&gt;服务活动管理保存（<span style="color:red;">如果导入了VIN和里程、车型、实销日期同时做了录入，以导入VIN为准</span>）</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
		<input type="hidden" name="activityId" id="activityId" value="${info.ACTIVITY_ID }"/>
		<input type="hidden" name="updateId" value="${update}"/>
		<input type="hidden" name="activity_type" id="activity_type" value="<%=Constant.SERVICEACTIVITY_TYPE_NEW_01%>"/>
		<input type="hidden" name="activity_status" id="activity_status"/>
		<div class="form-panel">
		<h2><img src="<%=contextPath%>/img/nav.gif"/>服务活动信息</h2>
			<div class="form-body">
<table  class="table_query" >
        <tr>
		<td class="right">活动名称：</td>
		<td >
			<input class="middle_txt" id="activity_name"  name="activity_name" type="text" value="${info.ACTIVITY_NAME }"  />
		  <span style="color:red;">*</span>
		</td>
		
		<td class="right">活动编号：</td>
		<td >
			<input class="middle_txt" id="activity_code"  name="activity_code" type="text"  value="${info.ACTIVITY_CODE }" />
		  <span style="color:red;">*</span>
		  </td>
		<td class="right">活动日期：</td>
		<td >
			<input id="activity_strate_date" name="activity_strate_date" readonly class="Wdate" type="text" onclick="WdatePicker({onpicked:function(dq){changeArrivalDate(dq.cal.getNewDateStr());},dateFmt:'yyyy-MM-dd HH:mm'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;" value="${info.ACTIVITY_STRATE_DATE }"/> 至
			<input id="activity_end_date" name="activity_end_date" readonly class="Wdate" type="text" onclick="WdatePicker({onpicked:function(dq){changeArrivalDate(dq.cal.getNewDateStr());},dateFmt:'yyyy-MM-dd HH:mm'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;" value="${info.ACTIVITY_END_DATE }"/>
		  <span style="color:red;">*</span>
		</td>
	</tr>
	<tr >
		<td class="right">里程设置：</td>
		<td >
			<input class="middle_txt" style="width: 64px" onkeyup="this.value=this.value.replace(/\D/g,'')" onblur="this.value=this.value.replace(/\D/g,'')" maxlength="8" id="activity_strate_mileage"  name="activity_strate_mileage" type="text" value="${info.ACTIVITY_STRATE_MILEAGE }"  />至
		  	<input class="middle_txt" style="width: 64px" onkeyup="this.value=this.value.replace(/\D/g,'')" onblur="this.value=this.value.replace(/\D/g,'')" maxlength="8" id="activity_end_mileage"  name="activity_end_mileage" type="text" value="${info.ACTIVITY_END_MILEAGE }"  />
		</td>
		 <td >车型组选择：</td>
            <td >
            <input type="text" name="WRGROUP_CODE"  id ="WRGROUP_CODE" class="middle_txt" readonly="readonly" onmouseover="this.title=this.value" value="${modelCode}" onclick="showLabor();"/>
            <input type="hidden"  name="MODEL_ID11"  id ="WRGROUP_ID" class="middle_txt" value="${modelId }"/>
         	<!-- <input type="button" value="..." class="u-button" onclick="showLabor();"/> -->
         	<input type="button" value="清空" class="u-button" onclick="cleanInput();"/>
            </td>
		<td class="right">实销日期：</td>
		<td  nowrap="nowrap">
			<input id="activity_sales_strate_date" name="activity_sales_strate_date" readonly class="Wdate" type="text" onclick="WdatePicker({onpicked:function(dq){changeArrivalDate(dq.cal.getNewDateStr());},dateFmt:'yyyy-MM-dd HH:mm'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;" value="${info.ACTIVITY_SALES_STRATE_DATE }"/>
			至
		  	<input id="activity_sales_end_date" name="activity_sales_end_date" readonly class="Wdate" type="text" onclick="WdatePicker({onpicked:function(dq){changeArrivalDate(dq.cal.getNewDateStr());},dateFmt:'yyyy-MM-dd HH:mm'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;" value="${info.ACTIVITY_SALES_END_DATE }"/>
			<span style="color:red;">*</span>
		</td>
	</tr>
	<tr >
		<td class="right">折扣率：</td>
		<td  colspan="5">
			<input class="middle_txt" style="width: 40px"  id="activity_discount"  name="activity_discount" type="text"  value="${info.ACTIVITY_DISCOUNT }" maxlength="3" />%
		<span style="color:red;">*</span>
		</td>
	</tr>
	</table>
	</div>
	</div>
	<div class="form-panel">
		<h2><img src="<%=contextPath%>/img/nav.gif"/>申请内容</h2>
			<div class="form-body">
	<table class="table_query"  id="REMARKS_ID" > 
          	<tr>
					<td class="right" >故障描述：
					</td>
					<td  colspan="2">
						<textarea name='trouble_desc' maxlength="500"  	id='trouble_desc' rows='3' cols='32'>${info.FAULT_DESCRIPTION }</textarea>
					<span style="color:red;">*</span>
					</td>
					<td class="right" >故障原因：
					</td>
					<td colspan="2" >
						<textarea name='trouble_reason' id='trouble_reason' maxlength="500"   rows='3' cols='32'>${info.FAULT_PROBLEM }</textarea>
					<span style="color:red;">*</span>
					</td>
				</tr>
				<tr>
				<td class="right">维修措施：
					</td>
					<td colspan="2" >
						<textarea name='maintenance_measures' id='maintenance_measures' maxlength="500"   rows='3' cols='32'>${info.MAINTENANCE_MEASURES }</textarea>
				<span style="color:red;">*</span>
				</td>
				<td></td>
				<td colspan="2" ></td>
				</tr>
          </table>
          </div>
          </div>
          <div class="form-panel">
		<h2><img src="<%=contextPath%>/img/nav.gif"/>附件信息&nbsp;&nbsp;&nbsp;<a class="u-anchor" href="#" onclick="showUpload('<%=contextPath%>','PNG;PDF;JPG;JPEG;BMP;RAR;ZIP;TXT;XLS;XLSX;DOC;DOCX',10)" />添加附件</a> </h2>
			<div class="form-body">
		<table class="table_query"  id="file">
			<tr>
				<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
			</tr>
			<c:choose>
				<c:when test="${empty select}">
					<c:forEach items="${fsList}" var="flist">
			    		<script type="text/javascript">
			    			addUploadRowByDb('${flist.filename}','${flist.fjid}','${flist.fileurl}','${flist.fjid}','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
						</script>
	    			</c:forEach>
				</c:when>
				<c:otherwise>
					<c:forEach items="${fsList}" var="flist">
			    		<script type="text/javascript">
			    			addUploadRowByDb('${flist.filename}','${flist.fjid}','${flist.fileurl}','${flist.fjid}','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
						</script>
			    	</c:forEach>
				</c:otherwise>
			</c:choose>
	 	</table>
	 	</div>
	 	</div>
	 	<div class="form-panel">
		<h2>
		<img src="<%=contextPath%>/img/nav.gif"/>下发经销商&nbsp;&nbsp;&nbsp;
		<input type="hidden" name="dealer_id" id="dealer_id" value="${dealerId }"/>
		<a class="u-anchor" href="#" name="button_add"  id="button_add" onclick="addDealer('dealer_id','dealer_name','true');"/>新增</a>
		</h2>
			<div class="form-body">
	<table class="table_list" id="REMARKS_ID" > 
     <tr id="tr" >
        <td class="center" colspan="1" nowrap="nowrap">行号</td>
        <td class="center" colspan="2" nowrap="nowrap">经销商代码</td>
        <td class="center" colspan="2"  nowrap="nowrap">经销商名称</td>
        <td class="center" colspan="1"  nowrap="nowrap">操作</td>
      </tr>
   
      <c:forEach items="${activityDealer }" var="list" varStatus="st">
	      <tr id="tr${list.DEALER_ID }"  class="right">
      		<td class="center" colspan="1" nowrap="nowrap" class = "dealerNum">${st.index + 1}</td>
        	<td class="center" colspan="2" nowrap="nowrap">${list.DEALER_CODE }</td>
        	<td class="center" colspan="2" nowrap="nowrap">${list.DEALER_NAME }</td>
        	<td class="center" colspan="1" nowrap="nowrap">
        		<input onclick="deleteRow(${list.DEALER_ID },'dealer_id');" value="删除" type="button" class="normal_btn"/>
        	</td>
	      </tr>
      </c:forEach>
      </table>
      </div>
      </div>
      <div class="form-panel">
		<h2>
		<img src="<%=contextPath%>/img/nav.gif"/>导入及查看VIN&nbsp;&nbsp;<a class="u-anchor" href="#"  onclick="checkVin(1);">导入</a>
		</h2>
			<div class="form-body">
      <table class="table_query"  >
      <tr>
      	<td>查看已导入车辆信息，请点击"导入"按钮查询。</td>
      </tr>
     </table>
     </div>
     </div>
     <!--子表信息-->
     <div class="form-panel">
		<h2>
		<img src="<%=contextPath%>/img/nav.gif"/>维修工时
		</h2>
			<div class="form-body">
		<table id="itemTableId" class="table_list">
				<tr align="center" class="right">
					<td> 工时代码 </td>
				    <td> 工时名称</td>
				    <td> 工时数</td>
					<td>
						<input id="itemBtn" type="button" class="normal_btn" value="新增"
							name="button422" onClick="javascript:addRow('itemTableId');" />
					</td>
				</tr>
				<c:forEach items="${activityLabour }" var="list" varStatus="st">
			      	<tr  class="right">
		        	<td align="center" nowrap="nowrap">
		        	<input type="text" class="middle_txt"  id="LABOUR_CODE${st.index + 1}" name="LABOUR_CODE" readonly  size="10" value="${list.HOURS_CODE }" onClick="addLabour(${st.index + 1})"/><input type="hidden"  id="LABOUR_ID${st.index + 1}" name="LABOUR_ID" /><span style="color:red">*</span>
		        	</td>
		        	<td align="center"  nowrap="nowrap">
		        	<input type="text" id="LABOUR_NAME${st.index + 1}" name="LABOUR_NAME" class="middle_txt" value="${list.HOURS_NAME }" size="10"  readonly /></span>
		        	</td>
		        	<td align="center"  nowrap="nowrap">
		        	<input type="text" id="LABOUR_HOUR${st.index + 1}" name="LABOUR_HOUR" class="middle_txt" value="${list.APPLY_HOURS_COUNT }" size="10"  readonly />
		        	</td>
					<td align="center"  nowrap="nowrap"><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delItem(this);"/></td>        	</td>
	     		 </tr>
     		 </c:forEach>
			</table>
			</div>
			</div>
			<div class="form-panel">
		<h2>
		<img src="<%=contextPath%>/img/nav.gif"/>维修配件
		</h2>
			<div class="form-body">
			<table id="partTableId"  class="table_list">
				<tr align="center" class="right">
				    <td> 配件代码 </td>
				    <td> 配件名称</td>
				    <td> 数量 </td>
				    <td> 关联工时 </td>
				    <td> 是否主因件 </td>
				    <td> 关联主因件 </td>
				    <td> 配件使用类型 </td>
					<td>
						<input id="partBtn" type="button" class="normal_btn" value="新增"
							name="button422" onClick="javascript:addRow('partTableId');" />
					</td>
				</tr>
				<c:forEach items="${activityPart }" var="list" varStatus="st">
			      <tr  class="right">
		        	<td align="center"  nowrap="nowrap">
		        	<input type="hidden"  id="PART_ID${st.index + 1}" name="PART_ID" value="${list.PART_ID }"/><input type="text" class="middle_txt"  id="PART_CODE${st.index + 1}" name="PART_CODE" readonly  value="${list.PART_CODE }" size="10" onClick="javascript:addLabour(${st.index + 1});"/><span style="color:red">*</span>
		        	</td>
		        	<td align="center"  nowrap="nowrap">
		        	<input type="text" class="middle_txt" name="PART_NAME"   id="PART_NAME${st.index + 1}"  maxlength="20" value="${list.PART_NAME }"/>
		        	</td>
		        	<td align="center"  nowrap="nowrap">
		        	<input type="text" class="middle_txt" size="10" name="QUANTITY" id="QUANTITY${st.index + 1}" value="${list.APPLY_PART_COUNT }" />
		        	</td>
		        	<td align="center"  nowrap="nowrap">
		        	<select value="" name="Labour0" id="Labour0"  class="u-select" style="width:80px"><option value="-1">-请选择-</option></select>
		        	<input type="hidden"  id="hoursCode" name="hoursCode" value="${list.ACTIVITY_HOURS_CODE }"/>
		        	</td>
		        	<td align="center"  nowrap="nowrap">
		        	<select  onchange="setPartCodes();" class="u-select" 
						name="HAS_PART" id="HAS_PART${st.index + 1}">
						<c:if test="${list.IS_MAIN==10041001}">
							<option value="<%=Constant.IF_TYPE_YES%>" selected>是</option>
							<option value="<%=Constant.IF_TYPE_NO%>">否</option>
						</c:if>
						<c:if test="${list.IS_MAIN==10041002}">
							<option value="<%=Constant.IF_TYPE_YES%>" >是</option>
							<option value="<%=Constant.IF_TYPE_NO%>" selected>否</option>
						</c:if>
					</select>
		        	</td>
		        	<td align="center"  nowrap="nowrap" >
		        	<select id="Part${st.index + 1}" name="Part0" class="u-select"><option value="-1">-请选择-</option></select>
		        	<input type="hidden"  id="partMain" name="partMain" value="${list.PART_MAIN_ID }"/>
		        	</td>
		        	<td align="center"  nowrap="nowrap" >
		        	<script type="text/javascript">
		           		 genSelBoxExp("PART_USE_TYPE",<%=Constant.PART_USE_TYPE%>,"${list.PART_USE_TYPE }",true,"","","false",'');
		           	</script>
		        	</td>
		        	<td align="center"  nowrap="nowrap"><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this);"/></td>
	      </tr>
      </c:forEach>
			</table>
			</div>
			</div>
	<div style="text-align: center;">
		<input class="u-button u-submit" type="button" name="button1"  id="saveBtn1" value="保存" onclick="saveData(1);"/>
		<input class="u-button u-cancel" type="button" name="button1"  id="saveBtn2" value="发布" onclick="saveData();"/>
		<input class="u-button u-query" type="button" name="button1"  id="backBtn" value="返回" onclick="backInit();"/>
	</div>
</form>
</div>
</body>
</html>
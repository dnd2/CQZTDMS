<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%String contextPath = request.getContextPath();%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<title>工时单价维护</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	</head>
	<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />
			&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;工时单价政策新增
		</div>
		<form name='fm' id='fm'>
		<div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
			<table class="table_query">
				<tr>
					<td style="text-align:right">变更类型：</td>
					<td>
						<script type="text/javascript">
        					genSelBoxExp("changeType",<%=Constant.LABOR_CHANGE_TYPE%>,<%=Constant.LABOR_CHANGE_TYPE_01%>,"","","","false","");
        				</script>
					</td>
					
					<td style="text-align:right">政策名称：</td>
					<td><input name="policyName" id="policyName" value="" type="text" class="middle_txt"/></td><!-- datatype="0,is_null,15" -->
					<td style="text-align:right">加价方式：</td>
					<td>
						<script type="text/javascript">
        					genSelBoxExp("moneyType",<%=Constant.MONEY_TYPE%>,<%=Constant.MONEY_TYPE_01%>,"","","onchange=isCheck();","false","");
        				</script>
        				<select name="money" id="money" class="u-select" style="width:50px;">
							<%
							double j=-10;
							double k = 0;
							for(int i=0;i<=40;i++){
							%>
							
							<%
								if(j+k!=0.0){
									
								%>	
								<option value=<%=j+k%>><%=j+k%></option>
							<%	}
							%>
							
							
							
							<% k=k+0.5;}
							%>
						</select>
					</td>
				</tr>
				<%-- <tr id="a">
					<td></td>
					<td>
						<select name="money" id="money">
							<%
							double j=-10;
							double k = 0;
							for(int i=0;i<=40;i++){
							%>
							
							<%
								if(j+k!=0.0){
									
								%>	
								<option value=<%=j+k%>><%=j+k%></option>
							<%	}
							%>
							
							
							
							<% k=k+0.5;}
							%>
						</select>
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr id="b">
					<td></td>
					<td>
						<select name="money1" id="money1">
							
							<%
							int a=5;
							int b = 0;
							String ss = "";
							for(int i=0;i<10;i++){
								ss = a+b+"%";
							%>
								<option value=<%=a+b%>><%=ss%></option>
							<% b=b+5;}
							%>
						</select>
					</td>
					<td></td>
					<td></td>
				</tr> --%>
				<tr>
					<td style="text-align:right">
						政策生效日期：
					</td>
					<td>
					<input id="policytrueTime" name="policytrueTime" datatype="1,is_date,10" class="Wdate" type="text" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'policyfalseTime\')}'})" readonly/>
					
					<%-- <input name="policytrueTime" type="text" class="middle_txt" id="t1" readonly="readonly" value="${startDate }"/> 
					<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 't1', false);" /> 
					<span style="color: red">*</span> --%>
					</td>
					<td style="text-align:right">
						政策失效日期：
					</td>
					<td>
					<input id="policyfalseTime" name="policyfalseTime" datatype="1,is_date,10" class="Wdate" type="text" onclick="WdatePicker({minDate:'#F{$dp.$D(\'policytrueTime\')}'})" readonly/>
					<%-- <input name="policyfalseTime" type="text" class="middle_txt" id="t2" readonly="readonly" value="${endDate }"/> 
					<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 't2', false);" /> 
					<span style="color: red">*</span> --%>
					</td>
					<td style="text-align:right"></td>
					<td>
						
					</td>
				</tr>
				<tr>
					<td style="text-align:right">备注：</td>
					<td colspan="5">
						<textarea name="remark" id="remark" rows="5" cols="80"></textarea>
					</td>
				</tr>
				<tr>
				<td colspan="6">
        			<strong>首页新闻</strong><input class="normal_btn"  name="add4" type="button" onclick="selectMainNew();" value ='新增'/></td>
    			</tr>
    			</div>
    			<tr>
	      			<td colspan="6">
	      				<table class="table_list" id="t_news">
		        			<tr >
					          <th width="50"  style="text-align:center" nowrap="nowrap" >序号 </th>
					          <th width="220" style="text-align:center" nowrap="nowrap" >编码 </th>
					          <th width="400" style="text-align:center"nowrap="nowrap" >新闻名称</th>
					          <th width="80" style="text-align:center" nowrap="nowrap" >操作 </th>
		        			</tr>
						</table>
					</td>
				</tr>
				
  		  		
  		  		<table class="table_list" id="mileage_body" style=""><!-- 经销商身份变更 -->
		        <tr>
		        	<td align="left" nowrap="nowrap">经销商选择：</td>
		       		<td colspan="3" align="left" nowrap="nowrap">
		       			<input id="dealerCode" type="hidden" name="dealerCode" value=${dealerCode }></input>
		       			<input id="dealerId" type="hidden" name="dealerId"   value=${dealerId }></input>
            			<input name="button1" type="button" class="normal_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','dealerId','true','','true','','10771002');" value="选择经销商" />
            			<input name="button1" type="button" class="normal_btn" onclick="viewDealerCode();" style="cursor: pointer;"value="显示经销商" />                
		       		</td>
		       	</tr>
		        <tr id="showDealer">
		          <td></td>
		          <td></td>
		          <td></td>
		          <td></td>
		        </tr>
		        </table><!-- 单一经销商变更 -->
  		  		
  		  		<table id="freetimes_body" style="display:none">
		        <tr>
		        	<td align="right" nowrap="nowrap">省份选择：
		        		<input type="button" class="normal_btn" value="新增" onclick="addUserRegion()"/>&nbsp;
		        	</td>

		       	</tr>
		        <tr id="showRegion">
					<td></td>
		          <td></td>
		        </tr>
		        </table>
		        
		        <table id="purchase_date_chg" style="display:none">
		        <tr>
		        	<td align="right" nowrap="nowrap">车型组：</td>
		       		<td>
						<input type="button" class="normal_btn" value="新增" onclick="addModelGroup()"/>&nbsp;
					</td>

		       	</tr>
		        <tr id="showModelGroup">
				  <td></td>
		          <td></td>
		        </tr>
		        </table>
		  		  		
			</table>
			<table class="table_query">
				<tr>
					<td colspan="6" style="text-align:center">
						<input class="normal_btn" type="button" name="ok" value="保存" id="commitBtn" onclick="save();"/>
						<input class="normal_btn" name="back" id="back" type="button" onclick="javascript:history.go(-1)" value="返回"/>	
					</td>
				</tr>
			</table>
			</div>
		</form>
		</div>
<script>
function doCusChange(){
	document.getElementById("a").style.display = "none";
	document.getElementById("b").style.display = "none";
	
	var selectType = document.getElementById("changeType").value;
	if (selectType == '') {
		document.getElementById("mileage_body").style.display = "none";
		document.getElementById("freetimes_body").style.display = "none";
		document.getElementById("purchase_date_chg").style.display = "none";
		//clearApplyData();
	}
	if (selectType == 80621001) {
		document.getElementById("mileage_body").style.display = "inline";
		document.getElementById("freetimes_body").style.display = "none";
		document.getElementById("purchase_date_chg").style.display = "none";
		//clearApplyData();
	}
	else if (selectType == 80621002) {
		document.getElementById("mileage_body").style.display = "none";
		document.getElementById("freetimes_body").style.display = "inline";
		document.getElementById("purchase_date_chg").style.display = "none";
		//clearApplyData();
	}
	else if(selectType == 80621003) {
		document.getElementById("mileage_body").style.display = "none";
		document.getElementById("freetimes_body").style.display = "none";
		document.getElementById("purchase_date_chg").style.display = "inline";
		//clearApplyData();
	}
}

function isCheck(){
	var moneyType = document.getElementById("moneyType").value;
	if (moneyType == '') {
		document.getElementById("a").style.display = "none";
		document.getElementById("b").style.display = "none";
	}
	if (moneyType == '80631001') {
		document.getElementById("a").style.display = "";
		document.getElementById("b").style.display = "none";
	}
	if (moneyType == '80631002') {
		document.getElementById("a").style.display = "none";
		document.getElementById("b").style.display = "";
	}
}

//onchange下拉框的时候 清空原来的选择
function clearApplyData() {
	document.getElementById("ferrorRoCode").value="";//变更保养次数的单据
	document.getElementById("roDealerCode").innerText = '';//变更里程的经销商代码
}
//清空
function clr() {
	  document.getElementById('dealerCode').value = "";
}

function viewDealerCode(){
	var dealerCodes = document.getElementById("dealerCode").value;
	var dealerIds = document.getElementById("dealerId").value;
	var codes = new Array();
	var ids = new Array();
	if(dealerCodes==null || dealerIds==null||dealerCodes=="" || dealerIds==""){
	MyAlert("请先选择经销商!");
	return false;
	}
	codes = dealerCodes.split(',');
	ids = dealerIds.split(',');
	ids_code = document.getElementById("dealerCode");
	for(var i=0;i<codes.length;i++){
		var j=0;
		if(ids_code!=null){
			var dealers = document.getElementsByName('dealer_code');
			var  flag=0;
			for(var k=0;k<dealers.length;k++){
				name=dealers[k].value;	
			    if(name==codes[i]){
				    flag=1;
				}
			}
			 if(flag==0){
			var tb = document.getElementById("mileage_body") ;
			 var tr = document.getElementById("showDealer");
			 var newTr =  tb.insertRow(1) ;
			 //tr.insert({'after':newTr});
			// tb.insertRow(1) ;
					var newTd1 = newTr.insertCell();
					/* newTd1.innerHTML="<input class='normal_btn' name='delete' type='button' value ='删除' onclick='javascript:deleteRowConfirm1(this,\"mileage_body\");'/><input type='hidden' name='dealer_id' id='dealer_id' value='"+ids[i]+"'>"; */
					newTd1.innerHTML="<a class='u-anchor' onclick='javascript:deleteRowConfirm1(this,\"mileage_body\");'>删除</a><input type='hidden' name='dealer_id' id='dealer_id' value='"+ids[i]+"'>"; 
					var newTd2 = newTr.insertCell();
					newTd2.innerHTML="经销商代码:"+codes[i]+"<input type='hidden' name='dealer_code' id='dealer_code' value='"+codes[i]+"'>" ;
				}
		}else{
			var tb = document.getElementById("mileage_body") ;
		var tr = document.getElementById("showDealer");
		var newTr =  tb.insertRow(1) ;
		//tr.insert({'after':newTr});
		 //tb.insertRow(1) ;
		var newTd1 = newTr.insertCell();
		/* newTd1.innerHTML="<input class='normal_btn' name='delete' type='button' value ='删除' onclick='javascript:deleteRowConfirm1(this,\"mileage_body\");'/><input type='hidden' name='dealer_id' id='dealer_id' value='"+ids[i]+"'>"; */
		newTd1.innerHTML="<a class='u-anchor' onclick='javascript:deleteRowConfirm1(this,\"mileage_body\");'>删除</a><input type='hidden' name='dealer_id' id='dealer_id' value='"+ids[i]+"'>"; 
		var newTd2 = newTr.insertCell();
		newTd2.innerHTML="经销商代码:"+codes[i]+"<input type='hidden' name='dealer_code' id='dealer_code' value='"+codes[i]+"'>" ;
		}
	}	
}

function viewRegionCode(codes,names){
	var dealerCodes = names;
	var dealerIds = codes;
	var codes = new Array();
	var ids = new Array();
	
	codes = dealerCodes;
	ids = dealerIds;
	ids_code = $('region_name');
	for(var i=0;i<codes.length;i++){
		var tr = $('showRegion');
		var newTr = tr.cloneNode(true);
		tr.insert({'after':newTr});
		var j=0;
		if(ids_code!=null){
			var dealers = document.getElementsByName('region_name');
			var  flag=0;
			for(var k=0;k<dealers.length;k++){
				name=dealers[k].value;	
			    if(name==codes[i]){
				    flag=1;
				}
			}
			if(flag==0){
				newTr.cells[j].innerHTML="<td><input class='normal_btn' name='delete' type='button' value ='删除' onclick='javascript:deleteRowConfirm2(this,"+'freetimes_body'+");'/></td><td><input type='hidden' name='region_code' id='region_code' value='"+ids[i]+"'></td>";
				newTr.cells[j+1].innerHTML="<td>省份名称:</td><td>"+codes[i]+"<input type='hidden' name='region_name' id='region_name' value='"+codes[i]+"'></td>"
			}
		}else{
			newTr.cells[0].innerHTML="<td><input class='normal_btn' name='delete' type='button' value ='删除' onclick='javascript:deleteRowConfirm2(this,"+'freetimes_body'+");'/></td><td><input type='hidden' name='region_code' id='region_code' value='"+ids[i]+"'></td>";
			newTr.cells[1].innerHTML="<td>省份名称:</td><td>"+codes[i]+"<input type='hidden' name='region_name' id='region_name' value='"+codes[i]+"'></td>"
		}
	}
}


function viewModelGroup(codes,names){
	var dealerCodes = names;
	var dealerIds = codes;
	var codes = new Array();
	var ids = new Array();
	
	codes = dealerCodes;
	ids = dealerIds;
	ids_code = $('wrgroup_name');
	for(var i=0;i<codes.length;i++){
		var tr = $('showModelGroup');
		var newTr = tr.cloneNode(true);
		tr.insert({'after':newTr});
		var j=0;
		if(ids_code!=null){
			var dealers = document.getElementsByName('wrgroup_name');
			var  flag=0;
			for(var k=0;k<dealers.length;k++){
				name=dealers[k].value;	
			    if(name==codes[i]){
				    flag=1;
				}
			}
			if(flag==0){
				newTr.cells[j].innerHTML="<td><input class='normal_btn' name='delete' type='button' value ='删除' onclick='javascript:deleteRowConfirm3(this,"+'purchase_date_chg'+");'/></td><td><input type='hidden' name='wrgroup_id' id='wrgroup_id' value='"+ids[i]+"'></td>";
				newTr.cells[j+1].innerHTML="<td>车型组名称:</td><td>"+codes[i]+"<input type='hidden' name='wrgroup_name' id='wrgroup_name' value='"+codes[i]+"'></td>"
			}
		}else{
			newTr.cells[0].innerHTML="<td><input class='normal_btn' name='delete' type='button' value ='删除' onclick='javascript:deleteRowConfirm3(this,"+'purchase_date_chg'+");'/></td><td><input type='hidden' name='wrgroup_id' id='wrgroup_id' value='"+ids[i]+"'></td>";
			newTr.cells[1].innerHTML="<td>车型组名称:</td><td>"+codes[i]+"<input type='hidden' name='wrgroup_name' id='wrgroup_name' value='"+codes[i]+"'></td>"
		}
	}
}

function save(){
	$('#commitBtn')[0].disabled=true;
	
	if($('#changeType')[0].value==""){
		MyAlert("变更类型不能为空!");
		$('#commitBtn')[0].disabled=false;
		return;
		
	}
	if($('#moneyType')[0].value==""){
		MyAlert("加价不能为空!");
		$('#commitBtn')[0].disabled=false;
		return;
	}
	if($('#policytrueTime')[0].value==""){
		MyAlert("政策生效时间不能为空!");
		$('#commitBtn')[0].disabled=false;
		return;
	}
	if($('#policyfalseTime')[0].value==""){
		MyAlert("政策失效时间不能为空!");
		$('#commitBtn')[0].disabled=false;
		return;
	}
	if($('#policyName')[0].value==""){
		MyAlert("政策名称不能为空!");
		$('#commitBtn')[0].disabled=false;
		return;
	}

	var url = "<%=contextPath%>/claim/basicData/LaborPriceMain/VerificationName.json";
	sendAjax(url,showDetail11,"fm") ;
}
function showDetail11(json){
	var error = json.error;
	if(error=='error'){
		MyAlert("政策名称已存在!");
		return;
	}else{
		var url = "<%=contextPath%>/claim/basicData/LaborPriceMain/save.json";
		sendAjax(url,showDetail,"fm") ;
	}
}
function showDetail(json){
	var ok = json.ok;
	if(ok=='ok'){
		MyAlert("变更成功!");
		back();
	}else{
		MyAlert("变更失败!");
		$('#commitBtn')[0].disabled=false ;
		return;
	}
}


function doInit(){
   	loadcalendar();  //初始化时间控件
   	doCusChange();
}
function selectMainNew(){
	OpenHtmlWindow('<%=contextPath%>/claim/other/Bonus/newsQuery.do',900,500);
}
function addRow(tableId,newId,newCode,newTitle){
    var addTable = document.getElementById(tableId);
	var rows = addTable.rows;
	var length = rows.length;
	var insertRow = addTable.insertRow(length);

	insertRow.className = "table_list_row1";
	insertRow.insertCell(0);
	insertRow.insertCell(1);
	insertRow.insertCell(2);
	insertRow.insertCell(3);
		addTable.rows[length].cells[0].innerHTML =  '<td>'+length+'</td>';
		addTable.rows[length].cells[1].innerHTML =  '<td><a href="#" onclick="viewNews('+newId+')">'+newCode+'</a><input type=hidden name="newsId" value="'+newId+'"/></td>';
		addTable.rows[length].cells[2].innerHTML =  '<td>'+newTitle+'</td>';
		addTable.rows[length].cells[3].innerHTML =  '<td><input class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRowConfirm(this);" /></td>';
		
		return addTable.rows[length];
	}
function deleteRowConfirm(obj){
	 var tabl=document.all['t_news'];
	 var index = obj.parentElement.parentElement.rowIndex;
	 
	 tabl.deleteRow(index); 
	 countSeq();
}
function countSeq(){
	 var table=document.all['t_news'];

	/*******beg重新定义序号*******/
	var trs = table.getElementsByTagName('tr');//
	var rowCount=trs.length;
	var index=0;
	for(var i = 1; i <rowCount ; i++){
		 var cells = trs[i].cells;
		 cells[0].innerHTML=++index;
	}
}
function viewNews(value){
	OpenHtmlWindow("<%=contextPath%>/claim/basicData/HomePageNews/viewNews.do?comman=2&newsId="+value,900,500);
}

function addUserRegion(){
	OpenHtmlWindow('<%=contextPath%>/sysmng/usemng/UserRegionRelation/queryRegion1.do',800,500);
}
function addModelGroup(){
	OpenHtmlWindow('<%=contextPath%>/sysmng/usemng/UserRegionRelation/queryModelGroup.do',800,500);
}
function back(){
	location.href = "<%=contextPath%>/claim/basicData/LaborPriceMain/LaborPriceInit.do";
}

function deleteRowConfirm1(obj,id){
	 var index = obj.parentElement.parentElement.rowIndex;
	 document.getElementById(id).deleteRow(index);
}

function deleteRowConfirm2(obj,id){
	 var index = obj.parentElement.parentElement.rowIndex;
	 $(id).deleteRow(index);
}
function deleteRowConfirm3(obj,id){
	 var index = obj.parentElement.parentElement.rowIndex;
	 $(id).deleteRow(index);
}
</script>
</body>
</html>

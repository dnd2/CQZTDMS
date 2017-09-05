<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
	String reFlag = (String)request.getAttribute("REFLAG");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔工时查询</title>
<script>
	function doInit(){
   		<%if("1".equals(reFlag)){%> 
   			__extQuery__(1);
   		<%}%>
	}

	function excuteAjaxQuery(){
		__extQueryAddFunction__(1,myCallBack1,'fm');
	}
function myCallBack1(json){
	var len = json.ps.totalRecords;
	if(len==0){
		savebt.style.display="none";
	}
}
</script>
</head>
<body>
<div class="wbox">
<form name='fm' id='fm'>
<input type="hidden" name="wrmodelgrouplist" id="wrmodelgrouplist" value="<%=request.getAttribute("wrmodelgrouplist")%>"/>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;索赔工时维护</div>
   <div class="form-panel">
		<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
   <table  class="table_query">
          <tr>
            <td class="table_query_2Col_label_6Letter" style="text-align:right">索赔车型组：</td>
            <td >
            <input type="text" value="" name="WRGROUP_CODE"  id ="WRGROUP_CODE" class="middle_txt" readonly="readonly"  onclick="showLabor();"/>
            <input type="hidden" value="" name="WRGROUP_ID"  id ="WRGROUP_ID" class="middle_txt"/>
         	<!-- <input type="button" value="..." class="normal_btn" onclick="showLabor();"/>  -->
         	<input type="button" value="清空" class="normal_btn" onclick="cleanInput();"/>
            </td>
            <td class="table_query_2Col_label_5Letter" style="text-align:right">
                                      是否有效：
            </td>
            <td>
              <select id="is_del" name="is_del" class="u-select">
                 <option value="">-请选择-</option>
                 <option value="0">--是--</option>
                 <option value="1">--否--</option>
              </select>
            </td>
            <td class="table_query_2Col_label_6Letter" style="text-align:right">工时大类代码：</td>
          	<td>
          		<input name="LABOUR_CODE_BIG" type="text" id="LABOUR_CODE_BIG"  datatype="1,is_null,20"  class="middle_txt" />
          		<input name="coefficient " type="hidden" id="coefficient"  />
          		<input name="quantity" type="hidden" id="quantity"  />
          	</td>
           </tr>
          <tr>
          	
          	<td class="table_query_2Col_label_6Letter" style="text-align:right">工时大类名称：</td>
          	<td><input name="CN_DES_BIG" type="text" id="CN_DES_BIG"  datatype="1,is_null,100"  class="middle_txt"/></td>
          	<td class="table_query_2Col_label_6Letter" style="text-align:right">工时代码：</td>
          	<td><input name="LABOUR_CODE" type="text" id="LABOUR_CODE"  datatype="1,is_null,20"  class="middle_txt" /></td>
          	<td class="table_query_2Col_label_6Letter" style="text-align:right">工时名称：</td>
          	<td><input name="CN_DES" type="text" id="CN_DES"  datatype="1,is_null,100"  class="middle_txt"/></td>
          </tr>           
		   <tr>    
		   <td colspan="6" style="text-align:center">
            <input class="normal_btn" type="button" name="queryBtn" id="queryBtn" value="查询"  onclick="__extQuery__(1);"/>
            <input class="normal_btn" type="button" value="新增" name="add" onclick="subFun();"/>
			<!-- <input class="normal_btn" type="button" value="批量修改" name="mutilMod" onclick="mutilModed();"/>
			<input class="normal_btn" type="button" value="批量失效" name="mutildel"  id="mutildel" onclick="muti();"/> -->
			<input class="normal_btn" type="button" onclick="goImport();" value="批量导入"/>
           </td>
           <td>
           <div id="savebt" style="display:none">
					<input type="button"  value="" onclick="batchUpdate();" class="normal_btn"/>
				</div>
           </td>
           </tr>
       
  </table>
  </div>
  </div>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
</form>  
</div>
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/ClaimLaborMain/claimLaborQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'},
				/* {header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\"  onclick='selectAll(this,\"recesel\")' />选择", align:'center',sortable:false, dataIndex:'ID',width:'2%',renderer:checkBoxShow}, */
				{header: '车型组',align:'center',dataIndex:'WRGROUP_CODE'},
				{header: "工时代码",sortable: false,dataIndex: 'LABOUR_CODE',align:'center'},//,renderer:mySelect},				
				{header: "工时名称",sortable: false,dataIndex: 'CN_DES',align:'center'},
				{header: "工时大类代码",sortable: false,dataIndex: 'LABOUR_CODE_BIG',align:'center'},			
				{header: "工时大类名称",sortable: false,dataIndex: 'CN_DES_BIG',align:'center'},
				{header: "工时系数",sortable: false,dataIndex: 'LABOUR_QUOTIETY',align:'center'},
				{header: "索赔工时",sortable: false,dataIndex: 'LABOUR_HOUR',renderer:repale,align:'center'},
				{header: "是否有效",sortable: false,dataIndex: 'IS_DEL',renderer:mylink1,align:'center'}
		      ];

//设置超链接  begin      
	function mylink1(value,meta,record){
		if("0"==value){
		    return String.format("有效");
		}else if("1"==value){
			return String.format("无效");
		}
	}
	function checkBoxShow(value,meta,record){
		return String.format("<input type='checkbox' id='recesel' name='recesel' value='" + value + "' LABOUR_CODE='"+record.data.LABOUR_CODE+ "' CN_DES='"+record.data.CN_DES + + "' LABOUR_QUOTIETY='"+record.data.LABOUR_QUOTIETY + "' LABOUR_HOUR='"+record.data.LABOUR_HOUR+ "' LABOUR_CODE_BIG='"+record.data.LABOUR_CODE_BIG +"' />");
	}
	
	function mutilModed() {
		var checkBoxes = document.getElementsByName("recesel");
		var arr = new Array();
		var arr2 = new Array();
		for (var i=0;i<checkBoxes.length;i++) {
			if(checkBoxes[i].checked) {
				arr.push(checkBoxes[i].value);
				arr2.push(checkBoxes[i].getAttribute("LABOUR_CODE"));
			}
		}
		if (unique(arr2).length == 0) {
			MyAlert("请选择工时代码");
			return;
		}
		
		if (unique(arr2).length > 1) {
			MyAlert("批量更新必须选择相同工时代码");
			return;
		}
		
		var url = '<%=contextPath%>/claim/basicData/ClaimLaborMain/claimLaborMultiUpdateInit.do?ID=' + arr[0] +'&ARRID=' + arr ;
		OpenHtmlWindow(url,600,400);
		
	}
	
	function backUpate(){
		excuteAjaxQuery();
	}
	function unique(arr) {
	    var result = [], hash = {};
	    for (var i = 0, elem; (elem = arr[i]) != null; i++) {
	        if (!hash[elem]) {
	            result.push(elem);
	            hash[elem] = true;
	        }
	    }
	    return result;
	}
	//设置失效
     function Failure(value, uFlag){
       MyConfirm("确认修改状态？",Failurecommit,[value, uFlag]);
     }
     function   Failurecommit(value, uFlag){
    	 var url = "<%=contextPath%>/claim/basicData/ClaimLaborMain/Failure.json?id="+value + "&uFlag=" + uFlag;
    	 sendAjax(url,Failureback,'fm');
     }
     function Failureback(json){
       if(json.succ=='1'){
    	   __extQuery__(1);
          MyAlert("操作成功!");
       }else{
    	   MyAlert("操作失败!");
       }
     }
	function  muti(){
		var checkBoxes = document.getElementsByName("recesel");
		var arr = new Array();
		for (var i=0;i<checkBoxes.length;i++) {
			if(checkBoxes[i].checked) {
				arr.push(checkBoxes[i].value);
			}
		}
		MyConfirm("真的要将这些数据失效？",muticommit,[arr]);
	}
    function  muticommit(arr){
    	 var url = "<%=contextPath%>/claim/basicData/ClaimLaborMain/Failure.json?type=updates&ids="+arr;
    	 sendAjax(url,Failureback,'fm');
    }
	
	
	function batchUpdate(){
		if($('WRGROUP_CODE').value!=""){
			if($('LABOUR_CODE').value!=""||$('CN_DES').value!=""){
				OpenHtmlWindow('<%=contextPath%>/claim/basicData/ClaimLaborMain/batchUpdateMain.do',200,200);
			

			}
			else{
				MyAlert("批量更新必须选择工时名称或者工时代码");
			}
		}
		else {
			MyAlert("批量更新必须选择车型组");
		}
	
	}
		
	//修改的超链接设置
	function myLink(value,meta,record){
		var  url ="";
		  url += "<a  href=\"<%=contextPath%>/claim/basicData/ClaimLaborMain/claimLaborUpdateInit.do?ID="+ value + "\">[修改]</a>" ;
          var isdel = record.data.IS_DEL;
	        if("0"==isdel){
               url += "<a  href=\"#\" onclick='Failure(\""+value+"\", 1)'>[失效]</a>";
            } else {
            	url += "<a  href=\"#\" onclick='Failure(\""+value+"\", 0)'>[有效]</a>";
            }
		   return String.format(url);
	}
	
	
	function repale(value,meta,record)
	{
	   if(value.substring(0,1) == '.')
	   {
	     value = '0'+value;
	   }
	   return String.format(value);
	}

	function showLabor(){
		var url = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/laborListInit2.do' ;
		OpenHtmlWindow(url,800,500);
	}
	function setLaborList(codes,Wcodes){
		 var scode="";
		 var wcode="";
			 for(var i=0;i<codes.length;i++){
				 scode+=codes[i]+",";
				 wcode += Wcodes[i]+",";
			 }
		 	document.getElementById("WRGROUP_ID").value = scode.substring(0,scode.length-1);
		 	document.getElementById("WRGROUP_CODE").value = wcode.substring(0,wcode.length-1);
		}

	function cleanInput(){
		document.getElementById("WRGROUP_ID").value ="";
		document.getElementById("WRGROUP_CODE").value='';
	}
	
//设置超链接 end

//设置超链接
function mySelect(value,meta,record){
 		return String.format(
        "<a href=\"#\" onclick='selbyid(\""+record.data.ID+"\")'>["+ value +"]</a>");
}

//详细页面
function selbyid(value){
	OpenHtmlWindow('<%=contextPath%>/claim/basicData/ClaimLaborMain/claimLaborDetail.do?ID='+value,900,500);
}
  //新增
  function subFun(){
    location="<%=contextPath%>/claim/basicData/ClaimLaborMain/claimLaborAddInit.do";   
  }
//删除方法：
function sel(str){
	MyConfirm("是否确认删除？",del,[str]);
}  
//删除
function del(str){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimLaborMain/claimLaborDel.json?ID='+str,delBack,'fm','');
}

function getLabourPrice(var1,var2){
	$('coefficient').value=var1;
	$('quantity').value=var2;
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimLaborMain/batchUpdate.json?quantity2='+var2,updateBack,'fm','');
}
//删除回调方法：

function updateBack(){
	if(json.success != null && json.success == "true") {
		MyAlert("批量修改成功！");
		__extQuery__(1);
	} else {
		MyAlert("批量修改失败！请联系管理员！");
	}
	
}
function delBack(json) {
	if(json.success != null && json.success == "true") {
		MyAlert("删除成功！");
		__extQuery__(1);
	} else {
		MyAlert("删除失败！请联系管理员！");
	}
}
//页面跳转
function sendPage(){
	fm.action="<%=contextPath%>/claim/basicData/ClaimLaborMain/claimLaborUpdateInit.do";
	fm.submit();
}

function goImport(){
	location = '<%=request.getContextPath()%>/claim/basicData/ClaimLaborMain/inportPer.do' ;
}

</script>
</body>
</html>
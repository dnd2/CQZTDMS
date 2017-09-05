<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<%@page import="com.infodms.dms.util.sequenceUitl.SequenceManager" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>索赔件回运清单维护</title>
<% String contextPath = request.getContextPath(); %>
</head>
<script type="text/javascript">
function keyListnerResp(){
   if((typeof window.event)!= 'undefined'){
	   var type = event.srcElement.type;   
       var code = event.keyCode;
       if(type=='text'||type=='textarea'){
    	   event.returnValue=true;
       }else{//如 不是文本域则屏蔽 Alt+ 方向键 ← Alt+ 方向键 →   //屏蔽后退键    
         if(code==8||((window.event.altKey)&&((code==37)||(code==39)))){
            event.returnValue=false;       
         }
       }
   }
}
var myDate=new Date();
/**
 * 显示年份列表框
 * yearComponent:年份id
 * showNum:显示年份的数目,默认显示最近5年
 */
function showYearList(yearComponentName,showNum){
	if(yearComponentName==null||yearComponentName=='') return;
	var yearSelect=document.getElementById(yearComponentName);
	var curYear=myDate.getFullYear();
	var curMon=myDate.getMonth()+1;
	if(yearSelect!=null){
	   clearOptions(yearSelect.options);
	}
	if(showNum==null||showNum==''){
		showNum=5;
	}
	for(var yearCount=0;yearCount<showNum;yearCount++){
      var varItem = new Option(curYear-yearCount,curYear-yearCount);
      yearSelect.options.add(varItem);
    }
	if(curMon==1){//删除跨年时的年份
      for(var i = 0; i < yearSelect.options.length; i++) {        
        if (yearSelect.options[i].value == curYear) {        
            yearSelect.options.remove(i);        
            break;        
        }        
      }
      yearSelect.value=curYear-1;
    }else{
      yearSelect.value=curYear;
    }
}
/**
 * 显示月份列表框
 * monthComponentName:月份id
 */
function showMonthList(monthComponentName){
	if(monthComponentName==null||monthComponentName=='') return;
	var monSelect=document.getElementById(monthComponentName);
	
	var curMon=myDate.getMonth()+1;
	if(monSelect!=null){
	   clearOptions(monSelect.options);
	}
	for(var monCount=1;monCount<=12;monCount++){
      if((curMon-monCount)>=0){
        var varItem = new Option(monCount,monCount);
        monSelect.options.add(varItem);
      }else if(curMon==1){
    	var varItem = new Option(monCount,monCount);
        monSelect.options.add(varItem);
      }
    }
	if(curMon==1){
		monSelect.value=12;
	}else{
		monSelect.value=myDate.getMonth()+1;//默认选择为本月
	}
	
}
/**
 * @param 联动年份id
 * @param 联动月份id
 * @return
 */
function changeMonthList(yearName,monName){
	var yearSelect=document.getElementById(yearName);
	var monSelect=document.getElementById(monName);
	if(yearSelect==null||monSelect==null) return;
	var selectedYearValue=yearSelect.value;
	var curMon=myDate.getMonth()+1;
	clearOptions(monSelect.options);
	if(selectedYearValue==null||selectedYearValue==''){
		selectedYearValue=myDate.getFullYear();
    }
	if(selectedYearValue==myDate.getFullYear()){
      for(var monCount=1;monCount<=12;monCount++){
        if((curMon-monCount)>=0){
          var varItem = new Option(monCount,monCount);
          monSelect.options.add(varItem);
        }
      }
    }else{
      for(var monCount=1;monCount<=12;monCount++){
        var varItem = new Option(monCount,monCount);
          monSelect.options.add(varItem);
      }
    }
}
//清除下拉式控件信息
function clearOptions(colls){ 
  var length = colls.length; 
  for(var i=length-1;i>=0;i--){ 
     colls.remove(i); 
  } 
}
</script>
<BODY onload="doInit();" onkeydown="keyListnerResp();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔件回运清单维护</div>
  <form id="fm" name="fm">
   <input type="hidden" name="i_claimOrdId" id="i_claimOrdId" value="" />
   <input type="hidden" name="i_freight_type" id="i_freight_type" value="" />
   <input type="hidden" name="i_back_type" id="i_back_type" value="" />
   <input type="hidden" name="i_claimQryDate" id="i_claimQryDate" value="" />
   <input type="hidden" name="i_boxTotalNum" id="i_boxTotalNum" value="" />
  <div id="submitTimeDiv" style="display:">
    <TABLE class="table_query">
       <tr>
         <td align="right"  >&nbsp;</td>
         <td align="right" nowrap >索赔单提交时间段： </td>
         <td nowrap>
          <select id="yearStartSelect" name="yearStartSelect" onchange="changeMonthList('yearStartSelect','monStartSelect');"></select>年
          <select id="monStartSelect" name="monStartSelect"></select>月&nbsp;至&nbsp;
          <select id="yearEndSelect" name="yearEndSelect" onchange="changeMonthList('yearEndSelect','monEndSelect');"></select>年
          <select id="monEndSelect" name="monEndSelect"></select>月
         </td>
         <td height="25" align="right"  >&nbsp;</td>
       </tr>
       <tr>
         <td align="right" nowrap >&nbsp;</td>
         <td nowrap>&nbsp;</td>
         <td align="center" nowrap><input class="long_btn" type="button" id="createOrdBtn" name="createOrdBtn" value="生成回运清单"  onClick="createBackOrd();"></td>
         <td>&nbsp;</td>
       </tr>
  </table>
  </div>
  <div id="claimApplyOrdDiv" style="display:none">
    <table class="table_edit">
      <tr>
        <th colspan="4"  ><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 回运清单信息</th>
      </tr>
      <tr>
        <td height="25" align="right"  >回运清单号：</td>
        <td align="left"><input type="text" name="backOrdId" id="backOrdId" class="long_txt" readonly="readonly"/></td>
        <td align="right">索赔单提交时间段：</td>
        <td><input type="text" name="claimQryDate" id="claimQryDate"  class="long_txt" readonly="readonly"/></td>
      </tr>
      <tr>
        <td height="25" align="right">货运方式：</td>
        <td height="25" align="left">
        <script type="text/javascript">
         genSelBoxExp("freight_type",<%=Constant.OLD_RETURN_STATUS%>,"",true,"short_sel","","true",'');
        </script>
        </td>
        <td height="25" align="right"  >装箱总数量：</td>
        <td height="25" align="left"  ><span class="zi">
          <input type="text" id="boxTotalNum" name="boxTotalNum" datatype="1,isDigit,6" class="short_txt"/>
        </span></td>
      </tr>
      <tr>
        <td height="25" align="right">回运类型：</td>
        <td height="25" align="left"  >
         <script type="text/javascript">
         genSelBoxExp("back_type",<%=Constant.BACK_TRANSPORT_TYPE%>,"",false,"short_sel","","true",'<%=Constant.BACK_TRANSPORT_TYPE_01%>');
        </script>
        </td>
        <td height="25" align="right">生产基地：</td>
        <td height="25" align="left"  >
         <script type="text/javascript">
         genSelBoxExp("YIELDLY_TYPE",<%=Constant.YIELDLY_TYPE%>,"",false,"short_sel","","true",'');
        </script>
        </td>
        <td height="25" align="right"  >&nbsp;</td>
        <td height="25" align="left"  >&nbsp;</td>
      </tr>
      <tr>
      <td height="25" align="right">备注：</td>
      <td>
      <textarea rows="2" cols="50" name="remark"></textarea>
      </td>
      </tr>
      <tr>
         <td align="right" nowrap >&nbsp;</td>
         <td nowrap>&nbsp;</td>
         <td align="center" nowrap><input class="long_btn" type="button" id="addClaimApplyOrdBtn" name="addClaimApplyOrdBtn" value="添加索赔申请单"  onClick="addClaimApplyOrd();"></td>
         <td>&nbsp;</td>
       </tr>
    </table>
  </div>
  <br>
  <div id="claimApplyOrdListDiv" style="display:none">
    <table class="table_edit">
    <tr>
        <th colspan="15"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>索赔申请单信息</th>
    </tr>
    <tr>
         <td width="7%" align="right" nowrap>索赔申请单：</td>
         <td><input id="q_claim_no" name="q_claim_no" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,30" callFunction="javascript:MyAlert();"></td>
         <td width="7%" align="right" nowrap>VIN：</td>
         <td><input id="q_vin" name="q_vin" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,30" callFunction="javascript:MyAlert();"></td>
    </tr>
    <tr>
         <td width="7%" align="right" id="query_part_code_desc">配件代码：</td>
         <td id="query_part_code_textinput"><input id="q_part_code" name="q_part_code" value="" type="text" class="middle_txt" datatype="1,is_null,60" callFunction="javascript:MyAlert();"></td>
         <td width="9%" align="right" id="query_part_name_desc">配件名称：</td>
         <td align="left"id="query_part_name_textinput" ><input type="text" id="q_part_name" name="q_part_name" value="" class="middle_txt" datatype="1,is_null,60" callFunction="javascript:MyAlert();"/></td>
    </tr>
    <tr>
         <td align="center" colspan="4">
           <input class="normal_btn" type="button" name="qryButton" value="查询"  onClick="addClaimApplyOrd();">
         </td>
    </tr>
    </table>
    <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
    <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
  </div>
</form> 
<form name="form1" style="display:none">
  <table class="table_edit">
    <tr>
         <td align="center" nowrap colspan="15">
           <input class="normal_btn" type="button" name="joinButton" value="保存"  onclick="joinPreConfirm();">
           &nbsp;&nbsp;&nbsp;
           <input class="normal_btn" type="button" name="finishButton" value="返回" onclick="finish();">
         </td>
    </tr>
    </table>
</form>
<br>
<script type="text/javascript">
document.form1.style.display = "none";

var HIDDEN_ARRAY_IDS=['form1'];

var myPage;
//查询路径
var url="";
				
var title = null;

var columns = null;
function doInit(){
   loadcalendar();
   showYearList("yearStartSelect","8");
   showYearList("yearEndSelect","8");
   showMonthList("monStartSelect");
   showMonthList("monEndSelect");
}
//查看索赔单明细页面
function queryDetail(value,metaDate,record){
	var id=record.data.claim_id;
	return String.format("<a href='#' onclick=\"queryClaimDetail('"+id+"');\">" + value + "</a><input type='hidden' id='orderIds' name='orderIds' value='" + record.data.part_id + "' />");
}
function queryClaimDetail(id){
	var width=900;
	var height=500;

	var screenW = window.screen.width-30;	
	var screenH = document.viewport.getHeight();

	if(screenW!=null && screenW!='undefined')
		width = screenW;
	if(screenH!=null && screenH!='undefined')
		height = screenH;
	
	OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillDetailForward.do?ID='+id,width,height);
}
//全选checkbox
function myCheckBox(value,metaDate,record){
	  return String.format("<input type='checkbox' id='orderIds' name='orderIds' value='" + value + "' />");
}
function createBackOrd(){
    var startYear=document.getElementById("yearStartSelect").value;
    var endYear=document.getElementById("yearEndSelect").value;
    var startMon=document.getElementById("monStartSelect").value;
    var endMon=document.getElementById("monEndSelect").value;
    var startDate=startYear+"-"+(startMon.length==2?startMon:"0"+startMon);
    var endDate=endYear+"-"+(endMon.length==2?endMon:"0"+endMon);
    
    if(startYear>endYear){
       MyAlert("起始年份不应大于结束年份！");
       return;
    }
	if((endDate.replace("-","")-startDate.replace("-",""))<0){
		MyAlert("月份选择不正确！");
	    return;
	}
	//生成索赔回运单号并把提交日期回置给页面
	makeNomalFormCall("<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/createNewBackOrdPre.json?submit_start_date="
	         +startDate+"&submit_end_date="+endDate,executeBackOrd,'fm','createOrdBtn');
	
}
function executeBackOrd(json){
	document.getElementById("submitTimeDiv").style.display= "none";
	document.getElementById("claimApplyOrdDiv").style.display = "";
	document.getElementById("claimApplyOrdListDiv").style.display= "none";
	
	document.getElementById("backOrdId").value=json.claimBackOrdId;
	document.getElementById("claimQryDate").value=json.userSubmitDate;
}
function addClaimApplyOrd(){
    if(document.getElementById("freight_type")==null
    	    ||document.getElementById("freight_type").value==''){
    	MyAlert("请选择货运方式！");
    	return;
    }
    if(document.getElementById("back_type")==null
    	    ||document.getElementById("back_type").value==''){
    	MyAlert("请选择回运类型！");
    	return;
    }
    
    var param="command=2&claimOrdId="+document.getElementById("backOrdId").value;
    param+="&claimQryDate="+document.getElementById("claimQryDate").value.replace('至','#');
    param+="&freight_type="+document.getElementById("freight_type").value;
    param+="&back_type="+document.getElementById("back_type").value;
    param+="&boxTotalNum="+document.getElementById("boxTotalNum").value;
    param+="&yieldly="+document.getElementById("YIELDLY_TYPE").value;
    document.getElementById("back_type").disabled="disabled";
    url="<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryClaimUnBackList.json?"
        +param;
    //设置列表
    var back_type=document.getElementById("back_type").value;
    if(back_type==<%=Constant.BACK_TRANSPORT_TYPE_01%>){//紧急回运
    	columns = [
      				{header: "序号", align:'center',renderer:getIndex},
      				//drop by liuqiang.
      				//{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderIds\")' />", width:'8%',sortable: false,dataIndex: 'part_id',renderer:myCheckBox},
      				{header: "索赔申请单", dataIndex: 'claim_no', align:'center',renderer:queryDetail},
      				{header: "VIN", dataIndex: 'vin', align:'center'},
      				{header: "配件代码", dataIndex: 'part_code', align:'center'},
      				{header: "配件名称", dataIndex: 'part_name', align:'center'},
      				{header: "需回运数", dataIndex: 'quantity', align:'center'},
      				{header: "回运数", dataIndex: 'return_num', align:'center',renderer:returnBackNum},
      				{id:'action',header: "装箱单号",sortable: false,align:'center',renderer:boxOrdTxt}
      		      ];
    }else if(back_type==<%=Constant.BACK_TRANSPORT_TYPE_02%>){//常规回运
        	columns = [
      				{header: "序号", align:'center',renderer:getIndex},
      				//{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderIds\")' />", width:'8%',sortable: false,dataIndex: 'part_id',renderer:myCheckBox},
      				{header: "索赔申请单", dataIndex: 'claim_no', align:'center',renderer:queryDetail},
      				{header: "VIN", dataIndex: 'vin', align:'center'},
      				{header: "需回运数", dataIndex: 'quantity', align:'center'},
      				{header: "回运数", dataIndex: 'return_num', align:'center',renderer:returnBackNum}
      		      ];
	    //将配件代码和配件名称的查询条件去掉
	    document.getElementById("query_part_code_desc").innerHTML="";
	    document.getElementById("query_part_code_textinput").innerHTML="";
	    document.getElementById("query_part_name_desc").innerHTML="";
	    document.getElementById("query_part_name_textinput").innerHTML="";
    }
    makeNomalFormCall(url,showList,'fm','createOrdBtn');
}
//设置回运数量是否可编辑
function returnBackNum(value,metaDate,record){
	var back_type=document.getElementById("back_type").value;
	var str=record.data.part_id;
	var requiredNum=record.data.quantity;
	document.getElementById("back_type").disabled="disabled";
	if(back_type==<%=Constant.BACK_TRANSPORT_TYPE_01%>){//紧急回运
		return String.format("<input type=\"text\" id=\"urgeBackNum"+str+"\" name=\"urgeBackNum"+str
				+"\" class=\"short_txt\" datatype=\"0,is_digit,10\" onkeypress=\"return event.keyCode>=48&&event.keyCode<=57\" value=\"" 
				+ requiredNum + "\" onblur=\"checkUrgeBackCondition('urgeBackNum"+str+"','" + requiredNum + "');\" />");
	}else if(back_type==<%=Constant.BACK_TRANSPORT_TYPE_02%>){//常规回运
		return String.format(requiredNum+"<input type=\"hidden\" id=\"urgeBackNum"+str+"\" name=\"urgeBackNum"+str+"\" value=\"" + requiredNum + "\" />");
	}
}
//装箱单号文本
function boxOrdTxt(value,metaDate,record){
	var str=record.data.part_id;
	return String.format("<input type=\"text\" id=\"boxOrd"+str+"\" name=\"boxOrd"+str+"\" class=\"short_txt\" datatype='1,is_digit_letter,30' value=''/>");
}
//检查是否被选中
function checkIsSelected(checkId){
	var select_id=checkId.substring(11,checkId.length);
	var retFlag=false;
	var chk = document.getElementsByName("orderIds");
	var len = chk.length;
	for(var i=0;i<len;i++){
		if(chk[i].checked){        
			if(select_id==chk[i].value){
				retFlag=true;//被选中
				break;
			}
		}
	} 
	return retFlag;
}
//检查回运数量是否大于需回运数量
function checkUrgeBackCondition(textId,requiredNum){
	if(!checkIsSelected(textId)){//不对未被选中的数据行进回运数量的检查
		return;
	}
	if(document.getElementById(textId)==null||document.getElementById(textId).value==''){
		MyAlert("请填写紧急回运数！");
		document.getElementById(textId).focus();
		return;
	}
	if(document.getElementById(textId).value<=0){
		MyAlert("紧急回运数不能小于等于零！");
		document.getElementById(textId).focus();
		return;
	}
	if((document.getElementById(textId).value-requiredNum)>'0'){
		MyAlert("紧急回运数不能超过需回运数！");
		document.getElementById(textId).focus();
		return;
	}
}
//回调函数，处理数据显示
function showList(json){
	document.getElementById("addClaimApplyOrdBtn").disabled="disabled";
	document.getElementById("submitTimeDiv").style.display= "none";
	document.getElementById("YIELDLY_TYPE").disabled= true;
	document.getElementById("claimApplyOrdDiv").style.display = "";
	document.getElementById("claimApplyOrdListDiv").style.display = "";
	var joinFlag=json.joinFlag;
	var pss;
	if(Object.keys(json).length>0){
		keys = Object.keys(json);
		for(var i=0;i<keys.length;i++){
		   if(keys[i] =="ps"){
			   pss = json[keys[i]];
			   break;
		   }
		}
	}
	if(pss.records != null){
		$("_page").hide();
		$('myGrid').show();
		new reCreateGrid(title,columns, $("myGrid"),pss).load();			
		//分页
		myPage = new showPages("myPage",pss,url);
		myPage.printHtml();
		hiddenDocObject(2);
	}else{
		$("_page").show();
		$("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据</div>";
		$("myPage").innerHTML = "";
		removeGird('myGrid');
		$('myGrid').hide();
		hiddenDocObject(1);
	}
	if(joinFlag=="1"){
       MyAlert("保存成功！");
	}
}
//选中预检查
function preChecked() {
	var str="";
	var chk = document.getElementsByName("orderIds");
	var len = chk.length;
	var cnt = 0;
	for(var i=0;i<len;i++){        
		str = chk[i].value+","+str; 
		cnt++;
	}
	
	/*if(cnt==0){
        MyAlert("请选择要加入的索赔申请单！");
        return;
    }*/
    return str.substring(0,str.length-1);
}
function joinPreConfirm(){
	if(document.getElementById("freight_type")==null
    	    ||document.getElementById("freight_type").value==''){
    	MyAlert("请选择货运方式！");
    	return;
    }
	MyConfirm("确认保存？",join,"");
}
//加入操作
function join(){
	var str=preChecked();//获得选中的id串
	//主表信息
	var param="claimOrdId="+document.getElementById("backOrdId").value;
    param+="&idStr="+str;
    param+="&back_type="+document.getElementById("back_type").value;
    var idArr=str.split(",");
    var join_url="<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/joinClaimOldPart.json?"
        +param;
    makeNomalFormCall(join_url,showList,'fm','createOrdBtn');
}
//完成功能，返回到查询页面
function finish(){
	fm.action="<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryListPage.do";
	fm.submit();
}
</script>
</BODY>
</html>
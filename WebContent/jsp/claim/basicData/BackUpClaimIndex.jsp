<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>备件三包期维护</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
//日历控件初始化
function doInit()
	{
	   loadcalendar();
	}
//配件导入页面
function importParts(){
        var ruleId=document.getElementById("ruleId").value;
		fm.action = "<%=contextPath%>/claim/basicData/ClaimRule/claimRuleImport.do?ruleId="+ruleId;
		fm.submit();
}
//导出.do 提交方式开始
 function exportExcel(){
       var ruleId=document.getElementById("ruleId").value;
	   var partCode=document.getElementById("partCode").value;
	   var claimMonth=document.getElementById("claimMonth").value;
	   var claimMelieage=document.getElementById("claimMelieage").value;
       fm.action="<%=contextPath%>/claim/basicData/ClaimRule/exportExcel.do";
       fm.submit();
	 }
//导出.do 提交方式结束
</script>
</head>

<body onload = "__extQuery__(1);">
     <div class="navigation">
         <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;备件三包期维护
     </div>
  <!-- 查询条件 begin -->
  <form method="post" name="fm" id="fm">
  <input type="hidden" id="ruleId" name="ruleId" value="<%=request.getAttribute("ruleId") %>"/>
  <input type="hidden" id="delIdValue" name="delIdValue" value=""/>
  <table class="table_query">
      <tr>
        <td class="table_query_2Col_label_7Letter"> 备件代码： </td>
        <td align="left">
	        <input  type="text" class="middle_txt" size="25" id="partCode" name="partCode" datatype="1,is_null,20"/>
        </td>
        <td class="table_query_2Col_label_7Letter"> 备件名称： </td>
        <td align="left">
	        <input  type="text" class="middle_txt" size="25" id="partName" name="partName" datatype="1,is_null,20"/>
        </td>
      </tr>
      <tr>
      	 <td class="table_query_2Col_label_7Letter"> 三包月份： </td>
        <td align="left">
	        <input type="text" class="middle_txt" size="25" id="claimMonth" name="claimMonth" datatype="1,is_digit,10"/>
        </td>
        <td class="table_query_2Col_label_7Letter"> 三包里程：</td>
        <td align="left">
	       <input  type="text" class="middle_txt" size="25" id="claimMelieage" name="claimMelieage" datatype="1,is_digit,12"/>
        </td>
      </tr>
      <tr>
      	<td class="table_query_2Col_label_7Letter" nowrap="nowrap">三包类型：</td>
				<td nowrap="nowrap">
					<select class="width:90px" id="txt1" name="partWarType">
					<option selected="selected" value="">--请选择--</option>
      				<option value="94031002">易损易耗件</option>
      				<option value="94031001">常规件</option>
				   </select>
				</td>
      </tr>
      <tr>
      	<td colspan="8" align="center">
      		<input name="buttonQuery" type="button" class="normal_btn" value="查询" onclick="__extQuery__(1);"/>
      	</td>
      </tr>
</table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
  </form> 
  <form name="form1" style="display:none">
   <table class=tblopt align=center width="90%">
    <tr>
      <td width="90%" height="21" align=center>
          <input name="button" type="button" class="normal_btn" onclick="subChecked();" value="修改" />
    </tr>
 </table>
 </form>
<!-- 查询条件 end -->
<br/>
<!--页面列表 begin -->
<script type="text/javascript" >
	//没有数据时 上报/删除按钮隐藏 开始 
	document.form1.style.display = "none";
	
	var HIDDEN_ARRAY_IDS=['form1'];
	//没有数据时 上报/删除按钮隐藏 结束

	var myPage;
//查询路径
	var url = "<%=contextPath%>/claim/basicData/ClaimRule/backUpClaimDetailQuery.json";
				
	var title = null;

	var columns = [
	            {id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderIds\")' />", width:'8%',sortable: false,dataIndex: 'ID',renderer:myCheckBox},
				{header: "备件代码", dataIndex: 'PART_CODE', align:'center'},
				{header: "备件名称",dataIndex: 'PART_NAME' ,align:'center'},
				{id:'action',header: "三包月份",sortable: false,dataIndex: 'CLAIM_MONTH' ,align:'center',renderer:claimMonth},
				{id:'action',header: "三包里程",sortable: false,dataIndex: 'CLAIM_MELIEAGE' ,align:'center',renderer:claimMelieage},
		    	{header: "配件三包类型",sortable: false,dataIndex: 'PART_WAR_TYPE' ,align:'center'}
		      ];
    function customTableFunc(tabObj)
    {
    	addListener();//form表达校验	
    }
    
    //全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='orderIds' value='" + value + "' />");
	}
	//三包月份
	function claimMonth(value,metaDate,record){
		return String.format("<input type='text' name='CLAIM_MONTH' id='"+value+"' value='" + value + "' datatype='1,is_digit,18'/>");
	}
	//三包里程
	function claimMelieage(value,metaDate,record){
		return String.format("<input type='text' name='CLAIM_MELIEAGE' id='"+value+"' value='" + value + "' datatype='1,is_digit_line,18'/>");
	}
		/*
  	功能：增加方法
  	参数：action : "add":增加
  	描述: 取得已经选择的checkbox，拼接成字符串，各项目以,隔开
   */
function subChecked() {
	var str="";
	var st="";
	var stm="";
	var chk = document.getElementsByName("orderIds");
	var ch = document.getElementsByName("CLAIM_MONTH");
	var chm = document.getElementsByName("CLAIM_MELIEAGE");
	var l = chk.length;
	var ll = ch.length;
	var lll = chm.length;
	var cnt = 0;
	for(var i=0;i<l;i++){        
		if(chk[i].checked)
		{          
			if(str)
			{  
				str += ","+chk[i].value;
				st += ","+ch[i].value;
				stm += ","+chm[i].value;
			}else{
				str += chk[i].value;
				st += ch[i].value;
				stm += chm[i].value;
			} 
			cnt++;
		}
	}
	if(cnt==0){
		MyAlert("请选择备件！");
        return;
    }else{
	       if(!submitForm('fm')) {//表单校验
				return false;
		    }
	    MyConfirm("是否确认修改？",sures,[str,st,stm]);
   }
}
//删除配件
function delPart(){
	var idValue = '';
	var chk = document.getElementsByName("orderIds");
	if(chk==null||chk==''){
		MyAlert('请选择你要删除的配件！');
		return;
	}else{
		for(var i=0;i<chk.length;i++){
			if(chk[i].checked){
				if(idValue==''){
					idValue = chk[i].value;
				}else{
					idValue = idValue +','+chk[i].value;
				}
			}
		}
		if($('delIdValue').value!=''){
			$('delIdValue').value=='';
		}
		$('delIdValue').value=idValue;
		MyDivConfirm("是否确认删除？",delConfirm);
	}
	
}

function delConfirm(){
	makeNomalFormCall('<%=request.getContextPath()%>/claim/basicData/ClaimRule/claimRuleDelImportParts.json',myFunc,'fm','queryBtn');
}
//不能删除回调方法
function myFunc(json){
	var msg = json.success;
	var info = json.msg;
	if(msg=="true"){
		MyDivAlert('删除成功');
		__extQuery__(1);
	}else if(msg=="false"){
		MyDivAlert('操作失败:'+info);
	}
}

function sures(str,st,stm){
	makeNomalFormCall('<%=request.getContextPath()%>/claim/basicData/ClaimRule/backUpClaimUpdateImportParts.json?id='+str+'&claimMonth='+st+'&claimMelieage='+stm,delBack,'fm','queryBtn');
}

//新增回调方法
function delBack(json) {
	if(json.success != null && json.success == "true") {
		MyAlert("修改成功！");
	} else {
		MyAlert("修改失败！请联系管理员！");
	}
}
</script>
<!--页面列表 end -->
</body>
</html>
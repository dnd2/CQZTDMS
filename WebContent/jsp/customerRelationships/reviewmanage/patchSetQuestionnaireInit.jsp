<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import=" com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>  
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<script type="text/javascript">
function doInit()
{
	loadcalendar();
}
</script>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>我的客户回访</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  客户关系管理&gt;回访管理&gt;批量设置问卷 </div>
<form method="post" name="fm" id="fm">
<input type="hidden" id="isClick" name="isClick" />
<TABLE class=table_query >
  <TBODY>
    <tr class="">
      <td width="10%" align="right"> 回访类型：</td>
      <td width="20%"><script type="text/javascript">
           genSelBoxExp("RV_TYPE",<%=Constant.TYPE_RETURN_VISIT%>,null,true,"short_sel","","false",'');
	 </script></td>
      <td width="10%" align="right"> 开始日期：</td>
      <td width="25%"><input name="checkSDate" class="short_txt" id="checkSDate" maxlength="10" group="checkSDate,checkEDate" datatype="1,is_date,10" readonly />
        <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" type="button" value=" " />
        至 
        <input name="checkEDate" class="short_txt" id="checkEDate" maxlength="10" group="checkSDate,checkEDate" datatype="1,is_date,10" readonly />
        <input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" type="button" value=" " /></td>
      <!-- 
      <td width="10%"  align="right">回访人：</td>
      <td width="15%"  align="left" ><input  id="RV_ASS_USER" class="middle_txt" name="RV_ASS_USER"  /></td>
       -->
    </tr>
    <tr>
    	<td colspan="4" height="1">&nbsp;</td>
    </tr>
    <tr>
        <td align="right">选择回访类型 ：</td>
        <td align="left"><script type="text/javascript">
	           genSelBoxExp("SELECT_RV_TYPE",<%=Constant.TYPE_RETURN_VISIT%>,null,true,"short_sel","","false",'');
		 </script></td>
         <td colspan="2" align="center"><input name="queryBtn" id="queryBtn" type="button" class="normal_btn" align="left" onclick="__extQuery__(1);" value="查询" /></td>
    </tr>
    <tr>
      <td align="right">选择问卷模版：</td>
      <td align="left"><select name="QR_ID" class="short_sel" id="QR_ID">
          <option selected="selected" value="">--请选择--</option>
          <c:forEach var="ql" items="${questionairList}">
            <option value="${ql.QR_ID}" >${ql.QR_NAME}</option>
          </c:forEach>
        </select></td>
      <td colspan="2">从
        <input id="NumFrom" style="WIDTH: 57px" name="NumFrom" onafterpaste="this.value=this.value.replace(/\D/g,'')"  onkeyup="this.value=this.value.replace(/\D/g,'')"/> 号－到 
        <input id="NumTo" style="WIDTH: 57px" name="NumTo" onafterpaste="this.value=this.value.replace(/\D/g,'')"  onkeyup="this.value=this.value.replace(/\D/g,'')"/> 号
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input name="button" type="button" class="normal_btn" onclick="setQuestionair();" align="left" value="设置" /></td>
    </tr>
    <tr>
    
  </TBODY>
</TABLE>
<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
 <!--分页 end --> 
</form> 
<br/>
<!--页面列表 begin -->
<script type="text/javascript" ><!--
var myPage;
//查询路径
	var url = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/setQuestionairQuery.json";
	var title = null;
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"code\")' />全选", align:'center',sortable:false, dataIndex:'id',width:'2%',renderer:checkBoxShow},
				{header: "客户姓名",sortable: false,dataIndex: 'RV_CUS_NAME',align:'center'}, 
				{header: "回访类型 ",sortable: false,dataIndex: 'RV_TYPE',renderer:setIsClick,align:'center'},
				{header: "生成时间",sortable: false,dataIndex: 'RV_DATE',align:'center'},
				{header: "当前状态",sortable: false,dataIndex: 'RV_STATUS',align:'center'},
				{header: "回访人",sortable: false,dataIndex: 'RV_ASS_USER',align:'center'},
				{header: "查看",sortable: false,dataIndex: 'RV_ID',renderer:myHandler ,align:'center'}
		      ];
		      
	//查看回访超链接		      
  	function myHandler(value,meta,record){
		return '<a href="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/seeReview.do?pagetype=questionair&RV_ID='+record.data.RV_ID+'">[查看]</a>' ;
	}
	function setIsClick(value,meta,record){
		document.fm.isClick.value='true';
		return record.data.RV_TYPE ;
	}
	
	//设置复选框
	function checkBoxShow(value,meta,record){
		return String.format("<input type='checkbox' id='code' name='code' value='" + record.data.RV_ID + "' />");
	}
	 //设置操作
    function setQuestionair() {
    
    	var ischeck=document.fm.isClick.value;
    	if(null==ischeck||"".indexOf(ischeck)==0)
    	{
    		MyAlert("请先进行查询再点击设置按钮！");
	       	return false;
    	}
	    var qrId = document.fm.QR_ID.value;
	    if(null==qrId||"".indexOf(qrId)==0){
	       MyAlert("请选择问卷模版！");
	       return false;
	    }
    
		var allChecks = document.getElementsByName("code");
		var allFlag = false;
		var tNumFrom = document.getElementById("NumFrom").value;
		var tNumTo = document.getElementById("NumTo").value;
		var	NumFrom=Trim(tNumFrom);
		var	NumTo=Trim(tNumTo);
		if(NumFrom.length>0||NumTo.length>0)
		{
			if(NumFrom.length<=0)
			{
				 MyAlert("起始序号不能为空！");
			     return false;
			}else if(NumTo.length<=0)
			{
				MyAlert("结束序号不能为空！");
			    return false;
			}else if(NumFrom<=0)
			{
				MyAlert("起始序号不能为负数或0！");
			    return false;
			}else if(NumTo<=0)
			{
				MyAlert("结束序号不能为负数或0！");
			    return false;
			}else if(NumFrom>tot(json))
			{
				MyAlert("起始序号不能大于总条数！");
			    return false;
			}else if(NumTo>tot(json))
			{
				MyAlert("结束序号不能大于总条数！");
			    return false;
			}
			if(parseInt(NumTo) < parseInt(NumFrom))
			{
				//MyAlert(false);
				MyAlert("起始序号不能大于结束序号！");
			    return false;
			   
			}
			if(parseInt(NumFrom)<=(NumTo))
			{
				allFlag = true;
			}
		}	
		
		for(var i = 0;i<allChecks.length;i++){
			if(allChecks[i].checked){
				allFlag = true;
			}
		}
		if(allFlag){
			MyConfirm("确认设置?", setSubmit);
		}else{
			MyAlert("请选择或者填写号段再点击设置按钮！");
		}
	}
	function setSubmit() {
		var url="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/setQuestionair.json";
		makeNomalFormCall(url,showResult22,'fm');
    }
    
    function showResult22(json){
	var msg=json.msg;
	if(msg=='01'){
		MyAlert('设置成功');
		__extQuery__(1);
	}else{
		MyAlert('设置失败,请联系管理员');
	}
	
}

function tot(json)
{
	if(null==json)
	{
		MyAlert('请先点查询数据！');
		return false;
	}else{
		var tot=json.tot;
		//MyAlert(tot);
		return tot;
	}
}
function LTrim(str)
{
    var i;
    for(i=0;i<str.length;i++)
    {
        if(str.charAt(i)!=" "&&str.charAt(i)!=" ")break;
    }
    str=str.substring(i,str.length);
    return str;
}
function RTrim(str)
{
    var i;
    for(i=str.length-1;i>=0;i--)
    {
        if(str.charAt(i)!=" "&&str.charAt(i)!=" ")break;
    }
    str=str.substring(0,i+1);
    return str;
}
function Trim(str)
{
    return LTrim(RTrim(str));
}


--></script>  
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
<script type="text/javascript"> 

 
var arr = new Array();
function f_leave(obj, e) {
	if (obj.contains(e.toElement) || obj == e.toElement) {
		arr = new Array();
		var k = 0;
		var reschs = document.getElementsByName("resch");
		var chis = document.getElementsByName("chid");
		for(var i=0; i<reschs.length; i++) {
			if(reschs[i].checked){
					arr[k++]=chis[i].value;
				}
			}
		if(arr.length != 0){
			document.getElementById("resvalue").value = arr.toString();
		}else{
			document.getElementById("resvalue").value = '';
		}
	}
	if(obj == e.toElement){
       document.getElementById("res").style.display = "none";
	}
}
 
 
</script>

<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>我的客户回访</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body >
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  客户关系管理&gt;回访管理&gt;分配回访任务 
</div>
  <form method="post" name="fm" id="fm">
  <input type="hidden" id="resvalue" name="resvalue"/>
  <input type="hidden" id="isClick" name="isClick" />
  <TABLE class=table_query >
  <TBODY>
    <tr class="">
      <td width="19%" align="right"> 回访类型：</td>
      <td width="22%">
      <script type="text/javascript">
           genSelBoxExp("RV_TYPE",<%=Constant.TYPE_RETURN_VISIT%>,null,true,"short_sel","","false",'');
	 </script>
      </td>
      <td width="13%" align="right"> 开始日期：</td>
      <td width="20%"><input name="checkSDate" class="short_txt" id="checkSDate" maxlength="10" group="checkSDate,checkEDate" datatype="1,is_date,10" readonly />
        <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" type="button" value=" " />
        至 
  <input name="checkEDate" class="short_txt" id="checkEDate" maxlength="10" group="checkSDate,checkEDate" datatype="1,is_date,10" readonly />
  <input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" type="button" value=" " /></td>
      <td width="5%" align="right">&nbsp;</td>
      <td width="15%"><span class="table">
        <input name="queryBtn" id="queryBtn" type="button" class="normal_btn" align="right" value="查询"  onclick="queryRes();"/>
        </span></td>
    </tr>
    
    <tr class="">
      <td align="right">指定回访人：</td>
      <td>
      <input name="chid" id="chid" type="hidden" value="" />
      <textarea rows="3" cols="20" disabled="disabled" name="name" id ="name"></textarea>
        <input name="search" id="search" disabled="disabled" class="normal_btn"  onclick="querySet();" type="button" value="选择" />
	  </td>
      <td colspan="2" align="right">从
        <input id="NumberFrom" style="WIDTH: 57px" name="NumberFrom" />
        号－到
        <input id="NumberTo" style="WIDTH: 57px" name="NumberTo" />
        号<span class="table">
          <input name="button"  id="setQ" type="button" class="normal_btn" disabled="disabled" onclick="setReviewPerson();"  value="设置" />
        </span></td>
      <td align="center">&nbsp;</td>
      <td align="center"> </td>
    </tr>
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
	var url = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/allocateReviewQuery.json";
	var title = null;
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"code\")' />全选", align:'center',sortable:false, dataIndex:'id',width:'2%',renderer:checkBoxShow},
				{header: "客户姓名",sortable: false,dataIndex: 'RV_CUS_NAME',align:'center'}, 
				{header: "回访类型 ",sortable: false,dataIndex: 'RV_TYPE',renderer:setIsClick,align:'center'},
				{header: "生成时间",sortable: true,dataIndex: 'RV_DATE',align:'center'},
				{header: "当前状态",sortable: false,dataIndex: 'RV_STATUS',align:'center'},
				{header: "回访人",sortable: false,dataIndex: 'RV_ASS_USER',align:'center'},
				{header: "查看",sortable: false,dataIndex: 'RV_ID',renderer:myHandler ,align:'center'}
		      ];
		      
	//查看回访超链接		      
  	function myHandler(value,meta,record){
		return '<a href="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/seeReview.do?pagetype=allocate&RV_ID='+record.data.RV_ID+'">[查看]</a>' ;
	}
	//设置复选框
	function checkBoxShow(value,meta,record){
		return String.format("<input type='checkbox' id='code' name='code' value='" + record.data.RV_ID + "' />");
	}
	function setIsClick(value,meta,record){
		document.fm.isClick.value='true';
		return record.data.RV_TYPE ;
	}
	function querySet(){
			OpenHtmlWindow('<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/querySet.do',800,500);
	} 
	function queryRes(){
	__extQuery__(1);
	$('search').disabled = false;
	$('setQ').disabled = false;
	}
	//设置问卷
    function setReviewPerson() {
    
    	var ischeck=document.fm.isClick.value;
    	if(null==ischeck||"".indexOf(ischeck)==0)
    	{
    		MyAlert("请先进行查询再点击设置按钮！");
	       	return false;
    	}
    	var resvalue = document.fm.resvalue.value;
	    if(null==resvalue||"".indexOf(resvalue)==0){
	       MyAlert("请指定回访人！");
	       return false;
	    }
	   	var strs= new Array(); 
	   	strs=resvalue.split(","); 
		var allChecks = document.getElementsByName("code");
		var allFlag = false;
		var tNumFrom = document.getElementById("NumberFrom").value;
		var tNumTo = document.getElementById("NumberTo").value;
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
			 if(strs.length>(parseInt(NumTo)-parseInt(NumFrom)+1))
		     {
		    	MyAlert("指定的回访人数不能大于所填序号人数！");
				return false;
		     }
			if(parseInt(NumFrom)<=(NumTo))
			{
				allFlag = true;
			}
		}	
		var numcount=0;
		for(var i = 0;i<allChecks.length;i++){
			if(allChecks[i].checked){
				allFlag = true;
				numcount++;
			}
		}
		 if(numcount>0&&strs.length>numcount)
	     {
	    	MyAlert("指定的回访人数不能大于所选人数！");
			return false;
	     }
		if(allFlag){
			MyConfirm("确认设置?", authoritySubmit);
		}else{
			MyAlert("请选择或输入序号后再点击设置按钮！");
		}
}
	function authoritySubmit() {
		var url="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/setReviewPerson.json";
		makeNomalFormCall(url,showResult22,'fm');
    }
    
function showResult22(json){
	var msg=json.msg;
	if(msg=='01'){
		MyAlert('设置成功');
		__extQuery__(1);
		document.getElementById("NumberFrom").value="";
		 document.getElementById("NumberTo").value="";
		 $('search').disabled = true;
		$('setQ').disabled = true;
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
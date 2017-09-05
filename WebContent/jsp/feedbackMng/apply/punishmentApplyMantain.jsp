<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>奖惩审批表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
function doInit()
	{
	   loadcalendar();
	}
</script>
</head>

<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置： 信息反馈管理&gt;信息反馈提报&gt;奖惩审批表</div>
    <!-- 查询条件 begin -->
  <form method="post" name ="fm" id="fm">
    <table class="table_query">
        <tr>
            <td width="7%" align="right" nowrap>工单号：</td>
            <td colspan="6">
            <input name="orderId" id="orderId" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,18" />
            </td>
            <td width="13%" align="right" nowrap>经销商代码：</td>
        	<td>
			<textarea rows="2" cols="35" id="dealerCode" name="dealerCode"></textarea>
            <input name="button1" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
            <input name="button2" type="button" class="normal_btn" onclick="clr();" value="清除"/>
       		 </td>
          </tr>
          <tr>
            <td align="right" nowrap >经销商名称：</td>
            <td colspan="6" nowrap>
                <input class="middle_txt" id="dealerName"  name="dealerName" style="cursor: pointer;" type="text"/>
            </td>
            <td align="right" nowrap>类型：</td>
            <td>
                <script type="text/javascript">
   					genSelBoxExp("rewardType",<%=Constant.PUNISHMENT%>,"",true,"short_sel","","true",'');
  				</script>
            </td>
            <td align="right" >&nbsp;</td>
          </tr>
          <tr>
            <td width="7%" align="right" nowrap >创建时间： </td>
            <td colspan="6" nowrap>
              		<div align="left">
	            		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);"/>
	            		&nbsp;至&nbsp;
	            		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);"/>
            	    </div>			
            </td>
            <td width="23%" align="right" >&nbsp;</td>
          </tr>
          <tr>
            <td align="right" nowrap >&nbsp;</td>
            <td colspan="6" nowrap>&nbsp;</td>
            <td align="right" nowrap><input class="normal_btn" type="button" name="button" value="查询"  onClick="showButton();__extQuery__(1)">
			<input class="normal_btn" type="button" name="button1" value="新增"  onclick="getPunishmentApplyMantainInfo()"/></td>
            <td>&nbsp;</td>
            <td align="right" ></td>
          </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form> 
<!-- 查询条件 end -->
<!--没有数据时 上报/删除按钮隐藏 开始  -->
<form name="form1" style="display:none">
<table id="bt" class="table_list">
	  <tr><th align="left"><p>
	    <input class="normal_btn" type=button value='上报' name=modify onclick="subChecked('sub');">
	    &nbsp;<input class="normal_btn" type=button value='删除' name=modify onclick="subChecked('del');">
	  </p></th>
	  </tr>
  </table>
 </form>
<!--没有数据时 上报/删除按钮隐藏 结束  -->
<br/>
<!--页面列表 begin -->
<script type="text/javascript" >
//没有数据时 上报/删除按钮隐藏 开始 
	document.form1.style.display = "none";

	var HIDDEN_ARRAY_IDS=['form1'];
//没有数据时 上报/删除按钮隐藏 结束
	var myPage;
//查询路径
	var url = "<%=contextPath%>/feedbackmng/apply/PunishmentApplyMantain/punishmentApplyMantainQuery.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderIds\")' />", width:'8%',sortable: false,dataIndex: 'ORDER_ID',renderer:myCheckBox},
				//{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				//{header: "经销商心名称",dataIndex: 'DEALER_NAME' ,align:'center'},
				{id:'action',header: "工单号",sortable: false,dataIndex: 'ORDER_ID',renderer:mySelect ,align:'center'},
				{header: "申请单位",dataIndex: 'LINK_MAN' ,align:'center'},
				{header: "类型",dataIndex: 'REWARD_TYPE' ,align:'center',renderer:getItemValue},
				{header: "创建时间",dataIndex: 'CREATE_DATE' ,align:'center'},
				{header: "工单状态",dataIndex: 'REWARD_STATUS' ,align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ORDER_ID',renderer:punishmentApplyMantainUpdateInit ,align:'center'}
		      ];
     //设置超链接  begin      
	//修改的超链接设置
	function punishmentApplyMantainUpdateInit(value,meta,record){
	    return String.format(
         "<a href=\"<%=contextPath%>/feedbackmng/apply/PunishmentApplyMantain/punishmentApplyMantainUpdateInit.do?orderId="
			+ value + "\">[修改]</a>");
	}
	
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='orderIds' value='" + value + "' />");
	}
	
	//设置超链接
	function mySelect(value,meta,record){
  		return String.format(
         "<a href=\"#\" onclick='sel(\""+record.data.ORDER_ID+"\")'>["+ value +"]</a>");
	}
	
	//详细页面
	function sel(value){
		OpenHtmlWindow('<%=contextPath%>/feedbackmng/apply/PunishmentApplyMantain/getOrderIdInfo.do?orderId='+value,800,500);
	}
	
	//查询登录人信息（姓名、电话）
	function getPunishmentApplyMantainInfo(){
		fm.action = "<%=contextPath%>/feedbackmng/apply/PunishmentApplyMantain/getPunishmentApplyMantainInfo.do";
		fm.submit();
	}
	
	function showButton(){//显示上报、删除按钮
		document.getElementById("bt").style.visibility = "visible";
	}
	//删除开始
	function punishmentApplyMantainDelete(str){
	makeNomalFormCall('<%=request.getContextPath()%>/feedbackmng/apply/PunishmentApplyMantain/punishmentApplyMantainDelete.json?orderIds='+str,returnBack,'fm','queryBtn');
	}
	//取得已经选择的checkbox，拼接成字符串，各项目以,隔开
	function getCheckedToStr(name) {
		var str="";
		var chk = document.getElementsByName(name);
		var l = chk.length;
		for(var i=0;i<l;i++){        
			if(chk[i].checked)
			{            
			str = chk[i].value+","+str; 
			}
	}
	return str;
	}
	//删除回调函数
	function returnBack(json){
		var del = json.returnValue;
		if(del==1){
			__extQuery__(1);
			MyAlert("删除成功！");
		}else{
			MyAlert("删除失败！请联系管理员！");
		}
	}
	//删除结束
	
	//上报开始
	function punishmentApplyMantainResport(str){
	makeNomalFormCall('<%=request.getContextPath()%>/feedbackmng/apply/PunishmentApplyMantain/punishmentApplyMantainReport.json?orderIds='+str,returnResport,'fm','queryBtn');
	}
	//上报回调函数
	function returnResport(json){
		var del = json.returnValue;
		if(del==1){
			__extQuery__(1);
			MyAlert("上报成功！");
		}else{
			MyAlert("上报失败！请联系管理员！");
		}
	}
//清除方法
 function clr() {
	document.getElementById('dealerCode').value = "";
  }
	//上报结束
	//上报/删除确认 开始
	function subChecked(action) {
		var str="";
		var chk = document.getElementsByName("orderIds");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++){        
		if(chk[i].checked)
		{            
		str = chk[i].value+","+str; 
		cnt++;
		}
		}
		if(cnt==0){
		        MyAlert("请选择！");
		        return;
		    }else{
		     if(action == 'del'){
		     MyConfirm("确认删除？",punishmentApplyMantainDelete,[str]);
		     
		     }else if(action == 'sub'){
		     MyConfirm("确认上报？",punishmentApplyMantainResport,[str]);
		     
		     }
		    }
		} 
	//上报/删除确认 结束
</script>
<!--页面列表 end -->
</body>
</html>
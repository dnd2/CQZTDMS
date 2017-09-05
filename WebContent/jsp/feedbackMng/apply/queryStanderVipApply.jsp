<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>合格证的补办、更换申请表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}

	//申请类型和操作类型的联动
	function selectAction(){
		if(document.fm.stType.value == '<%=Constant.ORDER_SV_TYPE_01%>') {
			var sc = genSelBoxStrExp('stAction','<%=Constant.ORDER_SV_ACTION%>','',true,'short_sel','','true','<%=Constant.ORDER_SV_ACTION_03%>');
			var pu = document.getElementById("productUnit").innerHTML = sc;
		}else if(document.fm.stType.value == '<%=Constant.ORDER_SV_TYPE_02%>'){
			var sc = genSelBoxStrExp('stAction','<%=Constant.ORDER_SV_ACTION%>','',true,'short_sel','','true','');
			var pu = document.getElementById("productUnit").innerHTML = sc;
		}else{
			var sc = genSelBoxStrExp('stAction','','',true,'short_sel','','true','');
			var pu = document.getElementById("productUnit").innerHTML = sc;
		}
	}
</script>
</head>
<body>

<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理 &gt; 信息反馈提报 &gt;合格证补办更换申请表</div>
  
  <form method="post" name="fm" id="fm">
  
  <!-- 车系列表 -->
  <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
  
   <!-- 查询条件 begin -->
    <table class="table_query" >
          <tr>
            <td width="7%" align="right" nowrap>工单号：</td>
            
            <td colspan="6">
            	<input name="orderId" id="orderId" type="text" size="18"  class="middle_txt" value="" >
            </td>
            <td width="9%" align="right" nowrap>车辆识别码(VIN)：</td>
            <td colspan="2" align="left" >
            	<input type="text" name="vin" size="17" value="" class="middle_txt"/>            
            </td>
          </tr>
          <tr>
            <td align="right" nowrap="nowrap" >工单类型：</td>
            <td colspan="6" nowrap="nowrap"><label>
              <script type="text/javascript">
 				 genSelBoxExp("stType",<%=Constant.ORDER_SV_TYPE%>,"",true,"short_sel","onchange='selectAction()'","false",'');
			  </script>
            </label></td>
            <td align="right" nowrap>操作类型：</td>
 			<td colspan="2" align="left" >
 				<div id="productUnit">
					<select name="stAction" class="short_sel">
						<option value="">-请选择-</option>				
					</select>
				</div>
 			</td>
          </tr>                   
          <tr>
            <td align="right" nowrap="nowrap" >创建时间：</td>
            <td colspan="6" nowrap="nowrap">
            	<div align="left">
            		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            		&nbsp;至&nbsp;
            		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            	</div>
            </td>
            <td align="right" nowrap>车型：</td>
            <td width="19%">
            	<script type="text/javascript">
		              var seriesList=document.getElementById("seriesList").value;
		    	      var str="";
		    	      str += "<select id='vehicleSeriesList' name='vehicleSeriesList' class='short_txt'>";
		    	      str+=seriesList;
		    		  str += "</select>";
		    		  document.write(str);
	       		</script>
            </td>
            <td width="29%" align="right" ></td>
          </tr>
          <tr>
            <td align="right" nowrap>&nbsp;</td>
            <td colspan="6">&nbsp;</td>
            <td align="right" nowrap><input class="normal_btn" type="button" name="queryBtn" id="queryBtn" value="查询"  onClick="__extQuery__(1);">
			<input class="normal_btn" type="button" name="button1" value="新增"  onClick="addStandderVip()"></td>
            <td colspan="2" align="left" >&nbsp;</td>
          </tr>
  </table>
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
  </form> 
  <form name="form1" style="display:none">
   <table class="table_list" id="table1" >
  	  <tr>
  	  	<th align="left">
    		<input class="normal_btn" type="button" value="上 报" onclick="putForwordConfirm()">&nbsp;
    		<input class="normal_btn" type="button" value="删 除" onclick="deleteConfirm()">
       </th>
  	  </tr>
   </table>
  </form>
<!--页面列表 begin -->
<script type="text/javascript" >

	document.form1.style.display = "none";
	
	var HIDDEN_ARRAY_IDS=['form1'];

	var myPage;
//查询路径
	var url = "<%=contextPath%>/feedbackmng/apply/StandardVipApplyManager/queryStandardVip.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderIds\")' />", width:'8%',sortable: false,dataIndex: 'ORDER_ID',renderer:myCheckBox},
				{id:'action',header: "申请单号",sortable: false,dataIndex: 'ORDER_ID',renderer:mySelect ,align:'center'},
				{header: "车辆识别码(VIN)", dataIndex: 'VIN', align:'center'},
				{header: "车型", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "工单类型", dataIndex: 'ST_TYPE', align:'center',renderer:getItemValue},
				{header: "操作类型", dataIndex: 'ST_ACTION', align:'center',renderer:getItemValue},
				{header: "创建时间", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "工单状态", dataIndex: 'ST_STATUS', align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ORDER_ID',renderer:myLink ,align:'center'}
		      ];
		      
//设置超链接  begin      
	
	//修改的超链接
	function myLink(value,meta,record)
	{
		var value = record.data.ORDER_ID;
  		return String.format("<a href='#' onclick='updateStandardVip(\""+ value +"\")'>[修改]</a>");
	}
	
	//修改的超链接设置
	function updateStandardVip(value)
	{
		fm.action = '<%=contextPath%>/feedbackmng/apply/StandardVipApplyManager/updateStandardVipApply.do?orderId=' + value;
	 	fm.submit();
	}
	
	//全选checkbox
	function myCheckBox(value,metaDate,record)
	{
		return String.format("<input type='checkbox' name='orderIds' value='" + value + "'/>");
	}
	
	//设置超链接
	function mySelect(value,meta,record)
	{
  		return String.format("<a href=\"#\" onclick='sel(\""+record.data.ORDER_ID+"\")'>["+ value +"]</a>");
	}
	
	//详细页面
	function sel(value)
	{
		OpenHtmlWindow('<%=contextPath%>/feedbackmng/apply/StandardVipApplyManager/queryStandardVipApplyInfo.do?orderId='+value,800,500);
	}
	
	//跳转新增页面
	function addStandderVip()
	{
		fm.action = "<%=contextPath%>/feedbackmng/apply/StandardVipApplyManager/addStandderVipInit.do";
		fm.submit();
	}
	
	//申报提醒
	function putForwordConfirm()
	{
		var chk = document.getElementsByName("orderIds");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++)
		{        
			if(chk[i].checked)
			{            
				str = chk[i].value+","+str; 
				cnt++;
			}
		}
        if(cnt==0)
        {
             MyAlert("请选择要提报工单！");
             return;
        }
		MyConfirm("确认提报？",putForword);
	}
	
	//提报申请
	function putForword()
	{
		makeNomalFormCall("<%=contextPath%>/feedbackmng/apply/StandardVipApplyManager/putForwordStandardVip.json",showForwordValue,'fm','queryBtn'); 
	}
	
	//提报回调函数
	function showForwordValue(json)
	{
		if(json.returnValue == '1')
		{
			MyAlert("提报成功！");
			__extQuery__(1);
		}else
		{
			MyAlert("提报失败！请联系系统管理员！");
		}
	}
	
	//删除提醒
	function deleteConfirm()
	{
		var chk = document.getElementsByName("orderIds");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++)
		{        
			if(chk[i].checked)
			{            
				str = chk[i].value+","+str; 
				cnt++;
			}
		}
        if(cnt==0)
        {
             MyAlert("请选择要删除工单！");
             return;
        }
		MyConfirm("确认删除？",deleteStanderVip);
	}
	
	//删除操作
	function deleteStanderVip()
	{
		makeNomalFormCall("<%=contextPath%>/feedbackmng/apply/StandardVipApplyManager/deleteStandardVip.json",showDeleteValue,'fm','queryBtn'); 
	}
	
	//删除回调函数
	function showDeleteValue(json)
	{
		if(json.returnValue == '1')
		{
			MyAlert("删除成功！");
			__extQuery__(1);
		}else
		{
			MyAlert("删除失败！请联系系统管理员！");
		}
	}
	
//设置超链接 end
	
</script>
<!--页面列表 end -->


</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>DE接口表运行情况</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">

	function doInit()
	{
   		loadcalendar();  //初始化时间控件
	}

</script>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：系统管理&gt;DE接口监控情况</div>

  <form method="post" name="fm" id="fm">
    <!-- 查询条件 begin -->
  <table class="table_query">
 		<tr>
 		   <td  align="right" nowrap="nowrap">消息编号：</td>
            <td colspan="2" align="left" >
            	<input class="middle_txt" id="msg_id" name="msg_id" value="" type="text" />     	
            </td>
             <td align="left" nowrap>&nbsp;</td>
             <td align="left" nowrap>&nbsp;</td>		
          </tr>  
          <tr>
            <td align="right" nowrap>处理接口： </td>
            <td nowrap>
            	 <input class="middle_txt" id="biz_type" name="biz_type" value="" type="text" />
			</td>
			 <td  align="right" nowrap="nowrap">是否执行：</td>
            <td colspan="2" align="left" >
              <select name="process" id="process">
                  <option value="0" selected="selected">否</option>
                  <option value="1">是</option>
              </select>
             </td> 
          </tr>                    
          <tr> 
           <td align="right" nowrap>起始编号：</td>        
            <td align="left" nowrap>
               <input class="middle_txt" id="msg_from" name="msg_from" value="" type="text" />     	          
             </td>  
             <td align="right" nowrap>目的编号：</td>        
            <td align="left" nowrap>
               <input class="middle_txt" id="msg_to" name="msg_to" value="" type="text" />     	              
            </td>                  
          </tr>  
          <tr>
             <td  align="right" nowrap="nowrap">创建时间： </td>
             <td align="left" nowrap>
              <div align="left">
              		<input name="beginTime" id="t1" value="" type="text" class="short_txt"  group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            		&nbsp;至&nbsp;
            		<input name="endTime" id="t2" value="" type="text" class="short_txt"  group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            		</div>
             </td> 	
              <td align="right" nowrap>
               <input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询"  onclick="__extQuery__(1)"/>&nbsp;&nbsp;&nbsp;&nbsp;
               <input class="normal_btn" type="button" id="queryBtn" name="button1" value="冻结"  onclick="confirmUpdate();"/>
            </td>  
          </tr>    
   </table>
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>

<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
//查询路径
	var url = "<%=contextPath%>/report/jcafterservicereport/DeMonitor/queryDeMonitor.json";
				
	var title = null;

	var columns = [
				{header: "消息ID", dataIndex: 'MSG_ID', align:'center'},
				{header: "起始节点", dataIndex: 'MSG_FROM', align:'center'},
				{header: "目标结点",dataIndex: 'MSG_TO' ,align:'center'},
				{header: "优先级", dataIndex: 'MSG_PRIORITY', align:'center'},
				{header: "处理接口", dataIndex: 'BIZ_TYPE', align:'center'},
				{header: "消息类型", dataIndex: 'MSG_TYPE', align:'center'},
				{header: "数据文件", dataIndex: 'MSG_FILE_ID', align:'center'},
				{header: "是否执行", dataIndex: 'PROCESS', align:'center'},
				{header: "重试次数", dataIndex: 'TRY_TIMES', align:'center'},
				{header: "最后执行时间", dataIndex: 'LAST_TRY_TIME', align:'center'},
				{header: "创建时间", dataIndex: 'CREATE_DATE', align:'center'}
		      ];
  function confirmUpdate() {
   MyConfirm('确定要冻结吗？',confirmUpdate0,[]);
  }
  function confirmUpdate0(){
   var url = '<%=contextPath%>/report/jcafterservicereport/DeMonitor/forstData.json';
   makeNomalFormCall(url,Callback,'fm','');  	
  }
  function Callback(json){
 	 if(json.flag!= null && json.flag== true){
		MyAlert("冻结成功!");
 		}
	 else{
		 MyAlert("冻结失败!");
 	   }
	}
  
  function showMonthFirstDay()     
  {     
	  var Nowdate = new Date();     
	  var MonthFirstDay = new Date(Nowdate.getYear(),Nowdate.getMonth(),1);     
	  return DateUtil.Format("yyyy-MM-dd",MonthFirstDay);  
  }     
  function showMonthLastDay()     
  {     
	  var Nowdate = new Date();     
	  var MonthNextFirstDay = new Date(Nowdate.getYear(),Nowdate.getMonth()+1,1);     
	  var MonthLastDay = new Date(MonthNextFirstDay-86400000);     
	  return DateUtil.Format("yyyy-MM-dd",MonthLastDay);   
  }
  $('beginTime').value=showMonthFirstDay();
  $('endTime').value=showMonthLastDay();
  $('pbeginTime').value=showMonthFirstDay();
  $('pendTime').value=showMonthLastDay();
  $('bbeginTime').value=showMonthFirstDay();
  $('bendTime').value=showMonthLastDay(); 	
</script>
<!--页面列表 end -->
</body>
</html>
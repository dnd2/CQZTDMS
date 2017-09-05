<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>客户投诉查询(服务站)</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
   		selModel();      //取得车系生成下拉框
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 客户投诉管理 &gt;客户投诉查询(服务站)</div>
 <form method="post" name = "fm" >
 <input type="hidden" name="orgId" id="orgId" value="<%=request.getAttribute("orgId")%>"/>
 <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
          <th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />客户查询</th>
		  <tr>
		    <td class="table_query_2Col_label_5Letter">客户姓名：</td>
		    <td><input type='text'  class="middle_txt" name="linkman"  id='linkmanId' value=""/></td>
		    <td class="table_query_2Col_label_4Letter">客户电话：</td>
		    <td><input type='text'  class="middle_txt" name="tel"  id='telId' value=""/></td>
	      </tr>
		  <tr>
		    <td class="table_query_2Col_label_5Letter">投诉时间：</td>
		    <td><input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
            	&nbsp;至&nbsp;
            	<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
            </td>
		    <td class="table_query_2Col_label_4Letter">所购车型：</td>
		    	<td id="aaa">
		    </td>
	      </tr>
		  <tr>
		    <td class="table_query_2Col_label_5Letter">投诉经销商：</td>
		    <td>
		    	<input class="middle_txt" id="compDealerCode" name="compDealerCode" value="" type="text"/>
            	<input class="mark_btn" type="button" value="&hellip;" onclick="showOrgDealer('compDealerCode','compDealerId','','false',true)"/> 
            	<input class="cssbutton" type="button" value="清空" onclick="clrDlr()"/> 
            	<input type="hidden" name="compDealerId" id="compDealerId" value=""/>    
		    </td>
		    <td class="table_query_2Col_label_4Letter">投诉编号：</td>
		    <td><input type='text'  class="middle_txt" name="compCode"  id='compCode' value=""/></td>
	      </tr>
		  <tr>
            <td class="table_query_2Col_label_5Letter">投诉来源：</td>
            <td><script type="text/javascript">
            	genSelBoxExp("compSource",<%=Constant.COMP_SOURCE_TYPE%>,"",true,"short_sel","","false",'');
                </script>
            </td>
            <td class="table_query_2Col_label_4Letter">投诉等级：</td>
            <td><script type="text/javascript">
            	genSelBoxExp("compLevel",<%=Constant.COMP_LEVEL_TYPE%>,"",true,"short_sel","","true",'');
                </script>
            </td>
          </tr>
          <tr>
            <td class="table_query_2Col_label_5Letter">投诉类型：</td>
            <td>
            	<script type="text/javascript">
            	genSelBoxExp("compType",<%=Constant.COMP_TYPE_TYPE%>,"",true,"min_sel","onchange='selType()'","true",'');
                </script>
            	<span id="compTypechng">
            	<select name="compType2" class="min_sel">
				<option value="">-请选择-</option>				
			    </select>
            	</span>
            </td>
            <td class="table_query_2Col_label_4Letter">投诉状态：</td>
            <td>
            	<script type="text/javascript">
            	genSelBoxExp("compStatus",<%=Constant.COMP_STATUS_TYPE%>,"",true,"min_sel","onchange='selectAction()'","true",'<%=Constant.COMP_STATUS_TYPE_01%>');
                </script>
                  处理中状态：
                <span id="statuschng">
                <select name="auditResult" class="min_sel">
				<option value="">-请选择-</option>				
			    </select>
			    </span>
            </td>
          </tr>
          <tr>
           	<td class="table_query_2Col_label_5Letter">车牌号：</td>
		    <td><input type='text'  class="middle_txt" name="licenseNo"  id='licenseNo' value=""/></td>
          </tr>
          <tr>
          <td>&nbsp;&nbsp;&nbsp;</td>
          <td align="right"><input name="searchBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查询" /></td>
          <td>&nbsp;&nbsp;&nbsp;</td>
		  <td>&nbsp;&nbsp;&nbsp;</td>
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
	var url = "<%=contextPath%>/customerRelationships/search/ComplaintSearch/queryComplaintDealer.json";
				
	var title = null;

	var columns = [
				{header: "投诉编号", dataIndex: 'COMP_CODE', align:'center'},
				{header: "客户名称",sortable: false,dataIndex: 'LINK_MAN',align:'center'},
				{header: "联系电话", dataIndex: 'TEL', align:'center'},
				{header: "所购车型", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "车牌号", dataIndex: 'LICENSE_NO', align:'center'},
				{header: "大区", dataIndex: 'ORG_NAME', align:'center'},
				{header: "省份", dataIndex: 'PROVINCE', align:'center',renderer:getRegionName},
				{header: "投诉经销商", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "投诉来源", dataIndex: 'COMP_SOURCE', align:'center',renderer:getItemValue},
				{header: "投诉等级", dataIndex: 'COMP_LEVEL', align:'center',renderer:getItemValue},
				{header: "投诉类型", dataIndex: 'COMP_TYPE', align:'center',renderer:getItemValue},
				{header: "投诉时间", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "投诉状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "处理结果", dataIndex: 'AUDIT_RESULT', align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'COMP_ID',renderer:myLink ,align:'center'}
		      ];
		      
//设置超链接  begin      
	
	//处理的超链接
	function myLink(value,meta,record){
  		return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='viewDetail(\""+ value +"\")' value='明细'/>");
	}
	
	//处理的ACTION设置
	function viewDetail(value){
		fm.action = '<%=contextPath%>/customerRelationships/search/ComplaintSearch/viewComplaintDetail.do?compId=' + value;
	 	fm.submit();
	}
	
	
	//动态显示投诉状态和处理状态的联动
	function selectAction(){
		if(document.fm.compStatus.value == '<%=Constant.COMP_STATUS_TYPE_02%>') {
			var sc = genSelBoxStrExp('auditResult','<%=Constant.AUDIT_RESULT_TYPE%>','',true,'min_sel','','true','');
			var pu = document.getElementById("statuschng").innerHTML = sc;
		}else{
			var sc = genSelBoxStrExp('auditResult','','',true,'min_sel','','true','');
			var pu = document.getElementById("statuschng").innerHTML = sc;
		}
	}
	
	
	// 投诉类型的联动显示
function selType(){
	if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_01%>') {
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_QUALITY%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
			
		}else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_02%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_MEDIUM_COMPLAINS%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_03%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_SERVES%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}
		else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_04%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_ACCESSORIES%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}
		else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_05%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SALE_CONSULTS%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}
		else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_06%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SALE_COMPLAINS%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}
		else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_07%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_HELPS%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}
		else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_08%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_SERVES_DEALER%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}
		else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_09%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_ACTIVITY%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}else if(document.fm.compType.value == '<%=Constant.COMP_TYPE_TYPE_10%>'){
			var sc = genSelBoxStrExp('compType2',<%=Constant.SEDAN_OTHER_PROBLEMS%>,"",false,"min_sel","","false",'');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		
		}else{
			var sc = genSelBoxStrExp('compType2','','',true,'min_sel','','true','');
			var pu = document.getElementById("compTypechng").innerHTML = sc;
		}
}
	// 取得所有车系
	function selModel()
	{
		makeNomalFormCall('<%=contextPath%>/customerRelationships/complaint/ComplaintDisposalOEM/getModel.json',showModelBack,'fm');
	}
	
	// 通过传回的查询结果生成下拉框
	function showModelBack(json)
	{
		var seriesList=json.seriesList
    	var str="";
    	str += "<select id='modelCode' name='modelCode' class='short_txt'>";
    	str +=seriesList;
    	str += "</select>";
    	document.getElementById("aaa").innerHTML = str;
	}  
	
	//清空按钮
	function clrDlr(){
		document.getElementById("compDealerId").value="";
		document.getElementById("compDealerCode").value="";
	}
	
//设置超链接 end
	
</script>
<!--页面列表 end -->
</body>
</html>

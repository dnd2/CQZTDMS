<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>

<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>产地维护</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  系统管理 &gt; 系统业务参数维护 &gt; 产地查询</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
		<table class="table_query" border="0">
			<tr>
				<td class="right"><div align="right">产地代码：</div></td>
				<td>
      				<input type="text" id="area_code" class="middle_txt" name="area_code" datatype="1,is_textarea,10" />
    			</td>
    			<td class="right"><div align="right">产地名称：</div></td>
				<td>
      				<input type="text" id="area_name" class="middle_txt" name="area_name" datatype="1,is_textarea,60" />
    			</td>
			</tr>
			<tr>
			<td width="20%" class="tblopt"><div align="right" style="display:none">生产基地：</div></td>
      		<td align="left" >
				<label style="display:none">
					<script type="text/javascript">
						genSelBoxExp("produce_base",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
    		</td>
			<td class="table_query_3Col_input" >
					<input type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> 
					<!-- <input type="button" class="normal_btn" onclick="toAddBusArea();" value="新 增" id="queryBtn" />  -->
					<input type="hidden" id="up_area_short" name="up_area_short" />
					<input type="hidden" id="up_area_code" name="up_area_code" />
					<input type="hidden" id="up_area_name" name="up_area_name" />
					<input type="hidden" id="up_status" name="up_status" />
					<input type="hidden" id="up_produce_base" name="up_produce_base" />
				</td>
			</tr>
		</table>
		</div>
		</div>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
<script type="text/javascript" >

	var myPage;
	
	var url = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/BusinessAreaManage/businessAreaList.json?COMMAND=1";
	
	var title = null;

	var columns = [
				/* {id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'AREA_ID',renderer:myLink}, */
				{header: "产地代码", dataIndex: 'AREA_CODE', align:'center',width:'40%'},
				{header: "产地名称", dataIndex: 'AREA_NAME', align:'center',width:'40%'},
				//{header: "生产基地", dataIndex: 'PRODUCE_BASE', align:'center',width:'40%',renderer:getItemValue},
				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue}
		      ];

	function myLink(area_id,metaDate,record){
		var data = record.data;
        return String.format(
        		 "<a href=\"#\" onclick=toUpdate('"+area_id+"','"+data.AREA_CODE+"','"+data.STATUS+"','"+data.PRODUCE_BASE+"','" + data.AREA_SHORTCODE + "');>[修改]</a>");
    }

    function toUpdate(area_id,area_code,status,produce_base,area_short){
        document.getElementById("up_area_code").value=area_code;
        // document.getElementById("up_area_name").value=area_name;
        
        var tab = document.getElementById("myTable");

		for(var i=1; i < tab.rows.length; i++)
		{
			var checkObj = tab.rows[i].cells[0].innerHTML;
			if(checkObj ==  area_code)
			{
				document.getElementById("up_area_name").value=tab.rows[i].cells[1].innerHTML;
			}
		}
		document.getElementById("up_area_short").value=area_short;
        document.getElementById("up_status").value=status;
        document.getElementById("up_produce_base").value=produce_base;
       
    	fm.action = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/BusinessAreaManage/toEditBusinessAreaInfo.do?area_id="+area_id;
		fm.submit();
    }

    function toAddBusArea(){
    	fm.action = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/BusinessAreaManage/toAddBusArea.do";
		fm.submit();
    }

    function toDelete(area_id){
    	MyConfirm("是否删除?",deleteAction,[area_id]);
    }
    function deleteAction(area_id){
    	makeNomalFormCall('<%=contextPath%>/sysbusinesparams/businesparamsmanage/BusinessAreaManage/deleteArea.json?area_id='+area_id,showResult,'fm');
	}
	
	function showResult(json){
		turnQuery();
	}
	
	function turnQuery() {
		 __extQuery__(1);
		
	}
	    
 </script>    
</body>
</html>
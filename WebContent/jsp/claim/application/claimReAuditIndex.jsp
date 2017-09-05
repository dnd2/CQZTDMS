<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>索赔申请重新审核作业</title>
	<script type="text/javascript">
	    function doInit()
		{
		   loadcalendar();
		}
	    function genSelBoxExpZ(id,type,selectedKey,setAll,_class_,_script_,nullFlag,expStr){
	    	var str = "";
	    	var arr;
	    	if(expStr.indexOf(",")>0)
	    		arr = expStr.split(",");
	    	else {
	    		expStr = expStr+",";
	    		arr = expStr.split(",");
	    	}
	    	str += "<select id='" + id + "' name='" + id +"' class='"+ _class_ +"' " + _script_ ;
	    	// modified by lishuai@infoservice.com.cn 2010-05-18 解决select下拉框中增加属性datatype判断bug begin
	    	if(nullFlag && nullFlag == "true"){
	    		str += " datatype='0,0,0' ";
	    	}
	    	// end
	    	str += " onChange=doCusChangeZ(this.value);> ";
	    	if(setAll){
	    		str += genDefaultOpt();
	    	}
	    	for(var i=0;i<codeData.length;i++){
	    		var flag = true;
	    		for(var j=0;j<arr.length;j++){
	    			if(codeData[i].codeId == arr[j]){
	    				flag = false;
	    			}
	    		}
	    		if(codeData[i].type == type && flag){
	    			str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '"+codeData[i].codeDesc+"' >" + codeData[i].codeDesc + "</option>";
	    		}
	    	}
	    	str += "</select>";	
	    	document.write(str);
	    }
	    	    function doCusChangeZ(obj){
	    			if(obj==<%=Constant.CLA_TYPE_07%>&&$('CODE_ID').value==<%=Constant.chana_wc%>){
	    				$('show_id').style.display="block";
	    			}else{
	    				$('show_id').style.display="none";
	    			}
	    				
	    		
	    		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔申请&gt;索赔申请重新审核
</div>
<form method="post" name="fm">
<table align="center" class="table_query">
	<tr>
		<td align="right" nowrap="true">经销商代码：</td>
		<td align="left" nowrap="true">
			<input type="hidden" id="CODE_ID" value="${codeId }"/>
			<input class="middle_txt" id="dealerCode"  name="dealerCode" type="text" readonly="readonly"/>
            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/>
		</td>
		<td align="right" nowrap="true">经销商名称：</td>
		<td align="left" nowrap="true">
			<input type="text" name="DEALER_NAME" id="DEALER_NAME" datatype="1,is_digit_letter_cn,30" class="middle_txt" value=""/>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">索赔单号：</td>
        <td>
        	<input name="CLAIM_NO" value="" type="text" class="middle_txt"/>
        </td>
		<td align="right" nowrap="true">索赔类型：</td>
		<td align="left" nowrap="true">
			<script type="text/javascript">
				 genSelBoxExpZ("CLAIM_TYPE",<%=Constant.CLA_TYPE%>,"",true,"short_sel","","true",'');
		    </script>
		</td>
	</tr>
	<tr id="show_id" style="display: none;">
	    <td align="right" nowrap="true"></td>
	    <td align="left" nowrap="true"></td>
		<td align="right" nowrap="true">质损类型:</td>
		<td align="left" nowrap="true">
			<script type="text/javascript">
			genSelBoxExp("CLAIM_ZHISHUN",<%=Constant.CLAIM_ZHISHUN%>,"",true,"short_sel","false","false","");
			</script>
		</td>
	</tr>
	<tr>
	    <td align="right" nowrap="true">工单号：</td>
		<td align="left" nowrap="true">
			<input name="CON_RO_NO" class="middle_txt" type="text" value="" />
			<input name="CON_LINE_NO" id="CON_LINE_NO" value="" type="hidden" datatype="1,is_digit,3" class="mini_txt"/>
		</td>
		<td align="right" nowrap="true" rowspan="2">VIN：</td>
		<td align="left" rowspan="2">
			<textarea name="CON_VIN" rows="3" cols="18"></textarea>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">申请日期：</td>
		<td align="left" nowrap="true">
			<input type="text" name="CON_APPLY_DATE_START" id="CON_APPLY_DATE_START"
             value="" type="text" class="short_txt" 
             datatype="1,is_date,10" group="CON_APPLY_DATE_START,CON_APPLY_DATE_END" 
             hasbtn="true" callFunction="showcalendar(event, 'CON_APPLY_DATE_START', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="CON_APPLY_DATE_END" id="CON_APPLY_DATE_END" 
 			value="" type="text" class="short_txt" datatype="1,is_date,10" 
 			group="CON_APPLY_DATE_START,CON_APPLY_DATE_END" 
 			hasbtn="true" callFunction="showcalendar(event, 'CON_APPLY_DATE_END', false);"/>
		</td>
	</tr>
	<tr>
	    <td align="right" nowrap="true">产地：</td>
	    <td align="left" >
	      <script type="text/javascript">
	      	genSelBoxContainStr("YIELDLY",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'<%=CommonUtils.checkNull(request.getAttribute("yieldly"))%>');
	      </script>
	    </td>
		<td  class="table_query_2Col_label_6Letter">申请单状态：</td>
 		<td >
 			<script type="text/javascript">
 				genSelBoxContainStr("STATUS",<%=Constant.CLAIM_APPLY_ORD_TYPE%>,"",true,"short_sel","","true",'<%=Constant.CLAIM_APPLY_ORD_TYPE_03%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_04%>');
       		</script>
       </td>
    </tr>
    <tr>
    	<td align="right">审核时间：</td>
    	<td>
    		<input type="text" name="approveDate" id="approveDate"
             value="" type="text" class="short_txt" 
             datatype="1,is_date,10" group="approveDate,approveDate2" 
             hasbtn="true" callFunction="showcalendar(event, 'approveDate', false);"/>
             &nbsp;至&nbsp;
 			<input type="text" name="approveDate2" id="approveDate2" 
 			value="" type="text" class="short_txt" datatype="1,is_date,10" 
 			group="approveDate,approveDate2" 
 			hasbtn="true" callFunction="showcalendar(event, 'approveDate2', false);"/>
    	</td>
    	<td align="right">授权人：</td>
    	<td>
    		<input type="text" class="middle_txt" name="approveName"/>
    	</td>
    </tr>
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<script type="text/javascript">
		var myPage;
		var url = "<%=contextPath%>/claim/application/ClaimManualAuditing/claimManualReAuditingQuery.json";
		var title = null;
		
		var columns = [
					{header: "序号",sortable: false,align:'center',renderer:getIndex},
					{header: "经销商代码",sortable: false,dataIndex: 'DEALER_CODE',align:'center'},
					{header: "经销商名称",sortable: false,dataIndex: 'DEALER_NAME',align:'center'},	
					{header: "索赔申请单号",dataIndex: 'CLAIM_NO',renderer:claimDetail},			
					{header: "工单号",dataIndex: 'RO_NO'},
					{header: "索赔类型",sortable: false,dataIndex: 'CLAIM_TYPE',align:'center',renderer:getItemValue}, 
					{header: "VIN",sortable: false,dataIndex: 'VIN',align:'center'},
					{header: "索赔金额(元)",sortable: false,dataIndex: 'REPAIR_TOTAL',align:'right',renderer:formatCurrency},
					{header: "申请日期",sortable: false,dataIndex: 'CREATE_DATE',align:'center'},
					{header: "申请状态",sortable: false,dataIndex: 'STATUS',align:'center',renderer:getItemValue},
					{header:'授权人',dataIndex:'AUTH_PERSON',align:'center'},
					{header:'审核时间',dataIndex:'AUDITING_DATE',align:'center',renderer:formatDate},
					{id:'action',header: "操作",dataIndex: 'ID',renderer:claimAuditing}
			      ];
	      
		//索赔审核
	    function claimAuditing(value,meta,record){
	    	return String.format("<a href=\"#\" onclick=auditingClaim("+value+",this) >[审核]</a>");
	    }
	  //格式化时间为YYYY-MM-DD
		function formatDate(value,meta,record) {
			 if(record.data.STATUS =='<%=Constant.CLAIM_APPLY_ORD_TYPE_03%>'){
					return '';
				}else if (value==""||value==null) {
				return "";
			}else {
				return value.substr(0,10);
			}
		}

	    //转入审核页面
		function auditingClaim(value,aobj){
			var tarUrl = '<%=contextPath%>/claim/application/ClaimManualAuditing/claimAuditingPage.do?isAudit=true&isReAuditing=true&ID='+ value;
            //不采用OpenHtmlWindow方式
			//aobj.href = tarUrl;
			//aobj.click();
			var width=900;
			var height=500;

			var screenW = window.screen.width-30;	
			var screenH = document.viewport.getHeight();

			if(screenW!=null && screenW!='undefined')
				width = screenW;
			if(screenH!=null && screenH!='undefined')
				height = screenH;
			
			OpenHtmlWindow(tarUrl,width,height);
			//makeNomalFormCall('<%=contextPath%>/claim/application/ClaimBillStatusTrack/claimBillDetailForward.do?isAudit=true&ID='+ value,showResult,'fm');
			//disableLink(aobj,true);
		}

		//显示审核后的数据
		function showResult(json){
			//空
		}
		
        //清空经销商框
		function clearInput(){
			var target = document.getElementById('dealerCode');
			target.value = '';
		}

        //控制表单元素是否显示
		function disableLink(obj,show){
			MyAlert(obj.innerHTML);
			if(!show){
				obj.disabled = true;
				obj.style.border = '1px solid #999';
				obj.style.background = '#EEE';
				obj.style.color = '#999';
			}else{
				obj.disabled = false;
				obj.style = '';
			}
		}

		//索赔抵扣明细
		function claimDetail(value,meta,record){
	  		return String.format("<a href=\"#\" onclick=\"queryDetail("+record.data.ID+")\">[" + value + "]</a>");
		}

		//查询索赔单明细
		function queryDetail(id){
			var tarUrl = "<%=contextPath%>/claim/application/ClaimBillStatusTrack/claimBillDetailForward.do?ID="+id;
			var width=900;
			var height=500;
			var screenW = window.screen.width-30;	
			var screenH = document.viewport.getHeight();
			if(screenW!=null && screenW!='undefined')
				width = screenW;
			if(screenH!=null && screenH!='undefined')
				height = screenH;
			
			OpenHtmlWindow(tarUrl,width,height);
		}
		 $('show_id').style.display="none";
	</script>
</body>
</html>
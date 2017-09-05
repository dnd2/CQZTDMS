<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<% String contextPath = request.getContextPath(); %>
<%@page import="com.infodms.dms.bean.TtAsWrGameBean"%>

<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.TtAsWrRulePO"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.TtAsWrGamefeePO"%><html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>三包策略明细维护(保养费用、车型和省份)</title>
		<script type="text/javascript">
		    function doInit()
			{
			   loadcalendar();
			}
		</script>
	</head>
<body onload="doInit();">
<div class="wbox">
	<div class="navigation">
  		<img src="<%=contextPath%>/img/nav.gif" />当前位置：售后服务管理&gt;索赔基础数据&gt;三包策略维护
  	</div>
<% 
	TtAsWrGameBean gamePO = (TtAsWrGameBean)request.getAttribute("gamePO");
%>
	<form method="post"  name="fm" id="fm">
	    <table border="0" width="100%">
	    <tr>
		    <td>
			    <!-- 三包策略基本信息  -->
			    <div class="form-panel">
					<h2>基本信息</h2>
			<div class="form-body">
				<!-- 三包策略基本信息  -->
				<table class="table_edit" id="baseTab">
					<tr>
						<td style="text-align:right">三包策略类型：</td>
						<td>
							<script type="text/javascript">
								document.write(getItemValue(<%=gamePO.getGameType()%>));
							</script>
						</td>
						<td style="text-align:right">三包策略状态：</td>
						<td>
							<script type="text/javascript">
								 genSelBoxExp("STATUS",<%=Constant.STATUS%>,"<%=CommonUtils.checkNull(gamePO.getGameStatus())%>",false,"","","false",'');
						    </script>
						</td>
						<td style="text-align:right">三包策略代码：</td>
						<td>
						<%=CommonUtils.checkNull(gamePO.getGameCode())%>
							<input type='hidden' name='STRATEGY_CODE' id='STRATEGY_CODE'  readonly="readonly"
							class="middle_txt" datatype="1,is_null,20" 
							value="<%=CommonUtils.checkNull(gamePO.getGameCode())%>"/>
						</td>
					</tr>
					<tr>
						<td style="text-align:right">三包策略名称：</td>
						<td>
							<input type='text' name='STRATEGY_NAME' id='STRATEGY_NAME'
							class="middle_txt" datatype="0,is_null,60"
							value="<%=CommonUtils.checkNull(gamePO.getGameName())%>"/>
						</td>
						<td style="text-align:right">三包规则：</td>
						<td >
						    <select name="GUARANTEE_RULE_ID" id="GUARANTEE_RULE_ID" 
						    	class="u-select">
							<% 
								List dataList = (List)request.getAttribute("ruleList");
								if(dataList!=null && dataList.size()>0){
							%>
							<%
									for(int i=0;i<dataList.size();i++){
										TtAsWrRulePO rulePO = (TtAsWrRulePO)dataList.get(i);
							%>
									<option value="<%=rulePO.getId()%>" <%=rulePO.getId().equals(gamePO.getRuleId())?"selected":""%>><%=rulePO.getRuleName()%></option>
							<%		}%>
							<% } else { %>
								<option value="">请先维护业务类型三包规则</option>
							<% } %>
							</select>
						</td>
						<td style="text-align:right">三包策略开始时间：</td>
						<td>
							<input name="START_TIME" id="START_TIME" readonly class="Wdate" type="text" value="<%=CommonUtils.checkNull(gamePO.getStartTime())%>" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'END_TIME\')}'})"/>
							<%-- <input type="text" name="START_TIME" id="START_TIME"
				             value="<%=CommonUtils.checkNull(gamePO.getStartTime())%>" type="text" class="short_txt" 
				             datatype="0,is_date,10" group="START_TIME,END_TIME" 
				             hasbtn="true" readonly />
				             <input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'START_TIME', false);" />  	 --%>
						</td>
					</tr>
					<tr>
						<td style="text-align:right">三包策略结束时间：</td>
						<td>
							<input name="END_TIME" id="END_TIME" readonly class="Wdate" type="text" value="<%=CommonUtils.checkNull(gamePO.getEndTime())%>" onclick="WdatePicker({minDate:'#F{$dp.$D(\'START_TIME\')}'})"/>
							<%-- <input type="text" name="END_TIME" id="END_TIME"
					             value="<%=CommonUtils.checkNull(gamePO.getEndTime())%>" type="text" class="short_txt" 
					             datatype="0,is_date,10" group="START_TIME,END_TIME" 
					             hasbtn="true" readonly />
					             <input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'END_TIME', false);" />  	 --%>
						</td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
				    <tr>
				    	<td style="text-align:right">车辆性质:</td>
				    	<td >
							<textarea rows="3" cols="25" name="codeDesc" readonly="readonly" id="codeDesc" onclick='open_conent()'>${gamePO.vehicleProBusiDesc}</textarea><span style="color:red">*</span>
							<input type="hidden" datatype="0,is_null,98"  readonly="readonly" name='code_busi' id="code_busi" value="${gamePO.vehicleProBusi}"  />
							<input type="hidden" datatype="0,is_null,98"  readonly="readonly" name='code_id' id="code_id" value="${gamePO.vehicelProperty}"  />
							<!-- <input name='showBtn' type='button' class='mini_btn' style='cursor: pointer;' onclick='open_conent()' value='...' /> -->
						</td>
						
							
				    </tr>
				    
					<tr>
						<td style="text-align:right" valign="top">备注：</td>
						<td colspan="5">
							<textarea name="REMARK" id="REMARK" rows="3" cols="80"><%=CommonUtils.checkNull(gamePO.getRemark())%></textarea>
						</td>
					</tr>
					<tr>
									<td colspan="6" style="text-align:center">
										    <!-- 三包策略ID -->
										    <input type="hidden" name="GUARANTEE_COUNT" value="1" />
					       		            <input type="hidden" name="ID" value="<%=gamePO.getId()%>"/>
											<input type="button" name="bt_add" class="normal_btn"  id="bt_add"
											     onclick="updateGuaranteeConfirm();" value="修改"/>&nbsp;&nbsp;
											<input type="button" name="bt_back" class="normal_btn" id="bt_back"
											    onclick="backTo();" value="返回"/>
									</td>
								</tr>
				</table>
				</div>
				</div>
			</td>
		</tr>
        <% if(Constant.GAME_TYPE_02.equals(gamePO.getGameType())) {%>
        <tr>
	        <td>
				<div class="form-panel">
					<h2>车型信息</h2>
			<div class="form-body">
				<table width="100%" id="modelTab" style="text-align:center">	
					<tr>
						<td colspan="6">
							<iframe frameborder="0" scrolling="yes" style="offsetLeft:0;offsetWidth:0;OVERFLOW:SCROLL;OVERFLOW-Y:HIDDEN" id="carModelFrame"
							 src="<%=contextPath%>/jsp/claim/basicData/guaranteesStrategyModelAdd.jsp?ID=<%=gamePO.getId()%>" name="carModelFrame" width="100%">
							</iframe>
				        </td>
					</tr>
				</table>
				</div>
				</div>
			</td>
		</tr>
		<%} %>
		</table>
	</form>	
	</div>
	<script type="text/javascript">
	    //控制是否显示TABLE
		function showTab(tableId){
			var tableVar = document.getElementById(tableId);
			var displayContent = tableVar.style.display;
			if(displayContent!='' && displayContent!=null && displayContent=='none'){
				tableVar.style.display = '';
			}else{
				tableVar.style.display = 'none';
			}
		}

		//确认保存保养费用
		function updateGuaranteeConfirm(){
			var remarkValue = document.getElementById("REMARK").value;
			if(remarkValue && remarkValue.length>500){
				MyAlert("备注信息长度不能超过 500!");
				return;
			}
			if($('#codeDesc')[0].value==""){
				MyAlert("请选择车辆性质!");
				return;
			}
			if(submitForm('fm')){//查询表单数据格式
				//var nowDate = (new Date()).Format("yyyy-MM-dd");
			    //var startTime = document.getElementById('START_TIME').value;
			    //if(!checkDate(startTime,nowDate)){//选择三包策略开始时间必须大于当前时间    
			        MyConfirm("是否确认修改？", updateGuarantee) ;
			        	
					//MyConfirm("是否确认修改？",updateGuarantee,[null]);
			    //}else{
				   // MyAlert("三包策略开始日期必须大于当前日期！");
			    //}
			}
		}

		//保存保修费用
		function updateGuarantee(){
		$('#bt_add')[0].disabled=true;
		$('#bt_back')[0].disabled=true;
			url="<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/updateStrategy.json"
			makeNomalFormCall(url,backDeal,'fm','');
		//	var frm = document.getElementById('fm');
			//frm.submit();
		}

		//保存后处理
		function backDeal(json){
			var msg=json.msg;
			if(msg==""){
				MyAlert("修改成功!");
				var frm = document.getElementById('fm');
				frm.action="<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/strategyQueryInit.do";
				frm.submit();
			}else{
				MyAlert(msg);
				$('#bt_add')[0].disabled=false;
				$('#bt_back')[0].disabled=false;
			}
		}
function backTo(){
			var frm = document.getElementById('fm');
				frm.action="<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/strategyQueryInit.do";
				frm.submit();
}
		function historyBack(){
			if(parent.${'inIframe'}){
				try{
				//location.href='<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/strategyQueryInit.do';
				parent.window._hide();
				parent.${'inIframe'}.__extQuery__(1);
				}catch(e){
					location.href='<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/strategyQueryInit.do';
				}
			}else{
				window.close();
			}
		}
       function open_conent(val)
       {
            var code_id= document.getElementById('code_id').value;code_busi
            var code_busi= document.getElementById('code_busi').value;
       	   OpenHtmlWindow('<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/add_code.do?code_id='+code_id+'&code_busi='+code_busi,600,300);
       }

		//动态生成保养费用列表
		function dyncCreateGuarante(tbodyId,count){
			var varTbodyId = document.getElementById(tbodyId);
			var guaranteCount = count.value;
			if(varTbodyId!=null && varTbodyId!='undefined'){
				var content = '';
				for(var i=0;i<guaranteCount;i=i+2){
					var setguaranteValue = getHiddenSetGuaranteValue('HIDEN_GUARANTEE_AMOUNT'+i);
					content = content + getDyncContent(i,setguaranteValue);
			        if((i+1)<guaranteCount){
			        	var setguaranteValue2 = getHiddenSetGuaranteValue('HIDEN_GUARANTEE_AMOUNT'+(i+1));
			        	content = content + getDyncContent((i+1),setguaranteValue2);
			        }
					content = "<tr>" + content + "</tr>";
				}
				varTbodyId.innerHTML = '<table class="table_edit">' + content + '</table>';
				for(var i=0;i<guaranteCount;i++){
					setMustStyle([document.getElementById('GUARANTEE_AMOUNT'+i)]);
				}
			}
		}

		function getDyncContent(index,value){
			var content = '<td align="right">第'+(index+1)+'次保养费用(元)：</td>'+
			'<td align="left">'+
				'<input type="text" datatype="0,is_double,20" decimal="2" name="GUARANTEE_AMOUNT'+index+'" id="GUARANTEE_AMOUNT'+index+'"'+' value="'+value+'"/>'+
			'</td>';
			return content;
		}

		//取得已经设定的保养费用
		function getHiddenSetGuaranteValue(inputId){
			var res = '';
			var setguar = document.getElementById(inputId);
			if(setguar!=null && setguar!='undefined')
				res = setguar.value;
			return res;
		}

	</script>
</body>
</html>
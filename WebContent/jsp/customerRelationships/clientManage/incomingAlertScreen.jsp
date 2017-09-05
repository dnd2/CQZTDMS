<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<style type="text/css">
.tab{margin-top:50px;}

#TabTab03Con1{width:1050px;height:340px;line-height:100%;border-left:#757575 1px solid;border-bottom:#757575 1px solid;border-right:#757575 1px solid;}
#TabTab03Con2{width:1050px;height:340px;line-height:100%;border-left:#757575 1px solid;border-bottom:#757575 1px solid;border-right:#757575 1px solid;}
#TabTab03Con3{width:1050px;height:340px;line-height:100%;border-left:#757575 1px solid;border-bottom:#757575 1px solid;border-right:#757575 1px solid;}

.xixi1{width:346px;height:27px;line-height:27px;background-image:url("../../../img/tab_head1.jpg");cursor:pointer;}
.xixi2{width:346px;height:27px;line-height:27px;background-image:url("../../../img/tab_head2.jpg");cursor:pointer;}
.xixi3{width:346px;height:27px;line-height:27px;background-image:url("../../../img/tab_head3.jpg");cursor:pointer;}

.tab1{width:115px;height:27px;line-height:27px;float:left;text-align:center;cursor:pointer;}
.tab2{width:115px;height:27px;line-height:27px;float:left;text-align:center;cursor:pointer;color:#FFFFFF;}
.tab3{width:115px;height:27px;line-height:27px;float:left;text-align:center;cursor:pointer;color:#FFFFFF;}
</style>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
	String contextPath = request.getContextPath();
%>
<title>来电弹屏</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}

</script>
</head>
<body onload="__extQuery__(1);">
	
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 客户管理 &gt;客户信息查询</div>		
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />客户信息查询</th>
			<tr>
				<td align="right" nowrap="true">来电号码：</td>
				<td align="left" nowrap="true">
					<input type="text" id="txtTel" value="${strCallid}" name="incomeTep"/>
				</td>
				<td align="right" nowrap="true">所属地区：</td>
				<td align="left" nowrap="true">
				<input type="hidden"  id='SPROVINCE' />
					${strInfo}
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">底盘号：</td>
				<td align="left" nowrap="true">
					<input type="text" id="vinNo" name="vinNo"/>
				</td>
				<td align="right" nowrap="true">客户姓名：</td>
				<td align="left" nowrap="true">
					<input type="text" id="name" name="name"/>
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">联系电话：</td>
				<td align="left" nowrap="true">
					<input type="text" id="telephone" name="telephone"/>
				</td>
			</tr>
			<tr>
				<td colspan="8" align="center">
					<input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="__extQuery__(1);" />
					<input id="Pickup" class="normal_btn" onclick="AT_Command('Pickup');updateStatus('Pickup');" value="接听" type="button" name="queryBtn" />
					 <input id="PlaceCall" class="cssbutton" onclick="AT_Command('PlaceCall');updateStatus('PlaceCall');" value="拨号" type="button" name="queryBtn" />
        		</td>
			</tr>
		</table>
		<div>
	 <!-- 查询条件 end -->
	 <!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	 <!--分页 end -->
	 	<input class="normal_btn" type="button" value="受理投诉咨询" name="complaitAccepte" id="complaitAccepte" onclick="complaitAccepting();" style="display: none;width: 90px"/>
	 	
	</form>
	
	<div id="tabDiv" class="tab" style="display: none" width="100%">
		<div id="bg" class="xixi1">
			<div id="font1" class="tab1" onMouseOver="setTab03Syn(1);document.getElementById('bg').className='xixi1'"  >基本信息</div>
			<div id="font2" class="tab2" onMouseOver="setTab03Syn(2);document.getElementById('bg').className='xixi2'" >车辆维修记录</div>
			<div id="font3" class="tab3" onMouseOver="setTab03Syn(3);document.getElementById('bg').className='xixi3'" >投诉咨询记录</div>
			
		</div>
	    <div id="TabTab03Con1">
	    	<form method="post" name = "baseInfo" id="baseInfo">
				<table id="base"  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query" >					
					<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
					<tr>
						<td align="right" nowrap="true">客户名称：</td>
						<td align="left" nowrap="true">
							<input type="text" id="ctmname" name="ctmname"/>
						</td>
						<td align="right" nowrap="true">客户类型：</td>
						<td align="left" nowrap="true">
							<select id="ctmtype" name="ctmtype" class="short_sel">
								<option value=''>-请选择-</option>
							</select>
						</td>
						<td align="right" nowrap="true">用途：</td>
						<td align="left" nowrap="true">
							<select id="purpose" name="purpose" class="short_sel">
								<option value=''>-请选择-</option>
							</select>
						</td>
					</tr>
					
					<tr>
						<td align="right" nowrap="true">身份证号：</td>
						<td align="left" nowrap="true">
							<input type="text" id="cardNum" name="cardNum"/>
						</td>
					</tr>
		
					<tr>
						<td align="right" nowrap="true">省份：</td>
						<td align="left" nowrap="true">
							<select id="province" name="province" class="short_sel" onchange="changeCityEvent(this.value,'',false)">
								<option value=''>-请选择-</option>
							</select>
						</td>
						<td align="right" nowrap="true">城市：</td>
						<td align="left" nowrap="true">
							<select id="city" name="city" class="short_sel">
								<option value=''>-请选择-</option>
							</select>
						</td>
						<td align="right" nowrap="true">邮编：</td>
						<td align="left" nowrap="true">
							<input type="text" id="postCode" name="postCode"/>
						</td>
					</tr>
					<tr>
						<td align="right" nowrap="true">客户地址：</td>
						<td align="left" nowrap="true">
							<input type="text" id="address" name="address"/>
						</td>
						<td align="right" nowrap="true">客户级别：</td>
						<td align="left" nowrap="true">
							<select id="stars" name="stars" class="short_sel">
								<option value=''>-请选择-</option>
							</select>
						</td>
					</tr>
					<tr>
						<td align="right" nowrap="true">单位电话：</td>
						<td align="left" nowrap="true">
							<input type="text" id="ctel" name="ctel" datatype="1,is_null,50"/><!-- 艾春2013.12.5调整限定长度 -->
						</td>
						<td align="right" nowrap="true">住宅电话：</td>
						<td align="left" nowrap="true">
							<input type="text" id="otel" name="otel" datatype="1,is_null,50"/><!-- 艾春2013.12.5调整限定长度 -->
						</td>
						<td align="right" nowrap="true">手机：</td>
						<td align="left" nowrap="true">
							<input type="text" id="tele" name="tele" datatype="1,is_null,50"/><!-- 艾春2013.12.5调整限定长度 -->
						</td>
					</tr>
				</table>		
			</form>
			
		<table id="carInfo" border="0" align="center" cellpadding="1" cellspacing="1" class="table_query" >
			<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />车辆信息</th>
			<tr>
				<td align="right" nowrap="true">车种：</td>
				<td align="left" nowrap="true">
					<div id="series"></div>
				</td>
				<td align="right" nowrap="true">车型：</td>
				<td align="left" nowrap="true">
					<div id="model"></div>
				</td>
				<td align="right" nowrap="true">颜色：</td>
				<td align="left" nowrap="true">
					<div id="color"></div>
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">产地：</td>
				<td align="left" nowrap="true">
					<div id="yieldly"></div>
				</td>
				<td align="right" nowrap="true">内部型号：</td>
				<td align="left" nowrap="true">
					<div id="packageName"></div>
				</td>
				<td align="right" nowrap="true">地区：</td>
				<td align="left" nowrap="true">
					<div id="area"></div>
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">发动机号：</td>
				<td align="left" nowrap="true">
					<div id="engineNo"></div>
				</td>
				<td align="right" nowrap="true">底盘号：</td>
				<td align="left" nowrap="true">
					<div id="vin"></div>
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">生产日期：</td>
				<td align="left" nowrap="true">
					<div id="pdate"></div>
				</td>
				<td align="right" nowrap="true">购买日期：</td>
				<td align="left" nowrap="true">
					<div id="pudate"></div>
				</td>
				<td align="right" nowrap="true">价格：</td>
				<td align="left" nowrap="true">
					<div id="price"></div>
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">装备代码：</td>
				<td align="left" nowrap="true">
					<div id="materialcode"></div>
				</td>
				<td align="right" nowrap="true">经销商名称：</td>
				<td align="left" nowrap="true">
					<div id="dealerName"></div>
				</td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<div id = "btn">
					
          			</div>
        		</td>
			</tr>
		</table>
	    </div>
		<div id="TabTab03Con2" style="display:none;max-width: 1200px;overflow:scroll"  >

		</div>
	   <div id="TabTab03Con3" style="display:none;max-width: 1200px;overflow:scroll"  >
	    	
	    </div>
	   
	</div>
	
	
	
	
	

	
<script type="text/javascript">
	var cid; //客户ID
	var oid; //客户实销ID
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/clientManage/IncomingAlertScreen/queryIncomingAlertScreen.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择", width:'8%',sortable: false,dataIndex: 'CTMID',renderer:myCheckBox},				
				{header: "客户名称"	,dataIndex: 'CTMNAME',align:'center'},
				{header: "手机号码"	,dataIndex: 'PHONE',align:'center'},
				{header: "最近一次开单时间", dataIndex: 'RO_DATE', align:'center'},
				{header: "最近一次服务站名称", dataIndex: 'RO_DNAME', align:'center'},
				{header: "购车日期", dataIndex: 'BUYDATE', align:'center'},
				{header: "车型",dataIndex: 'MODELNAME',align:'center'},
				{header: "底盘号", dataIndex: 'VIN', align:'center'},
				{header: "省份",dataIndex: 'PROVINCE',align:'center'},
				{header: "县市", dataIndex: 'CITY', align:'center'},
				{header: "经销商名称",dataIndex: 'DEALERNAME',align:'center'},
				{header: "客户级别", dataIndex: 'GUESTSTARS', align:'center'}
		      ];

	
	function myCheckBox(value,metaDate,record){
		//处理只有一条记录时自动弹出功能 接收后台特殊处理了的onlyOne
		
		if(record.data.ONLYONE == true){
			cid = record.data.CTMID;
			oid = record.data.ORDERID;
			// 如果所属地区不为空, 则刷新所属地区信息
			if(null != record.data.PROVINCE){
				document.getElementById('SPROVINCE').value = record.data.PROVINCE;
			}
			
			makeNomalFormCall('<%=contextPath%>/customerRelationships/clientManage/IncomingAlertScreen/queryIncomingAlertScreenInfor.json?ctmid='+record.data.CTMID+'&orderid='+record.data.ORDERID,changeData,'fm','');
			
			return String.format("<input name='radio' type='radio' checked='checked' value='"+ record.data.CTMID +","+ record.data.ORDERID +"' onclick='changeCheck(this,"+ record.data.CTMID +","+ record.data.ORDERID +")'/>");
		}else{
			return String.format("<input name='radio' type='radio' value='"+ record.data.CTMID +","+ record.data.ORDERID +"'  onclick='changeCheck(this,"+ record.data.CTMID +","+ record.data.ORDERID +")'/>");
		}
	}
	
	function AT_Command(strCmd) {
    switch (strCmd) {
        case "Pickup":
            parent.document.ut_atocx.ATAnswer('${LOGON_USER.actn}', "");
            break;
        case "Hangup":
            parent.document.ut_atocx.ATHangup('${LOGON_USER.actn}', "");
            break;
        case "PlaceCall": //呼叫电话
            parent.document.ut_atocx.ATPlaceCall('${LOGON_USER.actn}',$("txtTel").value);
            break;
        default:
            MyAlert("未处理命令：" + strCmd);
            break;
    }
    return true;
}
	
	
		function setFree()
		{
        	 parent.document.ut_atocx.ATSetBusy('${LOGON_USER.actn}',0, '');
        	 MyAlertForFun('置闲成功');
			 makeNomalFormCall('<%=contextPath%>/customerRelationships/classMonitorManage/ClassMonitorManage/logupdate.json?RpcStat=95131002&stat=95131002',RpcStat,'fm','');
        	
	    }
	    
	    function setBusy()
		{
		    parent.document.ut_atocx.ATSetBusy('${LOGON_USER.actn}', 1, '');
		    MyAlert("置忙成功！");
		     makeNomalFormCall('<%=contextPath%>/customerRelationships/classMonitorManage/ClassMonitorManage/logupdate.json?RpcStat=95131005&stat=95131002',RpcStat,'fm','');
		}
	
	   function RpcStat(){
	   }
	
	function  Pingfen()
	{
		  var strIvr = "AC_SWITCHIVR;CALLID=" + parent.document.ut_atocx.myCall_CallId + ";EXT=" + ${LOGON_USER.txtExt} + ";IVRFILE=SCORE.DAT;NODE=1;IVRMSG="+${LOGON_USER.actn}+"|"+parent.document.ut_atocx.myCall_CallId + ";";
          parent.document.ut_atocx.ATTranCall_toIVR('${LOGON_USER.actn}',0,"",strIvr);
	}
	
	function doRowClick(obj){
    	if(obj.cells[0].firstChild.checked != true){
    		obj.cells[0].firstChild.checked = true;
    		var str = obj.cells[0].firstChild.value;
    		changeCheck(obj.cells[0].firstChild,str.split(',')[0],str.split(',')[1]);
    	}
	 }
	
	function changeCheck(checkBox,ctmid,orderid){
		if(checkBox.checked){
			cid = ctmid;
			oid = orderid;
			makeNomalFormCall('<%=contextPath%>/customerRelationships/clientManage/IncomingAlertScreen/queryIncomingAlertScreenInfor.json?ctmid='+ctmid+'&orderid='+orderid,changeData,'fm','');
		}
	}
	//返回的数据 更新页面数据
	function changeData(json) {
		setCustomerInfo(json);
		setVehicleInfo(json);
		mouseClick("btn",json.queryIncomingAlertScreeData.CTMID);
		setMaintainHistoryInfo(json);
		setComplaintHistory(json);
		
		showTable();
	}
	
	function setCustomerInfo(json){
		setTextData('ctmname',json.queryIncomingAlertScreeData.CTMNAME);
		resetSelectOption('ctmtype',json.customerTypeList,'CODE_DESC','CODE_ID',json.queryIncomingAlertScreeData.CTMTYPE,false);
		resetSelectOption('purpose',json.salesAddressList,'CODE_DESC','CODE_ID',json.queryIncomingAlertScreeData.SALESADDRESS,false);
		
		setTextData('cardNum',json.queryIncomingAlertScreeData.CARDNUM);
		resetSelectOption('province',json.proviceList,'REGION_NAME','REGION_CODE',json.queryIncomingAlertScreeData.PROVINCE,false);
		resetSelectOption('city',json.cityList,'REGION_NAME','REGION_CODE',json.queryIncomingAlertScreeData.CITY,false);
		setTextData('postCode',json.queryIncomingAlertScreeData.POSTCODE);
		
		setTextData('address',json.queryIncomingAlertScreeData.ADDRESS);
		resetSelectOption('stars',json.guestStarsList,'CODE_DESC','CODE_ID',json.queryIncomingAlertScreeData.GUESTSTARS,false);
		setTextData('ctel',json.queryIncomingAlertScreeData.COMPHONE);
		setTextData('otel',json.queryIncomingAlertScreeData.OTHERPHONE);
		setTextData('tele',json.queryIncomingAlertScreeData.PHONE);
	}
	
	function setVehicleInfo(json){
		setData('series',json.queryIncomingAlertScreeData.SERIESNAME);
		setData('model',json.queryIncomingAlertScreeData.MODELNAME);
		setData('color',json.queryIncomingAlertScreeData.COLOR);
		setData('yieldly',json.queryIncomingAlertScreeData.YIELDLY);
		setData('packageName',json.queryIncomingAlertScreeData.PACKAGENAME);
		setData('area',json.queryIncomingAlertScreeData.AREA);
		setData('engineNo',json.queryIncomingAlertScreeData.ENGINENO);
		setData('vin',json.queryIncomingAlertScreeData.VIN);
		setData('pdate',json.queryIncomingAlertScreeData.PDATE);		
		setData('pudate',json.queryIncomingAlertScreeData.PUDATE);
		setData('price',json.queryIncomingAlertScreeData.PRICE);
		setData('materialcode',json.queryIncomingAlertScreeData.MATERIALCODE);
		setData('dealerName',json.queryIncomingAlertScreeData.DEALERNAME);
	}
	
	function setMaintainHistoryInfo(json){
		var str = '<table class="table_list" > <tr class="table_list_th"><th>序号</th><th>工单号码</th><th>维修日期</th>'
				+ '<th>经销商名称</th><th>行驶里程</th><th>工时代码</th>'
				+ '<th>工时名称</th><th>配件代码</th><th>配件名称</th><th>故障描述</th><th>故障原因</th><th>工单类型</th>'		
				+ '<th>顾客问题</th><th>工时费</th><th>材料费</th><th>总申报金额</th><th>结算工时费</th><th>结算材料费</th>'
				+ '<th>结算总金额</th><th>审核备注</th><th>工单状态</th></tr>';
		var array = json.maintaimHisList;
		for(var row=0;row<array.length;row++){
			var maps = array[row];
			if((row%2)==0){
				str +='<tr class="table_list_row1">';
			}else{
				str +='<tr class="table_list_row2">';
			}
			str +='<td>'+(row+1)+'</td>';
			str+='<td>'+checkNull(maps.RO_NO)+'</td>';
			str+='<td>'+checkNull(maps.RO_CREATE_DATE) + '</td>';
			str+='<td>'+checkNull(maps.DEALER_NAME)+ '</td>';
			str+='<td>'+checkNull(maps.IN_MILEAGE) + '</td>';
			str+='<td>'+checkNull(maps.WR_LABOURCODE) + '</td>';
			str+='<td>'+checkNull(maps.WR_LABOURNAME) + '</td>';
			str+='<td>'+checkNull(maps.PART_CODE) + '</td>';
			str+='<td>'+checkNull(maps.PART_NAME) + '</td>';
			str+='<td>'+checkNull(maps.TROUBLE_DESC) + '</td>';
			str+='<td>'+checkNull(maps.TROUBLE_REASON) + '</td>';
			str+='<td>'+getItemValue(checkNull(maps.REPAIR_TYPE_CODE)) + '</td>';
			str+='<td>'+checkNull(maps.TROUBLE_TYPE) + '</td>';
			str+='<td>'+checkNull(maps.LABOUR_AMOUNT) + '</td>';
			str+='<td>'+checkNull(maps.PART_AMOUNT) + '</td>';
			str+='<td>'+checkNull(maps.REPAIR_AMOUNT) + '</td>';
			str+='<td>'+checkNull(maps.BALANCE_LABOUR_AMOUNT) + '</td>';
			str+='<td>'+checkNull(maps.BALANCE_PART_AMOUNT) + '</td>';
			str+='<td>'+checkNull(maps.BALANCE_AMOUNT) + '</td>';
			str+='<td>'+checkNull(maps.AUTH_REMARK) + '</td>';
			str+="<td>"+checkNull(maps.STATUS_NAME) + "</td>";

			str +='</tr>';
		}
		str+='</table>';
		
		document.getElementById('TabTab03Con2').innerHTML =str;
	}
	
	function setComplaintHistory(json){
		var str = '<table class="table_list" > <tr class="table_list_th"><th>序号</th><th>客户姓名</th><th>受理日期</th>'
				+ '<th>受理人</th><th>状态</th><th>业务类型</th><th>投诉咨询内容</th><th>查看';
		var array = json.complaintInfoList;
		for(var row=0;row<array.length;row++){
			var maps = array[row];
			if((row%2)==0){
				str +='<tr class="table_list_row1">';
			}else{
				str +='<tr class="table_list_row2">';
			}
			str +='<td>'+(row+1)+'</td>';
			str+='<td>'+checkNull(maps.CTMNAME)+'</td>';
			str+='<td>'+checkNull(maps.ACCDATE) + '</td>';
			str+='<td>'+checkNull(maps.ACCUSER)+ '</td>';
			str+='<td>'+checkNull(maps.STATUS)+ '</td>';
			str+='<td>'+checkNull(maps.BIZTYPE) + '</td>';
			str+='<td>'+checkNull(maps.CPCONTENT) + '</td>';			
			str+='<td><a href="<%=contextPath%>/customerRelationships/complaintConsult/ComplaintConsult/complaintConsultWatch.do?cpid='+checkNull(maps.CPID)+'&ctmid='+checkNull(maps.CTMID)+'"  target="_black">查看</a></td>';
			str +='</tr>';
		}
		str+='</table>';
		
		document.getElementById('TabTab03Con3').innerHTML =str;
	}
	function checkNull(str){
		if(str==null || str=='null'){
			return '';
		}else{
			return str;
		}
	}
	//重置下拉框数据
	function resetSelectOption(id,maps,dataName,dataValue,dataId,isdisabled){
		clearSelectNode(id);
		addSelectNode(id,maps,dataName,dataValue,dataId,isdisabled);
	}
	
	//动态删除下拉框节点
	function clearSelectNode(id) {			
		document.getElementById(id).options.length=0; 			
	}
	//动态添加下拉框节点
	function addSelectNode(id,maps,dataName,dataValue,dataId,isdisabled){
		document.getElementById(id).options.add(new Option('-请选择-',''));
		for(var i = 0; i<maps.length;i++){
			if((maps[i])['' +dataValue+''] == dataId){
				document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,true));
			}
			else{
				document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,false));
			}
		}
		if(isdisabled == 'true' || isdisabled == true){
			document.getElementById(id).disabled = "disabled";
		}else{
			document.getElementById(id).disabled = "";
		}
		
	}
	
	//刷新加载数据
	function setData(id,value){
		if(value == null) value = "";
		document.getElementById(id).innerHTML = value;
	}
	
	function setTextData(id,value){
		if(value == null) value = "";
		document.getElementById(id).value = value;
	}
	
	function mouseClick(id,ctmid){
		var str = '<input class="normal_btn" type="button" value="保存" name="recommit" id="queryBtn" onclick="'+"teleUpdate("+"'"+ctmid+"'"+")"+'"/>';				
		document.getElementById(id).innerHTML =str;
	}
	
	function teleUpdate(ctmid){
		if(check()){
			makeNomalFormCall('<%=contextPath%>/customerRelationships/clientManage/IncomingAlertScreen/updateCustomer.json?ctmid='+ctmid,updateBack,'baseInfo','');	
		}
	}
	function updateBack(json) {
		if(json.success != null && json.success=='true'){
			MyAlert("保存成功!");
		}else{
			MyAlert("保存失败!请联系管理员");
		}
	}
	
	function check(){
		var msg ="";
		if(""==document.getElementById('ctmname').value){
			msg+="客户名称不能为空!</br>"
		}

		if(""==document.getElementById('ctel').value && ""==document.getElementById('otel').value && ""==document.getElementById('tele').value){
			msg+="单位电话、住宅电话、手机至少填其一!</br>"
		}

		if(msg!=""){
			MyAlert(msg);
			return false;
		}else{
			return true;
		}
	}
	
	function complaint(ctmid){
		openWindowDialog('<%=contextPath%>/customerRelationships/clientManage/IncomingAlertScreen/complaintInfo.do?ctmid='+ctmid);
	}
	

	
	function showTable(){
		document.getElementById("tabDiv").style.display  = '';
		document.getElementById("complaitAccepte").style.display  = '';
		//document.getElementById("base").style.display  = '';
		//document.getElementById("carInfo").style.display  = '';
	}
	function openWindowDialog(targetUrl){
	    var height = 800;
	    var width = 1000;
	    var iTop = (window.screen.availHeight-30-height)/2;  //获得窗口的垂直位置      
	    var iLeft = (window.screen.availWidth-10-width)/2;   //获得窗口的水平位置
	    var params = "width="+width+",height="+height+",top="+iTop+",left="+iLeft+",scrollbars=yes,resizable=yes,status=no";
	    winHandle = window.open(targetUrl,null,params);
	    return winHandle;
	 }
	function complaitAccepting(){
		if(cid == '' || oid == ''){
			MyAlert('请选择客户信息!');
		}
		window.open('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAccept/complaintAcceptInitPort.do?ctmid='+cid+'&orderid='+oid,'','left=0,top=0,width='+screen.availWidth +'- 10,height='+screen.availHeight+'-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
 
		//location.href='<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAccept/complaintAcceptInitPort.do?ctmid='+cid+'&orderid='+oid;
	}
	function changeCityEvent(value,defaultValue,isdisabled){
		if(''!=value){
			makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/changeRegion.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeCityBack,'fm','');
		}else{
			checkedSelect('citysel','',false);
		}
	}
	//城市级联回调方法：
	function changeCityBack(json) {
		resetSelectOption('city',json.regionList,'REGION_NAME','REGION_CODE',json.defaultValue,json.isdisabled);
	}
	
	
	function setTab03Syn ( i ){
		selectTab03Syn(i);
	}
	
	function selectTab03Syn ( i ){
		switch(i){
			case 1:
			document.getElementById("TabTab03Con1").style.display="block";
			document.getElementById("TabTab03Con2").style.display="none";
			document.getElementById("TabTab03Con3").style.display="none";
			
			document.getElementById("font1").style.color="#000000";
			document.getElementById("font2").style.color="#ffffff";
			document.getElementById("font3").style.color="#ffffff";
			
			break;
			case 2:
			document.getElementById("TabTab03Con1").style.display="none";
			document.getElementById("TabTab03Con2").style.display="block";
			document.getElementById("TabTab03Con3").style.display="none";
			
			document.getElementById("font1").style.color="#ffffff";
			document.getElementById("font2").style.color="#000000";
			document.getElementById("font3").style.color="#ffffff";
			
			break;
			case 3:
			document.getElementById("TabTab03Con1").style.display="none";
			document.getElementById("TabTab03Con2").style.display="none";
			document.getElementById("TabTab03Con3").style.display="block";
			
			document.getElementById("font1").style.color="#ffffff";
			document.getElementById("font2").style.color="#ffffff";
			document.getElementById("font3").style.color="#000000";
			
			break;
		}
	}
function updateStatus(type){
	var url = "<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAccept/updateStatus.json?type="+type;
	makeNomalFormCall(url,afterCall,'fm','createOrdBtn');
	}
//回调处理
function afterCall(json)
{
  			parent.document.ut_atocx.UidSelected = '${LOGON_USER.actn}';
			var UidStatus = parent.document.ut_atocx.UidStatus;
		    var UidCall_Status = parent.document.ut_atocx.UidCall_Status;
		    var str = '';
			if(UidStatus == "00")
			{
			   str = '未登陆';
			}else if (UidCall_Status == "05")
			{
				str = '正在电话受理';
			}else if(UidCall_Status == "03")
			{
				str = '正在电话受理';
			}else if(UidCall_Status == "04")
			{
				str = '正在电话受理';
			}
			else if(UidStatus == "02")
			{
			  str = '已置忙';
			}
			else if(UidCall_Status == "01")
			{
				str = '空闲';
			}
			else 
			{
				str = '空闲';
			}
			parent.document.getElementById('Ustast').innerHTML = '你的状态是 : '+ str;
}
</script>


</body>
</html>
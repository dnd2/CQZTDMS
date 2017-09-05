/*
 * CreateDate : 2014-11-03
 * CreateBy   : yinshunhui
 * Comment    : 
 */
window.addEvent('domready', function(){
	__extQuery__(1);
});


function __extQuery__(page){
	entThisPage = page;
	$("queryBtn").disabled = "disabled";
	showMask();
	submitForm('fm') ? sendAjax(url+(url.lastIndexOf("?") == -1?"?":"&")+"curPage="+page,callBack,'fm') : ($("queryBtn").disabled = "",removeMask());
}
var myPage;
	var title = null;
	
	var columns = [
					{header: "序号", align:'center', renderer:getIndex,width:'7%'},
					{header: "用户账号", dataIndex: 'ACNT', align:'center'},
					{header: "经销商", dataIndex: 'COMPANY_SHORTNAME', align:'center'},
					{header: "职位级别",  dataIndex: 'POSE_RANK', align:'center'},
					{header: "组",  dataIndex: 'GROUP_NAME', align:'center'},
					{header: "姓名",  dataIndex: 'NAME', align:'center'},
					{header: "是否在职",  dataIndex: 'USER_STATUS', align:'center', renderer:getPosition},
					{header: "是否锁定",  dataIndex: 'IS_LOCK', align:'center', renderer:getLock},
					{header: "最后操作人", sortable: true, dataIndex: 'UPDATE_NAME', align:'center'},
					{header: "最后更新时间", sortable: true, dataIndex: 'UPDATE_DATE', align:'center'},
					{id:'action',header: "操作", width:70,sortable: false,dataIndex: 'USER_ID',renderer:myLink}
			      ];

	function myLink(value,metadata){
	   return String.format(
	          "<a href=\""+pat+"/crm/sysUser/DealerSysUser/uealerUserModifyInit.do?userId="
					+ value + "\">[查看]</a>");
	}

function requery() {
	$('DEALER_NAME').value="";
	$('DEALER_ID').value="";
	$('EMP_NUM').value="";
	$('NAME').value="";
}
//是否在职
function getPosition(value,metadata){
	var str="";
	if(value=="10011001"){
		str="是";
	}else{
		str="否";
	}
return String.format(str);
}
//是否锁定
function getLock(value,metadata){
	var str="";
	if(value=="1"){
		str="是";
	}else{
		str="否";
	}
return String.format(str);
}

function requery2() {
	$('DRLCODE').value="";
	$('DELSNAME').value="";
	$('DEPT_ID').value="";
}

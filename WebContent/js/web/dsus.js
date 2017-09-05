/*
 * CreateDate : 2009-09-11
 * CreateBy   : ChenLiang
 * Comment    : SGM维护经销商用户
 */

window.addEvent('domready', function(){
	__extQuery__(1);
});

var myPage;
function __extQuery__(page){
	entThisPage = page;
	$("queryBtn").disabled = "disabled";
	showMask();
	submitForm('fm') ? sendAjax(url+(url.lastIndexOf("?") == -1?"?":"&")+"curPage="+page+getPageSizeParam(),callBack,'fm') : ($("queryBtn").disabled = "",removeMask());
}
	
	var title = null;
	
	var columns = [
					{header: "序号", align:'center', renderer:getIndex,width:'7%'},
					{header: "用户账号", dataIndex: 'ACNT', align:'center'},
					{header: "经销商", dataIndex: 'COMPANY_SHORTNAME', align:'center'},
					{header: "职位",  dataIndex: 'POSE_NAME', align:'center'},
					{header: "姓名",  dataIndex: 'NAME', align:'center'},
					{header: "状态",  dataIndex: 'USER_STATUS', align:'center', orderCol:"USER_STATUS",renderer:getItemValue},
					{header: "最后操作人", sortable: true, dataIndex: 'UPDATE_NAME', align:'center'},
					{header: "最后更新时间", sortable: true, dataIndex: 'UPDATE_DATE', align:'center'},
					{id:'action',header: "操作", width:70,sortable: false,dataIndex: 'USER_ID',renderer:myLink}
			      ];

	function myLink(value,metadata){
	   return String.format(
	          "<a href=\""+pat+"/sysmng/usemng/SgmDealerSysUser/modfiSgmDealerSysUserInit.do?userId="
					+ value + "\">[查看]</a>");
	}

function requery() {
	$('DEALER_NAME').value="";
	$('DEALER_ID').value="";
	$('EMP_NUM').value="";
	$('NAME').value="";
}

function requery2() {
	$('DRLCODE').value="";
	$('DELSNAME').value="";
	$('DEPT_ID').value="";
}

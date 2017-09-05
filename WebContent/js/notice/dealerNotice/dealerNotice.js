var actionUrl = globalContextPath + "/sysmng/notice/dealerNotice/DealerNoticeAction";
var myPage;
var url = actionUrl + "/queryNotice.json";
var updateStateUrl = actionUrl + "/updateNoticeState.json";
var title = null;
var queryForm = "fm";

var parentContainer = parent || top;
var parentDocument =parentContainer.document;

var columns = [ 
	{header: "序号", align:'center', renderer:getIndex},
	{id : 'action',header : "操作",sortable : false,dataIndex : 'FUNC_ID',renderer : operTr,align : 'center'},
	{header : "提醒类型",dataIndex : 'NM_NOTICETYPE',	align : 'center', renderer:getNoticeType},
	{header : "提醒状态",dataIndex : 'CODE_DESC',	align : 'center'},
	{header : "提醒内容",dataIndex : 'DN_NOTICECONTENT',	align : 'center'},
	{header : "状态开始时间",	dataIndex : 'DN_BUSINESSSTATETIME',align : 'center'},
	{header: "状态", dataIndex : 'DN_HANDLESTATE',align:'center', renderer:getNoticeState}
];

function initQuery() {
	__extQuery__(1);
}

/**
 * 提醒类型的显示 
 * @param value
 * @param metadata
 */
function getNoticeType(value,metadata){
	var curType = null;
	if(value&&value=='1'){
		curType = '处理类';
	} else {
		curType = '通知类';
	}
	return curType;
}

/**
 * 转换状态列的显示
 * @param value
 * @param metadata
 */
function getNoticeState(value,metadata){
	var curState = null;
	if(value&&value=='1'){
		curState = '已处理';
	} else {
		curState = '未处理';
	}
	return curState;
}
	
/**
 * 操作列
 * @param value
 * @param metadata
 * @returns
 */
function operTr(value,metadata,record){
	var htm = '';
	htm += "<a href=\"#\" onclick=\"toUrl(\'"+value+"\');\">[查看]</a>  ";
	var hint = '已阅';
	if(record.data.DN_HANDLESTATE=='0'){
		if(record.data.NM_NOTICETYPE=='1'){
			hint = '处理';
		}
		htm += String.format("<a href=\"#\" onclick=\"executeNotice(\'"+record.data.ID+"\');\">["+hint+"]</a>");
	} else {
		
	}
    return String.format(htm);
}

/**
 * 跳转到列表页面
 * @param menuId
 */
function toUrl(menuId){
	var callBack = parentContainer.noticeReturn;
	var flag = false;
	if(null!=callBack&&typeof callBack == 'function'){
		flag = callBack(menuId);
	}
	if(flag){
		_hide();
	}
}

/**
 * 处理列
 * @param value
 * @param metadata
 * @returns
 */
function excuteTr(value,metadata,record){
	if(record.data.DN_HANDLESTATE=='0'){
		return String.format("<a href=\"#\" onclick=\"executeNotice(\'"+value+"\');\">[处理]</a>");
	} else {
		return '';
	}
}


/**
 * 处理指定的消息提醒记录 
 * @param noticeId
 */
function executeNotice(noticeId){
	var curUrl = updateStateUrl + "?noticeId="+noticeId;
	makeFormCall(curUrl,exeSuccess,queryForm);
}

/**
 * 处理成功方法
 * @param json
 */
function exeSuccess(json){
	var executedNotice = parentContainer.executedNotice;
	if (null != executedNotice && typeof executedNotice == 'function') {
		executedNotice();
	}
	initQuery();
}


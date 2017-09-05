var validateConfig = new Object({
	divCount : 1,
	isOnBlur : false,
	timeOut : 3000
});
var withoutIds = [ "nmTartype", "nmNoticetype", "nmNoticestaterelation",
		"nmOtherstaterelation1", "nmOtherstaterelation2", "columnSelect",
		"relationSelect" ];
var editForm = "noticeEditForm";
var editTable = "noticeModelEditTable";
var tarValue = "nmTarvalue";
var actionUrl = globalContextPath + "/sysmng/notice/noticeModel/NoticeModelAction";
var myPage;
var url = actionUrl + "/queryNoticeModel.json";
var title = null;
var columns = [ 
	{header: "序号", align:'center', renderer:getIndex,},
	{id : 'action',header : "操作",sortable : false,dataIndex : 'id',renderer : operTr,align : 'center'},
	{header : "模版名称",dataIndex : 'nmModelname',	align : 'center'},
	{header : "模版描述",dataIndex : 'nmModeldesc',align : 'center'}, 
	{header : "菜单名称",dataIndex : 'funcName',align : 'center'}, 
	{header : "提醒对象",dataIndex : 'nmTartypeDesc',align : 'center'}, 
	{header : "提醒状态值",dataIndex : 'nmNoticestatevalue',align : 'center'},
	{header : "开始提醒时间(分钟)",dataIndex : 'nmNoticemintime',align : 'center'}, 
	{header : "模版状态",dataIndex : 'nmModelstate',align : 'center',renderer:getState}, 
	{header : "创建人",	dataIndex : 'nmCreateUserName',align : 'center'}, 
	{header : "创建时间",	dataIndex : 'nmCreatetime',align : 'center'}
];

var maxSelectWidth = 300;

function initQuery() {
	__extQuery__(1);
}

/**
 * 初始化页面
 */
function initPage(){
	var idValue = document.getElementById('id').value;
	if(idValue==null||idValue==''){
		clearSelectOption(withoutIds);
	} else {
		resetSelectWidth(withoutIds);
	}
}

function getModel(){
	var nmMenuidNode = document.getElementById('nmMenuid');
	if(nmMenuidNode&&nmMenuidNode.value){
		clearSelectOption(withoutIds);
		var tableUrl = actionUrl + "/queryTableField.json?selectMenuId="+nmMenuidNode.value;
		makeFormCall(tableUrl,setFieldIterm,editForm);
	}
}

function setFieldIterm(json){
	var list = json.comments;
	if(list){
		var len = list.length;
		var columnSelect = document.getElementById('columnSelect');
		columnSelect.innerHTML = '';
		columnSelect.appendChild(createFOption());
		for(var i=0;i<len;i++){
			var comment = list[i];
			var opt = document.createElement('OPTION');
			opt.setAttribute('value',comment.COLUMN_NAME);
			opt.innerHTML = comment.COLUMN_NAME+":"+comment.COMMENTS;
			columnSelect.appendChild(opt);
		}
		copySelectOption(columnSelect,withoutIds);
	}
}

/**
 * 复制参数的选项到其他的selectnode中
 * @param columnSelect
 * @wthtIds 不进行复制的ids是个数组
 */
function copySelectOption(columnSelect,wthtIds){
	var selectNodes = document.getElementsByTagName('select');
	var maxWidth;
	if(selectNodes){
		for(var i=0;i<selectNodes.length;i++){
			snode = selectNodes[i];
			if(snode.getAttribute('id')==columnSelect.getAttribute('id')){
				continue;
			}
			// 是不是除外的select
			if(wthtIds){
				var isWithout = false;
				for(var j=0;j<wthtIds.length;j++){
					if(wthtIds[j]==snode.getAttribute('id')){
						isWithout = true;
						break;
					}
				}
				if(isWithout){
					continue;
				}
				
			}
			snode.innerHTML = '';
			var opts = columnSelect.options;
			if(opts){
				for(var j=0;j<opts.length;j++){
					snode.appendChild(opts[j].cloneNode(true));
				}
			}
			if(i==0){
				maxWidth = snode.offsetWidth;
			}
			snode.style.width = Math.min(Math.max(maxWidth,snode.offsetWidth),maxSelectWidth);
		}
	}
}

/**
 * 清空所有selectnode选项
 */
function clearSelectOption(wthIds){
	var selectNodes = document.getElementsByTagName('select');
	if(selectNodes){
		for(var i=0;i<selectNodes.length;i++){
			snode = selectNodes[i];
			// 是不是除外的select
			if(wthIds){
				var isWithout = false;
				for(var j=0;j<wthIds.length;j++){
					if(wthIds[j]==snode.getAttribute('id')){
						isWithout = true;
						break;
					}
				}
				if(isWithout){
					continue;
				}
				
			}
			snode.innerHTML = '';
			snode.appendChild(createFOption());
		}
	}
}

function resetSelectWidth(wthIds){
	var selectNodes = document.getElementsByTagName('select');
	if(selectNodes){
		for(var i=0;i<selectNodes.length;i++){
			snode = selectNodes[i];
			// 是不是除外的select
			if(wthIds){
				var isWithout = false;
				for(var j=0;j<wthIds.length;j++){
					if(wthIds[j]==snode.getAttribute('id')){
						isWithout = true;
						break;
					}
				}
				if(isWithout){
					continue;
				}
				setSelectWidth(snode,0);
			}
		}
	}
}

/**
 * 设置select的宽度 
 * @param snode
 * @param width
 */
function setSelectWidth(snode,width){
	snode.style.width = Math.min(Math.max(width,snode.offsetWidth),maxSelectWidth);
}

/**
 * 创建"请选择"选项
 * @returns {___anonymous2514_2517}
 */
function createFOption(){
	var fopt = document.createElement('OPTION');
	fopt.setAttribute('value','');
	fopt.innerHTML = "--请选择--";
	return fopt;
}

/**
 * 保存消息提醒
 */
function saveNoticeModel(){
	if(submitForm(editForm)){
		var submitUrl = actionUrl + "/saveNoticeModel.do";
		var smtForm = document.getElementById(editForm);
		smtForm.setAttribute("action",submitUrl);
		smtForm.submit();
	}
}

function operTr(value,metadata){
    return String.format("<a href=\""+actionUrl+"/toAddNotice.do?nmId="+value+"\">[查看]</a>");
}

/**
 * 增加两个条件
 */
function addNoticeState(){
	var otherState1 = document.getElementById('nmOtherstatefield1');
	var table = document.getElementById(editTable);
	if(otherState1){
		var otherState2 = document.getElementById('nmOtherstatefield2');
		if(otherState2){
			alert("达到条件上限!");
		} else {
			if(table){
				createStateTr(2);
			}
		}
	}else {
		if(table){
			createStateTr(1);
		}
	}
}

/**
 * 列表显示模版启用状态
 * @param value
 * @param metadata
 * @returns {String}
 */
function getState(value,metadata){
	var curType = null;
	if(value&&value=='1'){
		curType = '启用';
	} else {
		curType = '未启用';
	}
	return curType;
}

/**
 * 创建附加条件
 * @param num
 */
function createStateTr(num){

	var columnSelect = document.getElementById('columnSelect');
	var relationSelect = document.getElementById('relationSelect');
	var delRowButton = document.getElementById("delRow");
	var table = document.getElementById(editTable);
	// 创建行
	var ntr = createNode('TR');
	
	// 创建值td内容
	var newStateNode = columnSelect.cloneNode(true);
	newStateNode.setAttribute('id','nmOtherstatefield'+num);
	newStateNode.setAttribute('name','nmOtherstatefield'+num);
	newStateNode.setAttribute('datatype','0,is_null,200');
	// 创建值td
	var tdNode1 = createNode('TD');
	tdNode1.setAttribute('colspan','2');
	tdNode1.appendChild(newStateNode);
	
	// 创建标题td
	var tdLabel1 = createNode('TD',null,null,'tblopt');
	var labelValue = document.createTextNode("附加条件"+num);
	tdLabel1.appendChild(labelValue);
	
	// 将创建的标题和值td追加进ntr
	ntr.appendChild(tdLabel1);
	ntr.appendChild(tdNode1);
	
	
	//<input type="text" id="nmNoticestatevalue" name="nmNoticestatevalue" datatype='0,is_null,20'  value="${nmBean.nmNoticestatevalue }"/>
	// 创建标题td
	var tdLabel2 = createNode('TD',null,null,'tblopt');
	labelValue = document.createTextNode("附加条件值"+num);
	tdLabel2.appendChild(labelValue);
	
	// 创建值td
	var tdNode2 = createNode('TD');
	tdNode2.setAttribute('colspan','2');
	// 创建td内容
	var newRelationNode = relationSelect.cloneNode(true);
	newRelationNode.setAttribute('id','nmOtherstaterelation'+num);
	newRelationNode.setAttribute('name','nmOtherstaterelation'+num);
	newRelationNode.setAttribute('datatype','0,is_null,200');
	tdNode2.appendChild(newRelationNode);
	var blk = document.createTextNode(" ");
	tdNode2.appendChild(blk);
	var tdInput = createNode('INPUT','nmOtherstatevalue'+num,'nmOtherstatevalue'+num);
	tdInput.setAttribute('datatype','0,is_null,200');
	tdNode2.appendChild(tdInput);
	var delRowButton = delRowButton.cloneNode(true);
	delRowButton.removeAttribute("id");
	tdNode2.appendChild(delRowButton);
	
	// 将创建的标题和值td追加进ntr
	ntr.appendChild(tdLabel2);
	ntr.appendChild(tdNode2);
	// 将ntr追加到table
	table.appendChild(ntr);
}


/**
 * 创建新的node
 * @param nodeType  node类型
 * @param nId  id属性
 * @param nName  name属性
 * @param nClass class属性
 */
function createNode(nodeType,nId,nName,nClass){
	var nNode = document.createElement(nodeType);
	if(nId){
		nNode.setAttribute('id',nId);
	}
	if(nName){
		nNode.setAttribute('name',nName);
	}
	if(nClass){
		nNode.setAttribute('class',nClass);
	}
	return nNode;
}

/**
 * 删除按钮所在行
 * @param btn
 */
function delRow(btn){
	var td = btn.parentNode;
	var tr = td.parentNode;
	tr.parentNode.removeChild(tr);
}

/**
 * 选择提醒对象类型事件,选择职位和指定用户会自定义id
 * @param _node
 */
function changeTartype(_node){
	var tvModel = document.getElementById("nmTarValueModel");
	var tvNode = document.getElementById(tarValue);
	var val = _node.value;
	if ('owner' != val && 'dealer' != val) {
		if(tvNode){
			return;
		}
		var tvNode = tvModel.cloneNode(true);
		tvNode.setAttribute('id',tarValue);
		_node.parentNode.appendChild(tvNode);
	} else {
		_node.parentNode.removeChild(tvNode);
	}
}
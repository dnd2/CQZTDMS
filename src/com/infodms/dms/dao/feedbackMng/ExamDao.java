package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtExamDetailPO;
import com.infodms.dms.po.TtExamPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 考试模块中涉及的DAO方法
 * @author Administrator
 *
 */
public class ExamDao extends BaseDao {
	private ExamDao(){}
	public static ExamDao getInstance(){
		return new ExamDao();
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * 考试信息主查询(OEM)
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> queryMainExam(String sql,int pageSize,int curPage){
		return (PageResult<Map<String, Object>>)this.pageQuery(sql, null, this.getFunName(), pageSize, curPage);
	}
	
	/*
	 * 考试信息主查询(OEM)
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> queryMainAnswer(String sql,int pageSize,int curPage){
		return (PageResult<Map<String, Object>>)this.pageQuery(sql, null, this.getFunName(), pageSize, curPage);
	}
	
	/*
	 * 考试信息主表插入
	 */
	@SuppressWarnings("unchecked")
	public void addExam(TtExamPO po){
		this.insert(po);
	}
	
	/*
	 * 根据EXAM_ID查询考试信息
	 */
	@SuppressWarnings("unchecked")
	public TtExamPO queryExamById(TtExamPO po){
		List<TtExamPO> lists = this.select(po);
		if(lists.size()>0) return lists.get(0);
		return null ;
	}
	
	/*
	 * 根据EXAM_ID查询考试附件信息
	 */
	@SuppressWarnings("unchecked")
	public List<TtExamDetailPO> queryExamDetailById(TtExamDetailPO po){
		return (List<TtExamDetailPO>)this.select(po);
	}
	
	/*
	 * 根据DETAIL_ID删除考试附件信息
	 */
	@SuppressWarnings("unchecked")
	public void deleteDetailById(TtExamDetailPO po){
		this.delete(po);
	}
	
	/*
	 * 根据EXAM_ID查询考试信息
	 */
	@SuppressWarnings("unchecked")
	public FsFileuploadPO queryFileById(FsFileuploadPO po){
		List<FsFileuploadPO> lists = this.select(po);
		if(lists.size()>0) return lists.get(0);
		return null ;
	}
	
}

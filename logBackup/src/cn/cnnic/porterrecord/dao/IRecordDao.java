package cn.cnnic.porterrecord.dao;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import cn.cnnic.porterrecord.po.RecordPO;
import cn.cnnic.porterrecord.vo.PorterRecordVO;

/*
 * @author zhangtao
 * @since 2017-05-22
 */
public interface IRecordDao {
	public int addRecord(RecordPO record);
	public List<PorterRecordVO> getPorterRecordByDate(Date startDate,Date endDate);
	public LinkedList<PorterRecordVO> getPorterRecordByServiceId(Integer serviceId,Date startDate,Date endDate);
}

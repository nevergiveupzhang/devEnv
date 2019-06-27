package cn.cnnic.whoistool.dao;

import java.util.List;

import cn.cnnic.whoistool.po.ContactPO;

/*
 * @author zhangtao 
 * @since 2017-6-24
 */

public interface IContactDao {
	public ContactPO queryContact(String contactId);
}

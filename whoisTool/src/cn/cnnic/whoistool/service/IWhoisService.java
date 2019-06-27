package cn.cnnic.whoistool.service;

import java.util.List;

import cn.cnnic.whoistool.dto.DomainDTO;

public interface IWhoisService {

	List<DomainDTO> query(String[] domains, String[] whoisDatas);
	
}

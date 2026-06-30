package comtax.gov.webapp.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import comtax.gov.webapp.repository.IpLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IpLogService {

	
	private final IpLogRepository ipLogRepository;
	
	@Transactional(rollbackFor = Exception.class)
	public void logIp(String ip, String path,String requestParams, String status) {
        log.info("Enter into logIp() method:-- ");
        ipLogRepository.saveIpLog(ip, path,requestParams, status);
        log.info("Exit from logIp() method:--");
    }

}

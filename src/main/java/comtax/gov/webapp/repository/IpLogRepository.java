package comtax.gov.webapp.repository;

import java.sql.Timestamp;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class IpLogRepository {	
	
    private final JdbcTemplate jdbcTemplate;
    
    public void saveIpLog(String ip, String path,String requestParams, String status) {
        String sql = "INSERT INTO WBCOMTAX_GST_API_LOG_DETAILS (IP_ADDRESS, REQUEST_URI, REQUEST_PARAMS,API_NAME,"
        		+ " API_VERSION, STATUS,LOG_DT) VALUES (?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, ip, path, requestParams,"ChatBot","V1",status,new Timestamp(System.currentTimeMillis()));
    }

}

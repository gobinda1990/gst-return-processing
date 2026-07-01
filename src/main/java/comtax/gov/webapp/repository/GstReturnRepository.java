package comtax.gov.webapp.repository;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import comtax.gov.webapp.model.GstReturn3BResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class GstReturnRepository {

	private final JdbcTemplate jdbcTemplate;

//	String RET_QUERY = "SELECT * FROM ( SELECT gstin, ret_period,TO_DATE(fil_dt, 'DD-MM-YYYY') AS fil_dt FROM gst_api_ret_3b_main  "
//			+ " WHERE gstin = ? ORDER BY TO_DATE(fil_dt, 'DD-MM-YYYY') DESC) WHERE ROWNUM < 3 ";
	
	
	String RET_QUERY="SELECT * FROM (select a.gstin, a.retprd, a.fil_dt, b.reg_st_date,b.auth_status, a.ret_type, row_number() over "
			+ "  (partition by a.gstin order by a.fil_dt desc) rank "
			+ "	  from (select gstin,substr(substr('00'||ret_period,-6),3,4)||substr(substr('00'||ret_period,-6), "
			+ " 1,2) retprd,to_date(fil_dt, 'dd-mm-yyyy') fil_dt, 'R3B' ret_type "
			+ "	from gst_api_ret_3b_main where GSTIN=?	union select gstin,retprd,fil_dt, 'CMP08' ret_type 	"
			+ " from gst_ret_cmp08_main where GSTIN=?) a "
			+ " inner join ACS_MAST.GST_DEALER_MASTER_WBCOMTAX b on a.gstin=b.gstin) WHERE RANK=1 ";
			

	public List<GstReturn3BResponse> getReturn3BDet(String gstin) {

		log.info("Fetching latest GST Return 3B records for GSTIN: {}", gstin);

		try {

			List<GstReturn3BResponse> response = jdbcTemplate.query(RET_QUERY, (rs, rowNum) -> {

				GstReturn3BResponse dto = new GstReturn3BResponse();
				dto.setGstin(rs.getString("gstin"));
				dto.setRetPrd(rs.getString("retprd"));				
				dto.setFilingDt(rs.getDate("fil_dt").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
				dto.setRetType(rs.getString("ret_type"));
				dto.setRegStatus(rs.getString("auth_status"));
				dto.setRegStDt(rs.getDate("reg_st_date").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
						

				return dto;
			}, gstin,gstin);

			log.info("Fetched {} GST Return 3B record(s) for GSTIN: {}", response.size(), gstin);

			return response;

		} catch (DataAccessException ex) {

			log.error("Database error while fetching GST Return 3B for GSTIN: {}", gstin, ex);

			throw ex;
		}
	}

}

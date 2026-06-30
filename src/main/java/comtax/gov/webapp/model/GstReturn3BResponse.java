package comtax.gov.webapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GstReturn3BResponse {

	private String gstin;
	private String retPrd;
	private String filingDt;

}

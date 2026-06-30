package comtax.gov.webapp.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import comtax.gov.webapp.exception.EncryptionException;
import comtax.gov.webapp.exception.ResourceNotFoundException;
import comtax.gov.webapp.model.GstReturn3BResponse;
import comtax.gov.webapp.repository.GstReturnRepository;
import comtax.gov.webapp.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GstReturnService {

	private final GstReturnRepository gstReturnRepository;
	private final ObjectMapper objectMapper;
	private final EncryptionUtil encryptionUtil;
	

	/**
	 * Returns encrypted GST Return 3B data.
	 *
	 * @param gstin GSTIN Number
	 * @return Encrypted JSON String
	 */
	public String getReturn3B(String gstin) {

		log.info("Fetching GST Return 3B for GSTIN : {}", gstin);

		List<GstReturn3BResponse> responseList = gstReturnRepository.getReturn3BDet(gstin);

		if (responseList == null || responseList.isEmpty()) {

			log.warn("No GST Return 3B found for GSTIN : {}", gstin);

			throw new ResourceNotFoundException("No GST Return 3B found for GSTIN : " + gstin);
		}

		try {

			String json = objectMapper.writeValueAsString(responseList);

			return encryptionUtil.encrypt(json);

		} catch (JsonProcessingException ex) {

			log.error("JSON serialization failed for GSTIN : {}", gstin, ex);

			throw new EncryptionException("Unable to serialize GST Return 3B response", ex);

		} catch (Exception ex) {

			log.error("Encryption failed for GSTIN : {}", gstin, ex);

			throw new EncryptionException("Unable to encrypt GST Return 3B response", ex);
		}
	}
}
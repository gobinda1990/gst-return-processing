package comtax.gov.webapp.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import comtax.gov.webapp.model.ApiResponse;
import comtax.gov.webapp.service.GstReturnService;
import comtax.gov.webapp.util.EncryptionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "GSTIN Lookup", description = "API to fetch encrypted GSTIN return 3b filing date..")
public class GstReturnController {

	private final GstReturnService gstReturnService;
	
	private final EncryptionUtil encryptionUtil;

	@Operation(summary = "Fetch encrypted GSTIN details", description = "Returns AES-encrypted GSTIN details like legal name, trade name, and mobile number")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved and encrypted GSTIN details"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid GSTIN format or missing input"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "GSTIN details not found"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal error during encryption") })

	@GetMapping("/gst-return-3b")
	public ResponseEntity<ApiResponse<String>> getGstinDetails(@RequestParam String gstin, HttpServletRequest request) throws Exception {

		String encryptedData = gstReturnService.getReturn3B(gstin);
		
//		log.info("Encrypted data:--"+encryptedData);
		log.info("Decrypted data:--"+encryptionUtil.decrypt(encryptedData));
		
//		String enc = encryptionUtil.encrypt("Wbcomtax@1234");
//		
//		log.info(":::"+enc);

		return ResponseEntity.ok(ApiResponse.success("GST Return 3B fetched successfully.", encryptedData));
	}

}

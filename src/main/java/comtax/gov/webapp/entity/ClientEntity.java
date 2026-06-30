package comtax.gov.webapp.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("API_CLIENT_MASTER")
public class ClientEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("CLIENT_ID")
    private Long id;

    @Column("CLIENT_CODE")
    private String clientCode;

    @Column("CLIENT_NAME")
    private String clientName;

    /**
     * Used by API consumers
     */
    @Column("CLIENT_IDENTIFIER")
    private String clientId;

    /**
     * Store BCrypt hash only
     */
    @Column("CLIENT_SECRET")
    private String clientSecret;

    @Column("STATE_CD")
    private String stateCd;

    @Column("EMAIL_ID")
    private String emailId;

    @Column("MOBILE_NO")
    private String mobileNo;

    @Column("PUBLIC_KEY")
    private String publicKey;

    @Column("ACTIVE_FLAG")
    private String activeFlag;

    @Column("CREATED_BY")
    private String createdBy;

    @Column("CREATED_DATE")
    private LocalDateTime createdDate;

    @Column("UPDATED_BY")
    private String updatedBy;

    @Column("UPDATED_DATE")
    private LocalDateTime updatedDate;

}
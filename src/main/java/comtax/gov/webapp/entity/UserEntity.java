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

/**
 * API User Master
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("API_USER_MASTER")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("USER_ID")
    private String id;

    /**
     * Login Username
     */
    @Column("USERNAME")
    private String username;

    /**
     * Store BCrypt Hash Only
     */
    @Column("PASSWORD")
    private String password;

    /**
     * Client Mapping
     */
    @Column("CLIENT_ID")
    private String clientId;

    /**
     * Full Name
     */
    @Column("FULL_NAME")
    private String fullName;

    /**
     * Email
     */
    @Column("EMAIL_ID")
    private String emailId;

    /**
     * Mobile Number
     */
    @Column("MOBILE_NO")
    private String mobileNo;

    /**
     * PAN Number
     */
    @Column("PAN_NO")
    private String panNo;

    /**
     * GST State Code
     */
    @Column("STATE_CD")
    private String stateCd;

    /**
     * User Role
     * Example:
     * GST_USER
     * GST_ADMIN
     * GST_APPROVER
     */
    @Column("ROLE_NAME")
    private String roleName;

    /**
     * User Status
     * ACTIVE
     * LOCKED
     * DISABLED
     */
    @Column("STATUS")
    private String status;

    /**
     * Failed Login Count
     */
    @Column("FAILED_ATTEMPTS")
    private Integer failedAttempts;

    /**
     * Last Login Time
     */
    @Column("LAST_LOGIN")
    private LocalDateTime lastLogin;

    /**
     * Password Expiry
     */
    @Column("PASSWORD_EXPIRY")
    private LocalDateTime passwordExpiry;

    /**
     * Active Flag
     */
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
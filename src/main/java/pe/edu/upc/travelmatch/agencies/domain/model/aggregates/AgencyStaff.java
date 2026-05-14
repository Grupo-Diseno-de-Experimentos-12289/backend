package pe.edu.upc.travelmatch.agencies.domain.model.aggregates;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.AbstractAggregateRoot;

/** AgencyStaff type. */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class AgencyStaff extends AbstractAggregateRoot<AgencyStaff> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "agency_id", nullable = false)
  private Agency agency;

  @NotBlank
  @Column(nullable = false)
  private String firstName;

  @NotBlank
  @Column(nullable = false)
  private String lastName;

  @NotBlank
  @Email
  @Column(nullable = false, unique = true)
  private String email;

  @NotBlank
  @Pattern(regexp = "\\d{9}", message = "Phone must be 9 digits")
  @Column(nullable = false)
  private String phone;

  @NotBlank
  @Column(nullable = false)
  private String position;

  /** Constructs a new AgencyStaff. */
  public AgencyStaff(
      Agency agency,
      String firstName,
      String lastName,
      String email,
      String phone,
      String position) {
    this.agency = agency;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.position = position;
  }

  /** Update. */
  public AgencyStaff update(
      String firstName, String lastName, String email, String phone, String position) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.position = position;
    return this;
  }
}

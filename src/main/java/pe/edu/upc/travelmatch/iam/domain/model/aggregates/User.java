package pe.edu.upc.travelmatch.iam.domain.model.aggregates;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

/**
 * User aggregate root.
 */
@Entity
public class User extends AuditableAbstractAggregateRoot<User> {

  @Getter
  @NotBlank
  @Column(unique = true)
  @Size(max = 254)
  private String email;

  @Getter
  @NotBlank
  @Size(max = 256)
  private String password;

  @Getter
  @NotBlank
  @Size(max = 60)
  private String firstName;

  @Getter
  @NotBlank
  @Size(max = 60)
  private String lastName;

  @Getter
  @Size(max = 20)
  private String phone;

  @Getter
  private boolean isActive;

  @Getter
  private boolean emailVerified;

  @Getter
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles;

  /**
   * Default constructor.
   */
  public User() {
    this.roles = new HashSet<>();
    this.isActive = true;
    this.emailVerified = false;
  }

  /**
   * Constructor with basic info.
   *
   * @param email     the email
   * @param password  the password
   * @param firstName the first name
   * @param lastName  the last name
   * @param phone     the phone
   */
  public User(String email, String password, String firstName, String lastName, String phone) {
    this();
    this.email = email;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.roles = new HashSet<>();
  }

  /**
   * Constructor with roles.
   *
   * @param email     the email
   * @param password  the password
   * @param firstName the first name
   * @param lastName  the last name
   * @param phone     the phone
   * @param roles     the list of roles
   */
  public User(String email, String password, String firstName, String lastName, String phone,
              List<Role> roles) {
    this(email, password, firstName, lastName, phone);
    addRoles(roles);
  }

  /**
   * Adds a role to the user.
   *
   * @param role the role to add
   * @return the user instance
   */
  public User addRole(Role role) {
    this.roles.add(role);
    return this;
  }

  /**
   * Adds multiple roles to the user.
   *
   * @param roles the list of roles to add
   * @return the user instance
   */
  public User addRoles(List<Role> roles) {
    var validatedRoleSet = Role.validateRoleSet(roles);
    this.roles.addAll(validatedRoleSet);
    return this;
  }

  /**
   * Verifies the user's email.
   */
  public void verifyEmail() {
    this.emailVerified = true;
  }

  /**
   * Deactivates the user account.
   */
  public void deactivateUser() {
    this.isActive = false;
  }

  /**
   * Activates the user account.
   */
  public void activateUser() {
    this.isActive = true;
  }

  /**
   * Updates the user's basic information.
   *
   * @param firstName the new first name
   * @param lastName  the new last name
   * @param phone     the new phone
   */
  public void updateUserInfo(String firstName, String lastName, String phone) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
  }
}

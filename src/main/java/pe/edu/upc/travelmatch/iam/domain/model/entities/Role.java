package pe.edu.upc.travelmatch.iam.domain.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;
import lombok.Getter;
import pe.edu.upc.travelmatch.iam.domain.model.valueobjects.Roles;
import pe.edu.upc.travelmatch.shared.domain.model.entities.AuditableModel;

/**
 * Role entity.
 */
@Entity
public class Role extends AuditableModel {

  @Id
  @Getter
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Getter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, unique = true, length = 20)
  private Roles name;

  /**
   * Default constructor.
   */
  public Role() {
  }

  /**
   * Constructor with name.
   *
   * @param name the role name
   */
  public Role(Roles name) {
    this.name = name;
  }

  /**
   * Get the role name as string.
   *
   * @return the string representation of the role name
   */
  public String getStringName() {
    return name.name();
  }

  /**
   * Get the default role (ROLE_TOURIST).
   *
   * @return the default role
   */
  public static Role getDefaultRole() {
    return new Role(Roles.ROLE_TOURIST);
  }

  /**
   * Convert a string name to a Role entity.
   *
   * @param name the string name
   * @return the Role entity
   */
  public static Role toRoleFromName(String name) {
    return new Role(Roles.valueOf(name));
  }

  /**
   * Validate a list of roles, returning a list with default role if empty.
   *
   * @param roles the list of roles to validate
   * @return the validated list of roles
   */
  public static List<Role> validateRoleSet(List<Role> roles) {
    if (roles == null || roles.isEmpty()) {
      return List.of(getDefaultRole());
    }
    return roles;
  }
}

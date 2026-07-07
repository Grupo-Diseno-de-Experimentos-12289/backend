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

/** Role type. */
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

  /** Constructs a new Role. */
  public Role() {}

  /** Constructs a new Role. */
  public Role(Roles name) {
    this.name = name;
  }

  public String getStringName() {
    return name.name();
  }

  public static Role getDefaultRole() {
    return new Role(Roles.ROLE_TOURIST);
  }

  /** To role from name. */
  public static Role toRoleFromName(String name) {
    return new Role(Roles.valueOf(name));
  }

  /** Validate role set. */
  public static List<Role> validateRoleSet(List<Role> roles) {
    if (roles == null || roles.isEmpty()) {
      return List.of(getDefaultRole());
    }
    return roles;
  }
}

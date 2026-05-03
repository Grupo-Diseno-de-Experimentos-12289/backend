package pe.edu.upc.travelmatch.agencies.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import pe.edu.upc.travelmatch.agencies.domain.model.valueobjects.AgencyName;
import pe.edu.upc.travelmatch.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Agency extends AuditableAbstractAggregateRoot<Agency> {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "agency_name"))
    })
    private AgencyName name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, unique = true)
    private String ruc;

    @Column(nullable = false)
    private String contactEmail;

    @Column(nullable = false)
    private String contactPhone;

    @Column(nullable = false)
    private Long userId;

    public Agency(AgencyName name, String description, String ruc, String contactEmail, String contactPhone, Long userId) {
        this.name = name;
        this.description = description;
        this.ruc = ruc;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.userId = userId;
    }

    public void updateDetails(AgencyName name, String description, String contactEmail, String contactPhone) {
        if (name != null) {
            this.name = name;
        }
        if (description != null) {
            this.description = description;
        }
        if (contactEmail != null) {
            this.contactEmail = contactEmail;
        }
        if (contactPhone != null) {
            this.contactPhone = contactPhone;
        }
    }

}
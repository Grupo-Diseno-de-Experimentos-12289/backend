package pe.edu.upc.travelmatch.agencies.domain.model.aggregates;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AgencyDocument extends AbstractAggregateRoot<AgencyDocument> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id", nullable = false)
    private Agency agency;

    @NotBlank
    @Column(nullable = false)
    private String documentType;

    @NotBlank
    @Column(nullable = false)
    private String documentUrl;

    @Column(nullable = true)
    private String description;

    public AgencyDocument(Agency agency, String documentType, String documentUrl, String description) {
        this.agency = agency;
        this.documentType = documentType;
        this.documentUrl = documentUrl;
        this.description = description;
    }

    public AgencyDocument update(String documentType, String documentUrl, String description) {
        this.documentType = documentType;
        this.documentUrl = documentUrl;
        this.description = description;
        return this;
    }
}
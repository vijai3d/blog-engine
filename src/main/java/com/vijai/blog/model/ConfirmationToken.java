package com.vijai.blog.model;

import com.vijai.blog.model.audit.DateAudit;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "confitmation_token")
public class ConfirmationToken extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="confirmation_token")
    private String confirmationToken;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public ConfirmationToken(Domain domain, User user) {
        this.user = user;
        super.setDomain(domain);
        confirmationToken = UUID.randomUUID().toString();
    }
}

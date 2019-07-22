package com.vijai.blog.model.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vijai.blog.model.Domain;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"domain"}, allowGetters = true
)
@Data
class BaseEntity {

    @Enumerated(EnumType.STRING)
    private Domain domain;

}

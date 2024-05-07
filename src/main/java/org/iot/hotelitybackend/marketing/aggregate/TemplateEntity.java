package org.iot.hotelitybackend.marketing.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "template_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TemplateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer templateCodePk;
    private String templateName;
    private String templateContent;

    @Builder
    public TemplateEntity(Integer templateCodePk, String templateName, String templateContent) {
        this.templateCodePk = templateCodePk;
        this.templateName = templateName;
        this.templateContent = templateContent;
    }
}

package org.iot.hotelitybackend.marketing.aggregate;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class CampaignSpecification {

    public static Specification<CampaignEntity> equalsCampaignSendType(String campaignSendType) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("campaignSendType"), campaignSendType);
    }

    public static Specification<CampaignEntity> equalsEmployeeCode(Integer employeeCodeFk) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("employeeCodeFk"), employeeCodeFk);
    }

    public static Specification<CampaignEntity> equalsCampaignSentDate(Date campaignSentDate) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("campaignSentDate"), campaignSentDate);
    }
}

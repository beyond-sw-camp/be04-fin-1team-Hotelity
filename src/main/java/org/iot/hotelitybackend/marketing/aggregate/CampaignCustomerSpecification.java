package org.iot.hotelitybackend.marketing.aggregate;

import java.time.LocalDateTime;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;
import org.springframework.data.jpa.domain.Specification;

public class CampaignCustomerSpecification {


	public static Specification<CampaignCustomerEntity> equalsCampaignCodeFk(Integer campaignCodeFk) {
		return (root, query, criteriaBuilder) -> {
			var subquery = query.subquery(Integer.class);
			var campaign = subquery.from(CampaignEntity.class);
			subquery.select(campaign.get("id"))
				.where(criteriaBuilder.equal(campaign.get("campaignCodeFk"), campaignCodeFk));
			return criteriaBuilder.in(root.get("campaignCodeFk")).value(subquery);
		};
	}

	public static Specification<CampaignCustomerEntity> equalsCampaignSendType(String campaignSendType) {
		return (root, query, criteriaBuilder) -> {
			var subquery = query.subquery(Integer.class);
			var campaign = subquery.from(CampaignEntity.class);
			subquery.select(campaign.get("id"))
				.where(criteriaBuilder.equal(campaign.get("campaignSendType"), campaignSendType));
			return criteriaBuilder.in(root.get("campaignCodeFk")).value(subquery);
		};
	}

	public static Specification<CampaignCustomerEntity> equalsCampaignSentDate(LocalDateTime campaignSentDate) {
		return (root, query, criteriaBuilder) -> {
			var subquery = query.subquery(Integer.class);
			var campaign = subquery.from(CampaignEntity.class);
			subquery.select(campaign.get("id"))
				.where(criteriaBuilder.equal(campaign.get("campaignSentDate"), campaignSentDate));
			return criteriaBuilder.in(root.get("campaignCodeFk")).value(subquery);
		};
	}

	public static Specification<CampaignCustomerEntity> likesCustomerName(String customerName) {
		return (root, query, criteriaBuilder) -> {
			var subquery = query.subquery(Integer.class);
			var customer = subquery.from(CustomerEntity.class);
			subquery.select(customer.get("id"))
				.where(criteriaBuilder.like(customer.get("customerName"), "%" + customerName + "%"));
			return criteriaBuilder.in(root.get("customerCodeFk")).value(subquery);
		};
	}

	public static Specification<CampaignCustomerEntity> likesCampaignSentStatus(Integer campaignSentStatus) {
		return (root, query, criteriaBuilder) -> {
			var subquery = query.subquery(Integer.class);
			var campaign = subquery.from(CampaignEntity.class);
			subquery.select(campaign.get("id"))
				.where(criteriaBuilder.equal(campaign.get("campaignSentStatus"), campaignSentStatus));
			return criteriaBuilder.in(root.get("campaignCodeFk")).value(subquery);
		};
	}

	public static Specification<CampaignCustomerEntity> likesTemplateFk(Integer templateCodeFk) {
		return (root, query, criteriaBuilder) -> {
			var subquery = query.subquery(Integer.class);
			var campaign = subquery.from(CampaignEntity.class);
			subquery.select(campaign.get("id"))
				.where(criteriaBuilder.equal(campaign.get("templateCodeFk"), templateCodeFk));
			return criteriaBuilder.in(root.get("campaignCodeFk")).value(subquery);
		};
	}


	public static Specification<CampaignCustomerEntity> likesTemplateName(String templateName) {
		return (root, query, criteriaBuilder) -> {
			var subquery = query.subquery(Integer.class);
			var campaign = subquery.from(CampaignEntity.class);
			var template = subquery.from(TemplateEntity.class);
			subquery.select(campaign.get("id"))
				.where(criteriaBuilder.and(
					criteriaBuilder.equal(campaign.get("templateCodeFk"), template.get("id")),
					criteriaBuilder.like(template.get("templateName"), "%" + templateName + "%")
				));
			return criteriaBuilder.in(root.get("campaignCodeFk")).value(subquery);
		};
	}

	public static Specification<CampaignCustomerEntity> likesCampaignTitle(String campaignTitle) {
		return (root, query, criteriaBuilder) -> {
			var subquery = query.subquery(Integer.class);
			var campaign = subquery.from(CampaignEntity.class);
			subquery.select(campaign.get("id"))
				.where(criteriaBuilder.like(campaign.get("campaignTitle"), "%" + campaignTitle + "%"));
			return criteriaBuilder.in(root.get("campaignCodeFk")).value(subquery);
		};
	}
}

package takeoff.logistics_service.msa.user.domain.repository;

import org.springframework.data.jpa.domain.Specification;
import takeoff.logistics_service.msa.user.domain.entity.DeliveryManager;
import takeoff.logistics_service.msa.user.domain.entity.User;
import takeoff.logistics_service.msa.user.domain.service.DeliveryManagerSearchCondition;
import takeoff.logistics_service.msa.user.domain.service.UserSearchCondition;

public class UserSpecifications {
    public static Specification<User> toUserSpecification(UserSearchCondition condition) {
        Specification<User> spec = (root, query, cb) -> cb.isNull(root.get("deletedAt"));
        if (condition.username() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("username"), condition.username()));
        }
        if (condition.email() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("slackEmail"), condition.email()));
        }
        if (condition.role() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("role"), condition.role()));
        }
        return spec;
    }

    public static Specification<DeliveryManager> toDeliveryManagerSpecification(DeliveryManagerSearchCondition condition) {
        Specification<DeliveryManager> spec = (root, query, cb) -> cb.isNull(root.get("deletedAt"));
        if (condition.hubId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("hubId"), condition.hubId()));
        }
        if (condition.deliveryManagerType() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("deliveryManagerType"), condition.deliveryManagerType()));
        }
        return spec;
    }
}
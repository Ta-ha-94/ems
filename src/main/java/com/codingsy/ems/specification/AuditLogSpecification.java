package com.codingsy.ems.specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.codingsy.ems.model.AuditLog;

//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.Predicate;
//import jakarta.persistence.criteria.Root;
/**
 * This class provides specifications for querying the {@link AuditLog} entity.
 * It includes methods for filtering audit logs by username, action, date range, and specific date.
 * 
 * @author Taha
 * @version 1.0
 */
public class AuditLogSpecification {
	/**
     * Creates a specification to filter audit logs by username.
     * The search is case-insensitive and supports partial matching.
     *
     * @param username The username to filter by. If null, no filtering is applied.
     * @return A {@link Specification} for filtering by username.
     */
	public static Specification<AuditLog> hasUsermae(String username){
		
//		Uses LIKE operator (%username%) for partial matching.
//		Converts both database and input username to lower case to make the query case-insensitive.
		
//		Equivalent SQL Query:
//		SELECT * FROM audit_log WHERE LOWER(username) LIKE '%username%';
		
//		Specification<AuditLog> specificationAnoImpl = new Specification<AuditLog>() {
//
//			@Override
//			public Predicate toPredicate(Root<AuditLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//				// TODO Auto-generated method stub
//				if(username == null) return null;
//				Predicate like = criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + username.toLowerCase() + "%");
//				return like;
//			}
//		};
//		
//		Specification<AuditLog> specificationLmabda = (r, q, c) -> username == null ? null : c.like(c.lower(r.get("username")), "%" + username.toLowerCase() + "%");
		
		return (r, q, c) -> username == null ? c.isTrue(c.literal(true)) : c.like(c.lower(r.get("username")), "%" + username.toLowerCase() + "%");
	}
	
	/**
     * Creates a specification to filter audit logs by action.
     * The search is case-insensitive and supports partial matching.
     *
     * @param action The action to filter by. If null, no filtering is applied.
     * @return A {@link Specification} for filtering by action.
     */
	public static Specification<AuditLog> hasAction(String action) {
		
//		Uses LIKE operator (%action%) for partial matching.
//		Converts both database and input action to lower case to make the query case-insensitive.
		
//		Equivalent SQL Query:
//		SELECT * FROM audit_log WHERE LOWER(action) LIKE '%action%';
		
//		Specification<AuditLog> specificationAnoImpl = new Specification<AuditLog>() {
//
//			@Override
//			public Predicate toPredicate(Root<AuditLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//				if(action == null) return null;
//				else
//					return criteriaBuilder.like(criteriaBuilder.lower(root.get("action")), "%" + action.toLowerCase() + "%");
//			}
//		};
		
//		Specification<AuditLog> specificationLambda = (r, q, c) -> action == null ? null : c.like(c.lower(r.get("action")), "%" + action.toLowerCase() + "%");
		
		return (r, q, c) -> action == null ? c.isTrue(c.literal(true)) : c.like(c.lower(r.get("action")), "%" + action.toLowerCase() + "%");
	}
	
	 /**
     * Creates a specification to filter audit logs by a date range.
     * The range is inclusive of both start and end dates.
     *
     * @param startDate The start date of the range. If null, no filtering is applied.
     * @param endDate The end date of the range. If null, no filtering is applied.
     * @return A {@link Specification} for filtering by date range.
     * @throws IllegalArgumentException If startDate is after endDate.
     */
	public static Specification<AuditLog> isBetweenTimeStamps(LocalDate startDate, LocalDate endDate) {

//		Uses BETWEEN operator for fetching records.
		
//		Equivalent SQL Query:
//		SELECT * FROM audit_log WHERE timestamp BETWEEN 'start' AND 'end';
		
//		Specification<AuditLog> specificationAnoImpl = new Specification<AuditLog>() {
//
//			@Override
//			public Predicate toPredicate(Root<AuditLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//				if(startTimestamp == null || endTimeStamp == null)
//					return null;
//				else
//					return criteriaBuilder.between(root.get("timestamp"), startTimestamp, endTimeStamp);
//			}
//		};
		
//		Specification<AuditLog> specificationAnoImpl = (r, q, c) ->	(start == null || end == null) ? c.isTrue(c.literal(true)) : c.between(r.get("timestamp"), start, end);
		
		return (root, query, criteriaBuilder) -> {
	        if (startDate == null || endDate == null) {
	            return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // No filtering
	        }
	        LocalDateTime start = startDate.atStartOfDay(); // Convert to start of day
	        LocalDateTime end = endDate.atTime(23, 59, 59); // Convert to end of day
	        return criteriaBuilder.between(root.get("timestamp"), start, end);
	    };
	}
	
	/**
     * Creates a specification to filter audit logs by a specific date.
     * The search includes the entire day (from 00:00:00 to 23:59:59).
     *
     * @param date The specific date to filter by. If null, no filtering is applied.
     * @return A {@link Specification} for filtering by a specific date.
     */
	public static Specification<AuditLog> isOnSpecificDate(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // No filtering
            }
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            return criteriaBuilder.between(root.get("timestamp"), startOfDay, endOfDay);
        };
    }
}

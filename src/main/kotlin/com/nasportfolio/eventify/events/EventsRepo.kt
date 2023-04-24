package com.nasportfolio.eventify.events

import com.nasportfolio.eventify.categories.models.Category
import com.nasportfolio.eventify.events.models.entities.EventEntity
import com.nasportfolio.eventify.events.models.entities.LocationEntity
import com.nasportfolio.eventify.events.models.requests.FilterRequestParam
import com.nasportfolio.eventify.events.models.requests.IncludeRequestParam
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.awt.Point
import java.util.*
import javax.persistence.criteria.Predicate

@Repository
interface EventsRepo : JpaRepository<EventEntity, String>, JpaSpecificationExecutor<EventEntity> {
    fun findByEndDateAfter(ends: Date = Date(), pageable: Pageable): Page<EventEntity>
}

fun EventsRepo.filterEvents(
    filterRequestParam: FilterRequestParam?,
    pageable: Pageable
): Page<EventEntity> {
    return findAll(
        filterEventsSpecification(filterRequestParam),
        pageable
    )
}

private fun filterEventsSpecification(filterRequestParam: FilterRequestParam?): Specification<EventEntity> {
    return Specification { root, query, criteriaBuilder ->
        var predicate: Predicate? = criteriaBuilder.conjunction()
        filterRequestParam?.apply {
            query.distinct(true)
            if (!filterRequestParam.query.isNullOrBlank()) {
                predicate = criteriaBuilder.or(
                    criteriaBuilder.like(root.get("title"), "%${filterRequestParam.query}%"),
                    criteriaBuilder.like(root.get("description"), "%${filterRequestParam.query}%"),
                )
            }
            starts?.let {
                predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), it)
                )
            }
            ends?.let {
                predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), it)
                )
            }
            created?.let {
                predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), it)
                )
            }
            updated?.let {
                predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), it)
                )
            }
            minPrice?.let {
                predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.greaterThanOrEqualTo(root.get("price"), it)
                )
            }
            maxPrice?.let {
                predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.lessThanOrEqualTo(root.get("price"), it)
                )
            }
            if (latitude != null && longitude != null) {
                val radius = radius ?: 5.0
                val location = root.get<LocationEntity>("location")
                val point = criteriaBuilder.function(
                    "POINT",
                    Point::class.java,
                    location.get<Double>("longitude"),
                    location.get<Double>("latitude")
                )
                val distance = criteriaBuilder.function(
                    "ST_DISTANCE_SPHERE",
                    Double::class.java,
                    point,
                    criteriaBuilder.function(
                        "POINT",
                        Point::class.java,
                        criteriaBuilder.literal(longitude),
                        criteriaBuilder.literal(latitude)
                    )
                )
                predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.le(distance, radius * 1000)
                )
            }
            postalCode?.let {
                predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.like(
                        root.get<LocationEntity>("location").get("postalCode"),
                        "%$postalCode%"
                    )
                )
            }
            address?.let {
                predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.like(
                        root.get<LocationEntity>("location").get("address"),
                        "%$address%"
                    )
                )
            }
            categories?.let { categories ->
                predicate = criteriaBuilder.and(
                    predicate,
                    root.join<EventEntity, Category>("categories")
                        .get<String>("name")
                        .`in`(categories)
                )
            }
            include?.let { includes ->
                if (includes.contains(IncludeRequestParam.all)) return@let
                val now = Date()
                val completed = includes.contains(IncludeRequestParam.completed)
                val active = includes.contains(IncludeRequestParam.active)
                val upcoming = includes.contains(IncludeRequestParam.upcoming)
                if (completed) {
                    predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), now)
                    )
                }
                if (active) {
                    predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), now),
                        criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), now)
                    )
                }
                if (upcoming) {
                    predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), now)
                    )
                }
            }
        }
        query.orderBy(criteriaBuilder.asc(root.get<Date>("endDate")))
        predicate
    }
}

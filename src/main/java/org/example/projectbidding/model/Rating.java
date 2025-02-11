package org.example.projectbidding.model;

import java.math.BigDecimal;

/**
 * Represents a contractor's rating in the project bidding system.
 * <p>
 * This record is immutable and stores a {@link BigDecimal} value representing the rating.
 *
 * @param rating The numerical rating of a contractor.
 */
public record Rating(BigDecimal rating) {

}

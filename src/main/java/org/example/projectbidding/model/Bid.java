package org.example.projectbidding.model;

import org.example.projectbidding.exception.EmptyFieldException;
import org.example.projectbidding.exception.InvalidBidAmountException;


import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a bid placed by a contractor on a project.
 * <p>
 * This class extends {@link Entity} and implements {@link Validatable} to ensure
 * bid details are correctly validated before being used.
 */

public final class Bid extends Entity implements Validatable {
    private Project project;
    private Contractor contractor;
    private BigDecimal bidAmount;
    private LocalDate bidDate;




    /**
     * Private constructor for {@code Bid}, used by the {@link BidBuilder}.
     *
     * @param builder The builder containing bid details.
     */

    private Bid(BidBuilder builder) {
        super(builder.id);
        this.project = builder.project;
        this.contractor = builder.contractor;
        this.bidAmount = builder.bidAmount;
        this.bidDate = builder.bidDate;
    }

    public Project getProject() {
        return project;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public BigDecimal getBidAmount() {
        return bidAmount;
    }

    public LocalDate getBidDate() {
        return bidDate;
    }

    /**
     * Builder class for constructing bid instances
     */
    public static class BidBuilder {
        private Long id;
        private Project project;
        private Contractor contractor;
        private BigDecimal bidAmount;
        private LocalDate bidDate;


        public BidBuilder setId(Long id) {
            this.id = id;
            return this;
        }


        public BidBuilder setProject(Project project) {
            this.project = project;
            return this;
        }


        public BidBuilder setContractor(Contractor contractor) {
            this.contractor = contractor;
            return this;
        }


        public BidBuilder setBidAmount(BigDecimal bidAmount) {
            this.bidAmount = bidAmount;
            return this;
        }


        public BidBuilder setBidDate(LocalDate bidDate) {
            this.bidDate = bidDate;
            return this;
        }

        public Bid build() {
            return new Bid(this);
        }
    }

    /**
     * Validates the bid details to ensure all fields are properly set.
     * <p>
     * - Ensures that the contractor, project, bid date, and bid amount are not null. <br>
     * - Checks that the bid amount is greater than zero. <br>
     *
     * @throws EmptyFieldException If any required field is empty.
     * @throws InvalidBidAmountException If the bid amount is not greater than zero.
     */
    @Override
    public void validate() throws  EmptyFieldException{
        if(contractor == null || bidDate == null || bidAmount == null || project == null) {
            throw new EmptyFieldException("One of the fields is empty! Please fill out all the fields!");
        }

        if(bidAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidBidAmountException("Bid amount must be greater than zero");
        }


    }


}

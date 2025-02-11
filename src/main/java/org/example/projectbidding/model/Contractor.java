package org.example.projectbidding.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents a contractor in the project bidding system.
 * <p>
 * A contractor can place bids on projects and has a rating based on their past performance.
 * This class extends {@link User} and provides additional attributes such as email, rating, and bid history.
 */
public final class Contractor extends User{
    private String email;
    private Rating rating;
    private List<Bid> bidList;



    private Contractor(ContractorBuilder builder) {
        super(builder.id, builder.username, builder.password);
        this.email = builder.email;
        this.rating = builder.rating;
        this.bidList = builder.bidList;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getRating() {
        return rating.rating();
    }

    public List<Bid> getBidList() {
        return bidList;
    }

    /**
     * Builder class for constructing a Contractor object.
     */
    public static class ContractorBuilder {
        private Long id;
        private String username;
        private String password;
        private String email;
        private Rating rating;
        private List<Bid> bidList;

        public ContractorBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public ContractorBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public ContractorBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public ContractorBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public ContractorBuilder setRating(BigDecimal rating) {
            this.rating = new Rating(rating);
            return this;
        }

        public ContractorBuilder setBidList(List<Bid> bidList) {
            this.bidList = bidList;
            return this;
        }

        public Contractor build() {
            return new Contractor(this);
        }
    }
}


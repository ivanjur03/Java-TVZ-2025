package org.example.projectbidding.model;





public abstract class Entity implements Identifiable {
    private Long id;

    protected Entity(Long id) {
        this.id = id;
    }


    @Override
    public Long getId() {
        return id;
    }


}

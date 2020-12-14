package ru.javaops.graduation.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "restaurants", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "restaurants_unique_name_idx")})
@Getter
@Setter
public class Restaurant extends AbstractNamedEntity {

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
    private boolean enabled = true;

    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    private Date registered = new Date();

    @OneToMany(fetch = LAZY, mappedBy = "restaurant")
    @OrderBy("date DESC")
    private List<Dish> dishes;

    @OneToMany(fetch = LAZY, mappedBy = "restaurant")
    @OrderBy("date DESC")
    private List<Vote> votes;

    public Restaurant() {
    }

    public Restaurant(Integer id, String name, boolean enabled, Date registered) {
        super(id, name);
        this.enabled = enabled;
        this.registered = registered;
    }

    public Restaurant(Integer id, String name) {
        this(id, name, true, new Date());
    }

    public Restaurant(Restaurant restaurant) {
        this(restaurant.getId(), restaurant.getName(), restaurant.isEnabled(), restaurant.getRegistered());
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name +
                "enabled=" + enabled +
                '}';
    }
}

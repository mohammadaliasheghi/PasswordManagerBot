package com.m2a.common;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@SequenceGenerator(
        name = "main_seq",
        sequenceName = "main",
        initialValue = 1000
)
public abstract class BasePO implements EntityID<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "main_seq")
    private Long id;

    @Transient
    public boolean isNew() {
        return getId() == null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        BasePO other = (BasePO) obj;
        if (id == null)
            return other.id == null;
        else return id.equals(other.id);
    }
}
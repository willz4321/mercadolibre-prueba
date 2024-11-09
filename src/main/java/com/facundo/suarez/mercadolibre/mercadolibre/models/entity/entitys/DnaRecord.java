package com.facundo.suarez.mercadolibre.mercadolibre.models.entity.entitys;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name ="DNAs")
public class DnaRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dnaSequence;
    private boolean isMutant;

    public DnaRecord() {
    }

    public Long getId() {
        return id;
    }

    public String getDnaSequence() {
        return dnaSequence;
    }

    public void setDnaSequence(String dnaSequence) {
        this.dnaSequence = dnaSequence;
    }

    public boolean isIsMutant() {
        return isMutant;
    }

    public void setIsMutant(boolean isMutant) {
        this.isMutant = isMutant;
    }
}

package com.facundo.suarez.mercadolibre.mercadolibre.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.facundo.suarez.mercadolibre.mercadolibre.models.entity.entitys.DnaRecord;

public interface IRecordsDao extends CrudRepository<DnaRecord, Long>{
    DnaRecord findByDnaSequence(String dnaSequence);
    long countByIsMutant(boolean isMutant);
}

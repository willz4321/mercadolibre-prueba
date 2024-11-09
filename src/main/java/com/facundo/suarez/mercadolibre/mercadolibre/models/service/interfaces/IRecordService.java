package com.facundo.suarez.mercadolibre.mercadolibre.models.service.interfaces;

import java.util.List;
import java.util.Map;

import com.facundo.suarez.mercadolibre.mercadolibre.models.entity.entitys.DnaRecord;

public interface IRecordService {
    public List<DnaRecord> findAll();
    public DnaRecord findById(Long id);
    public DnaRecord save(String[] dna);
    public void delete(Long id);
    public Map<String, Object> getStats();
}

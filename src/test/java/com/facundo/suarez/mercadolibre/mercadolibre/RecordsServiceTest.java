package com.facundo.suarez.mercadolibre.mercadolibre;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.facundo.suarez.mercadolibre.mercadolibre.models.dao.IRecordsDao;
import com.facundo.suarez.mercadolibre.mercadolibre.models.entity.entitys.DnaRecord;
import com.facundo.suarez.mercadolibre.mercadolibre.models.service.records.RecordsService;

@RunWith(MockitoJUnitRunner.class)
public class RecordsServiceTest {

    @Spy
    @InjectMocks
    private RecordsService recordService;

    @Mock
    private IRecordsDao recordsDao;

    //Esta prueba Simula el caso donde la secuencia de ADN ya está en la base de datos. Verifica que el método devuelve el registro existente sin intentar guardarlo nuevamente.
    @Test
    public void testSave_WhenDnaAlreadyExists() {
       
        String[] dna = {"ATCGGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        String dnaSequence = String.join(",", dna);

        DnaRecord existingRecord = new DnaRecord();
        existingRecord.setDnaSequence(dnaSequence);
        existingRecord.setIsMutant(true);

        when(recordsDao.findByDnaSequence(dnaSequence)).thenReturn(existingRecord);

        DnaRecord result = recordService.save(dna);

        assertEquals(existingRecord, result);
        verify(recordsDao, never()).save(any(DnaRecord.class));
    }

    // Este metodo Prueba el caso donde la secuencia de ADN es nueva y isMutant devuelve true. Verifica que el método crea y guarda el registro con isMutant en true.
    @Test
    public void testSave_WhenDnaIsNew_AndIsMutant() {
        String[] dna = {"ATCGGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        String dnaSequence = String.join(",", dna);

        when(recordsDao.findByDnaSequence(dnaSequence)).thenReturn(null);
        Mockito.doReturn(true).when(recordService).isMutant(dna);

        DnaRecord newRecord = new DnaRecord();
        newRecord.setDnaSequence(dnaSequence);
        newRecord.setIsMutant(true);

        when(recordsDao.save(any(DnaRecord.class))).thenReturn(newRecord);

        DnaRecord result = recordService.save(dna);

        assertEquals(dnaSequence, result.getDnaSequence());
        assertTrue(result.isIsMutant());

        verify(recordsDao).save(any(DnaRecord.class));
    }

    // Este metodo Prueba el caso cuando isMutant devuelve false. Repite el proceso del metodo anterior.
    @Test
    public void testSave_WhenDnaIsNew_AndIsNotMutant() {
        String[] dna = {"ATCGGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        String dnaSequence = String.join(",", dna);

        when(recordsDao.findByDnaSequence(dnaSequence)).thenReturn(null);
        Mockito.doReturn(false).when(recordService).isMutant(dna);

        DnaRecord newRecord = new DnaRecord();
        newRecord.setDnaSequence(dnaSequence);
        newRecord.setIsMutant(false);

        when(recordsDao.save(any(DnaRecord.class))).thenReturn(newRecord);

        DnaRecord result = recordService.save(dna);

        assertEquals(dnaSequence, result.getDnaSequence());
        assertFalse(result.isIsMutant());

        verify(recordsDao).save(any(DnaRecord.class));
    }
}

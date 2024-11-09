package com.facundo.suarez.mercadolibre.mercadolibre.models.service.records;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.facundo.suarez.mercadolibre.mercadolibre.models.dao.IRecordsDao;
import com.facundo.suarez.mercadolibre.mercadolibre.models.entity.entitys.DnaRecord;
import com.facundo.suarez.mercadolibre.mercadolibre.models.service.interfaces.IRecordService;

@Service
public class RecordsService implements  IRecordService{
    private final IRecordsDao recordsDao;

    
    public RecordsService(IRecordsDao recordsDao) {
        this.recordsDao = recordsDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DnaRecord> findAll() {
       return (List<DnaRecord>) recordsDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public DnaRecord findById(Long id) {
      return recordsDao.findById(id).orElse(null);
    }

    @Override
    public Map<String, Object> getStats() {
        long countMutantDna = recordsDao.countByIsMutant(true);
        long countHumanDna = recordsDao.countByIsMutant(false);
        double ratio = countHumanDna > 0 ? (double) countMutantDna / countHumanDna : 0;

        Map<String, Object> stats = new HashMap<>();
        stats.put("count_mutant_dna", countMutantDna);
        stats.put("count_human_dna", countHumanDna);
        stats.put("ratio", ratio);
        
        return stats;
    }


    @Override
    @Transactional
    public DnaRecord save(String[] dna) {
        DnaRecord record = new DnaRecord();

        // Verificar si la secuencia de ADN ya existe
         String dnaSequence = String.join(",", dna);
         DnaRecord existingRecord = recordsDao.findByDnaSequence(dnaSequence);
          if (existingRecord != null) { 
                return existingRecord; 
             }

        // Verifica si es mutante
        boolean isMutant = isMutant(dna);
        record.setDnaSequence(String.join(",", dna));
        
        // Guarda el resultado en el registro
        record.setIsMutant(isMutant);
        
        // Guarda el registro en la base de datos
        return recordsDao.save(record);
    }

    @Override
    @Transactional
    public void delete(Long id) {
       recordsDao.deleteById(id);
    }

    //METODOS AUXILIARES:
    public boolean isMutant(String[] dna) {
        int secuenciasEncontradas = 0;
        int tamano = dna.length;
    
        // Convertir el array de ADN en una matriz de caracteres
        char[][] matrizDna = new char[tamano][tamano];
        for (int i = 0; i < tamano; i++) {
            matrizDna[i] = dna[i].toCharArray();
        }
    
        System.out.println("Matriz de ADN:");
        for (int i = 0; i < tamano; i++) {
            for (int j = 0; j < tamano; j++) {
                System.out.print(matrizDna[i][j] + " ");
            }
            System.out.println();
        }
    
        // Buscar secuencias horizontales, verticales y diagonales
        for (int i = 0; i < tamano; i++) {
            for (int j = 0; j < tamano; j++) {
                if (j <= tamano - 4 && verificarSecuencia(matrizDna, i, j, 0, 1)) {
                    secuenciasEncontradas++;
                }
                if (i <= tamano - 4 && verificarSecuencia(matrizDna, i, j, 1, 0)) {
                    secuenciasEncontradas++;
                }
                if (i <= tamano - 4 && j <= tamano - 4 && verificarSecuencia(matrizDna, i, j, 1, 1)) {
                    secuenciasEncontradas++;
                }
                if (i <= tamano - 4 && j >= 3 && verificarSecuencia(matrizDna, i, j, 1, -1)) {
                    secuenciasEncontradas++;
                }
            }
        }
    
        System.out.println("Cantidad de secuencias encontradas: " + secuenciasEncontradas);
        return secuenciasEncontradas > 1; // Se Considera mutante si hay mas de una secuencia
    }
    
    private boolean verificarSecuencia(char[][] matriz, int fila, int columna, int direccionFila, int direccionColumna) {
        char base = matriz[fila][columna];
        for (int k = 1; k < 4; k++) {
            if (matriz[fila + k * direccionFila][columna + k * direccionColumna] != base) {
                return false;
            }
        }
        return true;
    }
}

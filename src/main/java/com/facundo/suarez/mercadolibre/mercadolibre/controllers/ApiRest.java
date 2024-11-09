package com.facundo.suarez.mercadolibre.mercadolibre.controllers;

import java.util.Arrays;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.facundo.suarez.mercadolibre.mercadolibre.controllers.DTO.DnaRequest;
import com.facundo.suarez.mercadolibre.mercadolibre.models.entity.entitys.DnaRecord;
import com.facundo.suarez.mercadolibre.mercadolibre.models.service.interfaces.IRecordService;

@RestController
@RequestMapping("/api")
public class ApiRest {
   private final IRecordService recordService;

    public ApiRest(IRecordService recordService) {
        this.recordService = recordService;
    }
   
    @GetMapping("/stats")
    public ResponseEntity<Object> statsDNA() {
       try {
        Map<String, Object> respuesta = recordService.getStats();
         return ResponseEntity.status(HttpStatus.OK)
                              .body(respuesta);
       } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Error: " + e.getMessage());
       }
    }

    @PostMapping("/mutant")
    public ResponseEntity<Object> saveRecord(@RequestBody DnaRequest dnaRequest) {
        try {
            String[] dnaArray = dnaRequest.getDna();
        
            // Verificación de matriz cuadrada
            int size = dnaArray.length;
            if (size < 4 || !Arrays.stream(dnaArray).allMatch(s -> s.length() == size)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body("Error: La secuencia de ADN debe ser una matriz cuadrada de al menos 4x4");
            }
        
            // Verificar y convertir a mayusculas, ademas de validar caracteres permitidos
            for (int i = 0; i < size; i++) {
                dnaArray[i] = dnaArray[i].toUpperCase();
                if (!dnaArray[i].matches("[ATCG]+")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                         .body("Error: La secuencia de ADN solo debe contener las letras A, T, C, G");
                }
            }
            DnaRecord record = recordService.save(dnaArray);
        
            String message = record.isIsMutant() ? "Se agregó un mutante" : "Se agregó un humano";
            return ResponseEntity.status(record.isIsMutant()  ? HttpStatus.OK : HttpStatus.FORBIDDEN)
                                 .body(message);
        
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error: " + e.getMessage());
        }
    }
    
}

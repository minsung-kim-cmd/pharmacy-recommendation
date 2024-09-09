package com.project.pharmacyrecommendation.pharmacy.service;

import com.project.pharmacyrecommendation.pharmacy.entity.Pharmacy;
import com.project.pharmacyrecommendation.pharmacy.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class PharmacyRepositoryService {
    private final PharmacyRepository pharmacyRepository;

    @Transactional
    public void updateAddress(Long id, String address){
        Pharmacy pharmacy = pharmacyRepository.findById(id).orElse(null);

        if(Objects.isNull(pharmacy)){
            log.error("[PharmacyRepositoryService updateAddress] Pharmacy with id {} not found", id);
            return;
        }

        pharmacy.changePharmacyAddress(address);
    }

    //for test
    public void updateAddressTest(Long id, String address){
        Pharmacy pharmacy = pharmacyRepository.findById(id).orElse(null);

        if(Objects.isNull(pharmacy)){
            log.error("[PharmacyRepositoryService updateAddressTest] Pharmacy with id {} not found", id);
            return;
        }

        pharmacy.changePharmacyAddress(address);
    }

    @Transactional(readOnly = true)
    public List<Pharmacy> findAll(){
        return pharmacyRepository.findAll();
    }
}

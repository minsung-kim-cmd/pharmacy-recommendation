package com.project.pharmacyrecommendation.pharmacy.repository;

import com.project.pharmacyrecommendation.AbstractIntegrationContainerBaseTest
import com.project.pharmacyrecommendation.pharmacy.entity.Pharmacy;
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDateTime;

class PharmacyRepositoryTest extends AbstractIntegrationContainerBaseTest {

    def setup(){ //각 테스트 메서드 실행 전에 해당 디비 초기화
        pharmacyRepository.deleteAll()
    }

    @Autowired
    private PharmacyRepository pharmacyRepository;

    def "PharmacyRepository save"(){
        given:
        String address = "서울 특별시 성북구 종암동"
        String name = "은혜 약국"
        double latitude = 36.11
        double longitude = 128.11

        def pharmacy = Pharmacy.builder()
        .pharmacyAddress(address)
        .pharmacyName(name)
        .latitude(latitude)
        .longitude(longitude)
        .build()

        when:
        def result = pharmacyRepository.save(pharmacy)

        then:
        result.getPharmacyAddress() == address
        result.getPharmacyName() == name
        result.getLatitude() == latitude
        result.getLongitude() == longitude

    }

    def "PharmacyRepository saveAll"(){
        given:
        String address = "서울 특별시 성북구 종암동"
        String name = "은혜 약국"
        double latitude = 36.11
        double longitude = 128.11

        def pharmacy = Pharmacy.builder()
                .pharmacyAddress(address)
                .pharmacyName(name)
                .latitude(latitude)
                .longitude(longitude)
                .build()

        when:
        pharmacyRepository.saveAll(Arrays.asList(pharmacy))
        def result = pharmacyRepository.findAll()

        then:
        result.size() == 1
    }

    def "BaseTimeEntity 등록"(){
        given:
        LocalDateTime now = LocalDateTime.now()
        String address = "서울 특별시 성북구 종암동"
        String name = "은혜약국"

        def pharmacy = Pharmacy.builder()
        .pharmacyAddress(address)
        .pharmacyName(name)
        .build()

        when:
        pharmacyRepository.save(pharmacy)
        def result = pharmacyRepository.findAll()

        then:
        result.get(0).getCreatedDate().isAfter(now)
        result.get(0).getModifiedDate().isAfter(now)
    }
}
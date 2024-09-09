package com.project.pharmacyrecommendation.api.service

import com.project.pharmacyrecommendation.AbstractIntegrationContainerBaseTest
import org.springframework.beans.factory.annotation.Autowired

class KakaoAddressSearchServiceTest extends AbstractIntegrationContainerBaseTest{

    @Autowired
    private KakaoAddressSearchService kakaoAddressSearchService;

    def "address 파라미터 값이 null이면, requestAddressSearch 메소드는 null을 반환한다."(){
        given:
        String address = null

        when:
        def result = kakaoAddressSearchService.requestAddressSearch(address)
        then:
        result == null
    }

    def "주소값이 valid 한다면, requestAddressSearch 메소드는 정상적으로 document를 반환한다" (){
        given:
        def address = "전북 삼성동 100"

        when:
        def result = kakaoAddressSearchService.requestAddressSearch(address)

        then:
        result.documentDtoList.size() > 0
        result.metaDto.totalCount > 0
        result.documentDtoList.get(0).addressName != null
    }
}

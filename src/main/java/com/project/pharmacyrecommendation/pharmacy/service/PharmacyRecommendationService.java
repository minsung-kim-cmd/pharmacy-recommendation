package com.project.pharmacyrecommendation.pharmacy.service;

import com.project.pharmacyrecommendation.api.dto.DocumentDto;
import com.project.pharmacyrecommendation.api.dto.KakaoApiResponseDto;
import com.project.pharmacyrecommendation.api.service.KakaoAddressSearchService;
import com.project.pharmacyrecommendation.direction.entity.Direction;
import com.project.pharmacyrecommendation.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class PharmacyRecommendationService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;

    public void recommendPharmacyList(String address){
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if(Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentDtoList())){
            log.error("[PharmacyRecommendationService recommendPharmacyList ]Kakao address search failed, Input Address : {}",address);
            return;
        }

        DocumentDto documentDto = kakaoApiResponseDto.getDocumentDtoList().get(0);
        //List<Direction> directionList = directionService.buildDirectionList(documentDto);
        List<Direction> directionList = directionService.buildDirectionListByCategoryApi(documentDto);
        directionService.saveAll(directionList);
    }
}

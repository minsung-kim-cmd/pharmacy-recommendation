package com.project.pharmacyrecommendation.pharmacy.service;

import com.project.pharmacyrecommendation.api.dto.DocumentDto;
import com.project.pharmacyrecommendation.api.dto.KakaoApiResponseDto;
import com.project.pharmacyrecommendation.api.service.KakaoAddressSearchService;
import com.project.pharmacyrecommendation.direction.dto.OutputDto;
import com.project.pharmacyrecommendation.direction.entity.Direction;
import com.project.pharmacyrecommendation.direction.service.Base62Service;
import com.project.pharmacyrecommendation.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PharmacyRecommendationService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;
    private final Base62Service base62Service;

    @Value("${pharmacy.recommendation.base.url}")
    private String baseUrl;

    private static final String ROAD_VIEW_BASE_URL = "https://map.kakao.com/link/roadview/";

    public List<OutputDto> recommendPharmacyList(String address){
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if(Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentDtoList())){
            log.error("[PharmacyRecommendationService recommendPharmacyList ]Kakao address search failed, Input Address : {}",address);
            return Collections.emptyList();
        }

        DocumentDto documentDto = kakaoApiResponseDto.getDocumentDtoList().get(0);
        //List<Direction> directionList = directionService.buildDirectionList(documentDto);
        List<Direction> directionList = directionService.buildDirectionListByCategoryApi(documentDto);
        return directionService.saveAll(directionList)
                .stream()
                .map(this::convertToOutputDto)
                .collect(Collectors.toList());
    }

    private OutputDto convertToOutputDto(Direction direction){
        return OutputDto.builder()
                .pharmacyName(direction.getTargetPharmacyName())
                .pharmacyAddress(direction.getTargetAddress())
                .directionUrl(baseUrl + base62Service.encodeDirectionId(direction.getId()))
                .roadViewUrl(ROAD_VIEW_BASE_URL + direction.getTargetLatitude() + "," + direction.getTargetLongitude())
                .distance(String.format("%.2f km", direction.getDistance()))
                .latitude(direction.getTargetLatitude())
                .longitude(direction.getTargetLongitude())
                .build();
    }
}

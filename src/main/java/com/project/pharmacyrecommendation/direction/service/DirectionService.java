package com.project.pharmacyrecommendation.direction.service;

import com.project.pharmacyrecommendation.api.dto.DocumentDto;
import com.project.pharmacyrecommendation.api.service.KakaoCategorySearchService;
import com.project.pharmacyrecommendation.direction.entity.Direction;
import com.project.pharmacyrecommendation.direction.repository.DirectionRepository;
import com.project.pharmacyrecommendation.pharmacy.service.PharmacySearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectionService {

    // 최대 약국 검색 갯수
    private static final int MAX_SEARCH_COUNT = 5;

    // 반경 10km 이내 약국만 추천
    private static final double RADIUS_KM = 10.0;

    private final PharmacySearchService pharmacySearchService;
    private final DirectionRepository directionRepository;
    private final Base62Service base62Service;
    private final KakaoCategorySearchService kakaoCategorySearchService;

    @Transactional
    public List<Direction> saveAll(List<Direction> directions) {
        if(CollectionUtils.isEmpty(directions)){ return Collections.emptyList(); }
        return directionRepository.saveAll(directions);
    }

    public Direction findById(String encodedId){
        Long decodedId = base62Service.decodeDirectionId(encodedId);
        return directionRepository.findById(decodedId).orElse(null);
    }

    public List<Direction> buildDirectionList(DocumentDto documentDto) {
        if(Objects.isNull(documentDto)) {return Collections.emptyList();}

        return pharmacySearchService.searchPharmacyDtoList().stream()
                .map(pharmacyDto ->
                        Direction.builder()
                                .inputAddress(documentDto.getAddressName())
                                .inputLatitude(documentDto.getLatitude())
                                .inputLongitude(documentDto.getLongitude())
                                .targetPharmacyName(pharmacyDto.getPharmacyName())
                                .targetAddress(pharmacyDto.getPharmacyAddress())
                                .targetLatitude(pharmacyDto.getLatitude())
                                .targetLongitude(pharmacyDto.getLongitude())
                                .distance(
                                        calculateDistance(documentDto.getLatitude(), documentDto.getLongitude(), pharmacyDto.getLatitude(), pharmacyDto.getLongitude())
                                )
                                .build()
                        )
                .filter(direction -> direction.getDistance() <= RADIUS_KM)  //최대 거리 이하인 목록만 추출
                .sorted(Comparator.comparing(Direction::getDistance))   //거리가 가까운 기준으로 정렬
                .limit(MAX_SEARCH_COUNT)                                //최대 약국 검색 개수만큼 제한
                .collect(Collectors.toList());

    }

    // pharmacy search by category kakao api
    public List<Direction> buildDirectionListByCategoryApi(DocumentDto inputDocumentDto) {
        if(Objects.isNull(inputDocumentDto)) return Collections.emptyList();

        return kakaoCategorySearchService
                .requestPharmacyCategorySearch(inputDocumentDto.getLatitude(), inputDocumentDto.getLongitude(), RADIUS_KM)
                .getDocumentDtoList()
                .stream().map(resultDocumentDto ->
                        Direction.builder()
                                .inputAddress(inputDocumentDto.getAddressName())
                                .inputLatitude(inputDocumentDto.getLatitude())
                                .inputLongitude(inputDocumentDto.getLongitude())
                                .targetPharmacyName(resultDocumentDto.getPlaceName())
                                .targetAddress(resultDocumentDto.getAddressName())
                                .targetLatitude(resultDocumentDto.getLatitude())
                                .targetLongitude(resultDocumentDto.getLongitude())
                                .distance(resultDocumentDto.getDistance() * 0.001) // km 단위로 변경
                                .build())
                .limit(MAX_SEARCH_COUNT)
                .collect(Collectors.toList());
    }

    // Haversine formula
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371; //Kilometers
        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
    }
}

package com.project.pharmacyrecommendation.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@Slf4j
public class KakaoUriBuilderService {

    private static final String KAKAO_LOCAL_SEARCH_ADDRESS_URL = "https://dapi.kakao.com/v2/local/search/address.json";

    private static final String KAKAO_LOCAL_CATEGORY_SEARCH_URL = "https://dapi.kakao.com/v2/local/search/category.json";

    public URI buildUriByAddressSearch(String address){
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_SEARCH_ADDRESS_URL);
        uriComponentsBuilder.queryParam("query", address);

        URI uri = uriComponentsBuilder.build().encode().toUri();
        log.info("[KakaoUriBuilderService buildUriByAddressSearch] address : {}, Kakao URI: {}", address,uri);
        return uri;
    }

    public URI buildUriByCategorySearch(double latitude, double longitude, double radius, String category){

        double meterRadius = radius * 1000;

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_CATEGORY_SEARCH_URL);
        uriComponentsBuilder.queryParam("category_group_code", category);
        uriComponentsBuilder.queryParam("y", latitude);
        uriComponentsBuilder.queryParam("x", longitude);
        uriComponentsBuilder.queryParam("radius", meterRadius);
        uriComponentsBuilder.queryParam("sort","distance");

        URI uri = uriComponentsBuilder.build().encode().toUri();
        log.info("[KakaoUriBuilderService buildUriByAddressSearch] Kakao URI: {}",uri);
        return uri;
    }
}

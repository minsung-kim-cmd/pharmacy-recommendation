package com.project.pharmacyrecommendation.direction.controller;

import com.project.pharmacyrecommendation.direction.entity.Direction;
import com.project.pharmacyrecommendation.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@Slf4j
@RequiredArgsConstructor
public class DirectionController {

    private final DirectionService directionService;
    private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";

    @GetMapping("/dir/{encodedId}")
    public String searchDirection(@PathVariable("encodedId") String encodedId){
        Direction direction = directionService.findById(encodedId);
        String params = String.join(",",
                direction.getTargetPharmacyName(),
                String.valueOf(direction.getTargetLatitude()),
                String.valueOf(direction.getTargetLongitude()));
        String result = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL+params).toUriString();

        return "redirect:"+result;
    }

}

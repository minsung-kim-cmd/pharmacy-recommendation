package com.project.pharmacyrecommendation.direction.controller;

import com.project.pharmacyrecommendation.direction.dto.InputDto;
import com.project.pharmacyrecommendation.direction.dto.OutputDto;
import com.project.pharmacyrecommendation.pharmacy.service.PharmacyRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FormController {

    private final PharmacyRecommendationService pharmacyRecommendationService;

    @GetMapping("/")
    public String main() {return "main";}

    @PostMapping("/search")
    @ResponseBody
    public List<OutputDto> postDirection(String address) {
        /*ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("output");
        modelAndView.addObject("outputFormList", pharmacyRecommendationService.recommendPharmacyList(inputDto.getAddress()));*/

        return pharmacyRecommendationService.recommendPharmacyList(address);
    }


}

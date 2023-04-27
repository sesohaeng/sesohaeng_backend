package sesohaeng.sesohaengbackend.controller.cafe;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sesohaeng.sesohaengbackend.dto.response.cafe.CafeResponseDto;
import sesohaeng.sesohaengbackend.response.CommonResponse;
import sesohaeng.sesohaengbackend.response.ListResponse;
import sesohaeng.sesohaengbackend.service.cafe.CafeService;
import sesohaeng.sesohaengbackend.service.cafe.CafeServiceImpl;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class CafeController {
    private CafeService cafeService;

    @GetMapping("/area/{areaId}/cafe")
    public final CommonResponse getCafesByArea(@PathVariable Long areaId){
        return ListResponse.<CafeResponseDto>builder()
                .success(true)
                .status(200)
                .message("해당 특구에 속하는 카페들을 가져오는데 성공했습니다.")
                .result(cafeService.getCafesByArea(areaId))
                .build();
    }
}

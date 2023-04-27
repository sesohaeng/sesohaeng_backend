package sesohaeng.sesohaengbackend.service.area;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import sesohaeng.sesohaengbackend.domain.area.Area;
import sesohaeng.sesohaengbackend.domain.area.AreaRepository;
import sesohaeng.sesohaengbackend.domain.place.Place;
import sesohaeng.sesohaengbackend.dto.response.area.AreaResponseDto;

import javax.persistence.*;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AreaServiceTest {

    // Mock 빈을 주립받을 대상
    @InjectMocks
    AreaServiceImpl areaService;

    // Mock으로 만들 대상
    @Mock
    AreaRepository areaRepository;


    @BeforeEach
    void init(){


    }



    @DisplayName("Get Area List")
    @Test
    void getAreas() {
        // create a list of areas to be returned by the mocked repository
        List<Area> areaList = new ArrayList<>();
        Area area = Area.newInstance("AreaSeYeol", 20172894.11, 20172894.22);
        areaList.add(area);

        // define the expected behavior of the mocked repository
        // mock 레포지토리가 어떤 행동을 하길 원하는지 정의해라
        when(areaRepository.findAll()).thenReturn(areaList);

        // when
        List<AreaResponseDto> areas = areaService.getAreas();

        // then
        assertThat(areas.get(0).getAreaName()).isEqualTo("AreaSeYeol");
    }

    @Test
    void getArea() {
        // stubbing
        Area area = Area.newTestInstance(20172894L,"AreaSeYeol", 20172894.11, 20172894.22);
        Optional<Area> optionalArea = Optional.of(area);
        when(areaRepository.findById(20172894L)).thenReturn(optionalArea);

        // 진짜 매서드
        AreaResponseDto areaResponseDto = areaService.getArea(20172894L);

        // then
        assertThat(areaResponseDto.getAreaName()).isEqualTo("AreaSeYeol");

    }
}
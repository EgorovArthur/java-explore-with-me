package ru.yandex.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    @NotBlank
    @Length(max = 50)
    private String title; //Заголовок подборки
    private Boolean pinned; //Закреплена ли подборка на главной странице сайта
    private List<Long> events; //Список идентификаторов событий входящих в подборку

}

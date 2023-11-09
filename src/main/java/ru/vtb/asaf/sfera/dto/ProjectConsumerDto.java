package ru.vtb.asaf.sfera.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectConsumerDto {

    @JsonProperty("CODE")
    private String code;
    @JsonProperty("NAME")
    private String name;
}

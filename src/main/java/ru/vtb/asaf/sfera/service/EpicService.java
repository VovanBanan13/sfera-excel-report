package ru.vtb.asaf.sfera.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.vtb.asaf.sfera.dto.ParentChainDto;
import ru.vtb.asaf.sfera.util.Constant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EpicService {

    RestTemplate restTemplate = new RestTemplate();

    public String getEpicNumber(HttpEntity<String> requestEntity, String taskName) {
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(Constant.PARENT_CHAIN_GET_URL)
                .build();
        Map<String, String> variables = new HashMap<>();
        variables.put("taskName", taskName);
        ResponseEntity<List<ParentChainDto>> responseEntity = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<ParentChainDto>>() {}, variables);
        if (responseEntity.getBody() != null) {
            return getValueNotNull(responseEntity.getBody()
                    .stream()
                    .filter(chain -> "epic".equalsIgnoreCase(chain.getType()))
                    .findFirst()
                    .orElse(null));
        } else {
            return "";
        }
    }

    private static String getValueNotNull(ParentChainDto value) {
        if (value != null) {
            return value.getNumber();
        } else {
            return "";
        }
    }

}

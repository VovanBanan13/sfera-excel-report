package ru.vtb.asaf.sfera.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskHistoryDto {
    private List<Content> content;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content {
        private String timestamp;
        private List<Change> changes;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Change {
            private String code;
            private String name;
            private Values before;
            private Values after;

            @Data
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Values {
                private List<Value> values;

                @Data
                @Builder
                @NoArgsConstructor
                @AllArgsConstructor
                public static class Value {
                    private String type;
                    private String value;
                }
            }
        }
    }
}

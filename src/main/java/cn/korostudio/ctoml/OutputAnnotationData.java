package cn.korostudio.ctoml;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OutputAnnotationData {
    String value;
    Location at;
    Object data;
}

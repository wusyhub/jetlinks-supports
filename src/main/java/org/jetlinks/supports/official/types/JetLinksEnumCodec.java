package org.jetlinks.supports.official.types;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.jetlinks.core.metadata.types.EnumType;

import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Getter
@Setter
public class JetLinksEnumCodec extends AbstractDataTypeCodec<EnumType> {

    @Override
    public String getTypeId() {
        return EnumType.ID;
    }

    @Override
    public EnumType decode(EnumType type, Map<String, Object> config) {
        super.decode(type,config);
        JSONObject jsonObject = new JSONObject(config);

        ofNullable(jsonObject.getJSONArray("enums"))
                .map(list -> list.stream()
                        .map(JSONObject.class::cast)
                        .map(e -> EnumType.Element.of(e.getString("value"), e.getString("text")))
                        .collect(Collectors.toList()))
                .ifPresent(type::setElements);

        return type;
    }

    @Override
    protected void doEncode(Map<String, Object> encoded, EnumType type) {
        super.doEncode(encoded, type);
        encoded.put("enums", type.getElements()
                .stream()
                .map(EnumType.Element::toMap).collect(Collectors.toList()));

    }
}
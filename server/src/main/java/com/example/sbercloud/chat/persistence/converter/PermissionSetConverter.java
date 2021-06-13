package com.example.sbercloud.chat.persistence.converter;

import com.example.sbercloud.chat.model.Permission;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.example.sbercloud.chat.utility.Constant.DELIMITER_CHAR;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author Bulygin D.N.
 * @since 13.06.2021
 */
public class PermissionSetConverter implements AttributeConverter<Set<Permission>, String> {

    @Override
    public String convertToDatabaseColumn(Set<Permission> permissions) {
        String columnValue;
        if(permissions != null) {
            columnValue = permissions.stream()
                    .map(Enum::name)
                    .collect(joining(DELIMITER_CHAR));
        } else {
            columnValue = EMPTY;
        }
        return columnValue;
    }

    @Override
    public Set<Permission> convertToEntityAttribute(String columnValue) {
        Set<Permission> permissions;
        if (StringUtils.isNotBlank(columnValue)) {
            permissions = Arrays.stream(columnValue.split(DELIMITER_CHAR))
                    .filter(permissionName -> Arrays.stream(Permission.values())
                            .anyMatch(permission -> permission.name().equalsIgnoreCase(permissionName)))
                    .map(Permission::valueOf)
                    .collect(toSet());
        } else {
            permissions = new HashSet<>();
        }
        return permissions;
    }
}

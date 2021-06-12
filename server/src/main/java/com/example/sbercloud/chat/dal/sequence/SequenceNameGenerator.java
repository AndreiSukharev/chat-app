package com.example.sbercloud.chat.dal.sequence;

import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.util.Properties;

import static org.hibernate.internal.util.StringHelper.isEmpty;
import static org.hibernate.internal.util.StringHelper.isNotEmpty;

/**
 * Настройка параметров для sequences из базы данных.
 * Устанавливает sequences вида: table_name_id_seq
 */
public class SequenceNameGenerator extends SequenceStyleGenerator {
    private static final String SEQUENCE_POSTFIX = "_id_seq";
    public static final String GENERATOR_NAME = "generator";
    public static final String STRATEGY_NAME = "com.example.sbercloud.chat.dal.sequence.SequenceNameGenerator";

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) {
        if (isEmpty(params.getProperty(SEQUENCE_PARAM))) {
            String tableName = params.getProperty(PersistentIdentifierGenerator.TABLE);
            if (isNotEmpty(tableName)) {
                String sequenceName = tableName + SEQUENCE_POSTFIX;
                params.setProperty(SEQUENCE_PARAM, sequenceName);
            }
        }
        super.configure(type, params, serviceRegistry);
    }
}

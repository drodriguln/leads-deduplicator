package com.developer.drodriguln;

import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
class LeadsProcessor {

    enum IdType {
        ID, EMAIL
    }

    static List<Lead> deduplicate(IdType idType, List<Lead> leads) {
        Map<String, Lead> map = new LinkedHashMap<>();
        for (Lead lead : leads) {
            String key = idType == IdType.ID ? lead.getId() : lead.getEmail();
            if (!map.containsKey(key)) {
                map.put(key, lead);
            } else {
                LOGGER.info("Found duplicate [{}] key: [{}].", idType, key);
                Lead currentLead = map.get(key);
                OffsetDateTime storedDate = currentLead.getEntryDate();
                OffsetDateTime selectedDate = lead.getEntryDate();
                if (storedDate.isBefore(selectedDate)) {
                    LOGGER.info("Removing older record: [{}].", currentLead);
                    LOGGER.info("Storing newer record: [{}].", lead);
                    map.put(key, lead);
                } else if (storedDate.isEqual(selectedDate)) {
                    LOGGER.info("Removing record first in the list: [{}].", currentLead);
                    LOGGER.info("Storing newer record: [{}].", lead);
                    map.put(key, lead);
                }
            }
        }
        return new ArrayList<>(map.values());
    }

}

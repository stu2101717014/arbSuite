package org.example.commons.services;



import org.example.commons.entities.TableTennisEventEntity;

import java.util.Map;
import java.util.Set;

public interface DataNormalizationService {
    Set<TableTennisEventEntity> normalise(Map map);
}
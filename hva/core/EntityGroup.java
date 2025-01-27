package hva.core;

import java.util.*;


public class EntityGroup<T extends Entity> implements java.io.Serializable {
    private List<T> _entities = new ArrayList<>();

    public void addEntity(T entity) {
        _entities.add(entity);
    }

    public boolean checkExists(String id) {
        for (T entity : _entities) {
            if (entity.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public T getEntity(String id) {
        return _entities.stream()
                .filter(entity -> entity.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null); 
    }

    public List<T> getEntities() {
        return new ArrayList<>(_entities);
    }
}
package pt.psoft.g1.psoftg1.shared.services;

import java.util.List;

public interface TopXService<T> {
    List<T> findTopX();
    void updateTopX();
}

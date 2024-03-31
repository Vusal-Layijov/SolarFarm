package solar.data;

import solar.models.Panel;

import java.util.List;

public interface PanelRepository {

    List<Panel> findBySection(String section) throws DataException;
    Panel add(Panel panel) throws  DataException;
    boolean update(Panel panel) throws DataException;
    boolean deleteById(int panelId) throws DataException;
    List<Panel> findAll() throws DataException;
    Panel findById(int panelId) throws DataException;
}

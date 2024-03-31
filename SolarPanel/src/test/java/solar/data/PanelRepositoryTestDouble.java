package solar.data;

import solar.models.Material;
import solar.models.Panel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PanelRepositoryTestDouble implements PanelRepository{
    @Override
    public List<Panel> findBySection(String section) throws DataException {
        List<Panel> result= new ArrayList<>();
        for(Panel panel : findAll()){
            if(panel != null && Objects.equals(panel.getSection(), section)){
                result.add(panel);
            }
        }
        return result;
    }

    @Override
    public Panel add(Panel panel) throws DataException {
        panel.setId(99);
        return panel;
    }

    @Override
    public boolean update(Panel panel) throws DataException {
        return panel.getId()>0;
    }

    @Override
    public boolean deleteById(int panelId) throws DataException {
        return panelId !=999;
    }

    @Override
    public List<Panel> findAll() throws DataException {
        List<Panel> all = new ArrayList<>();
        all.add(new Panel(1, "A1", 5, 10, 2019, Material.MONOCRYSTALLINE_SILICON, true));
        all.add(new Panel(2, "B2", 3, 8, 2020, Material.MULTICRYSTALLINE_SILICON, false));
        all.add(new Panel(3, "C3", 6, 12, 2018, Material.AMORPHOUS_SILICON, true));
        return all;
    }

    @Override
    public Panel findById(int panelId) throws DataException {
        List<Panel> all = findAll();
        for(Panel panel : all){
            if(panel.getId()==panelId){
                return panel;
            }
        }

        return null;
    }
}

package solar.domain;

import solar.data.DataException;
import solar.data.PanelRepository;
import solar.models.Panel;

import java.util.List;

public class PanelService {
    private final PanelRepository repository;

    public PanelService(PanelRepository repository) {
        this.repository = repository;
    }
    public List<Panel> findAll() throws DataException{
        return repository.findAll();
    }
    public Panel findById(int panelId) throws DataException{
        return repository.findById(panelId);
    }
    public List<Panel> findBySection(String section) throws DataException{
        return  repository.findBySection(section);
    }
    public PanelResult add (Panel panel) throws DataException{
        PanelResult result = validate(panel);
        if(!result.isSuccess()){
            return result;
        }
        if(panel==null){
            result.addMessage("Panel cannot be null");
            return result;
        }
        if(panel.getId()>0){
            result.addMessage("Cannot add existing panel");
            return result;
        }
        panel=repository.add(panel);
        result.setPanel(panel);
        return result;
    }

    public PanelResult update(Panel panel) throws DataException {
        PanelResult result = validate(panel);
        if(!result.isSuccess()){
            return result;
        }
        boolean updated= repository.update(panel);
        if(!updated){
            result.addMessage(String.format("Panel with id: %s does not exist", panel.getId()));
        }
        result.setPanel(panel);
        return result;
    }
    public PanelResult deleteById(int PanelId) throws DataException {
        PanelResult result = new PanelResult();
        if(!repository.deleteById(PanelId)){
            result.addMessage(String.format("Panel with id: %s does not exist", PanelId));
        }
        return result;
    }
    public static Boolean stringToBoolean(String input) {
        if ("true".equalsIgnoreCase(input)) {
            return Boolean.TRUE;
        } else if ("false".equalsIgnoreCase(input)) {
            return Boolean.FALSE;
        } else {
            return null; // Or throw an IllegalArgumentException
        }
    }
    public PanelResult validate(Panel panel) throws DataException {
        PanelResult result = new PanelResult();

        if(panel == null){
            result.addMessage("Result can not be null");
            return result;
        }
        if(panel.getSection() == null || panel.getSection().isBlank()){
            result.addMessage("Section is required");
            return result;
        }
        if(panel.getRow()<=0 || panel.getRow()>250){
            result.addMessage("Row must be between: [1, 250]");
            return result;
        }
        if(panel.getColumn()<=0 || panel.getColumn()>250){
            result.addMessage("Column must be between: [1, 250]");
            return result;
        }
        if(panel.getInstallationYear()>2024){
            result.addMessage("Year Installed must be in the past.");
            return result;
        }
        if(panel.getMaterial()==null){
            result.addMessage("Material is required and can only be one of the five materials: MULTICRYSTALLINE_SILICON, MONOCRYSTALLINE_SILICON, AMORPHOUS_SILICON, CADMIUM_TELLURIDE, COPPER_INDIUM_GALLIUM_SELENIDE");
            return result;
        }
        Boolean tracking = stringToBoolean(String.valueOf(panel.isTracking()) );
        if(tracking==null){
            result.addMessage("Is Tracking is required: true or false");
            return result;
        }
        for(Panel p : findAll()){
            if(p.getSection()==panel.getSection() && p.getRow()==panel.getRow() && p.getColumn()==panel.getColumn()){
                result.addMessage("This spot is already taken");
                return result;
            }
        }




        return result;
    }

}

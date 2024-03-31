package solar.data;

import solar.models.Material;
import solar.models.Panel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PanelFileRepository implements PanelRepository {
    private final String filePath;

    private static final String DELIMITER=",";
    private static final String DELIMITER_REPLACEMENT="@@@";

    public PanelFileRepository(String filePath) {
        this.filePath = filePath;
    }
    @Override
    public List<Panel> findAll() throws DataException {
        List<Panel> results = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            for (String line = reader.readLine(); line!= null; line=reader.readLine()){
                Panel panel = lineToPanel(line);
                results.add(panel);
            }


        }catch (FileNotFoundException ex){

        }catch (IOException ex){
            throw new DataException("Could not open file path: "+ filePath);
        }
        return results;
    }

    @Override
    public List<Panel> findBySection(String section) throws DataException {
        List<Panel> results= new ArrayList<>();
        for(Panel panel: findAll()){
            if (panel != null && Objects.equals(panel.getSection(), section)){
                results.add(panel);
            }
        }
        return results;
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

    @Override
    public Panel add(Panel panel) throws DataException {
        List<Panel> all = findAll();
        int id = getNextId(all);
        panel.setId(id);
        all.add(panel);
        writeToFile(all);
        return panel;
    }

    @Override
    public boolean update(Panel panel) throws DataException {
        List<Panel> all = findAll();
        for(int i=0; i<all.size(); i++){
            if(all.get(i).getId()==panel.getId()){
                all.set(i,panel);
                writeToFile(all);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean deleteById(int panelId) throws DataException {
        List<Panel> all = findAll();
        for(int i=0; i<all.size(); i++){
            if(all.get(i).getId()==panelId){
                all.remove(i);
                writeToFile(all);
                return true;
            }
        }

        return false;
    }

    private void writeToFile(List<Panel> panels) throws DataException {
        try (PrintWriter writer = new PrintWriter(filePath)){
            writer.println("id,section,row,column,installationYear,material,tracking");
            for(Panel panel : panels){
                writer.println(panelToLine(panel));
            }
        }catch (IOException ex){
            throw new DataException("Could not write to filepath: "+filePath);
        }
    }

    private Panel lineToPanel(String line){
        String [] fields = line.split(DELIMITER);
        if(fields.length!=7){
            return null;
        }
        Panel panel = new Panel(
                Integer.parseInt(fields[0]),
                restore(fields[1]),
                Integer.parseInt(fields[2]),
                Integer.parseInt(fields[3]),
                Integer.parseInt(fields[4]),
                Material.valueOf(fields[5]),
                "true".equals(fields[6])

        );
        return panel;
    }

    private String panelToLine(Panel panel){
        StringBuilder buffer = new StringBuilder(200);
        buffer.append(panel.getId()).append(DELIMITER);
        buffer.append(clean(panel.getSection())).append(DELIMITER);
        buffer.append(panel.getRow()).append(DELIMITER);
        buffer.append(panel.getColumn()).append(DELIMITER);
        buffer.append(panel.getInstallationYear()).append(DELIMITER);
        buffer.append(panel.getMaterial()).append(DELIMITER);
        buffer.append(panel.isTracking());
        return buffer.toString();
    }
    private String restore(String value){
        return value.replace(DELIMITER_REPLACEMENT,DELIMITER);
    }
    private String clean(String value){
        return value.replace(DELIMITER, DELIMITER_REPLACEMENT);
    }

    private int getNextId(List<Panel> panels){
        int maxId=0;
        for (Panel panel : panels){
            if (maxId<panel.getId()){
                maxId=panel.getId();
            }
        }
        return maxId+1;
    }


}

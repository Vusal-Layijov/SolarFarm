package solar.ui;

import solar.data.DataException;
import solar.domain.PanelResult;
import solar.domain.PanelService;
import solar.models.Panel;

import java.util.List;

public class Controller {
    private final View view;
    private final PanelService service;
    public Controller(View view, PanelService service) {
        this.view = view;
        this.service = service;
    }

    public void run (){
        view.displayHeader("Welcome to Solar Farm");
        try{
            runMenu();
        } catch (DataException e) {
            view.displayText("Something is wrong");
            view.displayText(e.getMessage());
        }
        view.displayText("GoodBye");
    }
    private void runMenu() throws DataException {
        boolean exit =false;
        while (!exit){
            int selection = view.getMenuOption();
            switch (selection){
                case 0:
                    exit=true;
                    break;
                case 1:
                    viewPanelBySection();
                    break;
                case 2:
                    addPanel();
                    break;
                case 3:
                    updatePanel();
                    break;
                case 4:
                    deletePanel();
                    break;


            }
        }
    }
    private void addPanel() throws DataException {
        Panel panel =view.makePanel();
        PanelResult result = service.add(panel);
        if(result.isSuccess()){
            view.displayText("Your panel was successfully created");
        }else {
            view.displayErrors(result.getMessages());
        }


    }
    private void viewPanelBySection() throws DataException {
        view.displayHeader("Find Panels by Section");
        String section = view.readString("Section name: ");
        List<Panel> panels = service.findBySection(section);
        view.displayPanelsBySection(panels, section);
    }
    private void  updatePanel() throws DataException {
     view.displayHeader("Update a Panel");
        String section = view.readString("Section name: ");
        List<Panel> panels = service.findBySection(section);
        view.displayPanelsBySection(panels, section);
        int row = view.readInt("Choose row",1,250);
        int column = view.readInt("Choose column",1,250);
        Panel firstPanel=view.choosePanel(panels,row,column,section);
        if (firstPanel!=null){
            int id = firstPanel.getId();
            Panel toUpdate=service.findById(id);
            if(toUpdate !=null){
                System.out.printf("Editing %s-%d-%d",section,row,column);

            }
            Panel goUpdate=view.update(toUpdate);
            if(goUpdate !=null){
                goUpdate.setId(id);
                PanelResult result=service.update(goUpdate);
                if(result.isSuccess()){
                    System.out.println("Success");
                    System.out.printf("Panel %s - %d - %d updated",section,row,column);
                } else {
                    view.displayErrors(List.of(String.format("There is no Panel %s - %d - %d ",section,row,column)));
                }
            }
        }else {
            view.displayErrors(List.of(String.format("There is no Panel %s - %d - %d ",section,row,column)));
        }
    }
    private void deletePanel() throws DataException {
        view.displayHeader("Remove a Panel");
        String section = view.readString("Section name: ");
        List<Panel> panels = service.findBySection(section);
        view.displayPanelsBySection(panels, section);
        int row = view.readInt("Choose row",1,250);
        int column = view.readInt("Choose column",1,250);
        Panel firstPanel=view.choosePanel(panels,row,column,section);
        if (firstPanel!=null){
            int id = firstPanel.getId();
            Panel toDelete=service.findById(id);
            if(toDelete!=null){
                PanelResult result= service.deleteById(id);
                if(result.isSuccess()){
                    System.out.println("[Success]");
                    System.out.printf(String.format("Panel %s - %d - %d removed",section,row,column));
                }
            }else {
                view.displayErrors(List.of(String.format("There is no Panel %s - %d - %d ",section,row,column)));
            }
            }else {
            view.displayErrors(List.of(String.format("There is no Panel %s - %d - %d ",section,row,column)));
        }
    }

}

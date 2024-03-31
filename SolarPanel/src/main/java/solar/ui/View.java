package solar.ui;

import solar.models.Material;
import solar.models.Panel;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class View {
    Scanner console = new Scanner(System.in);
    public int getMenuOption(){

        displayText("0.Exit");
        displayText("1.Find Panels by Section");
        displayText("2.Add a Panel");
        displayText("3.Update a Panel");
        displayText("4.Remove a Panel");

        return readInt("Select [0-4] ?",0,4);
    }
    public Panel makePanel(){
        displayHeader("Add a Panel");
        Panel result = new Panel();
        result.setRow(readInt("Enter the row: ",1,250));
        result.setColumn(readInt("Enter the column: ",1,250));
        result.setSection(readString("Enter the section: "));
        result.setMaterial(readMaterial());
        result.setInstallationYear(readInt("Enter the Installation year: ",1,2024));
        result.setTracking(readString("Tracked [y/n]: ").equalsIgnoreCase("y"));
        return result;
    }
    public Panel makePanel2(){
        displayHeader("Update a Panel");
        Panel result = new Panel();
        result.setRow(readInt("Enter the row: ",1,250));
        result.setColumn(readInt("Enter the column: ",1,250));
        result.setSection(readString("Enter the section: "));
        result.setMaterial(readMaterial());
        result.setInstallationYear(readInt("Enter the Installation year: ",1,2024));
        result.setTracking(readString("Tracked [y/n]: ").equalsIgnoreCase("y"));
        return result;
    }
    public void displayPanelsBySection(List<Panel> panels, String section){
        System.out.printf("Panels in The %s%n", section);
        System.out.println("Row Col Year  Material                     Tracking");
        for(Panel panel : panels){
            displayText(String.format("%s    %s %s  %s      %s", panel.getRow(), panel.getColumn(), panel.getInstallationYear(), panel.getMaterial(), panel.isTracking()));
        }

    }
    public Panel choosePanel(List<Panel> panels,int row, int col, String section){

        for (Panel panel : panels){
            if(Objects.equals(panel.getSection(), section) && panel.getRow()==row && panel.getColumn()==col){
                return panel;
            }
        }
        return null;

    }

    public Panel update(Panel panel){
        displayText("Press [Enter] to keep original value. Or press any letter to continue");
        if(panel !=null){
            String string= console.nextLine();
            if(string.isEmpty()){
                System.out.println("Keeping the original panel");
            }else {
                Panel updated=makePanel2();
                return updated;
            }
        }
        return null;
    }
    public void displayHeader(String header){
        System.out.println();
        System.out.println(header);
        System.out.println("=".repeat(header.length()));
    }
    public void displayText(String line){
        System.out.println();
        System.out.println(line);
    }
    public void displayErrors(List<String> errors){
        displayHeader("Errors");
        for (String e:errors){
            displayText(e);
        }
        displayText("");
    }
    public String readString(String prompt){
        displayText(prompt);
        String string= console.nextLine();
        if(string == null || string.isBlank()){
            displayText("You must enter a value");
            string = readString(prompt);
        }
        return string;
    }
    public int readInt(String prompt, int min, int max){
        while (true){
            String value = readString(prompt);
            try {
                int intValue = Integer.parseInt(value);
                if(intValue<min || intValue>max){
                    System.out.println("[Err]");
                    System.out.printf("Sorry, that is not a valid choice. Please choose another number between [%s - %s]%n",min,max);
                }else {
                    return intValue;
                }
            }catch (NumberFormatException ex){
                System.out.printf("%s is not a valid number.%n",value);
            }
        }
    }
    public Material readMaterial(){
       displayText("Materials: ");
       for(Material material : Material.values()){
           displayText(material.getDisplayText().toUpperCase());
       }

        while (true){
            String selection = readString("Enter the material: ");

            try {
                return Material.valueOf(selection);
            }catch (IllegalArgumentException ex){
                System.out.printf("%S is not a material%n", selection);
            }
        }

   }

}

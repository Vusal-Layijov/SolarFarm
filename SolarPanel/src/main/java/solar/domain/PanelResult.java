package solar.domain;

import solar.models.Panel;

import java.util.ArrayList;

public class PanelResult {
    private final ArrayList<String> messages = new ArrayList<>();
    private Panel panel;

    public ArrayList<String> getMessages() {
        return messages;
    }

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    public void addMessage(String message){
        messages.add(message);
    }
    public boolean isSuccess(){
        return messages.size() == 0;

    }
}

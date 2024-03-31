package solar;

import solar.data.PanelFileRepository;
import solar.domain.PanelService;
import solar.ui.Controller;
import solar.ui.View;

public class App {
    public static void main(String[] args) {
        PanelFileRepository repository = new PanelFileRepository("./data/panels.csv");
        PanelService service = new PanelService(repository);
        View view = new View();
        Controller controller= new Controller(view,service);
        controller.run();
    }



}

package hva.app.vaccine;

import hva.core.Hotel;
import java.util.Comparator;
import java.util.List;
import pt.tecnico.uilib.menus.Command;
//FIXME add more imports if needed

/**
 * Show all vaccines.
 **/
class DoShowAllVaccines extends Command<Hotel> {

  DoShowAllVaccines(Hotel receiver) {
    super(Label.SHOW_ALL_VACCINES, receiver);
    //FIXME add command fields
  }
  
  @Override
  protected final void execute() {
    List<String> list = _receiver.getAllVaccinesList();
    list.sort(Comparator.comparing(s -> s.split("\\|")[1].trim()));

    for(String f: list){
      _display.addLine(f);
    }
    _display.display();
  }
}

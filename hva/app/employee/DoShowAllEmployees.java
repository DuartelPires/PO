package hva.app.employee;

import hva.core.Hotel;
import java.util.Comparator;
import java.util.List;
import pt.tecnico.uilib.menus.Command;
//FIXME add more imports if needed

/**
 * Show all employees of this zoo hotel.
 **/
class DoShowAllEmployees extends Command<Hotel> {

  DoShowAllEmployees(Hotel receiver) {
    super(Label.SHOW_ALL_EMPLOYEES, receiver);
  }
  
  @Override
  protected void execute() {
    List<String> list = _receiver.getAllEmployees();
    list.sort(Comparator.comparing(s -> s.split("\\|")[1].trim(), String::compareToIgnoreCase));

    for(String f: list){
      _display.addLine(f);
    }
    _display.display();
  }
}

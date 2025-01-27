package hva.app.main;

import hva.core.HotelManager;
import hva.core.exception.MissingFileAssociationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
// FIXME add more imports if needed

/**
 * Save to file under current name (if unnamed, query for name).
 */
class DoSaveFile extends Command<HotelManager> {
  DoSaveFile(HotelManager receiver) {
    super(Label.SAVE_FILE, receiver, r -> r.getHotel() != null);
  }

  @Override
  protected final void execute() {
    try{
      _receiver.save();
    } catch (MissingFileAssociationException | FileNotFoundException mfe){
      boolean saved = false;
      while(!saved){
        try{
          Form form = new Form();
          form.addStringField("nameOfFile", Prompt.newSaveAs());
          form.parse();
          String fileName = form.stringField("nameOfFile");
          saved = true;
          _receiver.saveAs(fileName);
          
        } catch (MissingFileAssociationException | IOException e){

        }
      }
    }catch (IOException ioe){
      ioe.printStackTrace();  
    }
  }
}
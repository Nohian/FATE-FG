package personajes;
 

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import utiles.Imagen;

public class Jeanne extends personajePrefab {
  
    public Jeanne(){
        spriteImagen = new Imagen("jeanneSheet");
        setAnims();     
    }

@Override
public void setAnims() {
    
    super.setAnims();
}

@Override
public Object getValue(String key) {
    
    return null;
}

@Override
public void putValue(String key, Object value) {
    
    
}

@Override
public void setEnabled(boolean b) {
    
    
}

@Override
public boolean isEnabled() {
    
    return false;
}

@Override
public void addPropertyChangeListener(PropertyChangeListener listener) {
    
    
}

@Override
public void removePropertyChangeListener(PropertyChangeListener listener) {
    
    
}

@Override
public void actionPerformed(ActionEvent e) {
    
    
}
    
}

package Online;

import java.io.IOException;
 
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.PatternSyntaxException;

import com.badlogic.gdx.Gdx;

import Entradas.Direcciones;
import Screens.Background;
import Screens.MenuPrincipal;
import Screens.SeleccionEscenarios;
import Screens.SeleccionPJ;
import Screens.Batalla.Escenarios;
import utiles.Config;
import utiles.Render;
 

public class HiloCliente extends Thread {
    DatagramSocket s;
    private boolean err=false, prmera=true;
    private InetAddress ipserver;
    private int puerto = 8080; 
    private Direcciones dir;
    private int idcliente;
    private String parte1;//partes del string
    private String parte2;
    private int cont,cont2;
    private String parte3;
    private String parte4;
    private int nmb;
    Enumeration e;
    Enumeration ee;
    public HiloCliente(){
         
        try 
        { e = NetworkInterface.getNetworkInterfaces(); 
            while(e.hasMoreElements())
            {
    NetworkInterface n = (NetworkInterface) e.nextElement();
    Enumeration ee = n.getInetAddresses();
    while (ee.hasMoreElements())
    {
          ipserver = (InetAddress) ee.nextElement();
         
    }
            }
            s= new DatagramSocket();
            
            System.out.println(ipserver);
            enviarMensaje(Direcciones.CONECTAR.getString());
        } catch (SocketException e) {
             
            e.printStackTrace();
        }
         
    }
    @Override
    public void run(){
       
        while(!err){
            byte[] a = new byte[1024];
            DatagramPacket dp = new DatagramPacket(a, a.length);
            try {
                s.receive(dp);
                procesarMensaje(dp);
               

                
            } catch (IOException e) {
                 
                e.printStackTrace();
            }
            
            
        }

    }
    private void llamarEvento() {
          
            if(dir.isActive()){
                
                for (int i = 0; i < Config.getListInput().size(); i++) {
                   Config.getListInput().get(i).handleInput();
                 
                  
             
                }
                
                
          }
      
        
    }
    public void identificarMensaje(String msg){
        System.out.println("entro en identifiar mensaje: " +msg);
         
        try {
            String partes[]= msg.split("<>"); 

            parte1= partes[0];
            parte2= partes[1];
       if (parte1.contains(",")) {
            String partes2[]=parte1.split(","); 
           
           parte3 = partes2[0];
           parte4=partes2[1];
          
            
            System.out.println("parte 1 "+parte1);
            System.out.println("p2 "+parte2);
            System.out.println("p3 "+parte3); 
            System.out.println("p4 "+parte4); 
            parte1=parte3;
            
            
       }
       
        } catch (PatternSyntaxException e) {       
           
        }
       
        for (int i = 0; i < Direcciones.values().length; i++) {
            
                if (Direcciones.values()[i].getString().equals(parte1)) {
                    dir= Direcciones.values()[i];
                   
                }
            
           
        }
           if (parte1.equals(Direcciones.POSX.getString())) {
               dir=Direcciones.POSX;
                mayonesa(Integer.parseInt(parte4));
                
            }
            if (parte1.equals(Direcciones.POSY.getString())) {
                dir=Direcciones.POSY;
                mayonesa(Integer.parseInt(parte4));
            }
             
            dir.doActive();
    }
     public int getIdcliente() {
         return idcliente;
     }
   
     public void mayonesa(int nmb){
        this.nmb=nmb;
     }
     public int darmayonesa(){
         return nmb;
     }
public boolean MiPropioMensaje(){
        
        if (Integer.parseInt(parte2)!=idcliente) {
                    //System.out.println("llego la id "+parte2+" y el id mio  es "+idcliente);
                    return false;
                    }

        else{
            return true;
        }
    }



    private void procesarMensaje(DatagramPacket dp) {
         
        String msg = new String(dp.getData()).trim();
        if (prmera) {
            primeraConexion(msg);
            prmera=false;
            System.out.println("primera"+ idcliente);
        }
        else if(!prmera){
           
            identificarMensaje(msg);
            
          decidirAccion();
          llamarEvento();
        }
    }
    private void primeraConexion(String msg) {
        if (Integer.parseInt(msg)==0) {
            idcliente=0;
        }
        else if(Integer.parseInt(msg)==1){
           idcliente=1;
        }
    }
    public Direcciones getDir() {
        return dir;
    }
    public void decidirAccion(){
        switch (dir) { 
            case SELECCIONPJ:
             
               
               Config.ONLINE=true;
               Gdx.app.postRunnable(new Runnable() {
                public void run(){
                    System.out.println("creando selecPJ....");
                    Render.app.setScreen(Direcciones.SELECCIONPJ.getClase());
   
                }
            });

            break;
            case HP:
            dir.HPaRestar(Integer.parseInt(parte4));
          
            break;
           case CERRAR:
           Gdx.app.postRunnable(new Runnable() {
            public void run(){
                System.out.println("Volviendo al menu...");
                Render.app.setScreen(new MenuPrincipal());

            }
        });
        this.interrupt();

           break;
            case SELECCIONESCENARIOS:
             
            if (cont==0) {
                Config.eraseInput(Config.getListInput().get(0));
                Gdx.app.postRunnable(new Runnable() {
                public void run(){
                    System.out.println("creando selecesc....");
                    Render.app.setScreen(new SeleccionEscenarios(cliente.getJ1(),cliente.getJ2()));
          
                }
            });
            }
            cont++;
            
            
            
            break;
            case ESCENARIOS:
             
                if (cont2==0) {

                    System.out.println("voy a hacer algo con escenario");
                    Config.eraseInput(Config.getListInput().get(0));
            Gdx.app.postRunnable(new Runnable() {
                public void run(){
                    Render.app.setScreen(new Escenarios(Background.values()[SeleccionEscenarios.getOpc()].getRoot(), cliente.getJ1(), cliente.getJ2()));
                }
            });
                }
                  
             cont2++;
            
            
            break;
            case PELEATERMINADA:
            Config.eraseInput(Config.getListInput().get(0));
            Gdx.app.postRunnable(new Runnable() {
                public void run(){
                    Render.app.setScreen(new SeleccionEscenarios(cliente.getJ1(),cliente.getJ2()));
                }
            });
            break;
            default:
            break;
        }




      
       }
    
    public void enviarMensaje(String msg){
        System.out.println("este mensaje se va a enviar " + msg);
        byte[] data = msg.getBytes();
        
        try {
      
            DatagramPacket dp = new DatagramPacket(data, data.length,ipserver,puerto);
            s.send(dp);
        } catch (IOException e) {
          
            e.printStackTrace();
        }  
        
        
    }
    
    public int getPuerto() {
        return puerto;
    }
}

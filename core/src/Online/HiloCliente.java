package Online;

import java.io.IOException;
 
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
 
import java.util.regex.PatternSyntaxException;

import com.badlogic.gdx.Gdx;

import Entradas.Direcciones;
import Screens.Background;
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

    public HiloCliente(){
        
        try { 
            s= new DatagramSocket();
            ipserver = InetAddress.getByName("26.64.240.173");
            System.out.println(ipserver);
            enviarMensaje(Direcciones.CONECTAR.getString());
        } catch (SocketException | UnknownHostException e) {
             
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
       

        } catch (PatternSyntaxException e) {
         parte1= msg;
         System.out.println("entre en el catch " + parte1);
        }
       
        for (int i = 0; i < Direcciones.values().length; i++) {
              
            if (Direcciones.values()[i].getString().equals(parte1)) {
                dir= Direcciones.values()[i];
               
            }
        }
            
            dir.doActive();
    }
     public int getIdcliente() {
         return idcliente;
     }
public boolean MiPropioMensaje(){
        
        if (Integer.parseInt(parte2)!=idcliente) {
                    System.out.println("llego el nro "+parte2+" y el idcliente es "+idcliente);
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




        //     case ENTER:
        //     Direcciones.ENTER.doActive();
        //     break;
        //     case ARRIBA:
        //     Direcciones.ARRIBA.doActive();
        //     break;
        //     case ABAJO:
        //     Direcciones.ABAJO.doActive();
        //     break;
        //     case DERECHA:
             
        //     Direcciones.DERECHA.doActive();
        //     break;
        //     case IZQUIERDA:
 
        //     dir.doActive();
        //     break;
        //     case ATAQUED:
        //     Direcciones.ATAQUED.doActive();
        //     break;
        //     case ATAQUEM:
        //     Direcciones.ATAQUEM.doActive();
        //     break;
            
        //     case ATAQUEF:
        //     Direcciones.ATAQUEF.doActive();
        //     break;
        //     case AGACHARSE:
        //     Direcciones.AGACHARSE.doActive();
        //     break;
        //    default:
           
        //        break;
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
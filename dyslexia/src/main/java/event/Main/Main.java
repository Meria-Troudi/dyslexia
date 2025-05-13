package event.Main ;
import event.Events_Attributs.Events;
import event.Participation.Participation_Attributs;
import event.Services.Events_Services;
import event.Services.Participation_Services;
import event.events.MyDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        Events_Services es =new Events_Services();
        Participation_Services ps =new Participation_Services();


        MyDB.getInstance().getConn();
       /* Events e1 = new Events("title1",
                "d1","2002-01-24",
                "00:00","tunisie",
                "t1",01);*/

        Events e2 = new Events(8,"title5555",
                "d44444","2002-01-24",
                "00:00","tunisie",
                "t1",01);
       // int id_eventss =1 ;

        Participation_Attributs p1 = new Participation_Attributs(1,
                "John",
                "Doe",
                "john.doe@example.com",
                "0123456789",
                "Commentaire de test");
        try {
            es.Create(e2);
            ps.Create(p1);
          /* System.out.println(es.Display());
           //es.Update(e2);
           es.Delete(41);
          System.out.println("Suppression de l'ID " + id_eventss + " effectuée.");*/
           // es.Delete(42);
           // es.Delete(45);


        }catch (Exception e) {
            System.out.println(e.getMessage());
            Participation_Services participationsService = new Participation_Services();}
    }}

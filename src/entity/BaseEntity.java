package entity;

import java.time.LocalDateTime;

public abstract class BaseEntity {
   private static long idCounter=1;
   private final long id;
   private final LocalDateTime creationTime;

   public BaseEntity(){
       this.id= idCounter++;
       this.creationTime=LocalDateTime.now();
   }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }
}

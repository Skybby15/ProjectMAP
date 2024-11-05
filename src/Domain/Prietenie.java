package Domain;

import java.time.LocalDateTime;


public class Prietenie extends Entity<Tuple<Long,Long>> {

    LocalDateTime date;

    public Prietenie(LocalDateTime friendDate) {
        date = friendDate;
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "[ Id:( "+getId().getLeft()+" ; "+getId().getRight()+" ) date: "+this.date+" ]";
    }
}

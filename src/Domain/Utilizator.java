package Domain;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Utilizator extends Entity<Long>{
    private String firstName;
    private String lastName;
    private List<Utilizator> friends;
    public static AtomicLong idGenerator = new AtomicLong(0);

    public Utilizator(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.setId(idGenerator.incrementAndGet()-1);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Utilizator> getFriends() {
        return friends;
    }

    @Override
    public String toString() {
        return  "id='" + getId() + '\''+
                ",firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", friends=" + friends ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator that)) return false;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }
}
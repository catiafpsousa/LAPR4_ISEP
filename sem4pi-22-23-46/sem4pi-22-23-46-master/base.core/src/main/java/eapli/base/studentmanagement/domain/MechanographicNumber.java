package eapli.base.studentmanagement.domain;


import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.domain.model.ValueObject;

import javax.persistence.*;
import java.io.Serializable;

import java.util.*;


@Embeddable
public class MechanographicNumber implements ValueObject, Serializable, Comparable<MechanographicNumber> {

    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id = 1;
    private String year;
    private String mechano;


////    @OneToOne
//    private Student user;
//
//    public Student getStudent() {
//        return user;
//    }
//
//    public void setStudent(Student user) {
//        this.user = user;
//    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mechano, year);
    }

    public MechanographicNumber(SystemUser user, long size) {
        this.id = (this.id + size);
        this.year = String.valueOf(user.createdOn().getWeekYear());

        // Append the sequential number to the mechanographic number
        String sequentialNumberString = String.format("%06d", id);

        List<String> liststr = Arrays.asList(year, sequentialNumberString); //List of String array


        this.mechano = String.join("", liststr);
    }

    public MechanographicNumber(String year, long id) {

        // Append the sequential number to the mechanographic number
        String sequentialNumberString = String.format("%06d", id);
        this.id = id;
        this.year = year;
        List<String> liststr = Arrays.asList(year, sequentialNumberString); //List of String array
        this.mechano = String.join("", liststr);
    }


    public MechanographicNumber() {
     // for ORM
    }

    /**
     * @return the mechanographic number
     */
    public String getNumber() {
        return mechano;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MechanographicNumber)) {
            return false;
        }

        final var mecNumber = (MechanographicNumber) o;

        if (!mechano.equals(mecNumber.getNumber())) {
            return false;
        }
        return mechano.equals(mecNumber.getNumber());
    }

    @Override
    public String toString() {
        return mechano;
    }


    public static MechanographicNumber valueOf(final String mechanographicNumber) {
        String year = mechanographicNumber.substring(0, 4);
        long id = Long.parseLong(mechanographicNumber.substring(4));
        return new MechanographicNumber(year, id);
    }


    @Override
    public int compareTo(final MechanographicNumber arg0) {
        return mechano.compareTo(arg0.getNumber());
    }
}


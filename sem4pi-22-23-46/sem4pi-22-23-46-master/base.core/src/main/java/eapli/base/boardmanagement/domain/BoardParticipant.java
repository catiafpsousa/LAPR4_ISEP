package eapli.base.boardmanagement.domain;

import eapli.base.infrastructure.authz.domain.model.SystemUser;
import validations.util.Preconditions;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table (name = "boardparticipant")
public class BoardParticipant implements Serializable, Comparable <BoardParticipant>{
    @Id
    @Column(name = "IDBOARDPARTICIPANT")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idBoardParticipant;

    @ManyToOne
    private SystemUser user;

    @Enumerated(EnumType.STRING)
    private BoardPermission permission;

    protected BoardParticipant(){
        //ORM
    }

    public BoardParticipant(SystemUser user, BoardPermission permission){
        Preconditions.noneNull(user);
        Preconditions.noneNull(permission);
        this.user=user;
        this.permission=permission;
    }

    public SystemUser user(){
        return this.user;
    }

    public BoardPermission permission(){
        return this.permission;
    }

    public void updatePermission(BoardPermission newPermission){
        this.permission=newPermission;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        BoardParticipant other = (BoardParticipant) obj;
        return this.user.identity().equals(other.user.identity()) && this.permission == other.permission;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.identity(), permission);
    }

    @Override
    public int compareTo(BoardParticipant other) {
        return this.user.identity().compareTo(other.user.identity());
    }
}

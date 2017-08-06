package cz.rudypokorny.expense.model;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Created by Rudolf on 01/03/17.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable implements Serializable {

    @Column(updatable = false, nullable = false)
    @CreatedDate
    protected ZonedDateTime createdDate;

    @Column(updatable = false, nullable = false)
    @LastModifiedDate
    protected ZonedDateTime updatedDate;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false, nullable = false)
    protected User createdBy;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public ZonedDateTime getUpdatedDate() {
        return updatedDate;
    }

    void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
}
